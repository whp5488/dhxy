
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
import java.util.stream.Collector;
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
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        final List<FightObject> mySidePets = fight.getMySide(effector.getId()).stream().filter(obj -> !obj.isFallDown() && obj.getLogic() == LOGIC.LOGICTYPE_PET.getValue() && !obj.getId().equals(effector.getId())).collect(Collectors.toList());
        if (mySidePets != null && mySidePets.size() > 0) {
            return null;
        }
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 81055);
        effector.getStatusMap().clear();
        final FightObject host = fight.queryFightObject(effector.getHostId());
        final FightObject unit = SkillAi.effectProBassHight(fight, effector, 1);
        final boolean fallDown = host.isFallDown();
        int hostLife = (int)(host.getMaxLife() * 0.5);
        int hostMana = (int)(host.getMaxMana() * 0.5);
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
        final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 7, 81055, 0);
        final FightObjectParam targetHostParam = ActionParamUtils.changeHpMpForTarget(fight, host, 2, hostLife, hostMana, isRevive);
        actionDetailParam.getTargetParamList().add(targetHostParam);
        skillRecord.setT(host.getId());
        if (unit != null) {
            int unitLife = (int)(unit.getMaxLife() * 0.5);
            int unitMana = (int)(unit.getMaxMana() * 0.5);
            if (unit.getCurrLife() + unitLife > unit.getMaxLife()) {
                unitLife = unit.getMaxLife() - unit.getCurrLife();
            }
            if (unit.getCurrMana() + unitMana > unit.getMaxMana()) {
                unitMana = unit.getMaxMana() - unit.getCurrMana();
            }
            final boolean down = unit.isFallDown();
            boolean isReviveUnit = false;
            if (down) {
                isReviveUnit = true;
            }
            ActionParamUtils.changeHpMp(fight, effector, unit, 7, 81055, 2, unitLife, unitMana, isReviveUnit);
            final FightObjectParam targetUnitParam = ActionParamUtils.changeHpMpForTarget(fight, unit, 2, unitLife, unitMana, isReviveUnit);
            actionDetailParam.getTargetParamList().add(targetUnitParam);
        }
        if (actionDetailParam.getTargetParamList().size() > 0) {
            fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        }
        final FightNewService fightNewService = SpringUtils.getBean(FightNewService.class);
        if (host != null) {
            host.setTargetId(effector.getId());
            host.setType(12);
            fightNewService.callbackPet(host, fight, false);
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
