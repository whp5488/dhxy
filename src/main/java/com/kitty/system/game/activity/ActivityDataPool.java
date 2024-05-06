package com.kitty.system.game.activity;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.game.activity.handler.time.BurningHotHandler;
import com.kitty.system.game.activity.handler.time.MonsterNianHandler;
import com.kitty.system.game.activity.handler.time.PartyBanditHandler;
import com.kitty.system.game.activity.handler.time.SealDemonHandler;
import com.kitty.system.game.activity.handler.time.StarOfficerHandler;
import com.kitty.system.game.activity.handler.time.ThreeScopeHandler;
import com.kitty.system.game.activity.handler.time.WorldBoosHandler;
import com.kitty.system.game.activity.handler.timing.at.AtWillHandler;
import com.kitty.system.game.activity.handler.timing.at.AtWillPkHandler;
import com.kitty.system.game.activity.handler.timing.ca.CaHandler;
import com.kitty.system.game.activity.handler.timing.ca.CaPkHandler;
import com.kitty.system.game.activity.handler.timing.hg.HgFightHandler;
import com.kitty.system.game.activity.handler.timing.wars.GangWarsHandler;
import com.kitty.system.game.activity.handler.timing.wars.GangWarsPkHandler;
import com.kitty.system.game.activity.handler.timing.water.WaterHandler;
import com.kitty.system.game.activity.handler.timing.water.WaterPkHandler;
import com.kitty.system.game.activity.model.Event;
import com.kitty.system.game.activity.model.product.ActivityLivenessRewardSet;
import com.kitty.system.game.activity.model.product.ActivityLivenessSet;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.activity.service.ActivityHandler;
import com.kitty.system.game.fightnew.handler.FightEndEquipHandle;
import com.kitty.system.game.pk.service.PkService;
import com.kitty.system.init.domain.vo.Param;
import com.kitty.system.init.service.InitService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ActivityDataPool {
  public static Map<Integer, Event> eventMap = new HashMap<>();
  
  public static Map<ActivityType, ActivityLivenessSet> activityLivenessSetMapByType = new HashMap<>();
  
  public static LinkedHashMap<Integer, ActivityLivenessRewardSet> activityLivenessRewardSetMap = new LinkedHashMap<>();
  
  public static List<ActivityHandler> activityHandlers = new ArrayList<>();
  
  static {
    activityHandlers.add(SpringUtils.getBean(AtWillHandler.class));
    activityHandlers.add(SpringUtils.getBean(FightEndEquipHandle.class));
    activityHandlers.add(SpringUtils.getBean(PkService.class));
    activityHandlers.add(SpringUtils.getBean(HgFightHandler.class));
    activityHandlers.add(SpringUtils.getBean(PartyBanditHandler.class));
    activityHandlers.add(SpringUtils.getBean(StarOfficerHandler.class));
    activityHandlers.add(SpringUtils.getBean(WorldBoosHandler.class));
    activityHandlers.add(SpringUtils.getBean(SealDemonHandler.class));
    activityHandlers.add(SpringUtils.getBean(ThreeScopeHandler.class));
    activityHandlers.add(SpringUtils.getBean(AtWillPkHandler.class));
    activityHandlers.add(SpringUtils.getBean(WaterPkHandler.class));
    activityHandlers.add(SpringUtils.getBean(CaPkHandler.class));
    activityHandlers.add(SpringUtils.getBean(GangWarsPkHandler.class));
    activityHandlers.add(SpringUtils.getBean(MonsterNianHandler.class));
    activityHandlers.add(SpringUtils.getBean(BurningHotHandler.class));
    activityHandlers.add(SpringUtils.getBean(AtWillHandler.class));
    activityHandlers.add(SpringUtils.getBean(CaHandler.class));
    activityHandlers.add(SpringUtils.getBean(WaterHandler.class));
    activityHandlers.add(SpringUtils.getBean(GangWarsHandler.class));
    addActivityLivenessSet(new ActivityLivenessSet("师门任务", ActivityType.SCHOOL_TASK, 0.5F, 20));
    addActivityLivenessSet(new ActivityLivenessSet("宝图任务", ActivityType.TREASURE_MAP_TASK, 1.0F, 15));
    addActivityLivenessSet(new ActivityLivenessSet("帮派任务", ActivityType.PARTY_TASK, 0.5F, 20));
    addActivityLivenessSet(new ActivityLivenessSet("大雁塔副本", ActivityType.TOWER_DUGEON, 10.0F, 20));
    addActivityLivenessSet(new ActivityLivenessSet("寻访副本", ActivityType.LOOK_FOR_DUGEON, 10.0F, 20));
    addActivityLivenessSet(new ActivityLivenessSet("五环任务", ActivityType.FIVE_TASK, 1.0F, 5));
    addActivityLivenessSet(new ActivityLivenessSet("钟馗抓鬼", ActivityType.CATCHES_GHOSTS_TASK, 3.0F, 60));
    addActivityLivenessSet(new ActivityLivenessSet("天庭降妖", ActivityType.GODS_COURT_TASK, 3.0F, 60));
    addActivityLivenessSet(new ActivityLivenessSet("大理寺答题", ActivityType.QUESTION_DLS, 2.0F, 20));
    addActivityLivenessSet(new ActivityLivenessSet("嘉年华", ActivityType.CARNIVAL, 0.5F, 30));
    addActivityLivenessSet(new ActivityLivenessSet("心魔", ActivityType.XM_DUGEON, 1.33F, 20));
    activityLivenessRewardSetMap.put(1, new ActivityLivenessRewardSet(25, Arrays.asList(new Param(ItemClassify.RES_TYPE_GOLD.getId(), 1000, 0))));
    activityLivenessRewardSetMap.put(2, new ActivityLivenessRewardSet(50, Arrays.asList(new Param(ItemClassify.RES_TYPE_GOLD.getId(), 1500, 0))));
    activityLivenessRewardSetMap.put(3, new ActivityLivenessRewardSet(75, Arrays.asList(new Param(ItemClassify.RES_TYPE_GOLD.getId(), 2000, 0))));
    activityLivenessRewardSetMap.put(4, new ActivityLivenessRewardSet(100, Arrays.asList(new Param(ItemClassify.RES_TYPE_GOLD.getId(), 2500, 0))));
    activityLivenessRewardSetMap.put(5, new ActivityLivenessRewardSet(125, Arrays.asList(new Param(ItemClassify.RES_TYPE_GOLD.getId(), 3000, 0))));
    activityLivenessRewardSetMap.put(6, new ActivityLivenessRewardSet(150, Arrays.asList(new Param(ItemClassify.RES_TYPE_GOLD.getId(), 3500, 0))));
  }
  
  private static void addActivityLivenessSet(ActivityLivenessSet activityLivenessSet) {
    activityLivenessSetMapByType.put(activityLivenessSet.getActivityType(), activityLivenessSet);
  }
  
  public static void load() {
    InitService initService = SpringUtils.getInitService();
    eventMap = initService.initEvent();
  }
}
