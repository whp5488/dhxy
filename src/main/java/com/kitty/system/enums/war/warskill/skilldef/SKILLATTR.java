package com.kitty.system.enums.war.warskill.skilldef;

public enum SKILLATTR
{
    SKILLATTR_WULI("物理攻击", 0), 
    SKILLATTR_POISON("毒系【女 人族】", 1), 
    SKILLATTR_SLEEP("睡系【男女 人族】", 2), 
    SKILLATTR_CONFUSE("混系【男 人族】", 3), 
    SKILLATTR_ICE("冰系【男女 人族】", 4), 
    SKILLATTR_FIRE("火系【女 仙族】", 5), 
    SKILLATTR_WIND("风系【男 仙族】", 6), 
    SKILLATTR_THUNDER("雷系【男女 仙族】", 7), 
    SKILLATTR_WATER("水系【男女 仙族】", 8), 
    SKILLATTR_PAN("盘-加防系【女 魔族】", 9), 
    SKILLATTR_ATTACK("攻系【男女 魔族】", 10), 
    SKILLATTR_SPEED("速系【男 魔族】", 11), 
    SKILLATTR_ZHEN("震慑系【男女 魔族】", 12), 
    SKILLATTR_SHUAIRUO("魅惑系【女 鬼族】", 13), 
    SKILLATTR_XIXUE("吸血-三尸系【男 鬼族】", 14), 
    SKILLATTR_AIHAO("哀嚎-鬼火系【男女 鬼族】", 15), 
    SKILLATTR_YIWANG("遗忘系【男女 鬼族】", 16), 
    SKILLATTR_HENGSAO("横扫系【男女 龙族】", 17), 
    SKILLATTR_ZHIYU("治愈系【男女 龙族】", 18), 
    SKILLATTR_POJIA("破甲系【男女 龙族】", 19), 
    SKILLATTR_ZHENJI("震击系【女 龙族】", 20), 
    SKILLATTR_NILIN("变身【男女 龙族】", 21), 
    SKILLATTR_UNKNOW("未知", -1);
    
    private String note;
    private int value;
    
    public static SKILLATTR getByValue(int value) {
        SKILLATTR[] values = values();
        int length = values.length;
        int i = 0;
        while (i < length) {
            SKILLATTR x = values[i];
            if (x.getValue() == value) {
                return x;
            }
            ++i;
        }
        return null;
    }
    
    public String getNote() {
        return this.note;
    }
    
    public int getValue() {
        return this.value;
    }
    
    private SKILLATTR(final String note, final int value) {
        this.note = note;
        this.value = value;
    }
}
