package com.kitty.system.game.skill.pool;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.book.acsry.def.ACSRY_SKILL_TRIGGER_DICT;
import com.kitty.system.game.tianfu.domain.SysDayInCePractice;
import com.kitty.system.game.tianfu.domain.SysTianFuSkill;
import com.kitty.system.init.domain.SysDataAcsrySkill;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.init.service.InitService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SkillDataPool {
  public static Map<Integer, SysSkill> skillMap = new HashMap<>();
  
  public static Map<Integer, Map<Integer, Map<Integer, List<SysSkill>>>> groupSkillMap = new HashMap<>();
  
  public static Map<Integer, SysPetSkill> petSkillMap = new HashMap<>();
  
  public static Map<Integer, SysTianFuSkill> tianFuSkillMap = new HashMap<>();
  
  public static Map<Integer, SysTianFuSkill> triggerTianFuSkillMap = new HashMap<>();
  
  public static Map<Integer, SysTianFuSkill> noTriggerTianFuSkillMap = new HashMap<>();
  
  public static Map<Short, SysDayInCePractice> dayInCePracticeMap = new HashMap<>();
  
  public static Map<Integer, SysDataAcsrySkill> acsrySkillMap = new HashMap<>();
  
  public static Map<String, Set<Integer>> acsrySkillSerialType = new HashMap<>();
  
  public static void load() {
    InitService initService = SpringUtils.getInitService();
    Object[] objects = initService.initSkill();
    skillMap = (Map<Integer, SysSkill>)objects[0];
    groupSkillMap = (Map<Integer, Map<Integer, Map<Integer, List<SysSkill>>>>)objects[1];
    petSkillMap = initService.initPetSkill();
    Object[] tianFuObjects = initService.initTianFuSkill();
    tianFuSkillMap = (Map<Integer, SysTianFuSkill>)tianFuObjects[0];
    triggerTianFuSkillMap = (Map<Integer, SysTianFuSkill>)tianFuObjects[1];
    noTriggerTianFuSkillMap = (Map<Integer, SysTianFuSkill>)tianFuObjects[2];
    dayInCePracticeMap = initService.initDayInCePractice();
    acsrySkillMap = initService.initAcsrySkill();
    for (ACSRY_SKILL_TRIGGER_DICT dict : ACSRY_SKILL_TRIGGER_DICT.values())
      acsrySkillSerialType.put(dict.getValue(), new HashSet<>()); 
    for (SysDataAcsrySkill sysDataAcsrySkill : acsrySkillMap.values()) {
      if (sysDataAcsrySkill == null || sysDataAcsrySkill.getSerial() <= 0)
        continue; 
      if (sysDataAcsrySkill != null && sysDataAcsrySkill.getTriggerType() != null) {
        if (sysDataAcsrySkill.getTriggerType() == null || sysDataAcsrySkill.getTriggerType().size() <= 0)
          continue; 
        for (String triggerType : sysDataAcsrySkill.getTriggerType()) {
          if (acsrySkillSerialType.get(triggerType) != null)
            ((Set<Integer>)acsrySkillSerialType.get(triggerType)).add(Integer.valueOf(sysDataAcsrySkill.getSerial())); 
        } 
      } 
      if (sysDataAcsrySkill.getForthwithType().contains(Integer.valueOf(1)))
        ((Set<Integer>)acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.WEAR_TAKE_EFFECT.getValue())).add(Integer.valueOf(sysDataAcsrySkill.getSerial())); 
      if (sysDataAcsrySkill.getForthwithType().contains(Integer.valueOf(2)))
        ((Set<Integer>)acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.INITIALIZE_FIGHT_OBJECT.getValue())).add(Integer.valueOf(sysDataAcsrySkill.getSerial())); 
    } 
  }
}
