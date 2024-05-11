
package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 14341)
public class ReqRestAttributeHero extends Message
{
    private Integer id;
    private byte type;
    
    public Integer getId() {
        return this.id;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqRestAttributeHero)) {
            return false;
        }
        final ReqRestAttributeHero other = (ReqRestAttributeHero)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null) {
            if (other$id == null) {
                return this.getType() == other.getType();
            }
        }
        else if (this$id.equals(other$id)) {
            return this.getType() == other.getType();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqRestAttributeHero;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqRestAttributeHero(id=" + this.getId() + ", type=" + this.getType() + ")";
    }
}
