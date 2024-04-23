package com.kitty.system.game.echo.facade;

import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.echo.service.EchoService;
import com.kitty.system.game.role.message.ReqEcho;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class EnterController {
  @Autowired
  EchoService echoService;
  
  @RequestMapping
  public void heart(IoSession session, ReqEcho reqEcho) {
    this.echoService.heart(session);
  }
}
