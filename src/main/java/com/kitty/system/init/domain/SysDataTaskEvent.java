package com.kitty.system.init.domain;

import com.kitty.system.enums.task.TaskEvent;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_task_event")
public class SysDataTaskEvent implements Serializable
{
    @Comment("事件编号")
    @Column("serial")
    private int serial;
    @Comment("执行编号")
    @Column("event")
    private int event;
    @Comment("类型")
    @Column("type")
    private TaskEvent type;
    @Comment("事件外键")
    @Column("foreign")
    private int foreign;
    
    public SysDataTaskEvent() {
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getEvent() {
        return this.event;
    }
    
    public TaskEvent getType() {
        return this.type;
    }
    
    public int getForeign() {
        return this.foreign;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setEvent(final int event) {
        this.event = event;
    }
    
    public void setType(final TaskEvent type) {
        this.type = type;
    }
    
    public void setForeign(final int foreign) {
        this.foreign = foreign;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataTaskEvent)) {
            return false;
        }
        SysDataTaskEvent other = (SysDataTaskEvent)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getEvent() != other.getEvent()) {
            return false;
        }
        Object this$type = this.getType();
        Object other$type = other.getType();
        return ((this$type != null) ? this$type.equals(other$type) : (other$type == null)) && this.getForeign() == other.getForeign();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataTaskEvent;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getEvent();
        Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        result = result * 59 + this.getForeign();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataTaskEvent(serial=" + this.getSerial() + ", event=" + this.getEvent() + ", type=" + this.getType() + ", foreign=" + this.getForeign() + ")";
    }
}
