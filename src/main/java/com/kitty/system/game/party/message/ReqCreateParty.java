
package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32780)
public class ReqCreateParty extends Message
{
    private String partyName;
    private String announce;
    private byte consent;
    
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
        final ReqCreateParty other = (ReqCreateParty)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$partyName = this.getPartyName();
        final Object other$partyName = other.getPartyName();
        Label_0065: {
            if (this$partyName == null) {
                if (other$partyName == null) {
                    break Label_0065;
                }
            }
            else if (this$partyName.equals(other$partyName)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$announce = this.getAnnounce();
        final Object other$announce = other.getAnnounce();
        if (this$announce == null) {
            if (other$announce == null) {
                return this.getConsent() == other.getConsent();
            }
        }
        else if (this$announce.equals(other$announce)) {
            return this.getConsent() == other.getConsent();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqCreateParty;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $partyName = this.getPartyName();
        result = result * 59 + (($partyName == null) ? 43 : $partyName.hashCode());
        final Object $announce = this.getAnnounce();
        result = result * 59 + (($announce == null) ? 43 : $announce.hashCode());
        result = result * 59 + this.getConsent();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqCreateParty(partyName=" + this.getPartyName() + ", announce=" + this.getAnnounce() + ", consent=" + this.getConsent() + ")";
    }
}
