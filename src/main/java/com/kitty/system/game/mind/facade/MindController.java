package com.kitty.system.game.mind.facade;

import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.mind.message.ReqUpgradeMind;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.mind.service.MindService;
import org.springframework.stereotype.Controller;

@Controller
public class MindController
{
    @Autowired
    MindService mindService;
    
    public MindController() {
    }
    
    @RequestMapping
    public void upgradeSeal(IoSession session, ReqUpgradeMind upgradeSeal) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.mindService.upgrade(role, upgradeSeal.getSerial(), upgradeSeal.getType(), upgradeSeal.getParam(), upgradeSeal.getOperationType(), true, "客户端-心法打造处理");
    }
}
