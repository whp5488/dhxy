package com.kitty.system.game.activity.model.product;

import com.kitty.system.init.domain.vo.Param;
import java.util.List;

public class ActivityLivenessRewardSet {
  private int livenessCount;
  
  private List<Param> list;
  
  public void setLivenessCount(int livenessCount) {
    this.livenessCount = livenessCount;
  }
  
  public void setList(List<Param> list) {
    this.list = list;
  }
  
  public int getLivenessCount() {
    return this.livenessCount;
  }
  
  public List<Param> getList() {
    return this.list;
  }
  
  public ActivityLivenessRewardSet() {}
  
  public ActivityLivenessRewardSet(int livenessCount, List<Param> list) {
    this.livenessCount = livenessCount;
    this.list = list;
  }
}
