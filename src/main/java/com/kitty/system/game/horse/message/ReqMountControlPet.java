
package com.kitty.system.game.horse.message;

import java.beans.ConstructorProperties;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 41044)
public class ReqMountControlPet extends Message
{
    private String mountId;
    private int petId;
    
    public String getMountId() {
        return this.mountId;
    }
    
    public int getPetId() {
        return this.petId;
    }
    
    public void setMountId(final String mountId) {
        this.mountId = mountId;
    }
    
    public void setPetId(final int petId) {
        this.petId = petId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqMountControlPet)) {
            return false;
        }
        final ReqMountControlPet other = (ReqMountControlPet)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$mountId = this.getMountId();
        final Object other$mountId = other.getMountId();
        if (this$mountId == null) {
            if (other$mountId == null) {
                return this.getPetId() == other.getPetId();
            }
        }
        else if (this$mountId.equals(other$mountId)) {
            return this.getPetId() == other.getPetId();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqMountControlPet;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $mountId = this.getMountId();
        result = result * 59 + (($mountId == null) ? 43 : $mountId.hashCode());
        result = result * 59 + this.getPetId();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqMountControlPet(mountId=" + this.getMountId() + ", petId=" + this.getPetId() + ")";
    }
    
    @ConstructorProperties({ "mountId", "petId" })
    public ReqMountControlPet(final String mountId, final int petId) {
        this.mountId = mountId;
        this.petId = petId;
    }
    
    public ReqMountControlPet() {
    }
}
