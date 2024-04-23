package com.kitty.system.game.fightnew.messages;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.fightnew.messages.old.RespFightCommandInfo;
import java.beans.ConstructorProperties;
import java.util.List;

@MessageMeta(module = 3587)
public class RespReturnSuccess extends Message {
  private int objId;
  
  private byte type;
  
  List<RespFightCommandInfo> fightCommandInfos;
  
  public void setObjId(int objId) {
    this.objId = objId;
  }
  
  public void setType(byte type) {
    this.type = type;
  }
  
  public void setFightCommandInfos(List<RespFightCommandInfo> fightCommandInfos) {
    this.fightCommandInfos = fightCommandInfos;
  }
  
  @ConstructorProperties({"objId", "type", "fightCommandInfos"})
  public RespReturnSuccess(int objId, byte type, List<RespFightCommandInfo> fightCommandInfos) {
    this.objId = objId;
    this.type = type;
    this.fightCommandInfos = fightCommandInfos;
  }
  
  public RespReturnSuccess() {}
  
  public int getObjId() {
    return this.objId;
  }
  
  public byte getType() {
    return this.type;
  }
  
  public List<RespFightCommandInfo> getFightCommandInfos() {
    return this.fightCommandInfos;
  }
}
