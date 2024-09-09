package com.kitty.system.enums.war.itempro.def;

public enum PRO_DICT_SERIAL
{
    BASICS_HERO("人物基础属性", 10000), 
    HERO_FACTION("人物帮派修炼属性", 20000), 
    HERO_RELIVE("转生修正属性", 30000), 
    BASICS_PET("宠物基础属性", 40000), 
    PET_RELIVE("宠物修炼属性", 50000), 
    ZQGZ("坐骑管制属性", 60000), 
    EQUIP_PRO("装备基础属性", 70000), 
    EQUIP_PRO_LH("装备炼化属性", 80000), 
    PS("配饰属性", 90000), 
    PSTZ("配饰套装属性", 100000), 
    TYC("天演策属性", 110000), 
    BS("变身/属性卡属性", 120000), 
    PET_PS("宠物配饰属性", 130000), 
    MIND("心法属性", 140000);
    
    private String name;
    private int serial;
    
    public String getName() {
        return this.name;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    private PRO_DICT_SERIAL(final String name, final int serial) {
        this.name = name;
        this.serial = serial;
    }
}
