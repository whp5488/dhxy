package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.activity.message.vo.GeneralClash;
import java.util.List;

@MessageMeta(module = 45245)
public class RespClashPlans extends Message {
  private String countDown;
  
  private List<GeneralClash> clashListA;
  
  private List<GeneralClash> clashListB;
  
  private int type;
  
  public void setCountDown(String countDown) {
    this.countDown = countDown;
  }
  
  public void setClashListA(List<GeneralClash> clashListA) {
    this.clashListA = clashListA;
  }
  
  public void setClashListB(List<GeneralClash> clashListB) {
    this.clashListB = clashListB;
  }
  
  public void setType(int type) {
    this.type = type;
  }
  
  public String getCountDown() {
    return this.countDown;
  }
  
  public List<GeneralClash> getClashListA() {
    return this.clashListA;
  }
  
  public List<GeneralClash> getClashListB() {
    return this.clashListB;
  }
  
  public int getType() {
    return this.type;
  }
}
