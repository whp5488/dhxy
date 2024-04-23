package com.kitty.system.game.equip.helper.vo;

import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.vo.FieldProp;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PetEquipNeo implements Serializable {
  private String id;
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setList(List<FieldProp> list) {
    this.list = list;
  }
  
  public void setProductOrder(byte productOrder) {
    this.productOrder = productOrder;
  }
  
  public String getId() {
    return this.id;
  }
  
  private int serial = 0;
  
  public int getSerial() {
    return this.serial;
  }
  
  private List<FieldProp> list = new ArrayList<>();
  
  public List<FieldProp> getList() {
    return this.list;
  }
  
  private byte productOrder = 0;
  
  public byte getProductOrder() {
    return this.productOrder;
  }
  
  public PetEquipNeo(long id, int serial, byte productOrder, List<FiedEquip> list) {
    this.id = String.valueOf(id);
    this.serial = serial;
    this.productOrder = productOrder;
    for (FiedEquip fiedEquip : list)
      this.list.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue())); 
  }
  
  public PetEquipNeo() {}
}
