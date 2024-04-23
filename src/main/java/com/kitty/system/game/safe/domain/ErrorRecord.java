package com.kitty.system.game.safe.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorRecord {
  int errorSum;
  
  long lastErrorTime;

  public void addErrorCount() {
    this.errorSum++;
  }
}
