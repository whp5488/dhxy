package com.kitty.system.game.player.serializer.ipml;

import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.ZipUtil;
import com.kitty.system.game.player.bean.PlayerExtendBox;
import com.kitty.system.game.player.serializer.IPlayerPropSerializer;
import com.kitty.system.game.role.domain.Role;
import org.apache.commons.lang3.StringUtils;

public class PlayerExtendSerializer implements IPlayerPropSerializer {
  public void serialize(Role player) {
    if (player.getExtendBox() != null) {
      String extendBoxJson = JsonUtils.object2String(player.getExtendBox());
      player.setExtendBoxJson(ZipUtil.compress(extendBoxJson));
    } 
  }
  
  public void deserialize(Role player) {
    String json = player.getExtendBoxJson();
    if (StringUtils.isNotEmpty(json)) {
      String content = ZipUtil.uncompress(json);
      player.setExtendBox((PlayerExtendBox)JsonUtils.string2Object(content, PlayerExtendBox.class));
    } 
  }
}
