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
    
    public SKILL_80020() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        return null;
    }
    
    @Override
    public int targetCount(Fight fight, FightObject effector, Object... args) {
        return 1;
    }
    
    @Override
    public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        return null;
    }
    
    @Override
    public void normalAttack(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
        if (actionDetailParam == null) {
            return;
        }
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 80020);
        double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1 + 3.0 * ((double)effector.getRelive() * 0.4 + 1.0) * PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0));
        boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag) {
            int oldHurt = (int)args[0];
            RoleSkill skill = effector.getSkillBy(Integer.valueOf(80020));
            SysPetSkill sysSkill = (SysPetSkill)SkillDataPool.petSkillMap.get(Integer.valueOf(80020));
            int proficient = 1;
            if (skill.getProficient() != null) {
                proficient = (int)skill.getProficient();
            }
            double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
            double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_SS.getDist());
            double hkx = effector.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_SS.getDist());
            double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_SS.getDist());
            double hurt = sysSkill.getPower()[0] * (double)effector.getLevel() * (Math.pow((double)proficient, 0.4) * 2.8853998118144273 / 100.0 + 1.0);
            double wx = SkillFunc.getWuXingKeZhiXiuZheng(effector, target, fight.getType());
            int harm = (int)SkillCompute.computePetSkillDamage(hurt, kx, hkx, extra, weak, wx);
            int reduceLife;
            oldHurt = (reduceLife = oldHurt + harm);
            if (reduceLife > target.getCurrLife()) {
                reduceLife = target.getCurrLife();
            }
            FightObjectParam targetReduce = ActionParamUtils.changeLifeForTarget(fight, target, 1, -reduceLife, 1);
            targetReduce.addDamageType(115);
            actionDetailParam.getTargetParamList().add(targetReduce);
            FightObject unit = SkillAi.effectProBass(fight, effector, Integer.valueOf(1), SKILL_80020.terms);
            int life = Math.min(unit.getCurrLife() + BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((double)oldHurt * 0.03)), unit.getMaxLife());
            int addLife = life - unit.getCurrLife();
            if (addLife > 0) {
                FightObjectParam targetAdd = ActionParamUtils.changeLifeForTarget(fight, unit, 1, addLife, 1);
                actionDetailParam.getTargetParamList().add(targetAdd);
            }
            skillRecord.setT((long)(int)unit.getId());
        }
        return;
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        return 0;
    }
    
    static {
        terms = Arrays.asList(new String[0]);
    }
}
