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
    
    public SysPetAcsryAdvAttrLib() {
    }
    
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
        SysPetAcsryAdvAttrLib other = (SysPetAcsryAdvAttrLib)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$dict = this.getDict();
        Object other$dict = other.getDict();
        if (!((this$dict != null) ? this$dict.equals(other$dict) : (other$dict == null))) {
            return false;
        }
        if (this.getAttrSerial() != other.getAttrSerial()) {
            return false;
        }
        Object this$propList = this.getPropList();
        Object other$propList = other.getPropList();
        if (!((this$propList != null) ? this$propList.equals(other$propList) : (other$propList == null))) {
            return false;
        }
        if (this.getScore() != other.getScore()) {
            return false;
        }
        Object this$skillList = this.getSkillList();
        Object other$skillList = other.getSkillList();
        if (!((this$skillList != null) ? this$skillList.equals(other$skillList) : (other$skillList == null))) {
            return false;
        }
        Object this$valueList = this.getValueList();
        Object other$valueList = other.getValueList();
        return ((this$valueList != null) ? this$valueList.equals(other$valueList) : (other$valueList == null)) && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysPetAcsryAdvAttrLib;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $dict = this.getDict();
        result = result * 59 + (($dict == null) ? 43 : $dict.hashCode());
        result = result * 59 + this.getAttrSerial();
        Object $propList = this.getPropList();
        result = result * 59 + (($propList == null) ? 43 : $propList.hashCode());
        result = result * 59 + this.getScore();
        Object $skillList = this.getSkillList();
        result = result * 59 + (($skillList == null) ? 43 : $skillList.hashCode());
        Object $valueList = this.getValueList();
        result = result * 59 + (($valueList == null) ? 43 : $valueList.hashCode());
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysPetAcsryAdvAttrLib(dict=" + this.getDict() + ", attrSerial=" + this.getAttrSerial() + ", propList=" + this.getPropList() + ", score=" + this.getScore() + ", skillList=" + this.getSkillList() + ", valueList=" + this.getValueList() + ", type=" + this.getType() + ")";
    }
}
