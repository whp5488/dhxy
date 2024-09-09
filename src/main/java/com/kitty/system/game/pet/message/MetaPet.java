package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 53252)
public class MetaPet extends Message
{
    private Integer type;
    
    public MetaPet() {
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MetaPet)) {
            return false;
        }
        MetaPet other = (MetaPet)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$type = this.getType();
        Object other$type = other.getType();
        return (this$type != null) ? this$type.equals(other$type) : (other$type == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof MetaPet;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "MetaPet(type=" + this.getType() + ")";
    }
}
