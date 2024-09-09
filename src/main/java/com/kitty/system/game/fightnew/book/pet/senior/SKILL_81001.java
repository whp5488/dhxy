package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_81001 extends SkillProcessor
{
    public SKILL_81001() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        if ((double)effector.getCurrLife() < (double)effector.getMaxLife() * 0.4) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 81001);
            double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1 + 1.5 * ((double)effector.getRelive() * 0.5 + 1.0) * (Math.pow((double)effector.getLevel(), 0.5) / 10.0 + Math.pow((double)effector.getClose(), 0.16666666666666666) * 10.0 / (double)(100 + effector.getRelive() * 20))) / 100.0));
            if (SkillAi.canAffectOnPro(percent)) {
                int round = 3;
                effector.getStatusMap().setv(String.valueOf(1011), Integer.valueOf(round));
                effector.getStatusMap().setv("1011value", Integer.valueOf(99));
                ActionParamUtils.addStatus(fight, effector, effector, 7, 81001, 2, 1011, round);
                skillRecord.setT((long)(int)effector.getId());
            }
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
