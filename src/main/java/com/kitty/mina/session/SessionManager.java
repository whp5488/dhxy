package com.kitty.mina.session;

import java.util.Iterator;
import java.util.Map;
import com.kitty.logs.LoggerUtils;
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
        this.player2sessions = new ConcurrentHashMap();
        this.anonymouSessions = new ConcurrentHashMap();
    }
    
    public void registerSession(int roleId, IoSession session) {
        session.setAttribute(SessionProperties.PLAYER_ID, Integer.valueOf(roleId));
        this.player2sessions.put(Integer.valueOf(roleId), session);
    }
    
    public void unRegisterSession(int playerId) {
        this.player2sessions.remove(Integer.valueOf(playerId));
    }
    
    public void add2Anonymous(IoSession session) {
        this.anonymouSessions.putIfAbsent(Long.valueOf(session.getId()), session);
    }
    
    public void removeFromAnonymous(IoSession session) {
        this.anonymouSessions.remove(Long.valueOf(session.getId()));
    }
    
    public long getPlayerIdBy(IoSession session) {
        if (session != null && session.containsAttribute(SessionProperties.PLAYER_ID)) {
            return (long)this.getSessionAttr(session, SessionProperties.PLAYER_ID, Long.class);
        }
        return 0L;
    }
    
    public IoSession getSessionBy(int playerId) {
        return (IoSession)this.player2sessions.get(Integer.valueOf(playerId));
    }
    
    public <T> T getSessionAttr(IoSession session, AttributeKey attrKey, Class<T> attrType) {
        return (T)session.getAttribute(attrKey);
    }
    
    public int getNextDistributeKey() {
        return this.distributeKeyGenerator.getAndIncrement();
    }
    
    public String getRemoteIp(IoSession session) {
        return ((InetSocketAddress)session.getRemoteAddress()).getAddress().getHostAddress();
    }
    
    public void closeSession(final IoSession session) {
        if (session == null) {
            return;
        }
        Role role = SessionUtils.getRoleBySession(session);
        final String name = (role != null) ? role.getName() : "";
        final Logger logger = LoggerFunction.MONOIOR.getLogger();
        if (session.isConnected() && !session.isClosing()) {
            CloseFuture future = session.closeNow();
            future.addListener(new IoFutureListener<IoFuture>() {
                public void operationComplete(IoFuture future) {
                    logger.error("session[{}] closed complete,player[{}]", Long.valueOf(session.getId()), name);
                }
            });
        }
        this.anonymouSessions.remove(Long.valueOf(session.getId()));
        if (role != null) {
            TeamService teamService = SpringUtils.getTeamService();
            Team team = teamService.getTeam((int)role.getRoleId());
            if (team != null) {
                teamService.leave(role, false);
            }
            SpringUtils.getRoleService().removeOnline(role);
        }
        return;
    }
    
    public boolean killAllPlayers() {
        GameServer gameServer = (GameServer)SpringUtils.getBean(GameServer.class);
        Map<Long, IoSession> managedSessions = gameServer.getAcceptor().getManagedSessions();
        for (IoSession session : managedSessions.values()) {
            try {
                MessagePusher.pushMessage(session, new RespServerReboot());
                this.closeSession(session);
            }
            catch (Exception e) {
                LoggerUtils.error("", e);
                return false;
            }
        }
        return true;
    }
}
