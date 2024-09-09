package com.kitty.system.init.domain;

import com.kitty.system.enums.task.TaskEvent;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_task_event_foreign")
public class SysDataTaskEventForeign implements Serializable
{
    @Comment("事件编号")
    @Column("serial")
    private int serial;
    @Comment("类型")
    @Column("type")
    private TaskEvent type;
    @Comment("角色限制（角色编号）")
    @Column("facade_serial")
    private int facadeSerial;
    @Comment("key")
    @Column("k")
    private int k;
    @Comment("value")
    @Column("v")
    private int v;
    @Comment("其他")
    @Column("other")
    private String other;
    
    public SysDataTaskEventForeign() {
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public TaskEvent getType() {
        return this.type;
    }
    
    public int getFacadeSerial() {
        return this.facadeSerial;
    }
    
    public int getK() {
        return this.k;
    }
    
    public int getV() {
        return this.v;
    }
    
    public String getOther() {
        return this.other;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setType(final TaskEvent type) {
        this.type = type;
    }
    
    public void setFacadeSerial(final int facadeSerial) {
        this.facadeSerial = facadeSerial;
    }
    
    public void setK(final int k) {
        this.k = k;
    }
    
    public void setV(final int v) {
        this.v = v;
    }
    
    public void setOther(final String other) {
        this.other = other;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataTaskEventForeign)) {
            return false;
        }
        SysDataTaskEventForeign other = (SysDataTaskEventForeign)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$type = this.getType();
        Object other$type = other.getType();
        if (!((this$type != null) ? this$type.equals(other$type) : (other$type == null))) {
            return false;
        }
        if (this.getFacadeSerial() != other.getFacadeSerial()) {
            return false;
        }
        if (this.getK() != other.getK()) {
            return false;
        }
        if (this.getV() != other.getV()) {
            return false;
        }
        Object this$other = this.getOther();
        Object other$other = other.getOther();
        return (this$other != null) ? this$other.equals(other$other) : (other$other == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataTaskEventForeign;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        result = result * 59 + this.getFacadeSerial();
        result = result * 59 + this.getK();
        result = result * 59 + this.getV();
        Object $other = this.getOther();
        result = result * 59 + (($other == null) ? 43 : $other.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataTaskEventForeign(serial=" + this.getSerial() + ", type=" + this.getType() + ", facadeSerial=" + this.getFacadeSerial() + ", k=" + this.getK() + ", v=" + this.getV() + ", other=" + this.getOther() + ")";
    }
}
