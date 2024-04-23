package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 32782)
public class ReqQueryPartys extends Message {
  private String type;
  
  private String para;

  private byte page = 0;

  private byte size = 8;

  private byte sort = 4;

  private byte way = 0;

}
