
package com.kitty.mina;

import org.slf4j.LoggerFactory;
import java.io.IOException;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.gate.message.RespOtherLogin;
import java.util.HashMap;
import org.nutz.json.Json;
import java.net.InetSocketAddress;
import com.kitty.mina.message.Message;
import com.kitty.system.game.role.domain.Role;
import org.nutz.lang.Lang;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.gate.model.QueueUpService;
import com.kitty.mina.session.SessionProperties;
import com.kitty.mina.session.SessionManager;
import org.apache.mina.core.session.IoSession;
import com.kitty.mina.message.IMessageDispatcher;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.slf4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;

public class ServerSocketIoHandler extends IoHandlerAdapter
{
    private static final Logger logger;
    private static final ScheduledThreadPoolExecutor executor;
    private static final long interval = 15000L;
    private final IMessageDispatcher messageDispatcher;
    
    public ServerSocketIoHandler(final IMessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }
    
    public void sessionOpened(final IoSession session) {
    }
    
    public void sessionClosed(final IoSession session) {
        final String sid = SessionManager.INSTANCE.getSessionAttr(session, SessionProperties.ACCOUNT_SID, String.class);
        SpringUtils.getBean(QueueUpService.class).remove(sid);
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        try {
            SpringUtils.getRoleService().addOffline(role, true);
        }
        catch (final Exception e) {
            ServerSocketIoHandler.logger.error("Session关闭错误信息===={}", (Object)Lang.getStackTrace((Throwable)e));
        }
    }
    
    public void sessionCreated(final IoSession session) {
        session.setAttributeIfAbsent((Object)SessionProperties.DISTRIBUTE_KEY, (Object)SessionManager.INSTANCE.getNextDistributeKey());
        SessionManager.INSTANCE.add2Anonymous(session);
    }
    
    public void messageReceived(final IoSession session, final Object data) {
        final Message message = (Message)data;
        final InetSocketAddress remoteAddress = (InetSocketAddress)session.getRemoteAddress();
        if (!ProtocalSet.loginSet.contains(message.getModule())) {
            final Role role = SessionUtils.getRoleBySession(session);
            if (role == null) {
                ServerSocketIoHandler.logger.error("角色不存在，退出登录,session={} Module={} ip={} message={}", new Object[] { session.getId(), message.getModule(), remoteAddress.getAddress().getHostAddress(), Json.toJson((Object)message) });
                session.closeNow();
                return;
            }
        }
        final HashMap<String, Long> heartbeatMap = SessionManager.INSTANCE.getSessionAttr(session, SessionProperties.HEARTBEAT_MAP, HashMap.class);
        final long lastHeartTime = heartbeatMap.getOrDefault("last_heart_time", 0L);
        final long currReqTime = heartbeatMap.getOrDefault("req_current_time", 0L);
        if (lastHeartTime > 0L) {
            final long check = currReqTime - lastHeartTime;
            if (check < 0L || check > 15000L) {
                final Role role2 = SessionUtils.getRoleBySession(session);
                if (role2 != null) {
                    final RespOtherLogin respOtherLogin = new RespOtherLogin();
                    respOtherLogin.setContent("请正常游戏OK？");
                    respOtherLogin.setResult(2);
                    MessagePusher.pushMessage(session, respOtherLogin);
                    role2.getHeartCounts().clear();
                    session.closeNow();
                    ServerSocketIoHandler.logger.error("玩家编号：{}，玩家名称：{}，请求包异常，moduleId:{}, 上次心跳时间:{}，当前请求时间:{}", new Object[] { role2.getRoleId(), role2.getName(), message.getModule(), lastHeartTime, currReqTime });
                }
                else {
                    session.closeNow();
                    ServerSocketIoHandler.logger.error("-玩家编号：{}，玩家名称：{}，请求包异常，moduleId:{}, 上次心跳时间:{}，当前请求时间:{}", new Object[] { role2.getRoleId(), role2.getName(), message.getModule(), lastHeartTime, currReqTime });
                }
                return;
            }
        }
        this.messageDispatcher.dispatch(session, message);
    }
    
    public void exceptionCaught(final IoSession session, final Throwable cause) {
        if (cause instanceof IOException) {
            session.closeNow();
        }
        else {
            final Role role = SessionUtils.getRoleBySession(session);
            if (role != null) {
                ServerSocketIoHandler.logger.error("异常捕捉关闭socket 角色编号={} 角色名称-{}", (Object)role.getRoleId(), (Object)role.getName());
            }
            ServerSocketIoHandler.logger.error("exceptionCaught sessionId={} exception-{}", (Object)session.getId(), (Object)Lang.getStackTrace(cause));
        }
    }
    
    static {
        logger = LoggerFactory.getLogger(ServerSocketIoHandler.class);
        executor = new ScheduledThreadPoolExecutor(16);
    }
}
