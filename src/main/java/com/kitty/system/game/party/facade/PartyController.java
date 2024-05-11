
package com.kitty.system.game.party.facade;

import com.kitty.listener.event.RoleLevelUpEvent;
import com.kitty.listener.event.RoleChangeEvent;
import com.kitty.listener.event.LogoutEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.LoginEvent;
import com.kitty.system.game.party.message.ReqPartyRedBagRecord;
import com.kitty.system.game.party.message.ReqPartyRecvRedBag;
import com.kitty.system.game.party.message.ReqShowRedBag;
import com.kitty.system.game.party.message.ReqPartyRedBagList;
import com.kitty.system.game.party.message.ReqPartySendRedBag;
import com.kitty.system.game.party.message.ReqPartyRedBagSendInfo;
import com.kitty.system.game.party.message.ReqPartyRole;
import com.kitty.system.game.party.message.ReqPartyRename;
import com.kitty.system.game.party.message.ReqPartyLog;
import com.kitty.system.game.party.message.ReqControlPartyChannel;
import com.kitty.system.game.party.message.ReqPartyRejectLevel;
import com.kitty.system.game.party.message.ReqModifyNotice;
import com.kitty.system.game.party.message.ReqPartyMembers;
import com.kitty.system.game.party.message.ReqQueryPartys;
import com.kitty.system.game.party.message.ReqPartyPolitics;
import com.kitty.system.game.party.message.ReqSavePartyFlag;
import com.kitty.system.game.party.message.ReqPartyUnlockFlag;
import com.kitty.system.game.party.model.Party;
import com.kitty.system.game.party.message.ReqPartyFlag;
import com.kitty.system.game.party.message.ReqJoinPartyList;
import com.kitty.system.game.party.message.ReqPartyModifyMember;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.party.message.ReqCreateParty;
import org.apache.mina.core.session.IoSession;
import com.kitty.system.game.party.service.PartyRedBagHandler;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.party.service.PartyService;
import org.springframework.stereotype.Controller;

@Controller
public class PartyController
{
    @Autowired
    private PartyService partyService;
    @Autowired
    private PartyRedBagHandler partyRedBagHandler;
    
    @RequestMapping
    public void createPatry(final IoSession session, final ReqCreateParty createParty) {
        final Role role = SessionUtils.getRoleBySession(session);
        this.partyService.confirmCreateParty(role, createParty);
    }
    
    @RequestMapping
    public void modifyParty(final IoSession session, final ReqPartyModifyMember reqPartyModifyMember) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (reqPartyModifyMember.getJob() == -2) {
            this.partyService.kickOut(role, reqPartyModifyMember.getId());
        }
        else if (reqPartyModifyMember.getJob() == 0) {
            this.partyService.quitParty(role);
        }
        else {
            this.partyService.changeJob(role, reqPartyModifyMember.getId(), reqPartyModifyMember.getName(), reqPartyModifyMember.getJob());
        }
    }
    
    @RequestMapping
    public void checkJoinList(final IoSession session, final ReqJoinPartyList reqJoinPartyList) {
        final Role role = SessionUtils.getRoleBySession(session);
        this.partyService.checkJoinList(role);
    }
    
    @RequestMapping
    public void partyFlag(final IoSession session, final ReqPartyFlag reqPartyFlag) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            final Party party = this.partyService.getParty(role);
            if (party != null) {
                this.partyService.partyFlag(party, role);
            }
        }
    }
    
    @RequestMapping
    public void unlockPartyFlag(final IoSession session, final ReqPartyUnlockFlag reqPartyUnlockFlag) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            final Party party = this.partyService.getParty(role);
            if (party != null) {
                this.partyService.unlockPartyFlag(party, role, reqPartyUnlockFlag);
            }
        }
    }
    
    @RequestMapping
    public void savePartyFlag(final IoSession session, final ReqSavePartyFlag reqPartyFlag) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            final Party party = this.partyService.getParty(role);
            if (party != null) {
                this.partyService.savePartyFlag(party, role, reqPartyFlag);
            }
        }
    }
    
    @RequestMapping
    public void partyPolitics(final IoSession session, final ReqPartyPolitics partyPolitics) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            final Party party = this.partyService.getParty(role);
            if (party != null) {
                this.partyService.getRespOpenPatry(party, role);
            }
        }
    }
    
    @RequestMapping
    public void queryParties(final IoSession session, final ReqQueryPartys reqQueryPartys) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.partyService.queryParties(role, reqQueryPartys);
        }
    }
    
    @RequestMapping
    public void checkPartyMember(final IoSession session, final ReqPartyMembers reqPartyMembers) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.partyService.checkPartyMember(role, reqPartyMembers);
        }
    }
    
    @RequestMapping
    public void modifyNotice(final IoSession session, final ReqModifyNotice reqModifyNotice) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.partyService.modifyNotice(role, reqModifyNotice.getMsg(), reqModifyNotice.getType());
        }
    }
    
    @RequestMapping
    public void autoAgree(final IoSession session, final ReqPartyRejectLevel reqPartyRejectLevel) {
        final Role role = SessionUtils.getRoleBySession(session);
        this.partyService.setAutoAgreeSetting(role, reqPartyRejectLevel);
    }
    
    @RequestMapping
    public void controlPartyChannel(final IoSession session, final ReqControlPartyChannel reqControlPartyChannel) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (reqControlPartyChannel.getType() == 1) {
            this.partyService.prohibitSpeaking(role, role.getRoleId(), reqControlPartyChannel.getName(), (short)1);
        }
        else if (reqControlPartyChannel.getType() == 2) {
            this.partyService.cancelProhibitSpeaking(role, role.getRoleId(), reqControlPartyChannel.getName());
        }
    }
    
    @RequestMapping
    public void getLog(final IoSession session, final ReqPartyLog reqPartyLog) {
        final Role role = SessionUtils.getRoleBySession(session);
        this.partyService.getPartyLog(role);
    }
    
    @RequestMapping
    public void rename(final IoSession session, final ReqPartyRename reqPartyRename) {
        final Role role = SessionUtils.getRoleBySession(session);
        this.partyService.rename(role, reqPartyRename.getName());
    }
    
    @RequestMapping
    public void getPartyRoleInfo(final IoSession session, final ReqPartyRole partyRole) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.partyService.getPartyRoleInfo(role, partyRole.getRoleId());
        }
    }
    
    @RequestMapping
    public void reqSendBagInfo(final IoSession session, final ReqPartyRedBagSendInfo reqPartyRedBagSendInfo) {
        final Role role = SessionUtils.getRoleBySession(session);
        this.partyRedBagHandler.sendRedBagSetting(role);
    }
    
    @RequestMapping
    public void reqSendRedBag(final IoSession session, final ReqPartySendRedBag reqPartySendRedBag) {
        final Role role = SessionUtils.getRoleBySession(session);
        this.partyRedBagHandler.sendRedBag(role, reqPartySendRedBag.getMoney(), reqPartySendRedBag.getNum(), reqPartySendRedBag.getMsg(), reqPartySendRedBag.getFormat());
    }
    
    @RequestMapping
    public void reqRedBagList(final IoSession session, final ReqPartyRedBagList reqPartyRedBagList) {
        final Role role = SessionUtils.getRoleBySession(session);
        this.partyRedBagHandler.showRedBagList(role);
    }
    
    @RequestMapping
    public void reqShowRedBag(final IoSession session, final ReqShowRedBag reqShowRedBag) {
        final Role role = SessionUtils.getRoleBySession(session);
        this.partyRedBagHandler.showRedBag(role, Long.parseLong(reqShowRedBag.getRedBagGid()));
    }
    
    @RequestMapping
    public void reqRecvRedBag(final IoSession session, final ReqPartyRecvRedBag reqPartyRecvRedBag) {
        final Role role = SessionUtils.getRoleBySession(session);
        this.partyRedBagHandler.fetchRedBag(role, Long.parseLong(reqPartyRecvRedBag.getRedBagGid()));
    }
    
    @RequestMapping
    public void reqRedBagRecord(final IoSession session, final ReqPartyRedBagRecord reqPartyRedBagRecord) {
        final Role role = SessionUtils.getRoleBySession(session);
        this.partyRedBagHandler.showRedBagRecord(role, reqPartyRedBagRecord.getType());
    }
    
    @EventHandler({ EventType.LOGIN })
    public void handleLoginEvent(final LoginEvent loginEvent) {
        this.partyService.handleLogin(loginEvent.getRole());
    }
    
    @EventHandler({ EventType.LOGOUT })
    public void handleLogoutEvent(final LogoutEvent logoutEvent) {
        this.partyService.handleLogout(logoutEvent.getRole());
    }
    
    @EventHandler({ EventType.ROLE_CHANGE })
    public void handleRoleNameChangeEvent(final RoleChangeEvent roleChangeEvent) {
        this.partyService.handleRoleChange(roleChangeEvent.getRole(), roleChangeEvent.getCurrName());
    }
    
    @EventHandler({ EventType.ROLE_LEVEL_UP })
    public void handleRoleLevelUpEvent(final RoleLevelUpEvent roleLevelUpEvent) {
        final Role role = roleLevelUpEvent.getRole();
        if (role == null) {
            return;
        }
        this.partyService.handleRoleLevelUpEvent(roleLevelUpEvent.getRole());
    }
}
