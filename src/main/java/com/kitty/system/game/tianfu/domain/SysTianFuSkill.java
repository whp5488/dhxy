package com.kitty.system.game.tianfu.domain;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_tian_fu_skill")
public class SysTianFuSkill implements Serializable {
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
  
  @ConstructorProperties({"id", "serial", "category", "categoryDesc", "name", "layer", "race", "skill", "tag", "target", "icon", "skillSeqId", "effs", "targetType", "ceSerial", "pointWeight", "rarity", "rarityJudge", "toIdentifyCeSerial", "baoDiWeight", "opportunity", "isEffects"})
  public SysTianFuSkill(int id, int serial, byte category, String categoryDesc, String name, byte layer, List<Byte> race, List<Integer> skill, String tag, byte target, int icon, int skillSeqId, List<Integer> effs, List<Integer> targetType, int ceSerial, List<Short> pointWeight, byte rarity, List<Byte> rarityJudge, int toIdentifyCeSerial, short baoDiWeight, String opportunity, boolean isEffects) {
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
  
  public SysTianFuSkill() {}
}
