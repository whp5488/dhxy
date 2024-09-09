package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Default;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("data_task_formula")
public class SysDataTaskFormula
{
    @Column("serial")
    private int serial;
    @Column("content")
    @Default("[]")
    private List<FiedValue> content;
    
    public SysDataTaskFormula() {
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public List<FiedValue> getContent() {
        return this.content;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setContent(final List<FiedValue> content) {
        this.content = content;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataTaskFormula)) {
            return false;
        }
        SysDataTaskFormula other = (SysDataTaskFormula)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$content = this.getContent();
        Object other$content = other.getContent();
        return (this$content != null) ? this$content.equals(other$content) : (other$content == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataTaskFormula;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        Object $content = this.getContent();
        result = result * 59 + (($content == null) ? 43 : $content.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataTaskFormula(serial=" + this.getSerial() + ", content=" + this.getContent() + ")";
    }
}
