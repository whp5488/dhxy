package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32784)
public class ReqPartyRejectLevel extends Message
{
    private byte consent;
    private short consentLevel;
    private byte refuse;
    private short refuseLevel;
    
    public ReqPartyRejectLevel() {
        this.consent = 0;
        this.consentLevel = 0;
        this.refuse = 1;
        this.refuseLevel = 0;
    }
    
    public byte getConsent() {
        return this.consent;
    }
    
    public short getConsentLevel() {
        return this.consentLevel;
    }
    
    public byte getRefuse() {
        return this.refuse;
    }
    
    public short getRefuseLevel() {
        return this.refuseLevel;
    }
    
    public void setConsent(final byte consent) {
        this.consent = consent;
    }
    
    public void setConsentLevel(final short consentLevel) {
        this.consentLevel = consentLevel;
    }
    
    public void setRefuse(final byte refuse) {
        this.refuse = refuse;
    }
    
    public void setRefuseLevel(final short refuseLevel) {
        this.refuseLevel = refuseLevel;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqPartyRejectLevel)) {
            return false;
        }
        ReqPartyRejectLevel other = (ReqPartyRejectLevel)o;
        return other.canEqual(this) && this.getConsent() == other.getConsent() && this.getConsentLevel() == other.getConsentLevel() && this.getRefuse() == other.getRefuse() && this.getRefuseLevel() == other.getRefuseLevel();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqPartyRejectLevel;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getConsent();
        result = result * 59 + this.getConsentLevel();
        result = result * 59 + this.getRefuse();
        result = result * 59 + this.getRefuseLevel();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqPartyRejectLevel(consent=" + (int)this.getConsent() + ", consentLevel=" + (int)this.getConsentLevel() + ", refuse=" + (int)this.getRefuse() + ", refuseLevel=" + (int)this.getRefuseLevel() + ")";
    }
}
