
package com.kitty.system.game.fightnew.book.awaken.other;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31311 extends AwakenSkillProcessor
{
    @Override
    public List<FiedValue> useDrug(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        if (args != null && args.length > 0 && !effector.getId().equals(target.getId())) {
            final int showClass = (int)args[0];
            if (effector.getSkillCoolMap().getInt(String.valueOf(31311)) <= 0 && (1 == showClass || 2 == showClass || 3 == showClass)) {
                final NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 20.0, 20.0);
                if (map == null) {
                    return null;
                }
                final double percent = map.getDouble("percent");
                if (percent > 0.0 && SkillAi.canAffectOnPro(percent)) {
                    final List<FiedValue> list = new ArrayList<FiedValue>();
                    list.add(new FiedValue("addTarget", (byte)6, true));
                    list.add(new FiedValue("addTargetId", (byte)3, effector.getId()));
                    final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 31311);
                    skillRecord.setT(effector.getId());
                    return list;
                }
            }
        }
        return null;
    }
}
