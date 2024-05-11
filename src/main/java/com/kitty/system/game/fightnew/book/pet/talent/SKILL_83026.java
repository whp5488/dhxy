
package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83026 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        return null;
    }
    
    @Override
    public int targetCount(final Fight fight, final FightObject effector, final Object... args) {
        int count = 1;
        if (AwakenSkillUtils.skillIntensifyBoolean(fight, effector, 31506)) {
            ++count;
        }
        return count;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final RoleSkill skill = effector.getSkillBy(83026);
        final Effect effect = new Effect();
        if (skill != null) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 83026);
            if (!target.isFrozen()) {
                final int time = 2;
                final double persent = BigDecimalUtil.decimalScaleObjectDown(3, (0.1 + 16.0 * (effector.getRelive() * 0.5 + 1.0) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose())) / 100.0);
                final int dist = PRO_DICT_TYPE.BURN_HURT.getDist();
                ImbueUtil.addOrUpdStatus(target.getImbueMap(), 2070, dist, time, persent);
                effect.setType(2070);
                effect.setTime(time);
            }
            else {
                effect.setType(2070);
                effect.setTime(0);
            }
            skillRecord.setT(target.getId());
        }
        return effect;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return 4300;
    }
    
    @Override
    public boolean excuteSkillCool(final Fight fight, final FightObject effector, final Object... args) {
        effector.getSkillCoolMap().setv(String.valueOf(83026), (Object)5);
        return true;
    }
}
