
package com.kitty.system.game.market.model;

public class StallItem
{
    private String id;
    private int serial;
    private String price;
    private byte status;
    private int startTime;
    private int endTime;
    private short amount;
    private int num;
    private int focus;
    private String extra;
    
    public StallItem() {
        this.amount = 1;
        this.num = 0;
        this.focus = 0;
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
    
    public int getStartTime() {
        return this.startTime;
    }
    
    public int getEndTime() {
        return this.endTime;
    }
    
    public short getAmount() {
        return this.amount;
    }
    
    public int getNum() {
        return this.num;
    }
    
    public int getFocus() {
        return this.focus;
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
    
    public void setStartTime(final int startTime) {
        this.startTime = startTime;
    }
    
    public void setEndTime(final int endTime) {
        this.endTime = endTime;
    }
    
    public void setAmount(final short amount) {
        this.amount = amount;
    }
    
    public void setNum(final int num) {
        this.num = num;
    }
    
    public void setFocus(final int focus) {
        this.focus = focus;
    }
    
    public void setExtra(final String extra) {
        this.extra = extra;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof StallItem)) {
            return false;
        }
        final StallItem other = (StallItem)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        Label_0065: {
            if (this$id == null) {
                if (other$id == null) {
                    break Label_0065;
                }
            }
            else if (this$id.equals(other$id)) {
                break Label_0065;
            }
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        final Object this$price = this.getPrice();
        final Object other$price = other.getPrice();
        Label_0115: {
            if (this$price == null) {
                if (other$price == null) {
                    break Label_0115;
                }
            }
            else if (this$price.equals(other$price)) {
                break Label_0115;
            }
            return false;
        }
        if (this.getStatus() != other.getStatus()) {
            return false;
        }
        if (this.getStartTime() != other.getStartTime()) {
            return false;
        }
        if (this.getEndTime() != other.getEndTime()) {
            return false;
        }
        if (this.getAmount() != other.getAmount()) {
            return false;
        }
        if (this.getNum() != other.getNum()) {
            return false;
        }
        if (this.getFocus() != other.getFocus()) {
            return false;
        }
        final Object this$extra = this.getExtra();
        final Object other$extra = other.getExtra();
        if (this$extra == null) {
            if (other$extra == null) {
                return true;
            }
        }
        else if (this$extra.equals(other$extra)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof StallItem;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getSerial();
        final Object $price = this.getPrice();
        result = result * 59 + (($price == null) ? 43 : $price.hashCode());
        result = result * 59 + this.getStatus();
        result = result * 59 + this.getStartTime();
        result = result * 59 + this.getEndTime();
        result = result * 59 + this.getAmount();
        result = result * 59 + this.getNum();
        result = result * 59 + this.getFocus();
        final Object $extra = this.getExtra();
        result = result * 59 + (($extra == null) ? 43 : $extra.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "StallItem(id=" + this.getId() + ", serial=" + this.getSerial() + ", price=" + this.getPrice() + ", status=" + this.getStatus() + ", startTime=" + this.getStartTime() + ", endTime=" + this.getEndTime() + ", amount=" + this.getAmount() + ", num=" + this.getNum() + ", focus=" + this.getFocus() + ", extra=" + this.getExtra() + ")";
    }
}
