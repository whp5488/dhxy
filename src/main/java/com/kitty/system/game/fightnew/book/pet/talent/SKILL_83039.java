package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83039 extends SkillProcessor
{
    public SKILL_83039() {
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
    public void normalAttack(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
        if (target.isFallDown() || !effector.isPet()) {
            return;
        }
        FightObjectParam targetParam = actionDetailParam.gainTargetParam((int)target.getId(), false);
        if (targetParam == null || !targetParam.isHit()) {
            return;
        }
        int skillCool = effector.getSkillCoolMap().getInt(String.valueOf(83039));
        if (skillCool > 0) {
            return;
        }
        boolean flag = SkillAi.canAffectOnPro(this.getPercent(effector));
        if (flag) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 83039);
            effector.getSkillCoolMap().setv(String.valueOf(83039), Integer.valueOf(0));
            effector.setType(0);
            effector.setSkillId(0);
            RoleSkill skill = new RoleSkill();
            skill.setType(Integer.valueOf(0));
            skill.setSkillId(Integer.valueOf(83039));
            AwakenSkillUtils.skillIntensifyDouble(fight, effector, 31427, target);
            BattleUnit battleUnit = new BattleUnit(fight, effector, target, skill);
            battleUnit.countDoubleHit();
            PetPassiveSkillUtil.passivePhysics(fight, effector, target, battleUnit, actionDetailParam, 83039);
            skillRecord.setT((long)(int)target.getId());
        }
        return;
    }
    
    private double getPercent(FightObject effector) {
        double param1 = 4.0 * ((double)effector.getRelive() * 0.8 + 1.0);
        double param2 = Math.pow((double)effector.getLevel(), 0.5) / 10.0 + Math.pow((double)effector.getClose(), 0.16666666666666666) * 10.0 / (double)(100 + effector.getRelive() * 20);
        return Math.min(50.0, 5.0 + param1 * param2) / 100.0;
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
    public int costMana(RoleSkill skill, Object... args) {
        return 0;
    }
}
