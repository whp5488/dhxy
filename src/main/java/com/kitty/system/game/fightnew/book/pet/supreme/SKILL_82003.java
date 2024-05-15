
package com.kitty.system.game.fightnew.book.pet.supreme;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import java.util.Iterator;
import com.kitty.system.game.fightnew.bean.AstrictSkill;
import java.util.Collection;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysPetSkill;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.bean.BeforeActionSkill;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_82003 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        FightParamPool.addSkillRecord(fight, 7, effector.getId(), 82003);
        final int round = 1;
        final List<FightObject> targets = fight.getTargetSide(effector.getId());
        final FightObject target = PetSkillEffectorUtil.getTargetForPos(targets, 3);
        final BeforeActionSkill beforeActionSkill = fight.getBeforeActionMap().get(effector.isASide());
        BeforeActionSkill beforeActionSkillCounter = null;
        if (beforeActionSkill != null && beforeActionSkill.isPassiveAction()) {
            ActionParamUtils.actionForTargetNoth(fight, effector, target, 7, 82003, 2);
            beforeActionSkillCounter = fight.getBeforeActionMap().get(!effector.isASide());
            final FightObject targetForPos = PetSkillEffectorUtil.getTargetForPos(targets, 3);
            if (beforeActionSkillCounter == null || !beforeActionSkillCounter.isPassiveAction()) {
                beforeActionSkillCounter = new BeforeActionSkill();
                beforeActionSkillCounter.setPassiveAction(true);
                beforeActionSkillCounter.setASide(!effector.isASide());
                if (beforeActionSkill.getRoleSkillMap() != null && beforeActionSkill.getRoleSkillMap().size() > 0) {
                    final Map<Integer, List<Integer>> roleSkillM = new HashMap<Integer, List<Integer>>();
                    for (final Map.Entry<Integer, List<Integer>> entry : beforeActionSkill.getRoleSkillMap().entrySet()) {
                        final List<Integer> list = new ArrayList<Integer>();
                        for (final Integer skillId : entry.getValue()) {
                            list.add(skillId);
                            if (beforeActionSkillCounter.getSkills() == null || !beforeActionSkillCounter.getSkills().contains(skillId)) {
                                PetSkillEffectorUtil.sendActionExcute(fight, entry.getKey(), skillId, targetForPos.getId());
                            }
                            if (beforeActionSkillCounter.getSkills() == null) {
                                beforeActionSkillCounter.setSkills(new ArrayList<Integer>());
                            }
                            beforeActionSkillCounter.getSkills().add(skillId);
                        }
                        roleSkillM.put(entry.getKey(), list);
                    }
                    beforeActionSkillCounter.setRoleSkillMap(roleSkillM);
                }
            }
            else {
                for (final Map.Entry<Integer, List<Integer>> entry2 : beforeActionSkill.getRoleSkillMap().entrySet()) {
                    final List<Integer> skills = new ArrayList<Integer>();
                    for (final Integer resouceSkill : entry2.getValue()) {
                        if (beforeActionSkillCounter.getSkills() == null || !beforeActionSkillCounter.getSkills().contains(resouceSkill)) {
                            PetSkillEffectorUtil.sendActionExcute(fight, entry2.getKey(), resouceSkill, targetForPos.getId());
                        }
                        else {
                            final SysPetSkill petSkill = SkillDataPool.petSkillMap.get(resouceSkill);
                            if (petSkill != null && PET_PASSIVE_SKILL_TRIGGER_DICT.ENEMY_FIRST_MAGIC.getValue().equals(petSkill.getTriggerType())) {
                                continue;
                            }
                        }
                        if (beforeActionSkillCounter.getSkills() == null) {
                            beforeActionSkillCounter.setSkills(new ArrayList<Integer>());
                        }
                        beforeActionSkillCounter.getSkills().add(resouceSkill);
                        skills.add(resouceSkill);
                    }
                    if (skills.size() > 0) {
                        final List<Integer> newSkills = beforeActionSkillCounter.getRoleSkillMap().get(entry2.getKey());
                        if (newSkills == null || newSkills.size() <= 0) {
                            beforeActionSkillCounter.getRoleSkillMap().put(entry2.getKey(), skills);
                        }
                        else {
                            beforeActionSkillCounter.getRoleSkillMap().get(entry2.getKey()).addAll(skills);
                        }
                    }
                }
            }
            beforeActionSkillCounter.setRound(round);
            fight.getBeforeActionMap().put(!effector.isASide(), beforeActionSkillCounter);
            if (beforeActionSkill != null) {
                final Map<Integer, List<Integer>> roleSkillMap = beforeActionSkill.getRoleSkillMap();
                if (roleSkillMap != null && roleSkillMap.size() > 0) {
                    final List<Integer> removeRoleIds = new ArrayList<Integer>();
                    for (final Map.Entry<Integer, List<Integer>> entry2 : roleSkillMap.entrySet()) {
                        final Integer key = entry2.getKey();
                        PetSkillEffectorUtil.sendCancelAction(fight, key, beforeActionSkill, fight.getMySide(effector.getId()), true);
                        if (entry2.getValue() == null || entry2.getValue().size() <= 0) {
                            removeRoleIds.add(key);
                        }
                    }
                    for (int i = removeRoleIds.size() - 1; i >= 0; --i) {
                        roleSkillMap.remove(removeRoleIds.get(i));
                    }
                }
            }
            fight.getBeforeActionMap().remove(effector.isASide());
        }
        final AstrictSkill astrictSkill = fight.getAstrictSkillMap().get(!effector.isASide());
        if (beforeActionSkillCounter != null && beforeActionSkillCounter.getSkills().contains(82007) && astrictSkill == null) {
            final SkillProcessor processor = SkillProcessor.queryProcessor(82007);
            processor.passive(fight, effector);
        }
        return null;
    }
    
    @Override
    public int targetCount(final Fight fight, final FightObject effector, final Object... args) {
        return 0;
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
