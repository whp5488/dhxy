package com.kitty.system.game.general.facade;

import com.kitty.mina.annotation.RequestMapping;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.general.message.ReqDomesticate;
import com.kitty.system.game.general.message.ReqGeneralNotify;
import com.kitty.system.game.general.service.GeneralService;
import com.kitty.system.game.role.domain.Role;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class GeneralController {
  Logger logger = LoggerFactory.getLogger(GeneralController.class);
  
  @Autowired
  public GeneralService generalService;
  
  @RequestMapping
  public void generalNotify(IoSession session, ReqGeneralNotify notify) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.generalService.generalNotify(role, notify);
  }
  
  @RequestMapping
  public void domesticate(IoSession session, ReqDomesticate reqDomesticate) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.generalService.domesticate(role, reqDomesticate);
  }
}
