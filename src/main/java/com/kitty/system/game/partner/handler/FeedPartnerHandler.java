package com.kitty.system.game.partner.handler;

import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.partner.domain.Partner;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.logs.LoggerFunction;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.partner.service.PartnerService;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public abstract class FeedPartnerHandler
{
    protected Logger logger;
    protected PartnerService partnerService;
    protected RoleService roleService;
    
    public FeedPartnerHandler() {
        this.logger = LoggerFunction.FEED_PARTNER.getLogger();
        this.partnerService = (PartnerService)SpringUtils.getBean(PartnerService.class);
        this.roleService = SpringUtils.getRoleService();
    }
    
    public abstract String acceptFeed(Partner partner, Role role, Integer param, String type, Object... args);
}
