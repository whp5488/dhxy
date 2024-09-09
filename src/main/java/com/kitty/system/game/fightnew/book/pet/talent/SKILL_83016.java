package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.service.CountUtils;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83016 extends SkillProcessor
{
    public SKILL_83016() {
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
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 83016);
        double percent = 0.05;
        int phyPower = effector.getPhyPower();
        String newPhyPowerStr = BigDecimalUtil.multiply(String.valueOf(phyPower), String.valueOf(BigDecimalUtil.additionDouble(1.0, percent)));
        String percentPhyPowerStr = BigDecimalUtil.divide(newPhyPowerStr, String.valueOf(CountUtils.countPhyPower(effector.getRole())));
        if (Double.parseDouble(percentPhyPowerStr) >= 3.0) {
            effector.setPhyPower(CountUtils.countPhyPower(effector.getRole()));
        }
        else {
            effector.setPhyPower((int)Double.parseDouble(newPhyPowerStr));
        }
        skillRecord.setT((long)(int)target.getId());
        return null;
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        return 0;
    }
}
