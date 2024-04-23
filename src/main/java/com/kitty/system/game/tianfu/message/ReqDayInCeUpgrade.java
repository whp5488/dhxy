package com.kitty.system.game.tianfu.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 4403)
public class ReqDayInCeUpgrade extends Message {
  private Integer addLevel;

}
