package com.kitty.system.game.instruct.handler;

import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.init.domain.SysDataAcsryEgg;
import com.kitty.system.init.domain.SysDataAcsry;
import com.kitty.common.utils.RandomUtil;
import java.util.stream.Collectors;
import com.kitty.system.game.equip.pool.EquipDataPool;
import java.util.List;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.instruct.inter.NcFather;

public class SuitHandler implements NcFather
{
    public SuitHandler() {
    }
    
    @Override
    public void handle(Role role, String info) {
        try {
            if (((ServerService)SpringUtils.getBean(ServerService.class)).getType() == 1) {
                return;
            }
            int pos = (int)Integer.valueOf(info);
            List<SysDataAcsry> list = (List)EquipDataPool.acsryMap.values().stream().filter(e -> ((e.getOwnerRole().contains(role.getRace()) && e.getSex() == 0) || e.getSex() == (int)role.getSex()) && e.getPos() == pos).collect(Collectors.toList());
            if (list.size() > 0) {
                int index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(list.size() - 1));
                SysDataAcsry acsry = (SysDataAcsry)list.get(index);
                SysDataAcsryEgg acsryEgg = (SysDataAcsryEgg)ItemDataPool.acsryEggMap.values().stream().filter(e -> e.getHatchType() == acsry.getSerial()).findFirst().orElse(null);
                if (acsryEgg != null) {
                    ((BagService)SpringUtils.getBean(BagService.class)).addGoods(role, acsryEgg.getSerial(), 1, 0, true, true, "SuitHandler");
                }
            }
        }
        catch (Exception e) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "未找到道具信息", false));
        }
    }
}
