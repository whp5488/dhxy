package com.kitty.system.game.house.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.house.message.vo.HouseEasy;
import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = 20578)
public class RespHouse extends Message {
  public void setList(List<HouseEasy> list) {
    this.list = list;
  }
  
  @ConstructorProperties({"list"})
  public RespHouse(List<HouseEasy> list) {
    this.list = list;
  }
  
  private List<HouseEasy> list = new ArrayList<>();
  
  public List<HouseEasy> getList() {
    return this.list;
  }
  
  public RespHouse() {}
}
