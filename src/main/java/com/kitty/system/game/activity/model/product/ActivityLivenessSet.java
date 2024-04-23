package com.kitty.system.game.activity.model.product;

public class ActivityLivenessSet {
  private String name;
  
  private ActivityType activityType;
  
  private float perLiveness;
  
  private int maxLiveness;
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setActivityType(ActivityType activityType) {
    this.activityType = activityType;
  }
  
  public void setPerLiveness(float perLiveness) {
    this.perLiveness = perLiveness;
  }
  
  public void setMaxLiveness(int maxLiveness) {
    this.maxLiveness = maxLiveness;
  }
  
  public String getName() {
    return this.name;
  }
  
  public ActivityType getActivityType() {
    return this.activityType;
  }
  
  public float getPerLiveness() {
    return this.perLiveness;
  }
  
  public int getMaxLiveness() {
    return this.maxLiveness;
  }
  
  public ActivityLivenessSet(String name, ActivityType activityType, float perLiveness, int maxLiveness) {
    this.name = name;
    this.activityType = activityType;
    this.perLiveness = perLiveness;
    this.maxLiveness = maxLiveness;
  }
}
