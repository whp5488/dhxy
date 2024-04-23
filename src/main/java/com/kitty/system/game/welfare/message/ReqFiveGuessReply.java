package com.kitty.system.game.welfare.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MessageMeta(module = 40996)
public class ReqFiveGuessReply extends Message {
  private String token;
}
