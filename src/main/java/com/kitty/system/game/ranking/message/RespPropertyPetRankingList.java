
package com.kitty.system.game.ranking.message;

import java.util.ArrayList;
import com.kitty.system.game.ranking.pojo.field.PetPropertyRankingField;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 42123)
public class RespPropertyPetRankingList extends Message
{
    private byte type;
    private byte serverId;
    private List<PetPropertyRankingField> petPropertyRankingFields;
    
    public RespPropertyPetRankingList(final byte type, final byte serverId, final List<PetPropertyRankingField> petPropertyRankingFields) {
        this.petPropertyRankingFields = new ArrayList<PetPropertyRankingField>();
        this.type = type;
        this.serverId = serverId;
        this.petPropertyRankingFields = petPropertyRankingFields;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public byte getServerId() {
        return this.serverId;
    }
    
    public List<PetPropertyRankingField> getPetPropertyRankingFields() {
        return this.petPropertyRankingFields;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setServerId(final byte serverId) {
        this.serverId = serverId;
    }
    
    public void setPetPropertyRankingFields(final List<PetPropertyRankingField> petPropertyRankingFields) {
        this.petPropertyRankingFields = petPropertyRankingFields;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespPropertyPetRankingList)) {
            return false;
        }
        final RespPropertyPetRankingList other = (RespPropertyPetRankingList)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        if (this.getServerId() != other.getServerId()) {
            return false;
        }
        final Object this$petPropertyRankingFields = this.getPetPropertyRankingFields();
        final Object other$petPropertyRankingFields = other.getPetPropertyRankingFields();
        if (this$petPropertyRankingFields == null) {
            if (other$petPropertyRankingFields == null) {
                return true;
            }
        }
        else if (this$petPropertyRankingFields.equals(other$petPropertyRankingFields)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespPropertyPetRankingList;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        result = result * 59 + this.getServerId();
        final Object $petPropertyRankingFields = this.getPetPropertyRankingFields();
        result = result * 59 + (($petPropertyRankingFields == null) ? 43 : $petPropertyRankingFields.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPropertyPetRankingList(type=" + this.getType() + ", serverId=" + this.getServerId() + ", petPropertyRankingFields=" + this.getPetPropertyRankingFields() + ")";
    }
}
