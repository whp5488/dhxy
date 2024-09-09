package com.kitty.system.game.ranking.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4225)
public class ReqWeaponsRanking extends Message
{
    private byte race;
    private byte equipType;
    
    public ReqWeaponsRanking() {
    }
    
    public byte getRace() {
        return this.race;
    }
    
    public byte getEquipType() {
        return this.equipType;
    }
    
    public void setRace(final byte race) {
        this.race = race;
    }
    
    public void setEquipType(final byte equipType) {
        this.equipType = equipType;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqWeaponsRanking)) {
            return false;
        }
        ReqWeaponsRanking other = (ReqWeaponsRanking)o;
        return other.canEqual(this) && this.getRace() == other.getRace() && this.getEquipType() == other.getEquipType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqWeaponsRanking;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRace();
        result = result * 59 + this.getEquipType();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqWeaponsRanking(race=" + (int)this.getRace() + ", equipType=" + (int)this.getEquipType() + ")";
    }
}
