package com.kitty.system.game.fightnew.messages.old;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 32984)
public class ReqAutoFightSetData extends Message {
  private int id;

  private int action = 1;
  
  private int type;
  
  private int skillId;

  private int born = 0;

}
