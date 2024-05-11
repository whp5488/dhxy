
package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import java.util.Map;
import com.kitty.system.game.fightnew.bean.BeforeActionSkill;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_81019 extends SkillProcessor
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
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 81019);
        final SysPetSkill petSkill = (SysPetSkill)args[1];
        final BeforeActionSkill beforeActionSkill = fight.getBeforeActionMap().get(target.isASide());
        final Map<Integer, SysPetSkill> resultMap = PetTriggerPassiveSkill.data_pet_skill_maps.get(petSkill.getTriggerType());
        final SysPetSkill coverPetSkill = PetSkillEffectorUtil.initiativeQueryCover(petSkill, (List<RoleSkill>)effector.getSkillIds().stream().filter(roleSkill -> roleSkill.getSkillId() > 1).collect(Collectors.toList()), resultMap);
        if (coverPetSkill != null && !coverPetSkill.getSerial().equals(petSkill.getSerial()) && beforeActionSkill.getSkills().contains(coverPetSkill.getSerial())) {
            return null;
        }
        if (effector.getSkillCoolMap().getInt(String.valueOf(81019)) > 0) {
            return null;
        }
        effector.getSkillCoolMap().setv(String.valueOf(81019), (Object)fight.getMaxRound());
        if (!beforeActionSkill.getSkills().contains(81019)) {
            return null;
        }
        final Integer skillid = beforeActionSkill.getSkills().stream().filter(skill -> 81019 == skill).findAny().get();
        if (skillid != null && skillid > 0) {
            final int hurt = (int)args[0];
            final int oldCurrLife = target.getCurrLife();
            final int surplusLife = oldCurrLife - hurt;
            int changeLife = -hurt;
            boolean isDie = false;
            if (surplusLife <= 0) {
                fight.onActorDie(target);
                changeLife = -oldCurrLife;
                isDie = true;
            }
            ActionParamUtils.changeLife(fight, effector, target, 7, 81019, 2, changeLife, false);
            PetSkillEffectorUtil.enemyFirstMagicCancelAction(fight, effector, target, beforeActionSkill, skillid);
            skillRecord.setT(target.getId());
            if (isDie) {
                final Effect e = new Effect();
                e.setType(99);
                return e;
            }
        }
        return null;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return 0;
    }
}
