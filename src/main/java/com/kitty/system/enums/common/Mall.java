package com.kitty.system.enums.common;

import com.google.common.collect.Lists;
import java.util.List;

public enum Mall
{
    RECOMMEND("多宝推荐", 1, (byte)1), 
    TREND_NEW("新鲜玩意", 2, (byte)1), 
    STRENGTHEN_MATERIAL("强化材料", 3, (byte)1), 
    WEEKLY_QUOTA("每周限购", 4, (byte)1), 
    NPC_SHOP_GROCERY("杂货店", 5, (byte)1), 
    NPC_SHOP_GUO("郭氏", 6, (byte)0), 
    NPC_SHOP_BRAC("降魔", 7, (byte)0), 
    NPC_SHOP_NAO("大闹天宫", 8, (byte)1), 
    NPC_SHOP_DI("地宫", 9, (byte)1), 
    NPC_SHOP_ARENA("竞技场", 10, (byte)1), 
    NPC_SHOP_LONG("龙战", 11, (byte)1), 
    NPC_SHOP_NICE("好心值", 12, (byte)1), 
    NPC_SHOP_FEATS("功绩", 13, (byte)1), 
    BOUND_SHOP("绑定商店", 14, (byte)1), 
    CHARGE_SCORE_SHOP("积分商店", 15, (byte)1);
    
    private String note;
    private int value;
    private byte state;
    
    public static List<Integer> toList() {
        List<Integer> list = Lists.newArrayList();
        for (Mall n : values()) {
            list.add(Integer.valueOf(n.getValue()));
        }
        return list;
    }
    
    public String getNote() {
        return this.note;
    }
    
    public int getValue() {
        return this.value;
    }
    
    public byte getState() {
        return this.state;
    }
    
    private Mall(final String note, final int value, final byte state) {
        this.note = note;
        this.value = value;
        this.state = state;
    }
}
