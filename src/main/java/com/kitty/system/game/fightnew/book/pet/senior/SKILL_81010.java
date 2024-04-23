package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.ArrayList;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class SKILL_81010 extends SkillProcessor {
  private int reduce = 0;
  
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    return 1;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 81010);
    double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(8), Double.valueOf((22.1D + 4.0D * (effector.getRelive() * 0.5D + 1.0D) * 
          PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0D));
    int lastHurt = 0;
    int hurt = (int)(this.reduce * percent);
    if (!target.isFrozen()) {
      lastHurt = PetSkillEffectorUtil.computeTMFGQMXLHurt(effector, target, hurt, 81010);
      double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
      lastHurt = (int)(lastHurt + lastHurt * weak);
      skillRecord.setT(target.getId().intValue());
    } 
    Effect effect = new Effect(100000, 0, lastHurt, -1.0D, new ArrayList());
    return effect;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 0;
  }
  
  public int costLife(FightObject effector) {
    if (effector.getCurrLife() >= 10000) {
      int hp = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(effector.getCurrLife() * 0.95D));
      this.reduce = hp;
      return hp;
    } 
    return 10000;
  }
  
  public List<Integer> getTargetIds(Fight fight, FightObject effector) {
    List<Integer> list = PetTriggerPassiveSkill.getTMTargetIds(fight, effector, 81010);
    return list;
  }
  
  public void changeVariate(Object... args) {
    if (args != null && args.length > 0) {
      Object arg = args[0];
      try {
        int value = Integer.parseInt(String.valueOf(arg));
        this.reduce = value;
      } catch (Exception e) {
        e.printStackTrace();
      } 
    } 
  }
}
