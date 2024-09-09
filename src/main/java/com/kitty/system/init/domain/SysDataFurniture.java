package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("data_furniture")
public class SysDataFurniture
{
    @Column("serial")
    private int serial;
    @Column("name")
    private String name;
    @Column("classify")
    private int classify;
    @Column("house")
    private int house;
    @Column("type")
    private int type;
    @Column("price")
    private long price;
    
    public SysDataFurniture() {
        this.classify = 0;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getClassify() {
        return this.classify;
    }
    
    public int getHouse() {
        return this.house;
    }
    
    public int getType() {
        return this.type;
    }
    
    public long getPrice() {
        return this.price;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setClassify(final int classify) {
        this.classify = classify;
    }
    
    public void setHouse(final int house) {
        this.house = house;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setPrice(final long price) {
        this.price = price;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataFurniture)) {
            return false;
        }
        SysDataFurniture other = (SysDataFurniture)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        return ((this$name != null) ? this$name.equals(other$name) : (other$name == null)) && this.getClassify() == other.getClassify() && this.getHouse() == other.getHouse() && this.getType() == other.getType() && this.getPrice() == other.getPrice();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataFurniture;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getClassify();
        result = result * 59 + this.getHouse();
        result = result * 59 + this.getType();
        long $price = this.getPrice();
        result = result * 59 + (int)($price >>> 32 ^ $price);
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataFurniture(serial=" + this.getSerial() + ", name=" + this.getName() + ", classify=" + this.getClassify() + ", house=" + this.getHouse() + ", type=" + this.getType() + ", price=" + this.getPrice() + ")";
    }
}
