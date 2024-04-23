package com.kitty.system.game.partner.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.partner.enter.PartnerFiedValue;
import java.beans.ConstructorProperties;
import java.util.List;

@MessageMeta(module = 42100)
public class RespPartnerList extends Message {
  private List<PartnerFiedValue> list;
  
  public void setList(List<PartnerFiedValue> list) {
    this.list = list;
  }
  
  public RespPartnerList() {}
  
  @ConstructorProperties({"list"})
  public RespPartnerList(List<PartnerFiedValue> list) {
    this.list = list;
  }
  
  public List<PartnerFiedValue> getList() {
    return this.list;
  }
}
