package com.kitty.system.init.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.io.Serializable;

@Setter
@Getter
public class Question implements Serializable {
  private int serial;
  
  private String answer;

  @ConstructorProperties({"serial", "answer", "type"})
  public Question(int serial, String answer, byte type) {
    this.serial = serial;
    this.answer = answer;
    this.type = type;
  }

  private byte type = -1;

  public Question(int serial, String answer) {
    this.serial = serial;
    this.answer = answer;
  }
  
  public Question() {}
}
