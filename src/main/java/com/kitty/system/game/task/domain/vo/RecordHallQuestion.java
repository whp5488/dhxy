package com.kitty.system.game.task.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class RecordHallQuestion implements Serializable {
  private int s;
  
  private int q;
  
  private int t;

  private byte r = -1;

}
