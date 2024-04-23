package com.kitty.system.game.bag.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
@MessageMeta(module = 30778)
public class ReqOperateItem extends Message {

  @ConstructorProperties({"pos", "type", "num", "param"})
  public ReqOperateItem(int pos, int type, int num, String param) {
    this.pos = pos;
    this.type = type;
    this.num = num;
    this.param = param;
  }
  
  private int pos = -1;
  
  private int type;

  private int num = 0;
  
  private String param;

  public ReqOperateItem() {}
}
