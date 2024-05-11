
package com.kitty.system.game.plug.facade;

import com.kitty.system.game.plug.message.ReqPlugCease;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.plug.message.ReqPlugExecuteTreasure;
import org.apache.mina.core.session.IoSession;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.event.MoveEvent;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.plug.service.PlugService;
import org.springframework.stereotype.Controller;

@Controller
public class PlugController
{
    @Autowired
    private PlugService plugService;
    
    @EventHandler({ EventType.MOVE })
    public void handleMoveEvent(final MoveEvent moveEvent) {
        final Role role = moveEvent.getRole();
        if (role == null) {
            return;
        }
        this.plugService.plugTreasureMap(role);
    }
    
    @RequestMapping
    public void plugExecute(final IoSession session, final ReqPlugExecuteTreasure reqPlugExecuteTreasure) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.plugService.plugExecute(role, reqPlugExecuteTreasure);
    }
    
    @RequestMapping
    public void plugCease(final IoSession session, final ReqPlugCease reqPlugCease) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.plugService.stopPlugExecute(role, reqPlugCease.getList());
    }
}
