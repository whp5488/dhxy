package com.kitty.system.game.fightnew.book.horse.utils;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.EventDispatcher;
import com.kitty.listener.EventType;
import com.kitty.listener.event.ranking.PetChangeEvent;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.enums.war.itempro.def.HORSE_SKILL_TRIGGER_DICT;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.warskill.skilldef.PETSKILLATTR;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.horse.domain.Horse;
import com.kitty.system.game.horse.pool.HorseDataPool;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.init.domain.HorseSkill;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import org.nutz.lang.util.NutMap;

public class HorseSkillUtils {
  public static void studyFinish(Role role, long horseId, Pet pet) {
    if (role == null)
      return; 
    Map<Long, Horse> horseMap = (Map<Long, Horse>)DataCache.HORSE_MAP.get(role.getId());
    if (horseMap == null || horseMap.size() <= 0)
      return; 
    Horse horse = horseMap.get(Long.valueOf(horseId));
    if (horse == null)
      return; 
    IdentityHashMap<Integer, Long> skill = horse.getSkill();
    if (skill == null || skill.size() <= 0)
      return; 
    if (pet == null) {
      excuteAll(role, horseId);
    } else {
      excuteOne(role.getId().longValue(), horseId, pet);
    } 
  }
  
  private static void excuteOne(long id, long horseId, Pet pet) {
    if (pet.getHorse() == horseId)
      ((PetService)SpringUtils.getBean(PetService.class)).calculateBase(id, pet); 
  }
  
  private static void excuteAll(Role role, long horseId) {
    PetService petService = (PetService)SpringUtils.getBean(PetService.class);
    Map<Integer, Pet> petMap = petService.selectPet(role);
    if (petMap != null && petMap.size() > 0)
      for (Pet pet : petMap.values()) {
        if (pet.getHorse() == horseId) {
          petService.calculateBase(role.getId().longValue(), pet);
          EventDispatcher.getInstance().fireEvent((BaseGameEvent)new PetChangeEvent(EventType.PET_CHANGE, pet, petService.getPetEffective(role.getId().longValue(), pet)));
        } 
      }  
  }
  
  public static int getHorseSkillProperty(Pet pet, int propertyDist, String horseSkillTriggerType) {
    if (pet.getHorse() == 0L)
      return 0; 
    Role role = SpringUtils.getRoleService().getPlayer(pet.getRoleId().intValue());
    if (role == null)
      return 0; 
    Map<Long, Horse> roleHorseMap = (Map<Long, Horse>)DataCache.HORSE_MAP.get(role.getId());
    if (roleHorseMap == null)
      return 0; 
    Horse horse = roleHorseMap.get(Long.valueOf(pet.getHorse()));
    if (horse == null)
      return 0; 
    IdentityHashMap<Integer, Long> skillMap = horse.getSkill();
    if (skillMap == null || skillMap.size() <= 0)
      return 0; 
    Set<Integer> serialSet = (Set<Integer>)HorseDataPool.horseSkillSerialType.get(horseSkillTriggerType);
    int addProperty = 0;
    for (Map.Entry<Integer, Long> entry : skillMap.entrySet()) {
      Integer key = entry.getKey();
      if (!serialSet.contains(key))
        continue; 
      HorseSkillParam horseSkillParam = new HorseSkillParam(key.intValue(), ((Long)entry.getValue()).longValue(), horse.getGrowUp(), horse.getLevel());
      HorseSkillProcessor processor = HorseSkillProcessor.queryProcessor(key);
      if (processor == null)
        continue; 
      NutMap nutMap = processor.studyExcute(pet, horseSkillParam, new Object[0]);
      if (nutMap != null && nutMap.size() > 0 && 
        nutMap.getInt(String.valueOf(propertyDist), 0) != 0)
        addProperty += nutMap.getInt(String.valueOf(propertyDist), 0); 
    } 
    return addProperty;
  }
  
  public static double getHorseSkillPropertyPct(Pet pet, int propertyPctDist, String horseSkillTriggerType) {
    if (pet.getHorse() == 0L)
      return 0.0D; 
    Role role = SpringUtils.getRoleService().getOnlinePlayer(pet.getRoleId().intValue());
    if (role == null)
      return 0.0D; 
    Map<Long, Horse> roleHorseMap = (Map<Long, Horse>)DataCache.HORSE_MAP.get(role.getId());
    if (roleHorseMap == null)
      return 0.0D; 
    Horse horse = roleHorseMap.get(Long.valueOf(pet.getHorse()));
    if (horse == null)
      return 0.0D; 
    IdentityHashMap<Integer, Long> skillMap = horse.getSkill();
    if (skillMap == null || skillMap.size() <= 0)
      return 0.0D; 
    Set<Integer> serialSet = (Set<Integer>)HorseDataPool.horseSkillSerialType.get(horseSkillTriggerType);
    Set<Integer> others = new HashSet<>();
    if (PRO_DICT_TYPE.SPEED_PCT.getDist() == propertyPctDist)
      for (Map.Entry<String, Set<Integer>> entry : (Iterable<Map.Entry<String, Set<Integer>>>)HorseDataPool.horseSkillSerialType.entrySet()) {
        if (horseSkillTriggerType.equals(entry.getKey()) || HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH_LAST.getValue().equals(entry.getKey()))
          continue; 
        others.addAll(entry.getValue());
      }  
    double addProperty = 0.0D;
    for (Map.Entry<Integer, Long> entry : skillMap.entrySet()) {
      Integer key = entry.getKey();
      if (!serialSet.contains(key) && !others.contains(key))
        continue; 
      addProperty += excuteSkill(pet, horse, propertyPctDist, key, entry.getValue());
    } 
    return addProperty;
  }
  
  private static double excuteSkill(Pet pet, Horse horse, int propertyPctDist, Integer key, Long value) {
    HorseSkillParam horseSkillParam = new HorseSkillParam(key.intValue(), value.longValue(), horse.getGrowUp(), horse.getLevel());
    HorseSkillProcessor processor = HorseSkillProcessor.queryProcessor(key);
    if (processor == null)
      return 0.0D; 
    double addProperty = 0.0D;
    NutMap nutMap = processor.studyExcute(pet, horseSkillParam, new Object[0]);
    if (nutMap != null && nutMap.size() > 0 && 
      nutMap.getDouble(String.valueOf(propertyPctDist), 0.0D) != 0.0D)
      addProperty += nutMap.getDouble(String.valueOf(propertyPctDist)); 
    return addProperty;
  }
  
  public static void countHorseSkillGain(NutMap propertyMap, Map<Integer, HorseSkillParam> horseSkillParamMap) {
    if (horseSkillParamMap == null || horseSkillParamMap.size() <= 0)
      return; 
    for (HorseSkillParam horseSkillParam : horseSkillParamMap.values()) {
      HorseSkill horseSkill = (HorseSkill)HorseDataPool.horseSkillMap.get(Integer.valueOf(horseSkillParam.getSerial()));
      if (horseSkill == null || !horseSkill.isFightInitializeLoad())
        continue; 
      HorseSkillProcessor processor = HorseSkillProcessor.queryProcessor(Integer.valueOf(horseSkillParam.getSerial()));
      if (processor == null)
        continue; 
      NutMap nutMap = processor.addition(horseSkillParam, new Object[0]);
      if (nutMap != null && nutMap.size() > 0)
        for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)nutMap.entrySet()) {
          String key = entry.getKey();
          propertyMap.setv(key, Double.valueOf(propertyMap.getDouble(key, 0.0D) + nutMap.getDouble(key, 0.0D)));
        }  
    } 
  }
  
  public static void beforeMagicHurt(Fight fight, FightObject effector, RoleSkill skill) {
    if (!effector.isPet())
      return; 
    Set<Integer> serialSet = (Set<Integer>)HorseDataPool.horseSkillSerialType.get(HORSE_SKILL_TRIGGER_DICT.BEFORE_MAGIC_HURT.getValue());
    Map<Integer, HorseSkillParam> horseSkillMap = ((PetFightObject)effector).getHorseSkillMap();
    for (HorseSkillParam horseSkillParam : horseSkillMap.values()) {
      if (serialSet.contains(Integer.valueOf(horseSkillParam.getSerial()))) {
        HorseSkillProcessor processor = HorseSkillProcessor.queryProcessor(Integer.valueOf(horseSkillParam.getSerial()));
        if (processor == null)
          continue; 
        processor.passive(fight, (PetFightObject)effector, null, new Object[] { skill });
      } 
    } 
  }
  
  public static void targetHits(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam) {
    if (effector.isPet()) {
      Set<Integer> serialSet = (Set<Integer>)HorseDataPool.horseSkillSerialType.get(HORSE_SKILL_TRIGGER_DICT.TARGET_HITS.getValue());
      Map<Integer, HorseSkillParam> horseSkillMap = ((PetFightObject)effector).getHorseSkillMap();
      for (HorseSkillParam horseSkillParam : horseSkillMap.values()) {
        if (serialSet.contains(Integer.valueOf(horseSkillParam.getSerial()))) {
          HorseSkillProcessor processor = HorseSkillProcessor.queryProcessor(Integer.valueOf(horseSkillParam.getSerial()));
          if (processor == null)
            continue; 
          processor.attackOver(fight, (PetFightObject)effector, target, actionDetailParam, new Object[0]);
        } 
      } 
    } else if (target.isPet()) {
      Set<Integer> serialSet = (Set<Integer>)HorseDataPool.horseSkillSerialType.get(HORSE_SKILL_TRIGGER_DICT.BY_TARGET_HITS.getValue());
      Map<Integer, HorseSkillParam> horseSkillMap = ((PetFightObject)target).getHorseSkillMap();
      for (HorseSkillParam horseSkillParam : horseSkillMap.values()) {
        if (serialSet.contains(Integer.valueOf(horseSkillParam.getSerial()))) {
          HorseSkillProcessor processor = HorseSkillProcessor.queryProcessor(Integer.valueOf(horseSkillParam.getSerial()));
          if (processor == null)
            continue; 
          processor.attackOver(fight, (PetFightObject)target, effector, actionDetailParam, new Object[0]);
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
      Set<Integer> serialSet = (Set<Integer>)HorseDataPool.horseSkillSerialType.get(HORSE_SKILL_TRIGGER_DICT.REDUCE_HURT.getValue());
      Map<Integer, HorseSkillParam> horseSkillMap = ((PetFightObject)effector).getHorseSkillMap();
      for (HorseSkillParam horseSkillParam : horseSkillMap.values()) {
        if (serialSet.contains(Integer.valueOf(horseSkillParam.getSerial()))) {
          HorseSkillProcessor processor = HorseSkillProcessor.queryProcessor(Integer.valueOf(horseSkillParam.getSerial()));
          if (processor == null)
            continue; 
          NutMap map = processor.passive(fight, (PetFightObject)effector, target, new Object[] { Integer.valueOf(hurt), Boolean.valueOf(isFury), skill });
          if (map != null)
            alertHurt += map.getInt("alterHurt", 0); 
        } 
      } 
    } else if (target.isPet()) {
      Set<Integer> serialSet = (Set<Integer>)HorseDataPool.horseSkillSerialType.get(HORSE_SKILL_TRIGGER_DICT.REDUCE_HURT_TARGET.getValue());
      Map<Integer, HorseSkillParam> horseSkillMap = ((PetFightObject)target).getHorseSkillMap();
      for (HorseSkillParam horseSkillParam : horseSkillMap.values()) {
        if (serialSet.contains(Integer.valueOf(horseSkillParam.getSerial()))) {
          HorseSkillProcessor processor = HorseSkillProcessor.queryProcessor(Integer.valueOf(horseSkillParam.getSerial()));
          if (processor == null)
            continue; 
          NutMap map = processor.passive(fight, (PetFightObject)target, effector, new Object[] { Integer.valueOf(hurt), Boolean.valueOf(isFury), skill });
          if (map != null)
            alertHurt += map.getInt("alterHurt", 0); 
        } 
      } 
    } 
    return alertHurt;
  }
  
  public static double getPercent(HorseSkillParam horseSkillParam) {
    double a = horseSkillParam.getGrowUp() / 1000.0D / 1.325D;
    double b = -0.0067D * Math.pow(horseSkillParam.getLevel(), 2.0D) + 2.06D * horseSkillParam.getLevel();
    double c = -0.0067D * Math.pow(horseSkillParam.getProficient(), 2.0D) / 1000.0D + 2.06D * horseSkillParam.getProficient();
    double value = a * (0.6D * b / 130.0D + c / 325000.0D + 0.1D) * 0.6D;
    return value;
  }
  
  public static double getPercentMiddle(HorseSkillParam horseSkillParam) {
    double a = horseSkillParam.getGrowUp() / 1000.0D / 1.325D;
    double b = -0.0067D * Math.pow(horseSkillParam.getLevel(), 2.0D) + 2.06D * horseSkillParam.getLevel();
    double c = -0.0067D * Math.pow(horseSkillParam.getProficient(), 2.0D) / 1000.0D + 2.06D * horseSkillParam.getProficient();
    double value = a * (0.6D * b / 130.0D + c / 325000.0D + 0.1D) * 0.8D;
    return value;
  }
  
  public static double getPercentSenior(HorseSkillParam horseSkillParam) {
    double a = horseSkillParam.getGrowUp() / 1000.0D / 1.325D;
    double b = -0.0067D * Math.pow(horseSkillParam.getLevel(), 2.0D) + 2.06D * horseSkillParam.getLevel();
    double c = -0.0067D * Math.pow(horseSkillParam.getProficient(), 2.0D) / 1000.0D + 2.06D * horseSkillParam.getProficient();
    double value = a * (0.6D * b / 130.0D + c / 325000.0D + 0.1D);
    return value;
  }
  
  public static double getPercentBeforeMagic(HorseSkillParam horseSkillParam) {
    double percent = (Math.floor((horseSkillParam.getGrowUp() + 10000.0D) / 11265.0D) * (-0.5D * horseSkillParam.getGrowUp() + 632.5D) + horseSkillParam.getGrowUp()) / 1000.0D / 1.325D * (0.6D * (-0.0067D * Math.pow(Math.floor((horseSkillParam.getLevel() + 1000.0D) / 1080.0D) * (-0.5D * horseSkillParam.getLevel() + 40.0D) + horseSkillParam.getLevel(), 2.0D) + 2.06D * (Math.floor((horseSkillParam.getLevel() + 1000.0D) / 1080.0D) * (-0.5D * horseSkillParam.getLevel() + 40.0D) + horseSkillParam.getLevel())) / 130.0D + (-0.0067D * Math.pow(Math.floor((horseSkillParam.getProficient() + 1000000.0D) / 1080000.0D) * (-0.5D * horseSkillParam.getProficient() + 40000.0D) + horseSkillParam.getProficient(), 2.0D) / 1000.0D + 2.06D * (Math.floor((horseSkillParam.getProficient() + 1000000.0D) / 1080000.0D) * (-0.5D * horseSkillParam.getProficient() + 40000.0D) + horseSkillParam.getProficient())) / 325000.0D + 0.1D) * 0.6D;
    return percent;
  }
  
  public static double getPercentBeforeMagicMiddle(HorseSkillParam horseSkillParam) {
    double percent = (Math.floor((horseSkillParam.getGrowUp() + 10000.0D) / 11265.0D) * (-0.5D * horseSkillParam.getGrowUp() + 632.5D) + horseSkillParam.getGrowUp()) / 1000.0D / 1.325D * (0.6D * (-0.0067D * Math.pow(Math.floor((horseSkillParam.getLevel() + 1000.0D) / 1080.0D) * (-0.5D * horseSkillParam.getLevel() + 40.0D) + horseSkillParam.getLevel(), 2.0D) + 2.06D * (Math.floor((horseSkillParam.getLevel() + 1000.0D) / 1080.0D) * (-0.5D * horseSkillParam.getLevel() + 40.0D) + horseSkillParam.getLevel())) / 130.0D + (-0.0067D * Math.pow(Math.floor((horseSkillParam.getProficient() + 1000000.0D) / 1080000.0D) * (-0.5D * horseSkillParam.getProficient() + 40000.0D) + horseSkillParam.getProficient(), 2.0D) / 1000.0D + 2.06D * (Math.floor((horseSkillParam.getProficient() + 1000000.0D) / 1080000.0D) * (-0.5D * horseSkillParam.getProficient() + 40000.0D) + horseSkillParam.getProficient())) / 325000.0D + 0.1D) * 0.8D;
    return percent;
  }
  
  public static double getPercentBeforeMagicSenior(HorseSkillParam horseSkillParam) {
    double percent = (Math.floor((horseSkillParam.getGrowUp() + 10000.0D) / 11265.0D) * (-0.5D * horseSkillParam.getGrowUp() + 632.5D) + horseSkillParam.getGrowUp()) / 1000.0D / 1.325D * (0.6D * (-0.0067D * Math.pow(Math.floor((horseSkillParam.getLevel() + 1000.0D) / 1080.0D) * (-0.5D * horseSkillParam.getLevel() + 40.0D) + horseSkillParam.getLevel(), 2.0D) + 2.06D * (Math.floor((horseSkillParam.getLevel() + 1000.0D) / 1080.0D) * (-0.5D * horseSkillParam.getLevel() + 40.0D) + horseSkillParam.getLevel())) / 130.0D + (-0.0067D * Math.pow(Math.floor((horseSkillParam.getProficient() + 1000000.0D) / 1080000.0D) * (-0.5D * horseSkillParam.getProficient() + 40000.0D) + horseSkillParam.getProficient(), 2.0D) / 1000.0D + 2.06D * (Math.floor((horseSkillParam.getProficient() + 1000000.0D) / 1080000.0D) * (-0.5D * horseSkillParam.getProficient() + 40000.0D) + horseSkillParam.getProficient())) / 325000.0D + 0.1D);
    return percent;
  }
  
  public static double getMathRoundPercent(double src) {
    long round = Math.round(src * 100.0D);
    double value = round / 100.0D;
    return value;
  }
  
  public static void main(String[] args) {}
}
