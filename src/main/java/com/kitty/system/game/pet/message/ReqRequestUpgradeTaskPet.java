package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 41153)
public class ReqRequestUpgradeTaskPet extends Message {
  private int petId;
  private int param;
}
