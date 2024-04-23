package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;

@MessageMeta(module = 45246)
public class RespAtClashResult extends Message {
  private byte result;
  
  private List<FiedValue> list;
  
  public void setResult(byte result) {
    this.result = result;
  }
  
  public void setList(List<FiedValue> list) {
    this.list = list;
  }
  
  public void setMateSecond(int mateSecond) {
    this.mateSecond = mateSecond;
  }
  
  public byte getResult() {
    return this.result;
  }
  
  public List<FiedValue> getList() {
    return this.list;
  }
  
  private int mateSecond = 0;
  
  public int getMateSecond() {
    return this.mateSecond;
  }
}
