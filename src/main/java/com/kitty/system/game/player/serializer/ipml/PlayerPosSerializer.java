
package com.kitty.system.game.player.serializer.ipml;

import com.kitty.common.model.Pos;
import org.apache.commons.lang3.StringUtils;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.player.serializer.IPlayerPropSerializer;

public class PlayerPosSerializer implements IPlayerPropSerializer
{
    @Override
    public void serialize(final Role player) {
        if (player.getPos() != null) {
            final String posJson = JsonUtils.object2String(player.getPos());
            player.setPosJson(posJson);
        }
    }
    
    @Override
    public void deserialize(final Role player) {
        final String json = player.getPosJson();
        if (StringUtils.isNotEmpty((CharSequence)json)) {
            player.setPos(JsonUtils.string2Object(json, Pos.class));
        }
    }
}
