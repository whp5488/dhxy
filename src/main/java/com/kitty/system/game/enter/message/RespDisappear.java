package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.enums.war.wardef.LOGIC;
import java.beans.ConstructorProperties;

@MessageMeta(module = 12285)
public class RespDisappear extends Message {
  private int id;
  
  private int type;
  
  public void setId(int id) {
    this.id = id;
  }
  
  public void setType(int type) {
    this.type = type;
  }
  
  public RespDisappear() {
    this.type = LOGIC.LOGICTYPE_HERO.getValue();
  }
  
  @ConstructorProperties({"id", "type"})
  public RespDisappear(int id, int type) {
    this.type = LOGIC.LOGICTYPE_HERO.getValue();
    this.id = id;
    this.type = type;
  }
  
  public int getId() {
    return this.id;
  }
  
  public int getType() {
    return this.type;
  }
}
