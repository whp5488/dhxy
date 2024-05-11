
package com.kitty.system.game.fightnew.book.pet.supreme;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.vo.utils.StatusConst;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_82001 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 82001);
        List<FightObject> myFightObject = null;
        if (effector.isASide()) {
            myFightObject = fight.getListA().stream().filter(obj -> !obj.getId().equals(effector.getId()) && !obj.isFallDown() && (obj.isPet() || obj.isPartner())).collect(Collectors.toList());
        }
        else {
            myFightObject = fight.getListB().stream().filter(obj -> !obj.getId().equals(effector.getId()) && !obj.isFallDown() && (obj.isPet() || obj.isPartner())).collect(Collectors.toList());
        }
        final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 7, 82001, 0);
        for (int i = 0; i < myFightObject.size(); ++i) {
            final FightObject obj = myFightObject.get(i);
            final List<Integer> clearStatusList = obj.getStatusList(StatusConst.getAnomalousStatus());
            if (clearStatusList != null) {
                obj.clearStatus(clearStatusList);
                final FightObjectParam targetParam = ActionParamUtils.clearStatusForTarget(obj.getId(), i + 1, clearStatusList, 2);
                actionDetailParam.getTargetParamList().add(targetParam);
            }
        }
        if (actionDetailParam.getTargetParamList().size() > 0) {
            fight.getActionParam().getActionDetailParams().add(actionDetailParam);
            skillRecord.setT(actionDetailParam.getTargetParamList().get(0).getObjId());
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
