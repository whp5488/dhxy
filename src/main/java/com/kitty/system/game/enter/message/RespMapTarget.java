package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 65506)
public class RespMapTarget extends Message {
  private int mapId;
  
  private int posX;
  
  private int posY;
  
  public void setMapId(int mapId) {
    this.mapId = mapId;
  }
  
  public void setPosX(int posX) {
    this.posX = posX;
  }
  
  public void setPosY(int posY) {
    this.posY = posY;
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
}
