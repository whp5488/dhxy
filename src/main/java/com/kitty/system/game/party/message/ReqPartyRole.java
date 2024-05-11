
package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8368)
public class ReqPartyRole extends Message
{
    private int roleId;
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqPartyRole)) {
            return false;
        }
        final ReqPartyRole other = (ReqPartyRole)o;
        return other.canEqual(this) && this.getRoleId() == other.getRoleId();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqPartyRole;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRoleId();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqPartyRole(roleId=" + this.getRoleId() + ")";
    }
}
