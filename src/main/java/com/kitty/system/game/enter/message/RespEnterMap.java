
package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 65505)
public class RespEnterMap extends Message
{
    private int mapId;
    private int posX;
    private int posY;
    private int dir;
    private byte safeZone;
    private byte isTaskWalk;
    
    public RespEnterMap() {
        this.isTaskWalk = 0;
    }
    
    public int getMapId() {
        return this.mapId;
    }
    
    public int getPosX() {
        return this.posX;
    }
    
    public int getPosY() {
        return this.posY;
    }
    
    public int getDir() {
        return this.dir;
    }
    
    public byte getSafeZone() {
        return this.safeZone;
    }
    
    public byte getIsTaskWalk() {
        return this.isTaskWalk;
    }
    
    public void setMapId(final int mapId) {
        this.mapId = mapId;
    }
    
    public void setPosX(final int posX) {
        this.posX = posX;
    }
    
    public void setPosY(final int posY) {
        this.posY = posY;
    }
    
    public void setDir(final int dir) {
        this.dir = dir;
    }
    
    public void setSafeZone(final byte safeZone) {
        this.safeZone = safeZone;
    }
    
    public void setIsTaskWalk(final byte isTaskWalk) {
        this.isTaskWalk = isTaskWalk;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespEnterMap)) {
            return false;
        }
        final RespEnterMap other = (RespEnterMap)o;
        return other.canEqual(this) && this.getMapId() == other.getMapId() && this.getPosX() == other.getPosX() && this.getPosY() == other.getPosY() && this.getDir() == other.getDir() && this.getSafeZone() == other.getSafeZone() && this.getIsTaskWalk() == other.getIsTaskWalk();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespEnterMap;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getMapId();
        result = result * 59 + this.getPosX();
        result = result * 59 + this.getPosY();
        result = result * 59 + this.getDir();
        result = result * 59 + this.getSafeZone();
        result = result * 59 + this.getIsTaskWalk();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespEnterMap(mapId=" + this.getMapId() + ", posX=" + this.getPosX() + ", posY=" + this.getPosY() + ", dir=" + this.getDir() + ", safeZone=" + this.getSafeZone() + ", isTaskWalk=" + this.getIsTaskWalk() + ")";
    }
}
