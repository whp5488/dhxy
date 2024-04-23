package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.service.FightMessageNewService;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.vo.utils.StatusConst;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.nutz.lang.util.NutMap;

public class SKILL_83030 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    if (21066 == effector.getSerial()) {
      FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 83030);
      List<Integer> statuss = (List<Integer>)StatusConst.getAnomalousStatus().stream().filter(status -> (status.intValue() != 1019)).collect(Collectors.toList());
      List<FightObject> mySide = (List<FightObject>)fight.getMySide(effector.getId().intValue()).stream().filter(obj -> !obj.isFallDown()).collect(Collectors.toList());
      List<FightObject> mySideObject = new ArrayList<>();
      for (FightObject fightObject : mySide) {
        for (Integer status : statuss) {
          if (fightObject.getStatusMap().getInt(String.valueOf(status)) > 0)
            mySideObject.add(fightObject); 
        } 
      } 
      int residue = mySide.size() - mySideObject.size();
      if (residue > 0) {
        List<Integer> listStatus = new ArrayList<>();
        double persent = Double.parseDouble(BigDecimalUtil.divide(String.valueOf(residue), String.valueOf(mySide.size())));
        if (persent < 0.4D)
          for (Integer status : statuss) {
            if (effector.getStatusMap().getInt(String.valueOf(status)) > 0) {
              effector.removeStatus(status.intValue());
              listStatus.add(status);
            } 
          }  
        if (listStatus.size() > 0)
          ActionParamUtils.clearStatus(fight, effector, effector, 7, 83030, 2, listStatus); 
      } 
    } 
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    return 1;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    RoleSkill skill = effector.getSkillBy(Integer.valueOf(83030));
    if (skill != null) {
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 83030);
      NutMap effectorStatus = effector.getStatusMap();
      NutMap targetStatus = target.getStatusMap();
      effector.setStatusMap(targetStatus);
      target.setStatusMap(effectorStatus);
      List<Integer> targetClearList = new ArrayList<>();
      Map<Integer, Integer> targetStatusMap = new HashMap<>();
      List<Integer> effectorClearList = new ArrayList<>();
      Map<Integer, Integer> effectorStatusMap = new HashMap<>();
      FightMessageNewService messageService = (FightMessageNewService)SpringUtils.getBean(FightMessageNewService.class);
      for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)effectorStatus.entrySet()) {
        String key = entry.getKey();
        if (!key.matches("^[0-9]+$"))
          continue; 
        Integer value = (Integer)entry.getValue();
        if (value.intValue() <= 0)
          continue; 
        effectorClearList.add(Integer.valueOf(Integer.parseInt(key)));
        targetStatusMap.put(Integer.valueOf(Integer.parseInt(key)), value);
      } 
      for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)targetStatus.entrySet()) {
        String key = entry.getKey();
        if (!key.matches("^[0-9]+$"))
          continue; 
        Integer value = (Integer)entry.getValue();
        if (value.intValue() <= 0)
          continue; 
        targetClearList.add(Integer.valueOf(Integer.parseInt(key)));
        effectorStatusMap.put(Integer.valueOf(Integer.parseInt(key)), value);
      } 
      List<FiedValue> list = new ArrayList<>();
      if (targetClearList.size() > 0)
        list.add(new FiedValue("targetClearStatus", (byte)7, targetClearList)); 
      if (targetStatusMap.size() > 0)
        list.add(new FiedValue("targetStatusMap", (byte)7, targetStatusMap)); 
      if (effectorClearList.size() > 0)
        list.add(new FiedValue("attaClearStatus", (byte)7, effectorClearList)); 
      if (effectorStatusMap.size() > 0)
        list.add(new FiedValue("attaStatusMap", (byte)7, effectorStatusMap)); 
      Effect e = new Effect();
      e.setType(2009);
      e.setTime(-1);
      e.setList(list);
      skillRecord.setT(target.getId().intValue());
      return e;
    } 
    return null;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    int level = PetSkillEffectorUtil.getExcuteLevel(args);
    return level * 32;
  }
  
  public boolean excuteSkillCool(Fight fight, FightObject effector, Object... args) {
    int skillCoolTime = 5;
    String skillS = String.valueOf(83030);
    effector.getSkillCoolMap().setv(skillS, Integer.valueOf(skillCoolTime));
    return true;
  }
}
