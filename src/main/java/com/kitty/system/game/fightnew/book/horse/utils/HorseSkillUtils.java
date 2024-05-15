
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
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.event.ranking.PetChangeEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.EventDispatcher;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.pet.service.PetService;
import java.util.IdentityHashMap;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.horse.domain.Horse;
import java.util.Map;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;

public class HorseSkillUtils
{
    public static void studyFinish(final Role role, final long horseId, final Pet pet) {
        if (role == null) {
            return;
        }
        final Map<Long, Horse> horseMap = DataCache.HORSE_MAP.get(role.getId());
        if (horseMap == null || horseMap.size() <= 0) {
            return;
        }
        final Horse horse = horseMap.get(horseId);
        if (horse == null) {
            return;
        }
        final IdentityHashMap<Integer, Long> skill = horse.getSkill();
        if (skill == null || skill.size() <= 0) {
            return;
        }
        if (pet == null) {
            excuteAll(role, horseId);
        }
        else {
            excuteOne(role.getId(), horseId, pet);
        }
    }
    
    private static void excuteOne(final long id, final long horseId, final Pet pet) {
        if (pet.getHorse() == horseId) {
            SpringUtils.getBean(PetService.class).calculateBase(id, pet);
        }
    }
    
    private static void excuteAll(final Role role, final long horseId) {
        final PetService petService = SpringUtils.getBean(PetService.class);
        final Map<Integer, Pet> petMap = petService.selectPet(role);
        if (petMap != null && petMap.size() > 0) {
            for (final Pet pet : petMap.values()) {
                if (pet.getHorse() == horseId) {
                    petService.calculateBase(role.getId(), pet);
                    EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_CHANGE, pet, petService.getPetEffective(role.getId(), pet)));
                }
            }
        }
    }
    
    public static int getHorseSkillProperty(final Pet pet, final int propertyDist, final String horseSkillTriggerType) {
        if (pet.getHorse() == 0L) {
            return 0;
        }
        final Role role = SpringUtils.getRoleService().getPlayer(pet.getRoleId());
        if (role == null) {
            return 0;
        }
        final Map<Long, Horse> roleHorseMap = DataCache.HORSE_MAP.get(role.getId());
        if (roleHorseMap == null) {
            return 0;
        }
        final Horse horse = roleHorseMap.get(pet.getHorse());
        if (horse == null) {
            return 0;
        }
        final IdentityHashMap<Integer, Long> skillMap = horse.getSkill();
        if (skillMap == null || skillMap.size() <= 0) {
            return 0;
        }
        final Set<Integer> serialSet = HorseDataPool.horseSkillSerialType.get(horseSkillTriggerType);
        int addProperty = 0;
        for (final Map.Entry<Integer, Long> entry : skillMap.entrySet()) {
            final Integer key = entry.getKey();
            if (!serialSet.contains(key)) {
                continue;
            }
            final HorseSkillParam horseSkillParam = new HorseSkillParam(key, entry.getValue(), horse.getGrowUp(), horse.getLevel());
            final HorseSkillProcessor processor = HorseSkillProcessor.queryProcessor(key);
            if (processor == null) {
                continue;
            }
            final NutMap nutMap = processor.studyExcute(pet, horseSkillParam);
            if (nutMap == null || nutMap.size() <= 0 || nutMap.getInt(String.valueOf(propertyDist), 0) == 0) {
                continue;
            }
            addProperty += nutMap.getInt(String.valueOf(propertyDist), 0);
        }
        return addProperty;
    }
    
    public static double getHorseSkillPropertyPct(final Pet pet, final int propertyPctDist, final String horseSkillTriggerType) {
        if (pet.getHorse() == 0L) {
            return 0.0;
        }
        final Role role = SpringUtils.getRoleService().getOnlinePlayer(pet.getRoleId());
        if (role == null) {
            return 0.0;
        }
        final Map<Long, Horse> roleHorseMap = DataCache.HORSE_MAP.get(role.getId());
        if (roleHorseMap == null) {
            return 0.0;
        }
        final Horse horse = roleHorseMap.get(pet.getHorse());
        if (horse == null) {
            return 0.0;
        }
        final IdentityHashMap<Integer, Long> skillMap = horse.getSkill();
        if (skillMap == null || skillMap.size() <= 0) {
            return 0.0;
        }
        final Set<Integer> serialSet = HorseDataPool.horseSkillSerialType.get(horseSkillTriggerType);
        final Set<Integer> others = new HashSet<Integer>();
        if (PRO_DICT_TYPE.SPEED_PCT.getDist() == propertyPctDist) {
            for (final Map.Entry<String, Set<Integer>> entry : HorseDataPool.horseSkillSerialType.entrySet()) {
                if (!horseSkillTriggerType.equals(entry.getKey())) {
                    if (HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH_LAST.getValue().equals(entry.getKey())) {
                        continue;
                    }
                    others.addAll(entry.getValue());
                }
            }
        }
        double addProperty = 0.0;
        for (final Map.Entry<Integer, Long> entry2 : skillMap.entrySet()) {
            final Integer key = entry2.getKey();
            if (!serialSet.contains(key) && !others.contains(key)) {
                continue;
            }
            addProperty += excuteSkill(pet, horse, propertyPctDist, key, entry2.getValue());
        }
        return addProperty;
    }
    
    private static double excuteSkill(final Pet pet, final Horse horse, final int propertyPctDist, final Integer key, final Long value) {
        final HorseSkillParam horseSkillParam = new HorseSkillParam(key, value, horse.getGrowUp(), horse.getLevel());
        final HorseSkillProcessor processor = HorseSkillProcessor.queryProcessor(key);
        if (processor == null) {
            return 0.0;
        }
        double addProperty = 0.0;
        final NutMap nutMap = processor.studyExcute(pet, horseSkillParam);
        if (nutMap != null && nutMap.size() > 0 && nutMap.getDouble(String.valueOf(propertyPctDist), 0.0) != 0.0) {
            addProperty += nutMap.getDouble(String.valueOf(propertyPctDist));
        }
        return addProperty;
    }
    
    public static void countHorseSkillGain(final NutMap propertyMap, final Map<Integer, HorseSkillParam> horseSkillParamMap) {
        if (horseSkillParamMap == null || horseSkillParamMap.size() <= 0) {
            return;
        }
        for (final HorseSkillParam horseSkillParam : horseSkillParamMap.values()) {
            final HorseSkill horseSkill = HorseDataPool.horseSkillMap.get(horseSkillParam.getSerial());
            if (horseSkill != null) {
                if (!horseSkill.isFightInitializeLoad()) {
                    continue;
                }
                final HorseSkillProcessor processor = HorseSkillProcessor.queryProcessor(horseSkillParam.getSerial());
                if (processor == null) {
                    continue;
                }
                final NutMap nutMap = processor.addition(horseSkillParam);
                if (nutMap == null || nutMap.size() <= 0) {
                    continue;
                }
                for (final Map.Entry<String, Object> entry : nutMap.entrySet()) {
                    final String key = entry.getKey();
                    propertyMap.setv(key, (Object)(propertyMap.getDouble(key, 0.0) + nutMap.getDouble(key, 0.0)));
                }
            }
        }
    }
    
    public static void beforeMagicHurt(final Fight fight, final FightObject effector, final RoleSkill skill) {
        if (!effector.isPet()) {
            return;
        }
        final Set<Integer> serialSet = HorseDataPool.horseSkillSerialType.get(HORSE_SKILL_TRIGGER_DICT.BEFORE_MAGIC_HURT.getValue());
        final Map<Integer, HorseSkillParam> horseSkillMap = ((PetFightObject)effector).getHorseSkillMap();
        for (final HorseSkillParam horseSkillParam : horseSkillMap.values()) {
            if (serialSet.contains(horseSkillParam.getSerial())) {
                final HorseSkillProcessor processor = HorseSkillProcessor.queryProcessor(horseSkillParam.getSerial());
                if (processor == null) {
                    continue;
                }
                processor.passive(fight, (PetFightObject)effector, null, skill);
            }
        }
    }
    
    public static void targetHits(final Fight fight, final FightObject effector, final FightObject target, final ActionDetailParam actionDetailParam) {
        if (effector.isPet()) {
            final Set<Integer> serialSet = HorseDataPool.horseSkillSerialType.get(HORSE_SKILL_TRIGGER_DICT.TARGET_HITS.getValue());
            final Map<Integer, HorseSkillParam> horseSkillMap = ((PetFightObject)effector).getHorseSkillMap();
            for (final HorseSkillParam horseSkillParam : horseSkillMap.values()) {
                if (serialSet.contains(horseSkillParam.getSerial())) {
                    final HorseSkillProcessor processor = HorseSkillProcessor.queryProcessor(horseSkillParam.getSerial());
                    if (processor == null) {
                        continue;
                    }
                    processor.attackOver(fight, (PetFightObject)effector, target, actionDetailParam);
                }
            }
        }
        else if (target.isPet()) {
            final Set<Integer> serialSet = HorseDataPool.horseSkillSerialType.get(HORSE_SKILL_TRIGGER_DICT.BY_TARGET_HITS.getValue());
            final Map<Integer, HorseSkillParam> horseSkillMap = ((PetFightObject)target).getHorseSkillMap();
            for (final HorseSkillParam horseSkillParam : horseSkillMap.values()) {
                if (serialSet.contains(horseSkillParam.getSerial())) {
                    final HorseSkillProcessor processor = HorseSkillProcessor.queryProcessor(horseSkillParam.getSerial());
                    if (processor == null) {
                        continue;
                    }
                    processor.attackOver(fight, (PetFightObject)target, effector, actionDetailParam);
                }
            }
        }
    }
    
    public static int reduceHurt(final Fight fight, final FightObject effector, final FightObject target, final int hurt, final boolean isFury, RoleSkill skill) {
        if (skill == null) {
            skill = new RoleSkill();
            skill.setType(PETSKILLATTR.PETSKILLATTR_WULI.getValue());
            skill.setSkillId(0);
        }
        int alertHurt = 0;
        if (effector.isPet()) {
            final Set<Integer> serialSet = HorseDataPool.horseSkillSerialType.get(HORSE_SKILL_TRIGGER_DICT.REDUCE_HURT.getValue());
            final Map<Integer, HorseSkillParam> horseSkillMap = ((PetFightObject)effector).getHorseSkillMap();
            for (final HorseSkillParam horseSkillParam : horseSkillMap.values()) {
                if (serialSet.contains(horseSkillParam.getSerial())) {
                    final HorseSkillProcessor processor = HorseSkillProcessor.queryProcessor(horseSkillParam.getSerial());
                    if (processor == null) {
                        continue;
                    }
                    final NutMap map = processor.passive(fight, (PetFightObject)effector, target, hurt, isFury, skill);
                    if (map == null) {
                        continue;
                    }
                    alertHurt += map.getInt("alterHurt", 0);
                }
            }
        }
        else if (target.isPet()) {
            final Set<Integer> serialSet = HorseDataPool.horseSkillSerialType.get(HORSE_SKILL_TRIGGER_DICT.REDUCE_HURT_TARGET.getValue());
            final Map<Integer, HorseSkillParam> horseSkillMap = ((PetFightObject)target).getHorseSkillMap();
            for (final HorseSkillParam horseSkillParam : horseSkillMap.values()) {
                if (serialSet.contains(horseSkillParam.getSerial())) {
                    final HorseSkillProcessor processor = HorseSkillProcessor.queryProcessor(horseSkillParam.getSerial());
                    if (processor == null) {
                        continue;
                    }
                    final NutMap map = processor.passive(fight, (PetFightObject)target, effector, hurt, isFury, skill);
                    if (map == null) {
                        continue;
                    }
                    alertHurt += map.getInt("alterHurt", 0);
                }
            }
        }
        return alertHurt;
    }
    
    public static double getPercent(final HorseSkillParam horseSkillParam) {
        final double a = horseSkillParam.getGrowUp() / 1000.0 / 1.325;
        final double b = -0.0067 * Math.pow(horseSkillParam.getLevel(), 2.0) + 2.06 * horseSkillParam.getLevel();
        final double c = -0.0067 * Math.pow((double)horseSkillParam.getProficient(), 2.0) / 1000.0 + 2.06 * horseSkillParam.getProficient();
        final double value = a * (0.6 * b / 130.0 + c / 325000.0 + 0.1) * 0.6;
        return value;
    }
    
    public static double getPercentMiddle(final HorseSkillParam horseSkillParam) {
        final double a = horseSkillParam.getGrowUp() / 1000.0 / 1.325;
        final double b = -0.0067 * Math.pow(horseSkillParam.getLevel(), 2.0) + 2.06 * horseSkillParam.getLevel();
        final double c = -0.0067 * Math.pow((double)horseSkillParam.getProficient(), 2.0) / 1000.0 + 2.06 * horseSkillParam.getProficient();
        final double value = a * (0.6 * b / 130.0 + c / 325000.0 + 0.1) * 0.8;
        return value;
    }
    
    public static double getPercentSenior(final HorseSkillParam horseSkillParam) {
        final double a = horseSkillParam.getGrowUp() / 1000.0 / 1.325;
        final double b = -0.0067 * Math.pow(horseSkillParam.getLevel(), 2.0) + 2.06 * horseSkillParam.getLevel();
        final double c = -0.0067 * Math.pow((double)horseSkillParam.getProficient(), 2.0) / 1000.0 + 2.06 * horseSkillParam.getProficient();
        final double value = a * (0.6 * b / 130.0 + c / 325000.0 + 0.1);
        return value;
    }
    
    public static double getPercentBeforeMagic(final HorseSkillParam horseSkillParam) {
        final double percent = (Math.floor((horseSkillParam.getGrowUp() + 10000.0) / 11265.0) * (-0.5 * horseSkillParam.getGrowUp() + 632.5) + horseSkillParam.getGrowUp()) / 1000.0 / 1.325 * (0.6 * (-0.0067 * Math.pow(Math.floor((horseSkillParam.getLevel() + 1000.0) / 1080.0) * (-0.5 * horseSkillParam.getLevel() + 40.0) + horseSkillParam.getLevel(), 2.0) + 2.06 * (Math.floor((horseSkillParam.getLevel() + 1000.0) / 1080.0) * (-0.5 * horseSkillParam.getLevel() + 40.0) + horseSkillParam.getLevel())) / 130.0 + (-0.0067 * Math.pow(Math.floor((horseSkillParam.getProficient() + 1000000.0) / 1080000.0) * (-0.5 * horseSkillParam.getProficient() + 40000.0) + horseSkillParam.getProficient(), 2.0) / 1000.0 + 2.06 * (Math.floor((horseSkillParam.getProficient() + 1000000.0) / 1080000.0) * (-0.5 * horseSkillParam.getProficient() + 40000.0) + horseSkillParam.getProficient())) / 325000.0 + 0.1) * 0.6;
        return percent;
    }
    
    public static double getPercentBeforeMagicMiddle(final HorseSkillParam horseSkillParam) {
        final double percent = (Math.floor((horseSkillParam.getGrowUp() + 10000.0) / 11265.0) * (-0.5 * horseSkillParam.getGrowUp() + 632.5) + horseSkillParam.getGrowUp()) / 1000.0 / 1.325 * (0.6 * (-0.0067 * Math.pow(Math.floor((horseSkillParam.getLevel() + 1000.0) / 1080.0) * (-0.5 * horseSkillParam.getLevel() + 40.0) + horseSkillParam.getLevel(), 2.0) + 2.06 * (Math.floor((horseSkillParam.getLevel() + 1000.0) / 1080.0) * (-0.5 * horseSkillParam.getLevel() + 40.0) + horseSkillParam.getLevel())) / 130.0 + (-0.0067 * Math.pow(Math.floor((horseSkillParam.getProficient() + 1000000.0) / 1080000.0) * (-0.5 * horseSkillParam.getProficient() + 40000.0) + horseSkillParam.getProficient(), 2.0) / 1000.0 + 2.06 * (Math.floor((horseSkillParam.getProficient() + 1000000.0) / 1080000.0) * (-0.5 * horseSkillParam.getProficient() + 40000.0) + horseSkillParam.getProficient())) / 325000.0 + 0.1) * 0.8;
        return percent;
    }
    
    public static double getPercentBeforeMagicSenior(final HorseSkillParam horseSkillParam) {
        final double percent = (Math.floor((horseSkillParam.getGrowUp() + 10000.0) / 11265.0) * (-0.5 * horseSkillParam.getGrowUp() + 632.5) + horseSkillParam.getGrowUp()) / 1000.0 / 1.325 * (0.6 * (-0.0067 * Math.pow(Math.floor((horseSkillParam.getLevel() + 1000.0) / 1080.0) * (-0.5 * horseSkillParam.getLevel() + 40.0) + horseSkillParam.getLevel(), 2.0) + 2.06 * (Math.floor((horseSkillParam.getLevel() + 1000.0) / 1080.0) * (-0.5 * horseSkillParam.getLevel() + 40.0) + horseSkillParam.getLevel())) / 130.0 + (-0.0067 * Math.pow(Math.floor((horseSkillParam.getProficient() + 1000000.0) / 1080000.0) * (-0.5 * horseSkillParam.getProficient() + 40000.0) + horseSkillParam.getProficient(), 2.0) / 1000.0 + 2.06 * (Math.floor((horseSkillParam.getProficient() + 1000000.0) / 1080000.0) * (-0.5 * horseSkillParam.getProficient() + 40000.0) + horseSkillParam.getProficient())) / 325000.0 + 0.1);
        return percent;
    }
    
    public static double getMathRoundPercent(final double src) {
        final long round = Math.round(src * 100.0);
        final double value = round / 100.0;
        return value;
    }
    
    public static void main(final String[] args) {
    }
}
