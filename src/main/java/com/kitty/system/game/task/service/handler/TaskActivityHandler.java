package com.kitty.system.game.task.service.handler;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.logs.LoggerUtils;
import com.kitty.mina.task.ThreadLocalUtil;
import com.kitty.system.enums.task.TaskType;
import com.kitty.system.game.activity.service.ActivityHandler;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.map.service.MapService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.task.message.vo.TaskInfo;
import com.kitty.system.game.task.service.TaskService;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.vo.Npc;
import java.util.List;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class TaskActivityHandler extends ActivityHandler {
  @Autowired
  TaskService taskService;
  
  @Autowired
  TeamService teamService;
  
  protected boolean isMatchFightType(Role role, int fightType) {
    return (getFightType(role) == fightType);
  }
  
  public String acceptTask(Role role, TaskSet taskSet, int expire, boolean auto) {
    String content = canAcceptTask(role, taskSet, auto);
    if (content != null)
      return content; 
    doBeforeAccept(role, taskSet);
    doAcceptTask(role, taskSet, expire);
    doAfterAccept(role, taskSet);
    return null;
  }
  
  public void doBeforeAccept(Role role, TaskSet taskSet) {}
  
  public boolean isTeamAcceptTask() {
    return true;
  }
  
  public void doAcceptTask(Role role, TaskSet taskSet, int expire) {
    if (isTeamAcceptTask() && this.teamService.isInTeam(role)) {
      Team team = ((TeamService)SpringUtils.getBean(TeamService.class)).getTeam(role.getRoleId().intValue());
      doAcceptTaskForTeam(team, taskSet, expire);
    } else {
      doAcceptTaskForSingle(role, taskSet, expire);
    } 
  }
  
  public void doAcceptTaskForTeam(Team team, TaskSet taskSet, int expire) {
    Role role = SpringUtils.getRoleService().getPlayer(team.getLeaderUid());
    List<Member> members = team.getList();
    for (int i = 0; i < members.size(); i++) {
      Member member = members.get(i);
      if (member != null)
        if (member.isInTeam())
          if (team.isLeader(member.getRoleId().intValue())) {
            acceptTask(role, taskSet, expire);
          } else {
            acceptMemberTask(member, taskSet, expire);
          }   
    } 
    TeamService teamService = (TeamService)SpringUtils.getBean(TeamService.class);
  }
  
  public boolean isMemberAcceptTask(Role role, TaskSet taskSet) {
    return true;
  }
  
  public void doAcceptTaskForSingle(Role role, TaskSet taskSet, int expire) {
    acceptTask(role, taskSet, expire);
  }
  
  public void acceptTask(Role role, TaskSet taskSet, int expire) {
    role.getTask().acceptTask(taskSet.getSerial(), taskSet.getPara(), expire);
    role.getTask().addTaskNpcId(taskSet.getSerial(), taskSet.getSubmitNpc(), expire);
    TaskInfo taskInfo = getTaskInfo(role, taskSet);
    this.taskService.pushTaskInfo(role, taskInfo);
    role.save();
  }
  
  public void acceptMemberTask(Member member, TaskSet taskSet, int expire) {
    Role role = SpringUtils.getRoleService().getPlayer(member.getId().longValue());
    if (role == null)
      return; 
    if (!isMemberAcceptTask(role, taskSet))
      return; 
    ThreadLocalUtil.addLocalTask(role, () -> acceptTask(role, taskSet, expire));
  }
  
  public void doAfterAccept(Role role, TaskSet taskSet) {}
  
  public void startFight(Role role, TaskSet taskSet) {
    setNpcFightStatus(role, taskSet, true);
  }
  
  public void setNpcFightStatus(Role role, TaskSet taskSet, boolean isInFight) {
    MapService mapService = (MapService)SpringUtils.getBean(MapService.class);
  }
  
  public TaskSet getTaskSetByType(Role role) {
    return getTaskSetByType(role, getTaskType());
  }
  
  public TaskSet getTaskSetByType(Role role, TaskType taskType) {
    List<TaskSet> acceptedTaskListMatchTaskType = this.taskService.getAcceptedTaskListMatchTaskType(role, taskType);
    if (acceptedTaskListMatchTaskType.isEmpty())
      return null; 
    return acceptedTaskListMatchTaskType.get(0);
  }
  
  public boolean isDeleteTaskTeamChange() {
    return false;
  }
  
  public boolean isDeleteTaskLogout() {
    return false;
  }
  
  public void handleTeamLeave(Role role, boolean isLeader, Team team) {
    if (!isDeleteTaskTeamChange())
      return; 
    TaskSet taskSet = getTaskSetByType(role);
    if (taskSet == null)
      return; 
    List<Member> memberList = team.getList();
    if (isLeader) {
      this.taskService.doDeleteTaskForTeam(memberList, role.getRoleId().intValue(), taskSet);
    } else {
      this.taskService.deleteTaskForSingle(role, taskSet);
    } 
  }
  
  public void handleTeamLeaveMoment(Role role) {
    if (!isDeleteTaskTeamChange())
      return; 
    TaskSet taskSet = getTaskSetByType(role);
    if (taskSet == null)
      return; 
    this.taskService.deleteTaskForSingle(role, taskSet);
  }
  
  public void handleTeamChangeLeader(Role role, List<Member> memberList) {
    if (!isDeleteTaskTeamChange())
      return; 
    TaskSet taskSet = getTaskSetByType(role);
    if (taskSet == null)
      return; 
    this.taskService.doDeleteTaskForTeam(memberList, role.getRoleId().intValue(), taskSet);
  }
  
  public void handleLogout(Role role) {
    if (!isDeleteTaskLogout())
      return; 
    TaskSet taskSet = getTaskSetByType(role);
    if (taskSet == null)
      return; 
    if (!this.teamService.isInTeam(role))
      this.taskService.deleteTask(role, taskSet); 
  }
  
  public void handleLogin(Role role) {
    TaskSet taskSet = getTaskSetByType(role);
    if (taskSet == null)
      return; 
    if (isDeleteTaskLogout() && !this.teamService.isInTeam(role)) {
      this.taskService.deleteTask(role, taskSet);
    } else {
      TaskInfo taskInfo = getTaskInfo(role, taskSet);
      this.taskService.pushTaskInfo(role, taskInfo);
    } 
  }
  
  public void handleHeartBeat(Role role, long prevHeartTime) {
    TaskSet taskSet = getTaskSetByType(role);
  }
  
  public void handleFightEnd(FightEndEvent fightEndEvent) {
    ThreadLocalUtil.addLocalTask(fightEndEvent.getRole(), () -> doFightEnd(fightEndEvent.getRole(), fightEndEvent));
  }
  
  private void doFightEnd(Role role, FightEndEvent fightEndEvent) {
    try {
      if (this.teamService.isInTeam(role) && !this.teamService.isTeamLeader(role))
        return; 
      TaskSet taskSet = getTaskSetByType(role);
      if (taskSet == null)
        return; 
      if (!isMatchFightType(role, fightEndEvent.getFightType()))
        return; 
      setNpcFightStatus(role, taskSet, false);
      switch (fightEndEvent.getStatus()) {
        case 1:
          doFightWin(role, taskSet, fightEndEvent);
          break;
        case 2:
          doFightFail(role, taskSet, fightEndEvent);
          break;
        case 3:
          doFightRunAway(role, taskSet, fightEndEvent);
          break;
        case 40:
          doFightMaxRound(role, taskSet, fightEndEvent);
          break;
      } 
    } catch (Exception e) {
      e.printStackTrace();
      LoggerUtils.error(role.getId() + "|" + getClass().getSimpleName() + "|" + e.getMessage(), e);
    } 
  }
  
  private void doFightWin(Role role, TaskSet taskSet, FightEndEvent fightEndEvent) {
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (this.teamService.isInTeam(role)) {
      doFightWinForTeam(team, taskSet, fightEndEvent);
    } else {
      doFightWinForSingle(role, taskSet, fightEndEvent);
    } 
  }
  
  public void doFightWinForTeam(Team team, TaskSet taskSet, FightEndEvent fightEndEvent) {
    int leaderRoleId = team.getLeaderRoleId();
    List<Member> members = team.getList();
    for (int i = 0; i < members.size(); i++) {
      Member member = members.get(i);
      if (member != null)
        if (member.isInTeam())
          if (leaderRoleId != member.getRoleId()) {
            Role role = SpringUtils.getRoleService().getPlayerBy(member.getId());
            if (role != null)
              ThreadLocalUtil.addLocalTask(role, () -> doFightWinForSingle(role, taskSet, fightEndEvent));
          }   
    } 
    Role role = SpringUtils.getRoleService().getPlayer(leaderRoleId);
    doFightWinForSingle(role, taskSet, fightEndEvent);
  }
  
  public void doFightWinForSingle(Role role, TaskSet taskSet, FightEndEvent fightEndEvent) {
    try {
      if (!isMatchFightType(role, fightEndEvent.getFightType()))
        return; 
      if (!isMemberSubmitTask(role, taskSet))
        return; 
      addActivityFinishCount(role, taskSet);
      int currPetId = ((Integer)role.getTempCache("fight_current_pet_id", Integer.valueOf(0))).intValue();
      Npc npc = SpringUtils.getMapService().getNpc(role.getTask().getTaskNpcId(taskSet.getSubmitNpc()));
      role.pushTempCache("drop_monster_name", (npc != null) ? npc.getName() : "");
      giveReward(role, fightEndEvent.getReward(), taskSet, currPetId);
      role.popTempCache("drop_monster_name");
      doBeforeDelete(role, taskSet);
      this.taskService.deleteTaskForSingle(role, taskSet);
      doAfterDelete(role, taskSet);
    } catch (Exception e) {
      e.printStackTrace();
      LoggerUtils.error(role.getId() + "|" + getClass().getSimpleName() + "|" + e.getMessage(), e);
    } 
  }
  
  public void handleRoleExpire(Role role) {
    Fight fight = SpringUtils.getFightNewService().getFightByRoleId(role.getRoleId().intValue());
    if (fight == null)
      return; 
    if (!isMatchFightType(role, fight.getType()))
      return; 
    super.handleRoleExpire(role);
    TaskSet taskSet = getTaskSetByType(role);
    if (taskSet != null)
      SpringUtils.getMapService().getNpc(role.getTask().getTaskNpcId(taskSet.getSubmitNpc())); 
    doRoleExpire(role, fight);
  }
  
  public void doRoleExpire(Role role, Fight fight) {
    TeamService teamService = SpringUtils.getTeamService();
    if (teamService.getTeamCount(role) <= 1);
  }
  
  public boolean isMemberSubmitTask(Role role, TaskSet taskSet) {
    return true;
  }
  
  public void doFightFail(Role role, TaskSet taskSet, FightEndEvent fightEndEvent) {}
  
  public void doFightRunAway(Role role, TaskSet taskSet, FightEndEvent fightEndEvent) {}
  
  public void doFightMaxRound(Role role, TaskSet taskSet, FightEndEvent fightEndEvent) {}
  
  public abstract TaskInfo getTaskInfo(Role paramRole, TaskSet paramTaskSet);
  
  protected abstract int getFightType(Role paramRole);
  
  protected abstract void addActivityFinishCount(Role paramRole, TaskSet paramTaskSet);
  
  protected abstract void giveReward(Role paramRole, NutMap paramNutMap, TaskSet paramTaskSet, int paramInt);
  
  protected abstract void doBeforeDelete(Role paramRole, TaskSet paramTaskSet);
  
  protected abstract void doAfterDelete(Role paramRole, TaskSet paramTaskSet);
  
  public abstract TaskType getTaskType();
  
  public abstract String getNpcContentNotFight(Role paramRole);
  
  public abstract void doStartFight(Role paramRole, TaskSet paramTaskSet);
  
  public abstract String canAcceptTask(Role paramRole, TaskSet paramTaskSet, boolean paramBoolean);
  
  public abstract boolean isCreateNpcForFight(TaskSet paramTaskSet);
}
