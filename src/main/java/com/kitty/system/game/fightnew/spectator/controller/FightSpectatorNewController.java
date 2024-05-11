
package com.kitty.system.game.fightnew.spectator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.map.service.MapService;
import com.kitty.system.game.fightnew.service.FightExecutorNewService;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.fightnew.messages.old.RespEndCombat;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightRecord.service.FightRecordService;
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
    public void fightSpectator(final IoSession session, final ReqLookOn reqLookOn) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        long objId = 0L;
        try {
            objId = Long.parseLong(reqLookOn.getObjId());
        }
        catch (final NumberFormatException e) {
            this.logger.error("观战用的objId不对,objId={}", (Object)reqLookOn.getObjId());
        }
        final byte isNo = reqLookOn.getIsNo();
        Integer fightId = null;
        if (objId / 1000000000000000000L > 0L) {
            if (isNo == 1) {
                SpringUtils.getBean(FightRecordService.class).playerInFight(role.getId(), objId);
            }
            else {
                final RespEndCombat respEndCombat = new RespEndCombat();
                MessagePusher.pushMessage(role, respEndCombat);
            }
            return;
        }
        if (objId / 10000000L > 0L) {
            fightId = FightExecutorNewService.FIGHT_ROOMS.get((int)objId);
        }
        else {
            final Npc npc = SpringUtils.getBean(MapService.class).getNpc((int)objId);
            if (npc != null && npc.isInFight() && npc.getFightId() > 0) {
                fightId = npc.getFightId();
                reqLookOn.setA(true);
            }
        }
        if (fightId == null) {
            return;
        }
        final Fight fight = FightExecutorNewService.FIGHTS.get(fightId);
        if (fight == null) {
            return;
        }
        if (isNo == 1) {
            this.fightSpectatorService.enterWatchFight(role, fight, reqLookOn.isA());
        }
        else {
            this.fightSpectatorService.outWatchFight(role, fight);
        }
    }
    
    @GetMapping({ "/test" })
    public void test(final long roleId, int fightId, final int fightRoleId, final byte isNo) {
        final Role role = SpringUtils.getRoleService().getOnlinePlayer(roleId);
        if (role == null) {
            return;
        }
        Fight fight = null;
        if (fightRoleId > 0) {
            fightId = FightExecutorNewService.FIGHT_ROOMS.get(fightRoleId);
            if (fightId <= 0) {
                return;
            }
            fight = FightExecutorNewService.FIGHTS.get(fightId);
            if (fight == null) {
                return;
            }
        }
        else {
            if (fightId <= 0) {
                return;
            }
            fight = FightExecutorNewService.FIGHTS.get(fightId);
            if (fight == null) {
                return;
            }
        }
        if (isNo == 1) {
            this.fightSpectatorService.enterWatchFight(role, fight, true);
        }
        else {
            this.fightSpectatorService.outWatchFight(role, fight);
        }
    }
}
