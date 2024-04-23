package com.kitty.system.init.domain;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_drug")
public class SysDrug implements Serializable {
  @Comment("编号")
  @Column("serial")
  private Integer serial;
  
  @Comment("名称")
  @Column("name")
  private String name;
  
  @Comment("药品回复的气血值")
  @Column("drug_add_hp_value")
  private Integer drugAddHpValue;
  
  @Comment("药品回复的气血百分比")
  @Column("drug_add_hp_percent")
  private Integer drugAddHpPercent;
  
  @Comment("药品储备的气血值")
  @Column("drug_add_hp_stored")
  private Integer drugAddHpStored;
  
  @Comment("药品回复的法力值")
  @Column("drug_add_mp_value")
  private Integer drugAddMpValue;
  
  @Comment("药品回复的法力百分比")
  @Column("drug_add_mp_percent")
  private Integer drugAddMpPercent;
  
  @Comment("药品储备的法力值")
  @Column("drug_add_mp_stored")
  private Integer drugAddMpStored;
  
  @Comment("物品外观")
  @Column("item_shape")
  private Integer itemShape;
  
  @Comment("价格")
  @Column("price")
  private Integer price;
  
  @Comment("交易货币：1：仙玉，2：银两，3：师贡")
  @Column("reprice")
  private Integer reprice;
  
  @Comment("重叠数量")
  @Column("can_merge")
  private Integer canMerge;
  
  @Comment("品阶")
  @Column("item_pj")
  private Integer itemPj;
  
  @Comment("1：血药，2：蓝药，3：血蓝双药，4：特效道具")
  @Column("show_class")
  private Integer showClass;
  
  @Comment("目标类型")
  @Column("target_type")
  private Integer targetType;
  
  @Comment("使用场景")
  @Column("use_type")
  private Integer useType;
  
  @Comment("状态编号")
  @Column("status")
  private List<Integer> status;
}
