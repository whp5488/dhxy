
package com.kitty.system.game.fightnew.service;

import com.kitty.system.game.fightnew.messages.vo.SkillCool;
import com.kitty.system.game.fightnew.messages.RespRoundStartParam;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.mina.cache.DataCache;
import java.util.concurrent.ScheduledFuture;
import com.kitty.common.core.SchedulerManager;
import com.kitty.system.game.fightnew.task.TimeOutNewTask;
import com.kitty.system.game.fightnew.task.FightingTask;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import java.util.HashMap;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.fightnew.messages.vo.CombatAddObject;
import com.kitty.system.game.fightnew.model.RefreshPet;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.messages.vo.ExcuteFightObjectParam;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.messages.vo.ExcuteFightParam;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.ActionParam;
import com.kitty.system.game.fightnew.messages.RespExcuteFight;
import java.util.Iterator;
import com.kitty.system.game.role.domain.Role;
import java.util.List;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.fightnew.messages.old.RespFightShouting;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.apache.mina.core.session.IoSession;
import com.kitty.logs.LoggerUtils;
import com.kitty.system.game.fightnew.messages.ReqFightHandle;
import java.util.Map;
import com.kitty.system.game.fightnew.book.vo.SkillAutoPicker;
import org.nutz.lang.Lang;
import com.kitty.system.enums.war.wardef.RACE;
import com.kitty.system.game.fightnew.model.AutoFightSkillParam;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.FightObject;
import org.slf4j.LoggerFactory;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.pet.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.role.service.RoleService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class FightMessageNewService
{
    private final Logger logger;
    @Autowired
    RoleService roleService;
    @Autowired
    PetService petService;
    @Autowired
    FightNewService fightNewService;
    @Autowired
    ServerService serverService;
    @Autowired
    FightExecutorNewService fightExecutorNewService;
    
    public FightMessageNewService() {
        this.logger = LoggerFactory.getLogger(FightMessageNewService.class);
    }
    
    public AutoFightSkillParam selfAuto(final FightObject fightObject, final Fight fight) {
        if (fightObject.getAutoFight() == 1) {
            try {
                final AutoFightSkillParam autoFightSkillParam = new AutoFightSkillParam();
                autoFightSkillParam.setObjId(fightObject.getId());
                final SkillAutoPicker autoPicker = this.fightNewService.getAutoObject(fightObject, fight);
                autoFightSkillParam.setSkillId(autoPicker.getSkillId());
                byte type = 0;
                if (autoPicker.isDef()) {
                    type = 1;
                }
                else if (autoPicker.getSkillId() == 0) {
                    type = 0;
                }
                else {
                    type = (byte)autoPicker.getType();
                }
                autoFightSkillParam.setAction(type);
                if (fightObject.isPlayer() && RACE.RACE_LONG.getValue() == fightObject.getRace()) {
                    final Map<String, Object> autoFightSet = fightObject.getRole().getExtendBox().getAutoFightSet();
                    if (autoFightSet != null) {
                        final Integer born = (Integer)autoFightSet.get("born");
                        if (born != null) {
                            autoFightSkillParam.setBorn(born);
                        }
                    }
                }
                return autoFightSkillParam;
            }
            catch (final Exception e) {
                this.logger.error("进战斗发送自动战斗状态报错==={}", (Object)Lang.getStackTrace((Throwable)e));
            }
        }
        return null;
    }
    
    public ReqFightHandle autoFightOne(final Fight fight, final FightObject fightObject) {
        try {
            if (fight.isOver()) {
                return null;
            }
            final IoSession session = fightObject.getSession();
            if (session == null) {
                final ReqFightHandle reqFightHandle = new ReqFightHandle();
                reqFightHandle.setRound(fight.getRound());
                reqFightHandle.setAutoFight(1);
                reqFightHandle.setAttackerId(fightObject.getId());
                reqFightHandle.setType(0);
                reqFightHandle.setSkillId(0);
                reqFightHandle.setTargetId(0);
                return reqFightHandle;
            }
            final ReqFightHandle reqFightHandle = new ReqFightHandle();
            reqFightHandle.setAutoFight(1);
            reqFightHandle.setRound(fight.getRound());
            reqFightHandle.setAttackerId(fightObject.getId());
            final SkillAutoPicker autoPicker = this.fightNewService.getAutoObject(fightObject, fight);
            reqFightHandle.setTargetId(autoPicker.getTargetId());
            reqFightHandle.setSkillId(autoPicker.getSkillId());
            if (autoPicker.isDef()) {
                reqFightHandle.setType(1);
                return reqFightHandle;
            }
            if (autoPicker.getType() == 5 && autoPicker.getSkillId() > 0) {
                reqFightHandle.setType(5);
                return reqFightHandle;
            }
            if (reqFightHandle.getSkillId() == 0) {
                reqFightHandle.setType(0);
                return reqFightHandle;
            }
            final RoleSkill roleSkill = fightObject.getSkillBy(reqFightHandle.getSkillId());
            if (roleSkill != null) {
                reqFightHandle.setType(7);
                reqFightHandle.setSkillId(roleSkill.getSkillId());
                return reqFightHandle;
            }
            reqFightHandle.setType(0);
            reqFightHandle.setSkillId(0);
            reqFightHandle.setTargetId(0);
            return reqFightHandle;
        }
        catch (final Exception e) {
            LoggerUtils.error("定时战斗报错={}", e);
            final ReqFightHandle reqFightHandle = new ReqFightHandle();
            reqFightHandle.setRound(fight.getRound());
            reqFightHandle.setAutoFight(1);
            reqFightHandle.setAttackerId(fightObject.getId());
            reqFightHandle.setType(1);
            reqFightHandle.setSkillId(0);
            reqFightHandle.setTargetId(fightObject.getId());
            return reqFightHandle;
        }
    }
    
    public RespFightShouting fightShouting(final FightObject fightObject, final String type) {
        try {
            if (fightObject == null) {
                return null;
            }
            final List<NutMap> fightMsg = fightObject.getFightMsg();
            if (fightMsg == null || fightMsg.size() <= 0) {
                return null;
            }
            Role role = fightObject.getRole();
            if (role == null) {
                role = SessionUtils.getRoleBySession(fightObject.getSession());
                if (role == null) {
                    return null;
                }
            }
            final int id = fightObject.getId();
            String content = null;
            switch (type) {
                case "战斗出场": {
                    for (final NutMap nutMap : fightMsg) {
                        if (nutMap.getInt("type") == 1) {
                            content = nutMap.getString("msg");
                        }
                    }
                    break;
                }
                case "法攻": {
                    for (final NutMap nutMap : fightMsg) {
                        if (nutMap.getInt("type") == 2) {
                            content = nutMap.getString("msg");
                        }
                    }
                    break;
                }
                case "挨打": {
                    for (final NutMap nutMap : fightMsg) {
                        if (nutMap.getInt("type") == 3) {
                            content = nutMap.getString("msg");
                        }
                    }
                    break;
                }
                case "主人倒地": {
                    for (final NutMap nutMap : fightMsg) {
                        if (nutMap.getInt("type") == 4) {
                            content = nutMap.getString("msg");
                        }
                    }
                    break;
                }
                case "使用药品": {
                    for (final NutMap nutMap : fightMsg) {
                        if (nutMap.getInt("type") == 5) {
                            content = nutMap.getString("msg");
                        }
                    }
                    break;
                }
            }
            if (Strings.isEmpty((CharSequence)content)) {
                return null;
            }
            final RespFightShouting respFightShouting = new RespFightShouting();
            respFightShouting.setId(id);
            respFightShouting.setMsg(content);
            return respFightShouting;
        }
        catch (final Exception e) {
            LoggerUtils.error("", e);
            return null;
        }
    }
    
    public RespFightShouting fightShouting(final FightObject fightObject, final String msg, final int showTime, final int msgDelayTime) {
        final RespFightShouting fightShouting = new RespFightShouting();
        fightShouting.setId(fightObject.getId());
        fightShouting.setMsg(msg);
        fightShouting.setShow_time(showTime);
        fightShouting.setMsgDelayTime(msgDelayTime);
        return fightShouting;
    }
    
    public NutMap poisonOrRecoverActionParam(final Fight fight, final FightObject fightObject) {
        final boolean poison = fightObject.getTempCache("poison", false);
        final boolean recover = fightObject.getTempCache("recover", false);
        final boolean alive = fightObject.getTempCache("alive", false);
        final int poisonValue = fightObject.getTempCache("poisonValue", 0);
        final int recoverLife = fightObject.getTempCache("recoverLife", 0);
        final int recoverMana = fightObject.getTempCache("recoverManaValue", 0);
        if (recover) {
            final NutMap map = NutMap.NEW();
            map.setv("currLife", (Object)fightObject.getCurrLife());
            map.setv("changeLife", (Object)recoverLife);
            if (recoverMana > 0) {
                map.setv("currMana", (Object)fightObject.getCurrMana());
                map.setv("changeMana", (Object)recoverMana);
            }
            map.setv("missed", (Object)2);
            return map;
        }
        if (poison && alive) {
            final NutMap map = NutMap.NEW();
            if (!fightObject.isFrozen()) {
                map.setv("currLife", (Object)fightObject.getCurrLife());
                map.setv("changeLife", (Object)(-poisonValue));
                map.setv("missed", (Object)1);
            }
            else {
                fightObject.removeStatus(1005);
                map.setv("clearStatus", (Object)1005);
                map.setv("missed", (Object)2);
            }
            return map;
        }
        return null;
    }
    
    public RespExcuteFight excuteSendFightMsg(final Fight fight) {
        final ActionParam actionParam = fight.getActionParam();
        final int fightRound = actionParam.getFightRound();
        final List<ActionDetailParam> actionDetailParams = actionParam.getActionDetailParams();
        if (actionDetailParams != null && actionDetailParams.size() > 0) {
            final RespExcuteFight respExcuteFight = new RespExcuteFight();
            respExcuteFight.setRound(fightRound);
            final List<ExcuteFightParam> excuteFightParams = this.getExcuteFightParams(actionDetailParams);
            if (excuteFightParams.size() > 0) {
                respExcuteFight.setExcuteFightParams(excuteFightParams);
            }
            respExcuteFight.setEndTime(String.valueOf(System.currentTimeMillis()));
            return respExcuteFight;
        }
        return null;
    }
    
    public List<ExcuteFightParam> getExcuteFightParams(final List<ActionDetailParam> actionDetailParams) {
        final List<ExcuteFightParam> excuteFightParams = new ArrayList<ExcuteFightParam>();
        for (final ActionDetailParam actionDetailParam : actionDetailParams) {
            final ExcuteFightParam excuteFightParam = this.getExcuteParam(actionDetailParam);
            if (excuteFightParam != null) {
                excuteFightParams.add(excuteFightParam);
            }
        }
        return excuteFightParams;
    }
    
    private ExcuteFightParam getExcuteParam(final ActionDetailParam actionDetailParam) {
        if (actionDetailParam == null) {
            return null;
        }
        final ExcuteFightParam excuteFightParam = new ExcuteFightParam();
        if (actionDetailParam.getType() != -1) {
            excuteFightParam.setType(actionDetailParam.getType());
        }
        if (actionDetailParam.getSkillSerial() != -1) {
            excuteFightParam.setSkillId(actionDetailParam.getSkillSerial());
            excuteFightParam.setHfType(actionDetailParam.getHfType());
        }
        if (actionDetailParam.getColorId() != 0) {
            excuteFightParam.setColorId(actionDetailParam.getColorId());
        }
        if (actionDetailParam.getAttackerId() != 0) {
            excuteFightParam.setAttaId(actionDetailParam.getAttackerId());
        }
        if (actionDetailParam.getRoleId() != 0L) {
            excuteFightParam.setRoleId(actionDetailParam.getRoleId());
        }
        final List<RefreshPet> petList = actionDetailParam.getPetList();
        if (petList != null && petList.size() > 0) {
            excuteFightParam.setPetList(petList);
        }
        final List<CombatAddObject> combatAddObjects = actionDetailParam.getCombatAddObjects();
        if (combatAddObjects != null && combatAddObjects.size() > 0) {
            excuteFightParam.setCombatAddObjects(combatAddObjects);
        }
        final List<FightObjectParam> targetParams = actionDetailParam.getTargetParamList();
        if (targetParams.size() > 0) {
            final List<ExcuteFightObjectParam> targetExcuteParams = new ArrayList<ExcuteFightObjectParam>();
            for (final FightObjectParam targetParam : targetParams) {
                if (targetParam != null) {
                    final ExcuteFightObjectParam excuteFightObjectParam = this.excuteParamTarget(targetParam);
                    targetExcuteParams.add(excuteFightObjectParam);
                }
            }
            if (targetExcuteParams.size() > 0) {
                excuteFightParam.setTargetParams(targetExcuteParams);
            }
        }
        final List<FightObjectParam> attackerParam = actionDetailParam.getAttackerParam();
        if (attackerParam != null && attackerParam.size() > 0) {
            final FightObjectParam fightObjectParam = attackerParam.get(0);
            final ExcuteFightObjectParam attaParam = new ExcuteFightObjectParam();
            final List<FiedValue> fiedValues = this.getFiedValues(fightObjectParam, false);
            if (fiedValues != null) {
                attaParam.setFiedValues(fiedValues);
            }
            if (fightObjectParam.getDamageType() != null && fightObjectParam.getDamageType().size() > 0) {
                attaParam.setDamageType(fightObjectParam.getDamageType());
            }
            if (fightObjectParam.getStatusMap() != null && fightObjectParam.getStatusMap().size() > 0) {
                final List<Integer> statusList = new ArrayList<Integer>();
                for (final Map.Entry<Integer, Integer> entry : fightObjectParam.getStatusMap().entrySet()) {
                    statusList.add(entry.getKey());
                    statusList.add(entry.getValue());
                }
                if (statusList.size() > 0) {
                    attaParam.setStatusList(statusList);
                }
            }
            if (fightObjectParam.getClearStatus() != null && fightObjectParam.getClearStatus().size() > 0) {
                attaParam.setClearStatus(fightObjectParam.getClearStatus());
            }
            if (excuteFightParam.getAttaParams() == null) {
                excuteFightParam.setAttaParams(new ArrayList<ExcuteFightObjectParam>());
            }
            excuteFightParam.getAttaParams().add(attaParam);
        }
        return excuteFightParam;
    }
    
    public ExcuteFightObjectParam excuteParamTarget(final FightObjectParam targetParam) {
        final ExcuteFightObjectParam excuteFightObjectParam = new ExcuteFightObjectParam();
        final List<FiedValue> fiedValues = this.getFiedValues(targetParam, true);
        if (fiedValues != null) {
            excuteFightObjectParam.setFiedValues(fiedValues);
        }
        if (targetParam.getDamageType() != null && targetParam.getDamageType().size() > 0) {
            excuteFightObjectParam.setDamageType(targetParam.getDamageType());
        }
        if (targetParam.getStatusMap() != null && targetParam.getStatusMap().size() > 0) {
            final List<Integer> statusList = new ArrayList<Integer>();
            for (final Map.Entry<Integer, Integer> entry : targetParam.getStatusMap().entrySet()) {
                statusList.add(entry.getKey());
                statusList.add(entry.getValue());
            }
            if (statusList.size() > 0) {
                excuteFightObjectParam.setStatusList(statusList);
            }
        }
        if (targetParam.getClearStatus() != null && targetParam.getClearStatus().size() > 0) {
            excuteFightObjectParam.setClearStatus(targetParam.getClearStatus());
        }
        return excuteFightObjectParam;
    }
    
    public List<FiedValue> getFiedValues(final FightObjectParam targetParam, final boolean isTarget) {
        final List<FiedValue> fiedValues = new ArrayList<FiedValue>();
        fiedValues.add(new FiedValue("targetId", (byte)3, targetParam.getObjId()));
        int isHit = 1;
        if (!targetParam.isHit()) {
            isHit = 0;
        }
        if (isTarget) {
            fiedValues.add(new FiedValue("isHit", (byte)1, isHit));
        }
        else if (4 == targetParam.getType()) {
            fiedValues.add(new FiedValue("isHit", (byte)1, isHit));
        }
        if (targetParam.getChangeLife() != 0) {
            final FiedValue fiedValue = new FiedValue("changeHp", (byte)3, targetParam.getChangeLife());
            fiedValues.add(fiedValue);
        }
        if (targetParam.getChangeMana() != 0) {
            final FiedValue fiedValue = new FiedValue("changeMp", (byte)3, targetParam.getChangeMana());
            fiedValues.add(fiedValue);
        }
        if (isTarget) {
            if (targetParam.getChangeLife() != 0) {
                fiedValues.add(new FiedValue("currHp", (byte)3, targetParam.getCurrentLife()));
            }
        }
        else {
            fiedValues.add(new FiedValue("currHp", (byte)3, targetParam.getCurrentLife()));
        }
        if (isTarget) {
            if (targetParam.getChangeMana() != 0) {
                fiedValues.add(new FiedValue("currMp", (byte)3, targetParam.getCurrentMana()));
            }
        }
        else {
            fiedValues.add(new FiedValue("currMp", (byte)3, targetParam.getCurrentMana()));
        }
        if (targetParam.getMaxLife() != 0) {
            final FiedValue fiedValue = new FiedValue("maxHp", (byte)3, targetParam.getMaxLife());
            fiedValues.add(fiedValue);
        }
        if (targetParam.getMaxMana() != 0) {
            final FiedValue fiedValue = new FiedValue("maxMp", (byte)3, targetParam.getMaxMana());
            fiedValues.add(fiedValue);
        }
        if (targetParam.getSpecialStatus() > 0) {
            fiedValues.add(new FiedValue("specialStatus", (byte)3, targetParam.getSpecialStatus()));
        }
        if (targetParam.getGuardTarget() > 0) {
            fiedValues.add(new FiedValue("guardTarget", (byte)3, targetParam.getGuardTarget()));
        }
        final RespFightShouting fightShouting = targetParam.getFightShouting();
        if (fightShouting != null) {
            if (fightShouting.getId() != 0) {
                final FiedValue fiedValue = new FiedValue("msgTarget", (byte)3, fightShouting.getId());
                fiedValues.add(fiedValue);
            }
            if (fightShouting.getChannel() != 0) {
                final FiedValue fiedValue = new FiedValue("channel", (byte)2, fightShouting.getChannel());
                fiedValues.add(fiedValue);
            }
            if (fightShouting.getMsg() != null && !"".equals(fightShouting.getMsg())) {
                final FiedValue fiedValue = new FiedValue("msg", (byte)4, fightShouting.getMsg());
                fiedValues.add(fiedValue);
            }
            if (fightShouting.getShow_time() != -1) {
                final FiedValue fiedValue = new FiedValue("msgST", (byte)3, fightShouting.getShow_time());
                fiedValues.add(fiedValue);
            }
            final FiedValue fiedValue = new FiedValue("msgDelayTime", (byte)3, fightShouting.getMsgDelayTime());
            fiedValues.add(fiedValue);
        }
        if (fiedValues.size() > 0) {
            return fiedValues;
        }
        return null;
    }
    
    public Map<Integer, Integer> sendFightObjectStatus(final Fight fight, final FightObject fightObject) {
        final NutMap statusMap = fightObject.getStatusMap();
        final List<Integer> removeList = new ArrayList<Integer>();
        final Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        statusMap.forEach((key, value) -> {
            if (value instanceof Integer && key.matches("^[0-9]+$")) {
                if ((int)value > 0) {
                    map.put(Integer.parseInt(key), Integer.parseInt(String.valueOf(value)));
                }
                else if (200 == Integer.parseInt(key)) {
                    TianFuProcessor.queryProcessor(323301).otherExcute(fight, fightObject, false);
                    map.put(Integer.parseInt(key), fightObject.getIcon());
                    removeList.add(Integer.parseInt(key));
                }
            }
            return;
        });
        if (removeList.size() > 0) {
            final Iterator<Integer> iterator = removeList.iterator();
            while (iterator.hasNext()) {
                Integer key = iterator.next();
                fightObject.removeStatus(key);
            }
        }
        if (map.size() > 0) {
            return map;
        }
        return null;
    }
    
    public void excuteRoundEnd(final Fight fight) {
        this.fightExecutorNewService.addFightTask(fight, new FightingTask() {
            @Override
            public void run() {
                fight.cancelTask(fight.getTimeOutTask());
                final TimeOutNewTask timeOut = new TimeOutNewTask(fight.getId());
                final ScheduledFuture<?> timeOutTask = SchedulerManager.getInstance().schedule(timeOut, 30500L);
                fight.setTimeOutTask(timeOutTask);
            }
        });
        for (final FightObject fightObject : fight.getAllFightObjects()) {
            if (!fightObject.isDoAction() && fightObject.isPlayer()) {
                final Role role = DataCache.ONLINE_ROLES.get(fightObject.getRole().getId());
                if (role == null) {
                    continue;
                }
                this.fightNewService.autoFightOne(fight, fightObject);
                this.setRoundStartOther(fightObject, fight);
                final PlayerFightObject playerFightObject = (PlayerFightObject)fightObject;
                playerFightObject.setAnimationFinish(false);
            }
        }
        this.fightNewService.excuteRoundStartSend(fight);
        fight.setRoundType(1);
    }
    
    public void setRoundStartOther(final FightObject fightObject, final Fight fight) {
        if (fightObject.isPlayer()) {
            final RespRoundStartParam respRoundStartParam = this.fightNewService.getRespRoundStartParam(fight, fightObject.getId());
            final List<SkillCool> cools = fightObject.getSkillCoolList();
            if (cools.size() > 0) {
                respRoundStartParam.addSkillCools(cools);
            }
            if (fightObject.getAutoFight() == 1) {
                respRoundStartParam.setAutoFight(true);
            }
            final AutoFightSkillParam autoFightSkillParam = this.selfAuto(fightObject, fight);
            if (autoFightSkillParam != null) {
                respRoundStartParam.addAutoFightSkillParam(autoFightSkillParam);
            }
            final FightObject petObject = fight.queryFightObject(fightObject.getFightPetId());
            if (petObject != null) {
                final List<SkillCool> petCools = petObject.getSkillCoolList();
                if (petCools.size() > 0) {
                    respRoundStartParam.addSkillCools(petCools);
                }
                if (fightObject.getAutoFight() == 1) {
                    petObject.setAutoFight(1);
                }
                final AutoFightSkillParam autoFightSkillParamPet = this.selfAuto(petObject, fight);
                if (autoFightSkillParamPet != null) {
                    respRoundStartParam.addAutoFightSkillParam(autoFightSkillParamPet);
                }
            }
            fight.getRespRoundStartParamMap().put(respRoundStartParam.getObjId(), respRoundStartParam);
        }
    }
}
