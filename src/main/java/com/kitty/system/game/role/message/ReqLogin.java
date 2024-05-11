
package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 12290)
public class ReqLogin extends Message
{
    private String accountId;
    private int auth_key;
    private String marking;
    
    public String getAccountId() {
        return this.accountId;
    }
    
    public int getAuth_key() {
        return this.auth_key;
    }
    
    public String getMarking() {
        return this.marking;
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
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqLogin)) {
            return false;
        }
        final ReqLogin other = (ReqLogin)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$accountId = this.getAccountId();
        final Object other$accountId = other.getAccountId();
        Label_0065: {
            if (this$accountId == null) {
                if (other$accountId == null) {
                    break Label_0065;
                }
            }
            else if (this$accountId.equals(other$accountId)) {
                break Label_0065;
            }
            return false;
        }
        if (this.getAuth_key() != other.getAuth_key()) {
            return false;
        }
        final Object this$marking = this.getMarking();
        final Object other$marking = other.getMarking();
        if (this$marking == null) {
            if (other$marking == null) {
                return true;
            }
        }
        else if (this$marking.equals(other$marking)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqLogin;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $accountId = this.getAccountId();
        result = result * 59 + (($accountId == null) ? 43 : $accountId.hashCode());
        result = result * 59 + this.getAuth_key();
        final Object $marking = this.getMarking();
        result = result * 59 + (($marking == null) ? 43 : $marking.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqLogin(accountId=" + this.getAccountId() + ", auth_key=" + this.getAuth_key() + ", marking=" + this.getMarking() + ")";
    }
}
