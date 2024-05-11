
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_stuff")
public class SysDataStuff implements Serializable
{
    @Comment("")
    @Column("id")
    private Integer id;
    @Comment("道具编号")
    @Column("serial")
    private Integer serial;
    @Comment("道具名称")
    @Column("name")
    private String name;
    @Comment("价格")
    @Column("price")
    private Integer price;
    @Comment("回收价格")
    @Column("reprice")
    private Integer reprice;
    @Comment("道具外形")
    @Column("shape")
    private Integer shape;
    @Comment("货币类型")
    @Column("cost_type")
    private Integer costType;
    @Comment("背包重叠数量")
    @Column("can_merge")
    private Integer canMerge;
    @Comment("批量")
    @Column("batches")
    private Integer batches;
    @Comment("提示")
    @Column("tips")
    private String tips;
    @Comment("价值")
    @Column("value")
    private Integer value;
    @Comment("道具品阶")
    @Column("item_pj")
    private Integer itemPJ;
    @Comment("使用次数限制")
    @Column("use_cnt_limit")
    private Integer useCntLimit;
    @Comment("转生需求")
    @Column("need_zs")
    private Integer needZs;
    @Comment("等级需求")
    @Column("need_lv")
    private Integer needLv;
    @Comment("是否开启")
    @Column("is_open")
    private Integer isOpen;
    @Column("type")
    private Integer type;
    @Comment("适用对象编号")
    @Column("apply_object_serial")
    private Integer applyObjectSerial;
    
    public Integer getId() {
        return this.id;
    }
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Integer getPrice() {
        return this.price;
    }
    
    public Integer getReprice() {
        return this.reprice;
    }
    
    public Integer getShape() {
        return this.shape;
    }
    
    public Integer getCostType() {
        return this.costType;
    }
    
    public Integer getCanMerge() {
        return this.canMerge;
    }
    
    public Integer getBatches() {
        return this.batches;
    }
    
    public String getTips() {
        return this.tips;
    }
    
    public Integer getValue() {
        return this.value;
    }
    
    public Integer getItemPJ() {
        return this.itemPJ;
    }
    
    public Integer getUseCntLimit() {
        return this.useCntLimit;
    }
    
    public Integer getNeedZs() {
        return this.needZs;
    }
    
    public Integer getNeedLv() {
        return this.needLv;
    }
    
    public Integer getIsOpen() {
        return this.isOpen;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public Integer getApplyObjectSerial() {
        return this.applyObjectSerial;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setPrice(final Integer price) {
        this.price = price;
    }
    
    public void setReprice(final Integer reprice) {
        this.reprice = reprice;
    }
    
    public void setShape(final Integer shape) {
        this.shape = shape;
    }
    
    public void setCostType(final Integer costType) {
        this.costType = costType;
    }
    
    public void setCanMerge(final Integer canMerge) {
        this.canMerge = canMerge;
    }
    
    public void setBatches(final Integer batches) {
        this.batches = batches;
    }
    
    public void setTips(final String tips) {
        this.tips = tips;
    }
    
    public void setValue(final Integer value) {
        this.value = value;
    }
    
    public void setItemPJ(final Integer itemPJ) {
        this.itemPJ = itemPJ;
    }
    
    public void setUseCntLimit(final Integer useCntLimit) {
        this.useCntLimit = useCntLimit;
    }
    
    public void setNeedZs(final Integer needZs) {
        this.needZs = needZs;
    }
    
    public void setNeedLv(final Integer needLv) {
        this.needLv = needLv;
    }
    
    public void setIsOpen(final Integer isOpen) {
        this.isOpen = isOpen;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    public void setApplyObjectSerial(final Integer applyObjectSerial) {
        this.applyObjectSerial = applyObjectSerial;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataStuff)) {
            return false;
        }
        final SysDataStuff other = (SysDataStuff)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        Label_0065: {
            if (this$id == null) {
                if (other$id == null) {
                    break Label_0065;
                }
            }
            else if (this$id.equals(other$id)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$serial = this.getSerial();
        final Object other$serial = other.getSerial();
        Label_0102: {
            if (this$serial == null) {
                if (other$serial == null) {
                    break Label_0102;
                }
            }
            else if (this$serial.equals(other$serial)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0139: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0139;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$price = this.getPrice();
        final Object other$price = other.getPrice();
        Label_0176: {
            if (this$price == null) {
                if (other$price == null) {
                    break Label_0176;
                }
            }
            else if (this$price.equals(other$price)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$reprice = this.getReprice();
        final Object other$reprice = other.getReprice();
        Label_0213: {
            if (this$reprice == null) {
                if (other$reprice == null) {
                    break Label_0213;
                }
            }
            else if (this$reprice.equals(other$reprice)) {
                break Label_0213;
            }
            return false;
        }
        final Object this$shape = this.getShape();
        final Object other$shape = other.getShape();
        Label_0250: {
            if (this$shape == null) {
                if (other$shape == null) {
                    break Label_0250;
                }
            }
            else if (this$shape.equals(other$shape)) {
                break Label_0250;
            }
            return false;
        }
        final Object this$costType = this.getCostType();
        final Object other$costType = other.getCostType();
        Label_0287: {
            if (this$costType == null) {
                if (other$costType == null) {
                    break Label_0287;
                }
            }
            else if (this$costType.equals(other$costType)) {
                break Label_0287;
            }
            return false;
        }
        final Object this$canMerge = this.getCanMerge();
        final Object other$canMerge = other.getCanMerge();
        Label_0324: {
            if (this$canMerge == null) {
                if (other$canMerge == null) {
                    break Label_0324;
                }
            }
            else if (this$canMerge.equals(other$canMerge)) {
                break Label_0324;
            }
            return false;
        }
        final Object this$batches = this.getBatches();
        final Object other$batches = other.getBatches();
        Label_0361: {
            if (this$batches == null) {
                if (other$batches == null) {
                    break Label_0361;
                }
            }
            else if (this$batches.equals(other$batches)) {
                break Label_0361;
            }
            return false;
        }
        final Object this$tips = this.getTips();
        final Object other$tips = other.getTips();
        Label_0398: {
            if (this$tips == null) {
                if (other$tips == null) {
                    break Label_0398;
                }
            }
            else if (this$tips.equals(other$tips)) {
                break Label_0398;
            }
            return false;
        }
        final Object this$value = this.getValue();
        final Object other$value = other.getValue();
        Label_0435: {
            if (this$value == null) {
                if (other$value == null) {
                    break Label_0435;
                }
            }
            else if (this$value.equals(other$value)) {
                break Label_0435;
            }
            return false;
        }
        final Object this$itemPJ = this.getItemPJ();
        final Object other$itemPJ = other.getItemPJ();
        Label_0472: {
            if (this$itemPJ == null) {
                if (other$itemPJ == null) {
                    break Label_0472;
                }
            }
            else if (this$itemPJ.equals(other$itemPJ)) {
                break Label_0472;
            }
            return false;
        }
        final Object this$useCntLimit = this.getUseCntLimit();
        final Object other$useCntLimit = other.getUseCntLimit();
        Label_0509: {
            if (this$useCntLimit == null) {
                if (other$useCntLimit == null) {
                    break Label_0509;
                }
            }
            else if (this$useCntLimit.equals(other$useCntLimit)) {
                break Label_0509;
            }
            return false;
        }
        final Object this$needZs = this.getNeedZs();
        final Object other$needZs = other.getNeedZs();
        Label_0546: {
            if (this$needZs == null) {
                if (other$needZs == null) {
                    break Label_0546;
                }
            }
            else if (this$needZs.equals(other$needZs)) {
                break Label_0546;
            }
            return false;
        }
        final Object this$needLv = this.getNeedLv();
        final Object other$needLv = other.getNeedLv();
        Label_0583: {
            if (this$needLv == null) {
                if (other$needLv == null) {
                    break Label_0583;
                }
            }
            else if (this$needLv.equals(other$needLv)) {
                break Label_0583;
            }
            return false;
        }
        final Object this$isOpen = this.getIsOpen();
        final Object other$isOpen = other.getIsOpen();
        Label_0620: {
            if (this$isOpen == null) {
                if (other$isOpen == null) {
                    break Label_0620;
                }
            }
            else if (this$isOpen.equals(other$isOpen)) {
                break Label_0620;
            }
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0657: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0657;
                }
            }
            else if (this$type.equals(other$type)) {
                break Label_0657;
            }
            return false;
        }
        final Object this$applyObjectSerial = this.getApplyObjectSerial();
        final Object other$applyObjectSerial = other.getApplyObjectSerial();
        if (this$applyObjectSerial == null) {
            if (other$applyObjectSerial == null) {
                return true;
            }
        }
        else if (this$applyObjectSerial.equals(other$applyObjectSerial)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataStuff;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        final Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $price = this.getPrice();
        result = result * 59 + (($price == null) ? 43 : $price.hashCode());
        final Object $reprice = this.getReprice();
        result = result * 59 + (($reprice == null) ? 43 : $reprice.hashCode());
        final Object $shape = this.getShape();
        result = result * 59 + (($shape == null) ? 43 : $shape.hashCode());
        final Object $costType = this.getCostType();
        result = result * 59 + (($costType == null) ? 43 : $costType.hashCode());
        final Object $canMerge = this.getCanMerge();
        result = result * 59 + (($canMerge == null) ? 43 : $canMerge.hashCode());
        final Object $batches = this.getBatches();
        result = result * 59 + (($batches == null) ? 43 : $batches.hashCode());
        final Object $tips = this.getTips();
        result = result * 59 + (($tips == null) ? 43 : $tips.hashCode());
        final Object $value = this.getValue();
        result = result * 59 + (($value == null) ? 43 : $value.hashCode());
        final Object $itemPJ = this.getItemPJ();
        result = result * 59 + (($itemPJ == null) ? 43 : $itemPJ.hashCode());
        final Object $useCntLimit = this.getUseCntLimit();
        result = result * 59 + (($useCntLimit == null) ? 43 : $useCntLimit.hashCode());
        final Object $needZs = this.getNeedZs();
        result = result * 59 + (($needZs == null) ? 43 : $needZs.hashCode());
        final Object $needLv = this.getNeedLv();
        result = result * 59 + (($needLv == null) ? 43 : $needLv.hashCode());
        final Object $isOpen = this.getIsOpen();
        result = result * 59 + (($isOpen == null) ? 43 : $isOpen.hashCode());
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final Object $applyObjectSerial = this.getApplyObjectSerial();
        result = result * 59 + (($applyObjectSerial == null) ? 43 : $applyObjectSerial.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataStuff(id=" + this.getId() + ", serial=" + this.getSerial() + ", name=" + this.getName() + ", price=" + this.getPrice() + ", reprice=" + this.getReprice() + ", shape=" + this.getShape() + ", costType=" + this.getCostType() + ", canMerge=" + this.getCanMerge() + ", batches=" + this.getBatches() + ", tips=" + this.getTips() + ", value=" + this.getValue() + ", itemPJ=" + this.getItemPJ() + ", useCntLimit=" + this.getUseCntLimit() + ", needZs=" + this.getNeedZs() + ", needLv=" + this.getNeedLv() + ", isOpen=" + this.getIsOpen() + ", type=" + this.getType() + ", applyObjectSerial=" + this.getApplyObjectSerial() + ")";
    }
}
