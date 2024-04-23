package com.kitty.system.game.fightnew.book.pet.supreme;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.vo.utils.StatusConst;
import java.util.List;
import java.util.stream.Collectors;
import org.nutz.lang.util.NutMap;

public class SKILL_82001 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 82001);
    List<FightObject> myFightObject = null;
    if (effector.isASide()) {
      myFightObject = (List<FightObject>)fight.getListA().stream().filter(obj -> (!obj.getId().equals(effector.getId()) && !obj.isFallDown() && (obj.isPet() || obj.isPartner()))).collect(Collectors.toList());
    } else {
      myFightObject = (List<FightObject>)fight.getListB().stream().filter(obj -> (!obj.getId().equals(effector.getId()) && !obj.isFallDown() && (obj.isPet() || obj.isPartner()))).collect(Collectors.toList());
    } 
    ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 7, 82001, 0);
    for (int i = 0; i < myFightObject.size(); i++) {
      FightObject obj = myFightObject.get(i);
      List<Integer> clearStatusList = obj.getStatusList(StatusConst.getAnomalousStatus());
      if (clearStatusList != null) {
        obj.clearStatus(clearStatusList);
        FightObjectParam targetParam = ActionParamUtils.clearStatusForTarget(obj.getId().intValue(), i + 1, clearStatusList, 2);
        actionDetailParam.getTargetParamList().add(targetParam);
      } 
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
