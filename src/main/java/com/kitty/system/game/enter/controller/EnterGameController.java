package com.kitty.system.game.enter.controller;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.EventDispatcher;
import com.kitty.listener.EventType;
import com.kitty.listener.event.EnterMapEvent;
import com.kitty.listener.event.LeaveMapEvent;
import com.kitty.logs.LoggerFunction;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.mina.session.SessionManager;
import com.kitty.mina.session.SessionProperties;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.account.domain.Account;
import com.kitty.system.game.enter.message.ReqEnterRoom;
import com.kitty.system.game.enter.message.ReqLoadExistedChar;
import com.kitty.system.game.enter.message.ReqLogout;
import com.kitty.system.game.enter.message.ReqRefreshUserData;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.gate.message.RespOtherLogin;
import com.kitty.system.game.map.service.MapService;
import com.kitty.system.game.player.PlayerService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.init.domain.SysMapInfo;
import com.kitty.system.init.domain.SysMapTeleporter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class EnterGameController {
  private Logger logger = LoggerFactory.getLogger(EnterGameController.class);
  
  @Autowired
  private PlayerService playerService;
  
  @Autowired
  private RoleService roleService;
  
  @Autowired
  private FightNewService fightNewService;
  
  @Autowired
  private MapService mapService;
  
  @RequestMapping
  public void refreshUserData(IoSession session, ReqRefreshUserData refreshUserData) {
    Role role = SessionUtils.getRoleBySession(session);
    try {
      Account account = SpringUtils.getAccountService().getAccount(role.getSid());
      synchronized (account) {
        role.setMobile(account.getMobile());
        this.roleService.enterMap(session, role);
      } 
    } catch (Exception e) {
      this.logger.error("后台切回报错=={}", role.getName());
    } 
  }
  
  @RequestMapping
  public void loadExistedRole(IoSession session, ReqLoadExistedChar req) {
    Role temp = SessionUtils.getRoleBySession(session);
    if (temp != null && !temp.getId().equals(req.getId())) {
      session.closeNow();
      return;
    } 
    Role role = this.roleService.getPlayer(Long.valueOf(req.getId()));
    if (role == null)
      return; 
    String sid = SessionManager.INSTANCE.getSessionAttr(session, SessionProperties.ACCOUNT_SID, String.class);
    if (sid == null || !sid.equals(role.getSid())) {
      RespOtherLogin respOtherLogin = new RespOtherLogin();
      respOtherLogin.setContent("角色登录失败。#r（原因：9025）");
      respOtherLogin.setResult(0);
      MessagePusher.pushMessage(session, respOtherLogin);
      if (role.getSid() != null) {
        LoggerFunction.MONOIOR.getLogger().warn("角色登录失败。#r（原因：9025）,sessionSid:{},roleSid:{},roleUid:{},roleName:{}", sid, role.getSid(), role.getId(), role.getName());
      } else {
        LoggerFunction.MONOIOR.getLogger().warn("角色登录失败。#r（原因：9025）,sessionSid:{},roleSid:{}", sid, role.getSid());
      } 
      return;
    } 
    Long attr = SessionManager.INSTANCE.getSessionAttr(session, SessionProperties.PLAYER_UID, Long.class);
    if (attr != null && attr > 0L) {
      this.logger.error("角色已登录=UID:{},角色名:{}", attr, SessionManager.INSTANCE.getSessionAttr(session, SessionProperties.PLAYER_NAME, String.class));
      return;
    } 
    try {
      Account account = SpringUtils.getAccountService().getAccount(role.getSid());
      if (!this.roleService.canLogin(account, session))
        return; 
      Lock lock = DataCache.UID_LOCK_MAP.getOrDefault(role.getId(), new ReentrantLock());
      if (lock.tryLock()) {
        try {
          role.setMobile(account.getMobile());
          this.playerService.loadRoleRests(role);
          this.roleService.enterMap(session, role);
        } finally {
          lock.unlock();
        } 
      } else {
        this.logger.error("获取不到锁, 登录=UID:{}", role.getId());
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  @RequestMapping
  public void changeMap(IoSession session, ReqTelePort reqTelePort) {
    this.mapService.changeMap(SessionUtils.getRoleBySession(session), reqTelePort, null);
  }
  
  @RequestMapping
  public void enterRoom(IoSession session, ReqEnterRoom reqEnterRoom) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    SysMapTeleporter transfer = this.mapService.getTrasfer(reqEnterRoom.getMapName(), role.getPos().getMapId());
    if (transfer == null)
      return; 
    SysMapInfo map = this.mapService.getMap(transfer.getToMapSerial());
    if (map == null)
      return; 
    int oldMapId = role.getPos().getMapId();
    EventDispatcher.getInstance().fireEvent(new LeaveMapEvent(EventType.LEAVE_MAP, role, oldMapId));
    role.setPos(transfer.getToMapX(), transfer.getToMapY(), transfer.getDir(), transfer.getToMapSerial());
    EventDispatcher.getInstance().fireEvent(new EnterMapEvent(EventType.ENTER_MAP, role));
  }
  
  @RequestMapping
  public void quiltRole(IoSession session, ReqLogout reqLogout) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null) {
      Fight fightByRoleId = this.fightNewService.getFightByRoleId(role.getRoleId());
      if (fightByRoleId != null)
        MessagePusher.pushMessage(session, (Message)new RespMsg("战斗中不可以直接退出哦")); 
      this.roleService.addOffline(role, true);
    } 
  }
}
