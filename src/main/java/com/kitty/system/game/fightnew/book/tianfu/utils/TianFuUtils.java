
package com.kitty.system.game.fightnew.book.tianfu.utils;

import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.event.ranking.PetChangeEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.EventDispatcher;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.enums.war.itempro.def.DAY_IN_CE_TRIGGER_DICT;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import com.kitty.system.enums.war.wardef.DAY_IN_CE;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.game.partner.pool.PartnerDataPool;
import com.kitty.system.init.domain.SysPartnerProperty;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import java.util.Optional;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import com.kitty.common.utils.reflect.ReflectUtils;
import java.util.HashMap;
import com.kitty.system.game.tianfu.domain.RoleDayInCe;
import java.util.Map;
import java.io.Serializable;

public class TianFuUtils implements Serializable
{
    public static Map<String, Byte> server2clientGridMap;
    public static Map<Byte, String> client2serverGridMap;
    
    public static Map<Long, String> getTianFuGridMap(final RoleDayInCe roleDayInCe) {
        final Map<Long, String> map = new HashMap<Long, String>();
        for (final Map.Entry<String, Byte> entry : TianFuUtils.server2clientGridMap.entrySet()) {
            final String key = entry.getKey();
            final long gridValue = ReflectUtils.invokeGetter(roleDayInCe, key);
            if (gridValue > 1L) {
                map.put(gridValue, key);
            }
        }
        return map;
    }
    
    public static List<Long> getAllGridValues(final RoleDayInCe roleDayInCe) {
        final List<Long> tianFuList = new ArrayList<Long>();
        getGattacaGridValues(roleDayInCe, tianFuList);
        getLoadObjGridValues(roleDayInCe, tianFuList);
        getDriveBeastsGridValues(roleDayInCe, tianFuList);
        return tianFuList;
    }
    
    public static void getGattacaGridValues(final RoleDayInCe roleDayInCe, final List<Long> tianFuList) {
        long value = -1L;
        value = roleDayInCe.getGattaca11();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getGattaca12();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getGattaca21();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getGattaca22();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getGattaca31();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getGattaca32();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getGattaca41();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getGattaca42();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getGattaca51();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getGattaca();
        if (value > 1L) {
            tianFuList.add(value);
        }
    }
    
    public static void getLoadObjGridValues(final RoleDayInCe roleDayInCe, final List<Long> tianFuList) {
        long value = -1L;
        value = roleDayInCe.getLoadObj11();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getLoadObj12();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getLoadObj21();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getLoadObj22();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getLoadObj31();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getLoadObj32();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getLoadObj41();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getLoadObj42();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getLoadObj51();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getLoadObj();
        if (value > 1L) {
            tianFuList.add(value);
        }
    }
    
    public static void getDriveBeastsGridValues(final RoleDayInCe roleDayInCe, final List<Long> tianFuList) {
        long value = -1L;
        value = roleDayInCe.getDriveBeasts11();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getDriveBeasts12();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getDriveBeasts21();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getDriveBeasts22();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getDriveBeasts31();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getDriveBeasts32();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getDriveBeasts41();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getDriveBeasts42();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getDriveBeasts51();
        if (value > 1L) {
            tianFuList.add(value);
            value = -1L;
        }
        value = roleDayInCe.getDriveBeasts();
        if (value > 1L) {
            tianFuList.add(value);
        }
    }
    
    public static TianFuObject getTianFuObject(final DayInCeFight dayInCeFight, final int ceSerial) {
        final Optional<TianFuObject> tianFuObjectOptional = dayInCeFight.getTianFuList().stream().filter(obj -> {
            final boolean b = obj.getCeSerial() == ceSerial;
            return b;
        }).findFirst();
        if (!tianFuObjectOptional.isPresent()) {
            return null;
        }
        return tianFuObjectOptional.get();
    }
    
    public static NutMap getHurtReturnMap(final int hurt, final double percent) {
        if (hurt <= 0) {
            return null;
        }
        final int returnHurt = (int)(hurt * percent);
        final NutMap map = NutMap.NEW();
        map.setv("addHurt", (Object)returnHurt);
        return map;
    }
    
    public static void whfmMethod(final Fight fight, final FightObject effector, int addLife, final ActionDetailParam actionDetailParam) {
        if (effector.getCurrLife() + addLife > effector.getMaxLife()) {
            addLife = effector.getMaxLife() - effector.getCurrLife();
        }
        final FightObjectParam targetParam = actionDetailParam.gainTargetParam(effector.getId(), true);
        targetParam.addDamageType(119);
        PassiveSkillUtils.excuteChangeLifeAuto(fight, effector, actionDetailParam, targetParam, addLife);
    }
    
    public static int defUp(final FightObject effector, Integer time) {
        final Map<Short, SysPartnerProperty> shortSysPartnerPropertyMap = PartnerDataPool.partnerPropertyMap.get(40008);
        short level = (short)(effector.getRelive() * 1000 + effector.getLevel());
        if (level > 3180) {
            level = 3250;
        }
        final SysPartnerProperty partnerProperty = shortSysPartnerPropertyMap.get(level);
        if (partnerProperty == null) {
            return -1;
        }
        final Integer proficient = partnerProperty.getSkillInfo().get(30024);
        if (proficient == null || proficient < 0) {
            return -1;
        }
        final SysSkill sysSkill = SkillDataPool.skillMap.get(30024);
        final double[] power = sysSkill.getPower();
        final double up = BigDecimalUtil.decimalScaleObjectDown(2, power[0] * (Math.pow(proficient, 0.35) * 20.0 / 100.0 + 1.0));
        final double buff = BigDecimalUtil.decimalScaleObjectDown(3, power[1] * (Math.pow(proficient, 0.35) * 2.0 / 100.0 + 1.0));
        final double add = BigDecimalUtil.decimalScaleObjectDown(3, power[2] * (Math.pow(proficient, 0.35) * 2.0 / 100.0 + 1.0));
        if (time == null || time <= 0) {
            time = BigDecimalUtil.decimalScaleIntegerDown(0, 3.0 * (1.0 + Math.pow(proficient, 0.35) * 5.0 / 100.0));
        }
        final int status = 1009;
        ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.K_FY.getDist(), time, up);
        ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.K_HS.getDist(), time, up);
        ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.K_HL.getDist(), time, up);
        ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.K_YW.getDist(), time, up);
        ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.K_F.getDist(), time, buff);
        ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.K_L.getDist(), time, buff);
        ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.K_S.getDist(), time, buff);
        ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.K_H.getDist(), time, buff);
        ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.WL_XS.getDist(), time, add);
        return time;
    }
    
    public static int speedUp(final FightObject effector, Integer time) {
        final Map<Short, SysPartnerProperty> shortSysPartnerPropertyMap = PartnerDataPool.partnerPropertyMap.get(40010);
        short level = (short)(effector.getRelive() * 1000 + effector.getLevel());
        if (level > 3180) {
            level = 3250;
        }
        final SysPartnerProperty partnerProperty = shortSysPartnerPropertyMap.get(level);
        if (partnerProperty == null) {
            return -1;
        }
        final Integer proficient = partnerProperty.getSkillInfo().get(30018);
        if (proficient == null || proficient < 0) {
            return -1;
        }
        final SysSkill sysSkill = SkillDataPool.skillMap.get(30018);
        final double[] power = sysSkill.getPower();
        if (time == null || time <= 0) {
            time = BigDecimalUtil.decimalScaleIntegerDown(0, 3.0 * (1.0 + Math.pow(proficient, 0.35) * 5.0 / 100.0));
        }
        final int addSpeed = (int)(BigDecimalUtil.decimalScaleDouble(2, (sysSkill.getPower()[0] + proficient / 5000.0) / 100.0) * effector.getSpeed());
        ImbueUtil.addOrUpdStatus(effector.getImbueMap(), 1010, PRO_DICT_TYPE.RISE_SPEED.getDist(), time, addSpeed);
        return time;
    }
    
    public static int phyPowerUp(final FightObject effector, Integer time) {
        final Map<Short, SysPartnerProperty> shortSysPartnerPropertyMap = PartnerDataPool.partnerPropertyMap.get(40002);
        short level = (short)(effector.getRelive() * 1000 + effector.getLevel());
        if (level > 3180) {
            level = 3250;
        }
        final SysPartnerProperty partnerProperty = shortSysPartnerPropertyMap.get(level);
        if (partnerProperty == null) {
            return -1;
        }
        final Integer proficient = partnerProperty.getSkillInfo().get(30020);
        if (proficient == null || proficient < 0) {
            return -1;
        }
        final SysSkill sysSkill = SkillDataPool.skillMap.get(30020);
        final double[] power = sysSkill.getPower();
        if (time == null || time <= 0) {
            time = BigDecimalUtil.decimalScaleIntegerDown(0, 3.0 * (1.0 + Math.pow(proficient, 0.35) * 5.0 / 100.0));
        }
        final double addMZ = sysSkill.getPower()[1];
        final double addG = BigDecimalUtil.decimalScaleDouble(3, sysSkill.getPower()[0] * (Math.pow(proficient, 0.35) * 3.0 / 100.0 + 1.0));
        final int status = 1008;
        ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.HIT_RATIO.getDist(), time, addMZ);
        ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.RISE_ATTACK.getDist(), time, addG);
        return time;
    }
    
    public static List<FightObject> getFightObject(final Fight fight, final FightObject effector, final Integer status) {
        List<FightObject> list = fight.getMySide(effector.getId()).stream().filter(obj -> {
            final boolean b = obj.getStatusValue(status) <= 0 && (obj.isPlayer() || obj.isPartner());
            return b;
        }).sorted(Comparator.comparing(FightObject::getCurrLife)).collect(Collectors.toList());
        if (list != null && list.size() > 0) {
            return list;
        }
        list = fight.getMySide(effector.getId()).stream().filter(obj -> obj.isPet()).sorted(Comparator.comparing(FightObject::getCurrLife)).collect(Collectors.toList());
        if (list == null || list.size() <= 0) {
            return null;
        }
        return list;
    }
    
    public static void changePlayerPropery(final Role role, final NutMap propertyMap) {
        final NutMap nutMap = countFuPlayerBookGain(role);
        for (final String key : nutMap.keySet()) {
            if (!key.equals(String.valueOf(PRO_DICT_TYPE.HP_CAP.getDist())) && !key.equals(String.valueOf(PRO_DICT_TYPE.MP_CAP.getDist())) && !key.equals(String.valueOf(PRO_DICT_TYPE.ATTACK.getDist())) && !key.equals(String.valueOf(PRO_DICT_TYPE.SPEED.getDist())) && !key.equals(String.valueOf(PRO_DICT_TYPE.HP_PCT.getDist())) && !key.equals(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist())) && !key.equals(String.valueOf(PRO_DICT_TYPE.ATTACK_PCT.getDist()))) {
                if (key.equals(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()))) {
                    continue;
                }
                excutePropertyMap(propertyMap, nutMap, key);
            }
        }
    }
    
    public static void changePetPropery(final Role role, final Pet pet, final NutMap map) {
        final NutMap nutMap = countFuPetBookGain(role, pet);
        for (final String key : nutMap.keySet()) {
            if (!key.equals(String.valueOf(PRO_DICT_TYPE.HP_CAP.getDist())) && !key.equals(String.valueOf(PRO_DICT_TYPE.MP_CAP.getDist())) && !key.equals(String.valueOf(PRO_DICT_TYPE.ATTACK.getDist())) && !key.equals(String.valueOf(PRO_DICT_TYPE.SPEED.getDist())) && !key.equals(String.valueOf(PRO_DICT_TYPE.HP_PCT.getDist())) && !key.equals(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist())) && !key.equals(String.valueOf(PRO_DICT_TYPE.ATTACK_PCT.getDist()))) {
                if (key.equals(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()))) {
                    continue;
                }
                excutePropertyMap(map, nutMap, key);
            }
        }
    }
    
    private static void excutePropertyMap(final NutMap propertyMap, final NutMap nutMap, final String key) {
        if (!key.contains("_")) {
            propertyMap.setv(key, (Object)(propertyMap.getDouble(key) + nutMap.getDouble(key)));
        }
        else {
            final String[] keys = key.split("_");
            if (keys[1].equals(121)) {
                final double upLimit = nutMap.getDouble(keys[0] + 19);
                final double pct = nutMap.getDouble(keys[0]);
                final double selfValue = propertyMap.getDouble(keys[0]);
                double value = selfValue * pct;
                if (upLimit > 0.0) {
                    if (value > upLimit) {
                        value = upLimit;
                    }
                }
                else if (value < upLimit) {
                    value = upLimit;
                }
                propertyMap.setv(keys[0], (Object)(selfValue + value));
            }
        }
    }
    
    public static double getPropertyForMap(final NutMap nutMap, final String key, final double selfValue) {
        double changeValue = nutMap.getDouble(key, 0.0);
        final double pct = nutMap.getDouble(key + "_" + 121, 0.0);
        if (pct != 0.0) {
            final double upLimit = nutMap.getDouble(key + "_" + 19, 0.0);
            double value = selfValue * pct;
            if (upLimit > 0.0) {
                if (value > upLimit) {
                    value = upLimit;
                }
            }
            else if (value < upLimit) {
                value = upLimit;
            }
            changeValue += value;
        }
        return changeValue;
    }
    
    public static NutMap countFuPetBookGain(final Role role, final Pet pet) {
        final NutMap map = new NutMap();
        if (role == null) {
            return map;
        }
        final List<RoleTianFu> roleTianFus = DataCache.ROLE_TIAN_FU.get(role.getId());
        final RoleDayInCe roleDayInCe = DataCache.ROLE_DAY_IN_CE.get(role.getId());
        if (roleTianFus == null || roleDayInCe == null) {
            return map;
        }
        for (final RoleTianFu roleTianFu : roleTianFus) {
            if (roleTianFu.getIsWear() == 1) {
                if (roleTianFu.getTianFuSerial() == 9997) {
                    continue;
                }
                if (roleTianFu.getTianFuSerial() != 321105 && DAY_IN_CE.DRIVER_BEASTS.getValue() != roleTianFu.getCategory()) {
                    continue;
                }
                excuteTianFuProcessor(map, roleDayInCe, roleTianFu);
                excuteOtherProccessor(pet, map, roleDayInCe, roleTianFu);
            }
        }
        return map;
    }
    
    public static NutMap countFuPlayerBookGain(final Role role) {
        final NutMap map = new NutMap();
        final List<RoleTianFu> roleTianFus = DataCache.ROLE_TIAN_FU.get(role.getId());
        final RoleDayInCe roleDayInCe = DataCache.ROLE_DAY_IN_CE.get(role.getId());
        if (roleTianFus == null || roleDayInCe == null) {
            return map;
        }
        for (int i = 0; i < roleTianFus.size(); ++i) {
            final RoleTianFu roleTianFu = roleTianFus.get(i);
            if (countFuPlayerBookGain(roleTianFu, true)) {
                excuteTianFuProcessor(map, roleDayInCe, roleTianFu);
            }
        }
        return map;
    }
    
    public static boolean countFuPlayerBookGain(final RoleTianFu roleTianFu, final boolean flag) {
        return roleTianFu.getTianFuSerial() != 9997 && roleTianFu.getTianFuSerial() != 321105 && DAY_IN_CE.DRIVER_BEASTS.getValue() != roleTianFu.getCategory() && (!flag || roleTianFu.getIsWear() == 1);
    }
    
    private static void excuteTianFuProcessor(final NutMap map, final RoleDayInCe roleDayInCe, final RoleTianFu roleTianFu) {
        if (roleTianFu.getIsWear() == 1) {
            final TianFuProcessor tianFuProcessor = TianFuProcessor.queryGainProcessor(roleTianFu.getTianFuSerial());
            if (tianFuProcessor != null) {
                final TianFuAddition tianFuAddition = new TianFuAddition(roleDayInCe.getLevel(), roleTianFu.getLevel());
                final NutMap nutMap = tianFuProcessor.addition(tianFuAddition);
                if (nutMap != null) {
                    for (final String key : nutMap.keySet()) {
                        map.setv(key, map.getDouble(key) + nutMap.getDouble(key));
                    }
                }
            }
        }
    }
    
    private static void excuteOtherProccessor(final Pet pet, final NutMap map, final RoleDayInCe roleDayInCe, final RoleTianFu roleTianFu) {
        if (roleTianFu.getIsWear() == 1) {
            final TianFuProcessor tianFuProcessor = TianFuProcessor.queryGainProcessor(roleTianFu.getTianFuSerial());
            if (tianFuProcessor != null) {
                final TianFuAddition tianFuAddition = new TianFuAddition(roleDayInCe.getLevel(), roleTianFu.getLevel());
                final NutMap nutMap = tianFuProcessor.otherAddition(pet, tianFuAddition);
                if (nutMap != null) {
                    for (final String key : nutMap.keySet()) {
                        map.setv(key, map.getDouble(key) + nutMap.getDouble(key));
                    }
                }
            }
        }
    }
    
    public static NutMap getWearInstantlyTakeEffect(final Object object) {
        Role role = null;
        Pet pet = null;
        if (object instanceof Pet) {
            pet = (Pet)object;
            role = SpringUtils.getRoleService().getOnlinePlayer(pet.getRoleId());
        }
        else if (object instanceof Role) {
            role = (Role)object;
        }
        if (role == null) {
            return null;
        }
        final NutMap map = NutMap.NEW();
        final RoleDayInCe roleDayInCe = DataCache.ROLE_DAY_IN_CE.get(role.getId());
        final List<RoleTianFu> roleTianFus = DataCache.ROLE_TIAN_FU.get(role.getId());
        if (roleTianFus == null || roleTianFus.size() <= 0) {
            return null;
        }
        final List<RoleTianFu> wearList = roleTianFus.stream().filter(obj -> {
            final boolean b = obj.getIsWear() == 1 && DAY_IN_CE_TRIGGER_DICT.WEAR_INSTANTLY_TAKE_EFFECT.getValue().equals(obj.getTriggerType());
            return b;
        }).collect(Collectors.toList());
        for (final RoleTianFu roleTianFu : wearList) {
            final TianFuProcessor tianFuProcessor = TianFuProcessor.queryGainProcessor(roleTianFu.getTianFuSerial());
            if (tianFuProcessor != null) {
                final TianFuAddition tianFuAddition = new TianFuAddition(roleDayInCe.getLevel(), roleTianFu.getLevel());
                final NutMap nutMap = tianFuProcessor.wearInstantlyTakeEffect(role, pet, tianFuAddition);
                if (nutMap == null) {
                    continue;
                }
                for (final String key : nutMap.keySet()) {
                    map.setv(key, map.getDouble(key) + nutMap.getDouble(key));
                }
            }
        }
        return map;
    }
    
    public static double getTianFuCount(final byte tianFuLevel, final short dayInCeLevel) {
        return tianFuLevel * (1.0 + dayInCeLevel * 0.002);
    }
    
    public static double getCountAddition(final TianFuAddition tianFuAddition) {
        return getTianFuCount(tianFuAddition.getTianFuLevel(), tianFuAddition.getDayInCeLevel());
    }
    
    public static void excuteWearInstantlyTakeEffect(final Role role, final RoleTianFu roleTianFu) {
        if (DAY_IN_CE_TRIGGER_DICT.WEAR_INSTANTLY_TAKE_EFFECT.getValue().equals(roleTianFu.getTriggerType())) {
            if (323501 == roleTianFu.getTianFuSerial()) {
                final PetService petService = SpringUtils.getBean(PetService.class);
                final Map<Integer, Pet> petMap = petService.selectPet(role);
                if (petMap.size() > 0) {
                    for (final Pet pet : petMap.values()) {
                        petService.calculateBase(role.getId(), pet);
                        EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_CHANGE, pet, petService.getPetEffective(role.getId(), pet)));
                    }
                }
            }
            else {
                SpringUtils.getRoleService().calculateBase(role);
            }
        }
    }
    
    public static void clearTianFuAffect(final FightObject fightObject, final int status) {
        if (1005 == status) {
            if (fightObject.getBeAttackedMap().getInt(String.valueOf(OTHER_SKILL_DICT_TYPE.D_SUPERPOSITION_COUNT.getDist())) > 0) {
                fightObject.getBeAttackedMap().unset(String.valueOf(OTHER_SKILL_DICT_TYPE.D_SUPERPOSITION_COUNT.getDist()));
            }
        }
        else if (1007 == status && fightObject.getBeAttackedMap().getInt(String.valueOf(OTHER_SKILL_DICT_TYPE.MH_SUPERPOSITION_COUNT.getDist())) > 0) {
            fightObject.getBeAttackedMap().unset(String.valueOf(OTHER_SKILL_DICT_TYPE.MH_SUPERPOSITION_COUNT.getDist()));
        }
    }
    
    static {
        TianFuUtils.server2clientGridMap = new HashMap<String, Byte>();
        TianFuUtils.client2serverGridMap = new HashMap<Byte, String>();
        TianFuUtils.server2clientGridMap.put("gattaca11", (byte)4);
        TianFuUtils.server2clientGridMap.put("gattaca12", (byte)64);
        TianFuUtils.server2clientGridMap.put("gattaca21", (byte)16);
        TianFuUtils.server2clientGridMap.put("gattaca22", (byte)76);
        TianFuUtils.server2clientGridMap.put("gattaca31", (byte)28);
        TianFuUtils.server2clientGridMap.put("gattaca32", (byte)88);
        TianFuUtils.server2clientGridMap.put("gattaca41", (byte)40);
        TianFuUtils.server2clientGridMap.put("gattaca42", (byte)100);
        TianFuUtils.server2clientGridMap.put("gattaca51", (byte)52);
        TianFuUtils.server2clientGridMap.put("gattaca", (byte)1);
        TianFuUtils.server2clientGridMap.put("loadObj11", (byte)8);
        TianFuUtils.server2clientGridMap.put("loadObj12", (byte)68);
        TianFuUtils.server2clientGridMap.put("loadObj21", (byte)20);
        TianFuUtils.server2clientGridMap.put("loadObj22", (byte)80);
        TianFuUtils.server2clientGridMap.put("loadObj31", (byte)32);
        TianFuUtils.server2clientGridMap.put("loadObj32", (byte)92);
        TianFuUtils.server2clientGridMap.put("loadObj41", (byte)44);
        TianFuUtils.server2clientGridMap.put("loadObj42", (byte)104);
        TianFuUtils.server2clientGridMap.put("loadObj51", (byte)56);
        TianFuUtils.server2clientGridMap.put("loadObj", (byte)2);
        TianFuUtils.server2clientGridMap.put("driveBeasts11", (byte)12);
        TianFuUtils.server2clientGridMap.put("driveBeasts12", (byte)72);
        TianFuUtils.server2clientGridMap.put("driveBeasts21", (byte)24);
        TianFuUtils.server2clientGridMap.put("driveBeasts22", (byte)84);
        TianFuUtils.server2clientGridMap.put("driveBeasts31", (byte)36);
        TianFuUtils.server2clientGridMap.put("driveBeasts32", (byte)96);
        TianFuUtils.server2clientGridMap.put("driveBeasts41", (byte)48);
        TianFuUtils.server2clientGridMap.put("driveBeasts42", (byte)108);
        TianFuUtils.server2clientGridMap.put("driveBeasts51", (byte)60);
        TianFuUtils.server2clientGridMap.put("driveBeasts", (byte)3);
        TianFuUtils.client2serverGridMap.put((byte)4, "gattaca11");
        TianFuUtils.client2serverGridMap.put((byte)64, "gattaca12");
        TianFuUtils.client2serverGridMap.put((byte)16, "gattaca21");
        TianFuUtils.client2serverGridMap.put((byte)76, "gattaca22");
        TianFuUtils.client2serverGridMap.put((byte)28, "gattaca31");
        TianFuUtils.client2serverGridMap.put((byte)88, "gattaca32");
        TianFuUtils.client2serverGridMap.put((byte)40, "gattaca41");
        TianFuUtils.client2serverGridMap.put((byte)100, "gattaca42");
        TianFuUtils.client2serverGridMap.put((byte)52, "gattaca51");
        TianFuUtils.client2serverGridMap.put((byte)1, "gattaca");
        TianFuUtils.client2serverGridMap.put((byte)8, "loadObj11");
        TianFuUtils.client2serverGridMap.put((byte)68, "loadObj12");
        TianFuUtils.client2serverGridMap.put((byte)20, "loadObj21");
        TianFuUtils.client2serverGridMap.put((byte)80, "loadObj22");
        TianFuUtils.client2serverGridMap.put((byte)32, "loadObj31");
        TianFuUtils.client2serverGridMap.put((byte)92, "loadObj32");
        TianFuUtils.client2serverGridMap.put((byte)44, "loadObj41");
        TianFuUtils.client2serverGridMap.put((byte)104, "loadObj42");
        TianFuUtils.client2serverGridMap.put((byte)56, "loadObj51");
        TianFuUtils.client2serverGridMap.put((byte)2, "loadObj");
        TianFuUtils.client2serverGridMap.put((byte)12, "driveBeasts11");
        TianFuUtils.client2serverGridMap.put((byte)72, "driveBeasts12");
        TianFuUtils.client2serverGridMap.put((byte)24, "driveBeasts21");
        TianFuUtils.client2serverGridMap.put((byte)84, "driveBeasts22");
        TianFuUtils.client2serverGridMap.put((byte)36, "driveBeasts31");
        TianFuUtils.client2serverGridMap.put((byte)96, "driveBeasts32");
        TianFuUtils.client2serverGridMap.put((byte)48, "driveBeasts41");
        TianFuUtils.client2serverGridMap.put((byte)108, "driveBeasts42");
        TianFuUtils.client2serverGridMap.put((byte)60, "driveBeasts51");
        TianFuUtils.client2serverGridMap.put((byte)3, "driveBeasts");
    }
}
