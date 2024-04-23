package com.kitty.system.game.tianfu.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
@MessageMeta(module = 44001)
public class RespTianFuDebris extends Message {
  private String id;
  
  private int num;

  public RespTianFuDebris() {}
  
  @ConstructorProperties({"id", "num"})
  public RespTianFuDebris(String id, int num) {
    this.id = id;
    this.num = num;
  }

  public RespTianFuDebris(RoleTianFu roleTianFu) {
    this.id = String.valueOf(roleTianFu.getId());
    this.num = roleTianFu.getCount();
  }
}
