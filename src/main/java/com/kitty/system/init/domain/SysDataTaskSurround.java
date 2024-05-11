
package com.kitty.system.init.domain;

import com.kitty.system.enums.task.TaskType;
import org.nutz.dao.entity.annotation.Default;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_task_surround")
public class SysDataTaskSurround implements Serializable
{
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
    @Comment("任务奖励")
    @Column("task_reward")
    private String taskReward;
    
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
        if (!(o instanceof SysDataTaskSurround)) {
            return false;
        }
        final SysDataTaskSurround other = (SysDataTaskSurround)o;
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
        final Object this$addTaskEvent = this.getAddTaskEvent();
        final Object other$addTaskEvent = other.getAddTaskEvent();
        Label_0156: {
            if (this$addTaskEvent == null) {
                if (other$addTaskEvent == null) {
                    break Label_0156;
                }
            }
            else if (this$addTaskEvent.equals(other$addTaskEvent)) {
                break Label_0156;
            }
            return false;
        }
        final Object this$finishTaskEvent = this.getFinishTaskEvent();
        final Object other$finishTaskEvent = other.getFinishTaskEvent();
        Label_0193: {
            if (this$finishTaskEvent == null) {
                if (other$finishTaskEvent == null) {
                    break Label_0193;
                }
            }
            else if (this$finishTaskEvent.equals(other$finishTaskEvent)) {
                break Label_0193;
            }
            return false;
        }
        final Object this$askEvent = this.getAskEvent();
        final Object other$askEvent = other.getAskEvent();
        Label_0230: {
            if (this$askEvent == null) {
                if (other$askEvent == null) {
                    break Label_0230;
                }
            }
            else if (this$askEvent.equals(other$askEvent)) {
                break Label_0230;
            }
            return false;
        }
        final Object this$taskType = this.getTaskType();
        final Object other$taskType = other.getTaskType();
        Label_0267: {
            if (this$taskType == null) {
                if (other$taskType == null) {
                    break Label_0267;
                }
            }
            else if (this$taskType.equals(other$taskType)) {
                break Label_0267;
            }
            return false;
        }
        final Object this$taskReward = this.getTaskReward();
        final Object other$taskReward = other.getTaskReward();
        if (this$taskReward == null) {
            if (other$taskReward == null) {
                return true;
            }
        }
        else if (this$taskReward.equals(other$taskReward)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataTaskSurround;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getNextTaskSerial();
        result = result * 59 + this.getSubmitNpc();
        result = result * 59 + this.getGiveUp();
        result = result * 59 + this.getNeedFight();
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
        final Object $taskReward = this.getTaskReward();
        result = result * 59 + (($taskReward == null) ? 43 : $taskReward.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataTaskSurround(serial=" + this.getSerial() + ", nextTaskSerial=" + this.getNextTaskSerial() + ", submitNpc=" + this.getSubmitNpc() + ", giveUp=" + this.getGiveUp() + ", needFight=" + this.getNeedFight() + ", getCondition=" + this.getGetCondition() + ", submitCondition=" + this.getSubmitCondition() + ", addTaskEvent=" + this.getAddTaskEvent() + ", finishTaskEvent=" + this.getFinishTaskEvent() + ", askEvent=" + this.getAskEvent() + ", taskType=" + this.getTaskType() + ", taskReward=" + this.getTaskReward() + ")";
    }
}
