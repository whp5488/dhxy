
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Default;
import com.kitty.system.init.domain.vo.Prop;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("data_pet_acsry_adv_attr_lib")
public class SysPetAcsryAdvAttrLib
{
    @Column("dict")
    private String dict;
    @Column("attr_serial")
    private int attrSerial;
    @Column("prop")
    @Default("[]")
    private List<Prop> propList;
    @Column("score")
    private int score;
    @Column("skill_id")
    @Default("[]")
    private List<Integer> skillList;
    @Column("value")
    @Default("[]")
    private List<Integer> valueList;
    @Column("value_show_type")
    private int type;
    
    public String getDict() {
        return this.dict;
    }
    
    public int getAttrSerial() {
        return this.attrSerial;
    }
    
    public List<Prop> getPropList() {
        return this.propList;
    }
    
    public int getScore() {
        return this.score;
    }
    
    public List<Integer> getSkillList() {
        return this.skillList;
    }
    
    public List<Integer> getValueList() {
        return this.valueList;
    }
    
    public int getType() {
        return this.type;
    }
    
    public void setDict(final String dict) {
        this.dict = dict;
    }
    
    public void setAttrSerial(final int attrSerial) {
        this.attrSerial = attrSerial;
    }
    
    public void setPropList(final List<Prop> propList) {
        this.propList = propList;
    }
    
    public void setScore(final int score) {
        this.score = score;
    }
    
    public void setSkillList(final List<Integer> skillList) {
        this.skillList = skillList;
    }
    
    public void setValueList(final List<Integer> valueList) {
        this.valueList = valueList;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysPetAcsryAdvAttrLib)) {
            return false;
        }
        final SysPetAcsryAdvAttrLib other = (SysPetAcsryAdvAttrLib)o;
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
        if (this.getAttrSerial() != other.getAttrSerial()) {
            return false;
        }
        final Object this$propList = this.getPropList();
        final Object other$propList = other.getPropList();
        Label_0115: {
            if (this$propList == null) {
                if (other$propList == null) {
                    break Label_0115;
                }
            }
            else if (this$propList.equals(other$propList)) {
                break Label_0115;
            }
            return false;
        }
        if (this.getScore() != other.getScore()) {
            return false;
        }
        final Object this$skillList = this.getSkillList();
        final Object other$skillList = other.getSkillList();
        Label_0165: {
            if (this$skillList == null) {
                if (other$skillList == null) {
                    break Label_0165;
                }
            }
            else if (this$skillList.equals(other$skillList)) {
                break Label_0165;
            }
            return false;
        }
        final Object this$valueList = this.getValueList();
        final Object other$valueList = other.getValueList();
        if (this$valueList == null) {
            if (other$valueList == null) {
                return this.getType() == other.getType();
            }
        }
        else if (this$valueList.equals(other$valueList)) {
            return this.getType() == other.getType();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysPetAcsryAdvAttrLib;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $dict = this.getDict();
        result = result * 59 + (($dict == null) ? 43 : $dict.hashCode());
        result = result * 59 + this.getAttrSerial();
        final Object $propList = this.getPropList();
        result = result * 59 + (($propList == null) ? 43 : $propList.hashCode());
        result = result * 59 + this.getScore();
        final Object $skillList = this.getSkillList();
        result = result * 59 + (($skillList == null) ? 43 : $skillList.hashCode());
        final Object $valueList = this.getValueList();
        result = result * 59 + (($valueList == null) ? 43 : $valueList.hashCode());
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysPetAcsryAdvAttrLib(dict=" + this.getDict() + ", attrSerial=" + this.getAttrSerial() + ", propList=" + this.getPropList() + ", score=" + this.getScore() + ", skillList=" + this.getSkillList() + ", valueList=" + this.getValueList() + ", type=" + this.getType() + ")";
    }
}
