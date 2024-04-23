package com.kitty.system.game.equip.domain;

import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.store.domain.StoreEquip;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.vo.FieldProp;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class EquipEasy {
  private String id;
  
  private Integer roleId;

  private Integer bd = 0;
  
  private Integer type;
  
  private Integer pos;
  
  private Integer position;

  private List<FieldProp> field = new ArrayList<>();

  private List<FieldProp> artifice = new ArrayList<>();

  private byte productOrder = 0;

  public EquipEasy(BagEquip equip, int roleId) {
    this.id = String.valueOf(equip.getId());
    this.roleId = Integer.valueOf(roleId);
    this.bd = Integer.valueOf(equip.getBd());
    this.type = Integer.valueOf(equip.getType());
    this.pos = Integer.valueOf(equip.getPos());
    this.position = Integer.valueOf(equip.getPosition());
    this.productOrder = equip.getProductOrder();
    for (FiedEquip fiedEquip : equip.getField())
      this.field.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue())); 
    for (FiedEquip fiedEquip : equip.getArtifice())
      this.artifice.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue())); 
  }
  
  public EquipEasy(WearEquip equip, int roleId) {
    this.id = String.valueOf(equip.getId());
    this.roleId = Integer.valueOf(roleId);
    this.bd = Integer.valueOf(equip.getBd());
    this.type = Integer.valueOf(equip.getType());
    this.pos = Integer.valueOf(equip.getPos());
    this.position = Integer.valueOf(equip.getPosition());
    this.productOrder = equip.getProductOrder();
    for (FiedEquip fiedEquip : equip.getField())
      this.field.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue())); 
    for (FiedEquip fiedEquip : equip.getArtifice())
      this.artifice.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue())); 
  }
  
  public EquipEasy(StoreEquip equip, int roleId) {
    this.id = String.valueOf(equip.getId());
    this.roleId = Integer.valueOf(roleId);
    this.bd = Integer.valueOf(equip.getBd());
    this.type = Integer.valueOf(equip.getType());
    this.pos = Integer.valueOf(equip.getPos());
    this.position = Integer.valueOf(equip.getPosition());
    this.productOrder = equip.getProductOrder();
    for (FiedEquip fiedEquip : equip.getField())
      this.field.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue())); 
    for (FiedEquip fiedEquip : equip.getArtifice())
      this.artifice.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue())); 
  }
  
  public EquipEasy() {}
}
