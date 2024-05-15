
package com.kitty.system.game.fightnew.book.acsry.utils;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.enums.common.ACC_GRADE;
import com.kitty.system.init.domain.SysDataAcsrySuit;
import java.util.Collection;
import java.util.Collections;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.init.domain.SysDataAcsry;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.equip.domain.WearEquip;
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
    public static void roundStart(final Fight fight, final FightObject fightObject) {
        if (fightObject.isPlayer()) {
            final PlayerFightObject playerFightObject = (PlayerFightObject)fightObject;
            final List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
            if (acsrySkillList != null && acsrySkillList.size() > 0) {
                for (final Integer acsrySerial : acsrySkillList) {
                    if (SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.BEFORE_ROUND_START.getValue()).contains(acsrySerial)) {
                        final AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(acsrySerial);
                        processor.passive(fight, playerFightObject);
                    }
                }
            }
        }
        final Set<Integer> skillSerials = SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.BEFORE_ROUND_START_TARGET.getValue());
        if (skillSerials != null && skillSerials.size() > 0) {
            for (final Integer skillSerial : skillSerials) {
                final AcsrySkillProcessor processor2 = AcsrySkillProcessor.queryProcessor(skillSerial);
                processor2.passive(fight, fightObject);
            }
        }
    }
    
    public static void roundEnd(final Fight fight, final FightObject fightObject) {
        if (fightObject.isPlayer()) {
            final PlayerFightObject playerFightObject = (PlayerFightObject)fightObject;
            final List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
            if (acsrySkillList == null || acsrySkillList.size() <= 0) {
                return;
            }
            for (final Integer acsrySerial : acsrySkillList) {
                if (SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.ROUND_END.getValue()).contains(acsrySerial)) {
                    final AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(acsrySerial);
                    processor.passive(fight, playerFightObject);
                }
            }
        }
    }
    
    public static void attackEnd(final Fight fight, final FightObject effecter, final FightObject target, RoleSkill roleSkill, final boolean isHit, final List<Integer> clearStatusList, final int targetHurt, final int targetMpHurt, final FightObject sideTarget, final ActionDetailParam actionDetailParam) {
        if (roleSkill == null) {
            roleSkill = new RoleSkill();
            roleSkill.setType(PETSKILLATTR.PETSKILLATTR_WULI.getValue());
            roleSkill.setSkillId(0);
        }
        if (effecter.isPlayer()) {
            final PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            final List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
            if (acsrySkillList != null && acsrySkillList.size() > 0) {
                for (final Integer acsrySerial : acsrySkillList) {
                    if (SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.ATTACK_END.getValue()).contains(acsrySerial) && AcsrySkillConst.getAttackEndEffector().contains(acsrySerial)) {
                        final AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(acsrySerial);
                        processor.attackOver(fight, playerFightObject, target, actionDetailParam, roleSkill, isHit, clearStatusList, targetHurt, targetMpHurt, true, sideTarget);
                    }
                }
            }
        }
        if (target.isPlayer()) {
            final PlayerFightObject playerFightObject = (PlayerFightObject)target;
            final List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
            if (acsrySkillList != null && acsrySkillList.size() > 0) {
                for (final Integer acsrySerial : acsrySkillList) {
                    if (SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.ATTACK_END.getValue()).contains(acsrySerial) && AcsrySkillConst.getAttackEndTarget().contains(acsrySerial)) {
                        final AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(acsrySerial);
                        processor.attackOver(fight, effecter, target, actionDetailParam, roleSkill, isHit, clearStatusList, targetHurt, targetMpHurt, true);
                    }
                }
            }
        }
        final String string = effecter.getStatusMap().getString("1008perpetrator", null);
        if (string != null) {
            if (!string.contains(String.valueOf(73323))) {
                if (!string.contains(String.valueOf(73322))) {
                    return;
                }
            }
            try {
                final String[] splits = string.split("_");
                final AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(Integer.parseInt(splits[0]));
                processor.attackOver(fight, effecter, target, actionDetailParam, roleSkill, isHit, clearStatusList, targetHurt, targetMpHurt, false);
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static boolean magicSelectTarget(final Fight fight, final FightObject fightObject, RoleSkill roleSkill) {
        if (!fightObject.isPlayer()) {
            return false;
        }
        final PlayerFightObject playerFightObject = (PlayerFightObject)fightObject;
        final List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
        if (acsrySkillList == null || acsrySkillList.size() <= 0) {
            return false;
        }
        if (roleSkill == null) {
            roleSkill = new RoleSkill();
            roleSkill.setType(PETSKILLATTR.PETSKILLATTR_WULI.getValue());
            roleSkill.setSkillId(0);
        }
        boolean flag = false;
        for (final Integer acsrySerial : acsrySkillList) {
            if (SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.MAGIC_SELECT_TARGET.getValue()).contains(acsrySerial)) {
                final AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(acsrySerial);
                flag = processor.magicSelectTarget(fight, playerFightObject, roleSkill);
                if (flag) {
                    break;
                }
                continue;
            }
        }
        return flag;
    }
    
    public static void statusOver(final Fight fight, final FightObject fightObject, final int status) {
        try {
            final Set<Integer> serials = SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.STATUS_OVER.getValue());
            if (serials != null && serials.size() > 0) {
                for (final Integer serial : serials) {
                    final AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(serial);
                    processor.statusOver(fight, fightObject, status);
                }
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static int executeReduceHp(final Fight fight, final FightObject effecter, final FightObject target, RoleSkill roleSkill, final int hurt, final List<Integer> clearStatusList) {
        int changeHurt = 0;
        if (roleSkill == null) {
            roleSkill = new RoleSkill();
            roleSkill.setType(PETSKILLATTR.PETSKILLATTR_WULI.getValue());
            roleSkill.setSkillId(0);
        }
        if (effecter.isPlayer()) {
            final PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            final List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
            if (acsrySkillList != null && acsrySkillList.size() > 0) {
                for (final Integer acsrySerial : acsrySkillList) {
                    if (SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.EXECUTE_REDUCE_HP.getValue()).contains(acsrySerial)) {
                        final AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(acsrySerial);
                        final NutMap map = processor.executeReduceHp(fight, playerFightObject, target, roleSkill, hurt, clearStatusList);
                        if (map == null) {
                            continue;
                        }
                        changeHurt += map.getInt("changeHurt", 0);
                    }
                }
            }
        }
        final String string = effecter.getStatusMap().getString("1008perpetrator", null);
        Label_0315: {
            if (string != null) {
                if (!string.contains(String.valueOf(73323))) {
                    if (!string.contains(String.valueOf(73322))) {
                        break Label_0315;
                    }
                }
                try {
                    final String[] splits = string.split("_");
                    final AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(Integer.parseInt(splits[0]));
                    final NutMap map = processor.executeReduceHp(fight, effecter, target, roleSkill, hurt, clearStatusList);
                    if (map != null) {
                        changeHurt += map.getInt("changeHurt", 0);
                    }
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
        final String str = effecter.getOtherMap().getString("skillRound", null);
        if (str != null) {
            if (!str.contains(String.valueOf(73332))) {
                if (!str.contains(String.valueOf(73333))) {
                    return changeHurt;
                }
            }
            try {
                final String[] splits2 = str.split("_");
                final AcsrySkillProcessor processor2 = AcsrySkillProcessor.queryProcessor(Integer.parseInt(splits2[0]));
                final NutMap map2 = processor2.executeReduceHp(fight, effecter, target, roleSkill, hurt, clearStatusList);
                if (map2 != null) {
                    changeHurt += map2.getInt("changeHurt", 0);
                }
            }
            catch (final Exception e2) {
                e2.printStackTrace();
            }
        }
        return changeHurt;
    }
    
    public static void afterPrintCommand(final Fight fight) {
        for (final FightObject fightObject : fight.getAllFightObjects()) {
            if (fightObject.isPlayer()) {
                final PlayerFightObject playerFightObject = (PlayerFightObject)fightObject;
                final List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
                if (acsrySkillList == null || acsrySkillList.size() <= 0) {
                    continue;
                }
                for (final Integer skillSerial : acsrySkillList) {
                    if (SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.AFTER_INPUT_INSTRUCT.getValue()).contains(skillSerial)) {
                        final AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(skillSerial);
                        processor.afterPrintCommand(fight, fightObject);
                    }
                }
            }
        }
    }
    
    public static int countRecoverBloodEnd(final Fight fight, final FightObject fightObject, final RoleSkill skill, final int life) {
        int changeLife = 0;
        for (final Integer serial : SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.COUNT_RECOVER_BLOOD_END.getValue())) {
            final AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(serial);
            final NutMap map = processor.countRecoverBloodEnd(fight, fightObject, life, skill);
            if (map != null) {
                changeLife += map.getInt("changeLife", 0);
            }
        }
        return changeLife;
    }
    
    public static int countRecoverMpEnd(final Fight fight, final FightObject fightObject, final int mana) {
        int changeMana = 0;
        for (final Integer serial : SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.COUNT_RECOVER_MP_END.getValue())) {
            final AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(serial);
            final NutMap map = processor.countRecoverMpEnd(fight, fightObject, mana);
            if (map != null) {
                changeMana += map.getInt("changeMana", 0);
            }
        }
        return changeMana;
    }
    
    public static int countRecoverBloodEndSS(final Fight fight, final FightObject effecter, final FightObject target, RoleSkill roleSkill, final int life) {
        int changeLife = 0;
        if (effecter.isPlayer()) {
            final PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            final List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
            if (acsrySkillList == null || acsrySkillList.size() <= 0) {
                return 0;
            }
            if (roleSkill == null) {
                roleSkill = new RoleSkill();
                roleSkill.setType(PETSKILLATTR.PETSKILLATTR_WULI.getValue());
                roleSkill.setSkillId(0);
            }
            for (final Integer acsrySerial : acsrySkillList) {
                if (SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.COUNT_RECOVER_BLOOD_END_SS.getValue()).contains(acsrySerial)) {
                    final AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(acsrySerial);
                    final NutMap map = processor.countRecoverBloodEndSS(fight, playerFightObject, target, life, roleSkill);
                    if (map == null) {
                        continue;
                    }
                    changeLife += map.getInt("changeLife", 0);
                }
            }
        }
        return changeLife;
    }
    
    public static int reduceMp(final Fight fight, final FightObject fightObject, final int mana) {
        int changeMana = 0;
        for (final Integer serial : SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.REDUCE_MP.getValue())) {
            final AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(serial);
            final NutMap map = processor.reduceMp(fight, fightObject, mana);
            if (map != null) {
                changeMana += map.getInt("changeMana", 0);
            }
        }
        return changeMana;
    }
    
    public static void beforeExecuteMagic(final Fight fight, final FightObject effecter, final FightObject target, final RoleSkill skill) {
        if (effecter.isPlayer()) {
            final PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            final List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
            for (final Integer skillSerial : acsrySkillList) {
                if (SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.BEFORE_EXECUTE_MAGIC.getValue()).contains(skillSerial)) {
                    final AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(skillSerial);
                    if (processor == null) {
                        continue;
                    }
                    processor.beforeExecuteMagic(fight, effecter, target, skill);
                }
            }
        }
    }
    
    public static int poisonReduceHp(final Fight fight, final FightObject target, final int hurt) {
        int changeHurt = 0;
        final Set<Integer> serials = SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.POISON_REDUCE_HP.getValue());
        if (serials.size() > 0) {
            for (final Integer serial : serials) {
                final AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(serial);
                if (processor != null) {
                    final NutMap map = processor.poisonReduceHp(fight, target, hurt);
                    if (map == null) {
                        continue;
                    }
                    changeHurt += map.getInt("changeHurt", 0);
                }
            }
        }
        return changeHurt;
    }
    
    public static int beforeReduceHurtMp(final Fight fight, final FightObject effecter, final FightObject target, final RoleSkill skill, final int mp) {
        int changeMp = 0;
        if (target.isPlayer()) {
            final PlayerFightObject playerTarget = (PlayerFightObject)target;
            final List<Integer> acsrySkillList = playerTarget.getAcsrySkillList();
            if (acsrySkillList.size() > 0) {
                for (final Integer serial : acsrySkillList) {
                    final AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(serial);
                    if (processor != null) {
                        final NutMap map = processor.beforeReduceHurtMp(fight, effecter, target, skill, mp);
                        if (map == null) {
                            continue;
                        }
                        changeMp += map.getInt("changeMana", 0);
                    }
                }
            }
        }
        return changeMp;
    }
    
    public static NutMap initializeFightObject(final Role role) {
        final List<Integer> acsrySkillList = getAcsrySkillList(role);
        final NutMap nutMap = NutMap.NEW();
        if (acsrySkillList != null && acsrySkillList.size() > 0) {
            for (final Integer serial : acsrySkillList) {
                if (SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.INITIALIZE_FIGHT_OBJECT.getValue()).contains(serial)) {
                    final AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(serial);
                    if (processor == null) {
                        continue;
                    }
                    final NutMap map = processor.addition(role, acsrySkillList);
                    if (map == null) {
                        continue;
                    }
                    for (final String key : map.keySet()) {
                        nutMap.setv(key, (Object)(nutMap.getDouble(key) + map.getDouble(key)));
                    }
                }
            }
        }
        return nutMap;
    }
    
    public static NutMap wearTakeEffect(final Role role) {
        final List<Integer> acsrySkillList = getAcsrySkillList(role);
        if (acsrySkillList != null && acsrySkillList.size() > 0) {
            final NutMap nutMap = NutMap.NEW();
            for (final Integer serial : acsrySkillList) {
                if (SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.WEAR_TAKE_EFFECT.getValue()).contains(serial)) {
                    final AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(serial);
                    if (processor == null) {
                        continue;
                    }
                    final NutMap map = processor.additionWear(role);
                    if (map == null) {
                        continue;
                    }
                    for (final String key : map.keySet()) {
                        nutMap.setv(key, (Object)(nutMap.getDouble(key) + map.getDouble(key)));
                    }
                }
            }
            return nutMap;
        }
        return null;
    }
    
    public static void beforePhysicalAttack(final Fight fight, final FightObject effecter, final FightObject target) {
        if (effecter.isPlayer()) {
            final PlayerFightObject playerEffecter = (PlayerFightObject)effecter;
            final List<Integer> acsrySkillList = playerEffecter.getAcsrySkillList();
            for (final Integer skillSerial : acsrySkillList) {
                if (SkillDataPool.acsrySkillSerialType.get(ACSRY_SKILL_TRIGGER_DICT.BEFORE_PHYSICAL_ATTACK.getValue()).contains(skillSerial)) {
                    final AcsrySkillProcessor processor = AcsrySkillProcessor.queryProcessor(skillSerial);
                    if (processor == null) {
                        continue;
                    }
                    processor.beforePhysicalAttack(fight, effecter, target);
                }
            }
        }
    }
    
    public static void excutePropertyChange(final FightObject fightObject) {
        if (fightObject.isPlayer()) {
            final PlayerFightObject playerFightObject = (PlayerFightObject)fightObject;
            final NutMap acsryPropertyMap = playerFightObject.getAcsryPropertyMap();
            final List<String> deleteProperies = new ArrayList<String>();
            for (final Map.Entry<String, Object> entry : acsryPropertyMap.entrySet()) {
                final String key = entry.getKey();
                final Object value = entry.getValue();
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
                acsryPropertyMap.setv(key, (Object)valueInt);
            }
            if (deleteProperies.size() > 0) {
                for (int i = 0; i < deleteProperies.size(); ++i) {
                    removeProperty(playerFightObject, deleteProperies.get(i));
                }
            }
        }
    }
    
    private static void removeProperty(final PlayerFightObject playerFightObject, final String distStr) {
        playerFightObject.getAcsryPropertyMap().remove((Object)distStr);
        playerFightObject.getAcsryPropertyMap().remove((Object)(distStr + "value"));
    }
    
    public static void excutePropertyType(final PlayerFightObject playerfightObject, final int dist) {
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
    
    public static int getChageBaseHurt(final FightObject fightObject) {
        try {
            if (fightObject.isPlayer()) {
                final PlayerFightObject playerFightObject = (PlayerFightObject)fightObject;
                final int chageHurt = playerFightObject.getAcsryPropertyMap().getInt("baseHurt", 0);
                return chageHurt;
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public static boolean isUnlock(final Fight fight, final FightObject fightObject, final Integer status) {
        try {
            int round = fightObject.getStatusMap().getInt(status + "statusConstraint", 0);
            if (round > 0 && round <= WarMaxRound.WAR_MAX_ROUND.getValue()) {
                fightObject.getStatusMap().setv(status + "statusConstraint", (Object)(--round));
                return true;
            }
            if (round > WarMaxRound.WAR_MAX_ROUND.getValue()) {
                int count = 0;
                final List<FightObject> mySide = fight.getMySide(fightObject.getId());
                if (mySide.size() > 0) {
                    for (int i = 0; i < mySide.size(); ++i) {
                        final FightObject obj = mySide.get(i);
                        if (obj.isFallDown()) {
                            ++count;
                        }
                        else {
                            for (final Integer j : StatusConst.getControlStatus()) {
                                if (obj.getStatusValue(j) > 0) {
                                    ++count;
                                    break;
                                }
                            }
                        }
                    }
                    if (count / mySide.size() <= 0.4) {
                        return true;
                    }
                }
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<Integer> getAcsrySkillList(final Role role) {
        final List<Integer> acsrySkillList = new ArrayList<Integer>();
        final List<WearEquip> wearEquips = DataCache.EQUIP_MAP.get(role.getId());
        final List<String> suitTypes = new ArrayList<String>();
        final List<Byte> acsryProductOrders = new ArrayList<Byte>();
        for (int i = 0; i < wearEquips.size(); ++i) {
            if (wearEquips.get(i).getPosition() > 5) {
                final SysDataAcsry sysDataAcsry = EquipDataPool.acsryMap.get(wearEquips.get(i).getType());
                if (sysDataAcsry != null) {
                    suitTypes.add(sysDataAcsry.getSuiteType());
                    acsryProductOrders.add(wearEquips.get(i).getProductOrder());
                }
            }
        }
        final long count = suitTypes.stream().distinct().count();
        if (suitTypes.size() >= 5 && count == 1L) {
            final Byte min = Collections.min((Collection<? extends Byte>)acsryProductOrders);
            final SysDataAcsrySuit sysDataAcsrySuit = EquipDataPool.acsrySuitMap.get(suitTypes.get(0));
            if (sysDataAcsrySuit != null) {
                final List<Integer> skills = sysDataAcsrySuit.getSkills();
                if (ACC_GRADE.PRICELESS.getValue() == min) {
                    for (final Integer skill : skills) {
                        acsrySkillList.add(skill);
                    }
                }
                else if (ACC_GRADE.RESERVE.getValue() == min) {
                    for (final Integer skill : skills) {
                        if (skill % 10000 % 1000 % 100 % 10 < 3) {
                            acsrySkillList.add(skill);
                        }
                    }
                }
                else if (ACC_GRADE.PLAY_WITH.getValue() == min) {
                    for (final Integer skill : skills) {
                        if (skill % 10000 % 1000 % 100 % 10 < 2) {
                            acsrySkillList.add(skill);
                        }
                    }
                }
            }
        }
        return acsrySkillList;
    }
    
    public static SysDataAcsrySuit getAcsrySuit(final Role role) {
        final List<WearEquip> wearEquips = SpringUtils.getEquipService().selectWearEquip(role);
        final List<String> suitTypes = new ArrayList<String>();
        for (int i = 0; i < wearEquips.size(); ++i) {
            if (wearEquips.get(i).getPosition() > 5) {
                final SysDataAcsry sysDataAcsry = EquipDataPool.acsryMap.get(wearEquips.get(i).getType());
                if (sysDataAcsry != null) {
                    suitTypes.add(sysDataAcsry.getSuiteType());
                }
            }
        }
        final long count = suitTypes.stream().distinct().count();
        if (suitTypes.size() >= 5 && count == 1L) {
            final SysDataAcsrySuit sysDataAcsrySuit = EquipDataPool.acsrySuitMap.get(suitTypes.get(0));
            return sysDataAcsrySuit;
        }
        return null;
    }
}
