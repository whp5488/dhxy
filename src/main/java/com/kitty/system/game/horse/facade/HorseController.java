package com.kitty.system.game.horse.facade;

import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.horse.message.ReqMountControlPet;
import com.kitty.system.game.horse.message.ReqRideMount;
import com.kitty.system.game.horse.message.ReqSetMountName;
import com.kitty.system.game.horse.message.ReqUpgradeHorse;
import com.kitty.system.game.horse.service.HorseService;
import com.kitty.system.game.role.domain.Role;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class HorseController {
  Logger logger = LoggerFactory.getLogger(HorseController.class);
  
  @Autowired
  public HorseService horseService;
  
  @EventHandler({EventType.FIGHT_END})
  public void handleFightEndEvent(FightEndEvent fightEndEvent) {
    this.horseService.vitDispose(fightEndEvent.getRole());
  }
  
  @RequestMapping
  public void upgrade(IoSession session, ReqUpgradeHorse reqUpgradeHorse) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.horseService.upgrade(role, reqUpgradeHorse);
  }
  
  @RequestMapping
  public void changeName(IoSession session, ReqSetMountName reqSetMountName) {
    this.horseService.changeName(session, reqSetMountName);
  }
  
  @RequestMapping
  public void rideMount(IoSession session, ReqRideMount reqRideMount) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.horseService.rideMount(role, reqRideMount.getMountId(), reqRideMount.getType());
  }
  
  @RequestMapping
  public void controlPet(IoSession session, ReqMountControlPet mountControlPet) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.horseService.controlPet(role, mountControlPet);
  }
}
