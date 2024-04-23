package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.imbue.bean.ImbueParam;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.nutz.lang.util.NutMap;

public class SKILL_83040 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    if (!effector.isPet())
      return null; 
    double percentAC = Math.round(8.0D + 0.2D * getParam1(effector)) / 100.0D;
    double percentBD = Math.round(3.2D + 0.08D * getParam1(effector)) / 100.0D;
    int round = 5 - fight.getRound() - effector.getComeRound();
    if (round <= 0)
      return null; 
    List<FightObject> mySide = fight.getMySide(effector.getId().intValue());
    List<FightObject> zlList = (List<FightObject>)fight.getMySide(effector.getId().intValue()).stream().filter(obj -> (obj.isPet() && obj.getSerial() == 21075)).collect(Collectors.toList());
    if (zlList != null && zlList.size() > 0)
      for (FightObject obj : zlList) {
        if (!obj.getId().equals(effector.getId()))
          exclude(mySide, obj); 
      }  
    for (FightObject obj : mySide) {
      if (obj.getId().equals(effector.getId()))
        continue; 
      if (obj.getSpeed() > effector.getSpeed()) {
        ImbueUtil.addOrUpdSkill(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.BURN_HURT.getDist()), round, 83040 + effector.getId().intValue(), percentAC);
        ImbueUtil.addOrUpdSkill(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist()), round, 83040 + effector.getId().intValue(), percentBD);
        continue;
      } 
      if (obj.getSpeed() < effector.getSpeed()) {
        ImbueUtil.addOrUpdSkill(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.SH_DK.getDist()), round, 83040 + effector.getId().intValue(), percentAC);
        ImbueUtil.addOrUpdSkill(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.MAGIC_MISS_RATIO.getDist()), round, 83040 + effector.getId().intValue(), percentBD);
      } 
    } 
    return null;
  }
  
  public void death(Fight fight, FightObject effector, FightObject target, Object... args) {
    if (target.isFallDown() && target.isPet()) {
      List<FightObject> mySide = fight.getMySide(target.getId().intValue());
      boolean isDelete = exclude(mySide, target);
      if (isDelete) {
        List<FightObject> zlList = (List<FightObject>)fight.getMySide(target.getId().intValue()).stream().filter(obj -> (obj.isPet() && obj.getSerial() == 21075 && !obj.getId().equals(target.getId()))).collect(Collectors.toList());
        if (zlList != null && zlList.size() > 0) {
          Collections.sort(zlList);
          FightObject firstZl = zlList.get(0);
          passive(fight, firstZl, new Object[0]);
        } 
      } 
    } 
  }
  
  private boolean exclude(List<FightObject> mySide, FightObject target) {
    boolean isDelete = false;
    for (FightObject obj : mySide) {
      if (obj.getId().equals(target.getId()))
        continue; 
      ImbueParam imbueParamBH = ImbueUtil.gainProForSkill(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.BURN_HURT.getDist()), 83040 + target.getId().intValue(), false);
      ImbueParam imbueParamMHR = ImbueUtil.gainProForSkill(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist()), 83040 + target.getId().intValue(), false);
      ImbueParam imbueParamSD = ImbueUtil.gainProForSkill(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.SH_DK.getDist()), 83040 + target.getId().intValue(), false);
      ImbueParam imbueParamMMR = ImbueUtil.gainProForSkill(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.MAGIC_MISS_RATIO.getDist()), 83040 + target.getId().intValue(), false);
      if (imbueParamBH != null) {
        ImbueUtil.removeSkill(obj.getImbueMap(), PRO_DICT_TYPE.BURN_HURT.getDist(), 83040 + target.getId().intValue());
        isDelete = true;
      } 
      if (imbueParamMHR != null) {
        ImbueUtil.removeSkill(obj.getImbueMap(), PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist(), 83040 + target.getId().intValue());
        isDelete = true;
      } 
      if (imbueParamSD != null) {
        ImbueUtil.removeSkill(obj.getImbueMap(), PRO_DICT_TYPE.SH_DK.getDist(), 83040 + target.getId().intValue());
        isDelete = true;
      } 
      if (imbueParamMMR != null) {
        ImbueUtil.removeSkill(obj.getImbueMap(), PRO_DICT_TYPE.MAGIC_MISS_RATIO.getDist(), 83040 + target.getId().intValue());
        isDelete = true;
      } 
    } 
    return isDelete;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    return 1;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  private double getParam1(FightObject effector) {
    return (effector.getRelive() * 5 + 1) * (Math.pow(effector.getLevel(), 0.5D) / 10.0D + 
      Math.pow(effector.getClose(), 0.16666666666666666D) * 10.0D / (100 + effector.getRelive() * 20));
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 0;
  }
}
