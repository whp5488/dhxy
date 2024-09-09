package com.kitty.system.game.fightnew.bean;

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
import java.util.Comparator;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.definition.SkillConst;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import java.util.Random;
import java.util.stream.Collectors;
import com.kitty.system.game.fightnew.utils.SkillTargetPicker;
import com.kitty.common.core.SchedulerManager;
import com.kitty.system.game.fightnew.task.TimeOutNewTask;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.fightnew.messages.old.RespEndCombat;
import com.kitty.system.game.fightnew.service.FightExecutorNewService;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
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
    
    public Fight(int type) {
        this.logger = LoggerFunction.FIGHT.getLogger();
        this.listA = Collections.synchronizedList(new ArrayList<>());
        this.listB = Collections.synchronizedList(new ArrayList<>());
        this.fightMemberA = new ArrayList<>();
        this.fightMemberB = new ArrayList<>();
        this.round = 1;
        this.roundParam = new RoundParam(this.round);
        this.maxRound = WarMaxRound.WAR_MAX_ROUND.getValue();
        this.astrictSkillMap = new HashMap();
        this.beforeActionMap = new HashMap();
        this.reduceRatioMap = new HashMap();
        this.ObjectAttributeMap = new HashMap();
        this.monsterAstrict = NutMap.NEW();
        this.fightInParams = Collections.synchronizedList(new ArrayList<>());
        this.actionParam = new ActionParam();
        this.respRoundStartParamMap = new HashMap();
        this.isCompleted = new HashSet();
        this.fleeList = new ArrayList<>();
        this.fightActors = new HashMap();
        this.deadPetIds = new HashSet();
        this.fightType = 0;
        this.rewardType = -1;
        this.fightId_timer_host = new HashMap();
        this.roundFinish_timer_player = new HashMap();
        this.fightId_timer_pet = new HashMap();
        this.animationPlayMap = new HashMap();
        this.fightNewService = (FightNewService)SpringUtils.getBean(FightNewService.class);
        this.watchFightOnOff = false;
        this.spectatorObjectMap = new ConcurrentHashMap();
        this.fightOverReason = 0;
        this.winSide = 0;
        this.exitRoles = new HashMap();
        this.id = Fight.idFactory.getAndIncrement();
        this.createTime = System.currentTimeMillis();
        this.type = type;
    }
    
    public ArrayList<FightObject> getAllFightObjects() {
        ArrayList<FightObject> container = new ArrayList<>();
        for (FightObject object : this.listA) {
            if (object != null) {
                container.add(object);
            }
            else {
                continue;
            }
        }
        for (FightObject object : this.listB) {
            if (object != null) {
                container.add(object);
            }
            else {
                continue;
            }
        }
        return container;
    }
    
    private boolean isCompleted(int round) {
        boolean boo = this.isCompleted.contains(Integer.valueOf(round));
        if (!boo) {
            this.isCompleted.add(Integer.valueOf(round));
        }
        return boo;
    }
    
    public ArrayList<FightObject> getSideAllFightObjects(boolean isASide) {
        ArrayList<FightObject> container = new ArrayList<>();
        if (isASide) {
            container.addAll(this.listA);
            return container;
        }
        container.addAll(this.listB);
        return container;
    }
    
    public FightObject queryFightObject(Integer roleId) {
        if (this.getListA() != null) {
            for (FightObject fightObject : this.getListA()) {
                if (fightObject.getId().equals(roleId)) {
                    return fightObject;
                }
                continue;
            }
        }
        if (this.getListB() != null) {
            for (FightObject fightObject : this.getListB()) {
                if (fightObject.getId().equals(roleId)) {
                    return fightObject;
                }
                continue;
            }
        }
        return null;
    }
    
    public FightObject getAttackTarget(FightObject effector) {
        List<FightObject> targets = effector.isASide() ? this.getListB() : this.getListA();
        for (FightObject target : targets) {
            if (!target.isFallDown()) {
                return target;
            }
            continue;
        }
        return null;
    }
    
    public FightObject getAttackSelf(FightObject effector) {
        List<FightObject> targets = effector.isASide() ? this.getListA() : this.getListB();
        for (FightObject target : targets) {
            if (!target.isFallDown()) {
                return target;
            }
            continue;
        }
        return null;
    }
    
    public boolean isSameSide(FightObject o1, FightObject o2) {
        return o1.isASide() == o2.isASide();
    }
    
    public List<FightObject> getMySide(int roleId) {
        for (FightObject fightObject : this.getListA()) {
            if ((int)fightObject.getId() == roleId) {
                return this.getListA();
            }
            continue;
        }
        return this.getListB();
    }
    
    public List<FightObject> getTargetSide(int roleId) {
        for (FightObject fightObject : this.getListA()) {
            if ((int)fightObject.getId() == roleId) {
                return this.getListB();
            }
            continue;
        }
        return this.getListA();
    }
    
    public boolean isWinner(int roleId) {
        if (this.getFightOverReason() == 1) {
            FightObject fightObject2 = this.queryFightObject(Integer.valueOf(roleId));
            if (this.winSide == 0) {
                return this.defineWin(fightObject2);
            }
            if (this.winSide == 1) {
                return fightObject2.isASide();
            }
            return !fightObject2.isASide();
        }
        else {
            List<FightObject> mySide = this.getMySide(roleId);
            for (FightObject fightObject : mySide) {
                if (fightObject.getCurrLife() > 0) {
                    return true;
                }
                continue;
            }
            return false;
        }
    }
    
    public boolean isWinner(int roleId, boolean isA) {
        if (this.getFightOverReason() == 1) {
            FightObject fightObject2 = this.queryFightObject(Integer.valueOf(roleId));
            if (this.winSide == 0) {
                return this.defineWin(roleId, isA);
            }
            if (this.winSide == 1) {
                return isA;
            }
            return !isA;
        }
        else {
            List<FightObject> mySide;
            if (isA) {
                mySide = this.listA;
            }
            else {
                mySide = this.listB;
            }
            for (FightObject fightObject : mySide) {
                if (fightObject.getCurrLife() > 0) {
                    return true;
                }
                continue;
            }
            return false;
        }
    }
    
    public boolean isStartFight() {
        for (FightObject fightObject : this.getListA()) {
            if (!fightObject.isDoAction()) {
                return false;
            }
            continue;
        }
        for (FightObject fightObject : this.getListB()) {
            if (!fightObject.isDoAction()) {
                return false;
            }
            continue;
        }
        return true;
    }
    
    public boolean isOver() {
        return this.isSideOver(this.getListA()) || this.isSideOver(this.getListB()) || (this.getData() != null && this.getData().getBoolean("fight_result_fail", false)) || (this.getData() != null && this.getData().getBoolean("fight_result_success", false));
    }
    
    public boolean isSideOver(List<FightObject> members) {
        if (members != null && members.size() > 0) {
            int i = 0;
            while (i < members.size()) {
                FightObject fightObject = (FightObject)members.get(i);
                if (fightObject != null && !fightObject.isFallDown()) {
                    return false;
                }
                ++i;
            }
        }
        return true;
    }
    
    public void addFightObject(FightObject addObject) {
        this.fightActors.put(addObject.getId(), addObject);
        for (FightObject object : this.getListA()) {
            if ((int)object.getId() == addObject.getHostId()) {
                this.getListA().add(addObject);
                return;
            }
            continue;
        }
        for (FightObject fightObject : this.getListB()) {
            if ((int)fightObject.getId() == addObject.getHostId()) {
                this.getListB().add(addObject);
                return;
            }
            continue;
        }
    }
    
    public void removeFightObject(FightObject fightObject) {
        if (this.getType() == 25 && fightObject.isPet() && fightObject.getPos() == 8) {
            if (this.getData() == null) {
                this.setData(NutMap.NEW());
            }
            this.getData().setv("fight_result_fail", Boolean.valueOf(true));
        }
        this.removeFightObject(this.getListA(), fightObject);
        this.removeFightObject(this.getListB(), fightObject);
    }
    
    private void removeFightObject(List<FightObject> pool, FightObject toRemove) {
        pool.removeIf(next -> next.getId().equals(toRemove.getId()));
    }
    
    public FightObject queryFightObjectAnyWay(int objId) {
        return (FightObject)this.fightActors.get(Integer.valueOf(objId));
    }
    
    public void destroy() {
        FightParamPool.fightDestroy(this);
        for (FightObject fightObject : this.getAllFightObjects()) {
            if (fightObject != null && fightObject.isPlayer()) {
                Role role = SpringUtils.getRoleService().getPlayer((long)fightObject.getRole().getId());
                if (role != null) {
                    role.setPrevFightEndTime(System.currentTimeMillis());
                    role.setCanFight(Boolean.valueOf(true));
                    this.removeRoundFinishTask((int)role.getRoleId());
                }
            }
            Integer temp = (Integer)FightExecutorNewService.FIGHT_ROOMS.get(fightObject.getId());
            if (temp == null) {
                continue;
            }
            continue;
        }
        if (this.watchFightOnOff && this.spectatorObjectMap.size() > 0) {
            this.setWatchFightOnOff(false);
            RespEndCombat respEndCombat = new RespEndCombat();
            for (Map.Entry<Long, SpectatorObject> entry : this.spectatorObjectMap.entrySet()) {
                MessagePusher.pushMessage((long)entry.getKey(), respEndCombat);
            }
        }
        FightExecutorNewService.FIGHTS.remove(Integer.valueOf(this.getId()));
        this.cancelTask(this.timeOutTask);
    }
    
    public void addHostTask(int roleId, ScheduledFuture<?> task) {
        this.removeHostTask(roleId);
        this.fightId_timer_host.put(Integer.valueOf(roleId), task);
    }
    
    public void removeHostTask(int roleId) {
        ScheduledFuture<?> task = (ScheduledFuture)this.fightId_timer_host.get(Integer.valueOf(roleId));
        if (task != null) {
            this.cancelTask(task);
            this.fightId_timer_host.remove(Integer.valueOf(roleId));
        }
    }
    
    public void addPetTask(int roleId, ScheduledFuture<?> task) {
        this.removePetTask(roleId);
        this.fightId_timer_pet.put(Integer.valueOf(roleId), task);
    }
    
    public void removePetTask(int memberId) {
        ScheduledFuture<?> task = (ScheduledFuture)this.fightId_timer_pet.get(Integer.valueOf(memberId));
        if (task != null) {
            this.cancelTask(task);
            this.fightId_timer_host.remove(Integer.valueOf(memberId));
        }
    }
    
    public void cancelTask(ScheduledFuture<?> task) {
        if (task != null && !task.isDone()) {
            task.cancel((boolean)Boolean.FALSE);
        }
    }
    
    public void addRoundFinishTask(int objId, ScheduledFuture<?> task) {
        this.removeRoundFinishTask(objId);
        this.roundFinish_timer_player.put(Integer.valueOf(objId), task);
    }
    
    public void removeRoundFinishTask(int objId) {
        ScheduledFuture<?> task = (ScheduledFuture)this.roundFinish_timer_player.get(Integer.valueOf(objId));
        if (task != null) {
            this.cancelTask(task);
            this.roundFinish_timer_player.remove(Integer.valueOf(objId));
        }
    }
    
    public void start() {
        FightExecutorNewService fightExecutorService = (FightExecutorNewService)SpringUtils.getBean(FightExecutorNewService.class);
        fightExecutorService.addFightTask(this, () -> {
            try {
                this.loadActors();
                this.notifyAndStart();
                Iterator<FightObject> iterator = this.getListA().iterator();
                while (iterator.hasNext()) {
                    FightObject object = (FightObject)iterator.next();
                    this.fightActors.put(object.getId(), object);
                }
                Iterator<FightObject> iterator2 = this.getListB().iterator();
                while (iterator2.hasNext()) {
                    FightObject object2 = (FightObject)iterator2.next();
                    this.fightActors.put(object2.getId(), object2);
                }
                ((FightNewService)SpringUtils.getBean(FightNewService.class)).excuteFightInSend(this);
                ((FightNewService)SpringUtils.getBean(FightNewService.class)).excuteRoundStartSend(this);
                this.setRoundType(1);
            }
            catch (Exception e) {
                LoggerFunction.FIGHT.getLogger().error(e.getMessage(), e);
                LoggerFunction.TASK.getLogger().error(e.getMessage(), e);
            }
            return;
        });
        ((FightNewService)SpringUtils.getBean(FightNewService.class)).besure(this);
        TimeOutNewTask timeOut = new TimeOutNewTask(this.getId());
        ScheduledFuture<?> timeOutTask = SchedulerManager.getInstance().schedule(timeOut, 30000L);
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
        ArrayList<FightObject> allFightObjects = this.getAllFightObjects();
        for (FightObject fightObject : allFightObjects) {
            if ((int)fightObject.getId() > maxId) {
                maxId = (int)fightObject.getId();
            }
            else {
                continue;
            }
        }
        return maxId;
    }
    
    public FightObject getTargetNoCurrentTarget(int attackerId, int oldTargetId) {
        FightObject attactor = this.queryFightObject(Integer.valueOf(attackerId));
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
        targetSideList = (List)targetSideList.stream().filter(obj -> (int)obj.getId() != oldTargetId && (int)obj.getId() != attackerId).collect(Collectors.toList());
        if (targetSideList.size() <= 0) {
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(targetSideList.size());
        return (FightObject)targetSideList.get(index);
    }
    
    public void clearAstrictSkills() {
        if (this.getAstrictSkillMap().get(Boolean.valueOf(true)) != null) {
            ((AstrictSkill)this.getAstrictSkillMap().get(Boolean.valueOf(true))).getSkillMap().clear();
        }
        if (this.getAstrictSkillMap().get(Boolean.valueOf(false)) != null) {
            ((AstrictSkill)this.getAstrictSkillMap().get(Boolean.valueOf(false))).getSkillMap().clear();
        }
    }
    
    public void excuteReduceRatio(FightObject fightObject, boolean isExcute) {
        if (fightObject == null) {
            return;
        }
        List<SysPetSkill> petSkillList = PetTriggerPassiveSkill.normalAttack(PET_PASSIVE_SKILL_TRIGGER_DICT.PET_BE.getValue(), fightObject.getSkillIds(), this.isPvp());
        SkillProcessor skillProcessor = null;
        if (petSkillList.size() > 0) {
            for (SysPetSkill petSkill : petSkillList) {
                skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                if (skillProcessor != null) {
                    skillProcessor.passive(this, fightObject, Boolean.valueOf(isExcute));
                }
                else {
                    continue;
                }
            }
        }
        for (Integer skillId : SkillConst.getPetBeSkills()) {
            for (RoleSkill roleSkill : fightObject.getSkillIds()) {
                if ((int)roleSkill.getSkillId() > 0 && roleSkill.getSkillId().equals(skillId)) {
                    skillProcessor = SkillProcessor.queryProcessor(skillId);
                    if (skillProcessor != null) {
                        skillProcessor.death(this, fightObject, fightObject, new Object[0]);
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
        return;
    }
    
    public boolean diedTriggerSkill(FightObject fightObject, String type) {
        if (fightObject == null) {
            return false;
        }
        boolean isRever = false;
        List<SysPetSkill> petSkillList = PetTriggerPassiveSkill.petExit(type, fightObject.getSkillIds(), this.isPvp());
        SkillProcessor skillProcessor = null;
        if (petSkillList.size() > 0) {
            petSkillList = (List)petSkillList.stream().sorted(Comparator.comparing(SysPetSkill::getSerial)).collect(Collectors.toList());
            for (SysPetSkill petSkill : petSkillList) {
                skillProcessor = SkillProcessor.queryProcessor(petSkill.getSerial());
                NutMap passive = skillProcessor.passive(this, fightObject, petSkill);
                if (passive != null && passive.getInt(String.valueOf(41)) > 0) {
                    isRever = true;
                }
                else {
                    continue;
                }
            }
        }
        return isRever;
    }
    
    public List<FightObject> queryAnomalousObject(FightObject effecter, int num, List<Integer> logics) {
        List<FightObject> returnObj = new ArrayList<>();
        List<FightObject> myFightObjects = this.getListA();
        if (!effecter.isASide()) {
            myFightObjects = this.getListB();
        }
        Collections.sort(myFightObjects);
        Iterator<FightObject> it = myFightObjects.iterator();
        FightObject obj = null;
        int count = 0;
        while (it.hasNext()) {
            obj = (FightObject)it.next();
            if (!logics.contains(Integer.valueOf(obj.getLogic()))) {
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
            continue;
        }
        return returnObj;
    }
    
    public int isPvp() {
        int isPvp = 0;
        if (1 == (int)FIGHT_TYPE.getType(Integer.valueOf(this.getType()))) {
            isPvp = 1;
        }
        return isPvp;
    }
    
    public void excuteExitPassive(FightObject actor) {
        try {
            this.diedTriggerSkill(actor, PET_PASSIVE_SKILL_TRIGGER_DICT.PET_EXIT.getValue());
            this.excuteReduceRatio(actor, false);
            List<FightObject> otherPets = (List)this.getMySide((int)actor.getId()).stream().filter(obj -> !obj.isFallDown() && obj.getLogic() == LOGIC.LOGICTYPE_PET.getValue() && !obj.getId().equals(actor.getId())).collect(Collectors.toList());
            for (FightObject otherPet : otherPets) {
                this.diedTriggerSkill(otherPet, PET_PASSIVE_SKILL_TRIGGER_DICT.NON_SELF_PET_EXIT.getValue());
            }
            PetSkillEffectorUtil.sendCancelAction(this, actor);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public RespFightInParam getRoleFightInParam(Role role) {
        RespFightInParam fightInParam = null;
        int i = 0;
        while (i < this.fightInParams.size()) {
            if (((RespFightInParam)this.fightInParams.get(i)).getRoleId() == (int)role.getRoleId()) {
                fightInParam = (RespFightInParam)this.fightInParams.get(i);
                break;
            }
            ++i;
        }
        if (fightInParam == null) {
            fightInParam = new RespFightInParam();
            fightInParam.setId(String.valueOf(role.getId()));
            List<SkillParam> skillParams = this.getSkillParams(role);
            if (skillParams != null) {
                fightInParam.setSkillParams(skillParams);
            }
            fightInParam.setRoleId((int)role.getRoleId());
            this.fightInParams.add(fightInParam);
            return fightInParam;
        }
        else {
            return fightInParam;
        }
    }
    
    public PetFightInParam addPetFightInParam(Role role, Pet pet) {
        PetFightInParam petFightInParam = null;
        RespFightInParam fightInParam = null;
        int i = 0;
        while (i < this.fightInParams.size()) {
            if (((RespFightInParam)this.fightInParams.get(i)).getRoleId() == (int)role.getRoleId()) {
                fightInParam = (RespFightInParam)this.fightInParams.get(i);
                if (fightInParam.getPetFightInParam() != null && fightInParam.getPetFightInParam().size() > 0) {
                    for (PetFightInParam petFightInParam2 : fightInParam.getPetFightInParam()) {
                        if ((int)pet.getPetId() == petFightInParam2.getPetId()) {
                            petFightInParam = petFightInParam2;
                        }
                        else {
                            continue;
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
            List<SkillParam> skillParams = this.getSkillParams(role);
            if (skillParams != null) {
                fightInParam.setSkillParams(skillParams);
            }
            fightInParam.setRoleId((int)role.getRoleId());
            this.fightInParams.add(fightInParam);
        }
        if (petFightInParam == null) {
            petFightInParam = this.getPetFightParam(pet);
            fightInParam.addPetFightInParam(petFightInParam);
            return petFightInParam;
        }
        return petFightInParam;
    }
    
    public PetFightInParam getPetFightParam(Pet pet) {
        PetFightInParam petFightInParam = new PetFightInParam();
        petFightInParam.setPetId((int)pet.getPetId());
        petFightInParam.addSkillIds(pet);
        return petFightInParam;
    }
    
    public void onActorDie(FightObject actor) {
        if (actor.getLogic() == LOGIC.LOGICTYPE_HERO.getValue()) {
            FightObject object = this.queryFightObject(Integer.valueOf(actor.getFightPetId()));
        }
    }
    
    public void petDie(FightObject actor) {
        if (actor.isPet() && !this.deadPetIds.contains(actor.getId())) {
            this.deadPetIds.add(actor.getId());
        }
        this.removeFightObject(actor);
    }
    
    public List<SkillParam> getSkillParams(Role role) {
        Map<Integer, RoleSkill> skills = role.getSkills();
        if (skills != null && skills.size() > 0) {
            List<SkillParam> skillParams = new ArrayList<>();
            for (Map.Entry<Integer, RoleSkill> entry : skills.entrySet()) {
                RoleSkill value = (RoleSkill)entry.getValue();
                if (value != null) {
                    SkillParam skillParam = new SkillParam();
                    skillParam.setSkillId((int)value.getSkillId());
                    skillParam.setProficient((int)value.getProficient() / 100);
                    skillParams.add(skillParam);
                }
                else {
                    continue;
                }
            }
            return skillParams;
        }
        return null;
    }
    
    public boolean petDeadOnce(int petId) {
        return this.deadPetIds.contains(Integer.valueOf(petId));
    }
    
    public List<ExcuteFightParam> getFightStatus() {
        List<ActionDetailParam> actionDetailParams = PetSkillEffectorUtil.checkActionForPrivate(this);
        if (actionDetailParams != null && actionDetailParams.size() > 0) {
            List<ExcuteFightParam> excuteFightParams = ((FightMessageNewService)SpringUtils.getBean(FightMessageNewService.class)).getExcuteFightParams(actionDetailParams);
            return excuteFightParams;
        }
        return null;
    }
    
    public void roundFinishTask(FightObject fightObject, long time) {
        AutoRoundFinishTask autoRoundFinishTask = new AutoRoundFinishTask(this, (int)fightObject.getId());
        ScheduledFuture<?> timeOut = SchedulerManager.getInstance().schedule(autoRoundFinishTask, time);
        this.addRoundFinishTask((int)fightObject.getId(), timeOut);
    }
    
    public boolean defineWin(FightObject fightObject) {
        int isWin = this.roundExceeded((int)fightObject.getId());
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
    
    public boolean defineWin(int roleId, boolean isA) {
        int isWin = this.roundExceeded(roleId);
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
    
    public byte roundExceeded(int roleId) {
        byte returnFlag = 2;
        List<FightObject> mySide = this.getMySide(roleId);
        List<FightObject> targetSide = this.getTargetSide(roleId);
        int mySideNum = 0;
        int mySideCurrLife = 0;
        int mySideMaxLife = 0;
        for (FightObject fightObject : mySide) {
            if (fightObject != null && !fightObject.isFallDown()) {
                ++mySideNum;
            }
            mySideCurrLife += fightObject.getCurrLife();
            mySideMaxLife += fightObject.getMaxLife();
        }
        int targetSideNum = 0;
        int targetSideCurrLife = 0;
        int targetSideMaxLife = 0;
        for (FightObject fightObject2 : targetSide) {
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
        for (FightObject fightObject3 : mySide) {
            if (fightObject3.isPlayer()) {
                myDefeatPet += FightParamPool.getDeathPetNum(this.getId(), (int)fightObject3.getId());
            }
            else {
                continue;
            }
        }
        int targetDefeatPet = 0;
        for (FightObject fightObject4 : targetSide) {
            if (fightObject4.isPlayer()) {
                targetDefeatPet += FightParamPool.getDeathPetNum(this.getId(), (int)fightObject4.getId());
            }
            else {
                continue;
            }
        }
        if (myDefeatPet < targetDefeatPet) {
            return 1;
        }
        if (myDefeatPet > targetDefeatPet) {
            return 0;
        }
        if (mySideMaxLife > 0 && targetSideMaxLife > 0) {
            String mySidePCTStr = BigDecimalUtil.divide(String.valueOf(mySideCurrLife), String.valueOf(mySideMaxLife), 4);
            String targetSidePCTStr = BigDecimalUtil.divide(String.valueOf(targetSideCurrLife), String.valueOf(targetSideMaxLife), 4);
            Double mySidePCT = Double.valueOf(mySidePCTStr);
            Double targetSidePCT = Double.valueOf(targetSidePCTStr);
            if ((double)mySidePCT > (double)targetSidePCT) {
                return 1;
            }
            if ((double)mySidePCT < (double)targetSidePCT) {
                return 0;
            }
        }
        int myControlNum = 0;
        for (FightObject fightObject5 : mySide) {
            if (fightObject5.isPlayer()) {
                myControlNum += FightParamPool.getControlNum(this.getId(), (int)fightObject5.getId());
            }
            else {
                continue;
            }
        }
        int targetControlNum = 0;
        for (FightObject fightObject6 : targetSide) {
            if (fightObject6.isPlayer()) {
                targetControlNum += FightParamPool.getControlNum(this.getId(), (int)fightObject6.getId());
            }
            else {
                continue;
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
    
    public void addExitRole(Boolean isA, FieldValue fieldValue) {
        Set<FieldValue> fieldValues = (Set)this.exitRoles.get(isA);
        if (fieldValues == null) {
            fieldValues = new HashSet();
            this.exitRoles.put(isA, fieldValues);
        }
        fieldValues.add(fieldValue);
    }
    
    public boolean isSameCamp(int objIdA, int objIdB) {
        boolean isInA = this.isSameCamp(this.listA, objIdA, objIdB);
        if (isInA) {
            return true;
        }
        boolean isInB = this.isSameCamp(this.listB, objIdA, objIdB);
        return isInB;
    }
    
    private boolean isSameCamp(List<FightObject> list, int objIdA, int objIdB) {
        if (list != null && list.size() > 0) {
            int flag = 0;
            for (FightObject obj : list) {
                if (obj.getId().equals(Integer.valueOf(objIdA))) {
                    ++flag;
                }
                if (obj.getId().equals(Integer.valueOf(objIdB))) {
                    ++flag;
                }
                else {
                    continue;
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
    
    public Fight(final Logger logger, final int id, final long recordId, final int leaderId, final long createTime, final List<FightObject> listA, final List<FightObject> listB, final ArrayList<FightMember> fightMemberA, final ArrayList<FightMember> fightMemberB, final int mapId, final int type, final byte specialMonsterType, final int round, final RoundParam roundParam, final int maxRound, final int taskId, final NutMap expMap, final NutMap bossReward, final int npcId, final NutMap data, final ScheduledFuture<?> timeOutTask, final Map<Boolean, AstrictSkill> astrictSkillMap, final Map<Boolean, BeforeActionSkill> beforeActionMap, final Map<Boolean, Map<Integer, Integer>> reduceRatioMap, final Map<Boolean, Map<Integer, Double>> ObjectAttributeMap, final NutMap monsterAstrict, final List<RespFightInParam> fightInParams, final ActionParam actionParam, final Map<Integer, RespRoundStartParam> respRoundStartParamMap, final Set<Integer> isCompleted, final List<Integer> fleeList, final Map<Integer, FightObject> fightActors, final Set<Integer> deadPetIds, final byte fightType, final int rewardType, final int roundType, final Map<Integer, ScheduledFuture<?>> fightId_timer_host, final Map<Integer, ScheduledFuture<?>> roundFinish_timer_player, final Map<Integer, ScheduledFuture<?>> fightId_timer_pet, final Map<Integer, AnimationPlayParam> animationPlayMap, final FightNewService fightNewService, final boolean watchFightOnOff, final ConcurrentHashMap<Long, SpectatorObject> spectatorObjectMap, final byte fightOverReason, final byte winSide, final Map<Boolean, Set<FieldValue>> exitRoles) {
        this.logger = LoggerFunction.FIGHT.getLogger();
        this.listA = Collections.synchronizedList(new ArrayList<>());
        this.listB = Collections.synchronizedList(new ArrayList<>());
        this.fightMemberA = new ArrayList<>();
        this.fightMemberB = new ArrayList<>();
        this.round = 1;
        this.roundParam = new RoundParam(this.round);
        this.maxRound = WarMaxRound.WAR_MAX_ROUND.getValue();
        this.astrictSkillMap = new HashMap();
        this.beforeActionMap = new HashMap();
        this.reduceRatioMap = new HashMap();
        this.ObjectAttributeMap = new HashMap();
        this.monsterAstrict = NutMap.NEW();
        this.fightInParams = Collections.synchronizedList(new ArrayList<>());
        this.actionParam = new ActionParam();
        this.respRoundStartParamMap = new HashMap();
        this.isCompleted = new HashSet();
        this.fleeList = new ArrayList<>();
        this.fightActors = new HashMap();
        this.deadPetIds = new HashSet();
        this.fightType = 0;
        this.rewardType = -1;
        this.fightId_timer_host = new HashMap();
        this.roundFinish_timer_player = new HashMap();
        this.fightId_timer_pet = new HashMap();
        this.animationPlayMap = new HashMap();
        this.fightNewService = (FightNewService)SpringUtils.getBean(FightNewService.class);
        this.watchFightOnOff = false;
        this.spectatorObjectMap = new ConcurrentHashMap();
        this.fightOverReason = 0;
        this.winSide = 0;
        this.exitRoles = new HashMap();
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
        this.listA = Collections.synchronizedList(new ArrayList<>());
        this.listB = Collections.synchronizedList(new ArrayList<>());
        this.fightMemberA = new ArrayList<>();
        this.fightMemberB = new ArrayList<>();
        this.round = 1;
        this.roundParam = new RoundParam(this.round);
        this.maxRound = WarMaxRound.WAR_MAX_ROUND.getValue();
        this.astrictSkillMap = new HashMap();
        this.beforeActionMap = new HashMap();
        this.reduceRatioMap = new HashMap();
        this.ObjectAttributeMap = new HashMap();
        this.monsterAstrict = NutMap.NEW();
        this.fightInParams = Collections.synchronizedList(new ArrayList<>());
        this.actionParam = new ActionParam();
        this.respRoundStartParamMap = new HashMap();
        this.isCompleted = new HashSet();
        this.fleeList = new ArrayList<>();
        this.fightActors = new HashMap();
        this.deadPetIds = new HashSet();
        this.fightType = 0;
        this.rewardType = -1;
        this.fightId_timer_host = new HashMap();
        this.roundFinish_timer_player = new HashMap();
        this.fightId_timer_pet = new HashMap();
        this.animationPlayMap = new HashMap();
        this.fightNewService = (FightNewService)SpringUtils.getBean(FightNewService.class);
        this.watchFightOnOff = false;
        this.spectatorObjectMap = new ConcurrentHashMap();
        this.fightOverReason = 0;
        this.winSide = 0;
        this.exitRoles = new HashMap();
    }
    
    static {
        idFactory = new AtomicInteger(1);
    }
}
