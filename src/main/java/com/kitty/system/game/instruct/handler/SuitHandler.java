
package com.kitty.system.game.instruct.handler;

import com.kitty.common.start.ServerService;
import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.instruct.inter.NcFather;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.init.domain.SysDataAcsry;
import com.kitty.system.init.domain.SysDataAcsryEgg;
import com.kitty.system.init.pool.ItemDataPool;

import java.util.List;
import java.util.stream.Collectors;

public class SuitHandler implements NcFather
{
    @Override
    public void handle(final Role role, final String info) {
        try {
            if (SpringUtils.getBean(ServerService.class).getType() == 1) {
                return;
            }
            final int pos = Integer.valueOf(info);
            final List<SysDataAcsry> list = EquipDataPool.acsryMap.values().stream().filter(e -> ((e.getOwnerRole().contains(role.getRace()) && e.getSex() == 0) || e.getSex() == role.getSex()) && e.getPos() == pos).collect(Collectors.toList());
            if (list.size() > 0) {
                final int index = RandomUtil.round(0, list.size() - 1);
                final SysDataAcsry acsry = list.get(index);
                final SysDataAcsryEgg acsryEgg = ItemDataPool.acsryEggMap.values().stream().filter(e -> e.getHatchType() == acsry.getSerial()).findFirst().orElse(null);
                if (acsryEgg != null) {
                    SpringUtils.getBean(BagService.class).addGoods(role, acsryEgg.getSerial(), 1, 0, true, true, "SuitHandler");
                }
            }
        }
        catch (final Exception e) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "未找到道具信息", false));
        }
    }
}
