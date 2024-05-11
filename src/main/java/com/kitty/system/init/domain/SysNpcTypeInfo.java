
package com.kitty.system.init.domain;

import java.beans.ConstructorProperties;
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
        final SysNpcTypeInfo other = (SysNpcTypeInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        Label_0065: {
            if (this$id == null) {
                if (other$id == null) {
                    break Label_0065;
                }
            }
            else if (this$id.equals(other$id)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0102: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0102;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$zs = this.getZs();
        final Object other$zs = other.getZs();
        Label_0139: {
            if (this$zs == null) {
                if (other$zs == null) {
                    break Label_0139;
                }
            }
            else if (this$zs.equals(other$zs)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$lv = this.getLv();
        final Object other$lv = other.getLv();
        if (this$lv == null) {
            if (other$lv == null) {
                return true;
            }
        }
        else if (this$lv.equals(other$lv)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysNpcTypeInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $zs = this.getZs();
        result = result * 59 + (($zs == null) ? 43 : $zs.hashCode());
        final Object $lv = this.getLv();
        result = result * 59 + (($lv == null) ? 43 : $lv.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysNpcTypeInfo(id=" + this.getId() + ", name=" + this.getName() + ", zs=" + this.getZs() + ", lv=" + this.getLv() + ")";
    }
    
    public SysNpcTypeInfo() {
    }
    
    @ConstructorProperties({ "id", "name", "zs", "lv" })
    public SysNpcTypeInfo(final Integer id, final String name, final Integer zs, final Integer lv) {
        this.id = id;
        this.name = name;
        this.zs = zs;
        this.lv = lv;
    }
}
