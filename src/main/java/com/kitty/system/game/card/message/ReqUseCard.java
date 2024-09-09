package com.kitty.system.game.card.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 250)
public class ReqUseCard extends Message
{
    private String id;
    private int serial;
    
    public ReqUseCard() {
    }
    
    public String getId() {
        return this.id;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqUseCard)) {
            return false;
        }
        ReqUseCard other = (ReqUseCard)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        return ((this$id != null) ? this$id.equals(other$id) : (other$id == null)) && this.getSerial() == other.getSerial();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqUseCard;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getSerial();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqUseCard(id=" + this.getId() + ", serial=" + this.getSerial() + ")";
    }
}
