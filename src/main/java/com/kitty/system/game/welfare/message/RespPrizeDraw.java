package com.kitty.system.game.welfare.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 41482)
public class RespPrizeDraw extends Message {
  private short count = 0;

}
