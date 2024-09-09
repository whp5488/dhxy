package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4323)
public class RespLevelUp extends Message
{
    private int roleId;
    private int level;
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setLevel(final int level) {
        this.level = level;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespLevelUp)) {
            return false;
        }
        RespLevelUp other = (RespLevelUp)o;
        return other.canEqual(this) && this.getRoleId() == other.getRoleId() && this.getLevel() == other.getLevel();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespLevelUp;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRoleId();
        result = result * 59 + this.getLevel();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespLevelUp(roleId=" + this.getRoleId() + ", level=" + this.getLevel() + ")";
    }
    
    public RespLevelUp() {
    }
    
    public RespLevelUp(final int roleId, final int level) {
        this.roleId = roleId;
        this.level = level;
    }
}
