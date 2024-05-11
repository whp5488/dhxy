
package com.kitty.system.game.config;

import java.util.ArrayList;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.enums.task.TaskType;
import java.util.List;

public class TaskSet implements Cloneable
{
    private int serial;
    private int nextTaskSerial;
    private int submitNpc;
    private int giveUp;
    private int getCondition;
    private int submitCondition;
    private List<Integer> addTaskEvent;
    private List<Integer> finishTaskEvent;
    private List<Integer> askEvent;
    private TaskType taskType;
    private byte kind;
    private List<Integer> dstNpc;
    private int recGrade;
    private String taskReward;
    private int timeoutSec;
    private int needCounter;
    private String para;
    private int previousId;
    
    public TaskSet clone() throws CloneNotSupportedException {
        return (TaskSet)super.clone();
    }
    
    public boolean isTeamAccept() {
        return Const.TEAM_ACCEPT.contains(this.taskType);
    }
    
    public TaskSet() {
        this.kind = 1;
        this.dstNpc = new ArrayList<Integer>();
        this.recGrade = -1;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getNextTaskSerial() {
        return this.nextTaskSerial;
    }
    
    public int getSubmitNpc() {
        return this.submitNpc;
    }
    
    public int getGiveUp() {
        return this.giveUp;
    }
    
    public int getGetCondition() {
        return this.getCondition;
    }
    
    public int getSubmitCondition() {
        return this.submitCondition;
    }
    
    public List<Integer> getAddTaskEvent() {
        return this.addTaskEvent;
    }
    
    public List<Integer> getFinishTaskEvent() {
        return this.finishTaskEvent;
    }
    
    public List<Integer> getAskEvent() {
        return this.askEvent;
    }
    
    public TaskType getTaskType() {
        return this.taskType;
    }
    
    public byte getKind() {
        return this.kind;
    }
    
    public List<Integer> getDstNpc() {
        return this.dstNpc;
    }
    
    public int getRecGrade() {
        return this.recGrade;
    }
    
    public String getTaskReward() {
        return this.taskReward;
    }
    
    public int getTimeoutSec() {
        return this.timeoutSec;
    }
    
    public int getNeedCounter() {
        return this.needCounter;
    }
    
    public String getPara() {
        return this.para;
    }
    
    public int getPreviousId() {
        return this.previousId;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setNextTaskSerial(final int nextTaskSerial) {
        this.nextTaskSerial = nextTaskSerial;
    }
    
    public void setSubmitNpc(final int submitNpc) {
        this.submitNpc = submitNpc;
    }
    
    public void setGiveUp(final int giveUp) {
        this.giveUp = giveUp;
    }
    
    public void setGetCondition(final int getCondition) {
        this.getCondition = getCondition;
    }
    
    public void setSubmitCondition(final int submitCondition) {
        this.submitCondition = submitCondition;
    }
    
    public void setAddTaskEvent(final List<Integer> addTaskEvent) {
        this.addTaskEvent = addTaskEvent;
    }
    
    public void setFinishTaskEvent(final List<Integer> finishTaskEvent) {
        this.finishTaskEvent = finishTaskEvent;
    }
    
    public void setAskEvent(final List<Integer> askEvent) {
        this.askEvent = askEvent;
    }
    
    public void setTaskType(final TaskType taskType) {
        this.taskType = taskType;
    }
    
    public void setKind(final byte kind) {
        this.kind = kind;
    }
    
    public void setDstNpc(final List<Integer> dstNpc) {
        this.dstNpc = dstNpc;
    }
    
    public void setRecGrade(final int recGrade) {
        this.recGrade = recGrade;
    }
    
    public void setTaskReward(final String taskReward) {
        this.taskReward = taskReward;
    }
    
    public void setTimeoutSec(final int timeoutSec) {
        this.timeoutSec = timeoutSec;
    }
    
    public void setNeedCounter(final int needCounter) {
        this.needCounter = needCounter;
    }
    
    public void setPara(final String para) {
        this.para = para;
    }
    
    public void setPreviousId(final int previousId) {
        this.previousId = previousId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TaskSet)) {
            return false;
        }
        final TaskSet other = (TaskSet)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getNextTaskSerial() != other.getNextTaskSerial()) {
            return false;
        }
        if (this.getSubmitNpc() != other.getSubmitNpc()) {
            return false;
        }
        if (this.getGiveUp() != other.getGiveUp()) {
            return false;
        }
        if (this.getGetCondition() != other.getGetCondition()) {
            return false;
        }
        if (this.getSubmitCondition() != other.getSubmitCondition()) {
            return false;
        }
        final Object this$addTaskEvent = this.getAddTaskEvent();
        final Object other$addTaskEvent = other.getAddTaskEvent();
        Label_0143: {
            if (this$addTaskEvent == null) {
                if (other$addTaskEvent == null) {
                    break Label_0143;
                }
            }
            else if (this$addTaskEvent.equals(other$addTaskEvent)) {
                break Label_0143;
            }
            return false;
        }
        final Object this$finishTaskEvent = this.getFinishTaskEvent();
        final Object other$finishTaskEvent = other.getFinishTaskEvent();
        Label_0180: {
            if (this$finishTaskEvent == null) {
                if (other$finishTaskEvent == null) {
                    break Label_0180;
                }
            }
            else if (this$finishTaskEvent.equals(other$finishTaskEvent)) {
                break Label_0180;
            }
            return false;
        }
        final Object this$askEvent = this.getAskEvent();
        final Object other$askEvent = other.getAskEvent();
        Label_0217: {
            if (this$askEvent == null) {
                if (other$askEvent == null) {
                    break Label_0217;
                }
            }
            else if (this$askEvent.equals(other$askEvent)) {
                break Label_0217;
            }
            return false;
        }
        final Object this$taskType = this.getTaskType();
        final Object other$taskType = other.getTaskType();
        Label_0254: {
            if (this$taskType == null) {
                if (other$taskType == null) {
                    break Label_0254;
                }
            }
            else if (this$taskType.equals(other$taskType)) {
                break Label_0254;
            }
            return false;
        }
        if (this.getKind() != other.getKind()) {
            return false;
        }
        final Object this$dstNpc = this.getDstNpc();
        final Object other$dstNpc = other.getDstNpc();
        Label_0304: {
            if (this$dstNpc == null) {
                if (other$dstNpc == null) {
                    break Label_0304;
                }
            }
            else if (this$dstNpc.equals(other$dstNpc)) {
                break Label_0304;
            }
            return false;
        }
        if (this.getRecGrade() != other.getRecGrade()) {
            return false;
        }
        final Object this$taskReward = this.getTaskReward();
        final Object other$taskReward = other.getTaskReward();
        Label_0354: {
            if (this$taskReward == null) {
                if (other$taskReward == null) {
                    break Label_0354;
                }
            }
            else if (this$taskReward.equals(other$taskReward)) {
                break Label_0354;
            }
            return false;
        }
        if (this.getTimeoutSec() != other.getTimeoutSec()) {
            return false;
        }
        if (this.getNeedCounter() != other.getNeedCounter()) {
            return false;
        }
        final Object this$para = this.getPara();
        final Object other$para = other.getPara();
        if (this$para == null) {
            if (other$para == null) {
                return this.getPreviousId() == other.getPreviousId();
            }
        }
        else if (this$para.equals(other$para)) {
            return this.getPreviousId() == other.getPreviousId();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof TaskSet;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getNextTaskSerial();
        result = result * 59 + this.getSubmitNpc();
        result = result * 59 + this.getGiveUp();
        result = result * 59 + this.getGetCondition();
        result = result * 59 + this.getSubmitCondition();
        final Object $addTaskEvent = this.getAddTaskEvent();
        result = result * 59 + (($addTaskEvent == null) ? 43 : $addTaskEvent.hashCode());
        final Object $finishTaskEvent = this.getFinishTaskEvent();
        result = result * 59 + (($finishTaskEvent == null) ? 43 : $finishTaskEvent.hashCode());
        final Object $askEvent = this.getAskEvent();
        result = result * 59 + (($askEvent == null) ? 43 : $askEvent.hashCode());
        final Object $taskType = this.getTaskType();
        result = result * 59 + (($taskType == null) ? 43 : $taskType.hashCode());
        result = result * 59 + this.getKind();
        final Object $dstNpc = this.getDstNpc();
        result = result * 59 + (($dstNpc == null) ? 43 : $dstNpc.hashCode());
        result = result * 59 + this.getRecGrade();
        final Object $taskReward = this.getTaskReward();
        result = result * 59 + (($taskReward == null) ? 43 : $taskReward.hashCode());
        result = result * 59 + this.getTimeoutSec();
        result = result * 59 + this.getNeedCounter();
        final Object $para = this.getPara();
        result = result * 59 + (($para == null) ? 43 : $para.hashCode());
        result = result * 59 + this.getPreviousId();
        return result;
    }
    
    @Override
    public String toString() {
        return "TaskSet(serial=" + this.getSerial() + ", nextTaskSerial=" + this.getNextTaskSerial() + ", submitNpc=" + this.getSubmitNpc() + ", giveUp=" + this.getGiveUp() + ", getCondition=" + this.getGetCondition() + ", submitCondition=" + this.getSubmitCondition() + ", addTaskEvent=" + this.getAddTaskEvent() + ", finishTaskEvent=" + this.getFinishTaskEvent() + ", askEvent=" + this.getAskEvent() + ", taskType=" + this.getTaskType() + ", kind=" + this.getKind() + ", dstNpc=" + this.getDstNpc() + ", recGrade=" + this.getRecGrade() + ", taskReward=" + this.getTaskReward() + ", timeoutSec=" + this.getTimeoutSec() + ", needCounter=" + this.getNeedCounter() + ", para=" + this.getPara() + ", previousId=" + this.getPreviousId() + ")";
    }
}
