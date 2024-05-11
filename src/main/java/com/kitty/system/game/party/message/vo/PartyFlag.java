
package com.kitty.system.game.party.message.vo;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class PartyFlag implements Serializable
{
    private short index;
    private byte state;
    
    public short getIndex() {
        return this.index;
    }
    
    public byte getState() {
        return this.state;
    }
    
    public void setIndex(final short index) {
        this.index = index;
    }
    
    public void setState(final byte state) {
        this.state = state;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PartyFlag)) {
            return false;
        }
        final PartyFlag other = (PartyFlag)o;
        return other.canEqual(this) && this.getIndex() == other.getIndex() && this.getState() == other.getState();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PartyFlag;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getIndex();
        result = result * 59 + this.getState();
        return result;
    }
    
    @Override
    public String toString() {
        return "PartyFlag(index=" + this.getIndex() + ", state=" + this.getState() + ")";
    }
    
    public PartyFlag() {
    }
    
    @ConstructorProperties({ "index", "state" })
    public PartyFlag(final short index, final byte state) {
        this.index = index;
        this.state = state;
    }
}
