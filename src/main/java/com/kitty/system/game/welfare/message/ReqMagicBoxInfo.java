package com.kitty.system.game.welfare.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MessageMeta(module = 45386)
public class ReqMagicBoxInfo extends Message {
  private int affiliation;

}
