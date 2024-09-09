package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 53360)
public class RespPreviewPet extends Message
{
    private Integer petId;
    private Integer type;
    private String color;
    
    public Integer getPetId() {
        return this.petId;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public String getColor() {
        return this.color;
    }
    
    public void setPetId(final Integer petId) {
        this.petId = petId;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    public void setColor(final String color) {
        this.color = color;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespPreviewPet)) {
            return false;
        }
        RespPreviewPet other = (RespPreviewPet)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$petId = this.getPetId();
        Object other$petId = other.getPetId();
        if (!((this$petId != null) ? this$petId.equals(other$petId) : (other$petId == null))) {
            return false;
        }
        Object this$type = this.getType();
        Object other$type = other.getType();
        if (!((this$type != null) ? this$type.equals(other$type) : (other$type == null))) {
            return false;
        }
        Object this$color = this.getColor();
        Object other$color = other.getColor();
        return (this$color != null) ? this$color.equals(other$color) : (other$color == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespPreviewPet;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $petId = this.getPetId();
        result = result * 59 + (($petId == null) ? 43 : $petId.hashCode());
        Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        Object $color = this.getColor();
        result = result * 59 + (($color == null) ? 43 : $color.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPreviewPet(petId=" + this.getPetId() + ", type=" + this.getType() + ", color=" + this.getColor() + ")";
    }
    
    public RespPreviewPet(final Integer petId, final Integer type, final String color) {
        this.type = Integer.valueOf(0);
        this.color = "";
        this.petId = petId;
        this.type = type;
        this.color = color;
    }
    
    public RespPreviewPet() {
        this.type = Integer.valueOf(0);
        this.color = "";
    }
}
