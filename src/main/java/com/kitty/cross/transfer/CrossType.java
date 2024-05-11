
package com.kitty.cross.transfer;

public enum CrossType
{
    SHI_DAO(1), 
    PK(2);
    
    int type;
    
    private CrossType(final int type) {
        this.type = type;
    }
    
    public int getType() {
        return this.type;
    }
}
