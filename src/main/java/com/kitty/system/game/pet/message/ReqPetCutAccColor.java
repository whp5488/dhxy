package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 14343)
public class ReqPetCutAccColor extends Message {
  private Integer id;
  
  private byte pos;
  
  private byte serial;

}
