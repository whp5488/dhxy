
package com.kitty.system.game.ygbh.facade;

import com.kitty.mina.annotation.RequestMapping;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.ygbh.message.ReqC2sYgbhInfo;
import com.kitty.system.game.ygbh.service.YgbhService;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class YgbhController
{
    @Autowired
    private YgbhService ygbhService;
    
    @RequestMapping
    public void getYbghData(final IoSession session, final ReqC2sYgbhInfo reqC2sYgbhInfo) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.ygbhService.getYbghData(role);
    }
}
