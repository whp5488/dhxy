
package com.kitty.system.game.fightnew.factory;

import com.kitty.system.game.fightnew.bean.WildMonsterFight;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.fightnew.bean.Fight;

public class WildMonsterFightFactory extends FightFactory
{
    @Override
    public Fight create(final int mapId, final Object... params) {
        final Team team = (Team)params[0];
        final Role role = (Role)params[1];
        return new WildMonsterFight(team, role);
    }
}
