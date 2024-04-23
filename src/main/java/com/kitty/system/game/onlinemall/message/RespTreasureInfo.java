package com.kitty.system.game.onlinemall.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.cache.Deploy;
import com.kitty.mina.message.Message;
import com.kitty.system.game.onlinemall.message.vo.WeekTreasureInfo;
import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = 8411)
public class RespTreasureInfo extends Message {
  private int count;
  
  public void setCount(int count) {
    this.count = count;
  }
  
  public void setOriginal(int original) {
    this.original = original;
  }
  
  public void setPrice(int price) {
    this.price = price;
  }
  
  public void setList(List<WeekTreasureInfo> list) {
    this.list = list;
  }
  
  public int getCount() {
    return this.count;
  }
  
  private int original = 8800;
  
  public int getOriginal() {
    return this.original;
  }
  
  private int price = Deploy.treasurePrice;
  
  public int getPrice() {
    return this.price;
  }
  
  private List<WeekTreasureInfo> list = new ArrayList<>();
  
  public List<WeekTreasureInfo> getList() {
    return this.list;
  }
}
