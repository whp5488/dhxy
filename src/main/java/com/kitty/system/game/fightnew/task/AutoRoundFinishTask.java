package com.kitty.system.game.fightnew.task;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.service.FightExecutorNewService;
import com.kitty.system.game.fightnew.service.FightNewService;

public class AutoRoundFinishTask implements FightingTask {
  private int objId;
  
  private Fight fight;
  
  public AutoRoundFinishTask() {}
  
  public AutoRoundFinishTask(Fight fight, int objId) {
    this.fight = fight;
    this.objId = objId;
  }
  
  public void run() {
    ((FightExecutorNewService)SpringUtils.getBean(FightExecutorNewService.class)).addFightTask(this.fight, () -> ((FightNewService)SpringUtils.getBean(FightNewService.class)).animationPlayEnd(this.fight, this.objId));
  }
}
