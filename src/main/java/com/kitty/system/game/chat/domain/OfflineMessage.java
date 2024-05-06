package com.kitty.system.game.chat.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfflineMessage {
  private int time;
  
  private String msg;

  public OfflineMessage() {}
  
  public OfflineMessage(int time, String msg) {
    this.time = time;
    this.msg = msg;
  }
}
