package com.kitty.system.game.welfare.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.welfare.domain.GiftsInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@MessageMeta(module = 49172)
public class RespPayBieGift extends Message {
  private List<GiftsInfo> gifts;
  
  private String total;
  
  private byte type = 0;

  public RespPayBieGift(List<GiftsInfo> gifts, String total, byte type) {
    this.gifts = gifts;
    this.total = total;
    this.type = type;
  }
  
  public RespPayBieGift() {}
}
