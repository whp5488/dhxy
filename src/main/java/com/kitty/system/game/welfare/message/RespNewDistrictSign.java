package com.kitty.system.game.welfare.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
@MessageMeta(module = 41240)
public class RespNewDistrictSign extends Message {
  private String json;
  
  @ConstructorProperties({"json", "day"})
  public RespNewDistrictSign(String json, int day) {
    this.json = json;
    this.day = day;
  }

  private int day = 1;

  public RespNewDistrictSign() {}
}
