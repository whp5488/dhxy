package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.party.model.PartyInfo;
import java.util.List;

@MessageMeta(module = 40977)
public class RespPartyInfo extends Message {
  private String order;
  
  private byte page;
  
  private byte sort;
  
  private byte way;
  
  private List<PartyInfo> list;
  
  public void setOrder(String order) {
    this.order = order;
  }
  
  public void setPage(byte page) {
    this.page = page;
  }
  
  public void setSort(byte sort) {
    this.sort = sort;
  }
  
  public void setWay(byte way) {
    this.way = way;
  }
  
  public void setList(List<PartyInfo> list) {
    this.list = list;
  }
  
  public String getOrder() {
    return this.order;
  }
  
  public byte getPage() {
    return this.page;
  }
  
  public byte getSort() {
    return this.sort;
  }
  
  public byte getWay() {
    return this.way;
  }
  
  public List<PartyInfo> getList() {
    return this.list;
  }
}
