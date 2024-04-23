package com.kitty.system.game.ranking.pojo;

import java.io.Serializable;

public class RankingField implements Serializable {
  private int order;
  
  private byte serverId;
  
  public RankingField() {}
  
  public RankingField(int order, byte serverId) {
    this.order = order;
    this.serverId = serverId;
  }
}
