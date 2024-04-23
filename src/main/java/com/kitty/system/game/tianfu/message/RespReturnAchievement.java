package com.kitty.system.game.tianfu.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.common.enter.FiedValue;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 44003)
public class RespReturnAchievement extends Message {

  @ConstructorProperties({"list"})
  public RespReturnAchievement(List<FiedValue> list) {
    this.list = list;
  }
  
  private List<FiedValue> list = new ArrayList<>();

  public RespReturnAchievement() {}
}
