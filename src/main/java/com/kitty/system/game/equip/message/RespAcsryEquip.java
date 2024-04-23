package com.kitty.system.game.equip.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.vo.FieldProp;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 41241)
public class RespAcsryEquip extends Message {

  private int serial = 0;

  private List<FieldProp> list = new ArrayList<>();

  private byte productOrder = 0;

  public RespAcsryEquip(BagEquip equip) {
    for (FiedEquip fiedEquip : equip.getField())
      this.list.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue())); 
    this.serial = equip.getType();
    this.productOrder = equip.getProductOrder();
  }
  
  public RespAcsryEquip() {}
}
