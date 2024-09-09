package com.kitty.system.game.partner.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4204)
public class ReqFeedPartner extends Message
{
    private int partnerId;
    private int param;
    private String type;
    private int num;
    
    public ReqFeedPartner() {
    }
    
    public int getPartnerId() {
        return this.partnerId;
    }
    
    public int getParam() {
        return this.param;
    }
    
    public String getType() {
        return this.type;
    }
    
    public int getNum() {
        return this.num;
    }
    
    public void setPartnerId(final int partnerId) {
        this.partnerId = partnerId;
    }
    
    public void setParam(final int param) {
        this.param = param;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public void setNum(final int num) {
        this.num = num;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqFeedPartner)) {
            return false;
        }
        ReqFeedPartner other = (ReqFeedPartner)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getPartnerId() != other.getPartnerId()) {
            return false;
        }
        if (this.getParam() != other.getParam()) {
            return false;
        }
        Object this$type = this.getType();
        Object other$type = other.getType();
        return ((this$type != null) ? this$type.equals(other$type) : (other$type == null)) && this.getNum() == other.getNum();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqFeedPartner;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getPartnerId();
        result = result * 59 + this.getParam();
        Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        result = result * 59 + this.getNum();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqFeedPartner(partnerId=" + this.getPartnerId() + ", param=" + this.getParam() + ", type=" + this.getType() + ", num=" + this.getNum() + ")";
    }
}
