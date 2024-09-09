package com.kitty.system.game.fightnew.utils;

import com.kitty.system.game.role.domain.Role;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.model.ActionParam;
import com.kitty.system.game.fightnew.messages.old.RespFightShouting;
import java.util.Arrays;
import com.kitty.system.game.fightnew.book.definition.SkillConst;
import java.util.Iterator;
import java.util.List;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;

public class ActionParamUtils
{
    public ActionParamUtils() {
    }
    
    public static void changeLife(Fight fight, FightObject effector, FightObject target, int type, int skillSerial, int missed, int changeLife, boolean isRevive) {
        if (!checkBase(fight, effector, target, type, skillSerial, missed) || changeLife == 0) {
            return;
        }
        ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, (int)target.getId());
        FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId((int)target.getId());
        fightObjectParam.setOrder(1);
        PassiveSkillUtils.excuteChangeLife(fight, target, fightObjectParam, changeLife);
        fightObjectParam.setMissed(missed);
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        return;
    }
    
    public static void changeMana(Fight fight, FightObject effector, FightObject target, int type, int skillSerial, int missed, int changeMana) {
        if (!checkBase(fight, effector, target, type, skillSerial, missed) || changeMana == 0) {
            return;
        }
        ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, (int)target.getId());
        FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId((int)target.getId());
        fightObjectParam.setOrder(1);
        PassiveSkillUtils.excuteChangeMana(fight, target, fightObjectParam, changeMana);
        fightObjectParam.setMissed(missed);
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        return;
    }
    
    public static void changeHpMp(Fight fight, FightObject effector, FightObject target, int type, int skillSerial, int missed, int changeLife, int changeMana, boolean isRevive) {
        if (!checkBase(fight, effector, target, type, skillSerial, missed)) {
            return;
        }
        if (changeMana == 0 && changeLife == 0) {
            return;
        }
        ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, (int)target.getId());
        FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId((int)target.getId());
        fightObjectParam.setOrder(1);
        PassiveSkillUtils.excuteChangeLife(fight, target, fightObjectParam, changeLife);
        PassiveSkillUtils.excuteChangeMana(fight, target, fightObjectParam, changeMana);
        fightObjectParam.setMissed(missed);
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        return;
    }
    
    public static void addStatus(Fight fight, FightObject effector, FightObject target, int type, int skillSerial, int missed, int status, int round) {
        if (!checkBase(fight, effector, target, type, skillSerial, missed) || status == 0 || round == 0) {
            return;
        }
        ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, (int)target.getId());
        FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId((int)target.getId());
        fightObjectParam.setOrder(1);
        fightObjectParam.getStatusMap().put(Integer.valueOf(status), Integer.valueOf(round));
        fightObjectParam.setMissed(missed);
        otherAddStatus(fight, effector, target, status, actionDetailParam, missed);
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        return;
    }
    
    public static void otherAddStatus(Fight fight, FightObject effector, FightObject target, int status, ActionDetailParam actionDetailParam, int missed) {
        List<NutMap> nutMapList = PassiveSkillUtils.addStatus(fight, effector, target, status);
        if (nutMapList != null && nutMapList.size() > 0) {
            for (NutMap nutMap : nutMapList) {
                FightObjectParam otherParam = actionDetailParam.gainTargetParam(nutMap.getInt("objId"), true);
                if (otherParam != null) {
                    otherParam.getStatusMap().put(Integer.valueOf(nutMap.getInt("status")), Integer.valueOf(nutMap.getInt("round")));
                    otherParam.setMissed(missed);
                    FightObject otherObject = fight.queryFightObject(Integer.valueOf(otherParam.getObjId()));
                    if (otherObject != null) {
                        otherObject.setStatus(nutMap.getInt("status"), nutMap.getInt("round"));
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
    
    public static void addStatus(Fight fight, int effectorId, int targetId, int type, int skillSerial, int missed, int status, int round) {
        if (!checkBase(fight, effectorId, targetId, type, skillSerial, missed) || status == 0 || round == 0) {
            return;
        }
        FightObject effector = fight.queryFightObject(Integer.valueOf(effectorId));
        ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, targetId);
        FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId(targetId);
        fightObjectParam.setOrder(1);
        fightObjectParam.getStatusMap().put(Integer.valueOf(status), Integer.valueOf(round));
        fightObjectParam.setMissed(missed);
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        return;
    }
    
    public static void clearStatus(Fight fight, FightObject effector, FightObject target, int type, int skillSerial, int missed, List<Integer> statusList) {
        if (!checkBase(fight, effector, target, type, skillSerial, missed) || statusList == null || statusList.size() <= 0) {
            return;
        }
        ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, (int)target.getId());
        FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId((int)target.getId());
        fightObjectParam.setOrder(1);
        fightObjectParam.setClearStatus(statusList);
        fightObjectParam.setMissed(missed);
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        return;
    }
    
    public static void changeLifeClearStatus(Fight fight, FightObject effector, FightObject target, int type, int skillSerial, int missed, int changeLife, boolean isRevive, List<Integer> clearStatusList) {
        if (!checkBase(fight, effector, target, type, skillSerial, missed) || changeLife == 0 || clearStatusList == null || clearStatusList.size() <= 0) {
            return;
        }
        ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, (int)target.getId());
        FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId((int)target.getId());
        fightObjectParam.setOrder(1);
        PassiveSkillUtils.excuteChangeLife(fight, target, fightObjectParam, changeLife);
        fightObjectParam.setClearStatus(clearStatusList);
        fightObjectParam.setMissed(missed);
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        return;
    }
    
    public static void attackNoHit(Fight fight, FightObject effector, FightObject target, int type, int skillSerial, int missed) {
        if (!checkBase(fight, effector, target, type, skillSerial, missed)) {
            return;
        }
        ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, (int)target.getId());
        FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId((int)target.getId());
        fightObjectParam.setOrder(1);
        fightObjectParam.setHit(false);
        fightObjectParam.setMissed(missed);
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        return;
    }
    
    public static void actionForTargetNoth(Fight fight, FightObject effector, FightObject target, int type, int skillSerial, int missed) {
        if (!checkBase(fight, effector, target, type, skillSerial, missed)) {
            return;
        }
        ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, (int)target.getId());
        FightObjectParam targetParam = actionDetailParam.gainTargetParam((int)target.getId(), true);
        if (targetParam.getMissed() < 0) {
            targetParam.setMissed(missed);
        }
        targetParam.setMissed(missed);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        return;
    }
    
    public static void actionForTargetsNoth(Fight fight, FightObject effector, List<Integer> targetIds, int type, int skillSerial, int missed) {
        ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial);
        if (targetIds != null && targetIds.size() > 0) {
            for (Integer targetId : targetIds) {
                FightObjectParam target = actionDetailParam.gainTargetParam((int)targetId, true);
                target.setMissed(missed);
            }
        }
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
    }
    
    public static void advanceNotice(FightObject effector, List<ActionDetailParam> actionDetailParams, int targetId, int skillSerial) {
        if (effector == null || targetId <= 0 || skillSerial < 0) {
            return;
        }
        ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 33, skillSerial, targetId);
        FightObjectParam targetParam = actionDetailParam.gainTargetParam(targetId, true);
        int[] skillStatus = (int[])Arrays.stream(SkillConst.skillStatus).filter(obj -> skillSerial == obj[0]).findAny().get();
        targetParam.setObjId(targetId);
        targetParam.setOrder(1);
        targetParam.getStatusMap().put(Integer.valueOf(skillStatus[1]), Integer.valueOf(-2));
        actionDetailParam.getTargetParamList().add(targetParam);
        actionDetailParams.add(actionDetailParam);
        return;
    }
    
    public static void excuteDisappear(Fight fight, FightObject effector, int targetId, int type, int skillSerial, int missed) {
        ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, targetId);
        FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId(targetId);
        fightObjectParam.setOrder(1);
        fightObjectParam.setDisappear(1);
        fightObjectParam.setMissed(missed);
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
    }
    
    public static boolean checkBase(Fight fight, FightObject effector, FightObject target, int type, int skillSerial, int missed) {
        return fight != null && effector != null && target != null && type >= 0 && skillSerial >= 0 && missed >= 0;
    }
    
    public static boolean checkBase(Fight fight, int effectorId, int targetId, int type, int skillSerial, int missed) {
        return fight != null && effectorId > 0 && targetId > 0 && type >= 0 && skillSerial >= 0 && missed >= 0;
    }
    
    public static ActionDetailParam addStatusReturn(FightObject effector, FightObject target, int type, int skillSerial, int missed, int status, int round) {
        ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, (int)target.getId());
        FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId((int)target.getId());
        fightObjectParam.setOrder(1);
        fightObjectParam.setMissed(missed);
        fightObjectParam.getStatusMap().put(Integer.valueOf(status), Integer.valueOf(round));
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        return actionDetailParam;
    }
    
    public static ActionDetailParam addStatusReturn(int effectorId, int targetId, int type, int skillSerial, int missed, int status, int round) {
        ActionDetailParam actionDetailParam = new ActionDetailParam(type, skillSerial, effectorId, targetId);
        FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId(targetId);
        fightObjectParam.setOrder(1);
        fightObjectParam.setMissed(missed);
        fightObjectParam.getStatusMap().put(Integer.valueOf(status), Integer.valueOf(round));
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        return actionDetailParam;
    }
    
    public static ActionDetailParam clearStatusReturn(FightObject effector, FightObject target, int type, int skillSerial, int missed, List<Integer> statusList) {
        ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, (int)target.getId());
        FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId((int)target.getId());
        fightObjectParam.setOrder(1);
        fightObjectParam.setClearStatus(statusList);
        fightObjectParam.setMissed(missed);
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        return actionDetailParam;
    }
    
    public static void updateObjectInfo(FightObject effector, int type, int skillSerial, int missed, RespFightShouting fightShouting, FightObjectParam fightObjectParam) {
        if (effector == null) {
            return;
        }
        if (type < 0 || skillSerial < 0 || missed < 0) {
            return;
        }
        if (fightObjectParam == null) {
            fightObjectParam = new FightObjectParam();
        }
        fightObjectParam.setObjId((int)effector.getId());
        fightObjectParam.setOrder(1);
        fightObjectParam.setCurrentLife(effector.getCurrLife());
        fightObjectParam.setCurrentMana(effector.getCurrMana());
        fightObjectParam.setFightShouting(fightShouting);
        fightObjectParam.setMissed(missed);
        return;
    }
    
    public static ActionParam createActionParam(Fight fight) {
        ActionParam actionParam = new ActionParam();
        actionParam.setFightRound(fight.getRound());
        List<Long> playerIds = new ArrayList<>();
        for (FightObject fightObject : fight.getAllFightObjects()) {
            if (fightObject.isPlayer()) {
                Role role = fightObject.getRole();
                if (role != null) {
                    playerIds.add(role.getId());
                }
                else {
                    continue;
                }
            }
            else {
                continue;
            }
        }
        if (playerIds != null && playerIds.size() > 0) {
            actionParam.setPlayerList(playerIds);
            return actionParam;
        }
        return actionParam;
    }
    
    public static FightObjectParam getTargetParam(ActionDetailParam actionDetailParam, int targetId) {
        if (actionDetailParam == null || targetId <= 0) {
            return null;
        }
        FightObjectParam targetParam = actionDetailParam.gainTargetParam(targetId, false);
        if (targetParam == null) {
            targetParam = new FightObjectParam();
            int size = actionDetailParam.getTargetParamList().size();
            targetParam.setOrder(++size);
            targetParam.setObjId(targetId);
            return targetParam;
        }
        return targetParam;
    }
    
    public static ActionDetailParam changeLifeReturn(Fight fight, FightObject effector, FightObject target, int type, int skillSerial, int missed, int changeLife, boolean isRevive) {
        if (!checkBase(fight, effector, target, type, skillSerial, missed) || changeLife == 0) {
            return null;
        }
        ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, (int)target.getId());
        FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId((int)target.getId());
        fightObjectParam.setOrder(1);
        PassiveSkillUtils.excuteChangeLife(fight, target, fightObjectParam, changeLife);
        fightObjectParam.setMissed(missed);
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        return actionDetailParam;
    }
    
    public static FightObjectParam addStatusForTarget(FightObject target, int missed, int status, int round, int order) {
        FightObjectParam targetParam = new FightObjectParam();
        targetParam.setObjId((int)target.getId());
        targetParam.setOrder(order);
        targetParam.setMissed(missed);
        targetParam.getStatusMap().put(Integer.valueOf(status), Integer.valueOf(round));
        return targetParam;
    }
    
    public static FightObjectParam clearStatusForTarget(int targetId, int order, List<Integer> clearStatusList, int missed) {
        FightObjectParam targetParam = new FightObjectParam();
        targetParam.setObjId(targetId);
        targetParam.setOrder(order);
        targetParam.setClearStatus(clearStatusList);
        targetParam.setMissed(missed);
        return targetParam;
    }
    
    public static FightObjectParam changeLifeForTarget(Fight fight, FightObject target, int order, int changeLife, int missed) {
        FightObjectParam targetParam = new FightObjectParam();
        targetParam.setObjId((int)target.getId());
        targetParam.setOrder(order);
        PassiveSkillUtils.excuteChangeLife(fight, target, targetParam, changeLife);
        targetParam.setMissed(missed);
        return targetParam;
    }
    
    public static FightObjectParam changeManaForTarget(Fight fight, FightObject target, int order, int changeMana, int missed) {
        FightObjectParam targetParam = new FightObjectParam();
        targetParam.setObjId((int)target.getId());
        targetParam.setOrder(order);
        PassiveSkillUtils.excuteChangeMana(fight, target, targetParam, changeMana);
        targetParam.setMissed(missed);
        return targetParam;
    }
    
    public static FightObjectParam changeHpMpForTarget(Fight fight, FightObject target, int missed, int changeLife, int changeMana, boolean isRevive) {
        FightObjectParam targetParam = new FightObjectParam();
        targetParam.setObjId((int)target.getId());
        targetParam.setOrder(1);
        PassiveSkillUtils.excuteChangeLife(fight, target, targetParam, changeLife);
        PassiveSkillUtils.excuteChangeMana(fight, target, targetParam, changeMana);
        targetParam.setMissed(missed);
        return targetParam;
    }
    
    public static void fightShouting(Fight fight, FightObject effector, FightObject target, int type, int skillSerial, int missed, RespFightShouting fightShouting) {
        if (!checkBase(fight, effector, target, type, skillSerial, missed) || fightShouting == null) {
            return;
        }
        ActionDetailParam actionDetailParam = new ActionDetailParam(effector, type, skillSerial, (int)target.getId());
        FightObjectParam fightObjectParam = new FightObjectParam();
        fightObjectParam.setObjId((int)target.getId());
        fightObjectParam.setOrder(1);
        fightObjectParam.setMissed(missed);
        fightObjectParam.setFightShouting(fightShouting);
        actionDetailParam.getTargetParamList().add(fightObjectParam);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        return;
    }
    
    public static void setNormalAttack(int targetId, ActionDetailParam actionDetailParam, ActionDetailParam actionDetailParamRutrun, Integer skillStatus) {
        List<FightObjectParam> fightObjectParams = actionDetailParamRutrun.gainTargetParamList(targetId, true);
        boolean flag = true;
        for (FightObjectParam targetParam : fightObjectParams) {
            if (skillStatus != null && flag) {
                targetParam.addDamageType((int)skillStatus);
                flag = false;
            }
            actionDetailParam.getTargetParamList().add(targetParam);
        }
    }
    
    public static void setNormalAttackAll(int targetId, ActionDetailParam actionDetailParam, ActionDetailParam actionDetailParamRutrun, Integer skillStatus) {
        if (actionDetailParamRutrun.getTargetParamList().size() > 0) {
            boolean isFlag = true;
            for (FightObjectParam targetParamReturnOther : actionDetailParamRutrun.getTargetParamList()) {
                if (isFlag) {
                    targetParamReturnOther.addDamageType((int)skillStatus);
                    isFlag = false;
                }
                actionDetailParam.getTargetParamList().add(targetParamReturnOther);
            }
        }
    }
    
    public static ActionDetailParam createActionDetailParam(int effectorId, int targetId, int type, int skillSerial) {
        ActionDetailParam actionDetailParam = new ActionDetailParam();
        actionDetailParam.setType(type);
        actionDetailParam.setSkillSerial(skillSerial);
        actionDetailParam.setAttackerId(effectorId);
        actionDetailParam.setTargetId(targetId);
        return actionDetailParam;
    }
}
