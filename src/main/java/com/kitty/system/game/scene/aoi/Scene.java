package com.kitty.system.game.scene.aoi;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.logs.LoggerFunction;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.broadcast.BroadcastService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.service.TeamService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;

public class Scene extends AOI {
  private static final Logger logger = LoggerFunction.SCENE.getLogger();
  
  private BroadcastService service = SpringUtils.getBroadcastService();
  
  private RoleService roleService = SpringUtils.getRoleService();
  
  private TeamService teamService = SpringUtils.getTeamService();
  
  private static final int DISTANCE_X = 75;
  
  private static final int DISTANCE_Y = 25;
  
  private Node head_;
  
  private Node tail_;
  
  private Map<Long, Node> allNodes = new HashMap<>();
  
  public Scene() {
    this.head_ = new Node(Long.MIN_VALUE, 0, 0);
    this.tail_ = new Node(Long.MAX_VALUE, 0, 0);
    this.head_.xNext = this.tail_;
    this.head_.yNext = this.tail_;
    this.tail_.xPrev = this.head_;
    this.tail_.yPrev = this.head_;
  }
  
  private void addActor(Node node) {
    this.allNodes.put(Long.valueOf(node.id), node);
    Node cur = this.head_.xNext;
    while (cur != null) {
      if (cur.x > node.x || cur == this.tail_) {
        node.xNext = cur;
        node.xPrev = cur.xPrev;
        cur.xPrev.xNext = node;
        cur.xPrev = node;
        break;
      } 
      cur = cur.xNext;
    } 
    cur = this.head_.yNext;
    while (cur != null) {
      if (cur.y > node.y || cur == this.tail_) {
        node.yNext = cur;
        node.yPrev = cur.yPrev;
        cur.yPrev.yNext = node;
        cur.yPrev = node;
        break;
      } 
      cur = cur.yNext;
    } 
  }
  
  private Node getNodeByRole(Role role) {
    Node node = this.allNodes.get(role.getId());
    if (node == null) {
      node = new Node(role.getId().longValue(), role.getPos().getX().intValue(), role.getPos().getY().intValue());
      addActor(node);
    } 
    return node;
  }
  
  private void leave(Node node) {
    if (node.xPrev != null)
      node.xPrev.xNext = node.xNext; 
    if (node.xNext != null)
      node.xNext.xPrev = node.xPrev; 
    if (node.yPrev != null)
      node.yPrev.yNext = node.yNext; 
    if (node.yNext != null)
      node.yNext.yPrev = node.yPrev; 
    node.xPrev = null;
    node.xNext = null;
    node.yPrev = null;
    node.yNext = null;
  }
  
  private Set<Long> getVisualObjects(Node node) {
    Set<Long> visuals = new HashSet<>();
    if (node == null) {
      logger.error("node is null:[{}]", (new RuntimeException()).getMessage());
      return visuals;
    } 
    Node cur = node.xNext;
    while (cur != this.tail_ && 
      Math.abs(cur.x - node.x) <= 75) {
      int inteval = Math.abs(node.y - cur.y);
      if (inteval < 25)
        visuals.add(Long.valueOf(cur.id)); 
      cur = cur.xNext;
    } 
    cur = node.xPrev;
    while (cur != this.head_ && 
      Math.abs(cur.x - node.x) <= 75) {
      int inteval = Math.abs(node.y - cur.y);
      if (inteval < 25)
        visuals.add(Long.valueOf(cur.id)); 
      cur = cur.xPrev;
    } 
    return visuals;
  }
  
  public void enter(Role role) {
    Node node = this.allNodes.get(role.getId());
    if (node == null)
      node = getNodeByRole(role); 
    Set<Long> enters = getVisualObjects(node);
    sendAppear(role, enters);
  }
  
  public void leave(long id) {
    Role role = this.roleService.getPlayer(id);
    if (role != null) {
      Node node = getNodeByRole(role);
      Set<Long> leaves = getVisualObjects(node);
      sendDisappear(role, leaves);
      leave(node);
      this.allNodes.remove(Long.valueOf(id));
    } 
  }
  
  public void move(Role role) {
    Node node = getNodeByRole(role);
    Set<Long> oldSet = getVisualObjects(node);
    leave(node);
    node.x = role.getPos().getX().intValue();
    node.y = role.getPos().getY().intValue();
    addActor(node);
    Set<Long> newSet = getVisualObjects(node);
    Set<Long> moves = (Set<Long>)oldSet.stream().filter(newSet::contains).collect(Collectors.toSet());
    Set<Long> enters = (Set<Long>)newSet.stream().filter(id -> !moves.contains(id)).collect(Collectors.toSet());
    Set<Long> leaves = (Set<Long>)oldSet.stream().filter(id -> !moves.contains(id)).collect(Collectors.toSet());
    sendAppear(role, enters);
    sendMove(role, moves);
    sendDisappear(role, leaves);
  }
  
  public void goTo(Role role) {
    Node node = getNodeByRole(role);
    Set<Long> oldSet = getVisualObjects(node);
    Set<Long> newSet = getVisualObjects(node);
    Set<Long> moves = (Set<Long>)oldSet.stream().filter(newSet::contains).collect(Collectors.toSet());
    sendGoTo(role, moves);
  }
  
  public void sendGoTo(Role role, Set<Long> moves) {
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (moves.size() <= 0 || team == null)
      return; 
    Role leader = this.roleService.getPlayerBy(team.getLeaderRoleId());
    Message moveGoto = this.service.getMoveGoto(role.getRoleId().intValue(), role.getPos().getX().intValue(), role.getPos().getY().intValue(), leader.getPos().getMapId().intValue());
    MessagePusher.pushMessage(role, moveGoto);
    for (Iterator<Long> iterator = moves.iterator(); iterator.hasNext(); ) {
      long targetId = ((Long)iterator.next()).longValue();
      Role target = this.roleService.getPlayer(targetId);
      if (target != null && this.service.inSameScene(role, target))
        MessagePusher.pushMessage(target, moveGoto); 
    } 
  }
  
  public void sendMessages(Role role, Message... messages) {
    Node node = getNodeByRole(role);
    Set<Long> ids = getVisualObjects(node);
    sendMessages(role, ids, messages);
  }
  
  public void giveAppear(Role role, Set<Long> enters) {
    sendAppear(role, enters);
  }
  
  private void sendMessages(Role role, Set<Long> ids, Message... messages) {
    MessagePusher.pushMessages(role, messages);
    if (ids.size() <= 0)
      return; 
    for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext(); ) {
      long targetId = ((Long)iterator.next()).longValue();
      Role target = this.roleService.getPlayer(targetId);
      if (target != null && this.service.inSameScene(role, target))
        MessagePusher.pushMessages(target, messages); 
    } 
  }
  
  private void sendMove(Role selfRole, Set<Long> moves) {
    Message movePacket = this.service.getMovePacket(selfRole);
    MessagePusher.pushMessage(selfRole, movePacket);
    if (moves.size() <= 0)
      return; 
    for (Iterator<Long> iterator = moves.iterator(); iterator.hasNext(); ) {
      long targetId = ((Long)iterator.next()).longValue();
      Role target = this.roleService.getPlayer(targetId);
      if (target != null && this.service.inSameScene(selfRole, target))
        if (this.teamService.getTeam(selfRole.getRoleId().intValue()) == null || this.teamService.isTeamLeader(selfRole) || !this.teamService.isInTeam(selfRole))
          MessagePusher.pushMessage(target, movePacket);  
    } 
  }
  
  private void sendAppear(Role selfRole, Set<Long> enters) {
    Message appear = this.service.getAppear(selfRole);
    Message titleInfo = this.service.getTitleInfoNew(selfRole);
    MessagePusher.pushMessage(selfRole, titleInfo);
    if (enters.size() <= 0)
      return; 
    for (Iterator<Long> iterator = enters.iterator(); iterator.hasNext(); ) {
      long targetId = ((Long)iterator.next()).longValue();
      Role target = this.roleService.getPlayer(targetId);
      if (target != null && this.service.inSameScene(selfRole, target)) {
        Message oAppear = this.service.getAppear(target);
        Message oTitleInfo = this.service.getTitleInfoNew(target);
        MessagePusher.pushMessage(selfRole, oAppear);
        MessagePusher.pushMessage(selfRole, oTitleInfo);
        MessagePusher.pushMessage(target, appear);
        MessagePusher.pushMessage(target, titleInfo);
      } 
    } 
  }
  
  private void sendDisappear(Role selfRole, Set<Long> leaves) {
    if (leaves.size() <= 0)
      return; 
    Message disAppear = this.service.getDisAppear(selfRole);
    for (Iterator<Long> iterator = leaves.iterator(); iterator.hasNext(); ) {
      long targetId = ((Long)iterator.next()).longValue();
      Role target = this.roleService.getPlayer(targetId);
      if (target != null) {
        MessagePusher.pushMessage(target, disAppear);
        MessagePusher.pushMessage(selfRole, this.service.getDisAppear(target));
      } 
    } 
  }
}
