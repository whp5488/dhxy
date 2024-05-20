
package com.kitty.system.game.task.service;

import com.kitty.system.game.task.message.RespTasks;
import com.kitty.system.game.task.service.handler.ConquerAsuraTaskHandler;
import com.kitty.system.init.domain.SysDataTaskCondition;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.common.model.Pos;
import java.util.stream.Collector;
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
import java.util.Collection;
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
import com.kitty.mina.message.Message;
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
    public void handleLoginEvent(final LoginEvent loginEvent) {
        final Role role = loginEvent.getRole();
        this.pushTasksOnLogin(role);
    }
    
    public TaskSet getTaskSet(final int taskId) {
        final TaskSet taskSet = TaskDataPool.taskMap.get(taskId);
        if (taskSet != null) {
            return taskSet;
        }
        return null;
    }
    
    public List<TaskSet> getAcceptedTasksMatchTarget(final Role role) {
        final List<Integer> acceptedTaskList = role.getTask().getAcceptedTaskList();
        final List<TaskSet> matchTaskList = new ArrayList<TaskSet>(acceptedTaskList.size());
        for (final Integer taskId : acceptedTaskList) {
            final TaskSet taskSet = this.getTaskSet(taskId);
            if (taskSet == null) {
                continue;
            }
            matchTaskList.add(taskSet);
        }
        return matchTaskList;
    }
    
    public void updateTargetProgress(final Role role, final TaskSet taskSet) {
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
    
    public void updateSingleTargetProgress(final Role role, final TaskSet taskSet) {
        final RoleTask roleTask = role.getTask();
        if (!roleTask.isAccepted(taskSet.getSerial())) {
            return;
        }
        roleTask.updateTargetProgress(taskSet.getSerial());
        this.logger.trace("更新任务进度：任务: {}", (Object)taskSet);
        this.triggerTask(role, taskSet);
    }
    
    private void triggerTask(final Role role, final TaskSet taskSet) {
        if (taskSet.getTaskType() == TaskType.MAIN_LINE) {
            TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType()).submitTask(role, taskSet);
        }
        if (role.getTask().isCanSubmit(taskSet.getSerial())) {
            final TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType());
            if (taskHandler != null) {
                taskHandler.submitTask(role, taskSet);
            }
        }
    }
    
    @EventHandler({ EventType.ROLE_LEVEL_UP })
    public void handleRoleLevelUpEvent(final RoleLevelUpEvent roleLevelUpEvent) {
        final Role role = roleLevelUpEvent.getRole();
        if (role == null) {
            return;
        }
        final Map<Integer, SysDataTaskMain> taskMainMap = TaskDataPool.taskMainMap;
        final List<Integer> acceptedTaskList = role.getTask().getAcceptedTaskList();
        for (final Integer serial : acceptedTaskList) {
            if (taskMainMap.get(serial) != null) {
                final TaskSet taskSet = this.getTaskSet(serial);
                this.editTaskEvent(role, taskSet.getAddTaskEvent(), taskSet, TaskHandler.getTaskPara(role, taskSet));
            }
        }
    }
    
    public boolean isTask(final Role role, final TaskSet taskSet) {
        if (taskSet == null || role == null) {
            return false;
        }
        final RoleTask roleTask = role.getTask();
        return roleTask.isAccepted(taskSet.getSerial());
    }
    
    public boolean isMatchTaskType(final TaskSet taskSet, final TaskType taskType) {
        return taskSet != null && taskSet.getTaskType() != null && taskSet.getTaskType() == taskType;
    }
    
    public void askUpEvent(final Role role, final ReqUpTaskEvent reqUpTaskEvent) {
        if (role.getTask().isAccepted(reqUpTaskEvent.getTaskId())) {
            final TaskSet taskSet = this.getTaskSet(reqUpTaskEvent.getTaskId());
            if (taskSet == null) {
                return;
            }
            for (final Integer key : reqUpTaskEvent.getEvent()) {
                if (taskSet.getAskEvent().contains(key)) {
                    this.editTaskEvent(role, Arrays.asList(key), taskSet, TaskHandler.getTaskPara(role, taskSet));
                }
            }
            if (role.getTask().isAccepted(taskSet.getSerial())) {
                final TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType());
                if (taskHandler != null) {
                    taskHandler.pushTask(role, taskSet, 1);
                }
            }
        }
    }
    
    public boolean isPrevTaskFinished(final Role role, final TaskSet taskSet) {
        final TaskSet needTask = this.getTaskSet(taskSet.getNextTaskSerial());
        if (needTask == null) {
            return true;
        }
        final DoingTask doingTask = role.getTask().getDoingTask().get(taskSet.getSerial());
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
    
    public void giveAward(final Role role, final TaskSet taskSet) {
        final Map<String, SysDataTaskReward> taskRewardMap = TaskDataPool.taskRewardMap;
        final SysDataTaskReward taskReward = taskRewardMap.get(taskSet.getTaskReward());
        if (taskReward != null) {
            List<Param> rewards = taskReward.getRewards();
            if (taskReward.getType() > 0) {
                final SysDataTaskForeign foreign = TaskDataPool.taskRewardForeign.stream().filter(e -> e.getSerial() == taskReward.getType() && e.getRoleIcon() == role.getIcon()).findFirst().orElse(null);
                if (foreign == null) {
                    this.logger.error("未找到奖励：任务编号 = {}, 角色外观 = {}", (Object)taskSet.getSerial(), (Object)role.getIcon());
                    return;
                }
                final SysDataTaskReward reward = taskRewardMap.get(foreign.getTaskRewardSerial());
                rewards = reward.getRewards();
            }
            this.bagService.sendReward(role, rewards, "任务", 0);
        }
    }
    
    public TaskInfo getTaskInfo(final Role role, final TaskSet taskSet) {
        final TaskInfo taskInfo = new TaskInfo();
        taskInfo.setSerial(taskSet.getSerial());
        taskInfo.setQuit((byte)taskSet.getGiveUp());
        final DoingTask doingTask = role.getTask().getDoingTask().get(taskSet.getSerial());
        if (doingTask != null) {
            taskInfo.setNpcList(doingTask.getEasyNpcList());
            if (doingTask.getChangeCounter() != null && doingTask.getChangeCounter().size() > 0) {
                taskInfo.setList(Arrays.asList(new FiedValue("changeCounter", (byte)4, JsonUtils.object2String(doingTask.getChangeCounter()))));
            }
            taskInfo.setTaskUpdateTime(doingTask.getUpdate());
        }
        return taskInfo;
    }
    
    public List<TaskSet> getAcceptedTaskListMatchTaskType(final Role role, final TaskType taskType) {
        List<Integer> acceptedTaskList = null;
        try {
            acceptedTaskList = role.getTask().getAcceptedTaskList();
        }
        catch (final ConcurrentModificationException e) {
            e.getStackTrace();
            LoggerUtils.error("{}({}),taskType:{},sessionId:{}", role.getRoleId(), role.getName(), taskType, e.getMessage(), e);
        }
        if (acceptedTaskList != null) {
            final List<TaskSet> taskList = new ArrayList<TaskSet>(acceptedTaskList.size());
            for (final Integer taskId : acceptedTaskList) {
                final TaskSet taskSet = this.getTaskSet(taskId);
                if (!this.isMatchTaskType(taskSet, taskType)) {
                    continue;
                }
                taskList.add(taskSet);
            }
            return taskList;
        }
        return Collections.emptyList();
    }
    
    public void pushTasksOnLogin(final Role role) {
        if (role.getTask() == null) {
            this.logger.error("上线加载任务异常：玩家 = {}，任务数据 = {}", role.getRoleId(), (Object)role.getTask());
            role.setTask(new RoleTask());
        }
        final List<Integer> acceptedTaskList = role.getTask().getAcceptedTaskList();
        for (final int taskId : acceptedTaskList) {
            final TaskSet taskSet = this.getTaskSet(taskId);
            if (taskSet == null || taskSet.getTaskType() == null) {
                new RuntimeException("任务数据异常:" + taskId);
            }
            else {
                final TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType());
                if (taskHandler == null) {
                    continue;
                }
                taskHandler.pushTask(role, taskSet, 1);
            }
        }
    }
    
    public void deleteTask(final Role role, final TaskSet taskSet) {
        this.deleteTaskForSingle(role, taskSet);
    }
    
    private void doDeleteTaskForTeam(final Team team, final TaskSet taskSet) {
        this.doDeleteTaskForTeam(team.getList(), team.getLeaderRoleId(), taskSet);
    }
    
    public void doDeleteTaskForTeam(final List<Member> members, final int leaderRoleId, final TaskSet taskSet) {
        for (final Member member : members) {
            if (member == null) {
                continue;
            }
            if (!member.isInTeam()) {
                continue;
            }
            if (leaderRoleId == member.getRoleId()) {
                final Role role = this.roleService.getPlayer(member.getRoleId());
                this.deleteTaskForSingle(role, taskSet);
            }
            else {
                this.deleteMemberTask(member, taskSet);
            }
        }
    }
    
    public void deleteTaskForSingle(final Role role, final TaskSet taskSet) {
        if (!this.isTask(role, taskSet)) {
            return;
        }
        final int taskNpcId = role.getTask().getTaskNpcId(taskSet.getSerial());
        final MapService mapService = SpringUtils.getBean(MapService.class);
        final Npc npc = mapService.getNpc(taskNpcId);
        role.getTask().updateToFinished(taskSet.getSerial());
        final RespTasksDel respTasksDel = new RespTasksDel();
        respTasksDel.setSerial(taskSet.getSerial());
        MessagePusher.pushMessage(role, respTasksDel);
    }
    
    private void deleteMemberTask(final Member member, final TaskSet taskSet) {
        final Role role = this.roleService.getPlayer(member.getId());
        if (role == null) {
            return;
        }
        ThreadLocalUtil.addLocalTask(role, () -> this.deleteTaskForSingle(role, taskSet));
    }
    
    public void changeCounterTask(final Role role, final TaskSet taskSet, final TaskEvent taskEvent, final int serial, final int num) {
        final Team team = SpringUtils.getBean(TeamService.class).getTeam(role.getRoleId());
        if (this.teamService.isInTeam(role)) {
            this.doChangeCounterTaskForTeam(team, taskSet, taskEvent, serial, num);
        }
        else {
            this.changeCounterTaskForSingle(role, taskSet, taskEvent, serial, num);
        }
    }
    
    private void doChangeCounterTaskForTeam(final Team team, final TaskSet taskSet, final TaskEvent taskEvent, final int serial, final int num) {
        this.doChangeCounterTaskForTeam(team.getList(), team.getLeaderRoleId(), taskSet, taskEvent, serial, num);
    }
    
    public void doChangeCounterTaskForTeam(final List<Member> members, final int leaderRoleId, final TaskSet taskSet, final TaskEvent taskEvent, final int serial, final int num) {
        for (final Member member : members) {
            if (member == null) {
                continue;
            }
            if (!member.isInTeam()) {
                continue;
            }
            if (leaderRoleId == member.getRoleId()) {
                final Role role = this.roleService.getPlayer(member.getRoleId());
                this.changeCounterTaskForSingle(role, taskSet, taskEvent, serial, num);
            }
            else {
                this.changeCounterMemberTask(member, taskSet, taskEvent, serial, num);
            }
        }
    }
    
    private void changeCounterMemberTask(final Member member, final TaskSet taskSet, final TaskEvent taskEvent, final int serial, final int num) {
        final Role role = this.roleService.getPlayer(member.getId());
        if (role == null) {
            return;
        }
        ThreadLocalUtil.addLocalTask(role, () -> this.changeCounterTaskForSingle(role, taskSet, taskEvent, serial, num));
    }
    
    public void changeCounterTaskForSingle(final Role role, final TaskSet taskSet, final TaskEvent taskEvent, final int serial, final int num) {
        if (!this.isTask(role, taskSet)) {
            return;
        }
        role.getTask().updateChangeCounterTask(taskSet.getSerial(), taskEvent, serial, num);
        final TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType());
        if (taskHandler != null) {
            taskHandler.pushTask(role, taskSet, 1);
        }
        else {
            final TaskInfo taskInfo = this.getTaskInfo(role, taskSet);
            if (taskInfo != null) {
                this.pushTaskInfo(role, taskInfo);
            }
        }
        role.save();
    }
    
    public void taskTriggerFight(final Role role, final TaskSet taskSet, final int serial) {
        if (SpringUtils.getFightNewService().isInFight(role)) {
            return;
        }
        SysDataTaskFight taskFight = null;
        try {
            if (serial > 0) {
                taskFight = TaskDataPool.taskFightMap.get(serial);
            }
            else {
                taskFight = TaskDataPool.taskFightMap.values().stream().filter(e -> e.getTaskSerial() == taskSet.getSerial()).findFirst().orElse(null);
            }
            if (taskFight != null && taskFight.getTaskSerial() == taskSet.getSerial()) {
                final SysWarRole warRole = FightDataPool.data_war_role.get(taskFight.getTarNpcData());
                if (warRole != null) {
                    final List<SysDataMonster> list = SpringUtils.getBean(MonsterService.class).getMonsters(FightDataPool.data_monster, warRole);
                    if (CollectionUtils.isEmpty(list)) {
                        return;
                    }
                    final Integer max = Collections.max((Collection<? extends Integer>)this.teamService.getMemberLevel(role));
                    if (taskSet.getTaskType().equals(TaskType.CRUSADE_RENEGADE)) {
                        for (int i = 0; i < list.size(); ++i) {
                            final SysDataMonster monster = list.get(i);
                            final int monsterSerial = CrusadeRenegadeTaskHandler.getMonster(max, monster.getSerial());
                            if (!monster.getSerial().equals(monsterSerial)) {
                                final SysDataMonster dataMonster = FightDataPool.data_monster.get(serial);
                                if (dataMonster != null) {
                                    list.set(i, dataMonster);
                                }
                            }
                        }
                    } else if (taskSet.getTaskType().equals(TaskType.XL)) {
                        for (int i = 0; i < list.size(); ++i) {
                            final SysDataMonster monster = list.get(i);
                            final int monsterSerial = ConquerAsuraTaskHandler.getMonster(max, monster.getSerial());
                            if (!monster.getSerial().equals(monsterSerial)) {
                                final SysDataMonster dataMonster = FightDataPool.data_monster.get(serial);
                                if (dataMonster != null) {
                                    list.set(i, dataMonster);
                                }
                            }
                        }
                    }
                    final List<SysDataMonster> monsters = new ArrayList<SysDataMonster>(list.size());
                    monsters.addAll(list);
                    final DoingTask doingTask = role.getTask().getDoingTask().get(taskSet.getSerial());
                    if (doingTask != null) {
                        EasyNpc easyNpc = null;
                        if (doingTask.getEasyNpcList().size() > 0) {
                            easyNpc = doingTask.getEasyNpcList().stream().filter(e -> e.getId() == doingTask.getNpcId()).findFirst().orElse(null);
                        }
                        for (final SysDataMonster monster2 : monsters) {
                            if (Const.taskTypeList.contains(taskSet.getTaskType())) {
                                DataGlobalFunc.createMonster(max, monster2);
                            }
                            if (easyNpc != null && monster2.getIsBoss() != null && monster2.getIsBoss() == 1) {
                                monster2.setName(easyNpc.getN());
                                monster2.setShape((int)easyNpc.getS());
                            }
                        }
                    }
                    final BossFightParam bossFightParam = new BossFightParam(monsters, FIGHT_TYPE.TASK.getValue(), taskSet.getSerial(), taskFight.getSerial());
                    final Fight fight = SpringUtils.getBean(BossService.class).startFightToBoss(role, bossFightParam);
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
        catch (final Exception e) {
            e.printStackTrace();
            this.logger.error("任务获取战斗异常：{}", e.getMessage());
        }
    }
    
    public void doFightEnd(final Role role, final FightEndEvent fightEndEvent) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (this.teamService.isInTeam(role) && !this.teamService.isTeamLeader(role)) {
            return;
        }
        final Integer taskId = role.getTask().getTaskIdByFightId(fightEndEvent.getFightId());
        if (taskId == null) {
            this.fightEndLeaveTeam(team);
            return;
        }
        final TaskSet taskSet = this.getTaskSet(taskId);
        final TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType());
        if (taskHandler != null) {
            taskHandler.doFightEnd(role, fightEndEvent);
        }
        this.fightEndLeaveTeam(team);
    }
    
    public void fightEndLeaveTeam(final Team team) {
        if (team != null) {
            for (final String key : team.getFightLeaveNutMap().keySet()) {
                final int anInt = team.getFightLeaveNutMap().getInt(key, -1);
                if (anInt > -1) {
                    final Role role = this.roleService.getPlayer(Long.valueOf(key));
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
            }
            team.getFightLeaveNutMap().clear();
        }
    }
    
    public boolean delItemTaskEvent(final Role role, final TaskSet taskSet) {
        final List<Integer> list = taskSet.getFinishTaskEvent();
        final Map<Integer, SysDataTaskEvent> taskEventMap = TaskDataPool.taskEventMap;
        for (final Integer serial : list) {
            final SysDataTaskEvent taskEvent = taskEventMap.get(serial);
            if (taskEvent != null && taskEvent.getType() == TaskEvent.DelItem) {
                final SysDataTaskEventForeign foreign = TaskDataPool.taskEventForeign.stream().filter(obj -> obj.getSerial() == taskEvent.getForeign()).findFirst().orElse(null);
                if (foreign == null || foreign.getType() != TaskEvent.DelItem) {
                    continue;
                }
                final Integer integer = this.bagService.consumeBagGoods(role, foreign.getK(), foreign.getV(), false, true, "任务提交");
                if (integer == null) {
                    continue;
                }
                final Map<Long, BagEquip> bagEquipMap = this.bagService.getBagEquip(role);
                final BagEquip equip = bagEquipMap.values().stream().filter(e -> e.getType() == foreign.getK()).findFirst().orElse(null);
                if (equip != null) {
                    this.bagService.deleteRoleEquip(role, equip, "任务扣除");
                    return true;
                }
                MessagePusher.pushMessage(role, new RespMsg("你背包中没有该道具"));
                return false;
            }
        }
        return true;
    }
    
    public void editTaskEvent(final Role role, final List<Integer> list, final TaskSet taskSet, final String para) {
        try {
            if (list.size() <= 0) {
                return;
            }
            final Map<Integer, SysDataTaskEvent> taskEventMap = TaskDataPool.taskEventMap;
            final List<SysDataTaskEvent> objects = new ArrayList<SysDataTaskEvent>();
            SysDataTaskEvent taskEvent = null;
            for (final Integer serial : list) {
                taskEvent = taskEventMap.get(serial);
                if (taskEvent != null && taskEvent.getType() != TaskEvent.DelItem) {
                    objects.add(taskEvent);
                }
            }
            for (final SysDataTaskEvent taskEvent2 : objects) {
                if (taskEvent2.getType() == TaskEvent.AddNewTask) {
                    final TaskSet task = this.getTaskSet(taskEvent2.getEvent());
                    if (task == null) {
                        continue;
                    }
                    final TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(task.getTaskType());
                    if (taskHandler == null) {
                        continue;
                    }
                    task.setPara(para);
                    taskHandler.acceptTask(role, task, -1);
                }
                else if (taskEvent2.getType() == TaskEvent.TeamFinishTask) {
                    final TaskSet task = this.getTaskSet(taskEvent2.getEvent());
                    this.updateTargetProgress(role, task);
                }
                else if (taskEvent2.getType() == TaskEvent.FinishTask) {
                    final TaskSet task = this.getTaskSet(taskEvent2.getEvent());
                    this.updateSingleTargetProgress(role, task);
                }
                else if (taskEvent2.getType() == TaskEvent.NewReward) {
                    final List<SysDataTaskEventForeign> collect = TaskDataPool.taskEventForeign.stream().filter(obj -> obj.getSerial() == taskEvent2.getForeign()).collect(Collectors.toList());
                    for (final SysDataTaskEventForeign foreign : collect) {
                        final Map<String, SysDataTaskReward> taskRewardMap = TaskDataPool.taskRewardMap;
                        final SysDataTaskReward taskReward = taskRewardMap.get(foreign.getOther());
                        if (taskReward != null) {
                            final List<Param> rewards = taskReward.getRewards();
                            this.bagService.sendReward(role, rewards, "奖励事件", 0);
                        }
                    }
                }
                else if (taskEvent2.getType() == TaskEvent.TeamChangeCounter) {
                    final List<SysDataTaskEventForeign> collect = TaskDataPool.taskEventForeign.stream().filter(obj -> obj.getSerial() == taskEvent2.getForeign()).collect(Collectors.toList());
                    for (final SysDataTaskEventForeign foreign : collect) {
                        if (foreign.getType() == TaskEvent.Add || foreign.getType() == TaskEvent.Set) {
                            this.changeCounterTask(role, taskSet, foreign.getType(), foreign.getK(), foreign.getV());
                            this.condition(role, taskSet, (byte)1);
                        }
                    }
                }
                else if (taskEvent2.getType() == TaskEvent.ChangeCounter) {
                    final List<SysDataTaskEventForeign> collect = TaskDataPool.taskEventForeign.stream().filter(obj -> obj.getSerial() == taskEvent2.getForeign()).collect(Collectors.toList());
                    for (final SysDataTaskEventForeign foreign : collect) {
                        if (foreign.getType() == TaskEvent.Add || foreign.getType() == TaskEvent.Set) {
                            this.changeCounterTaskForSingle(role, taskSet, foreign.getType(), foreign.getK(), foreign.getV());
                            this.condition(role, taskSet, (byte)1);
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
                    final SysDataTaskEventForeign foreign2 = TaskDataPool.taskEventForeign.stream().filter(obj -> obj.getSerial() == taskEvent2.getForeign()).findFirst().orElse(null);
                    if (foreign2 == null) {
                        continue;
                    }
                    final Pos pos = JsonUtils.string2Object(foreign2.getOther(), Pos.class);
                    if (pos == null) {
                        continue;
                    }
                    this.mapService.changeMap(role, new ReqTelePort(pos.getMapId(), pos.getX(), pos.getY(), 1), null);
                }
                else if (taskEvent2.getType() == TaskEvent.CreateNpc) {
                    final List<SysDataTaskEventForeign> collect = TaskDataPool.taskEventForeign.stream().filter(obj -> obj.getSerial() == taskEvent2.getForeign()).collect(Collectors.toList());
                    for (final SysDataTaskEventForeign foreign : collect) {
                        if (foreign.getType() == TaskEvent.CreateNpc) {
                            this.writeNpc(role, taskSet.getSerial(), foreign.getV(), 1, (byte)1);
                        }
                    }
                }
                else if (taskEvent2.getType() == TaskEvent.DestroyNpc) {
                    final int event = taskEvent2.getEvent();
                    final DoingTask doingTask = role.getTask().getDoingTask().get(taskSet.getSerial());
                    if (doingTask == null) {
                        continue;
                    }
                    final List<EasyNpc> npcList = doingTask.getEasyNpcList();
                    for (int i = 0; i < doingTask.getEasyNpcList().size(); ++i) {
                        final EasyNpc easyNpc = doingTask.getEasyNpcList().get(i);
                        if (easyNpc != null && easyNpc.getId() == event) {
                            npcList.remove(i);
                            break;
                        }
                    }
                }
                else {
                    if (taskEvent2.getType() == TaskEvent.AddTaskItem) {
                        continue;
                    }
                    if (taskEvent2.getType() == TaskEvent.DelTaskItem) {}
                }
            }
        }
        catch (final Exception e) {
            this.logger.error("任务触发事件错误---------玩家编号：{}，任务编号{}，任务类型：{}", new Object[] { role.getRoleId(), taskSet.getSerial(), taskSet.getTaskType().getId() });
            e.printStackTrace();
        }
    }
    
    public void writeNpc(final Role role, final int taskSerial, final int npcSerial, final int type, final byte t) {
        final DoingTask doingTask = role.getTask().getDoingTask().get(taskSerial);
        if (doingTask == null) {
            this.logger.error("给任务写入NPC，玩家没有该任务，任务编号 = {}，创建NPC编号：{}", (Object)taskSerial, (Object)npcSerial);
            return;
        }
        final Npc npc = DataGlobalFunc.createNpc(role, npcSerial, type);
        if (npc == null) {
            this.logger.error("动态创建NPC未找到 = 任务编号：{}，创建NPC编号：{}", (Object)taskSerial, (Object)npcSerial);
            return;
        }
        final List<EasyNpc> npcList = doingTask.getEasyNpcList();
        doingTask.setNpcId(npcSerial);
        npc.setId(npcSerial);
        npcList.add(new EasyNpc(npc, t));
    }
    
    public boolean condition(final Role role, final TaskSet taskSet, final byte type) {
        final int condition = (type == 0) ? taskSet.getGetCondition() : taskSet.getSubmitCondition();
        if (condition > 0) {
            final SysDataTaskCondition taskCondition = TaskDataPool.taskConditionMap.get(condition);
            final int needGrade = taskCondition.getNeedGrade();
            if (needGrade > 0) {
                final int curve = role.getRelive() * 1000 + role.getLevel();
                if (curve < needGrade) {
                    return false;
                }
            }
            if (type == 1) {
                if (!this.isPrevTaskFinished(role, taskSet)) {
                    return false;
                }
                if (taskCondition.getNeedCounter().size() > 0) {
                    final DoingTask doingTask = role.getTask().getDoingTask().get(taskSet.getSerial());
                    if (doingTask == null) {
                        return false;
                    }
                    for (final Param param : taskCondition.getNeedCounter()) {
                        final Map<Integer, Integer> changeCounter = doingTask.getChangeCounter();
                        if (changeCounter == null) {
                            this.logger.error("检查任务计数错误：{}", taskCondition.getNeedCounter());
                            return false;
                        }
                        final Integer num = changeCounter.get(param.getK());
                        if (num == null || num < param.getV()) {
                            return false;
                        }
                    }
                    doingTask.getEasyNpcList().clear();
                }
            }
        }
        return true;
    }
    
    public void pushEndTask(final Role role, final TaskSet taskSet, final byte type) {
        final RespTasksDel respTasksDel = this.getRespTasksDel(role, taskSet, type);
        MessagePusher.pushMessage(role, respTasksDel);
    }
    
    public RespTasksDel getRespTasksDel(final Role role, final TaskSet taskSet, final byte type) {
        final RespTasksDel respTasksDel = new RespTasksDel();
        respTasksDel.setSerial(taskSet.getSerial());
        respTasksDel.setType(type);
        return respTasksDel;
    }
    
    public void pushTaskInfo(final Role role, final TaskInfo taskInfo) {
        if (taskInfo == null) {
            return;
        }
        if (role.getTask().isAccepted(taskInfo.getSerial())) {
            final RespTasks respTasks = new RespTasks();
            final ArrayList<TaskInfo> taskList = new ArrayList<TaskInfo>();
            respTasks.setList(taskList);
            taskList.add(taskInfo);
            MessagePusher.pushMessage(role, respTasks);
        }
    }
    
    public void dailyResetTask(final Role role) {
        final List<Integer> acceptedTaskList = role.getTask().getAcceptedTaskList();
        TaskSet taskSet = null;
        for (final Integer taskId : acceptedTaskList) {
            taskSet = this.getTaskSet(taskId);
            if (taskSet == null) {
                role.getTask().deleteTask(taskId);
            }
            if (taskSet != null && taskSet.getTaskType() != TaskType.MAIN_LINE) {
                final TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType());
                if (taskHandler == null || !role.getTask().isAccepted(taskSet.getSerial())) {
                    continue;
                }
                final boolean flag = taskHandler.resetTask(role, taskSet);
                if (flag) {
                    continue;
                }
                taskHandler.pushTask(role, taskSet, 1);
            }
        }
    }
    
    public void requestGiveUpTask(final Role role, final int serial) {
        final TaskSet taskSet = this.getTaskSet(serial);
        if (taskSet == null) {
            return;
        }
        final TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType());
        if (taskHandler != null && role.getTask().isAccepted(taskSet.getSerial())) {
            taskHandler.giveUpTask(role, taskSet);
        }
    }
}
