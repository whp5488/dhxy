
package com.kitty.system.game.activity.model.product;

public class ActivityLivenessSet
{
    private String name;
    private ActivityType activityType;
    private float perLiveness;
    private int maxLiveness;
    
    public ActivityLivenessSet(final String name, final ActivityType activityType, final float perLiveness, final int maxLiveness) {
        this.name = name;
        this.activityType = activityType;
        this.perLiveness = perLiveness;
        this.maxLiveness = maxLiveness;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setActivityType(final ActivityType activityType) {
        this.activityType = activityType;
    }
    
    public void setPerLiveness(final float perLiveness) {
        this.perLiveness = perLiveness;
    }
    
    public void setMaxLiveness(final int maxLiveness) {
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
}
