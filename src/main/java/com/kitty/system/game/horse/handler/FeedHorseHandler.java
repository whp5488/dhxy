package com.kitty.system.game.horse.handler;

import com.kitty.logs.LoggerFunction;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.horse.domain.Horse;
import com.kitty.system.game.horse.service.HorseService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class FeedHorseHandler {
  protected Logger logger = LoggerFunction.HORSE.getLogger();
  
  @Autowired
  protected HorseService horseService;
  
  @Autowired
  protected BagService bagService;
  
  @Autowired
  protected RoleService roleService;
  
  public abstract String acceptFeed(Horse paramHorse, Role paramRole, short paramShort, byte paramByte, Object... paramVarArgs);
}
