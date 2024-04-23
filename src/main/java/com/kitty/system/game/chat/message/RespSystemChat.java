package com.kitty.system.game.chat.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.annotation.StringField;
import com.kitty.mina.message.Message;
import com.kitty.system.common.enter.FiedValue;
import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = 16384)
public class RespSystemChat extends Message {
  private int type;
  
  public void setType(int type) {
    this.type = type;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setName(String name) {
    this.name = name;
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
  
  public int getType() {
    return this.type;
  }
  
  private int serial = 0;
  
  public int getSerial() {
    return this.serial;
  }
  
  private String name = "系统";
  
  @StringField(1)
  private String msg;
  
  public String getName() {
    return this.name;
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
