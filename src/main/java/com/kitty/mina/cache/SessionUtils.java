
package com.kitty.mina.cache;

import com.kitty.mina.session.SessionProperties;
import com.kitty.system.game.role.domain.Role;
import java.util.HashMap;
import com.kitty.common.start.GameServer;
import java.util.Map;
import com.kitty.system.game.player.PlayerProfile;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.session.SessionManager;
import org.apache.mina.core.session.IoSession;

public class SessionUtils
{
    public static IoSession getSession(final int roleId) {
        return SessionManager.INSTANCE.getSessionBy(roleId);
    }
    
    public static IoSession getSessionBy(final long id) {
        final PlayerProfile playerProfile = SpringUtils.getPlayerService().queryPlayerProfile(id);
        if (playerProfile == null) {
            return null;
        }
        return getSession(playerProfile.getRoleId());
    }
    
    public static Map<Long, IoSession> getSessionList() {
        final GameServer gameServer = SpringUtils.getBean(GameServer.class);
        if (gameServer.getAcceptor() == null) {
            return new HashMap<Long, IoSession>();
        }
        return gameServer.getAcceptor().getManagedSessions();
    }
    
    public static Role getRoleBySession(final IoSession session) {
        if (session == null) {
            return null;
        }
        final Long uId = SessionManager.INSTANCE.getSessionAttr(session, SessionProperties.PLAYER_UID, Long.class);
        if (uId == null) {
            return null;
        }
        return SpringUtils.getRoleService().getPlayer(uId);
    }
}
