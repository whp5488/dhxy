package com.kitty.system.game.gate.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 9040)
public class ReqLoginAccount extends Message
{
    private String type;
    private String accountId;
    private String password;
    private String gameV;
    private String engV;
    
    public ReqLoginAccount() {
    }
    
    public String getType() {
        return this.type;
    }
    
    public String getAccountId() {
        return this.accountId;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public String getGameV() {
        return this.gameV;
    }
    
    public String getEngV() {
        return this.engV;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public void setAccountId(final String accountId) {
        this.accountId = accountId;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    public void setGameV(final String gameV) {
        this.gameV = gameV;
    }
    
    public void setEngV(final String engV) {
        this.engV = engV;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqLoginAccount)) {
            return false;
        }
        ReqLoginAccount other = (ReqLoginAccount)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$type = this.getType();
        Object other$type = other.getType();
        if (!((this$type != null) ? this$type.equals(other$type) : (other$type == null))) {
            return false;
        }
        Object this$accountId = this.getAccountId();
        Object other$accountId = other.getAccountId();
        if (!((this$accountId != null) ? this$accountId.equals(other$accountId) : (other$accountId == null))) {
            return false;
        }
        Object this$password = this.getPassword();
        Object other$password = other.getPassword();
        if (!((this$password != null) ? this$password.equals(other$password) : (other$password == null))) {
            return false;
        }
        Object this$gameV = this.getGameV();
        Object other$gameV = other.getGameV();
        if (!((this$gameV != null) ? this$gameV.equals(other$gameV) : (other$gameV == null))) {
            return false;
        }
        Object this$engV = this.getEngV();
        Object other$engV = other.getEngV();
        return (this$engV != null) ? this$engV.equals(other$engV) : (other$engV == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqLoginAccount;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        Object $accountId = this.getAccountId();
        result = result * 59 + (($accountId == null) ? 43 : $accountId.hashCode());
        Object $password = this.getPassword();
        result = result * 59 + (($password == null) ? 43 : $password.hashCode());
        Object $gameV = this.getGameV();
        result = result * 59 + (($gameV == null) ? 43 : $gameV.hashCode());
        Object $engV = this.getEngV();
        result = result * 59 + (($engV == null) ? 43 : $engV.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqLoginAccount(type=" + this.getType() + ", accountId=" + this.getAccountId() + ", password=" + this.getPassword() + ", gameV=" + this.getGameV() + ", engV=" + this.getEngV() + ")";
    }
}
