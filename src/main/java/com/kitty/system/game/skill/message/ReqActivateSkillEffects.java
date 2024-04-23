package com.kitty.system.game.skill.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 8309)
public class ReqActivateSkillEffects extends Message {
  private int serial;
  
  private byte type;
}
