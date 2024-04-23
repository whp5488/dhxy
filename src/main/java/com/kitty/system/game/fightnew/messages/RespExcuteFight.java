package com.kitty.system.game.fightnew.messages;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.fightnew.messages.vo.ExcuteFightParam;
import java.util.List;

@MessageMeta(module = 3585)
public class RespExcuteFight extends Message {
  private int round;
  
  private String endTime;
  
  private List<ExcuteFightParam> excuteFightParams;
  
  public void setRound(int round) {
    this.round = round;
  }
  
  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }
  
  public void setExcuteFightParams(List<ExcuteFightParam> excuteFightParams) {
    this.excuteFightParams = excuteFightParams;
  }
  
  public int getRound() {
    return this.round;
  }
  
  public String getEndTime() {
    return this.endTime;
  }
  
  public List<ExcuteFightParam> getExcuteFightParams() {
    return this.excuteFightParams;
  }
}
