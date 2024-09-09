package com.kitty.system.init.domain.vo.surround;

import com.kitty.system.enums.task.TaskType;
import java.util.List;
import java.io.Serializable;

public class SysDataTaskCrusadeRenegade implements Serializable
{
    private int serial;
    private int nextTaskSerial;
    private int submitNpc;
    private int giveUp;
    private int needFight;
    private int getCondition;
    private int submitCondition;
    private List<Integer> addTaskEvent;
    private List<Integer> finishTaskEvent;
    private List<Integer> askEvent;
    private TaskType taskType;
    private String taskReward;
    
    public SysDataTaskCrusadeRenegade() {
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
    
    public int getNeedFight() {
        return this.needFight;
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
    
    public String getTaskReward() {
        return this.taskReward;
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
    
    public void setNeedFight(final int needFight) {
        this.needFight = needFight;
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
    
    public void setTaskReward(final String taskReward) {
        this.taskReward = taskReward;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataTaskCrusadeRenegade)) {
            return false;
        }
        SysDataTaskCrusadeRenegade other = (SysDataTaskCrusadeRenegade)o;
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
        if (this.getNeedFight() != other.getNeedFight()) {
            return false;
        }
        if (this.getGetCondition() != other.getGetCondition()) {
            return false;
        }
        if (this.getSubmitCondition() != other.getSubmitCondition()) {
            return false;
        }
        Object this$addTaskEvent = this.getAddTaskEvent();
        Object other$addTaskEvent = other.getAddTaskEvent();
        if (!((this$addTaskEvent != null) ? this$addTaskEvent.equals(other$addTaskEvent) : (other$addTaskEvent == null))) {
            return false;
        }
        Object this$finishTaskEvent = this.getFinishTaskEvent();
        Object other$finishTaskEvent = other.getFinishTaskEvent();
        if (!((this$finishTaskEvent != null) ? this$finishTaskEvent.equals(other$finishTaskEvent) : (other$finishTaskEvent == null))) {
            return false;
        }
        Object this$askEvent = this.getAskEvent();
        Object other$askEvent = other.getAskEvent();
        if (!((this$askEvent != null) ? this$askEvent.equals(other$askEvent) : (other$askEvent == null))) {
            return false;
        }
        Object this$taskType = this.getTaskType();
        Object other$taskType = other.getTaskType();
        if (!((this$taskType != null) ? this$taskType.equals(other$taskType) : (other$taskType == null))) {
            return false;
        }
        Object this$taskReward = this.getTaskReward();
        Object other$taskReward = other.getTaskReward();
        return (this$taskReward != null) ? this$taskReward.equals(other$taskReward) : (other$taskReward == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataTaskCrusadeRenegade;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getNextTaskSerial();
        result = result * 59 + this.getSubmitNpc();
        result = result * 59 + this.getGiveUp();
        result = result * 59 + this.getNeedFight();
        result = result * 59 + this.getGetCondition();
        result = result * 59 + this.getSubmitCondition();
        Object $addTaskEvent = this.getAddTaskEvent();
        result = result * 59 + (($addTaskEvent == null) ? 43 : $addTaskEvent.hashCode());
        Object $finishTaskEvent = this.getFinishTaskEvent();
        result = result * 59 + (($finishTaskEvent == null) ? 43 : $finishTaskEvent.hashCode());
        Object $askEvent = this.getAskEvent();
        result = result * 59 + (($askEvent == null) ? 43 : $askEvent.hashCode());
        Object $taskType = this.getTaskType();
        result = result * 59 + (($taskType == null) ? 43 : $taskType.hashCode());
        Object $taskReward = this.getTaskReward();
        result = result * 59 + (($taskReward == null) ? 43 : $taskReward.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataTaskCrusadeRenegade(serial=" + this.getSerial() + ", nextTaskSerial=" + this.getNextTaskSerial() + ", submitNpc=" + this.getSubmitNpc() + ", giveUp=" + this.getGiveUp() + ", needFight=" + this.getNeedFight() + ", getCondition=" + this.getGetCondition() + ", submitCondition=" + this.getSubmitCondition() + ", addTaskEvent=" + this.getAddTaskEvent() + ", finishTaskEvent=" + this.getFinishTaskEvent() + ", askEvent=" + this.getAskEvent() + ", taskType=" + this.getTaskType() + ", taskReward=" + this.getTaskReward() + ")";
    }
}
