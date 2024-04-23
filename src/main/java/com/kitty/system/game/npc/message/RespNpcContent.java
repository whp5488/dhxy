package com.kitty.system.game.npc.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 8247)
public class RespNpcContent extends Message {
  private int id;
  
  private int icon;
  
  private String npcName;
  
  private String talk;
  
  private String content;

}
