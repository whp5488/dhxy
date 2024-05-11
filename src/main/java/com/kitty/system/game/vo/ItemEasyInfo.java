
package com.kitty.system.game.vo;

import com.kitty.system.game.vo.init.Item;
import java.util.List;

public class ItemEasyInfo
{
    private Integer serial;
    private String name;
    private Integer price;
    private Integer reprice;
    private Integer shape;
    private Integer costType;
    private Integer canMerge;
    private Integer batches;
    private Integer value;
    private Integer itemPJ;
    private Integer isOpen;
    private Integer type;
    private List<Item> extra;
    private Integer cycle;
    private Integer category;
    private Integer useCntLimit;
    private Integer applyObjectSerial;
    private String dict;
    
    public ItemEasyInfo() {
        this.price = 0;
        this.reprice = 0;
        this.costType = 0;
        this.canMerge = 1;
        this.batches = 0;
        this.value = 0;
        this.itemPJ = 1;
        this.isOpen = 0;
        this.type = 0;
        this.cycle = 0;
        this.category = 0;
        this.useCntLimit = -1;
        this.applyObjectSerial = 0;
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
    
    public Integer getValue() {
        return this.value;
    }
    
    public Integer getItemPJ() {
        return this.itemPJ;
    }
    
    public Integer getIsOpen() {
        return this.isOpen;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public List<Item> getExtra() {
        return this.extra;
    }
    
    public Integer getCycle() {
        return this.cycle;
    }
    
    public Integer getCategory() {
        return this.category;
    }
    
    public Integer getUseCntLimit() {
        return this.useCntLimit;
    }
    
    public Integer getApplyObjectSerial() {
        return this.applyObjectSerial;
    }
    
    public String getDict() {
        return this.dict;
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
    
    public void setValue(final Integer value) {
        this.value = value;
    }
    
    public void setItemPJ(final Integer itemPJ) {
        this.itemPJ = itemPJ;
    }
    
    public void setIsOpen(final Integer isOpen) {
        this.isOpen = isOpen;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    public void setExtra(final List<Item> extra) {
        this.extra = extra;
    }
    
    public void setCycle(final Integer cycle) {
        this.cycle = cycle;
    }
    
    public void setCategory(final Integer category) {
        this.category = category;
    }
    
    public void setUseCntLimit(final Integer useCntLimit) {
        this.useCntLimit = useCntLimit;
    }
    
    public void setApplyObjectSerial(final Integer applyObjectSerial) {
        this.applyObjectSerial = applyObjectSerial;
    }
    
    public void setDict(final String dict) {
        this.dict = dict;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemEasyInfo)) {
            return false;
        }
        final ItemEasyInfo other = (ItemEasyInfo)o;
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
        final Object this$reprice = this.getReprice();
        final Object other$reprice = other.getReprice();
        Label_0176: {
            if (this$reprice == null) {
                if (other$reprice == null) {
                    break Label_0176;
                }
            }
            else if (this$reprice.equals(other$reprice)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$shape = this.getShape();
        final Object other$shape = other.getShape();
        Label_0213: {
            if (this$shape == null) {
                if (other$shape == null) {
                    break Label_0213;
                }
            }
            else if (this$shape.equals(other$shape)) {
                break Label_0213;
            }
            return false;
        }
        final Object this$costType = this.getCostType();
        final Object other$costType = other.getCostType();
        Label_0250: {
            if (this$costType == null) {
                if (other$costType == null) {
                    break Label_0250;
                }
            }
            else if (this$costType.equals(other$costType)) {
                break Label_0250;
            }
            return false;
        }
        final Object this$canMerge = this.getCanMerge();
        final Object other$canMerge = other.getCanMerge();
        Label_0287: {
            if (this$canMerge == null) {
                if (other$canMerge == null) {
                    break Label_0287;
                }
            }
            else if (this$canMerge.equals(other$canMerge)) {
                break Label_0287;
            }
            return false;
        }
        final Object this$batches = this.getBatches();
        final Object other$batches = other.getBatches();
        Label_0324: {
            if (this$batches == null) {
                if (other$batches == null) {
                    break Label_0324;
                }
            }
            else if (this$batches.equals(other$batches)) {
                break Label_0324;
            }
            return false;
        }
        final Object this$value = this.getValue();
        final Object other$value = other.getValue();
        Label_0361: {
            if (this$value == null) {
                if (other$value == null) {
                    break Label_0361;
                }
            }
            else if (this$value.equals(other$value)) {
                break Label_0361;
            }
            return false;
        }
        final Object this$itemPJ = this.getItemPJ();
        final Object other$itemPJ = other.getItemPJ();
        Label_0398: {
            if (this$itemPJ == null) {
                if (other$itemPJ == null) {
                    break Label_0398;
                }
            }
            else if (this$itemPJ.equals(other$itemPJ)) {
                break Label_0398;
            }
            return false;
        }
        final Object this$isOpen = this.getIsOpen();
        final Object other$isOpen = other.getIsOpen();
        Label_0435: {
            if (this$isOpen == null) {
                if (other$isOpen == null) {
                    break Label_0435;
                }
            }
            else if (this$isOpen.equals(other$isOpen)) {
                break Label_0435;
            }
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0472: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0472;
                }
            }
            else if (this$type.equals(other$type)) {
                break Label_0472;
            }
            return false;
        }
        final Object this$extra = this.getExtra();
        final Object other$extra = other.getExtra();
        Label_0509: {
            if (this$extra == null) {
                if (other$extra == null) {
                    break Label_0509;
                }
            }
            else if (this$extra.equals(other$extra)) {
                break Label_0509;
            }
            return false;
        }
        final Object this$cycle = this.getCycle();
        final Object other$cycle = other.getCycle();
        Label_0546: {
            if (this$cycle == null) {
                if (other$cycle == null) {
                    break Label_0546;
                }
            }
            else if (this$cycle.equals(other$cycle)) {
                break Label_0546;
            }
            return false;
        }
        final Object this$category = this.getCategory();
        final Object other$category = other.getCategory();
        Label_0583: {
            if (this$category == null) {
                if (other$category == null) {
                    break Label_0583;
                }
            }
            else if (this$category.equals(other$category)) {
                break Label_0583;
            }
            return false;
        }
        final Object this$useCntLimit = this.getUseCntLimit();
        final Object other$useCntLimit = other.getUseCntLimit();
        Label_0620: {
            if (this$useCntLimit == null) {
                if (other$useCntLimit == null) {
                    break Label_0620;
                }
            }
            else if (this$useCntLimit.equals(other$useCntLimit)) {
                break Label_0620;
            }
            return false;
        }
        final Object this$applyObjectSerial = this.getApplyObjectSerial();
        final Object other$applyObjectSerial = other.getApplyObjectSerial();
        Label_0657: {
            if (this$applyObjectSerial == null) {
                if (other$applyObjectSerial == null) {
                    break Label_0657;
                }
            }
            else if (this$applyObjectSerial.equals(other$applyObjectSerial)) {
                break Label_0657;
            }
            return false;
        }
        final Object this$dict = this.getDict();
        final Object other$dict = other.getDict();
        if (this$dict == null) {
            if (other$dict == null) {
                return true;
            }
        }
        else if (this$dict.equals(other$dict)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ItemEasyInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
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
        final Object $value = this.getValue();
        result = result * 59 + (($value == null) ? 43 : $value.hashCode());
        final Object $itemPJ = this.getItemPJ();
        result = result * 59 + (($itemPJ == null) ? 43 : $itemPJ.hashCode());
        final Object $isOpen = this.getIsOpen();
        result = result * 59 + (($isOpen == null) ? 43 : $isOpen.hashCode());
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final Object $extra = this.getExtra();
        result = result * 59 + (($extra == null) ? 43 : $extra.hashCode());
        final Object $cycle = this.getCycle();
        result = result * 59 + (($cycle == null) ? 43 : $cycle.hashCode());
        final Object $category = this.getCategory();
        result = result * 59 + (($category == null) ? 43 : $category.hashCode());
        final Object $useCntLimit = this.getUseCntLimit();
        result = result * 59 + (($useCntLimit == null) ? 43 : $useCntLimit.hashCode());
        final Object $applyObjectSerial = this.getApplyObjectSerial();
        result = result * 59 + (($applyObjectSerial == null) ? 43 : $applyObjectSerial.hashCode());
        final Object $dict = this.getDict();
        result = result * 59 + (($dict == null) ? 43 : $dict.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ItemEasyInfo(serial=" + this.getSerial() + ", name=" + this.getName() + ", price=" + this.getPrice() + ", reprice=" + this.getReprice() + ", shape=" + this.getShape() + ", costType=" + this.getCostType() + ", canMerge=" + this.getCanMerge() + ", batches=" + this.getBatches() + ", value=" + this.getValue() + ", itemPJ=" + this.getItemPJ() + ", isOpen=" + this.getIsOpen() + ", type=" + this.getType() + ", extra=" + this.getExtra() + ", cycle=" + this.getCycle() + ", category=" + this.getCategory() + ", useCntLimit=" + this.getUseCntLimit() + ", applyObjectSerial=" + this.getApplyObjectSerial() + ", dict=" + this.getDict() + ")";
    }
}
