package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("t_data_stall")
public class SysDataStall implements Serializable
{
    @Column("serial")
    private Integer serial;
    @Comment("商品菜单编号")
    @Column("stall_menu_serial")
    private Integer stallMenuSerial;
    @Comment("货币类型：1.仙玉，2.银币，3.贡币")
    @Column("type")
    private Integer type;
    @Comment("商品名称")
    @Column("name")
    private String name;
    @Comment("分类")
    @Column("category")
    private int category;
    @Comment("分类名称")
    @Column("category_name")
    private String categoryName;
    @Comment("基础价格")
    @Column("basics_price")
    private Long basicsPrice;
    @Comment("涨值")
    @Column("rise")
    private double rise;
    @Comment("跌值")
    @Column("fall")
    private double fall;
    @Comment("手续费比例")
    @Column("revenue_ratio")
    private double revenueRatio;
    @Comment("物品类型：0.装备 1.道具 2.宠物 3.变身卡 4.天策符")
    @Column("is_item")
    private byte isItem;
    @Comment("可售时间：秒(s)")
    @Column("expire_time")
    private Integer expireTime;
    @Comment("公示期：秒(s)")
    @Column("publicity_period")
    private Integer publicityPeriod;
    @Comment("审核时间：秒(s)")
    @Column("audit_time")
    private Integer auditTime;
    @Comment("是否校验张跌值：0.否 1.是")
    @Column("is_check")
    private byte isCheck;
    
    public SysDataStall() {
    }
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public Integer getStallMenuSerial() {
        return this.stallMenuSerial;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getCategory() {
        return this.category;
    }
    
    public String getCategoryName() {
        return this.categoryName;
    }
    
    public Long getBasicsPrice() {
        return this.basicsPrice;
    }
    
    public double getRise() {
        return this.rise;
    }
    
    public double getFall() {
        return this.fall;
    }
    
    public double getRevenueRatio() {
        return this.revenueRatio;
    }
    
    public byte getIsItem() {
        return this.isItem;
    }
    
    public Integer getExpireTime() {
        return this.expireTime;
    }
    
    public Integer getPublicityPeriod() {
        return this.publicityPeriod;
    }
    
    public Integer getAuditTime() {
        return this.auditTime;
    }
    
    public byte getIsCheck() {
        return this.isCheck;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setStallMenuSerial(final Integer stallMenuSerial) {
        this.stallMenuSerial = stallMenuSerial;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setCategory(final int category) {
        this.category = category;
    }
    
    public void setCategoryName(final String categoryName) {
        this.categoryName = categoryName;
    }
    
    public void setBasicsPrice(final Long basicsPrice) {
        this.basicsPrice = basicsPrice;
    }
    
    public void setRise(final double rise) {
        this.rise = rise;
    }
    
    public void setFall(final double fall) {
        this.fall = fall;
    }
    
    public void setRevenueRatio(final double revenueRatio) {
        this.revenueRatio = revenueRatio;
    }
    
    public void setIsItem(final byte isItem) {
        this.isItem = isItem;
    }
    
    public void setExpireTime(final Integer expireTime) {
        this.expireTime = expireTime;
    }
    
    public void setPublicityPeriod(final Integer publicityPeriod) {
        this.publicityPeriod = publicityPeriod;
    }
    
    public void setAuditTime(final Integer auditTime) {
        this.auditTime = auditTime;
    }
    
    public void setIsCheck(final byte isCheck) {
        this.isCheck = isCheck;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataStall)) {
            return false;
        }
        SysDataStall other = (SysDataStall)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$serial = this.getSerial();
        Object other$serial = other.getSerial();
        if (!((this$serial != null) ? this$serial.equals(other$serial) : (other$serial == null))) {
            return false;
        }
        Object this$stallMenuSerial = this.getStallMenuSerial();
        Object other$stallMenuSerial = other.getStallMenuSerial();
        if (!((this$stallMenuSerial != null) ? this$stallMenuSerial.equals(other$stallMenuSerial) : (other$stallMenuSerial == null))) {
            return false;
        }
        Object this$type = this.getType();
        Object other$type = other.getType();
        if (!((this$type != null) ? this$type.equals(other$type) : (other$type == null))) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        if (this.getCategory() != other.getCategory()) {
            return false;
        }
        Object this$categoryName = this.getCategoryName();
        Object other$categoryName = other.getCategoryName();
        if (!((this$categoryName != null) ? this$categoryName.equals(other$categoryName) : (other$categoryName == null))) {
            return false;
        }
        Object this$basicsPrice = this.getBasicsPrice();
        Object other$basicsPrice = other.getBasicsPrice();
        if (!((this$basicsPrice != null) ? this$basicsPrice.equals(other$basicsPrice) : (other$basicsPrice == null))) {
            return false;
        }
        if (Double.compare(this.getRise(), other.getRise()) != 0) {
            return false;
        }
        if (Double.compare(this.getFall(), other.getFall()) != 0) {
            return false;
        }
        if (Double.compare(this.getRevenueRatio(), other.getRevenueRatio()) != 0) {
            return false;
        }
        if (this.getIsItem() != other.getIsItem()) {
            return false;
        }
        Object this$expireTime = this.getExpireTime();
        Object other$expireTime = other.getExpireTime();
        if (!((this$expireTime != null) ? this$expireTime.equals(other$expireTime) : (other$expireTime == null))) {
            return false;
        }
        Object this$publicityPeriod = this.getPublicityPeriod();
        Object other$publicityPeriod = other.getPublicityPeriod();
        if (!((this$publicityPeriod != null) ? this$publicityPeriod.equals(other$publicityPeriod) : (other$publicityPeriod == null))) {
            return false;
        }
        Object this$auditTime = this.getAuditTime();
        Object other$auditTime = other.getAuditTime();
        return ((this$auditTime != null) ? this$auditTime.equals(other$auditTime) : (other$auditTime == null)) && this.getIsCheck() == other.getIsCheck();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataStall;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        Object $stallMenuSerial = this.getStallMenuSerial();
        result = result * 59 + (($stallMenuSerial == null) ? 43 : $stallMenuSerial.hashCode());
        Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getCategory();
        Object $categoryName = this.getCategoryName();
        result = result * 59 + (($categoryName == null) ? 43 : $categoryName.hashCode());
        Object $basicsPrice = this.getBasicsPrice();
        result = result * 59 + (($basicsPrice == null) ? 43 : $basicsPrice.hashCode());
        long $rise = Double.doubleToLongBits(this.getRise());
        result = result * 59 + (int)($rise >>> 32 ^ $rise);
        long $fall = Double.doubleToLongBits(this.getFall());
        result = result * 59 + (int)($fall >>> 32 ^ $fall);
        long $revenueRatio = Double.doubleToLongBits(this.getRevenueRatio());
        result = result * 59 + (int)($revenueRatio >>> 32 ^ $revenueRatio);
        result = result * 59 + this.getIsItem();
        Object $expireTime = this.getExpireTime();
        result = result * 59 + (($expireTime == null) ? 43 : $expireTime.hashCode());
        Object $publicityPeriod = this.getPublicityPeriod();
        result = result * 59 + (($publicityPeriod == null) ? 43 : $publicityPeriod.hashCode());
        Object $auditTime = this.getAuditTime();
        result = result * 59 + (($auditTime == null) ? 43 : $auditTime.hashCode());
        result = result * 59 + this.getIsCheck();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataStall(serial=" + this.getSerial() + ", stallMenuSerial=" + this.getStallMenuSerial() + ", type=" + this.getType() + ", name=" + this.getName() + ", category=" + this.getCategory() + ", categoryName=" + this.getCategoryName() + ", basicsPrice=" + this.getBasicsPrice() + ", rise=" + this.getRise() + ", fall=" + this.getFall() + ", revenueRatio=" + this.getRevenueRatio() + ", isItem=" + (int)this.getIsItem() + ", expireTime=" + this.getExpireTime() + ", publicityPeriod=" + this.getPublicityPeriod() + ", auditTime=" + this.getAuditTime() + ", isCheck=" + (int)this.getIsCheck() + ")";
    }
}
