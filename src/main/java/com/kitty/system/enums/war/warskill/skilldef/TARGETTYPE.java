
package com.kitty.system.enums.war.warskill.skilldef;

public enum TARGETTYPE
{
    ALL_ENEMY("敌方所有角色", 1), 
    ALL_OURS("我方所有角色", 2), 
    MYSELF("自己", 3), 
    ALL_OURS_EXPME("我方除了自己", 4), 
    OWNER("宠物主人", 5), 
    ALL_OUR_SUMMON("己方召唤兽", 6), 
    ALL_ENEMY_SUMMON("敌方召唤兽", 7), 
    ALL_OUR_PARTNER("我方伙伴", 8), 
    ALL_ENEMY_PARTNER("敌方伙伴", 9), 
    ALL_ENEMY_USER("敌方英雄", 10), 
    ALL_OUR_USER("我方英雄", 11), 
    ALL_OUR_EXPMEANDPARTNER("我方除了自己和伙伴", 12), 
    ALL_OUR_SUMMON_EXPME("我方召唤兽除了自己的召唤兽", 13);
    
    private String note;
    private int value;
    
    public String getNote() {
        return this.note;
    }
    
    public int getValue() {
        return this.value;
    }
    
    private TARGETTYPE(final String note, final int value) {
        this.note = note;
        this.value = value;
    }
}
