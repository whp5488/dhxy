
package com.kitty.system.game.fightnew.book.awaken.other;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import java.util.Arrays;
import com.kitty.system.enums.war.itempro.def.PET_SKILL_TERM_DICT;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31412 extends AwakenSkillProcessor
{
    @Override
    public void sufferHarm(final Fight fight, final FightObject fightObject, final Object... args) {
        if (args == null || args.length <= 0 || fightObject.getSkillCoolMap().getInt(String.valueOf(31412)) > 0) {
            return;
        }
        final int hurt = (int)args[0];
        final NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)fightObject, 50.0, 20.0);
        if (map == null) {
            return;
        }
        final double percent = map.getDouble("percent");
        if (percent > 0.0 && SkillAi.canAffectOnPro(percent)) {
            int life = (int)(hurt * 0.3);
            if (life > 0) {
                life = ((life > 200000) ? 200000 : life);
                final FightObject unit = SkillAi.effectProBass(fight, fightObject, 1, Arrays.asList(PET_SKILL_TERM_DICT.PRO_BASS_DOWN_N.getValue()));
                if (unit != null) {
                    ActionParamUtils.changeLife(fight, fightObject, unit, 7, 31412, 2, life, false);
                    final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId(), 31412);
                    skillRecord.setT(unit.getId());
                    fightObject.getSkillCoolMap().setv(String.valueOf(31412), (Object)1);
                }
            }
        }
    }
}
