
package com.kitty.system.game.instruct.handler;

import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.instruct.inter.NcFather;

public class EscHandler implements NcFather
{
    @Override
    public void handle(final Role role, final String content) {
        try {
            if (SpringUtils.getBean(ServerService.class).getType() == 1) {
                return;
            }
            if ("00".equals(content)) {
                final FightNewService fightNewService = SpringUtils.getBean(FightNewService.class);
                final Fight fight = fightNewService.getFightByRoleId(role.getRoleId());
                if (fight != null) {
                    fightNewService.quitFight(fight);
                }
            }
        }
        catch (final Exception e) {
            System.out.println("e = " + e);
        }
    }
}
