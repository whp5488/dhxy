package com.kitty.system.game.instruct.handler;

import com.alibaba.fastjson.JSONObject;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.card.pool.CardDataPool;
import com.kitty.system.game.card.service.CardService;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.instruct.inter.NcFather;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.init.domain.SysDataCard;
import com.kitty.system.init.domain.SysEffect;
import com.kitty.system.init.pool.ItemDataPool;
import java.util.Map;

public class ItemHandler implements NcFather {
  public void handle(Role role, String info) {
    try {
      String[] split = info.split(",");
      Integer num = Integer.valueOf(split[1]);
      SysDataCard card = (SysDataCard)CardDataPool.cardMap.get(Integer.valueOf(split[0]));
      if (card != null) {
        ((CardService)SpringUtils.getBean(CardService.class)).addCard(role, Integer.valueOf(card.getSerial()), num, Integer.valueOf(0), true, true);
        return;
      } 
      SysEffect sysEffect = (SysEffect)ItemDataPool.effectMap.get(Integer.valueOf(split[0]));
      if (sysEffect != null) {
        ((EquipService)SpringUtils.getBean(EquipService.class)).addEffect(role, sysEffect.getSerial(), 0);
        return;
      } 
      Map map = DataGlobalFunc.GetItemDataByItemTypeId(Integer.valueOf(split[0]));
      JSONObject json = JsonUtils.objectToJson(map.get(Integer.valueOf(split[0])));
      int serial = json.getInteger("serial").intValue();
      ((BagService)SpringUtils.getBean(BagService.class)).addGoods(role, serial, num.intValue(), 0, true, true, "ItemHandler");
    } catch (Exception e) {
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "未找到道具信息", false));
    } 
  }
}
