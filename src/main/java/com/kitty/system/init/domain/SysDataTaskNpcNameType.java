package com.kitty.system.init.domain;

import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("data_task_npc_name_type")
public class SysDataTaskNpcNameType
{
    @Column("key")
    private String key;
    @Column("value")
    private List<String> value;
    
    public SysDataTaskNpcNameType() {
    }
    
    public String getKey() {
        return this.key;
    }
    
    public List<String> getValue() {
        return this.value;
    }
    
    public void setKey(final String key) {
        this.key = key;
    }
    
    public void setValue(final List<String> value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataTaskNpcNameType)) {
            return false;
        }
        SysDataTaskNpcNameType other = (SysDataTaskNpcNameType)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$key = this.getKey();
        Object other$key = other.getKey();
        if (!((this$key != null) ? this$key.equals(other$key) : (other$key == null))) {
            return false;
        }
        Object this$value = this.getValue();
        Object other$value = other.getValue();
        return (this$value != null) ? this$value.equals(other$value) : (other$value == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataTaskNpcNameType;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $key = this.getKey();
        result = result * 59 + (($key == null) ? 43 : $key.hashCode());
        Object $value = this.getValue();
        result = result * 59 + (($value == null) ? 43 : $value.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataTaskNpcNameType(key=" + this.getKey() + ", value=" + this.getValue() + ")";
    }
}
