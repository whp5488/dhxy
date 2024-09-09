package com.kitty.system.game.activity.handler.timing.water;

import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.team.domain.Member;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.activity.message.RespWaterClashResult;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.activity.model.FightInfo;
import com.kitty.mina.task.ThreadLocalUtil;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.factory.FightFactory;
import java.util.Map;
import com.kitty.system.game.activity.common.EventsUtils;
import java.util.HashMap;
import com.kitty.system.game.activity.model.user.lu.WaterData;
import java.util.Calendar;
import java.util.Collections;
import com.kitty.system.game.activity.model.user.lu.vo.WaterFight;
import java.util.LinkedHashMap;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.team.domain.Team;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.List;
import com.kitty.system.game.activity.model.user.lu.WaterPkData;
import java.util.ArrayList;
import com.kitty.system.game.activity.model.user.lu.WaterSet;
import com.kitty.common.core.SchedulerManager;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
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
public class WaterPkHandler extends ActivityHandler
{
    @Autowired
    WaterHandler waterHandler;
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
    private static final int SERIAL = 30023;
    private static final int MAX_ROUND;
    private static final String FINISH_TIPS = "今晚的水陆大会已结束，各位可以离场，下次水陆大会再战吧！#<E:90>#";
    private static final String COMPENSATE_TIPS;
    
    public WaterPkHandler() {
    }
    
    protected int getFightType() {
        return FIGHT_TYPE.WATER_PK.getValue();
    }
    
    private boolean isMatchFightType(int fightType) {
        return this.getFightType() == fightType;
    }
    
    public void start() {
        this.logger.error("水陆大会对决开始");
        try {
            WaterSet waterSet = this.waterHandler.getWaterSet();
            this.withdraw();
            this.waterHandler.getWaterData().setStage((byte)1);
            this.grouping();
            this.match();
            this.scheduledFuture = SchedulerManager.getInstance().scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    WaterPkHandler.this.check();
                }
            }, 10000L, (long)(waterSet.getPeriod() * 1000));
        }
        catch (Exception e) {
            this.end();
            e.printStackTrace();
            this.logger.error("水陆大会PK阶段开始错误：{}", e.getMessage());
        }
    }
    
    public void withdraw() {
        List<Integer> clearTeamList = new ArrayList<>();
        WaterSet waterSet = this.waterHandler.getWaterSet();
        List<WaterPkData> list = this.waterHandler.getWaterData().getPkDataList();
        for (WaterPkData data : list) {
            Team team = this.teamService.getTeamByTeamId(data.getTeamId());
            if (team == null) {
                clearTeamList.add(Integer.valueOf(data.getTeamId()));
                for (Long member : data.getMembers()) {
                    Role role2 = this.roleService.getPlayer((long)member);
                    if (role2 != null && this.waterHandler.isInMap(role2)) {
                        role2.pushTempCache("lu_leave", Boolean.valueOf(true));
                        this.teamService.leave(role2, false);
                        this.waterHandler.leave(role2);
                    }
                    else {
                        continue;
                    }
                }
            }
            else if (team != null && this.teamService.getTeamCount(team) < waterSet.getMinTeamMemberCount()) {
                Role player2 = this.roleService.getPlayer(team.getLeaderUid());
                if (player2 != null) {
                    this.waterHandler.leave(player2);
                }
                else {
                    this.logger.error("水陆比赛开始前弃权,队伍人数小于参赛最小人数清理出场。获取角色错误：{}", Long.valueOf(team.getLeaderUid()));
                }
                clearTeamList.add(Integer.valueOf(team.getId()));
            }
            else {
                boolean flag = true;
                List<Integer> objects = this.teamService.getTeamAfkMember(team);
                for (Integer object : objects) {
                    Role role = this.roleService.getPlayer((int)object);
                    if (role != null) {
                        role.pushTempCache("lu_leave", Boolean.valueOf(true));
                        this.teamService.leave(role, false);
                        this.waterHandler.leave(role);
                        role.pushTempCache("lu_leave", Boolean.valueOf(false));
                        if (this.teamService.getTeamCount(team) < waterSet.getMinTeamMemberCount()) {
                            Role player = this.roleService.getPlayer(team.getLeaderUid());
                            if (player != null) {
                                this.waterHandler.leave(player);
                            }
                            clearTeamList.add(Integer.valueOf(team.getId()));
                            flag = false;
                        }
                        else {
                            continue;
                        }
                    }
                    else {
                        continue;
                    }
                }
                if (flag) {
                    this.waterHandler.enter(data, team);
                }
                else {
                    continue;
                }
            }
        }
        for (int teamId : clearTeamList) {
            list.removeIf(e -> e.getTeamId() == teamId);
        }
        list = (List)list.stream().sorted(Comparator.comparing(WaterPkData::getScore, Comparator.reverseOrder()).thenComparing(WaterPkData::getMemberEquipScore, Comparator.reverseOrder()).thenComparing(WaterPkData::getMemberGrade, Comparator.reverseOrder())).collect(Collectors.toList());
        for (int surplus = list.size() % 4, i = 0; i < surplus; ++i) {
            int index = list.size() - 1;
            WaterPkData data2 = (WaterPkData)list.get(index);
            this.clearSurplus(data2);
            list.remove(index);
        }
        this.waterHandler.getWaterData().setPkDataList(list);
    }
    
    public void clearSurplus(WaterPkData data) {
        if (data != null) {
            Team team = this.teamService.getTeamByTeamId(data.getTeamId());
            Role leader = this.roleService.getPlayer(team.getLeaderUid());
            if (leader != null) {
                this.waterHandler.leave(leader);
            }
            for (Long id : data.getMembers()) {
                Role player = this.roleService.getPlayer((long)id);
                if (player != null) {
                    this.waterHandler.compensate(player, 50);
                    MessagePusher.pushMessage(player, new RespMsg(WaterPkHandler.COMPENSATE_TIPS));
                }
                else {
                    continue;
                }
            }
        }
    }
    
    public void grouping() {
        short rank = 1;
        LinkedHashMap<Integer, WaterPkData> map = new LinkedHashMap(4);
        List<WaterPkData> pkDataList = this.waterHandler.getWaterData().getPkDataList();
        for (int i = 0; i < pkDataList.size(); ++i) {
            map.put(Integer.valueOf(i), pkDataList.get(i));
            if (map.size() >= 4) {
                grouping(pkDataList, map, rank);
                map = new LinkedHashMap(4);
                ++rank;
            }
        }
    }
    
    public static void grouping(List<WaterPkData> pkDataList, LinkedHashMap<Integer, WaterPkData> map, short rank) {
        for (int index : map.keySet()) {
            boolean flag = true;
            WaterPkData data = (WaterPkData)map.get(Integer.valueOf(index));
            while (flag) {
                for (int key : map.keySet()) {
                    WaterPkData pkData = (WaterPkData)map.get(Integer.valueOf(key));
                    WaterFight fight = (WaterFight)data.getFight().stream().filter(e -> e.getTeamId() == pkData.getTeamId()).findFirst().orElse(null);
                    if (data.getTeamId() != pkData.getTeamId() && fight == null) {
                        byte session = getSession(data.getFight());
                        WaterFight waterFight = (WaterFight)pkData.getFight().stream().filter(e -> e.getSession() == session).findFirst().orElse(null);
                        if (waterFight == null) {
                            data.getFight().add(new WaterFight(pkData.getTeamId(), pkData.getMembers(), (byte)0, session));
                            pkData.getFight().add(new WaterFight(data.getTeamId(), data.getMembers(), (byte)0, session));
                        }
                        else {
                            continue;
                        }
                    }
                    else {
                        continue;
                    }
                }
                if (data.getFight().size() < 3) {
                    continue;
                }
                flag = false;
            }
            data.setRank(rank);
            List<WaterFight> collect = (List)data.getFight().stream().sorted(Comparator.comparing(WaterFight::getSession)).collect(Collectors.toList());
            data.setFight(collect);
            pkDataList.set(index, data);
        }
    }
    
    public static byte getSession(List<WaterFight> list) {
        if (list.size() <= 0) {
            return 1;
        }
        List<Byte> integers = new ArrayList<Byte>() {
            {
                this.add(Byte.valueOf((byte)1));
                this.add(Byte.valueOf((byte)2));
                this.add(Byte.valueOf((byte)3));
            }
        };
        for (WaterFight waterFight : list) {
            if (integers.contains(Byte.valueOf(waterFight.getSession()))) {
                integers.removeIf(e -> e.equals(Byte.valueOf(waterFight.getSession())));
            }
            else {
                continue;
            }
        }
        return (byte)Collections.min(integers);
    }
    
    private void check() {
        try {
            if (this.isCanEnd()) {
                this.logger.info("水陆大会结束");
                this.end();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            this.logger.error("水陆大会匹配出错-----------------------------------------------");
        }
    }
    
    private boolean isCanEnd() {
        WaterData data = this.waterHandler.getWaterData();
        return data == null || this.sceneTeamCount(data) <= 1 || Calendar.getInstance().after(data.getEndTime());
    }
    
    public int sceneTeamCount(WaterData data) {
        int count = 0;
        if (data != null) {
            for (WaterPkData obj : data.getPkDataList()) {
                Role leader = this.teamService.getTeamLeader(obj.getTeamId());
                if (leader == null) {
                    continue;
                }
                if (this.waterHandler.isInMap(leader)) {
                    ++count;
                }
                else {
                    continue;
                }
            }
        }
        return count;
    }
    
    public void end() {
        try {
            WaterData waterData = this.waterHandler.getWaterData();
            waterData.setStage((byte)2);
            this.logger.error("水陆大会对决结束");
            for (WaterPkData obj : waterData.getPkDataList()) {
                Role leaderRole = this.teamService.getTeamLeader(obj.getTeamId());
                if (leaderRole == null) {
                    continue;
                }
                this.waterHandler.leaveMap(leaderRole);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (this.scheduledFuture != null) {
            this.scheduledFuture.cancel((boolean)Boolean.FALSE);
        }
    }
    
    private void match() {
        try {
            WaterData obj = this.waterHandler.getWaterData();
            List<WaterPkData> objPkDataList = obj.getPkDataList();
            Map<Integer, Integer> map = new HashMap(objPkDataList.size());
            for (WaterPkData data : objPkDataList) {
                if (map.get(Integer.valueOf(data.getTeamId())) == null) {
                    WaterFight fight = EventsUtils.getMinNotFight(data);
                    if (fight != null) {
                        Team teamA = this.teamService.getTeamByTeamId(data.getTeamId());
                        Team teamB = this.teamService.getTeamByTeamId(fight.getTeamId());
                        this.match(teamA, teamB);
                        map.put(Integer.valueOf(teamA.getId()), Integer.valueOf(teamB.getId()));
                        map.put(Integer.valueOf(teamB.getId()), Integer.valueOf(teamA.getId()));
                    }
                    else {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            this.logger.error("匹配对手出错-----------------------------------------------");
        }
    }
    
    private void match(Team teamA, Team teamB) {
        if (teamA != null && teamB != null) {
            WaterData data = this.waterHandler.getWaterData();
            WaterPkData pkDataA = (WaterPkData)data.getPkDataList().stream().filter(e -> e.getTeamId() == teamA.getId()).findFirst().orElse(null);
            WaterPkData pkDataB = (WaterPkData)data.getPkDataList().stream().filter(e -> e.getTeamId() == teamB.getId()).findFirst().orElse(null);
            Role playerA = this.roleService.getPlayer(teamA.getLeaderUid());
            Role playerB = this.roleService.getPlayer(teamB.getLeaderUid());
            boolean inMapA = this.waterHandler.isInMap(playerA);
            boolean inMapB = this.waterHandler.isInMap(playerB);
            if (!inMapA && !inMapB) {
                return;
            }
            if (!inMapA) {
                this.jumpWin(pkDataB, EventsUtils.getMinNotFight(pkDataB));
                return;
            }
            if (!inMapB) {
                this.jumpWin(pkDataA, EventsUtils.getMinNotFight(pkDataA));
                return;
            }
            this.doStartFight(teamA, teamB);
            if (pkDataA != null) {
                pkDataA.setTime(0);
                pkDataA.updateTeamMember((byte)1);
            }
            else {
                this.logger.error("1.更新组队成员获取信息错误：队伍ID = {}", Integer.valueOf(teamA.getId()));
            }
            if (pkDataB != null) {
                pkDataB.setTime(0);
                pkDataB.updateTeamMember((byte)1);
            }
            else {
                this.logger.error("2.更新组队成员获取信息错误：队伍ID = {}", Integer.valueOf(teamB.getId()));
            }
        }
    }
    
    private void doStartFight(final Team teamA, final Team teamB) {
        final int mapId = 3003;
        final int fightType = this.getFightType();
        EventsUtils.clashPlans(teamA.getList(), teamB.getList(), 3003, 10);
        SchedulerManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                Fight fight = FightFactory.getFightFactory(fightType).create(mapId, Integer.valueOf(WaterPkHandler.this.getFightType()), teamA.getList(), teamB.getList());
                fight.setMaxRound(WaterPkHandler.MAX_ROUND);
                fight.start();
            }
        }, 10000L);
    }
    
    @Override
    public void handleFightEnd(FightEndEvent fightEndEvent) {
        ThreadLocalUtil.addLocalTask(fightEndEvent.getRole(), () -> this.doFightEnd(fightEndEvent.getRole(), fightEndEvent));
    }
    
    private void doFightEnd(Role role, FightEndEvent fightEndEvent) {
        try {
            if (this.teamService.isInTeam(role) && !this.teamService.isTeamLeader(role)) {
                return;
            }
            if (!this.isMatchFightType(fightEndEvent.getFightType())) {
                return;
            }
            if (!role.getPos().getMapId().equals(Integer.valueOf(3003))) {
                return;
            }
            WaterData waterData = this.waterHandler.getWaterData();
            Team team = this.teamService.getTeam((int)role.getRoleId());
            if (team == null) {
                return;
            }
            WaterPkData itselfData = (WaterPkData)waterData.getPkDataList().stream().filter(e -> e.getTeamId() == team.getId()).findFirst().orElse(null);
            if (itselfData != null) {
                itselfData.updateTeamMember((byte)0);
                itselfData.subPkValue((short)1);
                FightInfo fightInfo = EventsUtils.getFightInfo(fightEndEvent.getRolesA(), fightEndEvent.getRolesB(), (long)role.getId());
                if (fightInfo == null) {
                    return;
                }
                WaterFight fight = this.updateFightData(itselfData, fightInfo, fightEndEvent.getStatus());
                this.doFightEnd(fightEndEvent.getStatus(), fightInfo, itselfData, fight.getSession());
            }
            else {
                this.logger.error("战斗结束后获取队伍ID信息失败");
                return;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            this.logger.error("战斗结束后操作错误-------------------------------------------------------");
        }
    }
    
    private WaterFight updateFightData(WaterPkData pkData, FightInfo fightInfo, byte state) {
        WaterFight fight = EventsUtils.getMinNotFight(pkData);
        if (fight == null) {
            return null;
        }
        for (WaterFight waterFight : pkData.getFight()) {
            if (waterFight.getTeamId() == fight.getTeamId()) {
                waterFight.setState(state);
                waterFight.setMembers(fightInfo.getEnemy());
                break;
            }
            continue;
        }
        return fight;
    }
    
    private void doFightWin(FightInfo fightInfo, int score, int feats) {
        for (Long id : fightInfo.getItself()) {
            Role player = this.roleService.getPlayer((long)id);
            if (player != null) {
                this.waterHandler.compensate(player, score);
                this.roleService.addCurrency(player, Const.RES_TYPE_FEATS, Long.valueOf((long)feats), "水陆大会", false, false);
            }
            else {
                continue;
            }
        }
    }
    
    public void doFightEnd(byte result, FightInfo fightInfo, WaterPkData itselfData, byte session) {
        try {
            WaterSet waterSet = this.waterHandler.getWaterSet();
            RespWaterClashResult resp = new RespWaterClashResult();
            resp.setItself(this.waterHandler.fightMember(fightInfo.getItself(), fightInfo.getItselfLeader()));
            resp.setEnemy(this.waterHandler.fightMember(fightInfo.getEnemy(), fightInfo.getEnemyLeader()));
            resp.setResult(result);
            if (result == 1) {
                int score = waterSet.getScore() + waterSet.getRankExtraScore().getInt((int)itselfData.getRank() + "_SCORE", 0);
                int feats = waterSet.getFeats() + waterSet.getRankExtraScore().getInt((int)itselfData.getRank() + "_FEATS", 0);
                resp.setScore((short)score);
                resp.setFeats((short)feats);
                this.doFightWin(fightInfo, score, feats);
            }
            itselfData.setTime(0);
            for (int i = 0; i < fightInfo.getItself().size(); ++i) {
                Role player = this.roleService.getPlayer((long)fightInfo.getItself().get(i));
                if (player != null) {
                    this.sendAward(player, result);
                    this.waterHandler.sendRespIcon(player);
                    MessagePusher.pushMessage(player, resp);
                }
            }
            this.assigning((byte)(session + 1), itselfData);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void assigning(byte session, WaterPkData itselfData) {
        if (session > 3) {
            Team team = this.teamService.getTeamByTeamId(itselfData.getTeamId());
            if (team == null) {
                return;
            }
            this.teamService.memberHandleThreadLocal(team, memberRole -> MessagePusher.pushMessage(memberRole, new RespMsg("今晚的水陆大会已结束，各位可以离场，下次水陆大会再战吧！#<E:90>#")));
            long count = itselfData.getFight().stream().filter(e -> e.getState() == 1).count();
            if (count >= 3L) {
                BagService bagService = (BagService)SpringUtils.getBean(BagService.class);
                this.teamService.memberHandleThreadLocal(team, memberRole -> bagService.addGoods(memberRole, 30023, 1, 0, true, true, "水陆三胜"));
            }
            return;
        }
        else {
            WaterFight needFight = (WaterFight)itselfData.getFight().stream().filter(e -> e.getSession() == session).findFirst().orElse(null);
            WaterPkData enemyData = (WaterPkData)this.waterHandler.getWaterData().getPkDataList().stream().filter(e -> e.getTeamId() == needFight.getTeamId()).findFirst().orElse(null);
            if (enemyData == null) {
                this.jumpWin(itselfData, needFight);
                return;
            }
            final Team enemyTeam = this.teamService.getTeamByTeamId(enemyData.getTeamId());
            if (enemyTeam == null) {
                this.jumpWin(itselfData, needFight);
                return;
            }
            Role player = this.roleService.getPlayer(enemyTeam.getLeaderUid());
            if (player == null) {
                this.jumpWin(itselfData, needFight);
                return;
            }
            if (!this.waterHandler.isInMap(player)) {
                this.jumpWin(itselfData, needFight);
                return;
            }
            if (enemyData.getState() == 0) {
                final Team itselfTeam = this.teamService.getTeamByTeamId(itselfData.getTeamId());
                this.cancelTask(enemyData.getSchedule());
                int time = new Long(System.currentTimeMillis() / 1000L).intValue() + 60;
                enemyData.setTime(time);
                itselfData.setTime(time);
                this.teamService.memberHandleThreadLocal(itselfTeam, memberRole -> this.waterHandler.sendRespIcon(memberRole));
                this.teamService.memberHandleThreadLocal(enemyTeam, memberRole -> this.waterHandler.sendRespIcon(memberRole));
                SchedulerManager.getInstance().schedule(() -> this.awaitFight(itselfTeam, enemyTeam), 60000L);
            }
            else {
                final Team itselfTeam = this.teamService.getTeamByTeamId(itselfData.getTeamId());
                ScheduledFuture<?> schedule = SchedulerManager.getInstance().scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        WaterPkHandler.this.scheduler(itselfTeam.getId(), enemyTeam.getId());
                    }
                }, 0L, 60000L);
                itselfData.setSchedule(schedule);
            }
            return;
        }
    }
    
    public void awaitFight(Team teamA, Team teamB) {
        this.match(teamA, teamB);
    }
    
    public void scheduler(int itselfTeamId, int enemyTeamId) {
        WaterPkData itselfData = (WaterPkData)this.waterHandler.getWaterData().getPkDataList().stream().filter(e -> e.getTeamId() == itselfTeamId).findFirst().orElse(null);
        WaterFight needFight = (WaterFight)itselfData.getFight().stream().filter(e -> e.getTeamId() == enemyTeamId).findFirst().orElse(null);
        Team enemyTeam = this.teamService.getTeamByTeamId(enemyTeamId);
        if (enemyTeam == null) {
            this.cancelTask(itselfData.getSchedule());
            this.jumpWin(itselfData, needFight);
            return;
        }
        Role player = this.roleService.getPlayer(enemyTeam.getLeaderUid());
        if (player == null) {
            this.cancelTask(itselfData.getSchedule());
            this.jumpWin(itselfData, needFight);
            return;
        }
        if (!this.waterHandler.isInMap(player)) {
            this.cancelTask(itselfData.getSchedule());
            this.jumpWin(itselfData, needFight);
            return;
        }
        return;
    }
    
    public void jumpWin(WaterPkData itselfData, WaterFight fight) {
        Team itselfTeam = this.teamService.getTeamByTeamId(itselfData.getTeamId());
        if (itselfTeam == null) {
            return;
        }
        List<Role> listA = new ArrayList<>();
        for (int i = 0; i < itselfTeam.getList().size(); ++i) {
            Member member = (Member)itselfTeam.getList().get(i);
            if (member != null && member.isInTeam()) {
                Role player = this.roleService.getPlayer((long)member.getId());
                if (player != null) {
                    listA.add(player);
                }
            }
        }
        List<Role> listB = new ArrayList<>();
        for (Long id : fight.getMembers()) {
            Role player2 = this.roleService.getPlayer((long)id);
            if (player2 != null) {
                listA.add(player2);
            }
            else {
                continue;
            }
        }
        FightInfo fightInfo = EventsUtils.getFightInfo(listA, listB, itselfTeam.getLeaderUid());
        if (fightInfo == null) {
            return;
        }
        WaterFight atFight = this.updateFightData(itselfData, fightInfo, (byte)1);
        this.doFightEnd((byte)1, fightInfo, itselfData, atFight.getSession());
        return;
    }
    
    private void cancelTask(ScheduledFuture<?> task) {
        if (task != null) {
            task.cancel((boolean)Boolean.FALSE);
        }
    }
    
    private List<FiedValue> sendAward(Role role, byte type) {
        List<FiedValue> objects = new ArrayList<>();
        long heroExp = calculate((int)role.getLevel(), (int)role.getRelive(), ItemClassify.HERO_EXP);
        heroExp = (long)((type == 2) ? ((double)heroExp * 0.75) : ((double)heroExp));
        objects.add(new FiedValue(String.valueOf(ItemClassify.HERO_EXP.getId()), (byte)4, String.valueOf(heroExp)));
        this.roleService.addExp(role, heroExp, "", true);
        Integer combatPet = role.getExtendBox().getCombatPet();
        if ((int)combatPet > 0) {
            PetService petService = SpringUtils.getPetService();
            Pet pet = (Pet)petService.selectPet(role).get(combatPet);
            if (pet != null) {
                long calculate = calculate((int)pet.getPetLv(), (int)pet.getRebirth(), ItemClassify.PET_EXP);
                calculate = (long)((type == 2) ? ((double)calculate * 0.75) : ((double)calculate));
                objects.add(new FiedValue(String.valueOf(ItemClassify.PET_EXP.getId()), (byte)4, String.valueOf(calculate)));
                petService.addExp(role, pet, calculate, true);
            }
        }
        return objects;
    }
    
    private static long calculate(int grade, int rei, ItemClassify type) {
        if (type == ItemClassify.HERO_EXP) {
            return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(1.9E7 * (1.0 + ((double)grade + (double)rei * 15.0) / 140.0 * 2.5) / 30.0 / 1.39 * (0.23 + Math.pow(1.0, 0.35)) * 1.3)).longValue();
        }
        if (type == ItemClassify.PET_EXP) {
            return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(1.9E7 * (1.0 + ((double)grade + (double)rei * 15.0) / 140.0 * 2.5) / 30.0 / 1.39 * (0.23 + Math.pow(1.0, 0.35)) * 1.3 * 1.2)).longValue();
        }
        return 0L;
    }
    
    static {
        MAX_ROUND = WarMaxRound.WAR_MAX_ROUND_LU.getValue();
        COMPENSATE_TIPS = String.format("你处在了水陆大会的最后一组而且不足4队无法比赛了，水陆举办人见你们太艰难，给予了你们%d水陆积分,下轮要继续加油啊少年", Integer.valueOf(50));
    }
}
