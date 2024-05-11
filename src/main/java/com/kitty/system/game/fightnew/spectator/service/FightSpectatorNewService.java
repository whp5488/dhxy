
package com.kitty.system.game.fightnew.spectator.service;

import org.slf4j.Logger;
import com.kitty.logs.LoggerFunction;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.mina.task.ThreadLocalUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightRoundParam;
import com.kitty.listener.event.FightRoundParamEvent;
import com.kitty.system.game.fightnew.messages.RespRoundStartParam;
import com.kitty.system.game.fightnew.messages.RespExcuteFight;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import com.kitty.system.game.fightnew.service.FightExecutorNewService;
import java.util.Map;
import java.util.ArrayList;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.fightnew.messages.old.RespEndCombat;
import com.kitty.system.game.fightnew.messages.RespFightInParam;
import com.kitty.mina.message.Message;
import com.kitty.system.game.fightnew.utils.FightMessageUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.spectator.domain.SpectatorObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.role.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.fightnew.service.FightNewService;
import org.springframework.stereotype.Service;

@Service
public class FightSpectatorNewService
{
    @Autowired
    public FightNewService fightNewService;
    
    public void enterWatchFight(final Role role, final Fight fight, final boolean isA) {
        if (!fight.isWatchFightOnOff()) {
            fight.setWatchFightOnOff(true);
        }
        fight.getSpectatorObjectMap().put(role.getId(), new SpectatorObject(role, isA));
        if (3 == fight.getRoundType()) {
            final SpectatorObject spectatorObject = new SpectatorObject(role, isA);
            this.sendWatchFightData(fight, spectatorObject);
            fight.getSpectatorObjectMap().put(role.getId(), spectatorObject);
        }
        else {
            final RespFightInParam respFightInParam = FightParamPool.createRespFightInParam(true, fight.getId(), role.getRoleId(), isA);
            FightMessageUtil.sendFightPackage(role.getId(), respFightInParam);
        }
    }
    
    public void outWatchFight(final Role role, final Fight fight) {
        if (role == null || fight == null) {
            return;
        }
        this.clearSpectaorRole(fight, role.getId());
        final RespEndCombat respEndCombat = new RespEndCombat();
        MessagePusher.pushMessage(role, respEndCombat);
    }
    
    public void sendWatchFightData(final Fight fight, final byte type) {
        if (fight.isWatchFightOnOff()) {
            final ConcurrentHashMap<Long, SpectatorObject> spectatorObjectMap = fight.getSpectatorObjectMap();
            if (spectatorObjectMap == null || spectatorObjectMap.size() <= 0) {
                return;
            }
            final List<Long> removeList = new ArrayList<Long>();
            for (final Map.Entry<Long, SpectatorObject> spectatorObject : spectatorObjectMap.entrySet()) {
                if (spectatorObject.getValue() != null) {
                    final Integer fightId = FightExecutorNewService.FIGHT_ROOMS.get(spectatorObject.getValue().getRoleId());
                    if (fightId != null) {
                        removeList.add(spectatorObject.getKey());
                    }
                    else if (1 == type) {
                        this.sendSpectatorRoundStart(fight, spectatorObject.getValue(), false);
                    }
                    else {
                        if (2 != type) {
                            continue;
                        }
                        this.sendSpecatorExcute(fight, spectatorObject.getValue());
                    }
                }
            }
            for (final Long roleId : removeList) {
                this.clearSpectaorRole(fight, roleId);
            }
        }
    }
    
    public void sendWatchFightData(final Fight fight, final SpectatorObject spectatorObject) {
        this.sendSpectatorRoundStart(fight, spectatorObject, true);
        try {
            Thread.sleep(10L);
        }
        catch (final InterruptedException e) {
            e.printStackTrace();
        }
        this.sendSpecatorExcute(fight, spectatorObject);
    }
    
    public void sendSpecatorExcute(final Fight fight, final SpectatorObject spectatorObject) {
        final RespExcuteFight respExcuteFight = FightParamPool.gainFightRoundParam(fight.getId()).getRespExcuteFight();
        if (respExcuteFight != null) {
            FightMessageUtil.sendFightPackage(spectatorObject.getId(), respExcuteFight);
        }
    }
    
    public void sendSpectatorRoundStart(final Fight fight, final SpectatorObject spectatorObject, final boolean isFightIn) {
        if (isFightIn) {
            final RespFightInParam respFightInParam = FightParamPool.createRespFightInParam(true, fight.getId(), spectatorObject.getRoleId(), spectatorObject.isA());
            FightMessageUtil.sendFightPackage(spectatorObject.getId(), respFightInParam);
        }
        final RespRoundStartParam roundStartParam = FightParamPool.getRespRoundStartParam(fight, spectatorObject.isA());
        FightMessageUtil.sendFightPackage(spectatorObject.getId(), roundStartParam);
    }
    
    public void clearSpectaorRole(final Fight fight, final long roleId) {
        fight.getSpectatorObjectMap().remove(roleId);
        if (fight.getSpectatorObjectMap().size() <= 0) {
            fight.setWatchFightOnOff(false);
        }
    }
    
    @EventHandler({ EventType.FIGHT_ROUND_PARAM })
    public void fightRoundParamEvent(final FightRoundParamEvent fightRoundParamEvent) {
        final ConcurrentHashMap<Integer, FightRoundParam> fightRoundParamMap = FightParamPool.FIGHT_ROUND_PARAM;
        if (fightRoundParamMap.size() > 10000) {
            ThreadLocalUtil.addLocalTask(() -> this.excuteClearFightParam(fightRoundParamMap));
        }
    }
    
    private void excuteClearFightParam(final ConcurrentHashMap<Integer, FightRoundParam> fightRoundParamMap) {
        final int oldSize = fightRoundParamMap.size();
        final Logger logger = LoggerFunction.FIGHT.getLogger();
        final ConcurrentHashMap<Integer, Fight> map = new ConcurrentHashMap<Integer, Fight>(FightExecutorNewService.FIGHTS);
        for (final Map.Entry<Integer, Fight> entry : map.entrySet()) {
            final Fight fight = entry.getValue();
            if (fight.isOver()) {
                logger.error("战斗缓存超过{}条，删除的战场id={}", (Object)10000, (Object)fight.getId());
                this.fightNewService.quiteFightAll(fight, false);
                this.fightNewService.clearFight(fight);
            }
        }
        final int newSize = fightRoundParamMap.size();
        if (oldSize != newSize) {
            logger.error("非正常清理战场、缓存，当前战场数量={}, 删除后数量={}", (Object)oldSize, (Object)newSize);
        }
    }
}
