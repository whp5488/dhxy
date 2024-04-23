package com.kitty.system.game.horse.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.horse.message.vo.MountBase;
import java.beans.ConstructorProperties;
import java.util.List;

@MessageMeta(module = 45079)
public class RespPreUpgradeHorse extends Message {
  private String id;
  
  private List<MountBase> list;
  
  private byte ask;
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setList(List<MountBase> list) {
    this.list = list;
  }
  
  public void setAsk(byte ask) {
    this.ask = ask;
  }
  
  public RespPreUpgradeHorse() {}
  
  @ConstructorProperties({"id", "list", "ask"})
  public RespPreUpgradeHorse(String id, List<MountBase> list, byte ask) {
    this.id = id;
    this.list = list;
    this.ask = ask;
  }
  
  public String getId() {
    return this.id;
  }
  
  public List<MountBase> getList() {
    return this.list;
  }
  
  public byte getAsk() {
    return this.ask;
  }
}
