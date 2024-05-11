
package com.kitty.system.game.fightnew.book.pet.normal;

import java.util.Arrays;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillCompute;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import java.util.List;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_80020 extends SkillProcessor
{
    static final List<String> terms;
    
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
        return null;
    }
    
    @Override
    public void normalAttack(final Fight fight, final FightObject effector, final FightObject target, final ActionDetailParam actionDetailParam, final Object... args) {
        if (actionDetailParam == null) {
            return;
        }
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 80020);
        final double percent = BigDecimalUtil.decimalScaleObjectDown(3, (0.1 + 3.0 * (effector.getRelive() * 0.4 + 1.0) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose())) / 100.0);
        final boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag) {
            int oldHurt = (int)args[0];
            final RoleSkill skill = effector.getSkillBy(80020);
            final SysPetSkill sysSkill = SkillDataPool.petSkillMap.get(80020);
            int proficient = 1;
            if (skill.getProficient() != null) {
                proficient = skill.getProficient();
            }
            final double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
            final double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_SS.getDist());
            final double hkx = effector.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_SS.getDist());
            final double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_SS.getDist());
            final double hurt = sysSkill.getPower()[0] * effector.getLevel() * (Math.pow(proficient, 0.4) * 2.8853998118144273 / 100.0 + 1.0);
            final double wx = SkillFunc.getWuXingKeZhiXiuZheng(effector, target, fight.getType());
            final int harm = SkillCompute.computePetSkillDamage(hurt, kx, hkx, extra, weak, wx);
            int reduceLife;
            oldHurt = (reduceLife = oldHurt + harm);
            if (reduceLife > target.getCurrLife()) {
                reduceLife = target.getCurrLife();
            }
            final FightObjectParam targetReduce = ActionParamUtils.changeLifeForTarget(fight, target, 1, -reduceLife, 1);
            targetReduce.addDamageType(115);
            actionDetailParam.getTargetParamList().add(targetReduce);
            final FightObject unit = SkillAi.effectProBass(fight, effector, 1, SKILL_80020.terms);
            final int life = Math.min(unit.getCurrLife() + BigDecimalUtil.decimalScaleIntegerDown(0, oldHurt * 0.03), unit.getMaxLife());
            final int addLife = life - unit.getCurrLife();
            if (addLife > 0) {
                final FightObjectParam targetAdd = ActionParamUtils.changeLifeForTarget(fight, unit, 1, addLife, 1);
                actionDetailParam.getTargetParamList().add(targetAdd);
            }
            skillRecord.setT(unit.getId());
        }
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return 0;
    }
    
    static {
        terms = Arrays.asList(new String[0]);
    }
}
