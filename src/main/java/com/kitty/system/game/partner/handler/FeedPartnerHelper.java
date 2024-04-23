package com.kitty.system.game.partner.handler;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.enums.partner.FeedPartnerType;
import java.util.HashMap;
import java.util.Map;

public enum FeedPartnerHelper {
  INSTANCE;
  
  private static final Map<String, FeedPartnerHandler> handlers;
  
  static {
    handlers = new HashMap<>();
    INSTANCE.init(FeedPartnerType.PARTNER_PRACTICE.getType(), (FeedPartnerHandler)SpringUtils.getBean("PartnerPracticeHandler", PartnerPracticeHandler.class));
  }
  
  public void init(String type, FeedPartnerHandler feedPartnerHandler) {
    handlers.put(type, feedPartnerHandler);
  }
  
  public FeedPartnerHandler getHandler(String type) {
    return handlers.get(type);
  }
}
