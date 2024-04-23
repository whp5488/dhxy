package com.kitty.system.game.bag.helper;

import com.kitty.common.utils.JsonUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.enter.FieldValue;
import com.kitty.system.game.bag.domain.BagGoods;
import com.kitty.system.game.bag.message.RespZhenLuComposeInfo;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.init.Item;
import com.kitty.system.init.domain.SysOtherItem;
import com.kitty.system.init.pool.ItemDataPool;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component("ZhenLuComposeInfo")
public class ZhenLuComposeInfoHandler extends BagHandler {
  public String acceptOperate(Role role, Integer pos, Integer num, String param) {
    Map<Long, BagGoods> bagGoodsMap = this.bagService.getBagGoods(role);
    BagGoods goods = bagGoodsMap.values().stream().filter(item -> pos.equals(Integer.valueOf(item.getPos()))).findFirst().orElse(null);
    if (goods == null)
      return null; 
    SysOtherItem otherItem = (SysOtherItem)ItemDataPool.data_other_item.get(Integer.valueOf(goods.getSerial()));
    if (otherItem.getParam() == null || otherItem.getParam().isEmpty())
      return null; 
    RespZhenLuComposeInfo respComposeInfo = new RespZhenLuComposeInfo();
    respComposeInfo.setSerial(otherItem.getSerial().intValue());
    List<Item> list = (List<Item>)JsonUtils.string2Collection(otherItem.getParam(), List.class, Item.class);
    for (Item item : list)
      respComposeInfo.getList().add(new FieldValue(item.getItem().intValue(), item.getNum().intValue())); 
    MessagePusher.pushMessage(role.getId().longValue(), (Message)respComposeInfo);
    return null;
  }
}
