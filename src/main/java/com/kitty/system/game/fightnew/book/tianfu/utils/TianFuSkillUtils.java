
package com.kitty.system.game.fightnew.book.tianfu.utils;

import com.kitty.system.game.fightnew.imbue.bean.ImbueParam;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.tianfu.domain.RoleDayInCe;
import java.util.Collection;
import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.model.MagicAttackParam;
import org.nutz.lang.util.NutMap;
import java.util.Iterator;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.enums.war.itempro.def.DAY_IN_CE_TRIGGER_DICT;
import com.kitty.system.game.fightnew.bean.TianFuObject;
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
    public static void afterPhyAttack(final Fight fight, final FightObject effector, final BattleUnit battleUnit, final ActionDetailParam actionDetailParam) {
        final RoleSkill roleSkill = new RoleSkill();
        roleSkill.setType(SKILLATTR.SKILLATTR_WULI.getValue());
        targetHits(fight, effector, battleUnit.getTarget(), roleSkill, battleUnit.getPhyHurt(), actionDetailParam);
        targetHitsMore(fight, effector, roleSkill, 1, actionDetailParam);
        addEffectAll(fight, effector, battleUnit.getTarget(), battleUnit.getPhyHurt(), actionDetailParam);
    }
    
    public static void roundStart(final Fight fight, final FightObject effector) {
        if (effector.isPlayer() || effector.isPet()) {
            List<TianFuObject> tianFuList;
            if (effector.isPet()) {
                final FightObject host = fight.queryFightObject(effector.getHostId());
                tianFuList = ((PlayerFightObject)host).getDayInCeFight().getTianFuList();
            }
            else {
                tianFuList = ((PlayerFightObject)effector).getDayInCeFight().getTianFuList();
            }
            if (tianFuList.size() <= 0) {
                return;
            }
            final List<TianFuObject> thisTriggerTypeList = tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.ROUND_START.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return;
            }
            for (final TianFuObject tianFuObject : thisTriggerTypeList) {
                final TianFuProcessor processor = TianFuProcessor.queryProcessor(tianFuObject.getCeSerial());
                if (processor == null) {
                    continue;
                }
                processor.passive(fight, effector, null, new Object[0]);
            }
        }
    }
    
    public static int calculateHurt(final Fight fight, final FightObject effector, final FightObject target, final int hurt) {
        if (!target.isPlayer()) {
            return 0;
        }
        final List<TianFuObject> tianFuList = ((PlayerFightObject)target).getDayInCeFight().getTianFuList();
        if (tianFuList.size() <= 0) {
            return 0;
        }
        final List<TianFuObject> thisTriggerTypeList = tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.CALCULATE_HURT.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
        if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
            return 0;
        }
        int alertHurt = 0;
        for (final TianFuObject tianFuObject : thisTriggerTypeList) {
            final TianFuProcessor processor = TianFuProcessor.queryProcessor(tianFuObject.getCeSerial());
            if (processor == null) {
                continue;
            }
            final NutMap map = processor.passive(fight, effector, target, hurt);
            if (map == null) {
                continue;
            }
            alertHurt += map.getInt("alterHurt", 0);
        }
        return alertHurt;
    }
    
    public static void magicAttackEnd(final Fight fight, final FightObject effector, final List<MagicAttackParam> targets, final RoleSkill skill, final ActionDetailParam actionDetailParam) {
        final int dodgeCount = targets.stream().filter(obj -> obj.isDodge()).collect(Collectors.toList()).size();
        for (final MagicAttackParam magicParam : targets) {
            final FightObject target = magicParam.getTarget();
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
    
    public static void exitOrClearPositive(final Fight fight, final FightObject effector, final FightObject target, final List<Integer> statusList) {
        if (target.isPet()) {
            final FightObject host = fight.queryFightObject(target.getHostId());
            final List<TianFuObject> tianFuList = ((PlayerFightObject)host).getDayInCeFight().getTianFuList();
            if (tianFuList.size() <= 0) {
                return;
            }
            final List<TianFuObject> thisTriggerTypeList = tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.EXIT_OR_CLEAR_POSITIVE.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return;
            }
            for (final TianFuObject tianFuObject : thisTriggerTypeList) {
                final TianFuProcessor processor = TianFuProcessor.queryProcessor(tianFuObject.getCeSerial());
                if (processor == null) {
                    continue;
                }
                processor.passive(fight, effector, target, statusList);
            }
        }
    }
    
    public static void targetHitsMore(final Fight fight, final FightObject effector, final RoleSkill roleSkill, final int count, final ActionDetailParam actionDetailParam) {
        if (effector.isPlayer() || effector.isPet()) {
            List<TianFuObject> tianFuList;
            if (effector.isPet()) {
                final FightObject host = fight.queryFightObject(effector.getHostId());
                tianFuList = ((PlayerFightObject)host).getDayInCeFight().getTianFuList();
            }
            else {
                tianFuList = ((PlayerFightObject)effector).getDayInCeFight().getTianFuList();
            }
            if (tianFuList.size() <= 0) {
                return;
            }
            final List<TianFuObject> thisTriggerTypeList = tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.TARGET_HITS_MORE.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return;
            }
            for (final TianFuObject tianFuObject : thisTriggerTypeList) {
                final TianFuProcessor processor = TianFuProcessor.queryProcessor(tianFuObject.getCeSerial());
                if (processor == null) {
                    continue;
                }
                processor.attackOver(fight, effector, null, actionDetailParam, roleSkill, count);
            }
        }
    }
    
    public static void targetHits(final Fight fight, final FightObject effector, final FightObject target, final RoleSkill roleSkill, final int hurt, final ActionDetailParam actionDetailParam) {
        if (effector.isPlayer()) {
            final List<TianFuObject> tianFuList = ((PlayerFightObject)effector).getDayInCeFight().getTianFuList();
            if (tianFuList.size() > 0) {
                final List<TianFuObject> thisTriggerTypeList = tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.TARGET_HITS.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
                if (thisTriggerTypeList != null && thisTriggerTypeList.size() > 0) {
                    for (final TianFuObject tianFuObject : thisTriggerTypeList) {
                        final TianFuProcessor processor = TianFuProcessor.queryProcessor(tianFuObject.getCeSerial());
                        if (processor == null) {
                            continue;
                        }
                        processor.attackOver(fight, effector, target, actionDetailParam, roleSkill);
                    }
                }
            }
        }
        if (target.isPlayer()) {
            final List<TianFuObject> targetTianFuList = ((PlayerFightObject)target).getDayInCeFight().getTianFuList();
            if (targetTianFuList.size() <= 0) {
                return;
            }
            final List<TianFuObject> targetTriggerTypeList = targetTianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.BE_HIT_BY.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (targetTriggerTypeList == null || targetTriggerTypeList.size() <= 0) {
                return;
            }
            for (final TianFuObject tianFuObject : targetTriggerTypeList) {
                final TianFuProcessor processor = TianFuProcessor.queryProcessor(tianFuObject.getCeSerial());
                if (processor == null) {
                    continue;
                }
                processor.attackOver(fight, effector, target, actionDetailParam, roleSkill, hurt);
            }
        }
    }
    
    public static void addEffect(final Fight fight, final FightObject effector, final FightObject target, final RoleSkill roleSkill, final int hurt, final ActionDetailParam actionDetailParam) {
        if (effector.isPlayer() || effector.isPet()) {
            List<TianFuObject> tianFuList;
            if (effector.isPet()) {
                final FightObject host = fight.queryFightObject(effector.getHostId());
                tianFuList = ((PlayerFightObject)host).getDayInCeFight().getTianFuList();
            }
            else {
                tianFuList = ((PlayerFightObject)effector).getDayInCeFight().getTianFuList();
            }
            if (tianFuList.size() <= 0) {
                return;
            }
            final List<TianFuObject> thisTriggerTypeList = tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.ADD_EFFECT.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList != null && thisTriggerTypeList.size() > 0) {
                for (final TianFuObject tianFuObject : thisTriggerTypeList) {
                    final TianFuProcessor processor = TianFuProcessor.queryProcessor(tianFuObject.getCeSerial());
                    if (processor == null) {
                        continue;
                    }
                    processor.attackOver(fight, effector, target, actionDetailParam, roleSkill, hurt);
                }
            }
            addEffectAll(fight, effector, target, hurt, actionDetailParam);
        }
    }
    
    public static void addEffectAll(final Fight fight, final FightObject effector, final FightObject target, final int hurt, final ActionDetailParam actionDetailParam) {
        if (effector.isPet()) {
            final FightObject host = fight.queryFightObject(effector.getHostId());
            final List<TianFuObject> tianFuList = ((PlayerFightObject)host).getDayInCeFight().getTianFuList();
            if (tianFuList.size() <= 0) {
                return;
            }
            final List<TianFuObject> phyMagicTriggerTypeList = tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.ADD_EFFECT_ALL.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (phyMagicTriggerTypeList == null || phyMagicTriggerTypeList.size() <= 0) {
                return;
            }
            for (final TianFuObject tianFuObject : phyMagicTriggerTypeList) {
                final TianFuProcessor processor = TianFuProcessor.queryProcessor(tianFuObject.getCeSerial());
                if (processor == null) {
                    continue;
                }
                processor.attackOver(fight, effector, target, actionDetailParam, hurt);
            }
        }
    }
    
    public static void beforeDoMagic(final Fight fight, final FightObject effector, final RoleSkill roleSkill) {
        if (effector.isPlayer()) {
            final List<TianFuObject> tianFuList = ((PlayerFightObject)effector).getDayInCeFight().getTianFuList();
            if (tianFuList.size() <= 0) {
                return;
            }
            final List<TianFuObject> thisTriggerTypeList = tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.BEFORE_DO_MAGIC.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return;
            }
            for (final TianFuObject tianFuObject : thisTriggerTypeList) {
                final TianFuProcessor processor = TianFuProcessor.queryProcessor(tianFuObject.getCeSerial());
                if (processor == null) {
                    continue;
                }
                processor.passive(fight, effector, null, roleSkill);
            }
        }
    }
    
    public static void beforePhysicalAttack(final Fight fight, final FightObject effector, final FightObject target) {
        if (effector.isPlayer()) {
            final List<TianFuObject> tianFuList = ((PlayerFightObject)effector).getDayInCeFight().getTianFuList();
            if (tianFuList.size() <= 0) {
                return;
            }
            final List<TianFuObject> thisTriggerTypeList = tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.BEFORE_PHYSICAL_ATTACK.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return;
            }
            for (final TianFuObject tianFuObject : thisTriggerTypeList) {
                final TianFuProcessor processor = TianFuProcessor.queryProcessor(tianFuObject.getCeSerial());
                if (processor == null) {
                    continue;
                }
                processor.passive(fight, effector, target, new Object[0]);
            }
        }
    }
    
    public static void enteringSide(final Fight fight, final FightObject effector) {
        if (effector.isPlayer() || effector.isPet()) {
            List<TianFuObject> tianFuList;
            if (effector.isPet()) {
                final FightObject host = fight.queryFightObject(effector.getHostId());
                tianFuList = ((PlayerFightObject)host).getDayInCeFight().getTianFuList();
            }
            else {
                tianFuList = ((PlayerFightObject)effector).getDayInCeFight().getTianFuList();
            }
            if (tianFuList.size() <= 0) {
                return;
            }
            final List<TianFuObject> thisTriggerTypeList = tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.ENTERING_SIDE.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return;
            }
            for (final TianFuObject tianFuObject : thisTriggerTypeList) {
                final TianFuProcessor processor = TianFuProcessor.queryProcessor(tianFuObject.getCeSerial());
                if (processor == null) {
                    continue;
                }
                processor.passive(fight, effector, null, new Object[0]);
            }
        }
    }
    
    public static void beforeSkillEffect(final Fight fight, final FightObject effector, final FightObject target, final RoleSkill roleSkill) {
        if (effector.isPlayer()) {
            final List<TianFuObject> tianFuList = ((PlayerFightObject)effector).getDayInCeFight().getTianFuList();
            if (tianFuList.size() <= 0) {
                return;
            }
            final List<TianFuObject> thisTriggerTypeList = tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.BEFORE_SKILL_EFFECT.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return;
            }
            for (final TianFuObject tianFuObject : thisTriggerTypeList) {
                final TianFuProcessor processor = TianFuProcessor.queryProcessor(tianFuObject.getCeSerial());
                if (processor == null) {
                    continue;
                }
                processor.passive(fight, effector, target, roleSkill);
            }
        }
    }
    
    public static void magicMiss(final Fight fight, final FightObject effector, final FightObject target, final RoleSkill roleSkill, final ActionDetailParam actionDetailParam) {
        if (target.isPlayer()) {
            final List<TianFuObject> tianFuList = ((PlayerFightObject)target).getDayInCeFight().getTianFuList();
            if (tianFuList.size() <= 0) {
                return;
            }
            final List<TianFuObject> thisTriggerTypeList = tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.MAGIC_MISS.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return;
            }
            for (final TianFuObject tianFuObject : thisTriggerTypeList) {
                final TianFuProcessor processor = TianFuProcessor.queryProcessor(tianFuObject.getCeSerial());
                if (processor == null) {
                    continue;
                }
                processor.attackOver(fight, effector, target, actionDetailParam, roleSkill);
            }
        }
    }
    
    public static void beforeDoAction(final Fight fight, final FightObject effector) {
        if (effector.isPlayer()) {
            final List<TianFuObject> tianFuList = ((PlayerFightObject)effector).getDayInCeFight().getTianFuList();
            if (tianFuList.size() <= 0) {
                return;
            }
            final List<TianFuObject> thisTriggerTypeList = tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.BEFORE_DO_ACTION.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return;
            }
            for (final TianFuObject tianFuObject : thisTriggerTypeList) {
                final TianFuProcessor processor = TianFuProcessor.queryProcessor(tianFuObject.getCeSerial());
                if (processor == null) {
                    continue;
                }
                processor.passive(fight, effector, null, new Object[0]);
            }
        }
    }
    
    public static void physicalAttack(final Fight fight, final FightObject effector, final FightObject target, final ActionDetailParam actionDetailParam) {
        if (effector.isPet()) {
            final FightObject host = fight.queryFightObject(effector.getHostId());
            final List<TianFuObject> tianFuList = ((PlayerFightObject)host).getDayInCeFight().getTianFuList();
            if (tianFuList.size() <= 0) {
                return;
            }
            final List<TianFuObject> thisTriggerTypeList = tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.PHYSICAL_ATTACK.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return;
            }
            for (final TianFuObject tianFuObject : thisTriggerTypeList) {
                final TianFuProcessor processor = TianFuProcessor.queryProcessor(tianFuObject.getCeSerial());
                if (processor == null) {
                    continue;
                }
                processor.attackOver(fight, effector, target, actionDetailParam, new Object[0]);
            }
            final TianFuProcessor processor = TianFuProcessor.queryProcessor(323503);
            processor.passive(fight, effector, target, new Object[0]);
        }
    }
    
    public static int addMagicEffect(final Fight fight, final FightObject effector, final FightObject target, final RoleSkill roleSkill, final int hurt, final Effect effect) {
        excuteTianFuStatus(target, roleSkill);
        if (!effector.isPlayer()) {
            return 0;
        }
        final List<TianFuObject> tianFuList = ((PlayerFightObject)effector).getDayInCeFight().getTianFuList();
        if (tianFuList.size() <= 0) {
            return 0;
        }
        final List<TianFuObject> thisTriggerTypeList = tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.ADD_MAGIC_EFFECT.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
        if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
            return 0;
        }
        int returnHurt = 0;
        for (final TianFuObject tianFuObject : thisTriggerTypeList) {
            final TianFuProcessor processor = TianFuProcessor.queryProcessor(tianFuObject.getCeSerial());
            if (processor == null) {
                continue;
            }
            final NutMap passiveMap = processor.passive(fight, effector, target, hurt, roleSkill, effect);
            if (passiveMap == null || passiveMap.getInt("addHurt", 0) == 0) {
                continue;
            }
            returnHurt += passiveMap.getInt("addHurt", 0);
        }
        return returnHurt;
    }
    
    private static void excuteTianFuStatus(final FightObject target, final RoleSkill roleSkill) {
        if (SKILLATTR.SKILLATTR_POISON.getValue() == roleSkill.getType() && target.getStatusValue(1005) <= 0) {
            TianFuUtils.clearTianFuAffect(target, 1005);
        }
        else if (SKILLATTR.SKILLATTR_SHUAIRUO.getValue() == roleSkill.getType() && target.getStatusValue(1007) <= 0) {
            TianFuUtils.clearTianFuAffect(target, 1007);
        }
    }
    
    public static int subtractMp(final Fight fight, final FightObject effector, final int mp) {
        if (!effector.isPlayer()) {
            return 0;
        }
        final List<TianFuObject> tianFuList = ((PlayerFightObject)effector).getDayInCeFight().getTianFuList();
        if (tianFuList.size() <= 0) {
            return 0;
        }
        final List<TianFuObject> thisTriggerTypeList = tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.SUBTRACT_MP.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
        if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
            return 0;
        }
        int addMp = 0;
        for (final TianFuObject tianFuObject : thisTriggerTypeList) {
            final TianFuProcessor processor = TianFuProcessor.queryProcessor(tianFuObject.getCeSerial());
            if (processor == null) {
                continue;
            }
            final NutMap map = processor.passive(fight, effector, null, mp);
            if (map == null) {
                continue;
            }
            addMp += map.getInt("alterMp", 0);
        }
        return addMp;
    }
    
    public static List<FiedValue> useDrug(final Fight fight, final FightObject effector, final FightObject target) {
        if (target.isPlayer() || target.isPet()) {
            List<TianFuObject> tianFuList;
            if (target.isPet()) {
                final FightObject host = fight.queryFightObject(target.getHostId());
                tianFuList = ((PlayerFightObject)host).getDayInCeFight().getTianFuList();
            }
            else {
                tianFuList = ((PlayerFightObject)target).getDayInCeFight().getTianFuList();
            }
            if (tianFuList.size() <= 0) {
                return null;
            }
            final List<TianFuObject> thisTriggerTypeList = tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.USE_DRUG.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return null;
            }
            final List<FiedValue> returnList = new ArrayList<FiedValue>();
            for (final TianFuObject tianFuObject : thisTriggerTypeList) {
                final TianFuProcessor processor = TianFuProcessor.queryProcessor(tianFuObject.getCeSerial());
                if (processor == null) {
                    continue;
                }
                final List<FiedValue> fiedValues = processor.passiveFied(fight, effector, target, new Object[0]);
                if (fiedValues == null || fiedValues.size() <= 0) {
                    continue;
                }
                returnList.addAll(fiedValues);
            }
            if (returnList.size() > 0) {
                return returnList;
            }
        }
        return null;
    }
    
    public static void statusReduce(final Fight fight, final FightObject effector, final Integer status) {
        if (effector.isPlayer()) {
            final List<TianFuObject> tianFuList = ((PlayerFightObject)effector).getDayInCeFight().getTianFuList();
            if (tianFuList.size() <= 0) {
                return;
            }
            final List<TianFuObject> thisTriggerTypeList = tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.STATUS_REDUCE.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return;
            }
            for (final TianFuObject tianFuObject : thisTriggerTypeList) {
                final TianFuProcessor processor = TianFuProcessor.queryProcessor(tianFuObject.getCeSerial());
                if (processor == null) {
                    continue;
                }
                processor.passive(fight, effector, null, status);
            }
        }
    }
    
    public static void beforeCountHurt(final Fight fight, final FightObject effector, final FightObject target) {
        if (effector.isPlayer()) {
            final List<TianFuObject> tianFuList = ((PlayerFightObject)effector).getDayInCeFight().getTianFuList();
            if (tianFuList.size() <= 0) {
                return;
            }
            final List<TianFuObject> thisTriggerTypeList = tianFuList.stream().filter(obj -> DAY_IN_CE_TRIGGER_DICT.BEFORE_COUNT_HURT.getValue().equals(obj.getTriggerType())).collect(Collectors.toList());
            if (thisTriggerTypeList == null || thisTriggerTypeList.size() <= 0) {
                return;
            }
            final RoleDayInCe roleDayInCe = DataCache.ROLE_DAY_IN_CE.get(effector.getRole().getId());
            if (roleDayInCe != null) {
                for (final TianFuObject tianFuObject : thisTriggerTypeList) {
                    final TianFuProcessor processor = TianFuProcessor.queryProcessor(tianFuObject.getCeSerial());
                    if (processor == null) {
                        continue;
                    }
                    final TianFuAddition tianFuAddition = new TianFuAddition(roleDayInCe.getLevel(), tianFuObject.getLevel());
                    processor.beforeCountHurt(fight, effector, target, tianFuAddition);
                }
            }
        }
    }
    
    public static void clearTianFuProperty(final FightObject fightObject) {
        final String reduceHurtCountDist = String.valueOf(OTHER_SKILL_DICT_TYPE.REDUCE_HURT_COUNT.getDist());
        final int reduceHurtCount = fightObject.getBeAttackedMap().getInt(reduceHurtCountDist);
        if (reduceHurtCount > 0) {
            fightObject.getBeAttackedMap().unset(reduceHurtCountDist);
        }
    }
    
    public static void mhChangePro(final FightObject target, final int status, final int round, final double percent) {
        final ImbueParam kfy = ImbueUtil.getStatusValue(target.getImbueMap(), status, PRO_DICT_TYPE.K_FY.getDist(), round);
        if (kfy != null) {
            kfy.setValue(kfy.getValue() * (1.0 + percent));
        }
        final ImbueParam khs = ImbueUtil.getStatusValue(target.getImbueMap(), status, PRO_DICT_TYPE.K_HS.getDist(), round);
        if (khs != null) {
            khs.setValue(khs.getValue() * (1.0 + percent));
        }
        final ImbueParam khl = ImbueUtil.getStatusValue(target.getImbueMap(), status, PRO_DICT_TYPE.K_HL.getDist(), round);
        if (khl != null) {
            khl.setValue(khl.getValue() * (1.0 + percent));
        }
        final ImbueParam kyw = ImbueUtil.getStatusValue(target.getImbueMap(), status, PRO_DICT_TYPE.K_YW.getDist(), round);
        if (kyw != null) {
            kyw.setValue(kyw.getValue() * (1.0 + percent));
        }
        final ImbueParam kf = ImbueUtil.getStatusValue(target.getImbueMap(), status, PRO_DICT_TYPE.K_F.getDist(), round);
        if (kf != null) {
            kf.setValue(kf.getValue() * (1.0 + percent));
        }
        final ImbueParam kl = ImbueUtil.getStatusValue(target.getImbueMap(), status, PRO_DICT_TYPE.K_L.getDist(), round);
        if (kl != null) {
            kl.setValue(kl.getValue() * (1.0 + percent));
        }
        final ImbueParam ks = ImbueUtil.getStatusValue(target.getImbueMap(), status, PRO_DICT_TYPE.K_S.getDist(), round);
        if (ks != null) {
            ks.setValue(ks.getValue() * (1.0 + percent));
        }
        final ImbueParam kh = ImbueUtil.getStatusValue(target.getImbueMap(), status, PRO_DICT_TYPE.K_H.getDist(), round);
        if (kh != null) {
            kh.setValue(kh.getValue() * (1.0 + percent));
        }
        final ImbueParam wlxs = ImbueUtil.getStatusValue(target.getImbueMap(), status, PRO_DICT_TYPE.WL_XS.getDist(), round);
        if (wlxs != null) {
            wlxs.setValue(wlxs.getValue() * (1.0 + percent));
        }
    }
}
