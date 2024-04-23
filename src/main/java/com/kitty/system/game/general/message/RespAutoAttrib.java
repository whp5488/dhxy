package com.kitty.system.game.general.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8853)
public class RespAutoAttrib extends Message {
  private int id;
  
  public void setId(int id) {
    this.id = id;
  }
  
  public void setGg(int gg) {
    this.gg = gg;
  }
  
  public void setLx(int lx) {
    this.lx = lx;
  }
  
  public void setLl(int ll) {
    this.ll = ll;
  }
  
  public void setMj(int mj) {
    this.mj = mj;
  }
  
  public void setType(int type) {
    this.type = type;
  }
  
  public int getId() {
    return this.id;
  }
  
  private int gg = 0;
  
  public int getGg() {
    return this.gg;
  }
  
  private int lx = 0;
  
  public int getLx() {
    return this.lx;
  }
  
  private int ll = 0;
  
  public int getLl() {
    return this.ll;
  }
  
  private int mj = 0;
  
  public int getMj() {
    return this.mj;
  }
  
  private int type = 0;
  
  public int getType() {
    return this.type;
  }
}
