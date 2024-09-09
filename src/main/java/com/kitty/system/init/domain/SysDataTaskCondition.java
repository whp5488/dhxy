package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Default;
import com.kitty.system.init.domain.vo.Param;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_task_condition")
public class SysDataTaskCondition implements Serializable
{
    @Comment("提交编号")
    @Column("submit_condition")
    private int submitCondition;
    @Comment("计数")
    @Column("need_counter")
    @Default("[]")
    private List<Param> needCounter;
    @Comment("前置任务编号")
    @Column("pre_task")
    private int preTask;
    @Comment("等级需求")
    @Column("need_grade")
    private int needGrade;
    
    public SysDataTaskCondition() {
    }
    
    public int getSubmitCondition() {
        return this.submitCondition;
    }
    
    public List<Param> getNeedCounter() {
        return this.needCounter;
    }
    
    public int getPreTask() {
        return this.preTask;
    }
    
    public int getNeedGrade() {
        return this.needGrade;
    }
    
    public void setSubmitCondition(final int submitCondition) {
        this.submitCondition = submitCondition;
    }
    
    public void setNeedCounter(final List<Param> needCounter) {
        this.needCounter = needCounter;
    }
    
    public void setPreTask(final int preTask) {
        this.preTask = preTask;
    }
    
    public void setNeedGrade(final int needGrade) {
        this.needGrade = needGrade;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataTaskCondition)) {
            return false;
        }
        SysDataTaskCondition other = (SysDataTaskCondition)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSubmitCondition() != other.getSubmitCondition()) {
            return false;
        }
        Object this$needCounter = this.getNeedCounter();
        Object other$needCounter = other.getNeedCounter();
        return ((this$needCounter != null) ? this$needCounter.equals(other$needCounter) : (other$needCounter == null)) && this.getPreTask() == other.getPreTask() && this.getNeedGrade() == other.getNeedGrade();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataTaskCondition;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSubmitCondition();
        Object $needCounter = this.getNeedCounter();
        result = result * 59 + (($needCounter == null) ? 43 : $needCounter.hashCode());
        result = result * 59 + this.getPreTask();
        result = result * 59 + this.getNeedGrade();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataTaskCondition(submitCondition=" + this.getSubmitCondition() + ", needCounter=" + this.getNeedCounter() + ", preTask=" + this.getPreTask() + ", needGrade=" + this.getNeedGrade() + ")";
    }
}
