package com.kitty.system.game.gate.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 45140)
public class RespOtherLogin extends Message {

  private Integer result = 1;

  private Integer code = 9018;
  
  private String content;

}
