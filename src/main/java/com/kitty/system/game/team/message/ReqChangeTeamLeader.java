package com.kitty.system.game.team.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 30)
public class ReqChangeTeamLeader extends Message
{
    private int roleId;
    
    public ReqChangeTeamLeader() {
    }
    
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
        if (!(o instanceof ReqChangeTeamLeader)) {
            return false;
        }
        ReqChangeTeamLeader other = (ReqChangeTeamLeader)o;
        return other.canEqual(this) && this.getRoleId() == other.getRoleId();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqChangeTeamLeader;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRoleId();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqChangeTeamLeader(roleId=" + this.getRoleId() + ")";
    }
}
