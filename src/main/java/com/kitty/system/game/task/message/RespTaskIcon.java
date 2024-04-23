package com.kitty.system.game.task.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 16007)
public class RespTaskIcon extends Message {
  
  private int serial = 0;
  
  private String para;

  private byte is = 1;

  public RespTaskIcon(int serial, String para, byte is) {
    this.serial = serial;
    this.para = para;
    this.is = is;
  }
  
  public RespTaskIcon() {}
}
