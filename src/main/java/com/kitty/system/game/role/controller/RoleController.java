
package com.kitty.system.game.role.controller;

import com.kitty.system.game.role.message.ReqUpdateInstruct;
import com.kitty.system.game.suit.message.ReqSwitchoverSuit;
import com.kitty.system.game.pet.message.ReqAddAssistSuit;
import com.kitty.system.game.pet.message.ReqAssistPetList;
import com.kitty.common.utils.TimeUtil;
import com.kitty.system.game.enter.message.RespReplyServerTime;
import com.kitty.system.game.enter.message.ReqAskServerTime;
import com.kitty.system.game.role.message.ReqRestAttributeHero;
import com.kitty.system.game.role.message.ReqResetAssignPartyPractice;
import com.kitty.system.game.role.message.ReqAssignPartyPractice;
import com.kitty.system.game.role.message.ReqPartyPracticeUpGrade;
import com.kitty.system.game.role.message.ReqPartyPracticeInfo;
import com.kitty.system.game.enter.message.ReqDey;
import com.kitty.system.game.enter.message.ReqExtendStoreSize;
import com.kitty.system.game.general.message.ReqAssignAttrib;
import com.kitty.system.game.general.message.ReqChange;
import com.kitty.system.game.general.message.ReqSetRecommendAttrib;
import com.kitty.system.game.role.message.ReqChangeTitle;
import com.kitty.system.game.role.message.ReqRecover;
import com.kitty.system.game.role.message.ReqMobile;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.role.message.ReqVerificationCode;
import com.kitty.system.game.role.message.RandomName;
import com.kitty.system.game.role.message.RespRandomName;
import com.kitty.system.game.role.message.ReqRandomName;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.domain.RoleInfo;
import java.util.List;
import com.kitty.system.game.role.message.RespCreateNewRole;
import com.kitty.system.game.task.service.handler.MainTaskHandler;
import java.io.Serializable;
import com.kitty.system.game.player.PlayerProfile;
import java.text.MessageFormat;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.init.pool.PartDataPool;
import org.nutz.lang.Strings;
import com.kitty.mina.session.SessionManager;
import com.kitty.system.game.role.message.ReqCreateNewChar;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.account.domain.Account;
import com.kitty.mina.session.SessionProperties;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.gate.message.RespOtherLogin;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.account.service.AccountService;
import com.kitty.mina.filter.ModuleCounterFilter;
import com.kitty.system.game.role.message.ReqLogin;
import org.apache.mina.core.session.IoSession;
import org.slf4j.LoggerFactory;
import com.kitty.system.game.suit.service.SuitService;
import com.kitty.system.game.role.service.TitleService;
import com.kitty.cache.RedisService;
import com.kitty.system.game.role.service.SettingService;
import com.kitty.system.game.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.player.PlayerService;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;

@Controller
public class RoleController
{
    Logger logger;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private TitleService titleService;
    @Autowired
    private SuitService suitService;
    
    public RoleController() {
        this.logger = LoggerFactory.getLogger(RoleController.class);
    }
    
    @RequestMapping
    public void requestLogin(final IoSession session, final ReqLogin request) {
        final String sid = request.getAccountId();
        if (sid.contains("-")) {
            ModuleCounterFilter.banIp(session);
            return;
        }
        if (sid.length() != "01l3bRUZ".length()) {
            ModuleCounterFilter.banIp(session);
            return;
        }
        final Account account = SpringUtils.getBean(AccountService.class).getAccount(sid);
        if (account == null) {
            final RespOtherLogin respOtherLogin = new RespOtherLogin();
            respOtherLogin.setContent("账号登录失败。#r(原因：9027)");
            respOtherLogin.setResult(0);
            MessagePusher.pushMessage(session, respOtherLogin);
            ModuleCounterFilter.banIp(session);
            return;
        }
        if (!SpringUtils.getRoleService().canLogin(account, session)) {
            return;
        }
        if (account != null) {
            session.setAttribute((Object)SessionProperties.ACCOUNT_SID, (Object)sid);
            final String marking = request.getMarking();
            this.playerService.listRoleInfos(sid, marking, session);
        }
    }
    
    @RequestMapping
    public void createRole(final IoSession session, final ReqCreateNewChar reqCreateNewChar) {
        final String sid = SessionManager.INSTANCE.getSessionAttr(session, SessionProperties.ACCOUNT_SID, String.class);
        if (Strings.isEmpty((CharSequence)sid)) {
            final RespOtherLogin respOtherLogin = new RespOtherLogin();
            respOtherLogin.setContent("请重新登录账号。#r（原因：9028）");
            respOtherLogin.setResult(0);
            MessagePusher.pushMessage(session, respOtherLogin);
            return;
        }
        if (!PartDataPool.MARKING.contains(reqCreateNewChar.getMarking())) {
            MessagePusher.pushMessage(session, new RespMsg("创建失败"));
            return;
        }
        final List<RoleInfo> roleInfos = this.playerService.listRoleInfos(sid, reqCreateNewChar.getMarking(), null);
        if (roleInfos.size() >= 1) {
            final RespMsg respMsg = new RespMsg();
            respMsg.setMsg(MessageFormat.format("你已经创建了#<R>{0}个#角色，不能创建更多角色。", roleInfos.size()));
            MessagePusher.pushMessage(session, respMsg);
            return;
        }
        final Role newRole = this.roleService.createRole(session, sid, reqCreateNewChar);
        if (newRole == null) {
            MessagePusher.pushMessage(session, new RespMsg("创建失败"));
            return;
        }
        this.redisService.set(sid, new PlayerProfile(newRole), 60L);
        this.playerService.cacheRoles(sid, newRole.getId());
        SpringUtils.getBean(MainTaskHandler.class).acceptFirstMainTask(newRole);
        SessionManager.INSTANCE.removeFromAnonymous(session);
        this.playerService.listRoleInfos(sid, reqCreateNewChar.getMarking(), session);
        final RespCreateNewRole respCreateNewRole = new RespCreateNewRole();
        respCreateNewRole.setId(newRole.getRoleId());
        respCreateNewRole.setName(newRole.getName());
        MessagePusher.pushMessage(session, respCreateNewRole);
    }
    
    @RequestMapping
    public void randomName(final IoSession session, final ReqRandomName reqRandomName) {
        final RespRandomName respRandomName = new RespRandomName();
        respRandomName.setName(RandomName.getChineseName());
        MessagePusher.pushMessage(session, respRandomName);
    }
    
    @RequestMapping
    public void verificationCode(final IoSession session, final ReqVerificationCode verificationCode) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.roleService.verificationCode(role, verificationCode.getMobile());
        }
    }
    
    @RequestMapping
    public void relationMobile(final IoSession session, final ReqMobile reqMobile) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.roleService.relationMobile(role, reqMobile.getVerificationCode());
        }
    }
    
    @RequestMapping
    public void recover(final IoSession session, final ReqRecover recover) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.roleService.recover(role, recover.getType());
        }
    }
    
    @RequestMapping
    public void changeTitle(final IoSession session, final ReqChangeTitle changeTitle) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.titleService.changeTitle(role, changeTitle.getSerial(), changeTitle.getType());
        }
    }
    
    @RequestMapping
    public void commendAttrib(final IoSession session, final ReqSetRecommendAttrib notify) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.roleService.commendAttrib(role, notify);
    }
    
    @RequestMapping
    public void reqRelive(final IoSession session, final ReqChange reqChange) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        if (reqChange.getType() == 0) {
            this.roleService.changeRelive(role, reqChange.getSerial());
        }
        else {
            this.roleService.changeRace(role, reqChange.getSerial());
        }
    }
    
    @RequestMapping
    public void assignAttrib(final IoSession session, final ReqAssignAttrib assignAttrib) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.roleService.assignAttrib(role, assignAttrib);
    }
    
    @RequestMapping
    public void extendStoreSize(final IoSession session, final ReqExtendStoreSize extendStoreSize) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.roleService.extendStoreSize(role, extendStoreSize);
        }
    }
    
    @RequestMapping
    public void dye(final IoSession session, final ReqDey reqDey) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.roleService.dye(role, reqDey);
        }
    }
    
    @RequestMapping
    public void refreshPartyPracticeInfo(final IoSession session, final ReqPartyPracticeInfo partyPracticeInfo) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.roleService.refreshPartyPracticeInfo(role);
        }
    }
    
    @RequestMapping
    public void partyPractice(final IoSession session, final ReqPartyPracticeUpGrade practiceUpGrade) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null && practiceUpGrade.getGrade() > 0) {
            if (practiceUpGrade.getGrade() < 0) {
                return;
            }
            this.roleService.practiceUpGrade(role, practiceUpGrade.getGrade());
        }
    }
    
    @RequestMapping
    public void refreshPartyPracticeInfo(final IoSession session, final ReqAssignPartyPractice assignPartyPractice) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.roleService.assignPartyPractice(role, assignPartyPractice.getNutMap());
        }
    }
    
    @RequestMapping
    public void refreshPartyPracticeInfo(final IoSession session, final ReqResetAssignPartyPractice resetAssignPartyPractice) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.roleService.resetAssignPartyPractice(role);
        }
    }
    
    @RequestMapping
    public void restAttribute(final IoSession session, final ReqRestAttributeHero reqRestAttributeHero) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.roleService.restAttribute(role, reqRestAttributeHero.getType());
        }
    }
    
    @RequestMapping
    public void askServerTime(final IoSession session, final ReqAskServerTime askServerTime) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            final RespReplyServerTime respReplyServerTime = new RespReplyServerTime();
            respReplyServerTime.setServerTime(String.valueOf(TimeUtil.timeInMillis()));
            MessagePusher.pushMessage(role, respReplyServerTime);
        }
    }
    
    @RequestMapping
    public void getAssistPetList(final IoSession session, final ReqAssistPetList reqAssistPetList) {
        final Role role = SessionUtils.getRoleBySession(session);
        final Integer serial = reqAssistPetList.getSerial();
        if (serial == null) {
            return;
        }
        this.roleService.getAssistPetList(role, serial);
    }
    
    @RequestMapping
    public void addSuit(final IoSession session, final ReqAddAssistSuit reqAddAssistSuit) {
        final Role role = SessionUtils.getRoleBySession(session);
        final String name = reqAddAssistSuit.getName();
        if (name == null) {
            return;
        }
        final List<Integer> petIds = reqAddAssistSuit.getPetIds();
        if (petIds == null || petIds.size() <= 0) {
            return;
        }
        final Integer serial = reqAddAssistSuit.getSerial();
        this.roleService.addSuit(role, serial, name, petIds);
    }
    
    @RequestMapping
    public void switchover(final IoSession session, final ReqSwitchoverSuit switchoverSuit) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.suitService.switchover(role, Long.valueOf(switchoverSuit.getId()));
        }
    }
    
    @RequestMapping
    public void updateInstruct(final IoSession session, final ReqUpdateInstruct updateInstruct) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.settingService.updateInstruct(role, updateInstruct);
    }
}
