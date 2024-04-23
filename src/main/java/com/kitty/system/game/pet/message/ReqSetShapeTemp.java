package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 32772)
public class ReqSetShapeTemp extends Message {
  private int id;
  
  private int type;
  
  private int index;
}
