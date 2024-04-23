package com.kitty.mina;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.mina.message.IMessageDispatcher;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.mina.session.SessionManager;
import com.kitty.mina.session.SessionProperties;
import com.kitty.system.game.gate.message.RespOtherLogin;
import com.kitty.system.game.gate.model.QueueUpService;
import com.kitty.system.game.role.domain.Role;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerSocketIoHandler extends IoHandlerAdapter {
  private static final Logger logger = LoggerFactory.getLogger(ServerSocketIoHandler.class);
  
  private static final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(16);
  
  private static final long interval = 15000L;
  
  private final IMessageDispatcher messageDispatcher;
  
  public ServerSocketIoHandler(IMessageDispatcher messageDispatcher) {
    this.messageDispatcher = messageDispatcher;
  }
  
  public void sessionOpened(IoSession session) {}
  
  public void sessionClosed(IoSession session) {
    String sid = (String)SessionManager.INSTANCE.getSessionAttr(session, SessionProperties.ACCOUNT_SID, String.class);
    ((QueueUpService)SpringUtils.getBean(QueueUpService.class)).remove(sid);
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    try {
      SpringUtils.getRoleService().addOffline(role, true);
    } catch (Exception e) {
      logger.error("Session关闭错误信息===={}", Lang.getStackTrace(e));
    } 
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
        logger.error("角色不存在，退出登录,session={} Module={} ip={} message={}", new Object[] { Long.valueOf(session.getId()), Integer.valueOf(message.getModule()), remoteAddress.getAddress().getHostAddress(), Json.toJson(message) });
        session.closeNow();
        return;
      } 
    } 
    HashMap<String, Long> heartbeatMap = (HashMap<String, Long>)SessionManager.INSTANCE.getSessionAttr(session, SessionProperties.HEARTBEAT_MAP, HashMap.class);
    long lastHeartTime = ((Long)heartbeatMap.getOrDefault("last_heart_time", Long.valueOf(0L))).longValue();
    long currReqTime = ((Long)heartbeatMap.getOrDefault("req_current_time", Long.valueOf(0L))).longValue();
    if (lastHeartTime > 0L) {
      long check = currReqTime - lastHeartTime;
      if (check < 0L || check > 15000L) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
          RespOtherLogin respOtherLogin = new RespOtherLogin();
          respOtherLogin.setContent("请正常游戏OK？");
          respOtherLogin.setResult(Integer.valueOf(2));
          MessagePusher.pushMessage(session, (Message)respOtherLogin);
          role.getHeartCounts().clear();
          session.closeNow();
          logger.error("玩家编号：{}，玩家名称：{}，请求包异常，moduleId:{}, 上次心跳时间:{}，当前请求时间:{}", new Object[] { role.getRoleId(), role.getName(), Integer.valueOf(message.getModule()), Long.valueOf(lastHeartTime), Long.valueOf(currReqTime) });
        } else {
          session.closeNow();
          logger.error("-玩家编号：{}，玩家名称：{}，请求包异常，moduleId:{}, 上次心跳时间:{}，当前请求时间:{}", new Object[] { role.getRoleId(), role.getName(), Integer.valueOf(message.getModule()), Long.valueOf(lastHeartTime), Long.valueOf(currReqTime) });
        } 
        return;
      } 
    } 
    this.messageDispatcher.dispatch(session, message);
  }
  
  public void exceptionCaught(IoSession session, Throwable cause) {
    if (cause instanceof java.io.IOException) {
      session.closeNow();
    } else {
      Role role = SessionUtils.getRoleBySession(session);
      if (role != null)
        logger.error("异常捕捉关闭socket 角色编号={} 角色名称-{}", role.getRoleId(), role.getName()); 
      logger.error("exceptionCaught sessionId={} exception-{}", Long.valueOf(session.getId()), Lang.getStackTrace(cause));
    } 
  }
}
