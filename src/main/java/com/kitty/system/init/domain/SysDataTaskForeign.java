package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_task_reward_foreign")
public class SysDataTaskForeign implements Serializable
{
    @Column("serial")
    private int serial;
    @Column("role_icon")
    private int roleIcon;
    @Column("task_reward_serial")
    private String taskRewardSerial;
    
    public SysDataTaskForeign() {
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getRoleIcon() {
        return this.roleIcon;
    }
    
    public String getTaskRewardSerial() {
        return this.taskRewardSerial;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setRoleIcon(final int roleIcon) {
        this.roleIcon = roleIcon;
    }
    
    public void setTaskRewardSerial(final String taskRewardSerial) {
        this.taskRewardSerial = taskRewardSerial;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataTaskForeign)) {
            return false;
        }
        SysDataTaskForeign other = (SysDataTaskForeign)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getRoleIcon() != other.getRoleIcon()) {
            return false;
        }
        Object this$taskRewardSerial = this.getTaskRewardSerial();
        Object other$taskRewardSerial = other.getTaskRewardSerial();
        return (this$taskRewardSerial != null) ? this$taskRewardSerial.equals(other$taskRewardSerial) : (other$taskRewardSerial == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataTaskForeign;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getRoleIcon();
        Object $taskRewardSerial = this.getTaskRewardSerial();
        result = result * 59 + (($taskRewardSerial == null) ? 43 : $taskRewardSerial.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataTaskForeign(serial=" + this.getSerial() + ", roleIcon=" + this.getRoleIcon() + ", taskRewardSerial=" + this.getTaskRewardSerial() + ")";
    }
}
