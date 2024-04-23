package com.kitty.system.game.task.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
@MessageMeta(module = 33025)
public class RespTaskFightEndNotify extends Message {
  private int taskId;
  
  private int npcId;
  
  private byte result;

  @ConstructorProperties({"taskId", "npcId", "result"})
  public RespTaskFightEndNotify(int taskId, int npcId, byte result) {
    this.taskId = taskId;
    this.npcId = npcId;
    this.result = result;
  }
  
  public RespTaskFightEndNotify() {}

}
