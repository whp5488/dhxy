package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_shop_npc_pet")
public class SysShopNpcPet implements Serializable
{
    @Comment("编号")
    @Column("serial")
    private int serial;
    @Comment("数量")
    @Column("num")
    private int num;
    @Comment("价格")
    @Column("price")
    private int price;
    @Comment("等级限制")
    @Column("lv_limit")
    private int lvLimit;
    @Comment("转生限制")
    @Column("zs_limit")
    private int zsLimit;
    @Comment("消费类型")
    @Column("type")
    private int type;
    @Comment("折扣")
    @Column("discount")
    private int discount;
    
    public SysShopNpcPet() {
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getNum() {
        return this.num;
    }
    
    public int getPrice() {
        return this.price;
    }
    
    public int getLvLimit() {
        return this.lvLimit;
    }
    
    public int getZsLimit() {
        return this.zsLimit;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getDiscount() {
        return this.discount;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setNum(final int num) {
        this.num = num;
    }
    
    public void setPrice(final int price) {
        this.price = price;
    }
    
    public void setLvLimit(final int lvLimit) {
        this.lvLimit = lvLimit;
    }
    
    public void setZsLimit(final int zsLimit) {
        this.zsLimit = zsLimit;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setDiscount(final int discount) {
        this.discount = discount;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysShopNpcPet)) {
            return false;
        }
        SysShopNpcPet other = (SysShopNpcPet)o;
        return other.canEqual(this) && this.getSerial() == other.getSerial() && this.getNum() == other.getNum() && this.getPrice() == other.getPrice() && this.getLvLimit() == other.getLvLimit() && this.getZsLimit() == other.getZsLimit() && this.getType() == other.getType() && this.getDiscount() == other.getDiscount();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysShopNpcPet;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getNum();
        result = result * 59 + this.getPrice();
        result = result * 59 + this.getLvLimit();
        result = result * 59 + this.getZsLimit();
        result = result * 59 + this.getType();
        result = result * 59 + this.getDiscount();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysShopNpcPet(serial=" + this.getSerial() + ", num=" + this.getNum() + ", price=" + this.getPrice() + ", lvLimit=" + this.getLvLimit() + ", zsLimit=" + this.getZsLimit() + ", type=" + this.getType() + ", discount=" + this.getDiscount() + ")";
    }
}
