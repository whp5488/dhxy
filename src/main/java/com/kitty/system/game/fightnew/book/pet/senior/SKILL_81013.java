package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.common.enter.FiedValue;
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
import java.util.List;
import org.nutz.lang.util.NutMap;

public class SKILL_81013 extends SkillProcessor {
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
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 81013);
    int lastHurt = 0;
    double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(8), Double.valueOf((15.1D + 3.0D * (effector.getRelive() * 0.4D + 1.0D) * 
          PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0D));
    int hurt = (int)(this.reduce * percent);
    if (!target.isFrozen()) {
      lastHurt = PetSkillEffectorUtil.computeTMFGQMXLHurt(effector, target, hurt, 81013);
      double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
      lastHurt = (int)(lastHurt + lastHurt * weak);
      skillRecord.setT(target.getId().intValue());
    } 
    Effect effect = new Effect(100000, 0);
    List<FiedValue> list = effect.getOrCreateList();
    list.add(new FiedValue("mana", (byte)3, Integer.valueOf(lastHurt)));
    return effect;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    if (args != null && args.length > 0) {
      Object arg = args[0];
      try {
        FightObject effector = (FightObject)arg;
        if (effector != null && effector.getCurrMana() >= 10000) {
          int hp = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(effector.getCurrMana() * 0.95D));
          this.reduce = hp;
          return hp;
        } 
      } catch (Exception e) {
        e.printStackTrace();
      } 
    } 
    return 10000;
  }
  
  public List<Integer> getTargetIds(Fight fight, FightObject effector) {
    List<Integer> list = PetTriggerPassiveSkill.getTMTargetIds(fight, effector, 80009);
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
