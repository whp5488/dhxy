package com.kitty.system.common.enter;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.io.Serializable;

@Setter
@Getter
public class FieldValue implements Serializable {
  private int k;
  
  private int v;

  public FieldValue() {}
  
  @ConstructorProperties({"k", "v"})
  public FieldValue(int k, int v) {
    this.k = k;
    this.v = v;
  }

}
