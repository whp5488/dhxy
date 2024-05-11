
package com.kitty.system.enums.war.wardef;

import com.kitty.system.enums.war.itempro.def.PRO_TYPE;

public enum OTHER_SKILL_DICT_TYPE
{
    D_SUPERPOSITION_COUNT("毒叠加次数", 1, PRO_TYPE.Pro_Value_NUM_TYPE.getValue()), 
    MH_SUPERPOSITION_COUNT("魅惑叠加次数", 2, PRO_TYPE.Pro_Value_NUM_TYPE.getValue()), 
    REDUCE_HURT_COUNT("减伤次数", 3, PRO_TYPE.Pro_Value_NUM_TYPE.getValue()), 
    DEFY_RESISTANCE_COUNT("无视抗性次数", 4, PRO_TYPE.Pro_Value_NUM_TYPE.getValue()), 
    DEFY_RESISTANCE_PERCENT("无视抗性百分比", 5, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    DRUG_SHARE_PERCENT("保护分担百分比", 6, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    DRUG_SHARE_CHANGE_PERCENT("保护分担伤害变化百分比", 7, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    THE_ROUND_DRUG("当前回合是否被保护", 8, PRO_TYPE.Pro_Value_NUM_TYPE.getValue()), 
    COMBO_NUM_MAGIC("法术连击次数", 9, PRO_TYPE.Pro_Value_NUM_TYPE.getValue()), 
    COMBO_NUM_MAGIC_HURT_PERCENT("法术连击伤害百分比", 12, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    PHYSICAL_ATTACK_FURY("物理攻击暴击数量", 10, PRO_TYPE.Pro_Value_NUM_TYPE.getValue()), 
    PHYSICAL_ATTACK_GSDN_HURT("物理攻击隔山打牛伤害", 11, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    PHYSICAL_ATTACK_FLGJ_HURT("物理攻击分裂攻击伤害", 12, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    PHYSICAL_ATTACK_FLGJ_HURT_NUM("物理攻击分裂攻击次数", 13, PRO_TYPE.Pro_Value_NUM_TYPE.getValue());
    
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
    
    private OTHER_SKILL_DICT_TYPE(final String name, final int dist, final int type) {
        this.name = name;
        this.dist = dist;
        this.type = type;
    }
}
