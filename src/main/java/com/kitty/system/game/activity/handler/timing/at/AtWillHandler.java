package com.kitty.system.game.activity.handler.timing.at;

import com.kitty.system.game.vo.ItemEasyInfo;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.common.utils.text.Convert;
import com.kitty.system.game.vo.TitleInfo;
import com.kitty.system.init.domain.vo.Param;
import java.util.HashMap;
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
import com.kitty.system.game.confirm.model.LeaveTempTeamConfirm;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.activity.message.RespAtGuessRaceInfo;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.activity.model.user.at.AtWillPkData;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.team.domain.Team;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.common.utils.TimeUtil;
import java.text.MessageFormat;
import com.kitty.system.game.npc.service.NpcService;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.role.domain.Role;
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
    
    public AtWillHandler() {
    }
    
    public void prepareOnceAtWill() {
        this.initAtWillData();
        this.addCheckJob();
        this.addStartJob();
        this.addNoticeJob();
        this.addEndJob();
    }
    
    private void initAtWillData() {
        AtWillData atWillData = new AtWillData();
        this.initTime(atWillData);
        this.atWillData = atWillData;
    }
    
    public void initTime(AtWillData atWillData) {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(new Date());
        AtWillSet atWillSet = this.getAtWillSet();
        String[] signUp = atWillSet.getSignUpTime().split(":");
        calendarStart.set(11, (int)Integer.valueOf(signUp[0]));
        calendarStart.set(12, (int)Integer.valueOf(signUp[1]));
        calendarStart.set(13, 0);
        atWillData.setSignUpTime(EventsUtils.getTime(calendarStart));
        String[] start = atWillSet.getStartTime().split(":");
        calendarStart.set(11, (int)Integer.valueOf(start[0]));
        calendarStart.set(12, (int)Integer.valueOf(start[1]));
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
            if (this.scheduledFuture != null) {
                this.scheduledFuture.cancel(false);
            }
        }
        catch (Exception e) {
            this.logger.error("定时器关闭==", e);
        }
        this.scheduledFuture = SchedulerManager.getInstance().scheduleAtFixedRate(() -> this.checkInterval(), initialDelay, 60000L);
    }
    
    private void addStartJob() {
        long initialDelay = this.atWillData.getStartTime().getTimeInMillis() - System.currentTimeMillis();
        this.logger.error("添加英雄争霸赛开始的定时任务==={}", Long.valueOf(initialDelay));
        SchedulerManager.getInstance().schedule(() -> ((AtWillPkHandler)SpringUtils.getBean(AtWillPkHandler.class)).start(), initialDelay);
    }
    
    private void addNoticeJob() {
        long initialDelay = this.atWillData.getSignUpTime().getTimeInMillis() - System.currentTimeMillis();
        this.logger.error("添加英雄争霸赛报名通知的定时任务==={}", Long.valueOf(initialDelay));
        SchedulerManager.getInstance().schedule(() -> this.notice(), initialDelay);
    }
    
    private void addEndJob() {
        long initialDelay = this.atWillData.getEndTime().getTimeInMillis() - System.currentTimeMillis();
        this.logger.error("添加英雄争霸赛结束的定时任务==={}", Long.valueOf(initialDelay));
        SchedulerManager.getInstance().schedule(() -> this.end(), initialDelay);
    }
    
    private void notice() {
        SenderUtils.brodCastMessage(new RespActivityOpenNotice(), LOGIC.LOGICTYPE_HERO.getValue());
    }
    
    private void end() {
        ((AtWillPkHandler)SpringUtils.getBean(AtWillPkHandler.class)).end();
        if (this.scheduledFuture != null) {
            this.scheduledFuture.cancel((boolean)Boolean.FALSE);
        }
    }
    
    private void checkInterval() {
        Calendar calendar = Calendar.getInstance();
        if (this.atWillData.getStage() == 1) {
            ((AtWillPkHandler)SpringUtils.getBean(AtWillPkHandler.class)).checkSystemMessage(calendar);
        }
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
        AtWillData atWillData = this.getAtWillData();
        if (atWillData == null) {
            MessagePusher.pushMessage(role, new RespMsg(EventsUtils.canEnterTips(this.atWillSet.getDayOfWeek(), this.atWillSet.getSignUpTime(), this.atWillSet.getStartTime(), "英雄争霸赛")));
            return "";
        }
        Calendar calendar = Calendar.getInstance();
        if (calendar.before(atWillData.getSignUpTime())) {
            return MessageFormat.format("现在还不是报名时间，英雄争霸赛下一场将于#<R>{0}#开始报名。", TimeUtil.formatDate(atWillData.getSignUpTime().getTime(), "yyyy-MM-dd-HH:mm:ss"));
        }
        if ((int)role.getLevel() < this.atWillSet.getMinLevel()) {
            return "等级达到30级方可参加英雄争霸赛。";
        }
        if (calendar.after(atWillData.getStartTime())) {
            return "英雄争霸赛正在举行中不可报名参加";
        }
        int teamCount = this.teamService.getTeamCount(role);
        if (teamCount < this.atWillSet.getMinTeamMemberCount()) {
            return "队伍不足三人无法参加英雄争霸赛。";
        }
        Team team = this.teamService.getTeam((int)role.getRoleId());
        String names = this.teamService.getNotInTeam(team);
        if (names != null) {
            return MessageFormat.format("#<R>{0}#暂离，无法参加英雄争霸赛。", names);
        }
        return null;
    }
    
    public void doEnterMap(Role role) {
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (team != null) {
            ReqTelePort reqTelePort = new ReqTelePort();
            reqTelePort.setMapId(4154);
            reqTelePort.setX(60);
            reqTelePort.setY(35);
            SpringUtils.getMapService().changeMap(role, reqTelePort, null);
            AtWillPkData data = (AtWillPkData)this.atWillData.getPkDataList().stream().filter(e -> e.getTeamId() == team.getId()).findFirst().orElse(null);
            if (data != null) {
                data.updateTeamMember(team);
            }
            else {
                this.atWillData.getPkDataList().add(new AtWillPkData(team, this.atWillSet.getPkValue()));
            }
            for (int i = 0; i < team.getList().size(); ++i) {
                Member member = (Member)team.getList().get(i);
                Role player = SpringUtils.getRoleService().getPlayer((long)member.getId());
                if (player != null) {
                    RespAtGuessRaceInfo atGuessRaceInfo = new RespAtGuessRaceInfo();
                    atGuessRaceInfo.setMateSecond(this.atWillSet.getPeriod());
                    atGuessRaceInfo.setStartTime(Math.toIntExact(this.atWillData.getStartTime().getTimeInMillis() / 1000L));
                    MessagePusher.pushMessage(player, atGuessRaceInfo);
                }
            }
        }
    }
    
    public void leave(Role role) {
        if (role != null) {
            if (!this.isLeave(role)) {
                return;
            }
            Team team = this.teamService.getTeam((int)role.getRoleId());
            this.doAfterLeaveTeam(role, team);
        }
    }
    
    public boolean isLeave(Role role) {
        return !((FightNewService)SpringUtils.getBean(FightNewService.class)).isInFight(role);
    }
    
    public void leaveMap(Role role) {
        if (role == null) {
            return;
        }
        if ((int)role.getPos().getMapId() != 4154) {
            return;
        }
        try {
            Team team = this.teamService.getTeam((int)role.getRoleId());
            if (team != null && team.isLeader((int)role.getRoleId())) {
                AtWillPkData atWillPkData = (AtWillPkData)this.atWillData.getPkDataList().stream().filter(e -> e.getTeamId() == team.getId()).findFirst().orElse(null);
                if (atWillPkData != null) {
                    atWillPkData.setLeave(System.currentTimeMillis());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        role.pushTempCache("not_check_change_map", Boolean.valueOf(true));
        ReqTelePort reqTelePort = new ReqTelePort();
        reqTelePort.setMapId(1011);
        reqTelePort.setX(132);
        reqTelePort.setY(67);
        SpringUtils.getMapService().changeMap(role, reqTelePort, null);
        return;
    }
    
    public boolean isRequestJoinTeam(Role role, Team team) {
        if (this.isInMap(role)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("当前地图无法申请"));
            return false;
        }
        Role leaderRole = SpringUtils.getRoleService().getPlayer(team.getLeaderUid());
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
        Role invitRole = SpringUtils.getRoleService().getOnlinePlayer(inviteRoleId);
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
        Role joinRole = SpringUtils.getRoleService().getOnlinePlayer(joinRoleId);
        if (this.isInMap(joinRole)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("该地图无法操作申请队员"));
            return false;
        }
        return true;
    }
    
    public boolean isBackToTeam(Team team, Role memberRole) {
        Role leaderRole = SpringUtils.getRoleService().getOnlinePlayer(team.getLeaderUid());
        if (this.isInMap(leaderRole) && this.atWillData != null && this.atWillData.getStage() == 1) {
            MessagePusher.pushMessage(memberRole, new RespMsg("当前处于英雄争霸赛决战阶段不可以归队"));
            return false;
        }
        return true;
    }
    
    public boolean leaveMomentConfirm(Role role) {
        if (this.isInMap(role) && !this.isLeave(role)) {
            return false;
        }
        if (this.isInMap(role) && this.isLeave(role)) {
            role.setConfirm(new LeaveTempTeamConfirm(1));
            RespConfirm respConfirm = new RespConfirm();
            respConfirm.setTips("英雄争霸赛场地中无法暂离，确认暂离将会离开队伍。");
            respConfirm.setConfirmType("");
            MessagePusher.pushMessage(role, respConfirm);
            return false;
        }
        return true;
    }
    
    public boolean leaveConfirm(Role role) {
        if (this.isInMap(role) && !this.isLeave(role)) {
            return false;
        }
        if (this.isInMap(role) && this.isLeave(role)) {
            role.setConfirm(new LeaveTeamConfirm(1));
            RespConfirm respConfirm = new RespConfirm();
            respConfirm.setTips("英雄争霸赛场地中无法离队，确认后将会离开队伍。");
            respConfirm.setConfirmType("");
            MessagePusher.pushMessage(role, respConfirm);
            return false;
        }
        return true;
    }
    
    @Override
    public void handleTeamLeaveMoment(Role role) {
        if (this.isInMap(role)) {
            Team team = this.teamService.getTeam((int)role.getRoleId());
            this.doAfterLeaveTeam(role, team);
        }
        super.handleTeamLeaveMoment(role);
    }
    
    @Override
    public void handleTeamLeave(Role role, boolean isLeader, Team team) {
        if (team != null && team.getList().size() < this.atWillSet.getMinTeamMemberCount()) {
            this.doAfterLeaveTeam(role, team);
        }
        else if (team != null && this.atWillData != null) {
            AtWillPkData atWillPkData = (AtWillPkData)this.atWillData.getPkDataList().stream().filter(e -> e.getTeamId() == team.getId()).findFirst().orElse(null);
            if (atWillPkData != null) {
                atWillPkData.updateTeamMember(team);
            }
        }
    }
    
    @Override
    public void handleLogin(Role role) {
        boolean inMap = this.isInMap(role);
        boolean flag = false;
        if (this.atWillData == null || System.currentTimeMillis() >= this.atWillData.getEndTime().getTimeInMillis()) {
            flag = true;
        }
        if (inMap && flag) {
            this.leaveMap(role);
        }
        else if (this.atWillData != null) {
            boolean is = true;
            for (AtWillPkData atWillPkData : this.atWillData.getPkDataList()) {
                Member member = (Member)atWillPkData.getMembers().stream().filter(e -> e.getId().equals(role.getId())).findFirst().orElse(null);
                if (member != null) {
                    is = false;
                    break;
                }
                continue;
            }
            if (is) {
                this.leaveMap(role);
            }
        }
        super.handleLogin(role);
    }
    
    @EventHandler({ EventType.LEAVE_MAP })
    public void handleLeaveMapEvent(LeaveMapEvent leaveMapEvent) {
        Role role = leaveMapEvent.getRole();
        if (role != null && leaveMapEvent.getMapId() == 4154) {
            MessagePusher.pushMessage(role, new RespHint(17, (short)1));
        }
    }
    
    @Override
    public void handleFightEnd(FightEndEvent fightEndEvent) {
    }
    
    public boolean isInMap(Role role) {
        return role != null && DataGlobalFunc.isInMap(role, 4154);
    }
    
    public void doAfterLeaveTeam(Role role, Team team) {
        try {
            if (!this.isInMap(role)) {
                return;
            }
            this.leaveMap(role);
            if (team != null && this.teamService.getTeamCount(team) < this.atWillSet.getMinTeamMemberCount()) {
                Role leaderRole = SpringUtils.getRoleService().getPlayer(team.getLeaderUid());
                if (leaderRole != null) {
                    this.leaveMap(leaderRole);
                }
            }
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
        this.loadAtWillSet(commonSetMap);
    }
    
    private void loadAtWillSet(Map<String, String> commonSetMap) {
        String value = (String)commonSetMap.get("at_team_set");
        AtWillSet atWillSet = null;
        if (value != null) {
            atWillSet = (AtWillSet)Json.fromJson(AtWillSet.class, value);
        }
        else {
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
            if (index >= this.atWillSet.getDayOfWeek().size()) {
                index = 0;
            }
            String temporalAdjusters = TimeUtil.temporalAdjusters((int)Integer.valueOf((String)this.atWillSet.getDayOfWeek().get(index)));
            temporalAdjusters = temporalAdjusters + " " + this.atWillSet.getSignUpTime();
            Date parse = TimeUtil.parse(temporalAdjusters, "yyyy-MM-dd HH:mm:ss");
            int time = Math.toIntExact((parse.getTime() - 1200000L) / 1000L);
            List<Param> params = (List)this.atWillSet.getAward().get(Byte.valueOf(rank));
            Map<Integer, Integer> map = new HashMap();
            String msg = "";
            if (params != null) {
                for (Param param : params) {
                    ItemEasyInfo itemEasyInfo = DataGlobalFunc.getItemEasyInfo(param.getK());
                    if (itemEasyInfo != null) {
                        map.put(param.getK(), param.getV());
                        msg = msg + itemEasyInfo.getName() + "X" + param.getV() + "、";
                    }
                    else {
                        continue;
                    }
                }
            }
            String names = "";
            for (Member member : list) {
                Role role = roleService.getPlayer((long)member.getId());
                if (role == null) {
                    continue;
                }
                titleService.addTitle(role, new TitleInfo(10024, time));
                names = names + role.getName() + "、";
                for (Integer serial : map.keySet()) {
                    this.bagService.addGoods(role, (int)serial, (int)map.get(serial), 0, true, false, "英雄争霸赛");
                }
            }
            if (!names.isEmpty() && !msg.isEmpty()) {
                msg = msg.substring(0, msg.length() - 1);
                names = names.substring(0, names.length() - 1);
                String content = MessageFormat.format("#<LB1>{0}#赢得了本次英雄争霸赛#<R>第{1}名#，获得了{2}的奖励！", names, Convert.getChineseFromArabic(Integer.valueOf((int)rank)), msg);
                this.chatService.sendSystem(content, "role");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void notifyAt() {
        String day = DateUtils.dayForWeek(new Date());
        if (!this.atWillSet.getDayOfWeek().contains(day)) {
            return;
        }
        this.prepareOnceAtWill();
        SchedulerManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                String notice = "万众瞩目的英雄争霸赛将于二十分钟后正式开始请各路英雄组好队做好准备找比武使者进场吧";
                AtWillHandler.this.chatService.sendAdnotice(notice);
            }
        }, 30000L);
        return;
    }
    
    public void cs() {
        AtWillData atWillData = new AtWillData();
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(new Date());
        AtWillSet atWillSet = this.getAtWillSet();
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
