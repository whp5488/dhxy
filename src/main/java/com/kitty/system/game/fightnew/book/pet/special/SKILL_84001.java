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
    public SKILL_84001() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 84001);
        double percent = 0.3;
        boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag) {
            NutMap skillCoolMap = effector.getSkillCoolMap();
            if (skillCoolMap.getInt(String.valueOf(84001)) > 0) {
                return NutMap.NEW().setv("successful", Boolean.valueOf(false));
            }
            skillCoolMap.setv(String.valueOf(84001), Integer.valueOf(WarMaxRound.WAR_MAX_ROUND.getValue()));
            int life = effector.getMaxLife();
            List<Integer> clearStatusList = effector.getStatusList(StatusConst.getAnomalousStatus());
            if (clearStatusList != null && clearStatusList.size() > 0) {
                effector.clearStatus(clearStatusList);
                ActionParamUtils.changeLifeClearStatus(fight, effector, effector, 7, 84001, 2, life, true, clearStatusList);
            }
            else {
                ActionParamUtils.changeLife(fight, effector, effector, 7, 84001, 2, life, true);
            }
            skillRecord.setT((long)(int)effector.getId());
            return NutMap.NEW().setv(String.valueOf(41), Integer.valueOf(1)).setv("successful", Boolean.valueOf(true));
        }
        else {
            return NutMap.NEW().setv("successful", Boolean.valueOf(false));
        }
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
