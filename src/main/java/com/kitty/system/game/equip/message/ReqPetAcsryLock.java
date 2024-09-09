package com.kitty.system.game.equip.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32700)
public class ReqPetAcsryLock extends Message
{
    private String id;
    private byte lock;
    
    public ReqPetAcsryLock() {
    }
    
    public String getId() {
        return this.id;
    }
    
    public byte getLock() {
        return this.lock;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setLock(final byte lock) {
        this.lock = lock;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqPetAcsryLock)) {
            return false;
        }
        ReqPetAcsryLock other = (ReqPetAcsryLock)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        return ((this$id != null) ? this$id.equals(other$id) : (other$id == null)) && this.getLock() == other.getLock();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqPetAcsryLock;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getLock();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqPetAcsryLock(id=" + this.getId() + ", lock=" + (int)this.getLock() + ")";
    }
}
