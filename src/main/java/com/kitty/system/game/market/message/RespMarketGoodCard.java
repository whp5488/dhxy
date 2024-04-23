package com.kitty.system.game.market.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.vo.FieldProp;
import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = 45104)
public class RespMarketGoodCard extends Message {
  private String id;
  
  private int roleId;
  
  private int serial;
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setField(List<FieldProp> field) {
    this.field = field;
  }
  
  public void setArtifice(List<FieldProp> artifice) {
    this.artifice = artifice;
  }
  
  public void setPara(List<FiedValue> para) {
    this.para = para;
  }
  
  public void setStatus(byte status) {
    this.status = status;
  }
  
  public void setEndTime(int endTime) {
    this.endTime = endTime;
  }
  
  public String getId() {
    return this.id;
  }
  
  public int getRoleId() {
    return this.roleId;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  private List<FieldProp> field = new ArrayList<>();
  
  public List<FieldProp> getField() {
    return this.field;
  }
  
  private List<FieldProp> artifice = new ArrayList<>();
  
  public List<FieldProp> getArtifice() {
    return this.artifice;
  }
  
  private List<FiedValue> para = new ArrayList<>();
  
  public List<FiedValue> getPara() {
    return this.para;
  }
  
  private byte status = 2;
  
  private int endTime;
  
  public byte getStatus() {
    return this.status;
  }
  
  public int getEndTime() {
    return this.endTime;
  }
  
  public RespMarketGoodCard(String id, int roleId, int serial, List<FiedEquip> field, List<FiedEquip> artifice) {
    this.id = id;
    this.roleId = roleId;
    this.serial = serial;
    for (FiedEquip fiedEquip : field)
      this.field.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue())); 
    for (FiedEquip fiedEquip : artifice)
      this.artifice.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue())); 
  }
  
  public RespMarketGoodCard(String id, int roleId, int serial, List<FiedEquip> field, List<FiedEquip> artifice, List<FiedValue> para, byte status, int endTime) {
    this.id = id;
    this.roleId = roleId;
    this.serial = serial;
    this.para = para;
    this.status = status;
    this.endTime = endTime;
    for (FiedEquip fiedEquip : field)
      this.field.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue())); 
    for (FiedEquip fiedEquip : artifice)
      this.artifice.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue())); 
  }
  
  public RespMarketGoodCard() {}
}
