package com.kitty.system.game.activity.handler.timing.at;

import com.kitty.common.core.SchedulerManager;
import com.kitty.common.start.ServerService;
import com.kitty.common.utils.DateUtils;
import com.kitty.common.utils.TimeUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.utils.text.Convert;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.listener.event.LeaveMapEvent;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.common.utils.SenderUtils;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.activity.common.EventsUtils;
import com.kitty.system.game.activity.message.RespActivityOpenNotice;
import com.kitty.system.game.activity.message.RespAtGuessRaceInfo;
import com.kitty.system.game.activity.model.user.at.AtWillData;
import com.kitty.system.game.activity.model.user.at.AtWillPkData;
import com.kitty.system.game.activity.model.user.at.AtWillSet;
import com.kitty.system.game.activity.service.ActivityHandler;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.chat.service.ChatService;
import com.kitty.system.game.confirm.model.LeaveTeamConfirm;
import com.kitty.system.game.confirm.model.LeaveTempTeamConfirm;
import com.kitty.system.game.confirm.model.RoleConfirm;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.npc.service.NpcService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.role.service.TitleService;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.message.RespConfirm;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.vo.ItemEasyInfo;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.vo.TitleInfo;
import com.kitty.system.init.domain.vo.Param;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AtWillHandler extends ActivityHandler {
  @Autowired
  protected TeamService teamService;
  
  @Autowired
  ChatService chatService;
  
  @Autowired
  BagService bagService;
  
  private AtWillSet atWillSet;
  
  private AtWillData atWillData;
  
  @Autowired
  ServerService serverService;
  
  private static final int MAP = 4154;
  
  private static final int MAP_X = 60;
  
  private static final int MAP_Y = 35;
  
  private static final int EXIT_MAP = 1011;
  
  private static final int EXIT_X = 132;
  
  private static final int EXIT_Y = 67;
  
  private static final short TITLE_SERIAL = 10024;
  
  private ScheduledFuture scheduledFuture;
  
  private static final String ACTIVITY_NAME = "英雄争霸赛";
  
  private static final String TIME_FORMAT_PATTERN = "yyyy-MM-dd-HH:mm:ss";
  
  private static final String FIGHT_LEVEL_NOT_ENOUGH = "等级达到30级方可参加英雄争霸赛。";
  
  private static final String NOT_SIGN_UP_TIME = "现在还不是报名时间，英雄争霸赛下一场将于#<R>{0}#开始报名。";
  
  private static final String SIGN_UP_END = "英雄争霸赛正在举行中不可报名参加";
  
  private static final String TEAM_COUNT_NOT_ENOUGH = "队伍不足三人无法参加英雄争霸赛。";
  
  private static final String MEMBER_LEAVE_MOMENT = "#<R>{0}#暂离，无法参加英雄争霸赛。";
  
  private static final String REQUEST_JOIN_LIMIT = "当前地图无法申请";
  
  private static final String INVITE_JOIN_LIMIT = "当前地图无法邀请";
  
  private static final String ACCEPT_JOIN_LIMIT = "该地图无法操作申请队员";
  
  private static final String COMPETE_PK_LIMIT = "你现在不能与人切磋。";
  
  private static final String LEAVE_MOMENT = "英雄争霸赛场地中无法暂离，确认暂离将会离开队伍。";
  
  private static final String LEAVE_TEAM = "英雄争霸赛场地中无法离队，确认后将会离开队伍。";
  
  public static final String REWARD_CONTENT = "#<LB1>{0}#赢得了本次英雄争霸赛#<R>第{1}名#，获得了{2}的奖励！";
  
  public AtWillSet getAtWillSet() {
    return this.atWillSet;
  }
  
  public AtWillData getAtWillData() {
    return this.atWillData;
  }
  
  public void setAtWillData(AtWillData atWillData) {
    this.atWillData = atWillData;
  }
  
  public void prepareOnceAtWill() {
    initAtWillData();
    addCheckJob();
    addStartJob();
    addNoticeJob();
    addEndJob();
  }
  
  private void initAtWillData() {
    AtWillData atWillData = new AtWillData();
    initTime(atWillData);
    this.atWillData = atWillData;
  }
  
  public void initTime(AtWillData atWillData) {
    Calendar calendarStart = Calendar.getInstance();
    calendarStart.setTime(new Date());
    AtWillSet atWillSet = getAtWillSet();
    String[] signUp = atWillSet.getSignUpTime().split(":");
    calendarStart.set(11, Integer.valueOf(signUp[0]).intValue());
    calendarStart.set(12, Integer.valueOf(signUp[1]).intValue());
    calendarStart.set(13, 0);
    atWillData.setSignUpTime(EventsUtils.getTime(calendarStart));
    String[] start = atWillSet.getStartTime().split(":");
    calendarStart.set(11, Integer.valueOf(start[0]).intValue());
    calendarStart.set(12, Integer.valueOf(start[1]).intValue());
    calendarStart.set(13, 0);
    atWillData.setStartTime(EventsUtils.getTime(calendarStart));
    calendarStart.add(12, atWillSet.getSustain());
    atWillData.setEndTime(EventsUtils.getTime(calendarStart));
    int durationTime = (int)(atWillData.getStartTime().getTimeInMillis() / 1000L);
    atWillData.setPkDurationTime(durationTime);
  }
  
  private void addCheckJob() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(this.atWillData.getStartTime().getTime());
    calendar.add(11, -1);
    long initialDelay = this.atWillData.getStartTime().getTimeInMillis() - System.currentTimeMillis();
    this.logger.error("添加定时检测的定时任务==={}", Long.valueOf(initialDelay));
    try {
      if (this.scheduledFuture != null)
        this.scheduledFuture.cancel(false); 
    } catch (Exception e) {
      this.logger.error("定时器关闭==", e);
    } 
    this.scheduledFuture = SchedulerManager.getInstance().scheduleAtFixedRate(() -> checkInterval(), initialDelay, 60000L);
  }
  
  private void addStartJob() {
    long initialDelay = this.atWillData.getStartTime().getTimeInMillis() - System.currentTimeMillis();
    this.logger.error("添加英雄争霸赛开始的定时任务==={}", Long.valueOf(initialDelay));
    SchedulerManager.getInstance().schedule(() -> ((AtWillPkHandler)SpringUtils.getBean(AtWillPkHandler.class)).start(), initialDelay);
  }
  
  private void addNoticeJob() {
    long initialDelay = this.atWillData.getSignUpTime().getTimeInMillis() - System.currentTimeMillis();
    this.logger.error("添加英雄争霸赛报名通知的定时任务==={}", Long.valueOf(initialDelay));
    SchedulerManager.getInstance().schedule(() -> notice(), initialDelay);
  }
  
  private void addEndJob() {
    long initialDelay = this.atWillData.getEndTime().getTimeInMillis() - System.currentTimeMillis();
    this.logger.error("添加英雄争霸赛结束的定时任务==={}", Long.valueOf(initialDelay));
    SchedulerManager.getInstance().schedule(() -> end(), initialDelay);
  }
  
  private void notice() {
    SenderUtils.brodCastMessage((Message)new RespActivityOpenNotice(), LOGIC.LOGICTYPE_HERO.getValue());
  }
  
  private void end() {
    ((AtWillPkHandler)SpringUtils.getBean(AtWillPkHandler.class)).end();
    if (this.scheduledFuture != null)
      this.scheduledFuture.cancel(Boolean.FALSE.booleanValue()); 
  }
  
  private void checkInterval() {
    Calendar calendar = Calendar.getInstance();
    if (this.atWillData.getStage() == 1)
      ((AtWillPkHandler)SpringUtils.getBean(AtWillPkHandler.class)).checkSystemMessage(calendar); 
  }
  
  public void enterMap(Role role, Npc npc) {
    String talk = canEnter(role);
    if (talk != null) {
      if (!talk.isEmpty())
        ((NpcService)SpringUtils.getBean(NpcService.class)).sendNpcContent(role, npc, "", talk); 
    } else {
      doEnterMap(role);
    } 
  }
  
  public String canEnter(Role role) {
    AtWillData atWillData = getAtWillData();
    if (atWillData == null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg(EventsUtils.canEnterTips(this.atWillSet.getDayOfWeek(), this.atWillSet.getSignUpTime(), this.atWillSet.getStartTime(), "英雄争霸赛")));
      return "";
    } 
    Calendar calendar = Calendar.getInstance();
    if (calendar.before(atWillData.getSignUpTime()))
      return MessageFormat.format("现在还不是报名时间，英雄争霸赛下一场将于#<R>{0}#开始报名。", new Object[] { TimeUtil.formatDate(atWillData.getSignUpTime().getTime(), "yyyy-MM-dd-HH:mm:ss") }); 
    if (role.getLevel().intValue() < this.atWillSet.getMinLevel())
      return "等级达到30级方可参加英雄争霸赛。"; 
    if (calendar.after(atWillData.getStartTime()))
      return "英雄争霸赛正在举行中不可报名参加"; 
    int teamCount = this.teamService.getTeamCount(role);
    if (teamCount < this.atWillSet.getMinTeamMemberCount())
      return "队伍不足三人无法参加英雄争霸赛。"; 
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    String names = this.teamService.getNotInTeam(team);
    if (names != null)
      return MessageFormat.format("#<R>{0}#暂离，无法参加英雄争霸赛。", new Object[] { names }); 
    return null;
  }
  
  public void doEnterMap(Role role) {
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (team != null) {
      ReqTelePort reqTelePort = new ReqTelePort();
      reqTelePort.setMapId(4154);
      reqTelePort.setX(60);
      reqTelePort.setY(35);
      SpringUtils.getMapService().changeMap(role, reqTelePort, null);
      AtWillPkData data = this.atWillData.getPkDataList().stream().filter(e -> (e.getTeamId() == team.getId())).findFirst().orElse(null);
      if (data != null) {
        data.updateTeamMember(team);
      } else {
        this.atWillData.getPkDataList().add(new AtWillPkData(team, this.atWillSet.getPkValue()));
      } 
      for (int i = 0; i < team.getList().size(); i++) {
        Member member = team.getList().get(i);
        Role player = SpringUtils.getRoleService().getPlayer(member.getId().longValue());
        if (player != null) {
          RespAtGuessRaceInfo atGuessRaceInfo = new RespAtGuessRaceInfo();
          atGuessRaceInfo.setMateSecond(this.atWillSet.getPeriod());
          atGuessRaceInfo.setStartTime(Math.toIntExact(this.atWillData.getStartTime().getTimeInMillis() / 1000L));
          MessagePusher.pushMessage(player, (Message)atGuessRaceInfo);
        } 
      } 
    } 
  }
  
  public void leave(Role role) {
    if (role != null) {
      if (!isLeave(role))
        return; 
      Team team = this.teamService.getTeam(role.getRoleId().intValue());
      doAfterLeaveTeam(role, team);
    } 
  }
  
  public boolean isLeave(Role role) {
    if (((FightNewService)SpringUtils.getBean(FightNewService.class)).isInFight(role))
      return false; 
    return true;
  }
  
  public void leaveMap(Role role) {
    if (role == null)
      return; 
    if (role.getPos().getMapId().intValue() != 4154)
      return; 
    try {
      Team team = this.teamService.getTeam(role.getRoleId().intValue());
      if (team != null && team.isLeader(role.getRoleId().intValue())) {
        AtWillPkData atWillPkData = this.atWillData.getPkDataList().stream().filter(e -> (e.getTeamId() == team.getId())).findFirst().orElse(null);
        if (atWillPkData != null)
          atWillPkData.setLeave(System.currentTimeMillis()); 
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
    role.pushTempCache("not_check_change_map", Boolean.valueOf(true));
    ReqTelePort reqTelePort = new ReqTelePort();
    reqTelePort.setMapId(1011);
    reqTelePort.setX(132);
    reqTelePort.setY(67);
    SpringUtils.getMapService().changeMap(role, reqTelePort, null);
  }
  
  public boolean isRequestJoinTeam(Role role, Team team) {
    if (isInMap(role)) {
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("当前地图无法申请"));
      return false;
    } 
    Role leaderRole = SpringUtils.getRoleService().getPlayer(team.getLeaderUid());
    if (role == null || leaderRole == null)
      return false; 
    if (isInMap(leaderRole)) {
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("当前地图无法申请"));
      return false;
    } 
    return true;
  }
  
  public boolean isInviteJoinTeam(Role role, int inviteRoleId) {
    if (isInMap(role)) {
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("当前地图无法邀请"));
      return false;
    } 
    Role invitRole = SpringUtils.getRoleService().getOnlinePlayer(inviteRoleId);
    if (invitRole == null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("对方所在地图无法接受邀请哦。"));
      return false;
    } 
    if (isInMap(invitRole)) {
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("当前地图无法邀请"));
      return false;
    } 
    return true;
  }
  
  public boolean isAcceptJoinTeam(Role role, int joinRoleId) {
    if (isInMap(role)) {
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("该地图无法操作申请队员"));
      return false;
    } 
    Role joinRole = SpringUtils.getRoleService().getOnlinePlayer(joinRoleId);
    if (isInMap(joinRole)) {
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("该地图无法操作申请队员"));
      return false;
    } 
    return true;
  }
  
  public boolean isBackToTeam(Team team, Role memberRole) {
    Role leaderRole = SpringUtils.getRoleService().getOnlinePlayer(team.getLeaderUid());
    if (isInMap(leaderRole) && this.atWillData != null && this.atWillData.getStage() == 1) {
      MessagePusher.pushMessage(memberRole, (Message)new RespMsg("当前处于英雄争霸赛决战阶段不可以归队"));
      return false;
    } 
    return true;
  }
  
  public boolean leaveMomentConfirm(Role role) {
    if (isInMap(role) && !isLeave(role))
      return false; 
    if (isInMap(role) && isLeave(role)) {
      role.setConfirm((RoleConfirm)new LeaveTempTeamConfirm(1));
      RespConfirm respConfirm = new RespConfirm();
      respConfirm.setTips("英雄争霸赛场地中无法暂离，确认暂离将会离开队伍。");
      respConfirm.setConfirmType("");
      MessagePusher.pushMessage(role, (Message)respConfirm);
      return false;
    } 
    return true;
  }
  
  public boolean leaveConfirm(Role role) {
    if (isInMap(role) && !isLeave(role))
      return false; 
    if (isInMap(role) && isLeave(role)) {
      role.setConfirm((RoleConfirm)new LeaveTeamConfirm(1));
      RespConfirm respConfirm = new RespConfirm();
      respConfirm.setTips("英雄争霸赛场地中无法离队，确认后将会离开队伍。");
      respConfirm.setConfirmType("");
      MessagePusher.pushMessage(role, (Message)respConfirm);
      return false;
    } 
    return true;
  }
  
  public void handleTeamLeaveMoment(Role role) {
    if (isInMap(role)) {
      Team team = this.teamService.getTeam(role.getRoleId().intValue());
      doAfterLeaveTeam(role, team);
    } 
    super.handleTeamLeaveMoment(role);
  }
  
  public void handleTeamLeave(Role role, boolean isLeader, Team team) {
    if (team != null && team.getList().size() < this.atWillSet.getMinTeamMemberCount()) {
      doAfterLeaveTeam(role, team);
    } else if (team != null && this.atWillData != null) {
      AtWillPkData atWillPkData = this.atWillData.getPkDataList().stream().filter(e -> (e.getTeamId() == team.getId())).findFirst().orElse(null);
      if (atWillPkData != null)
        atWillPkData.updateTeamMember(team); 
    } 
  }
  
  public void handleLogin(Role role) {
    boolean inMap = isInMap(role);
    boolean flag = false;
    if (this.atWillData == null || System.currentTimeMillis() >= this.atWillData.getEndTime().getTimeInMillis())
      flag = true; 
    if (inMap && flag) {
      leaveMap(role);
    } else if (this.atWillData != null) {
      boolean is = true;
      for (AtWillPkData atWillPkData : this.atWillData.getPkDataList()) {
        Member member = atWillPkData.getMembers().stream().filter(e -> e.getId().equals(role.getId())).findFirst().orElse(null);
        if (member != null) {
          is = false;
          break;
        } 
      } 
      if (is)
        leaveMap(role); 
    } 
    super.handleLogin(role);
  }
  
  @EventHandler({EventType.LEAVE_MAP})
  public void handleLeaveMapEvent(LeaveMapEvent leaveMapEvent) {
    Role role = leaveMapEvent.getRole();
    if (role != null && leaveMapEvent.getMapId() == 4154)
      MessagePusher.pushMessage(role, (Message)new RespHint(17, (short)1)); 
  }
  
  public void handleFightEnd(FightEndEvent fightEndEvent) {}
  
  public boolean isInMap(Role role) {
    if (role == null)
      return false; 
    return DataGlobalFunc.isInMap(role, 4154);
  }
  
  public void doAfterLeaveTeam(Role role, Team team) {
    try {
      if (!isInMap(role))
        return; 
      leaveMap(role);
      if (team != null && this.teamService.getTeamCount(team) < this.atWillSet.getMinTeamMemberCount()) {
        Role leaderRole = SpringUtils.getRoleService().getPlayer(team.getLeaderUid());
        if (leaderRole != null)
          leaveMap(leaderRole); 
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public boolean isCompetePk(Role role, Role pkRole) {
    if (isInMap(role)) {
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("你现在不能与人切磋。"));
      return false;
    } 
    if (isInMap(pkRole)) {
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("你现在不能与人切磋。"));
      return false;
    } 
    return true;
  }
  
  public void loadCommonSet(Map<String, String> commonSetMap) {
    loadAtWillSet(commonSetMap);
  }
  
  private void loadAtWillSet(Map<String, String> commonSetMap) {
    String value = commonSetMap.get("at_team_set");
    AtWillSet atWillSet = null;
    if (value != null) {
      atWillSet = (AtWillSet)Json.fromJson(AtWillSet.class, value);
    } else {
      atWillSet = new AtWillSet();
    } 
    this.atWillSet = atWillSet;
  }
  
  public void send(List<Member> list, byte rank) {
    try {
      RoleService roleService = SpringUtils.getRoleService();
      TitleService titleService = (TitleService)SpringUtils.getBean(TitleService.class);
      String day = DateUtils.dayForWeek(new Date());
      int index = this.atWillSet.getDayOfWeek().indexOf(day) + 1;
      if (index >= this.atWillSet.getDayOfWeek().size())
        index = 0; 
      String temporalAdjusters = TimeUtil.temporalAdjusters(Integer.valueOf(this.atWillSet.getDayOfWeek().get(index)).intValue());
      temporalAdjusters = temporalAdjusters + " " + this.atWillSet.getSignUpTime();
      Date parse = TimeUtil.parse(temporalAdjusters, "yyyy-MM-dd HH:mm:ss");
      int time = Math.toIntExact((parse.getTime() - 1200000L) / 1000L);
      List<Param> params = (List<Param>)this.atWillSet.getAward().get(Byte.valueOf(rank));
      Map<Integer, Integer> map = new HashMap<>();
      String msg = "";
      if (params != null)
        for (Param param : params) {
          ItemEasyInfo itemEasyInfo = DataGlobalFunc.getItemEasyInfo(param.getK());
          if (itemEasyInfo != null) {
            map.put(param.getK(), param.getV());
            msg = msg + itemEasyInfo.getName() + "X" + param.getV() + "、";
          } 
        }  
      String names = "";
      for (Member member : list) {
        Role role = roleService.getPlayer(member.getId().longValue());
        if (role == null)
          continue; 
        titleService.addTitle(role, new TitleInfo(10024, time));
        names = names + role.getName() + "、";
        for (Integer serial : map.keySet())
          this.bagService.addGoods(role, serial.intValue(), ((Integer)map.get(serial)).intValue(), 0, true, false, "英雄争霸赛"); 
      } 
      if (!names.isEmpty() && !msg.isEmpty()) {
        msg = msg.substring(0, msg.length() - 1);
        names = names.substring(0, names.length() - 1);
        String content = MessageFormat.format("#<LB1>{0}#赢得了本次英雄争霸赛#<R>第{1}名#，获得了{2}的奖励！", new Object[] { names, Convert.getChineseFromArabic(Integer.valueOf(rank)), msg });
        this.chatService.sendSystem(content, "role");
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public void notifyAt() {
    String day = DateUtils.dayForWeek(new Date());
    if (!this.atWillSet.getDayOfWeek().contains(day))
      return; 
    prepareOnceAtWill();
    SchedulerManager.getInstance().schedule(new Runnable() {
          public void run() {
            String notice = "万众瞩目的英雄争霸赛将于二十分钟后正式开始请各路英雄组好队做好准备找比武使者进场吧";
            AtWillHandler.this.chatService.sendAdnotice(notice);
          }
        },  30000L);
  }
  
  public void cs() {
    AtWillData atWillData = new AtWillData();
    Calendar calendarStart = Calendar.getInstance();
    calendarStart.setTime(new Date());
    AtWillSet atWillSet = getAtWillSet();
    calendarStart.set(13, 0);
    calendarStart.add(12, 1);
    atWillData.setSignUpTime(EventsUtils.getTime(calendarStart));
    calendarStart.set(13, 0);
    calendarStart.add(12, 1);
    atWillData.setStartTime(EventsUtils.getTime(calendarStart));
    calendarStart.add(12, atWillSet.getSustain());
    atWillData.setEndTime(EventsUtils.getTime(calendarStart));
    int durationTime = (int)(atWillData.getStartTime().getTimeInMillis() / 1000L);
    atWillData.setPkDurationTime(durationTime);
    this.atWillData = atWillData;
    addCheckJob();
    addStartJob();
    addNoticeJob();
    addEndJob();
  }
}
