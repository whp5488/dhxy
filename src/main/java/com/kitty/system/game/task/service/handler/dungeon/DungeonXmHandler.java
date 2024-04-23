package com.kitty.system.game.task.service.handler.dungeon;

import com.kitty.common.thread.IdGenerator;
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
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.enums.task.TaskType;
import com.kitty.system.game.activity.ActivityDataPool;
import com.kitty.system.game.activity.model.Dugeon;
import com.kitty.system.game.activity.model.product.ActivityLivenessSet;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.map.pool.MapDataPool;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.task.domain.DoingTask;
import com.kitty.system.game.task.message.RespViewTaskAward;
import com.kitty.system.game.task.message.vo.TaskInfo;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.task.service.handler.TaskHandler;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysDataTaskXm;
import com.kitty.system.init.domain.vo.Param;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DungeonXmHandler extends TaskHandler {
  private static final int FIRST_TASK_ID = 1620001;
  
  private static final int MIN_TEAM_COUNT = 1;
  
  private static final int MIN_LEVEL = 1080;
  
  private static final String FDUGEON_NAME = "心魔";
  
  private static final int ENTER_MAP = 8001;
  
  private static final int ENTER_X = 36;
  
  private static final int ENTER_Y = 23;
  
  private static final int EXIT_MAP = 1209;
  
  private static final int EXIT_X = 34;
  
  private static final int EXIT_Y = 28;
  
  private static final int XM_NICE = 10;
  
  public boolean canResetTask(Role role, TaskSet taskSet) {
    return true;
  }
  
  public TaskType getTaskType() {
    return TaskType.XM;
  }
  
  @PostConstruct
  public void init() {
    for (Iterator<Integer> iterator = Const.DUGEON_XM_MAP.iterator(); iterator.hasNext(); ) {
      int mapId = ((Integer)iterator.next()).intValue();
      MapDataPool.MAP_KEY_GENERATE.put(Integer.valueOf(mapId), role -> (Long)DataCache.ROLE_DUNGEON.getOrDefault(role.getId(), Long.valueOf(0L)));
    } 
  }
  
  public void canSubmitTask(Role role, TaskSet taskSet) {
    int submitNpc = taskSet.getSubmitNpc();
    if (submitNpc > 0) {
      Npc npc = this.mapService.getNpc(submitNpc);
      if (npc != null) {
        boolean b = this.npcService.canClickNpc(role, npc);
        if (!b) {
          MessagePusher.pushMessage(role, (Message)new RespMsg("请前往" + npc.getName() + "处提交任务"));
          return;
        } 
      } 
    } 
    int submitCondition = taskSet.getSubmitCondition();
    if (submitCondition > 0 && !this.taskService.condition(role, taskSet, (byte)1))
      return; 
    this.taskService.updateSingleTargetProgress(role, taskSet);
  }
  
  public void createXmDugeon(Role role, Npc npc) {
    String talk = checkCreateDugeon(role);
    if (talk != null) {
      this.npcService.sendNpcContent(role, npc, "", talk);
      return;
    } 
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    Role leaderRole = SpringUtils.getRoleService().getOnlinePlayer(team.getLeaderUid());
    createDungeon(team);
    TaskSet taskSet = this.taskService.getTaskSet(1620001);
    this.teamService.memberHandleThreadLocal(team, memberRole -> acceptTask(memberRole, taskSet, -1));
    doEnterDugeon(leaderRole);
  }
  
  private String checkCreateDugeon(Role role) {
    if (!this.teamService.isTeamLeader(role))
      return String.format("心魔试炼之路艰险，准备不足何以自保？请招募%d人以上再来挑战吧!#<E:99>#", new Object[] { Integer.valueOf(1) }); 
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
    if (role.getRelive().intValue() * 1000 + role.getLevel().intValue() < 1080)
      return String.format("你等级不足#<R>%s#，无法创建该副本。", new Object[] { String.format("%d转%d级", new Object[] { Integer.valueOf(1), Integer.valueOf(80) }) }); 
    if (this.teamService.getTeamCount(role) < 1)
      return String.format("#<R>%s#中妖怪众多，少于#<R>%d#人的队伍进去是非常危险的。", new Object[] { "心魔", Integer.valueOf(1) }); 
    names = this.teamService.checkMember(role, memberRole -> (memberRole.getRelive().intValue() * 1000 + memberRole.getLevel().intValue() < 1080));
    if (names != null)
      return String.format("#<Y>%s#的等级不足#<Y>%s#，无法创建该副本。", new Object[] { names, String.format("%d转%d级", new Object[] { Integer.valueOf(1), Integer.valueOf(80) }) }); 
    return null;
  }
  
  public void viewDugeonAward(Role role) {
    for (SysDataTaskXm obj : TaskDataPool.taskXmList) {
      TaskSet taskSet = this.taskService.getTaskSet(obj.getSerial());
      if (this.taskService.isTask(role, taskSet)) {
        int level = obj.getGrade() % 1000;
        RespViewTaskAward viewTaskAward = new RespViewTaskAward();
        viewTaskAward.setSerial(taskSet.getSerial());
        int brac = 0;
        List<Param> list = (List<Param>)JsonUtils.string2Collection(taskSet.getTaskReward(), List.class, Param.class);
        if (list != null)
          for (Param param : list) {
            if (Const.RESTYPE_BRAC.equals(param.getK())) {
              brac = param.getV().intValue();
              break;
            } 
          }  
        viewTaskAward.getList().add(new FiedValue(Const.RESTYPE_BRAC.toString(), (byte)3, Integer.valueOf(brac)));
        long exp = 0L;
        Integer combatPet = role.getExtendBox().getCombatPet();
        if (combatPet.intValue() > 0) {
          Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
          if (pet != null)
            exp = calculate(level, pet.getRebirth().intValue(), obj.getPower(), ItemClassify.PET_EXP); 
        } 
        viewTaskAward.getList().add(new FiedValue(ItemClassify.PET_EXP.toString(), (byte)3, Long.valueOf(exp)));
        viewTaskAward.getList().add(new FiedValue(ItemClassify.HERO_EXP.toString(), (byte)3, Long.valueOf(calculate(level, role.getRelive().intValue(), obj.getPower(), ItemClassify.HERO_EXP))));
        MessagePusher.pushMessage(role.getId().longValue(), (Message)viewTaskAward);
        break;
      } 
    } 
  }
  
  public void doAfterSubmit(Role role, TaskSet taskSet) {
    super.doAfterSubmit(role, taskSet);
    this.taskService.updateTargetProgress(role, taskSet);
    addActivityCount(role, taskSet);
  }
  
  private void addActivityCount(Role role, TaskSet taskSet) {
    if (this.activityService.isHaveRemainCount(role, ActivityType.XM_DUGEON))
      if (role.getTaskProgress().getFinishRecord().getInt(ActivityType.XM_DUGEON.getActivityId() + taskSet.getSerial(), 0) == 0) {
        role.getTaskProgress().getFinishRecord().setv(ActivityType.XM_DUGEON.getActivityId() + taskSet.getSerial(), Integer.valueOf(1));
        this.activityService.increaseFinishCount(role, ActivityType.XM_DUGEON, 1);
        int totalCount = ActivityType.XM_DUGEON.getTotalCount();
        int finishCount = this.activityService.getFinishCount(role, ActivityType.XM_DUGEON, 1);
        if (totalCount - 1 == finishCount || finishCount == 1) {
          ActivityLivenessSet activityLivenessSet = (ActivityLivenessSet)ActivityDataPool.activityLivenessSetMapByType.get(ActivityType.XM_DUGEON);
          if (activityLivenessSet != null)
            activationTips(role, (int)activityLivenessSet.getPerLiveness()); 
        } 
      }  
  }
  
  private boolean isAcceptedTask(Role role) {
    return (getTaskSetByType(role) != null);
  }
  
  private void doEnterDugeon(Role role) {
    this.mapService.changeMap(role, new ReqTelePort(8001, 36, 23), null);
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
  
  public void leave(Role role) {
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (team != null && !team.isLeader(role.getRoleId().intValue())) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("既然是集体行动，还是老老实实听从队长的指挥啦。"));
      return;
    } 
    List<SysDataTaskXm> taskXmList = TaskDataPool.taskXmList;
    closeTaskIcon(team, role, taskXmList);
    clear(role, taskXmList);
    leaveDungeon(role, 1209, 34, 28, null);
  }
  
  public void leaveDugeon(Role role) {
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (team != null && team.isLeader(role.getRoleId().intValue())) {
      closeTaskIcon(team, role, TaskDataPool.taskXmList);
    } else {
      closeTaskIcon((Team)null, role, TaskDataPool.taskXmList);
    } 
    SpringUtils.getMapService().changeMap(role, 1209, 34, 28, null);
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
  
  public Dugeon getDugeon(Role role) {
    Long dugeonId = (Long)DataCache.ROLE_DUNGEON.getOrDefault(role.getId(), Long.valueOf(0L));
    if (dugeonId.longValue() > 0L)
      return (Dugeon)DataCache.DUNGEON_MAP.get(dugeonId); 
    return null;
  }
  
  public TaskInfo getTaskInfo(Role role, TaskSet taskSet) {
    TaskInfo taskInfo = super.getTaskInfo(role, taskSet);
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (team != null)
      this.teamService.memberHandleThreadLocal(team, memberRole -> taskScreenIcon(memberRole, taskSet.getSerial(), "", (byte)1)); 
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
    if (!isCanTeam(role, leaderRole, Const.DUGEON_XM_MAP)) {
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
    return Const.DUGEON_XM_MAP.contains(role.getPos().getMapId());
  }
  
  public boolean isInviteJoinTeam(Role role, int inviteRoleId) {
    Role invitRole = this.roleService.getPlayer(inviteRoleId);
    if (invitRole == null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("对方所在地图无法接受邀请哦。"));
      return false;
    } 
    if (!isCanTeam(role, invitRole, Const.DUGEON_XM_MAP)) {
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
    if (!isCanTeam(role, joinRole, Const.DUGEON_XM_MAP)) {
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
    if (!isCanTeam(leaderRole, memberRole, Const.DUGEON_XM_MAP)) {
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
    SysDataTaskXm taskXm = TaskDataPool.taskXmList.stream().filter(e -> (e.getSerial() == taskSet.getSerial())).findFirst().orElse(null);
    if (taskXm != null && taskXm.getPower() > 0.0D) {
      int record = role.getTaskProgress().getFinishRecord().getInt(ActivityType.XM_DUGEON.getActivityId() + taskSet.getSerial(), 0);
      if (record == 0) {
        if (this.teamService.isTeamLeader(role)) {
          long num = this.activityService.addNice(role, 5L);
          if (num > 0L)
            MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "获得好心值#<IR" + Const.RESTYPE_NICE + ">#" + MoneyUtil.colours(Long.valueOf(num)) + "，队长带队辛苦了#<E:23>#", true)); 
          this.activityService.teamLeaderNice(role, taskSet, ActivityType.XM_DUGEON.getActivityId(), 10L);
        } 
        int level = taskXm.getGrade() % 1000;
        List<Param> list = (List<Param>)JsonUtils.string2Collection(taskSet.getTaskReward(), List.class, Param.class);
        if (list != null)
          ((BagService)SpringUtils.getBean(BagService.class)).sendReward(role, list, "心魔副本", 0); 
        this.roleService.addExp(role, calculate(level, role.getRelive().intValue(), taskXm.getPower(), ItemClassify.HERO_EXP), "心魔", true);
        Integer combatPet = role.getExtendBox().getCombatPet();
        if (combatPet.intValue() > 0) {
          Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
          if (pet != null)
            this.petService.addExp(role, pet, calculate(level, pet.getRebirth().intValue(), taskXm.getPower(), ItemClassify.PET_EXP), true); 
        } 
      } else {
        MessagePusher.pushMessage(role, (Message)new RespMsg("你今日已获得本关卡奖励,不能再次获得"));
      } 
    } 
  }
  
  public void closeTaskIcon(Team team, Role role, List<SysDataTaskXm> list) {
    if (team != null) {
      if (team.isLeader(role.getRoleId().intValue()))
        for (Member member : team.getList()) {
          Role player = this.roleService.getPlayer(member.getId().longValue());
          if (player != null)
            closeTaskIcon(player, getDungeonTask(list, role)); 
        }  
    } else {
      closeTaskIcon(role, getDungeonTask(list, role));
    } 
  }
  
  public DoingTask getDungeonTask(List<SysDataTaskXm> list, Role role) {
    DoingTask doingTask = null;
    for (SysDataTaskXm task : list) {
      if (role.getTask().isAccepted(task.getSerial())) {
        doingTask = (DoingTask)role.getTask().getDoingTask().get(Integer.valueOf(task.getSerial()));
        break;
      } 
    } 
    return doingTask;
  }
  
  public void closeTaskIcon(Role role, DoingTask dungeonTask) {
    int serial = 0;
    String difficulty = null;
    if (dungeonTask != null) {
      serial = dungeonTask.getTaskId();
      difficulty = dungeonTask.getPara();
    } 
    taskScreenIcon(role, serial, difficulty, (byte)0);
  }
  
  private boolean isDungeonMember(Role role) {
    if (role == null)
      return false; 
    return DataCache.ROLE_DUNGEON.containsKey(role.getId());
  }
  
  @EventHandler({EventType.LEAVE_MAP})
  public void handleLeaveMapEvent(LeaveMapEvent leaveMapEvent) {
    Role role = leaveMapEvent.getRole();
    int mapId = role.getPos().getMapId().intValue();
    int oldMapId = leaveMapEvent.getMapId();
    if (Const.DUGEON_XM_MAP.contains(Integer.valueOf(oldMapId)) && !Const.DUGEON_XM_MAP.contains(Integer.valueOf(mapId))) {
      List<SysDataTaskXm> list = TaskDataPool.taskXmList;
      clear(role, list);
      closeTaskIcon(this.teamService.getTeam(role.getRoleId().intValue()), role, list);
      role.save();
    } 
  }
  
  @EventHandler({EventType.LOGIN})
  public void handleLoginEvent(LoginEvent loginEvent) {
    Role role = loginEvent.getRole();
    boolean flag = false;
    List<SysDataTaskXm> list = TaskDataPool.taskXmList;
    if (Const.DUGEON_XM_MAP.contains(role.getPos().getMapId())) {
      DoingTask doingTask = null;
      for (SysDataTaskXm task : list) {
        if (role.getTask().isAccepted(task.getSerial())) {
          flag = true;
          doingTask = (DoingTask)role.getTask().getDoingTask().get(Integer.valueOf(task.getSerial()));
          break;
        } 
      } 
      if (flag) {
        if (doingTask != null) {
          if (!isDungeonMember(role)) {
            List<Long> memberList = new ArrayList<>();
            memberList.add(role.getId());
            Dugeon dugeon = new Dugeon(IdGenerator.getNextId(), memberList);
            DataCache.DUNGEON_MAP.put(Long.valueOf(dugeon.getId()), dugeon);
            for (Iterator<Long> iterator = memberList.iterator(); iterator.hasNext(); ) {
              long uid = ((Long)iterator.next()).longValue();
              DataCache.ROLE_DUNGEON.put(Long.valueOf(uid), Long.valueOf(dugeon.getId()));
            } 
          } 
          init();
          taskScreenIcon(role, doingTask.getTaskId(), doingTask.getPara(), (byte)1);
        } else {
          leave(role);
        } 
      } else {
        leaveDungeon(role, 1209, 34, 28, null);
      } 
    } 
  }
  
  public void clear(Role role, List<SysDataTaskXm> list) {
    for (SysDataTaskXm obj : list) {
      TaskSet taskSet = this.taskService.getTaskSet(obj.getSerial());
      if (this.taskService.isTask(role, taskSet)) {
        delTask(role, taskSet);
        deleteDugeon(getDugeon(role));
      } 
    } 
  }
  
  private static long calculate(int grade, int rei, double power, ItemClassify type) {
    double fold = ActivityType.XM_DUGEON.getFold();
    double exp = 0.0D;
    if (ItemClassify.HERO_EXP == type) {
      exp = BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(1.6E7D * (1.0D + ((grade + rei * 15) / 140) * 2.5D) / 30.0D * 0.6D * power)).longValue();
    } else if (ItemClassify.PET_EXP == type) {
      exp = BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Long.valueOf(BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(1.6E7D * (1.0D + ((grade + rei * 15) / 140) * 2.5D) / 30.0D * 0.6D * power * 1.2D)).longValue())).longValue();
    } 
    return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf((fold > 1.0D) ? (exp * fold) : exp)).longValue();
  }
}
