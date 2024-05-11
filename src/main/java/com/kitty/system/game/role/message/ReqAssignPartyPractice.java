
package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 28843)
public class ReqAssignPartyPractice extends Message
{
    private String nutMap;
    
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
        final ReqAssignPartyPractice other = (ReqAssignPartyPractice)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$nutMap = this.getNutMap();
        final Object other$nutMap = other.getNutMap();
        if (this$nutMap == null) {
            if (other$nutMap == null) {
                return true;
            }
        }
        else if (this$nutMap.equals(other$nutMap)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqAssignPartyPractice;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $nutMap = this.getNutMap();
        result = result * 59 + (($nutMap == null) ? 43 : $nutMap.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqAssignPartyPractice(nutMap=" + this.getNutMap() + ")";
    }
}
