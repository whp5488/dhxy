package com.kitty.system.game.bag.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.bag.message.vo.PotholingVo;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 33042)
public class RespPotholing extends Message {
  private List<PotholingVo> list;
  
  private int index;
  
  private String id;
  
  @ConstructorProperties({"list", "index", "id"})
  public RespPotholing(List<PotholingVo> list, int index, String id) {
    this.list = list;
    this.index = index;
    this.id = id;
  }

}
