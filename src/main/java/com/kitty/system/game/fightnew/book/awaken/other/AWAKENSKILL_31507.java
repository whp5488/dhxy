
package com.kitty.system.game.fightnew.book.awaken.other;

import java.util.Iterator;
import java.util.List;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31507 extends AwakenSkillProcessor
{
    @Override
    public void enterFight(final Fight fight, final FightObject fightObject, final Object... args) {
        final NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)fightObject, 40.0, 20.0);
        if (map == null) {
            return;
        }
        final double percent = map.getDouble("percent");
        if (percent > 0.0) {
            final FightObject host = fight.queryFightObject(fightObject.getHostId());
            if (host != null) {
                final int dict = this.getDict(host);
                ImbueUtil.addOrUpdSkill(host.getImbueMap(), dict, WarMaxRound.WAR_MAX_ROUND.getValue(), 31507, percent);
            }
        }
    }
    
    private int getDict(final FightObject fightObject) {
        double flag = 0.0;
        int returnDict = 0;
        final List<Integer> qWuXinDist = PRO_DICT_TYPE.getQWuXinDist();
        for (final int dict : qWuXinDist) {
            if (flag < fightObject.getSelfAllTempPro(dict)) {
                flag = fightObject.getSelfAllTempPro(dict);
                returnDict = dict;
            }
        }
        if (returnDict == 0) {
            final int round = RandomUtil.round(0, qWuXinDist.size() - 1);
            returnDict = qWuXinDist.get(round);
        }
        return returnDict;
    }
    
    @Override
    public void exitSide(final Fight fight, final FightObject fightObject, final Object... args) {
        if (fightObject.isFallDown()) {
            final FightObject host = fight.queryFightObject(fightObject.getHostId());
            if (host != null) {
                final int dict = this.getDict(host);
                ImbueUtil.removeSkill(host.getImbueMap(), dict, 31507);
            }
        }
    }
}
