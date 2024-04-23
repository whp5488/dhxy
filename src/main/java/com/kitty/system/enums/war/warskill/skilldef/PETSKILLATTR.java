package com.kitty.system.enums.war.warskill.skilldef;

import com.google.common.collect.Lists;
import java.util.List;

public enum PETSKILLATTR {
  PETSKILLATTR_WULI("物理攻击", 0, 0),
  PETSKILLATTR_FIRE("火系", 5, 1),
  PETSKILLATTR_WIND("风系", 6, 1),
  PETSKILLATTR_THUNDER("雷系", 7, 1),
  PETSKILLATTR_WATER("水系", 8, 1),
  PETSKILLATTR_AIHAO("哀嚎-鬼火系", 15, 1),
  PETSKILLATTR_SPEED("速系", 11, 0),
  PETSKILLATTR_SUBTRACT_SPEED("减速系", 60, 0),
  PETSKILLATTR__ZHIYU("治愈系", 18, 0),
  PETSKILLATTR_RELIEVE_ABNORMAL("解除异常", 51, 0),
  PETSKILLATTR_RELIEVE_POSITIVE("清除正面状态", 52, 0),
  PETSKILLATTR_INCREASE_WX("提升五行", 53, 0),
  PETSKILLATTR_RELIEVE_WX("清除五行提升", 54, 0),
  PETSKILLATTR_GAIN("增益", 55, 0),
  PETSKILLATTR_DEBUFFS("减益", 56, 0),
  PETSKILLATTR_PROTECT("保护", 57, 0),
  PETSKILLATTR_CLOAKING("隐身", 58, 0),
  PETSKILLATTR_COMMON_MAGIC("普通法术攻击", 59, 1),
  PETSKILLATTR_TM("天魔", 61, 1),
  PETSKILLATTR_FG("分光", 62, 1),
  PETSKILLATTR_QM("青面", 63, 1),
  PETSKILLATTR_XL("小楼", 64, 1);
  
  PETSKILLATTR(String note, int value, int type) {
    this.note = note;
    this.value = value;
    this.type = type;
  }
  
  private String note;
  
  private int value;
  
  private int type;
  
  public String getNote() {
    return this.note;
  }
  
  public int getValue() {
    return this.value;
  }
  
  public int getType() {
    return this.type;
  }
  
  public static PETSKILLATTR getByValue(int value) {
    for (PETSKILLATTR x : values()) {
      if (x.getValue() == value)
        return x; 
    } 
    return null;
  }
  
  public static List<Integer> evade() {
    List<Integer> list = Lists.newArrayList();
    for (PETSKILLATTR e : values()) {
      if (e.getType() == 0)
        list.add(Integer.valueOf(e.getValue())); 
    } 
    return list;
  }
}
