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
        this.price = Integer.valueOf(0);
        this.reprice = Integer.valueOf(0);
        this.costType = Integer.valueOf(0);
        this.canMerge = Integer.valueOf(1);
        this.batches = Integer.valueOf(0);
        this.value = Integer.valueOf(0);
        this.itemPJ = Integer.valueOf(1);
        this.isOpen = Integer.valueOf(0);
        this.type = Integer.valueOf(0);
        this.cycle = Integer.valueOf(0);
        this.category = Integer.valueOf(0);
        this.useCntLimit = Integer.valueOf(-1);
        this.applyObjectSerial = Integer.valueOf(0);
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
}
