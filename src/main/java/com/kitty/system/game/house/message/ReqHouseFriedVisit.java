package com.kitty.system.game.house.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20605)
public class ReqHouseFriedVisit extends Message
{
    private int id;
    
    public ReqHouseFriedVisit() {
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqHouseFriedVisit)) {
            return false;
        }
        ReqHouseFriedVisit other = (ReqHouseFriedVisit)o;
        return other.canEqual(this) && this.getId() == other.getId();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqHouseFriedVisit;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqHouseFriedVisit(id=" + this.getId() + ")";
    }
}
