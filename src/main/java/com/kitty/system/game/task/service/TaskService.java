package com.kitty.system.game.task.service;

import com.kitty.system.game.task.message.RespTasks;
import com.kitty.system.init.domain.SysDataTaskCondition;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.common.model.Pos;
import java.util.stream.Collectors;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.init.domain.SysDataTaskEventForeign;
import com.kitty.system.init.domain.SysDataTaskEvent;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.boss.service.BossService;
import com.kitty.system.boss.model.BossFightParam;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.vo.EasyNpc;
import com.kitty.system.game.task.service.handler.CrusadeRenegadeTaskHandler;
import com.kitty.system.init.domain.SysDataMonster;
import org.springframework.util.CollectionUtils;
import com.kitty.system.game.service.MonsterService;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.system.init.domain.SysWarRole;
import com.kitty.system.init.domain.SysDataTaskFight;
import com.kitty.system.enums.task.TaskEvent;
import com.kitty.mina.task.ThreadLocalUtil;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.task.message.RespTasksDel;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import com.kitty.logs.LoggerUtils;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.task.message.vo.TaskInfo;
import com.kitty.system.init.domain.vo.Param;
import com.kitty.system.init.domain.SysDataTaskForeign;
import com.kitty.system.init.domain.SysDataTaskReward;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.task.domain.DoingTask;
import java.util.Arrays;
import com.kitty.system.game.task.message.ReqUpTaskEvent;
import com.kitty.system.init.domain.SysDataTaskMain;
import java.util.Map;
import com.kitty.listener.event.RoleLevelUpEvent;
import com.kitty.system.game.task.service.handler.TaskHandler;
import com.kitty.system.game.task.service.handler.TaskHelper;
import com.kitty.system.enums.task.TaskType;
import com.kitty.system.game.task.domain.RoleTask;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.config.TaskSet;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.event.LoginEvent;
import com.kitty.logs.LoggerFunction;
import com.kitty.system.queue.BroadcastThread;
import com.kitty.system.game.map.service.MapService;
import com.kitty.cache.RedisService;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.npc.service.NpcService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.team.service.TeamService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class TaskService
{
    protected Logger logger;
    @Autowired
    TeamService teamService;
    @Autowired
    NpcService npcService;
    @Autowired
    RoleService roleService;
    @Autowired
    BagService bagService;
    @Autowired
    PetService petService;
    @Autowired
    RedisService redisService;
    @Autowired
    protected MapService mapService;
    @Autowired
    BroadcastThread broadcastThread;
    
    public TaskService() {
        this.logger = LoggerFunction.TASK.getLogger();
    }
    
    @EventHandler({ EventType.LOGIN })
    public void handleLoginEvent(LoginEvent loginEvent) {
        Role role = loginEvent.getRole();
        this.pushTasksOnLogin(role);
    }
    
    public TaskSet getTaskSet(int taskId) {
        TaskSet taskSet = (TaskSet)TaskDataPool.taskMap.get(Integer.valueOf(taskId));
        if (taskSet != null) {
            return taskSet;
        }
        return null;
    }
    
    public List<TaskSet> getAcceptedTasksMatchTarget(Role role) {
        List<Integer> acceptedTaskList = role.getTask().getAcceptedTaskList();
        List<TaskSet> matchTaskList = new ArrayList(acceptedTaskList.size());
        for (Integer taskId : acceptedTaskList) {
            TaskSet taskSet = this.getTaskSet((int)taskId);
            if (taskSet == null) {
                continue;
            }
            matchTaskList.add(taskSet);
        }
        return matchTaskList;
    }
    
    public void updateTargetProgress(Role role, TaskSet taskSet) {
        if (this.teamService.isInTeam(role)) {
            if (this.teamService.isTeamLeader(role)) {
                if (taskSet.isTeamAccept()) {
                    this.teamService.memberHandleThreadLocal(role, memberRole -> this.updateSingleTargetProgress(memberRole, taskSet));
                }
                else {
                    this.updateSingleTargetProgress(role, taskSet);
                }
            }
        }
        else {
            this.updateSingleTargetProgress(role, taskSet);
        }
    }
    
    public void updateSingleTargetProgress(Role role, TaskSet taskSet) {
        RoleTask roleTask = role.getTask();
        if (!roleTask.isAccepted(taskSet.getSerial())) {
            return;
        }
        roleTask.updateTargetProgress(taskSet.getSerial());
        this.logger.trace("更新任务进度：任务: {}", taskSet);
        this.triggerTask(role, taskSet);
        return;
    }
    
    private void triggerTask(Role role, TaskSet taskSet) {
        if (taskSet.getTaskType() == TaskType.MAIN_LINE) {
            TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType()).submitTask(role, taskSet);
        }
        if (role.getTask().isCanSubmit(taskSet.getSerial())) {
            TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType());
            if (taskHandler != null) {
                taskHandler.submitTask(role, taskSet);
            }
        }
    }
    
    @EventHandler({ EventType.ROLE_LEVEL_UP })
    public void handleRoleLevelUpEvent(RoleLevelUpEvent roleLevelUpEvent) {
        Role role = roleLevelUpEvent.getRole();
        if (role == null) {
            return;
        }
        Map<Integer, SysDataTaskMain> taskMainMap = TaskDataPool.taskMainMap;
        List<Integer> acceptedTaskList = role.getTask().getAcceptedTaskList();
        for (Integer serial : acceptedTaskList) {
            if (taskMainMap.get(serial) != null) {
                TaskSet taskSet = this.getTaskSet((int)serial);
                this.editTaskEvent(role, taskSet.getAddTaskEvent(), taskSet, TaskHandler.getTaskPara(role, taskSet));
            }
            else {
                continue;
            }
        }
        return;
    }
    
    public boolean isTask(Role role, TaskSet taskSet) {
        if (taskSet == null || role == null) {
            return false;
        }
        RoleTask roleTask = role.getTask();
        return roleTask.isAccepted(taskSet.getSerial());
    }
    
    public boolean isMatchTaskType(TaskSet taskSet, TaskType taskType) {
        return taskSet != null && taskSet.getTaskType() != null && taskSet.getTaskType() == taskType;
    }
    
    public void askUpEvent(Role role, ReqUpTaskEvent reqUpTaskEvent) {
        if (role.getTask().isAccepted(reqUpTaskEvent.getTaskId())) {
            TaskSet taskSet = this.getTaskSet(reqUpTaskEvent.getTaskId());
            if (taskSet == null) {
                return;
            }
            for (Integer key : reqUpTaskEvent.getEvent()) {
                if (taskSet.getAskEvent().contains(key)) {
                    this.editTaskEvent(role, Arrays.asList(key), taskSet, TaskHandler.getTaskPara(role, taskSet));
                }
                else {
                    continue;
                }
            }
            if (role.getTask().isAccepted(taskSet.getSerial())) {
                TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType());
                if (taskHandler != null) {
                    taskHandler.pushTask(role, taskSet, 1);
                }
            }
        }
    }
    
    public boolean isPrevTaskFinished(Role role, TaskSet taskSet) {
        TaskSet needTask = this.getTaskSet(taskSet.getNextTaskSerial());
        if (needTask == null) {
            return true;
        }
        DoingTask doingTask = (DoingTask)role.getTask().getDoingTask().get(Integer.valueOf(taskSet.getSerial()));
        if (doingTask == null) {
            MessagePusher.pushMessage(role, new RespMsg("请先领取任务"));
            return false;
        }
        if (taskSet.getSerial() != needTask.getPreviousId()) {
            MessagePusher.pushMessage(role, new RespMsg("请先完成上一任务任务"));
            return false;
        }
        return true;
    }
    
    public void giveAward(Role role, TaskSet taskSet) {
        Map<String, SysDataTaskReward> taskRewardMap = TaskDataPool.taskRewardMap;
        SysDataTaskReward taskReward = (SysDataTaskReward)taskRewardMap.get(taskSet.getTaskReward());
        if (taskReward != null) {
            List<Param> rewards = taskReward.getRewards();
            if (taskReward.getType() > 0) {
                SysDataTaskForeign foreign = (SysDataTaskForeign)TaskDataPool.taskRewardForeign.stream().filter(e -> {
                    boolean b = e.getSerial() == taskReward.getType() && e.getRoleIcon() == role.getIcon();
                    return b;
                }).findFirst().orElse(null);
                if (foreign == null) {
                    this.logger.error("未找到奖励：任务编号 = {}, 角色外观 = {}", Integer.valueOf(taskSet.getSerial()), Integer.valueOf(role.getIcon()));
                    return;
                }
                SysDataTaskReward reward = (SysDataTaskReward)taskRewardMap.get(foreign.getTaskRewardSerial());
                rewards = reward.getRewards();
            }
            this.bagService.sendReward(role, rewards, "任务", 0);
        }
    }
    
    public TaskInfo getTaskInfo(Role role, TaskSet taskSet) {
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setSerial(taskSet.getSerial());
        taskInfo.setQuit((byte)taskSet.getGiveUp());
        DoingTask doingTask = (DoingTask)role.getTask().getDoingTask().get(Integer.valueOf(taskSet.getSerial()));
        if (doingTask != null) {
            taskInfo.setNpcList(doingTask.getEasyNpcList());
            if (doingTask.getChangeCounter() != null && doingTask.getChangeCounter().size() > 0) {
                taskInfo.setList(Arrays.asList(new FiedValue("changeCounter", (byte)4, JsonUtils.object2String(doingTask.getChangeCounter()))));
            }
            taskInfo.setTaskUpdateTime(doingTask.getUpdate());
            return taskInfo;
        }
        else {
            return taskInfo;
        }
    }
    
    public List<TaskSet> getAcceptedTaskListMatchTaskType(Role role, TaskType taskType) {
        List<Integer> acceptedTaskList = null;
        try {
            acceptedTaskList = role.getTask().getAcceptedTaskList();
        }
        catch (ConcurrentModificationException e) {
            e.getStackTrace();
            LoggerUtils.error("{}({}),taskType:{},sessionId:{}", role.getRoleId(), role.getName(), taskType, e.getMessage(), e);
        }
        if (acceptedTaskList != null) {
            List<TaskSet> taskList = new ArrayList(acceptedTaskList.size());
            for (Integer taskId : acceptedTaskList) {
                TaskSet taskSet = this.getTaskSet((int)taskId);
                if (!this.isMatchTaskType(taskSet, taskType)) {
                    continue;
                }
                taskList.add(taskSet);
            }
            return taskList;
        }
        return Collections.emptyList();
    }
    
    public void pushTasksOnLogin(Role role) {
        if (role.getTask() == null) {
            this.logger.error("上线加载任务异常：玩家 = {}，任务数据 = {}", role.getRoleId(), role.getTask());
            role.setTask(new RoleTask());
        }
        List<Integer> acceptedTaskList = role.getTask().getAcceptedTaskList();
        for (int taskId : acceptedTaskList) {
            TaskSet taskSet = this.getTaskSet(taskId);
            if (taskSet == null || taskSet.getTaskType() == null) {
                new RuntimeException("任务数据异常:" + taskId);
            }
            else {
                TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType());
                if (taskHandler == null) {
                    continue;
                }
                taskHandler.pushTask(role, taskSet, 1);
            }
        }
    }
    
    public void deleteTask(Role role, TaskSet taskSet) {
        this.deleteTaskForSingle(role, taskSet);
    }
    
    private void doDeleteTaskForTeam(Team team, TaskSet taskSet) {
        this.doDeleteTaskForTeam(team.getList(), team.getLeaderRoleId(), taskSet);
    }
    
    public void doDeleteTaskForTeam(List<Member> members, int leaderRoleId, TaskSet taskSet) {
        for (Member member : members) {
            if (member == null) {
                continue;
            }
            if (!member.isInTeam()) {
                continue;
            }
            if (leaderRoleId == (int)member.getRoleId()) {
                Role role = this.roleService.getPlayer((int)member.getRoleId());
                this.deleteTaskForSingle(role, taskSet);
            }
            else {
                this.deleteMemberTask(member, taskSet);
            }
        }
    }
    
    public void deleteTaskForSingle(Role role, TaskSet taskSet) {
        if (!this.isTask(role, taskSet)) {
            return;
        }
        int taskNpcId = role.getTask().getTaskNpcId(taskSet.getSerial());
        MapService mapService = (MapService)SpringUtils.getBean(MapService.class);
        Npc npc = mapService.getNpc(taskNpcId);
        role.getTask().updateToFinished(taskSet.getSerial());
        RespTasksDel respTasksDel = new RespTasksDel();
        respTasksDel.setSerial(taskSet.getSerial());
        MessagePusher.pushMessage(role, respTasksDel);
        return;
    }
    
    private void deleteMemberTask(Member member, TaskSet taskSet) {
        Role role = this.roleService.getPlayer((long)member.getId());
        if (role == null) {
            return;
        }
        ThreadLocalUtil.addLocalTask(role, () -> this.deleteTaskForSingle(role, taskSet));
        return;
    }
    
    public void changeCounterTask(Role role, TaskSet taskSet, TaskEvent taskEvent, int serial, int num) {
        Team team = ((TeamService)SpringUtils.getBean(TeamService.class)).getTeam((int)role.getRoleId());
        if (this.teamService.isInTeam(role)) {
            this.doChangeCounterTaskForTeam(team, taskSet, taskEvent, serial, num);
        }
        else {
            this.changeCounterTaskForSingle(role, taskSet, taskEvent, serial, num);
        }
    }
    
    private void doChangeCounterTaskForTeam(Team team, TaskSet taskSet, TaskEvent taskEvent, int serial, int num) {
        this.doChangeCounterTaskForTeam(team.getList(), team.getLeaderRoleId(), taskSet, taskEvent, serial, num);
    }
    
    public void doChangeCounterTaskForTeam(List<Member> members, int leaderRoleId, TaskSet taskSet, TaskEvent taskEvent, int serial, int num) {
        for (Member member : members) {
            if (member == null) {
                continue;
            }
            if (!member.isInTeam()) {
                continue;
            }
            if (leaderRoleId == (int)member.getRoleId()) {
                Role role = this.roleService.getPlayer((int)member.getRoleId());
                this.changeCounterTaskForSingle(role, taskSet, taskEvent, serial, num);
            }
            else {
                this.changeCounterMemberTask(member, taskSet, taskEvent, serial, num);
            }
        }
    }
    
    private void changeCounterMemberTask(Member member, TaskSet taskSet, TaskEvent taskEvent, int serial, int num) {
        Role role = this.roleService.getPlayer((long)member.getId());
        if (role == null) {
            return;
        }
        ThreadLocalUtil.addLocalTask(role, () -> this.changeCounterTaskForSingle(role, taskSet, taskEvent, serial, num));
        return;
    }
    
    public void changeCounterTaskForSingle(Role role, TaskSet taskSet, TaskEvent taskEvent, int serial, int num) {
        if (!this.isTask(role, taskSet)) {
            return;
        }
        role.getTask().updateChangeCounterTask(taskSet.getSerial(), taskEvent, serial, num);
        TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType());
        if (taskHandler != null) {
            taskHandler.pushTask(role, taskSet, 1);
        }
        else {
            TaskInfo taskInfo = this.getTaskInfo(role, taskSet);
            if (taskInfo != null) {
                this.pushTaskInfo(role, taskInfo);
            }
        }
        role.save();
        return;
    }
    
    public void taskTriggerFight(Role role, TaskSet taskSet, int serial) {
        if (SpringUtils.getFightNewService().isInFight(role)) {
            return;
        }
        SysDataTaskFight taskFight = null;
        try {
            if (serial > 0) {
                taskFight = (SysDataTaskFight)TaskDataPool.taskFightMap.get(Integer.valueOf(serial));
            }
            else {
                taskFight = (SysDataTaskFight)TaskDataPool.taskFightMap.values().stream().filter(e -> {
                    boolean b = e.getTaskSerial() == taskSet.getSerial();
                    return b;
                }).findFirst().orElse(null);
            }
            if (taskFight != null && taskFight.getTaskSerial() == taskSet.getSerial()) {
                SysWarRole warRole = (SysWarRole)FightDataPool.data_war_role.get(Integer.valueOf(taskFight.getTarNpcData()));
                if (warRole != null) {
                    List<SysDataMonster> list = ((MonsterService)SpringUtils.getBean(MonsterService.class)).getMonsters(FightDataPool.data_monster, warRole);
                    if (CollectionUtils.isEmpty(list)) {
                        return;
                    }
                    Integer max = (Integer)Collections.max(this.teamService.getMemberLevel(role));
                    if (taskSet.getTaskType().equals(TaskType.CRUSADE_RENEGADE)) {
                        for (int i = 0; i < list.size(); ++i) {
                            SysDataMonster monster = (SysDataMonster)list.get(i);
                            int monsterSerial = CrusadeRenegadeTaskHandler.getMonster((int)max, (int)monster.getSerial());
                            if (!monster.getSerial().equals(Integer.valueOf(monsterSerial))) {
                                SysDataMonster dataMonster = (SysDataMonster)FightDataPool.data_monster.get(Integer.valueOf(serial));
                                if (dataMonster != null) {
                                    list.set(i, dataMonster);
                                }
                            }
                        }
                    }
                    List<SysDataMonster> monsters = new ArrayList(list.size());
                    monsters.addAll(list);
                    DoingTask doingTask = (DoingTask)role.getTask().getDoingTask().get(Integer.valueOf(taskSet.getSerial()));
                    if (doingTask != null) {
                        EasyNpc easyNpc = null;
                        if (doingTask.getEasyNpcList().size() > 0) {
                            easyNpc = (EasyNpc)doingTask.getEasyNpcList().stream().filter(e -> {
                                boolean b2 = e.getId() == doingTask.getNpcId();
                                return b2;
                            }).findFirst().orElse(null);
                        }
                        for (SysDataMonster monster2 : monsters) {
                            if (Const.taskTypeList.contains(taskSet.getTaskType())) {
                                DataGlobalFunc.createMonster((int)max, monster2);
                            }
                            if (easyNpc != null && monster2.getIsBoss() != null && (int)monster2.getIsBoss() == 1) {
                                monster2.setName(easyNpc.getN());
                                monster2.setShape(Integer.valueOf((int)easyNpc.getS()));
                            }
                            else {
                                continue;
                            }
                        }
                    }
                    BossFightParam bossFightParam = new BossFightParam(monsters, FIGHT_TYPE.TASK.getValue(), taskSet.getSerial(), taskFight.getSerial());
                    Fight fight = ((BossService)SpringUtils.getBean(BossService.class)).startFightToBoss(role, bossFightParam);
                    if (fight != null) {
                        role.getTask().addRecord4FightAndTask(fight.getId(), taskSet.getSerial(), -1);
                    }
                }
                else {
                    MessagePusher.pushMessage(role, new RespMsg("未找到战场怪物数据"));
                }
            }
            else {
                MessagePusher.pushMessage(role, new RespMsg("未找到本场战斗数据"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            this.logger.error("任务获取战斗异常：{}", e.getMessage());
        }
        return;
    }
    
    public void doFightEnd(Role role, FightEndEvent fightEndEvent) {
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (this.teamService.isInTeam(role) && !this.teamService.isTeamLeader(role)) {
            return;
        }
        Integer taskId = role.getTask().getTaskIdByFightId(fightEndEvent.getFightId());
        if (taskId == null) {
            this.fightEndLeaveTeam(team);
            return;
        }
        TaskSet taskSet = this.getTaskSet((int)taskId);
        TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType());
        if (taskHandler != null) {
            taskHandler.doFightEnd(role, fightEndEvent);
        }
        this.fightEndLeaveTeam(team);
        return;
    }
    
    public void fightEndLeaveTeam(Team team) {
        if (team != null) {
            for (String key : team.getFightLeaveNutMap().keySet()) {
                int anInt = team.getFightLeaveNutMap().getInt(key, -1);
                if (anInt > -1) {
                    Role role = this.roleService.getPlayer((long)Long.valueOf(key));
                    if (role == null) {
                        continue;
                    }
                    if (anInt == 1) {
                        this.teamService.leave(role, false);
                    }
                    else {
                        if (anInt != 0) {
                            continue;
                        }
                        this.teamService.leaveTemp(role, true);
                    }
                }
                else {
                    continue;
                }
            }
            team.getFightLeaveNutMap().clear();
        }
    }
    
    public boolean delItemTaskEvent(Role role, TaskSet taskSet) {
        List<Integer> list = taskSet.getFinishTaskEvent();
        Map<Integer, SysDataTaskEvent> taskEventMap = TaskDataPool.taskEventMap;
        for (Integer serial : list) {
            SysDataTaskEvent taskEvent = (SysDataTaskEvent)taskEventMap.get(serial);
            if (taskEvent != null && taskEvent.getType() == TaskEvent.DelItem) {
                SysDataTaskEventForeign foreign = (SysDataTaskEventForeign)TaskDataPool.taskEventForeign.stream().filter(obj -> {
                    boolean b = obj.getSerial() == taskEvent.getForeign();
                    return b;
                }).findFirst().orElse(null);
                if (foreign == null) {
                    continue;
                }
                if (foreign.getType() != TaskEvent.DelItem) {
                    continue;
                }
                Integer integer = this.bagService.consumeBagGoods(role, foreign.getK(), foreign.getV(), false, true, "任务提交");
                if (integer == null) {
                    continue;
                }
                Map<Long, BagEquip> bagEquipMap = this.bagService.getBagEquip(role);
                BagEquip equip = (BagEquip)bagEquipMap.values().stream().filter(e -> {
                    boolean b2 = e.getType() == foreign.getK();
                    return b2;
                }).findFirst().orElse(null);
                if (equip != null) {
                    this.bagService.deleteRoleEquip(role, equip, "任务扣除");
                    return true;
                }
                MessagePusher.pushMessage(role, new RespMsg("你背包中没有该道具"));
                return false;
            }
            else {
                continue;
            }
        }
        return true;
    }
    
    public void editTaskEvent(Role role, List<Integer> list, TaskSet taskSet, String para) {
        try {
            if (list.size() <= 0) {
                return;
            }
            Map<Integer, SysDataTaskEvent> taskEventMap = TaskDataPool.taskEventMap;
            List<SysDataTaskEvent> objects = new ArrayList<>();
            SysDataTaskEvent taskEvent = null;
            for (Integer serial : list) {
                taskEvent = (SysDataTaskEvent)taskEventMap.get(serial);
                if (taskEvent != null && taskEvent.getType() != TaskEvent.DelItem) {
                    objects.add(taskEvent);
                }
                else {
                    continue;
                }
            }
            for (SysDataTaskEvent taskEvent2 : objects) {
                if (taskEvent2.getType() == TaskEvent.AddNewTask) {
                    TaskSet task = this.getTaskSet(taskEvent2.getEvent());
                    if (task == null) {
                        continue;
                    }
                    TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(task.getTaskType());
                    if (taskHandler == null) {
                        continue;
                    }
                    task.setPara(para);
                    taskHandler.acceptTask(role, task, -1);
                }
                else if (taskEvent2.getType() == TaskEvent.TeamFinishTask) {
                    TaskSet task = this.getTaskSet(taskEvent2.getEvent());
                    this.updateTargetProgress(role, task);
                }
                else if (taskEvent2.getType() == TaskEvent.FinishTask) {
                    TaskSet task = this.getTaskSet(taskEvent2.getEvent());
                    this.updateSingleTargetProgress(role, task);
                }
                else if (taskEvent2.getType() == TaskEvent.NewReward) {
                    List<SysDataTaskEventForeign> collect = (List)TaskDataPool.taskEventForeign.stream().filter(obj -> {
                        boolean b9 = obj.getSerial() == taskEvent2.getForeign();
                        return b9;
                    }).collect(Collectors.toList());
                    for (SysDataTaskEventForeign foreign : collect) {
                        Map<String, SysDataTaskReward> taskRewardMap = TaskDataPool.taskRewardMap;
                        SysDataTaskReward taskReward = (SysDataTaskReward)taskRewardMap.get(foreign.getOther());
                        if (taskReward != null) {
                            List<Param> rewards = taskReward.getRewards();
                            this.bagService.sendReward(role, rewards, "奖励事件", 0);
                        }
                        else {
                            continue;
                        }
                    }
                }
                else if (taskEvent2.getType() == TaskEvent.TeamChangeCounter) {
                    List<SysDataTaskEventForeign> collect = (List)TaskDataPool.taskEventForeign.stream().filter(obj -> {
                        boolean b8 = obj.getSerial() == taskEvent2.getForeign();
                        return b8;
                    }).collect(Collectors.toList());
                    for (SysDataTaskEventForeign foreign : collect) {
                        if (foreign.getType() == TaskEvent.Add || foreign.getType() == TaskEvent.Set) {
                            this.changeCounterTask(role, taskSet, foreign.getType(), foreign.getK(), foreign.getV());
                            this.condition(role, taskSet, (byte)1);
                        }
                        else {
                            continue;
                        }
                    }
                }
                else if (taskEvent2.getType() == TaskEvent.ChangeCounter) {
                    List<SysDataTaskEventForeign> collect = (List)TaskDataPool.taskEventForeign.stream().filter(obj -> {
                        boolean b7 = obj.getSerial() == taskEvent2.getForeign();
                        return b7;
                    }).collect(Collectors.toList());
                    for (SysDataTaskEventForeign foreign : collect) {
                        if (foreign.getType() == TaskEvent.Add || foreign.getType() == TaskEvent.Set) {
                            this.changeCounterTaskForSingle(role, taskSet, foreign.getType(), foreign.getK(), foreign.getV());
                            this.condition(role, taskSet, (byte)1);
                        }
                        else {
                            continue;
                        }
                    }
                }
                else if (taskEvent2.getType() == TaskEvent.ChangeShape) {
                    if (taskEvent2.getEvent() <= 0) {
                        role.setIcon(role.getTask().getIcon());
                    }
                    else {
                        role.setIcon(taskEvent2.getEvent());
                    }
                    SpringUtils.getBroadcastService().sendUpdateAppear(role);
                }
                else if (taskEvent2.getType() == TaskEvent.SendUser) {
                    SysDataTaskEventForeign foreign2 = (SysDataTaskEventForeign)TaskDataPool.taskEventForeign.stream().filter(obj -> {
                        boolean b6 = obj.getSerial() == taskEvent2.getForeign();
                        return b6;
                    }).findFirst().orElse(null);
                    if (foreign2 == null) {
                        continue;
                    }
                    Pos pos = (Pos)JsonUtils.string2Object(foreign2.getOther(), Pos.class);
                    if (pos == null) {
                        continue;
                    }
                    this.mapService.changeMap(role, new ReqTelePort((int)pos.getMapId(), (int)pos.getX(), (int)pos.getY(), 1), null);
                }
                else if (taskEvent2.getType() == TaskEvent.CreateNpc) {
                    List<SysDataTaskEventForeign> collect = (List)TaskDataPool.taskEventForeign.stream().filter(obj -> {
                        boolean b5 = obj.getSerial() == taskEvent2.getForeign();
                        return b5;
                    }).collect(Collectors.toList());
                    for (SysDataTaskEventForeign foreign : collect) {
                        if (foreign.getType() == TaskEvent.CreateNpc) {
                            this.writeNpc(role, taskSet.getSerial(), foreign.getV(), 1, (byte)1);
                        }
                        else {
                            continue;
                        }
                    }
                }
                else if (taskEvent2.getType() == TaskEvent.DestroyNpc) {
                    int event = taskEvent2.getEvent();
                    DoingTask doingTask = (DoingTask)role.getTask().getDoingTask().get(Integer.valueOf(taskSet.getSerial()));
                    if (doingTask == null) {
                        continue;
                    }
                    List<EasyNpc> npcList = doingTask.getEasyNpcList();
                    int i = 0;
                    while (i < doingTask.getEasyNpcList().size()) {
                        EasyNpc easyNpc = (EasyNpc)doingTask.getEasyNpcList().get(i);
                        if (easyNpc != null && easyNpc.getId() == event) {
                            npcList.remove(i);
                            break;
                        }
                        ++i;
                    }
                }
                else {
                    if (taskEvent2.getType() == TaskEvent.AddTaskItem) {
                        continue;
                    }
                    if (taskEvent2.getType() != TaskEvent.DelTaskItem) {}
                }
            }
        }
        catch (Exception e) {
            this.logger.error("任务触发事件错误---------玩家编号：{}，任务编号{}，任务类型：{}", new Object[] { role.getRoleId(), Integer.valueOf(taskSet.getSerial()), Integer.valueOf(taskSet.getTaskType().getId()) });
            e.printStackTrace();
        }
    }
    
    public void writeNpc(Role role, int taskSerial, int npcSerial, int type, byte t) {
        DoingTask doingTask = (DoingTask)role.getTask().getDoingTask().get(Integer.valueOf(taskSerial));
        if (doingTask == null) {
            this.logger.error("给任务写入NPC，玩家没有该任务，任务编号 = {}，创建NPC编号：{}", Integer.valueOf(taskSerial), Integer.valueOf(npcSerial));
            return;
        }
        Npc npc = DataGlobalFunc.createNpc(role, npcSerial, type);
        if (npc == null) {
            this.logger.error("动态创建NPC未找到 = 任务编号：{}，创建NPC编号：{}", Integer.valueOf(taskSerial), Integer.valueOf(npcSerial));
            return;
        }
        List<EasyNpc> npcList = doingTask.getEasyNpcList();
        doingTask.setNpcId(npcSerial);
        npc.setId(npcSerial);
        npcList.add(new EasyNpc(npc, t));
        return;
    }
    
    public boolean condition(Role role, TaskSet taskSet, byte type) {
        int n;
        if (type == 0) {
            n = taskSet.getGetCondition();
        }
        else {
            n = taskSet.getSubmitCondition();
        }
        int condition = n;
        if (condition > 0) {
            SysDataTaskCondition taskCondition = (SysDataTaskCondition)TaskDataPool.taskConditionMap.get(Integer.valueOf(condition));
            int needGrade = taskCondition.getNeedGrade();
            if (needGrade > 0) {
                int curve = (int)role.getRelive() * 1000 + (int)role.getLevel();
                if (curve < needGrade) {
                    return false;
                }
            }
            if (type == 1) {
                if (!this.isPrevTaskFinished(role, taskSet)) {
                    return false;
                }
                if (taskCondition.getNeedCounter().size() > 0) {
                    DoingTask doingTask = (DoingTask)role.getTask().getDoingTask().get(Integer.valueOf(taskSet.getSerial()));
                    if (doingTask == null) {
                        return false;
                    }
                    for (Param param : taskCondition.getNeedCounter()) {
                        Map<Integer, Integer> changeCounter = doingTask.getChangeCounter();
                        if (changeCounter == null) {
                            this.logger.error("检查任务计数错误：{}", taskCondition.getNeedCounter());
                            return false;
                        }
                        Integer num = (Integer)changeCounter.get(param.getK());
                        if (num == null || (int)num < (int)param.getV()) {
                            return false;
                        }
                        continue;
                    }
                    doingTask.getEasyNpcList().clear();
                }
            }
        }
        return true;
    }
    
    public void pushEndTask(Role role, TaskSet taskSet, byte type) {
        RespTasksDel respTasksDel = this.getRespTasksDel(role, taskSet, type);
        MessagePusher.pushMessage(role, respTasksDel);
    }
    
    public RespTasksDel getRespTasksDel(Role role, TaskSet taskSet, byte type) {
        RespTasksDel respTasksDel = new RespTasksDel();
        respTasksDel.setSerial(taskSet.getSerial());
        respTasksDel.setType(type);
        return respTasksDel;
    }
    
    public void pushTaskInfo(Role role, TaskInfo taskInfo) {
        if (taskInfo == null) {
            return;
        }
        if (role.getTask().isAccepted(taskInfo.getSerial())) {
            RespTasks respTasks = new RespTasks();
            ArrayList<TaskInfo> taskList = new ArrayList<>();
            respTasks.setList(taskList);
            taskList.add(taskInfo);
            MessagePusher.pushMessage(role, respTasks);
        }
        return;
    }
    
    public void dailyResetTask(Role role) {
        List<Integer> acceptedTaskList = role.getTask().getAcceptedTaskList();
        TaskSet taskSet = null;
        for (Integer taskId : acceptedTaskList) {
            taskSet = this.getTaskSet((int)taskId);
            if (taskSet == null) {
                role.getTask().deleteTask((int)taskId);
            }
            if (taskSet != null && taskSet.getTaskType() != TaskType.MAIN_LINE) {
                TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType());
                if (taskHandler == null) {
                    continue;
                }
                if (!role.getTask().isAccepted(taskSet.getSerial())) {
                    continue;
                }
                boolean flag = taskHandler.resetTask(role, taskSet);
                if (flag) {
                    continue;
                }
                taskHandler.pushTask(role, taskSet, 1);
            }
            else {
                continue;
            }
        }
    }
    
    public void requestGiveUpTask(Role role, int serial) {
        TaskSet taskSet = this.getTaskSet(serial);
        if (taskSet == null) {
            return;
        }
        TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType());
        if (taskHandler != null && role.getTask().isAccepted(taskSet.getSerial())) {
            taskHandler.giveUpTask(role, taskSet);
        }
        return;
    }
}
