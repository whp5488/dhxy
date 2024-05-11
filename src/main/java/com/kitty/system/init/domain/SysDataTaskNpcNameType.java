
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
        final SysDataTaskNpcNameType other = (SysDataTaskNpcNameType)o;
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
        final Object this$value = this.getValue();
        final Object other$value = other.getValue();
        if (this$value == null) {
            if (other$value == null) {
                return true;
            }
        }
        else if (this$value.equals(other$value)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataTaskNpcNameType;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $key = this.getKey();
        result = result * 59 + (($key == null) ? 43 : $key.hashCode());
        final Object $value = this.getValue();
        result = result * 59 + (($value == null) ? 43 : $value.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataTaskNpcNameType(key=" + this.getKey() + ", value=" + this.getValue() + ")";
    }
}
