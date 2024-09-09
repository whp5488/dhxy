package com.kitty.system.game.horse.handler;

import com.kitty.common.utils.spring.SpringUtils;
import java.util.HashMap;
import java.util.Map;

public enum FeedHelper
{
    INSTANCE;
    
    private static final Map<Short, FeedHorseHandler> handlers;
    
    public void init(short type, FeedHorseHandler feedPetHandler) {
        FeedHelper.handlers.put(Short.valueOf(type), feedPetHandler);
    }
    
    public FeedHorseHandler getHandler(short type) {
        return (FeedHorseHandler)FeedHelper.handlers.get(Short.valueOf(type));
    }
    
    static {
        handlers = new HashMap();
        FeedHelper.INSTANCE.init((short)1, (FeedHorseHandler)SpringUtils.getBean("HorseVitUpHandler", HorseVitUpHandler.class));
        FeedHelper.INSTANCE.init((short)2, (FeedHorseHandler)SpringUtils.getBean("HorseExpUpHandler", HorseExpUpHandler.class));
        FeedHelper.INSTANCE.init((short)3, (FeedHorseHandler)SpringUtils.getBean("HorseSkillUpHandler", HorseSkillUpHandler.class));
        FeedHelper.INSTANCE.init((short)4, (FeedHorseHandler)SpringUtils.getBean("HorseGrowUpHandler", HorseGrowUpHandler.class));
        FeedHelper.INSTANCE.init((short)5, (FeedHorseHandler)SpringUtils.getBean("HorseRefinePre", HorseRefinePreHandler.class));
        FeedHelper.INSTANCE.init((short)6, (FeedHorseHandler)SpringUtils.getBean("HorseRefine", HorseRefineHandler.class));
        FeedHelper.INSTANCE.init((short)7, (FeedHorseHandler)SpringUtils.getBean("HorseRefineReplace", HorseRefineReplaceHandler.class));
    }
}
