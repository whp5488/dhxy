package com.kitty.mina.message;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.i18n.I18nIdDataPool;
import com.kitty.system.game.i18n.P_I18nId;
import com.kitty.system.game.role.domain.Role;
import org.apache.mina.core.session.IoSession;

public class MessagePusher {
  public static void pushMessage(Role role, Message message) {
    if (role == null)
      return; 
    pushMessage(role.getId().longValue(), message);
  }
  
  public static void pushMessages(Role role, Message... messages) {
    if (role == null)
      return; 
    for (Message message : messages)
      pushMessage(role.getId().longValue(), message); 
  }
  
  public static void pushMessage(long playerId, Message message) {
    IoSession session = SessionUtils.getSessionBy(playerId);
    pushMessage(session, message);
  }
  
  public static void pushMessage(IoSession session, Message message) {
    if (session == null || message == null || session.isClosing())
      return; 
    session.write(message);
  }
  
  public static void notify2Player(IoSession session, int i18nId, Object... args) {
    P_I18nId idResource = (P_I18nId)I18nIdDataPool.ids.get(Integer.valueOf(i18nId));
    if (idResource != null) {
      String content = String.format(idResource.getContent(), args);
      pushMessage(session, (Message)new RespMsg(content));
    } 
  }
  
  public static void notify2Player(Role role, int i18nId, Object... args) {
    if (role == null)
      return; 
    IoSession session = SessionUtils.getSessionBy(role.getId().longValue());
    notify2Player(session, i18nId, args);
  }
  
  public static void notify2Lb(Role role, int i18nId, Object... args) {
    if (role == null)
      return; 
    P_I18nId idResource = (P_I18nId)I18nIdDataPool.ids.get(Integer.valueOf(i18nId));
    if (idResource != null) {
      String content = String.format(idResource.getContent(), args);
      SpringUtils.getBroadcastThread().add2Queue(content);
    } 
  }
  
  public static void notify2Misc(Role role, boolean flag, int i18nId, Object... args) {
    if (role == null)
      return; 
    P_I18nId idResource = (P_I18nId)I18nIdDataPool.ids.get(Integer.valueOf(i18nId));
    if (idResource != null) {
      String content = String.format(idResource.getContent(), args);
      pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), content, flag));
    } 
  }
}
