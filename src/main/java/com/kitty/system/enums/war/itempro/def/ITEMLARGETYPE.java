package com.kitty.system.enums.war.itempro.def;

public enum ITEMLARGETYPE
{
    ITEM_LARGE_TYPE_DRUG("药品", 1), 
    ITEM_LARGE_TYPE_PET("宠物", 2), 
    ITEM_LARGE_TYPE_GIFT("礼包", 3), 
    ITEM_LARGE_TYPE_STUFF("材料", 7), 
    ITEM_LARGE_TYPE_OTHERITEM("其他", 9), 
    ITEM_LARGE_TYPE_CARD("变身卡", 10), 
    ITEM_LARGE_TYPE_ACSRY_EGG("未鉴定配饰", 30), 
    ITEM_LARGE_TYPE_TASK("任务物品", 70), 
    ITEM_LARGE_TYPE_EQPT("初级装备", 200), 
    ITEM_LARGE_TYPE_SENIOREQPT("高级装备", 300), 
    ITEM_LARGE_TYPE_SHENBING("神兵", 400), 
    ITEM_LARGE_TYPE_XIANQI("仙器", 500), 
    ITEM_LARGE_TYPE_HUOBANEQPT("伙伴装备", 600), 
    ITEM_LARGE_TYPE_ACSRY("配饰", 1700);
    
    private String note;
    private int value;
    
    public String getNote() {
        return this.note;
    }
    
    public int getValue() {
        return this.value;
    }
    
    private ITEMLARGETYPE(final String note, final int value) {
        this.note = note;
        this.value = value;
    }
}
