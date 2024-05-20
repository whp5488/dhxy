
package com.kitty.system.game.task.service.handler;

import com.kitty.system.game.task.service.handler.activity.KejuTaskHandler;
import com.kitty.system.game.task.service.handler.dungeon.DungeonCmHandler;
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
    
    public void init(final TaskType taskType, final TaskHandler taskHandler) {
        TaskHelper.handlers.put(taskType, taskHandler);
    }
    
    public TaskHandler getTaskHandler(final TaskType taskType) {
        return TaskHelper.handlers.get(taskType);
    }
    
    public Map<TaskType, TaskHandler> getHandlers() {
        return TaskHelper.handlers;
    }
    
    static {
        handlers = new HashMap<>();
        TaskHelper.INSTANCE.init(TaskType.MAIN_LINE, SpringUtils.getBean(MainTaskHandler.class));
        TaskHelper.INSTANCE.init(TaskType.SCHOOL, SpringUtils.getBean(SchoolTaskHandler.class));
        TaskHelper.INSTANCE.init(TaskType.TREASURE_MAP, SpringUtils.getBean(TreasureMapHandler.class));
        TaskHelper.INSTANCE.init(TaskType.PARTY, SpringUtils.getBean(PartyTaskHandler.class));
        TaskHelper.INSTANCE.init(TaskType.LOOP, SpringUtils.getBean(LoopTaskHandler.class));
        TaskHelper.INSTANCE.init(TaskType.TOWER, SpringUtils.getBean(TowerTaskHandler.class));
        TaskHelper.INSTANCE.init(TaskType.LOOK_FOR, SpringUtils.getBean(LookForTaskHandler.class));
        TaskHelper.INSTANCE.init(TaskType.XM, SpringUtils.getBean(DungeonXmHandler.class));
        TaskHelper.INSTANCE.init(TaskType.FIVE, SpringUtils.getBean(FiveTaskHandler.class));
        TaskHelper.INSTANCE.init(TaskType.CATCHES_GHOSTS, SpringUtils.getBean(CatchesGhostsTaskHandler.class));
        TaskHelper.INSTANCE.init(TaskType.GODS_COURT, SpringUtils.getBean(GodsCourtTaskHandler.class));
        TaskHelper.INSTANCE.init(TaskType.CARNIVAL, SpringUtils.getBean(CarnivalTaskHandler.class));
        TaskHelper.INSTANCE.init(TaskType.CRUSADE_RENEGADE, SpringUtils.getBean(CrusadeRenegadeTaskHandler.class));
        TaskHelper.INSTANCE.init(TaskType.XL, SpringUtils.getBean(ConquerAsuraTaskHandler.class));
        TaskHelper.INSTANCE.init(TaskType.DRAGON, SpringUtils.getBean(DragonTaskHandler.class));
        TaskHelper.INSTANCE.init(TaskType.MWK_FRONT, SpringUtils.getBean(DungeonMwkFront.class));
        TaskHelper.INSTANCE.init(TaskType.CM, SpringUtils.getBean(DungeonCmHandler.class));
        TaskHelper.INSTANCE.init(TaskType.KJ, SpringUtils.getBean(KejuTaskHandler.class));
    }
}
