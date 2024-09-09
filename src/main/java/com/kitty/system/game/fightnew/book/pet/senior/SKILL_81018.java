package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
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

public class SKILL_81018 extends SkillProcessor
{
    public SKILL_81018() {
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
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 81018);
        SysPetSkill petSkill = (SysPetSkill)args[1];
        BeforeActionSkill beforeActionSkill = (BeforeActionSkill)fight.getBeforeActionMap().get(Boolean.valueOf(target.isASide()));
        Map<Integer, SysPetSkill> resultMap = (Map)PetTriggerPassiveSkill.data_pet_skill_maps.get(petSkill.getTriggerType());
        SysPetSkill coverPetSkill = PetSkillEffectorUtil.initiativeQueryCover(petSkill, (List)effector.getSkillIds().stream().filter(roleSkill -> (int)roleSkill.getSkillId() > 1).collect(Collectors.toList()), resultMap);
        if (coverPetSkill != null && !coverPetSkill.getSerial().equals(petSkill.getSerial()) && beforeActionSkill.getSkills().contains(coverPetSkill.getSerial())) {
            return null;
        }
        if (effector.getSkillCoolMap().getInt(String.valueOf(81018)) > 0) {
            return null;
        }
        effector.getSkillCoolMap().setv(String.valueOf(81018), Integer.valueOf(fight.getMaxRound()));
        if (!beforeActionSkill.getSkills().contains(Integer.valueOf(81018))) {
            return null;
        }
        Integer skillid = (Integer)beforeActionSkill.getSkills().stream().filter(skill -> 81018 == (int)skill).findAny().get();
        if (skillid != null && (int)skillid > 0) {
            int hurt = (int)args[0];
            int oldCurrMana = target.getCurrMana();
            int surplusMana = oldCurrMana - hurt;
            int changeMana = -hurt;
            if (surplusMana <= 0) {
                changeMana = -target.getCurrMana();
            }
            ActionParamUtils.changeMana(fight, effector, target, 7, 81018, 2, changeMana);
            PetSkillEffectorUtil.enemyFirstMagicCancelAction(fight, effector, target, beforeActionSkill, skillid);
            skillRecord.setT((long)(int)target.getId());
        }
        return null;
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        return 0;
    }
}
