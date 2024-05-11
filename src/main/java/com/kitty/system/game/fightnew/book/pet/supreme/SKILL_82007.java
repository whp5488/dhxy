
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
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        FightParamPool.addSkillRecord(fight, 7, effector.getId(), 82007);
        final boolean isASide = effector.isASide();
        final BeforeActionSkill beforeActionSkill = fight.getBeforeActionMap().get(!isASide);
        if (beforeActionSkill == null || !beforeActionSkill.isPassiveAction()) {
            return null;
        }
        if (beforeActionSkill.getSkills() != null && !beforeActionSkill.getSkills().contains(82007)) {
            return null;
        }
        AstrictSkill astrictSkill = fight.getAstrictSkillMap().get(!isASide);
        boolean isLayIn = true;
        if (astrictSkill != null) {
            isLayIn = false;
        }
        if (isLayIn) {
            astrictSkill = new AstrictSkill();
            astrictSkill.setASide(!isASide);
            astrictSkill.setRound(1);
            fight.getAstrictSkillMap().put(!isASide, astrictSkill);
        }
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
    public int costMana(final RoleSkill skill, final Object... args) {
        return 0;
    }
}
