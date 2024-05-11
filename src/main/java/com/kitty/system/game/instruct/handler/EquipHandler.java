
package com.kitty.system.game.instruct.handler;

import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.instruct.inter.NcFather;

public class EquipHandler implements NcFather
{
    @Override
    public void handle(final Role role, final String items) {
        if (SpringUtils.getBean(ServerService.class).getType() == 1) {
            return;
        }
        final Integer serial = Integer.valueOf(items);
        if (serial > 0) {
            SpringUtils.getBean(EquipService.class).upgradeEquip(role, 0, 11, String.valueOf(items), 0, false, "命令");
        }
        else {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "未找到#<r:255,g:0,b:0>" + items + "#装备信息", false));
        }
    }
}
