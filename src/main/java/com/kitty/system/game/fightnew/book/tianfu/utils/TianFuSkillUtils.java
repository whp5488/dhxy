package com.kitty.system.game.fightnew.book.tianfu.utils;

import com.kitty.system.game.fightnew.imbue.bean.ImbueParam;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.tianfu.domain.RoleDayInCe;
import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.model.MagicAttackParam;
import org.nutz.lang.util.NutMap;
import java.util.Iterator;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import java.util.stream.Collectors;
import com.kitty.system.enums.war.itempro.def.DAY_IN_CE_TRIGGER_DICT;
import java.util.List;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;

public class TianFuSkillUtils
{
    public TianFuSkillUtils() {
    }
    
    public static void afterPhyAttack(Fight fight, FightObject effector, BattleUnit battleUnit, ActionDetailParam actionDetailParam) {
        RoleSkill roleSkill = new RoleSkill();
        roleSkill.setType(Integer.valueOf(SKILLATTR.SKILLATTR_WULI.getValue()));
        targetHits(fight, effector, battleUnit.getTarget(), roleSkill, battleUnit.getPhyHurt(), actionDetailParam);
        targetHitsMore(fight, effector, roleSkill, 1, actionDetailParam);
        addEffectAll(fight, effector, battleUnit.getTarget(), battleUnit.getPhyHurt(), actionDetailParam);
    }
    
    public static void roundStart(Fight fight, FightObject effector) {
        if (effector.isPlayer() || effector.isPet()) {
            List<TianFuObject> tianFuList;
            if (effector.isPet()) {
                FightObject host = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
                tianFuList = ((PlayerFightObject)host).getDayInCeFight().getTianFuList();
            }
            else {
                tianFuList = ((PlayerFightObject)effector).getDayInCeFight().getTianFuList();
            }
            if (tianFuList.size() <= 0) {
                return;
            }
            List<TianFuObject> thisTriggerTypeList = (List)tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.ROUND_START.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return;
            }
            for (TianFuObject tianFuObject : thisTriggerTypeList) {
                TianFuProcessor processor = TianFuProcessor.queryProcessor(Integer.valueOf(tianFuObject.getCeSerial()));
                if (processor == null) {
                    continue;
                }
                processor.passive(fight, effector, null);
            }
        }
    }
    
    public static int calculateHurt(Fight fight, FightObject effector, FightObject target, int hurt) {
        if (target.isPlayer()) {
            List<TianFuObject> tianFuList = ((PlayerFightObject)target).getDayInCeFight().getTianFuList();
            if (tianFuList.size() <= 0) {
                return 0;
            }
            List<TianFuObject> thisTriggerTypeList = (List)tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.CALCULATE_HURT.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return 0;
            }
            int alertHurt = 0;
            for (TianFuObject tianFuObject : thisTriggerTypeList) {
                TianFuProcessor processor = TianFuProcessor.queryProcessor(Integer.valueOf(tianFuObject.getCeSerial()));
                if (processor == null) {
                    continue;
                }
                NutMap map = processor.passive(fight, effector, target, Integer.valueOf(hurt));
                if (map != null) {
                    alertHurt += map.getInt("alterHurt", 0);
                }
                else {
                    continue;
                }
            }
            return alertHurt;
        }
        else {
            return 0;
        }
    }
    
    public static void magicAttackEnd(Fight fight, FightObject effector, List<MagicAttackParam> targets, RoleSkill skill, ActionDetailParam actionDetailParam) {
        int dodgeCount = ((List)targets.stream().filter(obj -> obj.isDodge()).collect(Collectors.toList())).size();
        for (MagicAttackParam magicParam : targets) {
            FightObject target = magicParam.getTarget();
            if (magicParam.isDodge()) {
                targetHits(fight, effector, target, skill, magicParam.getHurt(), actionDetailParam);
            }
            else {
                magicMiss(fight, effector, target, skill, actionDetailParam);
            }
            addEffect(fight, effector, target, skill, magicParam.getHurt(), actionDetailParam);
        }
        targetHitsMore(fight, effector, skill, dodgeCount, actionDetailParam);
    }
    
    public static void exitOrClearPositive(Fight fight, FightObject effector, FightObject target, List<Integer> statusList) {
        if (target.isPet()) {
            FightObject host = fight.queryFightObject(Integer.valueOf(target.getHostId()));
            List<TianFuObject> tianFuList = ((PlayerFightObject)host).getDayInCeFight().getTianFuList();
            if (tianFuList.size() <= 0) {
                return;
            }
            List<TianFuObject> thisTriggerTypeList = (List)tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.EXIT_OR_CLEAR_POSITIVE.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return;
            }
            for (TianFuObject tianFuObject : thisTriggerTypeList) {
                TianFuProcessor processor = TianFuProcessor.queryProcessor(Integer.valueOf(tianFuObject.getCeSerial()));
                if (processor == null) {
                    continue;
                }
                processor.passive(fight, effector, target, statusList);
            }
        }
    }
    
    public static void targetHitsMore(Fight fight, FightObject effector, RoleSkill roleSkill, int count, ActionDetailParam actionDetailParam) {
        if (effector.isPlayer() || effector.isPet()) {
            List<TianFuObject> tianFuList;
            if (effector.isPet()) {
                FightObject host = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
                tianFuList = ((PlayerFightObject)host).getDayInCeFight().getTianFuList();
            }
            else {
                tianFuList = ((PlayerFightObject)effector).getDayInCeFight().getTianFuList();
            }
            if (tianFuList.size() <= 0) {
                return;
            }
            List<TianFuObject> thisTriggerTypeList = (List)tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.TARGET_HITS_MORE.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return;
            }
            for (TianFuObject tianFuObject : thisTriggerTypeList) {
                TianFuProcessor processor = TianFuProcessor.queryProcessor(Integer.valueOf(tianFuObject.getCeSerial()));
                if (processor == null) {
                    continue;
                }
                processor.attackOver(fight, effector, null, actionDetailParam, roleSkill, Integer.valueOf(count));
            }
        }
    }
    
    public static void targetHits(Fight fight, FightObject effector, FightObject target, RoleSkill roleSkill, int hurt, ActionDetailParam actionDetailParam) {
        if (effector.isPlayer()) {
            List<TianFuObject> tianFuList = ((PlayerFightObject)effector).getDayInCeFight().getTianFuList();
            if (tianFuList.size() > 0) {
                List<TianFuObject> thisTriggerTypeList = (List)tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.TARGET_HITS.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
                if (thisTriggerTypeList != null && thisTriggerTypeList.size() > 0) {
                    for (TianFuObject tianFuObject : thisTriggerTypeList) {
                        TianFuProcessor processor = TianFuProcessor.queryProcessor(Integer.valueOf(tianFuObject.getCeSerial()));
                        if (processor == null) {
                            continue;
                        }
                        processor.attackOver(fight, effector, target, actionDetailParam, roleSkill);
                    }
                }
            }
        }
        if (target.isPlayer()) {
            List<TianFuObject> targetTianFuList = ((PlayerFightObject)target).getDayInCeFight().getTianFuList();
            if (targetTianFuList.size() <= 0) {
                return;
            }
            List<TianFuObject> targetTriggerTypeList = (List)targetTianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.BE_HIT_BY.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (targetTriggerTypeList == null || targetTriggerTypeList.size() <= 0) {
                return;
            }
            for (TianFuObject tianFuObject : targetTriggerTypeList) {
                TianFuProcessor processor = TianFuProcessor.queryProcessor(Integer.valueOf(tianFuObject.getCeSerial()));
                if (processor == null) {
                    continue;
                }
                processor.attackOver(fight, effector, target, actionDetailParam, roleSkill, Integer.valueOf(hurt));
            }
        }
    }
    
    public static void addEffect(Fight fight, FightObject effector, FightObject target, RoleSkill roleSkill, int hurt, ActionDetailParam actionDetailParam) {
        if (effector.isPlayer() || effector.isPet()) {
            List<TianFuObject> tianFuList;
            if (effector.isPet()) {
                FightObject host = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
                tianFuList = ((PlayerFightObject)host).getDayInCeFight().getTianFuList();
            }
            else {
                tianFuList = ((PlayerFightObject)effector).getDayInCeFight().getTianFuList();
            }
            if (tianFuList.size() <= 0) {
                return;
            }
            List<TianFuObject> thisTriggerTypeList = (List)tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.ADD_EFFECT.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList != null && thisTriggerTypeList.size() > 0) {
                for (TianFuObject tianFuObject : thisTriggerTypeList) {
                    TianFuProcessor processor = TianFuProcessor.queryProcessor(Integer.valueOf(tianFuObject.getCeSerial()));
                    if (processor == null) {
                        continue;
                    }
                    processor.attackOver(fight, effector, target, actionDetailParam, roleSkill, Integer.valueOf(hurt));
                }
            }
            addEffectAll(fight, effector, target, hurt, actionDetailParam);
        }
    }
    
    public static void addEffectAll(Fight fight, FightObject effector, FightObject target, int hurt, ActionDetailParam actionDetailParam) {
        if (effector.isPet()) {
            FightObject host = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
            List<TianFuObject> tianFuList = ((PlayerFightObject)host).getDayInCeFight().getTianFuList();
            if (tianFuList.size() <= 0) {
                return;
            }
            List<TianFuObject> phyMagicTriggerTypeList = (List)tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.ADD_EFFECT_ALL.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (phyMagicTriggerTypeList == null || phyMagicTriggerTypeList.size() <= 0) {
                return;
            }
            for (TianFuObject tianFuObject : phyMagicTriggerTypeList) {
                TianFuProcessor processor = TianFuProcessor.queryProcessor(Integer.valueOf(tianFuObject.getCeSerial()));
                if (processor == null) {
                    continue;
                }
                processor.attackOver(fight, effector, target, actionDetailParam, Integer.valueOf(hurt));
            }
        }
    }
    
    public static void beforeDoMagic(Fight fight, FightObject effector, RoleSkill roleSkill) {
        if (effector.isPlayer()) {
            List<TianFuObject> tianFuList = ((PlayerFightObject)effector).getDayInCeFight().getTianFuList();
            if (tianFuList.size() <= 0) {
                return;
            }
            List<TianFuObject> thisTriggerTypeList = (List)tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.BEFORE_DO_MAGIC.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return;
            }
            for (TianFuObject tianFuObject : thisTriggerTypeList) {
                TianFuProcessor processor = TianFuProcessor.queryProcessor(Integer.valueOf(tianFuObject.getCeSerial()));
                if (processor == null) {
                    continue;
                }
                processor.passive(fight, effector, null, roleSkill);
            }
        }
    }
    
    public static void beforePhysicalAttack(Fight fight, FightObject effector, FightObject target) {
        if (effector.isPlayer()) {
            List<TianFuObject> tianFuList = ((PlayerFightObject)effector).getDayInCeFight().getTianFuList();
            if (tianFuList.size() <= 0) {
                return;
            }
            List<TianFuObject> thisTriggerTypeList = (List)tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.BEFORE_PHYSICAL_ATTACK.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return;
            }
            for (TianFuObject tianFuObject : thisTriggerTypeList) {
                TianFuProcessor processor = TianFuProcessor.queryProcessor(Integer.valueOf(tianFuObject.getCeSerial()));
                if (processor == null) {
                    continue;
                }
                processor.passive(fight, effector, target, new Object[0]);
            }
        }
    }
    
    public static void enteringSide(Fight fight, FightObject effector) {
        if (effector.isPlayer() || effector.isPet()) {
            List<TianFuObject> tianFuList;
            if (effector.isPet()) {
                FightObject host = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
                tianFuList = ((PlayerFightObject)host).getDayInCeFight().getTianFuList();
            }
            else {
                tianFuList = ((PlayerFightObject)effector).getDayInCeFight().getTianFuList();
            }
            if (tianFuList.size() <= 0) {
                return;
            }
            List<TianFuObject> thisTriggerTypeList = (List)tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.ENTERING_SIDE.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return;
            }
            for (TianFuObject tianFuObject : thisTriggerTypeList) {
                TianFuProcessor processor = TianFuProcessor.queryProcessor(Integer.valueOf(tianFuObject.getCeSerial()));
                if (processor == null) {
                    continue;
                }
                processor.passive(fight, effector, null, new Object[0]);
            }
        }
    }
    
    public static void beforeSkillEffect(Fight fight, FightObject effector, FightObject target, RoleSkill roleSkill) {
        if (effector.isPlayer()) {
            List<TianFuObject> tianFuList = ((PlayerFightObject)effector).getDayInCeFight().getTianFuList();
            if (tianFuList.size() <= 0) {
                return;
            }
            List<TianFuObject> thisTriggerTypeList = (List)tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.BEFORE_SKILL_EFFECT.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return;
            }
            for (TianFuObject tianFuObject : thisTriggerTypeList) {
                TianFuProcessor processor = TianFuProcessor.queryProcessor(Integer.valueOf(tianFuObject.getCeSerial()));
                if (processor == null) {
                    continue;
                }
                processor.passive(fight, effector, target, roleSkill);
            }
        }
    }
    
    public static void magicMiss(Fight fight, FightObject effector, FightObject target, RoleSkill roleSkill, ActionDetailParam actionDetailParam) {
        if (target.isPlayer()) {
            List<TianFuObject> tianFuList = ((PlayerFightObject)target).getDayInCeFight().getTianFuList();
            if (tianFuList.size() <= 0) {
                return;
            }
            List<TianFuObject> thisTriggerTypeList = (List)tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.MAGIC_MISS.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return;
            }
            for (TianFuObject tianFuObject : thisTriggerTypeList) {
                TianFuProcessor processor = TianFuProcessor.queryProcessor(Integer.valueOf(tianFuObject.getCeSerial()));
                if (processor == null) {
                    continue;
                }
                processor.attackOver(fight, effector, target, actionDetailParam, roleSkill);
            }
        }
    }
    
    public static void beforeDoAction(Fight fight, FightObject effector) {
        if (effector.isPlayer()) {
            List<TianFuObject> tianFuList = ((PlayerFightObject)effector).getDayInCeFight().getTianFuList();
            if (tianFuList.size() <= 0) {
                return;
            }
            List<TianFuObject> thisTriggerTypeList = (List)tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.BEFORE_DO_ACTION.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return;
            }
            for (TianFuObject tianFuObject : thisTriggerTypeList) {
                TianFuProcessor processor = TianFuProcessor.queryProcessor(Integer.valueOf(tianFuObject.getCeSerial()));
                if (processor == null) {
                    continue;
                }
                processor.passive(fight, effector, null, new Object[0]);
            }
        }
    }
    
    public static void physicalAttack(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam) {
        if (effector.isPet()) {
            FightObject host = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
            List<TianFuObject> tianFuList = ((PlayerFightObject)host).getDayInCeFight().getTianFuList();
            if (tianFuList.size() <= 0) {
                return;
            }
            List<TianFuObject> thisTriggerTypeList = (List)tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.PHYSICAL_ATTACK.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return;
            }
            for (TianFuObject tianFuObject : thisTriggerTypeList) {
                TianFuProcessor processor = TianFuProcessor.queryProcessor(Integer.valueOf(tianFuObject.getCeSerial()));
                if (processor == null) {
                    continue;
                }
                processor.attackOver(fight, effector, target, actionDetailParam, new Object[0]);
            }
            TianFuProcessor processor = TianFuProcessor.queryProcessor(Integer.valueOf(323503));
            processor.passive(fight, effector, target, new Object[0]);
        }
    }
    
    public static int addMagicEffect(Fight fight, FightObject effector, FightObject target, RoleSkill roleSkill, int hurt, Effect effect) {
        excuteTianFuStatus(target, roleSkill);
        if (effector.isPlayer()) {
            List<TianFuObject> tianFuList = ((PlayerFightObject)effector).getDayInCeFight().getTianFuList();
            if (tianFuList.size() <= 0) {
                return 0;
            }
            List<TianFuObject> thisTriggerTypeList = (List)tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.ADD_MAGIC_EFFECT.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return 0;
            }
            int returnHurt = 0;
            for (TianFuObject tianFuObject : thisTriggerTypeList) {
                TianFuProcessor processor = TianFuProcessor.queryProcessor(Integer.valueOf(tianFuObject.getCeSerial()));
                if (processor == null) {
                    continue;
                }
                NutMap passiveMap = processor.passive(fight, effector, target, Integer.valueOf(hurt), roleSkill, effect);
                if (passiveMap != null && passiveMap.getInt("addHurt", 0) != 0) {
                    returnHurt += passiveMap.getInt("addHurt", 0);
                }
                else {
                    continue;
                }
            }
            return returnHurt;
        }
        else {
            return 0;
        }
    }
    
    private static void excuteTianFuStatus(FightObject target, RoleSkill roleSkill) {
        if (SKILLATTR.SKILLATTR_POISON.getValue() == (int)roleSkill.getType() && target.getStatusValue(1005) <= 0) {
            TianFuUtils.clearTianFuAffect(target, 1005);
        }
        else if (SKILLATTR.SKILLATTR_SHUAIRUO.getValue() == (int)roleSkill.getType() && target.getStatusValue(1007) <= 0) {
            TianFuUtils.clearTianFuAffect(target, 1007);
        }
    }
    
    public static int subtractMp(Fight fight, FightObject effector, int mp) {
        if (effector.isPlayer()) {
            List<TianFuObject> tianFuList = ((PlayerFightObject)effector).getDayInCeFight().getTianFuList();
            if (tianFuList.size() <= 0) {
                return 0;
            }
            List<TianFuObject> thisTriggerTypeList = (List)tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.SUBTRACT_MP.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return 0;
            }
            int addMp = 0;
            for (TianFuObject tianFuObject : thisTriggerTypeList) {
                TianFuProcessor processor = TianFuProcessor.queryProcessor(Integer.valueOf(tianFuObject.getCeSerial()));
                if (processor == null) {
                    continue;
                }
                NutMap map = processor.passive(fight, effector, null, Integer.valueOf(mp));
                if (map != null) {
                    addMp += map.getInt("alterMp", 0);
                }
                else {
                    continue;
                }
            }
            return addMp;
        }
        else {
            return 0;
        }
    }
    
    public static List<FiedValue> useDrug(Fight fight, FightObject effector, FightObject target) {
        if (target.isPlayer() || target.isPet()) {
            List<TianFuObject> tianFuList;
            if (target.isPet()) {
                FightObject host = fight.queryFightObject(Integer.valueOf(target.getHostId()));
                tianFuList = ((PlayerFightObject)host).getDayInCeFight().getTianFuList();
            }
            else {
                tianFuList = ((PlayerFightObject)target).getDayInCeFight().getTianFuList();
            }
            if (tianFuList.size() <= 0) {
                return null;
            }
            List<TianFuObject> thisTriggerTypeList = (List)tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.USE_DRUG.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return null;
            }
            List<FiedValue> returnList = new ArrayList<>();
            for (TianFuObject tianFuObject : thisTriggerTypeList) {
                TianFuProcessor processor = TianFuProcessor.queryProcessor(Integer.valueOf(tianFuObject.getCeSerial()));
                if (processor == null) {
                    continue;
                }
                List<FiedValue> fiedValues = processor.passiveFied(fight, effector, target, new Object[0]);
                if (fiedValues != null && fiedValues.size() > 0) {
                    returnList.addAll(fiedValues);
                }
                else {
                    continue;
                }
            }
            if (returnList.size() > 0) {
                return returnList;
            }
        }
        return null;
    }
    
    public static void statusReduce(Fight fight, FightObject effector, Integer status) {
        if (effector.isPlayer()) {
            List<TianFuObject> tianFuList = ((PlayerFightObject)effector).getDayInCeFight().getTianFuList();
            if (tianFuList.size() <= 0) {
                return;
            }
            List<TianFuObject> thisTriggerTypeList = (List)tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.STATUS_REDUCE.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return;
            }
            for (TianFuObject tianFuObject : thisTriggerTypeList) {
                TianFuProcessor processor = TianFuProcessor.queryProcessor(Integer.valueOf(tianFuObject.getCeSerial()));
                if (processor == null) {
                    continue;
                }
                processor.passive(fight, effector, null, status);
            }
        }
    }
    
    public static void beforeCountHurt(Fight fight, FightObject effector, FightObject target) {
        if (effector.isPlayer()) {
            List<TianFuObject> tianFuList = ((PlayerFightObject)effector).getDayInCeFight().getTianFuList();
            if (tianFuList.size() <= 0) {
                return;
            }
            List<TianFuObject> thisTriggerTypeList = (List)tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.BEFORE_COUNT_HURT.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return;
            }
            RoleDayInCe roleDayInCe = (RoleDayInCe)DataCache.ROLE_DAY_IN_CE.get(effector.getRole().getId());
            if (roleDayInCe != null) {
                for (TianFuObject tianFuObject : thisTriggerTypeList) {
                    TianFuProcessor processor = TianFuProcessor.queryProcessor(Integer.valueOf(tianFuObject.getCeSerial()));
                    if (processor == null) {
                        continue;
                    }
                    TianFuAddition tianFuAddition = new TianFuAddition(roleDayInCe.getLevel(), tianFuObject.getLevel());
                    processor.beforeCountHurt(fight, effector, target, tianFuAddition);
                }
            }
        }
    }
    
    public static void clearTianFuProperty(FightObject fightObject) {
        String reduceHurtCountDist = String.valueOf(OTHER_SKILL_DICT_TYPE.REDUCE_HURT_COUNT.getDist());
        int reduceHurtCount = fightObject.getBeAttackedMap().getInt(reduceHurtCountDist);
        if (reduceHurtCount > 0) {
            fightObject.getBeAttackedMap().unset(reduceHurtCountDist);
        }
    }
    
    public static void mhChangePro(FightObject target, int status, int round, double percent) {
        ImbueParam kfy = ImbueUtil.getStatusValue(target.getImbueMap(), status, PRO_DICT_TYPE.K_FY.getDist(), round);
        if (kfy != null) {
            kfy.setValue(kfy.getValue() * (1.0 + percent));
        }
        ImbueParam khs = ImbueUtil.getStatusValue(target.getImbueMap(), status, PRO_DICT_TYPE.K_HS.getDist(), round);
        if (khs != null) {
            khs.setValue(khs.getValue() * (1.0 + percent));
        }
        ImbueParam khl = ImbueUtil.getStatusValue(target.getImbueMap(), status, PRO_DICT_TYPE.K_HL.getDist(), round);
        if (khl != null) {
            khl.setValue(khl.getValue() * (1.0 + percent));
        }
        ImbueParam kyw = ImbueUtil.getStatusValue(target.getImbueMap(), status, PRO_DICT_TYPE.K_YW.getDist(), round);
        if (kyw != null) {
            kyw.setValue(kyw.getValue() * (1.0 + percent));
        }
        ImbueParam kf = ImbueUtil.getStatusValue(target.getImbueMap(), status, PRO_DICT_TYPE.K_F.getDist(), round);
        if (kf != null) {
            kf.setValue(kf.getValue() * (1.0 + percent));
        }
        ImbueParam kl = ImbueUtil.getStatusValue(target.getImbueMap(), status, PRO_DICT_TYPE.K_L.getDist(), round);
        if (kl != null) {
            kl.setValue(kl.getValue() * (1.0 + percent));
        }
        ImbueParam ks = ImbueUtil.getStatusValue(target.getImbueMap(), status, PRO_DICT_TYPE.K_S.getDist(), round);
        if (ks != null) {
            ks.setValue(ks.getValue() * (1.0 + percent));
        }
        ImbueParam kh = ImbueUtil.getStatusValue(target.getImbueMap(), status, PRO_DICT_TYPE.K_H.getDist(), round);
        if (kh != null) {
            kh.setValue(kh.getValue() * (1.0 + percent));
        }
        ImbueParam wlxs = ImbueUtil.getStatusValue(target.getImbueMap(), status, PRO_DICT_TYPE.WL_XS.getDist(), round);
        if (wlxs != null) {
            wlxs.setValue(wlxs.getValue() * (1.0 + percent));
        }
    }
}
