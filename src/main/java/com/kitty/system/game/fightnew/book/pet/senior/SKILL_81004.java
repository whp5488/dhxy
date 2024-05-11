
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
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject target, final Object... args) {
        if (args[0] == null || "".equals(args[0])) {
            return NutMap.NEW().setv("successful", (Object)false);
        }
        if (fight.getRound() <= 3) {
            return null;
        }
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, target.getId(), 81004);
        final int flag = target.getSkillCoolMap().getInt(String.valueOf(81004));
        if (flag > 0) {
            return null;
        }
        target.getSkillCoolMap().setv(String.valueOf(81004), (Object)fight.getMaxRound());
        int life = 10;
        final boolean awakenFlag = AwakenSkillUtils.skillIntensifyBoolean(fight, target, 31501);
        if (awakenFlag) {
            final int awakenAddLife = (int)(target.getMaxLife() * 0.3);
            life = ((awakenAddLife > life) ? awakenAddLife : life);
        }
        target.setCurrLife(life);
        target.getStatusMap().clear();
        final List<Integer> clearStatusList = new ArrayList<Integer>();
        clearStatusList.add(19999);
        final NutMap map = NutMap.NEW();
        map.setv("successful", (Object)true);
        map.setv(String.valueOf(41), (Object)1);
        map.setv("specialStatus", (Object)2032);
        map.setv("clearStatus", (Object)clearStatusList);
        skillRecord.setT(target.getId());
        return map;
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
