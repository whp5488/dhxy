
package com.kitty.system.game.tianfu.domain;

import java.beans.ConstructorProperties;
import org.nutz.dao.entity.annotation.Default;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_tian_fu_skill")
public class SysTianFuSkill implements Serializable
{
    @Comment("主键")
    @Column("id")
    private int id;
    @Comment("编号")
    @Column("serial")
    private int serial;
    @Comment("所属目录(1=千钧,2=载物,3=御兽)")
    @Column("category")
    private byte category;
    @Comment("所属目录名称")
    @Column("category_desc")
    private String categoryDesc;
    @Comment("名称")
    @Column("name")
    private String name;
    @Comment("所在层")
    @Column("layer")
    private byte layer;
    @Comment("适用种族")
    @Column("race")
    @Default("[]")
    private List<Byte> race;
    @Comment("技能")
    @Column("skill")
    @Default("[]")
    private List<Integer> skill;
    @Comment("标签")
    @Column("tag")
    private String tag;
    @Comment("目标类型")
    @Column("target")
    private byte target;
    @Comment("动画效果")
    @Column("icon")
    private int icon;
    @Comment("")
    @Column("skill_seq_id")
    private int skillSeqId;
    @Comment("动画效果")
    @Column("effs")
    @Default("[]")
    private List<Integer> effs;
    @Comment("目标类型")
    @Column("target_type")
    @Default("[]")
    private List<Integer> targetType;
    @Comment("天演策编号")
    @Column("ce_serial")
    private int ceSerial;
    @Comment("权重")
    @Column("point_weight")
    @Default("[]")
    private List<Short> pointWeight;
    @Comment("品阶")
    @Column("rarity")
    private byte rarity;
    @Comment("品阶计算相关参数")
    @Column("rarity_judge")
    @Default("[]")
    private List<Byte> rarityJudge;
    @Comment("待鉴定天策符编号")
    @Column("to_identify_ce_serial")
    private int toIdentifyCeSerial;
    @Comment("保底权重")
    @Column("bao_di_weight")
    private short baoDiWeight;
    @Comment("触发时机")
    @Column("opportunity")
    private String opportunity;
    @Comment("是否是特效，true=特效技能，false=提升属性")
    @Column("is_effects")
    private boolean isEffects;
    
    public int getId() {
        return this.id;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public byte getCategory() {
        return this.category;
    }
    
    public String getCategoryDesc() {
        return this.categoryDesc;
    }
    
    public String getName() {
        return this.name;
    }
    
    public byte getLayer() {
        return this.layer;
    }
    
    public List<Byte> getRace() {
        return this.race;
    }
    
    public List<Integer> getSkill() {
        return this.skill;
    }
    
    public String getTag() {
        return this.tag;
    }
    
    public byte getTarget() {
        return this.target;
    }
    
    public int getIcon() {
        return this.icon;
    }
    
    public int getSkillSeqId() {
        return this.skillSeqId;
    }
    
    public List<Integer> getEffs() {
        return this.effs;
    }
    
    public List<Integer> getTargetType() {
        return this.targetType;
    }
    
    public int getCeSerial() {
        return this.ceSerial;
    }
    
    public List<Short> getPointWeight() {
        return this.pointWeight;
    }
    
    public byte getRarity() {
        return this.rarity;
    }
    
    public List<Byte> getRarityJudge() {
        return this.rarityJudge;
    }
    
    public int getToIdentifyCeSerial() {
        return this.toIdentifyCeSerial;
    }
    
    public short getBaoDiWeight() {
        return this.baoDiWeight;
    }
    
    public String getOpportunity() {
        return this.opportunity;
    }
    
    public boolean isEffects() {
        return this.isEffects;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setCategory(final byte category) {
        this.category = category;
    }
    
    public void setCategoryDesc(final String categoryDesc) {
        this.categoryDesc = categoryDesc;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setLayer(final byte layer) {
        this.layer = layer;
    }
    
    public void setRace(final List<Byte> race) {
        this.race = race;
    }
    
    public void setSkill(final List<Integer> skill) {
        this.skill = skill;
    }
    
    public void setTag(final String tag) {
        this.tag = tag;
    }
    
    public void setTarget(final byte target) {
        this.target = target;
    }
    
    public void setIcon(final int icon) {
        this.icon = icon;
    }
    
    public void setSkillSeqId(final int skillSeqId) {
        this.skillSeqId = skillSeqId;
    }
    
    public void setEffs(final List<Integer> effs) {
        this.effs = effs;
    }
    
    public void setTargetType(final List<Integer> targetType) {
        this.targetType = targetType;
    }
    
    public void setCeSerial(final int ceSerial) {
        this.ceSerial = ceSerial;
    }
    
    public void setPointWeight(final List<Short> pointWeight) {
        this.pointWeight = pointWeight;
    }
    
    public void setRarity(final byte rarity) {
        this.rarity = rarity;
    }
    
    public void setRarityJudge(final List<Byte> rarityJudge) {
        this.rarityJudge = rarityJudge;
    }
    
    public void setToIdentifyCeSerial(final int toIdentifyCeSerial) {
        this.toIdentifyCeSerial = toIdentifyCeSerial;
    }
    
    public void setBaoDiWeight(final short baoDiWeight) {
        this.baoDiWeight = baoDiWeight;
    }
    
    public void setOpportunity(final String opportunity) {
        this.opportunity = opportunity;
    }
    
    public void setEffects(final boolean isEffects) {
        this.isEffects = isEffects;
    }
    
    @ConstructorProperties({ "id", "serial", "category", "categoryDesc", "name", "layer", "race", "skill", "tag", "target", "icon", "skillSeqId", "effs", "targetType", "ceSerial", "pointWeight", "rarity", "rarityJudge", "toIdentifyCeSerial", "baoDiWeight", "opportunity", "isEffects" })
    public SysTianFuSkill(final int id, final int serial, final byte category, final String categoryDesc, final String name, final byte layer, final List<Byte> race, final List<Integer> skill, final String tag, final byte target, final int icon, final int skillSeqId, final List<Integer> effs, final List<Integer> targetType, final int ceSerial, final List<Short> pointWeight, final byte rarity, final List<Byte> rarityJudge, final int toIdentifyCeSerial, final short baoDiWeight, final String opportunity, final boolean isEffects) {
        this.id = id;
        this.serial = serial;
        this.category = category;
        this.categoryDesc = categoryDesc;
        this.name = name;
        this.layer = layer;
        this.race = race;
        this.skill = skill;
        this.tag = tag;
        this.target = target;
        this.icon = icon;
        this.skillSeqId = skillSeqId;
        this.effs = effs;
        this.targetType = targetType;
        this.ceSerial = ceSerial;
        this.pointWeight = pointWeight;
        this.rarity = rarity;
        this.rarityJudge = rarityJudge;
        this.toIdentifyCeSerial = toIdentifyCeSerial;
        this.baoDiWeight = baoDiWeight;
        this.opportunity = opportunity;
        this.isEffects = isEffects;
    }
    
    public SysTianFuSkill() {
    }
}
