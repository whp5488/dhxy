package com.kitty.system.game.bag.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 40964)
public class RespIconCartoon extends Message {
  
  private int type = 1;
  
  private int serial;

  private String onlyId = "";

}
