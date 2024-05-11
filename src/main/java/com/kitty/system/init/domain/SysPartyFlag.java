
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_party_flag")
public class SysPartyFlag implements Serializable
{
    @Comment("索引")
    @Column("index")
    private short index;
    @Comment("价格")
    @Column("price")
    private int price;
    @Comment("消费类型（1.元宝，2.银币，3.贡币）")
    @Column("type")
    private int type;
    @Comment("分类：0.旗帜 1.边饰 2.纹章 3.旗帜颜色 4.纹章颜色")
    @Column("classify")
    private short classify;
    
    public short getIndex() {
        return this.index;
    }
    
    public int getPrice() {
        return this.price;
    }
    
    public int getType() {
        return this.type;
    }
    
    public short getClassify() {
        return this.classify;
    }
    
    public void setIndex(final short index) {
        this.index = index;
    }
    
    public void setPrice(final int price) {
        this.price = price;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setClassify(final short classify) {
        this.classify = classify;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysPartyFlag)) {
            return false;
        }
        final SysPartyFlag other = (SysPartyFlag)o;
        return other.canEqual(this) && this.getIndex() == other.getIndex() && this.getPrice() == other.getPrice() && this.getType() == other.getType() && this.getClassify() == other.getClassify();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysPartyFlag;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getIndex();
        result = result * 59 + this.getPrice();
        result = result * 59 + this.getType();
        result = result * 59 + this.getClassify();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysPartyFlag(index=" + this.getIndex() + ", price=" + this.getPrice() + ", type=" + this.getType() + ", classify=" + this.getClassify() + ")";
    }
}
