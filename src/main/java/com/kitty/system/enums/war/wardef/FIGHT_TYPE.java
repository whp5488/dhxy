package com.kitty.system.enums.war.wardef;

public enum FIGHT_TYPE
{
    MONSTER("野怪", 1, 0), 
    PLAY("切磋", 2, 1), 
    HG_FIGHT("皇宫约战", 3, 1), 
    SUPER_BOSS("超级BOOS", 6, 0), 
    TASK("任务", 8, 0), 
    NIAN("年兽", 12, 0), 
    BURNING_HOT("红孩儿", 13, 0), 
    PARTY_BANDIT("帮派强盗", 19, 0), 
    STAR("星君", 20, 0), 
    SEAL_DEMON("野外封妖", 21, 0), 
    THREE_SCOPE("三界妖王", 22, 0), 
    FORCE_PK("强制PK", 30, 1), 
    AT_WILL_PK("英雄争霸赛", 100, 1), 
    GANG_WARS("帮派大战", 3001, 1), 
    CA_PK("决战长安", 3002, 1), 
    CS_SUB("测试战斗替补", 9999, 1), 
    WATER_PK("水陆大会", 3003, 1);
    
    private String note;
    private int value;
    private int type;
    
    public static Integer getType(Integer value) {
        FIGHT_TYPE[] values = values();
        int length = values.length;
        int i = 0;
        while (i < length) {
            FIGHT_TYPE fight = values[i];
            if (fight.getValue() == (int)value) {
                return Integer.valueOf(fight.type);
            }
            ++i;
        }
        return Integer.valueOf(FIGHT_TYPE.MONSTER.type);
    }
    
    public String getNote() {
        return this.note;
    }
    
    public int getValue() {
        return this.value;
    }
    
    public int getType() {
        return this.type;
    }
    
    private FIGHT_TYPE(final String note, final int value, final int type) {
        this.note = note;
        this.value = value;
        this.type = type;
    }
}
