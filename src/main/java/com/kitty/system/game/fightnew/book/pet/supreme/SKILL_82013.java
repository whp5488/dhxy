
package com.kitty.system.game.fightnew.book.pet.supreme;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import java.util.stream.Collector;
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
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 82013);
        final List<FightObject> mySidePets = fight.getMySide(effector.getId()).stream().filter(obj -> !obj.isFallDown() && obj.getLogic() == LOGIC.LOGICTYPE_PET.getValue() && !obj.getId().equals(effector.getId())).collect(Collectors.toList());
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
        final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 7, 82013, 0);
        int unitLife = 0;
        int unitMana = 0;
        for (int i = 0; i < list.size(); ++i) {
            FightObject obj = list.get(i);
            if (!obj.getId().equals(effector.getId())) {
                unitLife = (int)(obj.getMaxLife() * 0.5);
                unitMana = (int)(obj.getMaxMana() * 0.5);
                if (unitLife + obj.getCurrLife() > obj.getMaxLife()) {
                    unitLife = obj.getMaxLife() - obj.getCurrLife();
                }
                if (unitMana + obj.getCurrMana() > obj.getMaxMana()) {
                    unitMana = obj.getMaxMana() - obj.getCurrMana();
                }
                final boolean down = obj.isFallDown();
                boolean isRevive = false;
                if (down) {
                    isRevive = true;
                }
                final FightObjectParam targetParam = ActionParamUtils.changeHpMpForTarget(fight, obj, 2, unitLife, unitMana, isRevive);
                actionDetailParam.getTargetParamList().add(targetParam);
            }
        }
        if (actionDetailParam.getTargetParamList().size() > 0) {
            fight.getActionParam().getActionDetailParams().add(actionDetailParam);
            skillRecord.setT(actionDetailParam.getTargetParamList().get(0).getObjId());
        }
        final FightNewService fightNewService = SpringUtils.getBean(FightNewService.class);
        final FightObject host = fight.queryFightObject(effector.getHostId());
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
