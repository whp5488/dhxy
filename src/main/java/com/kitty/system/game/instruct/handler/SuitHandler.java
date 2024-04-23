package com.kitty.system.game.instruct.handler;

import com.kitty.common.start.ServerService;
import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.instruct.inter.NcFather;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.init.domain.SysDataAcsry;
import com.kitty.system.init.domain.SysDataAcsryEgg;
import com.kitty.system.init.pool.ItemDataPool;
import java.util.List;
import java.util.stream.Collectors;

public class SuitHandler implements NcFather {
  public void handle(Role role, String info) {
    try {
      if (((ServerService)SpringUtils.getBean(ServerService.class)).getType() == 1)
        return; 
      int pos = Integer.valueOf(info).intValue();
      List<SysDataAcsry> list = (List<SysDataAcsry>)EquipDataPool.acsryMap.values().stream().filter(e -> (((e.getOwnerRole().contains(role.getRace()) && e.getSex() == 0) || e.getSex() == role.getSex().intValue()) && e.getPos() == pos)).collect(Collectors.toList());
      if (list.size() > 0) {
        int index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(list.size() - 1));
        SysDataAcsry acsry = list.get(index);
        SysDataAcsryEgg acsryEgg = ItemDataPool.acsryEggMap.values().stream().filter(e -> (e.getHatchType() == acsry.getSerial())).findFirst().orElse(null);
        if (acsryEgg != null)
          ((BagService)SpringUtils.getBean(BagService.class)).addGoods(role, acsryEgg.getSerial(), 1, 0, true, true, "SuitHandler"); 
      } 
    } catch (Exception e) {
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "未找到道具信息", false));
    } 
  }
}
