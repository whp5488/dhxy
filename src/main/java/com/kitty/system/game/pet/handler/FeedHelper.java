
package com.kitty.system.game.pet.handler;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.enums.pet.FeedType;
import java.util.HashMap;
import java.util.Map;

public enum FeedHelper
{
    INSTANCE;
    
    private static final Map<String, FeedPetHandler> handlers;
    
    public void init(final String type, final FeedPetHandler feedPetHandler) {
        FeedHelper.handlers.put(type, feedPetHandler);
    }
    
    public FeedPetHandler getHandler(final String type) {
        return FeedHelper.handlers.get(type);
    }
    
    static {
        handlers = new HashMap<String, FeedPetHandler>();
        FeedHelper.INSTANCE.init(FeedType.REFINE.getType(), SpringUtils.getBean("RefineHandler", RefineHandler.class));
        FeedHelper.INSTANCE.init(FeedType.REFINE_HIGH.getType(), SpringUtils.getBean("RefineHandler", RefineHandler.class));
        FeedHelper.INSTANCE.init(FeedType.INSET_STONE.getType(), SpringUtils.getBean("InsetStoneHandler", InsetStoneHandler.class));
        FeedHelper.INSTANCE.init(FeedType.EXERCISE.getType(), SpringUtils.getBean("ExerciseHandler", ExerciseHandler.class));
        FeedHelper.INSTANCE.init(FeedType.INTIMATE.getType(), SpringUtils.getBean("IntimateHandler", IntimateHandler.class));
        FeedHelper.INSTANCE.init(FeedType.BONE.getType(), SpringUtils.getBean("BoneHandler", BoneHandler.class));
        FeedHelper.INSTANCE.init(FeedType.GRID.getType(), SpringUtils.getBean("GridHandler", GridHandler.class));
        FeedHelper.INSTANCE.init(FeedType.EXP.getType(), SpringUtils.getBean("ExpHandler", ExpHandler.class));
        FeedHelper.INSTANCE.init(FeedType.RESTS_SKILL.getType(), SpringUtils.getBean("RestsSkillHandler", RestsSkillHandler.class));
        FeedHelper.INSTANCE.init(FeedType.DOGZ.getType(), SpringUtils.getBean("DogzHandler", DogzHandler.class));
        FeedHelper.INSTANCE.init(FeedType.SOUL.getType(), SpringUtils.getBean("SoulHandler", SoulHandler.class));
        FeedHelper.INSTANCE.init(FeedType.YUAN_QI_DAN.getType(), SpringUtils.getBean("YuanQiDanHandler", YuanQiDanHandler.class));
        FeedHelper.INSTANCE.init(FeedType.AWAKEN.getType(), SpringUtils.getBean("AwakenHandler", AwakenHandler.class));
        FeedHelper.INSTANCE.init(FeedType.HF.getType(), SpringUtils.getBean("HfHandler", HfHandler.class));
        FeedHelper.INSTANCE.init(FeedType.CUT_HF.getType(), SpringUtils.getBean("CutHfHandler", CutHfHandler.class));
    }
}
