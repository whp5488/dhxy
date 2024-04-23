package com.kitty.system.game.team.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 4159)
public class ReqUpdateTeamTarget extends Message {
  private int roleId;
  
  private String activityType;

}
