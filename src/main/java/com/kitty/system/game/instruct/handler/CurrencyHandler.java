
package com.kitty.system.game.instruct.handler;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.instruct.inter.NcFather;

public class CurrencyHandler implements NcFather
{
    @Override
    public void handle(final Role role, final String content) {
        if (SpringUtils.getBean(ServerService.class).getType() == 1) {
            return;
        }
        final String[] split = content.split(":");
        if (split.length > 1) {
            final Integer type = Integer.valueOf(split[0]);
            final Long num = Long.valueOf(split[1]);
            SpringUtils.getRoleService().addCurrency(role, type, num, "命令", true, false);
        }
    }
}
