
package com.kitty.system.game.partner.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4201)
public class ReqSelectCurrentPartner extends Message
{
    private int partnerId;
    private byte isFight;
    private int replacePartnerId;
    
    public ReqSelectCurrentPartner() {
        this.replacePartnerId = 0;
    }
    
    public int getPartnerId() {
        return this.partnerId;
    }
    
    public byte getIsFight() {
        return this.isFight;
    }
    
    public int getReplacePartnerId() {
        return this.replacePartnerId;
    }
    
    public void setPartnerId(final int partnerId) {
        this.partnerId = partnerId;
    }
    
    public void setIsFight(final byte isFight) {
        this.isFight = isFight;
    }
    
    public void setReplacePartnerId(final int replacePartnerId) {
        this.replacePartnerId = replacePartnerId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqSelectCurrentPartner)) {
            return false;
        }
        final ReqSelectCurrentPartner other = (ReqSelectCurrentPartner)o;
        return other.canEqual(this) && this.getPartnerId() == other.getPartnerId() && this.getIsFight() == other.getIsFight() && this.getReplacePartnerId() == other.getReplacePartnerId();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqSelectCurrentPartner;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getPartnerId();
        result = result * 59 + this.getIsFight();
        result = result * 59 + this.getReplacePartnerId();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqSelectCurrentPartner(partnerId=" + this.getPartnerId() + ", isFight=" + this.getIsFight() + ", replacePartnerId=" + this.getReplacePartnerId() + ")";
    }
}
