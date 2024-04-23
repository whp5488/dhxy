package com.kitty.system.enums.partner;

public enum FeedPartnerType {
  PARTNER_PRACTICE("partner_practice");
  
  private String type;
  
  FeedPartnerType(String type) {
    this.type = type;
  }
  
  public String getType() {
    return this.type;
  }
}
