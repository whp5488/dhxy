
package com.kitty.system.game.activity.handler.timing.ca;

import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.RoleLevelUpEvent;
import com.kitty.common.utils.DateUtils;
import java.util.Map;
import com.kitty.listener.event.RoleChangeEvent;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.common.model.Pos;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.task.message.RespTaskIcon;
import com.kitty.system.game.activity.message.RespCaInfo;
import com.kitty.system.game.activity.message.RespCaIcon;
import java.util.ArrayList;
import com.kitty.system.game.activity.message.vo.MemberInfo;
import com.kitty.system.common.data.DataGlobalFunc;
import java.util.List;
import com.kitty.system.game.activity.model.user.ca.CaPkData;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.team.domain.Member;
import java.util.Collection;
import java.util.Collections;
import com.kitty.system.game.team.domain.Team;
import java.text.MessageFormat;
import com.kitty.common.utils.TimeUtil;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.npc.service.NpcService;
import com.kitty.system.game.vo.Npc;
import com.kitty.cache.JedisUtil;
import com.kitty.system.game.activity.model.user.ca.vo.CaCache;
import com.kitty.system.game.role.domain.Role;
import java.util.Iterator;
import com.kitty.system.game.activity.model.user.ca.CaRankRecord;
import com.kitty.system.game.ranking.RankingList;
import com.kitty.system.game.service.AsyncService;
import org.nutz.json.Json;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.activity.model.user.ca.CaEventSet;
import com.kitty.system.game.activity.ActivityDataPool;
import com.kitty.system.game.activity.model.Event;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.core.SchedulerManager;
import com.kitty.system.game.activity.common.EventsUtils;
import java.util.Date;
import java.util.Calendar;
import java.util.concurrent.ScheduledFuture;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.activity.model.user.ca.CaData;
import com.kitty.system.game.activity.model.user.ca.CaSet;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.chat.service.ChatService;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.common.thread.IdWorker;
import org.springframework.stereotype.Component;
import com.kitty.system.game.activity.service.ActivityHandler;

@Component
public class CaHandler extends ActivityHandler
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
    private CaSet caSet;
    private CaData caData;
    @Autowired
    ServerService serverService;
    public static final int MAP = 3005;
    private static final int MAP_X = 107;
    private static final int MAP_Y = 61;
    private static final int EXIT_MAP = 1011;
    private static final int EXIT_X = 124;
    private static final int EXIT_Y = 67;
    private ScheduledFuture scheduledFuture;
    private static final String ACTIVITY_NAME = "决战长安";
    private byte round;
    public long needTime;
    private static final String TIME_FORMAT_PATTERN = "yyyy-MM-dd-HH:mm:ss";
    private static final String FIGHT_LEVEL_NOT_ENOUGH = "等级达到60级方可参加决战长安。";
    private static final String NOT_SIGN_UP_TIME = "现在还不是报名时间，决战长安下一场将于#<R>{0}#开始报名。";
    private static final String SIGN_UP_END = "决战长安正在举行中不可报名参加";
    private static final String TEAM_COUNT_NOT_ENOUGH = "少侠组决战长安。";
    private static final String MEMBER_LEAVE_MOMENT = "#<R>{0}#暂离，无法参加决战长安人以上队伍方可前来参赛～";
    private static final String DISQUALIFICATION = "你取消了决战长安的%s资格";
    private static final String DISQUALIFICATION_TEAM = "由于队伍少于%d人，无法参加决战长安，请重新组满队伍参赛";
    private static final String REQUEST_JOIN_LIMIT = "当前地图无法申请";
    private static final String INVITE_JOIN_LIMIT = "当前地图无法邀请";
    private static final String ACCEPT_JOIN_LIMIT = "该地图无法操作申请队员";
    private static final String COMPETE_PK_LIMIT = "你现在不能与人切磋。";
    
    public CaHandler() {
        this.round = 1;
        this.needTime = 0L;
    }
    
    public void prepareOnce() {
        this.initData();
        this.addCheckJob();
        this.addStartJob();
        this.addEndJob();
        this.clearRankRecord();
    }
    
    private void initData() {
        final CaData obj = new CaData();
        this.initTime(obj);
        this.caData = obj;
    }
    
    public void initTime(final CaData objData) {
        final Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(new Date());
        final CaSet caSet = this.getCaSet();
        final String[] signUp = caSet.getSignUpTime().split(":");
        calendarStart.set(11, Integer.valueOf(signUp[0]));
        calendarStart.set(12, Integer.valueOf(signUp[1]));
        calendarStart.set(13, 0);
        objData.setSignUpTime(EventsUtils.getTime(calendarStart));
        final String[] start = caSet.getStartTime().split(":");
        calendarStart.set(11, Integer.valueOf(start[0]));
        calendarStart.set(12, Integer.valueOf(start[1]));
        calendarStart.set(13, 0);
        objData.setStartTime(EventsUtils.getTime(calendarStart));
        calendarStart.add(12, caSet.getSustain());
        objData.setEndTime(EventsUtils.getTime(calendarStart));
        final int durationTime = (int)(objData.getStartTime().getTimeInMillis() / 1000L);
        objData.setPkDurationTime(durationTime);
    }
    
    private void addCheckJob() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.caData.getStartTime().getTime());
        calendar.add(11, -1);
        final long initialDelay = this.caData.getStartTime().getTimeInMillis() - System.currentTimeMillis();
        this.logger.error("添加定时检测的定时任务==={}", initialDelay);
        try {
            if (this.scheduledFuture != null) {
                this.scheduledFuture.cancel(false);
            }
        }
        catch (final Exception e) {
            this.logger.error("定时器关闭==", e);
        }
    }
    
    private void addStartJob() {
        final long initialDelay = this.caData.getStartTime().getTimeInMillis() - System.currentTimeMillis();
        this.logger.error("添加决战长安开始的定时任务==={}", initialDelay);
        SchedulerManager.getInstance().schedule(() -> SpringUtils.getBean(CaPkHandler.class).start(), initialDelay);
    }
    
    private void addEndJob() {
        final long initialDelay = this.caData.getEndTime().getTimeInMillis() - System.currentTimeMillis();
        this.logger.error("添加决战长安结束的定时任务==={}", initialDelay);
        SchedulerManager.getInstance().schedule(() -> this.end(), initialDelay);
    }
    
    public void clearRankRecord() {
        Event event = ActivityDataPool.eventMap.get(3002);
        CaEventSet eventSet;
        if (event == null) {
            event = new Event(this.worker.nextId(), 3002);
            eventSet = new CaEventSet();
            event.setPara(JsonUtils.object2String(eventSet));
            ActivityDataPool.eventMap.put(3002, event);
            event.save();
        }
        else {
            eventSet = (CaEventSet)Json.fromJson(CaEventSet.class, (CharSequence)event.getPara());
        }
        if (this.round + 1 > eventSet.getMax()) {
            SpringUtils.getBean(AsyncService.class).clearCaTitle();
        }
        for (final CaRankRecord rankRecord : RankingList.CA_RANK_RECORDS.values()) {
            if (this.round <= 1) {
                rankRecord.setScore(1000);
            }
            rankRecord.save();
        }
    }
    
    private void end() {
        SpringUtils.getBean(CaPkHandler.class).end();
        if (this.scheduledFuture != null) {
            this.scheduledFuture.cancel(Boolean.FALSE);
        }
        SpringUtils.getBean(AsyncService.class).syncCaRan();
        final Event event = ActivityDataPool.eventMap.get(3002);
        final CaEventSet eventSet = (CaEventSet)Json.fromJson(CaEventSet.class, (CharSequence)event.getPara());
        int round = eventSet.getRound() + 1;
        if (round > eventSet.getMax()) {
            round = 1;
            eventSet.setSeason(eventSet.getSeason() + 1);
        }
        eventSet.setRound((byte)round);
        event.setPara(JsonUtils.object2String(eventSet));
        event.save();
        if (round <= 1) {
            SpringUtils.getBean(AsyncService.class).awardCaSeason();
        }
        this.caData = null;
    }
    
    public void compensate(final Role role, final int score) {
        CaRankRecord record = RankingList.CA_RANK_RECORDS.get(role.getId());
        if (record == null) {
            RankingList.CA_RANK_RECORDS.put(role.getId(), new CaRankRecord(this.worker.nextId(), role.getId(), role.getRoleId(), role.getSerial(), role.getName()));
            record = RankingList.CA_RANK_RECORDS.get(role.getId());
        }
        final int newScore = record.getScore() + score;
        record.setScore((newScore < 0) ? 0 : newScore);
        record.save();
        final CaCache caCache = new CaCache();
        caCache.setId(role.getId());
        caCache.setRoleId(role.getRoleId());
        caCache.setSerial(role.getSerial());
        caCache.setName(role.getName());
        caCache.setScore(record.getScore());
        caCache.setGrade(role.getRelive() * 1000 + role.getLevel());
        caCache.setFactionName(this.roleService.getFactionName(role));
        caCache.setMarking(role.getMarking());
        JedisUtil.set("jzca:" + this.serverService.getMarking() + "_" + role.getId(), JsonUtils.object2String(caCache));
    }
    
    public void updateWaterRank(final Role role, final int score, final int equip, final int grade) {
        CaRankRecord record = RankingList.CA_RANK_RECORDS.get(role.getId());
        if (record == null) {
            RankingList.CA_RANK_RECORDS.put(role.getId(), new CaRankRecord(this.worker.nextId(), role.getId(), role.getRoleId(), role.getSerial(), role.getName()));
            record = RankingList.CA_RANK_RECORDS.get(role.getId());
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
        record.save();
    }
    
    public void enterMap(final Role role, final Npc npc) {
        final String talk = this.canEnter(role);
        if (talk != null) {
            if (!talk.isEmpty()) {
                SpringUtils.getBean(NpcService.class).sendNpcContent(role, npc, "", talk);
            }
        }
        else {
            this.doEnterMap(role);
        }
    }
    
    public String canEnter(final Role role) {
        final CaData obj = this.getCaData();
        if (obj == null) {
            MessagePusher.pushMessage(role, new RespMsg(EventsUtils.canEnterTips(this.caSet.getDayOfWeek(), this.caSet.getSignUpTime(), this.caSet.getStartTime(), "决战长安")));
            return "";
        }
        final Calendar calendar = Calendar.getInstance();
        if (calendar.before(obj.getSignUpTime())) {
            return MessageFormat.format("现在还不是报名时间，决战长安下一场将于#<R>{0}#开始报名。", TimeUtil.formatDate(obj.getSignUpTime().getTime(), "yyyy-MM-dd-HH:mm:ss"));
        }
        if (role.getLevel() < this.caSet.getMinLevel()) {
            return "等级达到60级方可参加决战长安。";
        }
        if (calendar.after(obj.getStartTime())) {
            return "决战长安正在举行中不可报名参加";
        }
        final int teamCount = this.teamService.getTeamCount(role);
        if (teamCount < this.caSet.getMinTeamMemberCount()) {
            return "少侠组决战长安。";
        }
        final Team team = this.teamService.getTeam(role.getRoleId());
        final String names = this.teamService.getNotInTeam(team);
        if (names != null) {
            return MessageFormat.format("#<R>{0}#暂离，无法参加决战长安人以上队伍方可前来参赛～", names);
        }
        return null;
    }
    
    public void doEnterMap(final Role role) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null) {
            final List<Integer> memberGrade = this.teamService.getMemberGrade(this.roleService.getPlayer(team.getLeaderUid()));
            if (memberGrade == null || memberGrade.size() <= 0) {
                this.logger.error("决战长安进入地图获取队伍最大等级出错：{}", (Object)memberGrade);
                return;
            }
            final Integer max = Collections.max((Collection<? extends Integer>)memberGrade);
            final int group = this.getGroup(max / 1000, max % 1000);
            for (int i = 0; i < team.getList().size(); ++i) {
                final Member member = team.getList().get(i);
                final Role player = this.roleService.getPlayer(member.getId());
                if (player != null) {
                    player.setVisible(group);
                }
            }
            final ReqTelePort reqTelePort = new ReqTelePort();
            reqTelePort.setMapId(3005);
            reqTelePort.setX(107);
            reqTelePort.setY(61);
            SpringUtils.getMapService().changeMap(role, reqTelePort, null);
            final CaPkData data = this.caData.getPkDataList().stream().filter(e -> e.getTeamId() == team.getId()).findFirst().orElse(null);
            this.enter(data, team);
        }
    }
    
    public void enter(CaPkData data, final Team team) {
        if (data != null) {
            data.updateTeamMember(team);
        }
        else {
            data = new CaPkData(team, this.caSet.getPkValue());
            this.caData.getPkDataList().add(data);
        }
        final List<Integer> memberGrade = this.teamService.getMemberGrade(this.roleService.getPlayer(team.getLeaderUid()));
        if (memberGrade == null || memberGrade.size() <= 0) {
            this.logger.error("决战长安进入地图获取队伍最大等级出错：{}", (Object)memberGrade);
            return;
        }
        short sumScore = 0;
        int sumGrade = 0;
        int sumEquipScore = 0;
        final Integer max = Collections.max((Collection<? extends Integer>)memberGrade);
        final int group = this.getGroup(max / 1000, max % 1000);
        for (int i = 0; i < team.getList().size(); ++i) {
            final Member member = team.getList().get(i);
            final Role player = this.roleService.getPlayer(member.getId());
            if (player != null) {
                final int grade = player.getRelive() * 1000 + player.getLevel();
                final int equipScore = DataGlobalFunc.getEquipScore(SpringUtils.getEquipService().selectWearEquip(player));
                this.updateWaterRank(player, 0, equipScore, grade);
                final CaRankRecord record = RankingList.CA_RANK_RECORDS.get(player.getId());
                sumScore += (short)record.getScore();
                sumGrade += grade;
                sumEquipScore += equipScore;
                this.sendRespIcon(player);
                player.setVisible(group);
            }
        }
        data.setGroup((short)group);
        data.setScore(sumScore);
        data.setMemberGrade(sumGrade);
        data.setMemberEquipScore(sumEquipScore);
    }
    
    public List<MemberInfo> fightMember(final List<Long> members, final long leader) {
        final List<MemberInfo> list = new ArrayList<MemberInfo>(5);
        final Role role = this.roleService.getPlayer(leader);
        if (role != null) {
            this.setRankRecord(list, role);
        }
        for (final Long id : members) {
            if (!id.equals(leader)) {
                final Role player = this.roleService.getPlayer(id);
                if (player == null) {
                    continue;
                }
                this.setRankRecord(list, player);
            }
        }
        return list;
    }
    
    public void setRankRecord(final List<MemberInfo> list, final Role role) {
        CaRankRecord record = RankingList.CA_RANK_RECORDS.get(role.getId());
        if (record == null) {
            record = new CaRankRecord();
        }
        list.add(new MemberInfo(role.getName(), role.getRelive().byteValue(), role.getLevel().shortValue(), role.getSerial(), record.getScore()));
    }
    
    public void sendRespIcon(final Role role) {
        if (this.caData != null) {
            MessagePusher.pushMessage(role, this.getRespIcon(role));
        }
    }
    
    public RespCaIcon getRespIcon(final Role role) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team == null) {
            return null;
        }
        final CaPkData data = this.caData.getPkDataList().stream().filter(e -> e.getTeamId() == team.getId()).findFirst().orElse(null);
        if (data == null) {
            return null;
        }
        final RespCaIcon caIcon = new RespCaIcon();
        caIcon.setRound(this.round);
        switch (this.caData.getStage()) {
            case 0: {
                caIcon.setCurrentSession((byte)0);
                caIcon.setState((byte)0);
                caIcon.setTime(Math.toIntExact(this.caData.getStartTime().getTimeInMillis() / 1000L));
                break;
            }
            case 1: {
                byte state = 1;
                if (SpringUtils.getBean(CaPkHandler.class).session > 3) {
                    state = 3;
                }
                if (this.needTime > 0L) {
                    caIcon.setTime(Math.toIntExact(this.needTime / 1000L));
                }
                caIcon.setState(state);
                caIcon.setCurrentSession((byte)Math.max(1, SpringUtils.getBean(CaPkHandler.class).session - 1));
                break;
            }
        }
        return caIcon;
    }
    
    public void caInfo(final Role role) {
        if (this.caData == null || this.caData.getStage() == 2) {
            return;
        }
        if (!this.isInMap(role)) {
            return;
        }
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team == null) {
            return;
        }
        final CaPkData data = this.caData.getPkDataList().stream().filter(e -> e.getTeamId() == team.getId()).findFirst().orElse(null);
        if (data == null) {
            return;
        }
        final RespCaInfo caInfo = new RespCaInfo();
        caInfo.setRound(this.round);
        caInfo.setGroup((byte)data.getGroup());
        final Role leader = this.teamService.getTeamLeader(data.getTeamId());
        caInfo.setItself(this.fightMember(data.getMembers(), leader.getId()));
        if (this.caData.getStage() == 1) {
            caInfo.setCurrentSession((byte)(SpringUtils.getBean(CaPkHandler.class).session - 1));
        }
        MessagePusher.pushMessage(role, caInfo);
    }
    
    public void leave(final Role role) {
        if (role != null) {
            final Team team = this.teamService.getTeam(role.getRoleId());
            if (team != null && team.getLeaderUid() == role.getId()) {
                this.doAfterLeaveTeam(role, team);
            }
            else {
                this.leaveMap(role);
            }
        }
    }
    
    public void leaveMap(final Role role) {
        if (role == null) {
            return;
        }
        if (role.getPos().getMapId() != 3005) {
            return;
        }
        role.pushTempCache("not_check_change_map", true);
        final ReqTelePort reqTelePort = new ReqTelePort();
        reqTelePort.setMapId(1011);
        reqTelePort.setX(124);
        reqTelePort.setY(67);
        SpringUtils.getMapService().changeMap(role, reqTelePort, null);
        final RespTaskIcon respTaskIcon = new RespTaskIcon(3002, "ca", (byte)0);
        MessagePusher.pushMessage(role, respTaskIcon);
    }
    
    public boolean isRequestJoinTeam(final Role role, final Team team) {
        if (this.isInMap(role)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("当前地图无法申请"));
            return false;
        }
        final Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
        if (role == null || leaderRole == null) {
            return false;
        }
        if (this.isInMap(leaderRole)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("当前地图无法申请"));
            return false;
        }
        return true;
    }
    
    public boolean isInviteJoinTeam(final Role role, final int inviteRoleId) {
        if (this.isInMap(role)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("当前地图无法邀请"));
            return false;
        }
        final Role invitRole = this.roleService.getPlayer(inviteRoleId);
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
    
    public boolean isAcceptJoinTeam(final Role role, final int joinRoleId) {
        if (this.isInMap(role)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("该地图无法操作申请队员"));
            return false;
        }
        final Role joinRole = this.roleService.getOnlinePlayer(joinRoleId);
        if (this.isInMap(joinRole)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("该地图无法操作申请队员"));
            return false;
        }
        return true;
    }
    
    public boolean isBackToTeam(final Team team, final Role memberRole) {
        final Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
        if (this.isInMap(leaderRole) && this.caData != null && this.caData.getStage() == 1) {
            final Pos pos = leaderRole.getPos();
            MessagePusher.pushMessage(memberRole, new RespMsg(String.format("队长现在位于决战比武场 (%d，%d) ，暂时无法归队。", pos.getX(), pos.getY())));
            return false;
        }
        return true;
    }
    
    @Override
    public void handleTeamLeaveMoment(final Role role) {
        this.upTeamLeave(role, this.teamService.getTeam(role.getRoleId()));
        super.handleTeamLeaveMoment(role);
    }
    
    @Override
    public void handleTeamLeave(final Role role, final boolean isLeader, final Team team) {
        this.upTeamLeave(role, team);
        super.handleTeamLeave(role, isLeader, team);
    }
    
    public void upTeamLeave(final Role role, final Team team) {
        if (this.isInMap(role)) {
            boolean flag = false;
            this.leaveMap(role);
            if (this.caData != null && this.caData.getStage() == 0) {
                flag = true;
                MessagePusher.pushMessage(role, new RespMsg(String.format("你取消了决战长安的%s资格", "准备")));
            }
            if (flag) {
                if (team != null && this.teamService.getTeamCount(team) < this.caSet.getMinTeamMemberCount()) {
                    this.doAfterLeaveTeam(role, team);
                    final RespMsg respMsg = new RespMsg(String.format("由于队伍少于%d人，无法参加决战长安，请重新组满队伍参赛", this.caSet.getMinTeamMemberCount()));
                    this.teamService.memberHandleThreadLocal(team, memberRole -> MessagePusher.pushMessage(memberRole, respMsg));
                }
                else if (team != null && this.caData != null && this.caData.getStage() == 0) {
                    final CaPkData data = this.caData.getPkDataList().stream().filter(e -> e.getTeamId() == team.getId()).findFirst().orElse(null);
                    if (data != null) {
                        this.enter(data, team);
                    }
                }
            }
        }
    }
    
    @Override
    public void handleLogin(final Role role) {
        final boolean inMap = this.isInMap(role);
        boolean flag = false;
        if (this.caData == null || System.currentTimeMillis() >= this.caData.getEndTime().getTimeInMillis()) {
            flag = true;
        }
        if (inMap && flag) {
            this.leaveMap(role);
        }
        else if (this.caData != null) {
            boolean is = true;
            for (final CaPkData caPkData : this.caData.getPkDataList()) {
                if (caPkData.getMembers().contains(role.getId())) {
                    is = false;
                    break;
                }
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
    public void handleFightEnd(final FightEndEvent fightEndEvent) {
    }
    
    public void handleRoleNameChangeEvent(final RoleChangeEvent roleChangeEvent) {
        final Role role = roleChangeEvent.getRole();
        final CaRankRecord record = RankingList.CA_RANK_RECORDS.get(role.getId());
        if (record != null) {
            record.setGrade(role.getRelive() * 1000 + role.getLevel());
            record.setSerial(role.getSerial());
            record.setName(role.getName());
            record.save();
            final String json = JedisUtil.get("jzca:" + this.serverService.getMarking() + "_" + role.getId());
            if (json != null) {
                final CaCache caCache = new CaCache();
                caCache.setId(role.getId());
                caCache.setRoleId(role.getRoleId());
                caCache.setSerial(role.getSerial());
                caCache.setName(role.getName());
                caCache.setScore(record.getScore());
                caCache.setGrade(role.getRelive() * 1000 + role.getLevel());
                caCache.setFactionName(this.roleService.getFactionName(role));
                caCache.setMarking(role.getMarking());
                JedisUtil.set("jzca:" + this.serverService.getMarking() + "_" + role.getId(), JsonUtils.object2String(caCache));
            }
            if (roleChangeEvent.getType() == RoleChangeEvent.REI) {
                SpringUtils.getBean(AsyncService.class).syncCaRan();
            }
        }
    }
    
    public boolean isInMap(final Role role) {
        return role != null && DataGlobalFunc.isInMap(role, 3005);
    }
    
    public void doAfterLeaveTeam(final Role role, final Team team) {
        try {
            if (team != null) {
                final Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
                if (leaderRole != null && (leaderRole.getId().equals(role.getId()) || this.teamService.getTeamCount(team) < this.caSet.getMinTeamMemberCount())) {
                    for (final Member member : team.getList()) {
                        final Role player = this.roleService.getPlayer(member.getId());
                        if (player != null) {
                            player.pushTempCache("not_check_change_map", true);
                            MessagePusher.pushMessage(player, new RespTaskIcon(3002, "ca", (byte)0));
                        }
                    }
                    this.leaveMap(leaderRole);
                    return;
                }
            }
            this.leaveMap(role);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean isCompetePk(final Role role, final Role pkRole) {
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
    
    public int getGroup(final int rei, final int level) {
        if (rei <= 1) {
            return 1;
        }
        if (rei <= 2) {
            return 2;
        }
        if (rei <= 3) {
            return 3;
        }
        final int grade = rei * 1000 + level;
        if (grade >= 3120) {
            return 4;
        }
        return 3;
    }
    
    public void loadCommonSet(final Map<String, String> commonSetMap) {
        this.loadSet(commonSetMap);
    }
    
    private void loadSet(final Map<String, String> commonSetMap) {
        final String value = commonSetMap.get("ca_team_set");
        CaSet caSet = null;
        if (value != null) {
            caSet = (CaSet)Json.fromJson(CaSet.class, value);
        }
        else {
            caSet = new CaSet();
        }
        this.caSet = caSet;
    }
    
    public void notifyAt() {
        final String day = DateUtils.dayForWeek(new Date());
        if (!this.caSet.getDayOfWeek().contains(day)) {
            return;
        }
        final Event event = ActivityDataPool.eventMap.get(3002);
        CaEventSet eventSet;
        if (event == null) {
            eventSet = new CaEventSet();
        }
        else {
            eventSet = (CaEventSet)Json.fromJson(CaEventSet.class, event.getPara());
        }
        this.round = eventSet.getRound();
        this.prepareOnce();
        SchedulerManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                final String notice = "万众瞩目的决战长安今晚20点整准时开始，请各位少侠届时踊跃参加";
                CaHandler.this.chatService.sendAdnotice(notice);
            }
        }, 30000L);
    }
    
    public void cs() {
        final Event event = ActivityDataPool.eventMap.get(3002);
        CaEventSet eventSet;
        if (event == null) {
            eventSet = new CaEventSet();
        }
        else {
            eventSet = (CaEventSet)Json.fromJson(CaEventSet.class, (CharSequence)event.getPara());
        }
        this.round = eventSet.getRound();
        this.caData = new CaData();
        final Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(new Date());
        final CaSet caSet = this.caSet;
        calendarStart.add(12, 1);
        this.caData.setSignUpTime(EventsUtils.getTime(calendarStart));
        calendarStart.add(12, 1);
        this.caData.setStartTime(EventsUtils.getTime(calendarStart));
        calendarStart.add(12, caSet.getSustain());
        this.caData.setEndTime(EventsUtils.getTime(calendarStart));
        final int durationTime = (int)(this.caData.getStartTime().getTimeInMillis() / 1000L);
        this.caData.setPkDurationTime(durationTime);
        this.addCheckJob();
        this.addStartJob();
        this.addEndJob();
        this.clearRankRecord();
    }
    
    @EventHandler({ EventType.ROLE_LEVEL_UP })
    public void handleRoleLevelUpEvent(final RoleLevelUpEvent roleLevelUpEvent) {
        final Role role = roleLevelUpEvent.getRole();
        if (role == null) {
            return;
        }
        final CaRankRecord record = RankingList.CA_RANK_RECORDS.get(role.getId());
        if (record != null) {
            record.setGrade(role.getRelive() * 1000 + role.getLevel());
            record.save();
        }
    }
    
    public CaSet getCaSet() {
        return this.caSet;
    }
    
    public CaData getCaData() {
        return this.caData;
    }
    
    public void setCaData(final CaData caData) {
        this.caData = caData;
    }
}
