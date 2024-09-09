package com.kitty.system.game.fightnew.book.awaken.intensify;

import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31417 extends AwakenSkillProcessor
{
    public AWAKENSKILL_31417() {
    }
    
    @Override
    public NutMap skillIntensify(Fight fight, PetFightObject petFightObject, Object... args) {
        return AwakenSkillEffectUtils.getPercentDouble(petFightObject, 20.0, 20.0);
    }
}
