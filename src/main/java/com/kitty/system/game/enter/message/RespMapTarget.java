package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 65506)
public class RespMapTarget extends Message
{
    private int mapId;
    private int posX;
    private int posY;
    
    public RespMapTarget() {
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
    
    public void setMapId(final int mapId) {
        this.mapId = mapId;
    }
    
    public void setPosX(final int posX) {
        this.posX = posX;
    }
    
    public void setPosY(final int posY) {
        this.posY = posY;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespMapTarget)) {
            return false;
        }
        RespMapTarget other = (RespMapTarget)o;
        return other.canEqual(this) && this.getMapId() == other.getMapId() && this.getPosX() == other.getPosX() && this.getPosY() == other.getPosY();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespMapTarget;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getMapId();
        result = result * 59 + this.getPosX();
        result = result * 59 + this.getPosY();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespMapTarget(mapId=" + this.getMapId() + ", posX=" + this.getPosX() + ", posY=" + this.getPosY() + ")";
    }
}
