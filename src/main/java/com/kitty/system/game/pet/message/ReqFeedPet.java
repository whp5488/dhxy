package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 8270)
public class ReqFeedPet extends Message {
  private int petId;
  
  private int param;
  
  private String type;
  
  private int num;

}
