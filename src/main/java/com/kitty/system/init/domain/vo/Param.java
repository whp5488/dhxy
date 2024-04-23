package com.kitty.system.init.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class Param implements Serializable {
  
  private Integer k = Integer.valueOf(0);

  private Integer v = Integer.valueOf(0);

  private Integer r = Integer.valueOf(-1);

  private Integer t = Integer.valueOf(0);

  private String d = null;

  private int bd = 0;

  public Param(Integer k, Integer v) {
    this.k = k;
    this.v = v;
  }
  
  public Param(Integer k, Integer v, Integer t) {
    this.k = k;
    this.v = v;
    this.t = t;
  }
  
  public Param(Integer k, Integer v, Integer t, Integer r) {
    this.k = k;
    this.v = v;
    this.t = t;
    this.r = r;
  }
  
  public Param(Integer k, Integer v, Integer r, Integer t, String d) {
    this.k = k;
    this.v = v;
    this.r = r;
    this.t = t;
    this.d = d;
  }
  
  public Param() {}
}
