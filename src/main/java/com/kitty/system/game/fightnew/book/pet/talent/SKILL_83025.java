package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import java.util.List;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83025 extends SkillProcessor
{
    public SKILL_83025() {
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
        return null;
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        return 0;
    }
    
    @Override
    public List<Integer> getTargetIdsForTFQX(Fight fight, FightObject effector, Object... args) {
        return PetTriggerPassiveSkill.getTFQXTargetIds(fight, effector, 83025);
    }
}
