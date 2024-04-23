package com.kitty.system.init.domain;

import com.kitty.common.utils.JsonUtils;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("s_pet_skill")
public class SysPetSkill implements Serializable {
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
  
  private Map<String, Double> astrictTypeMap = new HashMap<>();
  
  public Map<String, Double> getAstrictTypeMap() {
    if (this.astrictTypeJson != null && !"".equals(this.astrictTypeJson))
      this.astrictTypeMap = JsonUtils.string2Map(getAstrictTypeJson(), String.class, Double.class); 
    return this.astrictTypeMap;
  }
}
