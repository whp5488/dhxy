
package com.kitty.system.game.fightnew.book.awaken.intensify;

import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31417 extends AwakenSkillProcessor
{
    @Override
    public NutMap skillIntensify(final Fight fight, final PetFightObject petFightObject, final Object... args) {
        return AwakenSkillEffectUtils.getPercentDouble(petFightObject, 20.0, 20.0);
    }
}
