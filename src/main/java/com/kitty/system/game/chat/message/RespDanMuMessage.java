package com.kitty.system.game.chat.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.common.enter.FiedValue;
import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = 12288)
public class RespDanMuMessage extends Message {
  private int id;
  
  private String name;
  
  public void setId(int id) {
    this.id = id;
  }
  
  public void setName(String name) {
    this.name = name;
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
  
  public String getName() {
    return this.name;
  }
  
  private int channel = 1;
  
  private String msg;
  
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
