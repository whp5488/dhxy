package com.kitty.system.game.task.service.handler;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.RandomUtil;
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
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.task.domain.DoingTask;
import com.kitty.system.game.task.message.vo.TaskInfo;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.init.domain.vo.surround.SysDataTaskCrusadeRenegade;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CrusadeRenegadeTaskHandler extends TaskHandler {
  private static final String TASK_DIST = "TFPZ";
  
  private static final int MIN_TEAM_COUNT = 3;
  
  private static final int NEED_RELIVE = 2;
  
  private static final int NEED_LEVEL = 120;
  
  private static final String NEED_LEVEL_NOT = "%s尚浅，请提升至%s之后再来报效天庭吧#<E:17>#";
  
  private static final String TEAM_NOT = "讨伐叛贼之行无比险恶，请务必组齐至少%d位大侠应战#<E:51>#";
  
  private static final String TEAM_FIGHT_NOT = "想与我一战,至少要有%d位像你一样强壮的家伙，快去组几个人来吧!#<E:18>#";
  
  public TaskType getTaskType() {
    return TaskType.CRUSADE_RENEGADE;
  }
  
  public boolean resetTask(Role role, TaskSet taskSet) {
    return true;
  }
  
  public void acceptTask(Role role, Npc npc) {
    randomAcceptTask(role);
  }
  
  public void randomAcceptTask(Role role) {
    if (!canAcceptTask(role))
      return; 
    List<SysDataTaskCrusadeRenegade> list = TaskDataPool.crusadeRenegadeList;
    int random = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(list.size() - 1));
    Integer taskId = Integer.valueOf(((SysDataTaskCrusadeRenegade)list.get(random)).getSerial());
    TaskSet taskSet = this.taskService.getTaskSet(taskId.intValue());
    if (taskSet == null)
      return; 
    acceptTask(role, taskSet, -1);
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    this.teamService.memberHandleThreadLocal(team, memberRole -> syncTask(memberRole));
  }
  
  private boolean canAcceptTask(Role role) {
    if (!this.teamService.isInTeam(role) || this.teamService.getTeamCount(role) < 3) {
      MessagePusher.pushMessage(role, (Message)new RespMsg(String.format("讨伐叛贼之行无比险恶，请务必组齐至少%d位大侠应战#<E:51>#", new Object[] { Integer.valueOf(3) })));
      return false;
    } 
    if (!canTeamGrade(role, 2, 120, "%s尚浅，请提升至%s之后再来报效天庭吧#<E:17>#"))
      return false; 
    if (isAcceptedSchoolTask(role))
      return false; 
    if (isWaitGiveUpCD(role))
      return false; 
    return true;
  }
  
  public boolean isAcceptedSchoolTask(Role role) {
    List<TaskSet> taskList = this.taskService.getAcceptedTaskListMatchTaskType(role, TaskType.CRUSADE_RENEGADE);
    if (taskList == null || taskList.isEmpty())
      return false; 
    return true;
  }
  
  private boolean isWaitGiveUpCD(Role role) {
    long giveUpTime = role.getTask().getGiveUpTime(TaskType.CRUSADE_RENEGADE);
    if (giveUpTime > 0L && System.currentTimeMillis() - giveUpTime < 300000L)
      return true; 
    return false;
  }
  
  public void startFight(Role role, Npc npc) {
    startFight(role, getTaskType(), npc, 3, 2, 120, "想与我一战,至少要有%d位像你一样强壮的家伙，快去组几个人来吧!#<E:18>#", "%s尚浅，请提升至%s之后再来报效天庭吧#<E:17>#");
  }
  
  private void addActivity(Role role) {
    int count = count(role);
    count++;
    role.getTaskProgress().getFinishCounts().setv(ActivityType.CRUSADE_RENEGADE.getActivityId(), Integer.valueOf(count));
    int step = role.getTaskProgress().getFinishTallyCounts().getInt("TFPZ", 1);
    role.getTaskProgress().getFinishTallyCounts().setv("TFPZ", Integer.valueOf(step + 1));
  }
  
  private int count(Role role) {
    return role.getTaskProgress().getFinishCounts().getInt(ActivityType.CRUSADE_RENEGADE.getActivityId(), 0);
  }
  
  public TaskInfo getTaskInfo(Role role, TaskSet taskSet) {
    TaskInfo taskInfo = super.getTaskInfo(role, taskSet);
    if (this.taskService.isMatchTaskType(taskSet, TaskType.CRUSADE_RENEGADE)) {
      DoingTask doingTask = (DoingTask)role.getTask().getDoingTask().get(Integer.valueOf(taskSet.getSerial()));
      if (doingTask != null) {
        int step = role.getTaskProgress().getFinishTallyCounts().getInt("TFPZ", 1);
        if (step > 10) {
          step = 1;
          role.getTaskProgress().getFinishTallyCounts().setv("TFPZ", Integer.valueOf(step));
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
    int cnt = role.getTaskProgress().getFinishTallyCounts().getInt("TFPZ", 1);
    int teamCount = this.teamService.getTeamCount(role);
    long heroExp = calculate(role.getLevel().intValue(), role.getRelive().intValue(), cnt, count, teamCount, ItemClassify.HERO_EXP);
    this.roleService.addExp(role, heroExp, "", true);
    Integer combatPet = role.getExtendBox().getCombatPet();
    if (combatPet.intValue() > 0) {
      Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
      long calculate = calculate(pet.getPetLv().intValue(), pet.getRebirth().intValue(), cnt, count, teamCount, ItemClassify.PET_EXP);
      this.petService.addExp(role, pet, calculate, true);
    } 
    addActivity(role);
    if (this.teamService.isTeamLeader(role) && count < ActivityType.CRUSADE_RENEGADE.getTotalCount())
      randomAcceptTask(role); 
  }
  
  private static long calculate(int grade, int rei, int cnt, int todayCnt, int factor, ItemClassify type) {
    double fold = ActivityType.CRUSADE_RENEGADE.getFold();
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
    List<SysDataTaskCrusadeRenegade> list = TaskDataPool.crusadeRenegadeList;
    for (SysDataTaskCrusadeRenegade obj : list) {
      if (role.getTask().isAccepted(obj.getSerial()))
        delTask(role, this.taskService.getTaskSet(obj.getSerial())); 
    } 
    role.getTaskProgress().getFinishTallyCounts().setv("TFPZ", Integer.valueOf(1));
    role.save();
  }
  
  public void syncTask(Role role) {
    if (DataGlobalFunc.needGradeJudge(2, 120, role.getRelive().intValue(), role.getLevel().intValue(), "你的#<R>角色等级#不足") != null)
      return; 
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (team != null) {
      Role leader = this.roleService.getPlayer(team.getLeaderRoleId());
      if (leader != null && !leader.getRoleId().equals(role.getRoleId())) {
        List<SysDataTaskCrusadeRenegade> list = TaskDataPool.crusadeRenegadeList;
        for (SysDataTaskCrusadeRenegade obj : list) {
          DoingTask doingTask = (DoingTask)leader.getTask().getDoingTask().get(Integer.valueOf(obj.getSerial()));
          if (doingTask != null) {
            deleteTask(role);
            int anInt = leader.getTaskProgress().getFinishTallyCounts().getInt("TFPZ", 1);
            role.getTaskProgress().getFinishTallyCounts().setv("TFPZ", Integer.valueOf(anInt));
            syncTeamTask(role, doingTask);
            break;
          } 
        } 
      } 
    } 
  }
  
  public static int getMonster(int level, int serial) {
    if (level >= 1080 && level <= 1100) {
      serial += 20;
    } else if (level >= 1101 && level <= 1120) {
      serial += 40;
    } else if (level >= 2100 && level <= 2110) {
      serial += 40;
    } else if (level >= 2111 && level <= 2120) {
      serial += 80;
    } else if (level >= 2121 && level <= 2130) {
      serial += 100;
    } else if (level >= 2131 && level <= 2140) {
      serial += 120;
    } else if (level >= 3120 && level <= 3130) {
      serial += 140;
    } else if (level >= 3131 && level <= 3140) {
      serial += 160;
    } else if (level >= 3141 && level <= 3150) {
      serial += 180;
    } else if (level >= 3151 && level <= 3160) {
      serial += 200;
    } else if (level >= 3161 && level <= 3180) {
      serial += 220;
    } 
    return serial;
  }
}
