
package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import java.util.Iterator;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.game.vo.utils.StatusConst;
import java.util.List;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83035 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 83035);
        boolean isExcute = false;
        if (fight.getRound() == 1) {
            isExcute = true;
        }
        else {
            final int type = effector.getFightActionRecord().getInt(String.valueOf(fight.getRound() - 1));
            if (type >= 0) {
                final Integer typeDetails = effector.getFightActionRecord().getInt(fight.getRound() - 1 + "details");
                final Integer typeDetailsD = effector.getFightActionRecord().getInt(fight.getRound() + "details");
                if (typeDetails.equals(typeDetailsD)) {
                    isExcute = true;
                }
            }
        }
        if (!isExcute) {
            return null;
        }
        final List<Integer> statuss = StatusConst.getAnomalousStatus().stream().filter(status -> status != 1019).collect(Collectors.toList());
        final List<Integer> statusList = new ArrayList<Integer>();
        final Iterator<Integer> iterator = statuss.iterator();
        while (iterator.hasNext()) {
            Integer status = iterator.next();
            final int time = effector.getStatusMap().getInt(String.valueOf(status));
            if (time > 0) {
                statusList.add(status);
                effector.removeStatus(status);
            }
        }
        if (statusList.size() > 0) {
            ActionParamUtils.clearStatus(fight, effector, effector, 7, 83035, 2, statusList);
            skillRecord.setT(effector.getId());
        }
        return null;
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
