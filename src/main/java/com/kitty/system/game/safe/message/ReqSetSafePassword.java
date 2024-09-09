package com.kitty.system.game.safe.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32828)
public class ReqSetSafePassword extends Message
{
    private String password;
    
    public ReqSetSafePassword() {
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqSetSafePassword)) {
            return false;
        }
        ReqSetSafePassword other = (ReqSetSafePassword)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$password = this.getPassword();
        Object other$password = other.getPassword();
        return (this$password != null) ? this$password.equals(other$password) : (other$password == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqSetSafePassword;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $password = this.getPassword();
        result = result * 59 + (($password == null) ? 43 : $password.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqSetSafePassword(password=" + this.getPassword() + ")";
    }
}
