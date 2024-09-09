package com.kitty.system.game.general.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 14338)
public class ReqAssignCorrection extends Message
{
    private Integer petId;
    private String nutMap;
    
    public ReqAssignCorrection() {
    }
    
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
        ReqAssignCorrection other = (ReqAssignCorrection)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$petId = this.getPetId();
        Object other$petId = other.getPetId();
        if (!((this$petId != null) ? this$petId.equals(other$petId) : (other$petId == null))) {
            return false;
        }
        Object this$nutMap = this.getNutMap();
        Object other$nutMap = other.getNutMap();
        return (this$nutMap != null) ? this$nutMap.equals(other$nutMap) : (other$nutMap == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqAssignCorrection;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $petId = this.getPetId();
        result = result * 59 + (($petId == null) ? 43 : $petId.hashCode());
        Object $nutMap = this.getNutMap();
        result = result * 59 + (($nutMap == null) ? 43 : $nutMap.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqAssignCorrection(petId=" + this.getPetId() + ", nutMap=" + this.getNutMap() + ")";
    }
}
