package com.kitty.system.game.player.serializer.ipml;

import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.player.serializer.IPlayerPropSerializer;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.domain.RoleOfflineNotice;
import org.apache.commons.lang3.StringUtils;

public class PlayerOfflinNoticekSerializer implements IPlayerPropSerializer {
  public void serialize(Role player) {
    if (player.getOfflineNotice() != null) {
      String json = JsonUtils.object2String(player.getOfflineNotice());
      player.setOfflinNoticeJson(json);
    } 
  }
  
  public void deserialize(Role player) {
    String json = player.getOfflinNoticeJson();
    if (StringUtils.isNotEmpty(json))
      player.setOfflineNotice((RoleOfflineNotice)JsonUtils.string2Object(json, RoleOfflineNotice.class)); 
  }
}
