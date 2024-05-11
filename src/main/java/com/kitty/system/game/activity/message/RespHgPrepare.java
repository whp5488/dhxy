
package com.kitty.system.game.activity.message;

import java.util.ArrayList;
import com.kitty.system.game.activity.model.user.hg.HgPrepare;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49169)
public class RespHgPrepare extends Message
{
    private int time;
    private List<HgPrepare> initiator;
    private List<HgPrepare> receiver;
    
    public RespHgPrepare() {
        this.initiator = new ArrayList<HgPrepare>(5);
        this.receiver = new ArrayList<HgPrepare>(5);
    }
    
    public int getTime() {
        return this.time;
    }
    
    public List<HgPrepare> getInitiator() {
        return this.initiator;
    }
    
    public List<HgPrepare> getReceiver() {
        return this.receiver;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    public void setInitiator(final List<HgPrepare> initiator) {
        this.initiator = initiator;
    }
    
    public void setReceiver(final List<HgPrepare> receiver) {
        this.receiver = receiver;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespHgPrepare)) {
            return false;
        }
        final RespHgPrepare other = (RespHgPrepare)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getTime() != other.getTime()) {
            return false;
        }
        final Object this$initiator = this.getInitiator();
        final Object other$initiator = other.getInitiator();
        Label_0078: {
            if (this$initiator == null) {
                if (other$initiator == null) {
                    break Label_0078;
                }
            }
            else if (this$initiator.equals(other$initiator)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$receiver = this.getReceiver();
        final Object other$receiver = other.getReceiver();
        if (this$receiver == null) {
            if (other$receiver == null) {
                return true;
            }
        }
        else if (this$receiver.equals(other$receiver)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespHgPrepare;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getTime();
        final Object $initiator = this.getInitiator();
        result = result * 59 + (($initiator == null) ? 43 : $initiator.hashCode());
        final Object $receiver = this.getReceiver();
        result = result * 59 + (($receiver == null) ? 43 : $receiver.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespHgPrepare(time=" + this.getTime() + ", initiator=" + this.getInitiator() + ", receiver=" + this.getReceiver() + ")";
    }
}
