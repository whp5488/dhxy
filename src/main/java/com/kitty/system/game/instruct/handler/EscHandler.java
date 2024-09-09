package com.kitty.system.game.instruct.handler;

import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.instruct.inter.NcFather;

public class EscHandler implements NcFather
{
    public EscHandler() {
    }
    
    @Override
    public void handle(Role role, String content) {
        try {
            if (((ServerService)SpringUtils.getBean(ServerService.class)).getType() == 1) {
                return;
            }
            if ("00".equals(content)) {
                FightNewService fightNewService = (FightNewService)SpringUtils.getBean(FightNewService.class);
                Fight fight = fightNewService.getFightByRoleId((int)role.getRoleId());
                if (fight != null) {
                    fightNewService.quitFight(fight);
                }
            }
        }
        catch (Exception e) {
            System.out.println("e = " + e);
        }
    }
}
