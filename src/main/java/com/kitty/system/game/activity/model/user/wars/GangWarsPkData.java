
package com.kitty.system.game.activity.model.user.wars;

import java.io.Serializable;

public class GangWarsPkData implements Serializable
{
    private byte state;
    private byte position;
    private int rebirth;
    
    public GangWarsPkData() {
        this.state = 0;
        this.position = 0;
        this.rebirth = 0;
    }
    
    public void updateState(final byte state) {
        this.state = state;
    }
    
    public void updatePosition(final byte position) {
        this.position = position;
    }
    
    public byte getState() {
        return this.state;
    }
    
    public byte getPosition() {
        return this.position;
    }
    
    public int getRebirth() {
        return this.rebirth;
    }
    
    public void setState(final byte state) {
        this.state = state;
    }
    
    public void setPosition(final byte position) {
        this.position = position;
    }
    
    public void setRebirth(final int rebirth) {
        this.rebirth = rebirth;
    }
    
    @Override
    public String toString() {
        return "GangWarsPkData(state=" + this.getState() + ", position=" + this.getPosition() + ", rebirth=" + this.getRebirth() + ")";
    }
}
