package com.kitty.system.game.fightnew.messages.old;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 222)
public class RespEqUpdateDur extends Message
{
    private String id;
    private int dur;
    
    public String getId() {
        return this.id;
    }
    
    public int getDur() {
        return this.dur;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setDur(final int dur) {
        this.dur = dur;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespEqUpdateDur)) {
            return false;
        }
        RespEqUpdateDur other = (RespEqUpdateDur)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        return ((this$id != null) ? this$id.equals(other$id) : (other$id == null)) && this.getDur() == other.getDur();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespEqUpdateDur;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getDur();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespEqUpdateDur(id=" + this.getId() + ", dur=" + this.getDur() + ")";
    }
    
    public RespEqUpdateDur(final String id, final int dur) {
        this.id = id;
        this.dur = dur;
    }
    
    public RespEqUpdateDur() {
    }
}
