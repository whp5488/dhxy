package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 14343)
public class ReqPetCutAccColor extends Message
{
    private Integer id;
    private byte pos;
    private byte serial;
    
    public ReqPetCutAccColor() {
    }
    
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
        ReqPetCutAccColor other = (ReqPetCutAccColor)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        return ((this$id != null) ? this$id.equals(other$id) : (other$id == null)) && this.getPos() == other.getPos() && this.getSerial() == other.getSerial();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqPetCutAccColor;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getPos();
        result = result * 59 + this.getSerial();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqPetCutAccColor(id=" + this.getId() + ", pos=" + (int)this.getPos() + ", serial=" + (int)this.getSerial() + ")";
    }
}
