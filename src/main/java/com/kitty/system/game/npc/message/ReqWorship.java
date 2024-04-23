package com.kitty.system.game.npc.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 6888)
public class ReqWorship extends Message {
  private int npcId;
}
