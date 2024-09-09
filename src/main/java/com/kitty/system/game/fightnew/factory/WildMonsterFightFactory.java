package com.kitty.system.game.fightnew.factory;

import com.kitty.system.game.fightnew.bean.WildMonsterFight;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.fightnew.bean.Fight;

public class WildMonsterFightFactory extends FightFactory
{
    public WildMonsterFightFactory() {
    }
    
    @Override
    public Fight create(int mapId, Object... params) {
        Team team = (Team)params[0];
        Role role = (Role)params[1];
        return new WildMonsterFight(team, role);
    }
}
