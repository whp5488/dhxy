package com.kitty.system.game.instruct.handler;

import com.kitty.system.game.mind.service.MindService;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.instruct.inter.NcFather;

public class MindHandler implements NcFather
{
    public MindHandler() {
    }
    
    @Override
    public void handle(Role role, String string) {
        if (((ServerService)SpringUtils.getBean(ServerService.class)).getType() == 1) {
            return;
        }
        String[] split = string.split(",");
        Integer type = Integer.valueOf(split[0]);
        Integer serial = Integer.valueOf(split[1]);
        Integer operationType = Integer.valueOf(split[2]);
        if ((int)serial > 0) {
            ((MindService)SpringUtils.getBean(MindService.class)).upgrade(role, (int)serial, (int)type, "", (int)operationType, false, "命令");
        }
        else {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "未找到该命令", false));
        }
        return;
    }
}
