
package com.kitty.system.game.fightnew.service;

import com.kitty.system.game.fightnew.messages.RespFightObjectAttr;
import com.kitty.system.game.fightnew.messages.vo.FightObjectAttr;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.messages.old.ReqAutoFightSetData;
import com.kitty.system.game.partner.service.PartnerService;
import org.springframework.beans.BeanUtils;
import com.kitty.system.game.fightnew.spectator.service.FightSpectatorExecutorNewService;
import com.kitty.system.game.fightnew.spectator.service.FightSpectatorNewService;
import java.util.Set;
import com.kitty.mina.cache.PetCache;
import com.kitty.system.game.fightnew.messages.vo.ExcuteFightParam;
import com.kitty.system.game.fightnew.model.SkillParam;
import com.kitty.system.game.fightnew.model.PetFightInParam;
import com.kitty.system.game.fightnew.messages.RespExcuteFight;
import com.kitty.system.game.fightnew.bean.RoundParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.LoginEvent;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.game.fightnew.utils.FightRateUtil;
import com.kitty.system.game.fightnew.utils.SkillCompute;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import com.kitty.system.common.enter.FieldValue;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.fightnew.book.drug.DrugProcessor;
import com.kitty.system.game.bag.service.BagService;
import java.util.Optional;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.game.fightnew.bean.AstrictSkill;
import com.kitty.system.game.fightnew.messages.vo.CombatAddObject;
import com.kitty.system.init.domain.SysDataMonster;
import com.kitty.common.model.Pos;
import org.springframework.util.CollectionUtils;
import com.kitty.system.game.service.MonsterService;
import com.kitty.system.game.fightnew.factory.FightFactory;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.partner.domain.Partner;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.messages.vo.FightMember;
import java.util.concurrent.ScheduledFuture;
import com.kitty.common.core.SchedulerManager;
import com.kitty.system.game.fightnew.task.AutoChuShouNewTask;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import java.util.Arrays;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.enums.war.warskill.skilldef.TARGETTYPE;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.init.domain.SysDrug;
import com.kitty.system.game.fightnew.book.vo.SkillAutoPicker;
import com.kitty.system.game.enter.message.RespCombatOpponets;
import com.kitty.system.game.enter.message.RespCombatFriends;
import com.kitty.system.game.fightnew.model.LifeManaParam;
import org.nutz.lang.util.NutMap;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.EventDispatcher;
import org.nutz.lang.Lang;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.fightnew.reward.FightRewardHandler;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.fightnew.messages.old.RespEndCombat;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.domain.Member;
import com.kitty.mina.task.ThreadLocalUtil;
import com.kitty.system.game.fightnew.utils.FightUtils;
import java.util.concurrent.TimeUnit;
import com.kitty.system.game.fightnew.messages.old.RespFightShouting;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.model.MagicAttackParam;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import com.kitty.system.game.vo.utils.StatusConst;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.utils.SkillTargetPicker;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.bean.MonsterFightObject;
import com.kitty.system.game.fightnew.model.SkillAction;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.enums.war.wardef.RACE;
import com.kitty.system.game.fightnew.bean.PartnerFightObject;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;
import com.kitty.system.game.fightnew.model.ActionParam;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.Random;
import com.kitty.logs.LoggerUtils;
import com.kitty.mina.message.Message;
import com.kitty.system.game.fightnew.utils.FightMessageUtil;
import com.kitty.system.game.fightnew.messages.RespInputInstruct;
import com.kitty.system.game.fightnew.messages.old.RespFightCommandInfo;
import com.kitty.system.game.fightnew.utils.MessageAssistUtil;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.task.FightingTask;
import com.kitty.system.game.fightnew.messages.ReqFightHandle;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.model.RefreshPet;
import com.kitty.system.game.fightnew.model.AutoFightSkillParam;
import com.kitty.system.game.fightnew.messages.vo.SkillCool;
import java.util.List;
import com.kitty.system.game.fightnew.messages.RespFightInParam;
import com.kitty.system.game.fightnew.messages.RespRoundStartParam;
import org.apache.mina.core.session.IoSession;
import java.util.Iterator;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.utils.FightBeforeActionUtil;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.logs.LoggerFunction;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.fightRecord.service.FightRecordService;
import com.kitty.system.game.broadcast.BroadcastService;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.pet.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class FightNewService
{
    private final Logger logger;
    @Autowired
    public FightExecutorNewService fightExecutorService;
    @Autowired
    public PetService petService;
    @Autowired
    public RoleService roleService;
    @Autowired
    public TeamService teamService;
    @Autowired
    public FightMessageNewService fightMessageNewService;
    @Autowired
    public BroadcastService broadcastService;
    @Autowired
    public FightRecordService fightRecordService;
    @Autowired
    ServerService serverService;
    
    public FightNewService() {
        this.logger = LoggerFunction.FIGHT.getLogger();
    }
    
    public void whileSendStart(final Fight fight) {
        FightBeforeActionUtil.beforeAction(fight);
        for (final FightObject fightObject : fight.getAllFightObjects()) {
            if (fight.isPvp() == 1) {
                fightObject.setAutoFight(0);
            }
            final IoSession session = fightObject.getSession();
            if (session == null) {
                continue;
            }
            int objId = fightObject.getId();
            if (fightObject.isPet()) {
                objId = fightObject.getHostId();
            }
            final RespRoundStartParam respRoundStartParam = this.getRespRoundStartParam(fight, objId);
            RespFightInParam roleFightInParam = null;
            if (fightObject.isPlayer()) {
                roleFightInParam = fight.getRoleFightInParam(fightObject.getRole());
                if (fightObject.getAutoFight() == 1) {
                    respRoundStartParam.setAutoFight(true);
                }
            }
            if (LOGIC.LOGICTYPE_PET.getValue() == fightObject.getLogic()) {
                fight.excuteReduceRatio(fightObject, true);
            }
            final List<SkillCool> cools = fightObject.getSkillCoolList();
            if (cools.size() > 0 && roleFightInParam != null) {
                respRoundStartParam.addSkillCools(cools);
            }
            final AutoFightSkillParam autoFightSkillParam = this.fightMessageNewService.selfAuto(fightObject, fight);
            if (autoFightSkillParam != null) {
                if (fightObject.isPlayer()) {
                    final FightObject petObject = fight.queryFightObject(fightObject.getFightPetId());
                    if (petObject != null) {
                        petObject.setAutoFight(1);
                    }
                }
                respRoundStartParam.addAutoFightSkillParam(autoFightSkillParam);
            }
            if (!fightObject.isPlayer()) {
                continue;
            }
            if (fightObject.getFightPetId() > 0 && fightObject.isPlayer()) {
                final List<RefreshPet> refreshPets = this.getFightInParamPetList(fight, fightObject);
                roleFightInParam.setPetList(refreshPets);
            }
            final TianFuProcessor tianFuProcessor = TianFuProcessor.queryProcessor(324507);
            tianFuProcessor.passive(fight, fightObject, null);
            fight.getRespRoundStartParamMap().put(respRoundStartParam.getObjId(), respRoundStartParam);
        }
    }
    
    public void quitFight(Fight fight) {
        fight = this.getFight(fight.getId());
        if (fight == null) {
            return;
        }
        this.quiteFightAll(fight, true);
        if (fight.getFleeList().size() > 0) {
            this.excuteFlee(fight);
        }
        this.clearFight(fight);
    }
    
    public void excuteFlee(final Fight fight) {
        final List<Integer> fleeList = fight.getFleeList();
        if (fleeList != null && fleeList.size() > 0) {
            for (int i = fleeList.size() - 1; i >= 0; --i) {
                final Integer fightObjectId = fleeList.get(i);
                this.deleteRoleFightData(fight, fightObjectId);
                final Role role = this.roleService.getPlayer(fightObjectId);
                if (role != null) {
                    this.broadcastService.sendTitleInfoNew(role);
                }
                fleeList.remove(fightObjectId);
            }
            if (fight.getListA().size() <= 0 || fight.getListB().size() <= 0) {
                fight.destroy();
            }
        }
    }
    
    public void checkRoundShouting(final Fight fight, final Role role) {
        if (fight.getData() == null) {
            return;
        }
        final List<String> roundStartTips = fight.getData().getAsList("fight_round_start_shouting", (Class)String.class);
        if (roundStartTips == null || roundStartTips.size() == 0) {
            return;
        }
        final int roundStartShoutingPeriod = fight.getData().getInt("fight_round_start_shouting_period", 0);
        if (roundStartShoutingPeriod > 0 && fight.getRound() % roundStartShoutingPeriod != 0) {
            return;
        }
        final int remainRound = (fight.getRound() == 0) ? fight.getMaxRound() : (fight.getMaxRound() - fight.getRound() + 1);
    }
    
    public void doActionNew(final ReqFightHandle reqFightHandle) {
        if (reqFightHandle.getType() == 0) {
            if (reqFightHandle.getAttackerId() == reqFightHandle.getTargetId()) {
                reqFightHandle.setTargetId(0);
            }
        }
        else if (reqFightHandle.getType() == 1) {
            reqFightHandle.setTargetId(reqFightHandle.getAttackerId());
        }
        final int hostId = this.getHostId(reqFightHandle.getAttackerId());
        final Fight fight = this.getFightByRoleId(reqFightHandle.getAttackerId());
        if (fight == null) {
            return;
        }
        this.fightExecutorService.addFightTask(fight, new FightingTask() {
            @Override
            public void run() {
                try {
                    FightNewService.this.fightDoAction(reqFightHandle, hostId, fight);
                }
                catch (final Exception e) {
                    FightNewService.this.logger.error("战斗出手报错了战场ID[{}]出手id[{}}{}{}{}", new Object[] { fight.getId(), reqFightHandle.getAttackerId(), e.getMessage(), e.getStackTrace(), e });
                }
            }
        });
    }
    
    public void animationPlayEnd(final Fight fight, final int roleId) {
        fight.removeRoundFinishTask(roleId);
        final FightObject fightObject = fight.queryFightObject(roleId);
        if (fightObject == null) {
            if (fight.getFleeList().contains(roleId)) {
                this.excuteFlee(fight);
            }
            return;
        }
        final PlayerFightObject playerObj = (PlayerFightObject)fightObject;
        playerObj.setAnimationFinish(true);
        boolean allAnimationEnd = true;
        for (final FightObject object : fight.getAllFightObjects()) {
            if (!object.isPlayer()) {
                continue;
            }
            final PlayerFightObject player = (PlayerFightObject)object;
            if (!player.isAnimationFinish()) {
                allAnimationEnd = false;
                break;
            }
        }
        if (allAnimationEnd) {
            if (fight.isOver()) {
                this.fightExecutorService.addFightTask(fight, new FightingTask() {
                    @Override
                    public void run() {
                        if (fight.isOver()) {
                            try {
                                FightNewService.this.quitFight(fight);
                            }
                            catch (final Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                return;
            }
            fight.setRound(fight.getRound() + 1);
            for (final FightObject object : fight.getAllFightObjects()) {
                object.setTargetId(0);
                object.setDoAction(false);
            }
            this.fightMessageNewService.excuteRoundEnd(fight);
        }
    }
    
    private void fightDoAction(final ReqFightHandle reqFightHandle, final int hostId, final Fight fight) {
        if (fight.isOver()) {
            return;
        }
        final FightObject fightObject = fight.queryFightObject(reqFightHandle.getAttackerId());
        if (fightObject == null) {
            return;
        }
        final Role role = fightObject.getRole();
        if (role == null) {
            return;
        }
        if (fightObject.isPlayer() && reqFightHandle.getType() == 3 && reqFightHandle.getSkillId() == 0) {
            final int petId = reqFightHandle.getTargetId();
            final Pet pet = this.petService.selectPet(role).get(petId);
            if (pet != null) {
                final int petLevel = pet.getPetLv() + pet.getRebirth() * 1000;
                final int roleLevle = role.getLevel() + role.getRelive() * 1000;
                if (pet == null || petLevel - roleLevle > 10 || pet.getPetId().equals(fightObject.getFightPetId()) || FightExecutorNewService.PETID_ROLEID.get(petId) != null) {
                    MessageAssistUtil.sendIsSuccess(role, fightObject.getId(), (byte)4, null);
                    return;
                }
            }
        }
        if (fightObject.isCompleted(fight.getRound(), false)) {
            return;
        }
        if (reqFightHandle.getType() != 6 && reqFightHandle.getType() != 3 && reqFightHandle.getType() != 12) {
            fightObject.setAutoTarget(reqFightHandle.getTargetId());
            fightObject.setTargetId(reqFightHandle.getTargetId());
        }
        this.updateDoActionStatus(fight, reqFightHandle.getAttackerId(), reqFightHandle.getTargetId(), reqFightHandle.getType(), reqFightHandle.getSkillId());
        this.monsterAI(fight);
        MessageAssistUtil.sendIsSuccess(role, fightObject.getId(), (byte)1, null);
        final RespInputInstruct respInputInstruct = new RespInputInstruct(fightObject.getId(), fightObject.getTargetId(), fightObject.getType(), fightObject.getSkillId(), 0);
        FightMessageUtil.sendFightPackage(fight, respInputInstruct);
        try {
            this.roundAction(fight);
        }
        catch (final Exception e) {
            e.printStackTrace();
            LoggerUtils.error("错误", e);
        }
    }
    
    private void monsterAI(final Fight fight) {
        if (fight.isPkFight()) {
            return;
        }
        final Random random = new Random();
        fight.getListB().forEach(attacker -> {
            if (!attacker.isDoAction()) {
                if (fight.getData() != null && fight.getData().getBoolean("fight_guess_true", false)) {
                    attacker.setType(99);
                }
                else if (fight.getData() != null && fight.getData().getBoolean("fight_order_target", false)) {
                    attacker.setType(1);
                }
                else if (this.canUseLifeItem(fight)) {
                    if (attacker.getCurrLife() < attacker.getMaxLife() * 0.2) {
                        attacker.setType(5);
                    }
                    else {
                        final FightObject hostObject = fight.queryFightObject(attacker.getHostId());
                        if (hostObject != null && hostObject.isFallDown()) {
                            attacker.setTargetId(hostObject.getId());
                            attacker.setType(5);
                        }
                        else {
                            attacker.setType((attacker.getSkillIds().size() == 0) ? 0 : 7);
                            if (attacker.getType() == 7) {
                                final int index = random.nextInt(attacker.getSkillIds().size());
                                attacker.setSkillId(attacker.getSkillIds().get(index).getSkillId());
                            }
                            else {
                                attacker.setSkillId(0);
                            }
                        }
                    }
                }
                else if (fight.getType() != FIGHT_TYPE.MONSTER.getValue()) {
                    final boolean canUseMagic = attacker.getSkillIds().size() > 0;
                    attacker.setType(canUseMagic ? 7 : 0);
                    if (attacker.getType() == 7) {
                        final int index2 = random.nextInt(attacker.getSkillIds().size());
                        attacker.setSkillId(attacker.getSkillIds().get(index2).getSkillId());
                    }
                    else {
                        attacker.setSkillId(0);
                    }
                }
                attacker.setDoAction(true);
            }
        });
    }
    
    private void roundAction(final Fight fight) {
        if (!fight.isStartFight()) {
            return;
        }
        final ActionParam actionParam = ActionParamUtils.createActionParam(fight);
        fight.setActionParam(actionParam);
        fight.setRoundType(2);
        fight.cancelTask(fight.getTimeOutTask());
        if (fight.getRound() >= fight.getMaxRound()) {
            this.logger.error("最大回合数=={}", fight.getMaxRound());
            boolean isReward = false;
            if (fight.isPvp() == 1) {
                fight.setFightOverReason((byte)1);
                isReward = true;
            }
            this.quiteFightAll(fight, isReward);
            this.clearFight(fight);
            return;
        }
        PassiveSkillUtils.roundStart(fight);
        this.excuteRoundStartPoison(fight);
        PetPassiveSkillUtil.roundStartPassive(fight);
        PassiveSkillUtils.afterPrintCommand(fight);
        this.actionStart(fight, fight.getAllFightObjects());
        final Map<Integer, List<Integer>> clearStatusMap = this.excuteStatusReduce(fight);
        PassiveSkillUtils.statusOver(fight, clearStatusMap);
        PassiveSkillUtils.roundEnd(fight);
        final boolean isRoundFinishTask = this.sendFightParams(fight, false);
        this.besure(fight);
        if (isRoundFinishTask) {
            this.maxRoundTimer(fight, 180000L);
        }
        fight.setRoundType(3);
    }
    
    private void actionStart(final Fight fight, final List<FightObject> list) {
        if (fight.isOver()) {
            return;
        }
        final List<FightObject> copyFightObjs = new ArrayList<FightObject>(list);
        Collections.sort(copyFightObjs);
        final Iterator<FightObject> it = copyFightObjs.iterator();
        while (it.hasNext()) {
            final FightObject next = it.next();
            it.remove();
            if (next == null) {
                continue;
            }
            if (fight.queryFightObject(next.getId()) == null) {
                continue;
            }
            if (fight.queryFightObject(next.getId()) == null) {
                continue;
            }
            if (LOGIC.LOGICTYPE_PET.getValue() == next.getLogic() && this.isAstrictSkill(fight, next)) {
                continue;
            }
            try {
                if (next.isCompleted(fight.getRound(), true)) {
                    continue;
                }
                this.doActionResult(next, fight);
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
            break;
        }
        if (copyFightObjs.size() > 0) {
            this.actionStart(fight, copyFightObjs);
        }
    }
    
    private void doActionResult(final FightObject attacker, final Fight fight) {
        if (attacker.isFallDown()) {
            return;
        }
        if (attacker.isMonster()) {
            this.getMonsterActionType(fight, attacker);
        }
        if (attacker.isPartner() && attacker.getSkillId() == 0 && !attacker.isConfusion()) {
            attacker.setSkillId(((PartnerFightObject)attacker).getFightSkillId());
            attacker.setType(7);
        }
        int type = attacker.getType();
        if (attacker.isFallDown() && type != 4 && type != 12) {
            return;
        }
        if (type == 7 && attacker.getSkillId() <= 0) {
            attacker.setType(0);
            type = 0;
        }
        if (attacker.getMorph() > 0 && attacker.getRace() == RACE.RACE_LONG.getValue()) {
            final RoleSkill skill = attacker.getSkillBy(attacker.getMorph());
            final SkillProcessor processor = SkillProcessor.queryProcessor(attacker.getMorph());
            final SkillAction skillAction = new SkillAction(fight, attacker, skill, processor);
            skillAction.dragonMorph();
        }
        if (attacker.getStatusValue(1011) > 0 && type != 1) {
            attacker.removeStatus(1011);
            final List<Integer> clearStatusList = new ArrayList<Integer>();
            clearStatusList.add(1011);
            ActionParamUtils.clearStatus(fight, attacker, attacker, 43, 0, 2, clearStatusList);
        }
        if (attacker.getStatusMap().getInt(String.valueOf(1019)) > 0) {
            type = 0;
            this.getSacrificeJusticeExcute(fight, attacker);
        }
        if (attacker.isPlayer()) {
            ((PlayerFightObject)attacker).instalAbnormalRoundMap(fight);
        }
        PassiveSkillUtils.beforeDoAction(fight, attacker);
        if (type == 1) {
            this.defense(attacker, fight);
        }
        else if (type == 0) {
            this.normalAttack(attacker, fight);
        }
        else if (type == 7) {
            this.magicAttack(attacker, fight);
        }
        else if (type == 5) {
            this.useItem(attacker, fight);
        }
        else if (type == 4) {
            this.flee(attacker, fight);
        }
        else if (type == 3) {
            if (attacker instanceof MonsterFightObject) {
                SpringUtils.getBean(FightMonsterNewService.class).selectMonster(fight, attacker);
            }
            else {
                this.selectPet(attacker, fight);
            }
        }
        else if (type != 6) {
            if (type == 12) {
                this.callbackPet(attacker, fight, true);
            }
            else if (type == 8) {
                this.guard(attacker, fight);
            }
        }
        PetPassiveSkillUtil.deathExcute(fight);
    }
    
    private void defense(final FightObject fightObject, final Fight fight) {
        fightObject.recordAction(fight);
        PassiveSkillUtils.selfSellEnd(fight, fightObject);
        if (this.checkDeStatus(fight, fightObject)) {
            return;
        }
    }
    
    public void normalAttack(final FightObject attacker, final Fight fight) {
        attacker.setType(0);
        attacker.setSkillId(0);
        attacker.createFallDownList();
        final boolean isConfusion = attacker.isConfusion();
        if (!isConfusion && this.checkDeStatus(fight, attacker)) {
            return;
        }
        if (attacker.getId() == attacker.getTargetId()) {
            attacker.setTargetId(0);
        }
        attacker.recordAction(fight);
        FightObject fightTarget = fight.queryFightObject(attacker.getTargetId());
        if (fightTarget == null || fightTarget.isFallDown()) {
            fightTarget = this.getPhysicTarget(attacker, fight);
            if (fightTarget == null || fightTarget.isFallDown()) {
                return;
            }
            attacker.setTargetId(fightTarget.getId());
            fightTarget = fight.queryFightObject(attacker.getTargetId());
        }
        final List<Integer> fightObjectIds = SkillTargetPicker.getFightObjectIds(fight, attacker, true);
        if (!fightObjectIds.contains(fightTarget.getId())) {
            return;
        }
        if (isConfusion) {
            final List<FightObject> arrayList = this.getConfusionFightTargets(attacker, fight);
            if (arrayList.size() <= 0) {
                return;
            }
            final Random random = new Random();
            final int index = random.nextInt(arrayList.size());
            fightTarget = arrayList.get(index);
            attacker.setTargetId(fightTarget.getId());
        }
        if (fightTarget.isFallDown()) {
            return;
        }
        PassiveSkillUtils.beforePhysicalAttack(fight, attacker, fightTarget);
        final BattleUnit battleUnit = new BattleUnit(fight, attacker, fightTarget);
        battleUnit.triggerPhysic();
        if (fightTarget.isFallDown()) {
            attacker.addFallDownList(fightTarget.getId());
        }
        PassiveSkillUtils.normalAttackAfter(fight, attacker, battleUnit.getTarget(), battleUnit.getActionDetailParam());
        PassiveSkillUtils.phyAttackEnd(fight, attacker, battleUnit, battleUnit.getActionDetailParam());
        fight.getActionParam().getActionDetailParams().add(battleUnit.getActionDetailParam());
        PassiveSkillUtils.sufferHarm(fight, battleUnit.getTarget(), battleUnit.getPhyHurt(), attacker);
        final List<FightObject> fallDownObjs = fight.getAllFightObjects().stream().filter(obj -> obj.isFallDown() && obj.isPet()).collect(Collectors.toList());
        for (final FightObject target : fallDownObjs) {
            final List<Integer> statusList = new ArrayList<Integer>();
            for (final int status : StatusConst.getPositiveStatus()) {
                if (target.getStatusValue(status) > 0) {
                    statusList.add(status);
                }
            }
            PassiveSkillUtils.exitOrClearPositive(fight, attacker, target, statusList);
        }
        this.checkOrderTarget(fight, Collections.singletonList(attacker.getTargetId()));
        if (fightTarget.isFallDown()) {
            this.checkGuessSuccess(fight);
            fight.onActorDie(fightTarget);
        }
        if (attacker.isFallDown()) {
            fight.onActorDie(attacker);
        }
        PassiveSkillUtils.selfSellEnd(fight, attacker);
    }
    
    private void magicAttack(final FightObject attacker, final Fight fight) {
        if (this.checkDeStatus(fight, attacker)) {
            return;
        }
        attacker.recordAction(fight);
        attacker.createFallDownList();
        final RoleSkill skill = attacker.getSkillBy(attacker.getSkillId());
        if (skill != null) {
            Integer targetsCount = 0;
            if (LOGIC.LOGICTYPE_PET.getValue() == attacker.getLogic() || attacker.isMonster()) {
                final boolean flag = PetTriggerPassiveSkill.checkSkillIsUsable(fight, attacker, skill.getSkillId(), PET_PASSIVE_SKILL_TRIGGER_DICT.INITIATIVE_SKILL.getValue());
                if (!flag) {
                    if (attacker.isMonster()) {
                        this.setNormalAttack(fight, attacker);
                    }
                    return;
                }
            }
            final SkillProcessor processor = SkillProcessor.queryProcessor(skill.getSkillId());
            if (processor == null) {
                this.getSkillFail(fight, attacker, String.format("获取技能信息失败，未找到技能：%d", skill.getSkillId()));
                return;
            }
            if (LOGIC.LOGICTYPE_PET.getValue() == attacker.getLogic() || attacker.isMonster()) {
                final boolean flag2 = processor.isExcute(fight, attacker);
                if (!flag2) {
                    if (attacker.isMonster()) {
                        this.setNormalAttack(fight, attacker);
                    }
                    return;
                }
            }
            if (processor == null) {
                this.getSkillFail(fight, attacker, String.format("获取技能信息失败，未找到技能：%d", skill.getSkillId()));
                return;
            }
            targetsCount = processor.targetCount(fight, attacker);
            final SkillAction skillAction = new SkillAction(fight, attacker, skill, processor);
            List<Integer> targetList = new ArrayList<Integer>();
            if (attacker.isPet()) {
                targetList = processor.getTargetIds(fight, attacker);
                if (targetList == null || targetList.size() <= 0) {
                    targetList = skillAction.pickUpTargets(targetsCount);
                }
                else {
                    skillAction.setSkillTargets(targetList);
                }
            }
            else {
                targetList = skillAction.pickUpTargets(targetsCount);
            }
            if (targetList.size() <= 0) {
                return;
            }
            FightObject mainTarget = fight.queryFightObject(attacker.getTargetId());
            if (mainTarget == null || mainTarget.isFallDown() || mainTarget.isASide() == attacker.isASide()) {
                mainTarget = fight.queryFightObject(targetList.get(0));
                attacker.setTargetId(mainTarget.getId());
            }
            final int life = processor.costLife(attacker);
            if (life > 0) {
                if (attacker.getCurrLife() < life) {
                    PetSkillEffectorUtil.sendMsg(fight, attacker, "气血不足！");
                    return;
                }
                attacker.reduceHp(life);
            }
            int mana = processor.costMana(skill, attacker, fight);
            mana = this.costMana(fight, attacker, skill, mana);
            if (attacker.getCurrMana() >= mana) {
                final boolean excuteSkillCool = processor.excuteSkillCool(fight, attacker);
                if (!excuteSkillCool) {
                    return;
                }
                PassiveSkillUtils.unitMagic(fight, attacker, processor, skill);
                if (attacker.isFallDown()) {
                    return;
                }
                mana += PassiveSkillUtils.reduceMp(fight, attacker, mana);
                attacker.reduceMp(mana);
                if (PassiveSkillUtils.enemyFristMagic(fight, attacker, processor, skill)) {
                    final ActionDetailParam actionDetailParam = ActionParamUtils.createActionDetailParam(attacker.getId(), attacker.getId(), 7, attacker.getSkillId());
                    final FightObjectParam attackerParam = actionDetailParam.gainAttackerParam();
                    final RespFightShouting fightShouting = SpringUtils.getBean(FightMessageNewService.class).fightShouting(attacker, "法攻");
                    ActionParamUtils.updateObjectInfo(attacker, 7, attacker.getSkillId(), 2, fightShouting, attackerParam);
                    fight.getActionParam().getActionDetailParams().add(actionDetailParam);
                    return;
                }
                if (attacker.isFallDown()) {
                    return;
                }
                skillAction.doMagicEffect();
                final ActionDetailParam actionDetailParam = skillAction.fetchActionDetail();
                PassiveSkillUtils.magicAttackEnd(fight, attacker, skillAction.getMagicTargets(), skill, actionDetailParam);
                PassiveSkillUtils.friendMagic(fight, attacker, actionDetailParam);
                if (skillAction.getActionDetailParams().size() > 0) {
                    fight.getActionParam().getActionDetailParams().addAll(skillAction.getActionDetailParams());
                }
                PassiveSkillUtils.excutePassiveSkill(fight, attacker, PET_PASSIVE_SKILL_TRIGGER_DICT.SELF_SELL_END.getValue());
                PassiveSkillUtils.magicAttackEndOther(fight, attacker, skill, skillAction.getMagicTargets());
                for (final MagicAttackParam magicParam : skillAction.getMagicTargets()) {
                    final FightObject target = magicParam.getTarget();
                    final List<Integer> statusList = magicParam.getPositiveStatus();
                    PassiveSkillUtils.exitOrClearPositive(fight, attacker, target, statusList);
                }
                this.checkOrderTarget(fight, targetList);
                if (attacker.isFallDown() && !fight.getDeadPetIds().contains(attacker.getId())) {
                    fight.onActorDie(attacker);
                }
            }
            else if (attacker.isPlayer() || attacker.isPet()) {
                PetSkillEffectorUtil.sendMsg(fight, attacker, "法力不足！");
            }
            else if (attacker.isMonster()) {
                this.setNormalAttack(fight, attacker);
            }
        }
        else {
            this.getSkillFail(fight, attacker, "获取技能信息失败，objId:" + attacker.getId() + ",skillId:" + attacker.getSkillId());
        }
    }
    
    public int getHostId(final int petId) {
        final Integer integer = FightExecutorNewService.PETID_ROLEID.get(petId);
        return (integer == null) ? 0 : integer;
    }
    
    public Fight getFightByRoleId(final int roleId) {
        final Integer fightId = FightExecutorNewService.FIGHT_ROOMS.get(roleId);
        if (fightId == null) {
            return null;
        }
        return this.getFight(fightId);
    }
    
    public Fight getFight(final int fightId) {
        return FightExecutorNewService.FIGHTS.get(fightId);
    }
    
    public void resetDoActionStatus(final Fight fight, final ReqFightHandle reqFightHandle) {
        final int hostId = this.getHostId(reqFightHandle.getAttackerId());
        final FightObject hostObj = fight.queryFightObject(hostId);
        if (hostObj != null) {
            hostObj.setDoAction(false);
        }
    }
    
    private void updateDoActionStatus(final Fight fight, final int attackerId, final int targetId, final int actionType, final int skillId) {
        final FightObject attacker = fight.queryFightObject(attackerId);
        if (attacker != null) {
            attacker.setSkillId(skillId);
            attacker.setTargetId(targetId);
            attacker.setType(actionType);
            attacker.setDoAction(true);
            if (attacker.isPlayer()) {
                final List<FightObject> partners = fight.getMySide(attacker.getId()).stream().filter(obj -> obj.isPartner()).collect(Collectors.toList());
                if (partners != null && partners.size() > 0) {
                    for (final FightObject fightObject : partners) {
                        if (attacker.getId().equals(fightObject.getHostId())) {
                            fightObject.setDoAction(true);
                            fightObject.setType(7);
                        }
                    }
                }
            }
        }
    }
    
    private boolean canUseLifeItem(final Fight fight) {
        return fight.getType() == FIGHT_TYPE.STAR.getValue() || fight.getType() == FIGHT_TYPE.SUPER_BOSS.getValue();
    }
    
    public void besure(final Fight fight) {
        final int fightId = fight.getId();
        final int currRound = fight.getRound();
        this.serverService.getScheduledExecutorService().schedule(new Runnable() {
            @Override
            public void run() {
                final Fight temp = FightNewService.this.getFight(fightId);
                if (temp != null && currRound == temp.getRound()) {
                    FightNewService.this.fightExecutorService.addFightTask(temp, new FightingTask() {
                        @Override
                        public void run() {
                            try {
                                FightNewService.this.quiteFightAll(fight, false);
                                FightNewService.this.clearFight(fight);
                            }
                            catch (final Exception e) {
                                LoggerFunction.FIGHT.getLogger().error("timer定时器结束战斗==报错", (Throwable)e);
                            }
                        }
                    });
                }
                else if (currRound != temp.getRound()) {}
            }
        }, 180L, TimeUnit.SECONDS);
    }
    
    public void quiteFightAll(final Fight fight, final boolean isReward) {
        for (final FightObject fightObject : fight.getAllFightObjects()) {
            this.quiteFight(fight, fightObject, isReward);
        }
    }
    
    private void excuteSubstitutionQuite(final Fight fight, final boolean isReward) {
        final Map<Boolean, List<Role>> rolesMap = FightUtils.substitutionQuiteFight(fight);
        if (rolesMap != null) {
            final List<Role> rolesA = rolesMap.get(true);
            final List<Role> rolesB = rolesMap.get(true);
            this.excuteSubstitutionQuite(fight, rolesA, isReward, rolesB, true);
            this.excuteSubstitutionQuite(fight, rolesB, isReward, rolesA, false);
        }
    }
    
    private void excuteSubstitutionQuite(final Fight fight, final List<Role> roles, final boolean isReward, final List<Role> rolesOther, final boolean isA) {
        if (roles != null && roles.size() > 0) {
            for (final Role role : roles) {
                if (isA) {
                    this.quiteFightSubstitution(fight, role, isReward, roles, rolesOther, isA);
                }
                else {
                    this.quiteFightSubstitution(fight, role, isReward, rolesOther, roles, isA);
                }
            }
        }
    }
    
    public void quiteFight(final Fight fight, final FightObject fightObject, final boolean isReward) {
        if (!fightObject.isPlayer()) {
            return;
        }
        final Role role = this.roleService.getPlayer(fightObject.getId());
        role.setPrevFightEndTime(System.currentTimeMillis());
        role.setCanFight(true);
        final Integer fightId = FightExecutorNewService.FIGHT_ROOMS.get(fightObject.getId());
        if (fightId != null && fight.getId() == fightId) {
            FightExecutorNewService.FIGHT_ROOMS.remove(fightObject.getFightPetId());
            this.deleteRoleFightData(fight, fightObject.getId());
        }
        ThreadLocalUtil.addLocalTask(role, () -> this.playerQuitFight(fight, fightObject, isReward, role));
    }
    
    public void quiteFightSubstitution(final Fight fight, final Role role, final boolean isReward, final List<Role> rolesA, final List<Role> rolesB, final boolean isA) {
        ThreadLocalUtil.addLocalTask(role, () -> this.playerQuitFightSubstitution(fight, role, isReward, rolesA, rolesB, isA));
    }
    
    public void clearFight(final Fight fight) {
        if (fight == null) {
            return;
        }
        fight.destroy();
    }
    
    public void deleteRoleFightData(final Fight fight, final Integer roleId) {
        final List<Integer> petIds = new ArrayList<Integer>();
        for (final Map.Entry<Integer, Integer> entry : FightExecutorNewService.PETID_ROLEID.entrySet()) {
            if (entry.getValue().equals(roleId)) {
                petIds.add(entry.getKey());
            }
        }
        for (final Integer petId : petIds) {
            FightExecutorNewService.PETID_ROLEID.remove(petId);
        }
        fight.clearAstrictSkills();
        final Integer fightId = FightExecutorNewService.FIGHT_ROOMS.get(roleId);
        if (fightId.equals(fight.getId())) {
            FightExecutorNewService.FIGHT_ROOMS.remove(roleId);
        }
        final Team team = this.teamService.getTeam(roleId);
        if (team != null) {
            final List<Member> members = team.getList().stream().filter(member -> member.isPartner() && member.getRoleId().equals(roleId)).collect(Collectors.toList());
            for (int i = 0; i < members.size(); ++i) {
                Member member = members.get(i);
                if (member.getRoleId().equals(roleId)) {
                    team.getList().remove(member);
                }
            }
        }
    }
    
    private void playerQuitFight(final Fight fight, final FightObject fightObject, final boolean isReward, final Role role) {
        try {
            final FightObject currPetObject = fight.queryFightObject(fightObject.getFightPetId());
            Pet currPet = null;
            if (currPetObject != null) {
                currPet = currPetObject.getPet();
            }
            else {
                currPet = this.petService.selectPet(role).get(role.getExtendBox().getCombatPet());
            }
            role.pushTempCache("fight_current_pet_id", (currPet != null) ? currPet.getId() : 0L);
            role.setLastDead((fightObject.getCurrLife() > 0) ? 1 : 0);
            if (isReward) {
                this.sendReward(fight, role, currPet);
            }
            this.fireEndEvent(fight, role, currPet);
            this.resetAfterLeaveFight(fight, fightObject);
        }
        catch (final Exception e) {
            e.printStackTrace();
            this.logger.error("退出战斗报错了,战场ID=[{}],角色名=[{}],错误信息[{}]", new Object[] { fight.getId(), fightObject.getName(), e.getMessage() });
        }
        finally {
            final Integer fightId = FightExecutorNewService.FIGHT_ROOMS.get(role.getRoleId());
            if (fightId == null) {
                final RespEndCombat respEndCombat = new RespEndCombat();
                MessagePusher.pushMessage(role, respEndCombat);
            }
            this.broadcastService.sendUpdateAppear(role);
            this.broadcastService.sendTitleInfoNew(role);
        }
    }
    
    private void playerQuitFightSubstitution(final Fight fight, final Role role, final boolean isReward, final List<Role> rolesA, final List<Role> rolesB, final boolean isA) {
        try {
            final Pet currPet = this.petService.selectPet(role).get(role.getExtendBox().getCombatPet());
            role.pushTempCache("fight_current_pet_id", (currPet != null) ? currPet.getId() : 0L);
            if (isReward) {
                this.sendReward(fight, role, currPet);
            }
            this.fireEndEventSubstitution(fight, role, currPet, rolesA, rolesB, isA);
        }
        catch (final Exception e) {
            e.printStackTrace();
            this.logger.error("退出战斗报错了,战场ID=[{}],角色id=[{}],错误信息[{}]", new Object[] { fight.getId(), role.getId(), e.getMessage() });
        }
        finally {
            final Integer fightId = FightExecutorNewService.FIGHT_ROOMS.get(role.getRoleId());
            if (fightId == null) {
                final RespEndCombat respEndCombat = new RespEndCombat();
                MessagePusher.pushMessage(role, respEndCombat);
            }
            this.broadcastService.sendUpdateAppear(role);
            this.broadcastService.sendTitleInfoNew(role);
        }
    }
    
    private void sendReward(final Fight fight, final Role role, final Pet pet) {
        try {
            final FightRewardHandler handler = FightRewardHandler.queryHandler(fight.getRewardType());
            if (fight.isWinner(role.getRoleId())) {
                final RespNotifyMiscEx respNotifyMiscEx = new RespNotifyMiscEx(role.getId(), "战斗胜利", false);
                MessagePusher.pushMessage(role, respNotifyMiscEx);
                if (handler != null) {
                    handler.sendReward(fight, role, pet);
                }
            }
            else {
                final RespNotifyMiscEx respNotifyMiscEx = new RespNotifyMiscEx(role.getId(), "战斗失败", false);
                MessagePusher.pushMessage(role, respNotifyMiscEx);
                if (handler != null) {
                    handler.fightFailed(fight, role);
                }
            }
        }
        catch (final Exception e) {
            LoggerUtils.error(e.getMessage(), e);
            this.logger.error("退出战斗战斗结算时报错了,战场ID=[{}],角色名=[{}],错误信息[{}]", new Object[] { fight.getId(), role.getName(), Lang.getStackTrace((Throwable)e) });
        }
    }
    
    private void fireEndEvent(final Fight fight, final Role role, final Pet pet) {
        try {
            if (fight.getData() != null && fight.getData().getBoolean("fight_result_fail", false)) {
                EventDispatcher.getInstance().fireEvent(new FightEndEvent(fight, EventType.FIGHT_END, role, fight.getId(), (byte)2, fight.getType(), fight.getBossReward(), fight.getTaskId(), fight.getNpcId(), 0));
                return;
            }
            if (fight.getData() != null && fight.getData().getBoolean("fight_result_success", false)) {
                EventDispatcher.getInstance().fireEvent(new FightEndEvent(fight, EventType.FIGHT_END, role, fight.getId(), (byte)1, fight.getType(), fight.getBossReward(), fight.getTaskId(), fight.getNpcId(), (pet != null) ? pet.getPetId() : 0));
                return;
            }
            if (fight.getRound() >= fight.getMaxRound()) {
                EventDispatcher.getInstance().fireEvent(new FightEndEvent(fight, EventType.FIGHT_END, role, fight.getId(), (byte)40, fight.getType(), fight.getBossReward(), fight.getTaskId(), fight.getNpcId(), 0));
            }
            else if (fight.isWinner(role.getRoleId())) {
                EventDispatcher.getInstance().fireEvent(new FightEndEvent(fight, EventType.FIGHT_END, role, fight.getId(), (byte)1, fight.getType(), fight.getBossReward(), fight.getTaskId(), fight.getNpcId(), (pet != null) ? pet.getPetId() : 0));
            }
            else {
                EventDispatcher.getInstance().fireEvent(new FightEndEvent(fight, EventType.FIGHT_END, role, fight.getId(), (byte)2, fight.getType(), fight.getBossReward(), fight.getTaskId(), fight.getNpcId(), 0));
            }
        }
        catch (final Exception e) {
            LoggerUtils.error(e.getMessage(), e);
            this.logger.error("退出战斗产生结束事件时报错了,战场ID=[{}],角色名=[{}],错误信息[{}]", new Object[] { fight.getId(), role.getName(), e });
        }
    }
    
    private void fireEndEventSubstitution(final Fight fight, final Role role, final Pet pet, final List<Role> rolesA, final List<Role> rolesB, final boolean isA) {
        try {
            if (fight.getData() != null && fight.getData().getBoolean("fight_result_fail", false)) {
                EventDispatcher.getInstance().fireEvent(new FightEndEvent(fight, EventType.FIGHT_END, role, (byte)2, 0, rolesA, rolesB));
                return;
            }
            if (fight.getData() != null && fight.getData().getBoolean("fight_result_success", false)) {
                EventDispatcher.getInstance().fireEvent(new FightEndEvent(fight, EventType.FIGHT_END, role, (byte)1, (pet != null) ? pet.getPetId() : 0, rolesA, rolesB));
                return;
            }
            if (fight.getRound() >= fight.getMaxRound()) {
                EventDispatcher.getInstance().fireEvent(new FightEndEvent(fight, EventType.FIGHT_END, role, (byte)40, 0, rolesA, rolesB));
            }
            else if (fight.isWinner(role.getRoleId(), isA)) {
                EventDispatcher.getInstance().fireEvent(new FightEndEvent(fight, EventType.FIGHT_END, role, (byte)1, (pet != null) ? pet.getPetId() : 0, rolesA, rolesB));
            }
            else {
                EventDispatcher.getInstance().fireEvent(new FightEndEvent(fight, EventType.FIGHT_END, role, (byte)2, 0, rolesA, rolesB));
            }
        }
        catch (final Exception e) {
            LoggerUtils.error(e.getMessage(), e);
            this.logger.error("退出战斗产生结束事件时报错了,战场ID=[{}],角色名=[{}],错误信息[{}]", new Object[] { fight.getId(), role.getName(), e });
        }
    }
    
    private void resetAfterLeaveFight(final Fight fight, final FightObject fightObject) {
        final Role role = this.roleService.getPlayer(fightObject.getId());
        int lastLife = fightObject.getCurrLife();
        int lastMana = fightObject.getCurrMana();
        int maxLife = role.getMaxLife();
        int maxMana = role.getMaxMana();
        int needLife = maxLife - lastLife;
        long saveLife = role.getSaveLife();
        int needMana = maxMana - lastMana;
        long saveMna = role.getSaveMana();
        if (saveLife >= 1L) {
            if (saveLife >= needLife) {
                lastLife = maxLife;
                saveLife -= needLife;
            }
            else {
                lastLife += (int)saveLife;
                saveLife = 0L;
            }
        }
        else {
            if (lastLife <= 0) {
                lastLife = 1;
            }
            saveLife = 0L;
        }
        if (saveMna >= 1L) {
            if (saveMna >= needMana) {
                lastMana = maxMana;
                saveMna -= needMana;
            }
            else {
                lastMana += (int)saveMna;
                saveMna = 0L;
            }
        }
        else {
            if (lastMana <= 0) {
                lastMana = 1;
            }
            saveMna = 0L;
        }
        role.setCurrLife(lastLife);
        role.setCurrMana(lastMana);
        Map<String, Object> autoSet = role.getExtendBox().getAutoFightSet();
        autoSet.put("type", fightObject.getAutoFightType());
        autoSet.put("skillId", fightObject.getAutoSkillId());
        autoSet.put("action", fightObject.getAction());
        autoSet.put("target", fightObject.getAutoTarget());
        int born = 0;
        if (role.getRace() == RACE.RACE_LONG.getValue()) {
            born = fightObject.getBorn();
        }
        autoSet.put("born", born);
        for (final Integer petId : fightObject.getYepFightPet()) {
            final Pet pet = this.petService.selectPet(role).get(petId);
            if (pet != null) {
                lastLife = pet.getCurrLife();
                lastMana = pet.getCurrMana();
                maxLife = pet.getMaxLife();
                maxMana = pet.getMaxMana();
                needLife = maxLife - lastLife;
                needMana = maxMana - lastMana;
                if (saveLife >= 1L) {
                    if (saveLife >= needLife) {
                        lastLife = maxLife;
                        saveLife -= needLife;
                    }
                    else {
                        lastLife += (int)saveLife;
                        saveLife = 0L;
                    }
                }
                else {
                    if (lastLife <= 0) {
                        lastLife = 1;
                    }
                    saveLife = 0L;
                }
                if (saveMna >= 1L) {
                    if (saveMna >= needMana) {
                        lastMana = maxMana;
                        saveMna -= needMana;
                    }
                    else {
                        lastMana += (int)saveMna;
                        saveMna = 0L;
                    }
                }
                else {
                    if (lastMana <= 0) {
                        lastMana = 1;
                    }
                    saveMna = 0L;
                }
                pet.setCurrMana(lastMana);
                pet.setCurrLife(lastLife);
                final FightObject petObject = fight.queryFightObject(petId);
                if (petObject != null) {
                    autoSet = (Map<String, Object>)NutMap.NEW();
                    autoSet.put("type", petObject.getAutoFightType());
                    autoSet.put("skillId", petObject.getAutoSkillId());
                    autoSet.put("action", petObject.getAction());
                    autoSet.put("target", petObject.getAutoTarget());
                    pet.setAutoFightSet(autoSet);
                }
                final boolean once = fight.petDeadOnce(petId);
                this.petService.refreshPetClose(role, pet, once, fight.getType());
            }
        }
        role.setSaveMana(saveMna);
        role.setSaveLife(saveLife);
        this.roleService.checkReserve(role);
        this.roleService.refreshRoleInfo(role, true);
        role.save();
    }
    
    public void fullLifeRoleAndPet(final Fight fight, final Role role) {
        fight.getRoleFightInParam(role);
        this.fullHpAndMpOfRole(role);
        final Integer combatPet = role.getExtendBox().getCombatPet();
        if (combatPet >= 1) {
            final Pet pet = this.petService.selectPet(role).get(combatPet);
            if (pet != null) {
                fight.addPetFightInParam(role, pet);
                this.fullHpAndMpOfPet(role, pet);
            }
        }
        role.save();
    }
    
    private LifeManaParam fullHpAndMpOfRole(final Role role) {
        final LifeManaParam lifeManaParam = new LifeManaParam();
        final int maxLife = role.getMaxLife();
        final int maxMana = role.getMaxMana();
        if (role.getSaveLife() >= maxLife - role.getCurrLife()) {
            role.setCurrLife(maxLife);
            role.setSaveLife(role.getSaveLife() - (maxLife - role.getCurrLife()));
        }
        else {
            role.setCurrLife(role.getCurrLife() + role.getSaveLife().intValue());
            role.setSaveLife(0L);
        }
        lifeManaParam.setCurrLife(role.getCurrLife());
        lifeManaParam.setSaveLife(String.valueOf(role.getSaveLife()));
        if (role.getSaveMana() >= maxMana - role.getCurrMana()) {
            role.setSaveMana(role.getSaveMana() - (maxMana - role.getCurrMana()));
            role.setCurrMana(maxMana);
        }
        else {
            role.setCurrMana(role.getCurrMana() + role.getSaveMana().intValue());
            role.setSaveMana(0L);
        }
        lifeManaParam.setCurrMana(role.getCurrMana());
        lifeManaParam.setSaveMana(String.valueOf(role.getSaveMana()));
        return lifeManaParam;
    }
    
    public LifeManaParam fullHpAndMpOfPet(final Role role, final Pet pet) {
        if (pet == null) {
            return null;
        }
        final LifeManaParam lifeManaParam = new LifeManaParam();
        if (role.getSaveLife() >= pet.getMaxLife() - pet.getCurrLife()) {
            role.setSaveLife(role.getSaveLife() - (pet.getMaxLife() - pet.getCurrLife()));
            pet.setCurrLife(pet.getMaxLife());
        }
        else {
            pet.setCurrLife(role.getCurrLife() + role.getSaveLife().intValue());
            role.setSaveLife(0L);
        }
        lifeManaParam.setCurrLife(pet.getCurrLife());
        lifeManaParam.setSaveLife(String.valueOf(role.getSaveLife()));
        if (role.getSaveMana() >= pet.getMaxMana() - pet.getCurrMana()) {
            role.setSaveMana(role.getSaveMana() - (pet.getMaxMana() - pet.getCurrMana()));
            pet.setCurrMana(pet.getMaxMana());
        }
        else {
            pet.setCurrMana(pet.getCurrMana() + role.getSaveMana().intValue());
            role.setSaveMana(0L);
        }
        lifeManaParam.setCurrMana(pet.getCurrMana());
        lifeManaParam.setSaveMana(String.valueOf(role.getSaveMana()));
        return lifeManaParam;
    }
    
    public void checkAtFightStart(final Role role) {
        if (role.getCurrLife() <= 0) {
            role.setCurrLife(1);
        }
    }
    
    public void sendStartCombat(final Fight fight, final RespCombatFriends respCombatFriends, final RespCombatOpponets respCombatOpponets, final List<Member> members) {
        if (fight != null) {
            for (int i = 0; i < members.size(); ++i) {
                final Member member = members.get(i);
                if (member.isInTeam()) {
                    if (!member.isPartner()) {
                        final Role tempRole = this.roleService.getPlayer(member.getId());
                        if (tempRole == null) {
                            this.logger.error("战斗开始未找到role=={}", member.getId());
                        }
                        else {
                            final RespFightInParam fightInParam = fight.getRoleFightInParam(tempRole);
                            fightInParam.setFightType(fight.getType());
                            if (fight.isPvp() == 1 && tempRole.getExtendBox().getAutoFight() == 1) {
                                tempRole.getExtendBox().setAutoFight((byte)0);
                            }
                            fightInParam.setRespCombatFriends(respCombatFriends);
                            fightInParam.setRespCombatOpponets(respCombatOpponets);
                            this.broadcastService.sendTitleInfoNew(tempRole);
                        }
                    }
                }
            }
        }
    }
    
    public byte getTitleInfo(final Role role) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        final Fight fight = this.getFightByRoleId(role.getRoleId());
        String temp = "";
        if (team != null) {
            if (team.isLeader(role.getRoleId())) {
                if (team.getList().size() == 5) {
                    temp += "10";
                }
                else {
                    temp += "01";
                }
            }
            else {
                for (int i = 0; i < team.getList().size(); ++i) {
                    final Member member = team.getList().get(i);
                    if (member.getRoleId().equals(role.getRoleId()) && member.isInTeam()) {
                        temp += "00";
                        break;
                    }
                }
            }
        }
        else {
            temp += "00";
        }
        if (fight != null && !fight.isOver()) {
            temp += "1";
        }
        else {
            temp += "0";
        }
        if (role.getPkInfoBox().isRedName() && !role.getPos().getMapId().equals(1201)) {
            temp += "1";
        }
        else {
            temp += "0";
        }
        final byte test = Byte.parseByte(temp, 2);
        return test;
    }
    
    public SkillAutoPicker getAutoObject(final FightObject attacker, final Fight fight) {
        final SkillAutoPicker useResult = new SkillAutoPicker();
        int autoSkillId = 0;
        try {
            autoSkillId = attacker.getAutoSkillId();
            final int autoType = attacker.getAutoFightType();
            useResult.setType(autoType);
            if (autoType == 5) {
                final SysDrug drug = ItemDataPool.data_drug.get(autoSkillId);
                if (drug == null) {
                    useResult.setDef(true).setSkillId(autoSkillId).setTargetId(attacker.getId());
                    return useResult;
                }
                useResult.setSkillId(autoSkillId);
                if (attacker.getAutoTarget() > 0) {
                    final FightObject target = fight.queryFightObject(attacker.getAutoTarget());
                    if (target != null) {
                        useResult.setTargetId(target.getId());
                        return useResult;
                    }
                }
                FightObject fightObject = null;
                if (drug.getTargetType() == TARGETTYPE.ALL_OURS.getValue()) {
                    if (drug.getShowClass() == 4) {
                        fightObject = SkillAi.certainStatusOrPro(fight, drug.getStatus(), attacker.isASide(), 4);
                    }
                    else {
                        fightObject = SkillAi.effectProBass(fight, attacker, drug.getShowClass(), Arrays.asList(new String[0]), false);
                    }
                    if (fightObject != null) {
                        useResult.setTargetId(fightObject.getId());
                    }
                    return useResult;
                }
                if (drug.getTargetType() == TARGETTYPE.ALL_ENEMY.getValue()) {
                    final FightObject unit = SkillAi.certainStatusOrPro(fight, drug.getStatus(), !attacker.isASide(), 4);
                    if (unit != null) {
                        useResult.setSkillId(autoSkillId).setTargetId(unit.getId());
                    }
                    return useResult;
                }
            }
            else {
                if (autoType == 1) {
                    useResult.setDef(true).setSkillId(autoSkillId).setTargetId(attacker.getId());
                    return useResult;
                }
                if (autoType == 0) {
                    final FightObject randTarget = SkillTargetPicker.getPhysicTarget(attacker, fight);
                    if (randTarget != null) {
                        return useResult.setSkillId(autoSkillId).setTargetId(randTarget.getId());
                    }
                    return useResult;
                }
                else {
                    final RoleSkill roleSkill = attacker.getSkillBy(autoSkillId);
                    final int autoTargetId = attacker.getAutoTarget();
                    final FightObject autoTarget = fight.queryFightObject(autoTargetId);
                    if (roleSkill == null) {
                        if (autoTarget != null && !autoTarget.isFallDown() && autoTarget.isASide() != attacker.isASide()) {
                            return useResult.setSkillId(autoSkillId).setTargetId(autoTarget.getId());
                        }
                        final FightObject randTarget2 = SkillTargetPicker.getPhysicTarget(attacker, fight);
                        if (randTarget2 != null) {
                            return useResult.setSkillId(autoSkillId).setTargetId(randTarget2.getId());
                        }
                        return useResult;
                    }
                    else {
                        final int skillTargetSum = SkillFunc.getSkillTargeNum(roleSkill.getSkillId(), roleSkill.getProficient(), attacker.getSerial());
                        if (autoTarget == null || autoTarget.isFallDown()) {
                            if (SkillFunc.isAssistSkill(roleSkill.getSkillId(), attacker.getLogic())) {
                                getUseResult(fight, attacker, roleSkill, useResult, autoSkillId, skillTargetSum);
                                return useResult;
                            }
                            getUseResult(fight, attacker, roleSkill, useResult, autoSkillId, skillTargetSum);
                            return useResult;
                        }
                        else if (SkillFunc.isAssistSkill(roleSkill.getSkillId(), attacker.getLogic())) {
                            if (autoTarget.isASide() == attacker.isASide()) {
                                return useResult.setSkillId(autoSkillId).setTargetId(autoTarget.getId());
                            }
                            getUseResult(fight, attacker, roleSkill, useResult, autoSkillId, skillTargetSum);
                            return useResult;
                        }
                        else {
                            if (autoTarget.isASide() != attacker.isASide()) {
                                return useResult.setSkillId(autoSkillId).setTargetId(autoTarget.getId());
                            }
                            getUseResult(fight, attacker, roleSkill, useResult, autoSkillId, skillTargetSum);
                            return useResult;
                        }
                    }
                }
            }
        }
        catch (final Exception e) {
            LoggerUtils.error(e.getMessage(), e);
        }
        return useResult;
    }
    
    public static void getUseResult(final Fight fight, final FightObject attacker, final RoleSkill roleSkill, final SkillAutoPicker useResult, final int skillId, final int skillTargetSum) {
        attacker.setTargetId(0);
        attacker.setSkillId(roleSkill.getSkillId());
        final List<Integer> targetListNew = SkillTargetPicker.getTargetListNew(attacker, skillTargetSum, fight);
        if (targetListNew != null && targetListNew.size() > 0) {
            final int tempRoleId = targetListNew.get(0);
            useResult.setSkillId(skillId).setTargetId(tempRoleId);
            return;
        }
        attacker.setTargetId(0);
        attacker.setSkillId(roleSkill.getSkillId());
        final int tempRoleId = 0;
        useResult.setSkillId(skillId).setTargetId(tempRoleId);
    }
    
    public boolean isSameTeam(final FightObject attacker, final FightObject target) {
        return attacker.isASide() == target.isASide();
    }
    
    public void autoFightOne(final Fight fight, final FightObject attacker) {
        this.autoFightOne(fight, attacker, 2500L);
    }
    
    public void autoFightOne(final Fight fight, final FightObject attacker, final long delay) {
        this.fightExecutorService.addFightTask(fight, () -> {
            try {
                if (!fight.isOver()) {
                    if (attacker.getAutoFight() == 1) {
                        final ReqFightHandle reqFightHandle = this.fightMessageNewService.autoFightOne(fight, attacker);
                        if (reqFightHandle == null) {
                            this.logger.error("fight[{}], type[{}],{}出手动作为空", fight.getId(), fight.getType(), attacker);
                        }
                        else {
                            final AutoChuShouNewTask autoTask = new AutoChuShouNewTask(reqFightHandle, fight);
                            final ScheduledFuture<?> timeOut = SchedulerManager.getInstance().schedule(autoTask, delay);
                            fight.addHostTask(reqFightHandle.getAttackerId(), timeOut);
                            if (attacker.getFightPetId() > 0) {
                                final FightObject petObject = fight.queryFightObject(attacker.getFightPetId());
                                if (petObject != null) {
                                    final ReqFightHandle reqFightHandle2 = this.fightMessageNewService.autoFightOne(fight, petObject);
                                    if (reqFightHandle2 == null) {
                                        this.logger.error("fight[{}], type[{}],{}出手动作为空", fight.getId(), fight.getType(), attacker);
                                    }
                                    else {
                                        final AutoChuShouNewTask petAutoTask = new AutoChuShouNewTask(reqFightHandle2, fight);
                                        final ScheduledFuture<?> petTimeOut = SchedulerManager.getInstance().schedule(petAutoTask, delay + 20L);
                                        fight.addPetTask(reqFightHandle2.getAttackerId(), petTimeOut);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            catch (final Exception e) {
                this.logger.error("fight[{}], type[{}],自动战斗出错{}", fight.getId(), fight.getType(), e);
                final ReqFightHandle reqFightHandle3 = new ReqFightHandle();
                reqFightHandle3.setRound(fight.getRound());
                reqFightHandle3.setAutoFight(1);
                reqFightHandle3.setAttackerId(attacker.getId());
                reqFightHandle3.setTargetId(attacker.getId());
                reqFightHandle3.setSkillId(0);
                reqFightHandle3.setType(1);
                final AutoChuShouNewTask autoTask2 = new AutoChuShouNewTask(reqFightHandle3, fight);
                final ScheduledFuture<?> timeOut2 = SchedulerManager.getInstance().schedule(autoTask2, delay);
                fight.addHostTask(reqFightHandle3.getAttackerId(), timeOut2);
                if (attacker.getFightPetId() > 0) {
                    final FightObject petObject2 = fight.queryFightObject(attacker.getFightPetId());
                    if (petObject2 != null) {
                        final ReqFightHandle reqFightHandle4 = new ReqFightHandle();
                        reqFightHandle4.setAutoFight(1);
                        reqFightHandle4.setRound(fight.getRound());
                        reqFightHandle4.setAttackerId(petObject2.getId());
                        reqFightHandle4.setTargetId(petObject2.getId());
                        reqFightHandle4.setSkillId(0);
                        reqFightHandle4.setType(1);
                        final AutoChuShouNewTask petAutoTask2 = new AutoChuShouNewTask(reqFightHandle4, fight);
                        final ScheduledFuture<?> petTimeOut2 = SchedulerManager.getInstance().schedule(petAutoTask2, delay + 20L);
                        fight.addPetTask(reqFightHandle4.getAttackerId(), petTimeOut2);
                    }
                }
            }
        });
    }
    
    public List<Member> loadTeamBattleInfo(final Team team, final List<FightMember> fightMemberA, final Fight battleground) {
        final List<FightObject> memberListA = battleground.getListA();
        battleground.setListA(memberListA);
        FightExecutorNewService.FIGHTS.put(battleground.getId(), battleground);
        final List<Member> list = team.getList();
        int index = 1;
        int petIndex = 5;
        int customIndex = 0;
        int playerNum = 0;
        for (int i = list.size() - 1; i >= 0; --i) {
            final Member member = list.get(i);
            if (member.isInTeam()) {
                if (!member.isPartner()) {
                    final Role tmpRole = this.roleService.getPlayer(member.getId());
                    FightExecutorNewService.FIGHT_ROOMS.put(member.getRoleId(), battleground.getId());
                    this.fullLifeRoleAndPet(battleground, tmpRole);
                    this.checkAtFightStart(tmpRole);
                    FightObject fightObject = null;
                    if (i == 0) {
                        fightObject = new PlayerFightObject(tmpRole, 0, customIndex, true);
                    }
                    else {
                        fightObject = new PlayerFightObject(tmpRole, index, customIndex, true);
                    }
                    memberListA.add(fightObject);
                    final FightMember fightMember = fightObject.asFightMember();
                    fightMemberA.add(fightMember);
                    if (i != 0) {
                        ++index;
                    }
                    ++playerNum;
                    ++customIndex;
                    final Pet pet = this.petService.selectPet(tmpRole).get(tmpRole.getExtendBox().getCombatPet());
                    if (pet != null) {
                        ++customIndex;
                        final PetFightObject petFightObj = new PetFightObject(pet, fightObject);
                        memberListA.add(petFightObj);
                        final FightMember petFightMember = petFightObj.asFightMember();
                        fightMemberA.add(petFightMember);
                        FightExecutorNewService.PETID_ROLEID.put(pet.getPetId(), member.getRoleId());
                        FightExecutorNewService.FIGHT_ROOMS.put(pet.getPetId(), battleground.getId());
                    }
                    ++petIndex;
                }
            }
        }
        if (!Const.not_partner.contains(battleground.getType()) && playerNum < 5 && team.getPartner() != null && team.getPartner().size() > 0) {
            int residueNum = 5 - playerNum;
            for (final Member member2 : team.getPartner()) {
                if (residueNum <= 0) {
                    break;
                }
                if (!member2.isInTeam()) {
                    continue;
                }
                final Role teamRole = this.roleService.getPlayer(member2.getRoleId());
                if (teamRole == null) {
                    continue;
                }
                final Map<Integer, Partner> partnerMap = DataCache.PARTNER_MAP.get(teamRole.getId());
                if (partnerMap == null) {
                    continue;
                }
                if (partnerMap.size() <= 0) {
                    continue;
                }
                final Partner partner = partnerMap.get(member2.getObjectId());
                if (partner == null) {
                    continue;
                }
                final FightObject hostFightObject = battleground.queryFightObject(member2.getRoleId());
                if (hostFightObject == null) {
                    continue;
                }
                final FightObject partnerFightObject = new PartnerFightObject(partner, hostFightObject, index);
                memberListA.add(partnerFightObject);
                final FightMember partnerFightMember = partnerFightObject.asFightMember();
                fightMemberA.add(partnerFightMember);
                ++index;
                --residueNum;
            }
        }
        return list;
    }
    
    public Fight enterTeamPK(final List<Member> listA, final List<Member> listB, final Role role) {
        if (!this.canFight(role, listA)) {
            this.logger.error("无法战斗A：{}", role.getName());
            return null;
        }
        if (!this.canFight(role, listB)) {
            this.logger.error("无法战斗B：{}", role.getName());
            return null;
        }
        if (this.teamService.isInTeamAndNotLeader(role)) {
            this.logger.info("{}({})不是队长触发战斗", role.getRoleId(), role.getName());
            return null;
        }
        synchronized (role.getCanFight()) {
            if (!role.getCanFight()) {
                return null;
            }
            role.setCanFight(false);
        }
        final int mapId = role.getPos().getMapId();
        final Fight battleground = FightFactory.getFightFactory(FIGHT_TYPE.FORCE_PK.getValue()).create(mapId, FIGHT_TYPE.FORCE_PK.getValue(), listA, listB);
        battleground.setRewardType(FIGHT_TYPE.FORCE_PK.getValue());
        battleground.start();
        return battleground;
    }
    
    public void enterFieldFight(final Team team, final Role role) {
        if (!this.canFight(role, team.getList())) {
            return;
        }
        if (this.teamService.isInTeamAndNotLeader(role)) {
            return;
        }
        synchronized (role.getCanFight()) {
            if (!role.getCanFight()) {
                return;
            }
            role.setCanFight(false);
        }
        final Pos position = role.getPos();
        final int mapId = position.getMapId();
        final MonsterService monsterService = SpringUtils.getBean(MonsterService.class);
        final Object[] objs = monsterService.getMapMonster(role, mapId);
        final Integer warRoleSerial = (Integer)objs[0];
        final List<SysDataMonster> monsters = (List<SysDataMonster>)objs[1];
        if (CollectionUtils.isEmpty(monsters)) {
            return;
        }
        final Fight battleground = FightFactory.getFightFactory(FIGHT_TYPE.MONSTER.getValue()).create(mapId, team, role);
        battleground.setRewardType(warRoleSerial);
        battleground.start();
    }
    
    public boolean canFight(final Role role, final List<Member> list) {
        return !this.isInFight(role, list);
    }
    
    public boolean isInFight(final Role role) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        List<Member> memberList = Collections.emptyList();
        if (team != null) {
            memberList = team.getList();
        }
        return this.isInFight(role, memberList);
    }
    
    private boolean isInFight(final Role role, final List<Member> list) {
        if (role != null) {
            final Fight fight = this.getFightByRoleId(role.getRoleId());
            if (fight != null) {
                return false;
            }
            if (list != null) {
                final Member member = list.stream().filter(e -> e.getId().equals(role.getId())).findFirst().orElse(null);
                if (member != null && !member.isInTeam() && fight == null) {
                    return false;
                }
            }
            if (fight != null && !fight.isOver()) {
                return true;
            }
        }
        if (list != null) {
            for (int i = 0; i < list.size(); ++i) {
                final Member member2 = list.get(i);
                final Fight fight2 = this.getFightByRoleId(member2.getRoleId());
                if (member2.isInTeam() && fight2 != null && !fight2.isOver()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void checkOrderTarget(final Fight fight, final List<Integer> targetList) {
        if (fight.getData() != null && fight.getData().getBoolean("fight_order_target", false)) {
            String targetName = null;
            final List<String> names = fight.getData().getAsList("fight_order_target_name", String.class);
            final List<String> deadNames = fight.getData().getAsList("fight_order_target_dead_name", String.class);
            if (names.size() > deadNames.size()) {
                targetName = names.get(deadNames.size());
            }
            for (final int targetId : targetList) {
                final FightObject fightTarget = fight.queryFightObject(targetId);
                if (fightTarget.getName().equals(targetName)) {
                    fightTarget.setCurrLife(0);
                    deadNames.add(targetName);
                    if (names.size() == deadNames.size()) {
                        fight.getData().setv("fight_result_success", true);
                        break;
                    }
                    continue;
                }
            }
        }
    }
    
    public void checkGuessSuccess(final Fight fight) {
        if (fight.getData() != null && fight.getData().getBoolean("fight_guess_true", false)) {
            fight.getData().setv("fight_result_success", true);
        }
    }
    
    public CombatAddObject addPet2Fight(final int hostId, final Pet pet, final Fight fight, final int pos) {
        final FightObject hostObject = fight.queryFightObject(hostId);
        final LifeManaParam lifeManaParam = this.fullHpAndMpOfPet(hostObject.getRole(), pet);
        final PetFightObject fightObject = new PetFightObject(pet, hostObject);
        fightObject.setComeRound(fight.getRound());
        hostObject.setFightPetId(pet.getPetId());
        fight.addFightObject(fightObject);
        final CombatAddObject combatAddObject = new CombatAddObject();
        combatAddObject.setId(pet.getPetId());
        combatAddObject.setPosition(pos);
        combatAddObject.setHostId(hostId);
        combatAddObject.setSerial(pet.getPetType());
        combatAddObject.setCustomIcon(fightObject.getIcon());
        combatAddObject.setList(fightObject.asFieldValues());
        combatAddObject.addSkillIds(pet);
        return combatAddObject;
    }
    
    public CombatAddObject addObject2Fight(final int hostId, final FightObject fightObject, final Fight fight) {
        fightObject.setComeRound(fight.getRound());
        final CombatAddObject combatAddObject = new CombatAddObject();
        combatAddObject.setId(fightObject.getId());
        combatAddObject.setPosition(fightObject.getPos());
        combatAddObject.setHostId(hostId);
        combatAddObject.setSerial(fightObject.getSerial());
        combatAddObject.setCustomIcon(fightObject.getIcon());
        combatAddObject.setList(fightObject.asFieldValues());
        combatAddObject.setSkillIds(this.getFightObjectIds(fightObject));
        return combatAddObject;
    }
    
    private List<Integer> getFightObjectIds(final FightObject fightObject) {
        final List<Integer> list = new ArrayList<Integer>();
        final List<RoleSkill> skillIds = fightObject.getSkillIds();
        if (skillIds != null && skillIds.size() > 0) {
            for (final RoleSkill roleSkill : skillIds) {
                if (roleSkill != null && roleSkill.getSkillId() > 0) {
                    list.add(roleSkill.getSkillId());
                }
            }
            return list;
        }
        return null;
    }
    
    private boolean isAstrictSkill(final Fight fight, final FightObject fightObject) {
        boolean isContinue = true;
        final AstrictSkill astrictSkill = fight.getAstrictSkillMap().get(fightObject.isASide());
        if (astrictSkill != null) {
            Integer count = astrictSkill.getSkillMap().get(fightObject.getSkillId());
            if (count == null) {
                count = 0;
            }
            if (count < astrictSkill.getTime()) {
                ++count;
                astrictSkill.getSkillMap().put(fightObject.getSkillId(), count);
                isContinue = false;
            }
        }
        else {
            isContinue = false;
        }
        return isContinue;
    }
    
    private void getMonsterActionType(final Fight fight, final FightObject attacker) {
        final FightMonsterNewService fightMonsterNewService = SpringUtils.getBean(FightMonsterNewService.class);
        if (fightMonsterNewService.checkSpecialExcute(fight, attacker)) {
            fightMonsterNewService.specialExcute(fight, attacker);
            return;
        }
        if (fightMonsterNewService.runAway(fight, attacker)) {
            attacker.setType(4);
            attacker.setSkillId(0);
        }
        else if (fightMonsterNewService.isSelectMonster(fight, attacker)) {
            attacker.setType(3);
            attacker.setSkillId(0);
        }
        else if (fightMonsterNewService.isAllergyMonster(fight, attacker)) {
            attacker.setType(0);
        }
        else if (fightMonsterNewService.isUseItem(fight, attacker)) {
            attacker.setType(5);
        }
        else if (fightMonsterNewService.isUseSkills(fight, attacker)) {
            attacker.setType(7);
        }
        else {
            attacker.setType(0);
            attacker.setSkillId(0);
        }
        this.excuteMonsterPetSkill(attacker);
    }
    
    private void excuteMonsterPetSkill(final FightObject effector) {
        if (effector.getType() != 7) {
            return;
        }
        if (effector.getSkillId() == 83031) {
            final int[] flzt = { 830311, 830312, 830313, 830314 };
            final int round = RandomUtil.round(0, flzt.length - 1);
            effector.setSkillId(flzt[round]);
        }
        if (effector.getSkillId() == 83034) {
            final int[] yfly = { 830341, 830342, 830343, 830344, 830345 };
            final int round = RandomUtil.round(0, yfly.length - 1);
            effector.setSkillId(yfly[round]);
        }
    }
    
    public FightObject getPhysicTarget(final FightObject fightObject, final Fight fight) {
        final FightObject target = fight.queryFightObject(fightObject.getTargetId());
        if (target != null && !target.isFallDown() && !fightObject.getId().equals(target.getId())) {
            return target;
        }
        final List<FightObject> selfList = SkillTargetPicker.getFightObjects(fight, fightObject, true);
        if (selfList.size() <= 0) {
            return null;
        }
        final Random random = new Random();
        final int index = random.nextInt(selfList.size());
        return selfList.get(index);
    }
    
    public List<Integer> getTargetListNew(final FightObject fightObject, final int maxNum, final Fight fight) {
        final RoleSkill skill = fightObject.getSkillBy(fightObject.getSkillId());
        if (skill == null) {
            this.logger.error("{}技能不存在", fightObject.getSkillId());
            return Collections.emptyList();
        }
        if (SkillFunc.isAssistSkill(skill.getSkillId(), fightObject.getLogic())) {
            return SkillTargetPicker.getAssistTarget(fight, fightObject, maxNum);
        }
        return SkillTargetPicker.getRandomAttackTarget(fight, fightObject, maxNum);
    }
    
    private void getSacrificeJusticeExcute(final Fight fight, final FightObject attacker) {
        attacker.setType(0);
        attacker.setSkillId(0);
        FightObject target = fight.queryFightObject(attacker.getTargetId());
        if (target != null && attacker.isASide() == target.isASide()) {
            final List<FightObject> selfList = SkillTargetPicker.getFightObjects(fight, attacker, true);
            if (selfList.size() <= 0) {
                return;
            }
            final Random random = new Random();
            final int index = random.nextInt(selfList.size());
            target = selfList.get(index);
            attacker.setTargetId(target.getId());
        }
    }
    
    public boolean checkDeStatus(final Fight fight, final FightObject fightObject) {
        if (fightObject.isConfusion()) {
            fightObject.setType(0);
            fightObject.setSkillId(0);
            fightObject.setTargetId(0);
            this.normalAttack(fightObject, fight);
            return true;
        }
        boolean noAction = false;
        if (fightObject.isSleep() && fightObject.getType() != 5 && fightObject.getType() != 3 && fightObject.getType() != 12) {
            noAction = true;
        }
        if (fightObject.isForgotten() && fightObject.getType() != 0 && fightObject.getType() != 3 && fightObject.getType() != 12) {
            if (!fightObject.isPet() || fightObject.getType() != 7) {
                noAction = true;
            }
            else if (fightObject.getStatusValue(8008) > 0) {
                noAction = true;
            }
        }
        if (fightObject.isFrozen()) {
            noAction = true;
        }
        if (noAction) {}
        return noAction;
    }
    
    private List<FightObject> getConfusionFightTargets(final FightObject fightObject, final Fight fight) {
        final List<FightObject> result = new ArrayList<FightObject>();
        final List<FightObject> firstTargets = SkillTargetPicker.getFightObjects(fight, fightObject, true);
        final List<FightObject> secondTargets = SkillTargetPicker.getFightObjects(fight, fightObject, false);
        final Optional<RoleSkill> any = fightObject.getSkillIds().stream().filter(obj -> obj.getSkillId().equals(83032)).findAny();
        if (any.isPresent()) {
            for (final FightObject target : firstTargets) {
                if (fightObject.getId().equals(target.getId())) {
                    continue;
                }
                result.add(target);
            }
        }
        if (result.size() <= 0) {
            for (final FightObject target : secondTargets) {
                if (fightObject.getId().equals(target.getId())) {
                    continue;
                }
                result.add(target);
            }
        }
        if (result.size() <= 0) {
            for (final FightObject target : firstTargets) {
                if (fightObject.getId().equals(target.getId())) {
                    continue;
                }
                result.add(target);
            }
        }
        return result;
    }
    
    private void setNormalAttack(final Fight fight, final FightObject attacker) {
        attacker.setType(0);
        attacker.setSkillId(0);
        attacker.setTargetId(0);
        this.normalAttack(attacker, fight);
    }
    
    public void getSkillFail(final Fight fight, final FightObject attacker, final String cause) {
        attacker.setType(0);
        attacker.setSkillId(0);
        this.normalAttack(attacker, fight);
        this.logger.error("获取技能信息失败改变为普通攻击，原因{}", cause);
    }
    
    private int costMana(final Fight fight, final FightObject attacker, final RoleSkill skill, int mana) {
        if (attacker.isMonster()) {
            final String skillStr = skill.getSkillId().toString();
            final String skillStrFrist = skillStr.substring(0, 1);
            if ("3".equals(skillStrFrist) || "8".equals(skillStrFrist)) {
                mana = 0;
            }
        }
        else if (attacker.isPartner()) {
            mana = 0;
        }
        else if (attacker.isPlayer()) {
            final int alterMp = PassiveSkillUtils.subtractMp(fight, attacker, mana);
            if (alterMp != 0) {
                mana += alterMp;
            }
        }
        return mana;
    }
    
    public void useItem(final FightObject effector, final Fight fight) {
        final int num = 1;
        if (this.checkDeStatus(fight, effector)) {
            return;
        }
        effector.recordAction(fight);
        final SysDrug drug = ItemDataPool.data_drug.get(effector.getSkillId());
        if (drug == null) {
            return;
        }
        if (effector.isPet() && (10027 == drug.getSerial() || 10028 == drug.getSerial())) {
            return;
        }
        final Integer useType = drug.getUseType();
        if (useType.equals(0)) {
            return;
        }
        FightObject target = fight.queryFightObject(effector.getTargetId());
        if (target == null) {
            if (4 == drug.getShowClass()) {
                return;
            }
            final List<FightObject> mySide = fight.getMySide(effector.getId());
            final Random random = new Random();
            final int i = random.nextInt(mySide.size());
            final FightObject fightObject = target = mySide.get(i);
        }
        if (effector.getLogic() != LOGIC.LOGICTYPE_MONSTER.getValue()) {
            final BagService bagService = SpringUtils.getBean(BagService.class);
            final Role role = effector.getRole();
            final Integer drugNum = bagService.consumeBagGoods(role, drug.getSerial(), num, true, true, "战斗");
            if (drugNum != null) {
                PetSkillEffectorUtil.sendMsg(fight, effector, "药品已用尽!");
                return;
            }
        }
        final List<FiedValue> passiveEffects = PassiveSkillUtils.useDrug(fight, effector, target, drug.getShowClass());
        DrugProcessor drugProcessor = null;
        if (drug.getShowClass() == 4 || drug.getShowClass() == 5) {
            drugProcessor = DrugProcessor.queryProcessor(drug.getSerial());
            if (drugProcessor == null) {
                return;
            }
            final boolean isExcute = drugProcessor.checkExcute(fight, effector, target, passiveEffects);
            if (!isExcute) {
                return;
            }
        }
        switch (drug.getShowClass()) {
            case 1: {
                this.useItemHP(fight, effector, target, drug, passiveEffects);
                break;
            }
            case 2: {
                this.useItemMP(fight, effector, target, drug, passiveEffects);
                break;
            }
            case 3: {
                this.useItemHPMP(fight, effector, target, drug, passiveEffects);
                break;
            }
            case 4: {
                drugProcessor.effect(fight, effector, target, passiveEffects);
                break;
            }
            case 5: {
                drugProcessor.effect(fight, effector, target, passiveEffects);
                break;
            }
        }
    }
    
    private void useItemHP(final Fight fight, final FightObject effector, final FightObject target, final SysDrug drug, final List<FiedValue> passiveEffects) {
        final int oldLife = target.getCurrLife();
        final int addHp = this.reduceHPMP(fight, true, target, drug, passiveEffects);
        this.sendMsgHp(fight, effector, target, drug, oldLife, addHp);
        this.tianFuAddTarget(fight, effector, target, passiveEffects, drug, addHp, 0);
    }
    
    private void useItemMP(final Fight fight, final FightObject effector, final FightObject target, final SysDrug drug, final List<FiedValue> passiveEffects) {
        final int addMp = this.reduceHPMP(fight, false, target, drug, passiveEffects);
        this.sendMsgMp(fight, effector, target, drug, addMp);
        this.tianFuAddTarget(fight, effector, target, passiveEffects, drug, 0, addMp);
    }
    
    private void useItemHPMP(final Fight fight, final FightObject effector, final FightObject target, final SysDrug drug, final List<FiedValue> passiveEffects) {
        final int addMp = this.reduceHPMP(fight, false, target, drug, passiveEffects);
        final int oldLife = target.getCurrLife();
        final int addHp = this.reduceHPMP(fight, true, target, drug, passiveEffects);
        boolean isRevive = false;
        if (oldLife <= 0) {
            isRevive = true;
        }
        ActionParamUtils.changeHpMp(fight, effector, target, 5, drug.getSerial(), 2, addHp, addMp, isRevive);
    }
    
    private int reduceHPMP(final Fight fight, final boolean isHP, final FightObject target, final SysDrug drug, final List<FiedValue> passiveEffects) {
        if (isHP) {
            int addHp = 0;
            if (drug.getDrugAddHpValue() > 0) {
                addHp = drug.getDrugAddHpValue();
            }
            else if (drug.getDrugAddHpPercent() > 0) {
                addHp = target.getMaxLife() * drug.getDrugAddHpPercent() / 100;
            }
            if (passiveEffects != null && passiveEffects.size() > 0) {
                int addPercentHp = 0;
                for (final FiedValue fied : passiveEffects) {
                    if (fied.getType().contains("hp") && fied.getType().contains("addPercent") && 5 == fied.getVT()) {
                        final Double value = (Double)fied.getValue();
                        if (value != null && value > 0.0) {
                            addPercentHp += (int)(addHp * value);
                            break;
                        }
                        continue;
                    }
                }
                if (addPercentHp > 0) {
                    addHp += addPercentHp;
                }
            }
            addHp += PassiveSkillUtils.countRecoverBloodEnd(fight, target, addHp, null);
            return addHp;
        }
        int addMp = 0;
        if (drug.getDrugAddMpValue() > 0) {
            addMp = drug.getDrugAddMpValue();
        }
        else if (drug.getDrugAddMpPercent() > 0) {
            addMp = target.getMaxMana() * drug.getDrugAddMpPercent() / 100;
        }
        if (passiveEffects != null && passiveEffects.size() > 0) {
            int addPercentMp = 0;
            for (final FiedValue fied : passiveEffects) {
                if (fied.getType().contains("mp") && fied.getType().contains("addPercent") && 5 == fied.getVT()) {
                    final Double value = (Double)fied.getValue();
                    if (value != null && value > 0.0) {
                        addPercentMp += (int)(addMp * value);
                        break;
                    }
                    continue;
                }
            }
            if (addPercentMp > 0) {
                addMp += addPercentMp;
            }
        }
        addMp += PassiveSkillUtils.countRecoverMpEnd(fight, target, addMp);
        return addMp;
    }
    
    private void tianFuAddTarget(final Fight fight, final FightObject effector, final FightObject target, final List<FiedValue> tianFuEffects, final SysDrug drug, final int addHp, final int addMp) {
        if (tianFuEffects != null && tianFuEffects.size() > 0) {
            boolean flag = false;
            int targetId = 0;
            for (final FiedValue fied : tianFuEffects) {
                if ("addTarget".equals(fied.getType()) && 6 == fied.getVT()) {
                    flag = (boolean)fied.getValue();
                }
                if ("addTargetId".equals(fied.getType()) && 3 == fied.getVT() && fied.getValue() != null && String.valueOf(fied.getValue()).matches("^[0-9]+$")) {
                    targetId = (int)fied.getValue();
                }
            }
            if (flag) {
                if (addHp > 0) {
                    if (targetId == 0) {
                        if (target.isPet()) {
                            final FightObject host = fight.queryFightObject(target.getHostId());
                            this.addTargetHp(fight, effector, host, drug, addHp, addMp);
                        }
                        else if (target.isPlayer()) {
                            final Optional<FightObject> petOptional = fight.getMySide(target.getId()).stream().filter(obj -> obj.isPet() && target.getId().equals(obj.getHostId())).findFirst();
                            if (petOptional.isPresent()) {
                                final FightObject pet = petOptional.get();
                                this.addTargetHp(fight, effector, pet, drug, addHp, addMp);
                            }
                        }
                    }
                    else {
                        final FightObject newTarget = fight.queryFightObject(targetId);
                        this.addTargetHp(fight, effector, newTarget, drug, addHp, addMp);
                    }
                }
                else if (addMp > 0) {
                    if (targetId == 0) {
                        this.addTargetMp(fight, effector, target, drug, addMp);
                    }
                    else {
                        final FightObject newTarget = fight.queryFightObject(targetId);
                        this.sendMsgMp(fight, effector, newTarget, drug, addMp);
                    }
                }
            }
        }
    }
    
    private void addTargetHp(final Fight fight, final FightObject effector, final FightObject newTarget, final SysDrug drug, final int addHp, final int addMp) {
        boolean isRevive = false;
        if (newTarget.getCurrLife() <= 0) {
            isRevive = true;
        }
        if (addMp > 0) {
            ActionParamUtils.changeHpMp(fight, effector, newTarget, 5, drug.getSerial(), 2, addHp, addMp, isRevive);
        }
        else {
            ActionParamUtils.changeLife(fight, effector, newTarget, 5, drug.getSerial(), 2, addHp, isRevive);
        }
    }
    
    private void addTargetMp(final Fight fight, final FightObject effector, final FightObject target, final SysDrug drug, final int addMp) {
        if (target.isPet()) {
            final FightObject host = fight.queryFightObject(target.getHostId());
            this.sendMsgMp(fight, effector, host, drug, addMp);
        }
        else if (target.isPlayer()) {
            final Optional<FightObject> petOptional = fight.getMySide(target.getId()).stream().filter(obj -> obj.isPet() && target.getId().equals(obj.getHostId())).findFirst();
            if (petOptional.isPresent()) {
                final FightObject pet = petOptional.get();
                this.sendMsgMp(fight, effector, pet, drug, addMp);
            }
        }
    }
    
    private void sendMsgHp(final Fight fight, final FightObject effector, final FightObject target, final SysDrug drug, final int oldLife, final int addHp) {
        boolean isRevive = false;
        if (oldLife <= 0) {
            isRevive = true;
        }
        ActionParamUtils.changeLife(fight, effector, target, 5, drug.getSerial(), 2, addHp, isRevive);
    }
    
    private void sendMsgMp(final Fight fight, final FightObject effector, final FightObject target, final SysDrug drug, final int addMp) {
        ActionParamUtils.changeMana(fight, effector, target, 5, drug.getSerial(), 2, addMp);
    }
    
    private void flee(final FightObject fightObject, final Fight fight) {
        if (fightObject.isFallDown()) {
            return;
        }
        if (this.checkDeStatus(fight, fightObject)) {
            return;
        }
        final Role role = this.roleService.getOnlinePlayer(fightObject.getRole().getId());
        if (role == null) {
            if (!(fightObject instanceof MonsterFightObject)) {
                return;
            }
            final FightMonsterNewService bean = SpringUtils.getBean(FightMonsterNewService.class);
            bean.monsterFlee(fightObject, fight);
        }
        fightObject.recordAction(fight);
        final ActionDetailParam actionDetailParam = new ActionDetailParam(fightObject, 4, 4, 0);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        final FightObjectParam attackerParam = actionDetailParam.gainAttackerParam();
        if (!this.canFlee(fightObject, fight)) {
            attackerParam.setHit(false);
            return;
        }
        this.excuteTeamDisappear(fight, role, fightObject);
        fight.getFleeList().add(fightObject.getId());
        fight.addExitRole(fightObject.isASide(), new FieldValue(2, fightObject.getId()));
        role.setLastDead((fightObject.getCurrLife() > 0) ? 1 : 0);
        EventDispatcher.getInstance().fireEvent(new FightEndEvent(fight, EventType.FIGHT_END, role, fight.getId(), (byte)3, fight.getType(), fight.getBossReward(), fight.getTaskId(), fight.getNpcId(), 0));
        fight.removeFightObject(fightObject);
        final FightObject petObject = fight.queryFightObject(fightObject.getFightPetId());
        if (petObject != null) {
            fight.getFleeList().add(petObject.getId());
            fight.petDie(petObject);
        }
        role.save();
    }
    
    public void excuteTeamDisappear(final Fight fight, final Role role, final FightObject roleObject) {
        role.setPrevFightEndTime(System.currentTimeMillis());
        role.setCanFight(true);
        this.resetAfterLeaveFight(fight, roleObject);
        final int teamCount = this.teamService.getTeamCount(role);
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null && teamCount > 1 && !role.getRoleId().equals(team.getLeaderRoleId())) {
            this.teamService.leaveTemp(role, false);
        }
        final List<FightObject> mySide = fight.getMySide(roleObject.getId());
        if (team != null) {
            final Member member = team.getList().get(0);
            if (role.getRoleId().equals(member.getRoleId())) {
                for (int i = mySide.size() - 1; i >= 0; --i) {
                    final FightObject obj = mySide.get(i);
                    if (obj.isPartner()) {
                        fight.removeFightObject(obj);
                    }
                }
            }
        }
        else if (mySide != null && mySide.size() > 0) {
            for (int j = mySide.size() - 1; j >= 0; --j) {
                if (mySide.get(j).isPartner() && mySide.get(j).getHostId() == roleObject.getId()) {
                    fight.removeFightObject(mySide.get(j));
                }
            }
        }
    }
    
    private boolean canFlee(final FightObject fightObject, final Fight fight) {
        if (Const.not_flee.contains(fight.getType())) {
            return false;
        }
        int substractOdds = 0;
        if (fight.getType() == FIGHT_TYPE.FORCE_PK.getValue()) {
            if (fightObject.inDeBuffStatus()) {
                return false;
            }
            if (fightObject.isASide()) {
                return false;
            }
            substractOdds = 20;
        }
        final int rand = ThreadLocalRandom.current().nextInt(100);
        final int odds = 100 - substractOdds;
        return rand < odds;
    }
    
    public void selectPet(final FightObject fightObject, final Fight fight) {
        if (this.checkDeStatus(fight, fightObject)) {
            return;
        }
        fightObject.recordAction(fight);
        final Integer roleId = FightExecutorNewService.PETID_ROLEID.get(fightObject.getTargetId());
        if (roleId != null) {
            return;
        }
        final int oldPetId = fightObject.getFightPetId();
        final int newPetId = fightObject.getTargetId();
        final Role role = fightObject.getRole();
        final Pet newPet = this.petService.selectPet(role).get(newPetId);
        if (newPet == null) {
            this.logger.error("未找到要召唤的的宠物id：" + newPetId + ",宠物的主人id：" + role.getId());
            return;
        }
        final CombatAddObject combatAddObject = this.addPet2Fight(fightObject.getId(), newPet, fight, SkillFunc.getPetPosByMasterPos(fightObject.getPos()));
        fightObject.setFightPetId(newPet.getPetId());
        final FightObject delPet = fight.queryFightObject(oldPetId);
        FightUtils.excutePetDisappear(fight, fightObject, delPet);
        final ActionDetailParam actionDetailParam = new ActionDetailParam(fightObject, 3, 0, newPet.getPetId());
        final RefreshPet refreshPet = new RefreshPet();
        final ArrayList<RefreshPet> refreshPets = new ArrayList<RefreshPet>();
        refreshPets.add(refreshPet);
        refreshPet.setPetId(newPet.getPetId());
        refreshPet.setType(1);
        actionDetailParam.setPetList(refreshPets);
        actionDetailParam.setRoleId(role.getId());
        FightExecutorNewService.PETID_ROLEID.put(newPet.getPetId(), newPet.getRoleId());
        FightExecutorNewService.FIGHT_ROOMS.put(newPet.getPetId(), fight.getId());
        final List<CombatAddObject> list = new ArrayList<CombatAddObject>();
        list.add(combatAddObject);
        actionDetailParam.setCombatAddObjects(list);
        final RespFightShouting fightShouting = this.fightMessageNewService.fightShouting(fight.queryFightObject(newPet.getPetId()), "战斗出场");
        final FightObjectParam fightObjectParam = actionDetailParam.gainTargetParam(newPetId, true);
        fightObjectParam.setFightShouting(fightShouting);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        PetPassiveSkillUtil.otherEnterBattle(fight, newPetId);
    }
    
    public void callbackPet(final FightObject fightObject, final Fight fight, final boolean isNoPassiveSkill) {
        if (isNoPassiveSkill && this.checkDeStatus(fight, fightObject)) {
            return;
        }
        fightObject.recordAction(fight);
        boolean flag = false;
        final FightObject target = fight.queryFightObject(fightObject.getTargetId());
        fightObject.setFightPetId(0);
        if (target != null && !target.isFallDown()) {
            target.setCurrLife(0);
        }
        else {
            flag = true;
        }
        if (target != null) {
            final List<Integer> statusList = new ArrayList<Integer>();
            for (final int status : StatusConst.getPositiveStatus()) {
                if (target.getStatusValue(status) > 0) {
                    statusList.add(status);
                }
            }
            PassiveSkillUtils.exitOrClearPositive(fight, fightObject, target, statusList);
            fight.excuteExitPassive(target);
        }
        if (target != null) {
            ActionParamUtils.excuteDisappear(fight, fightObject, target.getId(), 12, 0, 2);
        }
        if (!flag) {
            PetSkillEffectorUtil.sendCancelAction(fight, target);
            fight.petDie(target);
        }
    }
    
    private void guard(final FightObject fightObject, final Fight fight) {
        fightObject.recordAction(fight);
        PassiveSkillUtils.excutePassiveSkill(fight, fightObject, PET_PASSIVE_SKILL_TRIGGER_DICT.SELF_SELL_END.getValue());
        if (this.checkDeStatus(fight, fightObject)) {
            return;
        }
        final TianFuProcessor tianFuProcessor = TianFuProcessor.queryProcessor(323304);
        tianFuProcessor.passive(fight, fightObject, null);
    }
    
    private Map<Integer, List<Integer>> excuteStatusReduce(final Fight fight) {
        boolean isHaveDie = false;
        final Map<Integer, FightObject> dragonObjectMap = new HashMap<Integer, FightObject>();
        final Map<Integer, List<Integer>> clearStatusMap = new HashMap<Integer, List<Integer>>();
        final List<FightObjectParam> targetParamList = new ArrayList<FightObjectParam>();
        int targetOrder = 1;
        for (final FightObject fightObject : fight.getAllFightObjects()) {
            final List<Integer> list = new ArrayList<Integer>();
            final int dict = 1022;
            final String dist = String.valueOf(dict);
            final NutMap statusMap = fightObject.getStatusMap();
            int recoverStatus = 0;
            if (statusMap.getInt(dist, 0) != 0) {
                recoverStatus = dict;
            }
            NutMap recoverMap = null;
            if (statusMap.getInt(dist) > 0) {
                final boolean dead = fightObject.isFallDown();
                fightObject.pushTempCache("dead", dead);
                if (recoverStatus != 0 && !fightObject.isFrozen() && !fight.isOver()) {
                    int recoverValue = fightObject.getStatusSubMap().getInt(dict + "value");
                    if (recoverValue > 0) {
                        fightObject.pushTempCache("recover", true);
                        if (recoverValue > fightObject.getMaxLife() - fightObject.getCurrLife()) {
                            recoverValue = fightObject.getMaxLife() - fightObject.getCurrLife();
                        }
                        recoverValue += PassiveSkillUtils.countRecoverBloodEnd(fight, fightObject, recoverValue, null);
                        fightObject.pushTempCache("recoverLife", recoverValue);
                        fightObject.setCurrLife(fightObject.getCurrLife() + recoverValue);
                        if (fightObject.getMaxLife() < fightObject.getCurrLife()) {
                            fightObject.setCurrLife(fightObject.getMaxLife());
                        }
                        if (!fightObject.isFallDown()) {
                            fightObject.getTempCache().remove("dead");
                        }
                    }
                    fightObject.pushTempCache("recoverSkill", 0);
                    recoverMap = this.fightMessageNewService.poisonOrRecoverActionParam(fight, fightObject);
                    if (dead) {
                        fightObject.removeStatus(dict);
                        fightObject.removeStatusSub(dict + "value");
                        list.add(dict);
                    }
                }
            }
            final List<Integer> statusResidueTimes = new ArrayList<Integer>();
            for (final Map.Entry<String, Object> entry : statusMap.entrySet()) {
                final String key = entry.getKey();
                if (!key.matches("^[0-9]+$")) {
                    continue;
                }
                int value = (int) entry.getValue();
                if (value > 0) {
                    final Integer status = Integer.valueOf(key);
                    if (StatusConst.getControlStatus().contains(status)) {
                        if (PassiveSkillUtils.isUnlock(fight, fightObject, status)) {
                            continue;
                        }
                        final double odds = statusMap.getDouble(key + "odds");
                        if (!SkillAi.canAffectOnPro(odds)) {
                            statusMap.setv(key, 0);
                            list.add(status);
                            PassiveSkillUtils.statusReduce(fight, fightObject, status);
                            continue;
                        }
                    }
                    if (--value <= 0) {
                        if (status.equals(1023)) {
                            dragonObjectMap.put(fightObject.getId(), fightObject);
                        }
                        list.add(status);
                        PassiveSkillUtils.statusReduce(fight, fightObject, status);
                        if (200 == Integer.parseInt(key)) {
                            statusResidueTimes.add(status);
                            statusResidueTimes.add(value);
                        }
                    }
                    else {
                        statusResidueTimes.add(status);
                        statusResidueTimes.add(value);
                    }
                }
                statusMap.put(key, value);
            }
            clearStatusMap.put(fightObject.getId(), list);
            PassiveSkillUtils.excutePropertyChange(fightObject);
            fightObject.reduceImbue();
            fightObject.reduceSkillCool();
            fight.clearAstrictSkills();
            FightObjectParam targetParam = this.getPoisonOrRecoverParam(fightObject, recoverMap);
            if (list.size() > 0 || dragonObjectMap.size() > 0 || statusResidueTimes.size() > 0) {
                if (targetParam == null) {
                    targetParam = new FightObjectParam();
                    targetParam.setObjId(fightObject.getId());
                }
                if (dragonObjectMap.size() > 0) {
                    final FightObject dragonObject = dragonObjectMap.get(fightObject.getId());
                    if (dragonObject != null) {
                        this.dragonMorphRenew(dragonObject);
                        targetParam.setMaxLife(fightObject.getMaxLife());
                        targetParam.setCurrentLife(fightObject.getCurrLife());
                        targetParam.setMaxMana(fightObject.getMaxLife());
                        targetParam.setCurrentMana(fightObject.getCurrMana());
                    }
                }
                if (list.size() > 0) {
                    targetParam.setClearStatus(list);
                }
                if (statusResidueTimes.size() > 0) {
                    final Map<Integer, Integer> updateStatusMap = this.fightMessageNewService.sendFightObjectStatus(fight, fightObject);
                    targetParam.setStatusMap(updateStatusMap);
                }
            }
            if (targetParam != null) {
                targetParam.setOrder(targetOrder);
                targetParam.setMissed(2);
                targetParamList.add(targetParam);
                ++targetOrder;
            }
            if (fightObject.isFallDown()) {
                isHaveDie = true;
            }
        }
        if (targetParamList.size() > 0) {
            final ActionDetailParam actionDetailParam = new ActionDetailParam(43, 0, 0, 0);
            actionDetailParam.getTargetParamList().addAll(targetParamList);
            fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        }
        if (isHaveDie) {
            PetPassiveSkillUtil.deathExcute(fight);
        }
        return clearStatusMap;
    }
    
    public void excuteRoundStartPoison(final Fight fight) {
        for (final FightObject fightObject : fight.getAllFightObjects()) {
            final NutMap statusMap = fightObject.getStatusMap();
            fightObject.pushTempCache("alive", !fightObject.isFallDown());
            if (statusMap.getInt(String.valueOf(1005), 0) != 0 && !fightObject.isFrozen()) {
                fightObject.pushTempCache("poison", true);
                final NutMap nutMap = fightObject.getStatusSubMap();
                Integer hurt = SkillCompute.computeSkillDamagePoisonFollowUp(nutMap);
                hurt += PassiveSkillUtils.poisonReduceHp(fight, fightObject, hurt);
                if (hurt < 1) {
                    hurt = 1;
                }
                else if (hurt > fightObject.getCurrLife()) {
                    hurt = fightObject.getCurrLife();
                }
                fightObject.pushTempCache("poisonValue", hurt);
                fightObject.setCurrLife(fightObject.getCurrLife() - hurt);
                if (fightObject.isFallDown()) {
                    final boolean revive = FightRateUtil.petCanRevive(fightObject);
                    fightObject.pushTempCache("revive", revive);
                }
                fightObject.pushTempCache("poisonDead", fightObject.isFallDown());
                final int round = fightObject.getStatusMap().getInt("1005_round") + 1;
                fightObject.getStatusMap().setv("1005_round", (Object)round);
            }
        }
        final ActionDetailParam actionDetailParam = new ActionDetailParam(43, 0, 0, 0);
        boolean isHavePoison = false;
        boolean isHaveDie = false;
        int order = 1;
        for (final FightObject fightObject2 : fight.getAllFightObjects()) {
            final NutMap poisonMap = this.fightMessageNewService.poisonOrRecoverActionParam(fight, fightObject2);
            if (poisonMap != null) {
                final FightObjectParam target = this.getPoisonOrRecoverParam(fightObject2, poisonMap);
                if (target != null) {
                    target.setOrder(order);
                    actionDetailParam.getTargetParamList().add(target);
                    if (!isHavePoison) {
                        isHavePoison = true;
                    }
                    ++order;
                }
            }
            if (!isHaveDie && fightObject2.isFallDown()) {
                isHaveDie = true;
            }
        }
        if (isHavePoison) {
            fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        }
        if (isHaveDie) {
            PetPassiveSkillUtil.deathExcute(fight);
        }
        for (final FightObject fightObject2 : fight.getAllFightObjects()) {
            final boolean poisonDead = fightObject2.getTempCache("poisonDead", false);
            final boolean dead = fightObject2.getTempCache("dead", false);
            if (dead || poisonDead) {
                final boolean revive2 = fightObject2.getTempCache("revive", false);
                if (fightObject2.isMonster()) {
                    final int monsterType = ((MonsterFightObject)fightObject2).getMonsterType();
                    if (monsterType != FIGHT_TYPE.STAR.getValue() && monsterType != FIGHT_TYPE.SEAL_DEMON.getValue() && monsterType != FIGHT_TYPE.THREE_SCOPE.getValue()) {
                        fight.removeFightObject(fightObject2);
                    }
                }
                else if (!revive2 && fightObject2.isPet()) {
                    fight.removeFightObject(fightObject2);
                }
            }
            if (fightObject2.getTempCache() != null) {
                fightObject2.getTempCache().clear();
            }
        }
    }
    
    private FightObjectParam getPoisonOrRecoverParam(final FightObject fightObject, final NutMap map) {
        if (map != null) {
            final FightObjectParam targetParam = new FightObjectParam();
            targetParam.setObjId(fightObject.getId());
            final int currLife = map.getInt("currLife", 0);
            if (currLife != 0) {
                targetParam.setCurrentLife(currLife);
            }
            else {
                targetParam.setCurrentLife(fightObject.getCurrLife());
            }
            final int changeLife = map.getInt("changeLife", 0);
            if (changeLife != 0) {
                targetParam.setChangeLife(changeLife);
            }
            final int currMana = map.getInt("currMana", 0);
            if (currMana != 0) {
                targetParam.setCurrentMana(currMana);
            }
            else {
                targetParam.setCurrentMana(fightObject.getCurrMana());
            }
            final int changeMana = map.getInt("changeMana", 0);
            if (changeMana != 0) {
                targetParam.setChangeMana(changeMana);
            }
            final int missed = map.getInt("missed", -1);
            if (missed != -1) {
                targetParam.setMissed(missed);
            }
            return targetParam;
        }
        return null;
    }
    
    public void dragonMorphRenew(final FightObject fightObject) {
        final SysSkill sysSkill = SkillDataPool.skillMap.get(30041);
        final double[] power = sysSkill.getPower();
        final int life_gain = (int)(fightObject.getCurrLife() * power[0]);
        final int currLife = life_gain + fightObject.getCurrLife();
        final int mana_gain = (int)(fightObject.getCurrMana() * power[1]);
        final int currMana = mana_gain + fightObject.getCurrMana();
        final int maxLife = fightObject.getStatusSubMap().getInt("1023_max_life");
        final int maxMana = fightObject.getStatusSubMap().getInt("1023_max_mana");
        fightObject.removeStatusSub("1023_max_life");
        fightObject.removeStatusSub("1023_max_mana");
        fightObject.setCurrLife((currLife > maxLife) ? maxLife : currLife);
        fightObject.setCurrMana((currMana > maxMana) ? maxMana : currMana);
        fightObject.setMaxLife(maxLife);
        fightObject.setMaxMana(maxMana);
    }
    
    @EventHandler({ EventType.LOGIN })
    public void handleLoginEvent(final LoginEvent loginEvent) {
        final Role role = loginEvent.getRole();
        this.enterFight(role);
    }
    
    private void enterFight(final Role role) {
        final Fight fight = this.getFightByRoleId(role.getRoleId());
        if (fight == null) {
            role.setCanFight(true);
            return;
        }
        if (fight.isOver() && 3 != fight.getRoundType()) {
            FightExecutorNewService.FIGHT_ROOMS.remove(role.getRoleId());
            return;
        }
        final FightObject roleObject = fight.queryFightObject(role.getRoleId());
        if (roleObject == null) {
            role.setCanFight(true);
            return;
        }
        if (2 == fight.getRoundType()) {
            try {
                Thread.sleep(100L);
            }
            catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
        final RoundParam roundParam = fight.getRoundParam();
        RespExcuteFight respExcuteFight = null;
        boolean flag = false;
        RespFightInParam respFightInParam;
        RespRoundStartParam roundStartParam;
        if (3 == fight.getRoundType()) {
            respFightInParam = FightParamPool.createRespFightInParam(false, fight.getId(), role.getRoleId(), roleObject.isASide());
            roundStartParam = FightParamPool.getRespRoundStartParam(fight.getId(), role.getRoleId());
            respExcuteFight = FightParamPool.gainFightRoundParam(fight.getId()).getRespExcuteFight();
            flag = true;
        }
        else {
            respFightInParam = this.getRoundFightParam(role, fight, roleObject);
            roundStartParam = this.getRoundStartParamAll(fight, roleObject);
            if (fight.getRound() == roundParam.getRound()) {
                roundStartParam.setStartTime(String.valueOf(roundParam.getBeganTime()));
            }
        }
        respFightInParam.setFightType(fight.getType());
        respFightInParam.setId(String.valueOf(fight.getId()));
        FightMessageUtil.sendFightPackage(role.getId(), respFightInParam);
        FightMessageUtil.sendFightPackage(role.getId(), roundStartParam);
        for (final FightObject fightObject : fight.getAllFightObjects()) {
            if (!fightObject.isMonster() && fightObject.isDoAction()) {
                final RespInputInstruct respInputInstruct = new RespInputInstruct(fightObject.getId(), fightObject.getTargetId(), fightObject.getType(), fightObject.getSkillId(), 0);
                FightMessageUtil.sendFightPackage(role.getId(), respInputInstruct);
            }
        }
        if (flag) {
            FightMessageUtil.sendFightPackage(role.getId(), respExcuteFight);
        }
        this.broadcastService.sendTitleInfoNew(role);
    }
    
    public RespFightInParam getRoundFightParam(final Role role, final Fight fight, final FightObject roleObject) {
        final RespFightInParam respFightInParam = new RespFightInParam();
        final FightObject petObject = fight.queryFightObject(roleObject.getFightPetId());
        if (petObject != null) {
            final Pet pet = petObject.getPet();
            if (pet != null) {
                final PetFightInParam petFightParam = fight.getPetFightParam(pet);
                respFightInParam.addPetFightInParam(petFightParam);
            }
        }
        final List<SkillParam> skillParams = fight.getSkillParams(role);
        if (skillParams != null) {
            respFightInParam.setSkillParams(skillParams);
        }
        final List<RefreshPet> refreshPets = this.getFightInParamPetList(fight, roleObject);
        respFightInParam.setPetList(refreshPets);
        final List<ExcuteFightParam> excuteFightParams = fight.getFightStatus();
        if (excuteFightParams != null) {
            respFightInParam.setExcuteFightParams(excuteFightParams);
        }
        if (roleObject.isASide()) {
            respFightInParam.getRespCombatFriends().setList(this.getFightMemberList(fight.getListA()));
            respFightInParam.getRespCombatOpponets().setList(this.getFightMemberList(fight.getListB()));
        }
        else {
            respFightInParam.getRespCombatFriends().setList(this.getFightMemberList(fight.getListB()));
            respFightInParam.getRespCombatOpponets().setList(this.getFightMemberList(fight.getListA()));
        }
        return respFightInParam;
    }
    
    public List<FightMember> getFightMemberList(final List<FightObject> fightObjs) {
        final ArrayList<FightMember> members = new ArrayList<FightMember>();
        for (final FightObject fightObject : fightObjs) {
            final FightMember fightMember = fightObject.asFightMember();
            if (fightMember != null) {
                if (fightObject.getLogic() == LOGIC.LOGICTYPE_MONSTER.getValue()) {
                    fightMember.setSerial(50000);
                }
                members.add(fightMember);
            }
        }
        return members;
    }
    
    public List<RefreshPet> getFightInParamPetList(final Fight fight, final FightObject fightObject) {
        final FightObject petObject = fight.queryFightObject(fightObject.getFightPetId());
        final List<RefreshPet> refreshPets = new ArrayList<RefreshPet>();
        RefreshPet refreshPet = null;
        if (petObject != null) {
            refreshPet = new RefreshPet();
            refreshPets.add(refreshPet);
            refreshPet.setPetId(petObject.getId());
            refreshPet.setType(1);
        }
        final Role role = fightObject.getRole();
        if (role != null) {
            final Map<Integer, Pet> petMap = PetCache.PET_MAP.get(role.getId());
            if (petMap != null && petMap.size() > 0) {
                final Set<Integer> deadPetIds = fight.getDeadPetIds();
                for (final Map.Entry<Integer, Pet> entry : petMap.entrySet()) {
                    final Pet pet = entry.getValue();
                    if (refreshPet != null && pet.getPetId().equals(refreshPet.getPetId())) {
                        continue;
                    }
                    final RefreshPet refreshPet2 = new RefreshPet();
                    refreshPet2.setPetId(pet.getPetId());
                    if (deadPetIds.contains(pet.getPetId())) {
                        refreshPet2.setType(1);
                    }
                    else {
                        refreshPet2.setType(0);
                    }
                    refreshPets.add(refreshPet2);
                }
            }
        }
        return refreshPets;
    }
    
    public RespRoundStartParam getRespRoundStartParam(final Fight fight, final int objId) {
        if (fight.getRespRoundStartParamMap().size() > 0) {
            RespRoundStartParam respRoundStartParam = fight.getRespRoundStartParamMap().get(objId);
            if (respRoundStartParam == null) {
                respRoundStartParam = new RespRoundStartParam();
                respRoundStartParam.setObjId(objId);
                respRoundStartParam.setRound(fight.getRound());
            }
            return respRoundStartParam;
        }
        RespRoundStartParam respRoundStartParam = new RespRoundStartParam();
        respRoundStartParam.setObjId(objId);
        respRoundStartParam.setRound(fight.getRound());
        return respRoundStartParam;
    }
    
    public RespRoundStartParam getRoundStartParamAll(final Fight fight, final FightObject fightObject) {
        final RespRoundStartParam respRoundStartParam = new RespRoundStartParam();
        respRoundStartParam.setObjId(fightObject.getId());
        respRoundStartParam.setAutoFight(fightObject.getAutoFight() == 1);
        respRoundStartParam.setRound(fight.getRound());
        final AutoFightSkillParam autoFightSkillParam = this.fightMessageNewService.selfAuto(fightObject, fight);
        if (autoFightSkillParam != null) {
            respRoundStartParam.addAutoFightSkillParam(autoFightSkillParam);
        }
        final List<SkillCool> cools = fightObject.getSkillCoolList();
        respRoundStartParam.addSkillCools(cools);
        final FightObject petObject = fight.queryFightObject(fightObject.getFightPetId());
        if (petObject != null) {
            final List<SkillCool> petCools = petObject.getSkillCoolList();
            if (petCools.size() > 0) {
                respRoundStartParam.addSkillCools(petCools);
            }
            final AutoFightSkillParam autoFightSkillParamPet = this.fightMessageNewService.selfAuto(petObject, fight);
            if (autoFightSkillParamPet != null) {
                respRoundStartParam.addAutoFightSkillParam(autoFightSkillParamPet);
            }
        }
        return respRoundStartParam;
    }
    
    public void maxRoundTimer(final Fight fight, final long delay) {
        if (fight != null && fight.getAllFightObjects() != null) {
            for (final FightObject fightObject : fight.getAllFightObjects()) {
                if (!fightObject.isPlayer()) {
                    continue;
                }
                fight.roundFinishTask(fightObject, delay);
            }
        }
    }
    
    public boolean sendFightParams(final Fight fight, final boolean isDistinguish) {
        boolean flag = true;
        final RespExcuteFight respExcuteFight = this.fightMessageNewService.excuteSendFightMsg(fight);
        if (respExcuteFight == null) {
            flag = false;
        }
        final StringBuffer sendRoleSB = new StringBuffer("[");
        final List<Long> playerList = fight.getActionParam().getPlayerList();
        if (playerList != null && playerList.size() > 0) {
            for (final Long playerId : playerList) {
                final Role role = this.roleService.getPlayer(playerId);
                if (isDistinguish) {
                    this.excuteSubstitution(fight, role, respExcuteFight);
                }
                FightMessageUtil.sendFightPackage(playerId, respExcuteFight);
                sendRoleSB.append(role.getRoleId() + ",");
                if (!flag) {
                    this.animationPlayEnd(fight, role.getRoleId());
                }
            }
        }
        FightParamPool.gainFightRoundParam(fight.getId()).setRespExcuteFight(respExcuteFight);
        if (fight.isWatchFightOnOff()) {
            final FightSpectatorNewService fightSpectatorService = SpringUtils.getBean(FightSpectatorNewService.class);
            SpringUtils.getBean(FightSpectatorExecutorNewService.class).addFightTask(fight.getId(), () -> fightSpectatorService.sendWatchFightData(fight, (byte)2));
        }
        sendRoleSB.deleteCharAt(sendRoleSB.length() - 1);
        sendRoleSB.append("]");
        this.fightRecordService.saveRecord(fight, respExcuteFight, sendRoleSB.toString());
        fight.getActionParam().setActionDetailParams(new ArrayList<ActionDetailParam>());
        return flag;
    }
    
    private void excuteSubstitution(final Fight fight, final Role role, final RespExcuteFight respExcuteFight) {
        if (respExcuteFight.getExcuteFightParams() != null && respExcuteFight.getExcuteFightParams().size() > 0) {
            for (final ExcuteFightParam excuteFightParam : respExcuteFight.getExcuteFightParams()) {
                if (excuteFightParam.getType() == 2 && excuteFightParam.getCombatAddObjects() != null && excuteFightParam.getCombatAddObjects().size() > 0 && !fight.isSameCamp(excuteFightParam.getAttaId(), role.getRoleId())) {
                    for (final CombatAddObject combatAddObject : excuteFightParam.getCombatAddObjects()) {
                        combatAddObject.setPosition(combatAddObject.getPosition() + 10000);
                    }
                }
            }
        }
    }
    
    public void excuteRoundStartSend(final Fight fight) {
        if (fight == null) {
            return;
        }
        final Map<Integer, RespRoundStartParam> respRoundStartParamMap = fight.getRespRoundStartParamMap();
        final long millis = System.currentTimeMillis();
        final RoundParam roundParam = new RoundParam(fight.getRound());
        roundParam.setBeganTime(millis);
        fight.setRoundParam(roundParam);
        boolean isSaveRecord = true;
        RespRoundStartParam respRoundStartParamRecord = null;
        final StringBuffer sendRoleSB = new StringBuffer("[");
        for (final FightObject fightObject : fight.getAllFightObjects()) {
            if (!fightObject.isPlayer()) {
                continue;
            }
            final Role role = fightObject.getRole();
            if (role == null) {
                continue;
            }
            final RespRoundStartParam respRoundStartParam = respRoundStartParamMap.get(fightObject.getId());
            if (respRoundStartParam == null) {
                continue;
            }
            respRoundStartParam.setStartTime(String.valueOf(millis));
            FightMessageUtil.sendFightPackage(role.getId(), respRoundStartParam);
            sendRoleSB.append(fightObject.getId() + ",");
            if (!isSaveRecord) {
                continue;
            }
            respRoundStartParamRecord = respRoundStartParam;
            isSaveRecord = false;
        }
        if (respRoundStartParamRecord != null) {
            if (sendRoleSB.length() > 5) {
                sendRoleSB.deleteCharAt(sendRoleSB.length() - 1);
            }
            sendRoleSB.append("]");
            this.fightRecordService.saveRecord(fight, respRoundStartParamRecord, sendRoleSB.toString());
        }
        FightParamPool.gainFightRoundParam(fight.getId()).setRespRoundStartParamMap(respRoundStartParamMap);
        FightParamPool.addFightInParam(fight);
        if (fight.isWatchFightOnOff()) {
            final FightSpectatorNewService fightSpectatorService = SpringUtils.getBean(FightSpectatorNewService.class);
            SpringUtils.getBean(FightSpectatorExecutorNewService.class).addFightTask(fight.getId(), () -> fightSpectatorService.sendWatchFightData(fight, (byte)1));
        }
        fight.setRespRoundStartParamMap(new HashMap<Integer, RespRoundStartParam>());
    }
    
    public void excuteFightInSend(final Fight fight) {
        if (fight == null || fight.getFightInParams() == null) {
            return;
        }
        List<ExcuteFightParam> excuteFightParams = null;
        if (fight.getActionParam().getActionDetailParams().size() > 0) {
            excuteFightParams = this.fightMessageNewService.getExcuteFightParams(fight.getActionParam().getActionDetailParams());
        }
        boolean isSaveRecord = true;
        RespFightInParam fightInParamRecord = null;
        final StringBuffer sendRoleSB = new StringBuffer("[");
        for (int i = 0; i < fight.getFightInParams().size(); ++i) {
            final RespFightInParam fightInParam = fight.getFightInParams().get(i);
            if (excuteFightParams != null) {
                fightInParam.setExcuteFightParams(excuteFightParams);
            }
            final long roleId = Long.parseLong(fightInParam.getId());
            fightInParam.setId(String.valueOf(fight.getId()));
            FightMessageUtil.sendFightPackage(roleId, fightInParam);
            sendRoleSB.append(fightInParam.getRoleId() + ",");
            if (isSaveRecord) {
                fightInParamRecord = fightInParam;
                isSaveRecord = false;
            }
        }
        if (fightInParamRecord != null) {
            if (sendRoleSB.length() > 5) {
                sendRoleSB.deleteCharAt(sendRoleSB.length() - 1);
            }
            sendRoleSB.append("]");
            this.fightRecordService.saveRecord(fight, fightInParamRecord, sendRoleSB.toString());
        }
        fight.setActionParam(new ActionParam());
    }
    
    public List<Member> excuteMemberLeaveTemp(final Role role, final Team team) {
        List<Member> list = null;
        final Optional<Member> memberOptional = team.getList().stream().filter(member -> member.getRoleId().equals(role.getRoleId()) && !member.isInTeam()).findFirst();
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            final Member newMember = new Member();
            BeanUtils.copyProperties(member, newMember);
            newMember.setInTeam(true);
            list = new ArrayList<>();
            list.add(newMember);
        }
        else {
            list = team.getList();
        }
        return list;
    }
    
    public Team getRoleFightTeam(final Role role) {
        Team team = this.teamService.getTeam(role.getRoleId());
        if (!this.teamService.isInTeam(role)) {
            final List<Member> arrayList = Collections.synchronizedList(new ArrayList<Member>());
            final Member member = Member.valueOf(role);
            arrayList.add(member);
            team = new Team();
            team.setList(arrayList);
            final ArrayList<Member> partnerMembers = SpringUtils.getBean(PartnerService.class).partner2Member(role);
            team.setPartner(partnerMembers);
        }
        return team;
    }
    
    public Map<String, Object> saveAutoFightSet(final Role role, final ReqAutoFightSetData reqAutoFightSetData) {
        final int id = reqAutoFightSetData.getId();
        final Map<String, Object> autoSet = new HashMap<String, Object>();
        autoSet.put("action", reqAutoFightSetData.getAction());
        autoSet.put("type", reqAutoFightSetData.getType());
        autoSet.put("skillId", reqAutoFightSetData.getSkillId());
        int born = 0;
        if (role.getRace() == RACE.RACE_LONG.getValue()) {
            born = reqAutoFightSetData.getBorn();
        }
        autoSet.put("born", born);
        if (role.getRoleId().equals(id)) {
            role.getExtendBox().setAutoFightSet(autoSet);
        }
        else {
            final Pet pet = this.petService.selectPet(role).get(id);
            if (pet != null) {
                pet.setAutoFightSet(autoSet);
            }
        }
        role.save();
        return autoSet;
    }
    
    public void getFightObjectAttrs(final Role role, final int fightId) {
        final Fight fight = this.getFight(fightId);
        if (fight == null) {
            return;
        }
        final Map<Integer, Integer> map = PRO_DICT_TYPE.toMap();
        final List<FightObjectAttr> list = new ArrayList<FightObjectAttr>();
        for (final FightObject obj : fight.getAllFightObjects()) {
            if (obj != null) {
                final List<FiedValue> attrs = new ArrayList<FiedValue>();
                for (final Integer key : map.keySet()) {
                    final double attr = obj.getSelfAllTempPro(key);
                    if (attr == 0.0) {
                        continue;
                    }
                    attrs.add(new FiedValue(key.toString(), (byte)3, (int)(attr * 1000.0)));
                }
                list.add(new FightObjectAttr(obj.getId(), attrs));
            }
        }
        if (list.size() > 0) {
            final RespFightObjectAttr respFightObjectAttr = new RespFightObjectAttr();
            respFightObjectAttr.setList(list);
            FightMessageUtil.sendFightPackage(role.getId(), respFightObjectAttr);
        }
    }
}
