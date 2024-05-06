package com.kitty.system.game.pet.pool;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.enums.war.warskill.petskilldef.DOGZ_PETSKILL;
import com.kitty.system.enums.war.warskill.petskilldef.GENERAL_PETSKILL;
import com.kitty.system.enums.war.warskill.petskilldef.HIGH_PETSKILL;
import com.kitty.system.enums.war.warskill.petskilldef.ULTIMATE_PETSKILL;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.init.domain.vo.PetAccColor;
import com.kitty.system.init.service.InitService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PetDataPool {
  public static List<Integer> ACTIVE_PETSKILLLIST = new ArrayList<>();
  
  public static Map<Integer, SysPet> petMap = new HashMap<>();
  
  public static Map<String, Map<Byte, PetAccColor>> petAccColorMap = new HashMap<>();
  
  public static void load() {
    InitService initService = SpringUtils.getInitService();
    initActivePetSkill();
    initPet(initService);
    initPetAccColorMap(initService);
  }
  
  public static void initActivePetSkill() {
    List<Map<String, Integer>> dogz_maps = DOGZ_PETSKILL.toList();
    for (Map<String, Integer> map : dogz_maps) {
      Integer type = map.get("type");
      if (type == 1)
        ACTIVE_PETSKILLLIST.add(map.get("value")); 
    } 
    List<Map<String, Integer>> generl_maps = GENERAL_PETSKILL.toList();
    for (Map<String, Integer> map : generl_maps) {
      Integer type = map.get("type");
      if (type == 1)
        ACTIVE_PETSKILLLIST.add(map.get("value")); 
    } 
    List<Map<String, Integer>> high_maps = HIGH_PETSKILL.toList();
    for (Map<String, Integer> map : high_maps) {
      Integer type = map.get("type");
      if (type == 1)
        ACTIVE_PETSKILLLIST.add(map.get("value")); 
    } 
    List<Map<String, Integer>> ultimate_maps = ULTIMATE_PETSKILL.toList();
    for (Map<String, Integer> map : ultimate_maps) {
      Integer type = map.get("type");
      if (type == 1)
        ACTIVE_PETSKILLLIST.add(map.get("value")); 
    } 
  }
  
  public static void initPet(InitService initService) {
    petMap = initService.initPet();
  }
  
  public static void initPetAccColorMap(InitService initService) {
    petAccColorMap = initService.initPetAccColor();
  }
}
