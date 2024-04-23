package com.kitty.system.game.gate.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.util.List;

@MessageMeta(module = 45628)
public class ResplineData extends Message {
  private List<LineData> datas;
  
  public List<LineData> getDatas() {
    return this.datas;
  }
  
  public void setDatas(List<LineData> datas) {
    this.datas = datas;
  }
}
