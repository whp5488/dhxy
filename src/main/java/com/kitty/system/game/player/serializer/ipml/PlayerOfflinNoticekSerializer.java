package com.kitty.system.game.player.serializer.ipml;

import com.kitty.system.game.role.domain.RoleOfflineNotice;
import org.apache.commons.lang3.StringUtils;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.player.serializer.IPlayerPropSerializer;

public class PlayerOfflinNoticekSerializer implements IPlayerPropSerializer
{
    public PlayerOfflinNoticekSerializer() {
    }
    
    @Override
    public void serialize(Role player) {
        if (player.getOfflineNotice() != null) {
            String json = JsonUtils.object2String(player.getOfflineNotice());
            player.setOfflinNoticeJson(json);
        }
    }
    
    @Override
    public void deserialize(Role player) {
        String json = player.getOfflinNoticeJson();
        if (StringUtils.isNotEmpty(json)) {
            player.setOfflineNotice((RoleOfflineNotice)JsonUtils.string2Object(json, RoleOfflineNotice.class));
        }
    }
}
