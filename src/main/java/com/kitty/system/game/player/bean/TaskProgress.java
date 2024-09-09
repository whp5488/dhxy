package com.kitty.system.game.player.bean;

import com.kitty.common.utils.TimeUtil;
import com.kitty.system.game.activity.model.product.ActivityType;
import java.util.concurrent.ConcurrentHashMap;
import com.kitty.system.common.utils.NutConcurrentMap;
import java.io.Serializable;

public class TaskProgress implements Serializable
{
    private int loop;
    private NutConcurrentMap finishCounts;
    private NutConcurrentMap finishTallyCounts;
    private NutConcurrentMap finishRecord;
    private NutConcurrentMap everyDayUpperLimit;
    private NutConcurrentMap everyDayRecharge;
    private float liveness;
    private ConcurrentHashMap<Integer, Boolean> livenessRewardFetchStatus;
    
    public void dailyReset(boolean flag) {
        int loop = this.finishCounts.getInt(ActivityType.LOOP.getActivityId(), 0);
        this.finishCounts = NutConcurrentMap.NEW();
        if (loop > 0 && !flag) {
            this.finishCounts.setv(ActivityType.LOOP.getActivityId(), Integer.valueOf(loop));
        }
        this.liveness = 0.0f;
        this.everyDayUpperLimit = NutConcurrentMap.NEW();
        this.livenessRewardFetchStatus = new ConcurrentHashMap();
        this.finish();
        this.recharge();
    }
    
    public void recharge() {
        this.everyDayRecharge = NutConcurrentMap.NEW();
    }
    
    public void finish() {
        String when = TimeUtil.when();
        int moFrontCount = this.finishRecord.getInt(ActivityType.MWK_FRONT.getActivityId(), 0);
        this.finishRecord = NutConcurrentMap.NEW();
        if (!"星期六".equals(when)) {
            this.finishRecord.setv(ActivityType.MWK_FRONT.getActivityId(), Integer.valueOf(moFrontCount));
        }
    }
    
    public TaskProgress() {
        this.loop = 1;
        this.finishCounts = new NutConcurrentMap();
        this.finishTallyCounts = new NutConcurrentMap();
        this.finishRecord = new NutConcurrentMap();
        this.everyDayUpperLimit = new NutConcurrentMap();
        this.everyDayRecharge = new NutConcurrentMap();
        this.liveness = 0.0f;
        this.livenessRewardFetchStatus = new ConcurrentHashMap();
    }
    
    public int getLoop() {
        return this.loop;
    }
    
    public NutConcurrentMap getFinishCounts() {
        return this.finishCounts;
    }
    
    public NutConcurrentMap getFinishTallyCounts() {
        return this.finishTallyCounts;
    }
    
    public NutConcurrentMap getFinishRecord() {
        return this.finishRecord;
    }
    
    public NutConcurrentMap getEveryDayUpperLimit() {
        return this.everyDayUpperLimit;
    }
    
    public NutConcurrentMap getEveryDayRecharge() {
        return this.everyDayRecharge;
    }
    
    public float getLiveness() {
        return this.liveness;
    }
    
    public ConcurrentHashMap<Integer, Boolean> getLivenessRewardFetchStatus() {
        return this.livenessRewardFetchStatus;
    }
    
    public void setLoop(final int loop) {
        this.loop = loop;
    }
    
    public void setFinishCounts(final NutConcurrentMap finishCounts) {
        this.finishCounts = finishCounts;
    }
    
    public void setFinishTallyCounts(final NutConcurrentMap finishTallyCounts) {
        this.finishTallyCounts = finishTallyCounts;
    }
    
    public void setFinishRecord(final NutConcurrentMap finishRecord) {
        this.finishRecord = finishRecord;
    }
    
    public void setEveryDayUpperLimit(final NutConcurrentMap everyDayUpperLimit) {
        this.everyDayUpperLimit = everyDayUpperLimit;
    }
    
    public void setEveryDayRecharge(final NutConcurrentMap everyDayRecharge) {
        this.everyDayRecharge = everyDayRecharge;
    }
    
    public void setLiveness(final float liveness) {
        this.liveness = liveness;
    }
    
    public void setLivenessRewardFetchStatus(final ConcurrentHashMap<Integer, Boolean> livenessRewardFetchStatus) {
        this.livenessRewardFetchStatus = livenessRewardFetchStatus;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TaskProgress)) {
            return false;
        }
        TaskProgress other = (TaskProgress)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getLoop() != other.getLoop()) {
            return false;
        }
        Object this$finishCounts = this.getFinishCounts();
        Object other$finishCounts = other.getFinishCounts();
        if (!((this$finishCounts != null) ? this$finishCounts.equals(other$finishCounts) : (other$finishCounts == null))) {
            return false;
        }
        Object this$finishTallyCounts = this.getFinishTallyCounts();
        Object other$finishTallyCounts = other.getFinishTallyCounts();
        if (!((this$finishTallyCounts != null) ? this$finishTallyCounts.equals(other$finishTallyCounts) : (other$finishTallyCounts == null))) {
            return false;
        }
        Object this$finishRecord = this.getFinishRecord();
        Object other$finishRecord = other.getFinishRecord();
        if (!((this$finishRecord != null) ? this$finishRecord.equals(other$finishRecord) : (other$finishRecord == null))) {
            return false;
        }
        Object this$everyDayUpperLimit = this.getEveryDayUpperLimit();
        Object other$everyDayUpperLimit = other.getEveryDayUpperLimit();
        if (!((this$everyDayUpperLimit != null) ? this$everyDayUpperLimit.equals(other$everyDayUpperLimit) : (other$everyDayUpperLimit == null))) {
            return false;
        }
        Object this$everyDayRecharge = this.getEveryDayRecharge();
        Object other$everyDayRecharge = other.getEveryDayRecharge();
        if (!((this$everyDayRecharge != null) ? this$everyDayRecharge.equals(other$everyDayRecharge) : (other$everyDayRecharge == null))) {
            return false;
        }
        if (Float.compare(this.getLiveness(), other.getLiveness()) != 0) {
            return false;
        }
        Object this$livenessRewardFetchStatus = this.getLivenessRewardFetchStatus();
        Object other$livenessRewardFetchStatus = other.getLivenessRewardFetchStatus();
        return (this$livenessRewardFetchStatus != null) ? this$livenessRewardFetchStatus.equals(other$livenessRewardFetchStatus) : (other$livenessRewardFetchStatus == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof TaskProgress;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getLoop();
        Object $finishCounts = this.getFinishCounts();
        result = result * 59 + (($finishCounts == null) ? 43 : $finishCounts.hashCode());
        Object $finishTallyCounts = this.getFinishTallyCounts();
        result = result * 59 + (($finishTallyCounts == null) ? 43 : $finishTallyCounts.hashCode());
        Object $finishRecord = this.getFinishRecord();
        result = result * 59 + (($finishRecord == null) ? 43 : $finishRecord.hashCode());
        Object $everyDayUpperLimit = this.getEveryDayUpperLimit();
        result = result * 59 + (($everyDayUpperLimit == null) ? 43 : $everyDayUpperLimit.hashCode());
        Object $everyDayRecharge = this.getEveryDayRecharge();
        result = result * 59 + (($everyDayRecharge == null) ? 43 : $everyDayRecharge.hashCode());
        result = result * 59 + Float.floatToIntBits(this.getLiveness());
        Object $livenessRewardFetchStatus = this.getLivenessRewardFetchStatus();
        result = result * 59 + (($livenessRewardFetchStatus == null) ? 43 : $livenessRewardFetchStatus.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "TaskProgress(loop=" + this.getLoop() + ", finishCounts=" + this.getFinishCounts() + ", finishTallyCounts=" + this.getFinishTallyCounts() + ", finishRecord=" + this.getFinishRecord() + ", everyDayUpperLimit=" + this.getEveryDayUpperLimit() + ", everyDayRecharge=" + this.getEveryDayRecharge() + ", liveness=" + this.getLiveness() + ", livenessRewardFetchStatus=" + this.getLivenessRewardFetchStatus() + ")";
    }
}
