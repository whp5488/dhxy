package com.kitty.system.game.fightnew.messages.old;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RespFightShouting {
  private int id;

  private short channel = 14;
  
  private String msg;

  private int show_time = 2000;
  
  private int msgDelayTime;

}
