package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 14341)
public class ReqRestAttributeHero extends Message
{
    private Integer id;
    private byte type;
    
    public ReqRestAttributeHero() {
    }
    
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
        ReqRestAttributeHero other = (ReqRestAttributeHero)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        return ((this$id != null) ? this$id.equals(other$id) : (other$id == null)) && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqRestAttributeHero;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqRestAttributeHero(id=" + this.getId() + ", type=" + (int)this.getType() + ")";
    }
}
