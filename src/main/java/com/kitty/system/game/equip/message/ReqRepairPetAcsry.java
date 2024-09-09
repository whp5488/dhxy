package com.kitty.system.game.equip.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8238)
public class ReqRepairPetAcsry extends Message
{
    private String id;
    private int num;
    
    public ReqRepairPetAcsry() {
    }
    
    public String getId() {
        return this.id;
    }
    
    public int getNum() {
        return this.num;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setNum(final int num) {
        this.num = num;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqRepairPetAcsry)) {
            return false;
        }
        ReqRepairPetAcsry other = (ReqRepairPetAcsry)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        return ((this$id != null) ? this$id.equals(other$id) : (other$id == null)) && this.getNum() == other.getNum();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqRepairPetAcsry;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getNum();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqRepairPetAcsry(id=" + this.getId() + ", num=" + this.getNum() + ")";
    }
}
