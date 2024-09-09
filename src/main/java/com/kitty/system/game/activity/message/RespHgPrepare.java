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
        this.initiator = new ArrayList(5);
        this.receiver = new ArrayList(5);
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
        RespHgPrepare other = (RespHgPrepare)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getTime() != other.getTime()) {
            return false;
        }
        Object this$initiator = this.getInitiator();
        Object other$initiator = other.getInitiator();
        if (!((this$initiator != null) ? this$initiator.equals(other$initiator) : (other$initiator == null))) {
            return false;
        }
        Object this$receiver = this.getReceiver();
        Object other$receiver = other.getReceiver();
        return (this$receiver != null) ? this$receiver.equals(other$receiver) : (other$receiver == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespHgPrepare;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getTime();
        Object $initiator = this.getInitiator();
        result = result * 59 + (($initiator == null) ? 43 : $initiator.hashCode());
        Object $receiver = this.getReceiver();
        result = result * 59 + (($receiver == null) ? 43 : $receiver.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespHgPrepare(time=" + this.getTime() + ", initiator=" + this.getInitiator() + ", receiver=" + this.getReceiver() + ")";
    }
}
