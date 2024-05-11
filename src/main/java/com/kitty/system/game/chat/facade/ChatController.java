
package com.kitty.system.game.chat.facade;

import com.kitty.system.game.chat.message.ReqChatObjectDetail;
import com.kitty.system.game.chat.message.ReqTempFriendState;
import com.kitty.system.game.enter.message.RespGeneralNotify;
import com.kitty.system.game.chat.message.ReqUseLaba;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.role.domain.Role;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.chat.Dispatcher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import java.text.SimpleDateFormat;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.chat.message.ReqChat;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.chat.service.ChatService;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController
{
    @Autowired
    ChatService chatService;
    
    @RequestMapping
    public void chat(final IoSession session, final ReqChat reqChat) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        if (System.currentTimeMillis() <= role.getWordTime()) {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            MessagePusher.pushMessage(session, new RespNotifyMiscEx(role.getId(), "你已被禁言，#<R>" + simpleDateFormat.format(role.getWordTime()) + "#解除禁言!", false));
            return;
        }
        if (System.currentTimeMillis() <= role.getWordTime()) {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            MessagePusher.pushMessage(session, new RespMsg("当前角色已被禁言至#<R>" + simpleDateFormat.format(role.getWordTime()) + "#。"));
            return;
        }
        if (!SpringUtils.getBean(Dispatcher.class).dispatch(role, reqChat.getMsg())) {
            SpringUtils.getChatService().chat(session, reqChat);
        }
    }
    
    @RequestMapping
    public void chat(final IoSession session, final ReqUseLaba reqUseLaba) {
        final RespGeneralNotify respGeneralNotify = new RespGeneralNotify();
        respGeneralNotify.setNotify(97);
        respGeneralNotify.setValue("HornDlg");
        MessagePusher.pushMessage(session, respGeneralNotify);
    }
    
    @RequestMapping
    public void reqTempFriendState(final IoSession session, final ReqTempFriendState reqTempFriendState) {
        final Role role = SessionUtils.getRoleBySession(session);
        SpringUtils.getChatService().reqTempFriendState(role, reqTempFriendState.getGid());
    }
    
    @RequestMapping
    public void chatObjectDetail(final IoSession session, final ReqChatObjectDetail reqChatObjectDetail) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.chatService.chatObjectDetail(role, reqChatObjectDetail);
    }
}
