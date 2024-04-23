package com.kitty.system.game.fightnew.book.tianfu.utils;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.reflect.ReflectUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.EventDispatcher;
import com.kitty.listener.EventType;
import com.kitty.listener.event.ranking.PetChangeEvent;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.enums.war.itempro.def.DAY_IN_CE_TRIGGER_DICT;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.wardef.DAY_IN_CE;
import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.partner.pool.PartnerDataPool;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.game.tianfu.domain.RoleDayInCe;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import com.kitty.system.init.domain.SysPartnerProperty;
import com.kitty.system.init.domain.SysSkill;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.nutz.lang.util.NutMap;

public class TianFuUtils implements Serializable {
  public static Map<Long, String> getTianFuGridMap(RoleDayInCe roleDayInCe) {
    Map<Long, String> map = new HashMap<>();
    for (Map.Entry<String, Byte> entry : server2clientGridMap.entrySet()) {
      String key = entry.getKey();
      long gridValue = ((Long)ReflectUtils.invokeGetter(roleDayInCe, key)).longValue();
      if (gridValue > 1L)
        map.put(Long.valueOf(gridValue), key); 
    } 
    return map;
  }
  
  public static List<Long> getAllGridValues(RoleDayInCe roleDayInCe) {
    List<Long> tianFuList = new ArrayList<>();
    getGattacaGridValues(roleDayInCe, tianFuList);
    getLoadObjGridValues(roleDayInCe, tianFuList);
    getDriveBeastsGridValues(roleDayInCe, tianFuList);
    return tianFuList;
  }
  
  public static void getGattacaGridValues(RoleDayInCe roleDayInCe, List<Long> tianFuList) {
    long value = -1L;
    value = roleDayInCe.getGattaca11();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getGattaca12();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getGattaca21();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getGattaca22();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getGattaca31();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getGattaca32();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getGattaca41();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getGattaca42();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getGattaca51();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getGattaca();
    if (value > 1L)
      tianFuList.add(Long.valueOf(value)); 
  }
  
  public static void getLoadObjGridValues(RoleDayInCe roleDayInCe, List<Long> tianFuList) {
    long value = -1L;
    value = roleDayInCe.getLoadObj11();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getLoadObj12();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getLoadObj21();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getLoadObj22();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getLoadObj31();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getLoadObj32();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getLoadObj41();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getLoadObj42();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getLoadObj51();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getLoadObj();
    if (value > 1L)
      tianFuList.add(Long.valueOf(value)); 
  }
  
  public static void getDriveBeastsGridValues(RoleDayInCe roleDayInCe, List<Long> tianFuList) {
    long value = -1L;
    value = roleDayInCe.getDriveBeasts11();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getDriveBeasts12();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getDriveBeasts21();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getDriveBeasts22();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getDriveBeasts31();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getDriveBeasts32();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getDriveBeasts41();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getDriveBeasts42();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getDriveBeasts51();
    if (value > 1L) {
      tianFuList.add(Long.valueOf(value));
      value = -1L;
    } 
    value = roleDayInCe.getDriveBeasts();
    if (value > 1L)
      tianFuList.add(Long.valueOf(value)); 
  }
  
  public static TianFuObject getTianFuObject(DayInCeFight dayInCeFight, int ceSerial) {
    Optional<TianFuObject> tianFuObjectOptional = dayInCeFight.getTianFuList().stream().filter(obj -> {
          boolean b;
          if (obj.getCeSerial() == ceSerial) {
            b = true;
          } else {
            b = false;
          } 
          return b;
        }).findFirst();
    if (!tianFuObjectOptional.isPresent())
      return null; 
    return tianFuObjectOptional.get();
  }
  
  public static NutMap getHurtReturnMap(int hurt, double percent) {
    if (hurt <= 0)
      return null; 
    int returnHurt = (int)(hurt * percent);
    NutMap map = NutMap.NEW();
    map.setv("addHurt", Integer.valueOf(returnHurt));
    return map;
  }
  
  public static void whfmMethod(Fight fight, FightObject effector, int addLife, ActionDetailParam actionDetailParam) {
    if (effector.getCurrLife() + addLife > effector.getMaxLife())
      addLife = effector.getMaxLife() - effector.getCurrLife(); 
    FightObjectParam targetParam = actionDetailParam.gainTargetParam(effector.getId().intValue(), true);
    targetParam.addDamageType(119);
    PassiveSkillUtils.excuteChangeLifeAuto(fight, effector, actionDetailParam, targetParam, addLife);
  }
  
  public static int defUp(FightObject effector, Integer time) {
    Map<Short, SysPartnerProperty> shortSysPartnerPropertyMap = (Map<Short, SysPartnerProperty>)PartnerDataPool.partnerPropertyMap.get(Integer.valueOf(40008));
    short level = (short)(effector.getRelive() * 1000 + effector.getLevel());
    if (level > 3180)
      level = 3250; 
    SysPartnerProperty partnerProperty = shortSysPartnerPropertyMap.get(Short.valueOf(level));
    if (partnerProperty == null)
      return -1; 
    Integer proficient = (Integer)partnerProperty.getSkillInfo().get(Integer.valueOf(30024));
    if (proficient == null || proficient.intValue() < 0)
      return -1; 
    SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30024));
    double[] power = sysSkill.getPower();
    double up = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(2), Double.valueOf(power[0] * (Math.pow(proficient.intValue(), 0.35D) * 20.0D / 100.0D + 1.0D)));
    double buff = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(power[1] * (Math.pow(proficient.intValue(), 0.35D) * 2.0D / 100.0D + 1.0D)));
    double add = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(power[2] * (Math.pow(proficient.intValue(), 0.35D) * 2.0D / 100.0D + 1.0D)));
    if (time == null || time.intValue() <= 0)
      time = Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(3.0D * (1.0D + Math.pow(proficient.intValue(), 0.35D) * 5.0D / 100.0D)))); 
    int status = 1009;
    ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.K_FY.getDist(), time.intValue(), up);
    ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.K_HS.getDist(), time.intValue(), up);
    ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.K_HL.getDist(), time.intValue(), up);
    ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.K_YW.getDist(), time.intValue(), up);
    ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.K_F.getDist(), time.intValue(), buff);
    ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.K_L.getDist(), time.intValue(), buff);
    ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.K_S.getDist(), time.intValue(), buff);
    ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.K_H.getDist(), time.intValue(), buff);
    ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.WL_XS.getDist(), time.intValue(), add);
    return time.intValue();
  }
  
  public static int speedUp(FightObject effector, Integer time) {
    Map<Short, SysPartnerProperty> shortSysPartnerPropertyMap = (Map<Short, SysPartnerProperty>)PartnerDataPool.partnerPropertyMap.get(Integer.valueOf(40010));
    short level = (short)(effector.getRelive() * 1000 + effector.getLevel());
    if (level > 3180)
      level = 3250; 
    SysPartnerProperty partnerProperty = shortSysPartnerPropertyMap.get(Short.valueOf(level));
    if (partnerProperty == null)
      return -1; 
    Integer proficient = (Integer)partnerProperty.getSkillInfo().get(Integer.valueOf(30018));
    if (proficient == null || proficient.intValue() < 0)
      return -1; 
    SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30018));
    double[] power = sysSkill.getPower();
    if (time == null || time.intValue() <= 0)
      time = Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(3.0D * (1.0D + Math.pow(proficient.intValue(), 0.35D) * 5.0D / 100.0D)))); 
    int addSpeed = (int)(BigDecimalUtil.decimalScaleDouble(Integer.valueOf(2), Double.valueOf((sysSkill.getPower()[0] + proficient.intValue() / 5000.0D) / 100.0D)) * effector.getSpeed());
    ImbueUtil.addOrUpdStatus(effector.getImbueMap(), 1010, PRO_DICT_TYPE.RISE_SPEED.getDist(), time.intValue(), addSpeed);
    return time.intValue();
  }
  
  public static int phyPowerUp(FightObject effector, Integer time) {
    Map<Short, SysPartnerProperty> shortSysPartnerPropertyMap = (Map<Short, SysPartnerProperty>)PartnerDataPool.partnerPropertyMap.get(Integer.valueOf(40002));
    short level = (short)(effector.getRelive() * 1000 + effector.getLevel());
    if (level > 3180)
      level = 3250; 
    SysPartnerProperty partnerProperty = shortSysPartnerPropertyMap.get(Short.valueOf(level));
    if (partnerProperty == null)
      return -1; 
    Integer proficient = (Integer)partnerProperty.getSkillInfo().get(Integer.valueOf(30020));
    if (proficient == null || proficient.intValue() < 0)
      return -1; 
    SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30020));
    double[] power = sysSkill.getPower();
    if (time == null || time.intValue() <= 0)
      time = Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(3.0D * (1.0D + Math.pow(proficient.intValue(), 0.35D) * 5.0D / 100.0D)))); 
    double addMZ = sysSkill.getPower()[1];
    double addG = BigDecimalUtil.decimalScaleDouble(Integer.valueOf(3), Double.valueOf(sysSkill.getPower()[0] * (Math.pow(proficient.intValue(), 0.35D) * 3.0D / 100.0D + 1.0D)));
    int status = 1008;
    ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.HIT_RATIO.getDist(), time.intValue(), addMZ);
    ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.RISE_ATTACK.getDist(), time.intValue(), addG);
    return time.intValue();
  }
  
  public static List<FightObject> getFightObject(Fight fight, FightObject effector, Integer status) {
    List<FightObject> list = (List<FightObject>)fight.getMySide(effector.getId().intValue()).stream().filter(obj -> {
          boolean b;
          if (obj.getStatusValue(status.intValue()) <= 0 && (obj.isPlayer() || obj.isPartner())) {
            b = true;
          } else {
            b = false;
          } 
          return b;
        }).sorted(Comparator.comparing(FightObject::getCurrLife)).collect(Collectors.toList());
    if (list == null || list.size() <= 0) {
      list = (List<FightObject>)fight.getMySide(effector.getId().intValue()).stream().filter(obj -> obj.isPet()).sorted(Comparator.comparing(FightObject::getCurrLife)).collect(Collectors.toList());
      if (list == null || list.size() <= 0)
        return null; 
      return list;
    } 
    return list;
  }
  
  public static void changePlayerPropery(Role role, NutMap propertyMap) {
    NutMap nutMap = countFuPlayerBookGain(role);
    for (String key : nutMap.keySet()) {
      if (key.equals(String.valueOf(PRO_DICT_TYPE.HP_CAP.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.MP_CAP.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.ATTACK.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.SPEED.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.HP_PCT.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.ATTACK_PCT.getDist())) || 
        key.equals(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist())))
        continue; 
      excutePropertyMap(propertyMap, nutMap, key);
    } 
  }
  
  public static void changePetPropery(Role role, Pet pet, NutMap map) {
    NutMap nutMap = countFuPetBookGain(role, pet);
    for (String key : nutMap.keySet()) {
      if (key.equals(String.valueOf(PRO_DICT_TYPE.HP_CAP.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.MP_CAP.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.ATTACK.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.SPEED.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.HP_PCT.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.ATTACK_PCT.getDist())) || 
        key.equals(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist())))
        continue; 
      excutePropertyMap(map, nutMap, key);
    } 
  }
  
  private static void excutePropertyMap(NutMap propertyMap, NutMap nutMap, String key) {
    if (!key.contains("_")) {
      propertyMap.setv(key, Double.valueOf(propertyMap.getDouble(key) + nutMap.getDouble(key)));
    } else {
      String[] keys = key.split("_");
      if (keys[1].equals(Integer.valueOf(121))) {
        double upLimit = nutMap.getDouble(keys[0] + '\023');
        double pct = nutMap.getDouble(keys[0]);
        double selfValue = propertyMap.getDouble(keys[0]);
        double value = selfValue * pct;
        if (upLimit > 0.0D) {
          if (value > upLimit)
            value = upLimit; 
        } else if (value < upLimit) {
          value = upLimit;
        } 
        propertyMap.setv(keys[0], Double.valueOf(selfValue + value));
      } 
    } 
  }
  
  public static double getPropertyForMap(NutMap nutMap, String key, double selfValue) {
    double changeValue = nutMap.getDouble(key, 0.0D);
    double pct = nutMap.getDouble(key + "_" + 'y', 0.0D);
    if (pct != 0.0D) {
      double upLimit = nutMap.getDouble(key + "_" + '\023', 0.0D);
      double value = selfValue * pct;
      if (upLimit > 0.0D) {
        if (value > upLimit)
          value = upLimit; 
      } else if (value < upLimit) {
        value = upLimit;
      } 
      changeValue += value;
    } 
    return changeValue;
  }
  
  public static NutMap countFuPetBookGain(Role role, Pet pet) {
    NutMap map = new NutMap();
    if (role == null)
      return map; 
    List<RoleTianFu> roleTianFus = (List<RoleTianFu>)DataCache.ROLE_TIAN_FU.get(role.getId());
    RoleDayInCe roleDayInCe = (RoleDayInCe)DataCache.ROLE_DAY_IN_CE.get(role.getId());
    if (roleTianFus == null || roleDayInCe == null)
      return map; 
    for (RoleTianFu roleTianFu : roleTianFus) {
      if (roleTianFu.getIsWear() != 1 || 
        roleTianFu.getTianFuSerial() == 9997)
        continue; 
      if (roleTianFu.getTianFuSerial() == 321105 || DAY_IN_CE.DRIVER_BEASTS.getValue() == roleTianFu.getCategory()) {
        excuteTianFuProcessor(map, roleDayInCe, roleTianFu);
        excuteOtherProccessor(pet, map, roleDayInCe, roleTianFu);
      } 
    } 
    return map;
  }
  
  public static NutMap countFuPlayerBookGain(Role role) {
    NutMap map = new NutMap();
    List<RoleTianFu> roleTianFus = (List<RoleTianFu>)DataCache.ROLE_TIAN_FU.get(role.getId());
    RoleDayInCe roleDayInCe = (RoleDayInCe)DataCache.ROLE_DAY_IN_CE.get(role.getId());
    if (roleTianFus == null || roleDayInCe == null)
      return map; 
    for (int i = 0; i < roleTianFus.size(); i++) {
      RoleTianFu roleTianFu = roleTianFus.get(i);
      if (countFuPlayerBookGain(roleTianFu, true))
        excuteTianFuProcessor(map, roleDayInCe, roleTianFu); 
    } 
    return map;
  }
  
  public static boolean countFuPlayerBookGain(RoleTianFu roleTianFu, boolean flag) {
    if (roleTianFu.getTianFuSerial() == 9997 || roleTianFu.getTianFuSerial() == 321105 || DAY_IN_CE.DRIVER_BEASTS.getValue() == roleTianFu.getCategory())
      return false; 
    if (flag && roleTianFu.getIsWear() != 1)
      return false; 
    return true;
  }
  
  private static void excuteTianFuProcessor(NutMap map, RoleDayInCe roleDayInCe, RoleTianFu roleTianFu) {
    if (roleTianFu.getIsWear() == 1) {
      TianFuProcessor tianFuProcessor = TianFuProcessor.queryGainProcessor(Integer.valueOf(roleTianFu.getTianFuSerial()));
      if (tianFuProcessor != null) {
        TianFuAddition tianFuAddition = new TianFuAddition(roleDayInCe.getLevel(), roleTianFu.getLevel());
        NutMap nutMap = tianFuProcessor.addition(tianFuAddition, new Object[0]);
        if (nutMap != null)
          for (String key : nutMap.keySet())
            map.setv(key, Double.valueOf(map.getDouble(key) + nutMap.getDouble(key)));  
      } 
    } 
  }
  
  private static void excuteOtherProccessor(Pet pet, NutMap map, RoleDayInCe roleDayInCe, RoleTianFu roleTianFu) {
    if (roleTianFu.getIsWear() == 1) {
      TianFuProcessor tianFuProcessor = TianFuProcessor.queryGainProcessor(Integer.valueOf(roleTianFu.getTianFuSerial()));
      if (tianFuProcessor != null) {
        TianFuAddition tianFuAddition = new TianFuAddition(roleDayInCe.getLevel(), roleTianFu.getLevel());
        NutMap nutMap = tianFuProcessor.otherAddition(pet, tianFuAddition, new Object[0]);
        if (nutMap != null)
          for (String key : nutMap.keySet())
            map.setv(key, Double.valueOf(map.getDouble(key) + nutMap.getDouble(key)));  
      } 
    } 
  }
  
  public static NutMap getWearInstantlyTakeEffect(Object object) {
    Role role = null;
    Pet pet = null;
    if (object instanceof Pet) {
      pet = (Pet)object;
      role = SpringUtils.getRoleService().getOnlinePlayer(pet.getRoleId().intValue());
    } else if (object instanceof Role) {
      role = (Role)object;
    } 
    if (role != null) {
      NutMap map = NutMap.NEW();
      RoleDayInCe roleDayInCe = (RoleDayInCe)DataCache.ROLE_DAY_IN_CE.get(role.getId());
      List<RoleTianFu> roleTianFus = (List<RoleTianFu>)DataCache.ROLE_TIAN_FU.get(role.getId());
      if (roleTianFus == null || roleTianFus.size() <= 0)
        return null; 
      List<RoleTianFu> wearList = (List<RoleTianFu>)roleTianFus.stream().filter(obj -> {
            boolean b;
            if (obj.getIsWear() == 1 && DAY_IN_CE_TRIGGER_DICT.WEAR_INSTANTLY_TAKE_EFFECT.getValue().equals(obj.getTriggerType())) {
              b = true;
            } else {
              b = false;
            } 
            return b;
          }).collect(Collectors.toList());
      for (RoleTianFu roleTianFu : wearList) {
        TianFuProcessor tianFuProcessor = TianFuProcessor.queryGainProcessor(Integer.valueOf(roleTianFu.getTianFuSerial()));
        if (tianFuProcessor != null) {
          TianFuAddition tianFuAddition = new TianFuAddition(roleDayInCe.getLevel(), roleTianFu.getLevel());
          NutMap nutMap = tianFuProcessor.wearInstantlyTakeEffect(role, pet, tianFuAddition, new Object[0]);
          if (nutMap != null)
            for (String key : nutMap.keySet())
              map.setv(key, Double.valueOf(map.getDouble(key) + nutMap.getDouble(key)));  
        } 
      } 
      return map;
    } 
    return null;
  }
  
  public static double getTianFuCount(byte tianFuLevel, short dayInCeLevel) {
    return tianFuLevel * (1.0D + dayInCeLevel * 0.002D);
  }
  
  public static double getCountAddition(TianFuAddition tianFuAddition) {
    return getTianFuCount(tianFuAddition.getTianFuLevel(), tianFuAddition.getDayInCeLevel());
  }
  
  public static void excuteWearInstantlyTakeEffect(Role role, RoleTianFu roleTianFu) {
    if (DAY_IN_CE_TRIGGER_DICT.WEAR_INSTANTLY_TAKE_EFFECT.getValue().equals(roleTianFu.getTriggerType()))
      if (323501 == roleTianFu.getTianFuSerial()) {
        PetService petService = (PetService)SpringUtils.getBean(PetService.class);
        Map<Integer, Pet> petMap = petService.selectPet(role);
        if (petMap.size() > 0)
          for (Pet pet : petMap.values()) {
            petService.calculateBase(role.getId().longValue(), pet);
            EventDispatcher.getInstance().fireEvent((BaseGameEvent)new PetChangeEvent(EventType.PET_CHANGE, pet, petService.getPetEffective(role.getId().longValue(), pet)));
          }  
      } else {
        SpringUtils.getRoleService().calculateBase(role);
      }  
  }
  
  public static void clearTianFuAffect(FightObject fightObject, int status) {
    if (1005 == status) {
      if (fightObject.getBeAttackedMap().getInt(String.valueOf(OTHER_SKILL_DICT_TYPE.D_SUPERPOSITION_COUNT.getDist())) > 0)
        fightObject.getBeAttackedMap().unset(String.valueOf(OTHER_SKILL_DICT_TYPE.D_SUPERPOSITION_COUNT.getDist())); 
    } else if (1007 == status && fightObject.getBeAttackedMap().getInt(String.valueOf(OTHER_SKILL_DICT_TYPE.MH_SUPERPOSITION_COUNT.getDist())) > 0) {
      fightObject.getBeAttackedMap().unset(String.valueOf(OTHER_SKILL_DICT_TYPE.MH_SUPERPOSITION_COUNT.getDist()));
    } 
  }
  
  public static Map<String, Byte> server2clientGridMap = new HashMap<>();
  
  public static Map<Byte, String> client2serverGridMap = new HashMap<>();
  
  static {
    server2clientGridMap.put("gattaca11", Byte.valueOf((byte)4));
    server2clientGridMap.put("gattaca12", Byte.valueOf((byte)64));
    server2clientGridMap.put("gattaca21", Byte.valueOf((byte)16));
    server2clientGridMap.put("gattaca22", Byte.valueOf((byte)76));
    server2clientGridMap.put("gattaca31", Byte.valueOf((byte)28));
    server2clientGridMap.put("gattaca32", Byte.valueOf((byte)88));
    server2clientGridMap.put("gattaca41", Byte.valueOf((byte)40));
    server2clientGridMap.put("gattaca42", Byte.valueOf((byte)100));
    server2clientGridMap.put("gattaca51", Byte.valueOf((byte)52));
    server2clientGridMap.put("gattaca", Byte.valueOf((byte)1));
    server2clientGridMap.put("loadObj11", Byte.valueOf((byte)8));
    server2clientGridMap.put("loadObj12", Byte.valueOf((byte)68));
    server2clientGridMap.put("loadObj21", Byte.valueOf((byte)20));
    server2clientGridMap.put("loadObj22", Byte.valueOf((byte)80));
    server2clientGridMap.put("loadObj31", Byte.valueOf((byte)32));
    server2clientGridMap.put("loadObj32", Byte.valueOf((byte)92));
    server2clientGridMap.put("loadObj41", Byte.valueOf((byte)44));
    server2clientGridMap.put("loadObj42", Byte.valueOf((byte)104));
    server2clientGridMap.put("loadObj51", Byte.valueOf((byte)56));
    server2clientGridMap.put("loadObj", Byte.valueOf((byte)2));
    server2clientGridMap.put("driveBeasts11", Byte.valueOf((byte)12));
    server2clientGridMap.put("driveBeasts12", Byte.valueOf((byte)72));
    server2clientGridMap.put("driveBeasts21", Byte.valueOf((byte)24));
    server2clientGridMap.put("driveBeasts22", Byte.valueOf((byte)84));
    server2clientGridMap.put("driveBeasts31", Byte.valueOf((byte)36));
    server2clientGridMap.put("driveBeasts32", Byte.valueOf((byte)96));
    server2clientGridMap.put("driveBeasts41", Byte.valueOf((byte)48));
    server2clientGridMap.put("driveBeasts42", Byte.valueOf((byte)108));
    server2clientGridMap.put("driveBeasts51", Byte.valueOf((byte)60));
    server2clientGridMap.put("driveBeasts", Byte.valueOf((byte)3));
    client2serverGridMap.put(Byte.valueOf((byte)4), "gattaca11");
    client2serverGridMap.put(Byte.valueOf((byte)64), "gattaca12");
    client2serverGridMap.put(Byte.valueOf((byte)16), "gattaca21");
    client2serverGridMap.put(Byte.valueOf((byte)76), "gattaca22");
    client2serverGridMap.put(Byte.valueOf((byte)28), "gattaca31");
    client2serverGridMap.put(Byte.valueOf((byte)88), "gattaca32");
    client2serverGridMap.put(Byte.valueOf((byte)40), "gattaca41");
    client2serverGridMap.put(Byte.valueOf((byte)100), "gattaca42");
    client2serverGridMap.put(Byte.valueOf((byte)52), "gattaca51");
    client2serverGridMap.put(Byte.valueOf((byte)1), "gattaca");
    client2serverGridMap.put(Byte.valueOf((byte)8), "loadObj11");
    client2serverGridMap.put(Byte.valueOf((byte)68), "loadObj12");
    client2serverGridMap.put(Byte.valueOf((byte)20), "loadObj21");
    client2serverGridMap.put(Byte.valueOf((byte)80), "loadObj22");
    client2serverGridMap.put(Byte.valueOf((byte)32), "loadObj31");
    client2serverGridMap.put(Byte.valueOf((byte)92), "loadObj32");
    client2serverGridMap.put(Byte.valueOf((byte)44), "loadObj41");
    client2serverGridMap.put(Byte.valueOf((byte)104), "loadObj42");
    client2serverGridMap.put(Byte.valueOf((byte)56), "loadObj51");
    client2serverGridMap.put(Byte.valueOf((byte)2), "loadObj");
    client2serverGridMap.put(Byte.valueOf((byte)12), "driveBeasts11");
    client2serverGridMap.put(Byte.valueOf((byte)72), "driveBeasts12");
    client2serverGridMap.put(Byte.valueOf((byte)24), "driveBeasts21");
    client2serverGridMap.put(Byte.valueOf((byte)84), "driveBeasts22");
    client2serverGridMap.put(Byte.valueOf((byte)36), "driveBeasts31");
    client2serverGridMap.put(Byte.valueOf((byte)96), "driveBeasts32");
    client2serverGridMap.put(Byte.valueOf((byte)48), "driveBeasts41");
    client2serverGridMap.put(Byte.valueOf((byte)108), "driveBeasts42");
    client2serverGridMap.put(Byte.valueOf((byte)60), "driveBeasts51");
    client2serverGridMap.put(Byte.valueOf((byte)3), "driveBeasts");
  }
}
