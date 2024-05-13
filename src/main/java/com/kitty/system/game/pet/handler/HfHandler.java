
package com.kitty.system.game.pet.handler;

import java.util.List;
import java.util.Iterator;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.enter.message.RespUpdate;
import com.kitty.system.common.enter.FiedValue;
import java.util.ArrayList;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.init.domain.SysOtherItem;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.pet.domain.Pet;
import org.springframework.stereotype.Component;

@Component("HfHandler")
public class HfHandler extends FeedPetHandler
{
    private static final String SUCCESS = "激活幻肤成功";
    
    @Override
    public String acceptFeed(final Pet pet, final Role role, final Integer param, final String type, final int count, final Object... args) {
        final SysOtherItem item = ItemDataPool.data_other_item.values().stream().filter(e -> e.getValue().equals(param)).findFirst().orElse(null);
        if (item == null) {
            return null;
        }
        final SysPet sysPet = PetDataPool.petMap.get(pet.getPetType());
        if (!sysPet.getBrandNew().contains(item.getSerial())) {
            return null;
        }
        final Integer goods = this.bagService.consumeBagGoods(role, item.getSerial(), 1, true, true, "宠物激活幻肤");
        if (goods != null) {
            return null;
        }
        for (final Integer key : pet.getBrandNew().keySet()) {
            if (pet.getBrandNew().get(key).equals(1)) {
                pet.getBrandNew().put(key, (byte)0);
            }
        }
        pet.getBrandNew().put(item.getValue(), (byte)1);
        pet.save();
        final List<FiedValue> list = new ArrayList<>();
        this.petService.petHf(pet, list);
        final RespUpdate respUpdate = new RespUpdate();
        respUpdate.setId(pet.getPetId());
        respUpdate.setList(list);
        MessagePusher.pushMessage(role, respUpdate);
        return "激活幻肤成功";
    }
}
