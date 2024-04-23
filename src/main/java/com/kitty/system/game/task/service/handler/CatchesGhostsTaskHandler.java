package com.kitty.system.game.task.service.handler;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.listener.event.LoginEvent;
import com.kitty.listener.event.TeamJoinEvent;
import com.kitty.listener.event.TeamLeaveEvent;
import com.kitty.listener.event.TeamLeaveMomentEvent;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.enums.task.TaskType;
import com.kitty.system.game.activity.ActivityDataPool;
import com.kitty.system.game.activity.model.product.ActivityLivenessSet;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.task.domain.DoingTask;
import com.kitty.system.game.task.message.vo.TaskInfo;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysDataTaskCatchesGhosts;
import com.kitty.system.init.domain.vo.Param;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CatchesGhostsTaskHandler extends TaskHandler {
  ActivityLivenessSet activityLivenessSet = (ActivityLivenessSet)ActivityDataPool.activityLivenessSetMapByType.get(ActivityType.CATCHES_GHOSTS_TASK);
  
  public TaskType getTaskType() {
    return TaskType.CATCHES_GHOSTS;
  }
  
  public boolean resetTask(Role role, TaskSet taskSet) {
    return true;
  }
  
  public void acceptCatchesGhostsTask(Role role, Npc npc) {
    randomAcceptTask(role);
  }
  
  public void randomAcceptTask(Role role) {
    if (canAcceptTask(role)) {
      List<SysDataTaskCatchesGhosts> list = TaskDataPool.taskCatchesGhosts;
      int random = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(list.size() - 1));
      Integer taskId = Integer.valueOf(((SysDataTaskCatchesGhosts)list.get(random)).getSerial());
      TaskSet taskSet = this.taskService.getTaskSet(taskId.intValue());
      if (taskSet != null) {
        acceptTask(role, taskSet, -1);
        Team team = this.teamService.getTeam(role.getRoleId().intValue());
        this.teamService.memberHandleThreadLocal(team, memberRole -> syncTask(memberRole));
      } 
    } 
  }
  
  private boolean canAcceptTask(Role role) {
    if (this.teamService.isInTeam(role) && this.teamService.getTeamCount(role) >= 1) {
      if (!canTeamGrade(role, 0, 22, "%s尚浅，请提升至%s之后再来报效天庭吧#<E:17>#"))
        return false; 
      if (isAcceptedSchoolTask(role))
        return false; 
      return !isWaitGiveUpCD(role);
    } 
    MessagePusher.pushMessage(role, (Message)new RespMsg(String.format("抓鬼之行无比险恶，请务必组齐至少%d位大侠应战#<E:51>#", new Object[] { Integer.valueOf(1) })));
    return false;
  }
  
  public boolean isAcceptedSchoolTask(Role role) {
    List<TaskSet> taskList = this.taskService.getAcceptedTaskListMatchTaskType(role, TaskType.CATCHES_GHOSTS);
    return (taskList != null && !taskList.isEmpty());
  }
  
  private boolean isWaitGiveUpCD(Role role) {
    long giveUpTime = role.getTask().getGiveUpTime(TaskType.CATCHES_GHOSTS);
    return (giveUpTime > 0L && System.currentTimeMillis() - giveUpTime < 300000L);
  }
  
  public void startFight(Role role, Npc npc) {
    startFight(role, getTaskType(), npc, 1, 0, 22, "想与我一战,至少要有%d位像你一样强壮的家伙，快去组几个人来吧!#<E:18>#", "%s尚浅，请提升至%s之后再来报效天庭吧#<E:17>#");
  }
  
  private void addActivity(Role role) {
    int sum = sum();
    int count = count(role);
    if (count < sum && this.activityLivenessSet != null)
      activationTips(role, (int)this.activityLivenessSet.getPerLiveness()); 
    count++;
    role.getTaskProgress().getFinishCounts().setv(ActivityType.CATCHES_GHOSTS_TASK.getActivityId(), Integer.valueOf(count));
    int step = role.getTaskProgress().getFinishTallyCounts().getInt("ZG", 1);
    role.getTaskProgress().getFinishTallyCounts().setv("ZG", Integer.valueOf(step + 1));
  }
  
  private int sum() {
    return (int)(this.activityLivenessSet.getMaxLiveness() / this.activityLivenessSet.getPerLiveness());
  }
  
  private int count(Role role) {
    return role.getTaskProgress().getFinishCounts().getInt(ActivityType.CATCHES_GHOSTS_TASK.getActivityId(), 0);
  }
  
  public TaskInfo getTaskInfo(Role role, TaskSet taskSet) {
    TaskInfo taskInfo = super.getTaskInfo(role, taskSet);
    if (this.taskService.isMatchTaskType(taskSet, TaskType.CATCHES_GHOSTS)) {
      DoingTask doingTask = (DoingTask)role.getTask().getDoingTask().get(Integer.valueOf(taskSet.getSerial()));
      if (doingTask != null) {
        int step = role.getTaskProgress().getFinishTallyCounts().getInt("ZG", 1);
        if (step > 10) {
          step = 1;
          role.getTaskProgress().getFinishTallyCounts().setv("ZG", Integer.valueOf(step));
        } 
        taskInfo.setCounter((short)step);
      } 
    } 
    return taskInfo;
  }
  
  public boolean canGiveUpTask(Role role, TaskSet taskSet) {
    return true;
  }
  
  public void doFightFail(Role role, FightEndEvent fightEndEvent) {
    super.doFightFail(role, fightEndEvent);
  }
  
  public void giveReward(Role role, TaskSet taskSet) {
    int count = count(role);
    int sum = sum();
    int cnt = role.getTaskProgress().getFinishTallyCounts().getInt("ZG", 1);
    int teamCount = this.teamService.getTeamCount(role);
    int current = role.getRelive().intValue() * 1000 + role.getLevel().intValue();
    int limit = 1080;
    if (current < limit) {
      if (count <= sum) {
        List<Param> list = (List<Param>)JsonUtils.string2Collection(taskSet.getTaskReward(), List.class, Param.class);
        if (list != null)
          ((BagService)SpringUtils.getBean(BagService.class)).sendReward(role, list, "钟馗抓鬼", 0); 
      } 
      long heroExp = calculate(role.getLevel().intValue(), role.getRelive().intValue(), cnt, count, teamCount, ItemClassify.HERO_EXP);
      this.roleService.addExp(role, heroExp, "", true);
      Integer combatPet = role.getExtendBox().getCombatPet();
      if (combatPet.intValue() > 0) {
        boolean flag = true;
        Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
        long calculate = calculate(pet.getPetLv().intValue(), pet.getRebirth().intValue(), cnt, count, teamCount, ItemClassify.PET_EXP);
        if (count > sum && calculate > 0L) {
          flag = false;
          MessagePusher.pushMessage(role, (Message)new RespMsg(String.format(PET_EXP_TIPS, new Object[] { pet.getPetName(), Long.valueOf(calculate) })));
        } 
        this.petService.addExp(role, pet, calculate, flag);
      } 
      addActivity(role);
    } else {
      MessagePusher.pushMessage(role, (Message)new RespMsg("大侠江湖历练过高,无法从小鬼身上获取经验。"));
    } 
    if (this.teamService.isTeamLeader(role) && count < 60)
      randomAcceptTask(role); 
  }
  
  private static long calculate(int grade, int rei, int cnt, int todayCnt, int factor, ItemClassify type) {
    double fold = ActivityType.CATCHES_GHOSTS_TASK.getFold();
    double exp = 0.0D;
    if (ItemClassify.HERO_EXP == type)
      exp = factor * 0.0866D * 1.6E7D * (1.0D + (grade + rei * 15.0D) / 140.0D * 2.5D) / 30.0D * 1.2D * (6.0E-4D * cnt + 0.83D) * (1 - (10000 + todayCnt) / 10061); 
    if (ItemClassify.PET_EXP == type)
      exp = 2.4D * factor * 0.0866D * 1.6E7D * (1.0D + (grade + rei * 15) / 140.0D * 2.5D) / 30.0D * 1.2D * (6.0E-4D * cnt + 0.83D) * (1 - (10000 + todayCnt) / 10061); 
    return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf((fold > 1.0D) ? (exp * fold) : exp)).longValue();
  }
  
  @EventHandler({EventType.LOGIN})
  public void handleLoginEvent(LoginEvent loginEvent) {
    Role role = loginEvent.getRole();
    if (!this.teamService.isInTeam(role))
      deleteTask(role); 
  }
  
  @EventHandler({EventType.TEAM_LEAVE})
  public void handleTeamLeaveEvent(TeamLeaveEvent teamLeaveEvent) {
    Role role = teamLeaveEvent.getRole();
    deleteTask(role);
  }
  
  @EventHandler({EventType.TEAM_LEAVE_MOMENT})
  public void handleTeamLeaveMonmentEvent(TeamLeaveMomentEvent teamLeaveMomentEvent) {
    Role role = teamLeaveMomentEvent.getRole();
    deleteTask(role);
  }
  
  @EventHandler({EventType.TEAM_JOIN})
  public void handleTeamJoinEvent(TeamJoinEvent joinEvent) {
    Role role = joinEvent.getRole();
    syncTask(role);
  }
  
  public void deleteTask(Role role) {
    List<SysDataTaskCatchesGhosts> list = TaskDataPool.taskCatchesGhosts;
    Iterator<SysDataTaskCatchesGhosts> var3 = list.iterator();
    while (var3.hasNext()) {
      SysDataTaskCatchesGhosts catchesGhosts = var3.next();
      if (role.getTask().isAccepted(catchesGhosts.getSerial()))
        delTask(role, this.taskService.getTaskSet(catchesGhosts.getSerial())); 
    } 
    role.getTaskProgress().getFinishTallyCounts().setv("ZG", Integer.valueOf(1));
    role.save();
  }
  
  public void syncTask(Role role) {
    if (DataGlobalFunc.needGradeJudge(0, 22, role.getRelive().intValue(), role.getLevel().intValue(), "你的#<R>角色等级#不足") == null) {
      Team team = this.teamService.getTeam(role.getRoleId().intValue());
      if (team != null) {
        Role leader = this.roleService.getPlayer(team.getLeaderRoleId());
        if (leader != null && !leader.getRoleId().equals(role.getRoleId())) {
          List<SysDataTaskCatchesGhosts> list = TaskDataPool.taskCatchesGhosts;
          Iterator<SysDataTaskCatchesGhosts> var5 = list.iterator();
          while (var5.hasNext()) {
            SysDataTaskCatchesGhosts catchesGhosts = var5.next();
            DoingTask doingTask = (DoingTask)leader.getTask().getDoingTask().get(Integer.valueOf(catchesGhosts.getSerial()));
            if (doingTask != null) {
              deleteTask(role);
              int anInt = leader.getTaskProgress().getFinishTallyCounts().getInt("ZG", 1);
              role.getTaskProgress().getFinishTallyCounts().setv("ZG", Integer.valueOf(anInt));
              syncTeamTask(role, doingTask);
              break;
            } 
          } 
        } 
      } 
    } 
  }
  
  private static final String PET_EXP_TIPS = "为善最乐！你的召唤兽#<r:7,g:234,b:93>%s#获得了召唤兽经验#<IR" + Const.RES_TYPE_EXP_PET + ">##<r:7,g:234,b:93>%d#";
  
  private static final String TEAM_FIGHT_NOT = "想与我一战,至少要有%d位像你一样强壮的家伙，快去组几个人来吧!#<E:18>#";
  
  private static final String TEAM_NOT = "抓鬼之行无比险恶，请务必组齐至少%d位大侠应战#<E:51>#";
  
  private static final String NEED_LEVEL_NOT = "%s尚浅，请提升至%s之后再来报效天庭吧#<E:17>#";
  
  private static final int LIMIT_LEVEL = 80;
  
  private static final int LIMIT_RELIVE = 1;
  
  private static final int NEED_LEVEL = 22;
  
  private static final int NEED_RELIVE = 0;
  
  private static final int MIN_TEAM_COUNT = 1;
  
  private static final String TASK_DIST = "ZG";
}
