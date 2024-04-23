package com.kitty.system.game.task.service;

import com.kitty.cache.RedisService;
import com.kitty.common.model.Pos;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.listener.event.LoginEvent;
import com.kitty.listener.event.RoleLevelUpEvent;
import com.kitty.logs.LoggerFunction;
import com.kitty.logs.LoggerUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.mina.task.ThreadLocalUtil;
import com.kitty.system.boss.model.BossFightParam;
import com.kitty.system.boss.service.BossService;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.enums.task.TaskEvent;
import com.kitty.system.enums.task.TaskType;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.system.game.map.service.MapService;
import com.kitty.system.game.npc.service.NpcService;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.service.MonsterService;
import com.kitty.system.game.task.domain.DoingTask;
import com.kitty.system.game.task.domain.RoleTask;
import com.kitty.system.game.task.message.ReqUpTaskEvent;
import com.kitty.system.game.task.message.RespTasks;
import com.kitty.system.game.task.message.RespTasksDel;
import com.kitty.system.game.task.message.vo.TaskInfo;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.task.service.handler.CrusadeRenegadeTaskHandler;
import com.kitty.system.game.task.service.handler.TaskHandler;
import com.kitty.system.game.task.service.handler.TaskHelper;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.vo.EasyNpc;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysDataMonster;
import com.kitty.system.init.domain.SysDataTaskCondition;
import com.kitty.system.init.domain.SysDataTaskEvent;
import com.kitty.system.init.domain.SysDataTaskEventForeign;
import com.kitty.system.init.domain.SysDataTaskFight;
import com.kitty.system.init.domain.SysDataTaskForeign;
import com.kitty.system.init.domain.SysDataTaskMain;
import com.kitty.system.init.domain.SysDataTaskReward;
import com.kitty.system.init.domain.SysWarRole;
import com.kitty.system.init.domain.vo.Param;
import com.kitty.system.queue.BroadcastThread;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class TaskService {
  protected Logger logger = LoggerFunction.TASK.getLogger();
  
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
  
  @EventHandler({EventType.LOGIN})
  public void handleLoginEvent(LoginEvent loginEvent) {
    Role role = loginEvent.getRole();
    pushTasksOnLogin(role);
  }
  
  public TaskSet getTaskSet(int taskId) {
    TaskSet taskSet = (TaskSet)TaskDataPool.taskMap.get(Integer.valueOf(taskId));
    if (taskSet != null)
      return taskSet; 
    return null;
  }
  
  public List<TaskSet> getAcceptedTasksMatchTarget(Role role) {
    List<Integer> acceptedTaskList = role.getTask().getAcceptedTaskList();
    List<TaskSet> matchTaskList = new ArrayList<>(acceptedTaskList.size());
    for (Integer taskId : acceptedTaskList) {
      TaskSet taskSet = getTaskSet(taskId.intValue());
      if (taskSet == null)
        continue; 
      matchTaskList.add(taskSet);
    } 
    return matchTaskList;
  }
  
  public void updateTargetProgress(Role role, TaskSet taskSet) {
    if (this.teamService.isInTeam(role)) {
      if (this.teamService.isTeamLeader(role))
        if (taskSet.isTeamAccept()) {
          this.teamService.memberHandleThreadLocal(role, memberRole -> updateSingleTargetProgress(memberRole, taskSet));
        } else {
          updateSingleTargetProgress(role, taskSet);
        }  
    } else {
      updateSingleTargetProgress(role, taskSet);
    } 
  }
  
  public void updateSingleTargetProgress(Role role, TaskSet taskSet) {
    RoleTask roleTask = role.getTask();
    if (!roleTask.isAccepted(taskSet.getSerial()))
      return; 
    roleTask.updateTargetProgress(taskSet.getSerial());
    this.logger.trace("更新任务进度：任务: {}", taskSet);
    triggerTask(role, taskSet);
  }
  
  private void triggerTask(Role role, TaskSet taskSet) {
    if (taskSet.getTaskType() == TaskType.MAIN_LINE)
      TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType()).submitTask(role, taskSet); 
    if (role.getTask().isCanSubmit(taskSet.getSerial())) {
      TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType());
      if (taskHandler != null)
        taskHandler.submitTask(role, taskSet); 
    } 
  }
  
  @EventHandler({EventType.ROLE_LEVEL_UP})
  public void handleRoleLevelUpEvent(RoleLevelUpEvent roleLevelUpEvent) {
    Role role = roleLevelUpEvent.getRole();
    if (role == null)
      return; 
    Map<Integer, SysDataTaskMain> taskMainMap = TaskDataPool.taskMainMap;
    List<Integer> acceptedTaskList = role.getTask().getAcceptedTaskList();
    for (Integer serial : acceptedTaskList) {
      if (taskMainMap.get(serial) != null) {
        TaskSet taskSet = getTaskSet(serial.intValue());
        editTaskEvent(role, taskSet.getAddTaskEvent(), taskSet, TaskHandler.getTaskPara(role, taskSet));
      } 
    } 
  }
  
  public boolean isTask(Role role, TaskSet taskSet) {
    if (taskSet == null || role == null)
      return false; 
    RoleTask roleTask = role.getTask();
    return roleTask.isAccepted(taskSet.getSerial());
  }
  
  public boolean isMatchTaskType(TaskSet taskSet, TaskType taskType) {
    if (taskSet == null || taskSet.getTaskType() == null)
      return false; 
    if (taskSet.getTaskType() != taskType)
      return false; 
    return true;
  }
  
  public void askUpEvent(Role role, ReqUpTaskEvent reqUpTaskEvent) {
    if (role.getTask().isAccepted(reqUpTaskEvent.getTaskId())) {
      TaskSet taskSet = getTaskSet(reqUpTaskEvent.getTaskId());
      if (taskSet == null)
        return; 
      for (Integer key : reqUpTaskEvent.getEvent()) {
        if (taskSet.getAskEvent().contains(key))
          editTaskEvent(role, Arrays.asList(new Integer[] { key }), taskSet, TaskHandler.getTaskPara(role, taskSet));
      } 
      if (role.getTask().isAccepted(taskSet.getSerial())) {
        TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType());
        if (taskHandler != null)
          taskHandler.pushTask(role, taskSet, 1); 
      } 
    } 
  }
  
  public boolean isPrevTaskFinished(Role role, TaskSet taskSet) {
    TaskSet needTask = getTaskSet(taskSet.getNextTaskSerial());
    if (needTask == null)
      return true; 
    DoingTask doingTask = (DoingTask)role.getTask().getDoingTask().get(Integer.valueOf(taskSet.getSerial()));
    if (doingTask == null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("请先领取任务"));
      return false;
    } 
    if (taskSet.getSerial() != needTask.getPreviousId()) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("请先完成上一任务任务"));
      return false;
    } 
    return true;
  }
  
  public void giveAward(Role role, TaskSet taskSet) {
    Map<String, SysDataTaskReward> taskRewardMap = TaskDataPool.taskRewardMap;
    SysDataTaskReward taskReward = taskRewardMap.get(taskSet.getTaskReward());
    if (taskReward != null) {
      List<Param> rewards = taskReward.getRewards();
      if (taskReward.getType() > 0) {
        SysDataTaskForeign foreign = TaskDataPool.taskRewardForeign.stream().filter(e -> (e.getSerial() == taskReward.getType() && e.getRoleIcon() == role.getIcon())).findFirst().orElse(null);
        if (foreign == null) {
          this.logger.error("未找到奖励：任务编号 = {}, 角色外观 = {}", Integer.valueOf(taskSet.getSerial()), Integer.valueOf(role.getIcon()));
          return;
        } 
        SysDataTaskReward reward = taskRewardMap.get(foreign.getTaskRewardSerial());
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
      if (doingTask.getChangeCounter() != null && doingTask.getChangeCounter().size() > 0)
        taskInfo.setList(Arrays.asList(new FiedValue[] { new FiedValue("changeCounter", (byte)4, JsonUtils.object2String(doingTask.getChangeCounter())) })); 
      taskInfo.setTaskUpdateTime(doingTask.getUpdate());
    } 
    return taskInfo;
  }
  
  public List<TaskSet> getAcceptedTaskListMatchTaskType(Role role, TaskType taskType) {
    List<Integer> acceptedTaskList = null;
    try {
      acceptedTaskList = role.getTask().getAcceptedTaskList();
    } catch (ConcurrentModificationException e) {
      e.getStackTrace();
      LoggerUtils.error("{}({}),taskType:{},sessionId:{}", new Object[] { role.getRoleId(), role.getName(), taskType, e.getMessage(), e });
    } 
    if (acceptedTaskList != null) {
      List<TaskSet> taskList = new ArrayList<>(acceptedTaskList.size());
      for (Integer taskId : acceptedTaskList) {
        TaskSet taskSet = getTaskSet(taskId.intValue());
        if (!isMatchTaskType(taskSet, taskType))
          continue; 
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
    for (Iterator<Integer> iterator = acceptedTaskList.iterator(); iterator.hasNext(); ) {
      int taskId = ((Integer)iterator.next()).intValue();
      TaskSet taskSet = getTaskSet(taskId);
      if (taskSet == null || taskSet.getTaskType() == null) {
        new RuntimeException("任务数据异常:" + taskId);
        continue;
      } 
      TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType());
      if (taskHandler != null)
        taskHandler.pushTask(role, taskSet, 1); 
    } 
  }
  
  public void deleteTask(Role role, TaskSet taskSet) {
    deleteTaskForSingle(role, taskSet);
  }
  
  private void doDeleteTaskForTeam(Team team, TaskSet taskSet) {
    doDeleteTaskForTeam(team.getList(), team.getLeaderRoleId(), taskSet);
  }
  
  public void doDeleteTaskForTeam(List<Member> members, int leaderRoleId, TaskSet taskSet) {
    for (Member member : members) {
      if (member == null)
        continue; 
      if (!member.isInTeam())
        continue; 
      if (leaderRoleId == member.getRoleId().intValue()) {
        Role role = this.roleService.getPlayer(member.getRoleId().intValue());
        deleteTaskForSingle(role, taskSet);
        continue;
      } 
      deleteMemberTask(member, taskSet);
    } 
  }
  
  public void deleteTaskForSingle(Role role, TaskSet taskSet) {
    if (!isTask(role, taskSet))
      return; 
    int taskNpcId = role.getTask().getTaskNpcId(taskSet.getSerial());
    MapService mapService = (MapService)SpringUtils.getBean(MapService.class);
    Npc npc = mapService.getNpc(taskNpcId);
    role.getTask().updateToFinished(taskSet.getSerial());
    RespTasksDel respTasksDel = new RespTasksDel();
    respTasksDel.setSerial(taskSet.getSerial());
    MessagePusher.pushMessage(role, (Message)respTasksDel);
  }
  
  private void deleteMemberTask(Member member, TaskSet taskSet) {
    Role role = this.roleService.getPlayer(member.getId().longValue());
    if (role == null)
      return; 
    ThreadLocalUtil.addLocalTask(role, () -> deleteTaskForSingle(role, taskSet));
  }
  
  public void changeCounterTask(Role role, TaskSet taskSet, TaskEvent taskEvent, int serial, int num) {
    Team team = ((TeamService)SpringUtils.getBean(TeamService.class)).getTeam(role.getRoleId().intValue());
    if (this.teamService.isInTeam(role)) {
      doChangeCounterTaskForTeam(team, taskSet, taskEvent, serial, num);
    } else {
      changeCounterTaskForSingle(role, taskSet, taskEvent, serial, num);
    } 
  }
  
  private void doChangeCounterTaskForTeam(Team team, TaskSet taskSet, TaskEvent taskEvent, int serial, int num) {
    doChangeCounterTaskForTeam(team.getList(), team.getLeaderRoleId(), taskSet, taskEvent, serial, num);
  }
  
  public void doChangeCounterTaskForTeam(List<Member> members, int leaderRoleId, TaskSet taskSet, TaskEvent taskEvent, int serial, int num) {
    for (Member member : members) {
      if (member == null)
        continue; 
      if (!member.isInTeam())
        continue; 
      if (leaderRoleId == member.getRoleId().intValue()) {
        Role role = this.roleService.getPlayer(member.getRoleId().intValue());
        changeCounterTaskForSingle(role, taskSet, taskEvent, serial, num);
        continue;
      } 
      changeCounterMemberTask(member, taskSet, taskEvent, serial, num);
    } 
  }
  
  private void changeCounterMemberTask(Member member, TaskSet taskSet, TaskEvent taskEvent, int serial, int num) {
    Role role = this.roleService.getPlayer(member.getId().longValue());
    if (role == null)
      return; 
    ThreadLocalUtil.addLocalTask(role, () -> changeCounterTaskForSingle(role, taskSet, taskEvent, serial, num));
  }
  
  public void changeCounterTaskForSingle(Role role, TaskSet taskSet, TaskEvent taskEvent, int serial, int num) {
    if (!isTask(role, taskSet))
      return; 
    role.getTask().updateChangeCounterTask(taskSet.getSerial(), taskEvent, serial, num);
    TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType());
    if (taskHandler != null) {
      taskHandler.pushTask(role, taskSet, 1);
    } else {
      TaskInfo taskInfo = getTaskInfo(role, taskSet);
      if (taskInfo != null)
        pushTaskInfo(role, taskInfo); 
    } 
    role.save();
  }
  
  public void taskTriggerFight(Role role, TaskSet taskSet, int serial) {
    if (SpringUtils.getFightNewService().isInFight(role))
      return; 
    SysDataTaskFight taskFight = null;
    try {
      if (serial > 0) {
        taskFight = (SysDataTaskFight)TaskDataPool.taskFightMap.get(Integer.valueOf(serial));
      } else {
        taskFight = TaskDataPool.taskFightMap.values().stream().filter(e -> (e.getTaskSerial() == taskSet.getSerial())).findFirst().orElse(null);
      } 
      if (taskFight != null && taskFight.getTaskSerial() == taskSet.getSerial()) {
        SysWarRole warRole = (SysWarRole)FightDataPool.data_war_role.get(Integer.valueOf(taskFight.getTarNpcData()));
        if (warRole != null) {
          List<SysDataMonster> list = ((MonsterService)SpringUtils.getBean(MonsterService.class)).getMonsters(FightDataPool.data_monster, warRole);
          if (CollectionUtils.isEmpty(list))
            return; 
          Integer max = Collections.<Integer>max(this.teamService.getMemberLevel(role));
          if (taskSet.getTaskType().equals(TaskType.CRUSADE_RENEGADE))
            for (int i = 0; i < list.size(); i++) {
              SysDataMonster monster = list.get(i);
              int monsterSerial = CrusadeRenegadeTaskHandler.getMonster(max.intValue(), monster.getSerial().intValue());
              if (!monster.getSerial().equals(Integer.valueOf(monsterSerial))) {
                SysDataMonster dataMonster = (SysDataMonster)FightDataPool.data_monster.get(Integer.valueOf(serial));
                if (dataMonster != null)
                  list.set(i, dataMonster); 
              } 
            }  
          List<SysDataMonster> monsters = new ArrayList<>(list.size());
          monsters.addAll(list);
          DoingTask doingTask = (DoingTask)role.getTask().getDoingTask().get(Integer.valueOf(taskSet.getSerial()));
          if (doingTask != null) {
            EasyNpc easyNpc = null;
            if (doingTask.getEasyNpcList().size() > 0)
              easyNpc = doingTask.getEasyNpcList().stream().filter(e -> (e.getId() == doingTask.getNpcId())).findFirst().orElse(null); 
            for (SysDataMonster monster : monsters) {
              if (Const.taskTypeList.contains(taskSet.getTaskType()))
                DataGlobalFunc.createMonster(max.intValue(), monster); 
              if (easyNpc != null)
                if (monster.getIsBoss() != null && monster.getIsBoss().intValue() == 1) {
                  monster.setName(easyNpc.getN());
                  monster.setShape(Integer.valueOf(easyNpc.getS()));
                }  
            } 
          } 
          BossFightParam bossFightParam = new BossFightParam(monsters, FIGHT_TYPE.TASK.getValue(), taskSet.getSerial(), taskFight.getSerial());
          Fight fight = ((BossService)SpringUtils.getBean(BossService.class)).startFightToBoss(role, bossFightParam);
          if (fight != null)
            role.getTask().addRecord4FightAndTask(fight.getId(), taskSet.getSerial(), -1); 
        } else {
          MessagePusher.pushMessage(role, (Message)new RespMsg("未找到战场怪物数据"));
        } 
      } else {
        MessagePusher.pushMessage(role, (Message)new RespMsg("未找到本场战斗数据"));
      } 
    } catch (Exception e) {
      e.printStackTrace();
      this.logger.error("任务获取战斗异常：{}", e.getMessage());
    } 
  }
  
  public void doFightEnd(Role role, FightEndEvent fightEndEvent) {
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (this.teamService.isInTeam(role) && !this.teamService.isTeamLeader(role))
      return; 
    Integer taskId = role.getTask().getTaskIdByFightId(fightEndEvent.getFightId());
    if (taskId == null) {
      fightEndLeaveTeam(team);
      return;
    } 
    TaskSet taskSet = getTaskSet(taskId.intValue());
    TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType());
    if (taskHandler != null)
      taskHandler.doFightEnd(role, fightEndEvent); 
    fightEndLeaveTeam(team);
  }
  
  public void fightEndLeaveTeam(Team team) {
    if (team != null) {
      for (String key : team.getFightLeaveNutMap().keySet()) {
        int anInt = team.getFightLeaveNutMap().getInt(key, -1);
        if (anInt > -1) {
          Role role = this.roleService.getPlayer(Long.valueOf(key).longValue());
          if (role != null) {
            if (anInt == 1) {
              this.teamService.leave(role, false);
              continue;
            } 
            if (anInt == 0)
              this.teamService.leaveTemp(role, true); 
          } 
        } 
      } 
      team.getFightLeaveNutMap().clear();
    } 
  }
  
  public boolean delItemTaskEvent(Role role, TaskSet taskSet) {
    List<Integer> list = taskSet.getFinishTaskEvent();
    Map<Integer, SysDataTaskEvent> taskEventMap = TaskDataPool.taskEventMap;
    for (Integer serial : list) {
      SysDataTaskEvent taskEvent = taskEventMap.get(serial);
      if (taskEvent != null)
        if (taskEvent.getType() == TaskEvent.DelItem) {
          SysDataTaskEventForeign foreign = TaskDataPool.taskEventForeign.stream().filter(obj -> (obj.getSerial() == taskEvent.getForeign())).findFirst().orElse(null);
          if (foreign != null && foreign.getType() == TaskEvent.DelItem) {
            Integer integer = this.bagService.consumeBagGoods(role, foreign.getK(), foreign.getV(), false, true, "任务提交");
            if (integer != null) {
              Map<Long, BagEquip> bagEquipMap = this.bagService.getBagEquip(role);
              BagEquip equip = bagEquipMap.values().stream().filter(e -> (e.getType() == foreign.getK())).findFirst().orElse(null);
              if (equip != null) {
                this.bagService.deleteRoleEquip(role, equip, "任务扣除");
                return true;
              } 
              MessagePusher.pushMessage(role, (Message)new RespMsg("你背包中没有该道具"));
              return false;
            } 
          } 
        }  
    } 
    return true;
  }
  
  public void editTaskEvent(Role role, List<Integer> list, TaskSet taskSet, String para) {
    try {
      if (list.size() <= 0)
        return; 
      Map<Integer, SysDataTaskEvent> taskEventMap = TaskDataPool.taskEventMap;
      List<SysDataTaskEvent> objects = new ArrayList<>();
      for (Integer serial : list) {
        SysDataTaskEvent taskEvent = taskEventMap.get(serial);
        if (taskEvent != null)
          if (taskEvent.getType() != TaskEvent.DelItem)
            objects.add(taskEvent);  
      } 
      for (SysDataTaskEvent taskEvent : objects) {
        if (taskEvent.getType() == TaskEvent.AddNewTask) {
          TaskSet task = getTaskSet(taskEvent.getEvent());
          if (task != null) {
            TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(task.getTaskType());
            if (taskHandler != null) {
              task.setPara(para);
              taskHandler.acceptTask(role, task, -1);
            } 
          } 
          continue;
        } 
        if (taskEvent.getType() == TaskEvent.TeamFinishTask) {
          TaskSet task = getTaskSet(taskEvent.getEvent());
          updateTargetProgress(role, task);
          continue;
        } 
        if (taskEvent.getType() == TaskEvent.FinishTask) {
          TaskSet task = getTaskSet(taskEvent.getEvent());
          updateSingleTargetProgress(role, task);
          continue;
        } 
        if (taskEvent.getType() == TaskEvent.NewReward) {
          List<SysDataTaskEventForeign> collect = (List<SysDataTaskEventForeign>)TaskDataPool.taskEventForeign.stream().filter(obj -> (obj.getSerial() == taskEvent.getForeign())).collect(Collectors.toList());
          for (SysDataTaskEventForeign foreign : collect) {
            Map<String, SysDataTaskReward> taskRewardMap = TaskDataPool.taskRewardMap;
            SysDataTaskReward taskReward = taskRewardMap.get(foreign.getOther());
            if (taskReward != null) {
              List<Param> rewards = taskReward.getRewards();
              this.bagService.sendReward(role, rewards, "奖励事件", 0);
            } 
          } 
          continue;
        } 
        if (taskEvent.getType() == TaskEvent.TeamChangeCounter) {
          List<SysDataTaskEventForeign> collect = (List<SysDataTaskEventForeign>)TaskDataPool.taskEventForeign.stream().filter(obj -> (obj.getSerial() == taskEvent.getForeign())).collect(Collectors.toList());
          for (SysDataTaskEventForeign foreign : collect) {
            if (foreign.getType() == TaskEvent.Add || foreign.getType() == TaskEvent.Set) {
              changeCounterTask(role, taskSet, foreign.getType(), foreign.getK(), foreign.getV());
              condition(role, taskSet, (byte)1);
            } 
          } 
          continue;
        } 
        if (taskEvent.getType() == TaskEvent.ChangeCounter) {
          List<SysDataTaskEventForeign> collect = (List<SysDataTaskEventForeign>)TaskDataPool.taskEventForeign.stream().filter(obj -> (obj.getSerial() == taskEvent.getForeign())).collect(Collectors.toList());
          for (SysDataTaskEventForeign foreign : collect) {
            if (foreign.getType() == TaskEvent.Add || foreign.getType() == TaskEvent.Set) {
              changeCounterTaskForSingle(role, taskSet, foreign.getType(), foreign.getK(), foreign.getV());
              condition(role, taskSet, (byte)1);
            } 
          } 
          continue;
        } 
        if (taskEvent.getType() == TaskEvent.ChangeShape) {
          if (taskEvent.getEvent() <= 0) {
            role.setIcon(role.getTask().getIcon());
          } else {
            role.setIcon(taskEvent.getEvent());
          } 
          SpringUtils.getBroadcastService().sendUpdateAppear(role);
          continue;
        } 
        if (taskEvent.getType() == TaskEvent.SendUser) {
          SysDataTaskEventForeign foreign = TaskDataPool.taskEventForeign.stream().filter(obj -> (obj.getSerial() == taskEvent.getForeign())).findFirst().orElse(null);
          if (foreign != null) {
            Pos pos = (Pos)JsonUtils.string2Object(foreign.getOther(), Pos.class);
            if (pos != null)
              this.mapService.changeMap(role, new ReqTelePort(pos.getMapId().intValue(), pos.getX().intValue(), pos.getY().intValue(), 1), null); 
          } 
          continue;
        } 
        if (taskEvent.getType() == TaskEvent.CreateNpc) {
          List<SysDataTaskEventForeign> collect = (List<SysDataTaskEventForeign>)TaskDataPool.taskEventForeign.stream().filter(obj -> (obj.getSerial() == taskEvent.getForeign())).collect(Collectors.toList());
          for (SysDataTaskEventForeign foreign : collect) {
            if (foreign.getType() == TaskEvent.CreateNpc)
              writeNpc(role, taskSet.getSerial(), foreign.getV(), 1, (byte)1); 
          } 
          continue;
        } 
        if (taskEvent.getType() == TaskEvent.DestroyNpc) {
          int event = taskEvent.getEvent();
          DoingTask doingTask = (DoingTask)role.getTask().getDoingTask().get(Integer.valueOf(taskSet.getSerial()));
          if (doingTask != null) {
            List<EasyNpc> npcList = doingTask.getEasyNpcList();
            for (int i = 0; i < doingTask.getEasyNpcList().size(); i++) {
              EasyNpc easyNpc = doingTask.getEasyNpcList().get(i);
              if (easyNpc != null && easyNpc.getId() == event) {
                npcList.remove(i);
                break;
              } 
            } 
          } 
          continue;
        } 
        if (taskEvent.getType() == TaskEvent.AddTaskItem)
          continue; 
        if (taskEvent.getType() == TaskEvent.DelTaskItem);
      } 
    } catch (Exception e) {
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
  }
  
  public boolean condition(Role role, TaskSet taskSet, byte type) {
    int condition = (type == 0) ? taskSet.getGetCondition() : taskSet.getSubmitCondition();
    if (condition > 0) {
      SysDataTaskCondition taskCondition = (SysDataTaskCondition)TaskDataPool.taskConditionMap.get(Integer.valueOf(condition));
      int needGrade = taskCondition.getNeedGrade();
      if (needGrade > 0) {
        int curve = role.getRelive().intValue() * 1000 + role.getLevel().intValue();
        if (curve < needGrade)
          return false; 
      } 
      if (type == 1) {
        if (!isPrevTaskFinished(role, taskSet))
          return false; 
        if (taskCondition.getNeedCounter().size() > 0) {
          DoingTask doingTask = (DoingTask)role.getTask().getDoingTask().get(Integer.valueOf(taskSet.getSerial()));
          if (doingTask == null)
            return false; 
          for (Param param : taskCondition.getNeedCounter()) {
            Map<Integer, Integer> changeCounter = doingTask.getChangeCounter();
            if (changeCounter == null) {
              this.logger.error("检查任务计数错误：{}", taskCondition.getNeedCounter());
              return false;
            } 
            Integer num = changeCounter.get(param.getK());
            if (num == null || num.intValue() < param.getV().intValue())
              return false; 
          } 
          doingTask.getEasyNpcList().clear();
        } 
      } 
    } 
    return true;
  }
  
  public void pushEndTask(Role role, TaskSet taskSet, byte type) {
    RespTasksDel respTasksDel = getRespTasksDel(role, taskSet, type);
    MessagePusher.pushMessage(role, (Message)respTasksDel);
  }
  
  public RespTasksDel getRespTasksDel(Role role, TaskSet taskSet, byte type) {
    RespTasksDel respTasksDel = new RespTasksDel();
    respTasksDel.setSerial(taskSet.getSerial());
    respTasksDel.setType(type);
    return respTasksDel;
  }
  
  public void pushTaskInfo(Role role, TaskInfo taskInfo) {
    if (taskInfo == null)
      return; 
    if (role.getTask().isAccepted(taskInfo.getSerial())) {
      RespTasks respTasks = new RespTasks();
      ArrayList<TaskInfo> taskList = new ArrayList<>();
      respTasks.setList(taskList);
      taskList.add(taskInfo);
      MessagePusher.pushMessage(role, (Message)respTasks);
    } 
  }
  
  public void dailyResetTask(Role role) {
    List<Integer> acceptedTaskList = role.getTask().getAcceptedTaskList();
    TaskSet taskSet = null;
    for (Integer taskId : acceptedTaskList) {
      taskSet = getTaskSet(taskId.intValue());
      if (taskSet == null)
        role.getTask().deleteTask(taskId.intValue()); 
      if (taskSet != null && taskSet.getTaskType() != TaskType.MAIN_LINE) {
        TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType());
        if (taskHandler != null && role.getTask().isAccepted(taskSet.getSerial())) {
          boolean flag = taskHandler.resetTask(role, taskSet);
          if (!flag)
            taskHandler.pushTask(role, taskSet, 1); 
        } 
      } 
    } 
  }
  
  public void requestGiveUpTask(Role role, int serial) {
    TaskSet taskSet = getTaskSet(serial);
    if (taskSet == null)
      return; 
    TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType());
    if (taskHandler != null && role.getTask().isAccepted(taskSet.getSerial()))
      taskHandler.giveUpTask(role, taskSet); 
  }
}
