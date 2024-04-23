package com.kitty.system.init.pool;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.init.domain.SysLevelExp;
import com.kitty.system.init.domain.SysPartyPractice;
import com.kitty.system.init.domain.SysSkillUpGradeConsume;
import com.kitty.system.init.service.InitService;
import java.util.HashMap;
import java.util.Map;

public class ExpDataPool {
  public static Map<Integer, SysLevelExp> heroLevelExpMap = new HashMap<>();
  
  public static Map<Integer, SysLevelExp> petLevelExpMap = new HashMap<>();
  
  public static Map<Integer, SysLevelExp> partnerLevelExpMap = new HashMap<>();
  
  public static Map<Integer, SysLevelExp> horseLevelExpMap = new HashMap<>();
  
  public static Map<Integer, SysSkillUpGradeConsume> skillUpGradeConsume = new HashMap<>();
  
  public static Map<Integer, SysPartyPractice> partyPractice = new HashMap<>();
  
  public static void load() {
    InitService initService = SpringUtils.getInitService();
    Map<String, Map<Integer, SysLevelExp>> levelExpMap = initService.initLevelExp();
    heroLevelExpMap = levelExpMap.get("heroMap");
    petLevelExpMap = levelExpMap.get("petMap");
    partnerLevelExpMap = levelExpMap.get("partnerMap");
    horseLevelExpMap = levelExpMap.get("horseMap");
    skillUpGradeConsume = initService.initSkillUpGradeConsume();
    partyPractice = initService.initPartyPractice();
  }
}
