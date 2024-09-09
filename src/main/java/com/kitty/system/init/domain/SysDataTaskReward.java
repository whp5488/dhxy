package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Default;
import com.kitty.system.init.domain.vo.Param;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_task_reward")
public class SysDataTaskReward implements Serializable
{
    @Column("serial")
    private String serial;
    @Column("rewards")
    @Default("[]")
    @ColDefine(customType = "text")
    private List<Param> rewards;
    @Column("type")
    private int type;
    
    public SysDataTaskReward() {
    }
    
    public String getSerial() {
        return this.serial;
    }
    
    public List<Param> getRewards() {
        return this.rewards;
    }
    
    public int getType() {
        return this.type;
    }
    
    public void setSerial(final String serial) {
        this.serial = serial;
    }
    
    public void setRewards(final List<Param> rewards) {
        this.rewards = rewards;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataTaskReward)) {
            return false;
        }
        SysDataTaskReward other = (SysDataTaskReward)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$serial = this.getSerial();
        Object other$serial = other.getSerial();
        if (!((this$serial != null) ? this$serial.equals(other$serial) : (other$serial == null))) {
            return false;
        }
        Object this$rewards = this.getRewards();
        Object other$rewards = other.getRewards();
        return ((this$rewards != null) ? this$rewards.equals(other$rewards) : (other$rewards == null)) && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataTaskReward;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        Object $rewards = this.getRewards();
        result = result * 59 + (($rewards == null) ? 43 : $rewards.hashCode());
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataTaskReward(serial=" + this.getSerial() + ", rewards=" + this.getRewards() + ", type=" + this.getType() + ")";
    }
}
