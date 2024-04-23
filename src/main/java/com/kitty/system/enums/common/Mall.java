package com.kitty.system.enums.common;

import com.google.common.collect.Lists;
import java.util.List;

public enum Mall {
  RECOMMEND("多宝推荐", 1),
  TREND_NEW("新鲜玩意", 2),
  STRENGTHEN_MATERIAL("强化材料", 3),
  WEEKLY_QUOTA("每周限购", 4),
  NPC_SHOP_GROCERY("杂货店", 5),
  NPC_SHOP_GUO("郭氏", 6),
  NPC_SHOP_BRAC("降魔", 7),
  NPC_SHOP_NAO("大闹天宫", 8),
  NPC_SHOP_DI("地宫", 9),
  NPC_SHOP_ARENA("竞技场", 10),
  NPC_SHOP_LONG("龙战", 11),
  NPC_SHOP_NICE("好心值", 12),
  NPC_SHOP_FEATS("功绩", 13),
  BOUND_SHOP("绑定商店", 14);
  
  Mall(String note, int value) {
    this.note = note;
    this.value = value;
  }
  
  private String note;
  
  private int value;
  
  public String getNote() {
    return this.note;
  }
  
  public int getValue() {
    return this.value;
  }
  
  public static List<Integer> toList() {
    List<Integer> list = Lists.newArrayList();
    for (Mall n : values())
      list.add(Integer.valueOf(n.getValue())); 
    return list;
  }
}
