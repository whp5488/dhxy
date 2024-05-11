
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
    
    @EventHandler({ EventType.ENTER_MAP })
    public void handleEnterMapEvent(final EnterMapEvent enterMapEvent) {
        final Role role = enterMapEvent.getRole();
        this.pkService.checkEnterMap(role);
    }
    
    @RequestMapping
    public void searchRole(final IoSession session, final ReqSearchRole searchRole) {
        this.pkService.searchRole(session, searchRole.getName());
    }
    
    @RequestMapping
    public void traceRole(final IoSession session, final ReqTraceRole traceRole) {
        this.pkService.traceRole(session, traceRole.getRoleId());
    }
    
    @RequestMapping
    public void traceGo(final IoSession session, final ReqTraceGo traceGo) {
        this.pkService.traceGo(session);
    }
    
    @RequestMapping
    public void releaseInfo(final IoSession session, final ReqReleaseInfo reqReleaseInfo) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.pkService.releaseInfo(role, reqReleaseInfo.getId());
    }
    
    @RequestMapping
    public void confirmReleasePrisoner(final IoSession session, final ReqPrisonerRelease reqPrisonerRelease) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.pkService.confirmReleasePrisoner(role, reqPrisonerRelease.getId());
    }
}
