package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_shop_npc_guo")
public class SysShopNpcGuo implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Column("serial")
    private Integer serial;
    @Column("num")
    private int num;
    @Column("price")
    private int price;
    @Column("sort_no")
    private int sortNo;
    @Column("lv_limit")
    private int lvLimit;
    @Column("zs_limit")
    private int zsLimit;
    @Column("discount")
    private int discount;
    @Column("quota_limit")
    private int quotaLimit;
    @Column("bind")
    private int bind;
    
    public SysShopNpcGuo() {
    }
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public int getNum() {
        return this.num;
    }
    
    public int getPrice() {
        return this.price;
    }
    
    public int getSortNo() {
        return this.sortNo;
    }
    
    public int getLvLimit() {
        return this.lvLimit;
    }
    
    public int getZsLimit() {
        return this.zsLimit;
    }
    
    public int getDiscount() {
        return this.discount;
    }
    
    public int getQuotaLimit() {
        return this.quotaLimit;
    }
    
    public int getBind() {
        return this.bind;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setNum(final int num) {
        this.num = num;
    }
    
    public void setPrice(final int price) {
        this.price = price;
    }
    
    public void setSortNo(final int sortNo) {
        this.sortNo = sortNo;
    }
    
    public void setLvLimit(final int lvLimit) {
        this.lvLimit = lvLimit;
    }
    
    public void setZsLimit(final int zsLimit) {
        this.zsLimit = zsLimit;
    }
    
    public void setDiscount(final int discount) {
        this.discount = discount;
    }
    
    public void setQuotaLimit(final int quotaLimit) {
        this.quotaLimit = quotaLimit;
    }
    
    public void setBind(final int bind) {
        this.bind = bind;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysShopNpcGuo)) {
            return false;
        }
        SysShopNpcGuo other = (SysShopNpcGuo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$serial = this.getSerial();
        Object other$serial = other.getSerial();
        return ((this$serial != null) ? this$serial.equals(other$serial) : (other$serial == null)) && this.getNum() == other.getNum() && this.getPrice() == other.getPrice() && this.getSortNo() == other.getSortNo() && this.getLvLimit() == other.getLvLimit() && this.getZsLimit() == other.getZsLimit() && this.getDiscount() == other.getDiscount() && this.getQuotaLimit() == other.getQuotaLimit() && this.getBind() == other.getBind();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysShopNpcGuo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        result = result * 59 + this.getNum();
        result = result * 59 + this.getPrice();
        result = result * 59 + this.getSortNo();
        result = result * 59 + this.getLvLimit();
        result = result * 59 + this.getZsLimit();
        result = result * 59 + this.getDiscount();
        result = result * 59 + this.getQuotaLimit();
        result = result * 59 + this.getBind();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysShopNpcGuo(serial=" + this.getSerial() + ", num=" + this.getNum() + ", price=" + this.getPrice() + ", sortNo=" + this.getSortNo() + ", lvLimit=" + this.getLvLimit() + ", zsLimit=" + this.getZsLimit() + ", discount=" + this.getDiscount() + ", quotaLimit=" + this.getQuotaLimit() + ", bind=" + this.getBind() + ")";
    }
}
