
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
        final SysPetAcsryAttrLib other = (SysPetAcsryAttrLib)o;
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
        final Object this$baseAttr = this.getBaseAttr();
        final Object other$baseAttr = other.getBaseAttr();
        Label_0102: {
            if (this$baseAttr == null) {
                if (other$baseAttr == null) {
                    break Label_0102;
                }
            }
            else if (this$baseAttr.equals(other$baseAttr)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$propByRarity = this.getPropByRarity();
        final Object other$propByRarity = other.getPropByRarity();
        if (this$propByRarity == null) {
            if (other$propByRarity == null) {
                return true;
            }
        }
        else if (this$propByRarity.equals(other$propByRarity)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysPetAcsryAttrLib;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $dict = this.getDict();
        result = result * 59 + (($dict == null) ? 43 : $dict.hashCode());
        final Object $baseAttr = this.getBaseAttr();
        result = result * 59 + (($baseAttr == null) ? 43 : $baseAttr.hashCode());
        final Object $propByRarity = this.getPropByRarity();
        result = result * 59 + (($propByRarity == null) ? 43 : $propByRarity.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysPetAcsryAttrLib(dict=" + this.getDict() + ", baseAttr=" + this.getBaseAttr() + ", propByRarity=" + this.getPropByRarity() + ")";
    }
}
