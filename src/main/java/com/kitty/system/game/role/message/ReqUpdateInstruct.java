package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 45557)
public class ReqUpdateInstruct extends Message {
  private byte type;
  
  private short serial;
  
  private byte index;
  
  private String string;

}
