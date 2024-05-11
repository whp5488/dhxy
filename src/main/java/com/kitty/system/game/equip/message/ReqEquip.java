
package com.kitty.system.game.equip.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4136)
public class ReqEquip extends Message
{
    private String id;
    private int position;
    
    public String getId() {
        return this.id;
    }
    
    public int getPosition() {
        return this.position;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setPosition(final int position) {
        this.position = position;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqEquip)) {
            return false;
        }
        final ReqEquip other = (ReqEquip)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null) {
            if (other$id == null) {
                return this.getPosition() == other.getPosition();
            }
        }
        else if (this$id.equals(other$id)) {
            return this.getPosition() == other.getPosition();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqEquip;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getPosition();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqEquip(id=" + this.getId() + ", position=" + this.getPosition() + ")";
    }
}
