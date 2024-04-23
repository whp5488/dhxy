package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 32768)
public class ReqTelePort extends Message {
  private int mapId;
  
  private int x;
  
  private int y;

  private int isTaskWalk = 0;

  public ReqTelePort(int mapId, int x, int y) {
    this.mapId = mapId;
    this.x = x;
    this.y = y;
  }
  
  public ReqTelePort(int mapId, int x, int y, int isTaskWalk) {
    this.mapId = mapId;
    this.x = x;
    this.y = y;
    this.isTaskWalk = isTaskWalk;
  }
  
  public ReqTelePort() {}
}
