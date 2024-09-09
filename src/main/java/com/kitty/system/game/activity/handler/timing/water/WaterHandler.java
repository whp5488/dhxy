package com.kitty.system.game.activity.handler.timing.water;

import com.kitty.common.utils.DateUtils;
import java.util.Map;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.common.model.Pos;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.task.message.RespTaskIcon;
import com.kitty.system.game.activity.message.vo.WaterMatchResultInfo;
import com.kitty.system.game.activity.message.RespWaterEnd;
import com.kitty.system.game.activity.message.RespWaterStage;
import com.kitty.system.game.activity.model.user.lu.vo.WaterFight;
import java.util.stream.Collectors;
import com.kitty.system.game.activity.message.RespWaterIcon;
import java.util.ArrayList;
import com.kitty.system.game.activity.message.vo.MemberInfo;
import java.util.List;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.activity.model.user.lu.WaterPkData;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.common.utils.TimeUtil;
import java.text.MessageFormat;
import com.kitty.system.game.npc.service.NpcService;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.role.domain.Role;
import java.util.Iterator;
import com.kitty.system.game.activity.model.user.lu.WaterRankRecord;
import com.kitty.system.game.ranking.RankingList;
import com.kitty.system.game.service.AsyncService;
import com.kitty.common.utils.JsonUtils;
import org.nutz.json.Json;
import com.kitty.system.game.activity.model.user.lu.WaterEventSet;
import com.kitty.system.game.activity.ActivityDataPool;
import com.kitty.system.game.activity.model.Event;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.core.SchedulerManager;
import com.kitty.system.game.activity.common.EventsUtils;
import java.util.Date;
import java.util.Calendar;
import java.util.concurrent.ScheduledFuture;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.activity.model.user.lu.WaterData;
import com.kitty.system.game.activity.model.user.lu.WaterSet;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.chat.service.ChatService;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.common.thread.IdWorker;
import org.springframework.stereotype.Component;
import com.kitty.system.game.activity.service.ActivityHandler;

@Component
public class WaterHandler extends ActivityHandler
{
    @Autowired
    IdWorker worker;
    @Autowired
    protected RoleService roleService;
    @Autowired
    protected TeamService teamService;
    @Autowired
    ChatService chatService;
    @Autowired
    BagService bagService;
    private WaterSet waterSet;
    private WaterData waterData;
    @Autowired
    ServerService serverService;
    public static final int MAP = 3003;
    private static final int MAP_X = 60;
    private static final int MAP_Y = 35;
    private static final int EXIT_MAP = 1206;
    private static final int EXIT_X = 49;
    private static final int EXIT_Y = 22;
    private static final short TITLE_SERIAL = 10024;
    private ScheduledFuture scheduledFuture;
    private static final String ACTIVITY_NAME = "水陆大会";
    private byte round;
    private static final String TIME_FORMAT_PATTERN = "yyyy-MM-dd-HH:mm:ss";
    private static final String FIGHT_LEVEL_NOT_ENOUGH = "等级达到30级方可参加水陆大会。";
    private static final String NOT_SIGN_UP_TIME = "现在还不是报名时间，水陆大会下一场将于#<R>{0}#开始报名。";
    private static final String SIGN_UP_END = "水陆大会正在举行中不可报名参加";
    private static final String TEAM_COUNT_NOT_ENOUGH = "队伍不足三人无法参加水陆大会。";
    private static final String MEMBER_LEAVE_MOMENT = "#<R>{0}#暂离，无法参加水陆大会。";
    private static final String DISQUALIFICATION = "你取消了水陆大会的%s资格";
    private static final String DISQUALIFICATION_TEAM = "由于队伍少于%d人，无法参加水陆大会，请重新组满队伍参赛";
    private static final String REQUEST_JOIN_LIMIT = "当前地图无法申请";
    private static final String INVITE_JOIN_LIMIT = "当前地图无法邀请";
    private static final String ACCEPT_JOIN_LIMIT = "该地图无法操作申请队员";
    private static final String COMPETE_PK_LIMIT = "你现在不能与人切磋。";
    
    public WaterHandler() {
        this.round = 1;
    }
    
    public void prepareOnceWater() {
        this.initWaterData();
        this.addCheckJob();
        this.addStartJob();
        this.addEndJob();
        this.clearWaterRankRecord();
    }
    
    private void initWaterData() {
        WaterData waterData = new WaterData();
        this.initTime(waterData);
        this.waterData = waterData;
    }
    
    public void initTime(WaterData objData) {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(new Date());
        WaterSet waterSet = this.getWaterSet();
        String[] signUp = waterSet.getSignUpTime().split(":");
        calendarStart.set(11, (int)Integer.valueOf(signUp[0]));
        calendarStart.set(12, (int)Integer.valueOf(signUp[1]));
        calendarStart.set(13, 0);
        objData.setSignUpTime(EventsUtils.getTime(calendarStart));
        String[] start = waterSet.getStartTime().split(":");
        calendarStart.set(11, (int)Integer.valueOf(start[0]));
        calendarStart.set(12, (int)Integer.valueOf(start[1]));
        calendarStart.set(13, 0);
        objData.setStartTime(EventsUtils.getTime(calendarStart));
        calendarStart.add(12, waterSet.getSustain());
        objData.setEndTime(EventsUtils.getTime(calendarStart));
        int durationTime = (int)(objData.getStartTime().getTimeInMillis() / 1000L);
        objData.setPkDurationTime(durationTime);
    }
    
    private void addCheckJob() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.waterData.getStartTime().getTime());
        calendar.add(11, -1);
        long initialDelay = this.waterData.getStartTime().getTimeInMillis() - System.currentTimeMillis();
        this.logger.error("添加定时检测的定时任务==={}", Long.valueOf(initialDelay));
        try {
            if (this.scheduledFuture != null) {
                this.scheduledFuture.cancel(false);
            }
        }
        catch (Exception e) {
            this.logger.error("定时器关闭==", e);
        }
    }
    
    private void addStartJob() {
        long initialDelay = this.waterData.getStartTime().getTimeInMillis() - System.currentTimeMillis();
        this.logger.error("添加水陆大会开始的定时任务==={}", Long.valueOf(initialDelay));
        SchedulerManager.getInstance().schedule(() -> ((WaterPkHandler)SpringUtils.getBean(WaterPkHandler.class)).start(), initialDelay);
    }
    
    private void addEndJob() {
        long initialDelay = this.waterData.getEndTime().getTimeInMillis() - System.currentTimeMillis();
        this.logger.error("添加水陆大会结束的定时任务==={}", Long.valueOf(initialDelay));
        SchedulerManager.getInstance().schedule(() -> this.end(), initialDelay);
    }
    
    public void clearWaterRankRecord() {
        Event event = (Event)ActivityDataPool.eventMap.get(Integer.valueOf(3003));
        WaterEventSet eventSet;
        if (event == null) {
            event = new Event(Long.valueOf(this.worker.nextId()), 3003);
            eventSet = new WaterEventSet();
            event.setPara(JsonUtils.object2String(eventSet));
            ActivityDataPool.eventMap.put(Integer.valueOf(3003), event);
            event.save();
        }
        else {
            eventSet = (WaterEventSet)Json.fromJson(WaterEventSet.class, event.getPara());
        }
        if (this.round + 1 > eventSet.getMax()) {
            ((AsyncService)SpringUtils.getBean(AsyncService.class)).clearWaterTitle();
        }
        for (WaterRankRecord rankRecord : RankingList.WATER_RANK_RECORDS.values()) {
            if (rankRecord.getEquipScore() > 0 || rankRecord.getGrade() > 0 || this.round <= 1) {
                rankRecord.setEquipScore(0);
                rankRecord.setGrade(0);
                if (this.round <= 1) {
                    rankRecord.setScore(0);
                }
                rankRecord.save();
            }
            else {
                continue;
            }
        }
    }
    
    private void end() {
        ((WaterPkHandler)SpringUtils.getBean(WaterPkHandler.class)).end();
        if (this.scheduledFuture != null) {
            this.scheduledFuture.cancel((boolean)Boolean.FALSE);
        }
        Event event = (Event)ActivityDataPool.eventMap.get(Integer.valueOf(3003));
        WaterEventSet eventSet = (WaterEventSet)Json.fromJson(WaterEventSet.class, event.getPara());
        int round = eventSet.getRound() + 1;
        if (round > eventSet.getMax()) {
            round = 1;
            eventSet.setSeason(eventSet.getSeason() + 1);
        }
        eventSet.setRound((byte)round);
        event.setPara(JsonUtils.object2String(eventSet));
        event.save();
        if (round <= 1) {
            ((AsyncService)SpringUtils.getBean(AsyncService.class)).awardWaterSeason();
        }
        this.waterData = null;
    }
    
    public void compensate(Role role, int score) {
        WaterRankRecord record = (WaterRankRecord)RankingList.WATER_RANK_RECORDS.get(role.getId());
        if (record == null) {
            record = new WaterRankRecord(Long.valueOf(this.worker.nextId()), (long)role.getId());
        }
        if (score > 0) {
            record.setScore(record.getScore() + score);
            record.save();
        }
    }
    
    public void updateWaterRank(Role role, int score, int equip, int grade) {
        WaterRankRecord record = (WaterRankRecord)RankingList.WATER_RANK_RECORDS.get(role.getId());
        if (record == null) {
            record = new WaterRankRecord(Long.valueOf(this.worker.nextId()), (long)role.getId());
            RankingList.WATER_RANK_RECORDS.put(role.getId(), record);
        }
        if (score > 0) {
            record.setScore(record.getScore() + score);
        }
        if (equip > 0) {
            record.setEquipScore(equip);
        }
        if (grade > 0) {
            record.setGrade(grade);
        }
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (team != null) {
            record.setLeader(team.getLeaderUid());
        }
        record.save();
    }
    
    public void enterMap(Role role, Npc npc) {
        String talk = this.canEnter(role);
        if (talk != null) {
            if (!talk.isEmpty()) {
                ((NpcService)SpringUtils.getBean(NpcService.class)).sendNpcContent(role, npc, "", talk);
            }
        }
        else {
            this.doEnterMap(role);
        }
    }
    
    public String canEnter(Role role) {
        WaterData waterData = this.getWaterData();
        if (waterData == null) {
            MessagePusher.pushMessage(role, new RespMsg(EventsUtils.canEnterTips(this.waterSet.getDayOfWeek(), this.waterSet.getSignUpTime(), this.waterSet.getStartTime(), "水陆大会")));
            return "";
        }
        Calendar calendar = Calendar.getInstance();
        if (calendar.before(waterData.getSignUpTime())) {
            return MessageFormat.format("现在还不是报名时间，水陆大会下一场将于#<R>{0}#开始报名。", TimeUtil.formatDate(waterData.getSignUpTime().getTime(), "yyyy-MM-dd-HH:mm:ss"));
        }
        if ((int)role.getLevel() < this.waterSet.getMinLevel()) {
            return "等级达到30级方可参加水陆大会。";
        }
        if (calendar.after(waterData.getStartTime())) {
            return "水陆大会正在举行中不可报名参加";
        }
        int teamCount = this.teamService.getTeamCount(role);
        if (teamCount < this.waterSet.getMinTeamMemberCount()) {
            return "队伍不足三人无法参加水陆大会。";
        }
        Team team = this.teamService.getTeam((int)role.getRoleId());
        String names = this.teamService.getNotInTeam(team);
        if (names != null) {
            return MessageFormat.format("#<R>{0}#暂离，无法参加水陆大会。", names);
        }
        return null;
    }
    
    public void doEnterMap(Role role) {
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (team != null) {
            ReqTelePort reqTelePort = new ReqTelePort();
            reqTelePort.setMapId(3003);
            reqTelePort.setX(60);
            reqTelePort.setY(35);
            SpringUtils.getMapService().changeMap(role, reqTelePort, null);
            WaterPkData data = (WaterPkData)this.waterData.getPkDataList().stream().filter(e -> e.getTeamId() == team.getId()).findFirst().orElse(null);
            this.enter(data, team);
        }
    }
    
    public void enter(WaterPkData data, Team team) {
        if (data != null) {
            data.updateTeamMember(team);
        }
        else {
            data = new WaterPkData(team, this.waterSet.getPkValue());
            this.waterData.getPkDataList().add(data);
        }
        int sumGrade = 0;
        int sumEquipScore = 0;
        for (int i = 0; i < team.getList().size(); ++i) {
            Member member = (Member)team.getList().get(i);
            Role player = this.roleService.getPlayer((long)member.getId());
            if (player != null) {
                int grade = (int)player.getRelive() * 1000 + (int)player.getLevel();
                int equipScore = DataGlobalFunc.getEquipScore(SpringUtils.getEquipService().selectWearEquip(player));
                this.updateWaterRank(player, 0, equipScore, grade);
                sumGrade += grade;
                sumEquipScore += equipScore;
                this.sendRespIcon(player);
            }
        }
        data.setMemberGrade(sumGrade);
        data.setMemberEquipScore(sumEquipScore);
    }
    
    public List<MemberInfo> fightMember(List<Long> members, long leader) {
        List<MemberInfo> list = new ArrayList(5);
        Role role = this.roleService.getPlayer(leader);
        if (role != null) {
            this.setWaterRankRecord(list, role);
        }
        for (Long id : members) {
            if (!id.equals(Long.valueOf(leader))) {
                Role player = this.roleService.getPlayer((long)id);
                if (player != null) {
                    this.setWaterRankRecord(list, player);
                }
                else {
                    continue;
                }
            }
            else {
                continue;
            }
        }
        return list;
    }
    
    public void setWaterRankRecord(List<MemberInfo> list, Role role) {
        WaterRankRecord record = (WaterRankRecord)RankingList.WATER_RANK_RECORDS.get(role.getId());
        if (record == null) {
            record = new WaterRankRecord();
        }
        list.add(new MemberInfo(role.getName(), role.getRelive().byteValue(), role.getLevel().shortValue(), (int)role.getSerial(), record.getScore()));
    }
    
    public void sendRespIcon(Role role) {
        if (this.waterData != null) {
            MessagePusher.pushMessage(role, this.getRespIcon(role));
        }
    }
    
    public RespWaterIcon getRespIcon(Role role) {
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (team == null) {
            return null;
        }
        WaterPkData data = (WaterPkData)this.waterData.getPkDataList().stream().filter(e -> e.getTeamId() == team.getId()).findFirst().orElse(null);
        if (data == null) {
            return null;
        }
        RespWaterIcon waterIcon = new RespWaterIcon();
        waterIcon.setRound(this.round);
        switch (this.waterData.getStage()) {
            case 0: {
                waterIcon.setState((byte)0);
                waterIcon.setTime(Math.toIntExact(this.waterData.getStartTime().getTimeInMillis() / 1000L));
                break;
            }
            case 1: {
                byte state = 1;
                if (data.getFight().size() > 0) {
                    List<WaterFight> list = (List)data.getFight().stream().filter(e -> e.getState() == 0).collect(Collectors.toList());
                    if (list.size() <= 0) {
                        state = 3;
                    }
                }
                if (data.getTime() > 0) {
                    state = 2;
                    waterIcon.setTime(data.getTime());
                }
                WaterFight minNotFight = EventsUtils.getMinNotFight(data);
                if (minNotFight != null) {
                    waterIcon.setCurrentSession(minNotFight.getSession());
                }
                waterIcon.setState(state);
                waterIcon.setRank(data.getRank());
                break;
            }
        }
        return waterIcon;
    }
    
    public void waterInfo(Role role) {
        if (this.waterData == null || this.waterData.getStage() == 2) {
            return;
        }
        if (!this.isInMap(role)) {
            return;
        }
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (team == null) {
            return;
        }
        WaterPkData data = (WaterPkData)this.waterData.getPkDataList().stream().filter(e -> e.getTeamId() == team.getId()).findFirst().orElse(null);
        if (data == null) {
            return;
        }
        switch (this.waterData.getStage()) {
            case 0: {
                RespWaterStage waterStage = new RespWaterStage();
                waterStage.setRound(this.round);
                Role leader = this.teamService.getTeamLeader(data.getTeamId());
                waterStage.setItself(this.fightMember(data.getMembers(), (long)leader.getId()));
                waterStage.setState((byte)0);
                waterStage.setTime(Math.toIntExact(this.waterData.getStartTime().getTimeInMillis() / 1000L));
                MessagePusher.pushMessage(role, waterStage);
                break;
            }
            case 1: {
                List<WaterFight> list = (List)data.getFight().stream().filter(e -> e.getState() == 0).collect(Collectors.toList());
                if (list == null || list.size() <= 0) {
                    RespWaterEnd waterEnd = new RespWaterEnd();
                    waterEnd.setRound(this.round);
                    waterEnd.setRank(data.getRank());
                    List<Integer> itself = new ArrayList(5);
                    for (Long id : data.getMembers()) {
                        Role player = this.roleService.getPlayer((long)id);
                        if (player != null) {
                            itself.add(player.getSerial());
                        }
                        else {
                            continue;
                        }
                    }
                    waterEnd.setItself(itself);
                    List<WaterMatchResultInfo> matchResult = new ArrayList(3);
                    int score = 0;
                    int feats = 0;
                    for (WaterFight waterFight : data.getFight()) {
                        List<Integer> members = new ArrayList(5);
                        for (Long id2 : waterFight.getMembers()) {
                            Role player2 = this.roleService.getPlayer((long)id2);
                            if (player2 != null) {
                                members.add(player2.getSerial());
                            }
                            else {
                                continue;
                            }
                        }
                        matchResult.add(new WaterMatchResultInfo(members, waterFight.getState()));
                        if (waterFight.getState() == 1) {
                            score += this.waterSet.getScore() + this.waterSet.getRankExtraScore().getInt((int)data.getRank() + "_SCORE", 0);
                            feats += this.waterSet.getFeats() + this.waterSet.getRankExtraScore().getInt((int)data.getRank() + "_FEATS", 0);
                        }
                        else {
                            continue;
                        }
                    }
                    waterEnd.setMatchResult(matchResult);
                    waterEnd.setScore(score);
                    waterEnd.setFeats(feats);
                    MessagePusher.pushMessage(role, waterEnd);
                    break;
                }
                byte state = 1;
                RespWaterStage respWaterStage = new RespWaterStage();
                respWaterStage.setRound(this.round);
                respWaterStage.setItself(this.fightMember(data.getMembers(), (long)this.teamService.getTeamLeader(data.getTeamId()).getId()));
                WaterFight minNotFight = EventsUtils.getMinNotFight(data);
                respWaterStage.setCurrentSession(minNotFight.getSession());
                long enemyLeaderId = 0L;
                Role teamLeader = this.teamService.getTeamLeader(minNotFight.getTeamId());
                if (teamLeader == null) {
                    enemyLeaderId = (long)minNotFight.getMembers().get(0);
                }
                else {
                    enemyLeaderId = (long)teamLeader.getId();
                }
                respWaterStage.setEnemy(this.fightMember(minNotFight.getMembers(), enemyLeaderId));
                if (data.getTime() > 0) {
                    state = 2;
                    respWaterStage.setTime(data.getTime());
                }
                respWaterStage.setState(state);
                MessagePusher.pushMessage(role, respWaterStage);
                break;
            }
        }
        return;
    }
    
    public void leave(Role role) {
        if (role != null) {
            Team team = this.teamService.getTeam((int)role.getRoleId());
            if (team != null && team.getLeaderUid() == (long)role.getId()) {
                this.doAfterLeaveTeam(role, team);
            }
            else {
                this.leaveMap(role);
            }
        }
    }
    
    public void leaveMap(Role role) {
        if (role == null) {
            return;
        }
        if ((int)role.getPos().getMapId() != 3003) {
            return;
        }
        role.pushTempCache("not_check_change_map", Boolean.valueOf(true));
        ReqTelePort reqTelePort = new ReqTelePort();
        reqTelePort.setMapId(1206);
        reqTelePort.setX(49);
        reqTelePort.setY(22);
        SpringUtils.getMapService().changeMap(role, reqTelePort, null);
        RespTaskIcon respTaskIcon = new RespTaskIcon(3003, "lu", (byte)0);
        MessagePusher.pushMessage(role, respTaskIcon);
        return;
    }
    
    public boolean isRequestJoinTeam(Role role, Team team) {
        if (this.isInMap(role)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("当前地图无法申请"));
            return false;
        }
        Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
        if (role == null || leaderRole == null) {
            return false;
        }
        if (this.isInMap(leaderRole)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("当前地图无法申请"));
            return false;
        }
        return true;
    }
    
    public boolean isInviteJoinTeam(Role role, int inviteRoleId) {
        if (this.isInMap(role)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("当前地图无法邀请"));
            return false;
        }
        Role invitRole = this.roleService.getPlayer(inviteRoleId);
        if (invitRole == null) {
            MessagePusher.pushMessage(role, new RespMsg("对方所在地图无法接受邀请哦。"));
            return false;
        }
        if (this.isInMap(invitRole)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("当前地图无法邀请"));
            return false;
        }
        return true;
    }
    
    public boolean isAcceptJoinTeam(Role role, int joinRoleId) {
        if (this.isInMap(role)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("该地图无法操作申请队员"));
            return false;
        }
        Role joinRole = this.roleService.getOnlinePlayer(joinRoleId);
        if (this.isInMap(joinRole)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("该地图无法操作申请队员"));
            return false;
        }
        return true;
    }
    
    public boolean isBackToTeam(Team team, Role memberRole) {
        Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
        if (this.isInMap(leaderRole) && this.waterData != null && this.waterData.getStage() == 1) {
            Pos pos = leaderRole.getPos();
            MessagePusher.pushMessage(memberRole, new RespMsg(String.format("队长现在位于水陆比武场 (%d，%d) ，暂时无法归队。", pos.getX(), pos.getY())));
            return false;
        }
        return true;
    }
    
    @Override
    public void handleTeamLeaveMoment(Role role) {
        this.upTeamLeave(role, this.teamService.getTeam((int)role.getRoleId()));
        super.handleTeamLeaveMoment(role);
    }
    
    @Override
    public void handleTeamLeave(Role role, boolean isLeader, Team team) {
        this.upTeamLeave(role, team);
        super.handleTeamLeave(role, isLeader, team);
    }
    
    public void upTeamLeave(Role role, Team team) {
        if (this.isInMap(role)) {
            boolean flag = false;
            this.leaveMap(role);
            if (this.waterData != null && this.waterData.getStage() == 0) {
                flag = true;
                MessagePusher.pushMessage(role, new RespMsg(String.format("你取消了水陆大会的%s资格", "准备")));
            }
            if (flag) {
                if (team != null && this.teamService.getTeamCount(team) < this.waterSet.getMinTeamMemberCount()) {
                    this.doAfterLeaveTeam(role, team);
                    RespMsg respMsg = new RespMsg(String.format("由于队伍少于%d人，无法参加水陆大会，请重新组满队伍参赛", Integer.valueOf(this.waterSet.getMinTeamMemberCount())));
                    this.teamService.memberHandleThreadLocal(team, memberRole -> MessagePusher.pushMessage(memberRole, respMsg));
                }
                else if (team != null && this.waterData != null && this.waterData.getStage() == 0) {
                    WaterPkData data = (WaterPkData)this.waterData.getPkDataList().stream().filter(e -> e.getTeamId() == team.getId()).findFirst().orElse(null);
                    if (data != null) {
                        this.enter(data, team);
                    }
                }
            }
        }
    }
    
    @Override
    public void handleLogin(Role role) {
        boolean inMap = this.isInMap(role);
        boolean flag = false;
        if (this.waterData == null || System.currentTimeMillis() >= this.waterData.getEndTime().getTimeInMillis()) {
            flag = true;
        }
        if (inMap && flag) {
            this.leaveMap(role);
        }
        else if (this.waterData != null) {
            boolean is = true;
            for (WaterPkData waterPkData : this.waterData.getPkDataList()) {
                if (waterPkData.getMembers().contains(role.getId())) {
                    is = false;
                    break;
                }
                continue;
            }
            if (is) {
                this.leaveMap(role);
            }
            else {
                this.sendRespIcon(role);
            }
        }
        super.handleLogin(role);
    }
    
    @Override
    public void handleFightEnd(FightEndEvent fightEndEvent) {
    }
    
    public boolean isInMap(Role role) {
        return role != null && DataGlobalFunc.isInMap(role, 3003);
    }
    
    public void doAfterLeaveTeam(Role role, Team team) {
        try {
            if (team != null) {
                Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
                if (leaderRole != null && (leaderRole.getId().equals(role.getId()) || this.teamService.getTeamCount(team) < this.waterSet.getMinTeamMemberCount())) {
                    for (Member member : team.getList()) {
                        Role player = this.roleService.getPlayer((long)member.getId());
                        if (player != null) {
                            player.pushTempCache("not_check_change_map", Boolean.valueOf(true));
                            MessagePusher.pushMessage(player, new RespTaskIcon(3003, "lu", (byte)0));
                        }
                        else {
                            continue;
                        }
                    }
                    this.leaveMap(leaderRole);
                    return;
                }
            }
            this.leaveMap(role);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean isCompetePk(Role role, Role pkRole) {
        if (this.isInMap(role)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("你现在不能与人切磋。"));
            return false;
        }
        if (this.isInMap(pkRole)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("你现在不能与人切磋。"));
            return false;
        }
        return true;
    }
    
    public void loadCommonSet(Map<String, String> commonSetMap) {
        this.loadWaterSet(commonSetMap);
    }
    
    private void loadWaterSet(Map<String, String> commonSetMap) {
        String value = (String)commonSetMap.get("lu_team_set");
        WaterSet waterSet = null;
        if (value != null) {
            waterSet = (WaterSet)Json.fromJson(WaterSet.class, value);
        }
        else {
            waterSet = new WaterSet();
        }
        waterSet.initRankExtraScore();
        this.waterSet = waterSet;
    }
    
    public void notifyAt() {
        String day = DateUtils.dayForWeek(new Date());
        if (!this.waterSet.getDayOfWeek().contains(day)) {
            return;
        }
        Event event = (Event)ActivityDataPool.eventMap.get(Integer.valueOf(3003));
        WaterEventSet eventSet;
        if (event == null) {
            eventSet = new WaterEventSet();
        }
        else {
            eventSet = (WaterEventSet)Json.fromJson(WaterEventSet.class, event.getPara());
        }
        this.round = eventSet.getRound();
        this.prepareOnceWater();
        SchedulerManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                String notice = "万众瞩目的水陆大会将于二十分钟后正式开始请各路英雄组好队做好准备找魏征进场吧";
                WaterHandler.this.chatService.sendAdnotice(notice);
            }
        }, 30000L);
        return;
    }
    
    public void cs() {
        Event event = (Event)ActivityDataPool.eventMap.get(Integer.valueOf(3003));
        WaterEventSet eventSet;
        if (event == null) {
            eventSet = new WaterEventSet();
        }
        else {
            eventSet = (WaterEventSet)Json.fromJson(WaterEventSet.class, event.getPara());
        }
        this.round = eventSet.getRound();
        this.waterData = new WaterData();
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(new Date());
        WaterSet waterSet = this.getWaterSet();
        calendarStart.add(12, 1);
        this.waterData.setSignUpTime(EventsUtils.getTime(calendarStart));
        calendarStart.add(12, 1);
        this.waterData.setStartTime(EventsUtils.getTime(calendarStart));
        calendarStart.add(12, waterSet.getSustain());
        this.waterData.setEndTime(EventsUtils.getTime(calendarStart));
        int durationTime = (int)(this.waterData.getStartTime().getTimeInMillis() / 1000L);
        this.waterData.setPkDurationTime(durationTime);
        this.addCheckJob();
        this.addStartJob();
        this.addEndJob();
        this.clearWaterRankRecord();
    }
    
    public WaterSet getWaterSet() {
        return this.waterSet;
    }
    
    public WaterData getWaterData() {
        return this.waterData;
    }
    
    public void setWaterData(final WaterData waterData) {
        this.waterData = waterData;
    }
}
