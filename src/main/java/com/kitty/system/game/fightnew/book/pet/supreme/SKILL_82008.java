
package com.kitty.system.game.fightnew.book.pet.supreme;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.bean.BeforeActionSkill;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_82008 extends SkillProcessor
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
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 82008);
        final Effect effect = new Effect(99, 0);
        if (!effector.getId().equals(target.getId()) && effector.getSkillCoolMap().getInt(String.valueOf(82008)) > 0) {
            return null;
        }
        final BeforeActionSkill beforeActionSkill = fight.getBeforeActionMap().get(target.isASide());
        if (!beforeActionSkill.getSkills().contains(82008)) {
            return null;
        }
        final Integer skillid = beforeActionSkill.getSkills().stream().filter(skill -> 82008 == skill).findAny().get();
        if (skillid != null && skillid > 0) {
            effector.getSkillCoolMap().setv(String.valueOf(82008), (Object)fight.getMaxRound());
            ActionParamUtils.actionForTargetNoth(fight, effector, target, 7, 82008, 2);
            PetSkillEffectorUtil.enemyFirstMagicCancelAction(fight, effector, target, beforeActionSkill, skillid);
            skillRecord.setT(target.getId());
            return effect;
        }
        return null;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return 0;
    }
}
