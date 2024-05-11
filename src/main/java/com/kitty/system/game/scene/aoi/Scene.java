
package com.kitty.system.game.scene.aoi;

import com.kitty.logs.LoggerFunction;
import java.util.Iterator;
import com.kitty.mina.message.Message;
import com.kitty.system.game.team.domain.Team;
import com.kitty.mina.message.MessagePusher;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Predicate;
import java.util.HashSet;
import java.util.Set;
import com.kitty.system.game.role.domain.Role;
import java.util.HashMap;
import com.kitty.common.utils.spring.SpringUtils;
import java.util.Map;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.broadcast.BroadcastService;
import org.slf4j.Logger;

public class Scene extends AOI
{
    private static final Logger logger;
    private BroadcastService service;
    private RoleService roleService;
    private TeamService teamService;
    private static final int DISTANCE_X = 75;
    private static final int DISTANCE_Y = 25;
    private Node head_;
    private Node tail_;
    private Map<Long, Node> allNodes;
    
    public Scene() {
        this.service = SpringUtils.getBroadcastService();
        this.roleService = SpringUtils.getRoleService();
        this.teamService = SpringUtils.getTeamService();
        this.allNodes = new HashMap<Long, Node>();
        this.head_ = new Node(Long.MIN_VALUE, 0, 0);
        this.tail_ = new Node(Long.MAX_VALUE, 0, 0);
        this.head_.xNext = this.tail_;
        this.head_.yNext = this.tail_;
        this.tail_.xPrev = this.head_;
        this.tail_.yPrev = this.head_;
    }
    
    private void addActor(final Node node) {
        this.allNodes.put(node.id, node);
        for (Node cur = this.head_.xNext; cur != null; cur = cur.xNext) {
            if (cur.x > node.x || cur == this.tail_) {
                node.xNext = cur;
                node.xPrev = cur.xPrev;
                cur.xPrev.xNext = node;
                cur.xPrev = node;
                break;
            }
        }
        for (Node cur = this.head_.yNext; cur != null; cur = cur.yNext) {
            if (cur.y > node.y || cur == this.tail_) {
                node.yNext = cur;
                node.yPrev = cur.yPrev;
                cur.yPrev.yNext = node;
                cur.yPrev = node;
                break;
            }
        }
    }
    
    private Node getNodeByRole(final Role role) {
        Node node = this.allNodes.get(role.getId());
        if (node == null) {
            node = new Node(role.getId(), role.getPos().getX(), role.getPos().getY());
            this.addActor(node);
        }
        return node;
    }
    
    private void leave(final Node node) {
        if (node.xPrev != null) {
            node.xPrev.xNext = node.xNext;
        }
        if (node.xNext != null) {
            node.xNext.xPrev = node.xPrev;
        }
        if (node.yPrev != null) {
            node.yPrev.yNext = node.yNext;
        }
        if (node.yNext != null) {
            node.yNext.yPrev = node.yPrev;
        }
        node.xPrev = null;
        node.xNext = null;
        node.yPrev = null;
        node.yNext = null;
    }
    
    private Set<Long> getVisualObjects(final Node node) {
        final Set<Long> visuals = new HashSet<Long>();
        if (node == null) {
            Scene.logger.error("node is null:[{}]", (Object)new RuntimeException().getMessage());
            return visuals;
        }
        for (Node cur = node.xNext; cur != this.tail_ && Math.abs(cur.x - node.x) <= 75; cur = cur.xNext) {
            final int inteval = Math.abs(node.y - cur.y);
            if (inteval < 25) {
                visuals.add(cur.id);
            }
        }
        for (Node cur = node.xPrev; cur != this.head_ && Math.abs(cur.x - node.x) <= 75; cur = cur.xPrev) {
            final int inteval = Math.abs(node.y - cur.y);
            if (inteval < 25) {
                visuals.add(cur.id);
            }
        }
        return visuals;
    }
    
    @Override
    public void enter(final Role role) {
        Node node = this.allNodes.get(role.getId());
        if (node == null) {
            node = this.getNodeByRole(role);
        }
        final Set<Long> enters = this.getVisualObjects(node);
        this.sendAppear(role, enters);
    }
    
    @Override
    public void leave(final long id) {
        final Role role = this.roleService.getPlayer(id);
        if (role != null) {
            final Node node = this.getNodeByRole(role);
            final Set<Long> leaves = this.getVisualObjects(node);
            this.sendDisappear(role, leaves);
            this.leave(node);
            this.allNodes.remove(id);
        }
    }
    
    @Override
    public void move(final Role role) {
        final Node node = this.getNodeByRole(role);
        final Set<Long> oldSet = this.getVisualObjects(node);
        this.leave(node);
        node.x = role.getPos().getX();
        node.y = role.getPos().getY();
        this.addActor(node);
        final Set<Long> newSet = this.getVisualObjects(node);
        final Set<Long> moves = oldSet.stream().filter(newSet::contains).collect(Collectors.toSet());
        final Set<Long> enters = newSet.stream().filter(id -> !moves.contains(id)).collect(Collectors.toSet());
        final Set<Long> leaves = oldSet.stream().filter(id -> !moves.contains(id)).collect(Collectors.toSet());
        this.sendAppear(role, enters);
        this.sendMove(role, moves);
        this.sendDisappear(role, leaves);
    }
    
    @Override
    public void goTo(final Role role) {
        final Node node = this.getNodeByRole(role);
        final Set<Long> oldSet = this.getVisualObjects(node);
        final Set<Long> newSet = this.getVisualObjects(node);
        final Set<Long> moves = oldSet.stream().filter(newSet::contains).collect(Collectors.toSet());
        this.sendGoTo(role, moves);
    }
    
    public void sendGoTo(final Role role, final Set<Long> moves) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (moves.size() <= 0 || team == null) {
            return;
        }
        final Role leader = this.roleService.getPlayerBy(team.getLeaderRoleId());
        final Message moveGoto = this.service.getMoveGoto(role.getRoleId(), role.getPos().getX(), role.getPos().getY(), leader.getPos().getMapId());
        MessagePusher.pushMessage(role, moveGoto);
        for (final long targetId : moves) {
            final Role target = this.roleService.getPlayer(targetId);
            if (target != null && this.service.inSameScene(role, target)) {
                MessagePusher.pushMessage(target, moveGoto);
            }
        }
    }
    
    @Override
    public void sendMessages(final Role role, final Message... messages) {
        final Node node = this.getNodeByRole(role);
        final Set<Long> ids = this.getVisualObjects(node);
        this.sendMessages(role, ids, messages);
    }
    
    @Override
    public void giveAppear(final Role role, final Set<Long> enters) {
        this.sendAppear(role, enters);
    }
    
    private void sendMessages(final Role role, final Set<Long> ids, final Message... messages) {
        MessagePusher.pushMessages(role, messages);
        if (ids.size() <= 0) {
            return;
        }
        for (final long targetId : ids) {
            final Role target = this.roleService.getPlayer(targetId);
            if (target != null && this.service.inSameScene(role, target)) {
                MessagePusher.pushMessages(target, messages);
            }
        }
    }
    
    private void sendMove(final Role selfRole, final Set<Long> moves) {
        final Message movePacket = this.service.getMovePacket(selfRole);
        MessagePusher.pushMessage(selfRole, movePacket);
        if (moves.size() <= 0) {
            return;
        }
        for (final long targetId : moves) {
            final Role target = this.roleService.getPlayer(targetId);
            if (target != null && this.service.inSameScene(selfRole, target) && (this.teamService.getTeam(selfRole.getRoleId()) == null || this.teamService.isTeamLeader(selfRole) || !this.teamService.isInTeam(selfRole))) {
                MessagePusher.pushMessage(target, movePacket);
            }
        }
    }
    
    private void sendAppear(final Role selfRole, final Set<Long> enters) {
        final Message appear = this.service.getAppear(selfRole);
        final Message titleInfo = this.service.getTitleInfoNew(selfRole);
        MessagePusher.pushMessage(selfRole, titleInfo);
        if (enters.size() <= 0) {
            return;
        }
        for (final long targetId : enters) {
            final Role target = this.roleService.getPlayer(targetId);
            if (target != null && this.service.inSameScene(selfRole, target)) {
                final Message oAppear = this.service.getAppear(target);
                final Message oTitleInfo = this.service.getTitleInfoNew(target);
                MessagePusher.pushMessage(selfRole, oAppear);
                MessagePusher.pushMessage(selfRole, oTitleInfo);
                MessagePusher.pushMessage(target, appear);
                MessagePusher.pushMessage(target, titleInfo);
            }
        }
    }
    
    private void sendDisappear(final Role selfRole, final Set<Long> leaves) {
        if (leaves.size() <= 0) {
            return;
        }
        final Message disAppear = this.service.getDisAppear(selfRole);
        for (final long targetId : leaves) {
            final Role target = this.roleService.getPlayer(targetId);
            if (target != null) {
                MessagePusher.pushMessage(target, disAppear);
                MessagePusher.pushMessage(selfRole, this.service.getDisAppear(target));
            }
        }
    }
    
    static {
        logger = LoggerFunction.SCENE.getLogger();
    }
}
