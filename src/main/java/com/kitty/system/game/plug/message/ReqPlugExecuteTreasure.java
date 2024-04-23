package com.kitty.system.game.plug.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@MessageMeta(module = 45300)
public class ReqPlugExecuteTreasure extends Message {
  private List<Integer> list;

}
