
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
        this.correction = new HashMap<Integer, Integer>();
        this.party = new HashMap<Integer, Integer>();
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
        final PlayerRest other = (PlayerRest)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$partyPractice = this.getPartyPractice();
        final Object other$partyPractice = other.getPartyPractice();
        Label_0065: {
            if (this$partyPractice == null) {
                if (other$partyPractice == null) {
                    break Label_0065;
                }
            }
            else if (this$partyPractice.equals(other$partyPractice)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$correction = this.getCorrection();
        final Object other$correction = other.getCorrection();
        Label_0102: {
            if (this$correction == null) {
                if (other$correction == null) {
                    break Label_0102;
                }
            }
            else if (this$correction.equals(other$correction)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$party = this.getParty();
        final Object other$party = other.getParty();
        if (this$party == null) {
            if (other$party == null) {
                return true;
            }
        }
        else if (this$party.equals(other$party)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PlayerRest;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $partyPractice = this.getPartyPractice();
        result = result * 59 + (($partyPractice == null) ? 43 : $partyPractice.hashCode());
        final Object $correction = this.getCorrection();
        result = result * 59 + (($correction == null) ? 43 : $correction.hashCode());
        final Object $party = this.getParty();
        result = result * 59 + (($party == null) ? 43 : $party.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "PlayerRest(partyPractice=" + this.getPartyPractice() + ", correction=" + this.getCorrection() + ", party=" + this.getParty() + ")";
    }
}
