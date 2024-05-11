
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
        final ReqLoginAccount other = (ReqLoginAccount)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0065: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0065;
                }
            }
            else if (this$type.equals(other$type)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$accountId = this.getAccountId();
        final Object other$accountId = other.getAccountId();
        Label_0102: {
            if (this$accountId == null) {
                if (other$accountId == null) {
                    break Label_0102;
                }
            }
            else if (this$accountId.equals(other$accountId)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$password = this.getPassword();
        final Object other$password = other.getPassword();
        Label_0139: {
            if (this$password == null) {
                if (other$password == null) {
                    break Label_0139;
                }
            }
            else if (this$password.equals(other$password)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$gameV = this.getGameV();
        final Object other$gameV = other.getGameV();
        Label_0176: {
            if (this$gameV == null) {
                if (other$gameV == null) {
                    break Label_0176;
                }
            }
            else if (this$gameV.equals(other$gameV)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$engV = this.getEngV();
        final Object other$engV = other.getEngV();
        if (this$engV == null) {
            if (other$engV == null) {
                return true;
            }
        }
        else if (this$engV.equals(other$engV)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqLoginAccount;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final Object $accountId = this.getAccountId();
        result = result * 59 + (($accountId == null) ? 43 : $accountId.hashCode());
        final Object $password = this.getPassword();
        result = result * 59 + (($password == null) ? 43 : $password.hashCode());
        final Object $gameV = this.getGameV();
        result = result * 59 + (($gameV == null) ? 43 : $gameV.hashCode());
        final Object $engV = this.getEngV();
        result = result * 59 + (($engV == null) ? 43 : $engV.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqLoginAccount(type=" + this.getType() + ", accountId=" + this.getAccountId() + ", password=" + this.getPassword() + ", gameV=" + this.getGameV() + ", engV=" + this.getEngV() + ")";
    }
}
