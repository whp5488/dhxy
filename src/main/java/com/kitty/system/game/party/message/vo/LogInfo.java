package com.kitty.system.game.party.message.vo;

import com.kitty.mina.annotation.StringField;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.io.Serializable;

@Setter
@Getter
public class LogInfo implements Serializable {
  @StringField(1)
  private String content;
  
  public LogInfo() {}
  
  @ConstructorProperties({"content"})
  public LogInfo(String content) {
    this.content = content;
  }

}
