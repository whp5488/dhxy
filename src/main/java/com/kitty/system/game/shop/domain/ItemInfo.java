package com.kitty.system.game.shop.domain;

import com.kitty.system.common.enter.FiedValue;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ItemInfo {
  private int serial;

  private int count = 1;

  private int pay_type = 1;
  
  private int price;

  private int discount = 0;

  private int quotaLimit = -1;

  private List<FiedValue> list = new ArrayList<>();

  public ItemInfo(int serial, int count, int pay_type, int price, int discount, int quotaLimit, List<FiedValue> list) {
    this.serial = serial;
    this.count = count;
    this.pay_type = pay_type;
    this.price = price;
    this.discount = discount;
    this.quotaLimit = quotaLimit;
    this.list = list;
  }
  
  public ItemInfo(int serial, int count, int pay_type, int price, int discount, int quotaLimit) {
    this.serial = serial;
    this.count = count;
    this.pay_type = pay_type;
    this.price = price;
    this.discount = discount;
    this.quotaLimit = quotaLimit;
  }
  
  public ItemInfo() {}
}
