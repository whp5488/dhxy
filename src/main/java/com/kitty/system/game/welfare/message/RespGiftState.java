package com.kitty.system.game.welfare.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 40971)
public class RespGiftState extends Message {

  private short type = 1;

  private byte state = 1;

  private byte is = 1;

}
