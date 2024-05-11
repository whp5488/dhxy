
package com.kitty.system.game.friend.facade;

import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.common.utils.TimeUtil;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.friend.message.ReqSendFriendMsg;
import com.kitty.system.game.friend.message.ReqSearchFriend;
import com.kitty.system.game.friend.message.ReqAlterFriend;
import com.kitty.common.utils.CheckCharUtils;
import com.kitty.system.game.friend.message.ReqAlterFriendGroup;
import com.kitty.system.game.friend.message.ReqSortFriendGroup;
import com.kitty.system.game.friend.message.ReqDelFriendGroup;
import com.kitty.system.game.friend.message.ReqAddFriendGroup;
import com.kitty.system.game.friend.message.ReqDelFriend;
import com.kitty.system.game.friend.message.ReqAcceptRequest;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.friend.message.ReqAddFriend;
import org.apache.mina.core.session.IoSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.friend.service.FriendService;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;

@Controller
public class FriendController
{
    Logger logger;
    @Autowired
    FriendService friendService;
    
    public FriendController() {
        this.logger = LoggerFactory.getLogger(FriendController.class);
    }
    
    @RequestMapping
    public void addFriend(final IoSession session, final ReqAddFriend reqAddFriend) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            if ("1".equals(reqAddFriend.getGroupId())) {
                this.friendService.reqAddBlack(role, reqAddFriend);
            }
            else {
                this.friendService.reqAddFriend(role, reqAddFriend);
            }
        }
    }
    
    @RequestMapping
    public void acceptRequest(final IoSession session, final ReqAcceptRequest reqAcceptRequest) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.friendService.acceptRequest(role, reqAcceptRequest);
        }
    }
    
    @RequestMapping
    public void removeFriend(final IoSession session, final ReqDelFriend reqDelFriend) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.friendService.delFriend(role, reqDelFriend.getList());
        }
    }
    
    @RequestMapping
    public void addFriendGroup(final IoSession session, final ReqAddFriendGroup reqAddFriendGroup) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.friendService.addFriendGroup(role, reqAddFriendGroup);
        }
    }
    
    @RequestMapping
    public void removeFriendGroup(final IoSession session, final ReqDelFriendGroup reqDelFriendGroup) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.friendService.delFriendGroup(role, Long.valueOf(reqDelFriendGroup.getId()));
        }
    }
    
    @RequestMapping
    public void sortFriendGroup(final IoSession session, final ReqSortFriendGroup sortFriendGroup) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.friendService.sortFriendGroup(role, sortFriendGroup);
        }
    }
    
    @RequestMapping
    public void alterFriendGroup(final IoSession session, final ReqAlterFriendGroup alterFriendGroup) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            alterFriendGroup.setName(CheckCharUtils.replaceName(alterFriendGroup.getName()));
            this.friendService.alterFriendGroup(role, alterFriendGroup);
        }
    }
    
    @RequestMapping
    public void alterFriend(final IoSession session, final ReqAlterFriend alterFriend) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.friendService.alterFriend(role, alterFriend);
        }
    }
    
    @RequestMapping
    public void searchFriend(final IoSession session, final ReqSearchFriend reqSearchFriend) {
        this.friendService.searchFriend(session, reqSearchFriend.getName());
    }
    
    @RequestMapping
    public void sendFriendMsg(final IoSession session, final ReqSendFriendMsg reqSendFriendMsg) {
        if (Const.CHART_LIMIT.contains(9)) {
            MessagePusher.pushMessage(session, new RespMsg("该频道暂不支持发言"));
            return;
        }
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            if (role.getWordTime() > 0L && System.currentTimeMillis() <= role.getWordTime()) {
                MessagePusher.pushMessage(session, new RespNotifyMiscEx("你已被禁言，#<R>" + TimeUtil.simpleDateFormat.format(role.getWordTime()) + "#解除禁言!"));
                return;
            }
            this.friendService.sendFriendMsg(role, reqSendFriendMsg);
        }
    }
}
