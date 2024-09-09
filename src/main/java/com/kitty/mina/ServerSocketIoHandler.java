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
    
    public ServerSocketIoHandler(IMessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }
    
    public void sessionOpened(final IoSession session) {
    }
    
    public void sessionClosed(IoSession session) {
        String sid = (String)SessionManager.INSTANCE.getSessionAttr(session, SessionProperties.ACCOUNT_SID, String.class);
        ((QueueUpService)SpringUtils.getBean(QueueUpService.class)).remove(sid);
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        try {
            SpringUtils.getRoleService().addOffline(role, true);
        }
        catch (Exception e) {
            ServerSocketIoHandler.logger.error("Session关闭错误信息===={}", Lang.getStackTrace(e));
        }
        return;
    }
    
    public void sessionCreated(IoSession session) {
        session.setAttributeIfAbsent(SessionProperties.DISTRIBUTE_KEY, Integer.valueOf(SessionManager.INSTANCE.getNextDistributeKey()));
        SessionManager.INSTANCE.add2Anonymous(session);
    }
    
    public void messageReceived(IoSession session, Object data) {
        Message message = (Message)data;
        InetSocketAddress remoteAddress = (InetSocketAddress)session.getRemoteAddress();
        if (!ProtocalSet.loginSet.contains(Integer.valueOf(message.getModule()))) {
            Role role = SessionUtils.getRoleBySession(session);
            if (role == null) {
                ServerSocketIoHandler.logger.error("角色不存在，退出登录,session={} Module={} ip={} message={}", new Object[] { Long.valueOf(session.getId()), Integer.valueOf(message.getModule()), remoteAddress.getAddress().getHostAddress(), Json.toJson(message) });
                session.closeNow();
                return;
            }
        }
        HashMap<String, Long> heartbeatMap = (HashMap)SessionManager.INSTANCE.getSessionAttr(session, SessionProperties.HEARTBEAT_MAP, HashMap.class);
        long lastHeartTime = (long)heartbeatMap.getOrDefault("last_heart_time", Long.valueOf(0L));
        long currReqTime = (long)heartbeatMap.getOrDefault("req_current_time", Long.valueOf(0L));
        if (lastHeartTime > 0L) {
            long check = currReqTime - lastHeartTime;
            if (check < 0L || check > 15000L) {
                Role role2 = SessionUtils.getRoleBySession(session);
                if (role2 != null) {
                    RespOtherLogin respOtherLogin = new RespOtherLogin();
                    respOtherLogin.setContent("请正常游戏OK？");
                    respOtherLogin.setResult(Integer.valueOf(2));
                    MessagePusher.pushMessage(session, respOtherLogin);
                    role2.getHeartCounts().clear();
                    session.closeNow();
                    ServerSocketIoHandler.logger.error("玩家编号：{}，玩家名称：{}，请求包异常，moduleId:{}, 上次心跳时间:{}，当前请求时间:{}", new Object[] { role2.getRoleId(), role2.getName(), Integer.valueOf(message.getModule()), Long.valueOf(lastHeartTime), Long.valueOf(currReqTime) });
                }
                else {
                    session.closeNow();
                    ServerSocketIoHandler.logger.error("-玩家编号：{}，玩家名称：{}，请求包异常，moduleId:{}, 上次心跳时间:{}，当前请求时间:{}", new Object[] { role2.getRoleId(), role2.getName(), Integer.valueOf(message.getModule()), Long.valueOf(lastHeartTime), Long.valueOf(currReqTime) });
                }
                return;
            }
        }
        this.messageDispatcher.dispatch(session, message);
    }
    
    public void exceptionCaught(IoSession session, Throwable cause) {
        if ((cause instanceof IOException)) {
            session.closeNow();
        }
        else {
            Role role = SessionUtils.getRoleBySession(session);
            if (role != null) {
                ServerSocketIoHandler.logger.error("异常捕捉关闭socket 角色编号={} 角色名称-{}", role.getRoleId(), role.getName());
            }
            ServerSocketIoHandler.logger.error("exceptionCaught sessionId={} exception-{}", Long.valueOf(session.getId()), Lang.getStackTrace(cause));
        }
    }
    
    static {
        logger = LoggerFactory.getLogger(ServerSocketIoHandler.class);
        executor = new ScheduledThreadPoolExecutor(16);
    }
}
