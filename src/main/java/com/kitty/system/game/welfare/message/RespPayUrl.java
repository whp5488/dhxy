package com.kitty.system.game.welfare.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 32853)
public class RespPayUrl extends Message {
  private String front;
  
  private String back;
  
  public RespPayUrl() {}
  
  public RespPayUrl(String front, String back) {
    this.front = front;
    this.back = back;
  }

}
