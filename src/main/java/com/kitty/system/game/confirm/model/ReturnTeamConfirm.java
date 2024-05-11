
package com.kitty.system.game.confirm.model;

import com.kitty.system.enums.confirm.ConfirmType;

public class ReturnTeamConfirm extends RoleConfirm
{
    @Override
    public ConfirmType getConfirmType() {
        return ConfirmType.RETURN_TEAM;
    }
}
