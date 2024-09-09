package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import java.util.Iterator;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import java.util.ArrayList;
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
    public SKILL_83035() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 83035);
        boolean isExcute = false;
        if (fight.getRound() == 1) {
            isExcute = true;
        }
        else {
            int type = effector.getFightActionRecord().getInt(String.valueOf(fight.getRound() - 1));
            if (type >= 0) {
                Integer typeDetails = Integer.valueOf(effector.getFightActionRecord().getInt(fight.getRound() - 1 + "details"));
                Integer typeDetailsD = Integer.valueOf(effector.getFightActionRecord().getInt(fight.getRound() + "details"));
                if (typeDetails.equals(typeDetailsD)) {
                    isExcute = true;
                }
            }
        }
        if (!isExcute) {
            return null;
        }
        List<Integer> statuss = (List)StatusConst.getAnomalousStatus().stream().filter(status -> (int)status != 1019).collect(Collectors.toList());
        List<Integer> statusList = new ArrayList<>();
        Iterator<Integer> iterator = statuss.iterator();
        while (iterator.hasNext()) {
        	Integer status = (Integer)iterator.next();
            int time = effector.getStatusMap().getInt(String.valueOf(status));
            if (time > 0) {
                statusList.add(status);
                effector.removeStatus((int)status);
            }
            else {
                continue;
            }
        }
        if (statusList.size() > 0) {
            ActionParamUtils.clearStatus(fight, effector, effector, 7, 83035, 2, statusList);
            skillRecord.setT((long)(int)effector.getId());
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
}
