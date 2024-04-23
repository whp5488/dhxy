package com.kitty.system.game.fightnew.book.drug.utils;

import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import java.util.ArrayList;
import java.util.List;

public class DrugUtil {
  public static void clearCloaking(Fight fight, FightObject effector, FightObject target, int drugId) {
    target.removeStatus(1011);
    List<Integer> clearList = new ArrayList<>();
    clearList.add(Integer.valueOf(1011));
    ActionParamUtils.clearStatus(fight, effector, target, 5, drugId, 2, clearList);
    SkillProcessor skillProcessor = SkillProcessor.queryProcessor(Integer.valueOf(81031));
    if (skillProcessor != null)
      skillProcessor.statusClear(fight, target, 1011, new Object[0]); 
  }
}
