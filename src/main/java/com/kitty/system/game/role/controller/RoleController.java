package com.kitty.system.game.role.controller;

import com.kitty.cache.RedisService;
import com.kitty.common.utils.TimeUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.mina.filter.ModuleCounterFilter;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.mina.session.SessionManager;
import com.kitty.mina.session.SessionProperties;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.account.domain.Account;
import com.kitty.system.game.account.service.AccountService;
import com.kitty.system.game.enter.message.ReqAskServerTime;
import com.kitty.system.game.enter.message.ReqDey;
import com.kitty.system.game.enter.message.ReqExtendStoreSize;
import com.kitty.system.game.enter.message.RespReplyServerTime;
import com.kitty.system.game.gate.message.RespOtherLogin;
import com.kitty.system.game.general.message.ReqAssignAttrib;
import com.kitty.system.game.general.message.ReqChange;
import com.kitty.system.game.general.message.ReqSetRecommendAttrib;
import com.kitty.system.game.pet.message.ReqAddAssistSuit;
import com.kitty.system.game.pet.message.ReqAssistPetList;
import com.kitty.system.game.player.PlayerProfile;
import com.kitty.system.game.player.PlayerService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.domain.RoleInfo;
import com.kitty.system.game.role.message.RandomName;
import com.kitty.system.game.role.message.ReqAssignPartyPractice;
import com.kitty.system.game.role.message.ReqChangeTitle;
import com.kitty.system.game.role.message.ReqCreateNewChar;
import com.kitty.system.game.role.message.ReqLogin;
import com.kitty.system.game.role.message.ReqMobile;
import com.kitty.system.game.role.message.ReqPartyPracticeInfo;
import com.kitty.system.game.role.message.ReqPartyPracticeUpGrade;
import com.kitty.system.game.role.message.ReqRandomName;
import com.kitty.system.game.role.message.ReqRecover;
import com.kitty.system.game.role.message.ReqResetAssignPartyPractice;
import com.kitty.system.game.role.message.ReqRestAttributeHero;
import com.kitty.system.game.role.message.ReqUpdateInstruct;
import com.kitty.system.game.role.message.ReqVerificationCode;
import com.kitty.system.game.role.message.RespCreateNewRole;
import com.kitty.system.game.role.message.RespRandomName;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.role.service.SettingService;
import com.kitty.system.game.role.service.TitleService;
import com.kitty.system.game.suit.message.ReqSwitchoverSuit;
import com.kitty.system.game.suit.service.SuitService;
import com.kitty.system.game.task.service.handler.MainTaskHandler;
import com.kitty.system.init.pool.PartDataPool;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;
import org.apache.mina.core.session.IoSession;
import org.nutz.lang.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class RoleController {
  Logger logger = LoggerFactory.getLogger(RoleController.class);
  
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
  
  @RequestMapping
  public void requestLogin(IoSession session, ReqLogin request) {
    String sid = request.getAccountId();
    if (sid.contains("-")) {
      ModuleCounterFilter.banIp(session);
      return;
    } 
    if (sid.length() != "01l3bRUZ".length()) {
      ModuleCounterFilter.banIp(session);
      return;
    } 
    Account account = ((AccountService)SpringUtils.getBean(AccountService.class)).getAccount(sid);
    if (account == null) {
      RespOtherLogin respOtherLogin = new RespOtherLogin();
      respOtherLogin.setContent("账号登录失败。#r(原因：9027)");
      respOtherLogin.setResult(Integer.valueOf(0));
      MessagePusher.pushMessage(session, (Message)respOtherLogin);
      ModuleCounterFilter.banIp(session);
      return;
    } 
    if (!SpringUtils.getRoleService().canLogin(account, session))
      return; 
    if (account != null) {
      session.setAttribute(SessionProperties.ACCOUNT_SID, sid);
      String marking = request.getMarking();
      this.playerService.listRoleInfos(sid, marking, session);
    } 
  }
  
  @RequestMapping
  public void createRole(IoSession session, ReqCreateNewChar reqCreateNewChar) {
    String sid = (String)SessionManager.INSTANCE.getSessionAttr(session, SessionProperties.ACCOUNT_SID, String.class);
    if (Strings.isEmpty(sid)) {
      RespOtherLogin respOtherLogin = new RespOtherLogin();
      respOtherLogin.setContent("请重新登录账号。#r（原因：9028）");
      respOtherLogin.setResult(Integer.valueOf(0));
      MessagePusher.pushMessage(session, (Message)respOtherLogin);
      return;
    } 
    if (!PartDataPool.MARKING.contains(reqCreateNewChar.getMarking())) {
      MessagePusher.pushMessage(session, (Message)new RespMsg("创建失败"));
      return;
    } 
    List<RoleInfo> roleInfos = this.playerService.listRoleInfos(sid, reqCreateNewChar.getMarking(), null);
    if (roleInfos.size() >= 1) {
      RespMsg respMsg = new RespMsg();
      respMsg.setMsg(MessageFormat.format("你已经创建了#<R>{0}个#角色，不能创建更多角色。", new Object[] { Integer.valueOf(roleInfos.size()) }));
      MessagePusher.pushMessage(session, (Message)respMsg);
      return;
    } 
    Role newRole = this.roleService.createRole(session, sid, reqCreateNewChar);
    if (newRole == null) {
      MessagePusher.pushMessage(session, (Message)new RespMsg("创建失败"));
      return;
    } 
    this.redisService.set(sid, (Serializable)new PlayerProfile(newRole), 60L);
    this.playerService.cacheRoles(sid, newRole.getId());
    ((MainTaskHandler)SpringUtils.getBean(MainTaskHandler.class)).acceptFirstMainTask(newRole);
    SessionManager.INSTANCE.removeFromAnonymous(session);
    this.playerService.listRoleInfos(sid, reqCreateNewChar.getMarking(), session);
    RespCreateNewRole respCreateNewRole = new RespCreateNewRole();
    respCreateNewRole.setId(newRole.getRoleId());
    respCreateNewRole.setName(newRole.getName());
    MessagePusher.pushMessage(session, (Message)respCreateNewRole);
  }
  
  @RequestMapping
  public void randomName(IoSession session, ReqRandomName reqRandomName) {
    RespRandomName respRandomName = new RespRandomName();
    respRandomName.setName(RandomName.getChineseName());
    MessagePusher.pushMessage(session, (Message)respRandomName);
  }
  
  @RequestMapping
  public void verificationCode(IoSession session, ReqVerificationCode verificationCode) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      this.roleService.verificationCode(role, verificationCode.getMobile()); 
  }
  
  @RequestMapping
  public void relationMobile(IoSession session, ReqMobile reqMobile) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      this.roleService.relationMobile(role, reqMobile.getVerificationCode()); 
  }
  
  @RequestMapping
  public void recover(IoSession session, ReqRecover recover) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      this.roleService.recover(role, recover.getType()); 
  }
  
  @RequestMapping
  public void changeTitle(IoSession session, ReqChangeTitle changeTitle) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      this.titleService.changeTitle(role, changeTitle.getSerial(), changeTitle.getType()); 
  }
  
  @RequestMapping
  public void commendAttrib(IoSession session, ReqSetRecommendAttrib notify) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.roleService.commendAttrib(role, notify);
  }
  
  @RequestMapping
  public void reqRelive(IoSession session, ReqChange reqChange) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    if (reqChange.getType() == 0) {
      this.roleService.changeRelive(role, Integer.valueOf(reqChange.getSerial()));
    } else {
      this.roleService.changeRace(role, Integer.valueOf(reqChange.getSerial()));
    } 
  }
  
  @RequestMapping
  public void assignAttrib(IoSession session, ReqAssignAttrib assignAttrib) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.roleService.assignAttrib(role, assignAttrib);
  }
  
  @RequestMapping
  public void extendStoreSize(IoSession session, ReqExtendStoreSize extendStoreSize) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      this.roleService.extendStoreSize(role, extendStoreSize); 
  }
  
  @RequestMapping
  public void dye(IoSession session, ReqDey reqDey) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      this.roleService.dye(role, reqDey); 
  }
  
  @RequestMapping
  public void refreshPartyPracticeInfo(IoSession session, ReqPartyPracticeInfo partyPracticeInfo) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      this.roleService.refreshPartyPracticeInfo(role); 
  }
  
  @RequestMapping
  public void partyPractice(IoSession session, ReqPartyPracticeUpGrade practiceUpGrade) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null && practiceUpGrade.getGrade() > 0) {
      if (practiceUpGrade.getGrade() < 0)
        return; 
      this.roleService.practiceUpGrade(role, practiceUpGrade.getGrade());
    } 
  }
  
  @RequestMapping
  public void refreshPartyPracticeInfo(IoSession session, ReqAssignPartyPractice assignPartyPractice) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      this.roleService.assignPartyPractice(role, assignPartyPractice.getNutMap()); 
  }
  
  @RequestMapping
  public void refreshPartyPracticeInfo(IoSession session, ReqResetAssignPartyPractice resetAssignPartyPractice) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      this.roleService.resetAssignPartyPractice(role); 
  }
  
  @RequestMapping
  public void restAttribute(IoSession session, ReqRestAttributeHero reqRestAttributeHero) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      this.roleService.restAttribute(role, reqRestAttributeHero.getType()); 
  }
  
  @RequestMapping
  public void askServerTime(IoSession session, ReqAskServerTime askServerTime) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null) {
      RespReplyServerTime respReplyServerTime = new RespReplyServerTime();
      respReplyServerTime.setServerTime(String.valueOf(TimeUtil.timeInMillis()));
      MessagePusher.pushMessage(role, (Message)respReplyServerTime);
    } 
  }
  
  @RequestMapping
  public void getAssistPetList(IoSession session, ReqAssistPetList reqAssistPetList) {
    Role role = SessionUtils.getRoleBySession(session);
    Integer serial = reqAssistPetList.getSerial();
    if (serial == null)
      return; 
    this.roleService.getAssistPetList(role, serial);
  }
  
  @RequestMapping
  public void addSuit(IoSession session, ReqAddAssistSuit reqAddAssistSuit) {
    Role role = SessionUtils.getRoleBySession(session);
    String name = reqAddAssistSuit.getName();
    if (name == null)
      return; 
    List<Integer> petIds = reqAddAssistSuit.getPetIds();
    if (petIds == null || petIds.size() <= 0)
      return; 
    Integer serial = reqAddAssistSuit.getSerial();
    this.roleService.addSuit(role, serial, name, petIds);
  }
  
  @RequestMapping
  public void switchover(IoSession session, ReqSwitchoverSuit switchoverSuit) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      this.suitService.switchover(role, Long.valueOf(switchoverSuit.getId()).longValue()); 
  }
  
  @RequestMapping
  public void updateInstruct(IoSession session, ReqUpdateInstruct updateInstruct) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.settingService.updateInstruct(role, updateInstruct);
  }
}
