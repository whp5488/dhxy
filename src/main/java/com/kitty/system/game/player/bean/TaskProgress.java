package com.kitty.system.game.player.bean;

import com.kitty.common.utils.TimeUtil;
import com.kitty.system.common.utils.NutConcurrentMap;
import com.kitty.system.game.activity.model.product.ActivityType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

@Setter
@Getter
public class TaskProgress implements Serializable {

  private int loop = 1;

  private NutConcurrentMap finishCounts = new NutConcurrentMap();

  private NutConcurrentMap finishTallyCounts = new NutConcurrentMap();

  private NutConcurrentMap finishRecord = new NutConcurrentMap();

  private NutConcurrentMap everyDayUpperLimit = new NutConcurrentMap();

  private NutConcurrentMap everyDayRecharge = new NutConcurrentMap();

  private float liveness = 0.0F;

  private ConcurrentHashMap<Integer, Boolean> livenessRewardFetchStatus = new ConcurrentHashMap<>();

  public void dailyReset(boolean flag) {
    int loop = this.finishCounts.getInt(ActivityType.LOOP.getActivityId(), 0);
    this.finishCounts = NutConcurrentMap.NEW();
    if (loop > 0 && !flag)
      this.finishCounts.setv(ActivityType.LOOP.getActivityId(), Integer.valueOf(loop)); 
    this.liveness = 0.0F;
    this.everyDayUpperLimit = NutConcurrentMap.NEW();
    this.livenessRewardFetchStatus = new ConcurrentHashMap<>();
    finish();
    recharge();
  }
  
  public void recharge() {
    this.everyDayRecharge = NutConcurrentMap.NEW();
  }
  
  public void finish() {
    String when = TimeUtil.when();
    int moFrontCount = this.finishRecord.getInt(ActivityType.MWK_FRONT.getActivityId(), 0);
    this.finishRecord = NutConcurrentMap.NEW();
    if (!"星期六".equals(when))
      this.finishRecord.setv(ActivityType.MWK_FRONT.getActivityId(), Integer.valueOf(moFrontCount)); 
  }
}
