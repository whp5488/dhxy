
package com.kitty.system.game.market.message.vo;

import com.kitty.system.game.market.model.MarketDealRecord;

public class MarketDealVo
{
    private int serial;
    private String price;
    private short amount;
    private byte status;
    
    public static MarketDealVo valueOf(final MarketDealRecord record) {
        final MarketDealVo vo = new MarketDealVo();
        vo.setSerial(record.getSerial());
        vo.setPrice(String.valueOf(record.getPrice()));
        vo.setAmount(record.getAmount());
        vo.setStatus(record.getStatus());
        return vo;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getPrice() {
        return this.price;
    }
    
    public short getAmount() {
        return this.amount;
    }
    
    public byte getStatus() {
        return this.status;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setPrice(final String price) {
        this.price = price;
    }
    
    public void setAmount(final short amount) {
        this.amount = amount;
    }
    
    public void setStatus(final byte status) {
        this.status = status;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MarketDealVo)) {
            return false;
        }
        final MarketDealVo other = (MarketDealVo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        final Object this$price = this.getPrice();
        final Object other$price = other.getPrice();
        if (this$price == null) {
            if (other$price == null) {
                return this.getAmount() == other.getAmount() && this.getStatus() == other.getStatus();
            }
        }
        else if (this$price.equals(other$price)) {
            return this.getAmount() == other.getAmount() && this.getStatus() == other.getStatus();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof MarketDealVo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        final Object $price = this.getPrice();
        result = result * 59 + (($price == null) ? 43 : $price.hashCode());
        result = result * 59 + this.getAmount();
        result = result * 59 + this.getStatus();
        return result;
    }
    
    @Override
    public String toString() {
        return "MarketDealVo(serial=" + this.getSerial() + ", price=" + this.getPrice() + ", amount=" + this.getAmount() + ", status=" + this.getStatus() + ")";
    }
}
