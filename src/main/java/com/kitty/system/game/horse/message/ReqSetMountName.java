package com.kitty.system.game.horse.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 8273)
public class ReqSetMountName extends Message {
  private String id;
  private String name;
}
