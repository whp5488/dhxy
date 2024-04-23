package com.kitty.system.init.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.io.Serializable;

@Setter
@Getter
public class Field implements Serializable {
  private Integer key;
  
  private Double value;
  
  public Field() {}
  
  @ConstructorProperties({"key", "value"})
  public Field(Integer key, Double value) {
    this.key = key;
    this.value = value;
  }

}
