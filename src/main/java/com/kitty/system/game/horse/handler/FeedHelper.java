package com.kitty.system.game.horse.handler;

import com.kitty.common.utils.spring.SpringUtils;
import java.util.HashMap;
import java.util.Map;

public enum FeedHelper {
  INSTANCE;
  
  private static final Map<Short, FeedHorseHandler> handlers;
  
  static {
    handlers = new HashMap<>();
    INSTANCE.init((short)1, (FeedHorseHandler)SpringUtils.getBean("HorseVitUpHandler", HorseVitUpHandler.class));
    INSTANCE.init((short)2, (FeedHorseHandler)SpringUtils.getBean("HorseExpUpHandler", HorseExpUpHandler.class));
    INSTANCE.init((short)3, (FeedHorseHandler)SpringUtils.getBean("HorseSkillUpHandler", HorseSkillUpHandler.class));
    INSTANCE.init((short)4, (FeedHorseHandler)SpringUtils.getBean("HorseGrowUpHandler", HorseGrowUpHandler.class));
    INSTANCE.init((short)5, (FeedHorseHandler)SpringUtils.getBean("HorseRefinePre", HorseRefinePreHandler.class));
    INSTANCE.init((short)6, (FeedHorseHandler)SpringUtils.getBean("HorseRefine", HorseRefineHandler.class));
    INSTANCE.init((short)7, (FeedHorseHandler)SpringUtils.getBean("HorseRefineReplace", HorseRefineReplaceHandler.class));
  }
  
  public void init(short type, FeedHorseHandler feedPetHandler) {
    handlers.put(Short.valueOf(type), feedPetHandler);
  }
  
  public FeedHorseHandler getHandler(short type) {
    return handlers.get(Short.valueOf(type));
  }
}
