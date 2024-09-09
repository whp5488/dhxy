package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83009 extends SkillProcessor
{
    public SKILL_83009() {
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
        double percent = 0.5;
        int targetCount = 1;
        while (SkillAi.canAffectOnPro(percent) && ++targetCount < 3) {}
        return targetCount;
    }
    
    @Override
    public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        RoleSkill skill = effector.getSkillBy(Integer.valueOf(83009));
        Effect e = new Effect();
        if (skill != null) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 83009);
            int level = effector.getLevel();
            if (level <= 0) {
                level = 1;
            }
            double hurt = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(0), Double.valueOf((double)(80 * level) + (double)(effector.getMaxMana() / 100 * 6) * ((double)effector.getRelive() * 0.6 + 1.0) * PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())));
            int harm = PetSkillEffectorUtil.wuXingWaterCount(fight, effector, target, hurt);
            e.setType(100000);
            e.setHurt(harm);
            skillRecord.setT((long)(int)target.getId());
            return e;
        }
        else {
            return e;
        }
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        int level = PetSkillEffectorUtil.getExcuteLevel(args);
        return level * 32;
    }
}
