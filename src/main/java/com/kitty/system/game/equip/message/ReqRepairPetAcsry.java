
package com.kitty.system.game.equip.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8238)
public class ReqRepairPetAcsry extends Message
{
    private String id;
    private int num;
    
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
        final ReqRepairPetAcsry other = (ReqRepairPetAcsry)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null) {
            if (other$id == null) {
                return this.getNum() == other.getNum();
            }
        }
        else if (this$id.equals(other$id)) {
            return this.getNum() == other.getNum();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqRepairPetAcsry;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getNum();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqRepairPetAcsry(id=" + this.getId() + ", num=" + this.getNum() + ")";
    }
}
