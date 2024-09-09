package com.kitty.system.init.domain;

import com.kitty.system.init.domain.vo.Prop;
import java.util.Map;
import org.nutz.dao.entity.annotation.Default;
import com.kitty.system.common.enter.FieldValue;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("data_pet_acsry_attr_lib")
public class SysPetAcsryAttrLib
{
    @Column("dict")
    private String dict;
    @Column("base_attr")
    @Default("[]")
    private List<FieldValue> baseAttr;
    @Column("prop_by_rarity")
    @Default("[]")
    private Map<Byte, List<Prop>> propByRarity;
    
    public SysPetAcsryAttrLib() {
    }
    
    public String getDict() {
        return this.dict;
    }
    
    public List<FieldValue> getBaseAttr() {
        return this.baseAttr;
    }
    
    public Map<Byte, List<Prop>> getPropByRarity() {
        return this.propByRarity;
    }
    
    public void setDict(final String dict) {
        this.dict = dict;
    }
    
    public void setBaseAttr(final List<FieldValue> baseAttr) {
        this.baseAttr = baseAttr;
    }
    
    public void setPropByRarity(final Map<Byte, List<Prop>> propByRarity) {
        this.propByRarity = propByRarity;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysPetAcsryAttrLib)) {
            return false;
        }
        SysPetAcsryAttrLib other = (SysPetAcsryAttrLib)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$dict = this.getDict();
        Object other$dict = other.getDict();
        if (!((this$dict != null) ? this$dict.equals(other$dict) : (other$dict == null))) {
            return false;
        }
        Object this$baseAttr = this.getBaseAttr();
        Object other$baseAttr = other.getBaseAttr();
        if (!((this$baseAttr != null) ? this$baseAttr.equals(other$baseAttr) : (other$baseAttr == null))) {
            return false;
        }
        Object this$propByRarity = this.getPropByRarity();
        Object other$propByRarity = other.getPropByRarity();
        return (this$propByRarity != null) ? this$propByRarity.equals(other$propByRarity) : (other$propByRarity == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysPetAcsryAttrLib;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $dict = this.getDict();
        result = result * 59 + (($dict == null) ? 43 : $dict.hashCode());
        Object $baseAttr = this.getBaseAttr();
        result = result * 59 + (($baseAttr == null) ? 43 : $baseAttr.hashCode());
        Object $propByRarity = this.getPropByRarity();
        result = result * 59 + (($propByRarity == null) ? 43 : $propByRarity.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysPetAcsryAttrLib(dict=" + this.getDict() + ", baseAttr=" + this.getBaseAttr() + ", propByRarity=" + this.getPropByRarity() + ")";
    }
}
