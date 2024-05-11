
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
        this.partnerService = SpringUtils.getBean(PartnerService.class);
        this.roleService = SpringUtils.getRoleService();
    }
    
    public abstract String acceptFeed(final Partner p0, final Role p1, final Integer p2, final String p3, final Object... p4);
}
