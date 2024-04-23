package com.kitty.system.game.fightnew.messages;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.beans.ConstructorProperties;

@MessageMeta(module = 3472)
public class ReqReturnRoundEnd extends Message {
  private String recordId;
  
  private int roleId;
  
  private int round;
  
  public void setRecordId(String recordId) {
    this.recordId = recordId;
  }
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setRound(int round) {
    this.round = round;
  }
  
  @ConstructorProperties({"recordId", "roleId", "round"})
  public ReqReturnRoundEnd(String recordId, int roleId, int round) {
    this.recordId = recordId;
    this.roleId = roleId;
    this.round = round;
  }
  
  public ReqReturnRoundEnd() {}
  
  public String getRecordId() {
    return this.recordId;
  }
  
  public int getRoleId() {
    return this.roleId;
  }
  
  public int getRound() {
    return this.round;
  }
}
