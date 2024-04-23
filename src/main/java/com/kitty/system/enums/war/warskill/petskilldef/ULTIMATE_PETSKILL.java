package com.kitty.system.enums.war.warskill.petskilldef;

import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ULTIMATE_PETSKILL {
  PETSKILL_JUEJINGFENGSHENG("绝境逢生", 82001, 1),
  PETSKILL_ZIXUWUYOU("子虚乌有", 82002, 1),
  PETSKILL_HUAWU("化无", 82003, 0),
  PETSKILL_JIANGSI("将死", 82004, 0),
  PETSKILL_DANGTOUBANGHE("当头棒喝", 82005, 0),
  PETSKILL_MINGCHAQIUHAO("明察秋毫", 82006, 0),
  PETSKILL_SHUANGGUANQIXIA("双管齐下", 82007, 0),
  PETSKILL_ZUONIAOSHOUSAN("作鸟兽散", 82008, 0),
  PETSKILL_CHUNNUANHUAKAI("春暖花开", 82009, 1),
  PETSKILL_YIYAHUANYA("以牙还牙", 82010, 0),
  PETSKILL_CHUNHUIDADI("春回大地", 82011, 1),
  PETSKILL_DUOHUNSUOMING("夺魂索命", 82012, 1);
  
  ULTIMATE_PETSKILL(String note, int value, int type) {
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
  
  public static List<Map<String, Integer>> toList() {
    List<Map<String, Integer>> list = Lists.newArrayList();
    for (ULTIMATE_PETSKILL n : values()) {
      Map<String, Integer> map = new HashMap<>();
      map.put("value", Integer.valueOf(n.getValue()));
      map.put("type", Integer.valueOf(n.getType()));
      list.add(map);
    } 
    return list;
  }
}
