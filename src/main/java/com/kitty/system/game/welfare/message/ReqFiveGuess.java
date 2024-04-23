package com.kitty.system.game.welfare.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MessageMeta(module = 40994)
public class ReqFiveGuess extends Message {
  private int amount = 0;
  
  private short choice;

}
