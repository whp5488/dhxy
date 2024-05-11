
package com.kitty.system.game.player.serializer.ipml;

import com.kitty.system.game.role.domain.RoleOfflineNotice;
import org.apache.commons.lang3.StringUtils;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.player.serializer.IPlayerPropSerializer;

public class PlayerOfflinNoticekSerializer implements IPlayerPropSerializer
{
    @Override
    public void serialize(final Role player) {
        if (player.getOfflineNotice() != null) {
            final String json = JsonUtils.object2String(player.getOfflineNotice());
            player.setOfflinNoticeJson(json);
        }
    }
    
    @Override
    public void deserialize(final Role player) {
        final String json = player.getOfflinNoticeJson();
        if (StringUtils.isNotEmpty((CharSequence)json)) {
            player.setOfflineNotice(JsonUtils.string2Object(json, RoleOfflineNotice.class));
        }
    }
}
