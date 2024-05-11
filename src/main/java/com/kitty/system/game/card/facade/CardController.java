
package com.kitty.system.game.card.facade;

import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.card.message.ReqUseCard;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.card.service.CardService;
import org.springframework.stereotype.Controller;

@Controller
public class CardController
{
    @Autowired
    private CardService cardService;
    
    @RequestMapping
    public void useCar(final IoSession session, final ReqUseCard reqUseCard) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.cardService.useCar(role, reqUseCard.getSerial(), Long.valueOf(reqUseCard.getId()));
    }
}
