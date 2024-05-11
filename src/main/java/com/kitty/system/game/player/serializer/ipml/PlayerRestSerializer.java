
package com.kitty.system.game.player.serializer.ipml;

import com.kitty.system.game.player.bean.PlayerRest;
import org.apache.commons.lang3.StringUtils;
import com.kitty.common.utils.ZipUtil;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.player.serializer.IPlayerPropSerializer;

public class PlayerRestSerializer implements IPlayerPropSerializer
{
    @Override
    public void serialize(final Role player) {
        if (player.getRest() != null) {
            final String restJson = JsonUtils.object2String(player.getRest());
            player.setRestJson(ZipUtil.compress(restJson));
        }
    }
    
    @Override
    public void deserialize(final Role player) {
        final String json = player.getRestJson();
        if (StringUtils.isNotEmpty((CharSequence)json)) {
            final String content = ZipUtil.uncompress(json);
            player.setRest(JsonUtils.string2Object(content, PlayerRest.class));
        }
    }
}
