package com.kitty.system.game.fightnew.factory;

import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.fightnew.bean.TeamPkFight;
import java.util.List;
import com.kitty.system.game.fightnew.bean.Fight;

public class TeamPkFightFactory extends FightFactory
{
    public TeamPkFightFactory() {
    }
    
    @Override
    public Fight create(int mapId, Object... params) {
        int type = (int)params[0];
        List<Member> listA = (List)params[1];
        List<Member> listB = (List)params[2];
        return new TeamPkFight(type, listA, listB);
    }
}
