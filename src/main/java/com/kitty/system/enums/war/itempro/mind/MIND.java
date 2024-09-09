package com.kitty.system.enums.war.itempro.mind;

import com.kitty.system.enums.war.itempro.def.PRO_TYPE;

public enum MIND
{
    K_DU("心法抗毒", 140201, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    K_F("心法抗风", 140204, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    K_L("心法抗雷", 140205, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    K_S("心法抗水", 140206, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    K_H("心法抗火", 140207, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    K_SS("心法抗三尸", 140209, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    K_GH("心法抗鬼火", 140210, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    KB_WL("物理抗暴率", 148000, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    KB_F("风系抗暴率", 148050, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    KB_L("雷系抗暴率", 148060, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    KB_S("水系抗暴率", 148070, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    KB_H("火系抗暴率", 148080, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    KB_SS("三尸抗暴率", 148090, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    KB_GH("鬼火抗暴率", 148091, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    XS_ZS_X("震慑气血吸收", 149010, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    XS_ZS_F("震慑法力吸收", 149020, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    XS_WL("物理伤害吸收", 149030, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    XS_WL_XF("心法物理吸收", 149031, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    XS_DU("毒法伤害吸收", 149040, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    XS_F("风法伤害吸收", 149050, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    XS_L("雷法伤害吸收", 149060, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    XS_S("水法伤害吸收", 149070, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    XS_H("火法伤害吸收", 149080, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    XS_SS("三尸伤害吸收", 149090, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    XS_GH("鬼火伤害吸收", 149100, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    SS_HX_XG("三尸回血效果", 141000, PRO_TYPE.Pro_Value_NUM_TYPE.getValue());
    
    private String name;
    private int dist;
    private int type;
    
    public String getName() {
        return this.name;
    }
    
    public int getDist() {
        return this.dist;
    }
    
    public int getType() {
        return this.type;
    }
    
    private MIND(final String name, final int dist, final int type) {
        this.name = name;
        this.dist = dist;
        this.type = type;
    }
}
