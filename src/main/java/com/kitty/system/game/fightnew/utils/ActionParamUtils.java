
package com.kitty.system.game.fightnew.utils;

import com.kitty.system.game.role.domain.Role;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.model.ActionParam;
import com.kitty.system.game.fightnew.messages.old.RespFightShouting;
import java.util.Arrays;
import com.kitty.system.game.fightnew.book.definition.SkillConst;
import java.util.Iterator;
import java.util.List;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;

public class ActionParamUtils
{
    public static void changeLife(final Fight fight, final FightObject effector, final FightObject target, final int type, final int skillSerial, final int missed, final int changeLife, final boolean isRevive) {
        if (!checkBase(fight, effector, target, type, skillSerial, missed) || changeLife == 0) {
            return;
        }
        final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, target.getId());
        final FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId(target.getId());
        fightObjectParam.setOrder(1);
        PassiveSkillUtils.excuteChangeLife(fight, target, fightObjectParam, changeLife);
        fightObjectParam.setMissed(missed);
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
    }
    
    public static void changeMana(final Fight fight, final FightObject effector, final FightObject target, final int type, final int skillSerial, final int missed, final int changeMana) {
        if (!checkBase(fight, effector, target, type, skillSerial, missed) || changeMana == 0) {
            return;
        }
        final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, target.getId());
        final FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId(target.getId());
        fightObjectParam.setOrder(1);
        PassiveSkillUtils.excuteChangeMana(fight, target, fightObjectParam, changeMana);
        fightObjectParam.setMissed(missed);
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
    }
    
    public static void changeHpMp(final Fight fight, final FightObject effector, final FightObject target, final int type, final int skillSerial, final int missed, final int changeLife, final int changeMana, final boolean isRevive) {
        if (!checkBase(fight, effector, target, type, skillSerial, missed)) {
            return;
        }
        if (changeMana == 0 && changeLife == 0) {
            return;
        }
        final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, target.getId());
        final FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId(target.getId());
        fightObjectParam.setOrder(1);
        PassiveSkillUtils.excuteChangeLife(fight, target, fightObjectParam, changeLife);
        PassiveSkillUtils.excuteChangeMana(fight, target, fightObjectParam, changeMana);
        fightObjectParam.setMissed(missed);
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
    }
    
    public static void addStatus(final Fight fight, final FightObject effector, final FightObject target, final int type, final int skillSerial, final int missed, final int status, final int round) {
        if (!checkBase(fight, effector, target, type, skillSerial, missed) || status == 0 || round == 0) {
            return;
        }
        final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, target.getId());
        final FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId(target.getId());
        fightObjectParam.setOrder(1);
        fightObjectParam.getStatusMap().put(status, round);
        fightObjectParam.setMissed(missed);
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
    }
    
    public static void addStatus(final Fight fight, final int effectorId, final int targetId, final int type, final int skillSerial, final int missed, final int status, final int round) {
        if (!checkBase(fight, effectorId, targetId, type, skillSerial, missed) || status == 0 || round == 0) {
            return;
        }
        final FightObject effector = fight.queryFightObject(effectorId);
        final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, targetId);
        final FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId(targetId);
        fightObjectParam.setOrder(1);
        fightObjectParam.getStatusMap().put(status, round);
        fightObjectParam.setMissed(missed);
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
    }
    
    public static void clearStatus(final Fight fight, final FightObject effector, final FightObject target, final int type, final int skillSerial, final int missed, final List<Integer> statusList) {
        if (!checkBase(fight, effector, target, type, skillSerial, missed) || statusList == null || statusList.size() <= 0) {
            return;
        }
        final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, target.getId());
        final FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId(target.getId());
        fightObjectParam.setOrder(1);
        fightObjectParam.setClearStatus(statusList);
        fightObjectParam.setMissed(missed);
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
    }
    
    public static void changeLifeClearStatus(final Fight fight, final FightObject effector, final FightObject target, final int type, final int skillSerial, final int missed, final int changeLife, final boolean isRevive, final List<Integer> clearStatusList) {
        if (!checkBase(fight, effector, target, type, skillSerial, missed) || changeLife == 0 || clearStatusList == null || clearStatusList.size() <= 0) {
            return;
        }
        final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, target.getId());
        final FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId(target.getId());
        fightObjectParam.setOrder(1);
        PassiveSkillUtils.excuteChangeLife(fight, target, fightObjectParam, changeLife);
        fightObjectParam.setClearStatus(clearStatusList);
        fightObjectParam.setMissed(missed);
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
    }
    
    public static void attackNoHit(final Fight fight, final FightObject effector, final FightObject target, final int type, final int skillSerial, final int missed) {
        if (!checkBase(fight, effector, target, type, skillSerial, missed)) {
            return;
        }
        final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, target.getId());
        final FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId(target.getId());
        fightObjectParam.setOrder(1);
        fightObjectParam.setHit(false);
        fightObjectParam.setMissed(missed);
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
    }
    
    public static void actionForTargetNoth(final Fight fight, final FightObject effector, final FightObject target, final int type, final int skillSerial, final int missed) {
        if (!checkBase(fight, effector, target, type, skillSerial, missed)) {
            return;
        }
        final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, target.getId());
        final FightObjectParam targetParam = actionDetailParam.gainTargetParam(target.getId(), true);
        if (targetParam.getMissed() < 0) {
            targetParam.setMissed(missed);
        }
        targetParam.setMissed(missed);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
    }
    
    public static void actionForTargetsNoth(final Fight fight, final FightObject effector, final List<Integer> targetIds, final int type, final int skillSerial, final int missed) {
        final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial);
        if (targetIds != null && targetIds.size() > 0) {
            for (final Integer targetId : targetIds) {
                final FightObjectParam target = actionDetailParam.gainTargetParam(targetId, true);
                target.setMissed(missed);
            }
        }
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
    }
    
    public static void advanceNotice(final FightObject effector, final List<ActionDetailParam> actionDetailParams, final int targetId, final int skillSerial) {
        if (effector == null || targetId <= 0 || skillSerial < 0) {
            return;
        }
        final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 33, skillSerial, targetId);
        final FightObjectParam targetParam = actionDetailParam.gainTargetParam(targetId, true);
        final int[] skillStatus = Arrays.stream(SkillConst.skillStatus).filter(obj -> skillSerial == obj[0]).findAny().get();
        targetParam.setObjId(targetId);
        targetParam.setOrder(1);
        targetParam.getStatusMap().put(skillStatus[1], -2);
        actionDetailParam.getTargetParamList().add(targetParam);
        actionDetailParams.add(actionDetailParam);
    }
    
    public static void excuteDisappear(final Fight fight, final FightObject effector, final int targetId, final int type, final int skillSerial, final int missed) {
        final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, targetId);
        final FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId(targetId);
        fightObjectParam.setOrder(1);
        fightObjectParam.setDisappear(1);
        fightObjectParam.setMissed(missed);
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
    }
    
    public static boolean checkBase(final Fight fight, final FightObject effector, final FightObject target, final int type, final int skillSerial, final int missed) {
        return fight != null && effector != null && target != null && type >= 0 && skillSerial >= 0 && missed >= 0;
    }
    
    public static boolean checkBase(final Fight fight, final int effectorId, final int targetId, final int type, final int skillSerial, final int missed) {
        return fight != null && effectorId > 0 && targetId > 0 && type >= 0 && skillSerial >= 0 && missed >= 0;
    }
    
    public static ActionDetailParam addStatusReturn(final FightObject effector, final FightObject target, final int type, final int skillSerial, final int missed, final int status, final int round) {
        final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, target.getId());
        final FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId(target.getId());
        fightObjectParam.setOrder(1);
        fightObjectParam.setMissed(missed);
        fightObjectParam.getStatusMap().put(status, round);
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        return actionDetailParam;
    }
    
    public static ActionDetailParam addStatusReturn(final int effectorId, final int targetId, final int type, final int skillSerial, final int missed, final int status, final int round) {
        final ActionDetailParam actionDetailParam = new ActionDetailParam(type, skillSerial, effectorId, targetId);
        final FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId(targetId);
        fightObjectParam.setOrder(1);
        fightObjectParam.setMissed(missed);
        fightObjectParam.getStatusMap().put(status, round);
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        return actionDetailParam;
    }
    
    public static ActionDetailParam clearStatusReturn(final FightObject effector, final FightObject target, final int type, final int skillSerial, final int missed, final List<Integer> statusList) {
        final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, target.getId());
        final FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId(target.getId());
        fightObjectParam.setOrder(1);
        fightObjectParam.setClearStatus(statusList);
        fightObjectParam.setMissed(missed);
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        return actionDetailParam;
    }
    
    public static void updateObjectInfo(final FightObject effector, final int type, final int skillSerial, final int missed, final RespFightShouting fightShouting, FightObjectParam fightObjectParam) {
        if (effector == null) {
            return;
        }
        if (type < 0 || skillSerial < 0 || missed < 0) {
            return;
        }
        if (fightObjectParam == null) {
            fightObjectParam = new FightObjectParam();
        }
        fightObjectParam.setObjId(effector.getId());
        fightObjectParam.setOrder(1);
        fightObjectParam.setCurrentLife(effector.getCurrLife());
        fightObjectParam.setCurrentMana(effector.getCurrMana());
        fightObjectParam.setFightShouting(fightShouting);
        fightObjectParam.setMissed(missed);
    }
    
    public static ActionParam createActionParam(final Fight fight) {
        final ActionParam actionParam = new ActionParam();
        actionParam.setFightRound(fight.getRound());
        final List<Long> playerIds = new ArrayList<Long>();
        for (final FightObject fightObject : fight.getAllFightObjects()) {
            if (fightObject.isPlayer()) {
                final Role role = fightObject.getRole();
                if (role == null) {
                    continue;
                }
                playerIds.add(role.getId());
            }
        }
        if (playerIds != null && playerIds.size() > 0) {
            actionParam.setPlayerList(playerIds);
        }
        return actionParam;
    }
    
    public static FightObjectParam getTargetParam(final ActionDetailParam actionDetailParam, final int targetId) {
        if (actionDetailParam == null || targetId <= 0) {
            return null;
        }
        FightObjectParam targetParam = actionDetailParam.gainTargetParam(targetId, false);
        if (targetParam == null) {
            targetParam = new FightObjectParam();
            int size = actionDetailParam.getTargetParamList().size();
            targetParam.setOrder(++size);
            targetParam.setObjId(targetId);
        }
        return targetParam;
    }
    
    public static ActionDetailParam changeLifeReturn(final Fight fight, final FightObject effector, final FightObject target, final int type, final int skillSerial, final int missed, final int changeLife, final boolean isRevive) {
        if (!checkBase(fight, effector, target, type, skillSerial, missed) || changeLife == 0) {
            return null;
        }
        final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, target.getId());
        final FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId(target.getId());
        fightObjectParam.setOrder(1);
        PassiveSkillUtils.excuteChangeLife(fight, target, fightObjectParam, changeLife);
        fightObjectParam.setMissed(missed);
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        return actionDetailParam;
    }
    
    public static FightObjectParam addStatusForTarget(final FightObject target, final int missed, final int status, final int round, final int order) {
        final FightObjectParam targetParam = new FightObjectParam();
        targetParam.setObjId(target.getId());
        targetParam.setOrder(order);
        targetParam.setMissed(missed);
        targetParam.getStatusMap().put(status, round);
        return targetParam;
    }
    
    public static FightObjectParam clearStatusForTarget(final int targetId, final int order, final List<Integer> clearStatusList, final int missed) {
        final FightObjectParam targetParam = new FightObjectParam();
        targetParam.setObjId(targetId);
        targetParam.setOrder(order);
        targetParam.setClearStatus(clearStatusList);
        targetParam.setMissed(missed);
        return targetParam;
    }
    
    public static FightObjectParam changeLifeForTarget(final Fight fight, final FightObject target, final int order, final int changeLife, final int missed) {
        final FightObjectParam targetParam = new FightObjectParam();
        targetParam.setObjId(target.getId());
        targetParam.setOrder(order);
        PassiveSkillUtils.excuteChangeLife(fight, target, targetParam, changeLife);
        targetParam.setMissed(missed);
        return targetParam;
    }
    
    public static FightObjectParam changeManaForTarget(final Fight fight, final FightObject target, final int order, final int changeMana, final int missed) {
        final FightObjectParam targetParam = new FightObjectParam();
        targetParam.setObjId(target.getId());
        targetParam.setOrder(order);
        PassiveSkillUtils.excuteChangeMana(fight, target, targetParam, changeMana);
        targetParam.setMissed(missed);
        return targetParam;
    }
    
    public static FightObjectParam changeHpMpForTarget(final Fight fight, final FightObject target, final int missed, final int changeLife, final int changeMana, final boolean isRevive) {
        final FightObjectParam targetParam = new FightObjectParam();
        targetParam.setObjId(target.getId());
        targetParam.setOrder(1);
        PassiveSkillUtils.excuteChangeLife(fight, target, targetParam, changeLife);
        PassiveSkillUtils.excuteChangeMana(fight, target, targetParam, changeMana);
        targetParam.setMissed(missed);
        return targetParam;
    }
    
    public static void fightShouting(final Fight fight, final FightObject effector, final FightObject target, final int type, final int skillSerial, final int missed, final RespFightShouting fightShouting) {
        if (!checkBase(fight, effector, target, type, skillSerial, missed) || fightShouting == null) {
            return;
        }
        final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, target.getId());
        final FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId(target.getId());
        fightObjectParam.setOrder(1);
        fightObjectParam.setMissed(missed);
        fightObjectParam.setFightShouting(fightShouting);
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
    }
    
    public static void setNormalAttack(final int targetId, final ActionDetailParam actionDetailParam, final ActionDetailParam actionDetailParamRutrun, final Integer skillStatus) {
        final List<FightObjectParam> fightObjectParams = actionDetailParamRutrun.gainTargetParamList(targetId, true);
        boolean flag = true;
        for (final FightObjectParam targetParam : fightObjectParams) {
            if (skillStatus != null && flag) {
                targetParam.addDamageType(skillStatus);
                flag = false;
            }
            actionDetailParam.getTargetParamList().add(targetParam);
        }
    }
    
    public static void setNormalAttackAll(final int targetId, final ActionDetailParam actionDetailParam, final ActionDetailParam actionDetailParamRutrun, final Integer skillStatus) {
        if (actionDetailParamRutrun.getTargetParamList().size() > 0) {
            boolean isFlag = true;
            for (final FightObjectParam targetParamReturnOther : actionDetailParamRutrun.getTargetParamList()) {
                if (isFlag) {
                    targetParamReturnOther.addDamageType(skillStatus);
                    isFlag = false;
                }
                actionDetailParam.getTargetParamList().add(targetParamReturnOther);
            }
        }
    }
    
    public static ActionDetailParam createActionDetailParam(final int effectorId, final int targetId, final int type, final int skillSerial) {
        final ActionDetailParam actionDetailParam = new ActionDetailParam();
        actionDetailParam.setType(type);
        actionDetailParam.setSkillSerial(skillSerial);
        actionDetailParam.setAttackerId(effectorId);
        actionDetailParam.setTargetId(targetId);
        return actionDetailParam;
    }
}
