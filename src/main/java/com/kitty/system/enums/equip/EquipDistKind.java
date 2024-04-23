package com.kitty.system.enums.equip;

import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.system.game.vo.FiedEquip;

public enum EquipDistKind {
  MAGICAL_ONE(39001, ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue(), 1),
  MAGICAL_TWO(39002, ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue(), 2),
  MAGICAL_THREE(39003, ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue(), 3),
  MAGICAL_FOUR(39004, ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue(), 4),
  MAGICAL_FIVE(39005, ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue(), 5),
  MAGICAL_SIX(39006, ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue(), 6),
  SIN_ONE(39011, ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue(), 1),
  SIN_TWO(39012, ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue(), 2),
  SIN_THREE(39013, ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue(), 3),
  SIN_FOUR(39014, ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue(), 4),
  SIN_FIVE(39015, ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue(), 5),
  SUIT(39030, ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY_EGG.getValue(), 0);
  
  EquipDistKind(int note, int type, int value) {
    this.note = note;
    this.type = type;
    this.value = value;
  }
  
  private int note;
  
  private int type;
  
  private int value;
  
  public int getNote() {
    return this.note;
  }
  
  public int getType() {
    return this.type;
  }
  
  public int getValue() {
    return this.value;
  }
  
  public static FiedEquip getInfo(Integer note) {
    for (EquipDistKind concise : values()) {
      if (concise.getNote() == note.intValue())
        return new FiedEquip(Integer.valueOf(concise.getType()), Integer.valueOf(concise.getValue())); 
    } 
    return null;
  }
}
