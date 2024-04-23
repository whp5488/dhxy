package com.kitty.system.game.tianfu.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 44004)
public class RespTianFuDelete extends Message {

  List<String> tianFuIds = new ArrayList<>();
  
  @ConstructorProperties({"tianFuIds"})
  public RespTianFuDelete(List<String> tianFuIds) {
    this.tianFuIds = tianFuIds;
  }

  public RespTianFuDelete() {}
}
