package com.kitty.system.game.market.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.market.message.vo.Columbia;
import java.util.List;

@MessageMeta(module = 32901)
public class RespColumbia extends Message {
  private String para;
  
  private List<Columbia> list;
  
  private String price;
  
  public void setPara(String para) {
    this.para = para;
  }
  
  public void setList(List<Columbia> list) {
    this.list = list;
  }
  
  public void setPrice(String price) {
    this.price = price;
  }
  
  public String getPara() {
    return this.para;
  }
  
  public List<Columbia> getList() {
    return this.list;
  }
  
  public String getPrice() {
    return this.price;
  }
}
