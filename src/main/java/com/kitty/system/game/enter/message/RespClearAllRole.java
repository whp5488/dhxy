
package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45157)
public class RespClearAllRole extends Message
{
    private int roleId;
    private int mapId;
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public int getMapId() {
        return this.mapId;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setMapId(final int mapId) {
        this.mapId = mapId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespClearAllRole)) {
            return false;
        }
        final RespClearAllRole other = (RespClearAllRole)o;
        return other.canEqual(this) && this.getRoleId() == other.getRoleId() && this.getMapId() == other.getMapId();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespClearAllRole;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRoleId();
        result = result * 59 + this.getMapId();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespClearAllRole(roleId=" + this.getRoleId() + ", mapId=" + this.getMapId() + ")";
    }
}
