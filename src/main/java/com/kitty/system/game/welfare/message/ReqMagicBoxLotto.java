package com.kitty.system.game.welfare.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MessageMeta(module = 45389)
public class ReqMagicBoxLotto extends Message {
  private int affiliation;
  
  private byte type;

}
