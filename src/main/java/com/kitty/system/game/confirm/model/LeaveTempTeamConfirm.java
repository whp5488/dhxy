
package com.kitty.system.game.confirm.model;

import com.kitty.system.enums.confirm.ConfirmType;

public class LeaveTempTeamConfirm extends RoleConfirm
{
    private int type;
    
    @Override
    public ConfirmType getConfirmType() {
        return ConfirmType.LEAVE_TEMP_TEAM;
    }
    
    public LeaveTempTeamConfirm(final int type) {
        this.type = -1;
        this.type = type;
    }
    
    public int getType() {
        return this.type;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
}
