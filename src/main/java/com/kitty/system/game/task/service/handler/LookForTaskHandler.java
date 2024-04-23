package com.kitty.system.game.task.service.handler;

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
import com.kitty.system.game.activity.model.Dugeon;
import com.kitty.system.game.activity.model.product.ActivityLivenessSet;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.map.pool.MapDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.task.message.vo.TaskInfo;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysDataTaskLookFor;
import com.kitty.system.init.domain.vo.Param;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class LookForTaskHandler extends TaskHandler {
  private static final int FIRST_TASK_ID = 1600001;
  
  private static final int MIN_TEAM_COUNT = 1;
  
  private static final int MIN_LEVEL = 40;
  
  private static final String FDUGEON_NAME = "寻芳";
  
  private static final int ENTER_MAP = 5001;
  
  private static final int ENTER_X = 52;
  
  private static final int ENTER_Y = 11;
  
  private static final int EXIT_MAP = 1011;
  
  private static final int EXIT_X = 120;
  
  private static final int EXIT_Y = 102;
  
  private static final int LOOK_FOR_NICE = 10;
  
  public TaskType getTaskType() {
    return TaskType.LOOK_FOR;
  }
  
  public boolean resetTask(Role role, TaskSet taskSet) {
    return true;
  }
  
  @PostConstruct
  public void init() {
    for (Iterator<Integer> iterator = Const.DUGEON_LOOK_FOR_MAP.iterator(); iterator.hasNext(); ) {
      int mapId = ((Integer)iterator.next()).intValue();
      MapDataPool.MAP_KEY_GENERATE.put(Integer.valueOf(mapId), role -> (Long)DataCache.ROLE_DUNGEON.getOrDefault(role.getId(), Long.valueOf(0L)));
    } 
  }
  
  public void createLookForDugeon(Role role, Npc npc) {
    String talk = checkCreateDugeon(role);
    if (talk != null) {
      this.npcService.sendNpcContent(role, npc, "", talk);
      return;
    } 
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    Role leaderRole = SpringUtils.getRoleService().getOnlinePlayer(team.getLeaderUid());
    createDungeon(team);
    TaskSet taskSet = this.taskService.getTaskSet(1600001);
    this.teamService.memberHandleThreadLocal(team, memberRole -> acceptTask(memberRole, taskSet, -1));
    doEnterDugeon(leaderRole);
  }
  
  private String checkCreateDugeon(Role role) {
    if (!this.teamService.isTeamLeader(role))
      return String.format("要风流得先保证安全呐，请至少组满%d位大侠再来挑战吧!#<E:99>#", new Object[] { Integer.valueOf(1) }); 
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    String names = this.teamService.getNotInTeam(team);
    if (names != null)
      return "队伍中#<Y>%s#处于暂离状态，不能创建副本"; 
    for (int i = 0; i < team.getList().size(); i++) {
      Member member = team.getList().get(i);
      if (member != null)
        if (member.isInTeam()) {
          Role memberRole = SpringUtils.getRoleService().getOnlinePlayer(member.getRoleId().intValue());
          if (memberRole == null);
        }  
    } 
    if (role.getLevel().intValue() < 40)
      return String.format("你等级不足#<R>%s#级，无法创建该副本。", new Object[] { Integer.valueOf(40) }); 
    if (this.teamService.getTeamCount(role) < 1)
      return String.format("#<R>%s#中妖怪众多，少于#<R>%d#人的队伍进去是非常危险的。", new Object[] { "寻芳", Integer.valueOf(1) }); 
    names = this.teamService.checkMember(role, memberRole -> (memberRole.getLevel().intValue() < 40));
    if (names != null)
      return String.format("#<Y>%s#的等级不足#<Y>%d#级，无法创建该副本。", new Object[] { names, Integer.valueOf(40) }); 
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
    if (this.activityService.isHaveRemainCount(role, ActivityType.LOOK_FOR_DUGEON))
      if (role.getTaskProgress().getFinishRecord().getInt(ActivityType.LOOK_FOR_DUGEON.getActivityId() + taskSet.getSerial(), 0) == 0) {
        role.getTaskProgress().getFinishRecord().setv(ActivityType.LOOK_FOR_DUGEON.getActivityId() + taskSet.getSerial(), Integer.valueOf(1));
        this.activityService.increaseFinishCount(role, ActivityType.LOOK_FOR_DUGEON, 1);
        int totalCount = ActivityType.LOOK_FOR_DUGEON.getTotalCount();
        int finishCount = this.activityService.getFinishCount(role, ActivityType.LOOK_FOR_DUGEON, 1);
        if (totalCount - 1 == finishCount || finishCount == 1) {
          ActivityLivenessSet activityLivenessSet = (ActivityLivenessSet)ActivityDataPool.activityLivenessSetMapByType.get(ActivityType.LOOK_FOR_DUGEON);
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
    reqTelePort.setMapId(5001);
    reqTelePort.setX(52);
    reqTelePort.setY(11);
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
  
  public void leaveDugeon(Role role) {
    SpringUtils.getMapService().changeMap(role, 1011, 120, 102, null);
  }
  
  public void handleLogin(Role role) {
    super.handleLogin(role);
    if (isInDugeon(role) && !SpringUtils.getFightNewService().isInFight(role))
      leaveDugeon(role); 
    if (isAcceptedTask(role) && getDugeon(role) == null) {
      TaskSet taskSet = getTaskSetByType(role);
      forceResetTask(role, taskSet);
    } 
  }
  
  private void deleteDugeon(Dugeon dugeon) {
    if (dugeon != null) {
      DataCache.DUNGEON_MAP.remove(Long.valueOf(dugeon.getId()));
      for (Iterator<Long> iterator = dugeon.getMemberList().iterator(); iterator.hasNext(); ) {
        long uid = ((Long)iterator.next()).longValue();
        DataCache.ROLE_DUNGEON.remove(Long.valueOf(uid));
      } 
    } 
  }
  
  private void deleteTaskAndLeave(Role role) {
    if (role == null)
      return; 
    TaskSet taskSet = getTaskSetByType(role);
    if (taskSet == null)
      return; 
    forceResetTask(role, taskSet);
    if (isInDugeon(role))
      leaveDugeon(role); 
  }
  
  public Dugeon getDugeon(Role role) {
    Long dugeonId = (Long)DataCache.ROLE_DUNGEON.getOrDefault(role.getId(), Long.valueOf(0L));
    if (dugeonId.longValue() > 0L)
      return (Dugeon)DataCache.DUNGEON_MAP.get(dugeonId); 
    return null;
  }
  
  public TaskInfo getTaskInfo(Role role, TaskSet taskSet) {
    TaskInfo taskInfo = super.getTaskInfo(role, taskSet);
    return taskInfo;
  }
  
  public boolean canGiveUpTask(Role role, TaskSet taskSet) {
    return false;
  }
  
  public void doFightFail(Role role, FightEndEvent fightEndEvent) {
    super.doFightFail(role, fightEndEvent);
  }
  
  public boolean isRequestJoinTeam(Role role, Team team) {
    Role leaderRole = SpringUtils.getRoleService().getPlayer(team.getLeaderUid());
    if (leaderRole == null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("对方所在地图无法接受申请哦。"));
      return false;
    } 
    if (!isCanTeam(role, leaderRole, Const.DUGEON_LOOK_FOR_MAP)) {
      if (isInDugeon(role)) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("本地图无法申请。"));
      } else {
        MessagePusher.pushMessage(role, (Message)new RespMsg("对方所在地图无法接受申请。"));
      } 
      return false;
    } 
    return true;
  }
  
  public boolean isInDugeon(Role role) {
    if (role == null)
      return false; 
    return Const.DUGEON_LOOK_FOR_MAP.contains(role.getPos().getMapId());
  }
  
  public boolean isInviteJoinTeam(Role role, int inviteRoleId) {
    Role invitRole = this.roleService.getOnlinePlayer(inviteRoleId);
    if (invitRole == null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("对方所在地图无法接受邀请哦。"));
      return false;
    } 
    if (!isCanTeam(role, invitRole, Const.DUGEON_LOOK_FOR_MAP)) {
      if (isInDugeon(role)) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("本地图无法邀请。"));
      } else {
        MessagePusher.pushMessage(role, (Message)new RespMsg("对方所在地图无法接受邀请。"));
      } 
      return false;
    } 
    return true;
  }
  
  public boolean isAcceptJoinTeam(Role role, int joinRoleId) {
    Role joinRole = SpringUtils.getRoleService().getOnlinePlayer(joinRoleId);
    if (!isCanTeam(role, joinRole, Const.DUGEON_LOOK_FOR_MAP)) {
      if (isInDugeon(role)) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("本地图无法操作申请队员。"));
      } else {
        MessagePusher.pushMessage(role, (Message)new RespMsg("对方所在地图无法接受操作。"));
      } 
      return false;
    } 
    return true;
  }
  
  public boolean isBackToTeam(Team team, Role memberRole) {
    Role leaderRole = SpringUtils.getRoleService().getOnlinePlayer(team.getLeaderUid());
    if (!isCanTeam(leaderRole, memberRole, Const.DUGEON_LOOK_FOR_MAP)) {
      if (isInDugeon(leaderRole)) {
        MessagePusher.pushMessage(memberRole, (Message)new RespMsg("队长所在地图无法直接进入。"));
      } else {
        MessagePusher.pushMessage(memberRole, (Message)new RespMsg("本地图无法直接进入。"));
      } 
      return false;
    } 
    return true;
  }
  
  public void giveReward(Role role, TaskSet taskSet) {
    int record = role.getTaskProgress().getFinishRecord().getInt(ActivityType.LOOK_FOR_DUGEON.getActivityId() + taskSet.getSerial(), 0);
    if (record == 0) {
      if (this.teamService.isTeamLeader(role)) {
        long num = this.activityService.addNice(role, 5L);
        if (num > 0L)
          MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "获得好心值#<IR" + Const.RESTYPE_NICE + ">#" + MoneyUtil.colours(Long.valueOf(num)) + "，队长带队辛苦了#<E:23>#", true)); 
        this.activityService.teamLeaderNice(role, taskSet, ActivityType.LOOK_FOR_DUGEON.getActivityId(), 10L);
      } 
      List<Param> list = (List<Param>)JsonUtils.string2Collection(taskSet.getTaskReward(), List.class, Param.class);
      if (list != null)
        ((BagService)SpringUtils.getBean(BagService.class)).sendReward(role, list, "寻芳副本", 0); 
    } else {
      MessagePusher.pushMessage(role, (Message)new RespMsg("你今日已获得本层奖励,不能再次获得"));
      if (this.teamService.isTeamLeader(role))
        this.activityService.teamLeaderNice(role, taskSet, ActivityType.LOOK_FOR_DUGEON.getActivityId(), 10L); 
    } 
    MessagePusher.pushMessage(role, (Message)new RespMsg("挑战成功!#<E:145>#可以进入下一层继续挑战！#<E:86>#"));
  }
  
  @EventHandler({EventType.LEAVE_MAP})
  public void handleLeaveMapEvent(LeaveMapEvent leaveMapEvent) {
    Role role = leaveMapEvent.getRole();
    int mapId = role.getPos().getMapId().intValue();
    int oldMapId = leaveMapEvent.getMapId();
    if (Const.DUGEON_LOOK_FOR_MAP.contains(Integer.valueOf(oldMapId)) && !Const.DUGEON_LOOK_FOR_MAP.contains(Integer.valueOf(mapId))) {
      List<SysDataTaskLookFor> list = TaskDataPool.taskLookForList;
      clear(role, list);
      role.save();
    } 
  }
  
  @EventHandler({EventType.LOGIN})
  public void handleLoginEvent(LoginEvent loginEvent) {
    Role role = loginEvent.getRole();
    boolean flag = false;
    List<SysDataTaskLookFor> list = TaskDataPool.taskLookForList;
    if (Const.DUGEON_LOOK_FOR_MAP.contains(role.getPos().getMapId())) {
      for (SysDataTaskLookFor obj : list) {
        if (role.getTask().isAccepted(obj.getSerial()) && isDugeonMember(role)) {
          flag = true;
          break;
        } 
      } 
      if (!flag) {
        clear(role, list);
        leaveDugeon(role);
        role.save();
      } 
    } 
  }
  
  public void clear(Role role, List<SysDataTaskLookFor> list) {
    for (SysDataTaskLookFor obj : list) {
      TaskSet taskSet = this.taskService.getTaskSet(obj.getSerial());
      if (this.taskService.isTask(role, taskSet)) {
        delTask(role, taskSet);
        deleteDugeon(getDugeon(role));
      } 
    } 
  }
}
