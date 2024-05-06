package com.kitty.system.game.friend.service;

import com.kitty.cache.RedisService;
import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.Db4CommonService;
import com.kitty.common.thread.IdWorker;
import com.kitty.common.utils.CheckCharUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.LoginEvent;
import com.kitty.listener.event.LogoutEvent;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.utils.FieldUtils;
import com.kitty.system.game.chat.domain.OfflineMessage;
import com.kitty.system.game.chat.domain.RoleOfflineMessage;
import com.kitty.system.game.chat.message.RespChat;
import com.kitty.system.game.friend.domain.PalFriend;
import com.kitty.system.game.friend.domain.PalFriendGroup;
import com.kitty.system.game.friend.domain.PalNotify;
import com.kitty.system.game.friend.message.ReqAcceptRequest;
import com.kitty.system.game.friend.message.ReqAddFriend;
import com.kitty.system.game.friend.message.ReqAddFriendGroup;
import com.kitty.system.game.friend.message.ReqAlterFriend;
import com.kitty.system.game.friend.message.ReqAlterFriendGroup;
import com.kitty.system.game.friend.message.ReqSendFriendMsg;
import com.kitty.system.game.friend.message.ReqSortFriendGroup;
import com.kitty.system.game.friend.message.RespDelFriend;
import com.kitty.system.game.friend.message.RespDelFriendGroup;
import com.kitty.system.game.friend.message.RespFriend;
import com.kitty.system.game.friend.message.RespFriendGroup;
import com.kitty.system.game.friend.message.RespSearchFriend;
import com.kitty.system.game.friend.message.RespVerifyInfo;
import com.kitty.system.game.friend.message.vo.FriendGroup;
import com.kitty.system.game.friend.message.vo.Pla;
import com.kitty.system.game.friend.message.vo.SearchFriend;
import com.kitty.system.game.player.PlayerService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.mina.core.session.IoSession;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendService {
  Logger logger = LoggerFactory.getLogger(FriendService.class);
  
  @Autowired
  Dao dao;
  
  @Autowired
  IdWorker worker;
  
  @Autowired
  RoleService roleService;
  
  @Autowired
  private RedisService redisService;
  
  @Autowired
  PlayerService playerService;
  
  public void loadPal(Role role) {
    List<PalFriendGroup> friendGroupList = this.dao.query(PalFriendGroup.class, (Condition)Cnd.where("r_id", "=", role.getId()));
    Map<Long, PalFriendGroup> friendGroupMap = (Map<Long, PalFriendGroup>)friendGroupList.stream().collect(Collectors.toMap(PalFriendGroup::getId, palFriendGroup -> palFriendGroup));
    role.setFriendGroupMap(friendGroupMap);
    List<PalFriend> palFriendList = this.dao.query(PalFriend.class, (Condition)Cnd.where("r_id", "=", role.getId()));
    Map<Long, PalFriend> palFriendMap = new HashMap<>();
    for (PalFriend palFriend : palFriendList) {
      PalFriend friend = palFriendMap.get(Long.valueOf(palFriend.getPalId()));
      if (friend != null) {
        palFriend.setDelete(true);
        Db4CommonService.getInstance().add2Queue((BaseEntity)palFriend);
        continue;
      } 
      palFriendMap.put(Long.valueOf(palFriend.getPalId()), palFriend);
    } 
    role.setFriendMap(palFriendMap);
    List<PalNotify> palNotifyList = this.dao.query(PalNotify.class, (Condition)Cnd.where("att", "=", role.getId()));
    Map<Long, PalNotify> palNotifyMap = (Map<Long, PalNotify>)palNotifyList.stream().collect(Collectors.toMap(PalNotify::getId, palNotify -> palNotify));
    role.setPalNotifyMap(palNotifyMap);
  }
  
  @EventHandler({EventType.LOGOUT})
  public void handleLogoutEvent(LogoutEvent logoutEvent) {
    Role role = logoutEvent.getRole();
    logoutInformFriends(role);
  }
  
  @EventHandler({EventType.LOGIN})
  public void handleLoginEvent(LoginEvent loginEvent) {
    Role role = loginEvent.getRole();
    Map<Long, PalFriendGroup> friendGroupMap = role.getFriendGroupMap();
    RespFriendGroup respFriendGroup = new RespFriendGroup();
    List<FriendGroup> objects = new ArrayList<>();
    for (Long key : friendGroupMap.keySet())
      objects.add(new FriendGroup(friendGroupMap.get(key))); 
    respFriendGroup.setList(objects);
    MessagePusher.pushMessage(role, (Message)respFriendGroup);
    loadFriends(role);
    Map<Long, PalNotify> palNotifyMap = role.getPalNotifyMap();
    for (Long key : palNotifyMap.keySet()) {
      PalNotify notify = palNotifyMap.get(key);
      Role player = this.roleService.getPlayer(notify.getSender());
      if (player != null)
        MessagePusher.pushMessage(role, (Message)new RespVerifyInfo(String.valueOf(notify.getId()), player.getName(), player.getRelive().byteValue(), player.getLevel().shortValue(), player.getSerial().intValue())); 
    } 
  }
  
  public void logoutInformFriends(Role role) {
    Map<Long, PalFriend> friendMap = role.getFriendMap();
    for (Long key : friendMap.keySet()) {
      PalFriend palFriend = friendMap.get(key);
      if (!this.roleService.isOnline(palFriend.getPalId()))
        continue; 
      Role player = this.roleService.getPlayer(palFriend.getPalId());
      if (player != null) {
        String name = role.getName();
        PalFriend friend = (PalFriend)player.getFriendMap().get(role.getId());
        if (friend != null) {
          if (friend.getRemark() != null)
            name = friend.getRemark(); 
        } else {
          this.logger.error("好友信息错误：玩家ID = {}，好友ID = {}", role.getId(), player.getId());
        } 
        List<Pla> list = new ArrayList<>();
        list.add(new Pla(role.getRoleId().intValue(), name, role.getRelive().byteValue(), role.getLevel().shortValue(), role.getSerial().intValue(), (byte)0, String.valueOf(palFriend.getClose()), String.valueOf(palFriend.getGroupId())));
        MessagePusher.pushMessage(player, (Message)new RespFriend((byte)1, list));
      } 
    } 
  }
  
  public void loadFriends(Role role) {
    Map<Long, PalFriend> friendMap = role.getFriendMap();
    List<Pla> list = new ArrayList<>();
    for (Long key : friendMap.keySet()) {
      PalFriend palFriend = friendMap.get(key);
      Role player = getFriendInfo(palFriend.getPalId());
      if (player != null) {
        String name = player.getName();
        if (palFriend.getRemark() != null)
          name = palFriend.getRemark(); 
        byte status = 0;
        if (this.roleService.isOnline(player.getId().longValue())) {
          status = 1;
          onlineInformFriend(role, player);
        } 
        list.add(new Pla(player.getRoleId().intValue(), name, player.getRelive().byteValue(), player.getLevel().shortValue(), player.getSerial().intValue(), status, String.valueOf(palFriend.getClose()), String.valueOf(palFriend.getGroupId())));
      } 
    } 
    MessagePusher.pushMessage(role, (Message)new RespFriend((byte)0, list));
  }
  
  public Role getFriendInfo(long palId) {
    if (DataCache.OFFLINE_ROLES.containsKey(Long.valueOf(palId)))
      return (Role)DataCache.OFFLINE_ROLES.get(Long.valueOf(palId)); 
    if (DataCache.ONLINE_ROLES.containsKey(Long.valueOf(palId)))
      return (Role)DataCache.ONLINE_ROLES.get(Long.valueOf(palId)); 
    Role role = (Role)this.redisService.get(String.valueOf(palId), Role.class);
    if (role == null)
      role = this.roleService.getPlayer(palId); 
    return role;
  }
  
  public void onlineInformFriend(Role role, Role friend) {
    PalFriend palFriend = (PalFriend)friend.getFriendMap().get(role.getId());
    if (palFriend != null) {
      List<Pla> list = new ArrayList<>();
      String name = role.getName();
      if (palFriend.getRemark() != null)
        name = palFriend.getRemark(); 
      list.add(new Pla(role.getRoleId().intValue(), name, role.getRelive().byteValue(), role.getLevel().shortValue(), role.getSerial().intValue(), (byte)1, String.valueOf(palFriend.getClose()), String.valueOf(palFriend.getGroupId())));
      MessagePusher.pushMessage(friend, (Message)new RespFriend((byte)0, list));
      MessagePusher.pushMessage(friend, (Message)new RespMsg("你的好友#<LB1>" + name + "#上线了"));
    } 
  }
  
  public void reqAddBlack(Role role, ReqAddFriend reqAddFriend) {
    Role player = this.roleService.getPlayer(reqAddFriend.getRoleId());
    if (player != null) {
      PalFriend friend = (PalFriend)role.getFriendMap().get(player.getId());
      if (friend == null) {
        friend = new PalFriend();
        friend.setId(Long.valueOf(this.worker.nextId()));
      } 
      friend(friend, role, player, Long.valueOf(reqAddFriend.getGroupId()).longValue(), reqAddFriend.getRemark());
      MessagePusher.pushMessage(role, (Message)new RespMsg(String.format("你已经成功将#<LB1>%s#添加到黑名单", new Object[] { player.getName() })));
      Map<Long, PalFriend> playerFriendMap = player.getFriendMap();
      PalFriend playerFriend = playerFriendMap.get(role.getId());
      if (playerFriend != null)
        delFriend(player, playerFriend, playerFriendMap, role.getRoleId().intValue()); 
    } 
  }
  
  public void reqAddFriend(Role role, ReqAddFriend reqAddFriend) {
    Role player = this.roleService.getPlayer(reqAddFriend.getRoleId());
    if (player != null && this.roleService.isOnline(player.getId().longValue())) {
      PalFriend palFriend = (PalFriend)player.getFriendMap().get(role.getId());
      if (palFriend != null) {
        if (palFriend.getGroupId() == 1L) {
          MessagePusher.pushMessage(role, (Message)new RespMsg("你在对方黑名单，无法申请好友"));
        } else {
          MessagePusher.pushMessage(role, (Message)new RespMsg("对方已经是你的好友"));
        } 
        return;
      } 
      Map<Long, PalNotify> notifyMap = player.getPalNotifyMap();
      PalNotify palNotify = notifyMap.values().stream().filter(e -> (e.getSender() == role.getId().longValue())).findFirst().orElse(null);
      if (palNotify != null) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("申请成功，等待对方同意"));
        return;
      } 
      int currTime = (new Long(System.currentTimeMillis() / 1000L)).intValue();
      palNotify = new PalNotify(Long.valueOf(this.worker.nextId()), (byte)1, role.getId().longValue(), player.getId().longValue(), (byte)1, currTime, Long.valueOf(reqAddFriend.getGroupId()).longValue(), reqAddFriend.getRemark());
      notifyMap.put(palNotify.getId(), palNotify);
      this.playerService.saveSync(player);
      Db4CommonService.getInstance().add2Queue((BaseEntity)palNotify);
      MessagePusher.pushMessage(role, (Message)new RespMsg("申请成功，等待对方同意"));
      MessagePusher.pushMessage(player, (Message)new RespVerifyInfo(String.valueOf(palNotify.getId()), role.getName(), role.getRelive().byteValue(), role.getLevel().shortValue(), role.getSerial().intValue()));
    } 
  }
  
  public void acceptRequest(Role role, ReqAcceptRequest reqAcceptRequest) {
    for (String key : reqAcceptRequest.getList()) {
      long id = Long.valueOf(key).longValue();
      PalNotify notify = (PalNotify)role.getPalNotifyMap().get(Long.valueOf(id));
      if (notify == null) {
        this.logger.error("玩家：{}，通过或者拒绝请求，获取数据为空：{}", role.getRoleId(), key);
        continue;
      } 
      acceptRequest(role, notify, reqAcceptRequest.getAccept());
    } 
    if (reqAcceptRequest.getAccept() == 2)
      MessagePusher.pushMessage(role, (Message)new RespMsg("你清除了好友申请")); 
  }
  
  public void acceptRequest(Role role, PalNotify notify, byte type) {
    if (type == 1) {
      Role player = this.roleService.getPlayer(notify.getSender());
      if (player == null)
        return; 
      if (role.getFriendMap().get(player.getId()) == null) {
        PalFriend friend = new PalFriend();
        friend.setId(Long.valueOf(this.worker.nextId()));
        friend(friend, role, player, 0L, null);
      } 
      MessagePusher.pushMessage(player, (Message)new RespMsg(String.format("#<LB1>%s#已经通过了你的好友申请", new Object[] { role.getName() })));
      if (player.getFriendMap().get(role.getId()) == null) {
        PalFriend friendAtt = new PalFriend();
        friendAtt.setId(Long.valueOf(this.worker.nextId()));
        friend(friendAtt, player, role, notify.getGroupId(), notify.getRemark());
      } 
      MessagePusher.pushMessage(role, (Message)new RespMsg(String.format("成功添加#<LB1>%s#为你的好友", new Object[] { player.getName() })));
    } 
    notify.setDelete(true);
    role.getPalNotifyMap().remove(notify.getId());
    this.playerService.saveSync(role);
    Db4CommonService.getInstance().add2Queue((BaseEntity)notify);
  }
  
  public void friend(PalFriend friend, Role role, Role player, long groupId, String remark) {
    friend.setrId(role.getId().longValue());
    friend.setPalId(player.getId().longValue());
    friend.setGroupId(groupId);
    friend.setClose(0L);
    if (remark == null || remark.isEmpty())
      remark = player.getName(); 
    friend.setRemark(remark);
    role.getFriendMap().put(player.getId(), friend);
    this.playerService.saveSync(role);
    Db4CommonService.getInstance().add2Queue((BaseEntity)friend);
    List<Pla> list = new ArrayList<>();
    String name = player.getName();
    if (friend.getRemark() != null)
      name = friend.getRemark(); 
    byte status = 0;
    if (this.roleService.isOnline(player.getId().longValue()))
      status = 1; 
    list.add(new Pla(player.getRoleId().intValue(), name, player.getRelive().byteValue(), player.getLevel().shortValue(), player.getSerial().intValue(), status, String.valueOf(friend.getClose()), String.valueOf(friend.getGroupId())));
    MessagePusher.pushMessage(role, (Message)new RespFriend((byte)1, list));
  }
  
  public void delFriend(Role role, List<Integer> list) {
    Map<Long, PalFriend> friendMap = role.getFriendMap();
    for (Integer id : list) {
      Role player = this.roleService.getPlayer(id.intValue());
      if (player == null)
        continue; 
      Map<Long, PalFriend> playerFriendMap = player.getFriendMap();
      PalFriend playerFriend = playerFriendMap.get(role.getId());
      if (playerFriend != null)
        delFriend(player, playerFriend, playerFriendMap, role.getRoleId().intValue()); 
      PalFriend friend = friendMap.get(player.getId());
      if (friend == null)
        continue; 
      delFriend(role, friend, friendMap, player.getRoleId().intValue());
    } 
    MessagePusher.pushMessage(role, (Message)new RespMsg("删除好友成功"));
  }
  
  public void delFriend(Role role, PalFriend friend, Map<Long, PalFriend> friendMap, int id) {
    friend.setDelete(true);
    friendMap.remove(Long.valueOf(friend.getPalId()));
    this.playerService.saveSync(role);
    Db4CommonService.getInstance().add2Queue((BaseEntity)friend);
    MessagePusher.pushMessage(role, (Message)new RespDelFriend(id));
  }
  
  public void addFriendGroup(Role role, ReqAddFriendGroup reqAddFriendGroup) {
    if (reqAddFriendGroup.getGroupName().length() < 1)
      return; 
    String groupName = reqAddFriendGroup.getGroupName().trim();
    Map<Long, PalFriendGroup> groupMap = role.getFriendGroupMap();
    PalFriendGroup orElse = groupMap.values().stream().filter(e -> e.getGroupName().equals(groupName)).findFirst().orElse(null);
    if (orElse != null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("已有同名分组，无法使用这个分组名"));
      return;
    } 
    PalFriendGroup friendGroup = new PalFriendGroup(Long.valueOf(this.worker.nextId()), role.getId().longValue(), groupName, (short)0);
    groupMap.put(friendGroup.getId(), friendGroup);
    Db4CommonService.getInstance().add2Queue((BaseEntity)friendGroup);
    RespFriendGroup respFriendGroup = new RespFriendGroup();
    List<FriendGroup> objects = new ArrayList<>();
    objects.add(new FriendGroup(friendGroup));
    respFriendGroup.setType((byte)1);
    respFriendGroup.setList(objects);
    MessagePusher.pushMessage(role, (Message)respFriendGroup);
    if (reqAddFriendGroup.getList().size() > 0) {
      Map<Long, PalFriend> friendMap = role.getFriendMap();
      List<Pla> list = new ArrayList<>();
      for (Integer key : reqAddFriendGroup.getList()) {
        Role player = this.roleService.getPlayer(key.intValue());
        if (player == null)
          continue; 
        PalFriend palFriend = friendMap.get(player.getId());
        if (palFriend == null)
          continue; 
        palFriend.setGroupId(friendGroup.getId().longValue());
        String name = player.getName();
        if (palFriend.getRemark() != null)
          name = palFriend.getRemark(); 
        byte status = 0;
        if (this.roleService.isOnline(player.getId().longValue()))
          status = 1; 
        Db4CommonService.getInstance().add2Queue((BaseEntity)palFriend);
        list.add(new Pla(player.getRoleId().intValue(), name, player.getRelive().byteValue(), player.getLevel().shortValue(), player.getSerial().intValue(), status, String.valueOf(palFriend.getClose()), String.valueOf(palFriend.getGroupId())));
      } 
      if (list.size() > 0)
        MessagePusher.pushMessage(role, (Message)new RespFriend((byte)1, list)); 
    } 
    this.playerService.saveSync(role);
    MessagePusher.pushMessage(role, (Message)new RespMsg("新建成功"));
  }
  
  public void delFriendGroup(Role role, Long id) {
    PalFriendGroup friendGroup = (PalFriendGroup)role.getFriendGroupMap().get(id);
    if (friendGroup == null)
      return; 
    Map<Long, PalFriend> friendMap = role.getFriendMap();
    List<Pla> list = new ArrayList<>();
    for (Long key : friendMap.keySet()) {
      Role player = this.roleService.getPlayer(key.longValue());
      if (player == null)
        continue; 
      PalFriend palFriend = friendMap.get(player.getId());
      if (palFriend == null)
        continue; 
      if (palFriend.getGroupId() == friendGroup.getId().longValue()) {
        palFriend.setGroupId(0L);
        String name = player.getName();
        if (palFriend.getRemark() != null)
          name = palFriend.getRemark(); 
        byte status = 0;
        if (this.roleService.isOnline(player.getId().longValue()))
          status = 1; 
        Db4CommonService.getInstance().add2Queue((BaseEntity)palFriend);
        list.add(new Pla(player.getRoleId().intValue(), name, player.getRelive().byteValue(), player.getLevel().shortValue(), player.getSerial().intValue(), status, String.valueOf(palFriend.getClose()), String.valueOf(palFriend.getGroupId())));
      } 
    } 
    if (list.size() > 0)
      MessagePusher.pushMessage(role, (Message)new RespFriend((byte)1, list)); 
    friendGroup.setDelete(true);
    role.getFriendGroupMap().remove(friendGroup.getId());
    this.playerService.saveSync(role);
    Db4CommonService.getInstance().add2Queue((BaseEntity)friendGroup);
    MessagePusher.pushMessage(role, (Message)new RespDelFriendGroup(String.valueOf(friendGroup.getId())));
    MessagePusher.pushMessage(role, (Message)new RespMsg("删除成功"));
  }
  
  public void sortFriendGroup(Role role, ReqSortFriendGroup sortFriendGroup) {
    Map<Long, PalFriendGroup> groupMap = role.getFriendGroupMap();
    PalFriendGroup friendGroup = groupMap.get(Long.valueOf(sortFriendGroup.getId()));
    if (friendGroup == null)
      return; 
    friendGroup.setSort(sortFriendGroup.getSort());
    this.playerService.saveSync(role);
    Db4CommonService.getInstance().add2Queue((BaseEntity)friendGroup);
    RespFriendGroup respFriendGroup = new RespFriendGroup();
    List<FriendGroup> objects = new ArrayList<>();
    objects.add(new FriendGroup(friendGroup));
    respFriendGroup.setType((byte)1);
    respFriendGroup.setList(objects);
    MessagePusher.pushMessage(role, (Message)respFriendGroup);
  }
  
  public void alterFriendGroup(Role role, ReqAlterFriendGroup alterFriendGroup) {
    Map<Long, PalFriendGroup> groupMap = role.getFriendGroupMap();
    PalFriendGroup friendGroup = groupMap.get(Long.valueOf(alterFriendGroup.getId()));
    if (friendGroup == null)
      return; 
    if (alterFriendGroup.getName() != null && alterFriendGroup.getName().length() > 0)
      friendGroup.setGroupName(alterFriendGroup.getName()); 
    Db4CommonService.getInstance().add2Queue((BaseEntity)friendGroup);
    RespFriendGroup respFriendGroup = new RespFriendGroup();
    List<FriendGroup> groupArrayList = new ArrayList<>();
    groupArrayList.add(new FriendGroup(friendGroup));
    respFriendGroup.setType((byte)1);
    respFriendGroup.setList(groupArrayList);
    MessagePusher.pushMessage(role, (Message)respFriendGroup);
    Map<Long, PalFriend> friendMap = role.getFriendMap();
    List<PalFriend> objects = new ArrayList<>();
    for (Long key : friendMap.keySet()) {
      PalFriend palFriend = friendMap.get(key);
      if (palFriend != null && palFriend.getGroupId() == friendGroup.getId().longValue()) {
        Role role1 = this.roleService.getPlayer(key.longValue());
        if (role1 != null && !alterFriendGroup.getList().contains(role1.getRoleId())) {
          palFriend.setGroupId(0L);
          Db4CommonService.getInstance().add2Queue((BaseEntity)palFriend);
          objects.add(palFriend);
        } 
        continue;
      } 
      Role player = this.roleService.getPlayer(key.longValue());
      if (player != null && alterFriendGroup.getList().contains(player.getRoleId())) {
        palFriend.setGroupId(friendGroup.getId().longValue());
        Db4CommonService.getInstance().add2Queue((BaseEntity)palFriend);
        objects.add(palFriend);
      } 
    } 
    if (objects.size() > 0) {
      List<Pla> list = new ArrayList<>();
      for (PalFriend object : objects) {
        Role player = this.roleService.getPlayer(object.getPalId());
        if (player == null)
          continue; 
        String name = player.getName();
        if (object.getRemark() != null)
          name = object.getRemark(); 
        byte status = 0;
        if (this.roleService.isOnline(player.getId().longValue()))
          status = 1; 
        list.add(new Pla(player.getRoleId().intValue(), name, player.getRelive().byteValue(), player.getLevel().shortValue(), player.getSerial().intValue(), status, String.valueOf(object.getClose()), String.valueOf(object.getGroupId())));
      } 
      MessagePusher.pushMessage(role, (Message)new RespFriend((byte)1, list));
    } 
    this.playerService.saveSync(role);
    MessagePusher.pushMessage(role, (Message)new RespMsg("修改成功，备注名会在好友界面中显示"));
  }
  
  public void alterFriend(Role role, ReqAlterFriend alterFriend) {
    Role player = this.roleService.getPlayer(alterFriend.getPal());
    if (player == null) {
      this.logger.error("修改好友获取玩家错误 = {}", Integer.valueOf(alterFriend.getPal()));
      return;
    } 
    Map<Long, PalFriend> friendMap = role.getFriendMap();
    PalFriend friend = friendMap.get(player.getId());
    if (friend == null) {
      this.logger.error("玩家：{} ，没有玩家好友信息 = {}", role.getRoleId(), player.getRoleId());
      return;
    } 
    List<Pla> list = new ArrayList<>();
    String name = player.getName();
    if (alterFriend.getRemark() != null) {
      name = CheckCharUtils.replaceName(alterFriend.getRemark());
      friend.setRemark(name);
    } 
    friend.setGroupId(Long.valueOf(alterFriend.getId()).longValue());
    byte status = 0;
    if (this.roleService.isOnline(player.getId().longValue()))
      status = 1; 
    list.add(new Pla(player.getRoleId().intValue(), name, player.getRelive().byteValue(), player.getLevel().shortValue(), player.getSerial().intValue(), status, String.valueOf(friend.getClose()), String.valueOf(friend.getGroupId())));
    MessagePusher.pushMessage(role, (Message)new RespFriend((byte)1, list));
    this.playerService.saveSync(role);
    Db4CommonService.getInstance().add2Queue((BaseEntity)friend);
    MessagePusher.pushMessage(role, (Message)new RespMsg("重置成功"));
  }
  
  public void sendFriendMsg(Role role, ReqSendFriendMsg reqSendFriendMsg) {
    int sendTime = (int)(System.currentTimeMillis() / 1000L);
    Role peer = this.roleService.getPlayer(reqSendFriendMsg.getAtt());
    if (peer == null)
      return; 
    PalFriend palFriend = (PalFriend)peer.getFriendMap().get(role.getId());
    if (palFriend != null && palFriend.getGroupId() == 1L) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("发送失败，你在对方黑名单中"));
      return;
    } 
    RespChat respChat = getRespChat(role, peer, sendTime, reqSendFriendMsg.getMsg(), true);
    if (this.roleService.isOnline(peer.getId().longValue()) && peer.getState() == 1) {
      RespChat respChatPeer = getRespChat(peer, role, sendTime, reqSendFriendMsg.getMsg(), false);
      MessagePusher.pushMessage(peer, (Message)respChatPeer);
      MessagePusher.pushMessage(role, (Message)respChat);
    } else {
      MessagePusher.pushMessage(role, (Message)new RespMsg("发送失败，对方不在线"));
    } 
  }
  
  private RespChat getRespChat(Role role, Role online, int sendTime, String msg, boolean flag) {
    RespChat respChat = new RespChat();
    respChat.setType(3);
    respChat.setRoleId(role.getRoleId().intValue());
    respChat.setFriendId(online.getRoleId().intValue());
    respChat.setMsg(msg);
    respChat.setTime(sendTime);
    if (flag) {
      respChat.setRoleName(role.getName());
      respChat.setList(FieldUtils.getChatList(role));
    } else {
      respChat.setRoleName(online.getName());
      respChat.setList(FieldUtils.getChatList(online));
    } 
    return respChat;
  }
  
  private void addOfflineMessage(Role role, long att, String msg, int sendTime) {
    RoleOfflineMessage roleOfflineMessage = DataCache.OFFLINE_MESSAGES.computeIfAbsent(att, k -> new RoleOfflineMessage());
    roleOfflineMessage.add(role.getId(), sendTime, msg);
  }
  
  public void sendOfflineMessageLogin(Role role) {
    if (role == null)
      return; 
    RoleOfflineMessage roleOfflineMessage = DataCache.OFFLINE_MESSAGES.remove(role.getId());
    if (roleOfflineMessage == null)
      return; 
    sendOfflineMessage(role, roleOfflineMessage);
  }
  
  private void sendOfflineMessage(Role att, RoleOfflineMessage roleOfflineMessage) {
    if (att == null || roleOfflineMessage == null)
      return; 
    if (roleOfflineMessage.getMessageMap() == null)
      return; 
    Map<Long, PalFriend> friendMap = att.getFriendMap();
    Set<Long> sendIds = roleOfflineMessage.getMessageMap().keySet();
    for (Iterator<Long> iterator = sendIds.iterator(); iterator.hasNext(); ) {
      long id = iterator.next();
      List<OfflineMessage> messages = roleOfflineMessage.getMessageMap().remove(id);
      Role player = this.roleService.getPlayer(id);
      PalFriend palFriend = friendMap.get(id);
      if (palFriend != null && palFriend.getGroupId() == 1L)
        continue; 
      for (OfflineMessage message : messages)
        SpringUtils.getChatService().privateChat(player, att, message.getMsg(), message.getTime()); 
    } 
  }
  
  public void searchFriend(IoSession session, String name) {
    if (name.length() <= 0) {
      MessagePusher.pushMessage(session, new RespMsg("搜索玩家不在线或不存在。"));
      MessagePusher.pushMessage(session, new RespSearchFriend());
      return;
    } 
    List<Role> collect = DataCache.ONLINE_ROLES.values().stream().filter(e -> (e.getName().contains(name) || e.getRoleId().toString().equals(name))).collect(Collectors.toList());
    if (collect.size() <= 0) {
      MessagePusher.pushMessage(session, new RespMsg("搜索玩家不在线或不存在。"));
      MessagePusher.pushMessage(session, new RespSearchFriend());
    } else {
      List<SearchFriend> objects = new ArrayList<>();
      for (Role role : collect)
        objects.add(new SearchFriend(role.getRoleId(), role.getName(), role.getSerial(), role.getRelive().byteValue(), role.getLevel().shortValue()));
      RespSearchFriend respSearchFriend = new RespSearchFriend();
      respSearchFriend.setList(objects);
      MessagePusher.pushMessage(session, respSearchFriend);
    } 
  }
}
