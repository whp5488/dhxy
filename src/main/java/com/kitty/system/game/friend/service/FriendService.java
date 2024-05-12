
package com.kitty.system.game.friend.service;

import com.kitty.system.game.friend.message.vo.SearchFriend;
import java.util.stream.Collector;
import com.kitty.system.game.friend.message.RespSearchFriend;
import org.apache.mina.core.session.IoSession;
import java.util.Set;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.chat.domain.OfflineMessage;
import com.kitty.system.game.chat.domain.RoleOfflineMessage;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.common.utils.FieldUtils;
import com.kitty.system.game.chat.message.RespChat;
import com.kitty.system.game.friend.message.ReqSendFriendMsg;
import com.kitty.common.utils.CheckCharUtils;
import com.kitty.system.game.friend.message.ReqAlterFriend;
import com.kitty.system.game.friend.message.ReqAlterFriendGroup;
import com.kitty.system.game.friend.message.ReqSortFriendGroup;
import com.kitty.system.game.friend.message.RespDelFriendGroup;
import com.kitty.system.game.friend.message.ReqAddFriendGroup;
import com.kitty.system.game.friend.message.RespDelFriend;
import com.kitty.system.game.friend.message.ReqAcceptRequest;
import com.kitty.system.game.friend.message.ReqAddFriend;
import com.kitty.system.common.message.RespMsg;
import java.io.Serializable;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.friend.message.RespFriend;
import com.kitty.system.game.friend.message.vo.Pla;
import com.kitty.system.game.friend.message.RespVerifyInfo;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.friend.message.vo.FriendGroup;
import java.util.ArrayList;
import com.kitty.system.game.friend.message.RespFriendGroup;
import com.kitty.listener.event.LoginEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.LogoutEvent;
import java.util.Iterator;
import java.util.List;
import com.kitty.system.game.friend.domain.PalNotify;
import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.Db4CommonService;
import java.util.HashMap;
import com.kitty.system.game.friend.domain.PalFriend;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.Map;
import org.nutz.dao.Condition;
import org.nutz.dao.Cnd;
import com.kitty.system.game.friend.domain.PalFriendGroup;
import com.kitty.system.game.role.domain.Role;
import org.slf4j.LoggerFactory;
import com.kitty.system.game.player.PlayerService;
import com.kitty.cache.RedisService;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.common.thread.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class FriendService
{
    Logger logger;
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
    
    public FriendService() {
        this.logger = LoggerFactory.getLogger(FriendService.class);
    }
    
    public void loadPal(final Role role) {
        final List<PalFriendGroup> friendGroupList = this.dao.query(PalFriendGroup.class, Cnd.where("r_id", "=", role.getId()));
        final Map<Long, PalFriendGroup> friendGroupMap = friendGroupList.stream().collect(Collectors.toMap(PalFriendGroup::getId, PalFriendGroup -> PalFriendGroup));
        role.setFriendGroupMap(friendGroupMap);
        final List<PalFriend> palFriendList = this.dao.query(PalFriend.class, Cnd.where("r_id", "=", role.getId()));
        final Map<Long, PalFriend> palFriendMap = new HashMap<Long, PalFriend>();
        for (final PalFriend palFriend : palFriendList) {
            final PalFriend friend = palFriendMap.get(palFriend.getPalId());
            if (friend != null) {
                palFriend.setDelete(true);
                Db4CommonService.getInstance().add2Queue(palFriend);
            }
            else {
                palFriendMap.put(palFriend.getPalId(), palFriend);
            }
        }
        role.setFriendMap(palFriendMap);
        final List<PalNotify> palNotifyList = this.dao.query(PalNotify.class, Cnd.where("att", "=", role.getId()));
        final Map<Long, PalNotify> palNotifyMap = palNotifyList.stream().collect(Collectors.toMap(PalNotify::getId, PalNotify -> PalNotify));
        role.setPalNotifyMap(palNotifyMap);
    }
    
    @EventHandler({ EventType.LOGOUT })
    public void handleLogoutEvent(final LogoutEvent logoutEvent) {
        final Role role = logoutEvent.getRole();
        this.logoutInformFriends(role);
    }
    
    @EventHandler({ EventType.LOGIN })
    public void handleLoginEvent(final LoginEvent loginEvent) {
        final Role role = loginEvent.getRole();
        final Map<Long, PalFriendGroup> friendGroupMap = role.getFriendGroupMap();
        final RespFriendGroup respFriendGroup = new RespFriendGroup();
        final List<FriendGroup> objects = new ArrayList<FriendGroup>();
        for (final Long key : friendGroupMap.keySet()) {
            objects.add(new FriendGroup(friendGroupMap.get(key)));
        }
        respFriendGroup.setList(objects);
        MessagePusher.pushMessage(role, respFriendGroup);
        this.loadFriends(role);
        final Map<Long, PalNotify> palNotifyMap = role.getPalNotifyMap();
        for (final Long key2 : palNotifyMap.keySet()) {
            final PalNotify notify = palNotifyMap.get(key2);
            final Role player = this.roleService.getPlayer(notify.getSender());
            if (player != null) {
                MessagePusher.pushMessage(role, new RespVerifyInfo(String.valueOf(notify.getId()), player.getName(), player.getRelive().byteValue(), player.getLevel().shortValue(), player.getSerial()));
            }
        }
    }
    
    public void logoutInformFriends(final Role role) {
        final Map<Long, PalFriend> friendMap = role.getFriendMap();
        for (final Long key : friendMap.keySet()) {
            final PalFriend palFriend = friendMap.get(key);
            if (!this.roleService.isOnline(palFriend.getPalId())) {
                continue;
            }
            final Role player = this.roleService.getPlayer(palFriend.getPalId());
            if (player == null) {
                continue;
            }
            String name = role.getName();
            final PalFriend friend = player.getFriendMap().get(role.getId());
            if (friend != null) {
                if (friend.getRemark() != null) {
                    name = friend.getRemark();
                }
            }
            else {
                this.logger.error("好友信息错误：玩家ID = {}，好友ID = {}", (Object)role.getId(), (Object)player.getId());
            }
            final List<Pla> list = new ArrayList<Pla>();
            list.add(new Pla(role.getRoleId(), name, role.getRelive().byteValue(), role.getLevel().shortValue(), role.getSerial(), (byte)0, String.valueOf(palFriend.getClose()), String.valueOf(palFriend.getGroupId())));
            MessagePusher.pushMessage(player, new RespFriend((byte)1, list));
        }
    }
    
    public void loadFriends(final Role role) {
        final Map<Long, PalFriend> friendMap = role.getFriendMap();
        final List<Pla> list = new ArrayList<Pla>();
        for (final Long key : friendMap.keySet()) {
            final PalFriend palFriend = friendMap.get(key);
            final Role player = this.getFriendInfo(palFriend.getPalId());
            if (player != null) {
                String name = player.getName();
                if (palFriend.getRemark() != null) {
                    name = palFriend.getRemark();
                }
                byte status = 0;
                if (this.roleService.isOnline(player.getId())) {
                    status = 1;
                    this.onlineInformFriend(role, player);
                }
                list.add(new Pla(player.getRoleId(), name, player.getRelive().byteValue(), player.getLevel().shortValue(), player.getSerial(), status, String.valueOf(palFriend.getClose()), String.valueOf(palFriend.getGroupId())));
            }
        }
        MessagePusher.pushMessage(role, new RespFriend((byte)0, list));
    }
    
    public Role getFriendInfo(final long palId) {
        if (DataCache.OFFLINE_ROLES.containsKey(palId)) {
            return DataCache.OFFLINE_ROLES.get(palId);
        }
        if (DataCache.ONLINE_ROLES.containsKey(palId)) {
            return DataCache.ONLINE_ROLES.get(palId);
        }
        Role role = this.redisService.get(String.valueOf(palId), Role.class);
        if (role == null) {
            role = this.roleService.getPlayer(palId);
        }
        return role;
    }
    
    public void onlineInformFriend(final Role role, final Role friend) {
        final PalFriend palFriend = friend.getFriendMap().get(role.getId());
        if (palFriend != null) {
            final List<Pla> list = new ArrayList<Pla>();
            String name = role.getName();
            if (palFriend.getRemark() != null) {
                name = palFriend.getRemark();
            }
            list.add(new Pla(role.getRoleId(), name, role.getRelive().byteValue(), role.getLevel().shortValue(), role.getSerial(), (byte)1, String.valueOf(palFriend.getClose()), String.valueOf(palFriend.getGroupId())));
            MessagePusher.pushMessage(friend, new RespFriend((byte)0, list));
            MessagePusher.pushMessage(friend, new RespMsg("你的好友#<LB1>" + name + "#上线了"));
        }
    }
    
    public void reqAddBlack(final Role role, final ReqAddFriend reqAddFriend) {
        final Role player = this.roleService.getPlayer(reqAddFriend.getRoleId());
        if (player != null) {
            PalFriend friend = role.getFriendMap().get(player.getId());
            if (friend == null) {
                friend = new PalFriend();
                friend.setId(this.worker.nextId());
            }
            this.friend(friend, role, player, Long.valueOf(reqAddFriend.getGroupId()), reqAddFriend.getRemark());
            MessagePusher.pushMessage(role, new RespMsg(String.format("你已经成功将#<LB1>%s#添加到黑名单", player.getName())));
            final Map<Long, PalFriend> playerFriendMap = player.getFriendMap();
            final PalFriend playerFriend = playerFriendMap.get(role.getId());
            if (playerFriend != null) {
                this.delFriend(player, playerFriend, playerFriendMap, role.getRoleId());
            }
        }
    }
    
    public void reqAddFriend(final Role role, final ReqAddFriend reqAddFriend) {
        final Role player = this.roleService.getPlayer(reqAddFriend.getRoleId());
        if (player != null && this.roleService.isOnline(player.getId())) {
            final PalFriend palFriend = player.getFriendMap().get(role.getId());
            if (palFriend != null) {
                if (palFriend.getGroupId() == 1L) {
                    MessagePusher.pushMessage(role, new RespMsg("你在对方黑名单，无法申请好友"));
                }
                else {
                    MessagePusher.pushMessage(role, new RespMsg("对方已经是你的好友"));
                }
                return;
            }
            final Map<Long, PalNotify> notifyMap = player.getPalNotifyMap();
            PalNotify palNotify = notifyMap.values().stream().filter(e -> e.getSender() == role.getId()).findFirst().orElse(null);
            if (palNotify != null) {
                MessagePusher.pushMessage(role, new RespMsg("申请成功，等待对方同意"));
                return;
            }
            final int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
            palNotify = new PalNotify(this.worker.nextId(), (byte)1, role.getId(), player.getId(), (byte)1, currTime, Long.valueOf(reqAddFriend.getGroupId()), reqAddFriend.getRemark());
            notifyMap.put(palNotify.getId(), palNotify);
            this.playerService.saveSync(player);
            Db4CommonService.getInstance().add2Queue(palNotify);
            MessagePusher.pushMessage(role, new RespMsg("申请成功，等待对方同意"));
            MessagePusher.pushMessage(player, new RespVerifyInfo(String.valueOf(palNotify.getId()), role.getName(), role.getRelive().byteValue(), role.getLevel().shortValue(), role.getSerial()));
        }
    }
    
    public void acceptRequest(final Role role, final ReqAcceptRequest reqAcceptRequest) {
        for (final String key : reqAcceptRequest.getList()) {
            final long id = Long.valueOf(key);
            final PalNotify notify = role.getPalNotifyMap().get(id);
            if (notify == null) {
                this.logger.error("玩家：{}，通过或者拒绝请求，获取数据为空：{}", (Object)role.getRoleId(), (Object)key);
            }
            else {
                this.acceptRequest(role, notify, reqAcceptRequest.getAccept());
            }
        }
        if (reqAcceptRequest.getAccept() == 2) {
            MessagePusher.pushMessage(role, new RespMsg("你清除了好友申请"));
        }
    }
    
    public void acceptRequest(final Role role, final PalNotify notify, final byte type) {
        if (type == 1) {
            final Role player = this.roleService.getPlayer(notify.getSender());
            if (player == null) {
                return;
            }
            if (role.getFriendMap().get(player.getId()) == null) {
                final PalFriend friend = new PalFriend();
                friend.setId(this.worker.nextId());
                this.friend(friend, role, player, 0L, null);
            }
            MessagePusher.pushMessage(player, new RespMsg(String.format("#<LB1>%s#已经通过了你的好友申请", role.getName())));
            if (player.getFriendMap().get(role.getId()) == null) {
                final PalFriend friendAtt = new PalFriend();
                friendAtt.setId(this.worker.nextId());
                this.friend(friendAtt, player, role, notify.getGroupId(), notify.getRemark());
            }
            MessagePusher.pushMessage(role, new RespMsg(String.format("成功添加#<LB1>%s#为你的好友", player.getName())));
        }
        notify.setDelete(true);
        role.getPalNotifyMap().remove(notify.getId());
        this.playerService.saveSync(role);
        Db4CommonService.getInstance().add2Queue(notify);
    }
    
    public void friend(final PalFriend friend, final Role role, final Role player, final long groupId, String remark) {
        friend.setrId(role.getId());
        friend.setPalId(player.getId());
        friend.setGroupId(groupId);
        friend.setClose(0L);
        if (remark == null || remark.isEmpty()) {
            remark = player.getName();
        }
        friend.setRemark(remark);
        role.getFriendMap().put(player.getId(), friend);
        this.playerService.saveSync(role);
        Db4CommonService.getInstance().add2Queue(friend);
        final List<Pla> list = new ArrayList<Pla>();
        String name = player.getName();
        if (friend.getRemark() != null) {
            name = friend.getRemark();
        }
        byte status = 0;
        if (this.roleService.isOnline(player.getId())) {
            status = 1;
        }
        list.add(new Pla(player.getRoleId(), name, player.getRelive().byteValue(), player.getLevel().shortValue(), player.getSerial(), status, String.valueOf(friend.getClose()), String.valueOf(friend.getGroupId())));
        MessagePusher.pushMessage(role, new RespFriend((byte)1, list));
    }
    
    public void delFriend(final Role role, final List<Integer> list) {
        final Map<Long, PalFriend> friendMap = role.getFriendMap();
        for (final Integer id : list) {
            final Role player = this.roleService.getPlayer(id);
            if (player == null) {
                continue;
            }
            final Map<Long, PalFriend> playerFriendMap = player.getFriendMap();
            final PalFriend playerFriend = playerFriendMap.get(role.getId());
            if (playerFriend != null) {
                this.delFriend(player, playerFriend, playerFriendMap, role.getRoleId());
            }
            final PalFriend friend = friendMap.get(player.getId());
            if (friend == null) {
                continue;
            }
            this.delFriend(role, friend, friendMap, player.getRoleId());
        }
        MessagePusher.pushMessage(role, new RespMsg("删除好友成功"));
    }
    
    public void delFriend(final Role role, final PalFriend friend, final Map<Long, PalFriend> friendMap, final int id) {
        friend.setDelete(true);
        friendMap.remove(friend.getPalId());
        this.playerService.saveSync(role);
        Db4CommonService.getInstance().add2Queue(friend);
        MessagePusher.pushMessage(role, new RespDelFriend(id));
    }
    
    public void addFriendGroup(final Role role, final ReqAddFriendGroup reqAddFriendGroup) {
        if (reqAddFriendGroup.getGroupName().length() < 1) {
            return;
        }
        final String groupName = reqAddFriendGroup.getGroupName().trim();
        final Map<Long, PalFriendGroup> groupMap = role.getFriendGroupMap();
        final PalFriendGroup orElse = groupMap.values().stream().filter(e -> e.getGroupName().equals(groupName)).findFirst().orElse(null);
        if (orElse != null) {
            MessagePusher.pushMessage(role, new RespMsg("已有同名分组，无法使用这个分组名"));
            return;
        }
        final PalFriendGroup friendGroup = new PalFriendGroup(this.worker.nextId(), role.getId(), groupName, (short)0);
        groupMap.put(friendGroup.getId(), friendGroup);
        Db4CommonService.getInstance().add2Queue(friendGroup);
        final RespFriendGroup respFriendGroup = new RespFriendGroup();
        final List<FriendGroup> objects = new ArrayList<FriendGroup>();
        objects.add(new FriendGroup(friendGroup));
        respFriendGroup.setType((byte)1);
        respFriendGroup.setList(objects);
        MessagePusher.pushMessage(role, respFriendGroup);
        if (reqAddFriendGroup.getList().size() > 0) {
            final Map<Long, PalFriend> friendMap = role.getFriendMap();
            final List<Pla> list = new ArrayList<Pla>();
            for (final Integer key : reqAddFriendGroup.getList()) {
                final Role player = this.roleService.getPlayer(key);
                if (player == null) {
                    continue;
                }
                final PalFriend palFriend = friendMap.get(player.getId());
                if (palFriend == null) {
                    continue;
                }
                palFriend.setGroupId(friendGroup.getId());
                String name = player.getName();
                if (palFriend.getRemark() != null) {
                    name = palFriend.getRemark();
                }
                byte status = 0;
                if (this.roleService.isOnline(player.getId())) {
                    status = 1;
                }
                Db4CommonService.getInstance().add2Queue(palFriend);
                list.add(new Pla(player.getRoleId(), name, player.getRelive().byteValue(), player.getLevel().shortValue(), player.getSerial(), status, String.valueOf(palFriend.getClose()), String.valueOf(palFriend.getGroupId())));
            }
            if (list.size() > 0) {
                MessagePusher.pushMessage(role, new RespFriend((byte)1, list));
            }
        }
        this.playerService.saveSync(role);
        MessagePusher.pushMessage(role, new RespMsg("新建成功"));
    }
    
    public void delFriendGroup(final Role role, final Long id) {
        final PalFriendGroup friendGroup = role.getFriendGroupMap().get(id);
        if (friendGroup == null) {
            return;
        }
        final Map<Long, PalFriend> friendMap = role.getFriendMap();
        final List<Pla> list = new ArrayList<Pla>();
        for (final Long key : friendMap.keySet()) {
            final Role player = this.roleService.getPlayer(key);
            if (player == null) {
                continue;
            }
            final PalFriend palFriend = friendMap.get(player.getId());
            if (palFriend == null) {
                continue;
            }
            if (palFriend.getGroupId() != friendGroup.getId()) {
                continue;
            }
            palFriend.setGroupId(0L);
            String name = player.getName();
            if (palFriend.getRemark() != null) {
                name = palFriend.getRemark();
            }
            byte status = 0;
            if (this.roleService.isOnline(player.getId())) {
                status = 1;
            }
            Db4CommonService.getInstance().add2Queue(palFriend);
            list.add(new Pla(player.getRoleId(), name, player.getRelive().byteValue(), player.getLevel().shortValue(), player.getSerial(), status, String.valueOf(palFriend.getClose()), String.valueOf(palFriend.getGroupId())));
        }
        if (list.size() > 0) {
            MessagePusher.pushMessage(role, new RespFriend((byte)1, list));
        }
        friendGroup.setDelete(true);
        role.getFriendGroupMap().remove(friendGroup.getId());
        this.playerService.saveSync(role);
        Db4CommonService.getInstance().add2Queue(friendGroup);
        MessagePusher.pushMessage(role, new RespDelFriendGroup(String.valueOf(friendGroup.getId())));
        MessagePusher.pushMessage(role, new RespMsg("删除成功"));
    }
    
    public void sortFriendGroup(final Role role, final ReqSortFriendGroup sortFriendGroup) {
        final Map<Long, PalFriendGroup> groupMap = role.getFriendGroupMap();
        final PalFriendGroup friendGroup = groupMap.get(Long.valueOf(sortFriendGroup.getId()));
        if (friendGroup == null) {
            return;
        }
        friendGroup.setSort(sortFriendGroup.getSort());
        this.playerService.saveSync(role);
        Db4CommonService.getInstance().add2Queue(friendGroup);
        final RespFriendGroup respFriendGroup = new RespFriendGroup();
        final List<FriendGroup> objects = new ArrayList<FriendGroup>();
        objects.add(new FriendGroup(friendGroup));
        respFriendGroup.setType((byte)1);
        respFriendGroup.setList(objects);
        MessagePusher.pushMessage(role, respFriendGroup);
    }
    
    public void alterFriendGroup(final Role role, final ReqAlterFriendGroup alterFriendGroup) {
        final Map<Long, PalFriendGroup> groupMap = role.getFriendGroupMap();
        final PalFriendGroup friendGroup = groupMap.get(Long.valueOf(alterFriendGroup.getId()));
        if (friendGroup == null) {
            return;
        }
        if (alterFriendGroup.getName() != null && alterFriendGroup.getName().length() > 0) {
            friendGroup.setGroupName(alterFriendGroup.getName());
        }
        Db4CommonService.getInstance().add2Queue(friendGroup);
        final RespFriendGroup respFriendGroup = new RespFriendGroup();
        final List<FriendGroup> groupArrayList = new ArrayList<FriendGroup>();
        groupArrayList.add(new FriendGroup(friendGroup));
        respFriendGroup.setType((byte)1);
        respFriendGroup.setList(groupArrayList);
        MessagePusher.pushMessage(role, respFriendGroup);
        final Map<Long, PalFriend> friendMap = role.getFriendMap();
        final List<PalFriend> objects = new ArrayList<PalFriend>();
        for (final Long key : friendMap.keySet()) {
            final PalFriend palFriend = friendMap.get(key);
            if (palFriend != null && palFriend.getGroupId() == friendGroup.getId()) {
                final Role player = this.roleService.getPlayer(key);
                if (player == null || alterFriendGroup.getList().contains(player.getRoleId())) {
                    continue;
                }
                palFriend.setGroupId(0L);
                Db4CommonService.getInstance().add2Queue(palFriend);
                objects.add(palFriend);
            }
            else {
                final Role player = this.roleService.getPlayer(key);
                if (player == null || !alterFriendGroup.getList().contains(player.getRoleId())) {
                    continue;
                }
                palFriend.setGroupId(friendGroup.getId());
                Db4CommonService.getInstance().add2Queue(palFriend);
                objects.add(palFriend);
            }
        }
        if (objects.size() > 0) {
            final List<Pla> list = new ArrayList<Pla>();
            for (final PalFriend object : objects) {
                final Role player = this.roleService.getPlayer(object.getPalId());
                if (player == null) {
                    continue;
                }
                String name = player.getName();
                if (object.getRemark() != null) {
                    name = object.getRemark();
                }
                byte status = 0;
                if (this.roleService.isOnline(player.getId())) {
                    status = 1;
                }
                list.add(new Pla(player.getRoleId(), name, player.getRelive().byteValue(), player.getLevel().shortValue(), player.getSerial(), status, String.valueOf(object.getClose()), String.valueOf(object.getGroupId())));
            }
            MessagePusher.pushMessage(role, new RespFriend((byte)1, list));
        }
        this.playerService.saveSync(role);
        MessagePusher.pushMessage(role, new RespMsg("修改成功，备注名会在好友界面中显示"));
    }
    
    public void alterFriend(final Role role, final ReqAlterFriend alterFriend) {
        final Role player = this.roleService.getPlayer(alterFriend.getPal());
        if (player == null) {
            this.logger.error("修改好友获取玩家错误 = {}", (Object)alterFriend.getPal());
            return;
        }
        final Map<Long, PalFriend> friendMap = role.getFriendMap();
        final PalFriend friend = friendMap.get(player.getId());
        if (friend == null) {
            this.logger.error("玩家：{} ，没有玩家好友信息 = {}", (Object)role.getRoleId(), (Object)player.getRoleId());
            return;
        }
        final List<Pla> list = new ArrayList<Pla>();
        String name = player.getName();
        if (alterFriend.getRemark() != null) {
            name = CheckCharUtils.replaceName(alterFriend.getRemark());
            friend.setRemark(name);
        }
        friend.setGroupId(Long.valueOf(alterFriend.getId()));
        byte status = 0;
        if (this.roleService.isOnline(player.getId())) {
            status = 1;
        }
        list.add(new Pla(player.getRoleId(), name, player.getRelive().byteValue(), player.getLevel().shortValue(), player.getSerial(), status, String.valueOf(friend.getClose()), String.valueOf(friend.getGroupId())));
        MessagePusher.pushMessage(role, new RespFriend((byte)1, list));
        this.playerService.saveSync(role);
        Db4CommonService.getInstance().add2Queue(friend);
        MessagePusher.pushMessage(role, new RespMsg("重置成功"));
    }
    
    public void sendFriendMsg(final Role role, final ReqSendFriendMsg reqSendFriendMsg) {
        final int sendTime = (int)(System.currentTimeMillis() / 1000L);
        final Role peer = this.roleService.getPlayer(reqSendFriendMsg.getAtt());
        if (peer == null) {
            return;
        }
        final PalFriend palFriend = peer.getFriendMap().get(role.getId());
        if (palFriend != null && palFriend.getGroupId() == 1L) {
            MessagePusher.pushMessage(role, new RespMsg("发送失败，你在对方黑名单中"));
            return;
        }
        final RespChat respChat = this.getRespChat(role, peer, sendTime, reqSendFriendMsg.getMsg(), true);
        if (this.roleService.isOnline(peer.getId()) && peer.getState() == 1) {
            final RespChat respChatPeer = this.getRespChat(peer, role, sendTime, reqSendFriendMsg.getMsg(), false);
            MessagePusher.pushMessage(peer, respChatPeer);
            MessagePusher.pushMessage(role, respChat);
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("发送失败，对方不在线"));
        }
    }
    
    private RespChat getRespChat(final Role role, final Role online, final int sendTime, final String msg, final boolean flag) {
        final RespChat respChat = new RespChat();
        respChat.setType(3);
        respChat.setRoleId(role.getRoleId());
        respChat.setFriendId(online.getRoleId());
        respChat.setMsg(msg);
        respChat.setTime(sendTime);
        if (flag) {
            respChat.setRoleName(role.getName());
            respChat.setList(FieldUtils.getChatList(role));
        }
        else {
            respChat.setRoleName(online.getName());
            respChat.setList(FieldUtils.getChatList(online));
        }
        return respChat;
    }
    
    private void addOfflineMessage(final Role role, final long att, final String msg, final int sendTime) {
        final RoleOfflineMessage roleOfflineMessage = DataCache.OFFLINE_MESSAGES.computeIfAbsent(att, k -> new RoleOfflineMessage());
        roleOfflineMessage.add(role.getId(), sendTime, msg);
    }
    
    public void sendOfflineMessageLogin(final Role role) {
        if (role == null) {
            return;
        }
        final RoleOfflineMessage roleOfflineMessage = DataCache.OFFLINE_MESSAGES.remove(role.getId());
        if (roleOfflineMessage == null) {
            return;
        }
        this.sendOfflineMessage(role, roleOfflineMessage);
    }
    
    private void sendOfflineMessage(final Role att, final RoleOfflineMessage roleOfflineMessage) {
        if (att == null || roleOfflineMessage == null) {
            return;
        }
        if (roleOfflineMessage.getMessageMap() == null) {
            return;
        }
        final Map<Long, PalFriend> friendMap = att.getFriendMap();
        final Set<Long> sendIds = roleOfflineMessage.getMessageMap().keySet();
        for (final long id : sendIds) {
            final List<OfflineMessage> messages = roleOfflineMessage.getMessageMap().remove(id);
            final Role player = this.roleService.getPlayer(id);
            final PalFriend palFriend = friendMap.get(id);
            if (palFriend != null && palFriend.getGroupId() == 1L) {
                continue;
            }
            for (final OfflineMessage message : messages) {
                SpringUtils.getChatService().privateChat(player, att, message.getMsg(), message.getTime());
            }
        }
    }
    
    public void searchFriend(final IoSession session, final String name) {
        if (name.length() <= 0) {
            MessagePusher.pushMessage(session, new RespMsg("搜索玩家不在线或不存在。"));
            MessagePusher.pushMessage(session, new RespSearchFriend());
            return;
        }
        final List<Role> collect = DataCache.ONLINE_ROLES.values().stream().filter(e -> e.getName().contains(name) || e.getRoleId().toString().equals(name)).collect(Collectors.toList());
        if (collect.size() <= 0) {
            MessagePusher.pushMessage(session, new RespMsg("搜索玩家不在线或不存在。"));
            MessagePusher.pushMessage(session, new RespSearchFriend());
        }
        else {
            final List<SearchFriend> objects = new ArrayList<SearchFriend>();
            for (final Role role : collect) {
                objects.add(new SearchFriend(role.getRoleId(), role.getName(), role.getSerial(), role.getRelive().byteValue(), role.getLevel().shortValue()));
            }
            final RespSearchFriend respSearchFriend = new RespSearchFriend();
            respSearchFriend.setList(objects);
            MessagePusher.pushMessage(session, respSearchFriend);
        }
    }
}
