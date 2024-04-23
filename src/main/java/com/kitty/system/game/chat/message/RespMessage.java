package com.kitty.system.game.chat.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.common.enter.FiedValue;
import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = 12287)
public class RespMessage extends Message {
  private int id;
  
  private int channel;
  
  private String msg;
  
  public void setId(int id) {
    this.id = id;
  }
  
  public void setChannel(int channel) {
    this.channel = channel;
  }
  
  public void setMsg(String msg) {
    this.msg = msg;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  public void setList(List<FiedValue> list) {
    this.list = list;
  }
  
  public int getId() {
    return this.id;
  }
  
  public int getChannel() {
    return this.channel;
  }
  
  public String getMsg() {
    return this.msg;
  }
  
  private int time = (new Long(System.currentTimeMillis() / 1000L)).intValue();
  
  public int getTime() {
    return this.time;
  }
  
  private List<FiedValue> list = new ArrayList<>();
  
  public List<FiedValue> getList() {
    return this.list;
  }
}
