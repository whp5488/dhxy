package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_81032 extends SkillProcessor
{
    public SKILL_81032() {
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
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 81032);
        SysPetSkill petSkill = (SysPetSkill)SkillDataPool.petSkillMap.get(Integer.valueOf(81032));
        if (PetSkillEffectorUtil.checkCover(petSkill, fight, effector, target, false, args)) {
            return;
        }
        if (args.length < 2) {
            return;
        }
        Integer skillSerial = (Integer)args[1];
        if (skillSerial != null && PetSkillEffectorUtil.isClash(petSkill, skillSerial)) {
            return;
        }
        double percent = 0.35;
        double pval = AcsryPetSkillUtil.getPval(80011, effector);
        if (pval > 0.0) {
            percent += pval;
        }
        boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag) {
            Object oldHurt = args[0];
            Integer hurt = Integer.valueOf(Integer.parseInt(oldHurt.toString()));
            Effect effect = new Effect();
            effect.setHurt(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((double)(int)hurt * 0.5)));
            FightObject newTarget = fight.getTargetNoCurrentTarget((int)effector.getId(), (int)target.getId());
            if (newTarget == null) {
                return;
            }
            RoleSkill skillBy = effector.getSkillBy(Integer.valueOf(81032));
            BattleUnit battleUnit = new BattleUnit(fight, effector, newTarget, skillBy);
            battleUnit.setEffect(effect);
            PetPassiveSkillUtil.passivePhysics(fight, effector, newTarget, battleUnit, actionDetailParam, 81032);
            skillRecord.setT((long)(int)newTarget.getId());
        }
        return;
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        return 0;
    }
}
