package com.kitty.system.init.domain;

import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_drug")
public class SysDrug implements Serializable
{
    @Comment("编号")
    @Column("serial")
    private Integer serial;
    @Comment("名称")
    @Column("name")
    private String name;
    @Comment("药品回复的气血值")
    @Column("drug_add_hp_value")
    private Integer drugAddHpValue;
    @Comment("药品回复的气血百分比")
    @Column("drug_add_hp_percent")
    private Integer drugAddHpPercent;
    @Comment("药品储备的气血值")
    @Column("drug_add_hp_stored")
    private Integer drugAddHpStored;
    @Comment("药品回复的法力值")
    @Column("drug_add_mp_value")
    private Integer drugAddMpValue;
    @Comment("药品回复的法力百分比")
    @Column("drug_add_mp_percent")
    private Integer drugAddMpPercent;
    @Comment("药品储备的法力值")
    @Column("drug_add_mp_stored")
    private Integer drugAddMpStored;
    @Comment("物品外观")
    @Column("item_shape")
    private Integer itemShape;
    @Comment("价格")
    @Column("price")
    private Integer price;
    @Comment("交易货币：1：仙玉，2：银两，3：师贡")
    @Column("reprice")
    private Integer reprice;
    @Comment("重叠数量")
    @Column("can_merge")
    private Integer canMerge;
    @Comment("品阶")
    @Column("item_pj")
    private Integer itemPj;
    @Comment("1：血药，2：蓝药，3：血蓝双药，4：特效道具")
    @Column("show_class")
    private Integer showClass;
    @Comment("目标类型")
    @Column("target_type")
    private Integer targetType;
    @Comment("使用场景")
    @Column("use_type")
    private Integer useType;
    @Comment("状态编号")
    @Column("status")
    private List<Integer> status;
    
    public SysDrug() {
    }
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Integer getDrugAddHpValue() {
        return this.drugAddHpValue;
    }
    
    public Integer getDrugAddHpPercent() {
        return this.drugAddHpPercent;
    }
    
    public Integer getDrugAddHpStored() {
        return this.drugAddHpStored;
    }
    
    public Integer getDrugAddMpValue() {
        return this.drugAddMpValue;
    }
    
    public Integer getDrugAddMpPercent() {
        return this.drugAddMpPercent;
    }
    
    public Integer getDrugAddMpStored() {
        return this.drugAddMpStored;
    }
    
    public Integer getItemShape() {
        return this.itemShape;
    }
    
    public Integer getPrice() {
        return this.price;
    }
    
    public Integer getReprice() {
        return this.reprice;
    }
    
    public Integer getCanMerge() {
        return this.canMerge;
    }
    
    public Integer getItemPj() {
        return this.itemPj;
    }
    
    public Integer getShowClass() {
        return this.showClass;
    }
    
    public Integer getTargetType() {
        return this.targetType;
    }
    
    public Integer getUseType() {
        return this.useType;
    }
    
    public List<Integer> getStatus() {
        return this.status;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setDrugAddHpValue(final Integer drugAddHpValue) {
        this.drugAddHpValue = drugAddHpValue;
    }
    
    public void setDrugAddHpPercent(final Integer drugAddHpPercent) {
        this.drugAddHpPercent = drugAddHpPercent;
    }
    
    public void setDrugAddHpStored(final Integer drugAddHpStored) {
        this.drugAddHpStored = drugAddHpStored;
    }
    
    public void setDrugAddMpValue(final Integer drugAddMpValue) {
        this.drugAddMpValue = drugAddMpValue;
    }
    
    public void setDrugAddMpPercent(final Integer drugAddMpPercent) {
        this.drugAddMpPercent = drugAddMpPercent;
    }
    
    public void setDrugAddMpStored(final Integer drugAddMpStored) {
        this.drugAddMpStored = drugAddMpStored;
    }
    
    public void setItemShape(final Integer itemShape) {
        this.itemShape = itemShape;
    }
    
    public void setPrice(final Integer price) {
        this.price = price;
    }
    
    public void setReprice(final Integer reprice) {
        this.reprice = reprice;
    }
    
    public void setCanMerge(final Integer canMerge) {
        this.canMerge = canMerge;
    }
    
    public void setItemPj(final Integer itemPj) {
        this.itemPj = itemPj;
    }
    
    public void setShowClass(final Integer showClass) {
        this.showClass = showClass;
    }
    
    public void setTargetType(final Integer targetType) {
        this.targetType = targetType;
    }
    
    public void setUseType(final Integer useType) {
        this.useType = useType;
    }
    
    public void setStatus(final List<Integer> status) {
        this.status = status;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDrug)) {
            return false;
        }
        SysDrug other = (SysDrug)o;
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
        Object this$drugAddHpValue = this.getDrugAddHpValue();
        Object other$drugAddHpValue = other.getDrugAddHpValue();
        if (!((this$drugAddHpValue != null) ? this$drugAddHpValue.equals(other$drugAddHpValue) : (other$drugAddHpValue == null))) {
            return false;
        }
        Object this$drugAddHpPercent = this.getDrugAddHpPercent();
        Object other$drugAddHpPercent = other.getDrugAddHpPercent();
        if (!((this$drugAddHpPercent != null) ? this$drugAddHpPercent.equals(other$drugAddHpPercent) : (other$drugAddHpPercent == null))) {
            return false;
        }
        Object this$drugAddHpStored = this.getDrugAddHpStored();
        Object other$drugAddHpStored = other.getDrugAddHpStored();
        if (!((this$drugAddHpStored != null) ? this$drugAddHpStored.equals(other$drugAddHpStored) : (other$drugAddHpStored == null))) {
            return false;
        }
        Object this$drugAddMpValue = this.getDrugAddMpValue();
        Object other$drugAddMpValue = other.getDrugAddMpValue();
        if (!((this$drugAddMpValue != null) ? this$drugAddMpValue.equals(other$drugAddMpValue) : (other$drugAddMpValue == null))) {
            return false;
        }
        Object this$drugAddMpPercent = this.getDrugAddMpPercent();
        Object other$drugAddMpPercent = other.getDrugAddMpPercent();
        if (!((this$drugAddMpPercent != null) ? this$drugAddMpPercent.equals(other$drugAddMpPercent) : (other$drugAddMpPercent == null))) {
            return false;
        }
        Object this$drugAddMpStored = this.getDrugAddMpStored();
        Object other$drugAddMpStored = other.getDrugAddMpStored();
        if (!((this$drugAddMpStored != null) ? this$drugAddMpStored.equals(other$drugAddMpStored) : (other$drugAddMpStored == null))) {
            return false;
        }
        Object this$itemShape = this.getItemShape();
        Object other$itemShape = other.getItemShape();
        if (!((this$itemShape != null) ? this$itemShape.equals(other$itemShape) : (other$itemShape == null))) {
            return false;
        }
        Object this$price = this.getPrice();
        Object other$price = other.getPrice();
        if (!((this$price != null) ? this$price.equals(other$price) : (other$price == null))) {
            return false;
        }
        Object this$reprice = this.getReprice();
        Object other$reprice = other.getReprice();
        if (!((this$reprice != null) ? this$reprice.equals(other$reprice) : (other$reprice == null))) {
            return false;
        }
        Object this$canMerge = this.getCanMerge();
        Object other$canMerge = other.getCanMerge();
        if (!((this$canMerge != null) ? this$canMerge.equals(other$canMerge) : (other$canMerge == null))) {
            return false;
        }
        Object this$itemPj = this.getItemPj();
        Object other$itemPj = other.getItemPj();
        if (!((this$itemPj != null) ? this$itemPj.equals(other$itemPj) : (other$itemPj == null))) {
            return false;
        }
        Object this$showClass = this.getShowClass();
        Object other$showClass = other.getShowClass();
        if (!((this$showClass != null) ? this$showClass.equals(other$showClass) : (other$showClass == null))) {
            return false;
        }
        Object this$targetType = this.getTargetType();
        Object other$targetType = other.getTargetType();
        if (!((this$targetType != null) ? this$targetType.equals(other$targetType) : (other$targetType == null))) {
            return false;
        }
        Object this$useType = this.getUseType();
        Object other$useType = other.getUseType();
        if (!((this$useType != null) ? this$useType.equals(other$useType) : (other$useType == null))) {
            return false;
        }
        Object this$status = this.getStatus();
        Object other$status = other.getStatus();
        return (this$status != null) ? this$status.equals(other$status) : (other$status == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDrug;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        Object $drugAddHpValue = this.getDrugAddHpValue();
        result = result * 59 + (($drugAddHpValue == null) ? 43 : $drugAddHpValue.hashCode());
        Object $drugAddHpPercent = this.getDrugAddHpPercent();
        result = result * 59 + (($drugAddHpPercent == null) ? 43 : $drugAddHpPercent.hashCode());
        Object $drugAddHpStored = this.getDrugAddHpStored();
        result = result * 59 + (($drugAddHpStored == null) ? 43 : $drugAddHpStored.hashCode());
        Object $drugAddMpValue = this.getDrugAddMpValue();
        result = result * 59 + (($drugAddMpValue == null) ? 43 : $drugAddMpValue.hashCode());
        Object $drugAddMpPercent = this.getDrugAddMpPercent();
        result = result * 59 + (($drugAddMpPercent == null) ? 43 : $drugAddMpPercent.hashCode());
        Object $drugAddMpStored = this.getDrugAddMpStored();
        result = result * 59 + (($drugAddMpStored == null) ? 43 : $drugAddMpStored.hashCode());
        Object $itemShape = this.getItemShape();
        result = result * 59 + (($itemShape == null) ? 43 : $itemShape.hashCode());
        Object $price = this.getPrice();
        result = result * 59 + (($price == null) ? 43 : $price.hashCode());
        Object $reprice = this.getReprice();
        result = result * 59 + (($reprice == null) ? 43 : $reprice.hashCode());
        Object $canMerge = this.getCanMerge();
        result = result * 59 + (($canMerge == null) ? 43 : $canMerge.hashCode());
        Object $itemPj = this.getItemPj();
        result = result * 59 + (($itemPj == null) ? 43 : $itemPj.hashCode());
        Object $showClass = this.getShowClass();
        result = result * 59 + (($showClass == null) ? 43 : $showClass.hashCode());
        Object $targetType = this.getTargetType();
        result = result * 59 + (($targetType == null) ? 43 : $targetType.hashCode());
        Object $useType = this.getUseType();
        result = result * 59 + (($useType == null) ? 43 : $useType.hashCode());
        Object $status = this.getStatus();
        result = result * 59 + (($status == null) ? 43 : $status.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDrug(serial=" + this.getSerial() + ", name=" + this.getName() + ", drugAddHpValue=" + this.getDrugAddHpValue() + ", drugAddHpPercent=" + this.getDrugAddHpPercent() + ", drugAddHpStored=" + this.getDrugAddHpStored() + ", drugAddMpValue=" + this.getDrugAddMpValue() + ", drugAddMpPercent=" + this.getDrugAddMpPercent() + ", drugAddMpStored=" + this.getDrugAddMpStored() + ", itemShape=" + this.getItemShape() + ", price=" + this.getPrice() + ", reprice=" + this.getReprice() + ", canMerge=" + this.getCanMerge() + ", itemPj=" + this.getItemPj() + ", showClass=" + this.getShowClass() + ", targetType=" + this.getTargetType() + ", useType=" + this.getUseType() + ", status=" + this.getStatus() + ")";
    }
}
