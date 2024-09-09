package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 22568)
public class ReqTelePort extends Message
{
    private int mapId;
    private int x;
    private int y;
    private int isTaskWalk;
    
    public ReqTelePort(int mapId, int x, int y) {
        this.isTaskWalk = 0;
        this.mapId = mapId;
        this.x = x;
        this.y = y;
    }
    
    public ReqTelePort(int mapId, int x, int y, int isTaskWalk) {
        this.isTaskWalk = 0;
        this.mapId = mapId;
        this.x = x;
        this.y = y;
        this.isTaskWalk = isTaskWalk;
    }
    
    public ReqTelePort() {
        this.isTaskWalk = 0;
    }
    
    public int getMapId() {
        return this.mapId;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getIsTaskWalk() {
        return this.isTaskWalk;
    }
    
    public void setMapId(final int mapId) {
        this.mapId = mapId;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public void setIsTaskWalk(final int isTaskWalk) {
        this.isTaskWalk = isTaskWalk;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqTelePort)) {
            return false;
        }
        ReqTelePort other = (ReqTelePort)o;
        return other.canEqual(this) && this.getMapId() == other.getMapId() && this.getX() == other.getX() && this.getY() == other.getY() && this.getIsTaskWalk() == other.getIsTaskWalk();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqTelePort;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getMapId();
        result = result * 59 + this.getX();
        result = result * 59 + this.getY();
        result = result * 59 + this.getIsTaskWalk();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqTelePort(mapId=" + this.getMapId() + ", x=" + this.getX() + ", y=" + this.getY() + ", isTaskWalk=" + this.getIsTaskWalk() + ")";
    }
}
