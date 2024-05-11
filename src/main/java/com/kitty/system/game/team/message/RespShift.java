
package com.kitty.system.game.team.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4249)
public class RespShift extends Message
{
    private int id;
    private int x;
    private int y;
    private int mapid;
    
    public int getId() {
        return this.id;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getMapid() {
        return this.mapid;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public void setMapid(final int mapid) {
        this.mapid = mapid;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespShift)) {
            return false;
        }
        final RespShift other = (RespShift)o;
        return other.canEqual(this) && this.getId() == other.getId() && this.getX() == other.getX() && this.getY() == other.getY() && this.getMapid() == other.getMapid();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespShift;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getX();
        result = result * 59 + this.getY();
        result = result * 59 + this.getMapid();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespShift(id=" + this.getId() + ", x=" + this.getX() + ", y=" + this.getY() + ", mapid=" + this.getMapid() + ")";
    }
}
