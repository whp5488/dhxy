package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Table("data_other_item")
public class SysOtherItem
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
    @Comment("参数类型：如果是道具：0.活动，1.仙玉，2.银两，3.师贡。装备：穿戴部位")
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
    @Column("dict")
    private String dict;
    @Column("param")
    @ColDefine(customType = "text")
    private String param;
    
    public SysOtherItem() {
    }
    
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
    
    public String getDict() {
        return this.dict;
    }
    
    public String getParam() {
        return this.param;
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
    
    public void setDict(final String dict) {
        this.dict = dict;
    }
    
    public void setParam(final String param) {
        this.param = param;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysOtherItem)) {
            return false;
        }
        SysOtherItem other = (SysOtherItem)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
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
        Object this$shape = this.getShape();
        Object other$shape = other.getShape();
        if (!((this$shape != null) ? this$shape.equals(other$shape) : (other$shape == null))) {
            return false;
        }
        Object this$costType = this.getCostType();
        Object other$costType = other.getCostType();
        if (!((this$costType != null) ? this$costType.equals(other$costType) : (other$costType == null))) {
            return false;
        }
        Object this$canMerge = this.getCanMerge();
        Object other$canMerge = other.getCanMerge();
        if (!((this$canMerge != null) ? this$canMerge.equals(other$canMerge) : (other$canMerge == null))) {
            return false;
        }
        Object this$batches = this.getBatches();
        Object other$batches = other.getBatches();
        if (!((this$batches != null) ? this$batches.equals(other$batches) : (other$batches == null))) {
            return false;
        }
        Object this$tips = this.getTips();
        Object other$tips = other.getTips();
        if (!((this$tips != null) ? this$tips.equals(other$tips) : (other$tips == null))) {
            return false;
        }
        Object this$value = this.getValue();
        Object other$value = other.getValue();
        if (!((this$value != null) ? this$value.equals(other$value) : (other$value == null))) {
            return false;
        }
        Object this$itemPJ = this.getItemPJ();
        Object other$itemPJ = other.getItemPJ();
        if (!((this$itemPJ != null) ? this$itemPJ.equals(other$itemPJ) : (other$itemPJ == null))) {
            return false;
        }
        Object this$useCntLimit = this.getUseCntLimit();
        Object other$useCntLimit = other.getUseCntLimit();
        if (!((this$useCntLimit != null) ? this$useCntLimit.equals(other$useCntLimit) : (other$useCntLimit == null))) {
            return false;
        }
        Object this$needZs = this.getNeedZs();
        Object other$needZs = other.getNeedZs();
        if (!((this$needZs != null) ? this$needZs.equals(other$needZs) : (other$needZs == null))) {
            return false;
        }
        Object this$needLv = this.getNeedLv();
        Object other$needLv = other.getNeedLv();
        if (!((this$needLv != null) ? this$needLv.equals(other$needLv) : (other$needLv == null))) {
            return false;
        }
        Object this$isOpen = this.getIsOpen();
        Object other$isOpen = other.getIsOpen();
        if (!((this$isOpen != null) ? this$isOpen.equals(other$isOpen) : (other$isOpen == null))) {
            return false;
        }
        Object this$type = this.getType();
        Object other$type = other.getType();
        if (!((this$type != null) ? this$type.equals(other$type) : (other$type == null))) {
            return false;
        }
        Object this$dict = this.getDict();
        Object other$dict = other.getDict();
        if (!((this$dict != null) ? this$dict.equals(other$dict) : (other$dict == null))) {
            return false;
        }
        Object this$param = this.getParam();
        Object other$param = other.getParam();
        return (this$param != null) ? this$param.equals(other$param) : (other$param == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysOtherItem;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        Object $price = this.getPrice();
        result = result * 59 + (($price == null) ? 43 : $price.hashCode());
        Object $reprice = this.getReprice();
        result = result * 59 + (($reprice == null) ? 43 : $reprice.hashCode());
        Object $shape = this.getShape();
        result = result * 59 + (($shape == null) ? 43 : $shape.hashCode());
        Object $costType = this.getCostType();
        result = result * 59 + (($costType == null) ? 43 : $costType.hashCode());
        Object $canMerge = this.getCanMerge();
        result = result * 59 + (($canMerge == null) ? 43 : $canMerge.hashCode());
        Object $batches = this.getBatches();
        result = result * 59 + (($batches == null) ? 43 : $batches.hashCode());
        Object $tips = this.getTips();
        result = result * 59 + (($tips == null) ? 43 : $tips.hashCode());
        Object $value = this.getValue();
        result = result * 59 + (($value == null) ? 43 : $value.hashCode());
        Object $itemPJ = this.getItemPJ();
        result = result * 59 + (($itemPJ == null) ? 43 : $itemPJ.hashCode());
        Object $useCntLimit = this.getUseCntLimit();
        result = result * 59 + (($useCntLimit == null) ? 43 : $useCntLimit.hashCode());
        Object $needZs = this.getNeedZs();
        result = result * 59 + (($needZs == null) ? 43 : $needZs.hashCode());
        Object $needLv = this.getNeedLv();
        result = result * 59 + (($needLv == null) ? 43 : $needLv.hashCode());
        Object $isOpen = this.getIsOpen();
        result = result * 59 + (($isOpen == null) ? 43 : $isOpen.hashCode());
        Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        Object $dict = this.getDict();
        result = result * 59 + (($dict == null) ? 43 : $dict.hashCode());
        Object $param = this.getParam();
        result = result * 59 + (($param == null) ? 43 : $param.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysOtherItem(id=" + this.getId() + ", serial=" + this.getSerial() + ", name=" + this.getName() + ", price=" + this.getPrice() + ", reprice=" + this.getReprice() + ", shape=" + this.getShape() + ", costType=" + this.getCostType() + ", canMerge=" + this.getCanMerge() + ", batches=" + this.getBatches() + ", tips=" + this.getTips() + ", value=" + this.getValue() + ", itemPJ=" + this.getItemPJ() + ", useCntLimit=" + this.getUseCntLimit() + ", needZs=" + this.getNeedZs() + ", needLv=" + this.getNeedLv() + ", isOpen=" + this.getIsOpen() + ", type=" + this.getType() + ", dict=" + this.getDict() + ", param=" + this.getParam() + ")";
    }
}
