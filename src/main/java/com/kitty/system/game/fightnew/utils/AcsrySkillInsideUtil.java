package com.kitty.system.game.fightnew.utils;

import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import java.util.ArrayList;
import java.util.List;

public class AcsrySkillInsideUtil {
  public static void excuteClearStatus(Fight fight, FightObject effecter, FightObject target, int status) {
    target.removeStatus(status);
    List<Integer> statusList = new ArrayList<>();
    statusList.add(Integer.valueOf(status));
    ActionParamUtils.clearStatus(fight, effecter, target, 43, 300999, 2, statusList);
  }
}
