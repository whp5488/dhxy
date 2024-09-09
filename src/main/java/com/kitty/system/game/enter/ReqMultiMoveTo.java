package com.kitty.system.game.enter;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 61634)
public class ReqMultiMoveTo extends Message
{
    private int roleId;
    private int mapId;
    private short x;
    private short y;
    private int send_time;
    
    public ReqMultiMoveTo() {
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public int getMapId() {
        return this.mapId;
    }
    
    public short getX() {
        return this.x;
    }
    
    public short getY() {
        return this.y;
    }
    
    public int getSend_time() {
        return this.send_time;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setMapId(final int mapId) {
        this.mapId = mapId;
    }
    
    public void setX(final short x) {
        this.x = x;
    }
    
    public void setY(final short y) {
        this.y = y;
    }
    
    public void setSend_time(final int send_time) {
        this.send_time = send_time;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqMultiMoveTo)) {
            return false;
        }
        ReqMultiMoveTo other = (ReqMultiMoveTo)o;
        return other.canEqual(this) && this.getRoleId() == other.getRoleId() && this.getMapId() == other.getMapId() && this.getX() == other.getX() && this.getY() == other.getY() && this.getSend_time() == other.getSend_time();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqMultiMoveTo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRoleId();
        result = result * 59 + this.getMapId();
        result = result * 59 + this.getX();
        result = result * 59 + this.getY();
        result = result * 59 + this.getSend_time();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqMultiMoveTo(roleId=" + this.getRoleId() + ", mapId=" + this.getMapId() + ", x=" + (int)this.getX() + ", y=" + (int)this.getY() + ", send_time=" + this.getSend_time() + ")";
    }
}
