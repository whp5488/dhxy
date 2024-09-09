package com.kitty.system.game.pet.handler;

import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import java.util.Iterator;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.enter.message.RespUpdate;
import java.util.ArrayList;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.pet.domain.Pet;
import org.springframework.stereotype.Component;

@Component("CutHfHandler")
public class CutHfHandler extends FeedPetHandler
{
    private static final String SUCCESS = "改变召唤兽幻肤成功";
    
    public CutHfHandler() {
    }
    
    @Override
    public String acceptFeed(Pet pet, Role role, Integer param, String type, int count, Object... args) {
        if ((int)param > 0 && !pet.getBrandNew().keySet().contains(param)) {
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
        if ((int)param > 0) {
            pet.getBrandNew().put(param, Byte.valueOf((byte)1));
        }
        pet.save();
        List<FiedValue> list = new ArrayList<>();
        this.petService.petHf(pet, list);
        RespUpdate respUpdate = new RespUpdate();
        respUpdate.setId((int)pet.getPetId());
        respUpdate.setList(list);
        MessagePusher.pushMessage(role, respUpdate);
        return "改变召唤兽幻肤成功";
    }
}
