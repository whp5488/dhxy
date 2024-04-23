package com.kitty.system.game.suit.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.suit.message.vo.SuitEasy;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 4758)
public class RespSuit extends Message {

  private List<SuitEasy> list = new ArrayList<>();

}
