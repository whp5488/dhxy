package com.kitty.system.game.task.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
@MessageMeta(module = 53725)
public class RespQuestionHall extends Message {
  private int time;
  
  private int serial;
  
  private int num;
  
  private int mistakes;
  
  public RespQuestionHall() {}
  
  @ConstructorProperties({"time", "serial", "num", "mistakes"})
  public RespQuestionHall(int time, int serial, int num, int mistakes) {
    this.time = time;
    this.serial = serial;
    this.num = num;
    this.mistakes = mistakes;
  }

}
