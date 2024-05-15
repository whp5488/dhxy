
package com.kitty.system.game.activity.handler.timing.ca;

import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.team.domain.Member;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.ranking.RankingList;
import com.kitty.system.game.activity.model.user.ca.CaRankRecord;
import com.kitty.system.game.activity.message.RespCaClashResult;
import com.kitty.mina.task.ThreadLocalUtil;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.system.game.fightnew.factory.FightFactory;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.activity.model.FightInfo;
import com.kitty.system.game.activity.common.EventsUtils;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.game.activity.model.user.ca.vo.CaFight;
import java.util.Map;
import java.util.HashMap;
import com.kitty.system.game.activity.model.user.ca.CaData;
import java.util.Calendar;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.team.domain.Team;
import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import java.util.List;
import com.kitty.system.game.activity.model.user.ca.CaPkData;
import java.util.ArrayList;
import com.kitty.system.game.activity.model.user.ca.CaSet;
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
public class CaPkHandler extends ActivityHandler
{
    @Autowired
    CaHandler caHandler;
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
    byte session;
    int period;
    private static final int MAX_ROUND;
    private static final String FINISH_TIPS = "今晚的决战长安已结束，各位可以离场，下次决战长安再战吧！#<E:90>#";
    
    public CaPkHandler() {
        this.session = 1;
        this.period = 2;
    }
    
    protected int getFightType() {
        return FIGHT_TYPE.CA_PK.getValue();
    }
    
    private boolean isMatchFightType(final int fightType) {
        return this.getFightType() == fightType;
    }
    
    public void start() {
        this.logger.error("决战长安对决开始");
        try {
            final CaSet set = this.caHandler.getCaSet();
            this.withdraw();
            this.caHandler.getCaData().setStage((byte)1);
            this.grouping();
            this.match();
            this.scheduledFuture = SchedulerManager.getInstance().scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    CaPkHandler.this.check();
                }
            }, 10000L, set.getPeriod() * 1000);
        }
        catch (final Exception e) {
            this.end();
            e.printStackTrace();
            this.logger.error("决战长安PK阶段开始错误：{}", e.getMessage());
        }
    }
    
    public void withdraw() {
        final List<Integer> clearTeamList = new ArrayList<Integer>();
        final CaSet set = this.caHandler.getCaSet();
        List<CaPkData> list = this.caHandler.getCaData().getPkDataList();
        for (final CaPkData data : list) {
            final Team team = this.teamService.getTeamByTeamId(data.getTeamId());
            if (team == null) {
                clearTeamList.add(data.getTeamId());
                for (final Long member : data.getMembers()) {
                    final Role role = this.roleService.getPlayer(member);
                    if (role != null && this.caHandler.isInMap(role)) {
                        role.pushTempCache("ca_leave", true);
                        this.teamService.leave(role, false);
                        this.caHandler.leave(role);
                    }
                }
            }
            else if (team != null && this.teamService.getTeamCount(team) < set.getMinTeamMemberCount()) {
                final Role player = this.roleService.getPlayer(team.getLeaderUid());
                if (player != null) {
                    this.caHandler.leave(player);
                }
                else {
                    this.logger.error("决战长安比赛开始前弃权,队伍人数小于参赛最小人数清理出场。获取角色错误：{}", (Object)team.getLeaderUid());
                }
                clearTeamList.add(team.getId());
            }
            else {
                boolean flag = true;
                final List<Integer> objects = this.teamService.getTeamAfkMember(team);
                for (final Integer object : objects) {
                    final Role role2 = this.roleService.getPlayer(object);
                    if (role2 != null) {
                        role2.pushTempCache("ca_leave", true);
                        this.teamService.leave(role2, false);
                        this.caHandler.leave(role2);
                        role2.pushTempCache("ca_leave", false);
                        if (this.teamService.getTeamCount(team) >= set.getMinTeamMemberCount()) {
                            continue;
                        }
                        final Role player2 = this.roleService.getPlayer(team.getLeaderUid());
                        if (player2 != null) {
                            this.caHandler.leave(player2);
                        }
                        clearTeamList.add(team.getId());
                        flag = false;
                    }
                }
                if (!flag) {
                    continue;
                }
                this.caHandler.enter(data, team);
            }
        }
        for (final int teamId : clearTeamList) {
            list.removeIf(e -> e.getTeamId() == teamId);
        }
        list = list.stream().sorted(Comparator.comparing(CaPkData::getScore, Comparator.reverseOrder()).thenComparing(CaPkData::getMemberEquipScore, Comparator.reverseOrder()).thenComparing(CaPkData::getMemberGrade, Comparator.reverseOrder())).collect(Collectors.toList());
        this.caHandler.getCaData().setPkDataList(list);
    }
    
    private void check() {
        try {
            if (this.isCanEnd()) {
                this.logger.info("决战长安结束");
                this.end();
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
            this.logger.error("决战长安匹配出错-----------------------------------------------");
        }
    }
    
    private boolean isCanEnd() {
        final CaData data = this.caHandler.getCaData();
        return data == null || this.sceneTeamCount(data) <= 1 || Calendar.getInstance().after(data.getEndTime());
    }
    
    public int sceneTeamCount(final CaData data) {
        int count = 0;
        if (data != null) {
            for (final CaPkData obj : data.getPkDataList()) {
                final Role leader = this.teamService.getTeamLeader(obj.getTeamId());
                if (leader == null) {
                    continue;
                }
                if (!this.caHandler.isInMap(leader)) {
                    continue;
                }
                ++count;
            }
        }
        return count;
    }
    
    public void end() {
        try {
            final CaData caData = this.caHandler.getCaData();
            caData.setStage((byte)2);
            this.logger.error("决战长安对决结束");
            for (final CaPkData obj : caData.getPkDataList()) {
                final Role leaderRole = this.teamService.getTeamLeader(obj.getTeamId());
                if (leaderRole == null) {
                    continue;
                }
                this.caHandler.leave(leaderRole);
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        if (this.scheduledFuture != null) {
            this.scheduledFuture.cancel(Boolean.FALSE);
        }
    }
    
    public void grouping() {
        final CaData obj = this.caHandler.getCaData();
        final Map<Integer, List<CaPkData>> map = new HashMap<>();
        for (final CaPkData caPkData : obj.getPkDataList()) {
            final Team team = this.teamService.getTeamByTeamId(caPkData.getTeamId());
            if (team == null) {
                continue;
            }
            final Role player = this.roleService.getPlayer(team.getLeaderUid());
            if (player == null) {
                continue;
            }
            if (!this.caHandler.isInMap(player)) {
                continue;
            }
            List<CaPkData> list = map.get(caPkData.getGroup());
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(caPkData);
            map.put(caPkData.getGroup(), list);
        }
        this.grouping(map);
    }
    
    public void grouping(final Map<Integer, List<CaPkData>> map) {
        for (final Integer key : map.keySet()) {
            List<CaPkData> caPkDataList = map.get(key);
            caPkDataList = caPkDataList.stream().sorted(Comparator.comparing(CaPkData::getScore, Comparator.reverseOrder()).thenComparing(CaPkData::getMemberEquipScore, Comparator.reverseOrder()).thenComparing(CaPkData::getMemberGrade, Comparator.reverseOrder())).collect(Collectors.toList());
            for (final CaPkData datum : caPkDataList) {
                if (datum.getFight().size() >= this.session) {
                    continue;
                }
                final int index = caPkDataList.indexOf(datum);
                if (index + 1 >= caPkDataList.size()) {
                    datum.getFight().add(new CaFight(-1, new ArrayList<>(), (byte)0, this.session));
                    break;
                }
                final CaPkData caPkData = caPkDataList.get(index + 1);
                CaFight caFight = datum.getFight().stream().filter(e -> e.getTeamId() == caPkData.getTeamId()).findFirst().orElse(null);
                if (caFight == null) {
                    datum.getFight().add(new CaFight(caPkData.getTeamId(), caPkData.getMembers(), (byte)0, this.session));
                    caPkData.getFight().add(new CaFight(datum.getTeamId(), datum.getMembers(), (byte)0, this.session));
                }
                else {
                    final List<CaPkData> resList = new ArrayList<>();
                    int i = index;
                    boolean flag = true;
                    while (flag) {
                        if (++i >= caPkDataList.size()) {
                            flag = false;
                        }
                        else {
                            final CaPkData pkData = caPkDataList.get(i);
                            if (pkData.getFight().size() >= this.session) {
                                continue;
                            }
                            final int teamId = pkData.getTeamId();
                            caFight = datum.getFight().stream().filter(e -> e.getTeamId() == teamId).findFirst().orElse(null);
                            if (caFight == null) {
                                datum.getFight().add(new CaFight(caPkData.getTeamId(), caPkData.getMembers(), (byte)0, this.session));
                                caPkData.getFight().add(new CaFight(datum.getTeamId(), datum.getMembers(), (byte)0, this.session));
                                flag = false;
                            }
                            else {
                                resList.add(pkData);
                            }
                        }
                    }
                    if (datum.getFight().size() < this.session && resList.size() > 0) {
                        CaPkData data = resList.get(RandomUtil.round(0, resList.size() - 1));
                        final int temId = data.getTeamId();
                        datum.getFight().add(new CaFight(data.getTeamId(), data.getMembers(), (byte)0, this.session));
                        data = caPkDataList.stream().filter(e -> e.getTeamId() == temId).findFirst().orElse(null);
                        data.getFight().add(new CaFight(datum.getTeamId(), datum.getMembers(), (byte)0, this.session));
                    }
                    else {
                        datum.getFight().add(new CaFight(-1, new ArrayList<>(), (byte)0, this.session));
                    }
                }
            }
        }
    }
    
    private void match() {
        try {
            final CaData obj = this.caHandler.getCaData();
            final List<CaPkData> objPkDataList = obj.getPkDataList();
            final Map<Integer, Integer> map = new HashMap<>(objPkDataList.size());
            for (final CaPkData data : objPkDataList) {
                if (map.get(data.getTeamId()) == null) {
                    final CaFight fight = EventsUtils.getMinNotFight(data);
                    final Team teamA = this.teamService.getTeamByTeamId(data.getTeamId());

                    if (fight.getTeamId() == -1) {
                        final FightInfo fightInfo = new FightInfo();
                        fightInfo.setItselfLeader(teamA.getLeaderUid());
                        fightInfo.setItself(data.getMembers());
                        this.doFightEnd((byte)1, fightInfo, data);
                    }
                    else {
                        if (fight == null) {
                            continue;
                        }
                        final Team teamB = this.teamService.getTeamByTeamId(fight.getTeamId());
                        this.match(teamA, teamB);
                        map.put(teamA.getId(), teamB.getId());
                        map.put(teamB.getId(), teamA.getId());
                    }
                }
            }
            this.needOver();
        }
        catch (final Exception e) {
            e.printStackTrace();
            this.logger.error("匹配对手出错-----------------------------------------------");
        }
    }
    
    public void needOver() {
        ++this.session;
        if (this.session > 3) {
            this.caHandler.needTime = 0L;
            return;
        }
        SchedulerManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    final CaData obj = CaPkHandler.this.caHandler.getCaData();
                    final List<CaPkData> objPkDataList = obj.getPkDataList();
                    for (final CaPkData caPkData : objPkDataList) {
                        final Team team = CaPkHandler.this.teamService.getTeamByTeamId(caPkData.getTeamId());
                        if (team != null) {
                            final Role player = CaPkHandler.this.roleService.getPlayer(team.getLeaderUid());
                            if (!CaPkHandler.this.caHandler.isInMap(player) || !CaPkHandler.this.fightNewService.isInFight(player)) {
                                continue;
                            }
                            final Fight fight = CaPkHandler.this.fightNewService.getFightByRoleId(player.getRoleId());
                            CaPkHandler.this.fightNewService.quitFight(fight);
                        }
                    }
                    CaPkHandler.this.grouping();
                    CaPkHandler.this.match();
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }, this.period * 60000L);
        this.caHandler.needTime = System.currentTimeMillis() + this.period * 60000L;
        System.out.println("下一场等待时间 = " + this.caHandler.needTime);
    }
    
    private void match(final Team teamA, final Team teamB) {
        if (teamA != null && teamB != null) {
            final CaData data = this.caHandler.getCaData();
            final CaPkData pkDataA = data.getPkDataList().stream().filter(e -> e.getTeamId() == teamA.getId()).findFirst().orElse(null);
            final CaPkData pkDataB = data.getPkDataList().stream().filter(e -> e.getTeamId() == teamB.getId()).findFirst().orElse(null);
            final Role playerA = this.roleService.getPlayer(teamA.getLeaderUid());
            final Role playerB = this.roleService.getPlayer(teamB.getLeaderUid());
            final boolean inMapA = this.caHandler.isInMap(playerA);
            final boolean inMapB = this.caHandler.isInMap(playerB);
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

            // 匹配双方一组还在战斗中
            boolean playerAisInFight = CaPkHandler.this.fightNewService.isInFight(playerA);
            boolean playerBisInFight = CaPkHandler.this.fightNewService.isInFight(playerB);
            if (playerAisInFight || playerBisInFight) {
                return;
            }

            this.doStartFight(teamA, teamB);
            if (pkDataA != null) {
                pkDataA.setTime(0);
                pkDataA.updateTeamMember((byte)1);
            }
            else {
                this.logger.error("1.更新组队成员获取信息错误：队伍ID = {}", teamA.getId());
            }
            if (pkDataB != null) {
                pkDataB.setTime(0);
                pkDataB.updateTeamMember((byte)1);
            }
            else {
                this.logger.error("2.更新组队成员获取信息错误：队伍ID = {}", teamB.getId());
            }
        }
    }
    
    private void doStartFight(final Team teamA, final Team teamB) {
        final int mapId = 3005;
        final int fightType = this.getFightType();
        EventsUtils.clashPlans(teamA.getList(), teamB.getList(), 3002, 10);
        SchedulerManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                final Fight fight = FightFactory.getFightFactory(fightType).create(mapId, CaPkHandler.this.getFightType(), teamA.getList(), teamB.getList());
                fight.setMaxRound(CaPkHandler.MAX_ROUND);
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
            if (!role.getPos().getMapId().equals(3005)) {
                return;
            }
            final CaData caData = this.caHandler.getCaData();
            final Team team = this.teamService.getTeam(role.getRoleId());
            if (team == null) {
                return;
            }
            final CaPkData itselfData = caData.getPkDataList().stream().filter(e -> e.getTeamId() == team.getId()).findFirst().orElse(null);
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
            final CaFight fight = this.updateFightData(itselfData, fightInfo, fightEndEvent.getStatus());
            this.doFightEnd(fightEndEvent.getStatus(), fightInfo, itselfData);
        }
        catch (final Exception e) {
            e.printStackTrace();
            this.logger.error("战斗结束后操作错误-------------------------------------------------------");
        }
    }
    
    private CaFight updateFightData(final CaPkData pkData, final FightInfo fightInfo, final byte state) {
        final CaFight fight = EventsUtils.getMinNotFight(pkData);
        if (fight == null) {
            return null;
        }
        for (final CaFight caFight : pkData.getFight()) {
            if (caFight.getTeamId() == fight.getTeamId()) {
                caFight.setState(state);
                caFight.setMembers(fightInfo.getEnemy());
                break;
            }
        }
        return fight;
    }
    
    private void doFightWin(final FightInfo fightInfo, final int score) {
        for (final Long id : fightInfo.getItself()) {
            final Role player = this.roleService.getPlayer(id);
            if (player != null) {
                this.caHandler.compensate(player, score);
            }
        }
    }
    
    public void doFightEnd(final byte result, final FightInfo fightInfo, final CaPkData itselfData) {
        try {
            final CaSet set = this.caHandler.getCaSet();
            final RespCaClashResult resp = new RespCaClashResult();
            resp.setItself(this.caHandler.fightMember(fightInfo.getItself(), fightInfo.getItselfLeader()));
            resp.setEnemy(this.caHandler.fightMember(fightInfo.getEnemy(), fightInfo.getEnemyLeader()));
            resp.setResult(result);
            if (result == 1) {
                this.doFightWin(fightInfo, set.getWinScore());
            }
            else {
                this.doFightWin(fightInfo, set.getFailScore());
            }
            itselfData.setTime(0);
            for (int i = 0; i < fightInfo.getItself().size(); ++i) {
                final Role player = this.roleService.getPlayer(fightInfo.getItself().get(i));
                if (player != null) {
                    this.sendAward(player, result);
                    this.caHandler.sendRespIcon(player);
                    final CaRankRecord record = RankingList.CA_RANK_RECORDS.get(player.getId());
                    resp.setScore((short)record.getScore());
                    resp.setMaxScore((short)(fightInfo.getItself().size() * set.getWinScore() + record.getScore()));
                    MessagePusher.pushMessage(player, resp);
                }
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public void jumpWin(final CaPkData itselfData, final CaFight fight) {
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
        final CaFight atFight = this.updateFightData(itselfData, fightInfo, (byte)1);
        this.doFightEnd((byte)1, fightInfo, itselfData);
    }
    
    private List<FiedValue> sendAward(final Role role, final byte type) {
        final List<FiedValue> objects = new ArrayList<FiedValue>();
        long heroExp = calculate(role.getLevel(), role.getRelive(), ItemClassify.HERO_EXP);
        heroExp = (long)((type == 2) ? (heroExp * 2.0) : ((double)heroExp));
        objects.add(new FiedValue(String.valueOf(ItemClassify.HERO_EXP.getId()), (byte)4, String.valueOf(heroExp)));
        this.roleService.addExp(role, heroExp, "", true);
        final Integer combatPet = role.getExtendBox().getCombatPet();
        if (combatPet > 0) {
            final PetService petService = SpringUtils.getPetService();
            final Pet pet = petService.selectPet(role).get(combatPet);
            if (pet != null) {
                long calculate = calculate(pet.getPetLv(), pet.getRebirth(), ItemClassify.PET_EXP);
                calculate = (long)((type == 2) ? (calculate * 2.0) : ((double)calculate));
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
        MAX_ROUND = WarMaxRound.WAR_MAX_ROUND_CA.getValue();
    }
}
