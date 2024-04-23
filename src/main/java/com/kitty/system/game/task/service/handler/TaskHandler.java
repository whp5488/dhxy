package com.kitty.system.game.task.service.handler;

import com.kitty.cache.RedisService;
import com.kitty.common.thread.IdGenerator;
import com.kitty.common.utils.RandomUtil;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.logs.LoggerFunction;
import com.kitty.logs.LoggerUtils;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.task.TaskType;
import com.kitty.system.game.activity.model.Dugeon;
import com.kitty.system.game.activity.service.ActivityService;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.email.service.EmailService;
import com.kitty.system.game.map.service.MapService;
import com.kitty.system.game.npc.service.NpcService;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.task.domain.DoingTask;
import com.kitty.system.game.task.domain.RoleTask;
import com.kitty.system.game.task.message.RespTaskFightEndNotify;
import com.kitty.system.game.task.message.RespTaskIcon;
import com.kitty.system.game.task.message.RespTasksDel;
import com.kitty.system.game.task.message.vo.TaskInfo;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.task.service.TaskService;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.vo.EasyNpc;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.init.domain.SysDataTaskFight;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class TaskHandler {
  protected Logger logger = LoggerFunction.TASK.getLogger();
  
  @Autowired
  protected TaskService taskService;
  
  @Autowired
  protected MapService mapService;
  
  @Autowired
  protected NpcService npcService;
  
  @Autowired
  protected RoleService roleService;
  
  @Autowired
  protected EmailService emailService;
  
  @Autowired
  protected PetService petService;
  
  @Autowired
  protected TeamService teamService;
  
  @Autowired
  protected ActivityService activityService;
  
  @Autowired
  protected RedisService redisService;
  
  public boolean acceptTask(Role role, TaskSet taskSet, int expire) {
    if (!canAcceptTask(role, taskSet))
      return false; 
    doBeforeAccept(role, taskSet);
    doAcceptTask(role, taskSet, expire);
    doAfterAccept(role, taskSet);
    return true;
  }
  
  public boolean canAcceptTask(Role role, TaskSet taskSet) {
    if (!this.taskService.condition(role, taskSet, (byte)0))
      return false; 
    if (this.taskService.isTask(role, taskSet))
      return false; 
    return true;
  }
  
  public void doBeforeAccept(Role role, TaskSet taskSet) {}
  
  public boolean doAcceptTask(Role role, TaskSet taskSet, int expire) {
    RoleTask roleTask = role.getTask();
    roleTask.acceptTask(taskSet.getSerial(), taskSet.getPara(), expire);
    dstNpc(role, taskSet);
    return true;
  }
  
  public void dstNpc(Role role, TaskSet taskSet) {
    if (taskSet.getDstNpc().size() > 0) {
      int index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(taskSet.getDstNpc().size() - 1));
      Integer serial = taskSet.getDstNpc().get(index);
      Npc npc = this.mapService.getNpc(serial.intValue());
      if (npc != null) {
        DoingTask doingTask = (DoingTask)role.getTask().getDoingTask().get(Integer.valueOf(taskSet.getSerial()));
        if (doingTask != null) {
          List<EasyNpc> npcList = doingTask.getEasyNpcList();
          npcList.add(new EasyNpc(npc, taskSet.getKind()));
        } 
      } 
    } 
  }
  
  public void doAfterAccept(Role role, TaskSet taskSet) {
    List<Integer> addTaskEvent = taskSet.getAddTaskEvent();
    this.taskService.editTaskEvent(role, addTaskEvent, taskSet, getTaskPara(role, taskSet));
    pushTask(role, taskSet, 0);
    role.save();
  }
  
  public static String getTaskPara(Role role, TaskSet taskSet) {
    DoingTask task = (DoingTask)role.getTask().getDoingTask().get(Integer.valueOf(taskSet.getSerial()));
    if (task == null)
      return null; 
    return task.getPara();
  }
  
  public boolean submitTask(Role role, TaskSet taskSet) {
    if (!role.getTask().isCanSubmit(taskSet.getSerial()))
      return false; 
    doBeforeSubmit(role, taskSet);
    doSubmitTask(role, taskSet);
    doAfterSubmit(role, taskSet);
    return true;
  }
  
  public void doBeforeSubmit(Role role, TaskSet taskSet) {}
  
  public void doSubmitTask(Role role, TaskSet taskSet) {
    String para = getTaskPara(role, taskSet);
    this.taskService.deleteTask(role, taskSet);
    this.taskService.editTaskEvent(role, taskSet.getFinishTaskEvent(), taskSet, para);
    role.save();
  }
  
  public void doAfterSubmit(Role role, TaskSet taskSet) {
    giveReward(role, taskSet);
  }
  
  public void pushTask(Role role, TaskSet taskSet, int refresh) {
    try {
      TaskInfo taskInfo = getTaskInfo(role, taskSet);
      taskInfo.setRefresh((byte)refresh);
      this.taskService.pushTaskInfo(role, taskInfo);
    } catch (Exception e) {
      e.printStackTrace();
      LoggerUtils.error("推送任务异常：{}", e);
    } 
  }
  
  public TaskInfo getTaskInfo(Role role, TaskSet taskSet) {
    return this.taskService.getTaskInfo(role, taskSet);
  }
  
  private boolean isPushEnd(TaskSet taskSet, TaskSet nextTask) {
    if (nextTask == null || nextTask.getNextTaskSerial() != taskSet.getSerial())
      return true; 
    return false;
  }
  
  public void taskScreenIcon(Role role, int serial, String difficulty, byte type) {
    MessagePusher.pushMessage(role, (Message)new RespTaskIcon(serial, difficulty, type));
  }
  
  public void giveReward(Role role, TaskSet taskSet) {
    this.taskService.giveAward(role, taskSet);
  }
  
  public boolean giveUpTask(Role role, TaskSet taskSet) {
    if (!canGiveUpTask(role, taskSet))
      return false; 
    if (taskSet.isTeamAccept()) {
      if (!this.teamService.isTeamLeader(role))
        return false; 
      this.teamService.memberHandleThreadLocal(role, memberRole -> giveUpTaskExecute(memberRole, taskSet));
    } else {
      giveUpTaskExecute(role, taskSet);
    } 
    return true;
  }
  
  public void giveUpTaskExecute(Role role, TaskSet taskSet) {
    doBeforeGiveUpTask(role, taskSet);
    doGiveUpTask(role, taskSet);
    doAfterGiveUp(role, taskSet, (byte)2);
  }
  
  public boolean canGiveUpTask(Role role, TaskSet taskSet) {
    return (taskSet.getGiveUp() == 1);
  }
  
  public void doBeforeGiveUpTask(Role role, TaskSet taskSet) {}
  
  public void doGiveUpTask(Role role, TaskSet taskSet) {
    role.getTask().giveUpTask(taskSet.getSerial());
    role.save();
  }
  
  public void doAfterGiveUp(Role role, TaskSet taskSet, byte type) {
    pushEndTask(role, taskSet, type);
  }
  
  public boolean resetTask(Role role, TaskSet taskSet) {
    if (!canResetTask(role, taskSet))
      return false; 
    doBoforeResetTask(role, taskSet);
    doResetTask(role, taskSet);
    doAfterReset(role, taskSet, (byte)1);
    return true;
  }
  
  public boolean forceResetTask(Role role, TaskSet taskSet) {
    doBoforeResetTask(role, taskSet);
    doResetTask(role, taskSet);
    doAfterReset(role, taskSet, (byte)2);
    return true;
  }
  
  public boolean canResetTask(Role role, TaskSet taskSet) {
    return false;
  }
  
  public void doBoforeResetTask(Role role, TaskSet taskSet) {}
  
  public void doResetTask(Role role, TaskSet taskSet) {
    role.getTask().resetTask(taskSet.getSerial());
    role.save();
  }
  
  public void doAfterReset(Role role, TaskSet taskSet, byte type) {
    pushEndTask(role, taskSet, type);
  }
  
  public void pushEndTask(Role role, TaskSet taskSet, byte type) {
    this.taskService.pushEndTask(role, taskSet, type);
    endTaskTips(role);
  }
  
  public void endTaskTips(Role role) {}
  
  public List<TaskSet> getAcceptedTaskListMatchTaskType(Role role, TaskType taskType) {
    List<Integer> acceptedTaskList = null;
    try {
      acceptedTaskList = role.getTask().getAcceptedTaskList();
    } catch (ConcurrentModificationException e) {
      LoggerUtils.error("{}({}),taskType:{},sessionId:{}", new Object[] { role.getRoleId(), role.getName(), taskType, e.getMessage(), e });
    } 
    if (acceptedTaskList != null) {
      List<TaskSet> taskList = new ArrayList<>(acceptedTaskList.size());
      for (Integer taskId : acceptedTaskList) {
        TaskSet taskSet = this.taskService.getTaskSet(taskId.intValue());
        if (!this.taskService.isMatchTaskType(taskSet, taskType))
          continue; 
        taskList.add(taskSet);
      } 
      return taskList;
    } 
    return Collections.emptyList();
  }
  
  public void doFightEnd(Role role, FightEndEvent fightEndEvent) {
    switch (fightEndEvent.getStatus()) {
      case 1:
        doFightWin(role, fightEndEvent);
        break;
      case 2:
        doFightFail(role, fightEndEvent);
        break;
      case 3:
        doFightRunAway(role, fightEndEvent);
        break;
      case 40:
        doFightMaxRound(role, fightEndEvent);
        break;
    } 
  }
  
  public void doFightWin(Role role, FightEndEvent fightEndEvent) {
    RoleTask roleTask = role.getTask();
    Integer taskId = roleTask.getTaskIdByFightId(fightEndEvent.getFightId());
    if (taskId == null)
      return; 
    TaskSet taskSet = this.taskService.getTaskSet(taskId.intValue());
    SysDataTaskFight taskFight = (SysDataTaskFight)TaskDataPool.taskFightMap.get(Integer.valueOf(fightEndEvent.getNpcId()));
    if (taskFight != null && taskFight.getTaskSerial() == fightEndEvent.getTaskId()) {
      this.taskService.editTaskEvent(role, taskFight.getWinEvent(), taskSet, getTaskPara(role, taskSet));
      if (roleTask.isAccepted(fightEndEvent.getTaskId())) {
        MessagePusher.pushMessage(role, (Message)new RespTaskFightEndNotify(taskId.intValue(), fightEndEvent.getNpcId(), (byte)1));
        pushTask(role, taskSet, 1);
      } 
    } 
  }
  
  public void doFightFail(Role role, FightEndEvent fightEndEvent) {
    SysDataTaskFight taskFight = (SysDataTaskFight)TaskDataPool.taskFightMap.get(Integer.valueOf(fightEndEvent.getNpcId()));
    if (taskFight != null && taskFight.getTaskSerial() == fightEndEvent.getTaskId()) {
      TaskSet taskSet = this.taskService.getTaskSet(fightEndEvent.getTaskId());
      this.taskService.editTaskEvent(role, taskFight.getLoseEvent(), taskSet, getTaskPara(role, taskSet));
      MessagePusher.pushMessage(role, (Message)new RespTaskFightEndNotify(fightEndEvent.getTaskId(), fightEndEvent.getNpcId(), (byte)2));
    } 
  }
  
  public void doFightRunAway(Role role, FightEndEvent fightEndEvent) {
    doFightFail(role, fightEndEvent);
  }
  
  public void doFightMaxRound(Role role, FightEndEvent fightEndEvent) {
    doFightFail(role, fightEndEvent);
  }
  
  public void handleLogin(Role role) {}
  
  public void startFight(Role role, TaskType taskType, Npc npc, int minTeamCount, int needRei, int needLv, String needFightNot, String needLvNot) {
    if (role == null)
      return; 
    try {
      TaskSet taskSet = getTaskSetByType(role, taskType);
      if (taskSet == null)
        return; 
      DoingTask doingTask = (DoingTask)role.getTask().getDoingTask().get(Integer.valueOf(taskSet.getSerial()));
      if (doingTask != null)
        for (int i = 0; i < doingTask.getEasyNpcList().size(); i++) {
          EasyNpc easyNpc = doingTask.getEasyNpcList().get(i);
          if (easyNpc != null && easyNpc.getId() == npc.getId()) {
            if (this.teamService.getTeamCount(role) < minTeamCount) {
              MessagePusher.pushMessage(role, (Message)new RespMsg(String.format(needFightNot, new Object[] { Integer.valueOf(minTeamCount) })));
              return;
            } 
            if (!canTeamGrade(role, needRei, needLv, needLvNot))
              return; 
            this.taskService.taskTriggerFight(role, taskSet, -1);
            break;
          } 
        }  
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public TaskSet getTaskSetByType(Role role) {
    List<TaskSet> acceptedTaskListMatchTaskType = this.taskService.getAcceptedTaskListMatchTaskType(role, getTaskType());
    if (acceptedTaskListMatchTaskType.isEmpty())
      return null; 
    return acceptedTaskListMatchTaskType.get(0);
  }
  
  public TaskSet getTaskSetByType(Role role, TaskType taskType) {
    List<TaskSet> acceptedTaskListMatchTaskType = this.taskService.getAcceptedTaskListMatchTaskType(role, taskType);
    if (acceptedTaskListMatchTaskType.isEmpty())
      return null; 
    return acceptedTaskListMatchTaskType.get(0);
  }
  
  public void syncTeamTask(Role role, DoingTask doingTask) {
    RoleTask task = role.getTask();
    task.getAcceptedTasks().put(Integer.valueOf(doingTask.getTaskId()), Byte.valueOf((byte)0));
    task.getDoingTask().put(Integer.valueOf(doingTask.getTaskId()), doingTask);
    pushTask(role, this.taskService.getTaskSet(doingTask.getTaskId()), 0);
    role.save();
  }
  
  public void delTask(Role role, TaskSet taskSet) {
    role.getTask().updateToFinished(taskSet.getSerial());
    RespTasksDel respTasksDel = new RespTasksDel();
    respTasksDel.setSerial(taskSet.getSerial());
    respTasksDel.setType((byte)2);
    MessagePusher.pushMessage(role, (Message)respTasksDel);
  }
  
  public Dugeon getDungeon(Role role) {
    Long dugeonId = (Long)DataCache.ROLE_DUNGEON.getOrDefault(role.getId(), Long.valueOf(0L));
    if (dugeonId.longValue() > 0L)
      return (Dugeon)DataCache.DUNGEON_MAP.get(dugeonId); 
    return null;
  }
  
  public boolean isCanTeam(Role role, Role otherRole, List<Integer> list) {
    if (role == null || otherRole == null)
      return false; 
    if (list.contains(role.getPos().getMapId()) || list.contains(otherRole.getPos().getMapId())) {
      if (isInDungeon(role, list) && isInDungeon(otherRole, list)) {
        long roleDugeonId = ((Long)DataCache.ROLE_DUNGEON.getOrDefault(role.getId(), Long.valueOf(0L))).longValue();
        long otherDugeonId = ((Long)DataCache.ROLE_DUNGEON.getOrDefault(otherRole.getId(), Long.valueOf(0L))).longValue();
        return (roleDugeonId == otherDugeonId);
      } 
      return false;
    } 
    return true;
  }
  
  public boolean isInDungeon(Role role, List<Integer> list) {
    if (role == null)
      return false; 
    return list.contains(role.getPos().getMapId());
  }
  
  public void deleteDungeon(Dugeon dugeon) {
    if (dugeon != null) {
      DataCache.DUNGEON_MAP.remove(Long.valueOf(dugeon.getId()));
      for (Iterator<Long> iterator = dugeon.getMemberList().iterator(); iterator.hasNext(); ) {
        long uid = ((Long)iterator.next()).longValue();
        DataCache.ROLE_DUNGEON.remove(Long.valueOf(uid));
      } 
    } 
  }
  
  public void leaveDungeon(Role role, int map, int x, int y, String param) {
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (team != null) {
      if (team.isLeader(role.getRoleId().intValue())) {
        List<Member> list = team.getList();
        for (int i = 0; i < list.size(); i++) {
          Member member = list.get(i);
          Role player = this.roleService.getPlayer(member.getId().longValue());
          if (player != null)
            player.pushTempCache("not_check_change_map", Boolean.valueOf(true)); 
        } 
      } else {
        role.pushTempCache("not_check_change_map", Boolean.valueOf(true));
      } 
    } else {
      role.pushTempCache("not_check_change_map", Boolean.valueOf(true));
    } 
    this.mapService.changeMap(role, map, x, y, param);
  }
  
  public void canSubmitTask(Role role, TaskSet taskSet) {}
  
  public boolean canTeamGrade(Role role, int needRei, int needLevel, String needNot) {
    String names = this.teamService.checkMember(role, memberRole -> (DataGlobalFunc.needGradeJudge(needRei, needLevel, memberRole.getRelive().intValue(), memberRole.getLevel().intValue(), "你的#<R>角色等级#不足") != null));
    if (names != null) {
      String msg = needLevel + "级";
      if (needRei > 0)
        msg = needRei + "转" + msg; 
      MessagePusher.pushMessage(role, (Message)new RespMsg(String.format(needNot, new Object[] { names, msg })));
      return false;
    } 
    return true;
  }
  
  public void createDungeon(Team team) {
    List<Long> memberList = new ArrayList<>();
    memberList.add(Long.valueOf(team.getLeaderUid()));
    this.teamService.memberHandle(team, memberRole -> {
          if (!memberList.contains(memberRole.getId()))
            memberList.add(memberRole.getId()); 
        });
    Dugeon dugeon = new Dugeon(IdGenerator.getNextId(), memberList);
    DataCache.DUNGEON_MAP.put(Long.valueOf(dugeon.getId()), dugeon);
    for (Iterator<Long> iterator = memberList.iterator(); iterator.hasNext(); ) {
      long uid = ((Long)iterator.next()).longValue();
      DataCache.ROLE_DUNGEON.put(Long.valueOf(uid), Long.valueOf(dugeon.getId()));
    } 
  }
  
  public void activationTips(Role role, int step) {
    MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "你获得了#<G>" + step + "#点活跃度", true));
  }
  
  public abstract TaskType getTaskType();
}
