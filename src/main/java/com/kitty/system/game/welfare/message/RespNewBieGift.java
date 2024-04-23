package com.kitty.system.game.welfare.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.welfare.domain.GiftsInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@MessageMeta(module = 49171)
public class RespNewBieGift extends Message {
  private List<GiftsInfo> gifts;

}
