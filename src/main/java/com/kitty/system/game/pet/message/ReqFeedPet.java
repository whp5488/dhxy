package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8270)
public class ReqFeedPet extends Message
{
    private int petId;
    private int param;
    private String type;
    private int num;
    
    public ReqFeedPet() {
    }
    
    public int getPetId() {
        return this.petId;
    }
    
    public int getParam() {
        return this.param;
    }
    
    public String getType() {
        return this.type;
    }
    
    public int getNum() {
        return this.num;
    }
    
    public void setPetId(final int petId) {
        this.petId = petId;
    }
    
    public void setParam(final int param) {
        this.param = param;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public void setNum(final int num) {
        this.num = num;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqFeedPet)) {
            return false;
        }
        ReqFeedPet other = (ReqFeedPet)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getPetId() != other.getPetId()) {
            return false;
        }
        if (this.getParam() != other.getParam()) {
            return false;
        }
        Object this$type = this.getType();
        Object other$type = other.getType();
        return ((this$type != null) ? this$type.equals(other$type) : (other$type == null)) && this.getNum() == other.getNum();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqFeedPet;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getPetId();
        result = result * 59 + this.getParam();
        Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        result = result * 59 + this.getNum();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqFeedPet(petId=" + this.getPetId() + ", param=" + this.getParam() + ", type=" + this.getType() + ", num=" + this.getNum() + ")";
    }
}
