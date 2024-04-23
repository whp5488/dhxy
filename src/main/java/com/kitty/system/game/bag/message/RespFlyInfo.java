package com.kitty.system.game.bag.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.equip.domain.EquipEasy;
import com.kitty.system.game.role.domain.Goods;
import com.kitty.system.game.tianfu.pojo.RoleTianFuClient;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 65525)
public class RespFlyInfo extends Message {
  
  private byte pos = 0;

  private byte type = 0;
  
  private Goods goods;
  
  private EquipEasy equip;
  
  private RoleTianFuClient roleTianFuClient;

}
