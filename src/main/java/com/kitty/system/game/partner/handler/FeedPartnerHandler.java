package com.kitty.system.game.partner.handler;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.logs.LoggerFunction;
import com.kitty.system.game.partner.domain.Partner;
import com.kitty.system.game.partner.service.PartnerService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public abstract class FeedPartnerHandler {
  protected Logger logger = LoggerFunction.FEED_PARTNER.getLogger();
  
  protected PartnerService partnerService = (PartnerService)SpringUtils.getBean(PartnerService.class);
  
  protected RoleService roleService = SpringUtils.getRoleService();
  
  public abstract String acceptFeed(Partner paramPartner, Role paramRole, Integer paramInteger, String paramString, Object... paramVarArgs);
}
