package com.kitty.system.game.ranking.message;

import java.util.ArrayList;
import com.kitty.system.game.ranking.pojo.field.WeaponsRankingField;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 42126)
public class RespWeaponsRankingList extends Message
{
    private byte race;
    private byte equipType;
    private List<WeaponsRankingField> weaponsRankingFields;
    
    public RespWeaponsRankingList(byte race, byte equipType, List<WeaponsRankingField> weaponsRankingFields) {
        this.weaponsRankingFields = new ArrayList(100);
        this.race = race;
        this.equipType = equipType;
        this.weaponsRankingFields = weaponsRankingFields;
    }
    
    public byte getRace() {
        return this.race;
    }
    
    public byte getEquipType() {
        return this.equipType;
    }
    
    public List<WeaponsRankingField> getWeaponsRankingFields() {
        return this.weaponsRankingFields;
    }
    
    public void setRace(final byte race) {
        this.race = race;
    }
    
    public void setEquipType(final byte equipType) {
        this.equipType = equipType;
    }
    
    public void setWeaponsRankingFields(final List<WeaponsRankingField> weaponsRankingFields) {
        this.weaponsRankingFields = weaponsRankingFields;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespWeaponsRankingList)) {
            return false;
        }
        RespWeaponsRankingList other = (RespWeaponsRankingList)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getRace() != other.getRace()) {
            return false;
        }
        if (this.getEquipType() != other.getEquipType()) {
            return false;
        }
        Object this$weaponsRankingFields = this.getWeaponsRankingFields();
        Object other$weaponsRankingFields = other.getWeaponsRankingFields();
        return (this$weaponsRankingFields != null) ? this$weaponsRankingFields.equals(other$weaponsRankingFields) : (other$weaponsRankingFields == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespWeaponsRankingList;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRace();
        result = result * 59 + this.getEquipType();
        Object $weaponsRankingFields = this.getWeaponsRankingFields();
        result = result * 59 + (($weaponsRankingFields == null) ? 43 : $weaponsRankingFields.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespWeaponsRankingList(race=" + (int)this.getRace() + ", equipType=" + (int)this.getEquipType() + ", weaponsRankingFields=" + this.getWeaponsRankingFields() + ")";
    }
}
