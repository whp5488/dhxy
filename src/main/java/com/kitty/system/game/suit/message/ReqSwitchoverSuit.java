package com.kitty.system.game.suit.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 4756)
public class ReqSwitchoverSuit extends Message {
  private String id;
}
