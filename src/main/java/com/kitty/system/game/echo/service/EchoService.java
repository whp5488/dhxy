package com.kitty.system.game.echo.service;

import com.kitty.cache.RedisService;
import com.kitty.common.core.SchedulerManager;
import com.kitty.common.start.ServerService;
import com.kitty.common.utils.JsonUtils;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.EventDispatcher;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.HeartBeatEvent;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.echo.handler.EchoHandler;
import com.kitty.system.game.echo.pool.EchoDataPool;
import com.kitty.system.game.gate.message.RespOtherLogin;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.message.RespReplyEcho;
import com.kitty.system.game.role.service.RoleService;
import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EchoService {
  Logger logger = LoggerFactory.getLogger(EchoService.class);
  
  @Autowired
  private RedisService redisService;
  
  @Autowired
  private RoleService roleService;
  
  @Autowired
  private ServerService serverService;
  
  public void heart(final IoSession session) {
    final Role role = SessionUtils.getRoleBySession(session);
    Object object = session.getAttribute("echo-echo-echo-echo-echo");
    if (object == null) {
      object = new ArrayList();
      session.setAttribute("echo-echo-echo-echo-echo", object);
    } 
    ArrayList<Long> arrayList = (ArrayList<Long>)object;
    try {
      arrayList.add(Long.valueOf(System.currentTimeMillis()));
      if (arrayList.size() >= 10) {
        session.setAttribute("echo-echo-echo-echo-echo", new ArrayList());
        if (((Long)arrayList
          .get(1)).longValue() - ((Long)arrayList.get(0)).longValue() <= 8000L && ((Long)arrayList.get(2)).longValue() - ((Long)arrayList.get(1)).longValue() <= 8000L && ((Long)arrayList.get(3)).longValue() - ((Long)arrayList.get(2)).longValue() <= 8000L && ((Long)arrayList
          .get(4)).longValue() - ((Long)arrayList.get(3)).longValue() <= 8000L && ((Long)arrayList.get(5)).longValue() - ((Long)arrayList.get(4)).longValue() <= 8000L && ((Long)arrayList.get(6)).longValue() - ((Long)arrayList.get(5)).longValue() <= 8000L && ((Long)arrayList
          .get(7)).longValue() - ((Long)arrayList.get(6)).longValue() <= 8000L && ((Long)arrayList.get(8)).longValue() - ((Long)arrayList.get(7)).longValue() <= 8000L && ((Long)arrayList.get(9)).longValue() - ((Long)arrayList.get(8)).longValue() <= 8000L) {
          this.logger.error("心跳检测到加速器=={}", role.getName());
          MessagePusher.pushMessage(session, (Message)new RespNotifyMiscEx("#R服务器认为你开了加速软件,3秒后将下线!!!#"));
          MessagePusher.pushMessage(session, (Message)new RespNotifyMiscEx("#R服务器认为你开了加速软件,3秒后将下线!!!#"));
          MessagePusher.pushMessage(session, (Message)new RespNotifyMiscEx("#R服务器认为你开了加速软件,3秒后将下线!!!#"));
          ScheduledFuture<?> timeOutTask = SchedulerManager.getInstance().schedule(new Runnable() {
                public void run() {
                  EchoService.this.logger.error("加速器断开连接=={}", role.getName());
                  session.closeNow();
                }
              }, 3000L);
          return;
        } 
      } 
    } catch (Exception e) {
      this.logger.error("心跳包出错==角色名称={}==roleId={}", role.getName(), role.getRoleId());
    } 
    MessagePusher.pushMessage(session, (Message)new RespReplyEcho());
    if (role != null) {
      long prevHeartTime = role.getPrevHeartTime();
      role.setPrevHeartTime(System.currentTimeMillis());
      checkHeart(role, prevHeartTime, session);
      EventDispatcher.getInstance().fireEvent((BaseGameEvent)new HeartBeatEvent(EventType.HEART_BEAT, role, prevHeartTime));
    } 
  }
  
  private void checkHeart(Role role, long prevHeartTime, IoSession session) {
    if (role == null)
      return; 
    int currentMin = (int)(System.currentTimeMillis() / 60000L);
    int count = ((Integer)role.getHeartCounts().getOrDefault(Integer.valueOf(currentMin), Integer.valueOf(0))).intValue() + 1;
    role.getHeartCounts().put(Integer.valueOf(currentMin), Integer.valueOf(count));
    if (isHeartUnlaw(role, session, currentMin, prevHeartTime)) {
      this.logger.error("{}({})使用加速器！！，包的数量:{}, 上一次心跳时间:{}，已封号处理", new Object[] { role.getId(), role.getName(), JsonUtils.object2String(role.getHeartCounts()), Long.valueOf(prevHeartTime) });
      RespOtherLogin respOtherLogin = new RespOtherLogin();
      respOtherLogin.setContent("请正常游戏");
      respOtherLogin.setResult(Integer.valueOf(2));
      MessagePusher.pushMessage(role, (Message)respOtherLogin);
      role.getHeartCounts().clear();
      session.closeNow();
    } 
  }
  
  private boolean isHeartUnlaw(Role role, IoSession session, int currentMin, long prevHeartTime) {
    int count = ((Integer)role.getHeartCounts().getOrDefault(Integer.valueOf(currentMin), Integer.valueOf(0))).intValue();
    if (count >= 10)
      return true; 
    if (role.getHeartCounts().size() > 1) {
      int speedCount = (int)role.getHeartCounts().values().stream().filter(v -> (v.intValue() >= 9)).count();
      role.getHeartCounts().remove(Integer.valueOf(currentMin - 1));
      if (speedCount >= 1)
        return true; 
    } 
    return false;
  }
  
  @EventHandler({EventType.HEART_BEAT})
  public void handleHeartBeat(HeartBeatEvent heartBeatEvent) {
    Role role = heartBeatEvent.getRole();
    if (role.getState() == 2) {
      this.logger.info("{}，恢复在线状态", role.getName());
      role.setState(1);
      cancelCountdown(role.getId().longValue());
    } 
    if (DataCache.OFFLINE_ROLES.containsKey(role.getId()))
      this.roleService.removeOffline(role); 
    EchoDataPool.echoHandlers.stream().forEach(handler -> handler.handleEcho(heartBeatEvent));
  }
  
  public void cancelCountdown(long id) {
    String marking = this.serverService.getMarking();
    String key = id + "_countdown_" + marking + "*";
    this.redisService.delContain(key);
  }
}
