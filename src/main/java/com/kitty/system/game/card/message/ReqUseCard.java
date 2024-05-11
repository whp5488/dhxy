
package com.kitty.system.game.card.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 250)
public class ReqUseCard extends Message
{
    private String id;
    private int serial;
    
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
        final ReqUseCard other = (ReqUseCard)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null) {
            if (other$id == null) {
                return this.getSerial() == other.getSerial();
            }
        }
        else if (this$id.equals(other$id)) {
            return this.getSerial() == other.getSerial();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqUseCard;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getSerial();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqUseCard(id=" + this.getId() + ", serial=" + this.getSerial() + ")";
    }
}
