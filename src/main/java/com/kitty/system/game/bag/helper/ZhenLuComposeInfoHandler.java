
package com.kitty.system.game.bag.helper;

import java.util.Iterator;
import java.util.Map;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.enter.FieldValue;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.vo.init.Item;
import java.util.List;
import com.kitty.system.game.bag.message.RespZhenLuComposeInfo;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.init.domain.SysOtherItem;
import com.kitty.system.game.bag.domain.BagGoods;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("ZhenLuComposeInfo")
public class ZhenLuComposeInfoHandler extends BagHandler
{
    @Override
    public String acceptOperate(final Role role, final Integer pos, final Integer num, final String param) {
        final Map<Long, BagGoods> bagGoodsMap = this.bagService.getBagGoods(role);
        final BagGoods goods = bagGoodsMap.values().stream().filter(item -> pos.equals(item.getPos())).findFirst().orElse(null);
        if (goods == null) {
            return null;
        }
        final SysOtherItem otherItem = ItemDataPool.data_other_item.get(goods.getSerial());
        if (otherItem.getParam() == null || otherItem.getParam().isEmpty()) {
            return null;
        }
        final RespZhenLuComposeInfo respComposeInfo = new RespZhenLuComposeInfo();
        respComposeInfo.setSerial(otherItem.getSerial());
        final List<Item> list = JsonUtils.string2Collection(otherItem.getParam(), List.class, Item.class);
        final Iterator<Item> iterator = list.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            respComposeInfo.getList().add(new FieldValue(item.getItem(), item.getNum()));
        }
        MessagePusher.pushMessage(role.getId(), respComposeInfo);
        return null;
    }
}
