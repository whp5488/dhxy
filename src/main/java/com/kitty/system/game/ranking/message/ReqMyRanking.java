package com.kitty.system.game.ranking.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 42120)
public class ReqMyRanking extends Message {
  private byte type;
  
  private int serial;
  
  private byte serverId;

}
