
package com.kitty.system.game.activity;

import java.util.Arrays;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.init.domain.vo.Param;
import com.kitty.system.game.activity.handler.timing.wars.GangWarsHandler;
import com.kitty.system.game.activity.handler.timing.water.WaterHandler;
import com.kitty.system.game.activity.handler.timing.ca.CaHandler;
import com.kitty.system.game.activity.handler.time.BurningHotHandler;
import com.kitty.system.game.activity.handler.time.MonsterNianHandler;
import com.kitty.system.game.activity.handler.timing.wars.GangWarsPkHandler;
import com.kitty.system.game.activity.handler.timing.ca.CaPkHandler;
import com.kitty.system.game.activity.handler.timing.water.WaterPkHandler;
import com.kitty.system.game.activity.handler.timing.at.AtWillPkHandler;
import com.kitty.system.game.activity.handler.time.ThreeScopeHandler;
import com.kitty.system.game.activity.handler.time.SealDemonHandler;
import com.kitty.system.game.activity.handler.time.WorldBoosHandler;
import com.kitty.system.game.activity.handler.time.StarOfficerHandler;
import com.kitty.system.game.activity.handler.time.PartyBanditHandler;
import com.kitty.system.game.activity.handler.timing.hg.HgFightHandler;
import com.kitty.system.game.pk.service.PkService;
import com.kitty.system.game.fightnew.handler.FightEndEquipHandle;
import com.kitty.system.game.activity.handler.timing.at.AtWillHandler;
import java.util.ArrayList;
import java.util.HashMap;
import com.kitty.system.init.service.InitService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.activity.service.ActivityHandler;
import java.util.List;
import com.kitty.system.game.activity.model.product.ActivityLivenessRewardSet;
import java.util.LinkedHashMap;
import com.kitty.system.game.activity.model.product.ActivityLivenessSet;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.activity.model.Event;
import java.util.Map;

public class ActivityDataPool
{
    public static Map<Integer, Event> eventMap;
    public static Map<ActivityType, ActivityLivenessSet> activityLivenessSetMapByType;
    public static LinkedHashMap<Integer, ActivityLivenessRewardSet> activityLivenessRewardSetMap;
    public static List<ActivityHandler> activityHandlers;
    
    private static void addActivityLivenessSet(final ActivityLivenessSet activityLivenessSet) {
        ActivityDataPool.activityLivenessSetMapByType.put(activityLivenessSet.getActivityType(), activityLivenessSet);
    }
    
    public static void load() {
        final InitService initService = SpringUtils.getInitService();
        ActivityDataPool.eventMap = initService.initEvent();
    }
    
    static {
        ActivityDataPool.eventMap = new HashMap<>();
        ActivityDataPool.activityLivenessSetMapByType = new HashMap<>();
        ActivityDataPool.activityLivenessRewardSetMap = new LinkedHashMap<>();
        (ActivityDataPool.activityHandlers = new ArrayList<>()).add(SpringUtils.getBean(AtWillHandler.class));
        ActivityDataPool.activityHandlers.add(SpringUtils.getBean(FightEndEquipHandle.class));
        ActivityDataPool.activityHandlers.add(SpringUtils.getBean(PkService.class));
        ActivityDataPool.activityHandlers.add(SpringUtils.getBean(HgFightHandler.class));
        ActivityDataPool.activityHandlers.add(SpringUtils.getBean(PartyBanditHandler.class));
        ActivityDataPool.activityHandlers.add(SpringUtils.getBean(StarOfficerHandler.class));
        ActivityDataPool.activityHandlers.add(SpringUtils.getBean(WorldBoosHandler.class));
        ActivityDataPool.activityHandlers.add(SpringUtils.getBean(SealDemonHandler.class));
        ActivityDataPool.activityHandlers.add(SpringUtils.getBean(ThreeScopeHandler.class));
        ActivityDataPool.activityHandlers.add(SpringUtils.getBean(AtWillPkHandler.class));
        ActivityDataPool.activityHandlers.add(SpringUtils.getBean(WaterPkHandler.class));
        ActivityDataPool.activityHandlers.add(SpringUtils.getBean(CaPkHandler.class));
        ActivityDataPool.activityHandlers.add(SpringUtils.getBean(GangWarsPkHandler.class));
        ActivityDataPool.activityHandlers.add(SpringUtils.getBean(MonsterNianHandler.class));
        ActivityDataPool.activityHandlers.add(SpringUtils.getBean(BurningHotHandler.class));
        ActivityDataPool.activityHandlers.add(SpringUtils.getBean(AtWillHandler.class));
        ActivityDataPool.activityHandlers.add(SpringUtils.getBean(CaHandler.class));
        ActivityDataPool.activityHandlers.add(SpringUtils.getBean(WaterHandler.class));
        ActivityDataPool.activityHandlers.add(SpringUtils.getBean(GangWarsHandler.class));
        addActivityLivenessSet(new ActivityLivenessSet("师门任务", ActivityType.SCHOOL_TASK, 0.5f, 20));
        addActivityLivenessSet(new ActivityLivenessSet("宝图任务", ActivityType.TREASURE_MAP_TASK, 1.0f, 15));
        addActivityLivenessSet(new ActivityLivenessSet("帮派任务", ActivityType.PARTY_TASK, 0.5f, 20));
        addActivityLivenessSet(new ActivityLivenessSet("大雁塔副本", ActivityType.TOWER_DUGEON, 10.0f, 20));
        addActivityLivenessSet(new ActivityLivenessSet("寻访副本", ActivityType.LOOK_FOR_DUGEON, 10.0f, 20));
        //addActivityLivenessSet(new ActivityLivenessSet("五环任务", ActivityType.FIVE_TASK, 1.0f, 5));
        addActivityLivenessSet(new ActivityLivenessSet("五环任务", ActivityType.FIVE_TASK, 1.0f, 1));
        addActivityLivenessSet(new ActivityLivenessSet("钟馗抓鬼", ActivityType.CATCHES_GHOSTS_TASK, 3.0f, 60));
        addActivityLivenessSet(new ActivityLivenessSet("天庭降妖", ActivityType.GODS_COURT_TASK, 3.0f, 60));
        addActivityLivenessSet(new ActivityLivenessSet("大理寺答题", ActivityType.QUESTION_DLS, 2.0f, 20));
        addActivityLivenessSet(new ActivityLivenessSet("嘉年华", ActivityType.CARNIVAL, 0.5f, 30));
        addActivityLivenessSet(new ActivityLivenessSet("心魔", ActivityType.XM_DUGEON, 1.33f, 20));
        ActivityDataPool.activityLivenessRewardSetMap.put(1, new ActivityLivenessRewardSet(25, Arrays.asList(new Param(ItemClassify.RES_TYPE_GOLD.getId(), 1000, 0))));
        ActivityDataPool.activityLivenessRewardSetMap.put(2, new ActivityLivenessRewardSet(50, Arrays.asList(new Param(ItemClassify.RES_TYPE_GOLD.getId(), 1500, 0))));
        ActivityDataPool.activityLivenessRewardSetMap.put(3, new ActivityLivenessRewardSet(75, Arrays.asList(new Param(ItemClassify.RES_TYPE_GOLD.getId(), 2000, 0))));
        ActivityDataPool.activityLivenessRewardSetMap.put(4, new ActivityLivenessRewardSet(100, Arrays.asList(new Param(ItemClassify.RES_TYPE_GOLD.getId(), 2500, 0))));
        ActivityDataPool.activityLivenessRewardSetMap.put(5, new ActivityLivenessRewardSet(125, Arrays.asList(new Param(ItemClassify.RES_TYPE_GOLD.getId(), 3000, 0))));
        ActivityDataPool.activityLivenessRewardSetMap.put(6, new ActivityLivenessRewardSet(150, Arrays.asList(new Param(ItemClassify.RES_TYPE_GOLD.getId(), 3500, 0))));
    }
}
