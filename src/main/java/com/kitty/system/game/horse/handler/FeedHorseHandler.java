package com.kitty.system.game.horse.handler;

import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.horse.domain.Horse;
import com.kitty.logs.LoggerFunction;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.bag.service.BagService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.horse.service.HorseService;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public abstract class FeedHorseHandler
{
    protected Logger logger;
    @Autowired
    protected HorseService horseService;
    @Autowired
    protected BagService bagService;
    @Autowired
    protected RoleService roleService;
    
    public FeedHorseHandler() {
        this.logger = LoggerFunction.HORSE.getLogger();
    }
    
    public abstract String acceptFeed(Horse horse, Role role, short type, byte operation, Object... args);
}
