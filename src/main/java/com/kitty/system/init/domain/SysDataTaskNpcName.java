
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
        final SysDataTaskNpcName other = (SysDataTaskNpcName)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$key = this.getKey();
        final Object other$key = other.getKey();
        Label_0065: {
            if (this$key == null) {
                if (other$key == null) {
                    break Label_0065;
                }
            }
            else if (this$key.equals(other$key)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null) {
            if (other$name == null) {
                return true;
            }
        }
        else if (this$name.equals(other$name)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataTaskNpcName;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $key = this.getKey();
        result = result * 59 + (($key == null) ? 43 : $key.hashCode());
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataTaskNpcName(key=" + this.getKey() + ", name=" + this.getName() + ")";
    }
}
