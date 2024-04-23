package com.kitty.system.game.task.service.handler.dungeon;

import com.kitty.common.bean.Lottery;
import com.kitty.common.core.SchedulerManager;
import com.kitty.common.thread.IdGenerator;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.common.utils.NumberUtil;
import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.LoginEvent;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.enums.task.TaskType;
import com.kitty.system.enums.war.wardef.HERO;
import com.kitty.system.enums.war.wardef.RACE;
import com.kitty.system.game.activity.model.Dugeon;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.confirm.model.CreateDungeonMwkFrontConfirm;
import com.kitty.system.game.confirm.model.RoleConfirm;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.i18n.I18nIdDataPool;
import com.kitty.system.game.i18n.P_I18nId;
import com.kitty.system.game.map.pool.MapDataPool;
import com.kitty.system.game.npc.domain.SysNpcInfo;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.task.domain.DoingTask;
import com.kitty.system.game.task.message.RespBallotResult;
import com.kitty.system.game.task.message.RespTeamBallot;
import com.kitty.system.game.task.message.vo.BallotInfo;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.task.service.handler.TaskHandler;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.init.domain.SysDataTaskMo;
import com.kitty.system.init.domain.SysDataTaskMoAward;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DungeonMwkFront extends TaskHandler {
  private static final int FIRST_TASK_ID = 1700001;
  
  private static final int MIN_TEAM_COUNT = 3;
  
  private static final int MIN_RELIVE = 2;
  
  private static final int MIN_LEVEL = 130;
  
  private static final int MAX_LEVEL_DIFF = 15;
  
  private static final String DUNGEON_NAME = "魔王窟前殿";
  
  private static final int SHOW_SECS = 30;
  
  private static final Map<String, Integer> DUNGEON_STAGE_FIGHT_MAP = new HashMap<String, Integer>() {
    
    };
  
  private static final List<Integer> DUNGEON_MAP = Arrays.asList(new Integer[] { Integer.valueOf(2011) });
  
  public static final int DUNGEON_ENTER_MAP = ((Integer)DUNGEON_MAP.get(0)).intValue();
  
  private static final int DUNGEON_ENTER_X = 23;
  
  private static final int DUNGEON_ENTER_Y = 25;
  
  private static final int EXIT_MAP = 1004;
  
  private static final int EXIT_X = 36;
  
  private static final int EXIT_Y = 97;
  
  private double PROB = 0.35D;
  
  public TaskType getTaskType() {
    return TaskType.MWK_FRONT;
  }
  
  public boolean resetTask(Role role, TaskSet taskSet) {
    return false;
  }
  
  @PostConstruct
  public void init() {
    MapDataPool.MAP_KEY_GENERATE.put(Integer.valueOf(DUNGEON_ENTER_MAP), role -> (Long)DataCache.ROLE_DUNGEON.getOrDefault(role.getId(), Long.valueOf(0L)));
  }
  
  public String getDifficulty(String type) {
    String difficulty = "#<LG1>普通#";
    if ("m_f_z".equals(type))
      difficulty = "#<B>卓越#"; 
    return difficulty;
  }
  
  public void chooseEnterDungeon(Role role, Npc npc, String type) {
    createDungeonAndBallot(role, npc, type);
  }
  
  private void createDungeonAndBallot(Role role, Npc npc, String type) {
    String talk = checkCreateDungeon(role);
    if (talk != null) {
      this.npcService.sendNpcContent(role, npc, "", talk);
      return;
    } 
    createDungeonBallot(role, npc, type);
  }
  
  private String checkCreateDungeon(Role role) {
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (team == null)
      return I18nIdDataPool.getI18nContent(1500); 
    if (!this.teamService.isTeamLeader(role) || team.getList().size() < 3)
      return I18nIdDataPool.getI18nContent(1500); 
    String names = this.teamService.getNotInTeam(team);
    if (names != null)
      return I18nIdDataPool.getI18nContent(1510, names);
      //names = this.teamService.checkMember(team, memberRole -> (this.teamService.checkMember(team, (mRole)) != null));
      names = this.teamService.checkMember(team, (memberRole) -> {
        return this.teamService.checkMember(team, (mRole) -> {
          return memberRole.getRelive() * 1000 + memberRole.getLevel() - (mRole.getRelive() * 1000 + mRole.getLevel()) > 15;
        }) != null;
    });
    if (names != null)
      return I18nIdDataPool.getI18nContent(1536, new Object[] { Integer.valueOf(15) }); 
    if (this.teamService.getTeamCount(role) < 3) {
      MessagePusher.notify2Player(role, 1501, new Object[] { "魔王窟前殿", Integer.valueOf(3) });
      return "";
    } 
    names = this.teamService.checkMember(role, memberRole -> (DataGlobalFunc.needGradeJudge(2, 130, memberRole.getRelive().intValue(), memberRole.getLevel().intValue(), "#<R>角色等级#不足") != null));
    if (names != null) {
      P_I18nId idResource = (P_I18nId)I18nIdDataPool.ids.get(Integer.valueOf(1523));
      if (idResource != null) {
        String content = String.format(idResource.getContent(), new Object[] { names, String.format("%d转%d级", new Object[] { Integer.valueOf(2), Integer.valueOf(130) }) });
        return content;
      } 
      return null;
    } 
    return null;
  }
  
  public void createDungeonBallot(Role role, Npc npc, String type) {
    if (this.teamService.isTeamLeader(role)) {
      Map<Integer, Short> ballotResults = new HashMap<>(5);
      //this.teamService.memberHandle(role, memberRole -> (Short)ballotResults.put(memberRole.getRoleId(), 0));
      this.teamService.memberHandle(role, (memberRolex) -> {
        ballotResults.put(memberRolex.getRoleId(), (short) 0);
      });

      ballotResults.put(role.getRoleId(), Short.valueOf((short)1));
      RespTeamBallot respTeamBallot = new RespTeamBallot();
      respTeamBallot.setTeamLeader(this.teamService.isTeamLeader(role));
      String difficulty = getDifficulty(type);
      String msg = I18nIdDataPool.getI18nContent(1506, new Object[] { difficulty, "魔王窟前殿" });
      respTeamBallot.setMsg(msg);
      long millis = System.currentTimeMillis();
      respTeamBallot.setEndTime((new Long(millis / 1000L)).intValue() + 30);
      String title = I18nIdDataPool.getI18nContent(1507, new Object[] { "魔王窟前殿" });
      respTeamBallot.setTitle(title);
      this.teamService.pushMessage(role, (Message)respTeamBallot);
      List<BallotInfo> ballotInfoList = new ArrayList<>();
      ballotInfoList.add(new BallotInfo(role.getSerial().intValue(), role.getName(), (short)1));
      for (Map.Entry<Integer, Short> entry : ballotResults.entrySet()) {
        if (!((Integer)entry.getKey()).equals(role.getRoleId())) {
          Role memberRole = this.roleService.getPlayer(((Integer)entry.getKey()).intValue());
          if (memberRole == null)
            continue; 
          BallotInfo ballotInfo = new BallotInfo(memberRole.getSerial().intValue(), memberRole.getName(), ((Short)entry.getValue()).shortValue());
          ballotInfoList.add(ballotInfo);
        } 
      } 
      RespBallotResult respBallotResult = new RespBallotResult();
      respBallotResult.setResultList(ballotInfoList);
      this.teamService.pushMessage(role, (Message)respBallotResult);
      ScheduledFuture<?> schedule = SchedulerManager.getInstance().schedule(() -> countdown(role, npc), 30000L);
      role.setConfirm((RoleConfirm)new CreateDungeonMwkFrontConfirm(ballotResults, schedule, type));
    } 
  }
  
  public void createBurgeonBallotDisagree(Role role) {
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (team != null) {
      Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
      CreateDungeonMwkFrontConfirm confirm = (CreateDungeonMwkFrontConfirm)leaderRole.getConfirm();
      Map<Integer, Short> ballotResults = confirm.getBallotResults();
      ballotResults.put(role.getRoleId(), Short.valueOf((short)2));
      RespBallotResult respBallotResult = getRespBallotResult(role);
      this.teamService.pushMessageToTeam(team, (Message)respBallotResult);
      if (isAllOp(ballotResults)) {
        cancelTask(confirm.getSchedule());
        this.teamService.memberHandle(team, memberRole -> this.npcService.closeDlg(memberRole, "DungeonVoteDlg"));
        refuseInform(team, ballotResults);
      } 
    } 
  }
  
  private RespBallotResult getRespBallotResult(Role role) {
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    Map<Integer, Short> ballotResults = null;
    Role leaderRole = null;
    if (team.isLeader(role.getRoleId().intValue())) {
      leaderRole = role;
      CreateDungeonMwkFrontConfirm confirm = (CreateDungeonMwkFrontConfirm)role.getConfirm();
      ballotResults = confirm.getBallotResults();
    } else {
      leaderRole = this.roleService.getPlayer(team.getLeaderUid());
      CreateDungeonMwkFrontConfirm confirm = (CreateDungeonMwkFrontConfirm)leaderRole.getConfirm();
      ballotResults = confirm.getBallotResults();
    } 
    ArrayList<BallotInfo> ballotInfoList = new ArrayList<>();
    if (leaderRole != null) {
      BallotInfo ballotInfo = new BallotInfo(leaderRole.getSerial().intValue(), leaderRole.getName(), (short)1);
      ballotInfoList.add(ballotInfo);
    } 
    for (Map.Entry<Integer, Short> entry : ballotResults.entrySet()) {
      Role memberRole = this.roleService.getPlayer(((Integer)entry.getKey()).intValue());
      if (memberRole == null || memberRole.getRoleId().equals(leaderRole.getRoleId()))
        continue; 
      BallotInfo ballotInfo = new BallotInfo(memberRole.getSerial().intValue(), memberRole.getName(), ((Short)entry.getValue()).shortValue());
      ballotInfoList.add(ballotInfo);
    } 
    RespBallotResult respBallotResult = new RespBallotResult();
    respBallotResult.setResultList(ballotInfoList);
    return respBallotResult;
  }
  
  public void createBurgeonBallotAgree(Role role) {
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    Role leaderRole = this.roleService.getOnlinePlayer(team.getLeaderUid());
    CreateDungeonMwkFrontConfirm confirm = (CreateDungeonMwkFrontConfirm)leaderRole.getConfirm();
    Map<Integer, Short> ballotResults = confirm.getBallotResults();
    ballotResults.put(role.getRoleId(), Short.valueOf((short)1));
    RespBallotResult respBallotResult = getRespBallotResult(role);
    this.teamService.pushMessageToTeam(team, (Message)respBallotResult);
    if (isAllOp(ballotResults)) {
      cancelTask(confirm.getSchedule());
      this.teamService.memberHandle(team, memberRole -> this.npcService.closeDlg(memberRole, "DungeonVoteDlg"));
      boolean allAgree = true;
      for (Iterator<Short> iterator = ballotResults.values().iterator(); iterator.hasNext(); ) {
        short result = ((Short)iterator.next()).shortValue();
        if (result != 1)
          allAgree = false; 
      } 
      if (allAgree) {
        createDungeon(team);
        TaskSet taskSet = this.taskService.getTaskSet(1700001);
        taskSet.setPara(confirm.getType());
        this.teamService.memberHandleThreadLocal(team, memberRole -> acceptTask(memberRole, taskSet, -1));
        doEnterDungeon(leaderRole);
        this.teamService.memberHandleThreadLocal(team, memberRole -> taskScreenIcon(memberRole, taskSet.getSerial(), confirm.getType(), (byte)1));
      } else {
        refuseInform(team, ballotResults);
      } 
    } 
  }
  
  public void refuseInform(Team team, Map<Integer, Short> ballotResults) {
    if (team != null && ballotResults.size() > 0) {
      Npc npc = new Npc((SysNpcInfo)DataCache.PLACE_NPC.get(Integer.valueOf(90236)));
      StringBuffer buffer = new StringBuffer();
      for (Integer key : ballotResults.keySet()) {
        short value = ((Short)ballotResults.get(key)).shortValue();
        if (2 == value) {
          Role player = this.roleService.getPlayer(key.intValue());
          if (player != null)
            buffer.append(player.getName() + "、"); 
        } 
      } 
      if (buffer.length() > 1)
        this.teamService.memberHandle(team, memberRole -> this.npcService.sendNpcContent(memberRole, npc, "", buffer.toString() + "没有确定，无法开始" + "魔王窟前殿")); 
    } 
  }
  
  public void cancelTask(ScheduledFuture<?> task) {
    if (task != null && !task.isDone())
      task.cancel(Boolean.FALSE.booleanValue()); 
  }
  
  public void countdown(Role role, Npc npc) {
    CreateDungeonMwkFrontConfirm createDungeonMwkFrontConfirm = (CreateDungeonMwkFrontConfirm)role.getConfirm();
    if (createDungeonMwkFrontConfirm != null) {
      Map<Integer, Short> ballotResults = createDungeonMwkFrontConfirm.getBallotResults();
      if (!isAllOp(ballotResults)) {
        StringBuffer buffer = new StringBuffer();
        for (Integer key : ballotResults.keySet()) {
          short value = ((Short)ballotResults.get(key)).shortValue();
          if (0 == value) {
            Role player = this.roleService.getPlayer(key.intValue());
            if (player != null)
              buffer.append(player.getName() + "、"); 
          } 
        } 
        if (buffer.length() > 1) {
          Team team = this.teamService.getTeam(role.getRoleId().intValue());
          this.teamService.memberHandle(team, memberRole -> this.npcService.sendNpcContent(memberRole, npc, "", buffer.toString() + "没有确定，无法开始" + "魔王窟前殿"));
        } 
      } 
    } 
  }
  
  public int getDungeonFinish(Role role) {
    return role.getTaskProgress().getFinishRecord().getInt(ActivityType.MWK_FRONT.getActivityId(), 0);
  }
  
  private void addActivityCount(Role role) {
    int dungeonFinish = getDungeonFinish(role) + 1;
    dungeonFinish = (dungeonFinish > ActivityType.MWK_FRONT.getTotalCount()) ? ActivityType.MWK_FRONT.getTotalCount() : dungeonFinish;
    role.getTaskProgress().getFinishRecord().setv(ActivityType.MWK_FRONT.getActivityId(), Integer.valueOf(dungeonFinish));
  }
  
  private void doEnterDungeon(Role role) {
    this.mapService.changeMap(role, new ReqTelePort(DUNGEON_ENTER_MAP, 23, 25), null);
  }
  
  private boolean isAllOp(Map<Integer, Short> ballotResults) {
    boolean allOp = true;
    for (Iterator<Short> iterator = ballotResults.values().iterator(); iterator.hasNext(); ) {
      short result = ((Short)iterator.next()).shortValue();
      if (result == 0)
        allOp = false; 
    } 
    return allOp;
  }
  
  private boolean isDungeonMember(Role role) {
    if (role == null)
      return false; 
    return DataCache.ROLE_DUNGEON.containsKey(role.getId());
  }
  
  public void startFight(Role role, Npc npc, String type) {
    String talk = getNpcContentNotFight(role, npc);
    if (talk != null) {
      this.npcService.sendNpcContent(role, npc, "", talk);
      return;
    } 
    TaskSet taskSet = getTaskSetByType(role);
    if (type == null) {
      int serial = ((Integer)DUNGEON_STAGE_FIGHT_MAP.get("m_f_c")).intValue();
      String para = getTaskPara(role, taskSet);
      if (para != null && DUNGEON_STAGE_FIGHT_MAP.get(para) != null)
        serial = ((Integer)DUNGEON_STAGE_FIGHT_MAP.get(para)).intValue(); 
      this.taskService.taskTriggerFight(role, taskSet, serial);
    } else {
      String[] split = type.split("_");
      if (split.length < 1)
        return; 
      String serial = split[split.length - 1];
      boolean number = NumberUtil.isNumber(serial);
      if (number) {
        Team team = this.teamService.getTeam(role.getRoleId().intValue());
        this.teamService.memberHandleThreadLocal(team, memberRole -> role.pushTempCache(String.valueOf(TaskType.MWK_FRONT), type));
        this.taskService.taskTriggerFight(role, taskSet, Integer.valueOf(serial).intValue());
      } 
    } 
  }
  
  private String getNpcContentNotFight(Role role, Npc npc) {
    if (SpringUtils.getFightNewService().isInFight(role))
      return "请老老实实打完你的战斗"; 
    if (this.teamService.getTeamCount(role) < 3)
      return String.format("请组队满%d人再来战斗", new Object[] { Integer.valueOf(3) }); 
    return null;
  }
  
  public void clear(Role role, List<SysDataTaskMo> taskMoList) {
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (team != null) {
      if (team.isLeader(role.getRoleId().intValue()))
        for (Member member : team.getList()) {
          Role player = this.roleService.getPlayer(member.getId().longValue());
          if (player != null)
            clearDungeonTask(player, taskMoList); 
        }  
    } else {
      clearDungeonTask(role, taskMoList);
    } 
  }
  
  public void clearDungeonTask(Role role, List<SysDataTaskMo> taskMoList) {
    for (SysDataTaskMo taskMo : taskMoList) {
      TaskSet taskSet = this.taskService.getTaskSet(taskMo.getSerial());
      if (this.taskService.isTask(role, taskSet)) {
        delTask(role, taskSet);
        deleteDungeon(getDungeon(role));
      } 
    } 
  }
  
  public boolean isInviteJoinTeam(Role role, int inviteRoleId) {
    Role invitRole = this.roleService.getPlayer(inviteRoleId);
    if (invitRole == null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("对方所在地图无法接受邀请哦。"));
      return false;
    } 
    List<Integer> list = Arrays.asList(new Integer[] { Integer.valueOf(DUNGEON_ENTER_MAP) });
    if (!isCanTeam(role, invitRole, list)) {
      if (isInDungeon(role, list)) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("本地图无法邀请。"));
      } else {
        MessagePusher.pushMessage(role, (Message)new RespMsg("对方所在地图无法接受邀请。"));
      } 
      return false;
    } 
    return true;
  }
  
  public boolean isAcceptJoinTeam(Role role, int joinRoleId) {
    Role joinRole = this.roleService.getOnlinePlayer(joinRoleId);
    if (!isCanTeam(role, joinRole, DUNGEON_MAP)) {
      if (isInDungeon(role, DUNGEON_MAP)) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("本地图无法操作申请队员。"));
      } else {
        MessagePusher.pushMessage(role, (Message)new RespMsg("对方所在地图无法接受操作。"));
      } 
      return false;
    } 
    return true;
  }
  
  public boolean isBackToTeam(Team team, Role memberRole) {
    Role leaderRole = this.roleService.getOnlinePlayer(team.getLeaderUid());
    if (!isCanTeam(leaderRole, memberRole, DUNGEON_MAP)) {
      if (isInDungeon(leaderRole, DUNGEON_MAP)) {
        MessagePusher.pushMessage(memberRole, (Message)new RespMsg("队长所在地图无法直接进入。"));
      } else {
        MessagePusher.pushMessage(memberRole, (Message)new RespMsg("本地图无法直接进入。"));
      } 
      return false;
    } 
    return true;
  }
  
  @EventHandler({EventType.LOGIN})
  public void handleLoginEvent(LoginEvent loginEvent) {
    Role role = loginEvent.getRole();
    boolean flag = false;
    List<SysDataTaskMo> taskMoList = TaskDataPool.taskMoList;
    if (role.getPos().getMapId().equals(Integer.valueOf(DUNGEON_ENTER_MAP))) {
      DoingTask doingTask = null;
      for (SysDataTaskMo task : taskMoList) {
        if (role.getTask().isAccepted(task.getSerial())) {
          flag = true;
          doingTask = (DoingTask)role.getTask().getDoingTask().get(Integer.valueOf(task.getSerial()));
          break;
        } 
      } 
      if (flag) {
        if (doingTask != null && doingTask.getPara() != null) {
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
          Map<Integer, Short> ballotResults = new HashMap<>(5);
          role.setConfirm((RoleConfirm)new CreateDungeonMwkFrontConfirm(ballotResults, null, doingTask.getPara()));
          init();
          taskScreenIcon(role, doingTask.getTaskId(), doingTask.getPara(), (byte)1);
        } else {
          leave(role);
        } 
      } else {
        leaveDungeon(role, 1004, 36, 97, null);
      } 
    } 
  }
  
  public void leave(Role role) {
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (team != null && !team.isLeader(role.getRoleId().intValue())) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("既然是集体行动，还是老老实实听从队长的指挥啦。"));
      return;
    } 
    List<SysDataTaskMo> taskMoList = TaskDataPool.taskMoList;
    closeTaskIcon(team, role, taskMoList);
    clear(role, TaskDataPool.taskMoList);
    leaveDungeon(role, 1004, 36, 97, null);
  }
  
  public void closeTaskIcon(Team team, Role role, List<SysDataTaskMo> taskMoList) {
    if (team != null) {
      if (team.isLeader(role.getRoleId().intValue()))
        for (Member member : team.getList()) {
          Role player = this.roleService.getPlayer(member.getId().longValue());
          if (player != null)
            closeTaskIcon(player, getDungeonTask(taskMoList, role)); 
        }  
    } else {
      closeTaskIcon(role, getDungeonTask(taskMoList, role));
    } 
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
  
  public DoingTask getDungeonTask(List<SysDataTaskMo> taskMoList, Role role) {
    DoingTask doingTask = null;
    for (SysDataTaskMo task : taskMoList) {
      if (role.getTask().isAccepted(task.getSerial())) {
        doingTask = (DoingTask)role.getTask().getDoingTask().get(Integer.valueOf(task.getSerial()));
        break;
      } 
    } 
    return doingTask;
  }
  
  public void giveReward(Role role, TaskSet taskSet) {
    String difficulty = "m_f_c";
    List<SysDataTaskMo> taskMoList = TaskDataPool.taskMoList;
    DoingTask doingTask = getDungeonTask(taskMoList, role);
    if (taskSet.getSerial() == 1700001)
      addActivityCount(role); 
    boolean flag = true;
    if (getDungeonFinish(role) < ActivityType.MWK_FRONT.getTotalCount()) {
      String type = null;
      if (doingTask == null) {
        flag = false;
        difficulty = "m_f_c";
        CreateDungeonMwkFrontConfirm createDungeonMwkFrontConfirm = (CreateDungeonMwkFrontConfirm)role.getConfirm();
        if (createDungeonMwkFrontConfirm != null)
          difficulty = createDungeonMwkFrontConfirm.getType(); 
        type = (String)role.getTempCache(String.valueOf(TaskType.MWK_FRONT), "threats_mo_c_170002");
      } else if (doingTask.getPara() != null) {
        difficulty = doingTask.getPara();
      } 
      this.roleService.addExp(role, calculate(role.getLevel().intValue(), role.getRelive().intValue(), difficulty, ItemClassify.HERO_EXP, flag), "魔王窟前殿-副本", true);
      Integer combatPet = role.getExtendBox().getCombatPet();
      if (combatPet.intValue() > 0) {
        Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
        if (pet != null)
          this.petService.addExp(role, pet, calculate(pet.getPetLv().intValue(), pet.getRebirth().intValue(), difficulty, ItemClassify.PET_EXP, flag), true); 
      } 
      specialReward(role, difficulty, flag, type);
    } 
    if (this.teamService.isTeamLeader(role) && doingTask == null) {
      closeTaskIcon(this.teamService.getTeam(role.getRoleId().intValue()), role, taskMoList);
      leaveDungeon(role, 1004, 36, 97, null);
    } 
  }
  
  private static long calculate(int grade, int rei, String difficulty, ItemClassify type, boolean flag) {
    double fold = ActivityType.MWK_FRONT.getFold();
    double exp = BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(1.6E7D * (1.0D + (grade + rei * 15.0D) / 140.0D * 2.5D) / 30.0D)).longValue();
    if (ItemClassify.PET_EXP == type)
      exp = BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(exp * 1.8D)).longValue(); 
    if (difficulty.equals("m_f_z"))
      exp *= 3.0D; 
    return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf((fold > 1.0D) ? (exp * fold) : exp)).longValue();
  }
  
  public void specialReward(Role role, String difficulty, boolean flag, String type) {
    double prob = this.PROB;
    int num = 1;
    if (difficulty.equals("m_f_z")) {
      num += RandomUtil.round(Integer.valueOf(1), Integer.valueOf(3));
      prob += 0.3D;
    } 
    if (!flag)
      if (type != null && type.equals("threats_mo_z_170003"))
        num += RandomUtil.round(Integer.valueOf(1), Integer.valueOf(2));  
    if (SkillAi.canAffectOnPro(prob)) {
      List<Lottery> lotteries = getPlayerAcsry(role.getRace(), role.getSex());
      for (int i = 0; i < num; i++) {
        Lottery lottery = lottery(lotteries);
        if (lottery.getNum() > 0)
          ((BagService)SpringUtils.getBean(BagService.class)).rewardItem(role, lottery.getSerial().intValue(), lottery.getNum(), 0, "魔王窟前殿"); 
      } 
    } 
  }
  
  public static Lottery lottery(List<Lottery> list) {
    List<Double> rates = new ArrayList<>(list.size());
    for (Lottery lottery : list)
      rates.add(lottery.getBili()); 
    return list.get(LotteryUtils.lottery(rates));
  }
  
  public static List<Lottery> getPlayerAcsry(Integer race, Integer sex) {
    double min = 3.0D;
    List<SysDataTaskMoAward> moAwardList = (List<SysDataTaskMoAward>)TaskDataPool.moAwardMap.get(Integer.valueOf(TaskType.MWK_FRONT.getId()));
    List<Lottery> list = new ArrayList<>();
    if (race.intValue() == RACE.RACE_REN.getValue()) {
      if (sex.intValue() == HERO.HERO_MALE.getValue()) {
        List<SysDataTaskMoAward> collect = (List<SysDataTaskMoAward>)moAwardList.stream().filter(e -> (e.getRenMale().intValue() > 0)).collect(Collectors.toList());
        for (SysDataTaskMoAward award : collect)
          list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf(award.getRenMale().doubleValue()), 1)); 
      } else {
        List<SysDataTaskMoAward> collect = (List<SysDataTaskMoAward>)moAwardList.stream().filter(e -> (e.getRenFemale().intValue() > 0)).collect(Collectors.toList());
        for (SysDataTaskMoAward award : collect)
          list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf(award.getRenFemale().doubleValue()), 1)); 
      } 
    } else if (race.intValue() == RACE.RACE_MO.getValue()) {
      if (sex.intValue() == HERO.HERO_MALE.getValue()) {
        List<SysDataTaskMoAward> collect = (List<SysDataTaskMoAward>)moAwardList.stream().filter(e -> (e.getMoMale().intValue() > 0)).collect(Collectors.toList());
        for (SysDataTaskMoAward award : collect)
          list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf(award.getMoMale().doubleValue()), 1)); 
      } else {
        List<SysDataTaskMoAward> collect = (List<SysDataTaskMoAward>)moAwardList.stream().filter(e -> (e.getMoFemale().intValue() > 0)).collect(Collectors.toList());
        for (SysDataTaskMoAward award : collect)
          list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf(award.getMoFemale().doubleValue()), 1)); 
      } 
    } else if (race.intValue() == RACE.RACE_XIAN.getValue()) {
      if (sex.intValue() == HERO.HERO_MALE.getValue()) {
        List<SysDataTaskMoAward> collect = (List<SysDataTaskMoAward>)moAwardList.stream().filter(e -> (e.getXianMale().intValue() > 0)).collect(Collectors.toList());
        for (SysDataTaskMoAward award : collect)
          list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf(award.getXianMale().doubleValue()), 1)); 
      } else {
        List<SysDataTaskMoAward> collect = (List<SysDataTaskMoAward>)moAwardList.stream().filter(e -> (e.getXianFemale().intValue() > 0)).collect(Collectors.toList());
        for (SysDataTaskMoAward award : collect)
          list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf(award.getXianFemale().doubleValue()), 1)); 
      } 
    } else if (race.intValue() == RACE.RACE_GUI.getValue()) {
      if (sex.intValue() == HERO.HERO_MALE.getValue()) {
        List<SysDataTaskMoAward> collect = (List<SysDataTaskMoAward>)moAwardList.stream().filter(e -> (e.getGuiMale().intValue() > 0)).collect(Collectors.toList());
        for (SysDataTaskMoAward award : collect)
          list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf(award.getGuiMale().doubleValue()), 1)); 
      } else {
        List<SysDataTaskMoAward> collect = (List<SysDataTaskMoAward>)moAwardList.stream().filter(e -> (e.getGuiFemale().intValue() > 0)).collect(Collectors.toList());
        for (SysDataTaskMoAward award : collect)
          list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf(award.getGuiFemale().doubleValue()), 1)); 
      } 
    } else if (race.intValue() == RACE.RACE_LONG.getValue()) {
      if (sex.intValue() == HERO.HERO_MALE.getValue()) {
        List<SysDataTaskMoAward> collect = (List<SysDataTaskMoAward>)moAwardList.stream().filter(e -> (e.getLongMale().intValue() > 0)).collect(Collectors.toList());
        for (SysDataTaskMoAward award : collect)
          list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf(award.getLongMale().doubleValue()), 1)); 
      } else {
        List<SysDataTaskMoAward> collect = (List<SysDataTaskMoAward>)moAwardList.stream().filter(e -> (e.getLongFemale().intValue() > 0)).collect(Collectors.toList());
        for (SysDataTaskMoAward award : collect)
          list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf(award.getLongFemale().doubleValue()), 1)); 
      } 
    } 
    return list;
  }
}
