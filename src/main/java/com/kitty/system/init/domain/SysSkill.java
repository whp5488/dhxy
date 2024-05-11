
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
        final SysSkill other = (SysSkill)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        Label_0065: {
            if (this$id == null) {
                if (other$id == null) {
                    break Label_0065;
                }
            }
            else if (this$id.equals(other$id)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$serial = this.getSerial();
        final Object other$serial = other.getSerial();
        Label_0102: {
            if (this$serial == null) {
                if (other$serial == null) {
                    break Label_0102;
                }
            }
            else if (this$serial.equals(other$serial)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0139: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0139;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0139;
            }
            return false;
        }
        if (this.getIcon() != other.getIcon()) {
            return false;
        }
        final Object this$attr = this.getAttr();
        final Object other$attr = other.getAttr();
        Label_0189: {
            if (this$attr == null) {
                if (other$attr == null) {
                    break Label_0189;
                }
            }
            else if (this$attr.equals(other$attr)) {
                break Label_0189;
            }
            return false;
        }
        final Object this$step = this.getStep();
        final Object other$step = other.getStep();
        Label_0226: {
            if (this$step == null) {
                if (other$step == null) {
                    break Label_0226;
                }
            }
            else if (this$step.equals(other$step)) {
                break Label_0226;
            }
            return false;
        }
        final Object this$race = this.getRace();
        final Object other$race = other.getRace();
        Label_0263: {
            if (this$race == null) {
                if (other$race == null) {
                    break Label_0263;
                }
            }
            else if (this$race.equals(other$race)) {
                break Label_0263;
            }
            return false;
        }
        final Object this$sex = this.getSex();
        final Object other$sex = other.getSex();
        Label_0300: {
            if (this$sex == null) {
                if (other$sex == null) {
                    break Label_0300;
                }
            }
            else if (this$sex.equals(other$sex)) {
                break Label_0300;
            }
            return false;
        }
        final Object this$initiative = this.getInitiative();
        final Object other$initiative = other.getInitiative();
        Label_0337: {
            if (this$initiative == null) {
                if (other$initiative == null) {
                    break Label_0337;
                }
            }
            else if (this$initiative.equals(other$initiative)) {
                break Label_0337;
            }
            return false;
        }
        final Object this$targetType = this.getTargetType();
        final Object other$targetType = other.getTargetType();
        Label_0374: {
            if (this$targetType == null) {
                if (other$targetType == null) {
                    break Label_0374;
                }
            }
            else if (this$targetType.equals(other$targetType)) {
                break Label_0374;
            }
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0411: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0411;
                }
            }
            else if (this$type.equals(other$type)) {
                break Label_0411;
            }
            return false;
        }
        if (Double.compare(this.getHitRatio(), other.getHitRatio()) != 0) {
            return false;
        }
        if (!Arrays.equals(this.getPower(), other.getPower())) {
            return false;
        }
        final Object this$coverSkillSerials = this.getCoverSkillSerials();
        final Object other$coverSkillSerials = other.getCoverSkillSerials();
        Label_0480: {
            if (this$coverSkillSerials == null) {
                if (other$coverSkillSerials == null) {
                    break Label_0480;
                }
            }
            else if (this$coverSkillSerials.equals(other$coverSkillSerials)) {
                break Label_0480;
            }
            return false;
        }
        final Object this$pos = this.getPos();
        final Object other$pos = other.getPos();
        if (this$pos == null) {
            if (other$pos == null) {
                return true;
            }
        }
        else if (this$pos.equals(other$pos)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysSkill;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        final Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getIcon();
        final Object $attr = this.getAttr();
        result = result * 59 + (($attr == null) ? 43 : $attr.hashCode());
        final Object $step = this.getStep();
        result = result * 59 + (($step == null) ? 43 : $step.hashCode());
        final Object $race = this.getRace();
        result = result * 59 + (($race == null) ? 43 : $race.hashCode());
        final Object $sex = this.getSex();
        result = result * 59 + (($sex == null) ? 43 : $sex.hashCode());
        final Object $initiative = this.getInitiative();
        result = result * 59 + (($initiative == null) ? 43 : $initiative.hashCode());
        final Object $targetType = this.getTargetType();
        result = result * 59 + (($targetType == null) ? 43 : $targetType.hashCode());
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final long $hitRatio = Double.doubleToLongBits(this.getHitRatio());
        result = result * 59 + (int)($hitRatio >>> 32 ^ $hitRatio);
        result = result * 59 + Arrays.hashCode(this.getPower());
        final Object $coverSkillSerials = this.getCoverSkillSerials();
        result = result * 59 + (($coverSkillSerials == null) ? 43 : $coverSkillSerials.hashCode());
        final Object $pos = this.getPos();
        result = result * 59 + (($pos == null) ? 43 : $pos.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysSkill(id=" + this.getId() + ", serial=" + this.getSerial() + ", name=" + this.getName() + ", icon=" + this.getIcon() + ", attr=" + this.getAttr() + ", step=" + this.getStep() + ", race=" + this.getRace() + ", sex=" + this.getSex() + ", initiative=" + this.getInitiative() + ", targetType=" + this.getTargetType() + ", type=" + this.getType() + ", hitRatio=" + this.getHitRatio() + ", power=" + Arrays.toString(this.getPower()) + ", coverSkillSerials=" + this.getCoverSkillSerials() + ", pos=" + this.getPos() + ")";
    }
}
