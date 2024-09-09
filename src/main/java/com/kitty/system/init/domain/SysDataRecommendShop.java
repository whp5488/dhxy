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
    @Comment("数量")
    @Column("num")
    private Integer num;
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
    
    public SysDataRecommendShop() {
        this.num = Integer.valueOf(1);
    }
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public Integer getNum() {
        return this.num;
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
    
    public void setNum(final Integer num) {
        this.num = num;
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
        SysDataRecommendShop other = (SysDataRecommendShop)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$serial = this.getSerial();
        Object other$serial = other.getSerial();
        if (!((this$serial != null) ? this$serial.equals(other$serial) : (other$serial == null))) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        Object this$type = this.getType();
        Object other$type = other.getType();
        if (!((this$type != null) ? this$type.equals(other$type) : (other$type == null))) {
            return false;
        }
        Object this$num = this.getNum();
        Object other$num = other.getNum();
        if (!((this$num != null) ? this$num.equals(other$num) : (other$num == null))) {
            return false;
        }
        if (Double.compare(this.getPrice(), other.getPrice()) != 0) {
            return false;
        }
        Object this$sortNo = this.getSortNo();
        Object other$sortNo = other.getSortNo();
        if (!((this$sortNo != null) ? this$sortNo.equals(other$sortNo) : (other$sortNo == null))) {
            return false;
        }
        Object this$lvLimit = this.getLvLimit();
        Object other$lvLimit = other.getLvLimit();
        if (!((this$lvLimit != null) ? this$lvLimit.equals(other$lvLimit) : (other$lvLimit == null))) {
            return false;
        }
        Object this$zsLimit = this.getZsLimit();
        Object other$zsLimit = other.getZsLimit();
        if (!((this$zsLimit != null) ? this$zsLimit.equals(other$zsLimit) : (other$zsLimit == null))) {
            return false;
        }
        Object this$mustVip = this.getMustVip();
        Object other$mustVip = other.getMustVip();
        if (!((this$mustVip != null) ? this$mustVip.equals(other$mustVip) : (other$mustVip == null))) {
            return false;
        }
        Object this$quotaLimit = this.getQuotaLimit();
        Object other$quotaLimit = other.getQuotaLimit();
        if (!((this$quotaLimit != null) ? this$quotaLimit.equals(other$quotaLimit) : (other$quotaLimit == null))) {
            return false;
        }
        Object this$discount = this.getDiscount();
        Object other$discount = other.getDiscount();
        if (!((this$discount != null) ? this$discount.equals(other$discount) : (other$discount == null))) {
            return false;
        }
        Object this$recommend = this.getRecommend();
        Object other$recommend = other.getRecommend();
        return (this$recommend != null) ? this$recommend.equals(other$recommend) : (other$recommend == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataRecommendShop;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        Object $num = this.getNum();
        result = result * 59 + (($num == null) ? 43 : $num.hashCode());
        long $price = Double.doubleToLongBits(this.getPrice());
        result = result * 59 + (int)($price >>> 32 ^ $price);
        Object $sortNo = this.getSortNo();
        result = result * 59 + (($sortNo == null) ? 43 : $sortNo.hashCode());
        Object $lvLimit = this.getLvLimit();
        result = result * 59 + (($lvLimit == null) ? 43 : $lvLimit.hashCode());
        Object $zsLimit = this.getZsLimit();
        result = result * 59 + (($zsLimit == null) ? 43 : $zsLimit.hashCode());
        Object $mustVip = this.getMustVip();
        result = result * 59 + (($mustVip == null) ? 43 : $mustVip.hashCode());
        Object $quotaLimit = this.getQuotaLimit();
        result = result * 59 + (($quotaLimit == null) ? 43 : $quotaLimit.hashCode());
        Object $discount = this.getDiscount();
        result = result * 59 + (($discount == null) ? 43 : $discount.hashCode());
        Object $recommend = this.getRecommend();
        result = result * 59 + (($recommend == null) ? 43 : $recommend.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataRecommendShop(serial=" + this.getSerial() + ", name=" + this.getName() + ", type=" + this.getType() + ", num=" + this.getNum() + ", price=" + this.getPrice() + ", sortNo=" + this.getSortNo() + ", lvLimit=" + this.getLvLimit() + ", zsLimit=" + this.getZsLimit() + ", mustVip=" + this.getMustVip() + ", quotaLimit=" + this.getQuotaLimit() + ", discount=" + this.getDiscount() + ", recommend=" + this.getRecommend() + ")";
    }
}
