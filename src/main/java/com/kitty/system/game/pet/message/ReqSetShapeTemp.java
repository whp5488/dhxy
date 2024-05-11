
package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32772)
public class ReqSetShapeTemp extends Message
{
    private int id;
    private int type;
    private int index;
    
    public int getId() {
        return this.id;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setIndex(final int index) {
        this.index = index;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqSetShapeTemp)) {
            return false;
        }
        final ReqSetShapeTemp other = (ReqSetShapeTemp)o;
        return other.canEqual(this) && this.getId() == other.getId() && this.getType() == other.getType() && this.getIndex() == other.getIndex();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqSetShapeTemp;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getType();
        result = result * 59 + this.getIndex();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqSetShapeTemp(id=" + this.getId() + ", type=" + this.getType() + ", index=" + this.getIndex() + ")";
    }
}
