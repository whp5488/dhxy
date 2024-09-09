package com.kitty.system.game.horse.pool;

import java.util.Iterator;
import java.util.HashSet;
import com.kitty.system.enums.war.itempro.def.HORSE_SKILL_TRIGGER_DICT;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.ArrayList;
import com.kitty.system.init.service.InitService;
import com.kitty.common.utils.spring.SpringUtils;
import java.util.Set;
import com.kitty.system.init.domain.HorseSkill;
import com.kitty.common.bean.Lottery;
import java.util.List;
import com.kitty.system.init.domain.SysHorse;
import java.util.Map;

public class HorseDataPool
{
    public static int horseVitMax;
    public static int horseLevelMax;
    public static int[] horseMoveScope;
    public static int[] horseSkillProb;
    public static Map<Integer, SysHorse> horseMap;
    public static List<Lottery> horseGrowList;
    public static Map<Integer, Byte> horseSkillGradeMap;
    public static Map<Byte, Map<Integer, List<Lottery>>> commonHorseSkillMap;
    public static Map<Byte, Map<Integer, List<Lottery>>> superHorseSkillMap;
    public static Map<Integer, HorseSkill> horseSkillMap;
    public static Map<String, Set<Integer>> horseSkillSerialType;
    
    public HorseDataPool() {
    }
    
    public static void load() {
        InitService initService = SpringUtils.getInitService();
        HorseDataPool.horseMap = initService.initHorse();
        loadHorseSkill(initService);
        loadHorseGrowList();
    }
    
    public static void loadHorseGrowList() {
        List<Lottery> list = new ArrayList<>();
        list.add(new Lottery(Integer.valueOf(815), "", Double.valueOf(0.03)));
        list.add(new Lottery(Integer.valueOf(855), "", Double.valueOf(0.03)));
        list.add(new Lottery(Integer.valueOf(895), "", Double.valueOf(0.04)));
        list.add(new Lottery(Integer.valueOf(935), "", Double.valueOf(0.1)));
        list.add(new Lottery(Integer.valueOf(975), "", Double.valueOf(0.1)));
        list.add(new Lottery(Integer.valueOf(1015), "", Double.valueOf(0.15)));
        list.add(new Lottery(Integer.valueOf(1055), "", Double.valueOf(0.15)));
        list.add(new Lottery(Integer.valueOf(1095), "", Double.valueOf(0.15)));
        list.add(new Lottery(Integer.valueOf(1135), "", Double.valueOf(0.15)));
        list.add(new Lottery(Integer.valueOf(1175), "", Double.valueOf(0.1)));
        HorseDataPool.horseGrowList = list;
    }
    
    public static void loadHorseSkill(InitService initService) {
        List<HorseSkill> skills = initService.initHorseSkill();
        for (HorseSkill skill : skills) {
            byte seat = skill.getSeat();
            HorseDataPool.horseSkillGradeMap.put(Integer.valueOf(skill.getSerial()), Byte.valueOf(skill.getRarity()));
            if (skill.getType() == 0) {
                Map<Integer, List<Lottery>> map = (Map)HorseDataPool.commonHorseSkillMap.get(Byte.valueOf(seat));
                if (map == null) {
                    map = new HashMap();
                }
                map = loadHorseSkill(map, skill);
                HorseDataPool.commonHorseSkillMap.put(Byte.valueOf(seat), map);
            }
            else {
                Map<Integer, List<Lottery>> map = (Map)HorseDataPool.superHorseSkillMap.get(Byte.valueOf(seat));
                if (map == null) {
                    map = new HashMap();
                }
                map = loadHorseSkill(map, skill);
                HorseDataPool.superHorseSkillMap.put(Byte.valueOf(seat), map);
            }
        }
        HorseDataPool.horseSkillMap = (Map)skills.stream().collect(Collectors.toMap(HorseSkill::getSerial, HorseSkill -> HorseSkill, (key1, key2) -> key1));
        for (HORSE_SKILL_TRIGGER_DICT dict : HORSE_SKILL_TRIGGER_DICT.values()) {
            HorseDataPool.horseSkillSerialType.put(dict.getValue(), new HashSet());
        }
        for (HorseSkill horseSkill : HorseDataPool.horseSkillMap.values()) {
            if (horseSkill != null && horseSkill.getTriggerType() != null && HorseDataPool.horseSkillSerialType.get(horseSkill.getTriggerType()) != null) {
                ((Set)HorseDataPool.horseSkillSerialType.get(horseSkill.getTriggerType())).add(Integer.valueOf(horseSkill.getSerial()));
            }
            else {
                continue;
            }
        }
    }
    
    public static Map<Integer, List<Lottery>> loadHorseSkill(Map<Integer, List<Lottery>> map, HorseSkill skill) {
        if (skill.getSeatWeightOne() > 0.0) {
            List<Lottery> list_ont = (List)map.get(Integer.valueOf(1));
            if (list_ont == null) {
                list_ont = new ArrayList<>();
            }
            list_ont.add(new Lottery(Integer.valueOf(skill.getSerial()), "", Double.valueOf(skill.getSeatWeightOne())));
            map.put(Integer.valueOf(1), list_ont);
        }
        if (skill.getSeatWeightTow() > 0.0) {
            List<Lottery> list_two = (List)map.get(Integer.valueOf(2));
            if (list_two == null) {
                list_two = new ArrayList<>();
            }
            list_two.add(new Lottery(Integer.valueOf(skill.getSerial()), "", Double.valueOf(skill.getSeatWeightTow())));
            map.put(Integer.valueOf(2), list_two);
        }
        if (skill.getSeatWeightThree() > 0.0) {
            List<Lottery> list_three = (List)map.get(Integer.valueOf(3));
            if (list_three == null) {
                list_three = new ArrayList<>();
            }
            list_three.add(new Lottery(Integer.valueOf(skill.getSerial()), "", Double.valueOf(skill.getSeatWeightThree())));
            map.put(Integer.valueOf(3), list_three);
            return map;
        }
        else {
            return map;
        }
    }
    
    public static byte getGrowthDanLimit(short level) {
        byte limit = 2;
        if (level >= 100) {
            limit = 15;
        }
        else if (level >= 80) {
            limit = 9;
        }
        else if (level >= 60) {
            limit = 6;
        }
        else if (level >= 40) {
            limit = 4;
        }
        return limit;
    }
    
    public static short getGrowthDanLimit(int level) {
        short limit = 40;
        if (level >= 3150) {
            limit = 100;
        }
        else if (level >= 3140) {
            limit = 90;
        }
        else if (level >= 3130) {
            limit = 80;
        }
        else if (level >= 2140) {
            limit = 70;
        }
        else if (level >= 2130) {
            limit = 60;
        }
        else if (level >= 2120) {
            limit = 50;
        }
        return limit;
    }
    
    static {
        HorseDataPool.horseVitMax = 20000;
        HorseDataPool.horseLevelMax = 100;
        HorseDataPool.horseMoveScope = new int[] { 10, 30 };
        HorseDataPool.horseSkillProb = new int[] { 1, 1000, 20 };
        HorseDataPool.horseMap = new HashMap();
        HorseDataPool.horseGrowList = new ArrayList<>();
        HorseDataPool.horseSkillGradeMap = new HashMap();
        HorseDataPool.commonHorseSkillMap = new HashMap();
        HorseDataPool.superHorseSkillMap = new HashMap();
        HorseDataPool.horseSkillMap = new HashMap();
        HorseDataPool.horseSkillSerialType = new HashMap();
    }
}
