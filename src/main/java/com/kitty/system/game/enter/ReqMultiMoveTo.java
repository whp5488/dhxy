package com.kitty.system.game.enter;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 61634)
public class ReqMultiMoveTo extends Message {
  private int roleId;
  
  private int mapId;
  
  private short x;
  
  private short y;
  
  private int send_time;
}
