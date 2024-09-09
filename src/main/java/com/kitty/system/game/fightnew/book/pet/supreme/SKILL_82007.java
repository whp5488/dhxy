package com.kitty.system.game.fightnew.book.pet.supreme;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.bean.AstrictSkill;
import com.kitty.system.game.fightnew.bean.BeforeActionSkill;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_82007 extends SkillProcessor
{
    public SKILL_82007() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 82007);
        boolean isASide = effector.isASide();
        BeforeActionSkill beforeActionSkill = (BeforeActionSkill)fight.getBeforeActionMap().get(Boolean.valueOf(!isASide));
        if (beforeActionSkill == null || !beforeActionSkill.isPassiveAction()) {
            return null;
        }
        if (beforeActionSkill.getSkills() != null && !beforeActionSkill.getSkills().contains(Integer.valueOf(82007))) {
            return null;
        }
        AstrictSkill astrictSkill = (AstrictSkill)fight.getAstrictSkillMap().get(Boolean.valueOf(!isASide));
        boolean isLayIn = true;
        if (astrictSkill != null) {
            isLayIn = false;
        }
        if (isLayIn) {
            astrictSkill = new AstrictSkill();
            astrictSkill.setASide(!isASide);
            astrictSkill.setRound(1);
            fight.getAstrictSkillMap().put(Boolean.valueOf(!isASide), astrictSkill);
        }
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
    public int costMana(RoleSkill skill, Object... args) {
        return 0;
    }
}
