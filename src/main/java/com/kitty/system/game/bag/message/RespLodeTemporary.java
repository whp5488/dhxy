package com.kitty.system.game.bag.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.bag.message.vo.TemporaryVo;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 45797)
public class RespLodeTemporary extends Message {
  private List<TemporaryVo> list;
  
  @ConstructorProperties({"list"})
  public RespLodeTemporary(List<TemporaryVo> list) {
    this.list = list;
  }

}
