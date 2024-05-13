
package com.kitty.system.game.activity.handler.timing.at;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.init.domain.vo.Param;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.activity.message.RespAtClashResult;
import com.kitty.mina.task.ThreadLocalUtil;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.game.fightnew.factory.FightFactory;
import com.kitty.system.game.activity.common.EventsUtils;
import com.kitty.system.common.data.ActivityGlobalFunc;
import java.util.Random;
import com.kitty.system.game.team.domain.Member;
import java.util.HashSet;
import com.kitty.system.game.fightnew.bean.Fight;
import java.util.Collection;
import java.text.MessageFormat;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.utils.JsonUtils;
import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import java.util.Calendar;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.team.domain.Team;
import java.util.Iterator;
import java.util.List;
import com.kitty.system.game.activity.model.user.at.AtWillPkData;
import com.kitty.system.game.activity.model.user.at.AtWillSet;
import com.kitty.system.game.activity.model.user.at.AtWillData;
import com.kitty.common.core.SchedulerManager;
import java.util.concurrent.ScheduledFuture;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.map.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.kitty.system.game.activity.service.ActivityHandler;

@Component
public class AtWillPkHandler extends ActivityHandler
{
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
    private static boolean MATCH_FLAG;
    private static final String WIN_MESSAGE = "恭喜你取得了战斗胜利，获得了{0}点积分，你当前的积分为#<R>{1}#点。";
    private static final String FAIL_MESSAGE = "真遗憾你失败了，失去了1点巅峰对决值。";
    private static final String OUT_MESSAGE = " 真是精彩纷呈啊！#<Y>{0}#的队伍英雄争霸赛正在如火如荼的进行！适才在高手对决中棋差一着，遗憾地败下阵来，真是太可惜了。其他还在拼搏的高手要继续努力！";
    private static final String REMAIN_TIME_MESSAGE = "英雄争霸赛还剩#<R>{0}分钟#，大家要抓紧时间啦！";
    private static final String USED_TIME_MESSAGE = "英雄争霸赛已经进行了#<R>{0}分钟#，大家加油啊！";
    private static final String FIRST_MESSAGE = "恭喜，#<Y>{0}#获得了本届的英雄争霸赛的#<R>第一名#。";
    
    public void start() {
        this.logger.error("英雄争霸赛对决开始");
        try {
            final AtWillData atWillData = this.atWillHandler.getAtWillData();
            final AtWillSet atWillSet = this.atWillHandler.getAtWillSet();
            this.withdraw(atWillData);
            atWillData.setStage((byte)1);
            this.scheduledFuture = SchedulerManager.getInstance().scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    AtWillPkHandler.this.check();
                }
            }, 10000L, atWillSet.getPeriod() * 1000);
        }
        catch (final Exception e) {
            this.end();
            e.printStackTrace();
            this.logger.error("英雄争霸赛PK阶段开始错误：{}", e.getMessage());
        }
    }
    
    public void withdraw(final AtWillData atWillData) {
        final AtWillSet atWillSet = this.atWillHandler.getAtWillSet();
        final List<AtWillPkData> list = atWillData.getPkDataList();
        for (final AtWillPkData data : list) {
            final Team team = this.teamService.getTeamByTeamId(data.getTeamId());
            if (team == null) {
                continue;
            }
            if (team != null && this.teamService.getTeamCount(team) < atWillSet.getMinTeamMemberCount()) {
                final Role player = this.roleService.getPlayer(team.getLeaderUid());
                if (player != null) {
                    this.atWillHandler.leave(player);
                }
                else {
                    this.logger.error("AK比赛开始前弃权,队伍人数小于参赛最小人数清理出场。获取角色错误：{}", (Object)team.getLeaderUid());
                }
            }
            else {
                final List<Integer> objects = this.teamService.getTeamAfkMember(team);
                for (final Integer object : objects) {
                    final Role role = this.roleService.getPlayer(object);
                    if (role != null) {
                        role.pushTempCache("at_leave", true);
                        this.teamService.leave(role, false);
                        this.atWillHandler.leave(role);
                        role.pushTempCache("at_leave", false);
                    }
                }
            }
        }
    }
    
    private void check() {
        final AtWillData atWillData = this.atWillHandler.getAtWillData();
        if (atWillData == null || atWillData.getEndTime().getTimeInMillis() < System.currentTimeMillis()) {
            this.end();
            this.logger.error("英雄争霸赛结束，不在匹配");
            return;
        }
        if (!AtWillPkHandler.MATCH_FLAG) {
            this.logger.error("英雄争霸赛状态为匹配中，上次匹配未结束--------------------------------------");
            return;
        }
        try {
            this.logger.info("英雄争霸赛pk匹配，当前剩余队伍数量 = {}", this.sceneTeamCount(atWillData));
            if (this.isCanEnd()) {
                this.logger.info("英雄争霸赛pk结束");
                this.end();
            }
            else {
                AtWillPkHandler.MATCH_FLAG = false;
                this.match();
                AtWillPkHandler.MATCH_FLAG = true;
            }
        }
        catch (final Exception e) {
            AtWillPkHandler.MATCH_FLAG = true;
            e.printStackTrace();
            this.logger.error("英雄争霸赛匹配出错-----------------------------------------------");
        }
    }
    
    private boolean isCanEnd() {
        final AtWillData data = this.atWillHandler.getAtWillData();
        return this.sceneTeamCount(data) <= 1 || Calendar.getInstance().after(data.getEndTime());
    }
    
    public int sceneTeamCount(final AtWillData data) {
        int count = 0;
        if (data != null) {
            for (final AtWillPkData obj : data.getPkDataList()) {
                final Role leader = this.teamService.getTeamLeader(obj.getTeamId());
                if (leader == null) {
                    continue;
                }
                if (!this.atWillHandler.isInMap(leader)) {
                    continue;
                }
                ++count;
            }
        }
        return count;
    }
    
    public void end() {
        try {
            final AtWillData atWillData = this.atWillHandler.getAtWillData();
            atWillData.setStage((byte)2);
            this.logger.error("英雄争霸赛对决结束");
            final List<AtWillPkData> list = atWillData.getPkDataList().stream().filter(e -> e.getPkValue() > 0 && e.getScore() > 0).sorted(Comparator.comparing(AtWillPkData::getPkValue).reversed()).collect(Collectors.toList());
            final List<AtWillPkData> topList = new ArrayList<AtWillPkData>(3);
            if (list.size() > 0) {
                this.logger.error("当前在场的成员：{}", list);
                for (final AtWillPkData willPkData : list) {
                    topList.add(willPkData);
                }
            }
            final List<AtWillPkData> collect = atWillData.getPkDataList().stream().sorted(Comparator.comparing(AtWillPkData::getLeave).reversed()).collect(Collectors.toList());
            for (final AtWillPkData atWillPkData : collect) {
                if (!topList.contains(atWillPkData)) {
                    topList.add(atWillPkData);
                }
                if (topList.size() == 3) {
                    break;
                }
            }
            if (topList.size() > 0) {
                this.showGlory(topList.get(0));
            }
            this.grant(topList);
            this.logger.info("英雄争霸赛结束时，队伍前三名=={}", (topList != null) ? JsonUtils.object2String(topList) : "");
            for (final AtWillPkData atWillPkData : atWillData.getPkDataList()) {
                final Team team = SpringUtils.getTeamService().getTeamByTeamId(atWillPkData.getTeamId());
                if (team == null) {
                    continue;
                }
                final Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
                if (leaderRole == null) {
                    continue;
                }
                this.atWillHandler.leaveMap(leaderRole);
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        if (this.scheduledFuture != null) {
            this.scheduledFuture.cancel(Boolean.FALSE);
        }
    }
    
    public void grant(final List<AtWillPkData> topList) {
        int size = 3;
        if (topList.size() < 3) {
            size = topList.size();
        }
        for (int i = 0; i < size; ++i) {
            final AtWillPkData atWillPkData = topList.get(i);
            this.atWillHandler.send(atWillPkData.getMembers(), (byte)(i + 1));
        }
    }
    
    public void showGlory(final AtWillPkData atWillPkData) {
        if (atWillPkData == null) {
            return;
        }
        final Team team = this.teamService.getTeamByTeamId(atWillPkData.getTeamId());
        if (team == null) {
            return;
        }
        final String names = this.teamService.checkMember(team, memberRole -> memberRole != null);
        final String msg = MessageFormat.format("恭喜，#<Y>{0}#获得了本届的英雄争霸赛的#<R>第一名#。", names);
        SpringUtils.getChatService().sendSystem(msg, "role");
    }
    
    private void match() {
        try {
            final AtWillData atWillData = this.atWillHandler.getAtWillData();
            final List<AtWillPkData> atWillPkData = atWillData.getPkDataList();
            final List<Team> list = this.getFightTeamList(atWillPkData);
            final List<Team> allList = this.getFightTeamSceneList(atWillPkData);
            if (list.size() > 0) {
                this.match(allList, list);
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
            this.logger.error("匹配对手出错-----------------------------------------------");
        }
    }
    
    private List<Team> getFightTeamList(final List<AtWillPkData> pkList) {
        try {
            final List<AtWillPkData> tempPkList = new ArrayList<AtWillPkData>(pkList);
            tempPkList.sort(Comparator.comparing(AtWillPkData::getPrevPKTime));
            final List<Team> list = new ArrayList<Team>(tempPkList.size());
            final long now = System.currentTimeMillis();
            for (final AtWillPkData atWillPkData : tempPkList) {
                final Team team = this.teamService.getTeamByTeamId(atWillPkData.getTeamId());
                if (team == null) {
                    continue;
                }
                final Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
                if (leaderRole == null) {
                    this.logger.error("获得可以战斗的队伍队长信息时为空了");
                }
                else {
                    if (atWillPkData.getState() == 1) {
                        continue;
                    }
                    if (!leaderRole.getPos().getMapId().equals(4154)) {
                        continue;
                    }
                    if (!this.fightNewService.canFight(leaderRole, team.getList())) {
                        continue;
                    }
                    final Fight fight = this.fightNewService.getFightByRoleId(leaderRole.getRoleId());
                    if (fight != null) {
                        continue;
                    }
                    if (atWillPkData.getPkValue() <= 0) {
                        continue;
                    }
                    if (now - atWillPkData.getPrevPKTime() < 10000L) {
                        continue;
                    }
                    if (now - leaderRole.getPrevFightEndTime() < 10000L) {
                        continue;
                    }
                    list.add(team);
                }
            }
            return list;
        }
        catch (final Exception e) {
            e.printStackTrace();
            this.logger.error("获得可以战斗的队伍出错了--------------------------------------");
            return new ArrayList<Team>();
        }
    }
    
    private List<Team> getFightTeamSceneList(final List<AtWillPkData> pkList) {
        try {
            final List<AtWillPkData> tempPkList = new ArrayList<AtWillPkData>(pkList);
            tempPkList.sort(Comparator.comparing(AtWillPkData::getPrevPKTime));
            final List<Team> list = new ArrayList<Team>(tempPkList.size());
            final long now = System.currentTimeMillis();
            for (final AtWillPkData atWillPkData : tempPkList) {
                final Team team = this.teamService.getTeamByTeamId(atWillPkData.getTeamId());
                if (team == null) {
                    continue;
                }
                final Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
                if (leaderRole == null) {
                    continue;
                }
                if (atWillPkData.getState() == 1) {
                    continue;
                }
                if (leaderRole.getPos().getMapId() != 4154) {
                    continue;
                }
                if (now - atWillPkData.getPrevPKTime() < 10000L) {
                    continue;
                }
                if (now - leaderRole.getPrevFightEndTime() < 10000L) {
                    continue;
                }
                list.add(team);
            }
            return list;
        }
        catch (final Exception e) {
            e.printStackTrace();
            this.logger.error("所有在场景中的队伍出错了--------------------------------------");
            return new ArrayList<Team>();
        }
    }
    
    private void match(final List<Team> allList, final List<Team> list) {
        try {
            final HashSet<Integer> hashSet = new HashSet<Integer>();
            for (final Team team : list) {
                if (hashSet.contains(team.getId())) {
                    continue;
                }
                final Team pkTeam = this.getPkTeam(team, allList, list, hashSet);
                if (pkTeam == null) {
                    continue;
                }
                pkTeam.setLastTeamId(team.getId());
                team.setLastTeamId(pkTeam.getId());
                this.doStartFight(team, pkTeam);
                hashSet.add(team.getId());
                hashSet.add(pkTeam.getId());
                final AtWillData atWillData = this.atWillHandler.getAtWillData();
                AtWillPkData atWillPkData = atWillData.getPkDataList().stream().filter(e -> e.getTeamId() == team.getId()).findFirst().orElse(null);
                if (atWillPkData != null) {
                    atWillPkData.updateTeamMember(team, (byte)1);
                }
                else {
                    this.logger.error("1.更新组队成员获取信息错误：队伍ID = {}", team.getId());
                }
                atWillPkData = atWillData.getPkDataList().stream().filter(e -> e.getTeamId() == pkTeam.getId()).findFirst().orElse(null);
                if (atWillPkData != null) {
                    atWillPkData.updateTeamMember(pkTeam, (byte)1);
                }
                else {
                    this.logger.error("2.更新组队成员获取信息错误：队伍ID = {}", team.getId());
                }
            }
        }
        catch (final Exception e) {
            this.logger.error("进行PK出错------------------------------------");
            e.printStackTrace();
        }
    }
    
    private Team getPkTeam(final Team team, final List<Team> allList, final List<Team> list, final HashSet<Integer> hashSet) {
        try {
            final HashSet<Integer> teamHash = new HashSet<Integer>();
            for (int i = 0; i < team.getList().size(); ++i) {
                final Member member = team.getList().get(i);
                teamHash.add(member.getRoleId());
            }
            final ArrayList<Team> result = new ArrayList<Team>();
            for (final Team tempTeam : list) {
                if (tempTeam.getId() == team.getId()) {
                    continue;
                }
                if (hashSet.contains(tempTeam.getId())) {
                    continue;
                }
                boolean boo = false;
                for (int j = 0; j < tempTeam.getList().size(); ++j) {
                    final Member member2 = tempTeam.getList().get(j);
                    if (teamHash.contains(member2.getRoleId())) {
                        boo = true;
                        break;
                    }
                }
                if (boo) {
                    continue;
                }
                result.add(tempTeam);
            }
            if (result.size() <= 0) {
                this.logger.info("没有空闲的队伍 = {}，在场景中队伍数量 = {}，在场景可以战斗的队伍数量 = {}", new Object[] { team.getId(), allList.size(), list.size() });
                return null;
            }
            if (allList.size() <= 2) {
                final int index = new Random().nextInt(result.size());
                final Team last = result.get(index);
                this.logger.info("连续匹配同一个队伍=={}=={}", last.getId(), team.getId());
                return last;
            }
            final ArrayList<Team> arrayList = new ArrayList<Team>();
            for (final Team tempTeam2 : result) {
                if (tempTeam2.getId() == team.getLastTeamId()) {
                    continue;
                }
                arrayList.add(tempTeam2);
            }
            if (arrayList.size() <= 0) {
                this.logger.info("没有匹配到队伍=={}", team.getId());
                return null;
            }
            if (arrayList.size() > 3) {
                final Team last = ActivityGlobalFunc.getApproximate(team, arrayList);
                if (last != null) {
                    return last;
                }
            }
            final int index2 = new Random().nextInt(result.size());
            final Team last2 = result.get(index2);
            this.logger.info("不连续匹配同一个队伍=={}=={}", last2.getId(), team.getId());
            return last2;
        }
        catch (final Exception e) {
            e.printStackTrace();
            this.logger.error("获取PK队伍错误------------------------------------------");
            return null;
        }
    }
    
    public void doStartFight(final Team teamA, final Team teamB) {
        final int mapId = 4154;
        final int fightType = this.getFightType();
        EventsUtils.clashPlans(teamA.getList(), teamB.getList(), 4154, 10);
        SchedulerManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                final Fight fight = FightFactory.getFightFactory(fightType).create(mapId, AtWillPkHandler.this.getFightType(), teamA.getList(), teamB.getList());
                fight.setMaxRound(30);
                fight.start();
            }
        }, 10000L);
    }
    
    protected int getFightType() {
        return FIGHT_TYPE.AT_WILL_PK.getValue();
    }
    
    @Override
    public void handleFightEnd(final FightEndEvent fightEndEvent) {
        ThreadLocalUtil.addLocalTask(fightEndEvent.getRole(), () -> this.doFightEnd(fightEndEvent.getRole(), fightEndEvent));
    }
    
    public void doFightEnd(final Role role, final FightEndEvent fightEndEvent) {
        try {
            if (this.teamService.isInTeam(role) && !this.teamService.isTeamLeader(role)) {
                return;
            }
            if (!this.isMatchFightType(fightEndEvent.getFightType())) {
                return;
            }
            if (!role.getPos().getMapId().equals(4154)) {
                return;
            }
            final AtWillData atWillData = this.atWillHandler.getAtWillData();
            final Team team = this.teamService.getTeam(role.getRoleId());
            if (team == null) {
                return;
            }
            final AtWillPkData atWillPkData = atWillData.getPkDataList().stream().filter(e -> e.getTeamId() == team.getId()).findFirst().orElse(null);
            if (atWillPkData != null) {
                atWillPkData.updateTeamMember(team, (byte)0);
            }
            else {
                this.logger.error("战斗结束后获取队伍ID信息失败");
            }
            final RespAtClashResult resp = new RespAtClashResult();
            resp.setMateSecond(this.atWillHandler.getAtWillSet().getPeriod());
            if (fightEndEvent.getStatus() == 1) {
                resp.setResult((byte)1);
                if (atWillData.getStage() == 2 || System.currentTimeMillis() >= atWillData.getEndTime().getTimeInMillis()) {
                    resp.setMateSecond(-1);
                }
                for (int i = 0; i < team.getList().size(); ++i) {
                    final Member member = team.getList().get(i);
                    final Role player = this.roleService.getPlayer(member.getId());
                    if (player != null) {
                        final List<FiedValue> values = this.sendAward(player, (byte)1);
                        resp.setList(values);
                        MessagePusher.pushMessage(player, resp);
                    }
                }
                this.doFightWin(role, fightEndEvent);
            }
            else {
                for (int i = 0; i < team.getList().size(); ++i) {
                    final Member member = team.getList().get(i);
                    final Role player = this.roleService.getPlayer(member.getId());
                    if (player != null) {
                        final List<FiedValue> values = this.sendAward(player, (byte)2);
                        resp.setList(values);
                        if (atWillData.getStage() == 2 || System.currentTimeMillis() >= atWillData.getEndTime().getTimeInMillis()) {
                            resp.setMateSecond(-1);
                        }
                        MessagePusher.pushMessage(player, resp);
                    }
                }
                this.doFightFail(role, fightEndEvent);
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
            this.logger.error("战斗结束后操作错误-------------------------------------------------------");
        }
    }
    
    private boolean isMatchFightType(final int fightType) {
        return this.getFightType() == fightType;
    }
    
    public void doFightWin(final Role role, final FightEndEvent fightEndEvent) {
        if (!this.isMatchFightType(fightEndEvent.getFightType())) {
            return;
        }
        this.addScore(role);
        this.setPrevPkTime(role);
    }
    
    private void addScore(final Role role) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null) {
            final AtWillData atWillData = this.atWillHandler.getAtWillData();
            final AtWillPkData atWillPkData = atWillData.getPkDataList().stream().filter(e -> e.getTeamId() == team.getId()).findFirst().orElse(null);
            if (atWillPkData != null) {
                final short scorePkWin = this.atWillHandler.getAtWillSet().getScorePkWin();
                atWillPkData.addTotalScore(scorePkWin);
                final String msg = MessageFormat.format("恭喜你取得了战斗胜利，获得了{0}点积分，你当前的积分为#<R>{1}#点。", scorePkWin, atWillPkData.getScore());
                this.teamService.pushMessageToTeam(team, new RespNotifyMiscEx(msg));
            }
        }
    }
    
    private void setPrevPkTime(final Role role) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null) {
            final AtWillData atWillData = this.atWillHandler.getAtWillData();
            final AtWillPkData atWillPkData = atWillData.getPkDataList().stream().filter(e -> e.getTeamId() == team.getId()).findFirst().orElse(null);
            if (atWillPkData != null) {
                atWillPkData.setPrevPKTime(System.currentTimeMillis());
            }
        }
    }
    
    public void doFightFail(final Role role, final FightEndEvent fightEndEvent) {
        this.fail(role, fightEndEvent);
    }
    
    public void fail(final Role role, final FightEndEvent fightEndEvent) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null) {
            final AtWillData atWillData = this.atWillHandler.getAtWillData();
            final AtWillPkData atWillPkData = atWillData.getPkDataList().stream().filter(e -> e.getTeamId() == team.getId()).findFirst().orElse(null);
            if (atWillPkData != null) {
                atWillPkData.substractPkValue((short)1);
                atWillPkData.subTotalScore();
                final String msg = MessageFormat.format("真遗憾你失败了，失去了1点巅峰对决值。", atWillPkData.getPkValue());
                this.teamService.pushMessageToTeam(team, new RespNotifyMiscEx(msg));
                if (atWillPkData.getPkValue() <= 0) {
                    this.atWillHandler.leaveMap(role);
                    final String outMsg = MessageFormat.format(" 真是精彩纷呈啊！#<Y>{0}#的队伍英雄争霸赛正在如火如荼的进行！适才在高手对决中棋差一着，遗憾地败下阵来，真是太可惜了。其他还在拼搏的高手要继续努力！", role.getName());
                    SpringUtils.getChatService().sendSystem(outMsg, "role");
                }
            }
            this.setPrevPkTime(role);
        }
    }
    
    public void checkSystemMessage(final Calendar calendar) {
        final AtWillData atWillData = this.atWillHandler.getAtWillData();
        if (calendar.get(11) == atWillData.getStartTime().get(11) + 1 && calendar.get(12) % 5 == 0) {
            final int intevalMinute = this.getIntevalMinute(atWillData.getStartTime(), calendar);
            final String msg = MessageFormat.format("英雄争霸赛已经进行了#<R>{0}分钟#，大家加油啊！", intevalMinute);
            SpringUtils.getChatService().sendSystem(msg, "role");
        }
        if (calendar.get(11) == atWillData.getStartTime().get(11) + 1 && calendar.get(12) % 10 == 0) {
            final int intevalMinute = this.getIntevalMinute(calendar, atWillData.getEndTime());
            final String msg = MessageFormat.format("英雄争霸赛还剩#<R>{0}分钟#，大家要抓紧时间啦！", intevalMinute);
            SpringUtils.getChatService().sendSystem(msg, "role");
        }
    }
    
    private int getIntevalMinute(final Calendar startCalendar, final Calendar endCalendar) {
        return (int)Math.abs((endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis()) / 60000L);
    }
    
    private List<FiedValue> sendAward(final Role role, final byte type) {
        final List<FiedValue> objects = new ArrayList<FiedValue>();
        long exp = 1000000L;
        int silver = 500000;
        final List<Param> list = new ArrayList<Param>();
        if (type == 2) {
            exp = 500000L;
            silver = 250000;
            list.add(new Param(ItemClassify.RESTYPE_SILVER.getId(), silver));
            list.add(new Param(75052, 1, 75052));
            list.add(new Param(75029, 1, 75029));
        }
        else {
            list.add(new Param(ItemClassify.RESTYPE_SILVER.getId(), silver));
            list.add(new Param(75052, 2, 75052));
            list.add(new Param(75029, 2, 75029));
        }
        objects.add(new FiedValue(String.valueOf(ItemClassify.HERO_EXP.getId()), (byte)4, String.valueOf(exp)));
        SpringUtils.getRoleService().addExp(role, exp, "", false);
        final Integer combatPet = role.getExtendBox().getCombatPet();
        if (combatPet > 0) {
            final Pet pet = SpringUtils.getPetService().selectPet(role).get(combatPet);
            objects.add(new FiedValue(String.valueOf(ItemClassify.PET_EXP.getId()), (byte)4, String.valueOf(exp)));
            SpringUtils.getPetService().addExp(role, pet, exp, false);
        }
        for (final Param param : list) {
            objects.add(new FiedValue(String.valueOf(param.getK()), (byte)3, param.getV()));
        }
        SpringUtils.getBean(BagService.class).sendReward(role, list, "英雄争霸赛-战斗" + type, 0);
        return objects;
    }
    
    private static long calculate(final int grade, final int rei, final ItemClassify type) {
        if (type == ItemClassify.HERO_EXP) {
            return BigDecimalUtil.decimalScaleDown(0, 1.6E7 * (1.0 + (grade + rei * 15) / 140 * 2.5) / 30.0 / 25.59 * (0.23 + Math.pow(1.0, 0.35)) * 1.3).longValue();
        }
        if (type == ItemClassify.PET_EXP) {
            return BigDecimalUtil.decimalScaleDown(0, 1.6E7 * (1.0 + (grade + rei * 15) / 140 * 2.5) / 30.0 / 25.59 * (0.23 + Math.pow(1.0, 0.35)) * 1.3 * 1.2).longValue();
        }
        return 0L;
    }
    
    static {
        AtWillPkHandler.MATCH_FLAG = true;
    }
}
