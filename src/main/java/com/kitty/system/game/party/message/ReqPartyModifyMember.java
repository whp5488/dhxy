package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 16570)
public class ReqPartyModifyMember extends Message {
  private Integer id;
  
  private String name;
  
  private byte job;
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setJob(byte job) {
    this.job = job;
  }
  
  public Integer getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public byte getJob() {
    return this.job;
  }
}
