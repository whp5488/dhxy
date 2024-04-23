package com.kitty.system.game.ranking.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 4229)
public class ReqCaNomRanking extends Message {

  private byte type = 0;

  private byte classify = 0;

}
