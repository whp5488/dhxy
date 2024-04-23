package com.kitty.system.game.instruct.handler;

import com.kitty.common.start.ServerService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.instruct.inter.NcFather;
import com.kitty.system.game.role.domain.Role;

public class EquipHandler implements NcFather {
  public void handle(Role role, String items) {
    if (((ServerService)SpringUtils.getBean(ServerService.class)).getType() == 1)
      return; 
    Integer serial = Integer.valueOf(items);
    if (serial.intValue() > 0) {
      ((EquipService)SpringUtils.getBean(EquipService.class)).upgradeEquip(role, 0, 11, String.valueOf(items), 0, false, "命令");
    } else {
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "未找到#<r:255,g:0,b:0>" + items + "#装备信息", false));
    } 
  }
}
