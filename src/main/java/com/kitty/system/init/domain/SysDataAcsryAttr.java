package com.kitty.system.init.domain;

import com.kitty.system.init.domain.vo.Prop;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Default;
import com.kitty.system.common.enter.FieldValue;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("data_acsry_attr_lib")
public class SysDataAcsryAttr
{
    @Column("dict")
    private String dict;
    @Column("base_attr")
    @Default("[]")
    @ColDefine(customType = "longtext")
    private List<FieldValue> baseAttrList;
    @Column("rnd_range")
    @Default("[]")
    private List<Prop> prop;
    
    public SysDataAcsryAttr() {
    }
    
    public String getDict() {
        return this.dict;
    }
    
    public List<FieldValue> getBaseAttrList() {
        return this.baseAttrList;
    }
    
    public List<Prop> getProp() {
        return this.prop;
    }
    
    public void setDict(final String dict) {
        this.dict = dict;
    }
    
    public void setBaseAttrList(final List<FieldValue> baseAttrList) {
        this.baseAttrList = baseAttrList;
    }
    
    public void setProp(final List<Prop> prop) {
        this.prop = prop;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataAcsryAttr)) {
            return false;
        }
        SysDataAcsryAttr other = (SysDataAcsryAttr)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$dict = this.getDict();
        Object other$dict = other.getDict();
        if (!((this$dict != null) ? this$dict.equals(other$dict) : (other$dict == null))) {
            return false;
        }
        Object this$baseAttrList = this.getBaseAttrList();
        Object other$baseAttrList = other.getBaseAttrList();
        if (!((this$baseAttrList != null) ? this$baseAttrList.equals(other$baseAttrList) : (other$baseAttrList == null))) {
            return false;
        }
        Object this$prop = this.getProp();
        Object other$prop = other.getProp();
        return (this$prop != null) ? this$prop.equals(other$prop) : (other$prop == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataAcsryAttr;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $dict = this.getDict();
        result = result * 59 + (($dict == null) ? 43 : $dict.hashCode());
        Object $baseAttrList = this.getBaseAttrList();
        result = result * 59 + (($baseAttrList == null) ? 43 : $baseAttrList.hashCode());
        Object $prop = this.getProp();
        result = result * 59 + (($prop == null) ? 43 : $prop.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataAcsryAttr(dict=" + this.getDict() + ", baseAttrList=" + this.getBaseAttrList() + ", prop=" + this.getProp() + ")";
    }
}
