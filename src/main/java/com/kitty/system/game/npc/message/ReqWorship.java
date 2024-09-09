package com.kitty.system.game.npc.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 6888)
public class ReqWorship extends Message
{
    private int npcId;
    
    public ReqWorship() {
    }
    
    public int getNpcId() {
        return this.npcId;
    }
    
    public void setNpcId(final int npcId) {
        this.npcId = npcId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqWorship)) {
            return false;
        }
        ReqWorship other = (ReqWorship)o;
        return other.canEqual(this) && this.getNpcId() == other.getNpcId();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqWorship;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getNpcId();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqWorship(npcId=" + this.getNpcId() + ")";
    }
}
