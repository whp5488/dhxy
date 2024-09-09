package com.kitty.system.game.fightnew.book.pet.senior;

import java.util.Arrays;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import java.util.List;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_81014 extends SkillProcessor
{
    static final List<String> terms;
    
    public SKILL_81014() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 81014);
        double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1 + 2.0 * ((double)effector.getRelive() * 0.4 + 1.0) * PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0));
        boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag) {
            FightObject unit = SkillAi.effectProBass(fight, effector, Integer.valueOf(1), SKILL_81014.terms);
            if (!unit.isFallDown()) {
                return null;
            }
            int addLife = (int)((double)unit.getMaxLife() * 0.5);
            ActionParamUtils.changeLife(fight, effector, unit, 7, 81014, 2, addLife, true);
            skillRecord.setT((long)(int)unit.getId());
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
    
    static {
        terms = Arrays.asList(new String[0]);
    }
}
