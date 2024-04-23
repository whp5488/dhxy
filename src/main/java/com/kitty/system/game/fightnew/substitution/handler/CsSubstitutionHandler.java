package com.kitty.system.game.fightnew.substitution.handler;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.substitution.SubstitutionHandler;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;

public class CsSubstitutionHandler extends SubstitutionHandler {
  public Role getRole(Fight fight, boolean isA) {
    if (isA) {
      boolean flag = true;
      for (FightObject obj : fight.getAllFightObjects()) {
        if (obj.getId().equals(Integer.valueOf(10022956))) {
          flag = false;
          break;
        } 
      } 
      if (flag) {
        Role role = ((RoleService)SpringUtils.getBean(RoleService.class)).getPlayer(10022956);
        return role;
      } 
    } 
    return null;
  }
}
