package com.kitty.system.game.activity.handler.timing.at;

import com.kitty.common.core.SchedulerManager;
import com.kitty.common.start.ServerService;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.mina.task.ThreadLocalUtil;
import com.kitty.system.common.data.ActivityGlobalFunc;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.game.activity.common.EventsUtils;
import com.kitty.system.game.activity.message.RespAtClashResult;
import com.kitty.system.game.activity.model.user.at.AtWillData;
import com.kitty.system.game.activity.model.user.at.AtWillPkData;
import com.kitty.system.game.activity.model.user.at.AtWillSet;
import com.kitty.system.game.activity.service.ActivityHandler;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.factory.FightFactory;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.map.service.MapService;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.init.domain.vo.Param;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AtWillPkHandler extends ActivityHandler {
  @Autowired
  AtWillHandler atWillHandler;
  
  @Autowired
  protected MapService mapService;
  
  @Autowired
  protected TeamService teamService;
  
  @Autowired
  protected FightNewService fightNewService;
  
  @Autowired
  RoleService roleService;
  
  @Autowired
  ServerService serverService;
  
  private ScheduledFuture scheduledFuture;
  
  private static final long PK_INTERVAL = 10000L;
  
  private static final byte MAX_ROUND = 30;
  
  private static final byte TOP_COUNT = 3;
  
  private static final int MATCH_TEAM = 3;
  
  private static boolean MATCH_FLAG = true;
  
  private static final String WIN_MESSAGE = "恭喜你取得了战斗胜利，获得了{0}点积分，你当前的积分为#<R>{1}#点。";
  
  private static final String FAIL_MESSAGE = "真遗憾你失败了，失去了1点巅峰对决值。";
  
  private static final String OUT_MESSAGE = " 真是精彩纷呈啊！#<Y>{0}#的队伍英雄争霸赛正在如火如荼的进行！适才在高手对决中棋差一着，遗憾地败下阵来，真是太可惜了。其他还在拼搏的高手要继续努力！";
  
  private static final String REMAIN_TIME_MESSAGE = "英雄争霸赛还剩#<R>{0}分钟#，大家要抓紧时间啦！";
  
  private static final String USED_TIME_MESSAGE = "英雄争霸赛已经进行了#<R>{0}分钟#，大家加油啊！";
  
  private static final String FIRST_MESSAGE = "恭喜，#<Y>{0}#获得了本届的英雄争霸赛的#<R>第一名#。";
  
  public void start() {
    this.logger.error("英雄争霸赛对决开始");
    try {
      AtWillData atWillData = this.atWillHandler.getAtWillData();
      AtWillSet atWillSet = this.atWillHandler.getAtWillSet();
      withdraw(atWillData);
      atWillData.setStage((byte)1);
      this.scheduledFuture = SchedulerManager.getInstance().scheduleAtFixedRate(new Runnable() {
            public void run() {
              AtWillPkHandler.this.check();
            }
          },  10000L, (atWillSet.getPeriod() * 1000));
    } catch (Exception e) {
      end();
      e.printStackTrace();
      this.logger.error("英雄争霸赛PK阶段开始错误：{}", e.getMessage());
    } 
  }
  
  public void withdraw(AtWillData atWillData) {
    AtWillSet atWillSet = this.atWillHandler.getAtWillSet();
    List<AtWillPkData> list = atWillData.getPkDataList();
    for (AtWillPkData data : list) {
      Team team = this.teamService.getTeamByTeamId(data.getTeamId());
      if (team == null)
        continue; 
      if (team != null && this.teamService.getTeamCount(team) < atWillSet.getMinTeamMemberCount()) {
        Role player = this.roleService.getPlayer(team.getLeaderUid());
        if (player != null) {
          this.atWillHandler.leave(player);
          continue;
        } 
        this.logger.error("AK比赛开始前弃权,队伍人数小于参赛最小人数清理出场。获取角色错误：{}", Long.valueOf(team.getLeaderUid()));
        continue;
      } 
      List<Integer> objects = this.teamService.getTeamAfkMember(team);
      for (Integer object : objects) {
        Role role = this.roleService.getPlayer(object.intValue());
        if (role != null) {
          role.pushTempCache("at_leave", Boolean.valueOf(true));
          this.teamService.leave(role, false);
          this.atWillHandler.leave(role);
          role.pushTempCache("at_leave", Boolean.valueOf(false));
        } 
      } 
    } 
  }
  
  private void check() {
    AtWillData atWillData = this.atWillHandler.getAtWillData();
    if (atWillData == null || atWillData.getEndTime().getTimeInMillis() < System.currentTimeMillis()) {
      end();
      this.logger.error("英雄争霸赛结束，不在匹配");
      return;
    } 
    if (!MATCH_FLAG) {
      this.logger.error("英雄争霸赛状态为匹配中，上次匹配未结束--------------------------------------");
      return;
    } 
    try {
      this.logger.info("英雄争霸赛pk匹配，当前剩余队伍数量 = {}", Integer.valueOf(sceneTeamCount(atWillData)));
      if (isCanEnd()) {
        this.logger.info("英雄争霸赛pk结束");
        end();
      } else {
        MATCH_FLAG = false;
        match();
        MATCH_FLAG = true;
      } 
    } catch (Exception e) {
      MATCH_FLAG = true;
      e.printStackTrace();
      this.logger.error("英雄争霸赛匹配出错-----------------------------------------------");
    } 
  }
  
  private boolean isCanEnd() {
    AtWillData data = this.atWillHandler.getAtWillData();
    if (sceneTeamCount(data) <= 1)
      return true; 
    if (Calendar.getInstance().after(data.getEndTime()))
      return true; 
    return false;
  }
  
  public int sceneTeamCount(AtWillData data) {
    int count = 0;
    if (data != null)
      for (AtWillPkData obj : data.getPkDataList()) {
        Role leader = this.teamService.getTeamLeader(obj.getTeamId());
        if (leader == null)
          continue; 
        if (this.atWillHandler.isInMap(leader))
          count++; 
      }  
    return count;
  }
  
  public void end() {
    try {
      AtWillData atWillData = this.atWillHandler.getAtWillData();
      atWillData.setStage((byte)2);
      this.logger.error("英雄争霸赛对决结束");
      List<AtWillPkData> list = (List<AtWillPkData>)atWillData.getPkDataList().stream().filter(e -> (e.getPkValue() > 0 && e.getScore() > 0)).sorted(Comparator.comparing(AtWillPkData::getPkValue).reversed()).collect(Collectors.toList());
      List<AtWillPkData> topList = new ArrayList<>(3);
      if (list.size() > 0) {
        this.logger.error("当前在场的成员：{}", list);
        for (AtWillPkData willPkData : list)
          topList.add(willPkData); 
      } 
      List<AtWillPkData> collect = (List<AtWillPkData>)atWillData.getPkDataList().stream().sorted(Comparator.comparing(AtWillPkData::getLeave).reversed()).collect(Collectors.toList());
      for (AtWillPkData atWillPkData : collect) {
        if (!topList.contains(atWillPkData))
          topList.add(atWillPkData); 
        if (topList.size() == 3)
          break; 
      } 
      if (topList.size() > 0)
        showGlory(topList.get(0)); 
      grant(topList);
      this.logger.info("英雄争霸赛结束时，队伍前三名=={}", (topList != null) ? JsonUtils.object2String(topList) : "");
      for (AtWillPkData atWillPkData : atWillData.getPkDataList()) {
        Team team = SpringUtils.getTeamService().getTeamByTeamId(atWillPkData.getTeamId());
        if (team == null)
          continue; 
        Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
        if (leaderRole == null)
          continue; 
        this.atWillHandler.leaveMap(leaderRole);
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
    if (this.scheduledFuture != null)
      this.scheduledFuture.cancel(Boolean.FALSE.booleanValue()); 
  }
  
  public void grant(List<AtWillPkData> topList) {
    int size = 3;
    if (topList.size() < 3)
      size = topList.size(); 
    for (int i = 0; i < size; i++) {
      AtWillPkData atWillPkData = topList.get(i);
      this.atWillHandler.send(atWillPkData.getMembers(), (byte)(i + 1));
    } 
  }
  
  public void showGlory(AtWillPkData atWillPkData) {
    if (atWillPkData == null)
      return; 
    Team team = this.teamService.getTeamByTeamId(atWillPkData.getTeamId());
    if (team == null)
      return; 
    String names = this.teamService.checkMember(team, memberRole -> (memberRole != null));
    String msg = MessageFormat.format("恭喜，#<Y>{0}#获得了本届的英雄争霸赛的#<R>第一名#。", new Object[] { names });
    SpringUtils.getChatService().sendSystem(msg, "role");
  }
  
  private void match() {
    try {
      AtWillData atWillData = this.atWillHandler.getAtWillData();
      List<AtWillPkData> atWillPkData = atWillData.getPkDataList();
      List<Team> list = getFightTeamList(atWillPkData);
      List<Team> allList = getFightTeamSceneList(atWillPkData);
      if (list.size() > 0)
        match(allList, list); 
    } catch (Exception e) {
      e.printStackTrace();
      this.logger.error("匹配对手出错-----------------------------------------------");
    } 
  }
  
  private List<Team> getFightTeamList(List<AtWillPkData> pkList) {
    try {
      List<AtWillPkData> tempPkList = new ArrayList<>(pkList);
      tempPkList.sort(Comparator.comparing(AtWillPkData::getPrevPKTime));
      List<Team> list = new ArrayList<>(tempPkList.size());
      long now = System.currentTimeMillis();
      for (AtWillPkData atWillPkData : tempPkList) {
        Team team = this.teamService.getTeamByTeamId(atWillPkData.getTeamId());
        if (team == null)
          continue; 
        Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
        if (leaderRole == null) {
          this.logger.error("获得可以战斗的队伍队长信息时为空了");
          continue;
        } 
        if (atWillPkData.getState() == 1)
          continue; 
        if (!leaderRole.getPos().getMapId().equals(Integer.valueOf(4154)))
          continue; 
        if (!this.fightNewService.canFight(leaderRole, team.getList()))
          continue; 
        Fight fight = this.fightNewService.getFightByRoleId(leaderRole.getRoleId().intValue());
        if (fight != null)
          continue; 
        if (atWillPkData.getPkValue() <= 0)
          continue; 
        if (now - atWillPkData.getPrevPKTime() < 10000L)
          continue; 
        if (now - leaderRole.getPrevFightEndTime() < 10000L)
          continue; 
        list.add(team);
      } 
      return list;
    } catch (Exception e) {
      e.printStackTrace();
      this.logger.error("获得可以战斗的队伍出错了--------------------------------------");
      return new ArrayList<>();
    } 
  }
  
  private List<Team> getFightTeamSceneList(List<AtWillPkData> pkList) {
    try {
      List<AtWillPkData> tempPkList = new ArrayList<>(pkList);
      tempPkList.sort(Comparator.comparing(AtWillPkData::getPrevPKTime));
      List<Team> list = new ArrayList<>(tempPkList.size());
      long now = System.currentTimeMillis();
      for (AtWillPkData atWillPkData : tempPkList) {
        Team team = this.teamService.getTeamByTeamId(atWillPkData.getTeamId());
        if (team == null)
          continue; 
        Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
        if (leaderRole == null)
          continue; 
        if (atWillPkData.getState() == 1)
          continue; 
        if (leaderRole.getPos().getMapId().intValue() != 4154)
          continue; 
        if (now - atWillPkData.getPrevPKTime() < 10000L)
          continue; 
        if (now - leaderRole.getPrevFightEndTime() < 10000L)
          continue; 
        list.add(team);
      } 
      return list;
    } catch (Exception e) {
      e.printStackTrace();
      this.logger.error("所有在场景中的队伍出错了--------------------------------------");
      return new ArrayList<>();
    } 
  }
  
  private void match(List<Team> allList, List<Team> list) {
    try {
      HashSet<Integer> hashSet = new HashSet<>();
      for (Team team : list) {
        if (hashSet.contains(Integer.valueOf(team.getId())))
          continue; 
        Team pkTeam = getPkTeam(team, allList, list, hashSet);
        if (pkTeam != null) {
          pkTeam.setLastTeamId(team.getId());
          team.setLastTeamId(pkTeam.getId());
          doStartFight(team, pkTeam);
          hashSet.add(Integer.valueOf(team.getId()));
          hashSet.add(Integer.valueOf(pkTeam.getId()));
          AtWillData atWillData = this.atWillHandler.getAtWillData();
          AtWillPkData atWillPkData = atWillData.getPkDataList().stream().filter(e -> (e.getTeamId() == team.getId())).findFirst().orElse(null);
          if (atWillPkData != null) {
            atWillPkData.updateTeamMember(team, (byte)1);
          } else {
            this.logger.error("1.更新组队成员获取信息错误：队伍ID = {}", Integer.valueOf(team.getId()));
          } 
          atWillPkData = atWillData.getPkDataList().stream().filter(e -> (e.getTeamId() == pkTeam.getId())).findFirst().orElse(null);
          if (atWillPkData != null) {
            atWillPkData.updateTeamMember(pkTeam, (byte)1);
            continue;
          } 
          this.logger.error("2.更新组队成员获取信息错误：队伍ID = {}", Integer.valueOf(team.getId()));
        } 
      } 
    } catch (Exception e) {
      this.logger.error("进行PK出错------------------------------------");
      e.printStackTrace();
    } 
  }
  
  private Team getPkTeam(Team team, List<Team> allList, List<Team> list, HashSet<Integer> hashSet) {
    try {
      HashSet<Integer> teamHash = new HashSet<>();
      for (int i = 0; i < team.getList().size(); i++) {
        Member member = team.getList().get(i);
        teamHash.add(member.getRoleId());
      } 
      ArrayList<Team> result = new ArrayList<>();
      for (Team tempTeam : list) {
        if (tempTeam.getId() == team.getId())
          continue; 
        if (hashSet.contains(Integer.valueOf(tempTeam.getId())))
          continue; 
        boolean boo = false;
        for (int j = 0; j < tempTeam.getList().size(); j++) {
          Member member1 = tempTeam.getList().get(j);
          if (teamHash.contains(member1.getRoleId())) {
            boo = true;
            break;
          } 
        } 
        if (boo)
          continue; 
        result.add(tempTeam);
      } 
      if (result.size() <= 0) {
        this.logger.info("没有空闲的队伍 = {}，在场景中队伍数量 = {}，在场景可以战斗的队伍数量 = {}", new Object[] { Integer.valueOf(team.getId()), Integer.valueOf(allList.size()), Integer.valueOf(list.size()) });
        return null;
      } 
      if (allList.size() <= 2) {
        int j = (new Random()).nextInt(result.size());
        Team team1 = result.get(j);
        this.logger.info("连续匹配同一个队伍=={}=={}", Integer.valueOf(team1.getId()), Integer.valueOf(team.getId()));
        return team1;
      } 
      ArrayList<Team> arrayList = new ArrayList<>();
      for (Team tempTeam : result) {
        if (tempTeam.getId() == team.getLastTeamId())
          continue; 
        arrayList.add(tempTeam);
      } 
      if (arrayList.size() <= 0) {
        this.logger.info("没有匹配到队伍=={}", Integer.valueOf(team.getId()));
        return null;
      } 
      if (arrayList.size() > 3) {
        Team team1 = ActivityGlobalFunc.getApproximate(team, arrayList);
        if (team1 != null)
          return team1; 
      } 
      int index = (new Random()).nextInt(result.size());
      Team last = result.get(index);
      this.logger.info("不连续匹配同一个队伍=={}=={}", Integer.valueOf(last.getId()), Integer.valueOf(team.getId()));
      return last;
    } catch (Exception e) {
      e.printStackTrace();
      this.logger.error("获取PK队伍错误------------------------------------------");
      return null;
    } 
  }

    public void doStartFight(final Team teamA, final Team teamB) {
      final int mapId = 4154;
    final int fightType = getFightType();
    EventsUtils.clashPlans(teamA.getList(), teamB.getList(), 4154, 10);
    SchedulerManager.getInstance().schedule(new Runnable() {
          public void run() {
            Fight fight = FightFactory.getFightFactory(fightType).create(mapId, new Object[] {fightType, teamA.getList(), teamB.getList() });
            fight.setMaxRound(30);
            fight.start();
          }
        }, 10000L);
  }
  
  protected int getFightType() {
    return FIGHT_TYPE.AT_WILL_PK.getValue();
  }
  
  public void handleFightEnd(FightEndEvent fightEndEvent) {
    ThreadLocalUtil.addLocalTask(fightEndEvent.getRole(), () -> doFightEnd(fightEndEvent.getRole(), fightEndEvent));
  }
  
  public void doFightEnd(Role role, FightEndEvent fightEndEvent) {
    try {
      if (this.teamService.isInTeam(role) && !this.teamService.isTeamLeader(role))
        return; 
      if (!isMatchFightType(fightEndEvent.getFightType()))
        return; 
      if (!role.getPos().getMapId().equals(Integer.valueOf(4154)))
        return; 
      AtWillData atWillData = this.atWillHandler.getAtWillData();
      Team team = this.teamService.getTeam(role.getRoleId().intValue());
      if (team == null)
        return; 
      AtWillPkData atWillPkData = atWillData.getPkDataList().stream().filter(e -> (e.getTeamId() == team.getId())).findFirst().orElse(null);
      if (atWillPkData != null) {
        atWillPkData.updateTeamMember(team, (byte)0);
      } else {
        this.logger.error("战斗结束后获取队伍ID信息失败");
      } 
      RespAtClashResult resp = new RespAtClashResult();
      resp.setMateSecond(this.atWillHandler.getAtWillSet().getPeriod());
      if (fightEndEvent.getStatus() == 1) {
        resp.setResult((byte)1);
        if (atWillData.getStage() == 2 || System.currentTimeMillis() >= atWillData.getEndTime().getTimeInMillis())
          resp.setMateSecond(-1); 
        for (int i = 0; i < team.getList().size(); i++) {
          Member member = team.getList().get(i);
          Role player = this.roleService.getPlayer(member.getId().longValue());
          if (player != null) {
            List<FiedValue> values = sendAward(player, (byte)1);
            resp.setList(values);
            MessagePusher.pushMessage(player, (Message)resp);
          } 
        } 
        doFightWin(role, fightEndEvent);
      } else {
        for (int i = 0; i < team.getList().size(); i++) {
          Member member = team.getList().get(i);
          Role player = this.roleService.getPlayer(member.getId().longValue());
          if (player != null) {
            List<FiedValue> values = sendAward(player, (byte)2);
            resp.setList(values);
            if (atWillData.getStage() == 2 || System.currentTimeMillis() >= atWillData.getEndTime().getTimeInMillis())
              resp.setMateSecond(-1); 
            MessagePusher.pushMessage(player, (Message)resp);
          } 
        } 
        doFightFail(role, fightEndEvent);
      } 
    } catch (Exception e) {
      e.printStackTrace();
      this.logger.error("战斗结束后操作错误-------------------------------------------------------");
    } 
  }
  
  private boolean isMatchFightType(int fightType) {
    return (getFightType() == fightType);
  }
  
  public void doFightWin(Role role, FightEndEvent fightEndEvent) {
    if (!isMatchFightType(fightEndEvent.getFightType()))
      return; 
    addScore(role);
    setPrevPkTime(role);
  }
  
  private void addScore(Role role) {
    Team team = this.teamService.getTeam(role.getRoleId());
    if (team != null) {
      AtWillData atWillData = this.atWillHandler.getAtWillData();
      AtWillPkData atWillPkData = atWillData.getPkDataList().stream().filter(e -> (e.getTeamId() == team.getId())).findFirst().orElse(null);
      if (atWillPkData != null) {
        short scorePkWin = this.atWillHandler.getAtWillSet().getScorePkWin();
        atWillPkData.addTotalScore(scorePkWin);
        String msg = MessageFormat.format("恭喜你取得了战斗胜利，获得了{0}点积分，你当前的积分为#<R>{1}#点。", new Object[] { Short.valueOf(scorePkWin), Short.valueOf(atWillPkData.getScore()) });
        this.teamService.pushMessageToTeam(team, (Message)new RespNotifyMiscEx(msg));
      } 
    } 
  }
  
  private void setPrevPkTime(Role role) {
    Team team = this.teamService.getTeam(role.getRoleId());
    if (team != null) {
      AtWillData atWillData = this.atWillHandler.getAtWillData();
      AtWillPkData atWillPkData = atWillData.getPkDataList().stream().filter(e -> (e.getTeamId() == team.getId())).findFirst().orElse(null);
      if (atWillPkData != null)
        atWillPkData.setPrevPKTime(System.currentTimeMillis()); 
    } 
  }
  
  public void doFightFail(Role role, FightEndEvent fightEndEvent) {
    fail(role, fightEndEvent);
  }
  
  public void fail(Role role, FightEndEvent fightEndEvent) {
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (team != null) {
      AtWillData atWillData = this.atWillHandler.getAtWillData();
      AtWillPkData atWillPkData = atWillData.getPkDataList().stream().filter(e -> (e.getTeamId() == team.getId())).findFirst().orElse(null);
      if (atWillPkData != null) {
        atWillPkData.substractPkValue((short)1);
        atWillPkData.subTotalScore();
        String msg = MessageFormat.format("真遗憾你失败了，失去了1点巅峰对决值。", new Object[] { Short.valueOf(atWillPkData.getPkValue()) });
        this.teamService.pushMessageToTeam(team, (Message)new RespNotifyMiscEx(msg));
        if (atWillPkData.getPkValue() <= 0) {
          this.atWillHandler.leaveMap(role);
          String outMsg = MessageFormat.format(" 真是精彩纷呈啊！#<Y>{0}#的队伍英雄争霸赛正在如火如荼的进行！适才在高手对决中棋差一着，遗憾地败下阵来，真是太可惜了。其他还在拼搏的高手要继续努力！", new Object[] { role.getName() });
          SpringUtils.getChatService().sendSystem(outMsg, "role");
        } 
      } 
      setPrevPkTime(role);
    } 
  }
  
  public void checkSystemMessage(Calendar calendar) {
    AtWillData atWillData = this.atWillHandler.getAtWillData();
    if (calendar.get(11) == atWillData.getStartTime().get(11) + 1 && calendar.get(12) % 5 == 0) {
      int intevalMinute = getIntevalMinute(atWillData.getStartTime(), calendar);
      String msg = MessageFormat.format("英雄争霸赛已经进行了#<R>{0}分钟#，大家加油啊！", new Object[] { Integer.valueOf(intevalMinute) });
      SpringUtils.getChatService().sendSystem(msg, "role");
    } 
    if (calendar.get(11) == atWillData.getStartTime().get(11) + 1 && calendar.get(12) % 10 == 0) {
      int intevalMinute = getIntevalMinute(calendar, atWillData.getEndTime());
      String msg = MessageFormat.format("英雄争霸赛还剩#<R>{0}分钟#，大家要抓紧时间啦！", new Object[] { Integer.valueOf(intevalMinute) });
      SpringUtils.getChatService().sendSystem(msg, "role");
    } 
  }
  
  private int getIntevalMinute(Calendar startCalendar, Calendar endCalendar) {
    return (int)Math.abs((endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis()) / 60000L);
  }
  
  private List<FiedValue> sendAward(Role role, byte type) {
    List<FiedValue> objects = new ArrayList<>();
    long exp = 1000000L;
    int silver = 500000;
    List<Param> list = new ArrayList<>();
    if (type == 2) {
      exp = 500000L;
      silver = 250000;
      list.add(new Param(ItemClassify.RESTYPE_SILVER.getId(), silver));
      list.add(new Param(Integer.valueOf(75052), Integer.valueOf(1), Integer.valueOf(75052)));
      list.add(new Param(Integer.valueOf(75029), Integer.valueOf(1), Integer.valueOf(75029)));
    } else {
      list.add(new Param(Integer.valueOf(ItemClassify.RESTYPE_SILVER.getId()), Integer.valueOf(silver)));
      list.add(new Param(Integer.valueOf(75052), Integer.valueOf(2), Integer.valueOf(75052)));
      list.add(new Param(Integer.valueOf(75029), Integer.valueOf(2), Integer.valueOf(75029)));
    } 
    objects.add(new FiedValue(String.valueOf(ItemClassify.HERO_EXP.getId()), (byte)4, String.valueOf(exp)));
    SpringUtils.getRoleService().addExp(role, exp, "", false);
    Integer combatPet = role.getExtendBox().getCombatPet();
    if (combatPet.intValue() > 0) {
      Pet pet = (Pet)SpringUtils.getPetService().selectPet(role).get(combatPet);
      objects.add(new FiedValue(String.valueOf(ItemClassify.PET_EXP.getId()), (byte)4, String.valueOf(exp)));
      SpringUtils.getPetService().addExp(role, pet, exp, false);
    } 
    for (Param param : list)
      objects.add(new FiedValue(String.valueOf(param.getK()), (byte)3, param.getV())); 
    ((BagService)SpringUtils.getBean(BagService.class)).sendReward(role, list, "英雄争霸赛-战斗" + type, 0);
    return objects;
  }
  
  private static long calculate(int grade, int rei, ItemClassify type) {
    if (type == ItemClassify.HERO_EXP)
      return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(1.6E7D * (1.0D + ((grade + rei * 15) / 140) * 2.5D) / 30.0D / 25.59D * (0.23D + Math.pow(1.0D, 0.35D)) * 1.3D)).longValue(); 
    if (type == ItemClassify.PET_EXP)
      return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(1.6E7D * (1.0D + ((grade + rei * 15) / 140) * 2.5D) / 30.0D / 25.59D * (0.23D + Math.pow(1.0D, 0.35D)) * 1.3D * 1.2D)).longValue(); 
    return 0L;
  }
}
