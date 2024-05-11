
package com.kitty.system.game.horse.handler;

import com.kitty.common.utils.spring.SpringUtils;
import java.util.HashMap;
import java.util.Map;

public enum FeedHelper
{
    INSTANCE;
    
    private static final Map<Short, FeedHorseHandler> handlers;
    
    public void init(final short type, final FeedHorseHandler feedPetHandler) {
        FeedHelper.handlers.put(type, feedPetHandler);
    }
    
    public FeedHorseHandler getHandler(final short type) {
        return FeedHelper.handlers.get(type);
    }
    
    static {
        handlers = new HashMap<Short, FeedHorseHandler>();
        FeedHelper.INSTANCE.init((short)1, SpringUtils.getBean("HorseVitUpHandler", HorseVitUpHandler.class));
        FeedHelper.INSTANCE.init((short)2, SpringUtils.getBean("HorseExpUpHandler", HorseExpUpHandler.class));
        FeedHelper.INSTANCE.init((short)3, SpringUtils.getBean("HorseSkillUpHandler", HorseSkillUpHandler.class));
        FeedHelper.INSTANCE.init((short)4, SpringUtils.getBean("HorseGrowUpHandler", HorseGrowUpHandler.class));
        FeedHelper.INSTANCE.init((short)5, SpringUtils.getBean("HorseRefinePre", HorseRefinePreHandler.class));
        FeedHelper.INSTANCE.init((short)6, SpringUtils.getBean("HorseRefine", HorseRefineHandler.class));
        FeedHelper.INSTANCE.init((short)7, SpringUtils.getBean("HorseRefineReplace", HorseRefineReplaceHandler.class));
    }
}
