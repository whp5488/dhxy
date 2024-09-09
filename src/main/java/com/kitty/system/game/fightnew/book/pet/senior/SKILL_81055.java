package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import java.util.stream.Collectors;
import com.kitty.system.enums.war.wardef.LOGIC;
import java.util.List;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_81055 extends SkillProcessor
{
    public SKILL_81055() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        List<FightObject> mySidePets = (List)fight.getMySide((int)effector.getId()).stream().filter(obj -> !obj.isFallDown() && obj.getLogic() == LOGIC.LOGICTYPE_PET.getValue() && !obj.getId().equals(effector.getId())).collect(Collectors.toList());
        if (mySidePets != null && mySidePets.size() > 0) {
            return null;
        }
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 81055);
        effector.getStatusMap().clear();
        FightObject host = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
        FightObject unit = SkillAi.effectProBassHight(fight, effector, Integer.valueOf(1));
        boolean fallDown = host.isFallDown();
        int hostLife = (int)((double)host.getMaxLife() * 0.5);
        int hostMana = (int)((double)host.getMaxMana() * 0.5);
        if (host.getCurrLife() + hostLife > host.getMaxLife()) {
            hostLife = host.getMaxLife() - host.getCurrLife();
        }
        if (host.getCurrMana() + hostMana > host.getMaxMana()) {
            hostMana = host.getMaxMana() - host.getCurrMana();
        }
        boolean isRevive = false;
        if (fallDown) {
            isRevive = true;
        }
        ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 7, 81055, 0);
        FightObjectParam targetHostParam = ActionParamUtils.changeHpMpForTarget(fight, host, 2, hostLife, hostMana, isRevive);
        actionDetailParam.getTargetParamList().add(targetHostParam);
        skillRecord.setT((long)(int)host.getId());
        if (unit != null) {
            int unitLife = (int)((double)unit.getMaxLife() * 0.5);
            int unitMana = (int)((double)unit.getMaxMana() * 0.5);
            if (unit.getCurrLife() + unitLife > unit.getMaxLife()) {
                unitLife = unit.getMaxLife() - unit.getCurrLife();
            }
            if (unit.getCurrMana() + unitMana > unit.getMaxMana()) {
                unitMana = unit.getMaxMana() - unit.getCurrMana();
            }
            boolean down = unit.isFallDown();
            boolean isReviveUnit = false;
            if (down) {
                isReviveUnit = true;
            }
            ActionParamUtils.changeHpMp(fight, effector, unit, 7, 81055, 2, unitLife, unitMana, isReviveUnit);
            FightObjectParam targetUnitParam = ActionParamUtils.changeHpMpForTarget(fight, unit, 2, unitLife, unitMana, isReviveUnit);
            actionDetailParam.getTargetParamList().add(targetUnitParam);
        }
        if (actionDetailParam.getTargetParamList().size() > 0) {
            fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        }
        FightNewService fightNewService = (FightNewService)SpringUtils.getBean(FightNewService.class);
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
