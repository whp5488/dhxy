package com.kitty.system.enums.war.wardef;

public enum ATTRCAP
{
    MAX_KANG_PET_NOMANAGE_VALUE("无管制宠物所有抗性上限75", 0.75), 
    MAX_KANG_PET_MANAGE_NOREN_VALUE("管制宠物非人族抗性上限85", 0.85), 
    MAX_KANG_PET_MANAGE_YIWANG_VALUE("管制宠物遗忘上限都95", 0.95), 
    MAX_KANG_PET_MANAGE_REN_VALUE("有管制的宠物人族抗性上限都95", 0.95), 
    MAX_RES_HERO_REN_VALUE("人族抗性上限120", 1.2), 
    MAX_RES_HERO_LONG_VALUE("龙族抗性上限110", 1.1), 
    MAX_RES_HERO_ELSE_VALUE("其他族抗性上限100", 1.0), 
    MAX_KANG_HERO_ForZB_TT("装备与帮派图腾的仙法（风、火、雷、水）抗性上限为75%", 0.75), 
    MAX_RACE_LV_LIMIT("种族天生抗性162级以后就不会再增加", 162.0), 
    MIN_PACC_NUM("英雄属性的上下限", 0.5), 
    MAX_PACC_NUM("攻击命中率", 2.0), 
    MIN_PSBL_NUM("最小物理闪躲", 0.0), 
    MAX_PSBL_NUM("最大物理闪躲", 1.0), 
    MIN_PLJTIMES_NUM("最小连击次数", 0.0), 
    MAX_PLJTIMES_NUM("最大连击次数", 15.0), 
    MIN_PWLFJTIMES_NUM("最小反击次数", 0.0), 
    MAX_PWLFJTIMES_NUM("最大反击次数", 10.0), 
    MIN_FTLV_NUM("最小反震程度", 0.0), 
    MAX_FTLV_NUM("最大反震程度", 1.0), 
    MAX_PET_PLJTIMES_NUM("最大宠物反击次数", 15.0), 
    PetRandomKangValue("宠物随机抗性的值", 0.3);
    
    private String note;
    private double value;
    
    public String getNote() {
        return this.note;
    }
    
    public double getValue() {
        return this.value;
    }
    
    private ATTRCAP(final String note, final double value) {
        this.note = note;
        this.value = value;
    }
}
