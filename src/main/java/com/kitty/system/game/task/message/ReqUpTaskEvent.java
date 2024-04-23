package com.kitty.system.game.task.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@MessageMeta(module = 33023)
public class ReqUpTaskEvent extends Message {
  private int taskId;
  
  private List<Integer> event;

}
