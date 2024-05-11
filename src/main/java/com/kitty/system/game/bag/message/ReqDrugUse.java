
package com.kitty.system.game.bag.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8237)
public class ReqDrugUse extends Message
{
    private int id;
    private int serial;
    
    public int getId() {
        return this.id;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public void setId(final int id) {
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
        if (!(o instanceof ReqDrugUse)) {
            return false;
        }
        final ReqDrugUse other = (ReqDrugUse)o;
        return other.canEqual(this) && this.getId() == other.getId() && this.getSerial() == other.getSerial();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqDrugUse;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getSerial();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqDrugUse(id=" + this.getId() + ", serial=" + this.getSerial() + ")";
    }
}
