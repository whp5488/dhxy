package com.kitty.system.game.ranking.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 4222)
public class ReqPropertyPetRanking extends Message {
  private byte type;
  
  private byte serverId;
}
