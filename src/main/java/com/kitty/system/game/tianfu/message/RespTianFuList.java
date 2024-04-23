package com.kitty.system.game.tianfu.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.tianfu.pojo.RoleTianFuClient;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 44000)
public class RespTianFuList extends Message {

  @ConstructorProperties({"type", "tianFuList"})
  public RespTianFuList(byte type, List<RoleTianFuClient> tianFuList) {
    this.type = type;
    this.tianFuList = tianFuList;
  }
  
  private byte type = 0;

  private List<RoleTianFuClient> tianFuList = new ArrayList<>();

  public RespTianFuList() {}
}
