
package com.kitty.system.game.suit.controller;

import com.kitty.system.game.suit.message.ReqSwitchoverSuit;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.suit.message.ReqSuit;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.suit.service.SuitService;
import org.springframework.stereotype.Controller;

@Controller
public class SuitController
{
    @Autowired
    private SuitService suitService;
    
    @RequestMapping
    public void suit(final IoSession session, final ReqSuit reqSuit) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.suitService.suit(role);
        }
    }
    
    @RequestMapping
    public void switchover(final IoSession session, final ReqSwitchoverSuit switchoverSuit) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.suitService.switchover(role, Long.valueOf(switchoverSuit.getId()));
        }
    }
}
