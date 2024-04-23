package com.kitty.system.game.onlinemall.domain;

public class OnlineMallInfo {
  private int serial;
  
  private int type;
  
  private String recommend;
  
  private int sort;
  
  private int price;
  
  private int costType;
  
  private int discount;
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setType(int type) {
    this.type = type;
  }
  
  public void setRecommend(String recommend) {
    this.recommend = recommend;
  }
  
  public void setSort(int sort) {
    this.sort = sort;
  }
  
  public void setPrice(int price) {
    this.price = price;
  }
  
  public void setCostType(int costType) {
    this.costType = costType;
  }
  
  public void setDiscount(int discount) {
    this.discount = discount;
  }
  
  public void setQuotaLimit(int quotaLimit) {
    this.quotaLimit = quotaLimit;
  }
  
  public void setMustVip(int mustVip) {
    this.mustVip = mustVip;
  }
  
  public void setLvLimit(int lvLimit) {
    this.lvLimit = lvLimit;
  }
  
  public void setZsLimit(int zsLimit) {
    this.zsLimit = zsLimit;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public int getType() {
    return this.type;
  }
  
  public String getRecommend() {
    return this.recommend;
  }
  
  public int getSort() {
    return this.sort;
  }
  
  public int getPrice() {
    return this.price;
  }
  
  public int getCostType() {
    return this.costType;
  }
  
  public int getDiscount() {
    return this.discount;
  }
  
  private int quotaLimit = -1;
  
  public int getQuotaLimit() {
    return this.quotaLimit;
  }
  
  private int mustVip = 0;
  
  public int getMustVip() {
    return this.mustVip;
  }
  
  private int lvLimit = 0;
  
  public int getLvLimit() {
    return this.lvLimit;
  }
  
  private int zsLimit = 0;
  
  public int getZsLimit() {
    return this.zsLimit;
  }
}
