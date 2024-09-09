package com.kitty.system.game.market.model;

public class Cargo
{
    private String id;
    private int serial;
    private String price;
    private byte status;
    private short amount;
    private short saleAmount;
    private int showEndTime;
    private int endTime;
    private String extra;
    
    public Cargo() {
        this.amount = 1;
        this.saleAmount = 1;
    }
    
    public String getId() {
        return this.id;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getPrice() {
        return this.price;
    }
    
    public byte getStatus() {
        return this.status;
    }
    
    public short getAmount() {
        return this.amount;
    }
    
    public short getSaleAmount() {
        return this.saleAmount;
    }
    
    public int getShowEndTime() {
        return this.showEndTime;
    }
    
    public int getEndTime() {
        return this.endTime;
    }
    
    public String getExtra() {
        return this.extra;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setPrice(final String price) {
        this.price = price;
    }
    
    public void setStatus(final byte status) {
        this.status = status;
    }
    
    public void setAmount(final short amount) {
        this.amount = amount;
    }
    
    public void setSaleAmount(final short saleAmount) {
        this.saleAmount = saleAmount;
    }
    
    public void setShowEndTime(final int showEndTime) {
        this.showEndTime = showEndTime;
    }
    
    public void setEndTime(final int endTime) {
        this.endTime = endTime;
    }
    
    public void setExtra(final String extra) {
        this.extra = extra;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Cargo)) {
            return false;
        }
        Cargo other = (Cargo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$price = this.getPrice();
        Object other$price = other.getPrice();
        if (!((this$price != null) ? this$price.equals(other$price) : (other$price == null))) {
            return false;
        }
        if (this.getStatus() != other.getStatus()) {
            return false;
        }
        if (this.getAmount() != other.getAmount()) {
            return false;
        }
        if (this.getSaleAmount() != other.getSaleAmount()) {
            return false;
        }
        if (this.getShowEndTime() != other.getShowEndTime()) {
            return false;
        }
        if (this.getEndTime() != other.getEndTime()) {
            return false;
        }
        Object this$extra = this.getExtra();
        Object other$extra = other.getExtra();
        return (this$extra != null) ? this$extra.equals(other$extra) : (other$extra == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Cargo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getSerial();
        Object $price = this.getPrice();
        result = result * 59 + (($price == null) ? 43 : $price.hashCode());
        result = result * 59 + this.getStatus();
        result = result * 59 + this.getAmount();
        result = result * 59 + this.getSaleAmount();
        result = result * 59 + this.getShowEndTime();
        result = result * 59 + this.getEndTime();
        Object $extra = this.getExtra();
        result = result * 59 + (($extra == null) ? 43 : $extra.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Cargo(id=" + this.getId() + ", serial=" + this.getSerial() + ", price=" + this.getPrice() + ", status=" + (int)this.getStatus() + ", amount=" + (int)this.getAmount() + ", saleAmount=" + (int)this.getSaleAmount() + ", showEndTime=" + this.getShowEndTime() + ", endTime=" + this.getEndTime() + ", extra=" + this.getExtra() + ")";
    }
}
