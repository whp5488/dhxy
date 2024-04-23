package com.kitty.system.enums.war.wardef;

import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum WARAIPROB {
  AI_Attack_VALUE("选择普通攻击的概率", "attackP", "A"),
  AI_Du_VALUE("选择使用毒法术的概率", "duP", "R"),
  AI_Sleep_VALUE("选择使用睡法术的概率", "sleepP", "R"),
  AI_Hun_VALUE("选择使用混法术的概率", "hunP", "R"),
  AI_Bing_VALUE("选择使用冰法术的概率", "bingP", "R"),
  AI_Huo_VALUE("选择使用火法术的概率", "huoP", "R"),
  AI_Feng_VALUE("选择使用风法术的概率", "fengP", "R"),
  AI_Lei_VALUE("选择使用雷法术的概率", "leiP", "R"),
  AI_Shui_VALUE("选择使用水法术的概率", "shuiP", "R"),
  AI_Pan_VALUE("选择使用盘法术的概率", "panP", "R"),
  AI_Gong_VALUE("选择使用攻法术的概率", "gongP", "R"),
  AI_Su_VALUE("选择使用速法术的概率", "suP", "R"),
  AI_Zhen_VALUE("选择使用虹吸法术的概率", "zhenP", "R"),
  AI_ShuaiRuo_VALUE("选择使用衰弱法术的概率", "shuairuoP", "R"),
  AI_XiXue_VALUE("选择使用吸血法术的概率", "xixueP", "R"),
  AI_AiHao_VALUE("选择使用哀嚎法术的概率", "aihaoP", "R"),
  AI_YiWang_VALUE("选择使用遗忘法术的概率", "yiwangP", "R"),
  AI_NeidanSkill_VALUE("选择使用魂石法术的概率", "neidanSkillP", "P"),
  AI_PetSkill_VALUE("选择使用宠物自带法术的概率", "skillP", "P");
  
  WARAIPROB(String note, String value, String type) {
    this.note = note;
    this.value = value;
    this.type = type;
  }
  
  private String note;
  
  private String value;
  
  private String type;
  
  public String getNote() {
    return this.note;
  }
  
  public String getValue() {
    return this.value;
  }
  
  public String getType() {
    return this.type;
  }
  
  public static List<Map<String, Object>> toList() {
    List<Map<String, Object>> list = Lists.newArrayList();
    for (WARAIPROB war : values()) {
      Map<String, Object> map = new HashMap<>();
      map.put("value", war.getValue());
      map.put("type", war.getType());
      list.add(map);
    } 
    return list;
  }
}
