package com.kitty.system.game.onlinemall.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.vo.FieldProp;
import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = 8413)
public class RespTreasureStoneCompose extends Message {
  private int serial;
  
  private String id;
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setList(List<FieldProp> list) {
    this.list = list;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public String getId() {
    return this.id;
  }
  
  private List<FieldProp> list = new ArrayList<>();
  
  public List<FieldProp> getList() {
    return this.list;
  }
}
