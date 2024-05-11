
package com.kitty.system.game.fightnew.book.awaken.intensify;

import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31427 extends AwakenSkillProcessor
{
    @Override
    public NutMap skillIntensify(final Fight fight, final PetFightObject petFightObject, final Object... args) {
        final NutMap map = AwakenSkillEffectUtils.getPercentDouble(petFightObject, 50.0, 20.0);
        if (map == null) {
            return null;
        }
        final double percent = map.getDouble("percent");
        if (percent != 0.0) {
            ImbueUtil.addRound(petFightObject.getImbueMap(), PRO_DICT_TYPE.RAGE_RATIO.getDist(), 1, percent);
        }
        return null;
    }
}
