package com.kitty.system.game.bag.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 8237)
public class ReqDrugUse extends Message {
  private int id;
  private int serial;
}
