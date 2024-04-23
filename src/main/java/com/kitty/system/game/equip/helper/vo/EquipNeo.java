package com.kitty.system.game.equip.helper.vo;

import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.vo.FieldProp;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EquipNeo implements Serializable {
  List<FieldProp> list = new ArrayList<>();
  
  public void setList(List<FieldProp> list) {
    this.list = list;
  }
  
  public List<FieldProp> getList() {
    return this.list;
  }
  
  public EquipNeo(List<FiedEquip> list) {
    for (FiedEquip fiedEquip : list) {
      if (!fiedEquip.getType().equals(Integer.valueOf(6681)))
        this.list.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue())); 
    } 
  }
  
  public EquipNeo() {}
}
