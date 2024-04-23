package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.vo.utils.StatusConst;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class SKILL_81054 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 81054);
    boolean excuteControlleSkill = PetPassiveSkillUtil.isExcuteControlleSkill(effector, 3);
    if (!excuteControlleSkill)
      return null; 
    List<Integer> clearStatusList = effector.getStatusList(StatusConst.getAnomalousStatus());
    if (clearStatusList != null) {
      effector.clearStatus(clearStatusList);
      ActionParamUtils.clearStatus(fight, effector, effector, 7, 81054, 2, clearStatusList);
    } 
    skillRecord.setT(effector.getId().intValue());
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    return 0;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 0;
  }
}
