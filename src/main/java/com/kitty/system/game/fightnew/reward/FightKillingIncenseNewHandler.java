package com.kitty.system.game.fightnew.reward;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component
public class FightKillingIncenseNewHandler extends FightRewardHandler {
  public void sendReward(Fight fight, Role role, Pet pet) {}
  
  public void fightFailed(Fight fight, Role role) {
    if (role.getExp() <= 0L)
      return; 
    long roleExp = (long)(role.getExp() * 0.5D);
    if (roleExp <= 0L)
      roleExp = role.getExp(); 
    SpringUtils.getRoleService().addExp(role, -roleExp, null, true);
  }
  
  public void flee(Fight fight, Role role) {}
  
  public int getType() {
    return FIGHT_TYPE.FORCE_PK.getValue();
  }
  
  public void init() {
    putHndlers(this);
  }
}
