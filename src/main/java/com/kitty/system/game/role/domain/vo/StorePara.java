package com.kitty.system.game.role.domain.vo;

import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.role.domain.Goods;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonIgnore;

public class StorePara implements Serializable {
  private int t;
  
  private int s;
  
  private int n;
  
  private String v;
  
  public void setT(int t) {
    this.t = t;
  }
  
  public void setS(int s) {
    this.s = s;
  }
  
  public void setN(int n) {
    this.n = n;
  }
  
  public void setV(String v) {
    this.v = v;
  }
  
  public void setRests(List<FiedValue> rests) {
    this.rests = rests;
  }
  
  public void setGoods(List<Goods> goods) {
    this.goods = goods;
  }
  
  public void setEquips(List<BagEquip> equips) {
    this.equips = equips;
  }
  
  public int getT() {
    return this.t;
  }
  
  public int getS() {
    return this.s;
  }
  
  public int getN() {
    return this.n;
  }
  
  public String getV() {
    return this.v;
  }
  
  private List<FiedValue> rests = new ArrayList<>();
  
  public List<FiedValue> getRests() {
    return this.rests;
  }
  
  @JsonIgnore
  private List<Goods> goods = new ArrayList<>();
  
  public List<Goods> getGoods() {
    return this.goods;
  }
  
  @JsonIgnore
  private List<BagEquip> equips = new ArrayList<>();
  
  public List<BagEquip> getEquips() {
    return this.equips;
  }
}
