package com.kitty.system.game.task.message.vo;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
public class BallotInfo {
  private int serial;
  
  private String name;
  
  private short type;
  
  public BallotInfo() {}
  
  @ConstructorProperties({"serial", "name", "type"})
  public BallotInfo(int serial, String name, short type) {
    this.serial = serial;
    this.name = name;
    this.type = type;
  }

}
