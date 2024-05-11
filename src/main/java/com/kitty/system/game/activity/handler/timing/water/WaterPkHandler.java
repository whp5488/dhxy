
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
import java.util.Collection;
import java.util.Collections;
import com.kitty.system.game.activity.model.user.lu.vo.WaterFight;
import java.util.LinkedHashMap;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.team.domain.Team;
import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
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
    
    protected int getFightType() {
        return FIGHT_TYPE.WATER_PK.getValue();
    }
    
    private boolean isMatchFightType(final int fightType) {
        return this.getFightType() == fightType;
    }
    
    public void start() {
        this.logger.error("水陆大会对决开始");
        try {
            final WaterSet waterSet = this.waterHandler.getWaterSet();
            this.withdraw();
            this.waterHandler.getWaterData().setStage((byte)1);
            this.grouping();
            this.match();
            this.scheduledFuture = SchedulerManager.getInstance().scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    WaterPkHandler.this.check();
                }
            }, 10000L, waterSet.getPeriod() * 1000);
        }
        catch (final Exception e) {
            this.end();
            e.printStackTrace();
            this.logger.error("水陆大会PK阶段开始错误：{}", (Object)e.getMessage());
        }
    }
    
    public void withdraw() {
        final List<Integer> clearTeamList = new ArrayList<Integer>();
        final WaterSet waterSet = this.waterHandler.getWaterSet();
        List<WaterPkData> list = this.waterHandler.getWaterData().getPkDataList();
        for (final WaterPkData data : list) {
            final Team team = this.teamService.getTeamByTeamId(data.getTeamId());
            if (team == null) {
                clearTeamList.add(data.getTeamId());
                for (final Long member : data.getMembers()) {
                    final Role role = this.roleService.getPlayer(member);
                    if (role != null && this.waterHandler.isInMap(role)) {
                        role.pushTempCache("lu_leave", true);
                        this.teamService.leave(role, false);
                        this.waterHandler.leave(role);
                    }
                }
            }
            else if (team != null && this.teamService.getTeamCount(team) < waterSet.getMinTeamMemberCount()) {
                final Role player = this.roleService.getPlayer(team.getLeaderUid());
                if (player != null) {
                    this.waterHandler.leave(player);
                }
                else {
                    this.logger.error("水陆比赛开始前弃权,队伍人数小于参赛最小人数清理出场。获取角色错误：{}", (Object)team.getLeaderUid());
                }
                clearTeamList.add(team.getId());
            }
            else {
                boolean flag = true;
                final List<Integer> objects = this.teamService.getTeamAfkMember(team);
                for (final Integer object : objects) {
                    final Role role2 = this.roleService.getPlayer(object);
                    if (role2 != null) {
                        role2.pushTempCache("lu_leave", true);
                        this.teamService.leave(role2, false);
                        this.waterHandler.leave(role2);
                        role2.pushTempCache("lu_leave", false);
                        if (this.teamService.getTeamCount(team) >= waterSet.getMinTeamMemberCount()) {
                            continue;
                        }
                        final Role player2 = this.roleService.getPlayer(team.getLeaderUid());
                        if (player2 != null) {
                            this.waterHandler.leave(player2);
                        }
                        clearTeamList.add(team.getId());
                        flag = false;
                    }
                }
                if (!flag) {
                    continue;
                }
                this.waterHandler.enter(data, team);
            }
        }
        for (final int teamId : clearTeamList) {
            list.removeIf(e -> e.getTeamId() == teamId);
        }
        list = list.stream().sorted(Comparator.comparing(WaterPkData::getScore, Comparator.reverseOrder()).thenComparing(WaterPkData::getMemberEquipScore, Comparator.reverseOrder()).thenComparing(WaterPkData::getMemberGrade, Comparator.reverseOrder())).collect(Collectors.toList());
        for (int surplus = list.size() % 4, i = 0; i < surplus; ++i) {
            final int index = list.size() - 1;
            final WaterPkData data2 = list.get(index);
            this.clearSurplus(data2);
            list.remove(index);
        }
        this.waterHandler.getWaterData().setPkDataList(list);
    }
    
    public void clearSurplus(final WaterPkData data) {
        if (data != null) {
            final Team team = this.teamService.getTeamByTeamId(data.getTeamId());
            final Role leader = this.roleService.getPlayer(team.getLeaderUid());
            if (leader != null) {
                this.waterHandler.leave(leader);
            }
            for (final Long id : data.getMembers()) {
                final Role player = this.roleService.getPlayer(id);
                if (player != null) {
                    this.waterHandler.compensate(player, 50);
                    MessagePusher.pushMessage(player, new RespMsg(WaterPkHandler.COMPENSATE_TIPS));
                }
            }
        }
    }
    
    public void grouping() {
        short rank = 1;
        LinkedHashMap<Integer, WaterPkData> map = new LinkedHashMap<Integer, WaterPkData>(4);
        final List<WaterPkData> pkDataList = this.waterHandler.getWaterData().getPkDataList();
        for (int i = 0; i < pkDataList.size(); ++i) {
            map.put(i, pkDataList.get(i));
            if (map.size() >= 4) {
                grouping(pkDataList, map, rank);
                map = new LinkedHashMap<Integer, WaterPkData>(4);
                ++rank;
            }
        }
    }
    
    public static void grouping(final List<WaterPkData> pkDataList, final LinkedHashMap<Integer, WaterPkData> map, final short rank) {
        for (final int index : map.keySet()) {
            boolean flag = true;
            final WaterPkData data = map.get(index);
            while (flag) {
                for (final int key : map.keySet()) {
                    final WaterPkData pkData = map.get(key);
                    final WaterFight fight = data.getFight().stream().filter(e -> e.getTeamId() == pkData.getTeamId()).findFirst().orElse(null);
                    if (data.getTeamId() != pkData.getTeamId() && fight == null) {
                        final byte session = getSession(data.getFight());
                        final WaterFight waterFight = pkData.getFight().stream().filter(e -> e.getSession() == session).findFirst().orElse(null);
                        if (waterFight != null) {
                            continue;
                        }
                        data.getFight().add(new WaterFight(pkData.getTeamId(), pkData.getMembers(), (byte)0, session));
                        pkData.getFight().add(new WaterFight(data.getTeamId(), data.getMembers(), (byte)0, session));
                    }
                }
                if (data.getFight().size() >= 3) {
                    flag = false;
                }
            }
            data.setRank(rank);
            final List<WaterFight> collect = data.getFight().stream().sorted(Comparator.comparing(WaterFight::getSession)).collect(Collectors.toList());
            data.setFight(collect);
            pkDataList.set(index, data);
        }
    }
    
    public static byte getSession(final List<WaterFight> list) {
        if (list.size() <= 0) {
            return 1;
        }
        final List<Byte> integers = new ArrayList<Byte>() {
            {
                this.add((byte)1);
                this.add((byte)2);
                this.add((byte)3);
            }
        };
        for (final WaterFight waterFight : list) {
            if (integers.contains(waterFight.getSession())) {
                integers.removeIf(e -> e.equals(waterFight.getSession()));
            }
        }
        return Collections.min((Collection<? extends Byte>)integers);
    }
    
    private void check() {
        try {
            if (this.isCanEnd()) {
                this.logger.info("水陆大会结束");
                this.end();
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
            this.logger.error("水陆大会匹配出错-----------------------------------------------");
        }
    }
    
    private boolean isCanEnd() {
        final WaterData data = this.waterHandler.getWaterData();
        return data == null || this.sceneTeamCount(data) <= 1 || Calendar.getInstance().after(data.getEndTime());
    }
    
    public int sceneTeamCount(final WaterData data) {
        int count = 0;
        if (data != null) {
            for (final WaterPkData obj : data.getPkDataList()) {
                final Role leader = this.teamService.getTeamLeader(obj.getTeamId());
                if (leader == null) {
                    continue;
                }
                if (!this.waterHandler.isInMap(leader)) {
                    continue;
                }
                ++count;
            }
        }
        return count;
    }
    
    public void end() {
        try {
            final WaterData waterData = this.waterHandler.getWaterData();
            waterData.setStage((byte)2);
            this.logger.error("水陆大会对决结束");
            for (final WaterPkData obj : waterData.getPkDataList()) {
                final Role leaderRole = this.teamService.getTeamLeader(obj.getTeamId());
                if (leaderRole == null) {
                    continue;
                }
                this.waterHandler.leaveMap(leaderRole);
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        if (this.scheduledFuture != null) {
            this.scheduledFuture.cancel(Boolean.FALSE);
        }
    }
    
    private void match() {
        try {
            final WaterData obj = this.waterHandler.getWaterData();
            final List<WaterPkData> objPkDataList = obj.getPkDataList();
            final Map<Integer, Integer> map = new HashMap<Integer, Integer>(objPkDataList.size());
            for (final WaterPkData data : objPkDataList) {
                if (map.get(data.getTeamId()) == null) {
                    final WaterFight fight = EventsUtils.getMinNotFight(data);
                    if (fight == null) {
                        continue;
                    }
                    final Team teamA = this.teamService.getTeamByTeamId(data.getTeamId());
                    final Team teamB = this.teamService.getTeamByTeamId(fight.getTeamId());
                    this.match(teamA, teamB);
                    map.put(teamA.getId(), teamB.getId());
                    map.put(teamB.getId(), teamA.getId());
                }
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
            this.logger.error("匹配对手出错-----------------------------------------------");
        }
    }
    
    private void match(final Team teamA, final Team teamB) {
        if (teamA != null && teamB != null) {
            final WaterData data = this.waterHandler.getWaterData();
            final WaterPkData pkDataA = data.getPkDataList().stream().filter(e -> e.getTeamId() == teamA.getId()).findFirst().orElse(null);
            final WaterPkData pkDataB = data.getPkDataList().stream().filter(e -> e.getTeamId() == teamB.getId()).findFirst().orElse(null);
            final Role playerA = this.roleService.getPlayer(teamA.getLeaderUid());
            final Role playerB = this.roleService.getPlayer(teamB.getLeaderUid());
            final boolean inMapA = this.waterHandler.isInMap(playerA);
            final boolean inMapB = this.waterHandler.isInMap(playerB);
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
                this.logger.error("1.更新组队成员获取信息错误：队伍ID = {}", (Object)teamA.getId());
            }
            if (pkDataB != null) {
                pkDataB.setTime(0);
                pkDataB.updateTeamMember((byte)1);
            }
            else {
                this.logger.error("2.更新组队成员获取信息错误：队伍ID = {}", (Object)teamB.getId());
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
                final Fight fight = FightFactory.getFightFactory(fightType).create(mapId, WaterPkHandler.this.getFightType(), teamA.getList(), teamB.getList());
                fight.setMaxRound(WaterPkHandler.MAX_ROUND);
                fight.start();
            }
        }, 10000L);
    }
    
    @Override
    public void handleFightEnd(final FightEndEvent fightEndEvent) {
        ThreadLocalUtil.addLocalTask(fightEndEvent.getRole(), () -> this.doFightEnd(fightEndEvent.getRole(), fightEndEvent));
    }
    
    private void doFightEnd(final Role role, final FightEndEvent fightEndEvent) {
        try {
            if (this.teamService.isInTeam(role) && !this.teamService.isTeamLeader(role)) {
                return;
            }
            if (!this.isMatchFightType(fightEndEvent.getFightType())) {
                return;
            }
            if (!role.getPos().getMapId().equals(3003)) {
                return;
            }
            final WaterData waterData = this.waterHandler.getWaterData();
            final Team team = this.teamService.getTeam(role.getRoleId());
            if (team == null) {
                return;
            }
            final WaterPkData itselfData = waterData.getPkDataList().stream().filter(e -> e.getTeamId() == team.getId()).findFirst().orElse(null);
            if (itselfData == null) {
                this.logger.error("战斗结束后获取队伍ID信息失败");
                return;
            }
            itselfData.updateTeamMember((byte)0);
            itselfData.subPkValue((short)1);
            final FightInfo fightInfo = EventsUtils.getFightInfo(fightEndEvent.getRolesA(), fightEndEvent.getRolesB(), role.getId());
            if (fightInfo == null) {
                return;
            }
            final WaterFight fight = this.updateFightData(itselfData, fightInfo, fightEndEvent.getStatus());
            this.doFightEnd(fightEndEvent.getStatus(), fightInfo, itselfData, fight.getSession());
        }
        catch (final Exception e) {
            e.printStackTrace();
            this.logger.error("战斗结束后操作错误-------------------------------------------------------");
        }
    }
    
    private WaterFight updateFightData(final WaterPkData pkData, final FightInfo fightInfo, final byte state) {
        final WaterFight fight = EventsUtils.getMinNotFight(pkData);
        if (fight == null) {
            return null;
        }
        for (final WaterFight waterFight : pkData.getFight()) {
            if (waterFight.getTeamId() == fight.getTeamId()) {
                waterFight.setState(state);
                waterFight.setMembers(fightInfo.getEnemy());
                break;
            }
        }
        return fight;
    }
    
    private void doFightWin(final FightInfo fightInfo, final int score, final int feats) {
        for (final Long id : fightInfo.getItself()) {
            final Role player = this.roleService.getPlayer(id);
            if (player != null) {
                this.waterHandler.compensate(player, score);
                this.roleService.addCurrency(player, Const.RES_TYPE_FEATS, (long)feats, "水陆大会", false, false);
            }
        }
    }
    
    public void doFightEnd(final byte result, final FightInfo fightInfo, final WaterPkData itselfData, final byte session) {
        try {
            final WaterSet waterSet = this.waterHandler.getWaterSet();
            final RespWaterClashResult resp = new RespWaterClashResult();
            resp.setItself(this.waterHandler.fightMember(fightInfo.getItself(), fightInfo.getItselfLeader()));
            resp.setEnemy(this.waterHandler.fightMember(fightInfo.getEnemy(), fightInfo.getEnemyLeader()));
            resp.setResult(result);
            if (result == 1) {
                final int score = waterSet.getScore() + waterSet.getRankExtraScore().getInt(itselfData.getRank() + "_SCORE", 0);
                final int feats = waterSet.getFeats() + waterSet.getRankExtraScore().getInt(itselfData.getRank() + "_FEATS", 0);
                resp.setScore((short)score);
                resp.setFeats((short)feats);
                this.doFightWin(fightInfo, score, feats);
            }
            itselfData.setTime(0);
            for (int i = 0; i < fightInfo.getItself().size(); ++i) {
                final Role player = this.roleService.getPlayer(fightInfo.getItself().get(i));
                if (player != null) {
                    this.sendAward(player, result);
                    this.waterHandler.sendRespIcon(player);
                    MessagePusher.pushMessage(player, resp);
                }
            }
            this.assigning((byte)(session + 1), itselfData);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    private void assigning(final byte session, final WaterPkData itselfData) {
        if (session > 3) {
            final Team team = this.teamService.getTeamByTeamId(itselfData.getTeamId());
            if (team == null) {
                return;
            }
            this.teamService.memberHandleThreadLocal(team, memberRole -> MessagePusher.pushMessage(memberRole, new RespMsg("今晚的水陆大会已结束，各位可以离场，下次水陆大会再战吧！#<E:90>#")));
            final long count = itselfData.getFight().stream().filter(e -> e.getState() == 1).count();
            if (count >= 3L) {
                final BagService bagService = SpringUtils.getBean(BagService.class);
                this.teamService.memberHandleThreadLocal(team, memberRole -> bagService.addGoods(memberRole, 30023, 1, 0, true, true, "水陆三胜"));
            }
        }
        else {
            final WaterFight needFight = itselfData.getFight().stream().filter(e -> e.getSession() == session).findFirst().orElse(null);
            final WaterPkData enemyData = this.waterHandler.getWaterData().getPkDataList().stream().filter(e -> e.getTeamId() == needFight.getTeamId()).findFirst().orElse(null);
            if (enemyData == null) {
                this.jumpWin(itselfData, needFight);
                return;
            }
            final Team enemyTeam = this.teamService.getTeamByTeamId(enemyData.getTeamId());
            if (enemyTeam == null) {
                this.jumpWin(itselfData, needFight);
                return;
            }
            final Role player = this.roleService.getPlayer(enemyTeam.getLeaderUid());
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
                final int time = new Long(System.currentTimeMillis() / 1000L).intValue() + 60;
                enemyData.setTime(time);
                itselfData.setTime(time);
                this.teamService.memberHandleThreadLocal(itselfTeam, memberRole -> this.waterHandler.sendRespIcon(memberRole));
                this.teamService.memberHandleThreadLocal(enemyTeam, memberRole -> this.waterHandler.sendRespIcon(memberRole));
                SchedulerManager.getInstance().schedule(() -> this.awaitFight(itselfTeam, enemyTeam), 60000L);
            }
            else {
                final Team itselfTeam = this.teamService.getTeamByTeamId(itselfData.getTeamId());
                final ScheduledFuture<?> schedule = SchedulerManager.getInstance().scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        WaterPkHandler.this.scheduler(itselfTeam.getId(), enemyTeam.getId());
                    }
                }, 0L, 60000L);
                itselfData.setSchedule(schedule);
            }
        }
    }
    
    public void awaitFight(final Team teamA, final Team teamB) {
        this.match(teamA, teamB);
    }
    
    public void scheduler(final int itselfTeamId, final int enemyTeamId) {
        final WaterPkData itselfData = this.waterHandler.getWaterData().getPkDataList().stream().filter(e -> e.getTeamId() == itselfTeamId).findFirst().orElse(null);
        final WaterFight needFight = itselfData.getFight().stream().filter(e -> e.getTeamId() == enemyTeamId).findFirst().orElse(null);
        final Team enemyTeam = this.teamService.getTeamByTeamId(enemyTeamId);
        if (enemyTeam == null) {
            this.cancelTask(itselfData.getSchedule());
            this.jumpWin(itselfData, needFight);
            return;
        }
        final Role player = this.roleService.getPlayer(enemyTeam.getLeaderUid());
        if (player == null) {
            this.cancelTask(itselfData.getSchedule());
            this.jumpWin(itselfData, needFight);
            return;
        }
        if (!this.waterHandler.isInMap(player)) {
            this.cancelTask(itselfData.getSchedule());
            this.jumpWin(itselfData, needFight);
        }
    }
    
    public void jumpWin(final WaterPkData itselfData, final WaterFight fight) {
        final Team itselfTeam = this.teamService.getTeamByTeamId(itselfData.getTeamId());
        if (itselfTeam == null) {
            return;
        }
        final List<Role> listA = new ArrayList<Role>();
        for (int i = 0; i < itselfTeam.getList().size(); ++i) {
            final Member member = itselfTeam.getList().get(i);
            if (member != null) {
                if (member.isInTeam()) {
                    final Role player = this.roleService.getPlayer(member.getId());
                    if (player != null) {
                        listA.add(player);
                    }
                }
            }
        }
        final List<Role> listB = new ArrayList<Role>();
        for (final Long id : fight.getMembers()) {
            final Role player2 = this.roleService.getPlayer(id);
            if (player2 != null) {
                listA.add(player2);
            }
        }
        final FightInfo fightInfo = EventsUtils.getFightInfo(listA, listB, itselfTeam.getLeaderUid());
        if (fightInfo == null) {
            return;
        }
        final WaterFight atFight = this.updateFightData(itselfData, fightInfo, (byte)1);
        this.doFightEnd((byte)1, fightInfo, itselfData, atFight.getSession());
    }
    
    private void cancelTask(final ScheduledFuture<?> task) {
        if (task != null) {
            task.cancel(Boolean.FALSE);
        }
    }
    
    private List<FiedValue> sendAward(final Role role, final byte type) {
        final List<FiedValue> objects = new ArrayList<FiedValue>();
        long heroExp = calculate(role.getLevel(), role.getRelive(), ItemClassify.HERO_EXP);
        heroExp = (long)((type == 2) ? (heroExp * 0.75) : ((double)heroExp));
        objects.add(new FiedValue(String.valueOf(ItemClassify.HERO_EXP.getId()), (byte)4, String.valueOf(heroExp)));
        this.roleService.addExp(role, heroExp, "", true);
        final Integer combatPet = role.getExtendBox().getCombatPet();
        if (combatPet > 0) {
            final PetService petService = SpringUtils.getPetService();
            final Pet pet = petService.selectPet(role).get(combatPet);
            if (pet != null) {
                long calculate = calculate(pet.getPetLv(), pet.getRebirth(), ItemClassify.PET_EXP);
                calculate = (long)((type == 2) ? (calculate * 0.75) : ((double)calculate));
                objects.add(new FiedValue(String.valueOf(ItemClassify.PET_EXP.getId()), (byte)4, String.valueOf(calculate)));
                petService.addExp(role, pet, calculate, true);
            }
        }
        return objects;
    }
    
    private static long calculate(final int grade, final int rei, final ItemClassify type) {
        if (type == ItemClassify.HERO_EXP) {
            return BigDecimalUtil.decimalScaleDown(0, 1.9E7 * (1.0 + (grade + rei * 15.0) / 140.0 * 2.5) / 30.0 / 1.39 * (0.23 + Math.pow(1.0, 0.35)) * 1.3).longValue();
        }
        if (type == ItemClassify.PET_EXP) {
            return BigDecimalUtil.decimalScaleDown(0, 1.9E7 * (1.0 + (grade + rei * 15.0) / 140.0 * 2.5) / 30.0 / 1.39 * (0.23 + Math.pow(1.0, 0.35)) * 1.3 * 1.2).longValue();
        }
        return 0L;
    }
    
    static {
        MAX_ROUND = WarMaxRound.WAR_MAX_ROUND_LU.getValue();
        COMPENSATE_TIPS = String.format("你处在了水陆大会的最后一组而且不足4队无法比赛了，水陆举办人见你们太艰难，给予了你们%d水陆积分,下轮要继续加油啊少年", 50);
    }
}
