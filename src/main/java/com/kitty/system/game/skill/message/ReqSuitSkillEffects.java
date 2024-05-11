
package com.kitty.system.game.skill.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8310)
public class ReqSuitSkillEffects extends Message
{
    private int serial;
    private byte type;
    
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
        if (!(o instanceof ReqSuitSkillEffects)) {
            return false;
        }
        final ReqSuitSkillEffects other = (ReqSuitSkillEffects)o;
        return other.canEqual(this) && this.getSerial() == other.getSerial() && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqSuitSkillEffects;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqSuitSkillEffects(serial=" + this.getSerial() + ", type=" + this.getType() + ")";
    }
}
