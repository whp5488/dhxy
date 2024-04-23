package com.kitty.system.game.player.serializer.ipml;

import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.ZipUtil;
import com.kitty.system.game.player.bean.PlayerRest;
import com.kitty.system.game.player.serializer.IPlayerPropSerializer;
import com.kitty.system.game.role.domain.Role;
import org.apache.commons.lang3.StringUtils;

public class PlayerRestSerializer implements IPlayerPropSerializer {
  public void serialize(Role player) {
    if (player.getRest() != null) {
      String restJson = JsonUtils.object2String(player.getRest());
      player.setRestJson(ZipUtil.compress(restJson));
    } 
  }
  
  public void deserialize(Role player) {
    String json = player.getRestJson();
    if (StringUtils.isNotEmpty(json)) {
      String content = ZipUtil.uncompress(json);
      player.setRest((PlayerRest)JsonUtils.string2Object(content, PlayerRest.class));
    } 
  }
}
