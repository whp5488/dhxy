package com.kitty.system.game.task.service.handler;

import com.kitty.system.game.task.service.handler.dungeon.DungeonCmHandler;
import com.kitty.system.game.task.service.handler.activity.KejuTaskHandler;
import com.kitty.system.game.task.service.handler.dungeon.DungeonMwkFront;
import com.kitty.system.game.task.service.handler.dungeon.DungeonXmHandler;
import com.kitty.common.utils.spring.SpringUtils;
import java.util.HashMap;
import com.kitty.system.enums.task.TaskType;
import java.util.Map;

public enum TaskHelper
{
    INSTANCE;
    
    private static final Map<TaskType, TaskHandler> handlers;
    
    public void init(TaskType taskType, TaskHandler taskHandler) {
        TaskHelper.handlers.put(taskType, taskHandler);
    }
    
    public TaskHandler getTaskHandler(TaskType taskType) {
        return (TaskHandler)TaskHelper.handlers.get(taskType);
    }
    
    public Map<TaskType, TaskHandler> getHandlers() {
        return TaskHelper.handlers;
    }
    
    static {
        handlers = new HashMap();
        TaskHelper.INSTANCE.init(TaskType.MAIN_LINE, (TaskHandler)SpringUtils.getBean(MainTaskHandler.class));
        TaskHelper.INSTANCE.init(TaskType.SCHOOL, (TaskHandler)SpringUtils.getBean(SchoolTaskHandler.class));
        TaskHelper.INSTANCE.init(TaskType.TREASURE_MAP, (TaskHandler)SpringUtils.getBean(TreasureMapHandler.class));
        TaskHelper.INSTANCE.init(TaskType.PARTY, (TaskHandler)SpringUtils.getBean(PartyTaskHandler.class));
        TaskHelper.INSTANCE.init(TaskType.LOOP, (TaskHandler)SpringUtils.getBean(LoopTaskHandler.class));
        TaskHelper.INSTANCE.init(TaskType.TOWER, (TaskHandler)SpringUtils.getBean(TowerTaskHandler.class));
        TaskHelper.INSTANCE.init(TaskType.LOOK_FOR, (TaskHandler)SpringUtils.getBean(LookForTaskHandler.class));
        TaskHelper.INSTANCE.init(TaskType.XM, (TaskHandler)SpringUtils.getBean(DungeonXmHandler.class));
        TaskHelper.INSTANCE.init(TaskType.FIVE, (TaskHandler)SpringUtils.getBean(FiveTaskHandler.class));
        TaskHelper.INSTANCE.init(TaskType.CATCHES_GHOSTS, (TaskHandler)SpringUtils.getBean(CatchesGhostsTaskHandler.class));
        TaskHelper.INSTANCE.init(TaskType.GODS_COURT, (TaskHandler)SpringUtils.getBean(GodsCourtTaskHandler.class));
        TaskHelper.INSTANCE.init(TaskType.CARNIVAL, (TaskHandler)SpringUtils.getBean(CarnivalTaskHandler.class));
        TaskHelper.INSTANCE.init(TaskType.CRUSADE_RENEGADE, (TaskHandler)SpringUtils.getBean(CrusadeRenegadeTaskHandler.class));
        TaskHelper.INSTANCE.init(TaskType.DRAGON, (TaskHandler)SpringUtils.getBean(DragonTaskHandler.class));
        TaskHelper.INSTANCE.init(TaskType.MWK_FRONT, (TaskHandler)SpringUtils.getBean(DungeonMwkFront.class));
        TaskHelper.INSTANCE.init(TaskType.KJ, (TaskHandler)SpringUtils.getBean(KejuTaskHandler.class));
        TaskHelper.INSTANCE.init(TaskType.CM, (TaskHandler)SpringUtils.getBean(DungeonCmHandler.class));
        TaskHelper.INSTANCE.init(TaskType.XL, (TaskHandler)SpringUtils.getBean(ConquerAsuraTaskHandler.class));
    }
}
