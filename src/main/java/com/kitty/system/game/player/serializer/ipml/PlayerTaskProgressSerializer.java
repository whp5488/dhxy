package com.kitty.system.game.player.serializer.ipml;

import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.ZipUtil;
import com.kitty.system.game.player.bean.TaskProgress;
import com.kitty.system.game.player.serializer.IPlayerPropSerializer;
import com.kitty.system.game.role.domain.Role;
import org.apache.commons.lang3.StringUtils;

public class PlayerTaskProgressSerializer implements IPlayerPropSerializer {
  public void serialize(Role player) {
    if (player.getTaskProgress() != null) {
      String json = JsonUtils.object2String(player.getTaskProgress());
      player.setTaskProgressJson(ZipUtil.compress(json));
    } 
  }
  
  public void deserialize(Role player) {
    String json = player.getTaskProgressJson();
    if (StringUtils.isNotEmpty(json)) {
      String content = ZipUtil.uncompress(json);
      player.setTaskProgress((TaskProgress)JsonUtils.string2Object(content, TaskProgress.class));
    } 
  }
}
