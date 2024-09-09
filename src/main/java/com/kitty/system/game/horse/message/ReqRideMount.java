package com.kitty.system.game.horse.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4382)
public class ReqRideMount extends Message
{
    private String mountId;
    private byte type;
    
    public String getMountId() {
        return this.mountId;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public void setMountId(final String mountId) {
        this.mountId = mountId;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqRideMount)) {
            return false;
        }
        ReqRideMount other = (ReqRideMount)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$mountId = this.getMountId();
        Object other$mountId = other.getMountId();
        return ((this$mountId != null) ? this$mountId.equals(other$mountId) : (other$mountId == null)) && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqRideMount;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $mountId = this.getMountId();
        result = result * 59 + (($mountId == null) ? 43 : $mountId.hashCode());
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqRideMount(mountId=" + this.getMountId() + ", type=" + (int)this.getType() + ")";
    }
    
    public ReqRideMount(final String mountId, final byte type) {
        this.mountId = mountId;
        this.type = type;
    }
    
    public ReqRideMount() {
    }
}
