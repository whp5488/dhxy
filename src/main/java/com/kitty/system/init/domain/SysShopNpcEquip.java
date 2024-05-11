
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_shop_npc_equip")
public class SysShopNpcEquip implements Serializable
{
    @Comment("编号")
    @Column("serial")
    private Integer serial;
    @Comment("数量")
    @Column("num")
    private Integer num;
    @Comment("价格")
    @Column("price")
    private Integer price;
    @Comment("等级限制")
    @Column("lv_limit")
    private Integer lvLimit;
    @Comment("转生限制")
    @Column("zs_limit")
    private Integer zsLimit;
    @Comment("消费类型")
    @Column("type")
    private Integer type;
    @Comment("折扣")
    @Column("discount")
    private Integer discount;
    @Comment("类型")
    @Column("mold")
    private Integer mold;
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public Integer getNum() {
        return this.num;
    }
    
    public Integer getPrice() {
        return this.price;
    }
    
    public Integer getLvLimit() {
        return this.lvLimit;
    }
    
    public Integer getZsLimit() {
        return this.zsLimit;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public Integer getDiscount() {
        return this.discount;
    }
    
    public Integer getMold() {
        return this.mold;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setNum(final Integer num) {
        this.num = num;
    }
    
    public void setPrice(final Integer price) {
        this.price = price;
    }
    
    public void setLvLimit(final Integer lvLimit) {
        this.lvLimit = lvLimit;
    }
    
    public void setZsLimit(final Integer zsLimit) {
        this.zsLimit = zsLimit;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    public void setDiscount(final Integer discount) {
        this.discount = discount;
    }
    
    public void setMold(final Integer mold) {
        this.mold = mold;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysShopNpcEquip)) {
            return false;
        }
        final SysShopNpcEquip other = (SysShopNpcEquip)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$serial = this.getSerial();
        final Object other$serial = other.getSerial();
        Label_0065: {
            if (this$serial == null) {
                if (other$serial == null) {
                    break Label_0065;
                }
            }
            else if (this$serial.equals(other$serial)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$num = this.getNum();
        final Object other$num = other.getNum();
        Label_0102: {
            if (this$num == null) {
                if (other$num == null) {
                    break Label_0102;
                }
            }
            else if (this$num.equals(other$num)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$price = this.getPrice();
        final Object other$price = other.getPrice();
        Label_0139: {
            if (this$price == null) {
                if (other$price == null) {
                    break Label_0139;
                }
            }
            else if (this$price.equals(other$price)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$lvLimit = this.getLvLimit();
        final Object other$lvLimit = other.getLvLimit();
        Label_0176: {
            if (this$lvLimit == null) {
                if (other$lvLimit == null) {
                    break Label_0176;
                }
            }
            else if (this$lvLimit.equals(other$lvLimit)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$zsLimit = this.getZsLimit();
        final Object other$zsLimit = other.getZsLimit();
        Label_0213: {
            if (this$zsLimit == null) {
                if (other$zsLimit == null) {
                    break Label_0213;
                }
            }
            else if (this$zsLimit.equals(other$zsLimit)) {
                break Label_0213;
            }
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0250: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0250;
                }
            }
            else if (this$type.equals(other$type)) {
                break Label_0250;
            }
            return false;
        }
        final Object this$discount = this.getDiscount();
        final Object other$discount = other.getDiscount();
        Label_0287: {
            if (this$discount == null) {
                if (other$discount == null) {
                    break Label_0287;
                }
            }
            else if (this$discount.equals(other$discount)) {
                break Label_0287;
            }
            return false;
        }
        final Object this$mold = this.getMold();
        final Object other$mold = other.getMold();
        if (this$mold == null) {
            if (other$mold == null) {
                return true;
            }
        }
        else if (this$mold.equals(other$mold)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysShopNpcEquip;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        final Object $num = this.getNum();
        result = result * 59 + (($num == null) ? 43 : $num.hashCode());
        final Object $price = this.getPrice();
        result = result * 59 + (($price == null) ? 43 : $price.hashCode());
        final Object $lvLimit = this.getLvLimit();
        result = result * 59 + (($lvLimit == null) ? 43 : $lvLimit.hashCode());
        final Object $zsLimit = this.getZsLimit();
        result = result * 59 + (($zsLimit == null) ? 43 : $zsLimit.hashCode());
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final Object $discount = this.getDiscount();
        result = result * 59 + (($discount == null) ? 43 : $discount.hashCode());
        final Object $mold = this.getMold();
        result = result * 59 + (($mold == null) ? 43 : $mold.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysShopNpcEquip(serial=" + this.getSerial() + ", num=" + this.getNum() + ", price=" + this.getPrice() + ", lvLimit=" + this.getLvLimit() + ", zsLimit=" + this.getZsLimit() + ", type=" + this.getType() + ", discount=" + this.getDiscount() + ", mold=" + this.getMold() + ")";
    }
}
