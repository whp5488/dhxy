package com.kitty.system.game.pet.handler;

import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import java.util.Iterator;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.enter.message.RespUpdate;
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
    
    public HfHandler() {
    }
    
    @Override
    public String acceptFeed(Pet pet, Role role, Integer param, String type, int count, Object... args) {
        SysOtherItem item = (SysOtherItem)ItemDataPool.data_other_item.values().stream().filter(e -> e.getValue().equals(param)).findFirst().orElse(null);
        if (item == null) {
            return null;
        }
        SysPet sysPet = (SysPet)PetDataPool.petMap.get(pet.getPetType());
        if (!sysPet.getBrandNew().contains(item.getSerial())) {
            return null;
        }
        Integer goods = this.bagService.consumeBagGoods(role, (int)item.getSerial(), 1, true, true, "宠物激活幻肤");
        if (goods != null) {
            return null;
        }
        for (Integer key : pet.getBrandNew().keySet()) {
            if (((Byte)pet.getBrandNew().get(key)).equals(Byte.valueOf((byte)1))) {
                pet.getBrandNew().put(key, Byte.valueOf((byte)0));
            }
            else {
                continue;
            }
        }
        pet.getBrandNew().put(item.getValue(), Byte.valueOf((byte)1));
        pet.save();
        List<FiedValue> list = new ArrayList<>();
        this.petService.petHf(pet, list);
        RespUpdate respUpdate = new RespUpdate();
        respUpdate.setId((int)pet.getPetId());
        respUpdate.setList(list);
        MessagePusher.pushMessage(role, respUpdate);
        return "激活幻肤成功";
    }
}
