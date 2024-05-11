
package com.kitty.system.game.bag.helper;

import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.bag.message.RespZhenLuCasePet;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.init.domain.SysPet;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.vo.init.Item;
import java.util.List;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.init.domain.SysOtherItem;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("ZhenLuCase")
public class ZhenLuCaseHandler extends BagHandler
{
    @Override
    public String acceptOperate(final Role role, final Integer pos, final Integer num, final String param) {
        final int serial = Integer.valueOf(param);
        final SysOtherItem otherItem = ItemDataPool.data_other_item.get(pos);
        if (otherItem.getParam() == null || otherItem.getParam().isEmpty()) {
            return null;
        }
        final List<Item> list = JsonUtils.string2Collection(otherItem.getParam(), List.class, Item.class);
        final Item item = list.stream().filter(e -> e.getItem().equals(serial)).findFirst().orElse(null);
        if (item == null) {
            return null;
        }
        final SysPet sysPet = PetDataPool.petMap.get(item.getItem());
        if (sysPet == null) {
            return null;
        }
        final PetService petService = SpringUtils.getPetService();
        final boolean flag = petService.canGetPet(role, sysPet, false);
        if (!flag) {
            return null;
        }
        final Integer integer = this.bagService.consumeBagGoods(role, otherItem.getSerial(), item.getNum(), true, true, "珍露兑换");
        if (integer != null) {
            MessagePusher.pushMessage(role.getId(), new RespMsg(String.format("缺少#<II%d>##<%s>[%s]#X%d", otherItem.getSerial(), DataGlobalFunc.getColours(otherItem.getItemPJ(), true), otherItem.getName(), integer)));
            return null;
        }
        final Pet pet = petService.addPet(role, sysPet, 0, "珍露兑换");
        if (pet != null) {
            petService.loadPet(role, pet);
        }
        else {
            this.logger.error("珍露兑换宠物失败，道具编号 = {}，合成宠物编号 = {}", (Object)otherItem.getSerial(), (Object)sysPet.getSerial());
        }
        MessagePusher.pushMessage(role.getId(), new RespZhenLuCasePet(pet));
        return null;
    }
}
