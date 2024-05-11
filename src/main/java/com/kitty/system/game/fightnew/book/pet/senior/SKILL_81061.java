
package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.bean.AstrictSkill;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.game.skill.domain.RoleSkill;
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
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        FightParamPool.addSkillRecord(fight, 7, effector.getId(), 81061);
        final SysPetSkill petSkill = SkillDataPool.petSkillMap.get(81061);
        if (petSkill != null) {
            final List<RoleSkill> skills = effector.getSkillIds().stream().filter(roleSkill -> roleSkill.getSkillId() > 0).collect(Collectors.toList());
            final Map<Integer, SysPetSkill> sysPetSkillMap = PetTriggerPassiveSkill.data_pet_skill_maps.get(petSkill.getTriggerType());
            final SysPetSkill sysPetSkill = PetSkillEffectorUtil.initiativeQueryCover(petSkill, skills, sysPetSkillMap);
            if (sysPetSkill != null && 81061 != sysPetSkill.getSerial()) {
                final SkillProcessor processor = SkillProcessor.queryProcessor(sysPetSkill.getSerial());
                processor.passive(fight, effector, new Object[0]);
                return null;
            }
        }
        final boolean isASide = effector.isASide();
        AstrictSkill astrictSkill = fight.getAstrictSkillMap().get(!isASide);
        boolean isLayIn = true;
        if (astrictSkill != null) {
            isLayIn = false;
        }
        if (isLayIn) {
            astrictSkill = new AstrictSkill();
            astrictSkill.setASide(!isASide);
            astrictSkill.setRound(1);
            astrictSkill.setTime(2);
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
