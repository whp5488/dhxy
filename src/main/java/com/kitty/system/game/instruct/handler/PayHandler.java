package com.kitty.system.game.instruct.handler;

import java.util.Iterator;
import java.util.Map;
import com.kitty.system.admin.service.AdminService;
import com.kitty.system.init.domain.SysCharge;
import com.kitty.system.game.welfare.pool.WelfareDataPool;
import com.kitty.common.utils.NumberUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.instruct.inter.NcFather;

public class PayHandler implements NcFather
{
    public PayHandler() {
    }
    
    @Override
    public void handle(Role role, String code) {
        if (((ServerService)SpringUtils.getBean(ServerService.class)).getType() == 1) {
            return;
        }
        if (code == null || !NumberUtil.isNumber(code)) {
            return;
        }
        Map<Integer, SysCharge> chargeMap = WelfareDataPool.chargeMap;
        int amount = (int)Integer.valueOf(code);
        for (SysCharge charge : chargeMap.values()) {
            if (charge.getAmount() == amount) {
                ((AdminService)SpringUtils.getBean(AdminService.class)).recharge(role, charge.getSerial());
            }
            else {
                continue;
            }
        }
        return;
    }
}
