
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
        final SysDataAcsryAttr other = (SysDataAcsryAttr)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$dict = this.getDict();
        final Object other$dict = other.getDict();
        Label_0065: {
            if (this$dict == null) {
                if (other$dict == null) {
                    break Label_0065;
                }
            }
            else if (this$dict.equals(other$dict)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$baseAttrList = this.getBaseAttrList();
        final Object other$baseAttrList = other.getBaseAttrList();
        Label_0102: {
            if (this$baseAttrList == null) {
                if (other$baseAttrList == null) {
                    break Label_0102;
                }
            }
            else if (this$baseAttrList.equals(other$baseAttrList)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$prop = this.getProp();
        final Object other$prop = other.getProp();
        if (this$prop == null) {
            if (other$prop == null) {
                return true;
            }
        }
        else if (this$prop.equals(other$prop)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataAcsryAttr;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $dict = this.getDict();
        result = result * 59 + (($dict == null) ? 43 : $dict.hashCode());
        final Object $baseAttrList = this.getBaseAttrList();
        result = result * 59 + (($baseAttrList == null) ? 43 : $baseAttrList.hashCode());
        final Object $prop = this.getProp();
        result = result * 59 + (($prop == null) ? 43 : $prop.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataAcsryAttr(dict=" + this.getDict() + ", baseAttrList=" + this.getBaseAttrList() + ", prop=" + this.getProp() + ")";
    }
}
