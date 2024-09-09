package com.kitty.system.game.fightnew.book.awaken.other;

import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31402 extends AwakenSkillProcessor
{
    public AWAKENSKILL_31402() {
    }
    
    @Override
    public double isGuard(Fight fight, FightObject effector, Object... args) {
        if (effector.getType() == 8 && effector.getTargetId() != effector.getHostId()) {
            NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 100.0, 20.0);
            if (map != null) {
                double percent = map.getDouble("percent");
                if (percent > 0.0 && SkillAi.canAffectOnPro(percent)) {
                    return 1.0;
                }
            }
        }
        return 0.0;
    }
}
