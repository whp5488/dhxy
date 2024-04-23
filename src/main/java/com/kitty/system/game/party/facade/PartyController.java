package com.kitty.system.game.party.facade;

import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.LoginEvent;
import com.kitty.listener.event.LogoutEvent;
import com.kitty.listener.event.RoleChangeEvent;
import com.kitty.listener.event.RoleLevelUpEvent;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.party.message.ReqControlPartyChannel;
import com.kitty.system.game.party.message.ReqCreateParty;
import com.kitty.system.game.party.message.ReqJoinPartyList;
import com.kitty.system.game.party.message.ReqModifyNotice;
import com.kitty.system.game.party.message.ReqPartyFlag;
import com.kitty.system.game.party.message.ReqPartyLog;
import com.kitty.system.game.party.message.ReqPartyMembers;
import com.kitty.system.game.party.message.ReqPartyModifyMember;
import com.kitty.system.game.party.message.ReqPartyPolitics;
import com.kitty.system.game.party.message.ReqPartyRecvRedBag;
import com.kitty.system.game.party.message.ReqPartyRedBagList;
import com.kitty.system.game.party.message.ReqPartyRedBagRecord;
import com.kitty.system.game.party.message.ReqPartyRedBagSendInfo;
import com.kitty.system.game.party.message.ReqPartyRejectLevel;
import com.kitty.system.game.party.message.ReqPartyRename;
import com.kitty.system.game.party.message.ReqPartyRole;
import com.kitty.system.game.party.message.ReqPartySendRedBag;
import com.kitty.system.game.party.message.ReqPartyUnlockFlag;
import com.kitty.system.game.party.message.ReqQueryPartys;
import com.kitty.system.game.party.message.ReqSavePartyFlag;
import com.kitty.system.game.party.message.ReqShowRedBag;
import com.kitty.system.game.party.model.Party;
import com.kitty.system.game.party.service.PartyRedBagHandler;
import com.kitty.system.game.party.service.PartyService;
import com.kitty.system.game.role.domain.Role;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class PartyController {
  @Autowired
  private PartyService partyService;
  
  @Autowired
  private PartyRedBagHandler partyRedBagHandler;
  
  @RequestMapping
  public void createPatry(IoSession session, ReqCreateParty createParty) {
    Role role = SessionUtils.getRoleBySession(session);
    this.partyService.confirmCreateParty(role, createParty);
  }
  
  @RequestMapping
  public void modifyParty(IoSession session, ReqPartyModifyMember reqPartyModifyMember) {
    Role role = SessionUtils.getRoleBySession(session);
    if (reqPartyModifyMember.getJob() == -2) {
      this.partyService.kickOut(role, reqPartyModifyMember.getId().intValue());
    } else if (reqPartyModifyMember.getJob() == 0) {
      this.partyService.quitParty(role);
    } else {
      this.partyService.changeJob(role, reqPartyModifyMember.getId().intValue(), reqPartyModifyMember.getName(), reqPartyModifyMember.getJob());
    } 
  }
  
  @RequestMapping
  public void checkJoinList(IoSession session, ReqJoinPartyList reqJoinPartyList) {
    Role role = SessionUtils.getRoleBySession(session);
    this.partyService.checkJoinList(role);
  }
  
  @RequestMapping
  public void partyFlag(IoSession session, ReqPartyFlag reqPartyFlag) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null) {
      Party party = this.partyService.getParty(role);
      if (party != null)
        this.partyService.partyFlag(party, role); 
    } 
  }
  
  @RequestMapping
  public void unlockPartyFlag(IoSession session, ReqPartyUnlockFlag reqPartyUnlockFlag) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null) {
      Party party = this.partyService.getParty(role);
      if (party != null)
        this.partyService.unlockPartyFlag(party, role, reqPartyUnlockFlag); 
    } 
  }
  
  @RequestMapping
  public void savePartyFlag(IoSession session, ReqSavePartyFlag reqPartyFlag) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null) {
      Party party = this.partyService.getParty(role);
      if (party != null)
        this.partyService.savePartyFlag(party, role, reqPartyFlag); 
    } 
  }
  
  @RequestMapping
  public void partyPolitics(IoSession session, ReqPartyPolitics partyPolitics) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null) {
      Party party = this.partyService.getParty(role);
      if (party != null)
        this.partyService.getRespOpenPatry(party, role); 
    } 
  }
  
  @RequestMapping
  public void queryParties(IoSession session, ReqQueryPartys reqQueryPartys) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      this.partyService.queryParties(role, reqQueryPartys); 
  }
  
  @RequestMapping
  public void checkPartyMember(IoSession session, ReqPartyMembers reqPartyMembers) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      this.partyService.checkPartyMember(role, reqPartyMembers); 
  }
  
  @RequestMapping
  public void modifyNotice(IoSession session, ReqModifyNotice reqModifyNotice) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      this.partyService.modifyNotice(role, reqModifyNotice.getMsg(), reqModifyNotice.getType()); 
  }
  
  @RequestMapping
  public void autoAgree(IoSession session, ReqPartyRejectLevel reqPartyRejectLevel) {
    Role role = SessionUtils.getRoleBySession(session);
    this.partyService.setAutoAgreeSetting(role, reqPartyRejectLevel);
  }
  
  @RequestMapping
  public void controlPartyChannel(IoSession session, ReqControlPartyChannel reqControlPartyChannel) {
    Role role = SessionUtils.getRoleBySession(session);
    if (reqControlPartyChannel.getType() == 1) {
      this.partyService.prohibitSpeaking(role, role.getRoleId().intValue(), reqControlPartyChannel.getName(), (short)1);
    } else if (reqControlPartyChannel.getType() == 2) {
      this.partyService.cancelProhibitSpeaking(role, role.getRoleId().intValue(), reqControlPartyChannel.getName());
    } 
  }
  
  @RequestMapping
  public void getLog(IoSession session, ReqPartyLog reqPartyLog) {
    Role role = SessionUtils.getRoleBySession(session);
    this.partyService.getPartyLog(role);
  }
  
  @RequestMapping
  public void rename(IoSession session, ReqPartyRename reqPartyRename) {
    Role role = SessionUtils.getRoleBySession(session);
    this.partyService.rename(role, reqPartyRename.getName());
  }
  
  @RequestMapping
  public void getPartyRoleInfo(IoSession session, ReqPartyRole partyRole) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      this.partyService.getPartyRoleInfo(role, partyRole.getRoleId()); 
  }
  
  @RequestMapping
  public void reqSendBagInfo(IoSession session, ReqPartyRedBagSendInfo reqPartyRedBagSendInfo) {
    Role role = SessionUtils.getRoleBySession(session);
    this.partyRedBagHandler.sendRedBagSetting(role);
  }
  
  @RequestMapping
  public void reqSendRedBag(IoSession session, ReqPartySendRedBag reqPartySendRedBag) {
    Role role = SessionUtils.getRoleBySession(session);
    this.partyRedBagHandler.sendRedBag(role, reqPartySendRedBag.getMoney(), reqPartySendRedBag.getNum(), reqPartySendRedBag.getMsg(), reqPartySendRedBag.getFormat());
  }
  
  @RequestMapping
  public void reqRedBagList(IoSession session, ReqPartyRedBagList reqPartyRedBagList) {
    Role role = SessionUtils.getRoleBySession(session);
    this.partyRedBagHandler.showRedBagList(role);
  }
  
  @RequestMapping
  public void reqShowRedBag(IoSession session, ReqShowRedBag reqShowRedBag) {
    Role role = SessionUtils.getRoleBySession(session);
    this.partyRedBagHandler.showRedBag(role, Long.parseLong(reqShowRedBag.getRedBagGid()));
  }
  
  @RequestMapping
  public void reqRecvRedBag(IoSession session, ReqPartyRecvRedBag reqPartyRecvRedBag) {
    Role role = SessionUtils.getRoleBySession(session);
    this.partyRedBagHandler.fetchRedBag(role, Long.parseLong(reqPartyRecvRedBag.getRedBagGid()));
  }
  
  @RequestMapping
  public void reqRedBagRecord(IoSession session, ReqPartyRedBagRecord reqPartyRedBagRecord) {
    Role role = SessionUtils.getRoleBySession(session);
    this.partyRedBagHandler.showRedBagRecord(role, reqPartyRedBagRecord.getType());
  }
  
  @EventHandler({EventType.LOGIN})
  public void handleLoginEvent(LoginEvent loginEvent) {
    this.partyService.handleLogin(loginEvent.getRole());
  }
  
  @EventHandler({EventType.LOGOUT})
  public void handleLogoutEvent(LogoutEvent logoutEvent) {
    this.partyService.handleLogout(logoutEvent.getRole());
  }
  
  @EventHandler({EventType.ROLE_CHANGE})
  public void handleRoleNameChangeEvent(RoleChangeEvent roleChangeEvent) {
    this.partyService.handleRoleChange(roleChangeEvent.getRole(), roleChangeEvent.getCurrName());
  }
  
  @EventHandler({EventType.ROLE_LEVEL_UP})
  public void handleRoleLevelUpEvent(RoleLevelUpEvent roleLevelUpEvent) {
    Role role = roleLevelUpEvent.getRole();
    if (role == null)
      return; 
    this.partyService.handleRoleLevelUpEvent(roleLevelUpEvent.getRole());
  }
}
