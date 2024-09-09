package com.kitty.system.init.domain;

import com.kitty.system.enums.task.TaskType;
import org.nutz.dao.entity.annotation.Default;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_task_mo")
public class SysDataTaskMo implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("任务编号")
    @Column("task_serial")
    private int serial;
    @Comment("下个任务编号")
    @Column("next_task_serial")
    private int nextTaskSerial;
    @Comment("提交NPC编号")
    @Column("submit_npc")
    private int submitNpc;
    @Comment("是否可以放弃： 1.是 0.否")
    @Column("give_up")
    private int giveUp;
    @Comment("是否战斗：0否，1.是")
    @Column("need_fight")
    private int needFight;
    @Comment("接取任务条件")
    @Column("get_condition")
    private int getCondition;
    @Comment("提交任务条件")
    @Column("submit_condition")
    private int submitCondition;
    @Comment("添加任务事件")
    @Column("add_task_event")
    @Default("[]")
    private List<Integer> addTaskEvent;
    @Comment("完成任务事件")
    @Column("finish_task_event")
    @Default("[]")
    private List<Integer> finishTaskEvent;
    @Comment("客户端请求的事件")
    @Column("ask_event")
    @Default("[]")
    private List<Integer> askEvent;
    @Comment("任务类型")
    @Column("task_type")
    private TaskType taskType;
    @Comment("类型：0.对话 1.战斗")
    @Column("type")
    private int type;
    @Comment("任务奖励")
    @Column("task_reward")
    private String taskReward;
    @Comment("任务超时(-1 则没有超时时间)ms")
    @Column("timeout_sec")
    private int timeoutSec;
    
    public SysDataTaskMo() {
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
    
    public int getType() {
        return this.type;
    }
    
    public String getTaskReward() {
        return this.taskReward;
    }
    
    public int getTimeoutSec() {
        return this.timeoutSec;
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
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setTaskReward(final String taskReward) {
        this.taskReward = taskReward;
    }
    
    public void setTimeoutSec(final int timeoutSec) {
        this.timeoutSec = timeoutSec;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataTaskMo)) {
            return false;
        }
        SysDataTaskMo other = (SysDataTaskMo)o;
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
        if (this.getType() != other.getType()) {
            return false;
        }
        Object this$taskReward = this.getTaskReward();
        Object other$taskReward = other.getTaskReward();
        return ((this$taskReward != null) ? this$taskReward.equals(other$taskReward) : (other$taskReward == null)) && this.getTimeoutSec() == other.getTimeoutSec();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataTaskMo;
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
        result = result * 59 + this.getType();
        Object $taskReward = this.getTaskReward();
        result = result * 59 + (($taskReward == null) ? 43 : $taskReward.hashCode());
        result = result * 59 + this.getTimeoutSec();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataTaskMo(serial=" + this.getSerial() + ", nextTaskSerial=" + this.getNextTaskSerial() + ", submitNpc=" + this.getSubmitNpc() + ", giveUp=" + this.getGiveUp() + ", needFight=" + this.getNeedFight() + ", getCondition=" + this.getGetCondition() + ", submitCondition=" + this.getSubmitCondition() + ", addTaskEvent=" + this.getAddTaskEvent() + ", finishTaskEvent=" + this.getFinishTaskEvent() + ", askEvent=" + this.getAskEvent() + ", taskType=" + this.getTaskType() + ", type=" + this.getType() + ", taskReward=" + this.getTaskReward() + ", timeoutSec=" + this.getTimeoutSec() + ")";
    }
}
