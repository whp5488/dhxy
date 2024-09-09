package com.kitty.system.init.domain;

import java.util.Arrays;
import java.util.HashMap;
import com.kitty.common.utils.JsonUtils;
import java.util.Map;
import org.nutz.dao.entity.annotation.Default;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("s_pet_skill")
public class SysPetSkill implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("主键")
    @Column("id")
    private Integer id;
    @Comment("编号")
    @Column("serial")
    private Integer serial;
    @Comment("名称")
    @Column("name")
    private String name;
    @Comment("外观")
    @Column("icon")
    private Integer icon;
    @Comment("技能派系")
    @Column("attr")
    private Integer attr;
    @Comment("比率")
    @Column("rarity")
    private Integer rarity;
    @Comment("学习概率")
    @Column("hxy_learn_rate_post_war")
    private Integer hxyLearnRatePostWar;
    @Comment("学习概率")
    @Column("learn_rate_post_war")
    private Integer learnRatePostWar;
    @Comment("万寿园领悟概率")
    @Column("wan_shou_learn_rate_post_war")
    private Integer wanShouLearnRatePostWar;
    @Comment("是否主动技能（1主动，0被动，2主、被动）")
    @Column("initiative")
    private Integer initiative;
    @Comment("目标类型")
    @Column("target_type")
    @Default("[]")
    private List<Integer> targetType;
    @Comment("触发类型")
    @Column("trigger_type")
    private String triggerType;
    @Comment("被动触发技能serial")
    @Column("passive_trigger_skill")
    private String passiveTriggerSkill;
    @Comment("是否仅pvp使用,0否，1是")
    @Column("is_pvp")
    private int isPvp;
    @Comment("覆盖技能编号（本技能被此字段技能覆盖），可以是多个，用逗号隔开")
    @Column("cover_skill_serials")
    private String coverSkillSerials;
    @Comment("冲突技能编号，可以多个，用逗号隔开")
    @Column("clash_skill_serials")
    private String clashSkillSerials;
    @Comment("可同时触发技能编号,可以多个，用逗号隔开")
    @Column("together_trigger_skill_serials")
    private String togetherTriggerSkillSerials;
    @Comment("限制类型")
    @Column("astrict_type_json")
    private String astrictTypeJson;
    @Comment("系数")
    @Column("power")
    @Default("[]")
    private double[] power;
    @Comment("执行顺序")
    @Column("execution_sequence")
    private int executionSequence;
    private Map<String, Double> astrictTypeMap;
    
    public Map<String, Double> getAstrictTypeMap() {
        if (this.astrictTypeJson != null && !"".equals(this.astrictTypeJson)) {
            this.astrictTypeMap = JsonUtils.string2Map(this.getAstrictTypeJson(), String.class, Double.class);
        }
        return this.astrictTypeMap;
    }
    
    public SysPetSkill() {
        this.astrictTypeMap = new HashMap();
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
    
    public Integer getIcon() {
        return this.icon;
    }
    
    public Integer getAttr() {
        return this.attr;
    }
    
    public Integer getRarity() {
        return this.rarity;
    }
    
    public Integer getHxyLearnRatePostWar() {
        return this.hxyLearnRatePostWar;
    }
    
    public Integer getLearnRatePostWar() {
        return this.learnRatePostWar;
    }
    
    public Integer getWanShouLearnRatePostWar() {
        return this.wanShouLearnRatePostWar;
    }
    
    public Integer getInitiative() {
        return this.initiative;
    }
    
    public List<Integer> getTargetType() {
        return this.targetType;
    }
    
    public String getTriggerType() {
        return this.triggerType;
    }
    
    public String getPassiveTriggerSkill() {
        return this.passiveTriggerSkill;
    }
    
    public int getIsPvp() {
        return this.isPvp;
    }
    
    public String getCoverSkillSerials() {
        return this.coverSkillSerials;
    }
    
    public String getClashSkillSerials() {
        return this.clashSkillSerials;
    }
    
    public String getTogetherTriggerSkillSerials() {
        return this.togetherTriggerSkillSerials;
    }
    
    public String getAstrictTypeJson() {
        return this.astrictTypeJson;
    }
    
    public double[] getPower() {
        return this.power;
    }
    
    public int getExecutionSequence() {
        return this.executionSequence;
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
    
    public void setIcon(final Integer icon) {
        this.icon = icon;
    }
    
    public void setAttr(final Integer attr) {
        this.attr = attr;
    }
    
    public void setRarity(final Integer rarity) {
        this.rarity = rarity;
    }
    
    public void setHxyLearnRatePostWar(final Integer hxyLearnRatePostWar) {
        this.hxyLearnRatePostWar = hxyLearnRatePostWar;
    }
    
    public void setLearnRatePostWar(final Integer learnRatePostWar) {
        this.learnRatePostWar = learnRatePostWar;
    }
    
    public void setWanShouLearnRatePostWar(final Integer wanShouLearnRatePostWar) {
        this.wanShouLearnRatePostWar = wanShouLearnRatePostWar;
    }
    
    public void setInitiative(final Integer initiative) {
        this.initiative = initiative;
    }
    
    public void setTargetType(final List<Integer> targetType) {
        this.targetType = targetType;
    }
    
    public void setTriggerType(final String triggerType) {
        this.triggerType = triggerType;
    }
    
    public void setPassiveTriggerSkill(final String passiveTriggerSkill) {
        this.passiveTriggerSkill = passiveTriggerSkill;
    }
    
    public void setIsPvp(final int isPvp) {
        this.isPvp = isPvp;
    }
    
    public void setCoverSkillSerials(final String coverSkillSerials) {
        this.coverSkillSerials = coverSkillSerials;
    }
    
    public void setClashSkillSerials(final String clashSkillSerials) {
        this.clashSkillSerials = clashSkillSerials;
    }
    
    public void setTogetherTriggerSkillSerials(final String togetherTriggerSkillSerials) {
        this.togetherTriggerSkillSerials = togetherTriggerSkillSerials;
    }
    
    public void setAstrictTypeJson(final String astrictTypeJson) {
        this.astrictTypeJson = astrictTypeJson;
    }
    
    public void setPower(final double[] power) {
        this.power = power;
    }
    
    public void setExecutionSequence(final int executionSequence) {
        this.executionSequence = executionSequence;
    }
    
    public void setAstrictTypeMap(final Map<String, Double> astrictTypeMap) {
        this.astrictTypeMap = astrictTypeMap;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysPetSkill)) {
            return false;
        }
        SysPetSkill other = (SysPetSkill)o;
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
        Object this$icon = this.getIcon();
        Object other$icon = other.getIcon();
        if (!((this$icon != null) ? this$icon.equals(other$icon) : (other$icon == null))) {
            return false;
        }
        Object this$attr = this.getAttr();
        Object other$attr = other.getAttr();
        if (!((this$attr != null) ? this$attr.equals(other$attr) : (other$attr == null))) {
            return false;
        }
        Object this$rarity = this.getRarity();
        Object other$rarity = other.getRarity();
        if (!((this$rarity != null) ? this$rarity.equals(other$rarity) : (other$rarity == null))) {
            return false;
        }
        Object this$hxyLearnRatePostWar = this.getHxyLearnRatePostWar();
        Object other$hxyLearnRatePostWar = other.getHxyLearnRatePostWar();
        if (!((this$hxyLearnRatePostWar != null) ? this$hxyLearnRatePostWar.equals(other$hxyLearnRatePostWar) : (other$hxyLearnRatePostWar == null))) {
            return false;
        }
        Object this$learnRatePostWar = this.getLearnRatePostWar();
        Object other$learnRatePostWar = other.getLearnRatePostWar();
        if (!((this$learnRatePostWar != null) ? this$learnRatePostWar.equals(other$learnRatePostWar) : (other$learnRatePostWar == null))) {
            return false;
        }
        Object this$wanShouLearnRatePostWar = this.getWanShouLearnRatePostWar();
        Object other$wanShouLearnRatePostWar = other.getWanShouLearnRatePostWar();
        if (!((this$wanShouLearnRatePostWar != null) ? this$wanShouLearnRatePostWar.equals(other$wanShouLearnRatePostWar) : (other$wanShouLearnRatePostWar == null))) {
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
        Object this$triggerType = this.getTriggerType();
        Object other$triggerType = other.getTriggerType();
        if (!((this$triggerType != null) ? this$triggerType.equals(other$triggerType) : (other$triggerType == null))) {
            return false;
        }
        Object this$passiveTriggerSkill = this.getPassiveTriggerSkill();
        Object other$passiveTriggerSkill = other.getPassiveTriggerSkill();
        if (!((this$passiveTriggerSkill != null) ? this$passiveTriggerSkill.equals(other$passiveTriggerSkill) : (other$passiveTriggerSkill == null))) {
            return false;
        }
        if (this.getIsPvp() != other.getIsPvp()) {
            return false;
        }
        Object this$coverSkillSerials = this.getCoverSkillSerials();
        Object other$coverSkillSerials = other.getCoverSkillSerials();
        if (!((this$coverSkillSerials != null) ? this$coverSkillSerials.equals(other$coverSkillSerials) : (other$coverSkillSerials == null))) {
            return false;
        }
        Object this$clashSkillSerials = this.getClashSkillSerials();
        Object other$clashSkillSerials = other.getClashSkillSerials();
        if (!((this$clashSkillSerials != null) ? this$clashSkillSerials.equals(other$clashSkillSerials) : (other$clashSkillSerials == null))) {
            return false;
        }
        Object this$togetherTriggerSkillSerials = this.getTogetherTriggerSkillSerials();
        Object other$togetherTriggerSkillSerials = other.getTogetherTriggerSkillSerials();
        if (!((this$togetherTriggerSkillSerials != null) ? this$togetherTriggerSkillSerials.equals(other$togetherTriggerSkillSerials) : (other$togetherTriggerSkillSerials == null))) {
            return false;
        }
        Object this$astrictTypeJson = this.getAstrictTypeJson();
        Object other$astrictTypeJson = other.getAstrictTypeJson();
        if (!((this$astrictTypeJson != null) ? this$astrictTypeJson.equals(other$astrictTypeJson) : (other$astrictTypeJson == null))) {
            return false;
        }
        if (!Arrays.equals(this.getPower(), other.getPower())) {
            return false;
        }
        if (this.getExecutionSequence() != other.getExecutionSequence()) {
            return false;
        }
        Object this$astrictTypeMap = this.getAstrictTypeMap();
        Object other$astrictTypeMap = other.getAstrictTypeMap();
        return (this$astrictTypeMap != null) ? this$astrictTypeMap.equals(other$astrictTypeMap) : (other$astrictTypeMap == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysPetSkill;
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
        Object $icon = this.getIcon();
        result = result * 59 + (($icon == null) ? 43 : $icon.hashCode());
        Object $attr = this.getAttr();
        result = result * 59 + (($attr == null) ? 43 : $attr.hashCode());
        Object $rarity = this.getRarity();
        result = result * 59 + (($rarity == null) ? 43 : $rarity.hashCode());
        Object $hxyLearnRatePostWar = this.getHxyLearnRatePostWar();
        result = result * 59 + (($hxyLearnRatePostWar == null) ? 43 : $hxyLearnRatePostWar.hashCode());
        Object $learnRatePostWar = this.getLearnRatePostWar();
        result = result * 59 + (($learnRatePostWar == null) ? 43 : $learnRatePostWar.hashCode());
        Object $wanShouLearnRatePostWar = this.getWanShouLearnRatePostWar();
        result = result * 59 + (($wanShouLearnRatePostWar == null) ? 43 : $wanShouLearnRatePostWar.hashCode());
        Object $initiative = this.getInitiative();
        result = result * 59 + (($initiative == null) ? 43 : $initiative.hashCode());
        Object $targetType = this.getTargetType();
        result = result * 59 + (($targetType == null) ? 43 : $targetType.hashCode());
        Object $triggerType = this.getTriggerType();
        result = result * 59 + (($triggerType == null) ? 43 : $triggerType.hashCode());
        Object $passiveTriggerSkill = this.getPassiveTriggerSkill();
        result = result * 59 + (($passiveTriggerSkill == null) ? 43 : $passiveTriggerSkill.hashCode());
        result = result * 59 + this.getIsPvp();
        Object $coverSkillSerials = this.getCoverSkillSerials();
        result = result * 59 + (($coverSkillSerials == null) ? 43 : $coverSkillSerials.hashCode());
        Object $clashSkillSerials = this.getClashSkillSerials();
        result = result * 59 + (($clashSkillSerials == null) ? 43 : $clashSkillSerials.hashCode());
        Object $togetherTriggerSkillSerials = this.getTogetherTriggerSkillSerials();
        result = result * 59 + (($togetherTriggerSkillSerials == null) ? 43 : $togetherTriggerSkillSerials.hashCode());
        Object $astrictTypeJson = this.getAstrictTypeJson();
        result = result * 59 + (($astrictTypeJson == null) ? 43 : $astrictTypeJson.hashCode());
        result = result * 59 + Arrays.hashCode(this.getPower());
        result = result * 59 + this.getExecutionSequence();
        Object $astrictTypeMap = this.getAstrictTypeMap();
        result = result * 59 + (($astrictTypeMap == null) ? 43 : $astrictTypeMap.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysPetSkill(id=" + this.getId() + ", serial=" + this.getSerial() + ", name=" + this.getName() + ", icon=" + this.getIcon() + ", attr=" + this.getAttr() + ", rarity=" + this.getRarity() + ", hxyLearnRatePostWar=" + this.getHxyLearnRatePostWar() + ", learnRatePostWar=" + this.getLearnRatePostWar() + ", wanShouLearnRatePostWar=" + this.getWanShouLearnRatePostWar() + ", initiative=" + this.getInitiative() + ", targetType=" + this.getTargetType() + ", triggerType=" + this.getTriggerType() + ", passiveTriggerSkill=" + this.getPassiveTriggerSkill() + ", isPvp=" + this.getIsPvp() + ", coverSkillSerials=" + this.getCoverSkillSerials() + ", clashSkillSerials=" + this.getClashSkillSerials() + ", togetherTriggerSkillSerials=" + this.getTogetherTriggerSkillSerials() + ", astrictTypeJson=" + this.getAstrictTypeJson() + ", power=" + Arrays.toString(this.getPower()) + ", executionSequence=" + this.getExecutionSequence() + ", astrictTypeMap=" + this.getAstrictTypeMap() + ")";
    }
}
