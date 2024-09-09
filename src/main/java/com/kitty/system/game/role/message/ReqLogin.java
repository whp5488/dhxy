package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 1833)
public class ReqLogin extends Message
{
    private int a;
    private String accountId;
    private int auth_key;
    private String marking;
    private int b;
    
    public ReqLogin() {
    }
    
    public int getA() {
        return this.a;
    }
    
    public String getAccountId() {
        return this.accountId;
    }
    
    public int getAuth_key() {
        return this.auth_key;
    }
    
    public String getMarking() {
        return this.marking;
    }
    
    public int getB() {
        return this.b;
    }
    
    public void setA(final int a) {
        this.a = a;
    }
    
    public void setAccountId(final String accountId) {
        this.accountId = accountId;
    }
    
    public void setAuth_key(final int auth_key) {
        this.auth_key = auth_key;
    }
    
    public void setMarking(final String marking) {
        this.marking = marking;
    }
    
    public void setB(final int b) {
        this.b = b;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqLogin)) {
            return false;
        }
        ReqLogin other = (ReqLogin)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getA() != other.getA()) {
            return false;
        }
        Object this$accountId = this.getAccountId();
        Object other$accountId = other.getAccountId();
        if (!((this$accountId != null) ? this$accountId.equals(other$accountId) : (other$accountId == null))) {
            return false;
        }
        if (this.getAuth_key() != other.getAuth_key()) {
            return false;
        }
        Object this$marking = this.getMarking();
        Object other$marking = other.getMarking();
        return ((this$marking != null) ? this$marking.equals(other$marking) : (other$marking == null)) && this.getB() == other.getB();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqLogin;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getA();
        Object $accountId = this.getAccountId();
        result = result * 59 + (($accountId == null) ? 43 : $accountId.hashCode());
        result = result * 59 + this.getAuth_key();
        Object $marking = this.getMarking();
        result = result * 59 + (($marking == null) ? 43 : $marking.hashCode());
        result = result * 59 + this.getB();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqLogin(a=" + this.getA() + ", accountId=" + this.getAccountId() + ", auth_key=" + this.getAuth_key() + ", marking=" + this.getMarking() + ", b=" + this.getB() + ")";
    }
}
