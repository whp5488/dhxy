
package com.kitty.system.game.fightnew.bean;

import java.beans.ConstructorProperties;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.task.AutoRoundFinishTask;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.service.FightMessageNewService;
import com.kitty.system.game.fightnew.messages.vo.ExcuteFightParam;
import com.kitty.system.game.fightnew.model.PetFightInParam;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.fightnew.model.SkillParam;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import java.util.function.Function;
import java.util.Comparator;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.definition.SkillConst;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.game.fightnew.utils.SkillTargetPicker;
import com.kitty.common.core.SchedulerManager;
import com.kitty.system.game.fightnew.task.TimeOutNewTask;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.fightnew.messages.old.RespEndCombat;
import com.kitty.system.game.fightnew.service.FightExecutorNewService;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import java.util.Collection;
import java.util.Iterator;
import com.kitty.common.utils.spring.SpringUtils;
import java.util.HashSet;
import java.util.HashMap;
import com.kitty.system.enums.war.wardef.WarMaxRound;
import java.util.Collections;
import com.kitty.logs.LoggerFunction;
import com.kitty.system.common.enter.FieldValue;
import com.kitty.system.game.fightnew.spectator.domain.SpectatorObject;
import java.util.concurrent.ConcurrentHashMap;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.fightnew.model.AnimationPlayParam;
import java.util.Set;
import com.kitty.system.game.fightnew.messages.RespRoundStartParam;
import com.kitty.system.game.fightnew.model.ActionParam;
import com.kitty.system.game.fightnew.messages.RespFightInParam;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.messages.vo.FightMember;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.FightObject;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;

public class Fight
{
    protected Logger logger;
    private static final AtomicInteger idFactory;
    private int id;
    private long recordId;
    private int leaderId;
    private long createTime;
    private List<FightObject> listA;
    private List<FightObject> listB;
    private ArrayList<FightMember> fightMemberA;
    private ArrayList<FightMember> fightMemberB;
    protected int mapId;
    private int type;
    private byte specialMonsterType;
    private int round;
    private RoundParam roundParam;
    private int maxRound;
    private int taskId;
    private NutMap expMap;
    private NutMap bossReward;
    protected int npcId;
    private NutMap data;
    private ScheduledFuture<?> timeOutTask;
    private Map<Boolean, AstrictSkill> astrictSkillMap;
    private Map<Boolean, BeforeActionSkill> beforeActionMap;
    private Map<Boolean, Map<Integer, Integer>> reduceRatioMap;
    private Map<Boolean, Map<Integer, Double>> ObjectAttributeMap;
    private NutMap monsterAstrict;
    private List<RespFightInParam> fightInParams;
    private ActionParam actionParam;
    private Map<Integer, RespRoundStartParam> respRoundStartParamMap;
    private Set<Integer> isCompleted;
    private List<Integer> fleeList;
    private Map<Integer, FightObject> fightActors;
    private Set<Integer> deadPetIds;
    private byte fightType;
    private int rewardType;
    private int roundType;
    private Map<Integer, ScheduledFuture<?>> fightId_timer_host;
    private Map<Integer, ScheduledFuture<?>> roundFinish_timer_player;
    private Map<Integer, ScheduledFuture<?>> fightId_timer_pet;
    private Map<Integer, AnimationPlayParam> animationPlayMap;
    protected FightNewService fightNewService;
    private boolean watchFightOnOff;
    private ConcurrentHashMap<Long, SpectatorObject> spectatorObjectMap;
    private byte fightOverReason;
    private byte winSide;
    private Map<Boolean, Set<FieldValue>> exitRoles;
    
    public Fight(final int type) {
        this.logger = LoggerFunction.FIGHT.getLogger();
        this.listA = Collections.synchronizedList(new ArrayList<FightObject>());
        this.listB = Collections.synchronizedList(new ArrayList<FightObject>());
        this.fightMemberA = new ArrayList<FightMember>();
        this.fightMemberB = new ArrayList<FightMember>();
        this.round = 1;
        this.roundParam = new RoundParam(this.round);
        this.maxRound = WarMaxRound.WAR_MAX_ROUND.getValue();
        this.astrictSkillMap = new HashMap<Boolean, AstrictSkill>();
        this.beforeActionMap = new HashMap<Boolean, BeforeActionSkill>();
        this.reduceRatioMap = new HashMap<Boolean, Map<Integer, Integer>>();
        this.ObjectAttributeMap = new HashMap<Boolean, Map<Integer, Double>>();
        this.monsterAstrict = NutMap.NEW();
        this.fightInParams = Collections.synchronizedList(new ArrayList<RespFightInParam>());
        this.actionParam = new ActionParam();
        this.respRoundStartParamMap = new HashMap<Integer, RespRoundStartParam>();
        this.isCompleted = new HashSet<Integer>();
        this.fleeList = new ArrayList<Integer>();
        this.fightActors = new HashMap<Integer, FightObject>();
        this.deadPetIds = new HashSet<Integer>();
        this.fightType = 0;
        this.rewardType = -1;
        this.fightId_timer_host = new HashMap<Integer, ScheduledFuture<?>>();
        this.roundFinish_timer_player = new HashMap<Integer, ScheduledFuture<?>>();
        this.fightId_timer_pet = new HashMap<Integer, ScheduledFuture<?>>();
        this.animationPlayMap = new HashMap<Integer, AnimationPlayParam>();
        this.fightNewService = SpringUtils.getBean(FightNewService.class);
        this.watchFightOnOff = false;
        this.spectatorObjectMap = new ConcurrentHashMap<Long, SpectatorObject>();
        this.fightOverReason = 0;
        this.winSide = 0;
        this.exitRoles = new HashMap<Boolean, Set<FieldValue>>();
        this.id = Fight.idFactory.getAndIncrement();
        this.createTime = System.currentTimeMillis();
        this.type = type;
    }
    
    public ArrayList<FightObject> getAllFightObjects() {
        final ArrayList<FightObject> container = new ArrayList<FightObject>();
        for (final FightObject object : this.listA) {
            if (object != null) {
                container.add(object);
            }
        }
        for (final FightObject object : this.listB) {
            if (object != null) {
                container.add(object);
            }
        }
        return container;
    }
    
    private boolean isCompleted(final int round) {
        final boolean boo = this.isCompleted.contains(round);
        if (!boo) {
            this.isCompleted.add(round);
        }
        return boo;
    }
    
    public ArrayList<FightObject> getSideAllFightObjects(final boolean isASide) {
        final ArrayList<FightObject> container = new ArrayList<FightObject>();
        if (isASide) {
            container.addAll(this.listA);
        }
        else {
            container.addAll(this.listB);
        }
        return container;
    }
    
    public FightObject queryFightObject(final Integer roleId) {
        if (this.getListA() != null) {
            for (final FightObject fightObject : this.getListA()) {
                if (fightObject.getId().equals(roleId)) {
                    return fightObject;
                }
            }
        }
        if (this.getListB() != null) {
            for (final FightObject fightObject : this.getListB()) {
                if (fightObject.getId().equals(roleId)) {
                    return fightObject;
                }
            }
        }
        return null;
    }
    
    public FightObject getAttackTarget(final FightObject effector) {
        final List<FightObject> targets = effector.isASide() ? this.getListB() : this.getListA();
        for (final FightObject target : targets) {
            if (!target.isFallDown()) {
                return target;
            }
        }
        return null;
    }
    
    public FightObject getAttackSelf(final FightObject effector) {
        final List<FightObject> targets = effector.isASide() ? this.getListA() : this.getListB();
        for (final FightObject target : targets) {
            if (!target.isFallDown()) {
                return target;
            }
        }
        return null;
    }
    
    public boolean isSameSide(final FightObject o1, final FightObject o2) {
        return o1.isASide() == o2.isASide();
    }
    
    public List<FightObject> getMySide(final int roleId) {
        for (final FightObject fightObject : this.getListA()) {
            if (fightObject.getId() == roleId) {
                return this.getListA();
            }
        }
        return this.getListB();
    }
    
    public List<FightObject> getTargetSide(final int roleId) {
        for (final FightObject fightObject : this.getListA()) {
            if (fightObject.getId() == roleId) {
                return this.getListB();
            }
        }
        return this.getListA();
    }
    
    public boolean isWinner(final int roleId) {
        if (this.getFightOverReason() != 1) {
            final List<FightObject> mySide = this.getMySide(roleId);
            for (final FightObject fightObject : mySide) {
                if (fightObject.getCurrLife() > 0) {
                    return true;
                }
            }
            return false;
        }
        final FightObject fightObject2 = this.queryFightObject(roleId);
        if (this.winSide == 0) {
            return this.defineWin(fightObject2);
        }
        if (this.winSide == 1) {
            return fightObject2.isASide();
        }
        return !fightObject2.isASide();
    }
    
    public boolean isWinner(final int roleId, final boolean isA) {
        if (this.getFightOverReason() != 1) {
            List<FightObject> mySide;
            if (isA) {
                mySide = this.listA;
            }
            else {
                mySide = this.listB;
            }
            for (final FightObject fightObject : mySide) {
                if (fightObject.getCurrLife() > 0) {
                    return true;
                }
            }
            return false;
        }
        final FightObject fightObject2 = this.queryFightObject(roleId);
        if (this.winSide == 0) {
            return this.defineWin(roleId, isA);
        }
        if (this.winSide == 1) {
            return isA;
        }
        return !isA;
    }
    
    public boolean isStartFight() {
        for (final FightObject fightObject : this.getListA()) {
            if (!fightObject.isDoAction()) {
                return false;
            }
        }
        for (final FightObject fightObject : this.getListB()) {
            if (!fightObject.isDoAction()) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isOver() {
        return this.isSideOver(this.getListA()) || this.isSideOver(this.getListB()) || (this.getData() != null && this.getData().getBoolean("fight_result_fail", false)) || (this.getData() != null && this.getData().getBoolean("fight_result_success", false));
    }
    
    public boolean isSideOver(final List<FightObject> members) {
        if (members != null && members.size() > 0) {
            for (int i = 0; i < members.size(); ++i) {
                final FightObject fightObject = members.get(i);
                if (fightObject != null && !fightObject.isFallDown()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void addFightObject(final FightObject addObject) {
        this.fightActors.put(addObject.getId(), addObject);
        for (final FightObject object : this.getListA()) {
            if (object.getId() == addObject.getHostId()) {
                this.getListA().add(addObject);
                return;
            }
        }
        for (final FightObject fightObject : this.getListB()) {
            if (fightObject.getId() == addObject.getHostId()) {
                this.getListB().add(addObject);
            }
        }
    }
    
    public void removeFightObject(final FightObject fightObject) {
        if (this.getType() == 25 && fightObject.isPet() && fightObject.getPos() == 8) {
            if (this.getData() == null) {
                this.setData(NutMap.NEW());
            }
            this.getData().setv("fight_result_fail", (Object)true);
        }
        this.removeFightObject(this.getListA(), fightObject);
        this.removeFightObject(this.getListB(), fightObject);
    }
    
    private void removeFightObject(final List<FightObject> pool, final FightObject toRemove) {
        pool.removeIf(next -> next.getId().equals(toRemove.getId()));
    }
    
    public FightObject queryFightObjectAnyWay(final int objId) {
        return this.fightActors.get(objId);
    }
    
    public void destroy() {
        FightParamPool.fightDestroy(this);
        for (final FightObject fightObject : this.getAllFightObjects()) {
            if (fightObject != null && fightObject.isPlayer()) {
                final Role role = SpringUtils.getRoleService().getPlayer(fightObject.getRole().getId());
                if (role != null) {
                    role.setPrevFightEndTime(System.currentTimeMillis());
                    role.setCanFight(true);
                    this.removeRoundFinishTask(role.getRoleId());
                }
            }
            final Integer temp = FightExecutorNewService.FIGHT_ROOMS.get(fightObject.getId());
            if (temp == null) {
                continue;
            }
        }
        if (this.watchFightOnOff && this.spectatorObjectMap.size() > 0) {
            this.setWatchFightOnOff(false);
            final RespEndCombat respEndCombat = new RespEndCombat();
            for (final Map.Entry<Long, SpectatorObject> entry : this.spectatorObjectMap.entrySet()) {
                MessagePusher.pushMessage(entry.getKey(), respEndCombat);
            }
        }
        FightExecutorNewService.FIGHTS.remove(this.getId());
        this.cancelTask(this.timeOutTask);
    }
    
    public void addHostTask(final int roleId, final ScheduledFuture<?> task) {
        this.removeHostTask(roleId);
        this.fightId_timer_host.put(roleId, task);
    }
    
    public void removeHostTask(final int roleId) {
        final ScheduledFuture<?> task = this.fightId_timer_host.get(roleId);
        if (task != null) {
            this.cancelTask(task);
            this.fightId_timer_host.remove(roleId);
        }
    }
    
    public void addPetTask(final int roleId, final ScheduledFuture<?> task) {
        this.removePetTask(roleId);
        this.fightId_timer_pet.put(roleId, task);
    }
    
    public void removePetTask(final int memberId) {
        final ScheduledFuture<?> task = this.fightId_timer_pet.get(memberId);
        if (task != null) {
            this.cancelTask(task);
            this.fightId_timer_host.remove(memberId);
        }
    }
    
    public void cancelTask(final ScheduledFuture<?> task) {
        if (task != null && !task.isDone()) {
            task.cancel(Boolean.FALSE);
        }
    }
    
    public void addRoundFinishTask(final int objId, final ScheduledFuture<?> task) {
        this.removeRoundFinishTask(objId);
        this.roundFinish_timer_player.put(objId, task);
    }
    
    public void removeRoundFinishTask(final int objId) {
        final ScheduledFuture<?> task = this.roundFinish_timer_player.get(objId);
        if (task != null) {
            this.cancelTask(task);
            this.roundFinish_timer_player.remove(objId);
        }
    }
    
    public void start() {
        final FightExecutorNewService fightExecutorService = SpringUtils.getBean(FightExecutorNewService.class);
        fightExecutorService.addFightTask(this, () -> {
            try {
                this.loadActors();
                this.notifyAndStart();
                for (FightObject object : getListA())
                    this.fightActors.put(object.getId(), object);
                for (FightObject object : getListB())
                    this.fightActors.put(object.getId(), object);
                SpringUtils.getBean(FightNewService.class).excuteFightInSend(this);
                SpringUtils.getBean(FightNewService.class).excuteRoundStartSend(this);
                this.setRoundType(1);
            }
            catch (final Exception e) {
                LoggerFunction.FIGHT.getLogger().error(e.getMessage(), e);
                LoggerFunction.TASK.getLogger().error(e.getMessage(), e);
            }
            return;
        });
        SpringUtils.getBean(FightNewService.class).besure(this);
        final TimeOutNewTask timeOut = new TimeOutNewTask(this.getId());
        final ScheduledFuture<?> timeOutTask = SchedulerManager.getInstance().schedule(timeOut, 30000L);
        this.setTimeOutTask(timeOutTask);
    }
    
    protected void loadActors() {
    }
    
    protected void notifyAndStart() {
    }
    
    public boolean isPkFight() {
        return false;
    }
    
    public int getMaxFightObjectId() {
        int maxId = 0;
        final ArrayList<FightObject> allFightObjects = this.getAllFightObjects();
        for (final FightObject fightObject : allFightObjects) {
            if (fightObject.getId() > maxId) {
                maxId = fightObject.getId();
            }
        }
        return maxId;
    }
    
    public FightObject getTargetNoCurrentTarget(final int attackerId, final int oldTargetId) {
        final FightObject attactor = this.queryFightObject(attackerId);
        List<FightObject> targetSideList = null;
        if (oldTargetId > 0) {
            targetSideList = SkillTargetPicker.getFightObjectsOldTarget(this, attactor, oldTargetId);
        }
        else {
            targetSideList = SkillTargetPicker.getFightObjects(this, attactor, true);
        }
        if (targetSideList == null || targetSideList.size() <= 0) {
            return null;
        }
        targetSideList = targetSideList.stream().filter(obj -> obj.getId() != oldTargetId && obj.getId() != attackerId).collect(Collectors.toList());
        if (targetSideList.size() <= 0) {
            return null;
        }
        final Random random = new Random();
        final int index = random.nextInt(targetSideList.size());
        return targetSideList.get(index);
    }
    
    public void clearAstrictSkills() {
        if (this.getAstrictSkillMap().get(true) != null) {
            this.getAstrictSkillMap().get(true).getSkillMap().clear();
        }
        if (this.getAstrictSkillMap().get(false) != null) {
            this.getAstrictSkillMap().get(false).getSkillMap().clear();
        }
    }
    
    public void excuteReduceRatio(final FightObject fightObject, final boolean isExcute) {
        if (fightObject == null) {
            return;
        }
        final List<SysPetSkill> petSkillList = PetTriggerPassiveSkill.normalAttack(PET_PASSIVE_SKILL_TRIGGER_DICT.PET_BE.getValue(), fightObject.getSkillIds(), this.isPvp());
        SkillProcessor skillProcessor = null;
        if (petSkillList.size() > 0) {
            for (final SysPetSkill petSkill : petSkillList) {
                skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                if (skillProcessor != null) {
                    skillProcessor.passive(this, fightObject, isExcute);
                }
            }
        }
        for (final Integer skillId : SkillConst.getPetBeSkills()) {
            for (final RoleSkill roleSkill : fightObject.getSkillIds()) {
                if (roleSkill.getSkillId() > 0 && roleSkill.getSkillId().equals(skillId)) {
                    skillProcessor = SkillProcessor.queryProcessor(skillId);
                    if (skillProcessor == null) {
                        continue;
                    }
                    skillProcessor.death(this, fightObject, fightObject, new Object[0]);
                }
            }
        }
    }
    
    public boolean diedTriggerSkill(final FightObject fightObject, final String type) {
        if (fightObject == null) {
            return false;
        }
        boolean isRever = false;
        List<SysPetSkill> petSkillList = PetTriggerPassiveSkill.petExit(type, fightObject.getSkillIds(), this.isPvp());
        SkillProcessor skillProcessor = null;
        if (petSkillList.size() > 0) {
            petSkillList = petSkillList.stream().sorted(Comparator.comparing(SysPetSkill::getSerial)).collect(Collectors.toList());
            for (final SysPetSkill petSkill : petSkillList) {
                skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                final NutMap passive = skillProcessor.passive(this, fightObject, petSkill);
                if (passive != null && passive.getInt(String.valueOf(41)) > 0) {
                    isRever = true;
                }
            }
        }
        return isRever;
    }
    
    public List<FightObject> queryAnomalousObject(final FightObject effecter, final int num, final List<Integer> logics) {
        final List<FightObject> returnObj = new ArrayList<FightObject>();
        List<FightObject> myFightObjects = this.getListA();
        if (!effecter.isASide()) {
            myFightObjects = this.getListB();
        }
        Collections.sort(myFightObjects);
        final Iterator<FightObject> it = myFightObjects.iterator();
        FightObject obj = null;
        int count = 0;
        while (it.hasNext()) {
            obj = it.next();
            if (!logics.contains(obj.getLogic())) {
                continue;
            }
            boolean isAdd = false;
            if (obj.getStatusMap().getInt(String.valueOf(1002)) > 0) {
                isAdd = true;
            }
            if (obj.getStatusMap().getInt(String.valueOf(1003)) > 0) {
                isAdd = true;
            }
            if (obj.getStatusMap().getInt(String.valueOf(1004)) > 0) {
                isAdd = true;
            }
            if (obj.getStatusMap().getInt(String.valueOf(1006)) > 0) {
                isAdd = true;
            }
            if (obj.getStatusMap().getInt(String.valueOf(1019)) > 0) {
                isAdd = true;
            }
            if (isAdd) {
                ++count;
                returnObj.add(obj);
            }
            if (count >= num) {
                break;
            }
        }
        return returnObj;
    }
    
    public int isPvp() {
        int isPvp = 0;
        if (1 == FIGHT_TYPE.getType(this.getType())) {
            isPvp = 1;
        }
        return isPvp;
    }
    
    public void excuteExitPassive(final FightObject actor) {
        try {
            this.diedTriggerSkill(actor, PET_PASSIVE_SKILL_TRIGGER_DICT.PET_EXIT.getValue());
            this.excuteReduceRatio(actor, false);
            final List<FightObject> otherPets = this.getMySide(actor.getId()).stream().filter(obj -> !obj.isFallDown() && obj.getLogic() == LOGIC.LOGICTYPE_PET.getValue() && !obj.getId().equals(actor.getId())).collect(Collectors.toList());
            for (final FightObject otherPet : otherPets) {
                this.diedTriggerSkill(otherPet, PET_PASSIVE_SKILL_TRIGGER_DICT.NON_SELF_PET_EXIT.getValue());
            }
            PetSkillEffectorUtil.sendCancelAction(this, actor);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public RespFightInParam getRoleFightInParam(final Role role) {
        RespFightInParam fightInParam = null;
        for (int i = 0; i < this.fightInParams.size(); ++i) {
            if (this.fightInParams.get(i).getRoleId() == role.getRoleId()) {
                fightInParam = this.fightInParams.get(i);
                break;
            }
        }
        if (fightInParam == null) {
            fightInParam = new RespFightInParam();
            fightInParam.setId(String.valueOf(role.getId()));
            final List<SkillParam> skillParams = this.getSkillParams(role);
            if (skillParams != null) {
                fightInParam.setSkillParams(skillParams);
            }
            fightInParam.setRoleId(role.getRoleId());
            this.fightInParams.add(fightInParam);
        }
        return fightInParam;
    }
    
    public PetFightInParam addPetFightInParam(final Role role, final Pet pet) {
        PetFightInParam petFightInParam = null;
        RespFightInParam fightInParam = null;
        int i = 0;
        while (i < this.fightInParams.size()) {
            if (this.fightInParams.get(i).getRoleId() == role.getRoleId()) {
                fightInParam = this.fightInParams.get(i);
                if (fightInParam.getPetFightInParam() != null && fightInParam.getPetFightInParam().size() > 0) {
                    for (final PetFightInParam petFightInParam2 : fightInParam.getPetFightInParam()) {
                        if (pet.getPetId() == petFightInParam2.getPetId()) {
                            petFightInParam = petFightInParam2;
                        }
                    }
                    break;
                }
                break;
            }
            else {
                ++i;
            }
        }
        if (fightInParam == null) {
            fightInParam = new RespFightInParam();
            fightInParam.setId(String.valueOf(role.getId()));
            final List<SkillParam> skillParams = this.getSkillParams(role);
            if (skillParams != null) {
                fightInParam.setSkillParams(skillParams);
            }
            fightInParam.setRoleId(role.getRoleId());
            this.fightInParams.add(fightInParam);
        }
        if (petFightInParam == null) {
            petFightInParam = this.getPetFightParam(pet);
            fightInParam.addPetFightInParam(petFightInParam);
        }
        return petFightInParam;
    }
    
    public PetFightInParam getPetFightParam(final Pet pet) {
        final PetFightInParam petFightInParam = new PetFightInParam();
        petFightInParam.setPetId(pet.getPetId());
        petFightInParam.addSkillIds(pet);
        return petFightInParam;
    }
    
    public void onActorDie(final FightObject actor) {
        if (actor.getLogic() == LOGIC.LOGICTYPE_HERO.getValue()) {
            final FightObject object = this.queryFightObject(actor.getFightPetId());
            if (object != null) {}
        }
    }
    
    public void petDie(final FightObject actor) {
        if (actor.isPet() && !this.deadPetIds.contains(actor.getId())) {
            this.deadPetIds.add(actor.getId());
        }
        this.removeFightObject(actor);
    }
    
    public List<SkillParam> getSkillParams(final Role role) {
        final Map<Integer, RoleSkill> skills = role.getSkills();
        if (skills != null && skills.size() > 0) {
            final List<SkillParam> skillParams = new ArrayList<SkillParam>();
            for (final Map.Entry<Integer, RoleSkill> entry : skills.entrySet()) {
                final RoleSkill value = entry.getValue();
                if (value != null) {
                    final SkillParam skillParam = new SkillParam();
                    skillParam.setSkillId(value.getSkillId());
                    skillParam.setProficient(value.getProficient() / 100);
                    skillParams.add(skillParam);
                }
            }
            return skillParams;
        }
        return null;
    }
    
    public boolean petDeadOnce(final int petId) {
        return this.deadPetIds.contains(petId);
    }
    
    public List<ExcuteFightParam> getFightStatus() {
        final List<ActionDetailParam> actionDetailParams = PetSkillEffectorUtil.checkActionForPrivate(this);
        if (actionDetailParams != null && actionDetailParams.size() > 0) {
            final List<ExcuteFightParam> excuteFightParams = SpringUtils.getBean(FightMessageNewService.class).getExcuteFightParams(actionDetailParams);
            return excuteFightParams;
        }
        return null;
    }
    
    public void roundFinishTask(final FightObject fightObject, final long time) {
        final AutoRoundFinishTask autoRoundFinishTask = new AutoRoundFinishTask(this, fightObject.getId());
        final ScheduledFuture<?> timeOut = SchedulerManager.getInstance().schedule(autoRoundFinishTask, time);
        this.addRoundFinishTask(fightObject.getId(), timeOut);
    }
    
    public boolean defineWin(final FightObject fightObject) {
        final int isWin = this.roundExceeded(fightObject.getId());
        boolean flag = false;
        if (isWin == 2) {
            flag = SkillAi.canAffectOnPro(0.5);
        }
        else if (isWin == 1) {
            flag = true;
        }
        if (flag) {
            if (fightObject.isASide()) {
                this.setWinSide((byte)1);
            }
            else {
                this.setWinSide((byte)2);
            }
        }
        else if (fightObject.isASide()) {
            this.setWinSide((byte)2);
        }
        else {
            this.setWinSide((byte)1);
        }
        return flag;
    }
    
    public boolean defineWin(final int roleId, final boolean isA) {
        final int isWin = this.roundExceeded(roleId);
        boolean flag = false;
        if (isWin == 2) {
            flag = SkillAi.canAffectOnPro(0.5);
        }
        else if (isWin == 1) {
            flag = true;
        }
        if (flag) {
            if (isA) {
                this.setWinSide((byte)1);
            }
            else {
                this.setWinSide((byte)2);
            }
        }
        else if (isA) {
            this.setWinSide((byte)2);
        }
        else {
            this.setWinSide((byte)1);
        }
        return flag;
    }
    
    public byte roundExceeded(final int roleId) {
        final byte returnFlag = 2;
        final List<FightObject> mySide = this.getMySide(roleId);
        final List<FightObject> targetSide = this.getTargetSide(roleId);
        int mySideNum = 0;
        int mySideCurrLife = 0;
        int mySideMaxLife = 0;
        for (final FightObject fightObject : mySide) {
            if (fightObject != null && !fightObject.isFallDown()) {
                ++mySideNum;
            }
            mySideCurrLife += fightObject.getCurrLife();
            mySideMaxLife += fightObject.getMaxLife();
        }
        int targetSideNum = 0;
        int targetSideCurrLife = 0;
        int targetSideMaxLife = 0;
        for (final FightObject fightObject2 : targetSide) {
            if (fightObject2 != null && !fightObject2.isFallDown()) {
                ++targetSideNum;
            }
            targetSideCurrLife += fightObject2.getCurrLife();
            targetSideMaxLife += fightObject2.getMaxLife();
        }
        if (mySideNum > targetSideNum) {
            return 1;
        }
        if (mySideNum < targetSideNum) {
            return 0;
        }
        int myDefeatPet = 0;
        for (final FightObject fightObject3 : mySide) {
            if (fightObject3.isPlayer()) {
                myDefeatPet += FightParamPool.getDeathPetNum(this.getId(), fightObject3.getId());
            }
        }
        int targetDefeatPet = 0;
        for (final FightObject fightObject4 : targetSide) {
            if (fightObject4.isPlayer()) {
                targetDefeatPet += FightParamPool.getDeathPetNum(this.getId(), fightObject4.getId());
            }
        }
        if (myDefeatPet < targetDefeatPet) {
            return 1;
        }
        if (myDefeatPet > targetDefeatPet) {
            return 0;
        }
        if (mySideMaxLife > 0 && targetSideMaxLife > 0) {
            final String mySidePCTStr = BigDecimalUtil.divide(String.valueOf(mySideCurrLife), String.valueOf(mySideMaxLife), 4);
            final String targetSidePCTStr = BigDecimalUtil.divide(String.valueOf(targetSideCurrLife), String.valueOf(targetSideMaxLife), 4);
            final Double mySidePCT = Double.valueOf(mySidePCTStr);
            final Double targetSidePCT = Double.valueOf(targetSidePCTStr);
            if (mySidePCT > targetSidePCT) {
                return 1;
            }
            if (mySidePCT < targetSidePCT) {
                return 0;
            }
        }
        int myControlNum = 0;
        for (final FightObject fightObject5 : mySide) {
            if (fightObject5.isPlayer()) {
                myControlNum += FightParamPool.getControlNum(this.getId(), fightObject5.getId());
            }
        }
        int targetControlNum = 0;
        for (final FightObject fightObject6 : targetSide) {
            if (fightObject6.isPlayer()) {
                targetControlNum += FightParamPool.getControlNum(this.getId(), fightObject6.getId());
            }
        }
        if (myControlNum < targetControlNum) {
            return 1;
        }
        if (myControlNum > targetControlNum) {
            return 0;
        }
        return returnFlag;
    }
    
    public void addExitRole(final Boolean isA, final FieldValue fieldValue) {
        Set<FieldValue> fieldValues = this.exitRoles.get(isA);
        if (fieldValues == null) {
            fieldValues = new HashSet<FieldValue>();
            this.exitRoles.put(isA, fieldValues);
        }
        fieldValues.add(fieldValue);
    }
    
    public boolean isSameCamp(final int objIdA, final int objIdB) {
        final boolean isInA = this.isSameCamp(this.listA, objIdA, objIdB);
        if (isInA) {
            return true;
        }
        final boolean isInB = this.isSameCamp(this.listB, objIdA, objIdB);
        return isInB;
    }
    
    private boolean isSameCamp(final List<FightObject> list, final int objIdA, final int objIdB) {
        if (list != null && list.size() > 0) {
            int flag = 0;
            for (final FightObject obj : list) {
                if (obj.getId().equals(objIdA)) {
                    ++flag;
                }
                if (obj.getId().equals(objIdB)) {
                    ++flag;
                }
            }
            if (flag == 2) {
                return true;
            }
            if (flag == 1) {
                return false;
            }
        }
        return false;
    }
    
    public void setLogger(final Logger logger) {
        this.logger = logger;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setRecordId(final long recordId) {
        this.recordId = recordId;
    }
    
    public void setLeaderId(final int leaderId) {
        this.leaderId = leaderId;
    }
    
    public void setCreateTime(final long createTime) {
        this.createTime = createTime;
    }
    
    public void setListA(final List<FightObject> listA) {
        this.listA = listA;
    }
    
    public void setListB(final List<FightObject> listB) {
        this.listB = listB;
    }
    
    public void setFightMemberA(final ArrayList<FightMember> fightMemberA) {
        this.fightMemberA = fightMemberA;
    }
    
    public void setFightMemberB(final ArrayList<FightMember> fightMemberB) {
        this.fightMemberB = fightMemberB;
    }
    
    public void setMapId(final int mapId) {
        this.mapId = mapId;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setSpecialMonsterType(final byte specialMonsterType) {
        this.specialMonsterType = specialMonsterType;
    }
    
    public void setRound(final int round) {
        this.round = round;
    }
    
    public void setRoundParam(final RoundParam roundParam) {
        this.roundParam = roundParam;
    }
    
    public void setMaxRound(final int maxRound) {
        this.maxRound = maxRound;
    }
    
    public void setTaskId(final int taskId) {
        this.taskId = taskId;
    }
    
    public void setExpMap(final NutMap expMap) {
        this.expMap = expMap;
    }
    
    public void setBossReward(final NutMap bossReward) {
        this.bossReward = bossReward;
    }
    
    public void setNpcId(final int npcId) {
        this.npcId = npcId;
    }
    
    public void setData(final NutMap data) {
        this.data = data;
    }
    
    public void setTimeOutTask(final ScheduledFuture<?> timeOutTask) {
        this.timeOutTask = timeOutTask;
    }
    
    public void setAstrictSkillMap(final Map<Boolean, AstrictSkill> astrictSkillMap) {
        this.astrictSkillMap = astrictSkillMap;
    }
    
    public void setBeforeActionMap(final Map<Boolean, BeforeActionSkill> beforeActionMap) {
        this.beforeActionMap = beforeActionMap;
    }
    
    public void setReduceRatioMap(final Map<Boolean, Map<Integer, Integer>> reduceRatioMap) {
        this.reduceRatioMap = reduceRatioMap;
    }
    
    public void setObjectAttributeMap(final Map<Boolean, Map<Integer, Double>> ObjectAttributeMap) {
        this.ObjectAttributeMap = ObjectAttributeMap;
    }
    
    public void setMonsterAstrict(final NutMap monsterAstrict) {
        this.monsterAstrict = monsterAstrict;
    }
    
    public void setFightInParams(final List<RespFightInParam> fightInParams) {
        this.fightInParams = fightInParams;
    }
    
    public void setActionParam(final ActionParam actionParam) {
        this.actionParam = actionParam;
    }
    
    public void setRespRoundStartParamMap(final Map<Integer, RespRoundStartParam> respRoundStartParamMap) {
        this.respRoundStartParamMap = respRoundStartParamMap;
    }
    
    public void setIsCompleted(final Set<Integer> isCompleted) {
        this.isCompleted = isCompleted;
    }
    
    public void setFleeList(final List<Integer> fleeList) {
        this.fleeList = fleeList;
    }
    
    public void setFightActors(final Map<Integer, FightObject> fightActors) {
        this.fightActors = fightActors;
    }
    
    public void setDeadPetIds(final Set<Integer> deadPetIds) {
        this.deadPetIds = deadPetIds;
    }
    
    public void setFightType(final byte fightType) {
        this.fightType = fightType;
    }
    
    public void setRewardType(final int rewardType) {
        this.rewardType = rewardType;
    }
    
    public void setRoundType(final int roundType) {
        this.roundType = roundType;
    }
    
    public void setFightId_timer_host(final Map<Integer, ScheduledFuture<?>> fightId_timer_host) {
        this.fightId_timer_host = fightId_timer_host;
    }
    
    public void setRoundFinish_timer_player(final Map<Integer, ScheduledFuture<?>> roundFinish_timer_player) {
        this.roundFinish_timer_player = roundFinish_timer_player;
    }
    
    public void setFightId_timer_pet(final Map<Integer, ScheduledFuture<?>> fightId_timer_pet) {
        this.fightId_timer_pet = fightId_timer_pet;
    }
    
    public void setAnimationPlayMap(final Map<Integer, AnimationPlayParam> animationPlayMap) {
        this.animationPlayMap = animationPlayMap;
    }
    
    public void setFightNewService(final FightNewService fightNewService) {
        this.fightNewService = fightNewService;
    }
    
    public void setWatchFightOnOff(final boolean watchFightOnOff) {
        this.watchFightOnOff = watchFightOnOff;
    }
    
    public void setSpectatorObjectMap(final ConcurrentHashMap<Long, SpectatorObject> spectatorObjectMap) {
        this.spectatorObjectMap = spectatorObjectMap;
    }
    
    public void setFightOverReason(final byte fightOverReason) {
        this.fightOverReason = fightOverReason;
    }
    
    public void setWinSide(final byte winSide) {
        this.winSide = winSide;
    }
    
    public void setExitRoles(final Map<Boolean, Set<FieldValue>> exitRoles) {
        this.exitRoles = exitRoles;
    }
    
    public Logger getLogger() {
        return this.logger;
    }
    
    public int getId() {
        return this.id;
    }
    
    public long getRecordId() {
        return this.recordId;
    }
    
    public int getLeaderId() {
        return this.leaderId;
    }
    
    public long getCreateTime() {
        return this.createTime;
    }
    
    public List<FightObject> getListA() {
        return this.listA;
    }
    
    public List<FightObject> getListB() {
        return this.listB;
    }
    
    public ArrayList<FightMember> getFightMemberA() {
        return this.fightMemberA;
    }
    
    public ArrayList<FightMember> getFightMemberB() {
        return this.fightMemberB;
    }
    
    public int getMapId() {
        return this.mapId;
    }
    
    public int getType() {
        return this.type;
    }
    
    public byte getSpecialMonsterType() {
        return this.specialMonsterType;
    }
    
    public int getRound() {
        return this.round;
    }
    
    public RoundParam getRoundParam() {
        return this.roundParam;
    }
    
    public int getMaxRound() {
        return this.maxRound;
    }
    
    public int getTaskId() {
        return this.taskId;
    }
    
    public NutMap getExpMap() {
        return this.expMap;
    }
    
    public NutMap getBossReward() {
        return this.bossReward;
    }
    
    public int getNpcId() {
        return this.npcId;
    }
    
    public NutMap getData() {
        return this.data;
    }
    
    public ScheduledFuture<?> getTimeOutTask() {
        return this.timeOutTask;
    }
    
    public Map<Boolean, AstrictSkill> getAstrictSkillMap() {
        return this.astrictSkillMap;
    }
    
    public Map<Boolean, BeforeActionSkill> getBeforeActionMap() {
        return this.beforeActionMap;
    }
    
    public Map<Boolean, Map<Integer, Integer>> getReduceRatioMap() {
        return this.reduceRatioMap;
    }
    
    public Map<Boolean, Map<Integer, Double>> getObjectAttributeMap() {
        return this.ObjectAttributeMap;
    }
    
    public NutMap getMonsterAstrict() {
        return this.monsterAstrict;
    }
    
    public List<RespFightInParam> getFightInParams() {
        return this.fightInParams;
    }
    
    public ActionParam getActionParam() {
        return this.actionParam;
    }
    
    public Map<Integer, RespRoundStartParam> getRespRoundStartParamMap() {
        return this.respRoundStartParamMap;
    }
    
    public Set<Integer> getIsCompleted() {
        return this.isCompleted;
    }
    
    public List<Integer> getFleeList() {
        return this.fleeList;
    }
    
    public Map<Integer, FightObject> getFightActors() {
        return this.fightActors;
    }
    
    public Set<Integer> getDeadPetIds() {
        return this.deadPetIds;
    }
    
    public byte getFightType() {
        return this.fightType;
    }
    
    public int getRewardType() {
        return this.rewardType;
    }
    
    public int getRoundType() {
        return this.roundType;
    }
    
    public Map<Integer, ScheduledFuture<?>> getFightId_timer_host() {
        return this.fightId_timer_host;
    }
    
    public Map<Integer, ScheduledFuture<?>> getRoundFinish_timer_player() {
        return this.roundFinish_timer_player;
    }
    
    public Map<Integer, ScheduledFuture<?>> getFightId_timer_pet() {
        return this.fightId_timer_pet;
    }
    
    public Map<Integer, AnimationPlayParam> getAnimationPlayMap() {
        return this.animationPlayMap;
    }
    
    public FightNewService getFightNewService() {
        return this.fightNewService;
    }
    
    public boolean isWatchFightOnOff() {
        return this.watchFightOnOff;
    }
    
    public ConcurrentHashMap<Long, SpectatorObject> getSpectatorObjectMap() {
        return this.spectatorObjectMap;
    }
    
    public byte getFightOverReason() {
        return this.fightOverReason;
    }
    
    public byte getWinSide() {
        return this.winSide;
    }
    
    public Map<Boolean, Set<FieldValue>> getExitRoles() {
        return this.exitRoles;
    }
    
    @ConstructorProperties({ "logger", "id", "recordId", "leaderId", "createTime", "listA", "listB", "fightMemberA", "fightMemberB", "mapId", "type", "specialMonsterType", "round", "roundParam", "maxRound", "taskId", "expMap", "bossReward", "npcId", "data", "timeOutTask", "astrictSkillMap", "beforeActionMap", "reduceRatioMap", "ObjectAttributeMap", "monsterAstrict", "fightInParams", "actionParam", "respRoundStartParamMap", "isCompleted", "fleeList", "fightActors", "deadPetIds", "fightType", "rewardType", "roundType", "fightId_timer_host", "roundFinish_timer_player", "fightId_timer_pet", "animationPlayMap", "fightNewService", "watchFightOnOff", "spectatorObjectMap", "fightOverReason", "winSide", "exitRoles" })
    public Fight(final Logger logger, final int id, final long recordId, final int leaderId, final long createTime, final List<FightObject> listA, final List<FightObject> listB, final ArrayList<FightMember> fightMemberA, final ArrayList<FightMember> fightMemberB, final int mapId, final int type, final byte specialMonsterType, final int round, final RoundParam roundParam, final int maxRound, final int taskId, final NutMap expMap, final NutMap bossReward, final int npcId, final NutMap data, final ScheduledFuture<?> timeOutTask, final Map<Boolean, AstrictSkill> astrictSkillMap, final Map<Boolean, BeforeActionSkill> beforeActionMap, final Map<Boolean, Map<Integer, Integer>> reduceRatioMap, final Map<Boolean, Map<Integer, Double>> ObjectAttributeMap, final NutMap monsterAstrict, final List<RespFightInParam> fightInParams, final ActionParam actionParam, final Map<Integer, RespRoundStartParam> respRoundStartParamMap, final Set<Integer> isCompleted, final List<Integer> fleeList, final Map<Integer, FightObject> fightActors, final Set<Integer> deadPetIds, final byte fightType, final int rewardType, final int roundType, final Map<Integer, ScheduledFuture<?>> fightId_timer_host, final Map<Integer, ScheduledFuture<?>> roundFinish_timer_player, final Map<Integer, ScheduledFuture<?>> fightId_timer_pet, final Map<Integer, AnimationPlayParam> animationPlayMap, final FightNewService fightNewService, final boolean watchFightOnOff, final ConcurrentHashMap<Long, SpectatorObject> spectatorObjectMap, final byte fightOverReason, final byte winSide, final Map<Boolean, Set<FieldValue>> exitRoles) {
        this.logger = LoggerFunction.FIGHT.getLogger();
        this.listA = Collections.synchronizedList(new ArrayList<FightObject>());
        this.listB = Collections.synchronizedList(new ArrayList<FightObject>());
        this.fightMemberA = new ArrayList<FightMember>();
        this.fightMemberB = new ArrayList<FightMember>();
        this.round = 1;
        this.roundParam = new RoundParam(this.round);
        this.maxRound = WarMaxRound.WAR_MAX_ROUND.getValue();
        this.astrictSkillMap = new HashMap<Boolean, AstrictSkill>();
        this.beforeActionMap = new HashMap<Boolean, BeforeActionSkill>();
        this.reduceRatioMap = new HashMap<Boolean, Map<Integer, Integer>>();
        this.ObjectAttributeMap = new HashMap<Boolean, Map<Integer, Double>>();
        this.monsterAstrict = NutMap.NEW();
        this.fightInParams = Collections.synchronizedList(new ArrayList<RespFightInParam>());
        this.actionParam = new ActionParam();
        this.respRoundStartParamMap = new HashMap<Integer, RespRoundStartParam>();
        this.isCompleted = new HashSet<Integer>();
        this.fleeList = new ArrayList<Integer>();
        this.fightActors = new HashMap<Integer, FightObject>();
        this.deadPetIds = new HashSet<Integer>();
        this.fightType = 0;
        this.rewardType = -1;
        this.fightId_timer_host = new HashMap<Integer, ScheduledFuture<?>>();
        this.roundFinish_timer_player = new HashMap<Integer, ScheduledFuture<?>>();
        this.fightId_timer_pet = new HashMap<Integer, ScheduledFuture<?>>();
        this.animationPlayMap = new HashMap<Integer, AnimationPlayParam>();
        this.fightNewService = SpringUtils.getBean(FightNewService.class);
        this.watchFightOnOff = false;
        this.spectatorObjectMap = new ConcurrentHashMap<Long, SpectatorObject>();
        this.fightOverReason = 0;
        this.winSide = 0;
        this.exitRoles = new HashMap<Boolean, Set<FieldValue>>();
        this.logger = logger;
        this.id = id;
        this.recordId = recordId;
        this.leaderId = leaderId;
        this.createTime = createTime;
        this.listA = listA;
        this.listB = listB;
        this.fightMemberA = fightMemberA;
        this.fightMemberB = fightMemberB;
        this.mapId = mapId;
        this.type = type;
        this.specialMonsterType = specialMonsterType;
        this.round = round;
        this.roundParam = roundParam;
        this.maxRound = maxRound;
        this.taskId = taskId;
        this.expMap = expMap;
        this.bossReward = bossReward;
        this.npcId = npcId;
        this.data = data;
        this.timeOutTask = timeOutTask;
        this.astrictSkillMap = astrictSkillMap;
        this.beforeActionMap = beforeActionMap;
        this.reduceRatioMap = reduceRatioMap;
        this.ObjectAttributeMap = ObjectAttributeMap;
        this.monsterAstrict = monsterAstrict;
        this.fightInParams = fightInParams;
        this.actionParam = actionParam;
        this.respRoundStartParamMap = respRoundStartParamMap;
        this.isCompleted = isCompleted;
        this.fleeList = fleeList;
        this.fightActors = fightActors;
        this.deadPetIds = deadPetIds;
        this.fightType = fightType;
        this.rewardType = rewardType;
        this.roundType = roundType;
        this.fightId_timer_host = fightId_timer_host;
        this.roundFinish_timer_player = roundFinish_timer_player;
        this.fightId_timer_pet = fightId_timer_pet;
        this.animationPlayMap = animationPlayMap;
        this.fightNewService = fightNewService;
        this.watchFightOnOff = watchFightOnOff;
        this.spectatorObjectMap = spectatorObjectMap;
        this.fightOverReason = fightOverReason;
        this.winSide = winSide;
        this.exitRoles = exitRoles;
    }
    
    public Fight() {
        this.logger = LoggerFunction.FIGHT.getLogger();
        this.listA = Collections.synchronizedList(new ArrayList<FightObject>());
        this.listB = Collections.synchronizedList(new ArrayList<FightObject>());
        this.fightMemberA = new ArrayList<FightMember>();
        this.fightMemberB = new ArrayList<FightMember>();
        this.round = 1;
        this.roundParam = new RoundParam(this.round);
        this.maxRound = WarMaxRound.WAR_MAX_ROUND.getValue();
        this.astrictSkillMap = new HashMap<Boolean, AstrictSkill>();
        this.beforeActionMap = new HashMap<Boolean, BeforeActionSkill>();
        this.reduceRatioMap = new HashMap<Boolean, Map<Integer, Integer>>();
        this.ObjectAttributeMap = new HashMap<Boolean, Map<Integer, Double>>();
        this.monsterAstrict = NutMap.NEW();
        this.fightInParams = Collections.synchronizedList(new ArrayList<RespFightInParam>());
        this.actionParam = new ActionParam();
        this.respRoundStartParamMap = new HashMap<Integer, RespRoundStartParam>();
        this.isCompleted = new HashSet<Integer>();
        this.fleeList = new ArrayList<Integer>();
        this.fightActors = new HashMap<Integer, FightObject>();
        this.deadPetIds = new HashSet<Integer>();
        this.fightType = 0;
        this.rewardType = -1;
        this.fightId_timer_host = new HashMap<Integer, ScheduledFuture<?>>();
        this.roundFinish_timer_player = new HashMap<Integer, ScheduledFuture<?>>();
        this.fightId_timer_pet = new HashMap<Integer, ScheduledFuture<?>>();
        this.animationPlayMap = new HashMap<Integer, AnimationPlayParam>();
        this.fightNewService = SpringUtils.getBean(FightNewService.class);
        this.watchFightOnOff = false;
        this.spectatorObjectMap = new ConcurrentHashMap<Long, SpectatorObject>();
        this.fightOverReason = 0;
        this.winSide = 0;
        this.exitRoles = new HashMap<Boolean, Set<FieldValue>>();
    }
    
    static {
        idFactory = new AtomicInteger(1);
    }
}
