package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 14341)
public class ReqRestAttributeHero extends Message {
  private Integer id;
  
  private byte type;
}
