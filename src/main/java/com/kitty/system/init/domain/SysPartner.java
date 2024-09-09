package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Default;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_partner")
public class SysPartner implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("主键")
    @Column("id")
    private int id;
    @Comment("编号")
    @Column("serial")
    private int serial;
    @Comment("伙伴名称")
    @Column("name")
    private String name;
    @Comment("是否可以招募")
    @Column("is_recruits")
    private byte isRecruits;
    @Comment("气血最大资质")
    @Column("hp_max")
    private int hpMax;
    @Comment("需要的等级")
    @Column("need_drade")
    private int needDrade;
    @Comment("默认使用的技能")
    @Column("oper_default_skill_id")
    private int operDefaultSkillId;
    @Comment("角色定位代码")
    @Column("pos_define")
    private int posDefine;
    @Comment("角色定位")
    @Column("position")
    private String position;
    @Comment("类型")
    @Column("type")
    private int type;
    @Comment("角色定位描述")
    @Column("position_desc")
    private String positionDesc;
    @Comment("优先级")
    @Column("priority")
    private int priority;
    @Comment("种族")
    @Column("race")
    private int race;
    @Comment("性别")
    @Column("sex")
    private int sex;
    @Comment("外观")
    @Column("icon")
    private int icon;
    @Comment("速度最大资质")
    @Column("speed")
    private int speed;
    @Comment("解锁需要的银两")
    @Column("need_yl")
    private int needYl;
    @Comment("和定位差不多，类型名字")
    @Column("type_name")
    private String typeName;
    @Comment("天赋1id")
    @Column("unique_aptitude1")
    private int uniqueAptitude1;
    @Comment("天赋1名称")
    @Column("unique_aptitude1_name")
    private String uniqueAptitude1Name;
    @Comment("天赋2id")
    @Column("unique_aptitude2")
    private int uniqueAptitude2;
    @Comment("天赋2名称")
    @Column("unique_aptitude2_name")
    private String uniqueAptitude2Name;
    @Comment("成长类型")
    @Column("upgrade_type")
    private int upgradeType;
    @Comment("是否带有天赋")
    @Column("nb_partner")
    private int nbPartner;
    @Comment("天赋配置编号")
    @Column("tfcfgidx")
    private int tfcfgidx;
    @Comment("解锁等级")
    @Column("auto_unlock_grade")
    private int autoUnlockGrade;
    @Comment("不知道干什么的,优先级？")
    @Column("conflict_priority")
    private int conflictPriority;
    @Comment("技能编号集合")
    @Column("skills")
    @Default("[]")
    private List<Integer> skills;
    @Comment("技能使用描述集合")
    @Column("skills_detail_json")
    private String skillsDetailJson;
    
    public SysPartner() {
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public byte getIsRecruits() {
        return this.isRecruits;
    }
    
    public int getHpMax() {
        return this.hpMax;
    }
    
    public int getNeedDrade() {
        return this.needDrade;
    }
    
    public int getOperDefaultSkillId() {
        return this.operDefaultSkillId;
    }
    
    public int getPosDefine() {
        return this.posDefine;
    }
    
    public String getPosition() {
        return this.position;
    }
    
    public int getType() {
        return this.type;
    }
    
    public String getPositionDesc() {
        return this.positionDesc;
    }
    
    public int getPriority() {
        return this.priority;
    }
    
    public int getRace() {
        return this.race;
    }
    
    public int getSex() {
        return this.sex;
    }
    
    public int getIcon() {
        return this.icon;
    }
    
    public int getSpeed() {
        return this.speed;
    }
    
    public int getNeedYl() {
        return this.needYl;
    }
    
    public String getTypeName() {
        return this.typeName;
    }
    
    public int getUniqueAptitude1() {
        return this.uniqueAptitude1;
    }
    
    public String getUniqueAptitude1Name() {
        return this.uniqueAptitude1Name;
    }
    
    public int getUniqueAptitude2() {
        return this.uniqueAptitude2;
    }
    
    public String getUniqueAptitude2Name() {
        return this.uniqueAptitude2Name;
    }
    
    public int getUpgradeType() {
        return this.upgradeType;
    }
    
    public int getNbPartner() {
        return this.nbPartner;
    }
    
    public int getTfcfgidx() {
        return this.tfcfgidx;
    }
    
    public int getAutoUnlockGrade() {
        return this.autoUnlockGrade;
    }
    
    public int getConflictPriority() {
        return this.conflictPriority;
    }
    
    public List<Integer> getSkills() {
        return this.skills;
    }
    
    public String getSkillsDetailJson() {
        return this.skillsDetailJson;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setIsRecruits(final byte isRecruits) {
        this.isRecruits = isRecruits;
    }
    
    public void setHpMax(final int hpMax) {
        this.hpMax = hpMax;
    }
    
    public void setNeedDrade(final int needDrade) {
        this.needDrade = needDrade;
    }
    
    public void setOperDefaultSkillId(final int operDefaultSkillId) {
        this.operDefaultSkillId = operDefaultSkillId;
    }
    
    public void setPosDefine(final int posDefine) {
        this.posDefine = posDefine;
    }
    
    public void setPosition(final String position) {
        this.position = position;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setPositionDesc(final String positionDesc) {
        this.positionDesc = positionDesc;
    }
    
    public void setPriority(final int priority) {
        this.priority = priority;
    }
    
    public void setRace(final int race) {
        this.race = race;
    }
    
    public void setSex(final int sex) {
        this.sex = sex;
    }
    
    public void setIcon(final int icon) {
        this.icon = icon;
    }
    
    public void setSpeed(final int speed) {
        this.speed = speed;
    }
    
    public void setNeedYl(final int needYl) {
        this.needYl = needYl;
    }
    
    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }
    
    public void setUniqueAptitude1(final int uniqueAptitude1) {
        this.uniqueAptitude1 = uniqueAptitude1;
    }
    
    public void setUniqueAptitude1Name(final String uniqueAptitude1Name) {
        this.uniqueAptitude1Name = uniqueAptitude1Name;
    }
    
    public void setUniqueAptitude2(final int uniqueAptitude2) {
        this.uniqueAptitude2 = uniqueAptitude2;
    }
    
    public void setUniqueAptitude2Name(final String uniqueAptitude2Name) {
        this.uniqueAptitude2Name = uniqueAptitude2Name;
    }
    
    public void setUpgradeType(final int upgradeType) {
        this.upgradeType = upgradeType;
    }
    
    public void setNbPartner(final int nbPartner) {
        this.nbPartner = nbPartner;
    }
    
    public void setTfcfgidx(final int tfcfgidx) {
        this.tfcfgidx = tfcfgidx;
    }
    
    public void setAutoUnlockGrade(final int autoUnlockGrade) {
        this.autoUnlockGrade = autoUnlockGrade;
    }
    
    public void setConflictPriority(final int conflictPriority) {
        this.conflictPriority = conflictPriority;
    }
    
    public void setSkills(final List<Integer> skills) {
        this.skills = skills;
    }
    
    public void setSkillsDetailJson(final String skillsDetailJson) {
        this.skillsDetailJson = skillsDetailJson;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysPartner)) {
            return false;
        }
        SysPartner other = (SysPartner)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        if (this.getIsRecruits() != other.getIsRecruits()) {
            return false;
        }
        if (this.getHpMax() != other.getHpMax()) {
            return false;
        }
        if (this.getNeedDrade() != other.getNeedDrade()) {
            return false;
        }
        if (this.getOperDefaultSkillId() != other.getOperDefaultSkillId()) {
            return false;
        }
        if (this.getPosDefine() != other.getPosDefine()) {
            return false;
        }
        Object this$position = this.getPosition();
        Object other$position = other.getPosition();
        if (!((this$position != null) ? this$position.equals(other$position) : (other$position == null))) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        Object this$positionDesc = this.getPositionDesc();
        Object other$positionDesc = other.getPositionDesc();
        if (!((this$positionDesc != null) ? this$positionDesc.equals(other$positionDesc) : (other$positionDesc == null))) {
            return false;
        }
        if (this.getPriority() != other.getPriority()) {
            return false;
        }
        if (this.getRace() != other.getRace()) {
            return false;
        }
        if (this.getSex() != other.getSex()) {
            return false;
        }
        if (this.getIcon() != other.getIcon()) {
            return false;
        }
        if (this.getSpeed() != other.getSpeed()) {
            return false;
        }
        if (this.getNeedYl() != other.getNeedYl()) {
            return false;
        }
        Object this$typeName = this.getTypeName();
        Object other$typeName = other.getTypeName();
        if (!((this$typeName != null) ? this$typeName.equals(other$typeName) : (other$typeName == null))) {
            return false;
        }
        if (this.getUniqueAptitude1() != other.getUniqueAptitude1()) {
            return false;
        }
        Object this$uniqueAptitude1Name = this.getUniqueAptitude1Name();
        Object other$uniqueAptitude1Name = other.getUniqueAptitude1Name();
        if (!((this$uniqueAptitude1Name != null) ? this$uniqueAptitude1Name.equals(other$uniqueAptitude1Name) : (other$uniqueAptitude1Name == null))) {
            return false;
        }
        if (this.getUniqueAptitude2() != other.getUniqueAptitude2()) {
            return false;
        }
        Object this$uniqueAptitude2Name = this.getUniqueAptitude2Name();
        Object other$uniqueAptitude2Name = other.getUniqueAptitude2Name();
        if (!((this$uniqueAptitude2Name != null) ? this$uniqueAptitude2Name.equals(other$uniqueAptitude2Name) : (other$uniqueAptitude2Name == null))) {
            return false;
        }
        if (this.getUpgradeType() != other.getUpgradeType()) {
            return false;
        }
        if (this.getNbPartner() != other.getNbPartner()) {
            return false;
        }
        if (this.getTfcfgidx() != other.getTfcfgidx()) {
            return false;
        }
        if (this.getAutoUnlockGrade() != other.getAutoUnlockGrade()) {
            return false;
        }
        if (this.getConflictPriority() != other.getConflictPriority()) {
            return false;
        }
        Object this$skills = this.getSkills();
        Object other$skills = other.getSkills();
        if (!((this$skills != null) ? this$skills.equals(other$skills) : (other$skills == null))) {
            return false;
        }
        Object this$skillsDetailJson = this.getSkillsDetailJson();
        Object other$skillsDetailJson = other.getSkillsDetailJson();
        return (this$skillsDetailJson != null) ? this$skillsDetailJson.equals(other$skillsDetailJson) : (other$skillsDetailJson == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysPartner;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getSerial();
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getIsRecruits();
        result = result * 59 + this.getHpMax();
        result = result * 59 + this.getNeedDrade();
        result = result * 59 + this.getOperDefaultSkillId();
        result = result * 59 + this.getPosDefine();
        Object $position = this.getPosition();
        result = result * 59 + (($position == null) ? 43 : $position.hashCode());
        result = result * 59 + this.getType();
        Object $positionDesc = this.getPositionDesc();
        result = result * 59 + (($positionDesc == null) ? 43 : $positionDesc.hashCode());
        result = result * 59 + this.getPriority();
        result = result * 59 + this.getRace();
        result = result * 59 + this.getSex();
        result = result * 59 + this.getIcon();
        result = result * 59 + this.getSpeed();
        result = result * 59 + this.getNeedYl();
        Object $typeName = this.getTypeName();
        result = result * 59 + (($typeName == null) ? 43 : $typeName.hashCode());
        result = result * 59 + this.getUniqueAptitude1();
        Object $uniqueAptitude1Name = this.getUniqueAptitude1Name();
        result = result * 59 + (($uniqueAptitude1Name == null) ? 43 : $uniqueAptitude1Name.hashCode());
        result = result * 59 + this.getUniqueAptitude2();
        Object $uniqueAptitude2Name = this.getUniqueAptitude2Name();
        result = result * 59 + (($uniqueAptitude2Name == null) ? 43 : $uniqueAptitude2Name.hashCode());
        result = result * 59 + this.getUpgradeType();
        result = result * 59 + this.getNbPartner();
        result = result * 59 + this.getTfcfgidx();
        result = result * 59 + this.getAutoUnlockGrade();
        result = result * 59 + this.getConflictPriority();
        Object $skills = this.getSkills();
        result = result * 59 + (($skills == null) ? 43 : $skills.hashCode());
        Object $skillsDetailJson = this.getSkillsDetailJson();
        result = result * 59 + (($skillsDetailJson == null) ? 43 : $skillsDetailJson.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysPartner(id=" + this.getId() + ", serial=" + this.getSerial() + ", name=" + this.getName() + ", isRecruits=" + (int)this.getIsRecruits() + ", hpMax=" + this.getHpMax() + ", needDrade=" + this.getNeedDrade() + ", operDefaultSkillId=" + this.getOperDefaultSkillId() + ", posDefine=" + this.getPosDefine() + ", position=" + this.getPosition() + ", type=" + this.getType() + ", positionDesc=" + this.getPositionDesc() + ", priority=" + this.getPriority() + ", race=" + this.getRace() + ", sex=" + this.getSex() + ", icon=" + this.getIcon() + ", speed=" + this.getSpeed() + ", needYl=" + this.getNeedYl() + ", typeName=" + this.getTypeName() + ", uniqueAptitude1=" + this.getUniqueAptitude1() + ", uniqueAptitude1Name=" + this.getUniqueAptitude1Name() + ", uniqueAptitude2=" + this.getUniqueAptitude2() + ", uniqueAptitude2Name=" + this.getUniqueAptitude2Name() + ", upgradeType=" + this.getUpgradeType() + ", nbPartner=" + this.getNbPartner() + ", tfcfgidx=" + this.getTfcfgidx() + ", autoUnlockGrade=" + this.getAutoUnlockGrade() + ", conflictPriority=" + this.getConflictPriority() + ", skills=" + this.getSkills() + ", skillsDetailJson=" + this.getSkillsDetailJson() + ")";
    }
}
