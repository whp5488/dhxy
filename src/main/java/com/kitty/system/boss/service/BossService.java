package com.kitty.system.boss.service;

import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.enter.message.RespDisappear;
import java.util.Iterator;
import java.util.Set;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.enter.message.RespElseAppear;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.map.model.GroupMapParam;
import com.kitty.common.model.Pos;
import com.kitty.system.game.fightnew.factory.FightFactory;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.team.domain.Team;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.boss.model.BossFightParam;
import com.kitty.system.game.role.domain.Role;
import org.slf4j.LoggerFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class BossService
{
    private Logger logger;
    private static AtomicInteger atomicInteger;
    
    public BossService() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }
    
    public synchronized int getTempNpcId() {
        return BossService.atomicInteger.getAndIncrement();
    }
    
    public Fight startFightToBoss(Role role, BossFightParam bossFightParam) {
        Team team = SpringUtils.getFightNewService().getRoleFightTeam(role);
        return this.sendBossTeam(team, role, bossFightParam);
    }
    
    public Fight sendBossTeam(Team team, Role role, BossFightParam bossFightParam) {
        FightNewService fightService = SpringUtils.getBean(FightNewService.class);
        if (!fightService.canFight(role, team.getList())) {
            return null;
        }
        synchronized (role.getCanFight()) {
            if (!(boolean)role.getCanFight()) {
                return null;
            }
            role.setCanFight(Boolean.FALSE);
        }
        if (SpringUtils.getTeamService().isInTeamAndNotLeader(role)) {
            this.logger.warn("{}({})不是队长触发战斗", role.getRoleId(), role.getName());
            return null;
        }
        int type = bossFightParam.getFightType();
        Pos position = role.getPos();
        int mapId = (int)position.getMapId();
        Fight battleground = FightFactory.getFightFactory(type).create(mapId, role, team, bossFightParam);
        battleground.setSpecialMonsterType(bossFightParam.getSpecialMonsterType());
        battleground.start();
        return battleground;
    }
    
    public void broadcastNpcShow(GroupMapParam groupMapParam, Npc npc) {
        if (npc == null || !npc.isShow()) {
            return;
        }
        Set<Integer> roleIds = SpringUtils.getMapService().getRoleIds(groupMapParam, npc.getMap());
        if (roleIds == null || roleIds.size() <= 0) {
            return;
        }
        RespElseAppear respAppear = new RespElseAppear();
        respAppear.setId(npc.getId());
        respAppear.setN(npc.getName());
        respAppear.setS((short)npc.getIcon());
        respAppear.setX((short)npc.getX());
        respAppear.setY((short)npc.getY());
        respAppear.setT(npc.getTitle());
        for (Integer roleId : roleIds) {
            Role tempRole = SpringUtils.getRoleService().getOnlinePlayer((int)roleId);
            if (tempRole == null) {
                continue;
            }
            MessagePusher.pushMessage(tempRole, respAppear);
        }
    }
    
    public void broadcastNpcHide(GroupMapParam groupMapParam, Npc npc) {
        Set<Integer> roleIds = SpringUtils.getMapService().getRoleIds(groupMapParam, npc.getMap());
        if (roleIds == null || roleIds.size() <= 0) {
            return;
        }
        RespDisappear respDisappear = new RespDisappear();
        respDisappear.setId(npc.getId());
        respDisappear.setType(LOGIC.NPC.getValue());
        for (Integer roleId : roleIds) {
            Role role = SpringUtils.getRoleService().getOnlinePlayer((int)roleId);
            MessagePusher.pushMessage(role, respDisappear);
        }
    }
    
    static {
        BossService.atomicInteger = new AtomicInteger(1000000);
    }
}
