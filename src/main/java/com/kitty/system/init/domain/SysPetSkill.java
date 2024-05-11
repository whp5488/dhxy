
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
        this.astrictTypeMap = new HashMap<String, Double>();
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
        final SysPetSkill other = (SysPetSkill)o;
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
        final Object this$icon = this.getIcon();
        final Object other$icon = other.getIcon();
        Label_0176: {
            if (this$icon == null) {
                if (other$icon == null) {
                    break Label_0176;
                }
            }
            else if (this$icon.equals(other$icon)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$attr = this.getAttr();
        final Object other$attr = other.getAttr();
        Label_0213: {
            if (this$attr == null) {
                if (other$attr == null) {
                    break Label_0213;
                }
            }
            else if (this$attr.equals(other$attr)) {
                break Label_0213;
            }
            return false;
        }
        final Object this$rarity = this.getRarity();
        final Object other$rarity = other.getRarity();
        Label_0250: {
            if (this$rarity == null) {
                if (other$rarity == null) {
                    break Label_0250;
                }
            }
            else if (this$rarity.equals(other$rarity)) {
                break Label_0250;
            }
            return false;
        }
        final Object this$hxyLearnRatePostWar = this.getHxyLearnRatePostWar();
        final Object other$hxyLearnRatePostWar = other.getHxyLearnRatePostWar();
        Label_0287: {
            if (this$hxyLearnRatePostWar == null) {
                if (other$hxyLearnRatePostWar == null) {
                    break Label_0287;
                }
            }
            else if (this$hxyLearnRatePostWar.equals(other$hxyLearnRatePostWar)) {
                break Label_0287;
            }
            return false;
        }
        final Object this$learnRatePostWar = this.getLearnRatePostWar();
        final Object other$learnRatePostWar = other.getLearnRatePostWar();
        Label_0324: {
            if (this$learnRatePostWar == null) {
                if (other$learnRatePostWar == null) {
                    break Label_0324;
                }
            }
            else if (this$learnRatePostWar.equals(other$learnRatePostWar)) {
                break Label_0324;
            }
            return false;
        }
        final Object this$wanShouLearnRatePostWar = this.getWanShouLearnRatePostWar();
        final Object other$wanShouLearnRatePostWar = other.getWanShouLearnRatePostWar();
        Label_0361: {
            if (this$wanShouLearnRatePostWar == null) {
                if (other$wanShouLearnRatePostWar == null) {
                    break Label_0361;
                }
            }
            else if (this$wanShouLearnRatePostWar.equals(other$wanShouLearnRatePostWar)) {
                break Label_0361;
            }
            return false;
        }
        final Object this$initiative = this.getInitiative();
        final Object other$initiative = other.getInitiative();
        Label_0398: {
            if (this$initiative == null) {
                if (other$initiative == null) {
                    break Label_0398;
                }
            }
            else if (this$initiative.equals(other$initiative)) {
                break Label_0398;
            }
            return false;
        }
        final Object this$targetType = this.getTargetType();
        final Object other$targetType = other.getTargetType();
        Label_0435: {
            if (this$targetType == null) {
                if (other$targetType == null) {
                    break Label_0435;
                }
            }
            else if (this$targetType.equals(other$targetType)) {
                break Label_0435;
            }
            return false;
        }
        final Object this$triggerType = this.getTriggerType();
        final Object other$triggerType = other.getTriggerType();
        Label_0472: {
            if (this$triggerType == null) {
                if (other$triggerType == null) {
                    break Label_0472;
                }
            }
            else if (this$triggerType.equals(other$triggerType)) {
                break Label_0472;
            }
            return false;
        }
        final Object this$passiveTriggerSkill = this.getPassiveTriggerSkill();
        final Object other$passiveTriggerSkill = other.getPassiveTriggerSkill();
        Label_0509: {
            if (this$passiveTriggerSkill == null) {
                if (other$passiveTriggerSkill == null) {
                    break Label_0509;
                }
            }
            else if (this$passiveTriggerSkill.equals(other$passiveTriggerSkill)) {
                break Label_0509;
            }
            return false;
        }
        if (this.getIsPvp() != other.getIsPvp()) {
            return false;
        }
        final Object this$coverSkillSerials = this.getCoverSkillSerials();
        final Object other$coverSkillSerials = other.getCoverSkillSerials();
        Label_0559: {
            if (this$coverSkillSerials == null) {
                if (other$coverSkillSerials == null) {
                    break Label_0559;
                }
            }
            else if (this$coverSkillSerials.equals(other$coverSkillSerials)) {
                break Label_0559;
            }
            return false;
        }
        final Object this$clashSkillSerials = this.getClashSkillSerials();
        final Object other$clashSkillSerials = other.getClashSkillSerials();
        Label_0596: {
            if (this$clashSkillSerials == null) {
                if (other$clashSkillSerials == null) {
                    break Label_0596;
                }
            }
            else if (this$clashSkillSerials.equals(other$clashSkillSerials)) {
                break Label_0596;
            }
            return false;
        }
        final Object this$togetherTriggerSkillSerials = this.getTogetherTriggerSkillSerials();
        final Object other$togetherTriggerSkillSerials = other.getTogetherTriggerSkillSerials();
        Label_0633: {
            if (this$togetherTriggerSkillSerials == null) {
                if (other$togetherTriggerSkillSerials == null) {
                    break Label_0633;
                }
            }
            else if (this$togetherTriggerSkillSerials.equals(other$togetherTriggerSkillSerials)) {
                break Label_0633;
            }
            return false;
        }
        final Object this$astrictTypeJson = this.getAstrictTypeJson();
        final Object other$astrictTypeJson = other.getAstrictTypeJson();
        Label_0670: {
            if (this$astrictTypeJson == null) {
                if (other$astrictTypeJson == null) {
                    break Label_0670;
                }
            }
            else if (this$astrictTypeJson.equals(other$astrictTypeJson)) {
                break Label_0670;
            }
            return false;
        }
        if (!Arrays.equals(this.getPower(), other.getPower())) {
            return false;
        }
        if (this.getExecutionSequence() != other.getExecutionSequence()) {
            return false;
        }
        final Object this$astrictTypeMap = this.getAstrictTypeMap();
        final Object other$astrictTypeMap = other.getAstrictTypeMap();
        if (this$astrictTypeMap == null) {
            if (other$astrictTypeMap == null) {
                return true;
            }
        }
        else if (this$astrictTypeMap.equals(other$astrictTypeMap)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysPetSkill;
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
        final Object $icon = this.getIcon();
        result = result * 59 + (($icon == null) ? 43 : $icon.hashCode());
        final Object $attr = this.getAttr();
        result = result * 59 + (($attr == null) ? 43 : $attr.hashCode());
        final Object $rarity = this.getRarity();
        result = result * 59 + (($rarity == null) ? 43 : $rarity.hashCode());
        final Object $hxyLearnRatePostWar = this.getHxyLearnRatePostWar();
        result = result * 59 + (($hxyLearnRatePostWar == null) ? 43 : $hxyLearnRatePostWar.hashCode());
        final Object $learnRatePostWar = this.getLearnRatePostWar();
        result = result * 59 + (($learnRatePostWar == null) ? 43 : $learnRatePostWar.hashCode());
        final Object $wanShouLearnRatePostWar = this.getWanShouLearnRatePostWar();
        result = result * 59 + (($wanShouLearnRatePostWar == null) ? 43 : $wanShouLearnRatePostWar.hashCode());
        final Object $initiative = this.getInitiative();
        result = result * 59 + (($initiative == null) ? 43 : $initiative.hashCode());
        final Object $targetType = this.getTargetType();
        result = result * 59 + (($targetType == null) ? 43 : $targetType.hashCode());
        final Object $triggerType = this.getTriggerType();
        result = result * 59 + (($triggerType == null) ? 43 : $triggerType.hashCode());
        final Object $passiveTriggerSkill = this.getPassiveTriggerSkill();
        result = result * 59 + (($passiveTriggerSkill == null) ? 43 : $passiveTriggerSkill.hashCode());
        result = result * 59 + this.getIsPvp();
        final Object $coverSkillSerials = this.getCoverSkillSerials();
        result = result * 59 + (($coverSkillSerials == null) ? 43 : $coverSkillSerials.hashCode());
        final Object $clashSkillSerials = this.getClashSkillSerials();
        result = result * 59 + (($clashSkillSerials == null) ? 43 : $clashSkillSerials.hashCode());
        final Object $togetherTriggerSkillSerials = this.getTogetherTriggerSkillSerials();
        result = result * 59 + (($togetherTriggerSkillSerials == null) ? 43 : $togetherTriggerSkillSerials.hashCode());
        final Object $astrictTypeJson = this.getAstrictTypeJson();
        result = result * 59 + (($astrictTypeJson == null) ? 43 : $astrictTypeJson.hashCode());
        result = result * 59 + Arrays.hashCode(this.getPower());
        result = result * 59 + this.getExecutionSequence();
        final Object $astrictTypeMap = this.getAstrictTypeMap();
        result = result * 59 + (($astrictTypeMap == null) ? 43 : $astrictTypeMap.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysPetSkill(id=" + this.getId() + ", serial=" + this.getSerial() + ", name=" + this.getName() + ", icon=" + this.getIcon() + ", attr=" + this.getAttr() + ", rarity=" + this.getRarity() + ", hxyLearnRatePostWar=" + this.getHxyLearnRatePostWar() + ", learnRatePostWar=" + this.getLearnRatePostWar() + ", wanShouLearnRatePostWar=" + this.getWanShouLearnRatePostWar() + ", initiative=" + this.getInitiative() + ", targetType=" + this.getTargetType() + ", triggerType=" + this.getTriggerType() + ", passiveTriggerSkill=" + this.getPassiveTriggerSkill() + ", isPvp=" + this.getIsPvp() + ", coverSkillSerials=" + this.getCoverSkillSerials() + ", clashSkillSerials=" + this.getClashSkillSerials() + ", togetherTriggerSkillSerials=" + this.getTogetherTriggerSkillSerials() + ", astrictTypeJson=" + this.getAstrictTypeJson() + ", power=" + Arrays.toString(this.getPower()) + ", executionSequence=" + this.getExecutionSequence() + ", astrictTypeMap=" + this.getAstrictTypeMap() + ")";
    }
}
