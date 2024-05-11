
package com.kitty.system.game.general.facade;

import com.kitty.system.game.general.message.ReqDomesticate;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.general.message.ReqGeneralNotify;
import org.apache.mina.core.session.IoSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.general.service.GeneralService;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;

@Controller
public class GeneralController
{
    Logger logger;
    @Autowired
    public GeneralService generalService;
    
    public GeneralController() {
        this.logger = LoggerFactory.getLogger(GeneralController.class);
    }
    
    @RequestMapping
    public void generalNotify(final IoSession session, final ReqGeneralNotify notify) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.generalService.generalNotify(role, notify);
    }
    
    @RequestMapping
    public void domesticate(final IoSession session, final ReqDomesticate reqDomesticate) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.generalService.domesticate(role, reqDomesticate);
    }
}
