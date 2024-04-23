package com.kitty.system.game.equip.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.common.enter.FiedValue;
import lombok.Getter;

import java.util.List;

@Getter
@MessageMeta(module = 41239)
public class RespUpgradePreview extends Message {
  public void setType(int type) {
    this.type = type;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setLevel(int level) {
    this.level = level;
  }
  
  public void setListOne(List<FiedValue> listOne) {
    this.listOne = listOne;
  }
  
  public void setListTwo(List<FiedValue> listTwo) {
    this.listTwo = listTwo;
  }
  
  private int type = 0;
  
  private int serial;
  
  private String name;
  
  private int level;
  
  private List<FiedValue> listOne;
  
  private List<FiedValue> listTwo;

}
