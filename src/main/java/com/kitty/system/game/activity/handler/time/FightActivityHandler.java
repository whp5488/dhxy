package com.kitty.system.game.activity.handler.time;

import java.util.concurrent.ConcurrentHashMap;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import java.util.Iterator;
import com.kitty.system.game.map.pool.MapDataPool;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.logs.LoggerUtils;
import com.kitty.mina.task.ThreadLocalUtil;
import java.util.Collections;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.cache.RedisService;
import com.kitty.system.init.domain.vo.Move;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.enter.Position;
import java.util.List;
import java.util.Map;
import com.kitty.system.game.service.MonsterService;
import com.kitty.system.game.map.service.MapService;
import com.kitty.system.game.task.service.TaskService;
import com.kitty.system.game.npc.service.NpcService;
import com.kitty.system.boss.service.BossService;
import com.kitty.system.game.party.service.PartyService;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.pet.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.activity.service.ActivityHandler;

public abstract class FightActivityHandler extends ActivityHandler
{
    @Autowired
    protected RoleService roleService;
    @Autowired
    protected PetService petService;
    @Autowired
    protected TeamService teamService;
    @Autowired
    protected PartyService partyService;
    @Autowired
    protected BossService bossService;
    @Autowired
    protected NpcService npcService;
    @Autowired
    protected TaskService taskService;
    @Autowired
    protected MapService mapService;
    @Autowired
    protected MonsterService monsterService;
    private static Map<Integer, List<Position>> usedPotions;
    
    public FightActivityHandler() {
    }
    
    protected abstract String getNpcContent(Role role, Npc npc);
    
    protected abstract String getNpcContentNotFight(Role role, Npc bossNpc);
    
    protected abstract void doStartFight(Role role, Npc bossNpc);
    
    protected abstract int getFightType(Role role);
    
    protected abstract Npc getBossNpc(int npcId);
    
    protected abstract void clearNpcAfterWin(Role role, FightEndEvent fightEndEvent, Npc bossNpc);
    
    protected abstract void giveReward(Role role, FightEndEvent fightEndEvent, Npc bossNpc);
    
    protected abstract void clearNpcTimeOut(Npc npc);
    
    protected Position getRandomPosition(int mapId) {
        Move move = (Move)((RedisService)SpringUtils.getBean(RedisService.class)).get("move_" + mapId, Move.class);
        if (move != null) {
            List<String> list = move.getList();
            List<Position> positions = this.getUsedPositions(mapId);
            if (positions != null) {
                if (list.size() == positions.size()) {
                    positions.clear();
                    this.logger.error("{}位置已经使用完，位置总数量为:{}。开始重置!!", Integer.valueOf(mapId), Integer.valueOf(list.size()));
                }
                list.removeAll(positions);
            }
            int index = ThreadLocalRandom.current().nextInt(list.size());
            String str = (String)list.get(index);
            String[] split = str.split(",");
            if (split.length > 1) {
                Position position = new Position(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
                this.recordPosition(mapId, position);
                return position;
            }
        }
        return null;
    }
    
    private void recordPosition(int mapId, Position position) {
        List<Position> positions = (List)FightActivityHandler.usedPotions.get(Integer.valueOf(mapId));
        if (positions == null) {
            positions = Collections.synchronizedList(new ArrayList<>());
            FightActivityHandler.usedPotions.put(Integer.valueOf(mapId), positions);
        }
        positions.add(position);
    }
    
    private List<Position> getUsedPositions(int mapId) {
        return (List)FightActivityHandler.usedPotions.get(Integer.valueOf(mapId));
    }
    
    protected void removeUsedPosition(int mapId, int x, int y) {
        List<Position> positions = (List)FightActivityHandler.usedPotions.get(Integer.valueOf(mapId));
        positions.removeIf(e -> e.getX().equals(Integer.valueOf(x)) && e.getY().equals(Integer.valueOf(y)));
    }
    
    public void sendNpcContent(Role role, Npc npc) {
        String content = this.getNpcContent(role, npc);
        this.sendNpcContent(role, npc, content);
    }
    
    public void sendNpcContent(Role role, Npc npc, String content) {
        this.npcService.sendNpcContent(role, npc, content, null);
    }
    
    public void startFight(Role role, Npc npc) {
        if (role == null) {
            return;
        }
        String content = this.getNpcContentNotFight(role, npc);
        if (content != null) {
            this.sendNpcContent(role, npc, content);
            return;
        }
        if (this.isSetInFight() && !npc.getInFight().compareAndSet(false, true)) {
            return;
        }
        try {
            this.doStartFight(role, npc);
        }
        catch (Exception e) {
            this.setNotInFight(npc);
            this.logger.error("请求战斗数据错误");
            e.printStackTrace();
        }
        return;
    }
    
    public boolean isSetInFight() {
        return true;
    }
    
    @Override
    public void handleFightEnd(FightEndEvent fightEndEvent) {
        ThreadLocalUtil.addLocalTask(fightEndEvent.getRole(), () -> this.doFightEnd(fightEndEvent.getRole(), fightEndEvent));
    }
    
    public void doFightEnd(Role role, FightEndEvent fightEndEvent) {
        try {
            if (this.teamService.isInTeam(role) && !this.teamService.isTeamLeader(role)) {
                return;
            }
            if (!this.isMatchFightType(role, fightEndEvent.getFightType())) {
                return;
            }
            switch (fightEndEvent.getStatus()) {
                case 1: {
                    this.doFightWin(role, fightEndEvent);
                    break;
                }
                case 2: {
                    this.doFightFail(role, fightEndEvent);
                    break;
                }
                case 3: {
                    this.doFightRunAway(role, fightEndEvent);
                    break;
                }
                case 40: {
                    this.doFightMaxRound(role, fightEndEvent);
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            LoggerUtils.error(role.getId() + "|" + this.getClass().getSimpleName() + "|" + (int)fightEndEvent.getStatus(), new Object[0]);
            Npc npc = this.getBossNpc(fightEndEvent.getNpcId());
            if (npc != null) {
                this.setNotInFight(npc);
            }
            else {
                this.logger.error("战斗结束异常，获取战斗Npc数据为空，战斗类型：{}，NPC-ID：{}", Integer.valueOf(fightEndEvent.getFightType()), Integer.valueOf(fightEndEvent.getNpcId()));
            }
        }
    }
    
    private boolean isMatchFightType(Role role, int fightType) {
        return this.getFightType(role) == fightType;
    }
    
    public void doFightWin(Role role, FightEndEvent fightEndEvent) {
        if (!this.isMatchFightType(role, fightEndEvent.getFightType())) {
            return;
        }
        Npc bossNpc = this.getBossNpc(fightEndEvent.getNpcId());
        if (bossNpc == null) {
            return;
        }
        if (this.teamService.isInTeam(role)) {
            Team team = this.teamService.getTeam((int)role.getRoleId());
            this.doFightWinForTeam(team, fightEndEvent, bossNpc);
        }
        else {
            this.doFightWinForSingle(role, fightEndEvent, bossNpc);
        }
        this.clearNpcAfterWin(role, fightEndEvent, bossNpc);
        this.setNotInFight(bossNpc);
        return;
    }
    
    public void doFightWinForTeam(Team team, FightEndEvent fightEndEvent, Npc bossNpc) {
        int leaderRoleId = team.getLeaderRoleId();
        List<Member> members = team.getList();
        for (int i = 0; i < members.size(); ++i) {
            Member member = (Member)members.get(i);
            if (member != null && member.isInTeam() && leaderRoleId != (int)member.getRoleId()) {
                Role memberRole = this.roleService.getPlayer((int)member.getRoleId());
                if (memberRole != null) {
                    ThreadLocalUtil.addLocalTask(memberRole, () -> this.doFightWinForSingle(memberRole, fightEndEvent, bossNpc));
                }
            }
        }
        Role role = this.roleService.getPlayer(leaderRoleId);
        if (role != null) {
            this.doFightWinForSingle(role, fightEndEvent, bossNpc);
        }
    }
    
    public void doFightWinForSingle(Role role, FightEndEvent fightEndEvent, Npc npc) {
        try {
            this.giveReward(role, fightEndEvent, npc);
            role.popTempCache("drop_monster_name");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void doFightFail(Role role, FightEndEvent fightEndEvent) {
        this.fail(role, fightEndEvent);
    }
    
    public void doFightRunAway(Role role, FightEndEvent fightEndEvent) {
        this.fail(role, fightEndEvent);
    }
    
    public void doFightMaxRound(Role role, FightEndEvent fightEndEvent) {
        this.fail(role, fightEndEvent);
    }
    
    private void fail(Role role, FightEndEvent fightEndEvent) {
        Npc npc = this.getBossNpc(fightEndEvent.getNpcId());
        if (npc == null) {
            return;
        }
        this.setNotInFight(npc);
        int currentTime = (int)(System.currentTimeMillis() / 1000L);
        if (npc.getEndTime() > 0 && currentTime > npc.getEndTime()) {
            this.clearNpcTimeOut(npc);
        }
        return;
    }
    
    public void setNotInFight(Npc npc) {
        if (npc != null) {
            npc.getInFight().set(false);
            npc.setFightId(-1);
        }
    }
    
    @Override
    public void handleRoleExpire(Role role) {
        Fight fight = SpringUtils.getFightNewService().getFightByRoleId((int)role.getRoleId());
        if (fight == null) {
            return;
        }
        if (!this.isMatchFightType(role, fight.getType())) {
            return;
        }
        super.handleRoleExpire(role);
        Npc npc = null;
        if (fight != null) {
            npc = SpringUtils.getMapService().getNpc(fight.getNpcId());
        }
        this.doRoleExpire(role, fight, npc);
        return;
    }
    
    public List<Role> beFightOutTeam(Role role, Team team, FightEndEvent fightEndEvent) {
        List<Role> objects = new ArrayList<>();
        Role rA = (Role)fightEndEvent.getRolesA().stream().filter(e -> e.getId().equals(role.getId())).findFirst().orElse(null);
        if (rA != null) {
            List<Member> list = team.getList();
            for (int i = 0; i < fightEndEvent.getRolesA().size(); ++i) {
                Role fightRole = (Role)fightEndEvent.getRolesA().get(i);
                Member member = (Member)list.stream().filter(e -> e.getId().equals(fightRole.getId())).findFirst().orElse(null);
                if (member == null) {
                    objects.add(fightRole);
                }
            }
        }
        Role rB = (Role)fightEndEvent.getRolesB().stream().filter(e -> e.getId().equals(role.getId())).findFirst().orElse(null);
        return objects;
    }
    
    public void doRoleExpire(Role role, Fight fight, Npc npc) {
        TeamService teamService = SpringUtils.getTeamService();
        if (teamService.getTeamCount(role) <= 1 && npc != null) {
            if (npc.isInFight()) {
                this.setNotInFight(npc);
            }
            int currentTime = (int)(System.currentTimeMillis() / 1000L);
            if (npc.getEndTime() > 0 && currentTime > npc.getEndTime()) {
                this.clearNpcTimeOut(npc);
            }
        }
    }
    
    public List<Integer> calculateFloat(int level, int need) {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 15; ++i) {
            list.add(Integer.valueOf(level + i));
            if (level - i >= need) {
                list.add(Integer.valueOf(level - i));
            }
        }
        return list;
    }
    
    public List<Integer> scope(int scene, List<Integer> list) {
        List<Integer> newList = new ArrayList<>();
        List<Integer> levelList = (List)MapDataPool.levelScopeMap.get(Integer.valueOf(scene));
        if (levelList != null) {
            for (Integer key : list) {
                if (levelList.contains(key)) {
                    newList.add(key);
                }
                else {
                    continue;
                }
            }
            return newList;
        }
        return newList;
    }
    
    public List<SysSkill> createSkill(Integer race, Integer sex) {
        Map<Integer, Map<Integer, List<SysSkill>>> mapMap = (Map)SkillDataPool.groupSkillMap.get(race);
        Map<Integer, List<SysSkill>> listMap = (Map)mapMap.get(sex);
        Integer[] keys = (Integer[])listMap.keySet().toArray(new Integer[0]);
        int random = (int)Math.random() * keys.length;
        Integer randomKey = keys[random];
        List<SysSkill> skills = (List)listMap.get(randomKey);
        return skills;
    }
    
    public int getBossCount(Role role) {
        int allCount = 10;
        int teamCount = ((TeamService)SpringUtils.getBean(TeamService.class)).getTeamCount(role);
        if (teamCount == 0) {
            teamCount = 1;
        }
        int count = 0;
        if (teamCount == 5) {
            count = allCount;
        }
        else {
            int minCount = 4 + teamCount;
            count = ThreadLocalRandom.current().nextInt(allCount - minCount + 1) + minCount;
        }
        return count;
    }
    
    static {
        FightActivityHandler.usedPotions = new ConcurrentHashMap();
    }
}
