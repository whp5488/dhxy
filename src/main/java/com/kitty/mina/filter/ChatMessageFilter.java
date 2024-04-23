package com.kitty.mina.filter;

import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.chat.message.ReqChat;
import com.kitty.system.game.dirtywords.WordDisposePool;
import com.kitty.system.game.role.message.ReqCreateNewChar;
import java.util.List;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.nutz.lang.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatMessageFilter extends IoFilterAdapter {
  private static final Logger log = LoggerFactory.getLogger(ChatMessageFilter.class);
  
  public void messageReceived(IoFilter.NextFilter nextFilter, IoSession session, Object packet) {
    Message message = (Message)packet;
    String content = null;
    if (message.getModule() == 16482) {
      ReqChat reqChat = (ReqChat)message;
      content = reqChat.getMsg();
      if (reqChat.getType() == 17)
        content = getTeamRecruitContent(content); 
    } else if (message.getModule() == 8284) {
      ReqCreateNewChar createNewChar = (ReqCreateNewChar)message;
      content = createNewChar.getName();
    } 
    if (!Strings.isEmpty(content)) {
      List<String> set = WordDisposePool.checkDirtyWords(content);
      if (set.size() >= 1) {
        MessagePusher.pushMessage(session, (Message)new RespMsg("输入的内容存在敏感词。"));
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
}
