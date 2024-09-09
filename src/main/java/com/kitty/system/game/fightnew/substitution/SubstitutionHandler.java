package com.kitty.system.game.fightnew.substitution;

import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.bean.Fight;

public abstract class SubstitutionHandler
{
    public SubstitutionHandler() {
    }
    
    public abstract Role getRole(Fight fight, boolean isA);
}
