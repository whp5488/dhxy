package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32836)
public class ReqPartyRename extends Message
{
    private String name;
    
    public ReqPartyRename() {
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqPartyRename)) {
            return false;
        }
        ReqPartyRename other = (ReqPartyRename)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        return (this$name != null) ? this$name.equals(other$name) : (other$name == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqPartyRename;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqPartyRename(name=" + this.getName() + ")";
    }
}
