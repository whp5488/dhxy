package com.kitty.system.game.horse.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 41040)
public class ReqUpgradeHorse extends Message
{
    private short type;
    private byte operationType;
    private int rests;
    private String param;
    
    public short getType() {
        return this.type;
    }
    
    public byte getOperationType() {
        return this.operationType;
    }
    
    public int getRests() {
        return this.rests;
    }
    
    public String getParam() {
        return this.param;
    }
    
    public void setType(final short type) {
        this.type = type;
    }
    
    public void setOperationType(final byte operationType) {
        this.operationType = operationType;
    }
    
    public void setRests(final int rests) {
        this.rests = rests;
    }
    
    public void setParam(final String param) {
        this.param = param;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqUpgradeHorse)) {
            return false;
        }
        ReqUpgradeHorse other = (ReqUpgradeHorse)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        if (this.getOperationType() != other.getOperationType()) {
            return false;
        }
        if (this.getRests() != other.getRests()) {
            return false;
        }
        Object this$param = this.getParam();
        Object other$param = other.getParam();
        return (this$param != null) ? this$param.equals(other$param) : (other$param == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqUpgradeHorse;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        result = result * 59 + this.getOperationType();
        result = result * 59 + this.getRests();
        Object $param = this.getParam();
        result = result * 59 + (($param == null) ? 43 : $param.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqUpgradeHorse(type=" + (int)this.getType() + ", operationType=" + (int)this.getOperationType() + ", rests=" + this.getRests() + ", param=" + this.getParam() + ")";
    }
    
    public ReqUpgradeHorse(final short type, final byte operationType, final int rests, final String param) {
        this.operationType = 0;
        this.rests = 0;
        this.type = type;
        this.operationType = operationType;
        this.rests = rests;
        this.param = param;
    }
    
    public ReqUpgradeHorse() {
        this.operationType = 0;
        this.rests = 0;
    }
}
