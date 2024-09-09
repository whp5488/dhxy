package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32780)
public class ReqCreateParty extends Message
{
    private String partyName;
    private String announce;
    private byte consent;
    
    public ReqCreateParty() {
    }
    
    public String getPartyName() {
        return this.partyName;
    }
    
    public String getAnnounce() {
        return this.announce;
    }
    
    public byte getConsent() {
        return this.consent;
    }
    
    public void setPartyName(final String partyName) {
        this.partyName = partyName;
    }
    
    public void setAnnounce(final String announce) {
        this.announce = announce;
    }
    
    public void setConsent(final byte consent) {
        this.consent = consent;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqCreateParty)) {
            return false;
        }
        ReqCreateParty other = (ReqCreateParty)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$partyName = this.getPartyName();
        Object other$partyName = other.getPartyName();
        if (!((this$partyName != null) ? this$partyName.equals(other$partyName) : (other$partyName == null))) {
            return false;
        }
        Object this$announce = this.getAnnounce();
        Object other$announce = other.getAnnounce();
        return ((this$announce != null) ? this$announce.equals(other$announce) : (other$announce == null)) && this.getConsent() == other.getConsent();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqCreateParty;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $partyName = this.getPartyName();
        result = result * 59 + (($partyName == null) ? 43 : $partyName.hashCode());
        Object $announce = this.getAnnounce();
        result = result * 59 + (($announce == null) ? 43 : $announce.hashCode());
        result = result * 59 + this.getConsent();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqCreateParty(partyName=" + this.getPartyName() + ", announce=" + this.getAnnounce() + ", consent=" + (int)this.getConsent() + ")";
    }
}
