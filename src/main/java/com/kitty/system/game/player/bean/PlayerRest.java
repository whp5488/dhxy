package com.kitty.system.game.player.bean;

import java.util.HashMap;
import java.util.Map;
import com.kitty.system.game.role.domain.vo.PartyPractice;
import java.io.Serializable;

public class PlayerRest implements Serializable
{
    private PartyPractice partyPractice;
    private Map<Integer, Integer> correction;
    private Map<Integer, Integer> party;
    
    public PlayerRest() {
        this.partyPractice = new PartyPractice();
        this.correction = new HashMap();
        this.party = new HashMap();
    }
    
    public PartyPractice getPartyPractice() {
        return this.partyPractice;
    }
    
    public Map<Integer, Integer> getCorrection() {
        return this.correction;
    }
    
    public Map<Integer, Integer> getParty() {
        return this.party;
    }
    
    public void setPartyPractice(final PartyPractice partyPractice) {
        this.partyPractice = partyPractice;
    }
    
    public void setCorrection(final Map<Integer, Integer> correction) {
        this.correction = correction;
    }
    
    public void setParty(final Map<Integer, Integer> party) {
        this.party = party;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayerRest)) {
            return false;
        }
        PlayerRest other = (PlayerRest)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$partyPractice = this.getPartyPractice();
        Object other$partyPractice = other.getPartyPractice();
        if (!((this$partyPractice != null) ? this$partyPractice.equals(other$partyPractice) : (other$partyPractice == null))) {
            return false;
        }
        Object this$correction = this.getCorrection();
        Object other$correction = other.getCorrection();
        if (!((this$correction != null) ? this$correction.equals(other$correction) : (other$correction == null))) {
            return false;
        }
        Object this$party = this.getParty();
        Object other$party = other.getParty();
        return (this$party != null) ? this$party.equals(other$party) : (other$party == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PlayerRest;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $partyPractice = this.getPartyPractice();
        result = result * 59 + (($partyPractice == null) ? 43 : $partyPractice.hashCode());
        Object $correction = this.getCorrection();
        result = result * 59 + (($correction == null) ? 43 : $correction.hashCode());
        Object $party = this.getParty();
        result = result * 59 + (($party == null) ? 43 : $party.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "PlayerRest(partyPractice=" + this.getPartyPractice() + ", correction=" + this.getCorrection() + ", party=" + this.getParty() + ")";
    }
}
