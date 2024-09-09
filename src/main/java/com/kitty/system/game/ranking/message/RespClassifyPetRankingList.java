package com.kitty.system.game.ranking.message;

import java.util.ArrayList;
import com.kitty.system.game.ranking.pojo.field.PetClassifyRankingField;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 42121)
public class RespClassifyPetRankingList extends Message
{
    private int petSerial;
    private byte serverId;
    private List<PetClassifyRankingField> petClassifyRankingFields;
    
    public RespClassifyPetRankingList(int petSerial, byte serverId, List<PetClassifyRankingField> petClassifyRankingFields) {
        this.petClassifyRankingFields = new ArrayList<>();
        this.petSerial = petSerial;
        this.serverId = serverId;
        this.petClassifyRankingFields = petClassifyRankingFields;
    }
    
    public int getPetSerial() {
        return this.petSerial;
    }
    
    public byte getServerId() {
        return this.serverId;
    }
    
    public List<PetClassifyRankingField> getPetClassifyRankingFields() {
        return this.petClassifyRankingFields;
    }
    
    public void setPetSerial(final int petSerial) {
        this.petSerial = petSerial;
    }
    
    public void setServerId(final byte serverId) {
        this.serverId = serverId;
    }
    
    public void setPetClassifyRankingFields(final List<PetClassifyRankingField> petClassifyRankingFields) {
        this.petClassifyRankingFields = petClassifyRankingFields;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespClassifyPetRankingList)) {
            return false;
        }
        RespClassifyPetRankingList other = (RespClassifyPetRankingList)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getPetSerial() != other.getPetSerial()) {
            return false;
        }
        if (this.getServerId() != other.getServerId()) {
            return false;
        }
        Object this$petClassifyRankingFields = this.getPetClassifyRankingFields();
        Object other$petClassifyRankingFields = other.getPetClassifyRankingFields();
        return (this$petClassifyRankingFields != null) ? this$petClassifyRankingFields.equals(other$petClassifyRankingFields) : (other$petClassifyRankingFields == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespClassifyPetRankingList;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getPetSerial();
        result = result * 59 + this.getServerId();
        Object $petClassifyRankingFields = this.getPetClassifyRankingFields();
        result = result * 59 + (($petClassifyRankingFields == null) ? 43 : $petClassifyRankingFields.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespClassifyPetRankingList(petSerial=" + this.getPetSerial() + ", serverId=" + (int)this.getServerId() + ", petClassifyRankingFields=" + this.getPetClassifyRankingFields() + ")";
    }
}
