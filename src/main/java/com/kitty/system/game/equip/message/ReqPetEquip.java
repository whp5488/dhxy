
package com.kitty.system.game.equip.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4137)
public class ReqPetEquip extends Message
{
    private String id;
    private String eId;
    
    public String getId() {
        return this.id;
    }
    
    public String getEId() {
        return this.eId;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setEId(final String eId) {
        this.eId = eId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqPetEquip)) {
            return false;
        }
        final ReqPetEquip other = (ReqPetEquip)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        Label_0065: {
            if (this$id == null) {
                if (other$id == null) {
                    break Label_0065;
                }
            }
            else if (this$id.equals(other$id)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$eId = this.getEId();
        final Object other$eId = other.getEId();
        if (this$eId == null) {
            if (other$eId == null) {
                return true;
            }
        }
        else if (this$eId.equals(other$eId)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqPetEquip;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        final Object $eId = this.getEId();
        result = result * 59 + (($eId == null) ? 43 : $eId.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqPetEquip(id=" + this.getId() + ", eId=" + this.getEId() + ")";
    }
}
