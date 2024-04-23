package com.kitty.system.game.task.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
@MessageMeta(module = 53720)
public class RespQuestionConfirm extends Message {
  private byte type;

  public RespQuestionConfirm() {
    this.type = 0;
  }
  
  @ConstructorProperties({"type"})
  public RespQuestionConfirm(byte type) {
    this.type = 0;
    this.type = type;
  }

}
