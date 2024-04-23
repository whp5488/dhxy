package com.kitty.system.game.task.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class RecordQuestion implements Serializable {

  private int s = 0;

  private List<Byte> r = new ArrayList<>();

  private int q = 0;

  private int e = 0;

  private byte t = 0;

  private int i = 0;

}
