package com.kitty.system.boss.service;

import com.kitty.common.model.Pos;
import com.kitty.common.utils.ConcurrentHashSet;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.boss.model.BossFightParam;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.enter.message.RespDisappear;
import com.kitty.system.game.enter.message.RespElseAppear;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.factory.FightFactory;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.map.model.GroupMapParam;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.vo.Npc;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BossService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  private static AtomicInteger atomicInteger = new AtomicInteger(1000000);
  
  public synchronized int getTempNpcId() {
    return atomicInteger.getAndIncrement();
  }
  
  public Fight startFightToBoss(Role role, BossFightParam bossFightParam) {
    Team team = SpringUtils.getFightNewService().getRoleFightTeam(role);
    return sendBossTeam(team, role, bossFightParam);
  }
  
  public Fight sendBossTeam(Team team, Role role, BossFightParam bossFightParam) {
    FightNewService fightService = SpringUtils.getBean(FightNewService.class);
    if (!fightService.canFight(role, team.getList()))
      return null; 
    synchronized (role.getCanFight()) {
      if (!role.getCanFight())
        return null; 
      role.setCanFight(Boolean.FALSE);
    } 
    if (SpringUtils.getTeamService().isInTeamAndNotLeader(role)) {
      this.logger.warn("{}({})不是队长触发战斗", role.getRoleId(), role.getName());
      return null;
    } 
    int type = bossFightParam.getFightType();
    Pos position = role.getPos();
    int mapId = position.getMapId();
    Fight battleground = FightFactory.getFightFactory(type).create(mapId, role, team, bossFightParam);
    battleground.setSpecialMonsterType(bossFightParam.getSpecialMonsterType());
    battleground.start();
    return battleground;
  }
  
  public void broadcastNpcShow(GroupMapParam groupMapParam, Npc npc) {
    if (npc == null || !npc.isShow())
      return; 
    ConcurrentHashSet<Integer> concurrentHashSet = SpringUtils.getMapService().getRoleIds(groupMapParam, npc.getMap());
    if (concurrentHashSet == null || concurrentHashSet.isEmpty())
      return; 
    RespElseAppear respAppear = new RespElseAppear();
    respAppear.setId(npc.getId());
    respAppear.setN(npc.getName());
    respAppear.setS((short)npc.getIcon());
    respAppear.setX((short)npc.getX());
    respAppear.setY((short)npc.getY());
    respAppear.setT(npc.getTitle());
    for (Integer roleId : concurrentHashSet) {
      Role tempRole = SpringUtils.getRoleService().getOnlinePlayer(roleId);
      if (tempRole == null)
        continue; 
      MessagePusher.pushMessage(tempRole, respAppear);
    } 
  }
  
  public void broadcastNpcHide(GroupMapParam groupMapParam, Npc npc) {
    ConcurrentHashSet<Integer> concurrentHashSet = SpringUtils.getMapService().getRoleIds(groupMapParam, npc.getMap());
    if (concurrentHashSet == null || concurrentHashSet.isEmpty())
      return; 
    RespDisappear respDisappear = new RespDisappear();
    respDisappear.setId(npc.getId());
    respDisappear.setType(LOGIC.NPC.getValue());
    for (Integer roleId : concurrentHashSet) {
      Role role = SpringUtils.getRoleService().getOnlinePlayer(roleId);
      MessagePusher.pushMessage(role, respDisappear);
    } 
  }
}
