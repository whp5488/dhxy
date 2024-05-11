
package com.kitty.system.game.confirm.model;

import com.kitty.system.enums.confirm.ConfirmType;

public class CreatePartyConfirm extends RoleConfirm
{
    private String partyName;
    
    @Override
    public ConfirmType getConfirmType() {
        return ConfirmType.CREATE_PARTY;
    }
    
    public CreatePartyConfirm(final String partyName) {
        this.partyName = partyName;
    }
    
    public String getPartyName() {
        return this.partyName;
    }
}
