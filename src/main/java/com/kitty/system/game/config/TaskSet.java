package com.kitty.system.game.config;

import java.util.ArrayList;
import com.kitty.system.game.vo.utils.Const;
import java.util.Arrays;
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
        List<TaskType> TEAM_ACCEPT = Arrays.asList(TaskType.CM, TaskType.XL);
        return TEAM_ACCEPT.contains(this.taskType) || Const.TEAM_ACCEPT.contains(this.taskType);
    }
    
    public TaskSet() {
        this.kind = 1;
        this.dstNpc = new ArrayList<>();
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
    
    public void setSerial(int serial) {
        this.serial = serial;
    }
    
    public void setNextTaskSerial(int nextTaskSerial) {
        this.nextTaskSerial = nextTaskSerial;
    }
    
    public void setSubmitNpc(int submitNpc) {
        this.submitNpc = submitNpc;
    }
    
    public void setGiveUp(int giveUp) {
        this.giveUp = giveUp;
    }
    
    public void setGetCondition(int getCondition) {
        this.getCondition = getCondition;
    }
    
    public void setSubmitCondition(int submitCondition) {
        this.submitCondition = submitCondition;
    }
    
    public void setAddTaskEvent(List<Integer> addTaskEvent) {
        this.addTaskEvent = addTaskEvent;
    }
    
    public void setFinishTaskEvent(List<Integer> finishTaskEvent) {
        this.finishTaskEvent = finishTaskEvent;
    }
    
    public void setAskEvent(List<Integer> askEvent) {
        this.askEvent = askEvent;
    }
    
    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }
    
    public void setKind(byte kind) {
        this.kind = kind;
    }
    
    public void setDstNpc(List<Integer> dstNpc) {
        this.dstNpc = dstNpc;
    }
    
    public void setRecGrade(int recGrade) {
        this.recGrade = recGrade;
    }
    
    public void setTaskReward(String taskReward) {
        this.taskReward = taskReward;
    }
    
    public void setTimeoutSec(int timeoutSec) {
        this.timeoutSec = timeoutSec;
    }
    
    public void setNeedCounter(int needCounter) {
        this.needCounter = needCounter;
    }
    
    public void setPara(String para) {
        this.para = para;
    }
    
    public void setPreviousId(int previousId) {
        this.previousId = previousId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TaskSet)) {
            return false;
        }
        TaskSet other = (TaskSet)o;
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
        Object this$addTaskEvent = this.getAddTaskEvent();
        Object other$addTaskEvent = other.getAddTaskEvent();
        if ((this$addTaskEvent == null) ? (other$addTaskEvent != null) : (!this$addTaskEvent.equals(other$addTaskEvent))) {
            return false;
        }
        Object this$finishTaskEvent = this.getFinishTaskEvent();
        Object other$finishTaskEvent = other.getFinishTaskEvent();
        if ((this$finishTaskEvent == null) ? (other$finishTaskEvent != null) : (!this$finishTaskEvent.equals(other$finishTaskEvent))) {
            return false;
        }
        Object this$askEvent = this.getAskEvent();
        Object other$askEvent = other.getAskEvent();
        if ((this$askEvent == null) ? (other$askEvent != null) : (!this$askEvent.equals(other$askEvent))) {
            return false;
        }
        Object this$taskType = this.getTaskType();
        Object other$taskType = other.getTaskType();
        if ((this$taskType == null) ? (other$taskType != null) : (!this$taskType.equals(other$taskType))) {
            return false;
        }
        if (this.getKind() != other.getKind()) {
            return false;
        }
        Object this$dstNpc = this.getDstNpc();
        Object other$dstNpc = other.getDstNpc();
        if ((this$dstNpc == null) ? (other$dstNpc != null) : (!this$dstNpc.equals(other$dstNpc))) {
            return false;
        }
        if (this.getRecGrade() != other.getRecGrade()) {
            return false;
        }
        Object this$taskReward = this.getTaskReward();
        Object other$taskReward = other.getTaskReward();
        if ((this$taskReward == null) ? (other$taskReward != null) : (!this$taskReward.equals(other$taskReward))) {
            return false;
        }
        if (this.getTimeoutSec() != other.getTimeoutSec()) {
            return false;
        }
        if (this.getNeedCounter() != other.getNeedCounter()) {
            return false;
        }
        Object this$para = this.getPara();
        Object other$para = other.getPara();
        if (this$para != null) {
            if (this$para.equals(other$para)) {
                return this.getPreviousId() == other.getPreviousId();
            }
        }
        else if (other$para == null) {
            return this.getPreviousId() == other.getPreviousId();
        }
        return false;
    }
    
    protected boolean canEqual(Object other) {
        return other instanceof TaskSet;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getNextTaskSerial();
        result = result * 59 + this.getSubmitNpc();
        result = result * 59 + this.getGiveUp();
        result = result * 59 + this.getGetCondition();
        result = result * 59 + this.getSubmitCondition();
        Object $addTaskEvent = this.getAddTaskEvent();
        int n = result * 59;
        int hashCode;
        if ($addTaskEvent == null) {
            hashCode = 43;
        }
        else {
            hashCode = $addTaskEvent.hashCode();
        }
        result = n + hashCode;
        Object $finishTaskEvent = this.getFinishTaskEvent();
        int n2 = result * 59;
        int hashCode2;
        if ($finishTaskEvent == null) {
            hashCode2 = 43;
        }
        else {
            hashCode2 = $finishTaskEvent.hashCode();
        }
        result = n2 + hashCode2;
        Object $askEvent = this.getAskEvent();
        int n3 = result * 59;
        int hashCode3;
        if ($askEvent == null) {
            hashCode3 = 43;
        }
        else {
            hashCode3 = $askEvent.hashCode();
        }
        result = n3 + hashCode3;
        Object $taskType = this.getTaskType();
        int n4 = result * 59;
        int hashCode4;
        if ($taskType == null) {
            hashCode4 = 43;
        }
        else {
            hashCode4 = $taskType.hashCode();
        }
        result = n4 + hashCode4;
        result = result * 59 + this.getKind();
        Object $dstNpc = this.getDstNpc();
        int n5 = result * 59;
        int hashCode5;
        if ($dstNpc == null) {
            hashCode5 = 43;
        }
        else {
            hashCode5 = $dstNpc.hashCode();
        }
        result = n5 + hashCode5;
        result = result * 59 + this.getRecGrade();
        Object $taskReward = this.getTaskReward();
        int n6 = result * 59;
        int hashCode6;
        if ($taskReward == null) {
            hashCode6 = 43;
        }
        else {
            hashCode6 = $taskReward.hashCode();
        }
        result = n6 + hashCode6;
        result = result * 59 + this.getTimeoutSec();
        result = result * 59 + this.getNeedCounter();
        Object $para = this.getPara();
        int n7 = result * 59;
        int hashCode7;
        if ($para == null) {
            hashCode7 = 43;
        }
        else {
            hashCode7 = $para.hashCode();
        }
        result = n7 + hashCode7;
        result = result * 59 + this.getPreviousId();
        return result;
    }
    
    @Override
    public String toString() {
        return "TaskSet(serial=" + this.getSerial() + ", nextTaskSerial=" + this.getNextTaskSerial() + ", submitNpc=" + this.getSubmitNpc() + ", giveUp=" + this.getGiveUp() + ", getCondition=" + this.getGetCondition() + ", submitCondition=" + this.getSubmitCondition() + ", addTaskEvent=" + this.getAddTaskEvent() + ", finishTaskEvent=" + this.getFinishTaskEvent() + ", askEvent=" + this.getAskEvent() + ", taskType=" + this.getTaskType() + ", kind=" + (int)this.getKind() + ", dstNpc=" + this.getDstNpc() + ", recGrade=" + this.getRecGrade() + ", taskReward=" + this.getTaskReward() + ", timeoutSec=" + this.getTimeoutSec() + ", needCounter=" + this.getNeedCounter() + ", para=" + this.getPara() + ", previousId=" + this.getPreviousId() + ")";
    }
}
