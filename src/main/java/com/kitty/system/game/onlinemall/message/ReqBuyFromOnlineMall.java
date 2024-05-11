
package com.kitty.system.game.onlinemall.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8410)
public class ReqBuyFromOnlineMall extends Message
{
    private int serial;
    private int amount;
    private int type;
    private String pwd;
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getAmount() {
        return this.amount;
    }
    
    public int getType() {
        return this.type;
    }
    
    public String getPwd() {
        return this.pwd;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setAmount(final int amount) {
        this.amount = amount;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setPwd(final String pwd) {
        this.pwd = pwd;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqBuyFromOnlineMall)) {
            return false;
        }
        final ReqBuyFromOnlineMall other = (ReqBuyFromOnlineMall)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getAmount() != other.getAmount()) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        final Object this$pwd = this.getPwd();
        final Object other$pwd = other.getPwd();
        if (this$pwd == null) {
            if (other$pwd == null) {
                return true;
            }
        }
        else if (this$pwd.equals(other$pwd)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqBuyFromOnlineMall;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getAmount();
        result = result * 59 + this.getType();
        final Object $pwd = this.getPwd();
        result = result * 59 + (($pwd == null) ? 43 : $pwd.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqBuyFromOnlineMall(serial=" + this.getSerial() + ", amount=" + this.getAmount() + ", type=" + this.getType() + ", pwd=" + this.getPwd() + ")";
    }
}
