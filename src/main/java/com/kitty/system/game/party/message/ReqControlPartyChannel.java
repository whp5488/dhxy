package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 11834)
public class ReqControlPartyChannel extends Message
{
    private int id;
    private String name;
    private byte type;
    
    public ReqControlPartyChannel() {
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqControlPartyChannel)) {
            return false;
        }
        ReqControlPartyChannel other = (ReqControlPartyChannel)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        return ((this$name != null) ? this$name.equals(other$name) : (other$name == null)) && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqControlPartyChannel;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqControlPartyChannel(id=" + this.getId() + ", name=" + this.getName() + ", type=" + (int)this.getType() + ")";
    }
}
