package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillTargetPicker;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class SKILL_83033 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    if (args.length > 0 && 
      args[0] instanceof com.kitty.system.init.domain.SysPetSkill) {
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 83033);
      int mana = Math.min(effector.getCurrMana() + BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(effector.getMaxMana() * 0.8D)), effector.getMaxMana());
      int change = mana - effector.getCurrMana();
      if (change <= 0)
        return null; 
      ActionParamUtils.changeMana(fight, effector, effector, 7, 83033, 2, change);
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
  
  public List<Integer> getTargetIdsForTFQX(Fight fight, FightObject effector, Object... args) {
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 83033);
    List<FightObject> targets = SkillTargetPicker.getFightObjects(fight, effector, true);
    if (targets != null && targets.size() > 0) {
      for (FightObject obj : targets) {
        if (obj.getId().equals(Integer.valueOf(effector.getTargetId()))) {
          targets.remove(obj);
          break;
        } 
      } 
      if (targets.size() > 0) {
        int length = 2;
        targets.sort(Comparator.comparing(obj -> Integer.valueOf(obj.getSpeed())));
        List<Integer> list = new ArrayList<>();
        for (FightObject target : targets) {
          list.add(target.getId());
          length--;
          if (length <= 0)
            break; 
        } 
        if (list.size() > 0) {
          skillRecord.setT(((Integer)list.get(0)).intValue());
          return list;
        } 
      } 
    } 
    return null;
  }
}
