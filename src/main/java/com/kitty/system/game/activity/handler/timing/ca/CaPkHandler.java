package com.kitty.system.game.activity.handler.timing.ca;

import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.team.domain.Member;
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
import java.util.stream.Collectors;
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
    
    private boolean isMatchFightType(int fightType) {
        return this.getFightType() == fightType;
    }
    
    public void start() {
        this.logger.error("决战长安对决开始");
        try {
            CaSet set = this.caHandler.getCaSet();
            this.withdraw();
            this.caHandler.getCaData().setStage((byte)1);
            this.grouping();
            this.match();
            this.scheduledFuture = SchedulerManager.getInstance().scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    CaPkHandler.this.check();
                }
            }, 10000L, (long)(set.getPeriod() * 1000));
        }
        catch (Exception e) {
            this.end();
            e.printStackTrace();
            this.logger.error("决战长安PK阶段开始错误：{}", e.getMessage());
        }
    }
    
    public void withdraw() {
        List<Integer> clearTeamList = new ArrayList<>();
        CaSet set = this.caHandler.getCaSet();
        List<CaPkData> list = this.caHandler.getCaData().getPkDataList();
        for (CaPkData data : list) {
            Team team = this.teamService.getTeamByTeamId(data.getTeamId());
            if (team == null) {
                clearTeamList.add(Integer.valueOf(data.getTeamId()));
                for (Long member : data.getMembers()) {
                    Role role2 = this.roleService.getPlayer((long)member);
                    if (role2 != null && this.caHandler.isInMap(role2)) {
                        role2.pushTempCache("ca_leave", Boolean.valueOf(true));
                        this.teamService.leave(role2, false);
                        this.caHandler.leave(role2);
                    }
                    else {
                        continue;
                    }
                }
            }
            else if (team != null && this.teamService.getTeamCount(team) < set.getMinTeamMemberCount()) {
                Role player2 = this.roleService.getPlayer(team.getLeaderUid());
                if (player2 != null) {
                    this.caHandler.leave(player2);
                }
                else {
                    this.logger.error("决战长安比赛开始前弃权,队伍人数小于参赛最小人数清理出场。获取角色错误：{}", Long.valueOf(team.getLeaderUid()));
                }
                clearTeamList.add(Integer.valueOf(team.getId()));
            }
            else {
                boolean flag = true;
                List<Integer> objects = this.teamService.getTeamAfkMember(team);
                for (Integer object : objects) {
                    Role role = this.roleService.getPlayer((int)object);
                    if (role != null) {
                        role.pushTempCache("ca_leave", Boolean.valueOf(true));
                        this.teamService.leave(role, false);
                        this.caHandler.leave(role);
                        role.pushTempCache("ca_leave", Boolean.valueOf(false));
                        if (this.teamService.getTeamCount(team) < set.getMinTeamMemberCount()) {
                            Role player = this.roleService.getPlayer(team.getLeaderUid());
                            if (player != null) {
                                this.caHandler.leave(player);
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
                    this.caHandler.enter(data, team);
                }
                else {
                    continue;
                }
            }
        }
        for (int teamId : clearTeamList) {
            list.removeIf(e -> e.getTeamId() == teamId);
        }
        list = (List)list.stream().sorted(Comparator.comparing(CaPkData::getScore, Comparator.reverseOrder()).thenComparing(CaPkData::getMemberEquipScore, Comparator.reverseOrder()).thenComparing(CaPkData::getMemberGrade, Comparator.reverseOrder())).collect(Collectors.toList());
        this.caHandler.getCaData().setPkDataList(list);
    }
    
    private void check() {
        try {
            if (this.isCanEnd()) {
                this.logger.info("决战长安结束");
                this.end();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            this.logger.error("决战长安匹配出错-----------------------------------------------");
        }
    }
    
    private boolean isCanEnd() {
        CaData data = this.caHandler.getCaData();
        return data == null || this.sceneTeamCount(data) <= 1 || Calendar.getInstance().after(data.getEndTime());
    }
    
    public int sceneTeamCount(CaData data) {
        int count = 0;
        if (data != null) {
            for (CaPkData obj : data.getPkDataList()) {
                Role leader = this.teamService.getTeamLeader(obj.getTeamId());
                if (leader == null) {
                    continue;
                }
                if (this.caHandler.isInMap(leader)) {
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
            CaData caData = this.caHandler.getCaData();
            caData.setStage((byte)2);
            this.logger.error("决战长安对决结束");
            for (CaPkData obj : caData.getPkDataList()) {
                Role leaderRole = this.teamService.getTeamLeader(obj.getTeamId());
                if (leaderRole == null) {
                    continue;
                }
                this.caHandler.leave(leaderRole);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (this.scheduledFuture != null) {
            this.scheduledFuture.cancel((boolean)Boolean.FALSE);
        }
    }
    
    public void grouping() {
        CaData obj = this.caHandler.getCaData();
        Map<Integer, List<CaPkData>> map = new HashMap();
        for (CaPkData caPkData : obj.getPkDataList()) {
            Team team = this.teamService.getTeamByTeamId(caPkData.getTeamId());
            if (team == null) {
                continue;
            }
            Role player = this.roleService.getPlayer(team.getLeaderUid());
            if (player == null) {
                continue;
            }
            if (!this.caHandler.isInMap(player)) {
                continue;
            }
            List<CaPkData> list = (List)map.get(Integer.valueOf(caPkData.getGroup()));
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(caPkData);
            map.put(Integer.valueOf(caPkData.getGroup()), list);
        }
        this.grouping(map);
    }
    
    public void grouping(Map<Integer, List<CaPkData>> map) {
        for (Integer key : map.keySet()) {
            List<CaPkData> caPkDataList = (List)map.get(key);
            caPkDataList = (List)caPkDataList.stream().sorted(Comparator.comparing(CaPkData::getScore, Comparator.reverseOrder()).thenComparing(CaPkData::getMemberEquipScore, Comparator.reverseOrder()).thenComparing(CaPkData::getMemberGrade, Comparator.reverseOrder())).collect(Collectors.toList());
            for (CaPkData datum : caPkDataList) {
                if (datum.getFight().size() >= this.session) {
                    continue;
                }
                int index = caPkDataList.indexOf(datum);
                if (index + 1 >= caPkDataList.size()) {
                    datum.getFight().add(new CaFight(-1, new ArrayList<>(), (byte)0, this.session));
                    break;
                }
                CaPkData caPkData = (CaPkData)caPkDataList.get(index + 1);
                CaFight caFight = (CaFight)datum.getFight().stream().filter(e -> e.getTeamId() == caPkData.getTeamId()).findFirst().orElse(null);
                if (caFight == null) {
                    datum.getFight().add(new CaFight(caPkData.getTeamId(), caPkData.getMembers(), (byte)0, this.session));
                    caPkData.getFight().add(new CaFight(datum.getTeamId(), datum.getMembers(), (byte)0, this.session));
                }
                else {
                    List<CaPkData> resList = new ArrayList<>();
                    int i = index;
                    boolean flag = true;
                    while (flag) {
                        if (++i >= caPkDataList.size()) {
                            flag = false;
                        }
                        else {
                            CaPkData pkData = (CaPkData)caPkDataList.get(i);
                            if (pkData.getFight().size() >= this.session) {
                                continue;
                            }
                            int teamId = pkData.getTeamId();
                            caFight = (CaFight)datum.getFight().stream().filter(e -> e.getTeamId() == teamId).findFirst().orElse(null);
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
                        CaPkData data = (CaPkData)resList.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(resList.size() - 1)));
                        int temId = data.getTeamId();
                        datum.getFight().add(new CaFight(data.getTeamId(), data.getMembers(), (byte)0, this.session));
                        data = (CaPkData)caPkDataList.stream().filter(e -> e.getTeamId() == temId).findFirst().orElse(null);
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
            CaData obj = this.caHandler.getCaData();
            List<CaPkData> objPkDataList = obj.getPkDataList();
            Map<Integer, Integer> map = new HashMap(objPkDataList.size());
            for (CaPkData data : objPkDataList) {
                if (map.get(Integer.valueOf(data.getTeamId())) == null) {
                    CaFight fight = EventsUtils.getMinNotFight(data);
                    Team teamA = this.teamService.getTeamByTeamId(data.getTeamId());
                    if (fight.getTeamId() == -1) {
                        FightInfo fightInfo = new FightInfo();
                        fightInfo.setItselfLeader(teamA.getLeaderUid());
                        fightInfo.setItself(data.getMembers());
                        this.doFightEnd((byte)1, fightInfo, data);
                    }
                    else if (fight != null) {
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
            this.needOver();
        }
        catch (Exception e) {
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
                    CaData obj = CaPkHandler.this.caHandler.getCaData();
                    List<CaPkData> objPkDataList = obj.getPkDataList();
                    for (CaPkData caPkData : objPkDataList) {
                        Team team = CaPkHandler.this.teamService.getTeamByTeamId(caPkData.getTeamId());
                        if (team != null) {
                            Role player = CaPkHandler.this.roleService.getPlayer(team.getLeaderUid());
                            if (CaPkHandler.this.caHandler.isInMap(player) && CaPkHandler.this.fightNewService.isInFight(CaPkHandler.this.roleService.getPlayer(team.getLeaderUid()))) {
                                Fight fight = CaPkHandler.this.fightNewService.getFightByRoleId((int)player.getRoleId());
                                CaPkHandler.this.fightNewService.quitFight(fight);
                            }
                            else {
                                continue;
                            }
                        }
                        else {
                            continue;
                        }
                    }
                    CaPkHandler.this.grouping();
                    CaPkHandler.this.match();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, (long)this.period * 60000L);
        this.caHandler.needTime = System.currentTimeMillis() + (long)this.period * 60000L;
        System.out.println("下一场等待时间 = " + this.caHandler.needTime);
        return;
    }
    
    private void match(Team teamA, Team teamB) {
        if (teamA != null && teamB != null) {
            CaData data = this.caHandler.getCaData();
            CaPkData pkDataA = (CaPkData)data.getPkDataList().stream().filter(e -> e.getTeamId() == teamA.getId()).findFirst().orElse(null);
            CaPkData pkDataB = (CaPkData)data.getPkDataList().stream().filter(e -> e.getTeamId() == teamB.getId()).findFirst().orElse(null);
            Role playerA = this.roleService.getPlayer(teamA.getLeaderUid());
            Role playerB = this.roleService.getPlayer(teamB.getLeaderUid());
            boolean inMapA = this.caHandler.isInMap(playerA);
            boolean inMapB = this.caHandler.isInMap(playerB);
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
        final int mapId = 3005;
        final int fightType = this.getFightType();
        EventsUtils.clashPlans(teamA.getList(), teamB.getList(), 3002, 10);
        SchedulerManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                Fight fight = FightFactory.getFightFactory(fightType).create(mapId, Integer.valueOf(CaPkHandler.this.getFightType()), teamA.getList(), teamB.getList());
                fight.setMaxRound(CaPkHandler.MAX_ROUND);
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
            if (!role.getPos().getMapId().equals(Integer.valueOf(3005))) {
                return;
            }
            CaData caData = this.caHandler.getCaData();
            Team team = this.teamService.getTeam((int)role.getRoleId());
            if (team == null) {
                return;
            }
            CaPkData itselfData = (CaPkData)caData.getPkDataList().stream().filter(e -> e.getTeamId() == team.getId()).findFirst().orElse(null);
            if (itselfData != null) {
                itselfData.updateTeamMember((byte)0);
                itselfData.subPkValue((short)1);
                FightInfo fightInfo = EventsUtils.getFightInfo(fightEndEvent.getRolesA(), fightEndEvent.getRolesB(), (long)role.getId());
                if (fightInfo == null) {
                    return;
                }
                CaFight fight = this.updateFightData(itselfData, fightInfo, fightEndEvent.getStatus());
                this.doFightEnd(fightEndEvent.getStatus(), fightInfo, itselfData);
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
    
    private CaFight updateFightData(CaPkData pkData, FightInfo fightInfo, byte state) {
        CaFight fight = EventsUtils.getMinNotFight(pkData);
        if (fight == null) {
            return null;
        }
        for (CaFight caFight : pkData.getFight()) {
            if (caFight.getTeamId() == fight.getTeamId()) {
                caFight.setState(state);
                caFight.setMembers(fightInfo.getEnemy());
                break;
            }
            continue;
        }
        return fight;
    }
    
    private void doFightWin(FightInfo fightInfo, int score) {
        for (Long id : fightInfo.getItself()) {
            Role player = this.roleService.getPlayer((long)id);
            if (player != null) {
                this.caHandler.compensate(player, score);
            }
            else {
                continue;
            }
        }
    }
    
    public void doFightEnd(byte result, FightInfo fightInfo, CaPkData itselfData) {
        try {
            CaSet set = this.caHandler.getCaSet();
            RespCaClashResult resp = new RespCaClashResult();
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
                Role player = this.roleService.getPlayer((long)fightInfo.getItself().get(i));
                if (player != null) {
                    this.sendAward(player, result);
                    this.caHandler.sendRespIcon(player);
                    CaRankRecord record = (CaRankRecord)RankingList.CA_RANK_RECORDS.get(player.getId());
                    resp.setScore((short)record.getScore());
                    resp.setMaxScore((short)(fightInfo.getItself().size() * set.getWinScore() + record.getScore()));
                    MessagePusher.pushMessage(player, resp);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void jumpWin(CaPkData itselfData, CaFight fight) {
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
        CaFight atFight = this.updateFightData(itselfData, fightInfo, (byte)1);
        this.doFightEnd((byte)1, fightInfo, itselfData);
        return;
    }
    
    private List<FiedValue> sendAward(Role role, byte type) {
        List<FiedValue> objects = new ArrayList<>();
        long heroExp = calculate((int)role.getLevel(), (int)role.getRelive(), ItemClassify.HERO_EXP);
        heroExp = (long)((type == 2) ? ((double)heroExp * 2.0) : ((double)heroExp));
        objects.add(new FiedValue(String.valueOf(ItemClassify.HERO_EXP.getId()), (byte)4, String.valueOf(heroExp)));
        this.roleService.addExp(role, heroExp, "", true);
        Integer combatPet = role.getExtendBox().getCombatPet();
        if ((int)combatPet > 0) {
            PetService petService = SpringUtils.getPetService();
            Pet pet = (Pet)petService.selectPet(role).get(combatPet);
            if (pet != null) {
                long calculate = calculate((int)pet.getPetLv(), (int)pet.getRebirth(), ItemClassify.PET_EXP);
                calculate = (long)((type == 2) ? ((double)calculate * 2.0) : ((double)calculate));
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
        MAX_ROUND = WarMaxRound.WAR_MAX_ROUND_CA.getValue();
    }
}
