
package com.kitty.system.game.enter.controller;

import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.enter.message.ReqLogout;
import com.kitty.system.init.domain.SysMapInfo;
import com.kitty.system.init.domain.SysMapTeleporter;
import com.kitty.listener.event.EnterMapEvent;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.event.LeaveMapEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.EventDispatcher;
import com.kitty.system.game.enter.message.ReqEnterRoom;
import com.kitty.system.game.enter.message.ReqTelePort;
import java.util.concurrent.locks.ReentrantLock;
import com.kitty.mina.cache.DataCache;
import java.util.concurrent.locks.Lock;
import com.kitty.logs.LoggerFunction;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.gate.message.RespOtherLogin;
import com.kitty.mina.session.SessionProperties;
import com.kitty.mina.session.SessionManager;
import com.kitty.system.game.enter.message.ReqLoadExistedChar;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.account.domain.Account;
import com.kitty.system.game.role.domain.Role;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.enter.message.ReqRefreshUserData;
import org.apache.mina.core.session.IoSession;
import org.slf4j.LoggerFactory;
import com.kitty.system.game.map.service.MapService;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.player.PlayerService;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;

@Controller
public class EnterGameController
{
    private Logger logger;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private FightNewService fightNewService;
    @Autowired
    private MapService mapService;
    
    public EnterGameController() {
        this.logger = LoggerFactory.getLogger(EnterGameController.class);
    }
    
    @RequestMapping
    public void refreshUserData(final IoSession session, final ReqRefreshUserData refreshUserData) {
        final Role role = SessionUtils.getRoleBySession(session);
        try {
            final Account account = SpringUtils.getAccountService().getAccount(role.getSid());
            synchronized (account) {
                role.setMobile(account.getMobile());
                this.roleService.enterMap(session, role);
            }
        }
        catch (final Exception e) {
            this.logger.error("后台切回报错=={}", (Object)role.getName());
        }
    }
    
    @RequestMapping
    public void loadExistedRole(final IoSession session, final ReqLoadExistedChar req) {
        final Role temp = SessionUtils.getRoleBySession(session);
        if (temp != null && !temp.getId().equals(req.getId())) {
            session.closeNow();
            return;
        }
        final Role role = this.roleService.getPlayer(Long.valueOf(req.getId()));
        if (role == null) {
            return;
        }
        final String sid = SessionManager.INSTANCE.getSessionAttr(session, SessionProperties.ACCOUNT_SID, String.class);
        if (sid == null || !sid.equals(role.getSid())) {
            final RespOtherLogin respOtherLogin = new RespOtherLogin();
            respOtherLogin.setContent("角色登录失败。#r（原因：9025）");
            respOtherLogin.setResult(0);
            MessagePusher.pushMessage(session, respOtherLogin);
            if (role.getSid() != null) {
                LoggerFunction.MONOIOR.getLogger().warn("角色登录失败。#r（原因：9025）,sessionSid:{},roleSid:{},roleUid:{},roleName:{}", new Object[] { sid, role.getSid(), role.getId(), role.getName() });
            }
            else {
                LoggerFunction.MONOIOR.getLogger().warn("角色登录失败。#r（原因：9025）,sessionSid:{},roleSid:{}");
            }
            return;
        }
        final Long attr = SessionManager.INSTANCE.getSessionAttr(session, SessionProperties.PLAYER_UID, Long.class);
        if (attr != null && attr > 0L) {
            this.logger.error("角色已登录=UID:{},角色名:{}", (Object)attr, (Object)SessionManager.INSTANCE.getSessionAttr(session, SessionProperties.PLAYER_NAME, String.class));
            return;
        }
        try {
            final Account account = SpringUtils.getAccountService().getAccount(role.getSid());
            if (!this.roleService.canLogin(account, session)) {
                return;
            }
            final Lock lock = DataCache.UID_LOCK_MAP.getOrDefault(role.getId(), new ReentrantLock());
            if (lock.tryLock()) {
                try {
                    role.setMobile(account.getMobile());
                    this.playerService.loadRoleRests(role);
                    this.roleService.enterMap(session, role);
                }
                finally {
                    lock.unlock();
                }
            }
            else {
                this.logger.error("获取不到锁, 登录=UID:{}", (Object)role.getId());
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    @RequestMapping
    public void changeMap(final IoSession session, final ReqTelePort reqTelePort) {
        this.mapService.changeMap(SessionUtils.getRoleBySession(session), reqTelePort, null);
    }
    
    @RequestMapping
    public void enterRoom(final IoSession session, final ReqEnterRoom reqEnterRoom) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final SysMapTeleporter transfer = this.mapService.getTrasfer(reqEnterRoom.getMapName(), role.getPos().getMapId());
        if (transfer == null) {
            return;
        }
        final SysMapInfo map = this.mapService.getMap(transfer.getToMapSerial());
        if (map == null) {
            return;
        }
        final int oldMapId = role.getPos().getMapId();
        EventDispatcher.getInstance().fireEvent(new LeaveMapEvent(EventType.LEAVE_MAP, role, oldMapId));
        role.setPos(transfer.getToMapX(), transfer.getToMapY(), transfer.getDir(), transfer.getToMapSerial());
        EventDispatcher.getInstance().fireEvent(new EnterMapEvent(EventType.ENTER_MAP, role));
    }
    
    @RequestMapping
    public void quiltRole(final IoSession session, final ReqLogout reqLogout) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            final Fight fightByRoleId = this.fightNewService.getFightByRoleId(role.getRoleId());
            if (fightByRoleId != null) {
                MessagePusher.pushMessage(session, new RespMsg("战斗中不可以直接退出哦"));
            }
            this.roleService.addOffline(role, true);
        }
    }
}
