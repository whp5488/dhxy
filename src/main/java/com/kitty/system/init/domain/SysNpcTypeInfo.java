package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_npc_type_info")
public class SysNpcTypeInfo implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("主键")
    @Column("id")
    private Integer id;
    @Comment("按钮名称")
    @Column("name")
    private String name;
    @Comment("转生限值")
    @Column("zs")
    private Integer zs;
    @Comment("等级限值")
    @Column("lv")
    private Integer lv;
    
    public Integer getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Integer getZs() {
        return this.zs;
    }
    
    public Integer getLv() {
        return this.lv;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setZs(final Integer zs) {
        this.zs = zs;
    }
    
    public void setLv(final Integer lv) {
        this.lv = lv;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysNpcTypeInfo)) {
            return false;
        }
        SysNpcTypeInfo other = (SysNpcTypeInfo)o;
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
        Object this$zs = this.getZs();
        Object other$zs = other.getZs();
        if (!((this$zs != null) ? this$zs.equals(other$zs) : (other$zs == null))) {
            return false;
        }
        Object this$lv = this.getLv();
        Object other$lv = other.getLv();
        return (this$lv != null) ? this$lv.equals(other$lv) : (other$lv == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysNpcTypeInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        Object $zs = this.getZs();
        result = result * 59 + (($zs == null) ? 43 : $zs.hashCode());
        Object $lv = this.getLv();
        result = result * 59 + (($lv == null) ? 43 : $lv.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysNpcTypeInfo(id=" + this.getId() + ", name=" + this.getName() + ", zs=" + this.getZs() + ", lv=" + this.getLv() + ")";
    }
    
    public SysNpcTypeInfo() {
    }
    
    public SysNpcTypeInfo(final Integer id, final String name, final Integer zs, final Integer lv) {
        this.id = id;
        this.name = name;
        this.zs = zs;
        this.lv = lv;
    }
}
