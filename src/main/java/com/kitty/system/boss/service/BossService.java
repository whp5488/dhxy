
package com.kitty.system.boss.service;

import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.enter.message.RespDisappear;
import java.util.Iterator;
import java.util.Set;
import com.kitty.mina.message.Message;
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
    
    public Fight startFightToBoss(final Role role, final BossFightParam bossFightParam) {
        final Team team = SpringUtils.getFightNewService().getRoleFightTeam(role);
        return this.sendBossTeam(team, role, bossFightParam);
    }
    
    public Fight sendBossTeam(final Team team, final Role role, final BossFightParam bossFightParam) {
        final FightNewService fightService = SpringUtils.getBean(FightNewService.class);
        if (!fightService.canFight(role, team.getList())) {
            return null;
        }
        synchronized (role.getCanFight()) {
            if (!role.getCanFight()) {
                return null;
            }
            role.setCanFight(false);
        }
        if (SpringUtils.getTeamService().isInTeamAndNotLeader(role)) {
            this.logger.warn("{}({})不是队长触发战斗", role.getRoleId(), role.getName());
            return null;
        }
        final int type = bossFightParam.getFightType();
        final Pos position = role.getPos();
        final int mapId = position.getMapId();
        final Fight battleground = FightFactory.getFightFactory(type).create(mapId, role, team, bossFightParam);
        battleground.setSpecialMonsterType(bossFightParam.getSpecialMonsterType());
        battleground.start();
        return battleground;
    }
    
    public void broadcastNpcShow(final GroupMapParam groupMapParam, final Npc npc) {
        if (npc == null || !npc.isShow()) {
            return;
        }
        final Set<Integer> roleIds = SpringUtils.getMapService().getRoleIds(groupMapParam, npc.getMap());
        if (roleIds == null || roleIds.size() <= 0) {
            return;
        }
        final RespElseAppear respAppear = new RespElseAppear();
        respAppear.setId(npc.getId());
        respAppear.setN(npc.getName());
        respAppear.setS((short)npc.getIcon());
        respAppear.setX((short)npc.getX());
        respAppear.setY((short)npc.getY());
        respAppear.setT(npc.getTitle());
        for (final Integer roleId : roleIds) {
            final Role tempRole = SpringUtils.getRoleService().getOnlinePlayer(roleId);
            if (tempRole == null) {
                continue;
            }
            MessagePusher.pushMessage(tempRole, respAppear);
        }
    }
    
    public void broadcastNpcHide(final GroupMapParam groupMapParam, final Npc npc) {
        final Set<Integer> roleIds = SpringUtils.getMapService().getRoleIds(groupMapParam, npc.getMap());
        if (roleIds == null || roleIds.size() <= 0) {
            return;
        }
        final RespDisappear respDisappear = new RespDisappear();
        respDisappear.setId(npc.getId());
        respDisappear.setType(LOGIC.NPC.getValue());
        for (final Integer roleId : roleIds) {
            final Role role = SpringUtils.getRoleService().getOnlinePlayer(roleId);
            MessagePusher.pushMessage(role, respDisappear);
        }
    }
    
    static {
        BossService.atomicInteger = new AtomicInteger(1000000);
    }
}
