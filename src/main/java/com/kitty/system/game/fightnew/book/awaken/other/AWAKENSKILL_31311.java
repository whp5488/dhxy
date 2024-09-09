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
    public AWAKENSKILL_31311() {
    }
    
    @Override
    public List<FiedValue> useDrug(Fight fight, FightObject effector, FightObject target, Object... args) {
        if (args != null && args.length > 0 && !effector.getId().equals(target.getId())) {
            int showClass = (int)args[0];
            if (effector.getSkillCoolMap().getInt(String.valueOf(31311)) <= 0 && (1 == showClass || 2 == showClass || 3 == showClass)) {
                NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 20.0, 20.0);
                if (map == null) {
                    return null;
                }
                double percent = map.getDouble("percent");
                if (percent > 0.0 && SkillAi.canAffectOnPro(percent)) {
                    List<FiedValue> list = new ArrayList<>();
                    list.add(new FiedValue("addTarget", (byte)6, Boolean.valueOf(true)));
                    list.add(new FiedValue("addTargetId", (byte)3, effector.getId()));
                    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 31311);
                    skillRecord.setT((long)(int)effector.getId());
                    return list;
                }
            }
        }
        return null;
    }
}
