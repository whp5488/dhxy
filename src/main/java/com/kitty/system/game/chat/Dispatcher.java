
package com.kitty.system.game.chat;

import com.kitty.system.game.instruct.inter.NcFather;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.instruct.service.InstructService;
import com.kitty.system.game.role.domain.Role;
import org.slf4j.LoggerFactory;
import com.kitty.system.game.account.service.AccountService;
import com.kitty.common.start.ServerService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class Dispatcher
{
    Logger logger;
    @Autowired
    @Qualifier("gameConversion")
    ConversionService conversionService;
    @Autowired
    ServerService serverService;
    @Autowired
    AccountService accountService;
    
    public Dispatcher() {
        this.logger = LoggerFactory.getLogger(Dispatcher.class);
    }
    
    public boolean dispatch(final Role player, final String command) {
        return this.dispatchOther(player, command);
    }
    
    private boolean dispatchOther(final Role player, final String command) {
        final String[] args = command.split("==");
        final String params = args[0];
        if (!params.startsWith("@")) {
            return false;
        }
        final InstructService instructService = SpringUtils.getBean(InstructService.class);
        final NcFather gmFather = instructService.getInstruct().get(params);
        if (gmFather == null) {
            return true;
        }
        if (this.serverService.getType() == 1) {
            return true;
        }
        gmFather.handle(player, args[1].trim());
        return true;
    }
}
