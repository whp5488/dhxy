package com.kitty.system.game.shop.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.shop.domain.ItemInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@MessageMeta(module = 65503)
public class RespGoodsList extends Message {
  
  private int type = 0;
  
  private int id;
  
  private List<ItemInfo> list;

}
