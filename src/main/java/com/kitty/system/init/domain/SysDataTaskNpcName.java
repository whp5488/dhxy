package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("data_task_npc_name")
public class SysDataTaskNpcName
{
    @Column("key")
    private String key;
    @Column("name")
    private String name;
    
    public SysDataTaskNpcName() {
    }
    
    public String getKey() {
        return this.key;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setKey(final String key) {
        this.key = key;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataTaskNpcName)) {
            return false;
        }
        SysDataTaskNpcName other = (SysDataTaskNpcName)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$key = this.getKey();
        Object other$key = other.getKey();
        if (!((this$key != null) ? this$key.equals(other$key) : (other$key == null))) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        return (this$name != null) ? this$name.equals(other$name) : (other$name == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataTaskNpcName;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $key = this.getKey();
        result = result * 59 + (($key == null) ? 43 : $key.hashCode());
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataTaskNpcName(key=" + this.getKey() + ", name=" + this.getName() + ")";
    }
}
