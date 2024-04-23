package com.kitty.system.game.horse.pool;

import com.kitty.common.bean.Lottery;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.enums.war.itempro.def.HORSE_SKILL_TRIGGER_DICT;
import com.kitty.system.init.domain.HorseSkill;
import com.kitty.system.init.domain.SysHorse;
import com.kitty.system.init.service.InitService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class HorseDataPool {
  public static int horseVitMax = 20000;
  
  public static int horseLevelMax = 100;
  
  public static int[] horseMoveScope = new int[] { 10, 30 };
  
  public static int[] horseSkillProb = new int[] { 1, 1000, 20 };
  
  public static Map<Integer, SysHorse> horseMap = new HashMap<>();
  
  public static List<Lottery> horseGrowList = new ArrayList<>();
  
  public static Map<Integer, Byte> horseSkillGradeMap = new HashMap<>();
  
  public static Map<Byte, Map<Integer, List<Lottery>>> commonHorseSkillMap = new HashMap<>();
  
  public static Map<Byte, Map<Integer, List<Lottery>>> superHorseSkillMap = new HashMap<>();
  
  public static Map<Integer, HorseSkill> horseSkillMap = new HashMap<>();
  
  public static Map<String, Set<Integer>> horseSkillSerialType = new HashMap<>();
  
  public static void load() {
    InitService initService = SpringUtils.getInitService();
    horseMap = initService.initHorse();
    loadHorseSkill(initService);
    loadHorseGrowList();
  }
  
  public static void loadHorseGrowList() {
    List<Lottery> list = new ArrayList<>();
    list.add(new Lottery(Integer.valueOf(815), "", Double.valueOf(0.03D)));
    list.add(new Lottery(Integer.valueOf(855), "", Double.valueOf(0.03D)));
    list.add(new Lottery(Integer.valueOf(895), "", Double.valueOf(0.04D)));
    list.add(new Lottery(Integer.valueOf(935), "", Double.valueOf(0.1D)));
    list.add(new Lottery(Integer.valueOf(975), "", Double.valueOf(0.1D)));
    list.add(new Lottery(Integer.valueOf(1015), "", Double.valueOf(0.15D)));
    list.add(new Lottery(Integer.valueOf(1055), "", Double.valueOf(0.15D)));
    list.add(new Lottery(Integer.valueOf(1095), "", Double.valueOf(0.15D)));
    list.add(new Lottery(Integer.valueOf(1135), "", Double.valueOf(0.15D)));
    list.add(new Lottery(Integer.valueOf(1175), "", Double.valueOf(0.1D)));
    horseGrowList = list;
  }
  
  public static void loadHorseSkill(InitService initService) {
    List<HorseSkill> skills = initService.initHorseSkill();
    for (HorseSkill skill : skills) {
      byte seat = skill.getSeat();
      horseSkillGradeMap.put(Integer.valueOf(skill.getSerial()), Byte.valueOf(skill.getRarity()));
      if (skill.getType() == 0) {
        Map<Integer, List<Lottery>> map1 = commonHorseSkillMap.get(Byte.valueOf(seat));
        if (map1 == null)
          map1 = new HashMap<>(); 
        map1 = loadHorseSkill(map1, skill);
        commonHorseSkillMap.put(Byte.valueOf(seat), map1);
        continue;
      } 
      Map<Integer, List<Lottery>> map = superHorseSkillMap.get(Byte.valueOf(seat));
      if (map == null)
        map = new HashMap<>(); 
      map = loadHorseSkill(map, skill);
      superHorseSkillMap.put(Byte.valueOf(seat), map);
    } 
    horseSkillMap = (Map<Integer, HorseSkill>)skills.stream().collect(Collectors.toMap(HorseSkill::getSerial, horseSkill -> horseSkill, (key1, key2) -> key1));
    for (HORSE_SKILL_TRIGGER_DICT dict : HORSE_SKILL_TRIGGER_DICT.values())
      horseSkillSerialType.put(dict.getValue(), new HashSet<>()); 
    for (HorseSkill horseSkill : horseSkillMap.values()) {
      if (horseSkill != null && horseSkill.getTriggerType() != null && horseSkillSerialType.get(horseSkill.getTriggerType()) != null)
        ((Set<Integer>)horseSkillSerialType.get(horseSkill.getTriggerType())).add(Integer.valueOf(horseSkill.getSerial())); 
    } 
  }
  
  public static Map<Integer, List<Lottery>> loadHorseSkill(Map<Integer, List<Lottery>> map, HorseSkill skill) {
    if (skill.getSeatWeightOne() > 0.0D) {
      List<Lottery> list_ont = map.get(Integer.valueOf(1));
      if (list_ont == null)
        list_ont = new ArrayList<>(); 
      list_ont.add(new Lottery(Integer.valueOf(skill.getSerial()), "", Double.valueOf(skill.getSeatWeightOne())));
      map.put(Integer.valueOf(1), list_ont);
    } 
    if (skill.getSeatWeightTow() > 0.0D) {
      List<Lottery> list_two = map.get(Integer.valueOf(2));
      if (list_two == null)
        list_two = new ArrayList<>(); 
      list_two.add(new Lottery(Integer.valueOf(skill.getSerial()), "", Double.valueOf(skill.getSeatWeightTow())));
      map.put(Integer.valueOf(2), list_two);
    } 
    if (skill.getSeatWeightThree() > 0.0D) {
      List<Lottery> list_three = map.get(Integer.valueOf(3));
      if (list_three == null)
        list_three = new ArrayList<>(); 
      list_three.add(new Lottery(Integer.valueOf(skill.getSerial()), "", Double.valueOf(skill.getSeatWeightThree())));
      map.put(Integer.valueOf(3), list_three);
    } 
    return map;
  }
  
  public static byte getGrowthDanLimit(short level) {
    byte limit = 2;
    if (level >= 100) {
      limit = 15;
    } else if (level >= 80) {
      limit = 9;
    } else if (level >= 60) {
      limit = 6;
    } else if (level >= 40) {
      limit = 4;
    } 
    return limit;
  }
  
  public static short getGrowthDanLimit(int level) {
    short limit = 40;
    if (level >= 3150) {
      limit = 100;
    } else if (level >= 3140) {
      limit = 90;
    } else if (level >= 3130) {
      limit = 80;
    } else if (level >= 2140) {
      limit = 70;
    } else if (level >= 2130) {
      limit = 60;
    } else if (level >= 2120) {
      limit = 50;
    } 
    return limit;
  }
}
