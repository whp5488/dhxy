
package com.kitty.system.game.pet.message;

import java.beans.ConstructorProperties;
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
        final RespPreviewPet other = (RespPreviewPet)o;
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
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0102: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0102;
                }
            }
            else if (this$type.equals(other$type)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$color = this.getColor();
        final Object other$color = other.getColor();
        if (this$color == null) {
            if (other$color == null) {
                return true;
            }
        }
        else if (this$color.equals(other$color)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespPreviewPet;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $petId = this.getPetId();
        result = result * 59 + (($petId == null) ? 43 : $petId.hashCode());
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final Object $color = this.getColor();
        result = result * 59 + (($color == null) ? 43 : $color.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPreviewPet(petId=" + this.getPetId() + ", type=" + this.getType() + ", color=" + this.getColor() + ")";
    }
    
    @ConstructorProperties({ "petId", "type", "color" })
    public RespPreviewPet(final Integer petId, final Integer type, final String color) {
        this.type = 0;
        this.color = "";
        this.petId = petId;
        this.type = type;
        this.color = color;
    }
    
    public RespPreviewPet() {
        this.type = 0;
        this.color = "";
    }
}
