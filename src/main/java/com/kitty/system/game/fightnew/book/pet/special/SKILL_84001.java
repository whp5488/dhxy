
package com.kitty.system.game.fightnew.book.pet.special;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import java.util.List;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.vo.utils.StatusConst;
import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_84001 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 84001);
        final double percent = 0.3;
        final boolean flag = SkillAi.canAffectOnPro(percent);
        if (!flag) {
            return NutMap.NEW().setv("successful", (Object)false);
        }
        final NutMap skillCoolMap = effector.getSkillCoolMap();
        if (skillCoolMap.getInt(String.valueOf(84001)) > 0) {
            return NutMap.NEW().setv("successful", (Object)false);
        }
        skillCoolMap.setv(String.valueOf(84001), (Object)WarMaxRound.WAR_MAX_ROUND.getValue());
        final int life = effector.getMaxLife();
        final List<Integer> clearStatusList = effector.getStatusList(StatusConst.getAnomalousStatus());
        if (clearStatusList != null && clearStatusList.size() > 0) {
            effector.clearStatus(clearStatusList);
            ActionParamUtils.changeLifeClearStatus(fight, effector, effector, 7, 84001, 2, life, true, clearStatusList);
        }
        else {
            ActionParamUtils.changeLife(fight, effector, effector, 7, 84001, 2, life, true);
        }
        skillRecord.setT(effector.getId());
        return NutMap.NEW().setv(String.valueOf(41), (Object)1).setv("successful", (Object)true);
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
