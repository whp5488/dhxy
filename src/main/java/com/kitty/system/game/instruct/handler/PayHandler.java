package com.kitty.system.game.instruct.handler;

import com.kitty.common.start.ServerService;
import com.kitty.common.utils.NumberUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.admin.service.AdminService;
import com.kitty.system.game.instruct.inter.NcFather;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.welfare.pool.WelfareDataPool;
import com.kitty.system.init.domain.SysCharge;
import java.util.Map;

public class PayHandler implements NcFather {
  public void handle(Role role, String code) {
    if (((ServerService)SpringUtils.getBean(ServerService.class)).getType() == 1)
      return; 
    if (code == null || !NumberUtil.isNumber(code))
      return; 
    Map<Integer, SysCharge> chargeMap = WelfareDataPool.chargeMap;
    int amount = Integer.valueOf(code).intValue();
    for (SysCharge charge : chargeMap.values()) {
      if (charge.getAmount() == amount)
        ((AdminService)SpringUtils.getBean(AdminService.class)).recharge(role, charge.getSerial()); 
    } 
  }
}
