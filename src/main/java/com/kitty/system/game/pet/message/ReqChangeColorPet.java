package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 14342)
public class ReqChangeColorPet extends Message {
  private int petId;
  private byte colorSerial;

}
