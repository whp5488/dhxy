package com.kitty.system.game.shop.domain;

import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;

public class ItemInfo
{
    private int serial;
    private int count;
    private int pay_type;
    private int price;
    private int discount;
    private int quotaLimit;
    private List<FiedValue> list;
    
    public ItemInfo() {
        this.count = 1;
        this.pay_type = 1;
        this.discount = 0;
        this.quotaLimit = -1;
        this.list = new ArrayList<>();
    }
    
    public ItemInfo(int serial, int count, int pay_type, int price, int discount, int quotaLimit, List<FiedValue> list) {
        this.count = 1;
        this.pay_type = 1;
        this.discount = 0;
        this.quotaLimit = -1;
        this.list = new ArrayList<>();
        this.serial = serial;
        this.count = count;
        this.pay_type = pay_type;
        this.price = price;
        this.discount = discount;
        this.quotaLimit = quotaLimit;
        this.list = list;
    }
    
    public ItemInfo(int serial, int count, int pay_type, int price, int discount, int quotaLimit) {
        this.count = 1;
        this.pay_type = 1;
        this.discount = 0;
        this.quotaLimit = -1;
        this.list = new ArrayList<>();
        this.serial = serial;
        this.count = count;
        this.pay_type = pay_type;
        this.price = price;
        this.discount = discount;
        this.quotaLimit = quotaLimit;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getCount() {
        return this.count;
    }
    
    public int getPay_type() {
        return this.pay_type;
    }
    
    public int getPrice() {
        return this.price;
    }
    
    public int getDiscount() {
        return this.discount;
    }
    
    public int getQuotaLimit() {
        return this.quotaLimit;
    }
    
    public List<FiedValue> getList() {
        return this.list;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setCount(final int count) {
        this.count = count;
    }
    
    public void setPay_type(final int pay_type) {
        this.pay_type = pay_type;
    }
    
    public void setPrice(final int price) {
        this.price = price;
    }
    
    public void setDiscount(final int discount) {
        this.discount = discount;
    }
    
    public void setQuotaLimit(final int quotaLimit) {
        this.quotaLimit = quotaLimit;
    }
    
    public void setList(final List<FiedValue> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemInfo)) {
            return false;
        }
        ItemInfo other = (ItemInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getCount() != other.getCount()) {
            return false;
        }
        if (this.getPay_type() != other.getPay_type()) {
            return false;
        }
        if (this.getPrice() != other.getPrice()) {
            return false;
        }
        if (this.getDiscount() != other.getDiscount()) {
            return false;
        }
        if (this.getQuotaLimit() != other.getQuotaLimit()) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ItemInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getCount();
        result = result * 59 + this.getPay_type();
        result = result * 59 + this.getPrice();
        result = result * 59 + this.getDiscount();
        result = result * 59 + this.getQuotaLimit();
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ItemInfo(serial=" + this.getSerial() + ", count=" + this.getCount() + ", pay_type=" + this.getPay_type() + ", price=" + this.getPrice() + ", discount=" + this.getDiscount() + ", quotaLimit=" + this.getQuotaLimit() + ", list=" + this.getList() + ")";
    }
}
