
package com.kitty.system.game.partner.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4202)
public class ReqPartnerRecruits extends Message
{
    private int partnerId;
    private byte isRecruits;
    
    public ReqPartnerRecruits() {
        this.partnerId = 0;
        this.isRecruits = 1;
    }
    
    public int getPartnerId() {
        return this.partnerId;
    }
    
    public byte getIsRecruits() {
        return this.isRecruits;
    }
    
    public void setPartnerId(final int partnerId) {
        this.partnerId = partnerId;
    }
    
    public void setIsRecruits(final byte isRecruits) {
        this.isRecruits = isRecruits;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqPartnerRecruits)) {
            return false;
        }
        final ReqPartnerRecruits other = (ReqPartnerRecruits)o;
        return other.canEqual(this) && this.getPartnerId() == other.getPartnerId() && this.getIsRecruits() == other.getIsRecruits();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqPartnerRecruits;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getPartnerId();
        result = result * 59 + this.getIsRecruits();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqPartnerRecruits(partnerId=" + this.getPartnerId() + ", isRecruits=" + this.getIsRecruits() + ")";
    }
}
