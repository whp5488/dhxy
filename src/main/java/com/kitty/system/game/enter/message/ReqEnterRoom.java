package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4144)
public class ReqEnterRoom extends Message {
  private String mapName;
  
  private int isTaskWalk;
  
  public void setMapName(String mapName) {
    this.mapName = mapName;
  }
  
  public void setIsTaskWalk(int isTaskWalk) {
    this.isTaskWalk = isTaskWalk;
  }
  
  public String getMapName() {
    return this.mapName;
  }
  
  public int getIsTaskWalk() {
    return this.isTaskWalk;
  }
}
