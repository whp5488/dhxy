
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("t_data_bound_shop")
public class SysBoundShop implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("道具编号")
    @Column("serial")
    private Integer serial;
    @Comment("消费类型：1.元宝，2.银币，3.铜币，16.绑定仙玉")
    @Column("type")
    private Integer type;
    @Comment("价格")
    @Column("price")
    private double price;
    @Comment("排序")
    @Column("sort_no")
    private Integer sortNo;
    @Comment("等级限制")
    @Column("lv_limit")
    private Integer lvLimit;
    @Comment("转生限制")
    @Column("zs_limit")
    private Integer zsLimit;
    @Comment("是否vip才可购买 0 否 1 是")
    @Column("must_vip")
    private Integer mustVip;
    @Comment("限购数量")
    @Column("quota_limit")
    private Integer quotaLimit;
    @Comment("折扣")
    @Column("discount")
    private Integer discount;
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public double getPrice() {
        return this.price;
    }
    
    public Integer getSortNo() {
        return this.sortNo;
    }
    
    public Integer getLvLimit() {
        return this.lvLimit;
    }
    
    public Integer getZsLimit() {
        return this.zsLimit;
    }
    
    public Integer getMustVip() {
        return this.mustVip;
    }
    
    public Integer getQuotaLimit() {
        return this.quotaLimit;
    }
    
    public Integer getDiscount() {
        return this.discount;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    public void setPrice(final double price) {
        this.price = price;
    }
    
    public void setSortNo(final Integer sortNo) {
        this.sortNo = sortNo;
    }
    
    public void setLvLimit(final Integer lvLimit) {
        this.lvLimit = lvLimit;
    }
    
    public void setZsLimit(final Integer zsLimit) {
        this.zsLimit = zsLimit;
    }
    
    public void setMustVip(final Integer mustVip) {
        this.mustVip = mustVip;
    }
    
    public void setQuotaLimit(final Integer quotaLimit) {
        this.quotaLimit = quotaLimit;
    }
    
    public void setDiscount(final Integer discount) {
        this.discount = discount;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysBoundShop)) {
            return false;
        }
        final SysBoundShop other = (SysBoundShop)o;
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
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0102: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0102;
                }
            }
            else if (this$type.equals(other$type)) {
                break Label_0102;
            }
            return false;
        }
        if (Double.compare(this.getPrice(), other.getPrice()) != 0) {
            return false;
        }
        final Object this$sortNo = this.getSortNo();
        final Object other$sortNo = other.getSortNo();
        Label_0155: {
            if (this$sortNo == null) {
                if (other$sortNo == null) {
                    break Label_0155;
                }
            }
            else if (this$sortNo.equals(other$sortNo)) {
                break Label_0155;
            }
            return false;
        }
        final Object this$lvLimit = this.getLvLimit();
        final Object other$lvLimit = other.getLvLimit();
        Label_0192: {
            if (this$lvLimit == null) {
                if (other$lvLimit == null) {
                    break Label_0192;
                }
            }
            else if (this$lvLimit.equals(other$lvLimit)) {
                break Label_0192;
            }
            return false;
        }
        final Object this$zsLimit = this.getZsLimit();
        final Object other$zsLimit = other.getZsLimit();
        Label_0229: {
            if (this$zsLimit == null) {
                if (other$zsLimit == null) {
                    break Label_0229;
                }
            }
            else if (this$zsLimit.equals(other$zsLimit)) {
                break Label_0229;
            }
            return false;
        }
        final Object this$mustVip = this.getMustVip();
        final Object other$mustVip = other.getMustVip();
        Label_0266: {
            if (this$mustVip == null) {
                if (other$mustVip == null) {
                    break Label_0266;
                }
            }
            else if (this$mustVip.equals(other$mustVip)) {
                break Label_0266;
            }
            return false;
        }
        final Object this$quotaLimit = this.getQuotaLimit();
        final Object other$quotaLimit = other.getQuotaLimit();
        Label_0303: {
            if (this$quotaLimit == null) {
                if (other$quotaLimit == null) {
                    break Label_0303;
                }
            }
            else if (this$quotaLimit.equals(other$quotaLimit)) {
                break Label_0303;
            }
            return false;
        }
        final Object this$discount = this.getDiscount();
        final Object other$discount = other.getDiscount();
        if (this$discount == null) {
            if (other$discount == null) {
                return true;
            }
        }
        else if (this$discount.equals(other$discount)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysBoundShop;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final long $price = Double.doubleToLongBits(this.getPrice());
        result = result * 59 + (int)($price >>> 32 ^ $price);
        final Object $sortNo = this.getSortNo();
        result = result * 59 + (($sortNo == null) ? 43 : $sortNo.hashCode());
        final Object $lvLimit = this.getLvLimit();
        result = result * 59 + (($lvLimit == null) ? 43 : $lvLimit.hashCode());
        final Object $zsLimit = this.getZsLimit();
        result = result * 59 + (($zsLimit == null) ? 43 : $zsLimit.hashCode());
        final Object $mustVip = this.getMustVip();
        result = result * 59 + (($mustVip == null) ? 43 : $mustVip.hashCode());
        final Object $quotaLimit = this.getQuotaLimit();
        result = result * 59 + (($quotaLimit == null) ? 43 : $quotaLimit.hashCode());
        final Object $discount = this.getDiscount();
        result = result * 59 + (($discount == null) ? 43 : $discount.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysBoundShop(serial=" + this.getSerial() + ", type=" + this.getType() + ", price=" + this.getPrice() + ", sortNo=" + this.getSortNo() + ", lvLimit=" + this.getLvLimit() + ", zsLimit=" + this.getZsLimit() + ", mustVip=" + this.getMustVip() + ", quotaLimit=" + this.getQuotaLimit() + ", discount=" + this.getDiscount() + ")";
    }
}
