
package com.kitty.system.game.fightnew.book.drug.utils;

import java.util.List;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;

public class DrugUtil
{
    public static void clearCloaking(final Fight fight, final FightObject effector, final FightObject target, final int drugId) {
        target.removeStatus(1011);
        final List<Integer> clearList = new ArrayList<Integer>();
        clearList.add(1011);
        ActionParamUtils.clearStatus(fight, effector, target, 5, drugId, 2, clearList);
        final SkillProcessor skillProcessor = SkillProcessor.queryProcessor(81031);
        if (skillProcessor != null) {
            skillProcessor.statusClear(fight, target, 1011, new Object[0]);
        }
    }
}
