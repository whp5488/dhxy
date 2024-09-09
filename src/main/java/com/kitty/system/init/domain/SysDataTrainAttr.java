package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Comment;
import com.kitty.system.init.domain.vo.Prop;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Default;
import com.kitty.system.common.enter.FieldValue;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_train_attr_lib")
public class SysDataTrainAttr implements Serializable
{
    @Column("dict")
    private int dict;
    @Column("base_attr")
    @Default("[]")
    @ColDefine(customType = "longtext")
    private List<FieldValue> baseAttrList;
    @Column("rnd_range")
    @Default("[]")
    private List<Prop> prop;
    @Comment("洗练类型")
    @Column("type")
    private int type;
    
    public SysDataTrainAttr() {
    }
    
    public int getDict() {
        return this.dict;
    }
    
    public List<FieldValue> getBaseAttrList() {
        return this.baseAttrList;
    }
    
    public List<Prop> getProp() {
        return this.prop;
    }
    
    public int getType() {
        return this.type;
    }
    
    public void setDict(final int dict) {
        this.dict = dict;
    }
    
    public void setBaseAttrList(final List<FieldValue> baseAttrList) {
        this.baseAttrList = baseAttrList;
    }
    
    public void setProp(final List<Prop> prop) {
        this.prop = prop;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataTrainAttr)) {
            return false;
        }
        SysDataTrainAttr other = (SysDataTrainAttr)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getDict() != other.getDict()) {
            return false;
        }
        Object this$baseAttrList = this.getBaseAttrList();
        Object other$baseAttrList = other.getBaseAttrList();
        if (!((this$baseAttrList != null) ? this$baseAttrList.equals(other$baseAttrList) : (other$baseAttrList == null))) {
            return false;
        }
        Object this$prop = this.getProp();
        Object other$prop = other.getProp();
        return ((this$prop != null) ? this$prop.equals(other$prop) : (other$prop == null)) && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataTrainAttr;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getDict();
        Object $baseAttrList = this.getBaseAttrList();
        result = result * 59 + (($baseAttrList == null) ? 43 : $baseAttrList.hashCode());
        Object $prop = this.getProp();
        result = result * 59 + (($prop == null) ? 43 : $prop.hashCode());
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataTrainAttr(dict=" + this.getDict() + ", baseAttrList=" + this.getBaseAttrList() + ", prop=" + this.getProp() + ", type=" + this.getType() + ")";
    }
}
