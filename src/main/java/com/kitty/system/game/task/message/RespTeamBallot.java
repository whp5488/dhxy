package com.kitty.system.game.task.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 45111)
public class RespTeamBallot extends Message {
  private boolean isTeamLeader;
  
  private String msg;
  
  private int endTime;
  
  private String title;
  
  private String type;
  
  private String para;
}
