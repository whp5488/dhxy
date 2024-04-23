package com.kitty.system.game.instruct.handler;

import com.kitty.common.start.ServerService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.instruct.inter.NcFather;
import com.kitty.system.game.role.domain.Role;

public class CurrencyHandler implements NcFather {
  public void handle(Role role, String content) {
    if (((ServerService)SpringUtils.getBean(ServerService.class)).getType() == 1)
      return; 
    String[] split = content.split(":");
    if (split.length > 1) {
      Integer type = Integer.valueOf(split[0]);
      Long num = Long.valueOf(split[1]);
      SpringUtils.getRoleService().addCurrency(role, type, num, "命令", true, false);
    } 
  }
}
