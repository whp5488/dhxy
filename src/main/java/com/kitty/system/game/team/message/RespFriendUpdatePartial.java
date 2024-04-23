package com.kitty.system.game.team.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.common.enter.FiedValue;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@MessageMeta(module = 24505)
public class RespFriendUpdatePartial extends Message {
  
  private int update_type = 1;

  private String type = "6";
  
  private String name;
  
  private List<FiedValue> list;

}
