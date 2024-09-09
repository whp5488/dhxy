package com.kitty.system.game.instruct.handler;

import com.kitty.system.game.equip.service.EquipService;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.instruct.inter.NcFather;

public class EquipHandler implements NcFather
{
    public EquipHandler() {
    }
    
    @Override
    public void handle(Role role, String items) {
        if (((ServerService)SpringUtils.getBean(ServerService.class)).getType() == 1) {
            return;
        }
        Integer serial = Integer.valueOf(items);
        if ((int)serial > 0) {
            ((EquipService)SpringUtils.getBean(EquipService.class)).upgradeEquip(role, 0, 11, items, 0, false, "命令");
        }
        else {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "未找到#<r:255,g:0,b:0>" + items + "#装备信息", false));
        }
        return;
    }
}
