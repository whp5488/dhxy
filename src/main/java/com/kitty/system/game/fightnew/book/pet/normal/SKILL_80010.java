package com.kitty.system.game.fightnew.book.pet.normal;

import java.util.Arrays;
import com.kitty.system.enums.war.itempro.def.PET_SKILL_TERM_DICT;
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

public class SKILL_80010 extends SkillProcessor
{
    static final List<String> terms;
    
    public SKILL_80010() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 80010);
        double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1 + 2.0 * ((double)effector.getRelive() * 0.4 + 1.0) * PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0));
        boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag) {
            FightObject unit = SkillAi.effectProBass(fight, effector, Integer.valueOf(2), SKILL_80010.terms, false);
            if (unit != null) {
                double probability = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((double)unit.getCurrMana() / (double)unit.getMaxMana()));
                if (probability >= 0.3) {
                    return null;
                }
                int addMana = (int)((double)unit.getMaxMana() * 0.5);
                if (addMana + unit.getCurrMana() > unit.getMaxMana()) {
                    addMana = unit.getMaxMana() - unit.getCurrMana();
                }
                ActionParamUtils.changeMana(fight, effector, unit, 7, 80010, 2, addMana);
                skillRecord.setT((long)(int)unit.getId());
            }
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
        terms = Arrays.asList(PET_SKILL_TERM_DICT.PRO_BASS_PET_N.getValue(), PET_SKILL_TERM_DICT.PRO_BASS_DOWN_N.getValue());
    }
}
