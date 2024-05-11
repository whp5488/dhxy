
package com.kitty.system.game.echo.service;

import com.kitty.system.game.echo.handler.EchoHandler;
import java.io.Serializable;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.system.game.echo.pool.EchoDataPool;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.gate.message.RespOtherLogin;
import com.kitty.common.utils.JsonUtils;
import java.util.concurrent.ScheduledFuture;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.event.HeartBeatEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.EventDispatcher;
import com.kitty.system.game.role.message.RespReplyEcho;
import com.kitty.system.game.role.domain.Role;
import com.kitty.common.core.SchedulerManager;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import java.util.ArrayList;
import com.kitty.mina.cache.SessionUtils;
import org.apache.mina.core.session.IoSession;
import org.slf4j.LoggerFactory;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.cache.RedisService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class EchoService
{
    Logger logger;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ServerService serverService;
    
    public EchoService() {
        this.logger = LoggerFactory.getLogger(EchoService.class);
    }
    
    public void heart(final IoSession session) {
        final Role role = SessionUtils.getRoleBySession(session);
        Object object = session.getAttribute((Object)"echo-echo-echo-echo-echo");
        if (object == null) {
            object = new ArrayList();
            session.setAttribute((Object)"echo-echo-echo-echo-echo", object);
        }
        final ArrayList<Long> arrayList = (ArrayList<Long>)object;
        try {
            arrayList.add(System.currentTimeMillis());
            if (arrayList.size() >= 10) {
                session.setAttribute((Object)"echo-echo-echo-echo-echo", (Object)new ArrayList());
                if (arrayList.get(1) - arrayList.get(0) <= 8000L && arrayList.get(2) - arrayList.get(1) <= 8000L && arrayList.get(3) - arrayList.get(2) <= 8000L && arrayList.get(4) - arrayList.get(3) <= 8000L && arrayList.get(5) - arrayList.get(4) <= 8000L && arrayList.get(6) - arrayList.get(5) <= 8000L && arrayList.get(7) - arrayList.get(6) <= 8000L && arrayList.get(8) - arrayList.get(7) <= 8000L && arrayList.get(9) - arrayList.get(8) <= 8000L) {
                    this.logger.error("心跳检测到加速器=={}", (Object)role.getName());
                    MessagePusher.pushMessage(session, new RespNotifyMiscEx("#R服务器认为你开了加速软件,3秒后将下线!!!#"));
                    MessagePusher.pushMessage(session, new RespNotifyMiscEx("#R服务器认为你开了加速软件,3秒后将下线!!!#"));
                    MessagePusher.pushMessage(session, new RespNotifyMiscEx("#R服务器认为你开了加速软件,3秒后将下线!!!#"));
                    final ScheduledFuture<?> timeOutTask = SchedulerManager.getInstance().schedule(new Runnable() {
                        @Override
                        public void run() {
                            EchoService.this.logger.error("加速器断开连接=={}", (Object)role.getName());
                            session.closeNow();
                        }
                    }, 3000L);
                    return;
                }
            }
        }
        catch (final Exception e) {
            this.logger.error("心跳包出错==角色名称={}==roleId={}", (Object)role.getName(), (Object)role.getRoleId());
        }
        MessagePusher.pushMessage(session, new RespReplyEcho());
        if (role != null) {
            final long prevHeartTime = role.getPrevHeartTime();
            role.setPrevHeartTime(System.currentTimeMillis());
            this.checkHeart(role, prevHeartTime, session);
            EventDispatcher.getInstance().fireEvent(new HeartBeatEvent(EventType.HEART_BEAT, role, prevHeartTime));
        }
    }
    
    private void checkHeart(final Role role, final long prevHeartTime, final IoSession session) {
        if (role == null) {
            return;
        }
        final int currentMin = (int)(System.currentTimeMillis() / 60000L);
        final int count = role.getHeartCounts().getOrDefault(currentMin, 0) + 1;
        role.getHeartCounts().put(currentMin, count);
        if (this.isHeartUnlaw(role, session, currentMin, prevHeartTime)) {
            this.logger.error("{}({})使用加速器！！，包的数量:{}, 上一次心跳时间:{}，已封号处理", new Object[] { role.getId(), role.getName(), JsonUtils.object2String(role.getHeartCounts()), prevHeartTime });
            final RespOtherLogin respOtherLogin = new RespOtherLogin();
            respOtherLogin.setContent("请正常游戏");
            respOtherLogin.setResult(2);
            MessagePusher.pushMessage(role, respOtherLogin);
            role.getHeartCounts().clear();
            session.closeNow();
        }
    }
    
    private boolean isHeartUnlaw(final Role role, final IoSession session, final int currentMin, final long prevHeartTime) {
        final int count = role.getHeartCounts().getOrDefault(currentMin, 0);
        if (count >= 10) {
            return true;
        }
        if (role.getHeartCounts().size() > 1) {
            final int speedCount = (int)role.getHeartCounts().values().stream().filter(v -> v >= 9).count();
            role.getHeartCounts().remove(currentMin - 1);
            if (speedCount >= 1) {
                return true;
            }
        }
        return false;
    }
    
    @EventHandler({ EventType.HEART_BEAT })
    public void handleHeartBeat(final HeartBeatEvent heartBeatEvent) {
        final Role role = heartBeatEvent.getRole();
        if (role.getState() == 2) {
            this.logger.info("{}，恢复在线状态", (Object)role.getName());
            role.setState(1);
            this.cancelCountdown(role.getId());
        }
        if (DataCache.OFFLINE_ROLES.containsKey(role.getId())) {
            this.roleService.removeOffline(role);
        }
        EchoDataPool.echoHandlers.stream().forEach(handler -> handler.handleEcho(heartBeatEvent));
    }
    
    public void cancelCountdown(final long id) {
        final String marking = this.serverService.getMarking();
        final String key = id + "_countdown_" + marking + "*";
        this.redisService.delContain(key);
    }
}
