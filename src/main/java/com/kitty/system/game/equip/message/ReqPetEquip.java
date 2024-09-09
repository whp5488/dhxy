package com.kitty.system.game.equip.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4137)
public class ReqPetEquip extends Message
{
    private String id;
    private String eId;
    
    public ReqPetEquip() {
    }
    
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
        ReqPetEquip other = (ReqPetEquip)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
            return false;
        }
        Object this$eId = this.getEId();
        Object other$eId = other.getEId();
        return (this$eId != null) ? this$eId.equals(other$eId) : (other$eId == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqPetEquip;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $eId = this.getEId();
        result = result * 59 + (($eId == null) ? 43 : $eId.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqPetEquip(id=" + this.getId() + ", eId=" + this.getEId() + ")";
    }
}
