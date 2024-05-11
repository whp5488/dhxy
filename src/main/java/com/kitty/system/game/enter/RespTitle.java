
package com.kitty.system.game.enter;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 61671)
public class RespTitle extends Message
{
    private int roleId;
    private byte param;
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public byte getParam() {
        return this.param;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setParam(final byte param) {
        this.param = param;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespTitle)) {
            return false;
        }
        final RespTitle other = (RespTitle)o;
        return other.canEqual(this) && this.getRoleId() == other.getRoleId() && this.getParam() == other.getParam();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespTitle;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRoleId();
        result = result * 59 + this.getParam();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespTitle(roleId=" + this.getRoleId() + ", param=" + this.getParam() + ")";
    }
}
