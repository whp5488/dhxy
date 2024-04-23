package com.kitty.system.init.domain;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Getter
@Setter
@Table("horse_skill")
public class HorseSkill {
  @Comment("技能编号")
  @Column("serial")
  private int serial;
  
  @Comment("位置一权重")
  @Column("seat_weight_one")
  private double seatWeightOne;
  
  @Comment("位置二权重")
  @Column("seat_weight_tow")
  private double seatWeightTow;
  
  @Comment("位置三权重")
  @Column("seat_weight_three")
  private double seatWeightThree;
  
  @Comment("品阶")
  @Column("rarity")
  private byte rarity;
  
  @Comment("类型：0.普通 1.高级")
  @Column("type")
  private byte type;
  
  @Comment("归属坐骑：1.一座 2.二座 3.三座 4.四座")
  @Column("seat")
  private byte seat;
  
  @Comment("触发类型")
  @Column("trigger_type")
  private String triggerType;
  
  @Comment("是否初始化加载，0=否，1=是")
  @Column("is_fight_initialize_load")
  private boolean isFightInitializeLoad;
}
