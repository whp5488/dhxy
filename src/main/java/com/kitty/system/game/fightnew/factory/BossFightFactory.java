package com.kitty.system.game.fightnew.factory;

import com.kitty.system.boss.model.BossFightParam;
import com.kitty.system.game.fightnew.bean.BossFight;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.service.FightExecutorNewService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.team.domain.Team;

public class BossFightFactory extends FightFactory {
  public BossFight create(int mapId, Object... params) {
    Role role = (Role)params[0];
    Team team = (Team)params[1];
    BossFightParam bossFightParam = (BossFightParam)params[2];
    BossFight battleground = new BossFight(bossFightParam.getFightType(), mapId, role, team, bossFightParam);
    battleground.setMapId(mapId);
    battleground.setNpcId(bossFightParam.getNpcId());
    battleground.setTaskId(bossFightParam.getTaskId());
    battleground.setLeaderId(team.getLeaderRoleId());
    FightExecutorNewService.FIGHTS.put(Integer.valueOf(battleground.getId()), battleground);
    return battleground;
  }
}
