
package com.kitty.system.game.horse.pool;

import java.util.Iterator;
import java.util.HashSet;
import com.kitty.system.enums.war.itempro.def.HORSE_SKILL_TRIGGER_DICT;
import java.util.function.Function;
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
    
    public static void load() {
        final InitService initService = SpringUtils.getInitService();
        HorseDataPool.horseMap = initService.initHorse();
        loadHorseSkill(initService);
        loadHorseGrowList();
    }
    
    public static void loadHorseGrowList() {
        final List<Lottery> list = new ArrayList<Lottery>();
        list.add(new Lottery(815, "", 0.03));
        list.add(new Lottery(855, "", 0.03));
        list.add(new Lottery(895, "", 0.04));
        list.add(new Lottery(935, "", 0.1));
        list.add(new Lottery(975, "", 0.1));
        list.add(new Lottery(1015, "", 0.15));
        list.add(new Lottery(1055, "", 0.15));
        list.add(new Lottery(1095, "", 0.15));
        list.add(new Lottery(1135, "", 0.15));
        list.add(new Lottery(1175, "", 0.1));
        HorseDataPool.horseGrowList = list;
    }
    
    public static void loadHorseSkill(final InitService initService) {
        final List<HorseSkill> skills = initService.initHorseSkill();
        for (final HorseSkill skill : skills) {
            final byte seat = skill.getSeat();
            HorseDataPool.horseSkillGradeMap.put(skill.getSerial(), skill.getRarity());
            if (skill.getType() == 0) {
                Map<Integer, List<Lottery>> map = HorseDataPool.commonHorseSkillMap.get(seat);
                if (map == null) {
                    map = new HashMap<Integer, List<Lottery>>();
                }
                map = loadHorseSkill(map, skill);
                HorseDataPool.commonHorseSkillMap.put(seat, map);
            }
            else {
                Map<Integer, List<Lottery>> map = HorseDataPool.superHorseSkillMap.get(seat);
                if (map == null) {
                    map = new HashMap<Integer, List<Lottery>>();
                }
                map = loadHorseSkill(map, skill);
                HorseDataPool.superHorseSkillMap.put(seat, map);
            }
        }
        HorseDataPool.horseSkillMap = skills.stream().collect(Collectors.toMap(HorseSkill::getSerial, HorseSkill -> HorseSkill, (key1, key2) -> key1));
        for (final HORSE_SKILL_TRIGGER_DICT dict : HORSE_SKILL_TRIGGER_DICT.values()) {
            HorseDataPool.horseSkillSerialType.put(dict.getValue(), new HashSet<Integer>());
        }
        for (final HorseSkill horseSkill : HorseDataPool.horseSkillMap.values()) {
            if (horseSkill != null && horseSkill.getTriggerType() != null && HorseDataPool.horseSkillSerialType.get(horseSkill.getTriggerType()) != null) {
                HorseDataPool.horseSkillSerialType.get(horseSkill.getTriggerType()).add(horseSkill.getSerial());
            }
        }
    }
    
    public static Map<Integer, List<Lottery>> loadHorseSkill(final Map<Integer, List<Lottery>> map, final HorseSkill skill) {
        if (skill.getSeatWeightOne() > 0.0) {
            List<Lottery> list_ont = map.get(1);
            if (list_ont == null) {
                list_ont = new ArrayList<Lottery>();
            }
            list_ont.add(new Lottery(skill.getSerial(), "", skill.getSeatWeightOne()));
            map.put(1, list_ont);
        }
        if (skill.getSeatWeightTow() > 0.0) {
            List<Lottery> list_two = map.get(2);
            if (list_two == null) {
                list_two = new ArrayList<Lottery>();
            }
            list_two.add(new Lottery(skill.getSerial(), "", skill.getSeatWeightTow()));
            map.put(2, list_two);
        }
        if (skill.getSeatWeightThree() > 0.0) {
            List<Lottery> list_three = map.get(3);
            if (list_three == null) {
                list_three = new ArrayList<Lottery>();
            }
            list_three.add(new Lottery(skill.getSerial(), "", skill.getSeatWeightThree()));
            map.put(3, list_three);
        }
        return map;
    }
    
    public static byte getGrowthDanLimit(final short level) {
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
    
    public static short getGrowthDanLimit(final int level) {
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
        HorseDataPool.horseMap = new HashMap<Integer, SysHorse>();
        HorseDataPool.horseGrowList = new ArrayList<Lottery>();
        HorseDataPool.horseSkillGradeMap = new HashMap<Integer, Byte>();
        HorseDataPool.commonHorseSkillMap = new HashMap<Byte, Map<Integer, List<Lottery>>>();
        HorseDataPool.superHorseSkillMap = new HashMap<Byte, Map<Integer, List<Lottery>>>();
        HorseDataPool.horseSkillMap = new HashMap<Integer, HorseSkill>();
        HorseDataPool.horseSkillSerialType = new HashMap<String, Set<Integer>>();
    }
}
