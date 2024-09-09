package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_81056 extends SkillProcessor
{
    public SKILL_81056() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 81056);
        FightObject target = fight.getTargetNoCurrentTarget((int)effector.getId(), 0);
        if (target == null) {
            return null;
        }
        effector.setType(0);
        effector.setSkillId(0);
        RoleSkill skill = new RoleSkill();
        skill.setType(Integer.valueOf(0));
        skill.setSkillId(Integer.valueOf(81056));
        BattleUnit battleUnit = new BattleUnit(fight, effector, target, skill);
        battleUnit.countDoubleHit();
        ActionDetailParam actionDetailParam = PetPassiveSkillUtil.passivePhysics(fight, effector, target, battleUnit, null, 81056);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        skillRecord.setT((long)(int)target.getId());
        return null;
    }
    
    @Override
    public int targetCount(Fight fight, FightObject effector, Object... args) {
        return 0;
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
