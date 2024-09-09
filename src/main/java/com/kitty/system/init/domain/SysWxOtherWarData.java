package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_wx_other_war_data")
public class SysWxOtherWarData implements Serializable
{
    @Comment("主键")
    @Column("id")
    private Integer id;
    @Comment("名称")
    @Column("name")
    private String name;
    @Comment("属性值")
    @Column("value")
    private Double value;
    
    public SysWxOtherWarData() {
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Double getValue() {
        return this.value;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setValue(final Double value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysWxOtherWarData)) {
            return false;
        }
        SysWxOtherWarData other = (SysWxOtherWarData)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        Object this$value = this.getValue();
        Object other$value = other.getValue();
        return (this$value != null) ? this$value.equals(other$value) : (other$value == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysWxOtherWarData;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        Object $value = this.getValue();
        result = result * 59 + (($value == null) ? 43 : $value.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysWxOtherWarData(id=" + this.getId() + ", name=" + this.getName() + ", value=" + this.getValue() + ")";
    }
}
