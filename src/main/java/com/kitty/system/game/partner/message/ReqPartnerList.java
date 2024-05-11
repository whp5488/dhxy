
package com.kitty.system.game.partner.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4203)
public class ReqPartnerList extends Message
{
    private int roleId;
    private byte isFight;
    
    public ReqPartnerList() {
        this.isFight = 0;
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public byte getIsFight() {
        return this.isFight;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setIsFight(final byte isFight) {
        this.isFight = isFight;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqPartnerList)) {
            return false;
        }
        final ReqPartnerList other = (ReqPartnerList)o;
        return other.canEqual(this) && this.getRoleId() == other.getRoleId() && this.getIsFight() == other.getIsFight();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqPartnerList;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRoleId();
        result = result * 59 + this.getIsFight();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqPartnerList(roleId=" + this.getRoleId() + ", isFight=" + this.getIsFight() + ")";
    }
}
