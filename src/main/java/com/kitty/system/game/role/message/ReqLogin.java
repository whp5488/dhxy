package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 12290)
public class ReqLogin extends Message {
  private String accountId;
  
  private int auth_key;
  
  private String marking;

}
