
package com.kitty.system.game.fightnew.book.awaken.intensify;

import java.util.Iterator;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import java.util.List;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31426 extends AwakenSkillProcessor
{
    @Override
    public void skillIntensifyTM(final Fight fight, final PetFightObject petFightObject, final Object... args) {
        if (args != null && args.length > 0) {
            final int skillId = (int)args[0];
            final List<Integer> list = (List<Integer>)args[1];
            if (80003 == skillId || 80005 == skillId || 81010 == skillId || 81011 == skillId) {
                NutMap map = AwakenSkillEffectUtils.getPercentDouble(petFightObject, 40.0, 20.0);
                if (map == null) {
                    return;
                }
                int num = 0;
                final double percenta = map.getDouble("percent");
                if (percenta > 0.0 && SkillAi.canAffectOnPro(percenta)) {
                    num = 1;
                }
                map = AwakenSkillEffectUtils.getPercentDouble(petFightObject, 20.0, 20.0);
                if (map == null) {
                    return;
                }
                final double percentb = map.getDouble("percent");
                if (percentb > 0.0 && SkillAi.canAffectOnPro(percentb)) {
                    num = 2;
                }
                if (num > 0) {
                    final List<FightObject> targets = PassiveSkillUtils.getTargetForCondition(fight, petFightObject, list, 1, true, num);
                    if (targets != null && targets.size() > 0) {
                        for (final FightObject obj : targets) {
                            list.add(obj.getId());
                        }
                    }
                }
            }
        }
    }
}
