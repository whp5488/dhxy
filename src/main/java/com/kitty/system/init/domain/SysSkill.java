package com.kitty.system.init.domain;

import java.util.Arrays;
import org.nutz.dao.entity.annotation.Default;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("s_skill")
public class SysSkill implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("主键")
    @Column("id")
    private Integer id;
    @Comment("技能编号")
    @Column("serial")
    private Integer serial;
    @Comment("名字")
    @Column("name")
    private String name;
    @Comment("图标")
    @Column("icon")
    private int icon;
    @Comment("技能派系")
    @Column("attr")
    private Integer attr;
    @Comment("技能等阶：4.单体 5.群体")
    @Column("step")
    private Integer step;
    @Comment("种族")
    @Column("race")
    private Integer race;
    @Comment("性别")
    @Column("sex")
    private Integer sex;
    @Comment("是否主动技能（1主动，0被动）")
    @Column("initiative")
    private Integer initiative;
    @Comment("目标类型")
    @Column("target_type")
    @Default("[]")
    private List<Integer> targetType;
    @Comment("技能类型：0英雄")
    @Column("type")
    private Integer type;
    @Comment("命中率")
    @Column("hit_ratio")
    private double hitRatio;
    @Comment("系数")
    @Column("power")
    @Default("[]")
    private double[] power;
    @Comment("覆盖技能编号（本技能被此字段技能覆盖），可以是多个，用逗号隔开")
    @Column("cover_skill_serials")
    private String coverSkillSerials;
    @Comment("位置")
    @Column("pos")
    private Integer pos;
    
    public SysSkill() {
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getIcon() {
        return this.icon;
    }
    
    public Integer getAttr() {
        return this.attr;
    }
    
    public Integer getStep() {
        return this.step;
    }
    
    public Integer getRace() {
        return this.race;
    }
    
    public Integer getSex() {
        return this.sex;
    }
    
    public Integer getInitiative() {
        return this.initiative;
    }
    
    public List<Integer> getTargetType() {
        return this.targetType;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public double getHitRatio() {
        return this.hitRatio;
    }
    
    public double[] getPower() {
        return this.power;
    }
    
    public String getCoverSkillSerials() {
        return this.coverSkillSerials;
    }
    
    public Integer getPos() {
        return this.pos;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setIcon(final int icon) {
        this.icon = icon;
    }
    
    public void setAttr(final Integer attr) {
        this.attr = attr;
    }
    
    public void setStep(final Integer step) {
        this.step = step;
    }
    
    public void setRace(final Integer race) {
        this.race = race;
    }
    
    public void setSex(final Integer sex) {
        this.sex = sex;
    }
    
    public void setInitiative(final Integer initiative) {
        this.initiative = initiative;
    }
    
    public void setTargetType(final List<Integer> targetType) {
        this.targetType = targetType;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    public void setHitRatio(final double hitRatio) {
        this.hitRatio = hitRatio;
    }
    
    public void setPower(final double[] power) {
        this.power = power;
    }
    
    public void setCoverSkillSerials(final String coverSkillSerials) {
        this.coverSkillSerials = coverSkillSerials;
    }
    
    public void setPos(final Integer pos) {
        this.pos = pos;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysSkill)) {
            return false;
        }
        SysSkill other = (SysSkill)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
            return false;
        }
        Object this$serial = this.getSerial();
        Object other$serial = other.getSerial();
        if (!((this$serial != null) ? this$serial.equals(other$serial) : (other$serial == null))) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        if (this.getIcon() != other.getIcon()) {
            return false;
        }
        Object this$attr = this.getAttr();
        Object other$attr = other.getAttr();
        if (!((this$attr != null) ? this$attr.equals(other$attr) : (other$attr == null))) {
            return false;
        }
        Object this$step = this.getStep();
        Object other$step = other.getStep();
        if (!((this$step != null) ? this$step.equals(other$step) : (other$step == null))) {
            return false;
        }
        Object this$race = this.getRace();
        Object other$race = other.getRace();
        if (!((this$race != null) ? this$race.equals(other$race) : (other$race == null))) {
            return false;
        }
        Object this$sex = this.getSex();
        Object other$sex = other.getSex();
        if (!((this$sex != null) ? this$sex.equals(other$sex) : (other$sex == null))) {
            return false;
        }
        Object this$initiative = this.getInitiative();
        Object other$initiative = other.getInitiative();
        if (!((this$initiative != null) ? this$initiative.equals(other$initiative) : (other$initiative == null))) {
            return false;
        }
        Object this$targetType = this.getTargetType();
        Object other$targetType = other.getTargetType();
        if (!((this$targetType != null) ? this$targetType.equals(other$targetType) : (other$targetType == null))) {
            return false;
        }
        Object this$type = this.getType();
        Object other$type = other.getType();
        if (!((this$type != null) ? this$type.equals(other$type) : (other$type == null))) {
            return false;
        }
        if (Double.compare(this.getHitRatio(), other.getHitRatio()) != 0) {
            return false;
        }
        if (!Arrays.equals(this.getPower(), other.getPower())) {
            return false;
        }
        Object this$coverSkillSerials = this.getCoverSkillSerials();
        Object other$coverSkillSerials = other.getCoverSkillSerials();
        if (!((this$coverSkillSerials != null) ? this$coverSkillSerials.equals(other$coverSkillSerials) : (other$coverSkillSerials == null))) {
            return false;
        }
        Object this$pos = this.getPos();
        Object other$pos = other.getPos();
        return (this$pos != null) ? this$pos.equals(other$pos) : (other$pos == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysSkill;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getIcon();
        Object $attr = this.getAttr();
        result = result * 59 + (($attr == null) ? 43 : $attr.hashCode());
        Object $step = this.getStep();
        result = result * 59 + (($step == null) ? 43 : $step.hashCode());
        Object $race = this.getRace();
        result = result * 59 + (($race == null) ? 43 : $race.hashCode());
        Object $sex = this.getSex();
        result = result * 59 + (($sex == null) ? 43 : $sex.hashCode());
        Object $initiative = this.getInitiative();
        result = result * 59 + (($initiative == null) ? 43 : $initiative.hashCode());
        Object $targetType = this.getTargetType();
        result = result * 59 + (($targetType == null) ? 43 : $targetType.hashCode());
        Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        long $hitRatio = Double.doubleToLongBits(this.getHitRatio());
        result = result * 59 + (int)($hitRatio >>> 32 ^ $hitRatio);
        result = result * 59 + Arrays.hashCode(this.getPower());
        Object $coverSkillSerials = this.getCoverSkillSerials();
        result = result * 59 + (($coverSkillSerials == null) ? 43 : $coverSkillSerials.hashCode());
        Object $pos = this.getPos();
        result = result * 59 + (($pos == null) ? 43 : $pos.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysSkill(id=" + this.getId() + ", serial=" + this.getSerial() + ", name=" + this.getName() + ", icon=" + this.getIcon() + ", attr=" + this.getAttr() + ", step=" + this.getStep() + ", race=" + this.getRace() + ", sex=" + this.getSex() + ", initiative=" + this.getInitiative() + ", targetType=" + this.getTargetType() + ", type=" + this.getType() + ", hitRatio=" + this.getHitRatio() + ", power=" + Arrays.toString(this.getPower()) + ", coverSkillSerials=" + this.getCoverSkillSerials() + ", pos=" + this.getPos() + ")";
    }
}
