package com.kitty.system.game.instruct.handler;

import com.kitty.system.game.partner.domain.Partner;
import com.kitty.system.game.partner.service.PartnerService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.instruct.inter.NcFather;

public class PartnerHandler implements NcFather
{
    public PartnerHandler() {
    }
    
    @Override
    public void handle(Role role, String code) {
        if (((ServerService)SpringUtils.getBean(ServerService.class)).getType() == 1) {
            return;
        }
        if (code == null && !code.matches("^[0-9]+$")) {
            return;
        }
        PartnerService bean = (PartnerService)SpringUtils.getBean(PartnerService.class);
        Partner partner = bean.addPartner(role, Integer.valueOf(Integer.parseInt(code)));
        bean.loadPartner(role, partner);
        return;
    }
}
