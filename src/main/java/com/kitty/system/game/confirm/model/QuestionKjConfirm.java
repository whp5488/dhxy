
package com.kitty.system.game.confirm.model;

import com.kitty.system.enums.confirm.ConfirmType;

public class QuestionKjConfirm extends RoleConfirm
{
    private byte type;
    
    @Override
    public ConfirmType getConfirmType() {
        return ConfirmType.COUNTRY;
    }
    
    public QuestionKjConfirm(final byte type) {
        this.type = 0;
        this.type = type;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
}
