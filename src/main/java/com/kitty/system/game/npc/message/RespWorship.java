package com.kitty.system.game.npc.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.beans.ConstructorProperties;

@MessageMeta(module = 6889)
public class RespWorship extends Message {
  private int npcId;
  
  private String name;
  
  private String motto;
  
  public void setNpcId(int npcId) {
    this.npcId = npcId;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setMotto(String motto) {
    this.motto = motto;
  }

  @ConstructorProperties({"npcId", "name", "motto"})
  public RespWorship(int npcId, String name, String motto) {
    this.npcId = npcId;
    this.name = name;
    this.motto = motto;
  }
  
  public RespWorship() {}
  
  public int getNpcId() {
    return this.npcId;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getMotto() {
    return this.motto;
  }
}
