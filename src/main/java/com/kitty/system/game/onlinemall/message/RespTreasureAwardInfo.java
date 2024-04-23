package com.kitty.system.game.onlinemall.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.vo.FieldProp;
import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = 8412)
public class RespTreasureAwardInfo extends Message {
  public void setNormal(List<FieldProp> normal) {
    this.normal = normal;
  }
  
  public void setSenior(List<FieldProp> senior) {
    this.senior = senior;
  }
  
  public void setSupreme(List<FieldProp> supreme) {
    this.supreme = supreme;
  }
  
  private List<FieldProp> normal = new ArrayList<>();
  
  public List<FieldProp> getNormal() {
    return this.normal;
  }
  
  private List<FieldProp> senior = new ArrayList<>();
  
  public List<FieldProp> getSenior() {
    return this.senior;
  }
  
  private List<FieldProp> supreme = new ArrayList<>();
  
  public List<FieldProp> getSupreme() {
    return this.supreme;
  }
}
