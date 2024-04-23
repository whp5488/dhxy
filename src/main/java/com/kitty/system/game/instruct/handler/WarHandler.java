package com.kitty.system.game.instruct.handler;

import com.kitty.common.start.ServerService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.instruct.inter.NcFather;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.service.MonsterService;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.init.domain.SysDataMonster;
import java.util.List;

public class WarHandler implements NcFather {
  public void handle(Role role, String content) {
    try {
      if (((ServerService)SpringUtils.getBean(ServerService.class)).getType() == 1)
        return; 
      FightNewService fightNewService = (FightNewService)SpringUtils.getBean(FightNewService.class);
      boolean inFight = fightNewService.isInFight(role);
      if (!inFight) {
        MonsterService monsterService = (MonsterService)SpringUtils.getBean(MonsterService.class);
        Object[] objs = monsterService.getMapMonster(role, Integer.valueOf(content).intValue());
        List<SysDataMonster> mapMonster = (List<SysDataMonster>)objs[1];
        if (mapMonster == null || mapMonster.size() <= 0)
          return; 
        Team team = fightNewService.getRoleFightTeam(role);
        fightNewService.enterFieldFight(team, role);
        monsterService.flushMonsterTime(role.getRoleId().intValue());
      } 
    } catch (Exception e) {
      System.out.println("e = " + e);
    } 
  }
}
