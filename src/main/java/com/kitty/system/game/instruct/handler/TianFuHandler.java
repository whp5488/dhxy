package com.kitty.system.game.instruct.handler;

import com.kitty.common.start.ServerService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.instruct.inter.NcFather;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.tianfu.service.RoleTianFuService;

public class TianFuHandler implements NcFather {
  public void handle(Role role, String info) {
    if (((ServerService)SpringUtils.getBean(ServerService.class)).getType() == 1)
      return; 
    String[] split = info.split(",");
    Integer serial = Integer.valueOf(split[0]);
    Short num = Short.valueOf(split[1]);
    if (serial == null || num == null)
      return; 
    if (9997 != serial.intValue())
      num = Short.valueOf((short)1); 
    RoleTianFuService bean = (RoleTianFuService)SpringUtils.getBean(RoleTianFuService.class);
    bean.putTianFu(role, serial.intValue(), num.shortValue());
  }
}
