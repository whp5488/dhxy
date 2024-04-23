package com.kitty.system.game.team.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.common.enter.FiedValue;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@MessageMeta(module = 20467)
public class RespAroundInfo extends Message {
  private int roleId;
  
  private String inviteName;
  
  private String type;
  
  private int serial;

  private int teamMembersCount = 1;
  
  private String time;
  
  private List<FiedValue> list;

}
