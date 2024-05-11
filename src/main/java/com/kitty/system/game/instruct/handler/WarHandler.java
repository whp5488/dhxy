
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
    @Override
    public void handle(final Role role, final String content) {
        try {
            if (SpringUtils.getBean(ServerService.class).getType() == 1) {
                return;
            }
            final FightNewService fightNewService = SpringUtils.getBean(FightNewService.class);
            final boolean inFight = fightNewService.isInFight(role);
            if (!inFight) {
                final MonsterService monsterService = SpringUtils.getBean(MonsterService.class);
                final Object[] objs = monsterService.getMapMonster(role, Integer.valueOf(content));
                final List<SysDataMonster> mapMonster = (List<SysDataMonster>)objs[1];
                if (mapMonster == null || mapMonster.size() <= 0) {
                    return;
                }
                final Team team = fightNewService.getRoleFightTeam(role);
                fightNewService.enterFieldFight(team, role);
                monsterService.flushMonsterTime(role.getRoleId());
            }
        }
        catch (final Exception e) {
            System.out.println("e = " + e);
        }
    }
}
