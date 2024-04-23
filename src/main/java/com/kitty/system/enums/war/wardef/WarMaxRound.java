package com.kitty.system.enums.war.wardef;

public enum WarMaxRound {
  WAR_MAX_ROUND("最大回合", 255),
  WAR_MAX_ROUND_BWC("竞技场", 50),
  WAR_MAX_ROUND_CA("决战长安", 40),
  WAR_MAX_ROUND_LU("水陆大会", 40),
  WAR_MAX_ROUND_HG("皇宫约战", 40),
  WAR_MAX_ROUND_GANG_WARS("帮派大战", 60);
  
  WarMaxRound(String note, int value) {
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
}
