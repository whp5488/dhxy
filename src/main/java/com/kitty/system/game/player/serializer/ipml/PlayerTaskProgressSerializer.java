
package com.kitty.system.game.player.serializer.ipml;

import com.kitty.system.game.player.bean.TaskProgress;
import org.apache.commons.lang3.StringUtils;
import com.kitty.common.utils.ZipUtil;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.player.serializer.IPlayerPropSerializer;

public class PlayerTaskProgressSerializer implements IPlayerPropSerializer
{
    @Override
    public void serialize(final Role player) {
        if (player.getTaskProgress() != null) {
            final String json = JsonUtils.object2String(player.getTaskProgress());
            player.setTaskProgressJson(ZipUtil.compress(json));
        }
    }
    
    @Override
    public void deserialize(final Role player) {
        final String json = player.getTaskProgressJson();
        if (StringUtils.isNotEmpty((CharSequence)json)) {
            final String content = ZipUtil.uncompress(json);
            player.setTaskProgress(JsonUtils.string2Object(content, TaskProgress.class));
        }
    }
}
