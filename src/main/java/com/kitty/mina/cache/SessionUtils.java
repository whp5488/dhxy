package com.kitty.mina.cache;

import com.kitty.common.start.GameServer;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.session.SessionManager;
import com.kitty.mina.session.SessionProperties;
import com.kitty.system.game.player.PlayerProfile;
import com.kitty.system.game.role.domain.Role;
import java.util.HashMap;
import java.util.Map;
import org.apache.mina.core.session.IoSession;

public class SessionUtils {
  public static IoSession getSession(int roleId) {
    return SessionManager.INSTANCE.getSessionBy(roleId);
  }
  
  public static IoSession getSessionBy(long id) {
    PlayerProfile playerProfile = SpringUtils.getPlayerService().queryPlayerProfile(id);
    if (playerProfile == null)
      return null; 
    return getSession(playerProfile.getRoleId());
  }
  
  public static Map<Long, IoSession> getSessionList() {
    GameServer gameServer = (GameServer)SpringUtils.getBean(GameServer.class);
    if (gameServer.getAcceptor() == null)
      return new HashMap<>(); 
    return gameServer.getAcceptor().getManagedSessions();
  }
  
  public static Role getRoleBySession(IoSession session) {
    if (session == null)
      return null; 
    Long uId = (Long)SessionManager.INSTANCE.getSessionAttr(session, SessionProperties.PLAYER_UID, Long.class);
    if (uId == null)
      return null; 
    return SpringUtils.getRoleService().getPlayer(uId);
  }
}
