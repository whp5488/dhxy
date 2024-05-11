
package com.kitty.system.game.fightnew.factory;

import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.fightnew.bean.TeamPkFight;
import java.util.List;
import com.kitty.system.game.fightnew.bean.Fight;

public class TeamPkFightFactory extends FightFactory
{
    @Override
    public Fight create(final int mapId, final Object... params) {
        final int type = (int)params[0];
        final List<Member> listA = (List<Member>)params[1];
        final List<Member> listB = (List<Member>)params[2];
        return new TeamPkFight(type, listA, listB);
    }
}
