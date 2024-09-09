package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 11800)
public class ReqPartyUnlockFlag extends Message
{
    private short type;
    private short index;
    
    public ReqPartyUnlockFlag() {
    }
    
    public short getType() {
        return this.type;
    }
    
    public short getIndex() {
        return this.index;
    }
    
    public void setType(final short type) {
        this.type = type;
    }
    
    public void setIndex(final short index) {
        this.index = index;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqPartyUnlockFlag)) {
            return false;
        }
        ReqPartyUnlockFlag other = (ReqPartyUnlockFlag)o;
        return other.canEqual(this) && this.getType() == other.getType() && this.getIndex() == other.getIndex();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqPartyUnlockFlag;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        result = result * 59 + this.getIndex();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqPartyUnlockFlag(type=" + (int)this.getType() + ", index=" + (int)this.getIndex() + ")";
    }
}
