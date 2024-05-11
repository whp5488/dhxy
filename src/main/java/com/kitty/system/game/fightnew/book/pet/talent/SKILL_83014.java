
package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83014 extends SkillProcessor
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
        return 1;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 83014);
        final Effect effect = new Effect();
        if (!target.isFrozen()) {
            final int time = 3;
            final double percent = -BigDecimalUtil.decimalScaleObjectDown(3, (0.1 + 3.0 * (effector.getRelive() * 0.6 + 1.0) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose())) / 100.0);
            final double reduce = -BigDecimalUtil.decimalScaleObjectDown(3, (0.1 + 3.5 * (effector.getRelive() * 0.4 + 1.0) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose())) / 100.0);
            final double down = -BigDecimalUtil.decimalScaleObjectDown(3, (0.1 + 6.0 * (effector.getRelive() * 0.6 + 1.0) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose())) / 100.0);
            ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1015, PRO_DICT_TYPE.K_ZS.getDist(), time, percent);
            ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1015, PRO_DICT_TYPE.K_YW.getDist(), time, reduce);
            ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1015, PRO_DICT_TYPE.K_GH.getDist(), time, down);
            ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1015, PRO_DICT_TYPE.K_SS.getDist(), time, down);
            AwakenSkillUtils.skillIntensifyDouble(fight, effector, 31403, target);
            effect.setType(1015);
            effect.setTime(time);
        }
        else {
            effect.setType(1015);
            effect.setTime(0);
        }
        skillRecord.setT(target.getId());
        return effect;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        final int level = PetSkillEffectorUtil.getExcuteLevel(args);
        return level * 21;
    }
}
