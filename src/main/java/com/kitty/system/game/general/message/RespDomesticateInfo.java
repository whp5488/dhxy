package com.kitty.system.game.general.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.general.message.vo.DomesticateInfo;
import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = 41481)
public class RespDomesticateInfo extends Message {

  public void setList(List<DomesticateInfo> list) {
    this.list = list;
  }
  
  private List<DomesticateInfo> list = new ArrayList<>();
  
  public List<DomesticateInfo> getList() {
    return this.list;
  }
}
