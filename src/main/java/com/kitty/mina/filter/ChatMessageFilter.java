package com.kitty.mina.filter;

import org.slf4j.LoggerFactory;
import java.util.List;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.dirtywords.WordDisposePool;
import org.nutz.lang.Strings;
import com.kitty.system.game.chat.message.ReqChat;
import com.kitty.system.game.role.message.ReqCreateNewChar;
import com.kitty.mina.message.Message;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.filterchain.IoFilter;
import org.slf4j.Logger;
import org.apache.mina.core.filterchain.IoFilterAdapter;

public class ChatMessageFilter extends IoFilterAdapter
{
    private static final Logger log;
    
    public ChatMessageFilter() {
    }
    
    public void messageReceived(IoFilter.NextFilter nextFilter, IoSession session, Object packet) {
        Message message = (Message)packet;
        String content = null;
        if (message.getModule() == 16482) {
            ReqChat reqChat = (ReqChat)message;
            content = reqChat.getMsg();
            if (reqChat.getType() == 17) {
                content = getTeamRecruitContent(content);
            }
        }
        else if (message.getModule() == 9005) {
            ReqCreateNewChar createNewChar = (ReqCreateNewChar)message;
            content = createNewChar.getName();
        }
        if (!Strings.isEmpty(content)) {
            List<String> set = WordDisposePool.checkDirtyWords(content);
            if (set.size() >= 1) {
                MessagePusher.pushMessage(session, new RespMsg("输入的内容存在敏感词。"));
                return;
            }
        }
        nextFilter.messageReceived(session, message);
    }
    
    public static String getTeamRecruitContent(String content) {
        String[] split = content.split("#<Add");
        int index = split[0].lastIndexOf("#");
        return split[0].substring(index + 1);
    }
    
    static {
        log = LoggerFactory.getLogger(ChatMessageFilter.class);
    }
}
