package com.kitty.system.game.safe.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 32832)
public class ReqUnlock extends Message {
  private String password;
}
