package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.activity.model.user.hg.HgPrepare;
import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = 49169)
public class RespHgPrepare extends Message {
  private int time;
  
  public void setTime(int time) {
    this.time = time;
  }
  
  public void setInitiator(List<HgPrepare> initiator) {
    this.initiator = initiator;
  }
  
  public void setReceiver(List<HgPrepare> receiver) {
    this.receiver = receiver;
  }
  
  public int getTime() {
    return this.time;
  }
  
  private List<HgPrepare> initiator = new ArrayList<>(5);
  
  public List<HgPrepare> getInitiator() {
    return this.initiator;
  }
  
  private List<HgPrepare> receiver = new ArrayList<>(5);
  
  public List<HgPrepare> getReceiver() {
    return this.receiver;
  }
}
