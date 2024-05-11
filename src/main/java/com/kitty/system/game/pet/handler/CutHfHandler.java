
package com.kitty.system.game.pet.handler;

import java.util.List;
import java.util.Iterator;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.enter.message.RespUpdate;
import com.kitty.system.common.enter.FiedValue;
import java.util.ArrayList;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.pet.domain.Pet;
import org.springframework.stereotype.Component;

@Component("CutHfHandler")
public class CutHfHandler extends FeedPetHandler
{
    private static final String SUCCESS = "改变召唤兽幻肤成功";
    
    @Override
    public String acceptFeed(final Pet pet, final Role role, final Integer param, final String type, final int count, final Object... args) {
        if (param > 0 && !pet.getBrandNew().keySet().contains(param)) {
            return null;
        }
        for (final Integer key : pet.getBrandNew().keySet()) {
            if (pet.getBrandNew().get(key).equals(1)) {
                pet.getBrandNew().put(key, (byte)0);
            }
        }
        if (param > 0) {
            pet.getBrandNew().put(param, (byte)1);
        }
        pet.save();
        final List<FiedValue> list = new ArrayList<FiedValue>();
        this.petService.petHf(pet, list);
        final RespUpdate respUpdate = new RespUpdate();
        respUpdate.setId(pet.getPetId());
        respUpdate.setList(list);
        MessagePusher.pushMessage(role, respUpdate);
        return "改变召唤兽幻肤成功";
    }
}
