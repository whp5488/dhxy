package com.kitty.system.game.team.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.common.enter.FiedValue;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@MessageMeta(module = 61545)
public class RespFriendAddRole extends Message {

  private int type1 = 1;

  private String type = "6";
  
  private String name;

  private int online = 1;

  private int insider_level = 0;
  
  private List<FiedValue> list;

}
