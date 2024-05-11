
package com.kitty.system.game.player.serializer.ipml;

import com.kitty.system.game.player.bean.PlayerExtendBox;
import org.apache.commons.lang3.StringUtils;
import com.kitty.common.utils.ZipUtil;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.player.serializer.IPlayerPropSerializer;

public class PlayerExtendSerializer implements IPlayerPropSerializer
{
    @Override
    public void serialize(final Role player) {
        if (player.getExtendBox() != null) {
            final String extendBoxJson = JsonUtils.object2String(player.getExtendBox());
            player.setExtendBoxJson(ZipUtil.compress(extendBoxJson));
        }
    }
    
    @Override
    public void deserialize(final Role player) {
        final String json = player.getExtendBoxJson();
        if (StringUtils.isNotEmpty((CharSequence)json)) {
            final String content = ZipUtil.uncompress(json);
            player.setExtendBox(JsonUtils.string2Object(content, PlayerExtendBox.class));
        }
    }
}
