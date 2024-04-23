package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.vo.utils.StatusConst;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.nutz.lang.util.NutMap;

public class SKILL_83035 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 83035);
    boolean isExcute = false;
    if (fight.getRound() == 1) {
      isExcute = true;
    } else {
      int type = effector.getFightActionRecord().getInt(String.valueOf(fight.getRound() - 1));
      if (type >= 0) {
        Integer typeDetails = Integer.valueOf(effector.getFightActionRecord().getInt((fight.getRound() - 1) + "details"));
        Integer typeDetailsD = Integer.valueOf(effector.getFightActionRecord().getInt(fight.getRound() + "details"));
        if (typeDetails.equals(typeDetailsD))
          isExcute = true; 
      } 
    } 
    if (!isExcute)
      return null; 
    List<Integer> statuss = (List<Integer>)StatusConst.getAnomalousStatus().stream().filter(status -> (status.intValue() != 1019)).collect(Collectors.toList());
    List<Integer> statusList = new ArrayList<>();
    for (Integer status : statuss) {
      int time = effector.getStatusMap().getInt(String.valueOf(status));
      if (time > 0) {
        statusList.add(status);
        effector.removeStatus(status.intValue());
      } 
    } 
    if (statusList.size() > 0) {
      ActionParamUtils.clearStatus(fight, effector, effector, 7, 83035, 2, statusList);
      skillRecord.setT(effector.getId().intValue());
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
