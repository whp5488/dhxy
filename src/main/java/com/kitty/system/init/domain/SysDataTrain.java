package com.kitty.system.init.domain;

import java.util.ArrayList;
import org.nutz.dao.entity.annotation.Default;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_train")
public class SysDataTrain implements Serializable
{
    @Comment("编号")
    @Column("serial")
    private int serial;
    @Comment("阵法名称")
    @Column("name")
    private int name;
    @Comment("技能")
    @Column("skills")
    @Default("[]")
    private List<Integer> base_attr;
    @Comment("适合种族")
    @Column("recommend_to")
    @Default("[]")
    private List<Integer> recommendTo;
    @Comment("有效佩戴位置")
    @Column("active_cond")
    @Default("[]")
    private List<Integer> activeCond;
    @Comment("系数")
    @Column("power")
    @Default("[]")
    private List<Integer> power;
    @Comment("系数")
    @Column("match")
    @Default("[]")
    private List<Integer> match;
    @Comment("阵法类型：1.辅助 2.伤害 3.控制 4.辅助")
    @Column("type")
    private int type;
    
    public SysDataTrain() {
        this.base_attr = new ArrayList<>();
        this.recommendTo = new ArrayList<>();
        this.activeCond = new ArrayList<>();
        this.power = new ArrayList<>();
        this.match = new ArrayList<>();
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getName() {
        return this.name;
    }
    
    public List<Integer> getBase_attr() {
        return this.base_attr;
    }
    
    public List<Integer> getRecommendTo() {
        return this.recommendTo;
    }
    
    public List<Integer> getActiveCond() {
        return this.activeCond;
    }
    
    public List<Integer> getPower() {
        return this.power;
    }
    
    public List<Integer> getMatch() {
        return this.match;
    }
    
    public int getType() {
        return this.type;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setName(final int name) {
        this.name = name;
    }
    
    public void setBase_attr(final List<Integer> base_attr) {
        this.base_attr = base_attr;
    }
    
    public void setRecommendTo(final List<Integer> recommendTo) {
        this.recommendTo = recommendTo;
    }
    
    public void setActiveCond(final List<Integer> activeCond) {
        this.activeCond = activeCond;
    }
    
    public void setPower(final List<Integer> power) {
        this.power = power;
    }
    
    public void setMatch(final List<Integer> match) {
        this.match = match;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataTrain)) {
            return false;
        }
        SysDataTrain other = (SysDataTrain)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getName() != other.getName()) {
            return false;
        }
        Object this$base_attr = this.getBase_attr();
        Object other$base_attr = other.getBase_attr();
        if (!((this$base_attr != null) ? this$base_attr.equals(other$base_attr) : (other$base_attr == null))) {
            return false;
        }
        Object this$recommendTo = this.getRecommendTo();
        Object other$recommendTo = other.getRecommendTo();
        if (!((this$recommendTo != null) ? this$recommendTo.equals(other$recommendTo) : (other$recommendTo == null))) {
            return false;
        }
        Object this$activeCond = this.getActiveCond();
        Object other$activeCond = other.getActiveCond();
        if (!((this$activeCond != null) ? this$activeCond.equals(other$activeCond) : (other$activeCond == null))) {
            return false;
        }
        Object this$power = this.getPower();
        Object other$power = other.getPower();
        if (!((this$power != null) ? this$power.equals(other$power) : (other$power == null))) {
            return false;
        }
        Object this$match = this.getMatch();
        Object other$match = other.getMatch();
        return ((this$match != null) ? this$match.equals(other$match) : (other$match == null)) && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataTrain;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getName();
        Object $base_attr = this.getBase_attr();
        result = result * 59 + (($base_attr == null) ? 43 : $base_attr.hashCode());
        Object $recommendTo = this.getRecommendTo();
        result = result * 59 + (($recommendTo == null) ? 43 : $recommendTo.hashCode());
        Object $activeCond = this.getActiveCond();
        result = result * 59 + (($activeCond == null) ? 43 : $activeCond.hashCode());
        Object $power = this.getPower();
        result = result * 59 + (($power == null) ? 43 : $power.hashCode());
        Object $match = this.getMatch();
        result = result * 59 + (($match == null) ? 43 : $match.hashCode());
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataTrain(serial=" + this.getSerial() + ", name=" + this.getName() + ", base_attr=" + this.getBase_attr() + ", recommendTo=" + this.getRecommendTo() + ", activeCond=" + this.getActiveCond() + ", power=" + this.getPower() + ", match=" + this.getMatch() + ", type=" + this.getType() + ")";
    }
}
