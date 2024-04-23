package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.List;
import java.util.stream.Collectors;
import org.nutz.lang.util.NutMap;

public class SKILL_83027 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 83027);
    int[] wuXing = { PRO_DICT_TYPE.Q_KJ.getDist(), PRO_DICT_TYPE.Q_KM.getDist(), PRO_DICT_TYPE.Q_KS.getDist(), PRO_DICT_TYPE.Q_KH.getDist(), PRO_DICT_TYPE.Q_KT.getDist() };
    List<FightObject> units = (List<FightObject>)fight.getMySide(effector.getId().intValue()).stream().filter(obj -> (obj.getLogic() == LOGIC.LOGICTYPE_HERO.getValue() && !obj.isFallDown())).collect(Collectors.toList());
    int index = (fight.getRound() - 1) % wuXing.length;
    double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((20.0D + 2.2D * (effector.getRelive() * 1 + 1) * 
          PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0D));
    ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 7, 83027, 0);
    for (int i = 0; i < units.size(); i++) {
      FightObject unit = units.get(i);
      double unitWuXing = unit.getPropertyMap().getDouble(String.valueOf(wuXing[index]));
      unitWuXing = BigDecimalUtil.additionDouble(unitWuXing, percent);
      unit.getPropertyMap().setv(String.valueOf(wuXing[index]), Double.valueOf(unitWuXing));
      FightObjectParam targetParam = ActionParamUtils.addStatusForTarget(unit, 2, 2067, -1, i + 1);
      actionDetailParam.getTargetParamList().add(targetParam);
    } 
    if (actionDetailParam.getTargetParamList().size() > 0) {
      fight.getActionParam().getActionDetailParams().add(actionDetailParam);
      skillRecord.setT(((FightObjectParam)actionDetailParam.getTargetParamList().get(0)).getObjId());
    } 
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    return 1;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 0;
  }
}
