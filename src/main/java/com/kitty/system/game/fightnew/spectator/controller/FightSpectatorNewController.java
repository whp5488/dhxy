package com.kitty.system.game.fightnew.spectator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightRecord.service.FightRecordService;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.fightnew.messages.old.RespEndCombat;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.service.FightExecutorNewService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.map.service.MapService;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.fightnew.spectator.message.ReqLookOn;
import org.apache.mina.core.session.IoSession;
import com.kitty.logs.LoggerFunction;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.fightnew.spectator.service.FightSpectatorNewService;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/fightSpectatorNew" })
public class FightSpectatorNewController
{
    private final Logger logger;
    @Autowired
    public FightSpectatorNewService fightSpectatorService;
    
    public FightSpectatorNewController() {
        this.logger = LoggerFunction.FIGHT.getLogger();
    }
    
    @com.kitty.mina.annotation.RequestMapping
    public void fightSpectator(IoSession session, ReqLookOn reqLookOn) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        long objId = 0L;
        try {
            objId = Long.parseLong(reqLookOn.getObjId());
        }
        catch (NumberFormatException e) {
            this.logger.error("观战用的objId不对,objId={}", reqLookOn.getObjId());
        }
        byte isNo = reqLookOn.getIsNo();
        Integer fightId = null;
        if (objId / 1000000000000000000L > 0L) {
            if (isNo == 1) {
                ((FightRecordService)SpringUtils.getBean(FightRecordService.class)).playerInFight((long)role.getId(), objId);
            }
            else {
                RespEndCombat respEndCombat = new RespEndCombat();
                MessagePusher.pushMessage(role, respEndCombat);
            }
            return;
        }
        else {
            if (objId / 10000000L > 0L) {
                fightId = (Integer)FightExecutorNewService.FIGHT_ROOMS.get(Integer.valueOf((int)objId));
            }
            else {
                Npc npc = ((MapService)SpringUtils.getBean(MapService.class)).getNpc((int)objId);
                if (npc != null && npc.isInFight() && npc.getFightId() > 0) {
                    fightId = Integer.valueOf(npc.getFightId());
                    reqLookOn.setA(true);
                }
            }
            if (fightId == null) {
                return;
            }
            Fight fight = (Fight)FightExecutorNewService.FIGHTS.get(fightId);
            if (fight == null) {
                return;
            }
            if (isNo == 1) {
                this.fightSpectatorService.enterWatchFight(role, fight, reqLookOn.isA());
            }
            else {
                this.fightSpectatorService.outWatchFight(role, fight);
            }
            return;
        }
    }
    
    @GetMapping({ "/test" })
    public void test(long roleId, int fightId, int fightRoleId, byte isNo) {
        Role role = SpringUtils.getRoleService().getOnlinePlayer(roleId);
        if (role == null) {
            return;
        }
        Fight fight = null;
        if (fightRoleId > 0) {
            fightId = (int)FightExecutorNewService.FIGHT_ROOMS.get(Integer.valueOf(fightRoleId));
            if (fightId <= 0) {
                return;
            }
            fight = (Fight)FightExecutorNewService.FIGHTS.get(Integer.valueOf(fightId));
            if (fight == null) {
                return;
            }
        }
        else if (fightId > 0) {
            fight = (Fight)FightExecutorNewService.FIGHTS.get(Integer.valueOf(fightId));
            if (fight == null) {
                return;
            }
        }
        else {
            return;
        }
        if (isNo == 1) {
            this.fightSpectatorService.enterWatchFight(role, fight, true);
        }
        else {
            this.fightSpectatorService.outWatchFight(role, fight);
        }
        return;
    }
}
