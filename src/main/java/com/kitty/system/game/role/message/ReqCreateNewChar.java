package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 8284)
public class ReqCreateNewChar extends Message {
  private String name;
  
  private Integer sex;
  
  private Integer race;
  
  private Integer icon;
  
  private Integer serial;
  
  private String marking;

}
