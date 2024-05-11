
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("t_data_recommend_shop")
public class SysDataRecommendShop implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("道具编号")
    @Column("serial")
    private Integer serial;
    @Comment("道具名称")
    @Column("name")
    private String name;
    @Comment("消费类型")
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
    @Comment("推荐")
    @Column("recommend")
    private String recommend;
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
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
    
    public String getRecommend() {
        return this.recommend;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
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
    
    public void setRecommend(final String recommend) {
        this.recommend = recommend;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataRecommendShop)) {
            return false;
        }
        final SysDataRecommendShop other = (SysDataRecommendShop)o;
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
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0102: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0102;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0139: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0139;
                }
            }
            else if (this$type.equals(other$type)) {
                break Label_0139;
            }
            return false;
        }
        if (Double.compare(this.getPrice(), other.getPrice()) != 0) {
            return false;
        }
        final Object this$sortNo = this.getSortNo();
        final Object other$sortNo = other.getSortNo();
        Label_0192: {
            if (this$sortNo == null) {
                if (other$sortNo == null) {
                    break Label_0192;
                }
            }
            else if (this$sortNo.equals(other$sortNo)) {
                break Label_0192;
            }
            return false;
        }
        final Object this$lvLimit = this.getLvLimit();
        final Object other$lvLimit = other.getLvLimit();
        Label_0229: {
            if (this$lvLimit == null) {
                if (other$lvLimit == null) {
                    break Label_0229;
                }
            }
            else if (this$lvLimit.equals(other$lvLimit)) {
                break Label_0229;
            }
            return false;
        }
        final Object this$zsLimit = this.getZsLimit();
        final Object other$zsLimit = other.getZsLimit();
        Label_0266: {
            if (this$zsLimit == null) {
                if (other$zsLimit == null) {
                    break Label_0266;
                }
            }
            else if (this$zsLimit.equals(other$zsLimit)) {
                break Label_0266;
            }
            return false;
        }
        final Object this$mustVip = this.getMustVip();
        final Object other$mustVip = other.getMustVip();
        Label_0303: {
            if (this$mustVip == null) {
                if (other$mustVip == null) {
                    break Label_0303;
                }
            }
            else if (this$mustVip.equals(other$mustVip)) {
                break Label_0303;
            }
            return false;
        }
        final Object this$quotaLimit = this.getQuotaLimit();
        final Object other$quotaLimit = other.getQuotaLimit();
        Label_0340: {
            if (this$quotaLimit == null) {
                if (other$quotaLimit == null) {
                    break Label_0340;
                }
            }
            else if (this$quotaLimit.equals(other$quotaLimit)) {
                break Label_0340;
            }
            return false;
        }
        final Object this$discount = this.getDiscount();
        final Object other$discount = other.getDiscount();
        Label_0377: {
            if (this$discount == null) {
                if (other$discount == null) {
                    break Label_0377;
                }
            }
            else if (this$discount.equals(other$discount)) {
                break Label_0377;
            }
            return false;
        }
        final Object this$recommend = this.getRecommend();
        final Object other$recommend = other.getRecommend();
        if (this$recommend == null) {
            if (other$recommend == null) {
                return true;
            }
        }
        else if (this$recommend.equals(other$recommend)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataRecommendShop;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
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
        final Object $recommend = this.getRecommend();
        result = result * 59 + (($recommend == null) ? 43 : $recommend.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataRecommendShop(serial=" + this.getSerial() + ", name=" + this.getName() + ", type=" + this.getType() + ", price=" + this.getPrice() + ", sortNo=" + this.getSortNo() + ", lvLimit=" + this.getLvLimit() + ", zsLimit=" + this.getZsLimit() + ", mustVip=" + this.getMustVip() + ", quotaLimit=" + this.getQuotaLimit() + ", discount=" + this.getDiscount() + ", recommend=" + this.getRecommend() + ")";
    }
}
