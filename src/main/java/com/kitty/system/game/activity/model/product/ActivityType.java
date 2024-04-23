package com.kitty.system.game.activity.model.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ActivityType {
  NO_TARGET("0", "无目标", 0, (byte)1, Arrays.asList(new String[0]), 0.0D),
  CATCHES_GHOSTS_TASK("10002", "钟馗抓鬼", 20, (byte)0, Arrays.asList(new String[0]), 4.0D),
  QUESTION_DLS("10003", "大理寺答题", 10, (byte)0, Arrays.asList(new String[0]), 2.0D),
  PARTY_TASK("10004", "帮派任务", 20, (byte)0, Arrays.asList(new String[0]), 3.0D),
  SCHOOL_TASK("10005", "师门任务", 20, (byte)0, Arrays.asList(new String[0]), 3.0D),
  TOWER_DUGEON("10006", "大雁塔", 8, (byte)0, Arrays.asList(new String[0]), 4.0D),
  LOOK_FOR_DUGEON("10007", "寻访副本", 16, (byte)0, Arrays.asList(new String[0]), 4.0D),
  TREASURE_MAP_TASK("10008", "宝图任务", 15, (byte)0, Arrays.asList(new String[0]), 0.0D),
  FIVE_TASK("10009", "五环任务", 1, (byte)0, Arrays.asList(new String[0]), 0.0D),
  SEAL_OFFICER("10010", "封妖", 5, (byte)0, Arrays.asList(new String[0]), 5.0D),
  SCOPE_OFFICER("10011", "三界妖王", 3, (byte)0, Arrays.asList(new String[0]), 5.0D),
  STAR_OFFICER("10012", "星官", 20, (byte)0, Arrays.asList(new String[0]), 5.0D),
  WORLD_BOOS_OFFICER("10013", "世界BOOS", 99999, (byte)0, Arrays.asList(new String[0]), 5.0D),
  GODS_COURT_TASK("10014", "天庭降妖", 20, (byte)0, Arrays.asList(new String[0]), 5.0D),
  NIAN_OFFICER("10015", "年兽", 50, (byte)0, Arrays.asList(new String[0]), 5.0D),
  BURNING_HOT("10016", "红孩儿", 50, (byte)0, Arrays.asList(new String[0]), 5.0D),
  LOOP("10019", "200环任务", 200, (byte)0, Arrays.asList(new String[0]), 0.0D),
  PK_CONTEST("10020", "pk大赛", 0, (byte)1, Arrays.asList(new String[0]), 0.0D),
  CARNIVAL("10021", "嘉年华", 60, (byte)0, Arrays.asList(new String[0]), 2.0D),
  MWK_FRONT("10022", "魔王窟前殿", 2, (byte)0, Arrays.asList(new String[0]), 2.0D),
  DRAGON("10053", "端午", 80, (byte)0, Arrays.asList(new String[0]), 2.0D),
  CRUSADE_RENEGADE("10054", "讨伐叛贼", 60, (byte)0, Arrays.asList(new String[0]), 0.0D),
  XM_DUGEON("10055", "心魔", 17, (byte)0, Arrays.asList(new String[] { "2", "4", "6" }), 4.0D),
  FACTION_BANDIT("9998", "帮派强盗", 2, (byte)1, Arrays.asList(new String[0]), 0.0D),
  STORY_LINE_TASK("9999", "剧情任务", 0, (byte)1, Arrays.asList(new String[0]), 0.0D);
  
  private String activityId;
  
  private String name;
  
  private int totalCount;
  
  private byte is;
  
  private double fold;
  
  private List<String> period;
  
  public String getActivityId() {
    return this.activityId;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getTotalCount() {
    return this.totalCount;
  }
  
  public byte getIs() {
    return this.is;
  }
  
  public double getFold() {
    return this.fold;
  }
  
  public List<String> getPeriod() {
    return this.period;
  }
  
  ActivityType(String activityId, String name, int totalCount, byte is, List<String> period, double fold) {
    this.activityId = activityId;
    this.name = name;
    this.totalCount = totalCount;
    this.is = is;
    this.period = period;
    this.fold = fold;
  }
  
  public static List<ActivityType> getActivityLiveness() {
    List<ActivityType> list = new ArrayList<>();
    for (ActivityType value : values()) {
      if (value.getIs() == 0)
        list.add(value); 
    } 
    return list;
  }
  
  public static List<ActivityType> getActivity() {
    List<ActivityType> list = new ArrayList<>();
    for (ActivityType value : values())
      list.add(value); 
    return list;
  }
}
