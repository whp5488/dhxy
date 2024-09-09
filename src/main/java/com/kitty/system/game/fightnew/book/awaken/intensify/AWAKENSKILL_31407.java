package com.kitty.system.game.fightnew.book.awaken.intensify;

import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import java.util.List;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31407 extends AwakenSkillProcessor
{
    public AWAKENSKILL_31407() {
    }
    
    @Override
    public void skillIntensifyTM(Fight fight, PetFightObject petFightObject, Object... args) {
        if (args != null && args.length > 0) {
            int skillId = (int)args[0];
            List<Integer> list = (List)args[1];
            if (80007 == skillId || 80009 == skillId || 81012 == skillId || 81013 == skillId) {
                NutMap map = AwakenSkillEffectUtils.getPercentDouble(petFightObject, 50.0, 20.0);
                if (map == null) {
                    return;
                }
                double percent = map.getDouble("percent");
                if (percent > 0.0 && SkillAi.canAffectOnPro(percent)) {
                    List<FightObject> targets = PassiveSkillUtils.getTargetForCondition(fight, petFightObject, list, 1, true, 1);
                    if (targets != null && targets.size() > 0) {
                        list.add(((FightObject)targets.get(0)).getId());
                    }
                }
            }
        }
    }
}
