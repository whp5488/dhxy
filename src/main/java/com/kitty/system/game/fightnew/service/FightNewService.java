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
import com.kitty.system.enums.war.itempro.mind.MIND;
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
import com.kitty.system.game.partner.domain.Partner;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.messages.vo.FightMember;
import java.util.concurrent.ScheduledFuture;
import com.kitty.common.core.SchedulerManager;
import com.kitty.system.game.fightnew.task.AutoChuShouNewTask;
import java.util.Arrays;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.enums.war.warskill.skilldef.TARGETTYPE;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.init.domain.SysDrug;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.game.fightnew.book.vo.SkillAutoPicker;
import com.kitty.system.game.enter.message.RespCombatOpponets;
import com.kitty.system.game.enter.message.RespCombatFriends;
import com.kitty.system.game.fightnew.model.LifeManaParam;
import org.nutz.lang.util.NutMap;
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
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.MagicAttackParam;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import com.kitty.system.game.vo.utils.StatusConst;
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
import java.util.ArrayList;
import java.util.Map;
import com.kitty.system.game.fightnew.model.ActionParam;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import java.util.Random;
import com.kitty.logs.LoggerUtils;
import com.kitty.system.game.fightnew.utils.FightMessageUtil;
import com.kitty.system.game.fightnew.messages.RespInputInstruct;
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
    
    public void whileSendStart(Fight fight) {
        FightBeforeActionUtil.beforeAction(fight);
        for (FightObject fightObject : fight.getAllFightObjects()) {
            if (fight.isPvp() == 1) {
                fightObject.setAutoFight(0);
            }
            IoSession session = fightObject.getSession();
            if (session == null) {
                continue;
            }
            int objId = (int)fightObject.getId();
            if (fightObject.isPet()) {
                objId = fightObject.getHostId();
            }
            RespRoundStartParam respRoundStartParam = this.getRespRoundStartParam(fight, objId);
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
            List<SkillCool> cools = fightObject.getSkillCoolList();
            if (cools.size() > 0 && roleFightInParam != null) {
                respRoundStartParam.addSkillCools(cools);
            }
            AutoFightSkillParam autoFightSkillParam = this.fightMessageNewService.selfAuto(fightObject, fight);
            if (autoFightSkillParam != null) {
                if (fightObject.isPlayer()) {
                    FightObject petObject = fight.queryFightObject(Integer.valueOf(fightObject.getFightPetId()));
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
                List<RefreshPet> refreshPets = this.getFightInParamPetList(fight, fightObject);
                roleFightInParam.setPetList(refreshPets);
            }
            TianFuProcessor tianFuProcessor = TianFuProcessor.queryProcessor(Integer.valueOf(324507));
            tianFuProcessor.passive(fight, fightObject, null, new Object[0]);
            fight.getRespRoundStartParamMap().put(Integer.valueOf(respRoundStartParam.getObjId()), respRoundStartParam);
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
        return;
    }
    
    public void excuteFlee(Fight fight) {
        List<Integer> fleeList = fight.getFleeList();
        if (fleeList != null && fleeList.size() > 0) {
            for (int i = fleeList.size() - 1; i >= 0; --i) {
                Integer fightObjectId = (Integer)fleeList.get(i);
                this.deleteRoleFightData(fight, fightObjectId);
                Role role = this.roleService.getPlayer((int)fightObjectId);
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
    
    public void checkRoundShouting(Fight fight, Role role) {
        if (fight.getData() == null) {
            return;
        }
        List<String> roundStartTips = fight.getData().getAsList("fight_round_start_shouting", String.class);
        if (roundStartTips == null || roundStartTips.size() == 0) {
            return;
        }
        int roundStartShoutingPeriod = fight.getData().getInt("fight_round_start_shouting_period", 0);
        if (roundStartShoutingPeriod > 0 && fight.getRound() % roundStartShoutingPeriod != 0) {
            return;
        }
        int remainRound = (fight.getRound() == 0) ? fight.getMaxRound() : (fight.getMaxRound() - fight.getRound() + 1);
        return;
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
                catch (Exception e) {
                    FightNewService.this.logger.error("战斗出手报错了战场ID[{}]出手id[{}}{}{}{}", new Object[] { Integer.valueOf(fight.getId()), Integer.valueOf(reqFightHandle.getAttackerId()), e.getMessage(), e.getStackTrace(), e });
                }
            }
        });
        return;
    }
    
    public void animationPlayEnd(final Fight fight, int roleId) {
        fight.removeRoundFinishTask(roleId);
        FightObject fightObject = fight.queryFightObject(Integer.valueOf(roleId));
        if (fightObject == null) {
            if (fight.getFleeList().contains(Integer.valueOf(roleId))) {
                this.excuteFlee(fight);
            }
            return;
        }
        else {
            PlayerFightObject playerObj = (PlayerFightObject)fightObject;
            playerObj.setAnimationFinish(true);
            boolean allAnimationEnd = true;
            for (FightObject object : fight.getAllFightObjects()) {
                if (!object.isPlayer()) {
                    continue;
                }
                PlayerFightObject player = (PlayerFightObject)object;
                if (!player.isAnimationFinish()) {
                    allAnimationEnd = false;
                    break;
                }
                continue;
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
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    return;
                }
                fight.setRound(fight.getRound() + 1);
                for (FightObject object : fight.getAllFightObjects()) {
                    object.setTargetId(0);
                    object.setDoAction(false);
                }
                this.fightMessageNewService.excuteRoundEnd(fight);
            }
            return;
        }
    }
    
    private void fightDoAction(ReqFightHandle reqFightHandle, int hostId, Fight fight) {
        if (fight.isOver()) {
            return;
        }
        FightObject fightObject = fight.queryFightObject(Integer.valueOf(reqFightHandle.getAttackerId()));
        if (fightObject == null) {
            return;
        }
        Role role = fightObject.getRole();
        if (role == null) {
            return;
        }
        if (fightObject.isPlayer() && reqFightHandle.getType() == 3 && reqFightHandle.getSkillId() == 0) {
            int petId = reqFightHandle.getTargetId();
            Pet pet = (Pet)this.petService.selectPet(role).get(Integer.valueOf(petId));
            if (pet != null) {
                int petLevel = (int)pet.getPetLv() + (int)pet.getRebirth() * 1000;
                int roleLevle = (int)role.getLevel() + (int)role.getRelive() * 1000;
                if (pet == null || petLevel - roleLevle > 10 || pet.getPetId().equals(Integer.valueOf(fightObject.getFightPetId())) || FightExecutorNewService.PETID_ROLEID.get(Integer.valueOf(petId)) != null) {
                    MessageAssistUtil.sendIsSuccess(role, (int)fightObject.getId(), (byte)4, null);
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
        MessageAssistUtil.sendIsSuccess(role, (int)fightObject.getId(), (byte)1, null);
        RespInputInstruct respInputInstruct = new RespInputInstruct((int)fightObject.getId(), fightObject.getTargetId(), fightObject.getType(), fightObject.getSkillId(), 0);
        FightMessageUtil.sendFightPackage(fight, respInputInstruct);
        try {
            this.roundAction(fight);
        }
        catch (Exception e) {
            e.printStackTrace();
            LoggerUtils.error("错误", e);
        }
        return;
    }
    
    private void monsterAI(Fight fight) {
        if (fight.isPkFight()) {
            return;
        }
        Random random = new Random();
        fight.getListB().forEach(attacker -> {
            if (!attacker.isDoAction()) {
                if (fight.getData() != null && fight.getData().getBoolean("fight_guess_true", false)) {
                    attacker.setType(99);
                }
                else if (fight.getData() != null && fight.getData().getBoolean("fight_order_target", false)) {
                    attacker.setType(1);
                }
                else if (this.canUseLifeItem(fight)) {
                    if ((double)attacker.getCurrLife() < (double)attacker.getMaxLife() * 0.2) {
                        attacker.setType(5);
                    }
                    else {
                        FightObject hostObject = fight.queryFightObject(Integer.valueOf(attacker.getHostId()));
                        if (hostObject != null && hostObject.isFallDown()) {
                            attacker.setTargetId((int)hostObject.getId());
                            attacker.setType(5);
                        }
                        else {
                            attacker.setType((attacker.getSkillIds().size() == 0) ? 0 : 7);
                            if (attacker.getType() == 7) {
                                int index2 = random.nextInt(attacker.getSkillIds().size());
                                attacker.setSkillId((int)((RoleSkill)attacker.getSkillIds().get(index2)).getSkillId());
                            }
                            else {
                                attacker.setSkillId(0);
                            }
                        }
                    }
                }
                else if (fight.getType() != FIGHT_TYPE.MONSTER.getValue()) {
                    boolean canUseMagic = attacker.getSkillIds().size() > 0;
                    attacker.setType(canUseMagic ? 7 : 0);
                    if (attacker.getType() == 7) {
                        int index = random.nextInt(attacker.getSkillIds().size());
                        attacker.setSkillId((int)((RoleSkill)attacker.getSkillIds().get(index)).getSkillId());
                    }
                    else {
                        attacker.setSkillId(0);
                    }
                }
                attacker.setDoAction(true);
            }
            return;
        });
        return;
    }
    
    private void roundAction(Fight fight) {
        if (!fight.isStartFight()) {
            return;
        }
        ActionParam actionParam = ActionParamUtils.createActionParam(fight);
        fight.setActionParam(actionParam);
        fight.setRoundType(2);
        fight.cancelTask(fight.getTimeOutTask());
        if (fight.getRound() >= fight.getMaxRound()) {
            this.logger.error("最大回合数=={}", Integer.valueOf(fight.getMaxRound()));
            boolean isReward = false;
            if (fight.isPvp() == 1) {
                fight.setFightOverReason((byte)1);
                isReward = true;
            }
            this.quiteFightAll(fight, isReward);
            this.clearFight(fight);
            return;
        }
        else {
            PassiveSkillUtils.roundStart(fight);
            this.excuteRoundStartPoison(fight);
            PetPassiveSkillUtil.roundStartPassive(fight);
            PassiveSkillUtils.afterPrintCommand(fight);
            this.actionStart(fight, fight.getAllFightObjects());
            Map<Integer, List<Integer>> clearStatusMap = this.excuteStatusReduce(fight);
            PassiveSkillUtils.statusOver(fight, clearStatusMap);
            PassiveSkillUtils.roundEnd(fight);
            boolean isRoundFinishTask = this.sendFightParams(fight, false);
            this.besure(fight);
            if (isRoundFinishTask) {
                this.maxRoundTimer(fight, 180000L);
            }
            fight.setRoundType(3);
            return;
        }
    }
    
    private void actionStart(Fight fight, List<FightObject> list) {
        if (fight.isOver()) {
            return;
        }
        List<FightObject> copyFightObjs = new ArrayList(list);
        Collections.sort(copyFightObjs);
        Iterator<FightObject> it = copyFightObjs.iterator();
        while (it.hasNext()) {
            FightObject next = (FightObject)it.next();
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
            catch (Exception e) {
                e.printStackTrace();
            }
            break;
        }
        if (copyFightObjs.size() > 0) {
            this.actionStart(fight, copyFightObjs);
        }
        return;
    }
    
    private void doActionResult(FightObject attacker, Fight fight) {
        if (attacker.isFallDown()) {
            return;
        }
        if (attacker.isMonster()) {
            this.getMonsterActionType(fight, attacker);
        }
        if (attacker.isPartner() && attacker.getSkillId() == 0 && !attacker.isConfusion()) {
            attacker.setSkillId((int)((PartnerFightObject)attacker).getFightSkillId());
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
            RoleSkill skill = attacker.getSkillBy(Integer.valueOf(attacker.getMorph()));
            SkillProcessor processor = SkillProcessor.queryProcessor(Integer.valueOf(attacker.getMorph()));
            SkillAction skillAction = new SkillAction(fight, attacker, skill, processor);
            skillAction.dragonMorph();
        }
        if (attacker.getStatusValue(1011) > 0 && type != 1) {
            attacker.removeStatus(1011);
            List<Integer> clearStatusList = new ArrayList<>();
            clearStatusList.add(Integer.valueOf(1011));
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
            if ((attacker instanceof MonsterFightObject)) {
                ((FightMonsterNewService)SpringUtils.getBean(FightMonsterNewService.class)).selectMonster(fight, attacker);
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
        return;
    }
    
    private void defense(FightObject fightObject, Fight fight) {
        fightObject.recordAction(fight);
        PassiveSkillUtils.selfSellEnd(fight, fightObject);
        if (this.checkDeStatus(fight, fightObject)) {
            return;
        }
        return;
    }
    
    public void normalAttack(FightObject attacker, Fight fight) {
        attacker.setType(0);
        attacker.setSkillId(0);
        attacker.createFallDownList();
        boolean isConfusion = attacker.isConfusion();
        if (!isConfusion && this.checkDeStatus(fight, attacker)) {
            return;
        }
        if ((int)attacker.getId() == attacker.getTargetId()) {
            attacker.setTargetId(0);
        }
        attacker.recordAction(fight);
        FightObject fightTarget = fight.queryFightObject(Integer.valueOf(attacker.getTargetId()));
        if (fightTarget == null || fightTarget.isFallDown()) {
            fightTarget = this.getPhysicTarget(attacker, fight);
            if (fightTarget == null || fightTarget.isFallDown()) {
                return;
            }
            attacker.setTargetId((int)fightTarget.getId());
            fightTarget = fight.queryFightObject(Integer.valueOf(attacker.getTargetId()));
        }
        List<Integer> fightObjectIds = SkillTargetPicker.getFightObjectIds(fight, attacker, true);
        if (!fightObjectIds.contains(fightTarget.getId())) {
            return;
        }
        if (isConfusion) {
            List<FightObject> arrayList = this.getConfusionFightTargets(attacker, fight);
            if (arrayList.size() <= 0) {
                return;
            }
            Random random = new Random();
            int index = random.nextInt(arrayList.size());
            fightTarget = (FightObject)arrayList.get(index);
            attacker.setTargetId((int)fightTarget.getId());
        }
        if (fightTarget.isFallDown()) {
            return;
        }
        PassiveSkillUtils.beforePhysicalAttack(fight, attacker, fightTarget);
        BattleUnit battleUnit = new BattleUnit(fight, attacker, fightTarget);
        battleUnit.triggerPhysic();
        if (fightTarget.isFallDown()) {
            attacker.addFallDownList((int)fightTarget.getId());
        }
        PassiveSkillUtils.normalAttackAfter(fight, attacker, battleUnit.getTarget(), battleUnit.getActionDetailParam());
        PassiveSkillUtils.phyAttackEnd(fight, attacker, battleUnit, battleUnit.getActionDetailParam());
        fight.getActionParam().getActionDetailParams().add(battleUnit.getActionDetailParam());
        PassiveSkillUtils.sufferHarm(fight, battleUnit.getTarget(), battleUnit.getPhyHurt(), attacker);
        List<FightObject> fallDownObjs = (List)fight.getAllFightObjects().stream().filter(obj -> obj.isFallDown() && obj.isPet()).collect(Collectors.toList());
        for (FightObject target : fallDownObjs) {
            List<Integer> statusList = new ArrayList<>();
            for (int status : StatusConst.getPositiveStatus()) {
                if (target.getStatusValue(status) > 0) {
                    statusList.add(Integer.valueOf(status));
                }
                else {
                    continue;
                }
            }
            PassiveSkillUtils.exitOrClearPositive(fight, attacker, target, statusList);
        }
        this.checkOrderTarget(fight, Collections.singletonList(Integer.valueOf(attacker.getTargetId())));
        if (fightTarget.isFallDown()) {
            this.checkGuessSuccess(fight);
            fight.onActorDie(fightTarget);
        }
        if (attacker.isFallDown()) {
            fight.onActorDie(attacker);
        }
        PassiveSkillUtils.selfSellEnd(fight, attacker);
        return;
    }
    
    private void magicAttack(FightObject attacker, Fight fight) {
        if (this.checkDeStatus(fight, attacker)) {
            return;
        }
        attacker.recordAction(fight);
        attacker.createFallDownList();
        RoleSkill skill = attacker.getSkillBy(Integer.valueOf(attacker.getSkillId()));
        if (skill != null) {
            Integer targetsCount = Integer.valueOf(0);
            if (LOGIC.LOGICTYPE_PET.getValue() == attacker.getLogic() || attacker.isMonster()) {
                boolean flag = PetTriggerPassiveSkill.checkSkillIsUsable(fight, attacker, skill.getSkillId(), PET_PASSIVE_SKILL_TRIGGER_DICT.INITIATIVE_SKILL.getValue());
                if (!flag) {
                    if (attacker.isMonster()) {
                        this.setNormalAttack(fight, attacker);
                    }
                    return;
                }
            }
            SkillProcessor processor = SkillProcessor.queryProcessor(skill.getSkillId());
            if (processor == null) {
                this.getSkillFail(fight, attacker, String.format("获取技能信息失败，未找到技能：%d", skill.getSkillId()));
                return;
            }
            if (LOGIC.LOGICTYPE_PET.getValue() == attacker.getLogic() || attacker.isMonster()) {
                boolean flag2 = processor.isExcute(fight, attacker, new Object[0]);
                if (!flag2) {
                    if (attacker.isMonster()) {
                        this.setNormalAttack(fight, attacker);
                    }
                    return;
                }
            }
            if (processor != null) {
                targetsCount = Integer.valueOf(processor.targetCount(fight, attacker, new Object[0]));
                SkillAction skillAction = new SkillAction(fight, attacker, skill, processor);
                List<Integer> targetList = new ArrayList<>();
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
                FightObject mainTarget = fight.queryFightObject(Integer.valueOf(attacker.getTargetId()));
                if (mainTarget == null || mainTarget.isFallDown() || mainTarget.isASide() == attacker.isASide()) {
                    mainTarget = fight.queryFightObject((Integer)targetList.get(0));
                    attacker.setTargetId((int)mainTarget.getId());
                }
                int life = processor.costLife(attacker);
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
                    boolean excuteSkillCool = processor.excuteSkillCool(fight, attacker, new Object[0]);
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
                        ActionDetailParam actionDetailParam = ActionParamUtils.createActionDetailParam((int)attacker.getId(), (int)attacker.getId(), 7, attacker.getSkillId());
                        FightObjectParam attackerParam = actionDetailParam.gainAttackerParam();
                        RespFightShouting fightShouting = ((FightMessageNewService)SpringUtils.getBean(FightMessageNewService.class)).fightShouting(attacker, "法攻");
                        ActionParamUtils.updateObjectInfo(attacker, 7, attacker.getSkillId(), 2, fightShouting, attackerParam);
                        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
                        return;
                    }
                    if (attacker.isFallDown()) {
                        return;
                    }
                    skillAction.doMagicEffect();
                    ActionDetailParam actionDetailParam = skillAction.fetchActionDetail();
                    PassiveSkillUtils.magicAttackEnd(fight, attacker, skillAction.getMagicTargets(), skill, actionDetailParam);
                    PassiveSkillUtils.friendMagic(fight, attacker, actionDetailParam);
                    if (skillAction.getActionDetailParams().size() > 0) {
                        fight.getActionParam().getActionDetailParams().addAll(skillAction.getActionDetailParams());
                    }
                    PassiveSkillUtils.excutePassiveSkill(fight, attacker, PET_PASSIVE_SKILL_TRIGGER_DICT.SELF_SELL_END.getValue(), new Object[0]);
                    PassiveSkillUtils.magicAttackEndOther(fight, attacker, skill, skillAction.getMagicTargets());
                    for (MagicAttackParam magicParam : skillAction.getMagicTargets()) {
                        FightObject target = magicParam.getTarget();
                        List<Integer> statusList = magicParam.getPositiveStatus();
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
                this.getSkillFail(fight, attacker, String.format("获取技能信息失败，未找到技能：%d", skill.getSkillId()));
                return;
            }
        }
        else {
            this.getSkillFail(fight, attacker, "获取技能信息失败，objId:" + attacker.getId() + ",skillId:" + attacker.getSkillId());
        }
        return;
    }
    
    public int getHostId(int petId) {
        Integer integer = (Integer)FightExecutorNewService.PETID_ROLEID.get(Integer.valueOf(petId));
        return (integer == null) ? 0 : ((int)integer);
    }
    
    public Fight getFightByRoleId(int roleId) {
        Integer fightId = (Integer)FightExecutorNewService.FIGHT_ROOMS.get(Integer.valueOf(roleId));
        if (fightId == null) {
            return null;
        }
        return this.getFight((int)fightId);
    }
    
    public Fight getFight(int fightId) {
        return (Fight)FightExecutorNewService.FIGHTS.get(Integer.valueOf(fightId));
    }
    
    public void resetDoActionStatus(Fight fight, ReqFightHandle reqFightHandle) {
        int hostId = this.getHostId(reqFightHandle.getAttackerId());
        FightObject hostObj = fight.queryFightObject(Integer.valueOf(hostId));
        if (hostObj != null) {
            hostObj.setDoAction(false);
        }
    }
    
    private void updateDoActionStatus(Fight fight, int attackerId, int targetId, int actionType, int skillId) {
        FightObject attacker = fight.queryFightObject(Integer.valueOf(attackerId));
        if (attacker != null) {
            attacker.setSkillId(skillId);
            attacker.setTargetId(targetId);
            attacker.setType(actionType);
            attacker.setDoAction(true);
            if (attacker.isPlayer()) {
                List<FightObject> partners = (List)fight.getMySide((int)attacker.getId()).stream().filter(obj -> obj.isPartner()).collect(Collectors.toList());
                if (partners != null && partners.size() > 0) {
                    for (FightObject fightObject : partners) {
                        if (attacker.getId().equals(Integer.valueOf(fightObject.getHostId()))) {
                            fightObject.setDoAction(true);
                            fightObject.setType(7);
                        }
                        else {
                            continue;
                        }
                    }
                }
            }
        }
    }
    
    private boolean canUseLifeItem(Fight fight) {
        return fight.getType() == FIGHT_TYPE.STAR.getValue() || fight.getType() == FIGHT_TYPE.SUPER_BOSS.getValue();
    }
    
    public void besure(final Fight fight) {
        final int fightId = fight.getId();
        final int currRound = fight.getRound();
        this.serverService.getScheduledExecutorService().schedule(new Runnable() {
            @Override
            public void run() {
                Fight temp = FightNewService.this.getFight(fightId);
                if (temp != null && currRound == temp.getRound()) {
                    FightNewService.this.fightExecutorService.addFightTask(temp, new FightingTask() {
                        @Override
                        public void run() {
                            try {
                                FightNewService.this.quiteFightAll(fight, false);
                                FightNewService.this.clearFight(fight);
                            }
                            catch (Exception e) {
                                LoggerFunction.FIGHT.getLogger().error("timer定时器结束战斗==报错", e);
                            }
                        }
                    });
                }
                else {
                    int val$currRound = currRound;
                    temp.getRound();
                }
            }
        }, 180L, TimeUnit.SECONDS);
    }
    
    public void quiteFightAll(Fight fight, boolean isReward) {
        for (FightObject fightObject : fight.getAllFightObjects()) {
            this.quiteFight(fight, fightObject, isReward);
        }
    }
    
    private void excuteSubstitutionQuite(Fight fight, boolean isReward) {
        Map<Boolean, List<Role>> rolesMap = FightUtils.substitutionQuiteFight(fight);
        if (rolesMap != null) {
            List<Role> rolesA = (List)rolesMap.get(Boolean.valueOf(true));
            List<Role> rolesB = (List)rolesMap.get(Boolean.valueOf(true));
            this.excuteSubstitutionQuite(fight, rolesA, isReward, rolesB, true);
            this.excuteSubstitutionQuite(fight, rolesB, isReward, rolesA, false);
        }
    }
    
    private void excuteSubstitutionQuite(Fight fight, List<Role> roles, boolean isReward, List<Role> rolesOther, boolean isA) {
        if (roles != null && roles.size() > 0) {
            for (Role role : roles) {
                if (isA) {
                    this.quiteFightSubstitution(fight, role, isReward, roles, rolesOther, isA);
                }
                else {
                    this.quiteFightSubstitution(fight, role, isReward, rolesOther, roles, isA);
                }
            }
        }
    }
    
    public void quiteFight(Fight fight, FightObject fightObject, boolean isReward) {
        if (!fightObject.isPlayer()) {
            return;
        }
        Role role = this.roleService.getPlayer((int)fightObject.getId());
        role.setPrevFightEndTime(System.currentTimeMillis());
        role.setCanFight(Boolean.valueOf(true));
        Integer fightId = (Integer)FightExecutorNewService.FIGHT_ROOMS.get(fightObject.getId());
        if (fightId != null && fight.getId() == (int)fightId) {
            FightExecutorNewService.FIGHT_ROOMS.remove(Integer.valueOf(fightObject.getFightPetId()));
            this.deleteRoleFightData(fight, fightObject.getId());
        }
        ThreadLocalUtil.addLocalTask(role, () -> this.playerQuitFight(fight, fightObject, isReward, role));
        return;
    }
    
    public void quiteFightSubstitution(Fight fight, Role role, boolean isReward, List<Role> rolesA, List<Role> rolesB, boolean isA) {
        ThreadLocalUtil.addLocalTask(role, () -> this.playerQuitFightSubstitution(fight, role, isReward, rolesA, rolesB, isA));
    }
    
    public void clearFight(Fight fight) {
        if (fight == null) {
            return;
        }
        fight.destroy();
        return;
    }
    
    public void deleteRoleFightData(Fight fight, Integer roleId) {
        List<Integer> petIds = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : FightExecutorNewService.PETID_ROLEID.entrySet()) {
            if (((Integer)entry.getValue()).equals(roleId)) {
                petIds.add(entry.getKey());
            }
            else {
                continue;
            }
        }
        for (Integer petId : petIds) {
            FightExecutorNewService.PETID_ROLEID.remove(petId);
        }
        fight.clearAstrictSkills();
        Integer fightId = (Integer)FightExecutorNewService.FIGHT_ROOMS.get(roleId);
        if (fightId.equals(Integer.valueOf(fight.getId()))) {
            FightExecutorNewService.FIGHT_ROOMS.remove(roleId);
        }
        Team team = this.teamService.getTeam((int)roleId);
        if (team != null) {
            List<Member> members = (List)team.getList().stream().filter(member -> member.isPartner() && member.getRoleId().equals(roleId)).collect(Collectors.toList());
            for (int i = 0; i < members.size(); ++i) {
            	Member member = (Member)members.get(i);
                if (member.getRoleId().equals(roleId)) {
                    team.getList().remove(member);
                }
            }
        }
    }
    
    private void playerQuitFight(Fight fight, FightObject fightObject, boolean isReward, Role role) {
        try {
            FightObject currPetObject = fight.queryFightObject(Integer.valueOf(fightObject.getFightPetId()));
            Pet currPet = null;
            if (currPetObject != null) {
                currPet = currPetObject.getPet();
            }
            else {
                currPet = (Pet)this.petService.selectPet(role).get(role.getExtendBox().getCombatPet());
            }
            role.pushTempCache("fight_current_pet_id", Long.valueOf((currPet != null) ? ((long)currPet.getId()) : 0L));
            role.setLastDead((fightObject.getCurrLife() <= 0) ? 0 : 1);
            if (isReward) {
                this.sendReward(fight, role, currPet);
            }
            this.fireEndEvent(fight, role, currPet);
            this.resetAfterLeaveFight(fight, fightObject);
        }
        catch (Exception e) {
            e.printStackTrace();
            this.logger.error("退出战斗报错了,战场ID=[{}],角色名=[{}],错误信息[{}]", new Object[] { Integer.valueOf(fight.getId()), fightObject.getName(), e.getMessage() });
        }
        finally {
            Integer fightId = (Integer)FightExecutorNewService.FIGHT_ROOMS.get(role.getRoleId());
            if (fightId == null) {
                RespEndCombat respEndCombat = new RespEndCombat();
                MessagePusher.pushMessage(role, respEndCombat);
            }
            this.broadcastService.sendUpdateAppear(role);
            this.broadcastService.sendTitleInfoNew(role);
        }
    }
    
    private void playerQuitFightSubstitution(Fight fight, Role role, boolean isReward, List<Role> rolesA, List<Role> rolesB, boolean isA) {
        try {
            Pet currPet = (Pet)this.petService.selectPet(role).get(role.getExtendBox().getCombatPet());
            role.pushTempCache("fight_current_pet_id", Long.valueOf((currPet != null) ? ((long)currPet.getId()) : 0L));
            if (isReward) {
                this.sendReward(fight, role, currPet);
            }
            this.fireEndEventSubstitution(fight, role, currPet, rolesA, rolesB, isA);
        }
        catch (Exception e) {
            e.printStackTrace();
            this.logger.error("退出战斗报错了,战场ID=[{}],角色id=[{}],错误信息[{}]", new Object[] { Integer.valueOf(fight.getId()), role.getId(), e.getMessage() });
        }
        finally {
            Integer fightId = (Integer)FightExecutorNewService.FIGHT_ROOMS.get(role.getRoleId());
            if (fightId == null) {
                RespEndCombat respEndCombat = new RespEndCombat();
                MessagePusher.pushMessage(role, respEndCombat);
            }
            this.broadcastService.sendUpdateAppear(role);
            this.broadcastService.sendTitleInfoNew(role);
        }
    }
    
    private void sendReward(Fight fight, Role role, Pet pet) {
        try {
            FightRewardHandler handler = FightRewardHandler.queryHandler(fight.getRewardType());
            if (fight.isWinner((int)role.getRoleId())) {
                RespNotifyMiscEx respNotifyMiscEx = new RespNotifyMiscEx(role.getId(), "战斗胜利", false);
                MessagePusher.pushMessage(role, respNotifyMiscEx);
                if (handler != null) {
                    handler.sendReward(fight, role, pet);
                }
            }
            else {
                RespNotifyMiscEx respNotifyMiscEx = new RespNotifyMiscEx(role.getId(), "战斗失败", false);
                MessagePusher.pushMessage(role, respNotifyMiscEx);
                if (handler != null) {
                    handler.fightFailed(fight, role);
                }
            }
        }
        catch (Exception e) {
            LoggerUtils.error(e.getMessage(), e);
            this.logger.error("退出战斗战斗结算时报错了,战场ID=[{}],角色名=[{}],错误信息[{}]", new Object[] { Integer.valueOf(fight.getId()), role.getName(), Lang.getStackTrace(e) });
        }
    }
    
    private void fireEndEvent(Fight fight, Role role, Pet pet) {
        try {
            if (fight.getData() != null && fight.getData().getBoolean("fight_result_fail", false)) {
                EventDispatcher.getInstance().fireEvent(new FightEndEvent(fight, EventType.FIGHT_END, role, fight.getId(), (byte)2, fight.getType(), fight.getBossReward(), fight.getTaskId(), fight.getNpcId(), 0));
                return;
            }
            if (fight.getData() != null && fight.getData().getBoolean("fight_result_success", false)) {
                EventDispatcher.getInstance().fireEvent(new FightEndEvent(fight, EventType.FIGHT_END, role, fight.getId(), (byte)1, fight.getType(), fight.getBossReward(), fight.getTaskId(), fight.getNpcId(), (pet != null) ? ((int)pet.getPetId()) : 0));
                return;
            }
            if (fight.getRound() >= fight.getMaxRound()) {
                EventDispatcher.getInstance().fireEvent(new FightEndEvent(fight, EventType.FIGHT_END, role, fight.getId(), (byte)40, fight.getType(), fight.getBossReward(), fight.getTaskId(), fight.getNpcId(), 0));
            }
            else if (fight.isWinner((int)role.getRoleId())) {
                EventDispatcher.getInstance().fireEvent(new FightEndEvent(fight, EventType.FIGHT_END, role, fight.getId(), (byte)1, fight.getType(), fight.getBossReward(), fight.getTaskId(), fight.getNpcId(), (pet != null) ? ((int)pet.getPetId()) : 0));
            }
            else {
                EventDispatcher.getInstance().fireEvent(new FightEndEvent(fight, EventType.FIGHT_END, role, fight.getId(), (byte)2, fight.getType(), fight.getBossReward(), fight.getTaskId(), fight.getNpcId(), 0));
            }
        }
        catch (Exception e) {
            LoggerUtils.error(e.getMessage(), e);
            this.logger.error("退出战斗产生结束事件时报错了,战场ID=[{}],角色名=[{}],错误信息[{}]", new Object[] { Integer.valueOf(fight.getId()), role.getName(), e });
        }
    }
    
    private void fireEndEventSubstitution(Fight fight, Role role, Pet pet, List<Role> rolesA, List<Role> rolesB, boolean isA) {
        try {
            if (fight.getData() != null && fight.getData().getBoolean("fight_result_fail", false)) {
                EventDispatcher.getInstance().fireEvent(new FightEndEvent(fight, EventType.FIGHT_END, role, (byte)2, 0, rolesA, rolesB));
                return;
            }
            if (fight.getData() != null && fight.getData().getBoolean("fight_result_success", false)) {
                EventDispatcher.getInstance().fireEvent(new FightEndEvent(fight, EventType.FIGHT_END, role, (byte)1, (pet != null) ? ((int)pet.getPetId()) : 0, rolesA, rolesB));
                return;
            }
            if (fight.getRound() >= fight.getMaxRound()) {
                EventDispatcher.getInstance().fireEvent(new FightEndEvent(fight, EventType.FIGHT_END, role, (byte)40, 0, rolesA, rolesB));
            }
            else if (fight.isWinner((int)role.getRoleId(), isA)) {
                EventDispatcher.getInstance().fireEvent(new FightEndEvent(fight, EventType.FIGHT_END, role, (byte)1, (pet != null) ? ((int)pet.getPetId()) : 0, rolesA, rolesB));
            }
            else {
                EventDispatcher.getInstance().fireEvent(new FightEndEvent(fight, EventType.FIGHT_END, role, (byte)2, 0, rolesA, rolesB));
            }
        }
        catch (Exception e) {
            LoggerUtils.error(e.getMessage(), e);
            this.logger.error("退出战斗产生结束事件时报错了,战场ID=[{}],角色名=[{}],错误信息[{}]", new Object[] { Integer.valueOf(fight.getId()), role.getName(), e });
        }
    }
    
    private void resetAfterLeaveFight(Fight fight, FightObject fightObject) {
        Role role = this.roleService.getPlayer((int)fightObject.getId());
        int lastLife = fightObject.getCurrLife();
        int lastMana = fightObject.getCurrMana();
        int maxLife = (int)role.getMaxLife();
        int maxMana = (int)role.getMaxMana();
        int needLife = maxLife - lastLife;
        long saveLife = (long)role.getSaveLife();
        int needMana = maxMana - lastMana;
        long saveMna = (long)role.getSaveMana();
        if (saveLife >= 1L) {
            if (saveLife >= (long)needLife) {
                lastLife = maxLife;
                saveLife -= (long)needLife;
            }
            else {
                lastLife = (int)((long)lastLife + saveLife);
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
            if (saveMna >= (long)needMana) {
                lastMana = maxMana;
                saveMna -= (long)needMana;
            }
            else {
                lastMana = (int)((long)lastMana + saveMna);
                saveMna = 0L;
            }
        }
        else {
            if (lastMana <= 0) {
                lastMana = 1;
            }
            saveMna = 0L;
        }
        role.setCurrLife(Integer.valueOf(lastLife));
        role.setCurrMana(Integer.valueOf(lastMana));
        Map<String, Object> autoSet = role.getExtendBox().getAutoFightSet();
        autoSet.put("type", Integer.valueOf(fightObject.getAutoFightType()));
        autoSet.put("skillId", Integer.valueOf(fightObject.getAutoSkillId()));
        autoSet.put("action", Integer.valueOf(fightObject.getAction()));
        autoSet.put("target", Integer.valueOf(fightObject.getAutoTarget()));
        int born = 0;
        if ((int)role.getRace() == RACE.RACE_LONG.getValue()) {
            born = fightObject.getBorn();
        }
        autoSet.put("born", Integer.valueOf(born));
        for (Integer petId : fightObject.getYepFightPet()) {
            Pet pet = (Pet)this.petService.selectPet(role).get(petId);
            if (pet != null) {
                lastLife = pet.getCurrLife();
                lastMana = (int)pet.getCurrMana();
                maxLife = (int)pet.getMaxLife();
                maxMana = (int)pet.getMaxMana();
                needLife = maxLife - lastLife;
                needMana = maxMana - lastMana;
                if (saveLife >= 1L) {
                    if (saveLife >= (long)needLife) {
                        lastLife = maxLife;
                        saveLife -= (long)needLife;
                    }
                    else {
                        lastLife = (int)((long)lastLife + saveLife);
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
                    if (saveMna >= (long)needMana) {
                        lastMana = maxMana;
                        saveMna -= (long)needMana;
                    }
                    else {
                        lastMana = (int)((long)lastMana + saveMna);
                        saveMna = 0L;
                    }
                }
                else {
                    if (lastMana <= 0) {
                        lastMana = 1;
                    }
                    saveMna = 0L;
                }
                pet.setCurrMana(Integer.valueOf(lastMana));
                pet.setCurrLife(lastLife);
                FightObject petObject = fight.queryFightObject(petId);
                if (petObject != null) {
                    autoSet = NutMap.NEW();
                    autoSet.put("type", Integer.valueOf(petObject.getAutoFightType()));
                    autoSet.put("skillId", Integer.valueOf(petObject.getAutoSkillId()));
                    autoSet.put("action", Integer.valueOf(petObject.getAction()));
                    autoSet.put("target", Integer.valueOf(petObject.getAutoTarget()));
                    pet.setAutoFightSet(autoSet);
                }
                boolean once = fight.petDeadOnce((int)petId);
                this.petService.refreshPetClose(role, pet, once, fight.getType());
            }
            else {
                continue;
            }
        }
        role.setSaveMana(Long.valueOf(saveMna));
        role.setSaveLife(Long.valueOf(saveLife));
        this.roleService.checkReserve(role);
        this.roleService.refreshRoleInfo(role, true);
        role.save();
    }
    
    public void fullLifeRoleAndPet(Fight fight, Role role) {
        fight.getRoleFightInParam(role);
        this.fullHpAndMpOfRole(role);
        Integer combatPet = role.getExtendBox().getCombatPet();
        if ((int)combatPet >= 1) {
            Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
            if (pet != null) {
                fight.addPetFightInParam(role, pet);
                this.fullHpAndMpOfPet(role, pet);
            }
        }
        role.save();
    }
    
    private LifeManaParam fullHpAndMpOfRole(Role role) {
        LifeManaParam lifeManaParam = new LifeManaParam();
        int maxLife = (int)role.getMaxLife();
        int maxMana = (int)role.getMaxMana();
        if ((long)role.getSaveLife() >= (long)(maxLife - (int)role.getCurrLife())) {
            role.setCurrLife(Integer.valueOf(maxLife));
            role.setSaveLife(Long.valueOf((long)role.getSaveLife() - (long)(maxLife - (int)role.getCurrLife())));
        }
        else {
            role.setCurrLife(Integer.valueOf((int)role.getCurrLife() + role.getSaveLife().intValue()));
            role.setSaveLife(Long.valueOf(0L));
        }
        lifeManaParam.setCurrLife((int)role.getCurrLife());
        lifeManaParam.setSaveLife(String.valueOf(role.getSaveLife()));
        if ((long)role.getSaveMana() >= (long)(maxMana - (int)role.getCurrMana())) {
            role.setSaveMana(Long.valueOf((long)role.getSaveMana() - (long)(maxMana - (int)role.getCurrMana())));
            role.setCurrMana(Integer.valueOf(maxMana));
        }
        else {
            role.setCurrMana(Integer.valueOf((int)role.getCurrMana() + role.getSaveMana().intValue()));
            role.setSaveMana(Long.valueOf(0L));
        }
        lifeManaParam.setCurrMana((int)role.getCurrMana());
        lifeManaParam.setSaveMana(String.valueOf(role.getSaveMana()));
        return lifeManaParam;
    }
    
    public LifeManaParam fullHpAndMpOfPet(Role role, Pet pet) {
        if (pet == null) {
            return null;
        }
        LifeManaParam lifeManaParam = new LifeManaParam();
        if ((long)role.getSaveLife() >= (long)((int)pet.getMaxLife() - pet.getCurrLife())) {
            role.setSaveLife(Long.valueOf((long)role.getSaveLife() - (long)((int)pet.getMaxLife() - pet.getCurrLife())));
            pet.setCurrLife((int)pet.getMaxLife());
        }
        else {
            pet.setCurrLife((int)role.getCurrLife() + role.getSaveLife().intValue());
            role.setSaveLife(Long.valueOf(0L));
        }
        lifeManaParam.setCurrLife(pet.getCurrLife());
        lifeManaParam.setSaveLife(String.valueOf(role.getSaveLife()));
        if ((long)role.getSaveMana() >= (long)((int)pet.getMaxMana() - (int)pet.getCurrMana())) {
            role.setSaveMana(Long.valueOf((long)role.getSaveMana() - (long)((int)pet.getMaxMana() - (int)pet.getCurrMana())));
            pet.setCurrMana(pet.getMaxMana());
        }
        else {
            pet.setCurrMana(Integer.valueOf((int)pet.getCurrMana() + role.getSaveMana().intValue()));
            role.setSaveMana(Long.valueOf(0L));
        }
        lifeManaParam.setCurrMana((int)pet.getCurrMana());
        lifeManaParam.setSaveMana(String.valueOf(role.getSaveMana()));
        return lifeManaParam;
    }
    
    public void checkAtFightStart(Role role) {
        if ((int)role.getCurrLife() <= 0) {
            role.setCurrLife(Integer.valueOf(1));
        }
    }
    
    public void sendStartCombat(Fight fight, RespCombatFriends respCombatFriends, RespCombatOpponets respCombatOpponets, List<Member> members) {
        if (fight != null) {
            for (int i = 0; i < members.size(); ++i) {
                Member member = (Member)members.get(i);
                if (member.isInTeam() && !member.isPartner()) {
                    Role tempRole = this.roleService.getPlayer((long)member.getId());
                    if (tempRole == null) {
                        this.logger.error("战斗开始未找到role=={}", member.getId());
                    }
                    else {
                        RespFightInParam fightInParam = fight.getRoleFightInParam(tempRole);
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
    
    public byte getTitleInfo(Role role) {
        Team team = this.teamService.getTeam((int)role.getRoleId());
        Fight fight = this.getFightByRoleId((int)role.getRoleId());
        String temp = "";
        if (team != null) {
            if (team.isLeader((int)role.getRoleId())) {
                if (team.getList().size() == 5) {
                    temp += "10";
                }
                else {
                    temp += "01";
                }
            }
            else {
                int i = 0;
                while (i < team.getList().size()) {
                    Member member = (Member)team.getList().get(i);
                    if (member.getRoleId().equals(role.getRoleId()) && member.isInTeam()) {
                        temp += "00";
                        break;
                    }
                    ++i;
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
        if (role.getPkInfoBox().isRedName() && !role.getPos().getMapId().equals(Integer.valueOf(1201))) {
            temp += "1";
        }
        else {
            temp += "0";
        }
        byte test = Byte.parseByte(temp, 2);
        return test;
    }
    
    public SkillAutoPicker getAutoObject(FightObject attacker, Fight fight) {
        SkillAutoPicker useResult = new SkillAutoPicker();
        int autoSkillId = 0;
        try {
            autoSkillId = attacker.getAutoSkillId();
            int autoType = attacker.getAutoFightType();
            useResult.setType(autoType);
            if (autoType == 5) {
                SysDrug drug = (SysDrug)ItemDataPool.data_drug.get(Integer.valueOf(autoSkillId));
                if (drug == null) {
                    useResult.setDef(true).setSkillId(autoSkillId).setTargetId((int)attacker.getId());
                    return useResult;
                }
                useResult.setSkillId(autoSkillId);
                if (attacker.getAutoTarget() > 0) {
                    FightObject target = fight.queryFightObject(Integer.valueOf(attacker.getAutoTarget()));
                    if (target != null) {
                        useResult.setTargetId((int)target.getId());
                        return useResult;
                    }
                }
                FightObject fightObject = null;
                if ((int)drug.getTargetType() == TARGETTYPE.ALL_OURS.getValue()) {
                    if ((int)drug.getShowClass() == 4) {
                        fightObject = SkillAi.certainStatusOrPro(fight, drug.getStatus(), attacker.isASide(), 4);
                    }
                    else {
                        fightObject = SkillAi.effectProBass(fight, attacker, drug.getShowClass(), Arrays.asList(new String[0]), false);
                    }
                    if (fightObject != null) {
                        useResult.setTargetId((int)fightObject.getId());
                        return useResult;
                    }
                    return useResult;
                }
                else if ((int)drug.getTargetType() == TARGETTYPE.ALL_ENEMY.getValue()) {
                    FightObject unit = SkillAi.certainStatusOrPro(fight, drug.getStatus(), !attacker.isASide(), 4);
                    if (unit != null) {
                        useResult.setSkillId(autoSkillId).setTargetId((int)unit.getId());
                        return useResult;
                    }
                    return useResult;
                }
            }
            else {
                if (autoType == 1) {
                    useResult.setDef(true).setSkillId(autoSkillId).setTargetId((int)attacker.getId());
                    return useResult;
                }
                if (autoType == 0) {
                    FightObject randTarget2 = SkillTargetPicker.getPhysicTarget(attacker, fight);
                    if (randTarget2 != null) {
                        return useResult.setSkillId(autoSkillId).setTargetId((int)randTarget2.getId());
                    }
                    return useResult;
                }
                else {
                    RoleSkill roleSkill = attacker.getSkillBy(Integer.valueOf(autoSkillId));
                    int autoTargetId = attacker.getAutoTarget();
                    FightObject autoTarget = fight.queryFightObject(Integer.valueOf(autoTargetId));
                    if (roleSkill == null) {
                        if (autoTarget != null && !autoTarget.isFallDown() && autoTarget.isASide() != attacker.isASide()) {
                            return useResult.setSkillId(autoSkillId).setTargetId((int)autoTarget.getId());
                        }
                        FightObject randTarget = SkillTargetPicker.getPhysicTarget(attacker, fight);
                        if (randTarget != null) {
                            return useResult.setSkillId(autoSkillId).setTargetId((int)randTarget.getId());
                        }
                        return useResult;
                    }
                    else {
                        int skillTargetSum = (int)SkillFunc.getSkillTargeNum(roleSkill.getSkillId(), roleSkill.getProficient(), Integer.valueOf(attacker.getSerial()));
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
                                return useResult.setSkillId(autoSkillId).setTargetId((int)autoTarget.getId());
                            }
                            getUseResult(fight, attacker, roleSkill, useResult, autoSkillId, skillTargetSum);
                            return useResult;
                        }
                        else {
                            if (autoTarget.isASide() != attacker.isASide()) {
                                return useResult.setSkillId(autoSkillId).setTargetId((int)autoTarget.getId());
                            }
                            getUseResult(fight, attacker, roleSkill, useResult, autoSkillId, skillTargetSum);
                            return useResult;
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            LoggerUtils.error(e.getMessage(), e);
        }
        return useResult;
    }
    
    public static void getUseResult(Fight fight, FightObject attacker, RoleSkill roleSkill, SkillAutoPicker useResult, int skillId, int skillTargetSum) {
        attacker.setTargetId(0);
        attacker.setSkillId((int)roleSkill.getSkillId());
        List<Integer> targetListNew = SkillTargetPicker.getTargetListNew(attacker, skillTargetSum, fight);
        if (targetListNew != null && targetListNew.size() > 0) {
            int tempRoleId = (int)targetListNew.get(0);
            useResult.setSkillId(skillId).setTargetId(tempRoleId);
            return;
        }
        attacker.setTargetId(0);
        attacker.setSkillId((int)roleSkill.getSkillId());
        int tempRoleId = 0;
        useResult.setSkillId(skillId).setTargetId(tempRoleId);
        return;
    }
    
    public boolean isSameTeam(FightObject attacker, FightObject target) {
        return attacker.isASide() == target.isASide();
    }
    
    public void autoFightOne(Fight fight, FightObject attacker) {
        this.autoFightOne(fight, attacker, 2500L);
    }
    
    public void autoFightOne(Fight fight, FightObject attacker, long delay) {
        this.fightExecutorService.addFightTask(fight, () -> {
            try {
                if (fight.isOver()) {
                    return;
                }
                if (attacker.getAutoFight() == 1) {
                    ReqFightHandle reqFightHandle = this.fightMessageNewService.autoFightOne(fight, attacker);
                    if (reqFightHandle == null) {
                        this.logger.error("fight[{}], type[{}],{}出手动作为空", new Object[] { Integer.valueOf(fight.getId()), Integer.valueOf(fight.getType()), attacker });
                        return;
                    }
                    AutoChuShouNewTask autoTask = new AutoChuShouNewTask(reqFightHandle, fight);
                    ScheduledFuture<?> timeOut = SchedulerManager.getInstance().schedule(autoTask, delay);
                    fight.addHostTask(reqFightHandle.getAttackerId(), timeOut);
                    if (attacker.getFightPetId() > 0) {
                        FightObject petObject = fight.queryFightObject(Integer.valueOf(attacker.getFightPetId()));
                        if (petObject != null) {
                            ReqFightHandle reqFightHandle2 = this.fightMessageNewService.autoFightOne(fight, petObject);
                            if (reqFightHandle2 == null) {
                                this.logger.error("fight[{}], type[{}],{}出手动作为空", new Object[] { Integer.valueOf(fight.getId()), Integer.valueOf(fight.getType()), attacker });
                                return;
                            }
                            AutoChuShouNewTask petAutoTask = new AutoChuShouNewTask(reqFightHandle2, fight);
                            ScheduledFuture<?> petTimeOut = SchedulerManager.getInstance().schedule(petAutoTask, delay + 20L);
                            fight.addPetTask(reqFightHandle2.getAttackerId(), petTimeOut);
                        }
                    }
                }
            }
            catch (Exception e) {
                this.logger.error("fight[{}], type[{}],自动战斗出错{}", new Object[] { Integer.valueOf(fight.getId()), Integer.valueOf(fight.getType()), e });
                ReqFightHandle reqFightHandle3 = new ReqFightHandle();
                reqFightHandle3.setRound(fight.getRound());
                reqFightHandle3.setAutoFight(1);
                reqFightHandle3.setAttackerId((int)attacker.getId());
                reqFightHandle3.setTargetId((int)attacker.getId());
                reqFightHandle3.setSkillId(0);
                reqFightHandle3.setType(1);
                AutoChuShouNewTask autoTask2 = new AutoChuShouNewTask(reqFightHandle3, fight);
                ScheduledFuture<?> timeOut2 = SchedulerManager.getInstance().schedule(autoTask2, delay);
                fight.addHostTask(reqFightHandle3.getAttackerId(), timeOut2);
                if (attacker.getFightPetId() > 0) {
                    FightObject petObject2 = fight.queryFightObject(Integer.valueOf(attacker.getFightPetId()));
                    if (petObject2 != null) {
                        ReqFightHandle reqFightHandle4 = new ReqFightHandle();
                        reqFightHandle4.setAutoFight(1);
                        reqFightHandle4.setRound(fight.getRound());
                        reqFightHandle4.setAttackerId((int)petObject2.getId());
                        reqFightHandle4.setTargetId((int)petObject2.getId());
                        reqFightHandle4.setSkillId(0);
                        reqFightHandle4.setType(1);
                        AutoChuShouNewTask petAutoTask2 = new AutoChuShouNewTask(reqFightHandle4, fight);
                        ScheduledFuture<?> petTimeOut2 = SchedulerManager.getInstance().schedule(petAutoTask2, delay + 20L);
                        fight.addPetTask(reqFightHandle4.getAttackerId(), petTimeOut2);
                    }
                }
            }
            return;
        });
    }
    
    public List<Member> loadTeamBattleInfo(Team team, List<FightMember> fightMemberA, Fight battleground) {
        List<FightObject> memberListA = battleground.getListA();
        battleground.setListA(memberListA);
        FightExecutorNewService.FIGHTS.put(Integer.valueOf(battleground.getId()), battleground);
        List<Member> list = team.getList();
        int index = 1;
        int petIndex = 5;
        int customIndex = 0;
        int playerNum = 0;
        for (int i = list.size() - 1; i >= 0; --i) {
            Member member = (Member)list.get(i);
            if (member.isInTeam() && !member.isPartner()) {
                Role tmpRole = this.roleService.getPlayer((long)member.getId());
                FightExecutorNewService.FIGHT_ROOMS.put(member.getRoleId(), Integer.valueOf(battleground.getId()));
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
                FightMember fightMember = fightObject.asFightMember();
                fightMemberA.add(fightMember);
                if (i != 0) {
                    ++index;
                }
                ++playerNum;
                ++customIndex;
                Pet pet = (Pet)this.petService.selectPet(tmpRole).get(tmpRole.getExtendBox().getCombatPet());
                if (pet != null) {
                    ++customIndex;
                    PetFightObject petFightObj = new PetFightObject(pet, fightObject);
                    memberListA.add(petFightObj);
                    FightMember petFightMember = petFightObj.asFightMember();
                    fightMemberA.add(petFightMember);
                    FightExecutorNewService.PETID_ROLEID.put(pet.getPetId(), member.getRoleId());
                    FightExecutorNewService.FIGHT_ROOMS.put(pet.getPetId(), Integer.valueOf(battleground.getId()));
                }
                ++petIndex;
            }
        }
        if (!Const.not_partner.contains(Integer.valueOf(battleground.getType())) && playerNum < 5 && team.getPartner() != null && team.getPartner().size() > 0) {
            int residueNum = 5 - playerNum;
            for (Member member2 : team.getPartner()) {
                if (residueNum <= 0) {
                    break;
                }
                if (!member2.isInTeam()) {
                    continue;
                }
                Role teamRole = this.roleService.getPlayer((int)member2.getRoleId());
                if (teamRole == null) {
                    continue;
                }
                Map<Integer, Partner> partnerMap = (Map)DataCache.PARTNER_MAP.get(teamRole.getId());
                if (partnerMap == null) {
                    continue;
                }
                if (partnerMap.size() <= 0) {
                    continue;
                }
                Partner partner = (Partner)partnerMap.get(member2.getObjectId());
                if (partner == null) {
                    continue;
                }
                FightObject hostFightObject = battleground.queryFightObject(member2.getRoleId());
                if (hostFightObject == null) {
                    continue;
                }
                FightObject partnerFightObject = new PartnerFightObject(partner, hostFightObject, index);
                memberListA.add(partnerFightObject);
                FightMember partnerFightMember = partnerFightObject.asFightMember();
                fightMemberA.add(partnerFightMember);
                ++index;
                --residueNum;
            }
        }
        return list;
    }
    
    public Fight enterTeamPK(List<Member> listA, List<Member> listB, Role role) {
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
            if (!(boolean)role.getCanFight()) {
                return null;
            }
            role.setCanFight(Boolean.valueOf(false));
        }
        int mapId = (int)role.getPos().getMapId();
        Fight battleground = FightFactory.getFightFactory(FIGHT_TYPE.FORCE_PK.getValue()).create(mapId, Integer.valueOf(FIGHT_TYPE.FORCE_PK.getValue()), listA, listB);
        battleground.setRewardType(FIGHT_TYPE.FORCE_PK.getValue());
        battleground.start();
        return battleground;
    }
    
    public void enterFieldFight(Team team, Role role) {
        if (!this.canFight(role, team.getList())) {
            return;
        }
        if (this.teamService.isInTeamAndNotLeader(role)) {
            return;
        }
        synchronized (role.getCanFight()) {
            if (!(boolean)role.getCanFight()) {
                return;
            }
            role.setCanFight(Boolean.valueOf(false));
        }
        Pos position = role.getPos();
        int mapId = (int)position.getMapId();
        MonsterService monsterService = (MonsterService)SpringUtils.getBean(MonsterService.class);
        Object[] objs = monsterService.getMapMonster(role, mapId);
        Integer warRoleSerial = (Integer)objs[0];
        List<SysDataMonster> monsters = (List)objs[1];
        if (CollectionUtils.isEmpty(monsters)) {
            return;
        }
        Fight battleground = FightFactory.getFightFactory(FIGHT_TYPE.MONSTER.getValue()).create(mapId, team, role);
        battleground.setRewardType((int)warRoleSerial);
        battleground.start();
        return;
    }
    
    public boolean canFight(Role role, List<Member> list) {
        return !this.isInFight(role, list);
    }
    
    public boolean isInFight(Role role) {
        Team team = this.teamService.getTeam((int)role.getRoleId());
        List<Member> memberList = Collections.emptyList();
        if (team != null) {
            memberList = team.getList();
        }
        return this.isInFight(role, memberList);
    }
    
    private boolean isInFight(Role role, List<Member> list) {
        if (role != null) {
            Fight fight = this.getFightByRoleId((int)role.getRoleId());
            if (fight != null) {
                return false;
            }
            if (list != null) {
                Member member = (Member)list.stream().filter(e -> e.getId().equals(role.getId())).findFirst().orElse(null);
                if (member != null && !member.isInTeam() && fight == null) {
                    return false;
                }
            }
            if (fight != null && !fight.isOver()) {
                return true;
            }
        }
        if (list != null) {
            int i = 0;
            while (i < list.size()) {
                Member member2 = (Member)list.get(i);
                Fight fight2 = this.getFightByRoleId((int)member2.getRoleId());
                if (member2.isInTeam() && fight2 != null && !fight2.isOver()) {
                    return true;
                }
                ++i;
            }
        }
        return false;
    }
    
    public void checkOrderTarget(Fight fight, List<Integer> targetList) {
        if (fight.getData() != null && fight.getData().getBoolean("fight_order_target", false)) {
            String targetName = null;
            List<String> names = fight.getData().getAsList("fight_order_target_name", String.class);
            List<String> deadNames = fight.getData().getAsList("fight_order_target_dead_name", String.class);
            if (names.size() > deadNames.size()) {
                targetName = (String)names.get(deadNames.size());
            }
            for (int targetId : targetList) {
                FightObject fightTarget = fight.queryFightObject(Integer.valueOf(targetId));
                if (fightTarget.getName().equals(targetName)) {
                    fightTarget.setCurrLife(0);
                    deadNames.add(targetName);
                    if (names.size() == deadNames.size()) {
                        fight.getData().setv("fight_result_success", Boolean.valueOf(true));
                        break;
                    }
                    continue;
                }
                else {
                    continue;
                }
            }
        }
    }
    
    public void checkGuessSuccess(Fight fight) {
        if (fight.getData() != null && fight.getData().getBoolean("fight_guess_true", false)) {
            fight.getData().setv("fight_result_success", Boolean.valueOf(true));
        }
    }
    
    public CombatAddObject addPet2Fight(int hostId, Pet pet, Fight fight, int pos) {
        FightObject hostObject = fight.queryFightObject(Integer.valueOf(hostId));
        LifeManaParam lifeManaParam = this.fullHpAndMpOfPet(hostObject.getRole(), pet);
        PetFightObject fightObject = new PetFightObject(pet, hostObject);
        fightObject.setComeRound(fight.getRound());
        hostObject.setFightPetId((int)pet.getPetId());
        fight.addFightObject(fightObject);
        CombatAddObject combatAddObject = new CombatAddObject();
        combatAddObject.setId((int)pet.getPetId());
        combatAddObject.setPosition(pos);
        combatAddObject.setHostId(hostId);
        combatAddObject.setSerial((int)pet.getPetType());
        combatAddObject.setCustomIcon(fightObject.getIcon());
        combatAddObject.setList(fightObject.asFieldValues());
        combatAddObject.addSkillIds(pet);
        return combatAddObject;
    }
    
    public CombatAddObject addObject2Fight(int hostId, FightObject fightObject, Fight fight) {
        fightObject.setComeRound(fight.getRound());
        CombatAddObject combatAddObject = new CombatAddObject();
        combatAddObject.setId((int)fightObject.getId());
        combatAddObject.setPosition(fightObject.getPos());
        combatAddObject.setHostId(hostId);
        combatAddObject.setSerial(fightObject.getSerial());
        combatAddObject.setCustomIcon(fightObject.getIcon());
        combatAddObject.setList(fightObject.asFieldValues());
        combatAddObject.setSkillIds(this.getFightObjectIds(fightObject));
        return combatAddObject;
    }
    
    private List<Integer> getFightObjectIds(FightObject fightObject) {
        List<Integer> list = new ArrayList<>();
        List<RoleSkill> skillIds = fightObject.getSkillIds();
        if (skillIds != null && skillIds.size() > 0) {
            for (RoleSkill roleSkill : skillIds) {
                if (roleSkill != null && (int)roleSkill.getSkillId() > 0) {
                    list.add(roleSkill.getSkillId());
                }
                else {
                    continue;
                }
            }
            return list;
        }
        return null;
    }
    
    private boolean isAstrictSkill(Fight fight, FightObject fightObject) {
        boolean isContinue = true;
        AstrictSkill astrictSkill = (AstrictSkill)fight.getAstrictSkillMap().get(Boolean.valueOf(fightObject.isASide()));
        if (astrictSkill != null) {
            Integer count = (Integer)astrictSkill.getSkillMap().get(Integer.valueOf(fightObject.getSkillId()));
            if (count == null) {
                count = Integer.valueOf(0);
            }
            if ((int)count < astrictSkill.getTime()) {
                count = Integer.valueOf((int)count + 1);
                astrictSkill.getSkillMap().put(Integer.valueOf(fightObject.getSkillId()), count);
                isContinue = false;
            }
        }
        else {
            isContinue = false;
        }
        return isContinue;
    }
    
    private void getMonsterActionType(Fight fight, FightObject attacker) {
        FightMonsterNewService fightMonsterNewService = (FightMonsterNewService)SpringUtils.getBean(FightMonsterNewService.class);
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
        return;
    }
    
    private void excuteMonsterPetSkill(FightObject effector) {
        if (effector.getType() != 7) {
            return;
        }
        if (effector.getSkillId() == 83031) {
            int[] flzt = { 830311, 830312, 830313, 830314 };
            int round = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(flzt.length - 1));
            effector.setSkillId(flzt[round]);
        }
        if (effector.getSkillId() == 83034) {
            int[] yfly = { 830341, 830342, 830343, 830344, 830345 };
            int round = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(yfly.length - 1));
            effector.setSkillId(yfly[round]);
        }
        return;
    }
    
    public FightObject getPhysicTarget(FightObject fightObject, Fight fight) {
        FightObject target = fight.queryFightObject(Integer.valueOf(fightObject.getTargetId()));
        if (target != null && !target.isFallDown() && !fightObject.getId().equals(target.getId())) {
            return target;
        }
        List<FightObject> selfList = SkillTargetPicker.getFightObjects(fight, fightObject, true);
        if (selfList.size() <= 0) {
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(selfList.size());
        return (FightObject)selfList.get(index);
    }
    
    public List<Integer> getTargetListNew(FightObject fightObject, int maxNum, Fight fight) {
        RoleSkill skill = fightObject.getSkillBy(Integer.valueOf(fightObject.getSkillId()));
        if (skill == null) {
            this.logger.error("{}技能不存在", Integer.valueOf(fightObject.getSkillId()));
            return Collections.emptyList();
        }
        if (SkillFunc.isAssistSkill(skill.getSkillId(), fightObject.getLogic())) {
            return SkillTargetPicker.getAssistTarget(fight, fightObject, maxNum);
        }
        return SkillTargetPicker.getRandomAttackTarget(fight, fightObject, maxNum);
    }
    
    private void getSacrificeJusticeExcute(Fight fight, FightObject attacker) {
        attacker.setType(0);
        attacker.setSkillId(0);
        FightObject target = fight.queryFightObject(Integer.valueOf(attacker.getTargetId()));
        if (target != null && attacker.isASide() == target.isASide()) {
            List<FightObject> selfList = SkillTargetPicker.getFightObjects(fight, attacker, true);
            if (selfList.size() <= 0) {
                return;
            }
            Random random = new Random();
            int index = random.nextInt(selfList.size());
            target = (FightObject)selfList.get(index);
            attacker.setTargetId((int)target.getId());
        }
    }
    
    public boolean checkDeStatus(Fight fight, FightObject fightObject) {
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
        return noAction;
    }
    
    private List<FightObject> getConfusionFightTargets(FightObject fightObject, Fight fight) {
        List<FightObject> result = new ArrayList<>();
        List<FightObject> firstTargets = SkillTargetPicker.getFightObjects(fight, fightObject, true);
        List<FightObject> secondTargets = SkillTargetPicker.getFightObjects(fight, fightObject, false);
        Optional<RoleSkill> any = fightObject.getSkillIds().stream().filter(obj -> obj.getSkillId().equals(Integer.valueOf(83032))).findAny();
        if (any.isPresent()) {
            for (FightObject target : firstTargets) {
                if (fightObject.getId().equals(target.getId())) {
                    continue;
                }
                result.add(target);
            }
        }
        if (result.size() <= 0) {
            for (FightObject target : secondTargets) {
                if (fightObject.getId().equals(target.getId())) {
                    continue;
                }
                result.add(target);
            }
        }
        if (result.size() <= 0) {
            for (FightObject target : firstTargets) {
                if (fightObject.getId().equals(target.getId())) {
                    continue;
                }
                result.add(target);
            }
            return result;
        }
        return result;
    }
    
    private void setNormalAttack(Fight fight, FightObject attacker) {
        attacker.setType(0);
        attacker.setSkillId(0);
        attacker.setTargetId(0);
        this.normalAttack(attacker, fight);
    }
    
    public void getSkillFail(Fight fight, FightObject attacker, String cause) {
        attacker.setType(0);
        attacker.setSkillId(0);
        this.normalAttack(attacker, fight);
        this.logger.error("获取技能信息失败改变为普通攻击，原因{}", cause);
    }
    
    private int costMana(Fight fight, FightObject attacker, RoleSkill skill, int mana) {
        if (attacker.isMonster()) {
            String skillStr = skill.getSkillId().toString();
            String skillStrFrist = skillStr.substring(0, 1);
            if ("3".equals(skillStrFrist) || "8".equals(skillStrFrist)) {
                mana = 0;
            }
        }
        else if (attacker.isPartner()) {
            mana = 0;
        }
        else if (attacker.isPlayer()) {
            int alterMp = PassiveSkillUtils.subtractMp(fight, attacker, mana);
            if (alterMp != 0) {
                mana += alterMp;
            }
        }
        return mana;
    }
    
    public void useItem(FightObject effector, Fight fight) {
        int num = 1;
        if (this.checkDeStatus(fight, effector)) {
            return;
        }
        effector.recordAction(fight);
        SysDrug drug = (SysDrug)ItemDataPool.data_drug.get(Integer.valueOf(effector.getSkillId()));
        if (drug == null) {
            return;
        }
        if (effector.isPet() && (10027 == (int)drug.getSerial() || 10028 == (int)drug.getSerial())) {
            return;
        }
        Integer useType = drug.getUseType();
        if (useType.equals(Integer.valueOf(0))) {
            return;
        }
        FightObject target = fight.queryFightObject(Integer.valueOf(effector.getTargetId()));
        if (target == null) {
            if (4 != (int)drug.getShowClass()) {
                List<FightObject> mySide = fight.getMySide((int)effector.getId());
                Random random = new Random();
                int i = random.nextInt(mySide.size());
                FightObject fightObject = target = (FightObject)mySide.get(i);
            }
            else {
                return;
            }
        }
        if (effector.getLogic() != LOGIC.LOGICTYPE_MONSTER.getValue()) {
            BagService bagService = (BagService)SpringUtils.getBean(BagService.class);
            Role role = effector.getRole();
            Integer drugNum = bagService.consumeBagGoods(role, (int)drug.getSerial(), num, true, true, "战斗");
            if (drugNum != null) {
                PetSkillEffectorUtil.sendMsg(fight, effector, "药品已用尽!");
                return;
            }
        }
        List<FiedValue> passiveEffects = PassiveSkillUtils.useDrug(fight, effector, target, (int)drug.getShowClass());
        DrugProcessor drugProcessor = null;
        if ((int)drug.getShowClass() == 4 || (int)drug.getShowClass() == 5) {
            drugProcessor = DrugProcessor.queryProcessor(drug.getSerial());
            if (drugProcessor == null) {
                return;
            }
            boolean isExcute = drugProcessor.checkExcute(fight, effector, target, passiveEffects);
            if (!isExcute) {
                return;
            }
        }
        switch ((int)drug.getShowClass()) {
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
        return;
    }
    
    private void useItemHP(Fight fight, FightObject effector, FightObject target, SysDrug drug, List<FiedValue> passiveEffects) {
        int oldLife = target.getCurrLife();
        int addHp = this.reduceHPMP(fight, true, target, drug, passiveEffects);
        this.sendMsgHp(fight, effector, target, drug, oldLife, addHp);
        this.tianFuAddTarget(fight, effector, target, passiveEffects, drug, addHp, 0);
    }
    
    private void useItemMP(Fight fight, FightObject effector, FightObject target, SysDrug drug, List<FiedValue> passiveEffects) {
        int addMp = this.reduceHPMP(fight, false, target, drug, passiveEffects);
        this.sendMsgMp(fight, effector, target, drug, addMp);
        this.tianFuAddTarget(fight, effector, target, passiveEffects, drug, 0, addMp);
    }
    
    private void useItemHPMP(Fight fight, FightObject effector, FightObject target, SysDrug drug, List<FiedValue> passiveEffects) {
        int addMp = this.reduceHPMP(fight, false, target, drug, passiveEffects);
        int oldLife = target.getCurrLife();
        int addHp = this.reduceHPMP(fight, true, target, drug, passiveEffects);
        boolean isRevive = false;
        if (oldLife <= 0) {
            isRevive = true;
        }
        ActionParamUtils.changeHpMp(fight, effector, target, 5, (int)drug.getSerial(), 2, addHp, addMp, isRevive);
    }
    
    private int reduceHPMP(Fight fight, boolean isHP, FightObject target, SysDrug drug, List<FiedValue> passiveEffects) {
        if (isHP) {
            int addHp = 0;
            if ((int)drug.getDrugAddHpValue() > 0) {
                addHp = (int)drug.getDrugAddHpValue();
            }
            else if ((int)drug.getDrugAddHpPercent() > 0) {
                addHp = target.getMaxLife() * (int)drug.getDrugAddHpPercent() / 100;
            }
            if (passiveEffects != null && passiveEffects.size() > 0) {
                int addPercentHp = 0;
                for (FiedValue fied : passiveEffects) {
                    if (fied.getType().contains("hp") && fied.getType().contains("addPercent") && 5 == fied.getVT()) {
                        Double value = (Double)fied.getValue();
                        if (value != null && (double)value > 0.0) {
                            addPercentHp += (int)((double)addHp * (double)value);
                            break;
                        }
                        continue;
                    }
                    else {
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
        else {
            int addMp = 0;
            if ((int)drug.getDrugAddMpValue() > 0) {
                addMp = (int)drug.getDrugAddMpValue();
            }
            else if ((int)drug.getDrugAddMpPercent() > 0) {
                addMp = target.getMaxMana() * (int)drug.getDrugAddMpPercent() / 100;
            }
            if (passiveEffects != null && passiveEffects.size() > 0) {
                int addPercentMp = 0;
                for (FiedValue fied : passiveEffects) {
                    if (fied.getType().contains("mp") && fied.getType().contains("addPercent") && 5 == fied.getVT()) {
                        Double value = (Double)fied.getValue();
                        if (value != null && (double)value > 0.0) {
                            addPercentMp += (int)((double)addMp * (double)value);
                            break;
                        }
                        continue;
                    }
                    else {
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
    }
    
    private void tianFuAddTarget(Fight fight, FightObject effector, FightObject target, List<FiedValue> tianFuEffects, SysDrug drug, int addHp, int addMp) {
        if (tianFuEffects != null && tianFuEffects.size() > 0) {
            boolean flag = false;
            int targetId = 0;
            for (FiedValue fied : tianFuEffects) {
                if ("addTarget".equals(fied.getType()) && 6 == fied.getVT()) {
                    flag = (boolean)fied.getValue();
                }
                if ("addTargetId".equals(fied.getType()) && 3 == fied.getVT() && fied.getValue() != null && String.valueOf(fied.getValue()).matches("^[0-9]+$")) {
                    targetId = (int)fied.getValue();
                }
                else {
                    continue;
                }
            }
            if (flag) {
                if (addHp > 0) {
                    if (targetId == 0) {
                        if (target.isPet()) {
                            FightObject host = fight.queryFightObject(Integer.valueOf(target.getHostId()));
                            this.addTargetHp(fight, effector, host, drug, addHp, addMp);
                        }
                        else if (target.isPlayer()) {
                            Optional<FightObject> petOptional = fight.getMySide((int)target.getId()).stream().filter(obj -> obj.isPet() && target.getId().equals(Integer.valueOf(obj.getHostId()))).findFirst();
                            if (petOptional.isPresent()) {
                                FightObject pet = (FightObject)petOptional.get();
                                this.addTargetHp(fight, effector, pet, drug, addHp, addMp);
                            }
                        }
                    }
                    else {
                        FightObject newTarget = fight.queryFightObject(Integer.valueOf(targetId));
                        this.addTargetHp(fight, effector, newTarget, drug, addHp, addMp);
                    }
                }
                else if (addMp > 0) {
                    if (targetId == 0) {
                        this.addTargetMp(fight, effector, target, drug, addMp);
                    }
                    else {
                        FightObject newTarget = fight.queryFightObject(Integer.valueOf(targetId));
                        this.sendMsgMp(fight, effector, newTarget, drug, addMp);
                    }
                }
            }
        }
    }
    
    private void addTargetHp(Fight fight, FightObject effector, FightObject newTarget, SysDrug drug, int addHp, int addMp) {
        boolean isRevive = false;
        if (newTarget.getCurrLife() <= 0) {
            isRevive = true;
        }
        if (addMp > 0) {
            ActionParamUtils.changeHpMp(fight, effector, newTarget, 5, (int)drug.getSerial(), 2, addHp, addMp, isRevive);
        }
        else {
            ActionParamUtils.changeLife(fight, effector, newTarget, 5, (int)drug.getSerial(), 2, addHp, isRevive);
        }
    }
    
    private void addTargetMp(Fight fight, FightObject effector, FightObject target, SysDrug drug, int addMp) {
        if (target.isPet()) {
            FightObject host = fight.queryFightObject(Integer.valueOf(target.getHostId()));
            this.sendMsgMp(fight, effector, host, drug, addMp);
        }
        else if (target.isPlayer()) {
            Optional<FightObject> petOptional = fight.getMySide((int)target.getId()).stream().filter(obj -> obj.isPet() && target.getId().equals(Integer.valueOf(obj.getHostId()))).findFirst();
            if (petOptional.isPresent()) {
                FightObject pet = (FightObject)petOptional.get();
                this.sendMsgMp(fight, effector, pet, drug, addMp);
            }
        }
    }
    
    private void sendMsgHp(Fight fight, FightObject effector, FightObject target, SysDrug drug, int oldLife, int addHp) {
        boolean isRevive = false;
        if (oldLife <= 0) {
            isRevive = true;
        }
        ActionParamUtils.changeLife(fight, effector, target, 5, (int)drug.getSerial(), 2, addHp, isRevive);
    }
    
    private void sendMsgMp(Fight fight, FightObject effector, FightObject target, SysDrug drug, int addMp) {
        ActionParamUtils.changeMana(fight, effector, target, 5, (int)drug.getSerial(), 2, addMp);
    }
    
    private void flee(FightObject fightObject, Fight fight) {
        if (fightObject.isFallDown()) {
            return;
        }
        if (this.checkDeStatus(fight, fightObject)) {
            return;
        }
        Role role = this.roleService.getOnlinePlayer((long)fightObject.getRole().getId());
        if (role == null) {
            if ((fightObject instanceof MonsterFightObject)) {
                FightMonsterNewService bean = (FightMonsterNewService)SpringUtils.getBean(FightMonsterNewService.class);
                bean.monsterFlee(fightObject, fight);
            }
            else {
                return;
            }
        }
        fightObject.recordAction(fight);
        ActionDetailParam actionDetailParam = new ActionDetailParam(fightObject, 4, 4, 0);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        FightObjectParam attackerParam = actionDetailParam.gainAttackerParam();
        if (!this.canFlee(fightObject, fight)) {
            attackerParam.setHit(false);
            return;
        }
        this.excuteTeamDisappear(fight, role, fightObject);
        fight.getFleeList().add(fightObject.getId());
        fight.addExitRole(Boolean.valueOf(fightObject.isASide()), new FieldValue(2, (int)fightObject.getId()));
        role.setLastDead((fightObject.getCurrLife() <= 0) ? 0 : 1);
        EventDispatcher.getInstance().fireEvent(new FightEndEvent(fight, EventType.FIGHT_END, role, fight.getId(), (byte)3, fight.getType(), fight.getBossReward(), fight.getTaskId(), fight.getNpcId(), 0));
        fight.removeFightObject(fightObject);
        FightObject petObject = fight.queryFightObject(Integer.valueOf(fightObject.getFightPetId()));
        if (petObject != null) {
            fight.getFleeList().add(petObject.getId());
            fight.petDie(petObject);
        }
        role.save();
        return;
    }
    
    public void excuteTeamDisappear(Fight fight, Role role, FightObject roleObject) {
        role.setPrevFightEndTime(System.currentTimeMillis());
        role.setCanFight(Boolean.valueOf(true));
        this.resetAfterLeaveFight(fight, roleObject);
        int teamCount = this.teamService.getTeamCount(role);
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (team != null && teamCount > 1 && !role.getRoleId().equals(Integer.valueOf(team.getLeaderRoleId()))) {
            this.teamService.leaveTemp(role, false);
        }
        List<FightObject> mySide = fight.getMySide((int)roleObject.getId());
        if (team != null) {
            Member member = (Member)team.getList().get(0);
            if (role.getRoleId().equals(member.getRoleId())) {
                for (int j = mySide.size() - 1; j >= 0; --j) {
                    FightObject obj = (FightObject)mySide.get(j);
                    if (obj.isPartner()) {
                        fight.removeFightObject(obj);
                    }
                }
            }
        }
        else if (mySide != null && mySide.size() > 0) {
            for (int i = mySide.size() - 1; i >= 0; --i) {
                if (((FightObject)mySide.get(i)).isPartner() && ((FightObject)mySide.get(i)).getHostId() == (int)roleObject.getId()) {
                    fight.removeFightObject((FightObject)mySide.get(i));
                }
            }
        }
    }
    
    private boolean canFlee(FightObject fightObject, Fight fight) {
        if (Const.not_flee.contains(Integer.valueOf(fight.getType()))) {
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
        int rand = ThreadLocalRandom.current().nextInt(100);
        int odds = 100 - substractOdds;
        return rand < odds;
    }
    
    public void selectPet(FightObject fightObject, Fight fight) {
        if (this.checkDeStatus(fight, fightObject)) {
            return;
        }
        fightObject.recordAction(fight);
        Integer roleId = (Integer)FightExecutorNewService.PETID_ROLEID.get(Integer.valueOf(fightObject.getTargetId()));
        if (roleId != null) {
            return;
        }
        int oldPetId = fightObject.getFightPetId();
        int newPetId = fightObject.getTargetId();
        Role role = fightObject.getRole();
        Pet newPet = (Pet)this.petService.selectPet(role).get(Integer.valueOf(newPetId));
        if (newPet == null) {
            this.logger.error("未找到要召唤的的宠物id：" + newPetId + ",宠物的主人id：" + role.getId());
            return;
        }
        CombatAddObject combatAddObject = this.addPet2Fight((int)fightObject.getId(), newPet, fight, (int)SkillFunc.getPetPosByMasterPos(Integer.valueOf(fightObject.getPos())));
        fightObject.setFightPetId((int)newPet.getPetId());
        FightObject delPet = fight.queryFightObject(Integer.valueOf(oldPetId));
        FightUtils.excutePetDisappear(fight, fightObject, delPet);
        ActionDetailParam actionDetailParam = new ActionDetailParam(fightObject, 3, 0, (int)newPet.getPetId());
        RefreshPet refreshPet = new RefreshPet();
        ArrayList<RefreshPet> refreshPets = new ArrayList<>();
        refreshPets.add(refreshPet);
        refreshPet.setPetId((int)newPet.getPetId());
        refreshPet.setType(1);
        actionDetailParam.setPetList(refreshPets);
        actionDetailParam.setRoleId((long)role.getId());
        FightExecutorNewService.PETID_ROLEID.put(newPet.getPetId(), newPet.getRoleId());
        FightExecutorNewService.FIGHT_ROOMS.put(newPet.getPetId(), Integer.valueOf(fight.getId()));
        List<CombatAddObject> list = new ArrayList<>();
        list.add(combatAddObject);
        actionDetailParam.setCombatAddObjects(list);
        RespFightShouting fightShouting = this.fightMessageNewService.fightShouting(fight.queryFightObject(newPet.getPetId()), "战斗出场");
        FightObjectParam fightObjectParam = actionDetailParam.gainTargetParam(newPetId, true);
        fightObjectParam.setFightShouting(fightShouting);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        PetPassiveSkillUtil.otherEnterBattle(fight, Integer.valueOf(newPetId));
        return;
    }
    
    public void callbackPet(FightObject fightObject, Fight fight, boolean isNoPassiveSkill) {
        if (isNoPassiveSkill && this.checkDeStatus(fight, fightObject)) {
            return;
        }
        fightObject.recordAction(fight);
        boolean flag = false;
        FightObject target = fight.queryFightObject(Integer.valueOf(fightObject.getTargetId()));
        fightObject.setFightPetId(0);
        if (target != null && !target.isFallDown()) {
            target.setCurrLife(0);
        }
        else {
            flag = true;
        }
        if (target != null) {
            List<Integer> statusList = new ArrayList<>();
            for (int status : StatusConst.getPositiveStatus()) {
                if (target.getStatusValue(status) > 0) {
                    statusList.add(Integer.valueOf(status));
                }
                else {
                    continue;
                }
            }
            PassiveSkillUtils.exitOrClearPositive(fight, fightObject, target, statusList);
            fight.excuteExitPassive(target);
        }
        if (target != null) {
            ActionParamUtils.excuteDisappear(fight, fightObject, (int)target.getId(), 12, 0, 2);
        }
        if (!flag) {
            PetSkillEffectorUtil.sendCancelAction(fight, target);
            fight.petDie(target);
        }
        return;
    }
    
    private void guard(FightObject fightObject, Fight fight) {
        fightObject.recordAction(fight);
        PassiveSkillUtils.excutePassiveSkill(fight, fightObject, PET_PASSIVE_SKILL_TRIGGER_DICT.SELF_SELL_END.getValue(), new Object[0]);
        if (this.checkDeStatus(fight, fightObject)) {
            return;
        }
        TianFuProcessor tianFuProcessor = TianFuProcessor.queryProcessor(Integer.valueOf(323304));
        tianFuProcessor.passive(fight, fightObject, null, new Object[0]);
        return;
    }
    
    private Map<Integer, List<Integer>> excuteStatusReduce(Fight fight) {
        boolean isHaveDie = false;
        Map<Integer, FightObject> dragonObjectMap = new HashMap();
        Map<Integer, List<Integer>> clearStatusMap = new HashMap();
        List<FightObjectParam> targetParamList = new ArrayList<>();
        int targetOrder = 1;
        for (FightObject fightObject : fight.getAllFightObjects()) {
            List<Integer> list = new ArrayList<>();
            int dict = 1022;
            String dist = String.valueOf(dict);
            NutMap statusMap = fightObject.getStatusMap();
            int recoverStatus = 0;
            if (statusMap.getInt(dist, 0) != 0) {
                recoverStatus = dict;
            }
            NutMap recoverMap = null;
            if (statusMap.getInt(dist) > 0) {
                boolean dead = fightObject.isFallDown();
                fightObject.pushTempCache("dead", Boolean.valueOf(dead));
                if (recoverStatus != 0 && !fightObject.isFrozen() && !fight.isOver()) {
                    int recoverValue = fightObject.getStatusSubMap().getInt(dict + "value");
                    if (recoverValue > 0) {
                        fightObject.pushTempCache("recover", Boolean.valueOf(true));
                        if (recoverValue > fightObject.getMaxLife() - fightObject.getCurrLife()) {
                            recoverValue = fightObject.getMaxLife() - fightObject.getCurrLife();
                        }
                        recoverValue += PassiveSkillUtils.countRecoverBloodEnd(fight, fightObject, recoverValue, null);
                        fightObject.pushTempCache("recoverLife", Integer.valueOf(recoverValue));
                        fightObject.setCurrLife(fightObject.getCurrLife() + recoverValue);
                        if (fightObject.getMaxLife() < fightObject.getCurrLife()) {
                            fightObject.setCurrLife(fightObject.getMaxLife());
                        }
                        if (!fightObject.isFallDown()) {
                            fightObject.getTempCache().remove("dead");
                        }
                    }
                    fightObject.pushTempCache("recoverSkill", Integer.valueOf(0));
                    recoverMap = this.fightMessageNewService.poisonOrRecoverActionParam(fight, fightObject);
                    if (dead) {
                        fightObject.removeStatus(dict);
                        fightObject.removeStatusSub(dict + "value");
                        list.add(Integer.valueOf(dict));
                    }
                }
            }
            List<Integer> statusResidueTimes = new ArrayList<>();
            for (Map.Entry<String, Object> entry : statusMap.entrySet()) {
                String key = (String)entry.getKey();
                if (!key.matches("^[0-9]+$")) {
                    continue;
                }
                int value = (int)entry.getValue();
                if (value > 0) {
                    Integer status = Integer.valueOf(key);
                    if (StatusConst.getControlStatus().contains(status)) {
                        if (PassiveSkillUtils.isUnlock(fight, fightObject, status)) {
                            continue;
                        }
                        double odds = statusMap.getDouble(key + "odds");
                        if (!SkillAi.canAffectOnPro(odds)) {
                            statusMap.setv(key, Integer.valueOf(0));
                            list.add(status);
                            PassiveSkillUtils.statusReduce(fight, fightObject, status);
                            continue;
                        }
                    }
                    if (--value <= 0) {
                        if (status.equals(Integer.valueOf(1023))) {
                            dragonObjectMap.put(fightObject.getId(), fightObject);
                        }
                        list.add(status);
                        PassiveSkillUtils.statusReduce(fight, fightObject, status);
                        if (200 == Integer.parseInt(key)) {
                            statusResidueTimes.add(status);
                            statusResidueTimes.add(Integer.valueOf(value));
                        }
                    }
                    else {
                        statusResidueTimes.add(status);
                        statusResidueTimes.add(Integer.valueOf(value));
                    }
                }
                statusMap.put(key, Integer.valueOf(value));
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
                    targetParam.setObjId((int)fightObject.getId());
                }
                if (dragonObjectMap.size() > 0) {
                    FightObject dragonObject = (FightObject)dragonObjectMap.get(fightObject.getId());
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
                    Map<Integer, Integer> updateStatusMap = this.fightMessageNewService.sendFightObjectStatus(fight, fightObject);
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
            else {
                continue;
            }
        }
        if (targetParamList.size() > 0) {
            ActionDetailParam actionDetailParam = new ActionDetailParam(43, 0, 0, 0);
            actionDetailParam.getTargetParamList().addAll(targetParamList);
            fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        }
        if (isHaveDie) {
            PetPassiveSkillUtil.deathExcute(fight);
            return clearStatusMap;
        }
        return clearStatusMap;
    }
    
    public void excuteRoundStartPoison(Fight fight) {
        for (FightObject fightObject : fight.getAllFightObjects()) {
            NutMap statusMap = fightObject.getStatusMap();
            fightObject.pushTempCache("alive", Boolean.valueOf(!fightObject.isFallDown()));
            if (statusMap.getInt(String.valueOf(1005), 0) != 0 && !fightObject.isFrozen()) {
                fightObject.pushTempCache("poison", Boolean.valueOf(true));
                NutMap nutMap = fightObject.getStatusSubMap();
                Integer hurt = SkillCompute.computeSkillDamagePoisonFollowUp(nutMap);
                hurt = Integer.valueOf((int)hurt - (int)fightObject.getSelfAllTempPro(MIND.XS_DU.getDist()));
                if ((int)hurt <= 0) {
                    hurt = Integer.valueOf(1);
                }
                hurt = Integer.valueOf((int)hurt + PassiveSkillUtils.poisonReduceHp(fight, fightObject, (int)hurt));
                if ((int)hurt < 1) {
                    hurt = Integer.valueOf(1);
                }
                else if ((int)hurt > fightObject.getCurrLife()) {
                    hurt = Integer.valueOf(fightObject.getCurrLife());
                }
                fightObject.pushTempCache("poisonValue", hurt);
                fightObject.setCurrLife(fightObject.getCurrLife() - (int)hurt);
                if (fightObject.isFallDown()) {
                    boolean revive = FightRateUtil.petCanRevive(fightObject);
                    fightObject.pushTempCache("revive", Boolean.valueOf(revive));
                }
                fightObject.pushTempCache("poisonDead", Boolean.valueOf(fightObject.isFallDown()));
                int round = fightObject.getStatusMap().getInt("1005_round") + 1;
                fightObject.getStatusMap().setv("1005_round", Integer.valueOf(round));
            }
            else {
                continue;
            }
        }
        ActionDetailParam actionDetailParam = new ActionDetailParam(43, 0, 0, 0);
        boolean isHavePoison = false;
        boolean isHaveDie = false;
        int order = 1;
        for (FightObject fightObject2 : fight.getAllFightObjects()) {
            NutMap poisonMap = this.fightMessageNewService.poisonOrRecoverActionParam(fight, fightObject2);
            if (poisonMap != null) {
                FightObjectParam target = this.getPoisonOrRecoverParam(fightObject2, poisonMap);
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
            else {
                continue;
            }
        }
        if (isHavePoison) {
            fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        }
        if (isHaveDie) {
            PetPassiveSkillUtil.deathExcute(fight);
        }
        for (FightObject fightObject2 : fight.getAllFightObjects()) {
            boolean poisonDead = (boolean)fightObject2.getTempCache("poisonDead", Boolean.valueOf(false));
            boolean dead = (boolean)fightObject2.getTempCache("dead", Boolean.valueOf(false));
            if (dead || poisonDead) {
                boolean revive2 = (boolean)fightObject2.getTempCache("revive", Boolean.valueOf(false));
                if (fightObject2.isMonster()) {
                    int monsterType = ((MonsterFightObject)fightObject2).getMonsterType();
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
            else {
                continue;
            }
        }
    }
    
    private FightObjectParam getPoisonOrRecoverParam(FightObject fightObject, NutMap map) {
        if (map != null) {
            FightObjectParam targetParam = new FightObjectParam();
            targetParam.setObjId((int)fightObject.getId());
            int currLife = map.getInt("currLife", 0);
            if (currLife != 0) {
                targetParam.setCurrentLife(currLife);
            }
            else {
                targetParam.setCurrentLife(fightObject.getCurrLife());
            }
            int changeLife = map.getInt("changeLife", 0);
            if (changeLife != 0) {
                targetParam.setChangeLife(changeLife);
            }
            int currMana = map.getInt("currMana", 0);
            if (currMana != 0) {
                targetParam.setCurrentMana(currMana);
            }
            else {
                targetParam.setCurrentMana(fightObject.getCurrMana());
            }
            int changeMana = map.getInt("changeMana", 0);
            if (changeMana != 0) {
                targetParam.setChangeMana(changeMana);
            }
            int missed = map.getInt("missed", -1);
            if (missed != -1) {
                targetParam.setMissed(missed);
                return targetParam;
            }
            return targetParam;
        }
        else {
            return null;
        }
    }
    
    public void dragonMorphRenew(FightObject fightObject) {
        SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30041));
        double[] power = sysSkill.getPower();
        int life_gain = (int)((double)fightObject.getCurrLife() * power[0]);
        int currLife = life_gain + fightObject.getCurrLife();
        int mana_gain = (int)((double)fightObject.getCurrMana() * power[1]);
        int currMana = mana_gain + fightObject.getCurrMana();
        int maxLife = fightObject.getStatusSubMap().getInt("1023_max_life");
        int maxMana = fightObject.getStatusSubMap().getInt("1023_max_mana");
        fightObject.removeStatusSub("1023_max_life");
        fightObject.removeStatusSub("1023_max_mana");
        fightObject.setCurrLife((currLife > maxLife) ? maxLife : currLife);
        fightObject.setCurrMana((currMana > maxMana) ? maxMana : currMana);
        fightObject.setMaxLife(maxLife);
        fightObject.setMaxMana(maxMana);
    }
    
    @EventHandler({ EventType.LOGIN })
    public void handleLoginEvent(LoginEvent loginEvent) {
        Role role = loginEvent.getRole();
        this.enterFight(role);
    }
    
    private void enterFight(Role role) {
        Fight fight = this.getFightByRoleId((int)role.getRoleId());
        if (fight == null) {
            role.setCanFight(Boolean.valueOf(true));
            return;
        }
        if (fight.isOver() && 3 != fight.getRoundType()) {
            FightExecutorNewService.FIGHT_ROOMS.remove(role.getRoleId());
            return;
        }
        FightObject roleObject = fight.queryFightObject(role.getRoleId());
        if (roleObject == null) {
            role.setCanFight(Boolean.valueOf(true));
            return;
        }
        if (2 == fight.getRoundType()) {
            try {
                Thread.sleep(100L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        RoundParam roundParam = fight.getRoundParam();
        RespExcuteFight respExcuteFight = null;
        boolean flag = false;
        RespFightInParam respFightInParam;
        RespRoundStartParam roundStartParam;
        if (3 == fight.getRoundType()) {
            respFightInParam = FightParamPool.createRespFightInParam(false, fight.getId(), (int)role.getRoleId(), roleObject.isASide());
            roundStartParam = FightParamPool.getRespRoundStartParam(fight.getId(), (int)role.getRoleId());
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
        FightMessageUtil.sendFightPackage((long)role.getId(), respFightInParam);
        FightMessageUtil.sendFightPackage((long)role.getId(), roundStartParam);
        for (FightObject fightObject : fight.getAllFightObjects()) {
            if (!fightObject.isMonster() && fightObject.isDoAction()) {
                RespInputInstruct respInputInstruct = new RespInputInstruct((int)fightObject.getId(), fightObject.getTargetId(), fightObject.getType(), fightObject.getSkillId(), 0);
                FightMessageUtil.sendFightPackage((long)role.getId(), respInputInstruct);
            }
            else {
                continue;
            }
        }
        if (flag) {
            FightMessageUtil.sendFightPackage((long)role.getId(), respExcuteFight);
        }
        this.broadcastService.sendTitleInfoNew(role);
        return;
    }
    
    public RespFightInParam getRoundFightParam(Role role, Fight fight, FightObject roleObject) {
        RespFightInParam respFightInParam = new RespFightInParam();
        FightObject petObject = fight.queryFightObject(Integer.valueOf(roleObject.getFightPetId()));
        if (petObject != null) {
            Pet pet = petObject.getPet();
            if (pet != null) {
                PetFightInParam petFightParam = fight.getPetFightParam(pet);
                respFightInParam.addPetFightInParam(petFightParam);
            }
        }
        List<SkillParam> skillParams = fight.getSkillParams(role);
        if (skillParams != null) {
            respFightInParam.setSkillParams(skillParams);
        }
        List<RefreshPet> refreshPets = this.getFightInParamPetList(fight, roleObject);
        respFightInParam.setPetList(refreshPets);
        List<ExcuteFightParam> excuteFightParams = fight.getFightStatus();
        if (excuteFightParams != null) {
            respFightInParam.setExcuteFightParams(excuteFightParams);
        }
        if (roleObject.isASide()) {
            respFightInParam.getRespCombatFriends().setList(this.getFightMemberList(fight.getListA()));
            respFightInParam.getRespCombatOpponets().setList(this.getFightMemberList(fight.getListB()));
            return respFightInParam;
        }
        respFightInParam.getRespCombatFriends().setList(this.getFightMemberList(fight.getListB()));
        respFightInParam.getRespCombatOpponets().setList(this.getFightMemberList(fight.getListA()));
        return respFightInParam;
    }
    
    public List<FightMember> getFightMemberList(List<FightObject> fightObjs) {
        ArrayList<FightMember> members = new ArrayList<>();
        for (FightObject fightObject : fightObjs) {
            FightMember fightMember = fightObject.asFightMember();
            if (fightMember != null) {
                if (fightObject.getLogic() == LOGIC.LOGICTYPE_MONSTER.getValue()) {
                    fightMember.setSerial(50000);
                }
                members.add(fightMember);
            }
            else {
                continue;
            }
        }
        return members;
    }
    
    public List<RefreshPet> getFightInParamPetList(Fight fight, FightObject fightObject) {
        FightObject petObject = fight.queryFightObject(Integer.valueOf(fightObject.getFightPetId()));
        List<RefreshPet> refreshPets = new ArrayList<>();
        RefreshPet refreshPet = null;
        if (petObject != null) {
            refreshPet = new RefreshPet();
            refreshPets.add(refreshPet);
            refreshPet.setPetId((int)petObject.getId());
            refreshPet.setType(1);
        }
        Role role = fightObject.getRole();
        if (role != null) {
            Map<Integer, Pet> petMap = (Map)PetCache.PET_MAP.get(role.getId());
            if (petMap != null && petMap.size() > 0) {
                Set<Integer> deadPetIds = fight.getDeadPetIds();
                for (Map.Entry<Integer, Pet> entry : petMap.entrySet()) {
                    Pet pet = (Pet)entry.getValue();
                    if (refreshPet != null && pet.getPetId().equals(Integer.valueOf(refreshPet.getPetId()))) {
                        continue;
                    }
                    RefreshPet refreshPet2 = new RefreshPet();
                    refreshPet2.setPetId((int)pet.getPetId());
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
    
    public RespRoundStartParam getRespRoundStartParam(Fight fight, int objId) {
        if (fight.getRespRoundStartParamMap().size() > 0) {
            RespRoundStartParam respRoundStartParam = (RespRoundStartParam)fight.getRespRoundStartParamMap().get(Integer.valueOf(objId));
            if (respRoundStartParam == null) {
                respRoundStartParam = new RespRoundStartParam();
                respRoundStartParam.setObjId(objId);
                respRoundStartParam.setRound(fight.getRound());
                return respRoundStartParam;
            }
            return respRoundStartParam;
        }
        else {
            RespRoundStartParam respRoundStartParam = new RespRoundStartParam();
            respRoundStartParam.setObjId(objId);
            respRoundStartParam.setRound(fight.getRound());
            return respRoundStartParam;
        }
    }
    
    public RespRoundStartParam getRoundStartParamAll(Fight fight, FightObject fightObject) {
        RespRoundStartParam respRoundStartParam = new RespRoundStartParam();
        respRoundStartParam.setObjId((int)fightObject.getId());
        respRoundStartParam.setAutoFight(fightObject.getAutoFight() == 1);
        respRoundStartParam.setRound(fight.getRound());
        AutoFightSkillParam autoFightSkillParam = this.fightMessageNewService.selfAuto(fightObject, fight);
        if (autoFightSkillParam != null) {
            respRoundStartParam.addAutoFightSkillParam(autoFightSkillParam);
        }
        List<SkillCool> cools = fightObject.getSkillCoolList();
        respRoundStartParam.addSkillCools(cools);
        FightObject petObject = fight.queryFightObject(Integer.valueOf(fightObject.getFightPetId()));
        if (petObject != null) {
            List<SkillCool> petCools = petObject.getSkillCoolList();
            if (petCools.size() > 0) {
                respRoundStartParam.addSkillCools(petCools);
            }
            AutoFightSkillParam autoFightSkillParamPet = this.fightMessageNewService.selfAuto(petObject, fight);
            if (autoFightSkillParamPet != null) {
                respRoundStartParam.addAutoFightSkillParam(autoFightSkillParamPet);
            }
        }
        return respRoundStartParam;
    }
    
    public void maxRoundTimer(Fight fight, long delay) {
        if (fight != null && fight.getAllFightObjects() != null) {
            for (FightObject fightObject : fight.getAllFightObjects()) {
                if (!fightObject.isPlayer()) {
                    continue;
                }
                fight.roundFinishTask(fightObject, delay);
            }
        }
    }
    
    public boolean sendFightParams(Fight fight, boolean isDistinguish) {
        boolean flag = true;
        RespExcuteFight respExcuteFight = this.fightMessageNewService.excuteSendFightMsg(fight);
        if (respExcuteFight == null) {
            flag = false;
        }
        StringBuffer sendRoleSB = new StringBuffer("[");
        List<Long> playerList = fight.getActionParam().getPlayerList();
        if (playerList != null && playerList.size() > 0) {
            for (Long playerId : playerList) {
                Role role = this.roleService.getPlayer((long)playerId);
                if (isDistinguish) {
                    this.excuteSubstitution(fight, role, respExcuteFight);
                }
                FightMessageUtil.sendFightPackage((long)playerId, respExcuteFight);
                sendRoleSB.append(role.getRoleId() + ",");
                if (!flag) {
                    this.animationPlayEnd(fight, (int)role.getRoleId());
                }
                else {
                    continue;
                }
            }
        }
        FightParamPool.gainFightRoundParam(fight.getId()).setRespExcuteFight(respExcuteFight);
        if (fight.isWatchFightOnOff()) {
            FightSpectatorNewService fightSpectatorService = (FightSpectatorNewService)SpringUtils.getBean(FightSpectatorNewService.class);
            ((FightSpectatorExecutorNewService)SpringUtils.getBean(FightSpectatorExecutorNewService.class)).addFightTask(fight.getId(), () -> fightSpectatorService.sendWatchFightData(fight, (byte)2));
        }
        sendRoleSB.deleteCharAt(sendRoleSB.length() - 1);
        sendRoleSB.append("]");
        this.fightRecordService.saveRecord(fight, respExcuteFight, sendRoleSB.toString());
        fight.getActionParam().setActionDetailParams(new ArrayList<>());
        return flag;
    }
    
    private void excuteSubstitution(Fight fight, Role role, RespExcuteFight respExcuteFight) {
        if (respExcuteFight.getExcuteFightParams() != null && respExcuteFight.getExcuteFightParams().size() > 0) {
            for (ExcuteFightParam excuteFightParam : respExcuteFight.getExcuteFightParams()) {
                if (excuteFightParam.getType() == 2 && excuteFightParam.getCombatAddObjects() != null && excuteFightParam.getCombatAddObjects().size() > 0 && !fight.isSameCamp(excuteFightParam.getAttaId(), (int)role.getRoleId())) {
                    for (CombatAddObject combatAddObject : excuteFightParam.getCombatAddObjects()) {
                        combatAddObject.setPosition(combatAddObject.getPosition() + 10000);
                    }
                }
                else {
                    continue;
                }
            }
        }
    }
    
    public void excuteRoundStartSend(Fight fight) {
        if (fight == null) {
            return;
        }
        Map<Integer, RespRoundStartParam> respRoundStartParamMap = fight.getRespRoundStartParamMap();
        long millis = System.currentTimeMillis();
        RoundParam roundParam = new RoundParam(fight.getRound());
        roundParam.setBeganTime(millis);
        fight.setRoundParam(roundParam);
        boolean isSaveRecord = true;
        RespRoundStartParam respRoundStartParamRecord = null;
        StringBuffer sendRoleSB = new StringBuffer("[");
        for (FightObject fightObject : fight.getAllFightObjects()) {
            if (!fightObject.isPlayer()) {
                continue;
            }
            Role role = fightObject.getRole();
            if (role == null) {
                continue;
            }
            RespRoundStartParam respRoundStartParam = (RespRoundStartParam)respRoundStartParamMap.get(fightObject.getId());
            if (respRoundStartParam != null) {
                respRoundStartParam.setStartTime(String.valueOf(millis));
                FightMessageUtil.sendFightPackage((long)role.getId(), respRoundStartParam);
                sendRoleSB.append(fightObject.getId() + ",");
                if (isSaveRecord) {
                    respRoundStartParamRecord = respRoundStartParam;
                    isSaveRecord = false;
                }
                else {
                    continue;
                }
            }
            else {
                continue;
            }
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
            FightSpectatorNewService fightSpectatorService = (FightSpectatorNewService)SpringUtils.getBean(FightSpectatorNewService.class);
            ((FightSpectatorExecutorNewService)SpringUtils.getBean(FightSpectatorExecutorNewService.class)).addFightTask(fight.getId(), () -> fightSpectatorService.sendWatchFightData(fight, (byte)1));
        }
        fight.setRespRoundStartParamMap(new HashMap());
        return;
    }
    
    public void excuteFightInSend(Fight fight) {
        if (fight == null || fight.getFightInParams() == null) {
            return;
        }
        List<ExcuteFightParam> excuteFightParams = null;
        if (fight.getActionParam().getActionDetailParams().size() > 0) {
            excuteFightParams = this.fightMessageNewService.getExcuteFightParams(fight.getActionParam().getActionDetailParams());
        }
        boolean isSaveRecord = true;
        RespFightInParam fightInParamRecord = null;
        StringBuffer sendRoleSB = new StringBuffer("[");
        for (int i = 0; i < fight.getFightInParams().size(); ++i) {
            RespFightInParam fightInParam = (RespFightInParam)fight.getFightInParams().get(i);
            if (excuteFightParams != null) {
                fightInParam.setExcuteFightParams(excuteFightParams);
            }
            long roleId = Long.parseLong(fightInParam.getId());
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
        return;
    }
    
    public List<Member> excuteMemberLeaveTemp(Role role, Team team) {
        List<Member> list = null;
        Optional<Member> memberOptional = team.getList().stream().filter(member -> member.getRoleId().equals(role.getRoleId()) && !member.isInTeam()).findFirst();
        if (memberOptional.isPresent()) {
        	Member member = (Member)memberOptional.get();
            Member newMember = new Member();
            BeanUtils.copyProperties(member, newMember);
            newMember.setInTeam(true);
            list = new ArrayList<>();
            list.add(newMember);
            return list;
        }
        list = team.getList();
        return list;
    }
    
    public Team getRoleFightTeam(Role role) {
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (!this.teamService.isInTeam(role)) {
            List<Member> arrayList = Collections.synchronizedList(new ArrayList<>());
            Member member = Member.valueOf(role);
            arrayList.add(member);
            team = new Team();
            team.setList(arrayList);
            ArrayList<Member> partnerMembers = ((PartnerService)SpringUtils.getBean(PartnerService.class)).partner2Member(role);
            team.setPartner(partnerMembers);
            return team;
        }
        return team;
    }
    
    public Map<String, Object> saveAutoFightSet(Role role, ReqAutoFightSetData reqAutoFightSetData) {
        int id = reqAutoFightSetData.getId();
        Map<String, Object> autoSet = new HashMap();
        autoSet.put("action", Integer.valueOf(reqAutoFightSetData.getAction()));
        autoSet.put("type", Integer.valueOf(reqAutoFightSetData.getType()));
        autoSet.put("skillId", Integer.valueOf(reqAutoFightSetData.getSkillId()));
        int born = 0;
        if ((int)role.getRace() == RACE.RACE_LONG.getValue()) {
            born = reqAutoFightSetData.getBorn();
        }
        autoSet.put("born", Integer.valueOf(born));
        if (role.getRoleId().equals(Integer.valueOf(id))) {
            role.getExtendBox().setAutoFightSet(autoSet);
        }
        else {
            Pet pet = (Pet)this.petService.selectPet(role).get(Integer.valueOf(id));
            if (pet != null) {
                pet.setAutoFightSet(autoSet);
            }
        }
        role.save();
        return autoSet;
    }
    
    public void getFightObjectAttrs(Role role, int fightId) {
        Fight fight = this.getFight(fightId);
        if (fight == null) {
            return;
        }
        Map<Integer, Integer> map = PRO_DICT_TYPE.toMap();
        List<FightObjectAttr> list = new ArrayList<>();
        for (FightObject obj : fight.getAllFightObjects()) {
            if (obj != null) {
                List<FiedValue> attrs = new ArrayList<>();
                for (Integer key : map.keySet()) {
                    double attr = obj.getSelfAllTempPro((int)key);
                    if (attr == 0.0) {
                        continue;
                    }
                    attrs.add(new FiedValue(key.toString(), (byte)3, Integer.valueOf((int)(attr * 1000.0))));
                }
                list.add(new FightObjectAttr((int)obj.getId(), attrs));
            }
            else {
                continue;
            }
        }
        if (list.size() > 0) {
            RespFightObjectAttr respFightObjectAttr = new RespFightObjectAttr();
            respFightObjectAttr.setList(list);
            FightMessageUtil.sendFightPackage((long)role.getId(), respFightObjectAttr);
        }
        return;
    }
}
