package com.kitty.system.game.fightnew.book.acsry.utils;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.enums.common.ACC_GRADE;
import com.kitty.system.init.domain.SysDataAcsrySuit;
import java.util.Collections;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.init.domain.SysDataAcsry;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.vo.utils.StatusConst;
import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import java.util.Map;
import java.util.ArrayList;
import com.kitty.system.game.role.domain.Role;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillConst;
import com.kitty.system.enums.war.warskill.skilldef.PETSKILLATTR;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.Iterator;
import java.util.List;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.fightnew.book.acsry.def.ACSRY_SKILL_TRIGGER_DICT;
import com.kitty.system.game.skill.pool.SkillDataPool;
import java.util.Set;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;

public class AcsrySkillUtil
{
    public AcsrySkillUtil() {
    }
    
    public static void roundStart(Fight fight, FightObject fightObject) {
        if (fightObject.isPlayer()) {
            PlayerFightObject playerFightObject = (PlayerFightObject)fightObject;
            List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
            if (acsrySkillList != null && acsrySkillList.size() > 0) {
                for (Integer acsrySerial : acsrySkillList) {
                    if (((Set)SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.BEFORE_ROUND_START.getValue())).contains(acsrySerial)) {
                        AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(acsrySerial);
                        processor.passive(fight, playerFightObject, new Object[0]);
                    }
                    else {
                        continue;
                    }
                }
            }
        }
        Set<Integer> skillSerials = (Set)SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.BEFORE_ROUND_START_TARGET.getValue());
        if (skillSerials != null && skillSerials.size() > 0) {
            for (Integer skillSerial : skillSerials) {
                AcsrySkillProcessor processor2 = AcsrySkillProcessor.queryProcessor(skillSerial);
                processor2.passive(fight, fightObject, new Object[0]);
            }
        }
    }
    
    public static void roundEnd(Fight fight, FightObject fightObject) {
        if (fightObject.isPlayer()) {
            PlayerFightObject playerFightObject = (PlayerFightObject)fightObject;
            List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
            if (acsrySkillList == null || acsrySkillList.size() <= 0) {
                return;
            }
            for (Integer acsrySerial : acsrySkillList) {
                if (((Set)SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.ROUND_END.getValue())).contains(acsrySerial)) {
                    AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(acsrySerial);
                    processor.passive(fight, playerFightObject, new Object[0]);
                }
                else {
                    continue;
                }
            }
        }
    }
    
    public static void attackEnd(Fight fight, FightObject effecter, FightObject target, RoleSkill roleSkill, boolean isHit, List<Integer> clearStatusList, int targetHurt, int targetMpHurt, FightObject sideTarget, ActionDetailParam actionDetailParam) {
        if (roleSkill == null) {
            roleSkill = new RoleSkill();
            roleSkill.setType(Integer.valueOf(PETSKILLATTR.PETSKILLATTR_WULI.getValue()));
            roleSkill.setSkillId(Integer.valueOf(0));
        }
        if (effecter.isPlayer()) {
            PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
            if (acsrySkillList != null && acsrySkillList.size() > 0) {
                for (Integer acsrySerial : acsrySkillList) {
                    if (((Set)SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.ATTACK_END.getValue())).contains(acsrySerial) && AcsrySkillConst.getAttackEndEffector().contains(acsrySerial)) {
                        AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(acsrySerial);
                        processor.attackOver(fight, playerFightObject, target, actionDetailParam, roleSkill, Boolean.valueOf(isHit), clearStatusList, Integer.valueOf(targetHurt), Integer.valueOf(targetMpHurt), Boolean.valueOf(true), sideTarget);
                    }
                    else {
                        continue;
                    }
                }
            }
        }
        if (target.isPlayer()) {
            PlayerFightObject playerFightObject = (PlayerFightObject)target;
            List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
            if (acsrySkillList != null && acsrySkillList.size() > 0) {
                for (Integer acsrySerial : acsrySkillList) {
                    if (((Set)SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.ATTACK_END.getValue())).contains(acsrySerial) && AcsrySkillConst.getAttackEndTarget().contains(acsrySerial)) {
                        AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(acsrySerial);
                        processor.attackOver(fight, effecter, target, actionDetailParam, roleSkill, Boolean.valueOf(isHit), clearStatusList, Integer.valueOf(targetHurt), Integer.valueOf(targetMpHurt), Boolean.valueOf(true));
                    }
                    else {
                        continue;
                    }
                }
            }
        }
        String string = effecter.getStatusMap().getString("1008perpetrator", null);
        if (string != null) {
            if (!string.contains(String.valueOf(73323)) && !string.contains(String.valueOf(73322))) {
                return;
            }
            try {
                String[] splits = string.split("_");
                AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(Integer.valueOf(Integer.parseInt(splits[0])));
                processor.attackOver(fight, effecter, target, actionDetailParam, roleSkill, Boolean.valueOf(isHit), clearStatusList, Integer.valueOf(targetHurt), Integer.valueOf(targetMpHurt), Boolean.valueOf(false));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static boolean magicSelectTarget(Fight fight, FightObject fightObject, RoleSkill roleSkill) {
        if (fightObject.isPlayer()) {
            PlayerFightObject playerFightObject = (PlayerFightObject)fightObject;
            List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
            if (acsrySkillList == null || acsrySkillList.size() <= 0) {
                return false;
            }
            if (roleSkill == null) {
                roleSkill = new RoleSkill();
                roleSkill.setType(Integer.valueOf(PETSKILLATTR.PETSKILLATTR_WULI.getValue()));
                roleSkill.setSkillId(Integer.valueOf(0));
            }
            boolean flag = false;
            for (Integer acsrySerial : acsrySkillList) {
                if (((Set)SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.MAGIC_SELECT_TARGET.getValue())).contains(acsrySerial)) {
                    AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(acsrySerial);
                    flag = processor.magicSelectTarget(fight, playerFightObject, roleSkill);
                    if (flag) {
                        break;
                    }
                    continue;
                }
                else {
                    continue;
                }
            }
            return flag;
        }
        else {
            return false;
        }
    }
    
    public static void statusOver(Fight fight, FightObject fightObject, int status) {
        try {
            Set<Integer> serials = (Set)SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.STATUS_OVER.getValue());
            if (serials != null && serials.size() > 0) {
                for (Integer serial : serials) {
                    AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(serial);
                    processor.statusOver(fight, fightObject, Integer.valueOf(status));
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static int executeReduceHp(Fight fight, FightObject effecter, FightObject target, RoleSkill roleSkill, int hurt, List<Integer> clearStatusList) {
        int changeHurt = 0;
        if (roleSkill == null) {
            roleSkill = new RoleSkill();
            roleSkill.setType(Integer.valueOf(PETSKILLATTR.PETSKILLATTR_WULI.getValue()));
            roleSkill.setSkillId(Integer.valueOf(0));
        }
        if (effecter.isPlayer()) {
            PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
            if (acsrySkillList != null && acsrySkillList.size() > 0) {
                for (Integer acsrySerial : acsrySkillList) {
                    if (((Set)SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.EXECUTE_REDUCE_HP.getValue())).contains(acsrySerial)) {
                        AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(acsrySerial);
                        NutMap map = processor.executeReduceHp(fight, playerFightObject, target, roleSkill, Integer.valueOf(hurt), clearStatusList);
                        if (map != null) {
                            changeHurt += map.getInt("changeHurt", 0);
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
        }
        String string = effecter.getStatusMap().getString("1008perpetrator", null);
        if (string != null) {
            if (string.contains(String.valueOf(73323)) || string.contains(String.valueOf(73322))) {
                try {
                    String[] splits = string.split("_");
                    AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(Integer.valueOf(Integer.parseInt(splits[0])));
                    NutMap map = processor.executeReduceHp(fight, effecter, target, roleSkill, Integer.valueOf(hurt), clearStatusList);
                    if (map != null) {
                        changeHurt += map.getInt("changeHurt", 0);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        String str = effecter.getOtherMap().getString("skillRound", null);
        if (str != null) {
            if (!str.contains(String.valueOf(73332)) && !str.contains(String.valueOf(73333))) {
                return changeHurt;
            }
            try {
                String[] splits2 = str.split("_");
                AcsrySkillProcessor processor2 = AcsrySkillProcessor.queryProcessor(Integer.valueOf(Integer.parseInt(splits2[0])));
                NutMap map2 = processor2.executeReduceHp(fight, effecter, target, roleSkill, Integer.valueOf(hurt), clearStatusList);
                if (map2 != null) {
                    changeHurt += map2.getInt("changeHurt", 0);
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return changeHurt;
    }
    
    public static void afterPrintCommand(Fight fight) {
        for (FightObject fightObject : fight.getAllFightObjects()) {
            if (fightObject.isPlayer()) {
                PlayerFightObject playerFightObject = (PlayerFightObject)fightObject;
                List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
                if (acsrySkillList != null && acsrySkillList.size() > 0) {
                    for (Integer skillSerial : acsrySkillList) {
                        if (((Set)SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.AFTER_INPUT_INSTRUCT.getValue())).contains(skillSerial)) {
                            AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(skillSerial);
                            processor.afterPrintCommand(fight, fightObject, new Object[0]);
                        }
                        else {
                            continue;
                        }
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
    }
    
    public static int countRecoverBloodEnd(Fight fight, FightObject fightObject, RoleSkill skill, int life) {
        int changeLife = 0;
        for (Integer serial : SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.COUNT_RECOVER_BLOOD_END.getValue())) {
            AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(serial);
            NutMap map = processor.countRecoverBloodEnd(fight, fightObject, Integer.valueOf(life), skill);
            if (map != null) {
                changeLife += map.getInt("changeLife", 0);
            }
            else {
                continue;
            }
        }
        return changeLife;
    }
    
    public static int countRecoverMpEnd(Fight fight, FightObject fightObject, int mana) {
        int changeMana = 0;
        for (Integer serial : SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.COUNT_RECOVER_MP_END.getValue())) {
            AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(serial);
            NutMap map = processor.countRecoverMpEnd(fight, fightObject, Integer.valueOf(mana));
            if (map != null) {
                changeMana += map.getInt("changeMana", 0);
            }
            else {
                continue;
            }
        }
        return changeMana;
    }
    
    public static int countRecoverBloodEndSS(Fight fight, FightObject effecter, FightObject target, RoleSkill roleSkill, int life) {
        int changeLife = 0;
        if (effecter.isPlayer()) {
            PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
            if (acsrySkillList == null || acsrySkillList.size() <= 0) {
                return 0;
            }
            if (roleSkill == null) {
                roleSkill = new RoleSkill();
                roleSkill.setType(Integer.valueOf(PETSKILLATTR.PETSKILLATTR_WULI.getValue()));
                roleSkill.setSkillId(Integer.valueOf(0));
            }
            for (Integer acsrySerial : acsrySkillList) {
                if (((Set)SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.COUNT_RECOVER_BLOOD_END_SS.getValue())).contains(acsrySerial)) {
                    AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(acsrySerial);
                    NutMap map = processor.countRecoverBloodEndSS(fight, playerFightObject, target, Integer.valueOf(life), roleSkill);
                    if (map != null) {
                        changeLife += map.getInt("changeLife", 0);
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
        return changeLife;
    }
    
    public static int reduceMp(Fight fight, FightObject fightObject, int mana) {
        int changeMana = 0;
        for (Integer serial : SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.REDUCE_MP.getValue())) {
            AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(serial);
            NutMap map = processor.reduceMp(fight, fightObject, Integer.valueOf(mana));
            if (map != null) {
                changeMana += map.getInt("changeMana", 0);
            }
            else {
                continue;
            }
        }
        return changeMana;
    }
    
    public static void beforeExecuteMagic(Fight fight, FightObject effecter, FightObject target, RoleSkill skill) {
        if (effecter.isPlayer()) {
            PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
            for (Integer skillSerial : acsrySkillList) {
                if (((Set)SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.BEFORE_EXECUTE_MAGIC.getValue())).contains(skillSerial)) {
                    AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(skillSerial);
                    if (processor != null) {
                        processor.beforeExecuteMagic(fight, effecter, target, skill);
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
    }
    
    public static int poisonReduceHp(Fight fight, FightObject target, int hurt) {
        int changeHurt = 0;
        Set<Integer> serials = (Set)SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.POISON_REDUCE_HP.getValue());
        if (serials.size() > 0) {
            for (Integer serial : serials) {
                AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(serial);
                if (processor != null) {
                    NutMap map = processor.poisonReduceHp(fight, target, Integer.valueOf(hurt));
                    if (map != null) {
                        changeHurt += map.getInt("changeHurt", 0);
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
        return changeHurt;
    }
    
    public static int beforeReduceHurtMp(Fight fight, FightObject effecter, FightObject target, RoleSkill skill, int mp) {
        int changeMp = 0;
        if (target.isPlayer()) {
            PlayerFightObject playerTarget = (PlayerFightObject)target;
            List<Integer> acsrySkillList = playerTarget.getAcsrySkillList();
            if (acsrySkillList.size() > 0) {
                for (Integer serial : acsrySkillList) {
                    AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(serial);
                    if (processor != null) {
                        NutMap map = processor.beforeReduceHurtMp(fight, effecter, target, skill, Integer.valueOf(mp));
                        if (map != null) {
                            changeMp += map.getInt("changeMana", 0);
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
        }
        return changeMp;
    }
    
    public static NutMap initializeFightObject(Role role) {
        List<Integer> acsrySkillList = getAcsrySkillList(role);
        NutMap nutMap = NutMap.NEW();
        if (acsrySkillList != null && acsrySkillList.size() > 0) {
            for (Integer serial : acsrySkillList) {
                if (((Set)SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.INITIALIZE_FIGHT_OBJECT.getValue())).contains(serial)) {
                    AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(serial);
                    if (processor != null) {
                        NutMap map = processor.addition(role, acsrySkillList);
                        if (map != null) {
                            for (String key : map.keySet()) {
                                nutMap.setv(key, Double.valueOf(nutMap.getDouble(key) + map.getDouble(key)));
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
                else {
                    continue;
                }
            }
            return nutMap;
        }
        return nutMap;
    }
    
    public static NutMap wearTakeEffect(Role role) {
        List<Integer> acsrySkillList = getAcsrySkillList(role);
        if (acsrySkillList != null && acsrySkillList.size() > 0) {
            NutMap nutMap = NutMap.NEW();
            for (Integer serial : acsrySkillList) {
                if (((Set)SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.WEAR_TAKE_EFFECT.getValue())).contains(serial)) {
                    AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(serial);
                    if (processor != null) {
                        NutMap map = processor.additionWear(role, new Object[0]);
                        if (map != null) {
                            for (String key : map.keySet()) {
                                nutMap.setv(key, Double.valueOf(nutMap.getDouble(key) + map.getDouble(key)));
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
                else {
                    continue;
                }
            }
            return nutMap;
        }
        return null;
    }
    
    public static void beforePhysicalAttack(Fight fight, FightObject effecter, FightObject target) {
        if (effecter.isPlayer()) {
            PlayerFightObject playerEffecter = (PlayerFightObject)effecter;
            List<Integer> acsrySkillList = playerEffecter.getAcsrySkillList();
            for (Integer skillSerial : acsrySkillList) {
                if (((Set)SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.BEFORE_PHYSICAL_ATTACK.getValue())).contains(skillSerial)) {
                    AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(skillSerial);
                    if (processor != null) {
                        processor.beforePhysicalAttack(fight, effecter, target, new Object[0]);
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
    }
    
    public static void excutePropertyChange(FightObject fightObject) {
        if (fightObject.isPlayer()) {
            PlayerFightObject playerFightObject = (PlayerFightObject)fightObject;
            NutMap acsryPropertyMap = playerFightObject.getAcsryPropertyMap();
            List<String> deleteProperies = new ArrayList<>();
            for (Map.Entry<String, Object> entry : acsryPropertyMap.entrySet()) {
                String key = (String)entry.getKey();
                Object value = entry.getValue();
                if (!key.matches("^[0-9]+$")) {
                    continue;
                }
                if (!(value instanceof Integer)) {
                    continue;
                }
                int valueInt = Integer.parseInt(value.toString());
                if (valueInt <= 1) {
                    valueInt = 0;
                    deleteProperies.add(key);
                }
                else {
                    --valueInt;
                }
                acsryPropertyMap.setv(key, Integer.valueOf(valueInt));
            }
            if (deleteProperies.size() > 0) {
                for (int i = 0; i < deleteProperies.size(); ++i) {
                    removeProperty(playerFightObject, (String)deleteProperies.get(i));
                }
            }
        }
    }
    
    private static void removeProperty(PlayerFightObject playerFightObject, String distStr) {
        playerFightObject.getAcsryPropertyMap().remove(distStr);
        playerFightObject.getAcsryPropertyMap().remove(distStr + "value");
    }
    
    public static void excutePropertyType(PlayerFightObject playerfightObject, int dist) {
        if (PRO_DICT_TYPE.OMIT_K_F.getDist() == dist || PRO_DICT_TYPE.OMIT_K_L.getDist() == dist || PRO_DICT_TYPE.OMIT_K_S.getDist() == dist || PRO_DICT_TYPE.OMIT_K_H.getDist() == dist) {
            removeProperty(playerfightObject, String.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist()));
            removeProperty(playerfightObject, String.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist()));
            removeProperty(playerfightObject, String.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist()));
            removeProperty(playerfightObject, String.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist()));
        }
        else {
            removeProperty(playerfightObject, String.valueOf(dist));
        }
    }
    
    public static int getChageBaseHurt(FightObject fightObject) {
        try {
            if (fightObject.isPlayer()) {
                PlayerFightObject playerFightObject = (PlayerFightObject)fightObject;
                int chageHurt = playerFightObject.getAcsryPropertyMap().getInt("baseHurt", 0);
                return chageHurt;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public static boolean isUnlock(Fight fight, FightObject fightObject, Integer status) {
        try {
            int round = fightObject.getStatusMap().getInt(status + "statusConstraint", 0);
            if (round > 0 && round <= WarMaxRound.WAR_MAX_ROUND.getValue()) {
                fightObject.getStatusMap().setv(status + "statusConstraint", Integer.valueOf(--round));
                return true;
            }
            if (round > WarMaxRound.WAR_MAX_ROUND.getValue()) {
                int count = 0;
                List<FightObject> mySide = fight.getMySide((int)fightObject.getId());
                if (mySide.size() > 0) {
                    for (int i = 0; i < mySide.size(); ++i) {
                        FightObject obj = (FightObject)mySide.get(i);
                        if (obj.isFallDown()) {
                            ++count;
                        }
                        else {
                            for (Integer j : StatusConst.getControlStatus()) {
                                if (obj.getStatusValue((int)j) > 0) {
                                    ++count;
                                    break;
                                }
                                continue;
                            }
                        }
                    }
                    if ((double)(count / mySide.size()) <= 0.4) {
                        return true;
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<Integer> getAcsrySkillList(Role role) {
        List<Integer> acsrySkillList = new ArrayList<>();
        List<WearEquip> wearEquips = (List)DataCache.EQUIP_MAP.get(role.getId());
        List<String> suitTypes = new ArrayList<>();
        List<Byte> acsryProductOrders = new ArrayList<>();
        for (int i = 0; i < wearEquips.size(); ++i) {
            if (((WearEquip)wearEquips.get(i)).getPosition() > 5) {
                SysDataAcsry sysDataAcsry = (SysDataAcsry)EquipDataPool.acsryMap.get(Integer.valueOf(((WearEquip)wearEquips.get(i)).getType()));
                if (sysDataAcsry != null) {
                    suitTypes.add(sysDataAcsry.getSuiteType());
                    acsryProductOrders.add(Byte.valueOf(((WearEquip)wearEquips.get(i)).getProductOrder()));
                }
            }
        }
        long count = suitTypes.stream().distinct().count();
        if (suitTypes.size() >= 5 && count == 1L) {
            Byte min = (Byte)Collections.min(acsryProductOrders);
            SysDataAcsrySuit sysDataAcsrySuit = (SysDataAcsrySuit)EquipDataPool.acsrySuitMap.get(suitTypes.get(0));
            if (sysDataAcsrySuit != null) {
                List<Integer> skills = sysDataAcsrySuit.getSkills();
                if (ACC_GRADE.PRICELESS.getValue() == (byte)min) {
                    for (Integer skill : skills) {
                        acsrySkillList.add(skill);
                    }
                }
                else if (ACC_GRADE.RESERVE.getValue() == (byte)min) {
                    for (Integer skill : skills) {
                        if ((int)skill % 10000 % 1000 % 100 % 10 < 3) {
                            acsrySkillList.add(skill);
                        }
                        else {
                            continue;
                        }
                    }
                }
                else if (ACC_GRADE.PLAY_WITH.getValue() == (byte)min) {
                    for (Integer skill : skills) {
                        if ((int)skill % 10000 % 1000 % 100 % 10 < 2) {
                            acsrySkillList.add(skill);
                        }
                        else {
                            continue;
                        }
                    }
                }
            }
        }
        return acsrySkillList;
    }
    
    public static SysDataAcsrySuit getAcsrySuit(Role role) {
        List<WearEquip> wearEquips = SpringUtils.getEquipService().selectWearEquip(role);
        List<String> suitTypes = new ArrayList<>();
        for (int i = 0; i < wearEquips.size(); ++i) {
            if (((WearEquip)wearEquips.get(i)).getPosition() > 5) {
                SysDataAcsry sysDataAcsry = (SysDataAcsry)EquipDataPool.acsryMap.get(Integer.valueOf(((WearEquip)wearEquips.get(i)).getType()));
                if (sysDataAcsry != null) {
                    suitTypes.add(sysDataAcsry.getSuiteType());
                }
            }
        }
        long count = suitTypes.stream().distinct().count();
        if (suitTypes.size() >= 5 && count == 1L) {
            SysDataAcsrySuit sysDataAcsrySuit = (SysDataAcsrySuit)EquipDataPool.acsrySuitMap.get(suitTypes.get(0));
            return sysDataAcsrySuit;
        }
        return null;
    }
}
