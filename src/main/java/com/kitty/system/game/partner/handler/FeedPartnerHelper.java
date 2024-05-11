
package com.kitty.system.game.partner.handler;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.enums.partner.FeedPartnerType;
import java.util.HashMap;
import java.util.Map;

public enum FeedPartnerHelper
{
    INSTANCE;
    
    private static final Map<String, FeedPartnerHandler> handlers;
    
    public void init(final String type, final FeedPartnerHandler feedPartnerHandler) {
        FeedPartnerHelper.handlers.put(type, feedPartnerHandler);
    }
    
    public FeedPartnerHandler getHandler(final String type) {
        return FeedPartnerHelper.handlers.get(type);
    }
    
    static {
        handlers = new HashMap<String, FeedPartnerHandler>();
        FeedPartnerHelper.INSTANCE.init(FeedPartnerType.PARTNER_PRACTICE.getType(), SpringUtils.getBean("PartnerPracticeHandler", PartnerPracticeHandler.class));
    }
}
