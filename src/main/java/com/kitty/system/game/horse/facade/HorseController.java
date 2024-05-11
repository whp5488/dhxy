
package com.kitty.system.game.horse.facade;

import com.kitty.system.game.horse.message.ReqMountControlPet;
import com.kitty.system.game.horse.message.ReqRideMount;
import com.kitty.system.game.horse.message.ReqSetMountName;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.horse.message.ReqUpgradeHorse;
import org.apache.mina.core.session.IoSession;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.FightEndEvent;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.horse.service.HorseService;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;

@Controller
public class HorseController
{
    Logger logger;
    @Autowired
    public HorseService horseService;
    
    public HorseController() {
        this.logger = LoggerFactory.getLogger(HorseController.class);
    }
    
    @EventHandler({ EventType.FIGHT_END })
    public void handleFightEndEvent(final FightEndEvent fightEndEvent) {
        this.horseService.vitDispose(fightEndEvent.getRole());
    }
    
    @RequestMapping
    public void upgrade(final IoSession session, final ReqUpgradeHorse reqUpgradeHorse) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.horseService.upgrade(role, reqUpgradeHorse);
    }
    
    @RequestMapping
    public void changeName(final IoSession session, final ReqSetMountName reqSetMountName) {
        this.horseService.changeName(session, reqSetMountName);
    }
    
    @RequestMapping
    public void rideMount(final IoSession session, final ReqRideMount reqRideMount) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.horseService.rideMount(role, reqRideMount.getMountId(), reqRideMount.getType());
    }
    
    @RequestMapping
    public void controlPet(final IoSession session, final ReqMountControlPet mountControlPet) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.horseService.controlPet(role, mountControlPet);
    }
}
