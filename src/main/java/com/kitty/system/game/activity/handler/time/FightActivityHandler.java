package com.kitty.system.game.activity.handler.time;

import com.kitty.cache.RedisService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.logs.LoggerUtils;
import com.kitty.mina.task.ThreadLocalUtil;
import com.kitty.system.boss.service.BossService;
import com.kitty.system.game.activity.service.ActivityHandler;
import com.kitty.system.game.enter.Position;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.map.pool.MapDataPool;
import com.kitty.system.game.map.service.MapService;
import com.kitty.system.game.npc.service.NpcService;
import com.kitty.system.game.party.service.PartyService;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.service.MonsterService;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.game.task.service.TaskService;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.init.domain.vo.Move;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class FightActivityHandler extends ActivityHandler {
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
  
  private static Map<Integer, List<Position>> usedPotions = new ConcurrentHashMap<>();
  
  protected Position getRandomPosition(int mapId) {
    Move move = (Move)((RedisService)SpringUtils.getBean(RedisService.class)).get("move_" + mapId, Move.class);
    if (move != null) {
      List<String> list = move.getList();
      List<Position> positions = getUsedPositions(mapId);
      if (positions != null) {
        if (list.size() == positions.size()) {
          positions.clear();
          this.logger.error("{}位置已经使用完，位置总数量为:{}。开始重置!!", Integer.valueOf(mapId), Integer.valueOf(list.size()));
        } 
        list.removeAll(positions);
      } 
      int index = ThreadLocalRandom.current().nextInt(list.size());
      String str = list.get(index);
      String[] split = str.split(",");
      if (split.length > 1) {
        Position position = new Position(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
        recordPosition(mapId, position);
        return position;
      } 
    } 
    return null;
  }
  
  private void recordPosition(int mapId, Position position) {
    List<Position> positions = usedPotions.get(Integer.valueOf(mapId));
    if (positions == null) {
      positions = Collections.synchronizedList(new ArrayList<>());
      usedPotions.put(Integer.valueOf(mapId), positions);
    } 
    positions.add(position);
  }
  
  private List<Position> getUsedPositions(int mapId) {
    return usedPotions.get(Integer.valueOf(mapId));
  }
  
  protected void removeUsedPosition(int mapId, int x, int y) {
    List<Position> positions = usedPotions.get(Integer.valueOf(mapId));
    positions.removeIf(e -> (e.getX().equals(Integer.valueOf(x)) && e.getY().equals(Integer.valueOf(y))));
  }
  
  public void sendNpcContent(Role role, Npc npc) {
    String content = getNpcContent(role, npc);
    sendNpcContent(role, npc, content);
  }
  
  public void sendNpcContent(Role role, Npc npc, String content) {
    this.npcService.sendNpcContent(role, npc, content, null);
  }
  
  public void startFight(Role role, Npc npc) {
    if (role == null)
      return; 
    String content = getNpcContentNotFight(role, npc);
    if (content != null) {
      sendNpcContent(role, npc, content);
      return;
    } 
    if (!isSetInFight() || npc.getInFight().compareAndSet(false, true))
      try {
        doStartFight(role, npc);
      } catch (Exception e) {
        setNotInFight(npc);
        this.logger.error("请求战斗数据错误");
        e.printStackTrace();
      }  
  }
  
  public boolean isSetInFight() {
    return true;
  }
  
  public void handleFightEnd(FightEndEvent fightEndEvent) {
    ThreadLocalUtil.addLocalTask(fightEndEvent.getRole(), () -> doFightEnd(fightEndEvent.getRole(), fightEndEvent));
  }
  
  public void doFightEnd(Role role, FightEndEvent fightEndEvent) {
    try {
      if (this.teamService.isInTeam(role) && !this.teamService.isTeamLeader(role))
        return; 
      if (!isMatchFightType(role, fightEndEvent.getFightType()))
        return; 
      switch (fightEndEvent.getStatus()) {
        case 1:
          doFightWin(role, fightEndEvent);
          break;
        case 2:
          doFightFail(role, fightEndEvent);
          break;
        case 3:
          doFightRunAway(role, fightEndEvent);
          break;
        case 40:
          doFightMaxRound(role, fightEndEvent);
          break;
      } 
    } catch (Exception e) {
      e.printStackTrace();
      LoggerUtils.error(role.getId() + "|" + getClass().getSimpleName() + "|" + fightEndEvent.getStatus(), new Object[0]);
      Npc npc = getBossNpc(fightEndEvent.getNpcId());
      if (npc != null) {
        setNotInFight(npc);
      } else {
        this.logger.error("战斗结束异常，获取战斗Npc数据为空，战斗类型：{}，NPC-ID：{}", Integer.valueOf(fightEndEvent.getFightType()), Integer.valueOf(fightEndEvent.getNpcId()));
      } 
    } 
  }
  
  private boolean isMatchFightType(Role role, int fightType) {
    return (getFightType(role) == fightType);
  }
  
  public void doFightWin(Role role, FightEndEvent fightEndEvent) {
    if (!isMatchFightType(role, fightEndEvent.getFightType()))
      return; 
    Npc bossNpc = getBossNpc(fightEndEvent.getNpcId());
    if (bossNpc == null)
      return; 
    if (this.teamService.isInTeam(role)) {
      Team team = this.teamService.getTeam(role.getRoleId().intValue());
      doFightWinForTeam(team, fightEndEvent, bossNpc);
    } else {
      doFightWinForSingle(role, fightEndEvent, bossNpc);
    } 
    clearNpcAfterWin(role, fightEndEvent, bossNpc);
    setNotInFight(bossNpc);
  }
  
  public void doFightWinForTeam(Team team, FightEndEvent fightEndEvent, Npc bossNpc) {
    int leaderRoleId = team.getLeaderRoleId();
    List<Member> members = team.getList();
    for (int i = 0; i < members.size(); i++) {
      Member member = members.get(i);
      if (member != null)
        if (member.isInTeam())
          if (leaderRoleId != member.getRoleId().intValue()) {
            Role memberRole = this.roleService.getPlayer(member.getRoleId().intValue());
            if (memberRole != null)
              ThreadLocalUtil.addLocalTask(memberRole, () -> doFightWinForSingle(memberRole, fightEndEvent, bossNpc)); 
          }   
    } 
    Role role = this.roleService.getPlayer(leaderRoleId);
    if (role != null)
      doFightWinForSingle(role, fightEndEvent, bossNpc); 
  }
  
  public void doFightWinForSingle(Role role, FightEndEvent fightEndEvent, Npc npc) {
    try {
      giveReward(role, fightEndEvent, npc);
      role.popTempCache("drop_monster_name");
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public void doFightFail(Role role, FightEndEvent fightEndEvent) {
    fail(role, fightEndEvent);
  }
  
  public void doFightRunAway(Role role, FightEndEvent fightEndEvent) {
    fail(role, fightEndEvent);
  }
  
  public void doFightMaxRound(Role role, FightEndEvent fightEndEvent) {
    fail(role, fightEndEvent);
  }
  
  private void fail(Role role, FightEndEvent fightEndEvent) {
    Npc npc = getBossNpc(fightEndEvent.getNpcId());
    if (npc == null)
      return; 
    setNotInFight(npc);
    int currentTime = (int)(System.currentTimeMillis() / 1000L);
    if (npc.getEndTime() > 0 && currentTime > npc.getEndTime())
      clearNpcTimeOut(npc); 
  }
  
  public void setNotInFight(Npc npc) {
    if (npc != null) {
      npc.getInFight().set(false);
      npc.setFightId(-1);
    } 
  }
  
  public void handleRoleExpire(Role role) {
    Fight fight = SpringUtils.getFightNewService().getFightByRoleId(role.getRoleId().intValue());
    if (fight == null)
      return; 
    if (!isMatchFightType(role, fight.getType()))
      return; 
    super.handleRoleExpire(role);
    Npc npc = null;
    if (fight != null)
      npc = SpringUtils.getMapService().getNpc(fight.getNpcId()); 
    doRoleExpire(role, fight, npc);
  }
  
  public List<Role> beFightOutTeam(Role role, Team team, FightEndEvent fightEndEvent) {
    List<Role> objects = new ArrayList<>();
    Role rA = fightEndEvent.getRolesA().stream().filter(e -> e.getId().equals(role.getId())).findFirst().orElse(null);
    if (rA != null) {
      List<Member> list = team.getList();
      for (int i = 0; i < fightEndEvent.getRolesA().size(); i++) {
        Role fightRole = fightEndEvent.getRolesA().get(i);
        Member member = list.stream().filter(e -> e.getId().equals(fightRole.getId())).findFirst().orElse(null);
        if (member == null)
          objects.add(fightRole); 
      } 
    } 
    Role rB = fightEndEvent.getRolesB().stream().filter(e -> e.getId().equals(role.getId())).findFirst().orElse(null);
    if (rB != null);
    return objects;
  }
  
  public void doRoleExpire(Role role, Fight fight, Npc npc) {
    TeamService teamService = SpringUtils.getTeamService();
    if (teamService.getTeamCount(role) <= 1 && 
      npc != null) {
      if (npc.isInFight())
        setNotInFight(npc); 
      int currentTime = (int)(System.currentTimeMillis() / 1000L);
      if (npc.getEndTime() > 0 && currentTime > npc.getEndTime())
        clearNpcTimeOut(npc); 
    } 
  }
  
  public List<Integer> calculateFloat(int level, int need) {
    List<Integer> list = new ArrayList<>();
    for (int i = 1; i <= 15; i++) {
      list.add(Integer.valueOf(level + i));
      if (level - i >= need)
        list.add(Integer.valueOf(level - i)); 
    } 
    return list;
  }
  
  public List<Integer> scope(int scene, List<Integer> list) {
    List<Integer> newList = new ArrayList<>();
    List<Integer> levelList = (List<Integer>)MapDataPool.levelScopeMap.get(Integer.valueOf(scene));
    if (levelList != null)
      for (Integer key : list) {
        if (levelList.contains(key))
          newList.add(key); 
      }  
    return newList;
  }
  
  public List<SysSkill> createSkill(Integer race, Integer sex) {
    Map<Integer, Map<Integer, List<SysSkill>>> mapMap = (Map<Integer, Map<Integer, List<SysSkill>>>)SkillDataPool.groupSkillMap.get(race);
    Map<Integer, List<SysSkill>> listMap = mapMap.get(sex);
    Integer[] keys = (Integer[])listMap.keySet().toArray((Object[])new Integer[0]);
    int random = (int)Math.random() * keys.length;
    Integer randomKey = keys[random];
    List<SysSkill> skills = listMap.get(randomKey);
    return skills;
  }
  
  public int getBossCount(Role role) {
    int allCount = 10;
    int teamCount = ((TeamService)SpringUtils.getBean(TeamService.class)).getTeamCount(role);
    if (teamCount == 0)
      teamCount = 1; 
    int count = 0;
    if (teamCount == 5) {
      count = allCount;
    } else {
      int minCount = 4 + teamCount;
      count = ThreadLocalRandom.current().nextInt(allCount - minCount + 1) + minCount;
    } 
    return count;
  }
  
  protected abstract String getNpcContent(Role paramRole, Npc paramNpc);
  
  protected abstract String getNpcContentNotFight(Role paramRole, Npc paramNpc);
  
  protected abstract void doStartFight(Role paramRole, Npc paramNpc);
  
  protected abstract int getFightType(Role paramRole);
  
  protected abstract Npc getBossNpc(int paramInt);
  
  protected abstract void clearNpcAfterWin(Role paramRole, FightEndEvent paramFightEndEvent, Npc paramNpc);
  
  protected abstract void giveReward(Role paramRole, FightEndEvent paramFightEndEvent, Npc paramNpc);
  
  protected abstract void clearNpcTimeOut(Npc paramNpc);
}
