package com.kitty.system.game.equip.domain;

import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.vo.FieldProp;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class PetEquipEasy {
  private String id;
  
  private String petId;

  private Integer bd = 0;
  
  private Integer type;

  private List<FieldProp> field = new ArrayList<>();
  
  private short positionNum;

  private byte productOrder = 0;

  private byte lock = 0;

  public PetEquipEasy(PetEquip equip) {
    this.id = String.valueOf(equip.getId());
    this.petId = String.valueOf(equip.getPId());
    this.bd = Integer.valueOf(equip.getBd());
    this.type = Integer.valueOf(equip.getSerial());
    this.productOrder = equip.getProductOrder();
    this.positionNum = equip.getPositionNum();
    this.lock = equip.getLock();
    for (FiedEquip fiedEquip : equip.getField())
      this.field.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue())); 
  }
  
  public PetEquipEasy() {}
}
