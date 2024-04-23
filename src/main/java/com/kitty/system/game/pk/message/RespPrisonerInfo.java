package com.kitty.system.game.pk.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.pk.message.vo.PrisonerInfo;
import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = 45230)
public class RespPrisonerInfo extends Message {
  public void setList(List<PrisonerInfo> list) {
    this.list = list;
  }
  
  private List<PrisonerInfo> list = new ArrayList<>();
  
  public List<PrisonerInfo> getList() {
    return this.list;
  }
}
