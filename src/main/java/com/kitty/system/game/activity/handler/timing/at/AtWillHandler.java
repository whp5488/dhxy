
package com.kitty.system.game.activity.handler.timing.at;

import com.kitty.system.game.vo.ItemEasyInfo;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.common.utils.text.Convert;
import com.kitty.system.game.vo.TitleInfo;
import java.util.HashMap;
import com.kitty.system.init.domain.vo.Param;
import com.kitty.common.utils.DateUtils;
import com.kitty.system.game.role.service.TitleService;
import java.util.List;
import org.nutz.json.Json;
import java.util.Map;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.system.common.message.RespHint;
import com.kitty.listener.event.LeaveMapEvent;
import java.util.Iterator;
import com.kitty.system.game.confirm.model.LeaveTeamConfirm;
import com.kitty.system.game.team.message.RespConfirm;
import com.kitty.system.game.confirm.model.RoleConfirm;
import com.kitty.system.game.confirm.model.LeaveTempTeamConfirm;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.activity.message.RespAtGuessRaceInfo;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.activity.model.user.at.AtWillPkData;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.team.domain.Team;
import java.text.MessageFormat;
import com.kitty.common.utils.TimeUtil;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.npc.service.NpcService;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.message.Message;
import com.kitty.system.common.utils.SenderUtils;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.activity.message.RespActivityOpenNotice;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.core.SchedulerManager;
import com.kitty.system.game.activity.common.EventsUtils;
import java.util.Date;
import java.util.Calendar;
import java.util.concurrent.ScheduledFuture;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.activity.model.user.at.AtWillData;
import com.kitty.system.game.activity.model.user.at.AtWillSet;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.chat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.team.service.TeamService;
import org.springframework.stereotype.Component;
import com.kitty.system.game.activity.service.ActivityHandler;

@Component
public class AtWillHandler extends ActivityHandler
{
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
    
    public void prepareOnceAtWill() {
        this.initAtWillData();
        this.addCheckJob();
        this.addStartJob();
        this.addNoticeJob();
        this.addEndJob();
    }
    
    private void initAtWillData() {
        final AtWillData atWillData = new AtWillData();
        this.initTime(atWillData);
        this.atWillData = atWillData;
    }
    
    public void initTime(final AtWillData atWillData) {
        final Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(new Date());
        final AtWillSet atWillSet = this.getAtWillSet();
        final String[] signUp = atWillSet.getSignUpTime().split(":");
        calendarStart.set(11, Integer.valueOf(signUp[0]));
        calendarStart.set(12, Integer.valueOf(signUp[1]));
        calendarStart.set(13, 0);
        atWillData.setSignUpTime(EventsUtils.getTime(calendarStart));
        final String[] start = atWillSet.getStartTime().split(":");
        calendarStart.set(11, Integer.valueOf(start[0]));
        calendarStart.set(12, Integer.valueOf(start[1]));
        calendarStart.set(13, 0);
        atWillData.setStartTime(EventsUtils.getTime(calendarStart));
        calendarStart.add(12, atWillSet.getSustain());
        atWillData.setEndTime(EventsUtils.getTime(calendarStart));
        final int durationTime = (int)(atWillData.getStartTime().getTimeInMillis() / 1000L);
        atWillData.setPkDurationTime(durationTime);
    }
    
    private void addCheckJob() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.atWillData.getStartTime().getTime());
        calendar.add(11, -1);
        final long initialDelay = this.atWillData.getStartTime().getTimeInMillis() - System.currentTimeMillis();
        this.logger.error("添加定时检测的定时任务==={}", (Object)initialDelay);
        try {
            if (this.scheduledFuture != null) {
                this.scheduledFuture.cancel(false);
            }
        }
        catch (final Exception e) {
            this.logger.error("定时器关闭==", (Throwable)e);
        }
        this.scheduledFuture = SchedulerManager.getInstance().scheduleAtFixedRate(() -> this.checkInterval(), initialDelay, 60000L);
    }
    
    private void addStartJob() {
        final long initialDelay = this.atWillData.getStartTime().getTimeInMillis() - System.currentTimeMillis();
        this.logger.error("添加英雄争霸赛开始的定时任务==={}", (Object)initialDelay);
        SchedulerManager.getInstance().schedule(() -> SpringUtils.getBean(AtWillPkHandler.class).start(), initialDelay);
    }
    
    private void addNoticeJob() {
        final long initialDelay = this.atWillData.getSignUpTime().getTimeInMillis() - System.currentTimeMillis();
        this.logger.error("添加英雄争霸赛报名通知的定时任务==={}", (Object)initialDelay);
        SchedulerManager.getInstance().schedule(() -> this.notice(), initialDelay);
    }
    
    private void addEndJob() {
        final long initialDelay = this.atWillData.getEndTime().getTimeInMillis() - System.currentTimeMillis();
        this.logger.error("添加英雄争霸赛结束的定时任务==={}", (Object)initialDelay);
        SchedulerManager.getInstance().schedule(() -> this.end(), initialDelay);
    }
    
    private void notice() {
        SenderUtils.brodCastMessage(new RespActivityOpenNotice(), LOGIC.LOGICTYPE_HERO.getValue());
    }
    
    private void end() {
        SpringUtils.getBean(AtWillPkHandler.class).end();
        if (this.scheduledFuture != null) {
            this.scheduledFuture.cancel(Boolean.FALSE);
        }
    }
    
    private void checkInterval() {
        final Calendar calendar = Calendar.getInstance();
        if (this.atWillData.getStage() == 1) {
            SpringUtils.getBean(AtWillPkHandler.class).checkSystemMessage(calendar);
        }
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
        final AtWillData atWillData = this.getAtWillData();
        if (atWillData == null) {
            MessagePusher.pushMessage(role, new RespMsg(EventsUtils.canEnterTips(this.atWillSet.getDayOfWeek(), this.atWillSet.getSignUpTime(), this.atWillSet.getStartTime(), "英雄争霸赛")));
            return "";
        }
        final Calendar calendar = Calendar.getInstance();
        if (calendar.before(atWillData.getSignUpTime())) {
            return MessageFormat.format("现在还不是报名时间，英雄争霸赛下一场将于#<R>{0}#开始报名。", TimeUtil.formatDate(atWillData.getSignUpTime().getTime(), "yyyy-MM-dd-HH:mm:ss"));
        }
        if (role.getLevel() < this.atWillSet.getMinLevel()) {
            return "等级达到30级方可参加英雄争霸赛。";
        }
        if (calendar.after(atWillData.getStartTime())) {
            return "英雄争霸赛正在举行中不可报名参加";
        }
        final int teamCount = this.teamService.getTeamCount(role);
        if (teamCount < this.atWillSet.getMinTeamMemberCount()) {
            return "队伍不足三人无法参加英雄争霸赛。";
        }
        final Team team = this.teamService.getTeam(role.getRoleId());
        final String names = this.teamService.getNotInTeam(team);
        if (names != null) {
            return MessageFormat.format("#<R>{0}#暂离，无法参加英雄争霸赛。", names);
        }
        return null;
    }
    
    public void doEnterMap(final Role role) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null) {
            final ReqTelePort reqTelePort = new ReqTelePort();
            reqTelePort.setMapId(4154);
            reqTelePort.setX(60);
            reqTelePort.setY(35);
            SpringUtils.getMapService().changeMap(role, reqTelePort, null);
            final AtWillPkData data = this.atWillData.getPkDataList().stream().filter(e -> e.getTeamId() == team.getId()).findFirst().orElse(null);
            if (data != null) {
                data.updateTeamMember(team);
            }
            else {
                this.atWillData.getPkDataList().add(new AtWillPkData(team, this.atWillSet.getPkValue()));
            }
            for (int i = 0; i < team.getList().size(); ++i) {
                final Member member = team.getList().get(i);
                final Role player = SpringUtils.getRoleService().getPlayer(member.getId());
                if (player != null) {
                    final RespAtGuessRaceInfo atGuessRaceInfo = new RespAtGuessRaceInfo();
                    atGuessRaceInfo.setMateSecond(this.atWillSet.getPeriod());
                    atGuessRaceInfo.setStartTime(Math.toIntExact(this.atWillData.getStartTime().getTimeInMillis() / 1000L));
                    MessagePusher.pushMessage(player, atGuessRaceInfo);
                }
            }
        }
    }
    
    public void leave(final Role role) {
        if (role != null) {
            if (!this.isLeave(role)) {
                return;
            }
            final Team team = this.teamService.getTeam(role.getRoleId());
            this.doAfterLeaveTeam(role, team);
        }
    }
    
    public boolean isLeave(final Role role) {
        return !SpringUtils.getBean(FightNewService.class).isInFight(role);
    }
    
    public void leaveMap(final Role role) {
        if (role == null) {
            return;
        }
        if (role.getPos().getMapId() != 4154) {
            return;
        }
        try {
            final Team team = this.teamService.getTeam(role.getRoleId());
            if (team != null && team.isLeader(role.getRoleId())) {
                final AtWillPkData atWillPkData = this.atWillData.getPkDataList().stream().filter(e -> e.getTeamId() == team.getId()).findFirst().orElse(null);
                if (atWillPkData != null) {
                    atWillPkData.setLeave(System.currentTimeMillis());
                }
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        role.pushTempCache("not_check_change_map", true);
        final ReqTelePort reqTelePort = new ReqTelePort();
        reqTelePort.setMapId(1011);
        reqTelePort.setX(132);
        reqTelePort.setY(67);
        SpringUtils.getMapService().changeMap(role, reqTelePort, null);
    }
    
    public boolean isRequestJoinTeam(final Role role, final Team team) {
        if (this.isInMap(role)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("当前地图无法申请"));
            return false;
        }
        final Role leaderRole = SpringUtils.getRoleService().getPlayer(team.getLeaderUid());
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
        final Role invitRole = SpringUtils.getRoleService().getOnlinePlayer(inviteRoleId);
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
        final Role joinRole = SpringUtils.getRoleService().getOnlinePlayer(joinRoleId);
        if (this.isInMap(joinRole)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("该地图无法操作申请队员"));
            return false;
        }
        return true;
    }
    
    public boolean isBackToTeam(final Team team, final Role memberRole) {
        final Role leaderRole = SpringUtils.getRoleService().getOnlinePlayer(team.getLeaderUid());
        if (this.isInMap(leaderRole) && this.atWillData != null && this.atWillData.getStage() == 1) {
            MessagePusher.pushMessage(memberRole, new RespMsg("当前处于英雄争霸赛决战阶段不可以归队"));
            return false;
        }
        return true;
    }
    
    public boolean leaveMomentConfirm(final Role role) {
        if (this.isInMap(role) && !this.isLeave(role)) {
            return false;
        }
        if (this.isInMap(role) && this.isLeave(role)) {
            role.setConfirm(new LeaveTempTeamConfirm(1));
            final RespConfirm respConfirm = new RespConfirm();
            respConfirm.setTips("英雄争霸赛场地中无法暂离，确认暂离将会离开队伍。");
            respConfirm.setConfirmType("");
            MessagePusher.pushMessage(role, respConfirm);
            return false;
        }
        return true;
    }
    
    public boolean leaveConfirm(final Role role) {
        if (this.isInMap(role) && !this.isLeave(role)) {
            return false;
        }
        if (this.isInMap(role) && this.isLeave(role)) {
            role.setConfirm(new LeaveTeamConfirm(1));
            final RespConfirm respConfirm = new RespConfirm();
            respConfirm.setTips("英雄争霸赛场地中无法离队，确认后将会离开队伍。");
            respConfirm.setConfirmType("");
            MessagePusher.pushMessage(role, respConfirm);
            return false;
        }
        return true;
    }
    
    @Override
    public void handleTeamLeaveMoment(final Role role) {
        if (this.isInMap(role)) {
            final Team team = this.teamService.getTeam(role.getRoleId());
            this.doAfterLeaveTeam(role, team);
        }
        super.handleTeamLeaveMoment(role);
    }
    
    @Override
    public void handleTeamLeave(final Role role, final boolean isLeader, final Team team) {
        if (team != null && team.getList().size() < this.atWillSet.getMinTeamMemberCount()) {
            this.doAfterLeaveTeam(role, team);
        }
        else if (team != null && this.atWillData != null) {
            final AtWillPkData atWillPkData = this.atWillData.getPkDataList().stream().filter(e -> e.getTeamId() == team.getId()).findFirst().orElse(null);
            if (atWillPkData != null) {
                atWillPkData.updateTeamMember(team);
            }
        }
    }
    
    @Override
    public void handleLogin(final Role role) {
        final boolean inMap = this.isInMap(role);
        boolean flag = false;
        if (this.atWillData == null || System.currentTimeMillis() >= this.atWillData.getEndTime().getTimeInMillis()) {
            flag = true;
        }
        if (inMap && flag) {
            this.leaveMap(role);
        }
        else if (this.atWillData != null) {
            boolean is = true;
            for (final AtWillPkData atWillPkData : this.atWillData.getPkDataList()) {
                final Member member = atWillPkData.getMembers().stream().filter(e -> e.getId().equals(role.getId())).findFirst().orElse(null);
                if (member != null) {
                    is = false;
                    break;
                }
            }
            if (is) {
                this.leaveMap(role);
            }
        }
        super.handleLogin(role);
    }
    
    @EventHandler({ EventType.LEAVE_MAP })
    public void handleLeaveMapEvent(final LeaveMapEvent leaveMapEvent) {
        final Role role = leaveMapEvent.getRole();
        if (role != null && leaveMapEvent.getMapId() == 4154) {
            MessagePusher.pushMessage(role, new RespHint(17, (short)1));
        }
    }
    
    @Override
    public void handleFightEnd(final FightEndEvent fightEndEvent) {
    }
    
    public boolean isInMap(final Role role) {
        return role != null && DataGlobalFunc.isInMap(role, 4154);
    }
    
    public void doAfterLeaveTeam(final Role role, final Team team) {
        try {
            if (!this.isInMap(role)) {
                return;
            }
            this.leaveMap(role);
            if (team != null && this.teamService.getTeamCount(team) < this.atWillSet.getMinTeamMemberCount()) {
                final Role leaderRole = SpringUtils.getRoleService().getPlayer(team.getLeaderUid());
                if (leaderRole != null) {
                    this.leaveMap(leaderRole);
                }
            }
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
    
    public void loadCommonSet(final Map<String, String> commonSetMap) {
        this.loadAtWillSet(commonSetMap);
    }
    
    private void loadAtWillSet(final Map<String, String> commonSetMap) {
        final String value = commonSetMap.get("at_team_set");
        AtWillSet atWillSet = null;
        if (value != null) {
            atWillSet = Json.fromJson(AtWillSet.class, value);
        }
        else {
            atWillSet = new AtWillSet();
        }
        this.atWillSet = atWillSet;
    }
    
    public void send(final List<Member> list, final byte rank) {
        try {
            final RoleService roleService = SpringUtils.getRoleService();
            final TitleService titleService = SpringUtils.getBean(TitleService.class);
            final String day = DateUtils.dayForWeek(new Date());
            int index = this.atWillSet.getDayOfWeek().indexOf(day) + 1;
            if (index >= this.atWillSet.getDayOfWeek().size()) {
                index = 0;
            }
            String temporalAdjusters = TimeUtil.temporalAdjusters(Integer.valueOf(this.atWillSet.getDayOfWeek().get(index)));
            temporalAdjusters = temporalAdjusters + " " + this.atWillSet.getSignUpTime();
            final Date parse = TimeUtil.parse(temporalAdjusters, "yyyy-MM-dd HH:mm:ss");
            final int time = Math.toIntExact((parse.getTime() - 1200000L) / 1000L);
            final List<Param> params = this.atWillSet.getAward().get(rank);
            final Map<Integer, Integer> map = new HashMap<Integer, Integer>();
            String msg = "";
            if (params != null) {
                for (final Param param : params) {
                    final ItemEasyInfo itemEasyInfo = DataGlobalFunc.getItemEasyInfo(param.getK());
                    if (itemEasyInfo != null) {
                        map.put(param.getK(), param.getV());
                        msg = msg + itemEasyInfo.getName() + "X" + param.getV() + "、";
                    }
                }
            }
            String names = "";
            for (final Member member : list) {
                final Role role = roleService.getPlayer(member.getId());
                if (role == null) {
                    continue;
                }
                titleService.addTitle(role, new TitleInfo(10024, time));
                names = names + role.getName() + "、";
                for (final Integer serial : map.keySet()) {
                    this.bagService.addGoods(role, serial, map.get(serial), 0, true, false, "英雄争霸赛");
                }
            }
            if (!names.isEmpty() && !msg.isEmpty()) {
                msg = msg.substring(0, msg.length() - 1);
                names = names.substring(0, names.length() - 1);
                final String content = MessageFormat.format("#<LB1>{0}#赢得了本次英雄争霸赛#<R>第{1}名#，获得了{2}的奖励！", names, Convert.getChineseFromArabic((int)rank), msg);
                this.chatService.sendSystem(content, "role");
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public void notifyAt() {
        final String day = DateUtils.dayForWeek(new Date());
        if (!this.atWillSet.getDayOfWeek().contains(day)) {
            return;
        }
        this.prepareOnceAtWill();
        SchedulerManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                final String notice = "万众瞩目的英雄争霸赛将于二十分钟后正式开始请各路英雄组好队做好准备找比武使者进场吧";
                AtWillHandler.this.chatService.sendAdnotice(notice);
            }
        }, 30000L);
    }
    
    public void cs() {
        final AtWillData atWillData = new AtWillData();
        final Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(new Date());
        final AtWillSet atWillSet = this.getAtWillSet();
        calendarStart.set(13, 0);
        calendarStart.add(12, 1);
        atWillData.setSignUpTime(EventsUtils.getTime(calendarStart));
        calendarStart.set(13, 0);
        calendarStart.add(12, 1);
        atWillData.setStartTime(EventsUtils.getTime(calendarStart));
        calendarStart.add(12, atWillSet.getSustain());
        atWillData.setEndTime(EventsUtils.getTime(calendarStart));
        final int durationTime = (int)(atWillData.getStartTime().getTimeInMillis() / 1000L);
        atWillData.setPkDurationTime(durationTime);
        this.atWillData = atWillData;
        this.addCheckJob();
        this.addStartJob();
        this.addNoticeJob();
        this.addEndJob();
    }
    
    public AtWillSet getAtWillSet() {
        return this.atWillSet;
    }
    
    public AtWillData getAtWillData() {
        return this.atWillData;
    }
    
    public void setAtWillData(final AtWillData atWillData) {
        this.atWillData = atWillData;
    }
}
