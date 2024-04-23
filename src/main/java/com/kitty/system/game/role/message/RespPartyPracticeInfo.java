package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
@MessageMeta(module = 28842)
public class RespPartyPracticeInfo extends Message {
  private short level;
  
  private short points;

  public RespPartyPracticeInfo() {
    this.level = 0;
    this.points = 0;
  }
  
  @ConstructorProperties({"level", "points"})
  public RespPartyPracticeInfo(short level, short points) {
    this.level = 0;
    this.points = 0;
    this.level = level;
    this.points = points;
  }

}
