package com.kitty.system.game.task.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 53724)
public class ReqSubmitQuestionProvince extends Message {
  private int serial;
  
  private String answer;

}
