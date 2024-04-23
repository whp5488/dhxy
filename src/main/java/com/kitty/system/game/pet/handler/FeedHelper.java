package com.kitty.system.game.pet.handler;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.enums.pet.FeedType;
import java.util.HashMap;
import java.util.Map;

public enum FeedHelper {
  INSTANCE;
  
  private static final Map<String, FeedPetHandler> handlers;
  
  static {
    handlers = new HashMap<>();
    INSTANCE.init(FeedType.REFINE.getType(), (FeedPetHandler)SpringUtils.getBean("RefineHandler", RefineHandler.class));
    INSTANCE.init(FeedType.REFINE_HIGH.getType(), (FeedPetHandler)SpringUtils.getBean("RefineHandler", RefineHandler.class));
    INSTANCE.init(FeedType.INSET_STONE.getType(), (FeedPetHandler)SpringUtils.getBean("InsetStoneHandler", InsetStoneHandler.class));
    INSTANCE.init(FeedType.EXERCISE.getType(), (FeedPetHandler)SpringUtils.getBean("ExerciseHandler", ExerciseHandler.class));
    INSTANCE.init(FeedType.INTIMATE.getType(), (FeedPetHandler)SpringUtils.getBean("IntimateHandler", IntimateHandler.class));
    INSTANCE.init(FeedType.BONE.getType(), (FeedPetHandler)SpringUtils.getBean("BoneHandler", BoneHandler.class));
    INSTANCE.init(FeedType.GRID.getType(), (FeedPetHandler)SpringUtils.getBean("GridHandler", GridHandler.class));
    INSTANCE.init(FeedType.EXP.getType(), (FeedPetHandler)SpringUtils.getBean("ExpHandler", ExpHandler.class));
    INSTANCE.init(FeedType.RESTS_SKILL.getType(), (FeedPetHandler)SpringUtils.getBean("RestsSkillHandler", RestsSkillHandler.class));
    INSTANCE.init(FeedType.DOGZ.getType(), (FeedPetHandler)SpringUtils.getBean("DogzHandler", DogzHandler.class));
    INSTANCE.init(FeedType.SOUL.getType(), (FeedPetHandler)SpringUtils.getBean("SoulHandler", SoulHandler.class));
    INSTANCE.init(FeedType.YUAN_QI_DAN.getType(), (FeedPetHandler)SpringUtils.getBean("YuanQiDanHandler", YuanQiDanHandler.class));
    INSTANCE.init(FeedType.AWAKEN.getType(), (FeedPetHandler)SpringUtils.getBean("AwakenHandler", AwakenHandler.class));
    INSTANCE.init(FeedType.HF.getType(), (FeedPetHandler)SpringUtils.getBean("HfHandler", HfHandler.class));
    INSTANCE.init(FeedType.CUT_HF.getType(), (FeedPetHandler)SpringUtils.getBean("CutHfHandler", CutHfHandler.class));
  }
  
  public void init(String type, FeedPetHandler feedPetHandler) {
    handlers.put(type, feedPetHandler);
  }
  
  public FeedPetHandler getHandler(String type) {
    return handlers.get(type);
  }
}
