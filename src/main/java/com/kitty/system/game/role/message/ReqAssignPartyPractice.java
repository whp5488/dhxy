package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 28843)
public class ReqAssignPartyPractice extends Message
{
    private String nutMap;
    
    public ReqAssignPartyPractice() {
    }
    
    public String getNutMap() {
        return this.nutMap;
    }
    
    public void setNutMap(final String nutMap) {
        this.nutMap = nutMap;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqAssignPartyPractice)) {
            return false;
        }
        ReqAssignPartyPractice other = (ReqAssignPartyPractice)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$nutMap = this.getNutMap();
        Object other$nutMap = other.getNutMap();
        return (this$nutMap != null) ? this$nutMap.equals(other$nutMap) : (other$nutMap == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqAssignPartyPractice;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $nutMap = this.getNutMap();
        result = result * 59 + (($nutMap == null) ? 43 : $nutMap.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqAssignPartyPractice(nutMap=" + this.getNutMap() + ")";
    }
}
