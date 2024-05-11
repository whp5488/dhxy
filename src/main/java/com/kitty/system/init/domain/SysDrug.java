
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
        final SysDrug other = (SysDrug)o;
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
        final Object this$drugAddHpValue = this.getDrugAddHpValue();
        final Object other$drugAddHpValue = other.getDrugAddHpValue();
        Label_0139: {
            if (this$drugAddHpValue == null) {
                if (other$drugAddHpValue == null) {
                    break Label_0139;
                }
            }
            else if (this$drugAddHpValue.equals(other$drugAddHpValue)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$drugAddHpPercent = this.getDrugAddHpPercent();
        final Object other$drugAddHpPercent = other.getDrugAddHpPercent();
        Label_0176: {
            if (this$drugAddHpPercent == null) {
                if (other$drugAddHpPercent == null) {
                    break Label_0176;
                }
            }
            else if (this$drugAddHpPercent.equals(other$drugAddHpPercent)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$drugAddHpStored = this.getDrugAddHpStored();
        final Object other$drugAddHpStored = other.getDrugAddHpStored();
        Label_0213: {
            if (this$drugAddHpStored == null) {
                if (other$drugAddHpStored == null) {
                    break Label_0213;
                }
            }
            else if (this$drugAddHpStored.equals(other$drugAddHpStored)) {
                break Label_0213;
            }
            return false;
        }
        final Object this$drugAddMpValue = this.getDrugAddMpValue();
        final Object other$drugAddMpValue = other.getDrugAddMpValue();
        Label_0250: {
            if (this$drugAddMpValue == null) {
                if (other$drugAddMpValue == null) {
                    break Label_0250;
                }
            }
            else if (this$drugAddMpValue.equals(other$drugAddMpValue)) {
                break Label_0250;
            }
            return false;
        }
        final Object this$drugAddMpPercent = this.getDrugAddMpPercent();
        final Object other$drugAddMpPercent = other.getDrugAddMpPercent();
        Label_0287: {
            if (this$drugAddMpPercent == null) {
                if (other$drugAddMpPercent == null) {
                    break Label_0287;
                }
            }
            else if (this$drugAddMpPercent.equals(other$drugAddMpPercent)) {
                break Label_0287;
            }
            return false;
        }
        final Object this$drugAddMpStored = this.getDrugAddMpStored();
        final Object other$drugAddMpStored = other.getDrugAddMpStored();
        Label_0324: {
            if (this$drugAddMpStored == null) {
                if (other$drugAddMpStored == null) {
                    break Label_0324;
                }
            }
            else if (this$drugAddMpStored.equals(other$drugAddMpStored)) {
                break Label_0324;
            }
            return false;
        }
        final Object this$itemShape = this.getItemShape();
        final Object other$itemShape = other.getItemShape();
        Label_0361: {
            if (this$itemShape == null) {
                if (other$itemShape == null) {
                    break Label_0361;
                }
            }
            else if (this$itemShape.equals(other$itemShape)) {
                break Label_0361;
            }
            return false;
        }
        final Object this$price = this.getPrice();
        final Object other$price = other.getPrice();
        Label_0398: {
            if (this$price == null) {
                if (other$price == null) {
                    break Label_0398;
                }
            }
            else if (this$price.equals(other$price)) {
                break Label_0398;
            }
            return false;
        }
        final Object this$reprice = this.getReprice();
        final Object other$reprice = other.getReprice();
        Label_0435: {
            if (this$reprice == null) {
                if (other$reprice == null) {
                    break Label_0435;
                }
            }
            else if (this$reprice.equals(other$reprice)) {
                break Label_0435;
            }
            return false;
        }
        final Object this$canMerge = this.getCanMerge();
        final Object other$canMerge = other.getCanMerge();
        Label_0472: {
            if (this$canMerge == null) {
                if (other$canMerge == null) {
                    break Label_0472;
                }
            }
            else if (this$canMerge.equals(other$canMerge)) {
                break Label_0472;
            }
            return false;
        }
        final Object this$itemPj = this.getItemPj();
        final Object other$itemPj = other.getItemPj();
        Label_0509: {
            if (this$itemPj == null) {
                if (other$itemPj == null) {
                    break Label_0509;
                }
            }
            else if (this$itemPj.equals(other$itemPj)) {
                break Label_0509;
            }
            return false;
        }
        final Object this$showClass = this.getShowClass();
        final Object other$showClass = other.getShowClass();
        Label_0546: {
            if (this$showClass == null) {
                if (other$showClass == null) {
                    break Label_0546;
                }
            }
            else if (this$showClass.equals(other$showClass)) {
                break Label_0546;
            }
            return false;
        }
        final Object this$targetType = this.getTargetType();
        final Object other$targetType = other.getTargetType();
        Label_0583: {
            if (this$targetType == null) {
                if (other$targetType == null) {
                    break Label_0583;
                }
            }
            else if (this$targetType.equals(other$targetType)) {
                break Label_0583;
            }
            return false;
        }
        final Object this$useType = this.getUseType();
        final Object other$useType = other.getUseType();
        Label_0620: {
            if (this$useType == null) {
                if (other$useType == null) {
                    break Label_0620;
                }
            }
            else if (this$useType.equals(other$useType)) {
                break Label_0620;
            }
            return false;
        }
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        if (this$status == null) {
            if (other$status == null) {
                return true;
            }
        }
        else if (this$status.equals(other$status)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDrug;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $drugAddHpValue = this.getDrugAddHpValue();
        result = result * 59 + (($drugAddHpValue == null) ? 43 : $drugAddHpValue.hashCode());
        final Object $drugAddHpPercent = this.getDrugAddHpPercent();
        result = result * 59 + (($drugAddHpPercent == null) ? 43 : $drugAddHpPercent.hashCode());
        final Object $drugAddHpStored = this.getDrugAddHpStored();
        result = result * 59 + (($drugAddHpStored == null) ? 43 : $drugAddHpStored.hashCode());
        final Object $drugAddMpValue = this.getDrugAddMpValue();
        result = result * 59 + (($drugAddMpValue == null) ? 43 : $drugAddMpValue.hashCode());
        final Object $drugAddMpPercent = this.getDrugAddMpPercent();
        result = result * 59 + (($drugAddMpPercent == null) ? 43 : $drugAddMpPercent.hashCode());
        final Object $drugAddMpStored = this.getDrugAddMpStored();
        result = result * 59 + (($drugAddMpStored == null) ? 43 : $drugAddMpStored.hashCode());
        final Object $itemShape = this.getItemShape();
        result = result * 59 + (($itemShape == null) ? 43 : $itemShape.hashCode());
        final Object $price = this.getPrice();
        result = result * 59 + (($price == null) ? 43 : $price.hashCode());
        final Object $reprice = this.getReprice();
        result = result * 59 + (($reprice == null) ? 43 : $reprice.hashCode());
        final Object $canMerge = this.getCanMerge();
        result = result * 59 + (($canMerge == null) ? 43 : $canMerge.hashCode());
        final Object $itemPj = this.getItemPj();
        result = result * 59 + (($itemPj == null) ? 43 : $itemPj.hashCode());
        final Object $showClass = this.getShowClass();
        result = result * 59 + (($showClass == null) ? 43 : $showClass.hashCode());
        final Object $targetType = this.getTargetType();
        result = result * 59 + (($targetType == null) ? 43 : $targetType.hashCode());
        final Object $useType = this.getUseType();
        result = result * 59 + (($useType == null) ? 43 : $useType.hashCode());
        final Object $status = this.getStatus();
        result = result * 59 + (($status == null) ? 43 : $status.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDrug(serial=" + this.getSerial() + ", name=" + this.getName() + ", drugAddHpValue=" + this.getDrugAddHpValue() + ", drugAddHpPercent=" + this.getDrugAddHpPercent() + ", drugAddHpStored=" + this.getDrugAddHpStored() + ", drugAddMpValue=" + this.getDrugAddMpValue() + ", drugAddMpPercent=" + this.getDrugAddMpPercent() + ", drugAddMpStored=" + this.getDrugAddMpStored() + ", itemShape=" + this.getItemShape() + ", price=" + this.getPrice() + ", reprice=" + this.getReprice() + ", canMerge=" + this.getCanMerge() + ", itemPj=" + this.getItemPj() + ", showClass=" + this.getShowClass() + ", targetType=" + this.getTargetType() + ", useType=" + this.getUseType() + ", status=" + this.getStatus() + ")";
    }
}
