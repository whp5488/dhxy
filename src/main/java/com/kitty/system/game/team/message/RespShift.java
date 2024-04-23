package com.kitty.system.game.team.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 4249)
public class RespShift extends Message {
  private int id;
  
  private int x;
  
  private int y;
  
  private int mapid;

}
