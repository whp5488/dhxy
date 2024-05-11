
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
        final SysPetAcsryColor other = (SysPetAcsryColor)o;
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
        return other instanceof SysPetAcsryColor;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $dict = this.getDict();
        result = result * 59 + (($dict == null) ? 43 : $dict.hashCode());
        final Object $value = this.getValue();
        result = result * 59 + (($value == null) ? 43 : $value.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysPetAcsryColor(dict=" + this.getDict() + ", value=" + this.getValue() + ")";
    }
}
