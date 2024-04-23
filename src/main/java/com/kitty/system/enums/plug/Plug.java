package com.kitty.system.enums.plug;

import java.util.List;
import org.apache.commons.compress.utils.Lists;

public enum Plug {
  TREASURE_MAP(2000, "plug_treasure", "挖宝"),
  TREASURE_MAP_BUY(2001, null, null);
  
  Plug(int serial, String dict, String tips) {
    this.serial = serial;
    this.dict = dict;
    this.tips = tips;
  }
  
  private int serial;
  
  private String dict;
  
  private String tips;
  
  public int getSerial() {
    return this.serial;
  }
  
  public String getDict() {
    return this.dict;
  }
  
  public String getTips() {
    return this.tips;
  }
  
  public static List<Integer> toList() {
    List<Integer> list = Lists.newArrayList();
    for (Plug n : values())
      list.add(Integer.valueOf(n.getSerial())); 
    return list;
  }
  
  public static String getTips(int serial) {
    for (Plug n : values()) {
      if (serial == n.getSerial())
        return n.getTips(); 
    } 
    return null;
  }
}
