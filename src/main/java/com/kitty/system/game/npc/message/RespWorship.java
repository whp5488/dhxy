package com.kitty.system.game.npc.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 6889)
public class RespWorship extends Message
{
    private int npcId;
    private String name;
    private String motto;
    
    public int getNpcId() {
        return this.npcId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getMotto() {
        return this.motto;
    }
    
    public void setNpcId(final int npcId) {
        this.npcId = npcId;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setMotto(final String motto) {
        this.motto = motto;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespWorship)) {
            return false;
        }
        RespWorship other = (RespWorship)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getNpcId() != other.getNpcId()) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        Object this$motto = this.getMotto();
        Object other$motto = other.getMotto();
        return (this$motto != null) ? this$motto.equals(other$motto) : (other$motto == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespWorship;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getNpcId();
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        Object $motto = this.getMotto();
        result = result * 59 + (($motto == null) ? 43 : $motto.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespWorship(npcId=" + this.getNpcId() + ", name=" + this.getName() + ", motto=" + this.getMotto() + ")";
    }
    
    public RespWorship(final int npcId, final String name, final String motto) {
        this.npcId = npcId;
        this.name = name;
        this.motto = motto;
    }
    
    public RespWorship() {
    }
}
