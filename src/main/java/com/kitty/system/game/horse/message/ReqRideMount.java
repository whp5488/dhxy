
package com.kitty.system.game.horse.message;

import java.beans.ConstructorProperties;
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
        final ReqRideMount other = (ReqRideMount)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$mountId = this.getMountId();
        final Object other$mountId = other.getMountId();
        if (this$mountId == null) {
            if (other$mountId == null) {
                return this.getType() == other.getType();
            }
        }
        else if (this$mountId.equals(other$mountId)) {
            return this.getType() == other.getType();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqRideMount;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $mountId = this.getMountId();
        result = result * 59 + (($mountId == null) ? 43 : $mountId.hashCode());
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqRideMount(mountId=" + this.getMountId() + ", type=" + this.getType() + ")";
    }
    
    @ConstructorProperties({ "mountId", "type" })
    public ReqRideMount(final String mountId, final byte type) {
        this.mountId = mountId;
        this.type = type;
    }
    
    public ReqRideMount() {
    }
}
