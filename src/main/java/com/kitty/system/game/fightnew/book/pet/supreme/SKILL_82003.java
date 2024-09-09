package com.kitty.system.game.fightnew.book.pet.supreme;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import java.util.Iterator;
import com.kitty.system.game.fightnew.bean.AstrictSkill;
import java.util.HashMap;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysPetSkill;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
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
    public SKILL_82003() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 82003);
        int round = 1;
        List<FightObject> targets = fight.getTargetSide((int)effector.getId());
        FightObject target = PetSkillEffectorUtil.getTargetForPos(targets, 3);
        BeforeActionSkill beforeActionSkill = (BeforeActionSkill)fight.getBeforeActionMap().get(Boolean.valueOf(effector.isASide()));
        BeforeActionSkill beforeActionSkillCounter = null;
        if (beforeActionSkill != null && beforeActionSkill.isPassiveAction()) {
            ActionParamUtils.actionForTargetNoth(fight, effector, target, 7, 82003, 2);
            beforeActionSkillCounter = (BeforeActionSkill)fight.getBeforeActionMap().get(Boolean.valueOf(!effector.isASide()));
            FightObject targetForPos = PetSkillEffectorUtil.getTargetForPos(targets, 3);
            if (beforeActionSkillCounter == null || !beforeActionSkillCounter.isPassiveAction()) {
                beforeActionSkillCounter = new BeforeActionSkill();
                beforeActionSkillCounter.setPassiveAction(true);
                beforeActionSkillCounter.setASide(!effector.isASide());
                if (beforeActionSkill.getRoleSkillMap() != null && beforeActionSkill.getRoleSkillMap().size() > 0) {
                    Map<Integer, List<Integer>> roleSkillM = new HashMap();
                    for (Map.Entry<Integer, List<Integer>> entry2 : beforeActionSkill.getRoleSkillMap().entrySet()) {
                        List<Integer> list = new ArrayList<>();
                        for (Integer skillId : entry2.getValue()) {
                            list.add(skillId);
                            if (beforeActionSkillCounter.getSkills() == null || !beforeActionSkillCounter.getSkills().contains(skillId)) {
                                PetSkillEffectorUtil.sendActionExcute(fight, (Integer)entry2.getKey(), (int)skillId, targetForPos.getId());
                            }
                            if (beforeActionSkillCounter.getSkills() == null) {
                                beforeActionSkillCounter.setSkills(new ArrayList<>());
                            }
                            beforeActionSkillCounter.getSkills().add(skillId);
                        }
                        roleSkillM.put(entry2.getKey(), list);
                    }
                    beforeActionSkillCounter.setRoleSkillMap(roleSkillM);
                }
            }
            else {
                for (Map.Entry<Integer, List<Integer>> entry : beforeActionSkill.getRoleSkillMap().entrySet()) {
                    List<Integer> skills = new ArrayList<>();
                    for (Integer resouceSkill : entry.getValue()) {
                        if (beforeActionSkillCounter.getSkills() == null || !beforeActionSkillCounter.getSkills().contains(resouceSkill)) {
                            PetSkillEffectorUtil.sendActionExcute(fight, (Integer)entry.getKey(), (int)resouceSkill, targetForPos.getId());
                        }
                        else {
                            SysPetSkill petSkill = (SysPetSkill)SkillDataPool.petSkillMap.get(resouceSkill);
                            if (petSkill != null && PET_PASSIVE_SKILL_TRIGGER_DICT.ENEMY_FIRST_MAGIC.getValue().equals(petSkill.getTriggerType())) {
                                continue;
                            }
                        }
                        if (beforeActionSkillCounter.getSkills() == null) {
                            beforeActionSkillCounter.setSkills(new ArrayList<>());
                        }
                        beforeActionSkillCounter.getSkills().add(resouceSkill);
                        skills.add(resouceSkill);
                    }
                    if (skills.size() > 0) {
                        List<Integer> newSkills = (List)beforeActionSkillCounter.getRoleSkillMap().get(entry.getKey());
                        if (newSkills == null || newSkills.size() <= 0) {
                            beforeActionSkillCounter.getRoleSkillMap().put((Integer)entry.getKey(), skills);
                        }
                        else {
                            ((List)beforeActionSkillCounter.getRoleSkillMap().get(entry.getKey())).addAll(skills);
                        }
                    }
                    else {
                        continue;
                    }
                }
            }
            beforeActionSkillCounter.setRound(round);
            fight.getBeforeActionMap().put(Boolean.valueOf(!effector.isASide()), beforeActionSkillCounter);
            if (beforeActionSkill != null) {
                Map<Integer, List<Integer>> roleSkillMap = beforeActionSkill.getRoleSkillMap();
                if (roleSkillMap != null && roleSkillMap.size() > 0) {
                    List<Integer> removeRoleIds = new ArrayList<>();
                    for (Map.Entry<Integer, List<Integer>> entry : roleSkillMap.entrySet()) {
                        Integer key = (Integer)entry.getKey();
                        PetSkillEffectorUtil.sendCancelAction(fight, key, beforeActionSkill, fight.getMySide((int)effector.getId()), true);
                        if (entry.getValue() == null || ((List)entry.getValue()).size() <= 0) {
                            removeRoleIds.add(key);
                        }
                        else {
                            continue;
                        }
                    }
                    for (int i = removeRoleIds.size() - 1; i >= 0; --i) {
                        roleSkillMap.remove(removeRoleIds.get(i));
                    }
                }
            }
            fight.getBeforeActionMap().remove(Boolean.valueOf(effector.isASide()));
        }
        AstrictSkill astrictSkill = (AstrictSkill)fight.getAstrictSkillMap().get(Boolean.valueOf(!effector.isASide()));
        if (beforeActionSkillCounter != null && beforeActionSkillCounter.getSkills().contains(Integer.valueOf(82007)) && astrictSkill == null) {
            SkillProcessor processor = SkillProcessor.queryProcessor(Integer.valueOf(82007));
            processor.passive(fight, effector, new Object[0]);
        }
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
