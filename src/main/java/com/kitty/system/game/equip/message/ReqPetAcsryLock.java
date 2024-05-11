
package com.kitty.system.game.equip.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32700)
public class ReqPetAcsryLock extends Message
{
    private String id;
    private byte lock;
    
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
        final ReqPetAcsryLock other = (ReqPetAcsryLock)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null) {
            if (other$id == null) {
                return this.getLock() == other.getLock();
            }
        }
        else if (this$id.equals(other$id)) {
            return this.getLock() == other.getLock();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqPetAcsryLock;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getLock();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqPetAcsryLock(id=" + this.getId() + ", lock=" + this.getLock() + ")";
    }
}
