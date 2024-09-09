package com.kitty.system.game.skill.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8311)
public class ReqSkillEffects extends Message
{
    private int serial;
    
    public ReqSkillEffects() {
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqSkillEffects)) {
            return false;
        }
        ReqSkillEffects other = (ReqSkillEffects)o;
        return other.canEqual(this) && this.getSerial() == other.getSerial();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqSkillEffects;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqSkillEffects(serial=" + this.getSerial() + ")";
    }
}
