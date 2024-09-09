package com.kitty.system.game.fightnew.book.pet.supreme;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import java.util.stream.Collectors;
import com.kitty.system.enums.war.wardef.LOGIC;
import java.util.List;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_82013 extends SkillProcessor
{
    public SKILL_82013() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 82013);
        List<FightObject> mySidePets = (List)fight.getMySide((int)effector.getId()).stream().filter(obj -> !obj.isFallDown() && obj.getLogic() == LOGIC.LOGICTYPE_PET.getValue() && !obj.getId().equals(effector.getId())).collect(Collectors.toList());
        if (mySidePets != null && mySidePets.size() > 0) {
            return null;
        }
        effector.getStatusMap().clear();
        List<FightObject> list = null;
        if (effector.isASide()) {
            list = fight.getListA();
        }
        else {
            list = fight.getListB();
        }
        ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 7, 82013, 0);
        int unitLife = 0;
        int unitMana = 0;
        for (int i = 0; i < list.size(); ++i) {
        	FightObject obj = (FightObject)list.get(i);
            if (!obj.getId().equals(effector.getId())) {
                unitLife = (int)((double)obj.getMaxLife() * 0.5);
                unitMana = (int)((double)obj.getMaxMana() * 0.5);
                if (unitLife + obj.getCurrLife() > obj.getMaxLife()) {
                    unitLife = obj.getMaxLife() - obj.getCurrLife();
                }
                if (unitMana + obj.getCurrMana() > obj.getMaxMana()) {
                    unitMana = obj.getMaxMana() - obj.getCurrMana();
                }
                boolean down = obj.isFallDown();
                boolean isRevive = false;
                if (down) {
                    isRevive = true;
                }
                FightObjectParam targetParam = ActionParamUtils.changeHpMpForTarget(fight, obj, 2, unitLife, unitMana, isRevive);
                actionDetailParam.getTargetParamList().add(targetParam);
            }
        }
        if (actionDetailParam.getTargetParamList().size() > 0) {
            fight.getActionParam().getActionDetailParams().add(actionDetailParam);
            skillRecord.setT((long)((FightObjectParam)actionDetailParam.getTargetParamList().get(0)).getObjId());
        }
        FightNewService fightNewService = (FightNewService)SpringUtils.getBean(FightNewService.class);
        FightObject host = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
        if (host != null) {
            host.setTargetId((int)effector.getId());
            host.setType(12);
            fightNewService.callbackPet(host, fight, false);
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
