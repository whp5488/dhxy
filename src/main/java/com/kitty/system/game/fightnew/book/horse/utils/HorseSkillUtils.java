package com.kitty.system.game.fightnew.book.horse.utils;

import com.kitty.system.enums.war.warskill.skilldef.PETSKILLATTR;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.init.domain.HorseSkill;
import java.util.Collection;
import com.kitty.system.enums.war.itempro.def.HORSE_SKILL_TRIGGER_DICT;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import java.util.HashSet;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.horse.pool.HorseDataPool;
import java.util.Set;
import java.util.Iterator;
import com.kitty.listener.event.ranking.PetChangeEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.EventDispatcher;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.pet.service.PetService;
import java.util.IdentityHashMap;
import com.kitty.system.game.horse.domain.Horse;
import com.kitty.mina.cache.DataCache;
import java.util.Map;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;

public class HorseSkillUtils
{
    public HorseSkillUtils() {
    }
    
    public static void studyFinish(Role role, long horseId, Pet pet) {
        if (role == null) {
            return;
        }
        Map<Long, Horse> horseMap = (Map)DataCache.HORSE_MAP.get(role.getId());
        if (horseMap == null || horseMap.size() <= 0) {
            return;
        }
        Horse horse = (Horse)horseMap.get(Long.valueOf(horseId));
        if (horse == null) {
            return;
        }
        IdentityHashMap<Integer, Long> skill = horse.getSkill();
        if (skill == null || skill.size() <= 0) {
            return;
        }
        if (pet == null) {
            excuteAll(role, horseId);
        }
        else {
            excuteOne((long)role.getId(), horseId, pet);
        }
        return;
    }
    
    private static void excuteOne(long id, long horseId, Pet pet) {
        if (pet.getHorse() == horseId) {
            ((PetService)SpringUtils.getBean(PetService.class)).calculateBase(id, pet);
        }
    }
    
    private static void excuteAll(Role role, long horseId) {
        PetService petService = (PetService)SpringUtils.getBean(PetService.class);
        Map<Integer, Pet> petMap = petService.selectPet(role);
        if (petMap != null && petMap.size() > 0) {
            for (Pet pet : petMap.values()) {
                if (pet.getHorse() == horseId) {
                    petService.calculateBase((long)role.getId(), pet);
                    EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_CHANGE, pet, petService.getPetEffective((long)role.getId(), pet)));
                }
                else {
                    continue;
                }
            }
        }
    }
    
    public static int getHorseSkillProperty(Pet pet, int propertyDist, String horseSkillTriggerType) {
        if (pet.getHorse() == 0L) {
            return 0;
        }
        Role role = SpringUtils.getRoleService().getPlayer((int)pet.getRoleId());
        if (role == null) {
            return 0;
        }
        Map<Long, Horse> roleHorseMap = (Map)DataCache.HORSE_MAP.get(role.getId());
        if (roleHorseMap == null) {
            return 0;
        }
        Horse horse = (Horse)roleHorseMap.get(Long.valueOf(pet.getHorse()));
        if (horse == null) {
            return 0;
        }
        IdentityHashMap<Integer, Long> skillMap = horse.getSkill();
        if (skillMap == null || skillMap.size() <= 0) {
            return 0;
        }
        Set<Integer> serialSet = (Set)HorseDataPool.horseSkillSerialType.get(horseSkillTriggerType);
        int addProperty = 0;
        for (Map.Entry<Integer, Long> entry : skillMap.entrySet()) {
            Integer key = (Integer)entry.getKey();
            if (!serialSet.contains(key)) {
                continue;
            }
            HorseSkillParam horseSkillParam = new HorseSkillParam((int)key, (long)entry.getValue(), horse.getGrowUp(), horse.getLevel());
            HorseSkillProcessor processor = HorseSkillProcessor.queryProcessor(key);
            if (processor == null) {
                continue;
            }
            NutMap nutMap = processor.studyExcute(pet, horseSkillParam, new Object[0]);
            if (nutMap != null && nutMap.size() > 0 && nutMap.getInt(String.valueOf(propertyDist), 0) != 0) {
                addProperty += nutMap.getInt(String.valueOf(propertyDist), 0);
            }
            else {
                continue;
            }
        }
        return addProperty;
    }
    
    public static double getHorseSkillPropertyPct(Pet pet, int propertyPctDist, String horseSkillTriggerType) {
        if (pet.getHorse() == 0L) {
            return 0.0;
        }
        Role role = SpringUtils.getRoleService().getOnlinePlayer((int)pet.getRoleId());
        if (role == null) {
            return 0.0;
        }
        Map<Long, Horse> roleHorseMap = (Map)DataCache.HORSE_MAP.get(role.getId());
        if (roleHorseMap == null) {
            return 0.0;
        }
        Horse horse = (Horse)roleHorseMap.get(Long.valueOf(pet.getHorse()));
        if (horse == null) {
            return 0.0;
        }
        IdentityHashMap<Integer, Long> skillMap = horse.getSkill();
        if (skillMap == null || skillMap.size() <= 0) {
            return 0.0;
        }
        Set<Integer> serialSet = (Set)HorseDataPool.horseSkillSerialType.get(horseSkillTriggerType);
        Set<Integer> others = new HashSet();
        if (PRO_DICT_TYPE.SPEED_PCT.getDist() == propertyPctDist) {
            for (Map.Entry<String, Set<Integer>> entry : HorseDataPool.horseSkillSerialType.entrySet()) {
                if (horseSkillTriggerType.equals(entry.getKey())) {
                    continue;
                }
                if (HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH_LAST.getValue().equals(entry.getKey())) {
                    continue;
                }
                others.addAll((Collection)entry.getValue());
            }
        }
        double addProperty = 0.0;
        for (Map.Entry<Integer, Long> entry2 : skillMap.entrySet()) {
            Integer key = (Integer)entry2.getKey();
            if (!serialSet.contains(key) && !others.contains(key)) {
                continue;
            }
            addProperty += excuteSkill(pet, horse, propertyPctDist, key, (Long)entry2.getValue());
        }
        return addProperty;
    }
    
    private static double excuteSkill(Pet pet, Horse horse, int propertyPctDist, Integer key, Long value) {
        HorseSkillParam horseSkillParam = new HorseSkillParam((int)key, (long)value, horse.getGrowUp(), horse.getLevel());
        HorseSkillProcessor processor = HorseSkillProcessor.queryProcessor(key);
        if (processor == null) {
            return 0.0;
        }
        double addProperty = 0.0;
        NutMap nutMap = processor.studyExcute(pet, horseSkillParam, new Object[0]);
        if (nutMap != null && nutMap.size() > 0 && nutMap.getDouble(String.valueOf(propertyPctDist), 0.0) != 0.0) {
            addProperty += nutMap.getDouble(String.valueOf(propertyPctDist));
        }
        return addProperty;
    }
    
    public static void countHorseSkillGain(NutMap propertyMap, Map<Integer, HorseSkillParam> horseSkillParamMap) {
        if (horseSkillParamMap == null || horseSkillParamMap.size() <= 0) {
            return;
        }
        for (HorseSkillParam horseSkillParam : horseSkillParamMap.values()) {
            HorseSkill horseSkill = (HorseSkill)HorseDataPool.horseSkillMap.get(Integer.valueOf(horseSkillParam.getSerial()));
            if (horseSkill == null) {
                continue;
            }
            if (!horseSkill.isFightInitializeLoad()) {
                continue;
            }
            HorseSkillProcessor processor = HorseSkillProcessor.queryProcessor(Integer.valueOf(horseSkillParam.getSerial()));
            if (processor == null) {
                continue;
            }
            NutMap nutMap = processor.addition(horseSkillParam, new Object[0]);
            if (nutMap != null && nutMap.size() > 0) {
                for (Map.Entry<String, Object> entry : nutMap.entrySet()) {
                    String key = (String)entry.getKey();
                    propertyMap.setv(key, Double.valueOf(propertyMap.getDouble(key, 0.0) + nutMap.getDouble(key, 0.0)));
                }
            }
            else {
                continue;
            }
        }
        return;
    }
    
    public static void beforeMagicHurt(Fight fight, FightObject effector, RoleSkill skill) {
        if (!effector.isPet()) {
            return;
        }
        Set<Integer> serialSet = (Set)HorseDataPool.horseSkillSerialType.get(HORSE_SKILL_TRIGGER_DICT.BEFORE_MAGIC_HURT.getValue());
        Map<Integer, HorseSkillParam> horseSkillMap = ((PetFightObject)effector).getHorseSkillMap();
        for (HorseSkillParam horseSkillParam : horseSkillMap.values()) {
            if (serialSet.contains(Integer.valueOf(horseSkillParam.getSerial()))) {
                HorseSkillProcessor processor = HorseSkillProcessor.queryProcessor(Integer.valueOf(horseSkillParam.getSerial()));
                if (processor == null) {
                    continue;
                }
                processor.passive(fight, (PetFightObject)effector, null, skill);
            }
            else {
                continue;
            }
        }
        return;
    }
    
    public static void targetHits(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam) {
        if (effector.isPet()) {
            Set<Integer> serialSet = (Set)HorseDataPool.horseSkillSerialType.get(HORSE_SKILL_TRIGGER_DICT.TARGET_HITS.getValue());
            Map<Integer, HorseSkillParam> horseSkillMap = ((PetFightObject)effector).getHorseSkillMap();
            for (HorseSkillParam horseSkillParam : horseSkillMap.values()) {
                if (serialSet.contains(Integer.valueOf(horseSkillParam.getSerial()))) {
                    HorseSkillProcessor processor = HorseSkillProcessor.queryProcessor(Integer.valueOf(horseSkillParam.getSerial()));
                    if (processor == null) {
                        continue;
                    }
                    processor.attackOver(fight, (PetFightObject)effector, target, actionDetailParam, new Object[0]);
                }
                else {
                    continue;
                }
            }
        }
        else if (target.isPet()) {
            Set<Integer> serialSet = (Set)HorseDataPool.horseSkillSerialType.get(HORSE_SKILL_TRIGGER_DICT.BY_TARGET_HITS.getValue());
            Map<Integer, HorseSkillParam> horseSkillMap = ((PetFightObject)target).getHorseSkillMap();
            for (HorseSkillParam horseSkillParam : horseSkillMap.values()) {
                if (serialSet.contains(Integer.valueOf(horseSkillParam.getSerial()))) {
                    HorseSkillProcessor processor = HorseSkillProcessor.queryProcessor(Integer.valueOf(horseSkillParam.getSerial()));
                    if (processor == null) {
                        continue;
                    }
                    processor.attackOver(fight, (PetFightObject)target, effector, actionDetailParam, new Object[0]);
                }
                else {
                    continue;
                }
            }
        }
    }
    
    public static int reduceHurt(Fight fight, FightObject effector, FightObject target, int hurt, boolean isFury, RoleSkill skill) {
        if (skill == null) {
            skill = new RoleSkill();
            skill.setType(Integer.valueOf(PETSKILLATTR.PETSKILLATTR_WULI.getValue()));
            skill.setSkillId(Integer.valueOf(0));
        }
        int alertHurt = 0;
        if (effector.isPet()) {
            Set<Integer> serialSet = (Set)HorseDataPool.horseSkillSerialType.get(HORSE_SKILL_TRIGGER_DICT.REDUCE_HURT.getValue());
            Map<Integer, HorseSkillParam> horseSkillMap = ((PetFightObject)effector).getHorseSkillMap();
            for (HorseSkillParam horseSkillParam : horseSkillMap.values()) {
                if (serialSet.contains(Integer.valueOf(horseSkillParam.getSerial()))) {
                    HorseSkillProcessor processor = HorseSkillProcessor.queryProcessor(Integer.valueOf(horseSkillParam.getSerial()));
                    if (processor == null) {
                        continue;
                    }
                    NutMap map = processor.passive(fight, (PetFightObject)effector, target, Integer.valueOf(hurt), Boolean.valueOf(isFury), skill);
                    if (map != null) {
                        alertHurt += map.getInt("alterHurt", 0);
                    }
                    else {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
        }
        else if (target.isPet()) {
            Set<Integer> serialSet = (Set)HorseDataPool.horseSkillSerialType.get(HORSE_SKILL_TRIGGER_DICT.REDUCE_HURT_TARGET.getValue());
            Map<Integer, HorseSkillParam> horseSkillMap = ((PetFightObject)target).getHorseSkillMap();
            for (HorseSkillParam horseSkillParam : horseSkillMap.values()) {
                if (serialSet.contains(Integer.valueOf(horseSkillParam.getSerial()))) {
                    HorseSkillProcessor processor = HorseSkillProcessor.queryProcessor(Integer.valueOf(horseSkillParam.getSerial()));
                    if (processor == null) {
                        continue;
                    }
                    NutMap map = processor.passive(fight, (PetFightObject)target, effector, Integer.valueOf(hurt), Boolean.valueOf(isFury), skill);
                    if (map != null) {
                        alertHurt += map.getInt("alterHurt", 0);
                    }
                    else {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
        }
        return alertHurt;
    }
    
    public static double getPercent(HorseSkillParam horseSkillParam) {
        double a = (double)horseSkillParam.getGrowUp() / 1000.0 / 1.325;
        double b = -0.0067 * Math.pow((double)horseSkillParam.getLevel(), 2.0) + 2.06 * (double)horseSkillParam.getLevel();
        double c = -0.0067 * Math.pow((double)horseSkillParam.getProficient(), 2.0) / 1000.0 + 2.06 * (double)horseSkillParam.getProficient();
        double value = a * (0.6 * b / 130.0 + c / 325000.0 + 0.1) * 0.6;
        return value;
    }
    
    public static double getPercentMiddle(HorseSkillParam horseSkillParam) {
        double a = (double)horseSkillParam.getGrowUp() / 1000.0 / 1.325;
        double b = -0.0067 * Math.pow((double)horseSkillParam.getLevel(), 2.0) + 2.06 * (double)horseSkillParam.getLevel();
        double c = -0.0067 * Math.pow((double)horseSkillParam.getProficient(), 2.0) / 1000.0 + 2.06 * (double)horseSkillParam.getProficient();
        double value = a * (0.6 * b / 130.0 + c / 325000.0 + 0.1) * 0.8;
        return value;
    }
    
    public static double getPercentSenior(HorseSkillParam horseSkillParam) {
        double a = (double)horseSkillParam.getGrowUp() / 1000.0 / 1.325;
        double b = -0.0067 * Math.pow((double)horseSkillParam.getLevel(), 2.0) + 2.06 * (double)horseSkillParam.getLevel();
        double c = -0.0067 * Math.pow((double)horseSkillParam.getProficient(), 2.0) / 1000.0 + 2.06 * (double)horseSkillParam.getProficient();
        double value = a * (0.6 * b / 130.0 + c / 325000.0 + 0.1);
        return value;
    }
    
    public static double getPercentBeforeMagic(HorseSkillParam horseSkillParam) {
        double percent = (Math.floor(((double)horseSkillParam.getGrowUp() + 10000.0) / 11265.0) * (-0.5 * (double)horseSkillParam.getGrowUp() + 632.5) + (double)horseSkillParam.getGrowUp()) / 1000.0 / 1.325 * (0.6 * (-0.0067 * Math.pow(Math.floor(((double)horseSkillParam.getLevel() + 1000.0) / 1080.0) * (-0.5 * (double)horseSkillParam.getLevel() + 40.0) + (double)horseSkillParam.getLevel(), 2.0) + 2.06 * (Math.floor(((double)horseSkillParam.getLevel() + 1000.0) / 1080.0) * (-0.5 * (double)horseSkillParam.getLevel() + 40.0) + (double)horseSkillParam.getLevel())) / 130.0 + (-0.0067 * Math.pow(Math.floor(((double)horseSkillParam.getProficient() + 1000000.0) / 1080000.0) * (-0.5 * (double)horseSkillParam.getProficient() + 40000.0) + (double)horseSkillParam.getProficient(), 2.0) / 1000.0 + 2.06 * (Math.floor(((double)horseSkillParam.getProficient() + 1000000.0) / 1080000.0) * (-0.5 * (double)horseSkillParam.getProficient() + 40000.0) + (double)horseSkillParam.getProficient())) / 325000.0 + 0.1) * 0.6;
        return percent;
    }
    
    public static double getPercentBeforeMagicMiddle(HorseSkillParam horseSkillParam) {
        double percent = (Math.floor(((double)horseSkillParam.getGrowUp() + 10000.0) / 11265.0) * (-0.5 * (double)horseSkillParam.getGrowUp() + 632.5) + (double)horseSkillParam.getGrowUp()) / 1000.0 / 1.325 * (0.6 * (-0.0067 * Math.pow(Math.floor(((double)horseSkillParam.getLevel() + 1000.0) / 1080.0) * (-0.5 * (double)horseSkillParam.getLevel() + 40.0) + (double)horseSkillParam.getLevel(), 2.0) + 2.06 * (Math.floor(((double)horseSkillParam.getLevel() + 1000.0) / 1080.0) * (-0.5 * (double)horseSkillParam.getLevel() + 40.0) + (double)horseSkillParam.getLevel())) / 130.0 + (-0.0067 * Math.pow(Math.floor(((double)horseSkillParam.getProficient() + 1000000.0) / 1080000.0) * (-0.5 * (double)horseSkillParam.getProficient() + 40000.0) + (double)horseSkillParam.getProficient(), 2.0) / 1000.0 + 2.06 * (Math.floor(((double)horseSkillParam.getProficient() + 1000000.0) / 1080000.0) * (-0.5 * (double)horseSkillParam.getProficient() + 40000.0) + (double)horseSkillParam.getProficient())) / 325000.0 + 0.1) * 0.8;
        return percent;
    }
    
    public static double getPercentBeforeMagicSenior(HorseSkillParam horseSkillParam) {
        double percent = (Math.floor(((double)horseSkillParam.getGrowUp() + 10000.0) / 11265.0) * (-0.5 * (double)horseSkillParam.getGrowUp() + 632.5) + (double)horseSkillParam.getGrowUp()) / 1000.0 / 1.325 * (0.6 * (-0.0067 * Math.pow(Math.floor(((double)horseSkillParam.getLevel() + 1000.0) / 1080.0) * (-0.5 * (double)horseSkillParam.getLevel() + 40.0) + (double)horseSkillParam.getLevel(), 2.0) + 2.06 * (Math.floor(((double)horseSkillParam.getLevel() + 1000.0) / 1080.0) * (-0.5 * (double)horseSkillParam.getLevel() + 40.0) + (double)horseSkillParam.getLevel())) / 130.0 + (-0.0067 * Math.pow(Math.floor(((double)horseSkillParam.getProficient() + 1000000.0) / 1080000.0) * (-0.5 * (double)horseSkillParam.getProficient() + 40000.0) + (double)horseSkillParam.getProficient(), 2.0) / 1000.0 + 2.06 * (Math.floor(((double)horseSkillParam.getProficient() + 1000000.0) / 1080000.0) * (-0.5 * (double)horseSkillParam.getProficient() + 40000.0) + (double)horseSkillParam.getProficient())) / 325000.0 + 0.1);
        return percent;
    }
    
    public static double getMathRoundPercent(double src) {
        long round = Math.round(src * 100.0);
        double value = (double)round / 100.0;
        return value;
    }
    
    public static void main(String[] args) {
    }
}
