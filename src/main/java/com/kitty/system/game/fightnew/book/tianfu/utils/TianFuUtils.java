package com.kitty.system.game.fightnew.book.tianfu.utils;

import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.listener.event.ranking.PetChangeEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.EventDispatcher;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.enums.war.itempro.def.DAY_IN_CE_TRIGGER_DICT;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import com.kitty.system.enums.war.wardef.DAY_IN_CE;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import java.util.stream.Collectors;
import java.util.Comparator;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.init.domain.SysPartnerProperty;
import com.kitty.system.game.partner.pool.PartnerDataPool;
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
    
    public TianFuUtils() {
    }
    
    public static Map<Long, String> getTianFuGridMap(RoleDayInCe roleDayInCe) {
        Map<Long, String> map = new HashMap();
        for (Map.Entry<String, Byte> entry : TianFuUtils.server2clientGridMap.entrySet()) {
            String key = (String)entry.getKey();
            long gridValue = (long)ReflectUtils.invokeGetter(roleDayInCe, key);
            if (gridValue > 1L) {
                map.put(Long.valueOf(gridValue), key);
            }
            else {
                continue;
            }
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
        if (value > 1L) {
            tianFuList.add(Long.valueOf(value));
        }
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
        if (value > 1L) {
            tianFuList.add(Long.valueOf(value));
        }
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
        if (value > 1L) {
            tianFuList.add(Long.valueOf(value));
        }
    }
    
    public static TianFuObject getTianFuObject(DayInCeFight dayInCeFight, int ceSerial) {
        Optional<TianFuObject> tianFuObjectOptional = dayInCeFight.getTianFuList().stream().filter(obj -> obj.getCeSerial() == ceSerial).findFirst();
        if (!tianFuObjectOptional.isPresent()) {
            return null;
        }
        return (TianFuObject)tianFuObjectOptional.get();
    }
    
    public static NutMap getHurtReturnMap(int hurt, double percent) {
        if (hurt <= 0) {
            return null;
        }
        int returnHurt = (int)((double)hurt * percent);
        NutMap map = NutMap.NEW();
        map.setv("addHurt", Integer.valueOf(returnHurt));
        return map;
    }
    
    public static void whfmMethod(Fight fight, FightObject effector, int addLife, ActionDetailParam actionDetailParam) {
        if (effector.getCurrLife() + addLife > effector.getMaxLife()) {
            addLife = effector.getMaxLife() - effector.getCurrLife();
        }
        FightObjectParam targetParam = actionDetailParam.gainTargetParam((int)effector.getId(), true);
        targetParam.addDamageType(119);
        PassiveSkillUtils.excuteChangeLifeAuto(fight, effector, actionDetailParam, targetParam, addLife);
    }
    
    public static int defUp(FightObject effector, Integer time) {
        Map<Short, SysPartnerProperty> shortSysPartnerPropertyMap = (Map)PartnerDataPool.partnerPropertyMap.get(Integer.valueOf(40008));
        short level = (short)(effector.getRelive() * 1000 + effector.getLevel());
        SysPartnerProperty partnerProperty = (SysPartnerProperty)shortSysPartnerPropertyMap.get(Short.valueOf(level));
        if (partnerProperty == null) {
            return -1;
        }
        Integer proficient = (Integer)partnerProperty.getSkillInfo().get(Integer.valueOf(30024));
        if (proficient == null || (int)proficient < 0) {
            return -1;
        }
        SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30024));
        double[] power = sysSkill.getPower();
        double up = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(2), Double.valueOf(power[0] * (Math.pow((double)(int)proficient, 0.35) * 20.0 / 100.0 + 1.0)));
        double buff = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(power[1] * (Math.pow((double)(int)proficient, 0.35) * 2.0 / 100.0 + 1.0)));
        double add = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(power[2] * (Math.pow((double)(int)proficient, 0.35) * 2.0 / 100.0 + 1.0)));
        if (time == null || (int)time <= 0) {
            time = Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(3.0 * (1.0 + Math.pow((double)(int)proficient, 0.35) * 5.0 / 100.0))));
        }
        int status = 1009;
        ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.K_FY.getDist(), (int)time, up);
        ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.K_HS.getDist(), (int)time, up);
        ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.K_HL.getDist(), (int)time, up);
        ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.K_YW.getDist(), (int)time, up);
        ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.K_F.getDist(), (int)time, buff);
        ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.K_L.getDist(), (int)time, buff);
        ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.K_S.getDist(), (int)time, buff);
        ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.K_H.getDist(), (int)time, buff);
        ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.WL_XS.getDist(), (int)time, add);
        return (int)time;
    }
    
    public static int speedUp(FightObject effector, Integer time) {
        Map<Short, SysPartnerProperty> shortSysPartnerPropertyMap = (Map)PartnerDataPool.partnerPropertyMap.get(Integer.valueOf(40010));
        short level = (short)(effector.getRelive() * 1000 + effector.getLevel());
        SysPartnerProperty partnerProperty = (SysPartnerProperty)shortSysPartnerPropertyMap.get(Short.valueOf(level));
        if (partnerProperty == null) {
            return -1;
        }
        Integer proficient = (Integer)partnerProperty.getSkillInfo().get(Integer.valueOf(30018));
        if (proficient == null || (int)proficient < 0) {
            return -1;
        }
        SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30018));
        double[] power = sysSkill.getPower();
        if (time == null || (int)time <= 0) {
            time = Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(3.0 * (1.0 + Math.pow((double)(int)proficient, 0.35) * 5.0 / 100.0))));
        }
        int addSpeed = (int)(BigDecimalUtil.decimalScaleDouble(Integer.valueOf(2), Double.valueOf((sysSkill.getPower()[0] + (double)(int)proficient / 5000.0) / 100.0)) * (double)effector.getSpeed());
        ImbueUtil.addOrUpdStatus(effector.getImbueMap(), 1010, PRO_DICT_TYPE.RISE_SPEED.getDist(), (int)time, (double)addSpeed);
        return (int)time;
    }
    
    public static int phyPowerUp(FightObject effector, Integer time) {
        Map<Short, SysPartnerProperty> shortSysPartnerPropertyMap = (Map)PartnerDataPool.partnerPropertyMap.get(Integer.valueOf(40002));
        short level = (short)(effector.getRelive() * 1000 + effector.getLevel());
        SysPartnerProperty partnerProperty = (SysPartnerProperty)shortSysPartnerPropertyMap.get(Short.valueOf(level));
        if (partnerProperty == null) {
            return -1;
        }
        Integer proficient = (Integer)partnerProperty.getSkillInfo().get(Integer.valueOf(30020));
        if (proficient == null || (int)proficient < 0) {
            return -1;
        }
        SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30020));
        double[] power = sysSkill.getPower();
        if (time == null || (int)time <= 0) {
            time = Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(3.0 * (1.0 + Math.pow((double)(int)proficient, 0.35) * 5.0 / 100.0))));
        }
        double addMZ = sysSkill.getPower()[1];
        double addG = BigDecimalUtil.decimalScaleDouble(Integer.valueOf(3), Double.valueOf(sysSkill.getPower()[0] * (Math.pow((double)(int)proficient, 0.35) * 3.0 / 100.0 + 1.0)));
        int status = 1008;
        ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.HIT_RATIO.getDist(), (int)time, addMZ);
        ImbueUtil.addOrUpdStatus(effector.getImbueMap(), status, PRO_DICT_TYPE.RISE_ATTACK.getDist(), (int)time, addG);
        return (int)time;
    }
    
    public static List<FightObject> getFightObject(Fight fight, FightObject effector, Integer status) {
        List<FightObject> list = (List)fight.getMySide((int)effector.getId()).stream().filter(obj -> obj.getStatusValue((int)status) <= 0 && (obj.isPlayer() || obj.isPartner())).sorted(Comparator.comparing(FightObject::getCurrLife)).collect(Collectors.toList());
        if (list == null || list.size() <= 0) {
            list = (List)fight.getMySide((int)effector.getId()).stream().filter(obj -> obj.isPet()).sorted(Comparator.comparing(FightObject::getCurrLife)).collect(Collectors.toList());
            if (list == null || list.size() <= 0) {
                return null;
            }
            return list;
        }
        else {
            return list;
        }
    }
    
    public static void changePlayerPropery(Role role, NutMap propertyMap) {
        NutMap nutMap = countFuPlayerBookGain(role);
        for (String key : nutMap.keySet()) {
            if (key.equals(String.valueOf(PRO_DICT_TYPE.HP_CAP.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.MP_CAP.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.ATTACK.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.SPEED.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.HP_PCT.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.ATTACK_PCT.getDist()))) {
                continue;
            }
            if (key.equals(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()))) {
                continue;
            }
            excutePropertyMap(propertyMap, nutMap, key);
        }
    }
    
    public static void changePetPropery(Role role, Pet pet, NutMap map) {
        NutMap nutMap = countFuPetBookGain(role, pet);
        for (String key : nutMap.keySet()) {
            if (key.equals(String.valueOf(PRO_DICT_TYPE.HP_CAP.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.MP_CAP.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.ATTACK.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.SPEED.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.HP_PCT.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.ATTACK_PCT.getDist()))) {
                continue;
            }
            if (key.equals(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()))) {
                continue;
            }
            excutePropertyMap(map, nutMap, key);
        }
    }
    
    private static void excutePropertyMap(NutMap propertyMap, NutMap nutMap, String key) {
        if (!key.contains("_")) {
            propertyMap.setv(key, Double.valueOf(propertyMap.getDouble(key) + nutMap.getDouble(key)));
        }
        else {
            String[] keys = key.split("_");
            if (keys[1].equals(Integer.valueOf(121))) {
                double upLimit = nutMap.getDouble(keys[0] + 19);
                double pct = nutMap.getDouble(keys[0]);
                double selfValue = propertyMap.getDouble(keys[0]);
                double value = selfValue * pct;
                if (upLimit > 0.0) {
                    if (value > upLimit) {
                        value = upLimit;
                    }
                }
                else if (value < upLimit) {
                    value = upLimit;
                }
                propertyMap.setv(keys[0], Double.valueOf(selfValue + value));
            }
        }
    }
    
    public static double getPropertyForMap(NutMap nutMap, String key, double selfValue) {
        double changeValue = nutMap.getDouble(key, 0.0);
        double pct = nutMap.getDouble(key + "_" + 121, 0.0);
        if (pct != 0.0) {
            double upLimit = nutMap.getDouble(key + "_" + 19, 0.0);
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
    
    public static NutMap countFuPetBookGain(Role role, Pet pet) {
        NutMap map = new NutMap();
        if (role == null) {
            return map;
        }
        List<RoleTianFu> roleTianFus = DataCache.ROLE_TIAN_FU.get(role.getId());
        RoleDayInCe roleDayInCe = (RoleDayInCe)DataCache.ROLE_DAY_IN_CE.get(role.getId());
        if (roleTianFus == null || roleDayInCe == null) {
            return map;
        }
        for (RoleTianFu roleTianFu : roleTianFus) {
            if (roleTianFu.getIsWear() != 1) {
                continue;
            }
            if (roleTianFu.getTianFuSerial() == 9997) {
                continue;
            }
            if (roleTianFu.getTianFuSerial() == 321105 || DAY_IN_CE.DRIVER_BEASTS.getValue() == roleTianFu.getCategory()) {
                excuteTianFuProcessor(map, roleDayInCe, roleTianFu);
                excuteOtherProccessor(pet, map, roleDayInCe, roleTianFu);
            }
            else {
                continue;
            }
        }
        return map;
    }
    
    public static NutMap countFuPlayerBookGain(Role role) {
        NutMap map = new NutMap();
        List<RoleTianFu> roleTianFus = DataCache.ROLE_TIAN_FU.get(role.getId());
        RoleDayInCe roleDayInCe = (RoleDayInCe)DataCache.ROLE_DAY_IN_CE.get(role.getId());
        if (roleTianFus == null || roleDayInCe == null) {
            return map;
        }
        for (int i = 0; i < roleTianFus.size(); ++i) {
            RoleTianFu roleTianFu = (RoleTianFu)roleTianFus.get(i);
            if (countFuPlayerBookGain(roleTianFu, true)) {
                excuteTianFuProcessor(map, roleDayInCe, roleTianFu);
            }
        }
        return map;
    }
    
    public static boolean countFuPlayerBookGain(RoleTianFu roleTianFu, boolean flag) {
        return roleTianFu.getTianFuSerial() != 9997 && roleTianFu.getTianFuSerial() != 321105 && DAY_IN_CE.DRIVER_BEASTS.getValue() != roleTianFu.getCategory() && (!flag || roleTianFu.getIsWear() == 1);
    }
    
    private static void excuteTianFuProcessor(NutMap map, RoleDayInCe roleDayInCe, RoleTianFu roleTianFu) {
        if (roleTianFu.getIsWear() == 1) {
            TianFuProcessor tianFuProcessor = TianFuProcessor.queryGainProcessor(Integer.valueOf(roleTianFu.getTianFuSerial()));
            if (tianFuProcessor != null) {
                TianFuAddition tianFuAddition = new TianFuAddition(roleDayInCe.getLevel(), roleTianFu.getLevel());
                NutMap nutMap = tianFuProcessor.addition(tianFuAddition, new Object[0]);
                if (nutMap != null) {
                    for (String key : nutMap.keySet()) {
                        map.setv(key, Double.valueOf(map.getDouble(key) + nutMap.getDouble(key)));
                    }
                }
            }
        }
    }
    
    private static void excuteOtherProccessor(Pet pet, NutMap map, RoleDayInCe roleDayInCe, RoleTianFu roleTianFu) {
        if (roleTianFu.getIsWear() == 1) {
            TianFuProcessor tianFuProcessor = TianFuProcessor.queryGainProcessor(Integer.valueOf(roleTianFu.getTianFuSerial()));
            if (tianFuProcessor != null) {
                TianFuAddition tianFuAddition = new TianFuAddition(roleDayInCe.getLevel(), roleTianFu.getLevel());
                NutMap nutMap = tianFuProcessor.otherAddition(pet, tianFuAddition, new Object[0]);
                if (nutMap != null) {
                    for (String key : nutMap.keySet()) {
                        map.setv(key, Double.valueOf(map.getDouble(key) + nutMap.getDouble(key)));
                    }
                }
            }
        }
    }
    
    public static NutMap getWearInstantlyTakeEffect(Object object) {
        Role role = null;
        Pet pet = null;
        if ((object instanceof Pet)) {
            pet = (Pet)object;
            role = SpringUtils.getRoleService().getOnlinePlayer((int)pet.getRoleId());
        }
        else if ((object instanceof Role)) {
            role = (Role)object;
        }
        if (role != null) {
            NutMap map = NutMap.NEW();
            RoleDayInCe roleDayInCe = (RoleDayInCe)DataCache.ROLE_DAY_IN_CE.get(role.getId());
            List<RoleTianFu> roleTianFus = DataCache.ROLE_TIAN_FU.get(role.getId());
            if (roleTianFus == null || roleTianFus.size() <= 0) {
                return null;
            }
            List<RoleTianFu> wearList = (List)roleTianFus.stream().filter(obj -> obj.getIsWear() == 1 && DAY_IN_CE_TRIGGER_DICT.WEAR_INSTANTLY_TAKE_EFFECT.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            for (RoleTianFu roleTianFu : wearList) {
                TianFuProcessor tianFuProcessor = TianFuProcessor.queryGainProcessor(Integer.valueOf(roleTianFu.getTianFuSerial()));
                if (tianFuProcessor != null) {
                    TianFuAddition tianFuAddition = new TianFuAddition(roleDayInCe.getLevel(), roleTianFu.getLevel());
                    NutMap nutMap = tianFuProcessor.wearInstantlyTakeEffect(role, pet, tianFuAddition, new Object[0]);
                    if (nutMap != null) {
                        for (String key : nutMap.keySet()) {
                            map.setv(key, Double.valueOf(map.getDouble(key) + nutMap.getDouble(key)));
                        }
                    }
                    else {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
            return map;
        }
        else {
            return null;
        }
    }
    
    public static double getTianFuCount(byte tianFuLevel, short dayInCeLevel) {
        return (double)tianFuLevel * (1.0 + (double)dayInCeLevel * 0.002);
    }
    
    public static double getCountAddition(TianFuAddition tianFuAddition) {
        return getTianFuCount(tianFuAddition.getTianFuLevel(), tianFuAddition.getDayInCeLevel());
    }
    
    public static void excuteWearInstantlyTakeEffect(Role role, RoleTianFu roleTianFu) {
        if (DAY_IN_CE_TRIGGER_DICT.WEAR_INSTANTLY_TAKE_EFFECT.getValue().equals(roleTianFu.getTriggerType())) {
            if (323501 == roleTianFu.getTianFuSerial()) {
                PetService petService = (PetService)SpringUtils.getBean(PetService.class);
                Map<Integer, Pet> petMap = petService.selectPet(role);
                if (petMap.size() > 0) {
                    for (Pet pet : petMap.values()) {
                        petService.calculateBase((long)role.getId(), pet);
                        EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_CHANGE, pet, petService.getPetEffective((long)role.getId(), pet)));
                    }
                }
            }
            else {
                SpringUtils.getRoleService().calculateBase(role);
            }
        }
    }
    
    public static void clearTianFuAffect(FightObject fightObject, int status) {
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
        TianFuUtils.server2clientGridMap = new HashMap();
        TianFuUtils.client2serverGridMap = new HashMap();
        TianFuUtils.server2clientGridMap.put("gattaca11", Byte.valueOf((byte)4));
        TianFuUtils.server2clientGridMap.put("gattaca12", Byte.valueOf((byte)64));
        TianFuUtils.server2clientGridMap.put("gattaca21", Byte.valueOf((byte)16));
        TianFuUtils.server2clientGridMap.put("gattaca22", Byte.valueOf((byte)76));
        TianFuUtils.server2clientGridMap.put("gattaca31", Byte.valueOf((byte)28));
        TianFuUtils.server2clientGridMap.put("gattaca32", Byte.valueOf((byte)88));
        TianFuUtils.server2clientGridMap.put("gattaca41", Byte.valueOf((byte)40));
        TianFuUtils.server2clientGridMap.put("gattaca42", Byte.valueOf((byte)100));
        TianFuUtils.server2clientGridMap.put("gattaca51", Byte.valueOf((byte)52));
        TianFuUtils.server2clientGridMap.put("gattaca", Byte.valueOf((byte)1));
        TianFuUtils.server2clientGridMap.put("loadObj11", Byte.valueOf((byte)8));
        TianFuUtils.server2clientGridMap.put("loadObj12", Byte.valueOf((byte)68));
        TianFuUtils.server2clientGridMap.put("loadObj21", Byte.valueOf((byte)20));
        TianFuUtils.server2clientGridMap.put("loadObj22", Byte.valueOf((byte)80));
        TianFuUtils.server2clientGridMap.put("loadObj31", Byte.valueOf((byte)32));
        TianFuUtils.server2clientGridMap.put("loadObj32", Byte.valueOf((byte)92));
        TianFuUtils.server2clientGridMap.put("loadObj41", Byte.valueOf((byte)44));
        TianFuUtils.server2clientGridMap.put("loadObj42", Byte.valueOf((byte)104));
        TianFuUtils.server2clientGridMap.put("loadObj51", Byte.valueOf((byte)56));
        TianFuUtils.server2clientGridMap.put("loadObj", Byte.valueOf((byte)2));
        TianFuUtils.server2clientGridMap.put("driveBeasts11", Byte.valueOf((byte)12));
        TianFuUtils.server2clientGridMap.put("driveBeasts12", Byte.valueOf((byte)72));
        TianFuUtils.server2clientGridMap.put("driveBeasts21", Byte.valueOf((byte)24));
        TianFuUtils.server2clientGridMap.put("driveBeasts22", Byte.valueOf((byte)84));
        TianFuUtils.server2clientGridMap.put("driveBeasts31", Byte.valueOf((byte)36));
        TianFuUtils.server2clientGridMap.put("driveBeasts32", Byte.valueOf((byte)96));
        TianFuUtils.server2clientGridMap.put("driveBeasts41", Byte.valueOf((byte)48));
        TianFuUtils.server2clientGridMap.put("driveBeasts42", Byte.valueOf((byte)108));
        TianFuUtils.server2clientGridMap.put("driveBeasts51", Byte.valueOf((byte)60));
        TianFuUtils.server2clientGridMap.put("driveBeasts", Byte.valueOf((byte)3));
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)4), "gattaca11");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)64), "gattaca12");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)16), "gattaca21");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)76), "gattaca22");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)28), "gattaca31");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)88), "gattaca32");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)40), "gattaca41");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)100), "gattaca42");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)52), "gattaca51");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)1), "gattaca");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)8), "loadObj11");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)68), "loadObj12");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)20), "loadObj21");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)80), "loadObj22");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)32), "loadObj31");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)92), "loadObj32");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)44), "loadObj41");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)104), "loadObj42");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)56), "loadObj51");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)2), "loadObj");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)12), "driveBeasts11");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)72), "driveBeasts12");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)24), "driveBeasts21");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)84), "driveBeasts22");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)36), "driveBeasts31");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)96), "driveBeasts32");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)48), "driveBeasts41");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)108), "driveBeasts42");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)60), "driveBeasts51");
        TianFuUtils.client2serverGridMap.put(Byte.valueOf((byte)3), "driveBeasts");
    }
}
