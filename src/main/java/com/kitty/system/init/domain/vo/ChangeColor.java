
package com.kitty.system.init.domain.vo;

import java.beans.ConstructorProperties;
import java.util.Arrays;

public class ChangeColor
{
    private int serial;
    private String name;
    private int[] goodsNum;
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int[] getGoodsNum() {
        return this.goodsNum;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setGoodsNum(final int[] goodsNum) {
        this.goodsNum = goodsNum;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ChangeColor)) {
            return false;
        }
        final ChangeColor other = (ChangeColor)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null) {
            if (other$name == null) {
                return Arrays.equals(this.getGoodsNum(), other.getGoodsNum());
            }
        }
        else if (this$name.equals(other$name)) {
            return Arrays.equals(this.getGoodsNum(), other.getGoodsNum());
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ChangeColor;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + Arrays.hashCode(this.getGoodsNum());
        return result;
    }
    
    @Override
    public String toString() {
        return "ChangeColor(serial=" + this.getSerial() + ", name=" + this.getName() + ", goodsNum=" + Arrays.toString(this.getGoodsNum()) + ")";
    }
    
    public ChangeColor() {
    }
    
    @ConstructorProperties({ "serial", "name", "goodsNum" })
    public ChangeColor(final int serial, final String name, final int[] goodsNum) {
        this.serial = serial;
        this.name = name;
        this.goodsNum = goodsNum;
    }
}
