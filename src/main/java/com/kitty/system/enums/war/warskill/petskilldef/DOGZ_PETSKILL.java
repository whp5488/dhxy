package com.kitty.system.enums.war.warskill.petskilldef;

import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum DOGZ_PETSKILL {
  PETSKILL_QIANGHUAXUANREN("强化悬刃", 83001, 0),
  PETSKILL_QIANGHUAYIHUAN("强化遗患", 83002, 0),
  PETSKILL_BINGLINCHENGXIA("兵临城下", 83003, 1),
  PETSKILL_RUHUTIANYI("如虎添翼", 83004, 0),
  PETSKILL_NIEPAN("涅槃--重生", 83005, 0),
  PETSKILL_JINGGUANBAIRI("精贯白日", 83006, 0),
  PETSKILL_CHAOMINGDIANCHE("潮鸣电掣", 83007, 0);
  
  DOGZ_PETSKILL(String note, int value, int type) {
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
    for (DOGZ_PETSKILL n : values()) {
      Map<String, Integer> map = new HashMap<>();
      map.put("value", Integer.valueOf(n.getValue()));
      map.put("type", Integer.valueOf(n.getType()));
      list.add(map);
    } 
    return list;
  }
}
