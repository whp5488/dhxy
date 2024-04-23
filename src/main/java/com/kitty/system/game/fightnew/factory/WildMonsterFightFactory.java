package com.kitty.system.game.fightnew.factory;

import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.WildMonsterFight;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.team.domain.Team;

public class WildMonsterFightFactory extends FightFactory {
  public Fight create(int mapId, Object... params) {
    Team team = (Team)params[0];
    Role role = (Role)params[1];
    return (Fight)new WildMonsterFight(team, role);
  }
}
