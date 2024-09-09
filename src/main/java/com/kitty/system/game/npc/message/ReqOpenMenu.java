package com.kitty.system.game.npc.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 3520)
public class ReqOpenMenu extends Message
{
    private int npcId;
    private int type;
    
    public ReqOpenMenu() {
    }
    
    public int getNpcId() {
        return this.npcId;
    }
    
    public int getType() {
        return this.type;
    }
    
    public void setNpcId(final int npcId) {
        this.npcId = npcId;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqOpenMenu)) {
            return false;
        }
        ReqOpenMenu other = (ReqOpenMenu)o;
        return other.canEqual(this) && this.getNpcId() == other.getNpcId() && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqOpenMenu;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getNpcId();
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqOpenMenu(npcId=" + this.getNpcId() + ", type=" + this.getType() + ")";
    }
}
