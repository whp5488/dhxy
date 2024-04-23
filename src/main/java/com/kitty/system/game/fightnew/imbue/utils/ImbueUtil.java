package com.kitty.system.game.fightnew.imbue.utils;

import com.kitty.system.game.fightnew.imbue.bean.Imbue;
import com.kitty.system.game.fightnew.imbue.bean.ImbueParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImbueUtil {
  public static void addRound(Map<Integer, Imbue> imbueMap, Integer dict, int round, double value) {
    if (imbueMap != null) {
      Imbue imbue = imbueMap.get(dict);
      if (imbue == null) {
        imbue = new Imbue();
        imbueMap.put(dict, imbue);
      } 
      imbue.getRounds().add(new ImbueParam(round, value));
    } 
  }
  
  public static void addOrUpdSkill(Map<Integer, Imbue> imbueMap, Integer dict, int round, int skill, double value) {
    if (imbueMap != null) {
      Imbue imbue = imbueMap.get(dict);
      if (imbue == null) {
        imbue = new Imbue();
        imbueMap.put(dict, imbue);
      } 
      ImbueParam imbueParam = null;
      for (ImbueParam imbueParam1 : imbue.getSkills()) {
        if (String.valueOf(skill).equals(imbueParam1.getOthers())) {
          imbueParam = imbueParam1;
          break;
        } 
      } 
      if (imbueParam != null) {
        imbueParam.setValue(value);
        imbueParam.setSr(round);
      } else {
        imbue.getSkills().add(new ImbueParam(round, value, String.valueOf(skill)));
      } 
    } 
  }
  
  public static double getProValue(Map<Integer, Imbue> imbueMap, Integer dict) {
    double returnValue = 0.0D;
    Imbue imbue = imbueMap.get(dict);
    if (imbue != null) {
      for (ImbueParam imbueParam : imbue.getStatus()) {
        if (imbueParam.getSr() > 0)
          returnValue += imbueParam.getValue(); 
      } 
      for (ImbueParam imbueParam : imbue.getSkills()) {
        if (imbueParam.getSr() > 0)
          returnValue += imbueParam.getValue(); 
      } 
      for (ImbueParam imbueParam : imbue.getRounds()) {
        if (imbueParam.getSr() > 0)
          returnValue += imbueParam.getValue(); 
      } 
      if (imbue.getTemp() != 0.0D)
        returnValue += imbue.getTemp(); 
    } 
    return returnValue;
  }
  
  public static ImbueParam gainProForSkill(Map<Integer, Imbue> imbueMap, Integer dict, int skill, boolean isCreate) {
    Imbue imbue = imbueMap.get(dict);
    ImbueParam param = null;
    if (imbue != null) {
      for (ImbueParam imbueParam : imbue.getSkills()) {
        if (String.valueOf(skill).equals(imbueParam.getOthers())) {
          param = imbueParam;
          break;
        } 
      } 
    } else {
      imbue = new Imbue();
      imbueMap.put(dict, imbue);
    } 
    if (param == null && isCreate) {
      param = new ImbueParam();
      param.setOthers(String.valueOf(skill));
      imbue.getSkills().add(param);
    } 
    return param;
  }
  
  public static int getProRound(Map<Integer, Imbue> imbueMap, Integer dict) {
    if (imbueMap != null) {
      Imbue imbue = imbueMap.get(dict);
      if (imbue != null) {
        int round = 0;
        for (ImbueParam imbueParam : imbue.getRounds()) {
          if (imbueParam.getSr() > round)
            round = imbueParam.getSr(); 
        } 
        return round;
      } 
    } 
    return 0;
  }
  
  public static void addOrUpdStatus(Map<Integer, Imbue> imbueMap, int status, int dict, int round, double value) {
    if (imbueMap != null) {
      Imbue imbue = imbueMap.get(Integer.valueOf(dict));
      if (imbue == null) {
        imbue = new Imbue();
        imbueMap.put(Integer.valueOf(dict), imbue);
      } 
      ImbueParam imbueP = null;
      for (ImbueParam imbueParam : imbue.getStatus()) {
        if (String.valueOf(status).equals(imbueParam.getOthers())) {
          imbueP = imbueParam;
          break;
        } 
      } 
      if (imbueP == null) {
        imbueP = new ImbueParam();
        imbue.getStatus().add(imbueP);
      } 
      imbueP.setSr(round);
      imbueP.setValue(value);
      imbueP.setOthers(String.valueOf(status));
    } 
  }
  
  public static void addTemp(Map<Integer, Imbue> imbueMap, int dict, double value) {
    if (imbueMap != null) {
      Imbue imbue = imbueMap.get(Integer.valueOf(dict));
      if (imbue == null) {
        imbue = new Imbue();
        imbueMap.put(Integer.valueOf(dict), imbue);
      } 
      imbue.setTemp(imbue.getTemp() + value);
    } 
  }
  
  public static void clearTemps(Map<Integer, Imbue> imbueMap) {
    if (imbueMap == null || imbueMap.size() <= 0)
      return; 
    for (Map.Entry<Integer, Imbue> entry : imbueMap.entrySet()) {
      Imbue imbue = entry.getValue();
      if (imbue != null && 
        imbue.getTemp() != 0.0D)
        imbue.setTemp(0.0D); 
    } 
  }
  
  public static ImbueParam getStatusValue(Map<Integer, Imbue> imbueMap, int status, int dict, int round) {
    if (imbueMap != null) {
      Imbue imbue = imbueMap.get(Integer.valueOf(dict));
      if (imbue != null) {
        ImbueParam value = null;
        for (int i = imbue.getStatus().size() - 1; i <= 0; i--) {
          if (String.valueOf(status).equals(((ImbueParam)imbue.getStatus().get(i)).getOthers()) && round == ((ImbueParam)imbue.getStatus().get(i)).getSr()) {
            value = imbue.getStatus().get(i);
            break;
          } 
        } 
        return value;
      } 
    } 
    return null;
  }
  
  public static ImbueParam getStatusValue(Map<Integer, Imbue> imbueMap, int status, int dict) {
    if (imbueMap != null) {
      Imbue imbue = imbueMap.get(Integer.valueOf(dict));
      if (imbue != null) {
        ImbueParam value = null;
        for (int i = imbue.getStatus().size() - 1; i <= 0; i--) {
          if (String.valueOf(status).equals(((ImbueParam)imbue.getStatus().get(i)).getOthers()) && ((ImbueParam)imbue.getStatus().get(i)).getSr() > 0) {
            value = imbue.getStatus().get(i);
            break;
          } 
        } 
        return value;
      } 
    } 
    return null;
  }
  
  public static ImbueParam gainStatusValue(Map<Integer, Imbue> imbueMap, int status, int dict) {
    Imbue imbue = imbueMap.get(Integer.valueOf(dict));
    ImbueParam value = null;
    if (imbue != null) {
      for (int i = imbue.getStatus().size() - 1; i <= 0; i--) {
        if (String.valueOf(status).equals(((ImbueParam)imbue.getStatus().get(i)).getOthers())) {
          value = imbue.getStatus().get(i);
          break;
        } 
      } 
    } else {
      imbue = new Imbue();
      imbueMap.put(Integer.valueOf(dict), imbue);
    } 
    if (value == null) {
      value = new ImbueParam();
      value.setOthers(String.valueOf(status));
      imbue.getStatus().add(value);
    } 
    return value;
  }
  
  public static void reduceImbueRounds(Map<Integer, Imbue> imbueMap) {
    if (imbueMap != null && imbueMap.size() > 0) {
      List<Integer> delMap = new ArrayList<>();
      for (Map.Entry<Integer, Imbue> entry : imbueMap.entrySet()) {
        Imbue imbue = entry.getValue();
        reduce(imbue.getStatus());
        reduce(imbue.getSkills());
        if (imbue.getStatus().size() <= 0 && imbue.getRounds().size() <= 0 && imbue.getSkills().size() <= 0)
          delMap.add(entry.getKey()); 
      } 
      for (Integer key : delMap)
        imbueMap.remove(key); 
    } 
  }
  
  private static void reduce(List<ImbueParam> imbueParams) {
    List<ImbueParam> delList = new ArrayList<>();
    for (ImbueParam imbueParam : imbueParams) {
      if (imbueParam.getSr() > 0)
        imbueParam.setSr(imbueParam.getSr() - 1); 
      if (imbueParam.getSr() <= 0)
        delList.add(imbueParam); 
    } 
    if (delList.size() > 0)
      for (ImbueParam imbueParam : delList)
        imbueParams.remove(imbueParam);  
  }
  
  public static void removePro(Map<Integer, Imbue> imbueMap, int dict) {
    if (imbueMap != null)
      imbueMap.remove(Integer.valueOf(dict)); 
  }
  
  public static void removeSkill(Map<Integer, Imbue> imbueMap, int dict, int skill) {
    if (imbueMap != null) {
      Imbue imbue = imbueMap.get(Integer.valueOf(dict));
      if (imbue != null)
        for (ImbueParam imbueParam : imbue.getSkills()) {
          if (String.valueOf(skill).equals(imbueParam.getOthers())) {
            imbue.getSkills().remove(imbueParam);
            break;
          } 
        }  
    } 
  }
  
  public static void removeStatus(Map<Integer, Imbue> imbueMap, int dict, int status) {
    if (imbueMap != null) {
      Imbue imbue = imbueMap.get(Integer.valueOf(dict));
      if (imbue != null) {
        List<ImbueParam> removeList = new ArrayList<>();
        for (ImbueParam imbueParam : imbue.getStatus()) {
          if (String.valueOf(status).equals(imbueParam.getOthers()))
            removeList.add(imbueParam); 
        } 
        for (ImbueParam imbueParam : removeList)
          imbue.getStatus().remove(imbueParam); 
      } 
    } 
  }
}
