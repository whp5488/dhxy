
package com.kitty.system.game.skill.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8308)
public class ReqLearnSkill extends Message
{
    private int id;
    private int serial;
    private int learnLevel;
    
    public int getId() {
        return this.id;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getLearnLevel() {
        return this.learnLevel;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setLearnLevel(final int learnLevel) {
        this.learnLevel = learnLevel;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqLearnSkill)) {
            return false;
        }
        final ReqLearnSkill other = (ReqLearnSkill)o;
        return other.canEqual(this) && this.getId() == other.getId() && this.getSerial() == other.getSerial() && this.getLearnLevel() == other.getLearnLevel();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqLearnSkill;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getLearnLevel();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqLearnSkill(id=" + this.getId() + ", serial=" + this.getSerial() + ", learnLevel=" + this.getLearnLevel() + ")";
    }
}
