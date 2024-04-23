package com.kitty.system.common.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import org.nutz.json.ToJson;

@ToJson
@MessageMeta(module = 65000)
public class RespRoundDataEnd extends Message {
  private String msg;
  
  private int type = 0;
  
  private int fightId;
  
  private int round;
  
  public RespRoundDataEnd() {}
  
  public RespRoundDataEnd(int fightId, int round, String msg) {
    this.fightId = fightId;
    this.round = round;
    this.msg = msg;
  }
  
  public RespRoundDataEnd(int fightId, int round, int type, String msg) {
    this.fightId = fightId;
    this.round = round;
    this.type = type;
    this.msg = msg;
  }
  
  public String getMsg() {
    return this.msg;
  }
  
  public void setMsg(String msg) {
    this.msg = msg;
  }
  
  public int getType() {
    return this.type;
  }
  
  public void setType(int type) {
    this.type = type;
  }
  
  public int getFightId() {
    return this.fightId;
  }
  
  public void setFightId(int fightId) {
    this.fightId = fightId;
  }
  
  public int getRound() {
    return this.round;
  }
  
  public void setRound(int round) {
    this.round = round;
  }
}
