
package com.kitty.mina.session;

import java.util.Iterator;
import java.util.Map;
import com.kitty.logs.LoggerUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespServerReboot;
import com.kitty.common.start.GameServer;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.service.TeamService;
import org.apache.mina.core.future.CloseFuture;
import com.kitty.system.game.role.domain.Role;
import com.kitty.common.utils.spring.SpringUtils;
import org.slf4j.Logger;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import com.kitty.logs.LoggerFunction;
import com.kitty.mina.cache.SessionUtils;
import java.net.InetSocketAddress;
import org.apache.mina.core.session.AttributeKey;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.mina.core.session.IoSession;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public enum SessionManager
{
    INSTANCE;
    
    private AtomicInteger distributeKeyGenerator;
    private ConcurrentMap<Integer, IoSession> player2sessions;
    private ConcurrentMap<Long, IoSession> anonymouSessions;
    
    private SessionManager() {
        this.distributeKeyGenerator = new AtomicInteger(1);
        this.player2sessions = new ConcurrentHashMap<Integer, IoSession>();
        this.anonymouSessions = new ConcurrentHashMap<Long, IoSession>();
    }
    
    public void registerSession(final int roleId, final IoSession session) {
        session.setAttribute(SessionProperties.PLAYER_ID, roleId);
        this.player2sessions.put(roleId, session);
    }
    
    public void unRegisterSession(final int playerId) {
        this.player2sessions.remove(playerId);
    }
    
    public void add2Anonymous(final IoSession session) {
        this.anonymouSessions.putIfAbsent(session.getId(), session);
    }
    
    public void removeFromAnonymous(final IoSession session) {
        this.anonymouSessions.remove(session.getId());
    }
    
    public long getPlayerIdBy(final IoSession session) {
        if (session != null && session.containsAttribute(SessionProperties.PLAYER_ID)) {
            return this.getSessionAttr(session, SessionProperties.PLAYER_ID, Long.class);
        }
        return 0L;
    }
    
    public IoSession getSessionBy(final int playerId) {
        return this.player2sessions.get(playerId);
    }
    
    public <T> T getSessionAttr(final IoSession session, final AttributeKey attrKey, final Class<T> attrType) {
        return (T)session.getAttribute(attrKey);
    }
    
    public int getNextDistributeKey() {
        return this.distributeKeyGenerator.getAndIncrement();
    }
    
    public String getRemoteIp(final IoSession session) {
        return ((InetSocketAddress)session.getRemoteAddress()).getAddress().getHostAddress();
    }
    
    public void closeSession(final IoSession session) {
        if (session == null) {
            return;
        }
        final Role role = SessionUtils.getRoleBySession(session);
        final String name = (role != null) ? role.getName() : "";
        final Logger logger = LoggerFunction.MONOIOR.getLogger();
        if (session.isConnected() && !session.isClosing()) {
            final CloseFuture future = session.closeNow();
            future.addListener((IoFutureListener)new IoFutureListener<IoFuture>() {
                public void operationComplete(final IoFuture future) {
                    logger.error("session[{}] closed complete,player[{}]", session.getId(), name);
                }
            });
        }
        this.anonymouSessions.remove(session.getId());
        if (role != null) {
            final TeamService teamService = SpringUtils.getTeamService();
            final Team team = teamService.getTeam(role.getRoleId());
            if (team != null) {
                teamService.leave(role, false);
            }
            SpringUtils.getRoleService().removeOnline(role);
        }
    }
    
    public boolean killAllPlayers() {
        final GameServer gameServer = SpringUtils.getBean(GameServer.class);
        final Map<Long, IoSession> managedSessions = gameServer.getAcceptor().getManagedSessions();
        for (final IoSession session : managedSessions.values()) {
            try {
                MessagePusher.pushMessage(session, new RespServerReboot());
                this.closeSession(session);
            }
            catch (final Exception e) {
                LoggerUtils.error("", e);
                return false;
            }
        }
        return true;
    }
}
