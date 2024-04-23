package com.kitty.system.game.general.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 63752)
public class ReqGeneralNotify extends Message {
  private int type;
  
  private String status;
  
  private String string;

}
