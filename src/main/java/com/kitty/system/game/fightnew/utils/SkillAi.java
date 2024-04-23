package com.kitty.system.game.fightnew.utils;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.MapObjectUtil;
import com.kitty.system.enums.war.itempro.def.PET_SKILL_TERM_DICT;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SkillAi {
  public static Integer[] obstacle = new Integer[] { Integer.valueOf(SKILLATTR.SKILLATTR_ICE.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_SLEEP.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_CONFUSE.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_POISON.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_YIWANG.getValue()) };
  
  public static FightObject certainStatusOrPro(Fight fight, List<Integer> status, boolean flag, int type) {
    List<FightObject> objects = new ArrayList<>();
    List<FightObject> list = unit(fight, flag);
    if (status.size() > 0)
      for (FightObject fightObject : list) {
        for (Integer k : status) {
          if (fightObject.getStatusMap().getInt(String.valueOf(k)) > 0)
            objects.add(fightObject); 
        } 
      }  
    if (objects.size() <= 0)
      objects.addAll(list); 
    List<FightObject> fightObjects = certainProMax(objects, type);
    if (fightObjects.size() > 0)
      return fightObjects.get(0); 
    if (list == null || list.size() <= 0)
      return null; 
    return list.get(0);
  }
  
  public static List<FightObject> unit(Fight fight, boolean flag) {
    List<FightObject> list = new ArrayList<>();
    if (flag) {
      list.addAll(fight.getListA());
    } else {
      list.addAll(fight.getListB());
    } 
    return list;
  }
  
  public static List<FightObject> certainProMax(List<FightObject> list, int type) {
    Map<Integer, Double> pos2Actors = new HashMap<>();
    if (type == 1) {
      for (int i = 0; i < list.size(); i++) {
        FightObject fightObject = list.get(i);
        double percentage = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Integer.valueOf(fightObject.getCurrLife() / fightObject.getMaxLife()));
        pos2Actors.put(Integer.valueOf(i), Double.valueOf(percentage));
      } 
    } else if (type == 2) {
      for (int i = 0; i < list.size(); i++) {
        FightObject fightObject = list.get(i);
        double percentage = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Integer.valueOf(fightObject.getCurrMana() / fightObject.getMaxMana()));
        pos2Actors.put(Integer.valueOf(i), Double.valueOf(percentage));
      } 
    } else if (type == 3) {
      for (int i = 0; i < list.size(); i++) {
        FightObject fightObject = list.get(i);
        double percentage = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(fightObject.getPhyPower() + fightObject.getSelfAllTempPro(PRO_DICT_TYPE.RISE_ATTACK.getDist()) * fightObject.getPhyPower()));
        pos2Actors.put(Integer.valueOf(i), Double.valueOf(percentage));
      } 
    } else {
      for (int i = 0; i < list.size(); i++) {
        FightObject fightObject = list.get(i);
        double percentage = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(fightObject.getSelfAllTempPro(PRO_DICT_TYPE.RISE_SPEED.getDist()) + fightObject.getSpeed()));
        pos2Actors.put(Integer.valueOf(i), Double.valueOf(percentage));
      } 
    } 
    List<FightObject> objects = new ArrayList<>();
    Map<Integer, Double> map = MapObjectUtil.sortByValue(pos2Actors, true);
    for (Integer key : map.keySet())
      objects.add(list.get(key.intValue())); 
    return objects;
  }
  
  public static FightObject effectProBass(Fight fight, FightObject effector, Integer type, List<String> terms, boolean isDie) {
    double down = -1.0D;
    FightObject unit = null;
    List<FightObject> list = unit(fight, effector.isASide());
    for (FightObject fightObject : list) {
      if (!fightObject.getId().equals(effector.getId())) {
        if (!isDie && terms.contains(PET_SKILL_TERM_DICT.PRO_BASS_DOWN_N.getValue()) && fightObject.isFallDown())
          continue; 
        double percentage = 0.0D;
        if (type.intValue() == 1) {
          percentage = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Integer.valueOf(fightObject.getCurrLife() / fightObject.getMaxLife()));
        } else if (type.intValue() == 2) {
          if (fightObject.getMaxMana() > 0) {
            percentage = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Integer.valueOf(fightObject.getCurrMana() / fightObject.getMaxMana()));
          } else {
            continue;
          } 
        } else {
          percentage = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Integer.valueOf(fightObject.getSpeed()));
        } 
        if (down == -1.0D) {
          down = percentage;
          unit = fightObject;
          continue;
        } 
        if (percentage < down) {
          down = percentage;
          unit = fightObject;
        } 
      } 
    } 
    return unit;
  }
  
  public static FightObject effectProBass(Fight fight, FightObject effector, Integer type, List<String> terms) {
    double down = -1.0D;
    FightObject unit = null;
    List<FightObject> list = unit(fight, effector.isASide());
    for (FightObject fightObject : list) {
      if (!fightObject.getId().equals(effector.getId())) {
        if (terms.contains(PET_SKILL_TERM_DICT.PRO_BASS_PET_N.getValue()) && fightObject.getLogic() == LOGIC.LOGICTYPE_PET.getValue())
          continue; 
        if (terms.contains(PET_SKILL_TERM_DICT.PRO_BASS_DOWN_N.getValue()) && fightObject.isFallDown())
          continue; 
        double percentage = 0.0D;
        if (type.intValue() == 1) {
          percentage = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(fightObject.getCurrLife() / (fightObject.getMaxLife() + 0.0D)));
        } else if (type.intValue() == 2) {
          if (fightObject.getMaxMana() == 0) {
            percentage = 1.0D;
          } else {
            percentage = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(fightObject.getCurrMana() / (fightObject.getMaxMana() + 0.0D)));
          } 
        } else {
          percentage = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Integer.valueOf(fightObject.getSpeed()));
        } 
        if (down == -1.0D) {
          down = percentage;
          unit = fightObject;
          continue;
        } 
        if (percentage < down) {
          down = percentage;
          unit = fightObject;
        } 
      } 
    } 
    return unit;
  }
  
  public static boolean canAffectOnPro(double pro) {
    if (pro <= 0.0D)
      return false; 
    if (pro >= 1.0D)
      return true; 
    int k = 1000000;
    int rdNumber = (int)(Math.random() * k + 1.0D);
    return (rdNumber <= pro * k);
  }
  
  public static boolean checkDoubleHit(FightObject effector) {
    String combo = String.valueOf(PRO_DICT_TYPE.COMBO_RATIO.getDist());
    double pro = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(8), effector.getPropertyMap().get(combo));
    return canAffectOnPro(pro);
  }
  
  public static boolean checkPetIsSkill(Fight fight, boolean flag, Integer skillId) {
    List<FightObject> victim = unit(fight, flag);
    for (FightObject object : victim) {
      FightObject petObject = fight.queryFightObject(Integer.valueOf(object.getFightPetId()));
      if (petObject != null) {
        RoleSkill skillBy = petObject.getSkillBy(skillId);
        if (skillBy != null)
          return true; 
      } 
    } 
    return false;
  }
  
  public static FightObject effectProBassHight(Fight fight, FightObject effector, Integer type) {
    FightObject unit = new FightObject();
    List<FightObject> list = (List<FightObject>)unit(fight, effector.isASide()).stream().filter(obj -> (!obj.getId().equals(effector.getId()) && !obj.getId().equals(Integer.valueOf(effector.getHostId())) && (obj.isPlayer() || obj.isPartner()))).collect(Collectors.toList());
    if (list.size() <= 0)
      return null; 
    if (type.intValue() == 1) {
      unit = list.stream().max(Comparator.comparing(FightObject::getMaxLife)).get();
    } else {
      unit = list.stream().max(Comparator.comparing(FightObject::getMaxMana)).get();
    } 
    return unit;
  }
  
  public static boolean checkMiss(FightObject effector, FightObject target) {
    double hitPro = effector.getSelfAllTempPro(PRO_DICT_TYPE.HIT_RATIO.getDist());
    double missPro = target.getSelfAllTempPro(PRO_DICT_TYPE.MISS_RATIO.getDist());
    double finalPro = hitPro - missPro;
    finalPro += 0.1D;
    if (finalPro <= 0.5D)
      finalPro = 0.5D; 
    return canAffectOnPro(finalPro);
  }
  
  public static boolean checkFury(FightObject effector) {
    double pro = effector.getSelfAllTempPro(PRO_DICT_TYPE.RAGE_RATIO.getDist());
    double pro_Ex = 0.0D;
    if (effector.getStatusValue(PRO_DICT_TYPE.RAGE_RATIO.getDist()) > 0)
      pro_Ex = effector.getStatusMap().getDouble(PRO_DICT_TYPE.RAGE_RATIO.getDist() + "value"); 
    return canAffectOnPro(pro + pro_Ex);
  }
}
