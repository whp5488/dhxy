
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
    @Override
    public void handle(final Role role, final String code) {
        if (SpringUtils.getBean(ServerService.class).getType() == 1) {
            return;
        }
        if (code == null || !NumberUtil.isNumber(code)) {
            return;
        }
        final Map<Integer, SysCharge> chargeMap = WelfareDataPool.chargeMap;
        final int amount = Integer.valueOf(code);
        for (final SysCharge charge : chargeMap.values()) {
            if (charge.getAmount() == amount) {
                SpringUtils.getBean(AdminService.class).recharge(role, charge.getSerial());
            }
        }
    }
}
