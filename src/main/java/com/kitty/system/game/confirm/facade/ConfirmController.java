
package com.kitty.system.game.confirm.facade;

import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.role.domain.Role;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.confirm.service.ConfirmService;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.confirm.message.ReqConfirmResult;
import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Controller;

@Controller
public class ConfirmController
{
    @RequestMapping
    public void reqConfirmResult(final IoSession session, final ReqConfirmResult reqConfirmResult) {
        final Role role = SessionUtils.getRoleBySession(session);
        SpringUtils.getBean(ConfirmService.class).handleConfirmResult(role, reqConfirmResult);
    }
}
