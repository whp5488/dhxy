
package com.kitty.system.game.general.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 14338)
public class ReqAssignCorrection extends Message
{
    private Integer petId;
    private String nutMap;
    
    public Integer getPetId() {
        return this.petId;
    }
    
    public String getNutMap() {
        return this.nutMap;
    }
    
    public void setPetId(final Integer petId) {
        this.petId = petId;
    }
    
    public void setNutMap(final String nutMap) {
        this.nutMap = nutMap;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqAssignCorrection)) {
            return false;
        }
        final ReqAssignCorrection other = (ReqAssignCorrection)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$petId = this.getPetId();
        final Object other$petId = other.getPetId();
        Label_0065: {
            if (this$petId == null) {
                if (other$petId == null) {
                    break Label_0065;
                }
            }
            else if (this$petId.equals(other$petId)) {
                break Label_0065;
            }
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
        return other instanceof ReqAssignCorrection;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $petId = this.getPetId();
        result = result * 59 + (($petId == null) ? 43 : $petId.hashCode());
        final Object $nutMap = this.getNutMap();
        result = result * 59 + (($nutMap == null) ? 43 : $nutMap.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqAssignCorrection(petId=" + this.getPetId() + ", nutMap=" + this.getNutMap() + ")";
    }
}
