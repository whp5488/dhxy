package com.kitty.system.game.player.serializer.ipml;

import com.kitty.system.game.task.domain.RoleTask;
import org.apache.commons.lang3.StringUtils;
import com.kitty.common.utils.ZipUtil;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.player.serializer.IPlayerPropSerializer;

public class PlayerTaskSerializer implements IPlayerPropSerializer
{
    public PlayerTaskSerializer() {
    }
    
    @Override
    public void serialize(Role player) {
        if (player.getTask() != null) {
            String json = JsonUtils.object2String(player.getTask());
            player.setTaskJson(ZipUtil.compress(json));
        }
    }
    
    @Override
    public void deserialize(Role player) {
        String json = player.getTaskJson();
        if (StringUtils.isNotEmpty(json)) {
            String content = ZipUtil.uncompress(json);
            player.setTask((RoleTask)JsonUtils.string2Object(content, RoleTask.class));
        }
    }
}
