package com.kitty.system.game.card.facade;

import com.kitty.mina.annotation.RequestMapping;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.card.message.ReqUseCard;
import com.kitty.system.game.card.service.CardService;
import com.kitty.system.game.role.domain.Role;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CardController {
  @Autowired
  private CardService cardService;
  
  @RequestMapping
  public void useCar(IoSession session, ReqUseCard reqUseCard) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.cardService.useCar(role, reqUseCard.getSerial(), Long.valueOf(reqUseCard.getId()).longValue());
  }
}
