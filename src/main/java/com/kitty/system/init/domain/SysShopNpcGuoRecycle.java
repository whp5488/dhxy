package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_shop_npc_guo_recycle")
public class SysShopNpcGuoRecycle implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Column("serial")
    private Integer serial;
    @Column("price")
    private int price;
    
    public SysShopNpcGuoRecycle() {
    }
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public int getPrice() {
        return this.price;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setPrice(final int price) {
        this.price = price;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysShopNpcGuoRecycle)) {
            return false;
        }
        SysShopNpcGuoRecycle other = (SysShopNpcGuoRecycle)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$serial = this.getSerial();
        Object other$serial = other.getSerial();
        return ((this$serial != null) ? this$serial.equals(other$serial) : (other$serial == null)) && this.getPrice() == other.getPrice();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysShopNpcGuoRecycle;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        result = result * 59 + this.getPrice();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysShopNpcGuoRecycle(serial=" + this.getSerial() + ", price=" + this.getPrice() + ")";
    }
}
