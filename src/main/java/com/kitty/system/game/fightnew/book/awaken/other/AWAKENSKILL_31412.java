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
    public AWAKENSKILL_31412() {
    }
    
    @Override
    public void sufferHarm(Fight fight, FightObject fightObject, Object... args) {
        if (args == null || args.length <= 0 || fightObject.getSkillCoolMap().getInt(String.valueOf(31412)) > 0) {
            return;
        }
        int hurt = (int)args[0];
        NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)fightObject, 50.0, 20.0);
        if (map == null) {
            return;
        }
        double percent = map.getDouble("percent");
        if (percent > 0.0 && SkillAi.canAffectOnPro(percent)) {
            int life = (int)((double)hurt * 0.3);
            if (life > 0) {
                life = ((life > 200000) ? 200000 : life);
                FightObject unit = SkillAi.effectProBass(fight, fightObject, Integer.valueOf(1), Arrays.asList(PET_SKILL_TERM_DICT.PRO_BASS_DOWN_N.getValue()));
                if (unit != null) {
                    ActionParamUtils.changeLife(fight, fightObject, unit, 7, 31412, 2, life, false);
                    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)fightObject.getId(), 31412);
                    skillRecord.setT((long)(int)unit.getId());
                    fightObject.getSkillCoolMap().setv(String.valueOf(31412), Integer.valueOf(1));
                }
            }
        }
        return;
    }
}
