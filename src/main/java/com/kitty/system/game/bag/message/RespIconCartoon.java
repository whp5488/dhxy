package com.kitty.system.game.bag.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 40964)
public class RespIconCartoon extends Message
{
    private int type;
    private int serial;
    private String onlyId;
    
    public RespIconCartoon() {
        this.type = 1;
        this.onlyId = "";
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getOnlyId() {
        return this.onlyId;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setOnlyId(final String onlyId) {
        this.onlyId = onlyId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespIconCartoon)) {
            return false;
        }
        RespIconCartoon other = (RespIconCartoon)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$onlyId = this.getOnlyId();
        Object other$onlyId = other.getOnlyId();
        return (this$onlyId != null) ? this$onlyId.equals(other$onlyId) : (other$onlyId == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespIconCartoon;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        result = result * 59 + this.getSerial();
        Object $onlyId = this.getOnlyId();
        result = result * 59 + (($onlyId == null) ? 43 : $onlyId.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespIconCartoon(type=" + this.getType() + ", serial=" + this.getSerial() + ", onlyId=" + this.getOnlyId() + ")";
    }
}
