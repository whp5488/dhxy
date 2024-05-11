
package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 14343)
public class ReqPetCutAccColor extends Message
{
    private Integer id;
    private byte pos;
    private byte serial;
    
    public Integer getId() {
        return this.id;
    }
    
    public byte getPos() {
        return this.pos;
    }
    
    public byte getSerial() {
        return this.serial;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public void setPos(final byte pos) {
        this.pos = pos;
    }
    
    public void setSerial(final byte serial) {
        this.serial = serial;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqPetCutAccColor)) {
            return false;
        }
        final ReqPetCutAccColor other = (ReqPetCutAccColor)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null) {
            if (other$id == null) {
                return this.getPos() == other.getPos() && this.getSerial() == other.getSerial();
            }
        }
        else if (this$id.equals(other$id)) {
            return this.getPos() == other.getPos() && this.getSerial() == other.getSerial();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqPetCutAccColor;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getPos();
        result = result * 59 + this.getSerial();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqPetCutAccColor(id=" + this.getId() + ", pos=" + this.getPos() + ", serial=" + this.getSerial() + ")";
    }
}
