package com.kitty.system.game.fightnew.book.pet.supreme;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.vo.utils.StatusConst;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
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
    public SKILL_82001() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 82001);
        List<FightObject> myFightObject = null;
        if (effector.isASide()) {
            myFightObject = (List)fight.getListA().stream().filter(obj -> !obj.getId().equals(effector.getId()) && !obj.isFallDown() && (obj.isPet() || obj.isPartner())).collect(Collectors.toList());
        }
        else {
            myFightObject = (List)fight.getListB().stream().filter(obj -> !obj.getId().equals(effector.getId()) && !obj.isFallDown() && (obj.isPet() || obj.isPartner())).collect(Collectors.toList());
        }
        ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 7, 82001, 0);
        for (int i = 0; i < myFightObject.size(); ++i) {
            FightObject obj = (FightObject)myFightObject.get(i);
            List<Integer> clearStatusList = obj.getStatusList(StatusConst.getAnomalousStatus());
            if (clearStatusList != null) {
                obj.clearStatus(clearStatusList);
                FightObjectParam targetParam = ActionParamUtils.clearStatusForTarget((int)obj.getId(), i + 1, clearStatusList, 2);
                actionDetailParam.getTargetParamList().add(targetParam);
            }
        }
        if (actionDetailParam.getTargetParamList().size() > 0) {
            fight.getActionParam().getActionDetailParams().add(actionDetailParam);
            skillRecord.setT((long)((FightObjectParam)actionDetailParam.getTargetParamList().get(0)).getObjId());
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
