package com.kitty.system.game.pk.facade;

import com.kitty.system.game.pk.message.ReqPrisonerRelease;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.pk.message.ReqReleaseInfo;
import com.kitty.system.game.pk.message.ReqTraceGo;
import com.kitty.system.game.pk.message.ReqTraceRole;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.pk.message.ReqSearchRole;
import org.apache.mina.core.session.IoSession;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.event.EnterMapEvent;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.pk.service.PkService;
import org.springframework.stereotype.Controller;

@Controller
public class PkController
{
    @Autowired
    PkService pkService;
    
    public PkController() {
    }
    
    @EventHandler({ EventType.ENTER_MAP })
    public void handleEnterMapEvent(EnterMapEvent enterMapEvent) {
        Role role = enterMapEvent.getRole();
        this.pkService.checkEnterMap(role);
    }
    
    @RequestMapping
    public void searchRole(IoSession session, ReqSearchRole searchRole) {
        this.pkService.searchRole(session, searchRole.getName());
    }
    
    @RequestMapping
    public void traceRole(IoSession session, ReqTraceRole traceRole) {
        this.pkService.traceRole(session, traceRole.getRoleId());
    }
    
    @RequestMapping
    public void traceGo(IoSession session, ReqTraceGo traceGo) {
        this.pkService.traceGo(session);
    }
    
    @RequestMapping
    public void releaseInfo(IoSession session, ReqReleaseInfo reqReleaseInfo) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.pkService.releaseInfo(role, reqReleaseInfo.getId());
        return;
    }
    
    @RequestMapping
    public void confirmReleasePrisoner(IoSession session, ReqPrisonerRelease reqPrisonerRelease) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.pkService.confirmReleasePrisoner(role, reqPrisonerRelease.getId());
        return;
    }
}
