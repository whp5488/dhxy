package com.kitty.system.game.shop.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 12356)
public class ReqBuyItem extends Message
{
    private int shipper;
    private int amount;
    private int serial;
    private byte type;
    private String param;
    
    public ReqBuyItem() {
    }
    
    public int getShipper() {
        return this.shipper;
    }
    
    public int getAmount() {
        return this.amount;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public String getParam() {
        return this.param;
    }
    
    public void setShipper(final int shipper) {
        this.shipper = shipper;
    }
    
    public void setAmount(final int amount) {
        this.amount = amount;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setParam(final String param) {
        this.param = param;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqBuyItem)) {
            return false;
        }
        ReqBuyItem other = (ReqBuyItem)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getShipper() != other.getShipper()) {
            return false;
        }
        if (this.getAmount() != other.getAmount()) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        Object this$param = this.getParam();
        Object other$param = other.getParam();
        return (this$param != null) ? this$param.equals(other$param) : (other$param == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqBuyItem;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getShipper();
        result = result * 59 + this.getAmount();
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getType();
        Object $param = this.getParam();
        result = result * 59 + (($param == null) ? 43 : $param.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqBuyItem(shipper=" + this.getShipper() + ", amount=" + this.getAmount() + ", serial=" + this.getSerial() + ", type=" + (int)this.getType() + ", param=" + this.getParam() + ")";
    }
}
