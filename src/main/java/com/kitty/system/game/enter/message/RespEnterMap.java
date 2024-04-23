package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 65505)
public class RespEnterMap extends Message {
  private int mapId;
  
  private int posX;
  
  private int posY;
  
  private int dir;
  
  private byte safeZone;
  
  public void setMapId(int mapId) {
    this.mapId = mapId;
  }
  
  public void setPosX(int posX) {
    this.posX = posX;
  }
  
  public void setPosY(int posY) {
    this.posY = posY;
  }
  
  public void setDir(int dir) {
    this.dir = dir;
  }
  
  public void setSafeZone(byte safeZone) {
    this.safeZone = safeZone;
  }
  
  public void setIsTaskWalk(byte isTaskWalk) {
    this.isTaskWalk = isTaskWalk;
  }
  
  public int getMapId() {
    return this.mapId;
  }
  
  public int getPosX() {
    return this.posX;
  }
  
  public int getPosY() {
    return this.posY;
  }
  
  public int getDir() {
    return this.dir;
  }
  
  public byte getSafeZone() {
    return this.safeZone;
  }
  
  private byte isTaskWalk = 0;
  
  public byte getIsTaskWalk() {
    return this.isTaskWalk;
  }
}
