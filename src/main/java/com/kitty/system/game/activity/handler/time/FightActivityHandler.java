
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
import java.util.Collection;
import java.io.Serializable;
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
    
    protected abstract String getNpcContent(final Role p0, final Npc p1);
    
    protected abstract String getNpcContentNotFight(final Role p0, final Npc p1);
    
    protected abstract void doStartFight(final Role p0, final Npc p1);
    
    protected abstract int getFightType(final Role p0);
    
    protected abstract Npc getBossNpc(final int p0);
    
    protected abstract void clearNpcAfterWin(final Role p0, final FightEndEvent p1, final Npc p2);
    
    protected abstract void giveReward(final Role p0, final FightEndEvent p1, final Npc p2);
    
    protected abstract void clearNpcTimeOut(final Npc p0);
    
    protected Position getRandomPosition(final int mapId) {
        final Move move = SpringUtils.getBean(RedisService.class).get("move_" + mapId, Move.class);
        if (move != null) {
            final List<String> list = move.getList();
            final List<Position> positions = this.getUsedPositions(mapId);
            if (positions != null) {
                if (list.size() == positions.size()) {
                    positions.clear();
                    this.logger.error("{}位置已经使用完，位置总数量为:{}。开始重置!!", (Object)mapId, (Object)list.size());
                }
                list.removeAll(positions);
            }
            final int index = ThreadLocalRandom.current().nextInt(list.size());
            final String str = list.get(index);
            final String[] split = str.split(",");
            if (split.length > 1) {
                final Position position = new Position(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
                this.recordPosition(mapId, position);
                return position;
            }
        }
        return null;
    }
    
    private void recordPosition(final int mapId, final Position position) {
        List<Position> positions = FightActivityHandler.usedPotions.get(mapId);
        if (positions == null) {
            positions = Collections.synchronizedList(new ArrayList<Position>());
            FightActivityHandler.usedPotions.put(mapId, positions);
        }
        positions.add(position);
    }
    
    private List<Position> getUsedPositions(final int mapId) {
        return FightActivityHandler.usedPotions.get(mapId);
    }
    
    protected void removeUsedPosition(final int mapId, final int x, final int y) {
        final List<Position> positions = FightActivityHandler.usedPotions.get(mapId);
        positions.removeIf(e -> e.getX().equals(x) && e.getY().equals(y));
    }
    
    public void sendNpcContent(final Role role, final Npc npc) {
        final String content = this.getNpcContent(role, npc);
        this.sendNpcContent(role, npc, content);
    }
    
    public void sendNpcContent(final Role role, final Npc npc, final String content) {
        this.npcService.sendNpcContent(role, npc, content, null);
    }
    
    public void startFight(final Role role, final Npc npc) {
        if (role == null) {
            return;
        }
        final String content = this.getNpcContentNotFight(role, npc);
        if (content != null) {
            this.sendNpcContent(role, npc, content);
            return;
        }
        if (this.isSetInFight()) {
            if (!npc.getInFight().compareAndSet(false, true)) {
                return;
            }
        }
        try {
            this.doStartFight(role, npc);
        }
        catch (final Exception e) {
            this.setNotInFight(npc);
            this.logger.error("请求战斗数据错误");
            e.printStackTrace();
        }
    }
    
    public boolean isSetInFight() {
        return true;
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
        catch (final Exception e) {
            e.printStackTrace();
            LoggerUtils.error(role.getId() + "|" + this.getClass().getSimpleName() + "|" + fightEndEvent.getStatus(), new Object[0]);
            final Npc npc = this.getBossNpc(fightEndEvent.getNpcId());
            if (npc != null) {
                this.setNotInFight(npc);
            }
            else {
                this.logger.error("战斗结束异常，获取战斗Npc数据为空，战斗类型：{}，NPC-ID：{}", (Object)fightEndEvent.getFightType(), (Object)fightEndEvent.getNpcId());
            }
        }
    }
    
    private boolean isMatchFightType(final Role role, final int fightType) {
        return this.getFightType(role) == fightType;
    }
    
    public void doFightWin(final Role role, final FightEndEvent fightEndEvent) {
        if (!this.isMatchFightType(role, fightEndEvent.getFightType())) {
            return;
        }
        final Npc bossNpc = this.getBossNpc(fightEndEvent.getNpcId());
        if (bossNpc == null) {
            return;
        }
        if (this.teamService.isInTeam(role)) {
            final Team team = this.teamService.getTeam(role.getRoleId());
            this.doFightWinForTeam(team, fightEndEvent, bossNpc);
        }
        else {
            this.doFightWinForSingle(role, fightEndEvent, bossNpc);
        }
        this.clearNpcAfterWin(role, fightEndEvent, bossNpc);
        this.setNotInFight(bossNpc);
    }
    
    public void doFightWinForTeam(final Team team, final FightEndEvent fightEndEvent, final Npc bossNpc) {
        final int leaderRoleId = team.getLeaderRoleId();
        final List<Member> members = team.getList();
        for (int i = 0; i < members.size(); ++i) {
            final Member member = members.get(i);
            if (member != null) {
                if (member.isInTeam()) {
                    if (leaderRoleId != member.getRoleId()) {
                        final Role memberRole = this.roleService.getPlayer(member.getRoleId());
                        if (memberRole != null) {
                            ThreadLocalUtil.addLocalTask(memberRole, () -> this.doFightWinForSingle(memberRole, fightEndEvent, bossNpc));
                        }
                    }
                }
            }
        }
        final Role role = this.roleService.getPlayer(leaderRoleId);
        if (role != null) {
            this.doFightWinForSingle(role, fightEndEvent, bossNpc);
        }
    }
    
    public void doFightWinForSingle(final Role role, final FightEndEvent fightEndEvent, final Npc npc) {
        try {
            this.giveReward(role, fightEndEvent, npc);
            role.popTempCache("drop_monster_name");
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public void doFightFail(final Role role, final FightEndEvent fightEndEvent) {
        this.fail(role, fightEndEvent);
    }
    
    public void doFightRunAway(final Role role, final FightEndEvent fightEndEvent) {
        this.fail(role, fightEndEvent);
    }
    
    public void doFightMaxRound(final Role role, final FightEndEvent fightEndEvent) {
        this.fail(role, fightEndEvent);
    }
    
    private void fail(final Role role, final FightEndEvent fightEndEvent) {
        final Npc npc = this.getBossNpc(fightEndEvent.getNpcId());
        if (npc == null) {
            return;
        }
        this.setNotInFight(npc);
        final int currentTime = (int)(System.currentTimeMillis() / 1000L);
        if (npc.getEndTime() > 0 && currentTime > npc.getEndTime()) {
            this.clearNpcTimeOut(npc);
        }
    }
    
    public void setNotInFight(final Npc npc) {
        if (npc != null) {
            npc.getInFight().set(false);
            npc.setFightId(-1);
        }
    }
    
    @Override
    public void handleRoleExpire(final Role role) {
        final Fight fight = SpringUtils.getFightNewService().getFightByRoleId(role.getRoleId());
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
    }
    
    public List<Role> beFightOutTeam(final Role role, final Team team, final FightEndEvent fightEndEvent) {
        final List<Role> objects = new ArrayList<Role>();
        final Role rA = fightEndEvent.getRolesA().stream().filter(e -> e.getId().equals(role.getId())).findFirst().orElse(null);
        if (rA != null) {
            final List<Member> list = team.getList();
            for (int i = 0; i < fightEndEvent.getRolesA().size(); ++i) {
                final Role fightRole = fightEndEvent.getRolesA().get(i);
                final Member member = list.stream().filter(e -> e.getId().equals(fightRole.getId())).findFirst().orElse(null);
                if (member == null) {
                    objects.add(fightRole);
                }
            }
        }
        final Role rB = fightEndEvent.getRolesB().stream().filter(e -> e.getId().equals(role.getId())).findFirst().orElse(null);
        if (rB != null) {}
        return objects;
    }
    
    public void doRoleExpire(final Role role, final Fight fight, final Npc npc) {
        final TeamService teamService = SpringUtils.getTeamService();
        if (teamService.getTeamCount(role) <= 1 && npc != null) {
            if (npc.isInFight()) {
                this.setNotInFight(npc);
            }
            final int currentTime = (int)(System.currentTimeMillis() / 1000L);
            if (npc.getEndTime() > 0 && currentTime > npc.getEndTime()) {
                this.clearNpcTimeOut(npc);
            }
        }
    }
    
    public List<Integer> calculateFloat(final int level, final int need) {
        final List<Integer> list = new ArrayList<Integer>();
        for (int i = 1; i <= 15; ++i) {
            list.add(level + i);
            if (level - i >= need) {
                list.add(level - i);
            }
        }
        return list;
    }
    
    public List<Integer> scope(final int scene, final List<Integer> list) {
        final List<Integer> newList = new ArrayList<Integer>();
        final List<Integer> levelList = MapDataPool.levelScopeMap.get(scene);
        if (levelList != null) {
            for (final Integer key : list) {
                if (levelList.contains(key)) {
                    newList.add(key);
                }
            }
        }
        return newList;
    }
    
    public List<SysSkill> createSkill(final Integer race, final Integer sex) {
        final Map<Integer, Map<Integer, List<SysSkill>>> mapMap = SkillDataPool.groupSkillMap.get(race);
        final Map<Integer, List<SysSkill>> listMap = mapMap.get(sex);
        final Integer[] keys = listMap.keySet().toArray(new Integer[0]);
        final int random = (int)Math.random() * keys.length;
        final Integer randomKey = keys[random];
        final List<SysSkill> skills = listMap.get(randomKey);
        return skills;
    }
    
    public int getBossCount(final Role role) {
        final int allCount = 10;
        int teamCount = SpringUtils.getBean(TeamService.class).getTeamCount(role);
        if (teamCount == 0) {
            teamCount = 1;
        }
        int count = 0;
        if (teamCount == 5) {
            count = allCount;
        }
        else {
            final int minCount = 4 + teamCount;
            count = ThreadLocalRandom.current().nextInt(allCount - minCount + 1) + minCount;
        }
        return count;
    }
    
    static {
        FightActivityHandler.usedPotions = new ConcurrentHashMap<Integer, List<Position>>();
    }
}
