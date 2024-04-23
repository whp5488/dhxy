package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 64700)
public class ReqDey extends Message {
  public int colorOne;
  
  public int colorTwo;
}
