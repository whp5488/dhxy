package com.kitty.system.game.fightnew.substitution;

import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.role.domain.Role;

public abstract class SubstitutionHandler {
  public abstract Role getRole(Fight paramFight, boolean paramBoolean);
}
