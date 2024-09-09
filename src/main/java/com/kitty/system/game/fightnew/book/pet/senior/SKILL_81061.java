package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.bean.AstrictSkill;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_81061 extends SkillProcessor
{
    public SKILL_81061() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 81061);
        SysPetSkill petSkill = (SysPetSkill)SkillDataPool.petSkillMap.get(Integer.valueOf(81061));
        if (petSkill != null) {
            List<RoleSkill> skills = (List)effector.getSkillIds().stream().filter(roleSkill -> (int)roleSkill.getSkillId() > 0).collect(Collectors.toList());
            Map<Integer, SysPetSkill> sysPetSkillMap = (Map)PetTriggerPassiveSkill.data_pet_skill_maps.get(petSkill.getTriggerType());
            SysPetSkill sysPetSkill = PetSkillEffectorUtil.initiativeQueryCover(petSkill, skills, sysPetSkillMap);
            if (sysPetSkill != null && 81061 != (int)sysPetSkill.getSerial()) {
                SkillProcessor processor = SkillProcessor.queryProcessor(sysPetSkill.getSerial());
                processor.passive(fight, effector, new Object[0]);
                return null;
            }
        }
        boolean isASide = effector.isASide();
        AstrictSkill astrictSkill = (AstrictSkill)fight.getAstrictSkillMap().get(Boolean.valueOf(!isASide));
        boolean isLayIn = true;
        if (astrictSkill != null) {
            isLayIn = false;
        }
        if (isLayIn) {
            astrictSkill = new AstrictSkill();
            astrictSkill.setASide(!isASide);
            astrictSkill.setRound(1);
            astrictSkill.setTime(2);
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
