package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 14339)
public class ReqRestAssignCorrection extends Message {
  private Integer petId;
  
  private byte type;

}
