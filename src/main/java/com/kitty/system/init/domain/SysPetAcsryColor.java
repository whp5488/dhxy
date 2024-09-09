package com.kitty.system.init.domain;

import com.kitty.system.init.domain.vo.PetAccColor;
import java.util.Map;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("data_pet_acsry_color")
public class SysPetAcsryColor
{
    @Column("dict")
    private String dict;
    @Column("value")
    private Map<Byte, PetAccColor> value;
    
    public SysPetAcsryColor() {
    }
    
    public String getDict() {
        return this.dict;
    }
    
    public Map<Byte, PetAccColor> getValue() {
        return this.value;
    }
    
    public void setDict(final String dict) {
        this.dict = dict;
    }
    
    public void setValue(final Map<Byte, PetAccColor> value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysPetAcsryColor)) {
            return false;
        }
        SysPetAcsryColor other = (SysPetAcsryColor)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$dict = this.getDict();
        Object other$dict = other.getDict();
        if (!((this$dict != null) ? this$dict.equals(other$dict) : (other$dict == null))) {
            return false;
        }
        Object this$value = this.getValue();
        Object other$value = other.getValue();
        return (this$value != null) ? this$value.equals(other$value) : (other$value == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysPetAcsryColor;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $dict = this.getDict();
        result = result * 59 + (($dict == null) ? 43 : $dict.hashCode());
        Object $value = this.getValue();
        result = result * 59 + (($value == null) ? 43 : $value.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysPetAcsryColor(dict=" + this.getDict() + ", value=" + this.getValue() + ")";
    }
}
