package com.kitty.system.game.fightnew.factory;

import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.TeamPkFight;
import com.kitty.system.game.team.domain.Member;
import java.util.List;

public class TeamPkFightFactory extends FightFactory {
  public Fight create(int mapId, Object... params) {
    int type = ((Integer)params[0]).intValue();
    List<Member> listA = (List<Member>)params[1];
    List<Member> listB = (List<Member>)params[2];
    return (Fight)new TeamPkFight(type, listA, listB);
  }
}
