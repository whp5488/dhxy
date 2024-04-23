package com.kitty.system.game.ranking.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 4226)
public class ReqQlkRanking extends Message {
  private short qlkType;
}
