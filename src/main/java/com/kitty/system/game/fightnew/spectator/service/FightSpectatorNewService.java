package com.kitty.system.game.fightnew.spectator.service;

import org.slf4j.Logger;
import com.kitty.logs.LoggerFunction;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.system.game.fightnew.spectator.pool.FightRoundParam;
import com.kitty.mina.task.ThreadLocalUtil;
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
    
    public FightSpectatorNewService() {
    }
    
    public void enterWatchFight(Role role, Fight fight, boolean isA) {
        if (!fight.isWatchFightOnOff()) {
            fight.setWatchFightOnOff(true);
        }
        fight.getSpectatorObjectMap().put(role.getId(), new SpectatorObject(role, isA));
        if (3 == fight.getRoundType()) {
            SpectatorObject spectatorObject = new SpectatorObject(role, isA);
            this.sendWatchFightData(fight, spectatorObject);
            fight.getSpectatorObjectMap().put(role.getId(), spectatorObject);
        }
        else {
            RespFightInParam respFightInParam = FightParamPool.createRespFightInParam(true, fight.getId(), (int)role.getRoleId(), isA);
            FightMessageUtil.sendFightPackage((long)role.getId(), respFightInParam);
        }
    }
    
    public void outWatchFight(Role role, Fight fight) {
        if (role == null || fight == null) {
            return;
        }
        this.clearSpectaorRole(fight, (long)role.getId());
        RespEndCombat respEndCombat = new RespEndCombat();
        MessagePusher.pushMessage(role, respEndCombat);
        return;
    }
    
    public void sendWatchFightData(Fight fight, byte type) {
        if (fight.isWatchFightOnOff()) {
            ConcurrentHashMap<Long, SpectatorObject> spectatorObjectMap = fight.getSpectatorObjectMap();
            if (spectatorObjectMap == null || spectatorObjectMap.size() <= 0) {
                return;
            }
            List<Long> removeList = new ArrayList<>();
            for (Map.Entry<Long, SpectatorObject> spectatorObject : spectatorObjectMap.entrySet()) {
                if (spectatorObject.getValue() != null) {
                    Integer fightId = (Integer)FightExecutorNewService.FIGHT_ROOMS.get(Integer.valueOf(((SpectatorObject)spectatorObject.getValue()).getRoleId()));
                    if (fightId != null) {
                        removeList.add(spectatorObject.getKey());
                    }
                    else if (1 == type) {
                        this.sendSpectatorRoundStart(fight, (SpectatorObject)spectatorObject.getValue(), false);
                    }
                    else if (2 == type) {
                        this.sendSpecatorExcute(fight, (SpectatorObject)spectatorObject.getValue());
                    }
                    else {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
            for (Long roleId : removeList) {
                this.clearSpectaorRole(fight, (long)roleId);
            }
        }
    }
    
    public void sendWatchFightData(Fight fight, SpectatorObject spectatorObject) {
        this.sendSpectatorRoundStart(fight, spectatorObject, true);
        try {
            Thread.sleep(10L);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.sendSpecatorExcute(fight, spectatorObject);
    }
    
    public void sendSpecatorExcute(Fight fight, SpectatorObject spectatorObject) {
        RespExcuteFight respExcuteFight = FightParamPool.gainFightRoundParam(fight.getId()).getRespExcuteFight();
        if (respExcuteFight != null) {
            FightMessageUtil.sendFightPackage(spectatorObject.getId(), respExcuteFight);
        }
    }
    
    public void sendSpectatorRoundStart(Fight fight, SpectatorObject spectatorObject, boolean isFightIn) {
        if (isFightIn) {
            RespFightInParam respFightInParam = FightParamPool.createRespFightInParam(true, fight.getId(), spectatorObject.getRoleId(), spectatorObject.isA());
            FightMessageUtil.sendFightPackage(spectatorObject.getId(), respFightInParam);
        }
        RespRoundStartParam roundStartParam = FightParamPool.getRespRoundStartParam(fight, spectatorObject.isA());
        FightMessageUtil.sendFightPackage(spectatorObject.getId(), roundStartParam);
    }
    
    public void clearSpectaorRole(Fight fight, long roleId) {
        fight.getSpectatorObjectMap().remove(Long.valueOf(roleId));
        if (fight.getSpectatorObjectMap().size() <= 0) {
            fight.setWatchFightOnOff(false);
        }
    }
    
    @EventHandler({ EventType.FIGHT_ROUND_PARAM })
    public void fightRoundParamEvent(FightRoundParamEvent fightRoundParamEvent) {
        ConcurrentHashMap<Integer, FightRoundParam> fightRoundParamMap = FightParamPool.FIGHT_ROUND_PARAM;
        if (fightRoundParamMap.size() > 10000) {
            ThreadLocalUtil.addLocalTask(() -> this.excuteClearFightParam(fightRoundParamMap));
        }
    }
    
    private void excuteClearFightParam(ConcurrentHashMap<Integer, FightRoundParam> fightRoundParamMap) {
        int oldSize = fightRoundParamMap.size();
        Logger logger = LoggerFunction.FIGHT.getLogger();
        ConcurrentHashMap<Integer, Fight> map = new ConcurrentHashMap(FightExecutorNewService.FIGHTS);
        for (Map.Entry<Integer, Fight> entry : map.entrySet()) {
            Fight fight = (Fight)entry.getValue();
            if (fight.isOver()) {
                logger.error("战斗缓存超过{}条，删除的战场id={}", Integer.valueOf(10000), Integer.valueOf(fight.getId()));
                this.fightNewService.quiteFightAll(fight, false);
                this.fightNewService.clearFight(fight);
            }
            else {
                continue;
            }
        }
        int newSize = fightRoundParamMap.size();
        if (oldSize != newSize) {
            logger.error("非正常清理战场、缓存，当前战场数量={}, 删除后数量={}", Integer.valueOf(oldSize), Integer.valueOf(newSize));
        }
    }
}
