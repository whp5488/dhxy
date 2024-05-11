
package com.kitty.system.game.fightnew.utils;

import java.util.List;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;

public class AcsrySkillInsideUtil
{
    public static void excuteClearStatus(final Fight fight, final FightObject effecter, final FightObject target, final int status) {
        target.removeStatus(status);
        final List<Integer> statusList = new ArrayList<Integer>();
        statusList.add(status);
        ActionParamUtils.clearStatus(fight, effecter, target, 43, 300999, 2, statusList);
    }
}
