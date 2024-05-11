
package com.kitty.system.game.enter;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 16431)
public class RespPositionMove extends Message
{
    private int x;
    private int y;
    private int roleId;
    
    public RespPositionMove() {
        this.x = 0;
        this.y = 0;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespPositionMove)) {
            return false;
        }
        final RespPositionMove other = (RespPositionMove)o;
        return other.canEqual(this) && this.getX() == other.getX() && this.getY() == other.getY() && this.getRoleId() == other.getRoleId();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespPositionMove;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getX();
        result = result * 59 + this.getY();
        result = result * 59 + this.getRoleId();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPositionMove(x=" + this.getX() + ", y=" + this.getY() + ", roleId=" + this.getRoleId() + ")";
    }
}
