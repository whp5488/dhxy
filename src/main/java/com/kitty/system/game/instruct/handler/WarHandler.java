package com.kitty.system.game.instruct.handler;

import com.kitty.system.game.team.domain.Team;
import com.kitty.system.init.domain.SysDataMonster;
import java.util.List;
import com.kitty.system.game.service.MonsterService;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.instruct.inter.NcFather;

public class WarHandler implements NcFather
{
    public WarHandler() {
    }
    
    @Override
    public void handle(Role role, String content) {
        try {
            if (((ServerService)SpringUtils.getBean(ServerService.class)).getType() == 1) {
                return;
            }
            FightNewService fightNewService = (FightNewService)SpringUtils.getBean(FightNewService.class);
            boolean inFight = fightNewService.isInFight(role);
            if (!inFight) {
                MonsterService monsterService = (MonsterService)SpringUtils.getBean(MonsterService.class);
                Object[] objs = monsterService.getMapMonster(role, (int)Integer.valueOf(content));
                List<SysDataMonster> mapMonster = (List)objs[1];
                if (mapMonster == null || mapMonster.size() <= 0) {
                    return;
                }
                Team team = fightNewService.getRoleFightTeam(role);
                fightNewService.enterFieldFight(team, role);
                monsterService.flushMonsterTime((int)role.getRoleId());
            }
        }
        catch (Exception e) {
            System.out.println("e = " + e);
        }
    }
}
