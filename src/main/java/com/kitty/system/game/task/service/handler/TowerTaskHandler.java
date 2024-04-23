package com.kitty.system.game.task.service.handler;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.convert.MoneyUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.listener.event.LeaveMapEvent;
import com.kitty.listener.event.LoginEvent;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.task.TaskType;
import com.kitty.system.game.activity.ActivityDataPool;
import com.kitty.system.game.activity.model.product.ActivityLivenessSet;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.map.pool.MapDataPool;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.task.message.vo.TaskInfo;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysDataTaskTower;
import com.kitty.system.init.domain.vo.Param;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class TowerTaskHandler extends TaskHandler {
  private static final int FIRST_TASK_ID = 800002;
  
  private static final int MIN_TEAM_COUNT = 3;
  
  private static final int MIN_LEVEL = 50;
  
  private static final String FDUGEON_NAME = "大雁塔";
  
  private static final int ENTER_MAP = 1111;
  
  private static final int ENTER_X = 24;
  
  private static final int ENTER_Y = 18;
  
  private static final int EXIT_MAP = 1011;
  
  private static final int EXIT_X = 120;
  
  private static final int EXIT_Y = 102;
  
  private static final int TOWER_NICE = 20;
  
  public TaskType getTaskType() {
    return TaskType.TOWER;
  }
  
  public boolean resetTask(Role role, TaskSet taskSet) {
    return true;
  }
  
  @PostConstruct
  public void init() {
    for (Iterator<Integer> iterator = Const.DUGEON_DYT_MAP.iterator(); iterator.hasNext(); ) {
      int mapId = ((Integer)iterator.next()).intValue();
      MapDataPool.MAP_KEY_GENERATE.put(Integer.valueOf(mapId), role -> (Long)DataCache.ROLE_DUNGEON.getOrDefault(role.getId(), Long.valueOf(0L)));
    } 
  }
  
  public void createTowerDugeon(Role role, Npc npc) {
    String talk = checkCreateDugeon(role);
    if (talk != null) {
      this.npcService.sendNpcContent(role, npc, "", talk);
      return;
    } 
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    Role leaderRole = SpringUtils.getRoleService().getOnlinePlayer(team.getLeaderUid());
    createDungeon(team);
    TaskSet taskSet = this.taskService.getTaskSet(800002);
    this.teamService.memberHandleThreadLocal(team, memberRole -> acceptTask(memberRole, taskSet, -1));
    doEnterDugeon(leaderRole);
  }
  
  private String checkCreateDugeon(Role role) {
    if (!this.teamService.isTeamLeader(role))
      return "你不是队长，不能创建副本。#<R>(至少需要1人组队才能进入副本)#"; 
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    String names = this.teamService.getNotInTeam(team);
    if (names != null)
      return String.format("队伍中#<Y>%s#处于暂离状态，不能创建副本", new Object[] { names }); 
    for (int i = 0; i < team.getList().size(); i++) {
      Member member = team.getList().get(i);
      if (member != null)
        if (member.isInTeam()) {
          Role memberRole = this.roleService.getPlayer(member.getRoleId().intValue());
          if (memberRole == null);
        }  
    } 
    if (role.getLevel().intValue() < 50)
      return String.format("你等级不足#<R>%s#级，无法创建该副本。", new Object[] { Integer.valueOf(50) }); 
    if (this.teamService.getTeamCount(role) < 1)
      return String.format("#<R>%s#中妖怪众多，少于#<R>%d#人的队伍进去是非常危险的。", new Object[] { "大雁塔", Integer.valueOf(1) }); 
    names = this.teamService.checkMember(role, memberRole -> (memberRole.getLevel().intValue() < 50));
    if (names != null)
      return String.format("#<Y>%s#的等级不足#<Y>%d#级，无法创建该副本。", new Object[] { names, Integer.valueOf(50) }); 
    return null;
  }
  
  private boolean isDugeonMember(Role role) {
    if (role == null)
      return false; 
    return DataCache.ROLE_DUNGEON.containsKey(role.getId());
  }
  
  public void doAfterSubmit(Role role, TaskSet taskSet) {
    super.doAfterSubmit(role, taskSet);
    this.taskService.updateTargetProgress(role, taskSet);
    addActivityCount(role, taskSet);
  }
  
  private void addActivityCount(Role role, TaskSet taskSet) {
    if (this.activityService.isHaveRemainCount(role, ActivityType.TOWER_DUGEON))
      if (role.getTaskProgress().getFinishRecord().getInt(ActivityType.TOWER_DUGEON.getActivityId() + taskSet.getSerial(), 0) == 0) {
        role.getTaskProgress().getFinishRecord().setv(ActivityType.TOWER_DUGEON.getActivityId() + taskSet.getSerial(), Integer.valueOf(1));
        this.activityService.increaseFinishCount(role, ActivityType.TOWER_DUGEON, 1);
        int totalCount = ActivityType.TOWER_DUGEON.getTotalCount();
        int finishCount = this.activityService.getFinishCount(role, ActivityType.TOWER_DUGEON, 1);
        if (totalCount - 1 == finishCount || finishCount == 1) {
          ActivityLivenessSet activityLivenessSet = (ActivityLivenessSet)ActivityDataPool.activityLivenessSetMapByType.get(ActivityType.TOWER_DUGEON);
          if (activityLivenessSet != null)
            activationTips(role, (int)activityLivenessSet.getPerLiveness()); 
        } 
      }  
  }
  
  private boolean isAcceptedTask(Role role) {
    return (getTaskSetByType(role) != null);
  }
  
  private void doEnterDugeon(Role role) {
    ReqTelePort reqTelePort = new ReqTelePort();
    reqTelePort.setMapId(1111);
    reqTelePort.setX(24);
    reqTelePort.setY(18);
    this.mapService.changeMap(role, reqTelePort, null);
  }
  
  public void startFight(Role role, Npc npc) {
    String content = getNpcContentNotFight(role, npc);
    if (content != null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg(content));
      return;
    } 
    TaskSet taskSet = getTaskSetByType(role);
    this.taskService.taskTriggerFight(role, taskSet, -1);
  }
  
  private String getNpcContentNotFight(Role role, Npc npc) {
    if (SpringUtils.getFightNewService().isInFight(role))
      return "请老老实实打完你的战斗"; 
    if (this.teamService.getTeamCount(role) < 1)
      return String.format("队伍人数不足%d人，不能挑战", new Object[] { Integer.valueOf(1) }); 
    return null;
  }
  
  public void handleLogin(Role role) {
    super.handleLogin(role);
    if (isInDungeon(role, Const.DUGEON_DYT_MAP) && !SpringUtils.getFightNewService().isInFight(role))
      leaveDungeon(role, 1011, 120, 102, null); 
    if (isAcceptedTask(role) && getDungeon(role) == null) {
      TaskSet taskSet = getTaskSetByType(role);
      forceResetTask(role, taskSet);
    } 
  }
  
  private void deleteTaskAndLeave(Role role) {
    if (role == null)
      return; 
    TaskSet taskSet = getTaskSetByType(role);
    if (taskSet == null)
      return; 
    forceResetTask(role, taskSet);
    if (isInDungeon(role, Const.DUGEON_DYT_MAP))
      leaveDungeon(role, 1011, 120, 102, null); 
  }
  
  public TaskInfo getTaskInfo(Role role, TaskSet taskSet) {
    TaskInfo taskInfo = super.getTaskInfo(role, taskSet);
    return taskInfo;
  }
  
  public void doFightFail(Role role, FightEndEvent fightEndEvent) {
    super.doFightFail(role, fightEndEvent);
  }
  
  public boolean isRequestJoinTeam(Role role, Team team) {
    Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
    if (leaderRole == null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("对方所在地图无法接受申请哦。"));
      return false;
    } 
    if (!isCanTeam(role, leaderRole, Const.DUGEON_DYT_MAP)) {
      if (isInDungeon(role, Const.DUGEON_DYT_MAP)) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("本地图无法申请。"));
      } else {
        MessagePusher.pushMessage(role, (Message)new RespMsg("对方所在地图无法接受申请。"));
      } 
      return false;
    } 
    return true;
  }
  
  public boolean isInviteJoinTeam(Role role, int inviteRoleId) {
    Role invitRole = this.roleService.getPlayer(inviteRoleId);
    if (invitRole == null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("对方所在地图无法接受邀请哦。"));
      return false;
    } 
    if (!isCanTeam(role, invitRole, Const.DUGEON_DYT_MAP)) {
      if (isInDungeon(role, Const.DUGEON_DYT_MAP)) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("本地图无法邀请。"));
      } else {
        MessagePusher.pushMessage(role, (Message)new RespMsg("对方所在地图无法接受邀请。"));
      } 
      return false;
    } 
    return true;
  }
  
  public boolean isAcceptJoinTeam(Role role, int joinRoleId) {
    Role joinRole = this.roleService.getPlayer(joinRoleId);
    if (!isCanTeam(role, joinRole, Const.DUGEON_DYT_MAP)) {
      if (isInDungeon(role, Const.DUGEON_DYT_MAP)) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("本地图无法操作申请队员。"));
      } else {
        MessagePusher.pushMessage(role, (Message)new RespMsg("对方所在地图无法接受操作。"));
      } 
      return false;
    } 
    return true;
  }
  
  public boolean isBackToTeam(Team team, Role memberRole) {
    Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
    if (!isCanTeam(leaderRole, memberRole, Const.DUGEON_DYT_MAP)) {
      if (isInDungeon(leaderRole, Const.DUGEON_DYT_MAP)) {
        MessagePusher.pushMessage(memberRole, (Message)new RespMsg("队长所在地图无法直接进入。"));
      } else {
        MessagePusher.pushMessage(memberRole, (Message)new RespMsg("本地图无法直接进入。"));
      } 
      return false;
    } 
    return true;
  }
  
  public void giveReward(Role role, TaskSet taskSet) {
    int record = role.getTaskProgress().getFinishRecord().getInt(ActivityType.TOWER_DUGEON.getActivityId() + taskSet.getSerial(), 0);
    if (record == 0) {
      this.roleService.addCurrency(role, Const.RESTYPE_BRAC, Long.valueOf(calculate(role.getLevel().intValue(), role.getRelive().intValue(), 3)), "大雁塔", true, false);
      if (this.teamService.isTeamLeader(role)) {
        long num = this.activityService.addNice(role, 5L);
        if (num > 0L)
          MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "获得好心值#<IR" + Const.RESTYPE_NICE + ">#" + MoneyUtil.colours(Long.valueOf(num)) + "，队长带队辛苦了#<E:23>#", true)); 
        this.activityService.teamLeaderNice(role, taskSet, ActivityType.TOWER_DUGEON.getActivityId(), 20L);
      } 
      this.roleService.addExp(role, calculate(role.getLevel().intValue(), role.getRelive().intValue(), 1), "大雁塔", true);
      Integer combatPet = role.getExtendBox().getCombatPet();
      if (combatPet.intValue() > 0) {
        Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
        if (pet != null)
          this.petService.addExp(role, pet, calculate(pet.getPetLv().intValue(), pet.getRebirth().intValue(), 2), true); 
      } 
      List<Param> list = (List<Param>)JsonUtils.string2Collection(taskSet.getTaskReward(), List.class, Param.class);
      if (list != null)
        ((BagService)SpringUtils.getBean(BagService.class)).sendReward(role, list, "大雁塔", 0); 
    } else {
      MessagePusher.pushMessage(role, (Message)new RespMsg("你今日已获得本层大雁塔奖励,不能再次获得"));
      if (this.teamService.isTeamLeader(role))
        this.activityService.teamLeaderNice(role, taskSet, ActivityType.TOWER_DUGEON.getActivityId(), 20L); 
    } 
    MessagePusher.pushMessage(role, (Message)new RespMsg("挑战成功!#<E:145>#可以进入下一层继续挑战！#<E:86>#"));
  }
  
  private static long calculate(int grade, int rei, int type) {
    double fold = ActivityType.TOWER_DUGEON.getFold();
    double exp = 0.0D;
    if (type == 1) {
      exp = 1.6E7D * (1.0D + (Math.min(grade, 180.0D) + Math.min(rei, 3.0D) * 15.0D) / 140.0D * 2.5D) / 30.0D * 0.6D * 1.3D;
    } else if (type == 2) {
      exp = 1.6E7D * (1.0D + (Math.min(grade, 180.0D) + Math.min(rei, 3.0D) * 15.0D) / 140.0D * 2.5D) / 30.0D * 0.6D * 1.3D * 1.2D;
    } else if (type == 3) {
      if (grade - 180 >= 50)
        return 37L; 
      if (grade - 180 >= 20)
        return 75L; 
      return 150L;
    } 
    return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf((fold > 1.0D) ? (exp * fold) : exp)).longValue();
  }
  
  @EventHandler({EventType.LEAVE_MAP})
  public void handleLeaveMapEvent(LeaveMapEvent leaveMapEvent) {
    Role role = leaveMapEvent.getRole();
    int mapId = role.getPos().getMapId().intValue();
    int oldMapId = leaveMapEvent.getMapId();
    if (Const.DUGEON_DYT_MAP.contains(Integer.valueOf(oldMapId)) && !Const.DUGEON_DYT_MAP.contains(Integer.valueOf(mapId))) {
      List<SysDataTaskTower> taskTowerList = TaskDataPool.taskTowerList;
      clear(role, taskTowerList);
      role.save();
    } 
  }
  
  @EventHandler({EventType.LOGIN})
  public void handleLoginEvent(LoginEvent loginEvent) {
    Role role = loginEvent.getRole();
    boolean flag = false;
    List<SysDataTaskTower> taskTowerList = TaskDataPool.taskTowerList;
    if (Const.DUGEON_DYT_MAP.contains(role.getPos().getMapId())) {
      for (SysDataTaskTower taskTower : taskTowerList) {
        if (role.getTask().isAccepted(taskTower.getSerial()) && isDugeonMember(role)) {
          flag = true;
          break;
        } 
      } 
      if (!flag) {
        clear(role, taskTowerList);
        leaveDungeon(role, 1011, 120, 102, null);
        role.save();
      } 
    } 
  }
  
  public void clear(Role role, List<SysDataTaskTower> taskTowerList) {
    for (SysDataTaskTower taskTower : taskTowerList) {
      TaskSet taskSet = this.taskService.getTaskSet(taskTower.getSerial());
      if (this.taskService.isTask(role, taskSet)) {
        delTask(role, taskSet);
        deleteDungeon(getDungeon(role));
      } 
    } 
  }
  
  public boolean canGiveUpTask(Role role, TaskSet taskSet) {
    return false;
  }
}
