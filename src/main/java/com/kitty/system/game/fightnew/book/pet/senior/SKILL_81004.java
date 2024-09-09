package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import java.util.List;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_81004 extends SkillProcessor
{
    public SKILL_81004() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject target, Object... args) {
        if (args[0] != null && !"".equals(args[0])) {
            if (fight.getRound() <= 3) {
                return null;
            }
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)target.getId(), 81004);
            int flag = target.getSkillCoolMap().getInt(String.valueOf(81004));
            if (flag > 0) {
                return null;
            }
            target.getSkillCoolMap().setv(String.valueOf(81004), Integer.valueOf(fight.getMaxRound()));
            int life = 10;
            boolean awakenFlag = AwakenSkillUtils.skillIntensifyBoolean(fight, target, 31501);
            if (awakenFlag) {
                int awakenAddLife = (int)((double)target.getMaxLife() * 0.3);
                life = ((awakenAddLife > life) ? awakenAddLife : life);
            }
            target.setCurrLife(life);
            target.getStatusMap().clear();
            List<Integer> clearStatusList = new ArrayList<>();
            clearStatusList.add(Integer.valueOf(19999));
            NutMap map = NutMap.NEW();
            map.setv("successful", Boolean.valueOf(true));
            map.setv(String.valueOf(41), Integer.valueOf(1));
            map.setv("specialStatus", Integer.valueOf(2032));
            map.setv("clearStatus", clearStatusList);
            skillRecord.setT((long)(int)target.getId());
            return map;
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
