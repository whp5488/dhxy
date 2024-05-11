
package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.enums.war.wardef.LOGIC;
import java.util.List;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83027 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 83027);
        final int[] wuXing = { PRO_DICT_TYPE.Q_KJ.getDist(), PRO_DICT_TYPE.Q_KM.getDist(), PRO_DICT_TYPE.Q_KS.getDist(), PRO_DICT_TYPE.Q_KH.getDist(), PRO_DICT_TYPE.Q_KT.getDist() };
        final List<FightObject> units = fight.getMySide(effector.getId()).stream().filter(obj -> obj.getLogic() == LOGIC.LOGICTYPE_HERO.getValue() && !obj.isFallDown()).collect(Collectors.toList());
        final int index = (fight.getRound() - 1) % wuXing.length;
        final double percent = BigDecimalUtil.decimalScaleObjectDown(3, (20.0 + 2.2 * (effector.getRelive() * 1 + 1) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose())) / 100.0);
        final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 7, 83027, 0);
        for (int i = 0; i < units.size(); ++i) {
            final FightObject unit = units.get(i);
            double unitWuXing = unit.getPropertyMap().getDouble(String.valueOf(wuXing[index]));
            unitWuXing = BigDecimalUtil.additionDouble(unitWuXing, percent);
            unit.getPropertyMap().setv(String.valueOf(wuXing[index]), (Object)unitWuXing);
            final FightObjectParam targetParam = ActionParamUtils.addStatusForTarget(unit, 2, 2067, -1, i + 1);
            actionDetailParam.getTargetParamList().add(targetParam);
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
