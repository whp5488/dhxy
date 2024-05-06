package com.kitty.system.game.gate.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 24113)
public class RespLoginPreview extends Message {
  private String sid;

  private int time = 0;
  
  private String cookie;

}
