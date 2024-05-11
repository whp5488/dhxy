
package com.kitty.system.game.instruct.handler;

import com.kitty.system.game.partner.domain.Partner;
import com.kitty.system.game.partner.service.PartnerService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.instruct.inter.NcFather;

public class PartnerHandler implements NcFather
{
    @Override
    public void handle(final Role role, final String code) {
        if (SpringUtils.getBean(ServerService.class).getType() == 1) {
            return;
        }
        if (code == null && !code.matches("^[0-9]+$")) {
            return;
        }
        final PartnerService bean = SpringUtils.getBean(PartnerService.class);
        final Partner partner = bean.addPartner(role, Integer.parseInt(code));
        bean.loadPartner(role, partner);
    }
}
