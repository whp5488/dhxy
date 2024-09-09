package com.kitty.system.game.pet.handler;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.enums.pet.FeedType;
import java.util.HashMap;
import java.util.Map;

public enum FeedHelper
{
    INSTANCE;
    
    private static final Map<String, FeedPetHandler> handlers;
    
    public void init(String type, FeedPetHandler feedPetHandler) {
        FeedHelper.handlers.put(type, feedPetHandler);
    }
    
    public FeedPetHandler getHandler(String type) {
        return (FeedPetHandler)FeedHelper.handlers.get(type);
    }
    
    static {
        handlers = new HashMap();
        FeedHelper.INSTANCE.init(FeedType.REFINE.getType(), (FeedPetHandler)SpringUtils.getBean("RefineHandler", RefineHandler.class));
        FeedHelper.INSTANCE.init(FeedType.REFINE_HIGH.getType(), (FeedPetHandler)SpringUtils.getBean("RefineHandler", RefineHandler.class));
        FeedHelper.INSTANCE.init(FeedType.INSET_STONE.getType(), (FeedPetHandler)SpringUtils.getBean("InsetStoneHandler", InsetStoneHandler.class));
        FeedHelper.INSTANCE.init(FeedType.EXERCISE.getType(), (FeedPetHandler)SpringUtils.getBean("ExerciseHandler", ExerciseHandler.class));
        FeedHelper.INSTANCE.init(FeedType.INTIMATE.getType(), (FeedPetHandler)SpringUtils.getBean("IntimateHandler", IntimateHandler.class));
        FeedHelper.INSTANCE.init(FeedType.BONE.getType(), (FeedPetHandler)SpringUtils.getBean("BoneHandler", BoneHandler.class));
        FeedHelper.INSTANCE.init(FeedType.GRID.getType(), (FeedPetHandler)SpringUtils.getBean("GridHandler", GridHandler.class));
        FeedHelper.INSTANCE.init(FeedType.EXP.getType(), (FeedPetHandler)SpringUtils.getBean("ExpHandler", ExpHandler.class));
        FeedHelper.INSTANCE.init(FeedType.RESTS_SKILL.getType(), (FeedPetHandler)SpringUtils.getBean("RestsSkillHandler", RestsSkillHandler.class));
        FeedHelper.INSTANCE.init(FeedType.DOGZ.getType(), (FeedPetHandler)SpringUtils.getBean("DogzHandler", DogzHandler.class));
        FeedHelper.INSTANCE.init(FeedType.SOUL.getType(), (FeedPetHandler)SpringUtils.getBean("SoulHandler", SoulHandler.class));
        FeedHelper.INSTANCE.init(FeedType.YUAN_QI_DAN.getType(), (FeedPetHandler)SpringUtils.getBean("YuanQiDanHandler", YuanQiDanHandler.class));
        FeedHelper.INSTANCE.init(FeedType.AWAKEN.getType(), (FeedPetHandler)SpringUtils.getBean("AwakenHandler", AwakenHandler.class));
        FeedHelper.INSTANCE.init(FeedType.HF.getType(), (FeedPetHandler)SpringUtils.getBean("HfHandler", HfHandler.class));
        FeedHelper.INSTANCE.init(FeedType.CUT_HF.getType(), (FeedPetHandler)SpringUtils.getBean("CutHfHandler", CutHfHandler.class));
    }
}
