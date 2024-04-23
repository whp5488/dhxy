package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 4275)
public class RespReplyEcho extends Message {

  private String peerTime = String.valueOf(System.currentTimeMillis());

}
