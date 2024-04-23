package com.kitty.system.game.task.service.handler;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.enums.task.TaskType;
import com.kitty.system.game.task.service.handler.activity.KejuTaskHandler;
import com.kitty.system.game.task.service.handler.dungeon.DungeonMwkFront;
import com.kitty.system.game.task.service.handler.dungeon.DungeonXmHandler;
import java.util.HashMap;
import java.util.Map;

public enum TaskHelper {
  INSTANCE;
  
  private static final Map<TaskType, TaskHandler> handlers;
  
  static {
    handlers = new HashMap<>();
    INSTANCE.init(TaskType.MAIN_LINE, (TaskHandler)SpringUtils.getBean(MainTaskHandler.class));
    INSTANCE.init(TaskType.SCHOOL, (TaskHandler)SpringUtils.getBean(SchoolTaskHandler.class));
    INSTANCE.init(TaskType.TREASURE_MAP, (TaskHandler)SpringUtils.getBean(TreasureMapHandler.class));
    INSTANCE.init(TaskType.PARTY, (TaskHandler)SpringUtils.getBean(PartyTaskHandler.class));
    INSTANCE.init(TaskType.LOOP, (TaskHandler)SpringUtils.getBean(LoopTaskHandler.class));
    INSTANCE.init(TaskType.TOWER, (TaskHandler)SpringUtils.getBean(TowerTaskHandler.class));
    INSTANCE.init(TaskType.LOOK_FOR, (TaskHandler)SpringUtils.getBean(LookForTaskHandler.class));
    INSTANCE.init(TaskType.XM, (TaskHandler)SpringUtils.getBean(DungeonXmHandler.class));
    INSTANCE.init(TaskType.FIVE, (TaskHandler)SpringUtils.getBean(FiveTaskHandler.class));
    INSTANCE.init(TaskType.CATCHES_GHOSTS, (TaskHandler)SpringUtils.getBean(CatchesGhostsTaskHandler.class));
    INSTANCE.init(TaskType.GODS_COURT, (TaskHandler)SpringUtils.getBean(GodsCourtTaskHandler.class));
    INSTANCE.init(TaskType.CARNIVAL, (TaskHandler)SpringUtils.getBean(CarnivalTaskHandler.class));
    INSTANCE.init(TaskType.CRUSADE_RENEGADE, (TaskHandler)SpringUtils.getBean(CrusadeRenegadeTaskHandler.class));
    INSTANCE.init(TaskType.DRAGON, (TaskHandler)SpringUtils.getBean(DragonTaskHandler.class));
    INSTANCE.init(TaskType.MWK_FRONT, (TaskHandler)SpringUtils.getBean(DungeonMwkFront.class));
    INSTANCE.init(TaskType.KJ, (TaskHandler)SpringUtils.getBean(KejuTaskHandler.class));
  }
  
  public void init(TaskType taskType, TaskHandler taskHandler) {
    handlers.put(taskType, taskHandler);
  }
  
  public TaskHandler getTaskHandler(TaskType taskType) {
    return handlers.get(taskType);
  }
  
  public Map<TaskType, TaskHandler> getHandlers() {
    return handlers;
  }
}
