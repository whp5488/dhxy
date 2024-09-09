package com.kitty.system.game.equip.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32776)
public class ReqUpgradeEquip extends Message
{
    private int pos;
    private int type;
    private int operationType;
    private String param;
    
    public int getPos() {
        return this.pos;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getOperationType() {
        return this.operationType;
    }
    
    public String getParam() {
        return this.param;
    }
    
    public void setPos(final int pos) {
        this.pos = pos;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setOperationType(final int operationType) {
        this.operationType = operationType;
    }
    
    public void setParam(final String param) {
        this.param = param;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqUpgradeEquip)) {
            return false;
        }
        ReqUpgradeEquip other = (ReqUpgradeEquip)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getPos() != other.getPos()) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        if (this.getOperationType() != other.getOperationType()) {
            return false;
        }
        Object this$param = this.getParam();
        Object other$param = other.getParam();
        return (this$param != null) ? this$param.equals(other$param) : (other$param == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqUpgradeEquip;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getPos();
        result = result * 59 + this.getType();
        result = result * 59 + this.getOperationType();
        Object $param = this.getParam();
        result = result * 59 + (($param == null) ? 43 : $param.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqUpgradeEquip(pos=" + this.getPos() + ", type=" + this.getType() + ", operationType=" + this.getOperationType() + ", param=" + this.getParam() + ")";
    }
    
    public ReqUpgradeEquip(final int pos, final int type, final int operationType, final String param) {
        this.operationType = 0;
        this.pos = pos;
        this.type = type;
        this.operationType = operationType;
        this.param = param;
    }
    
    public ReqUpgradeEquip() {
        this.operationType = 0;
    }
}
