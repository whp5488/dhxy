
package com.kitty.system.game.fightnew.book.awaken.other;

import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31414 extends AwakenSkillProcessor
{
    @Override
    public void enterFight(final Fight fight, final FightObject fightObject, final Object... args) {
        final NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)fightObject, 25.0, 20.0);
        if (map == null) {
            return;
        }
        final double percent = map.getDouble("percent");
        ImbueUtil.addRound(fightObject.getImbueMap(), PRO_DICT_TYPE.CONQUER_RATIO.getDist(), 1, percent + 0.1);
        ImbueUtil.addRound(fightObject.getImbueMap(), PRO_DICT_TYPE.CONQUER_RATIO.getDist(), 2, -0.1);
    }
}
