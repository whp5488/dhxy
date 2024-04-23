package com.kitty.system.game.market.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.market.model.StallItem;
import java.util.List;

@MessageMeta(module = 49183)
public class RespStallList extends Message {
  private short totalPage;
  
  private short currPage;
  
  private List<StallItem> list;
  
  private String classify;
  
  public void setTotalPage(short totalPage) {
    this.totalPage = totalPage;
  }
  
  public void setCurrPage(short currPage) {
    this.currPage = currPage;
  }
  
  public void setList(List<StallItem> list) {
    this.list = list;
  }
  
  public void setClassify(String classify) {
    this.classify = classify;
  }
  
  public short getTotalPage() {
    return this.totalPage;
  }
  
  public short getCurrPage() {
    return this.currPage;
  }
  
  public List<StallItem> getList() {
    return this.list;
  }
  
  public String getClassify() {
    return this.classify;
  }
}
