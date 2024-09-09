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
    
    public PlugController() {
    }
    
    @EventHandler({ EventType.MOVE })
    public void handleMoveEvent(MoveEvent moveEvent) {
        Role role = moveEvent.getRole();
        if (role == null) {
            return;
        }
        this.plugService.plugTreasureMap(role);
        return;
    }
    
    @RequestMapping
    public void plugExecute(IoSession session, ReqPlugExecuteTreasure reqPlugExecuteTreasure) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.plugService.plugExecute(role, reqPlugExecuteTreasure);
        return;
    }
    
    @RequestMapping
    public void plugCease(IoSession session, ReqPlugCease reqPlugCease) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.plugService.stopPlugExecute(role, reqPlugCease.getList());
        return;
    }
}
