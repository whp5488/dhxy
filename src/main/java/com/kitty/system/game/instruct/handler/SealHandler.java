package com.kitty.system.game.instruct.handler;

import com.kitty.system.game.train.TrainService;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.instruct.inter.NcFather;

public class SealHandler implements NcFather
{
    public SealHandler() {
    }
    
    @Override
    public void handle(Role role, String items) {
        if (((ServerService)SpringUtils.getBean(ServerService.class)).getType() == 1) {
            return;
        }
        Integer serial = Integer.valueOf(items);
        if ((int)serial > 0) {
            ((TrainService)SpringUtils.getBean(TrainService.class)).upgrade(role, 0L, 22, items, 0, false, "命令");
        }
        else {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "未找到#<r:255,g:0,b:0>" + items + "#装备信息", false));
        }
        return;
    }
}
