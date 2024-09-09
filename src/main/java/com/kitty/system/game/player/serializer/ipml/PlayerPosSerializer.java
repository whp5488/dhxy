package com.kitty.system.game.player.serializer.ipml;

import com.kitty.common.model.Pos;
import org.apache.commons.lang3.StringUtils;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.player.serializer.IPlayerPropSerializer;

public class PlayerPosSerializer implements IPlayerPropSerializer
{
    public PlayerPosSerializer() {
    }
    
    @Override
    public void serialize(Role player) {
        if (player.getPos() != null) {
            String posJson = JsonUtils.object2String(player.getPos());
            player.setPosJson(posJson);
        }
    }
    
    @Override
    public void deserialize(Role player) {
        String json = player.getPosJson();
        if (StringUtils.isNotEmpty(json)) {
            player.setPos((Pos)JsonUtils.string2Object(json, Pos.class));
        }
    }
}
