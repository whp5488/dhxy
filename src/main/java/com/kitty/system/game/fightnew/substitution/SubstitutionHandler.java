
package com.kitty.system.game.fightnew.substitution;

import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.bean.Fight;

public abstract class SubstitutionHandler
{
    public abstract Role getRole(final Fight p0, final boolean p1);
}
