
package com.kitty.system.game.fightnew.substitution.handler;

import java.util.Iterator;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.substitution.SubstitutionHandler;

public class CsSubstitutionHandler extends SubstitutionHandler
{
    @Override
    public Role getRole(final Fight fight, final boolean isA) {
        if (isA) {
            boolean flag = true;
            for (final FightObject obj : fight.getAllFightObjects()) {
                if (obj.getId().equals(10022956)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                final Role role = SpringUtils.getBean(RoleService.class).getPlayer(10022956);
                return role;
            }
        }
        return null;
    }
}
