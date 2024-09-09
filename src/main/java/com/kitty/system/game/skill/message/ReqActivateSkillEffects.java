package com.kitty.system.game.skill.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8309)
public class ReqActivateSkillEffects extends Message
{
    private int serial;
    private byte type;
    
    public ReqActivateSkillEffects() {
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqActivateSkillEffects)) {
            return false;
        }
        ReqActivateSkillEffects other = (ReqActivateSkillEffects)o;
        return other.canEqual(this) && this.getSerial() == other.getSerial() && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqActivateSkillEffects;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqActivateSkillEffects(serial=" + this.getSerial() + ", type=" + (int)this.getType() + ")";
    }
}
