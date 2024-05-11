
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
    
    public void dailyReset(final boolean flag) {
        final int loop = this.finishCounts.getInt(ActivityType.LOOP.getActivityId(), 0);
        this.finishCounts = NutConcurrentMap.NEW();
        if (loop > 0 && !flag) {
            this.finishCounts.setv(ActivityType.LOOP.getActivityId(), loop);
        }
        this.liveness = 0.0f;
        this.everyDayUpperLimit = NutConcurrentMap.NEW();
        this.livenessRewardFetchStatus = new ConcurrentHashMap<Integer, Boolean>();
        this.finish();
        this.recharge();
    }
    
    public void recharge() {
        this.everyDayRecharge = NutConcurrentMap.NEW();
    }
    
    public void finish() {
        final String when = TimeUtil.when();
        final int moFrontCount = this.finishRecord.getInt(ActivityType.MWK_FRONT.getActivityId(), 0);
        this.finishRecord = NutConcurrentMap.NEW();
        if (!"星期六".equals(when)) {
            this.finishRecord.setv(ActivityType.MWK_FRONT.getActivityId(), moFrontCount);
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
        this.livenessRewardFetchStatus = new ConcurrentHashMap<Integer, Boolean>();
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
        final TaskProgress other = (TaskProgress)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getLoop() != other.getLoop()) {
            return false;
        }
        final Object this$finishCounts = this.getFinishCounts();
        final Object other$finishCounts = other.getFinishCounts();
        Label_0078: {
            if (this$finishCounts == null) {
                if (other$finishCounts == null) {
                    break Label_0078;
                }
            }
            else if (this$finishCounts.equals(other$finishCounts)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$finishTallyCounts = this.getFinishTallyCounts();
        final Object other$finishTallyCounts = other.getFinishTallyCounts();
        Label_0115: {
            if (this$finishTallyCounts == null) {
                if (other$finishTallyCounts == null) {
                    break Label_0115;
                }
            }
            else if (this$finishTallyCounts.equals(other$finishTallyCounts)) {
                break Label_0115;
            }
            return false;
        }
        final Object this$finishRecord = this.getFinishRecord();
        final Object other$finishRecord = other.getFinishRecord();
        Label_0152: {
            if (this$finishRecord == null) {
                if (other$finishRecord == null) {
                    break Label_0152;
                }
            }
            else if (this$finishRecord.equals(other$finishRecord)) {
                break Label_0152;
            }
            return false;
        }
        final Object this$everyDayUpperLimit = this.getEveryDayUpperLimit();
        final Object other$everyDayUpperLimit = other.getEveryDayUpperLimit();
        Label_0189: {
            if (this$everyDayUpperLimit == null) {
                if (other$everyDayUpperLimit == null) {
                    break Label_0189;
                }
            }
            else if (this$everyDayUpperLimit.equals(other$everyDayUpperLimit)) {
                break Label_0189;
            }
            return false;
        }
        final Object this$everyDayRecharge = this.getEveryDayRecharge();
        final Object other$everyDayRecharge = other.getEveryDayRecharge();
        Label_0226: {
            if (this$everyDayRecharge == null) {
                if (other$everyDayRecharge == null) {
                    break Label_0226;
                }
            }
            else if (this$everyDayRecharge.equals(other$everyDayRecharge)) {
                break Label_0226;
            }
            return false;
        }
        if (Float.compare(this.getLiveness(), other.getLiveness()) != 0) {
            return false;
        }
        final Object this$livenessRewardFetchStatus = this.getLivenessRewardFetchStatus();
        final Object other$livenessRewardFetchStatus = other.getLivenessRewardFetchStatus();
        if (this$livenessRewardFetchStatus == null) {
            if (other$livenessRewardFetchStatus == null) {
                return true;
            }
        }
        else if (this$livenessRewardFetchStatus.equals(other$livenessRewardFetchStatus)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof TaskProgress;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getLoop();
        final Object $finishCounts = this.getFinishCounts();
        result = result * 59 + (($finishCounts == null) ? 43 : $finishCounts.hashCode());
        final Object $finishTallyCounts = this.getFinishTallyCounts();
        result = result * 59 + (($finishTallyCounts == null) ? 43 : $finishTallyCounts.hashCode());
        final Object $finishRecord = this.getFinishRecord();
        result = result * 59 + (($finishRecord == null) ? 43 : $finishRecord.hashCode());
        final Object $everyDayUpperLimit = this.getEveryDayUpperLimit();
        result = result * 59 + (($everyDayUpperLimit == null) ? 43 : $everyDayUpperLimit.hashCode());
        final Object $everyDayRecharge = this.getEveryDayRecharge();
        result = result * 59 + (($everyDayRecharge == null) ? 43 : $everyDayRecharge.hashCode());
        result = result * 59 + Float.floatToIntBits(this.getLiveness());
        final Object $livenessRewardFetchStatus = this.getLivenessRewardFetchStatus();
        result = result * 59 + (($livenessRewardFetchStatus == null) ? 43 : $livenessRewardFetchStatus.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "TaskProgress(loop=" + this.getLoop() + ", finishCounts=" + this.getFinishCounts() + ", finishTallyCounts=" + this.getFinishTallyCounts() + ", finishRecord=" + this.getFinishRecord() + ", everyDayUpperLimit=" + this.getEveryDayUpperLimit() + ", everyDayRecharge=" + this.getEveryDayRecharge() + ", liveness=" + this.getLiveness() + ", livenessRewardFetchStatus=" + this.getLivenessRewardFetchStatus() + ")";
    }
}
